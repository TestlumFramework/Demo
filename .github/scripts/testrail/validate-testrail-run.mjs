#!/usr/bin/env node
/**
 * Check that a regression execution actually reported into TestRail.
 *
 * Cases are identified by id, taken from the same variables the scenarios read.
 *
 * Against expected-run.json it checks:
 * - one result per expected case, and no more
 * - every expected case present as a test in the run
 * - every test in the expected status
 */

import { readFileSync } from "node:fs";
import { dirname, join } from "node:path";
import { fileURLToPath } from "node:url";
import { createClient, collect, fail, required } from "./testrail-client.mjs";

const STATUS_NAMES = { 1: "passed", 2: "blocked", 3: "untested", 4: "retest", 5: "failed" };
const label = (statusId) => `${STATUS_NAMES[statusId] ?? "unknown"} (${statusId})`;

function loadExpected() {
  const path =
    process.env.EXPECTED_FILE || join(dirname(fileURLToPath(import.meta.url)), "expected-run.json");
  try {
    return JSON.parse(readFileSync(path, "utf8"));
  } catch (error) {
    fail(`Could not read the expectation file ${path}: ${error.message}`);
  }
}

function validateExpected(expectedTests, problems, expectedCaseIds, byCaseId) {
  for (const expectedResult of expectedTests) {
    const caseId = process.env[expectedResult.case_id_env];
    if (!caseId) {
      problems.push(`${expectedResult.case_id_env} is not set, so '${expectedResult.title}' cannot be checked`);
      continue;
    }
    expectedCaseIds.add(String(caseId));

    const test = byCaseId.get(String(caseId));
    if (!test) {
      problems.push(`no test for case ${caseId} ('${expectedResult.title}') in the run`);
      continue;
    }
    if (test.status_id !== expectedResult.status_id) {
      problems.push(
          `'${expectedResult.title}' (case ${caseId}) is ${label(test.status_id)}, ` +
          `expected ${label(expectedResult.status_id)}`
      );
      continue;
    }
    console.log(`  ✓ ${expectedResult.title.padEnd(34)} ${label(test.status_id)}`);
  }
}

async function main() {
  const client = createClient(
    required("TESTRAIL_URL"),
    required("TESTRAIL_USERNAME"),
    required("TESTRAIL_API_KEY")
  );
  const runId = required("TESTRAIL_RUN_ID");

  const expected = loadExpected();
  const expectedTests = expected.tests ?? [];
  if (expectedTests.length === 0) {
    fail("expected-run.json lists no tests");
  }

  console.log(`Validating run ${runId}`);

  const tests = await collect(client, `get_tests/${runId}`, "tests");
  const results = await collect(client, `get_results_for_run/${runId}`, "results");

  const byCaseId = new Map(tests.filter((test) => test.case_id).map((t) => [String(t.case_id), t]));

  const problems = [];
  const expectedCaseIds = new Set();

  validateExpected(expectedTests, problems, expectedCaseIds, byCaseId);

  if (results.length < expectedTests.length) {
    problems.push(`run holds ${results.length} result(s), expected at least ${expectedTests.length}`);
  }

  const unexpected = [...byCaseId.keys()].filter((id) => !expectedCaseIds.has(id));
  if (unexpected.length > 0) {
    problems.push(`unexpected case(s) in the run: ${unexpected.join(", ")}`);
  }

  if (problems.length > 0) {
    fail(`Run ${runId} does not match expectations:\n  - ${problems.join("\n  - ")}`);
  }

  console.log(`Run ${runId} matches: ${results.length} result(s) across ${tests.length} test(s)`);
}

main().catch((error) => fail(error.message));
