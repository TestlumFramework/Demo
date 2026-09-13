#!/usr/bin/env node
/**
 * Make sure the TestRail resources a regression needs are in place.
 *
 * Ensures one project, one suite, one section and the CASES below. If the resources
 * exist, they are not recreated, so repeated pipelines reuse the same resources
 * instead of piling up duplicates. New TestRail runs are not getting created in this job
 * since each parallel pipeline has its own run id.
 *
 * Each automation_id has to match the caseMatchKeyValue of the scenario.
 *
 * Isolation between pipelines comes from the runs, not from the project: each
 * execution reports into its own run, and the cleanup job deletes those runs
 * afterwards without deleting the project, suite, section and cases.
 */

import { appendFileSync } from "node:fs";
import { createClient, collect, fail, required } from "./testrail-client.mjs";

const SECTION_NAME = "Regression";

const CASES = [
  { env: "TESTRAIL_CONDITION_CASE_ID", title: "Condition - Self Test" },
  { env: "TESTRAIL_HTTP_CASE_ID", title: "HTTP POST" },
  { env: "TESTRAIL_DB_CASE_ID", title: "PostgreSQL CRUD Operations Test" },
];

async function ensureProject(client, name, runUrl) {
  for (const project of await collect(client, "get_projects", "projects")) {
    if (project.name === name) {
      console.log(`Reusing project ${project.id} (${name})`);
      return project.id;
    }
  }

  const project = await client.post("add_project", {
    name,
    announcement: `Created by ${runUrl}`,
    show_announcement: true,
    suite_mode: 1,
  });
  console.log(`Created project ${project.id} (${name})`);
  return project.id;
}

async function ensureSuite(client, projectId) {
  const response = await client.get(`get_suites/${projectId}`);
  const suites = Array.isArray(response) ? response : response?.suites ?? [];

  if (suites.length === 0 || !suites[0]?.id) {
    fail(`Project ${projectId} has no suite to attach the section to. ` +
        `get_suites returned: ${JSON.stringify(response)}`);
  }
  console.log(`Using suite ${suites[0].id} of project ${projectId}`);
  return suites[0].id;
}

async function ensureSection(client, projectId, suiteId) {
  for (const section of await collect(client, `get_sections/${projectId}`, "sections")) {
    if (section.name === SECTION_NAME) {
      console.log(`Reusing section ${section.id} in suite ${suiteId}`);
      return section.id;
    }
  }
  const section = await client.post(`add_section/${projectId}`, {
    suite_id: suiteId,
    name: SECTION_NAME,
  });
  console.log(`Created section ${section.id} in suite ${suiteId}`);
  return section.id;
}

async function ensureCases(client, projectId, sectionId, cases) {
  const existing = new Map();
  for (const entry of await collect(
    client,
    `get_cases/${projectId}&section_id=${sectionId}`,
    "cases"
  )) {
    if (entry.title && !existing.has(entry.title)) {
      existing.set(entry.title, entry.id);
    }
  }

  for (const testCase of cases) {
    const existingCase = existing.get(testCase.title);
    if (existingCase) {
      testCase.case_id = existingCase;
      console.log(`Reusing case ${existingCase} for ${testCase.title}`);
      continue;
    }
    const createdCase = await client.post(`add_case/${sectionId}`, { title: testCase.title });
    if (!createdCase?.id) {
      fail(`add_case/${sectionId} returned no id for "${testCase.title}"`);
    }
    testCase.case_id = createdCase.id;
    console.log(`Created case ${createdCase.id} for ${testCase.title}`);
  }
}

function setJobOutputs(projectId, suiteId, sectionId, cases) {
  appendFileSync(
      process.env.GITHUB_OUTPUT,
      `project_id=${projectId}\n` +
      `suite_id=${suiteId}\n` +
      `section_id=${sectionId}\n` +
      `case_ids=${JSON.stringify(cases.map((c) => c.case_id))}\n` +
      cases.map((c) => `${c.env.toLowerCase()}=${c.case_id}\n`).join("")
  );
}

function setEnvironment(cases) {
  appendFileSync(process.env.GITHUB_ENV, cases.map((c) => `${c.env}=${c.case_id}\n`).join(""));
}

function printJobSummary(projectId, projectName, cases) {
  let summary = "### TestRail setup\n\n";
  summary += `Project \`${projectId}\` — ${projectName}\n\n`;
  summary += "| Case | Variable | Title |\n|---|---|---|\n";
  for (const testCase of cases) {
    summary += `| ${testCase.case_id} | \`${testCase.env}\` | ${testCase.title} |\n`;
  }
  appendFileSync(process.env.GITHUB_STEP_SUMMARY, summary);
}

async function main() {
  const client = createClient(
    required("TESTRAIL_URL"),
    required("TESTRAIL_USERNAME"),
    required("TESTRAIL_API_KEY")
  );
  const projectName = required("PROJECT_NAME");

  const cases = CASES.map((testCase) => ({ ...testCase }));
  console.log(`Available cases:`);
  for (const testCase of cases) {
    console.log(`  ${testCase.env.padEnd(28)} ${testCase.title}`);
  }

  const runUrl = process.env.RUN_URL || "a local run";
  const projectId = await ensureProject(client, projectName, runUrl);
  const suiteId = await ensureSuite(client, projectId);
  const sectionId = await ensureSection(client, projectId, suiteId);
  await ensureCases(client, projectId, sectionId, cases);

  if (process.env.GITHUB_OUTPUT) {
    setJobOutputs(projectId, suiteId, sectionId, cases);
  }
  if (process.env.GITHUB_ENV) {
    setEnvironment(cases);
  }
  if (process.env.GITHUB_STEP_SUMMARY) {
    printJobSummary(projectId, projectName, cases);
  }
}

main().catch((error) => fail(error.message));
