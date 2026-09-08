# Build guide: Music Release Agent

A minimal but genuine agent: it tracks artists you like and tells you when
they've released something new. Every class in `src/` is a skeleton —
interfaces, method signatures, and Javadoc explaining what the method needs
to do and why — but no logic. You write the logic. Import it into IntelliJ
as a Maven project and work through the milestones below roughly in order;
each one produces something you can compile and manually run/test before
moving to the next, rather than writing 12 files blind and debugging them
all at once.

Ask me (Claude) for a concept explanation, a design-decision sanity check,
or a review of code you've written at any point — that's the intended way
to use this alongside the guide.

## Milestone 0 — Setup

- Open the project folder in IntelliJ (`File > Open`, point at the folder
  with `pom.xml`). Let it import as a Maven project.
- Confirm the project SDK is Java 21 (`File > Project Structure`).
- Build once. It **will succeed** — Maven only compiles the code at this
  stage, it doesn't run any of it, and a method whose body unconditionally
  throws `UnsupportedOperationException` still compiles fine. A clean build
  here just confirms the project structure and the `org.json` dependency
  resolved correctly, not that anything works yet. You'll only actually see
  one of those exceptions fire once something calls a stub method at
  runtime — which won't happen until you're further into wiring things up.

## Milestone 1 — `ToolResult` (tools package)

The smallest file in the project. A two-field value holder (`ok`, `text`)
with static factories `ok(...)`/`error(...)`. Good warm-up.

## Milestone 2 — MusicBrainz client (musicbrainz package)

Implement `ArtistMatch`, `ReleaseGroup` (plain data classes), then
`MusicBrainzClient`. The Javadoc on `MusicBrainzClient` has the exact
endpoint URLs and JSON response shapes so you don't need to re-find the
MusicBrainz docs. Use `java.net.http.HttpClient` (built into the JDK) and
the `org.json` library already in `pom.xml` for parsing.

**Test it standalone** before moving on — write a throwaway `main` (or a
JUnit test if you'd rather set that up) that calls
`searchArtist("Radiohead")` then `getReleaseGroups(mbid)` and prints the
results. This talks to the real public API — no key needed — so you'll know
immediately if your parsing is right. Remember the User-Agent header and the
~1 req/sec rate limit mentioned in the Javadoc; MusicBrainz will reject
requests without a descriptive User-Agent.

## Milestone 3 — `ReleaseMemory` (memory package)

Load/save a JSON file at `data/state.json`. Test it standalone too: record
a fake artist + date, save, construct a fresh `ReleaseMemory`, load, confirm
you get the same value back.

Think through the "first time we've ever seen this artist" case (documented
in the class's Javadoc) before you move on — it affects `CheckNewReleasesTool`
in the next milestone.

## Milestone 4 — Tools (tools package)

`ToolRegistry`, `ListTrackedArtistsTool`, `CheckNewReleasesTool`. Wire the
real `MusicBrainzClient` and `ReleaseMemory` into `CheckNewReleasesTool`'s
constructor. Test each tool's `execute(...)` directly (no agent loop
involved yet) with a hand-built input map, e.g.
`Map.of("artist", "Radiohead")`.

## Milestone 5 — The mock brain (llm package)

`MockReasoningBrain`. This is the fiddliest part precisely because it's
faking intelligence with plain string logic. Its Javadoc spells out the
exact text format (the "ReAct" format) and the state machine it should
follow. Write a few lines of throwaway test code that calls `think(...)`
repeatedly with a hand-built transcript, printing what it returns, before
you have `Agent` to drive it — much easier to debug in isolation.

## Milestone 6 — The agent loop (agent package)

`AgentStep`, then `Agent`. This is the piece most worth understanding cold
by Monday — it's the part most likely to come up if you're asked to
whiteboard "how does an agent work." Its Javadoc walks through the loop
step by step.

## Milestone 7 — Wire it up (`App.java`)

Load `config/artists.json`, construct everything, run the agent, print the
result, save memory. At this point you have a working agent with zero LLM
API calls. Run it twice in a row against the real artists in your config —
the second run should report nothing new (memory is doing its job), unless
one of them genuinely released something between runs.

Add a `--verbose` flag (or just print unconditionally while you're
debugging) that shows every Thought/Action/Observation, not just the final
answer — you'll want this for debugging regardless.

## Milestone 8 (stretch) — Swap in a real LLM

`AnthropicBrain`. Get a key from console.anthropic.com, set
`ANTHROPIC_API_KEY`, and switch `App.java` to use it instead of the mock
when the env var is present. Nothing else in the project should need to
change — that's the payoff of the `LlmBrain` interface. This is a genuinely
good thing to have working (even against a couple of test artists) to talk
about in the interview: a real model actually driving your tool-use loop.

## Stretch ideas if you have time left

- A `--self-test` mode that spins up a tiny local HTTP server
  (`com.sun.net.httpserver.HttpServer`, built into the JDK) that returns
  canned MusicBrainz-shaped JSON, and points `MusicBrainzClient` at it via a
  configurable base URL — lets you test the whole loop without hitting the
  real API or waiting on rate limits. Good interview talking point about
  testing systems that depend on external APIs.
- Swap the manual ReAct text-parsing loop for Anthropic's native tool-use
  (the `tools` parameter in the Messages API) as an alternate `LlmBrain` —
  or even a variant `Agent` — and be ready to explain why that's more
  robust than regex-parsing free text.
- A third tool, e.g. `dismiss_artist` or `snooze_artist`, to make the agent
  feel more like something with real user-facing controls.

## When you're stuck

Share the file (or paste the code) and tell me what's happening (compiler
error, wrong output, or "I don't know how to approach X") — I'll help you
debug or reason through the design rather than hand you the fix outright,
unless you specifically want the answer.
