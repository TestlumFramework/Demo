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

const CASE_FIELD = "custom_automation_id";
const SECTION_NAME = "Regression";

const CASES = [
  { automation_id: "web_drop_down_case", title: "DropDown - AllValues" },
  { automation_id: "http_case", title: "HTTP POST" },
  { automation_id: "db_case", title: "PostgreSQL CRUD Operations Test" },
];

async function ensureCaseField(client) {
  const has = async () =>
    (await client.get("get_case_fields")).some((field) => field.system_name === CASE_FIELD);

  if (await has()) {
    console.log(`Case field ${CASE_FIELD} already exists`);
    return;
  }

  console.log(`Case field ${CASE_FIELD} is missing, creating it`);
  try {
    await client.post("add_case_field", {
      type: "String",
      name: "automation_id",
      label: "Automation ID",
      description: "Matches a Testlum scenario to its TestRail case.",
      include_all: true,
      configs: JSON.stringify([
        {
          context: { is_global: true, project_ids: [] },
          options: { is_required: false, default_value: "", format: "plain", rows: "0" },
        },
      ]),
    });
  } catch (error) {
    fail(`Could not create the ${CASE_FIELD} case field (${error.message}).`);
  }

  if (!(await has())) {
    fail(`${CASE_FIELD} still missing after add_case_field reported success`);
  }
}

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
  const suites = await client.get(`get_suites/${projectId}`);
  if (!suites || suites.length === 0) {
    fail(`Project ${projectId} has no suite to attach the section to`);
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
    if (entry[CASE_FIELD]) {
      existing.set(entry[CASE_FIELD], entry.id);
    }
  }

  for (const testCase of cases) {
    const existingCase = existing.get(testCase.automation_id);
    if (existingCase) {
      testCase.case_id = existingCase;
      console.log(`Reusing case ${existingCase} for ${testCase.automation_id}`);
      continue;
    }
    const createdCase = await client.post(`add_case/${sectionId}`, {
      title: testCase.title,
      [CASE_FIELD]: testCase.automation_id,
    });
    testCase.case_id = createdCase.id;
    console.log(`Created case ${createdCase.id} for ${testCase.automation_id}`);
  }
}

function setJobOutputs(projectId, suiteId, sectionId, cases) {
  appendFileSync(
      process.env.GITHUB_OUTPUT,
      `project_id=${projectId}\n` +
      `suite_id=${suiteId}\n` +
      `section_id=${sectionId}\n` +
      `case_ids=${JSON.stringify(cases.map((c) => c.case_id))}\n`
  );
}

function printJobOutput(projectId, projectName, cases) {
  let summary = "### TestRail setup\n\n";
  summary += `Project \`${projectId}\` — ${projectName}\n\n`;
  summary += "| Case | Automation ID | Title |\n|---|---|---|\n";
  for (const testCase of cases) {
    summary += `| ${testCase.case_id} | \`${testCase.automation_id}\` | ${testCase.title} |\n`;
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
    console.log(`  ${testCase.automation_id.padEnd(24)} ${testCase.title}`);
  }

  await ensureCaseField(client);

  const runUrl = process.env.RUN_URL || "a local run";
  const projectId = await ensureProject(client, projectName, runUrl);
  const suiteId = await ensureSuite(client, projectId);
  const sectionId = await ensureSection(client, projectId, suiteId);
  await ensureCases(client, projectId, sectionId, cases);

  if (process.env.GITHUB_OUTPUT) {
    setJobOutputs(projectId, suiteId, sectionId, cases);
  }

  if (process.env.GITHUB_STEP_SUMMARY) {
    printJobOutput(projectId, projectName, cases);
  }
}

main().catch((error) => fail(error.message));
