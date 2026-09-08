# Music Release Agent

A small, from-scratch Java agent that tracks artists you like and tells you
when they've released something new.

Everything under `src/` is a **skeleton**: interfaces, method signatures,
and Javadoc explaining what each piece needs to do and why. No logic is
implemented — that's the point. See **[BUILD_GUIDE.md](BUILD_GUIDE.md)**
for a milestone-by-milestone plan for filling it in.

## Project layout

```
config/artists.json     the artists you want tracked — edit this
data/state.json          created at runtime; remembers the last release
                          seen per artist so re-runs don't repeat themselves
src/main/java/com/musicagent/
  App.java                CLI entry point / wiring
  agent/                  the agent loop itself (Agent, AgentStep)
  llm/                    the "brain" abstraction (mock + real Anthropic)
  tools/                  what the agent is allowed to DO
  musicbrainz/            the only class that talks to the network
  memory/                 persisted state between runs
```

## Why these design choices

- **No LLM required to get a working agent.** `MockReasoningBrain`
  deterministically fakes what a real model would output, so the whole
  loop — reasoning, tool calls, memory — works before you spend a cent on
  API calls. `AnthropicBrain` is a stretch goal that drops in behind the
  same interface.
- **MusicBrainz** for release data: a free, public, keyless REST API — no
  account/API key friction to get something real running today.
- **Manual ReAct-style text parsing** rather than a provider's native
  tool-calling API, on purpose — it's more work, but it's what actually
  makes "how does an agent work under the hood" concrete instead of magic.
  `AnthropicBrain`'s Javadoc talks through the tradeoff vs. native tool use.
- **Minimal dependencies** (`org.json` for parsing, everything else is JDK)
  so there's nothing to fight with in terms of setup.
