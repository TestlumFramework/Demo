#!/usr/bin/env node
/**
 * Create the TestRail run for regression execution.
 *
 * Every matrix job with enabled TestRail validation creates its own run
 * so results from different images never overwrite each other. The new
 * run id is exported as TESTRAIL_RUN_ID, which each scenario's testRailRunId reads.
 */

import { createClient, fail, required } from "./testrail-client.mjs";

function printResult(appendFileSync, runId, runName) {
  appendFileSync(
      process.env.GITHUB_STEP_SUMMARY,
      `### TestRail run\n\nRun \`${runId}\` — ${runName}\n\n`
  );
}

async function sendCreateRunRequest(runName, caseIds, suiteId, client, projectId) {
  const payload = {
    name: runName,
    description: process.env.RUN_DESCRIPTION || "",
    include_all: false,
    case_ids: caseIds,
  };
  if (suiteId) {
    payload.suite_id = Number(suiteId);
  }

  const run = await client.post(`add_run/${projectId}`, payload);
  const runId = run.id;
  if (!runId) {
    fail(`add_run/${projectId} returned no run id: ${JSON.stringify(run)}`);
  }
  console.log(`Created run ${runId} (${runName}) with ${caseIds.length} cases`);

  const {appendFileSync} = await import("node:fs");
  if (process.env.GITHUB_OUTPUT) {
    appendFileSync(process.env.GITHUB_OUTPUT, `run_id=${runId}\n`);
  }
  if (process.env.GITHUB_ENV) {
    appendFileSync(process.env.GITHUB_ENV, `TESTRAIL_RUN_ID=${runId}\n`);
  }
  if (process.env.GITHUB_STEP_SUMMARY) {
    printResult(appendFileSync, runId, runName);
  }
}

async function main() {
  const client = createClient(
    required("TESTRAIL_URL"),
    required("TESTRAIL_USERNAME"),
    required("TESTRAIL_API_KEY")
  );
  const projectId = required("TESTRAIL_PROJECT_ID");
  const suiteId = process.env.TESTRAIL_SUITE_ID;
  const runName = required("RUN_NAME");

  let caseIds = [];
  if (process.env.TESTRAIL_CASE_IDS) {
    try {
      caseIds = JSON.parse(process.env.TESTRAIL_CASE_IDS);
    } catch (error) {
      fail(`TESTRAIL_CASE_IDS is not valid JSON: ${process.env.TESTRAIL_CASE_IDS}`);
    }
  }
  if (!Array.isArray(caseIds) || caseIds.length === 0) {
    fail("TESTRAIL_CASE_IDS must be a non-empty JSON array of case ids");
  }
  await sendCreateRunRequest(runName, caseIds, suiteId, client, projectId);
}

main().catch((error) => fail(error.message));
