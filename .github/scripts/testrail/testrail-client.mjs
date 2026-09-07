/**
 * Minimal TestRail API client shared by the workflow scripts.
 */
const MAX_ATTEMPTS = 5;

export function fail(message) {
  console.log(`::error::${message}`);
  process.exit(1);
}

export function required(name) {
  const value = process.env[name];
  if (!value) {
    fail(`${name} is not set`);
  }
  return value;
}

const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

export function createClient(url, username, apiKey) {
  const base = url.replace(/\/+$/, "");
  const token = Buffer.from(`${username}:${apiKey}`).toString("base64");

  async function call(endpoint, payload) {
    for (let attempt = 1; attempt <= MAX_ATTEMPTS; attempt += 1) {
      let response;
      try {
        response = await fetch(`${base}/${endpoint}`, {
          method: payload === undefined ? "GET" : "POST",
          headers: {
            Authorization: `Basic ${token}`,
            "Content-Type": "application/json",
          },
          body: payload === undefined ? undefined : JSON.stringify(payload),
          signal: AbortSignal.timeout(60000),
        });
      } catch (error) {
        if (attempt === MAX_ATTEMPTS) {
          throw new Error(`${endpoint} unreachable: ${error.message}`);
        }
        await sleep(attempt * 2000);
        continue;
      }

      const body = await response.text();
      if (response.ok) {
        return body ? JSON.parse(body) : {};
      }

      if (![429, 502, 503].includes(response.status) || attempt === MAX_ATTEMPTS) {
        throw new Error(`${endpoint} failed with HTTP ${response.status}: ${body}`);
      }
      const delay = Number(response.headers.get("Retry-After")) || attempt * 2;
      console.log(`${endpoint} returned ${response.status}, retrying in ${delay}s`);
      await sleep(delay * 1000);
    }
  }

  return {
    get: (endpoint) => call(endpoint),
    post: (endpoint, payload) => call(endpoint, payload ?? {}),
  };
}

export async function collect(client, endpoint, key) {
  const limit = 250;
  const items = [];

  for (let offset = 0; ; offset += limit) {
    const page = await client.get(`${endpoint}&limit=${limit}&offset=${offset}`);
    const batch = Array.isArray(page) ? page : page[key] ?? [];
    items.push(...batch);
    if (batch.length < limit) {
      return items;
    }
  }
}
