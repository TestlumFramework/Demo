#!/usr/bin/env node
/**
 * Delete every test run in the regression project once the pipeline is done.
 *
 * Deleting a run removes the tests and results it holds, which is all the
 * cleanup the next execution needs. The project, suite, section and cases are
 * left in place.
 */

import { createClient, collect, fail, required } from "./testrail-client.mjs";

async function outputSummary(summary, runs, failures, projectId) {
  const {appendFileSync} = await import("node:fs");
  appendFileSync(
      summary,
      `### TestRail cleanup\n\nDeleted ${runs.length - failures.length} of ${runs.length} run(s) ` +
      `from project \`${projectId}\`. Project, suite, section and cases were kept.\n\n`
  );
}

async function main() {
  const client = createClient(
    required("TESTRAIL_URL"),
    required("TESTRAIL_USERNAME"),
    required("TESTRAIL_API_KEY")
  );
  const projectId = required("TESTRAIL_PROJECT_ID");

  const runs = await collect(client, `get_runs/${projectId}`, "runs");
  if (runs.length === 0) {
    console.log(`Project ${projectId} has no runs to delete`);
    return;
  }

  console.log(`Deleting ${runs.length} run(s) from project ${projectId}`);
  const failures = [];
  for (const run of runs) {
    try {
      await client.post(`delete_run/${run.id}`);
      console.log(`  deleted run ${run.id} (${run.name})`);
    } catch (error) {
      failures.push(`run ${run.id}: ${error.message}`);
    }
  }

  const summary = process.env.GITHUB_STEP_SUMMARY;
  if (summary) {
    await outputSummary(summary, runs, failures, projectId);
  }

  if (failures.length > 0) {
    fail(`Could not delete ${failures.length} run(s):\n${failures.join("\n")}`);
  }
}

main().catch((error) => fail(error.message));
