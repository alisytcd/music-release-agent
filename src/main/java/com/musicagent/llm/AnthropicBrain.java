package com.musicagent.llm;

/**
 * STRETCH GOAL -- only tackle this once MockReasoningBrain + Agent are
 * working end to end. Swaps the fake brain for a real call to Claude, using
 * the exact same LlmBrain interface, so nothing else in the project has to
 * change.
 *
 * Approach: send `systemPrompt` as the system parameter and `transcript` as
 * a single user message to Anthropic's Messages API
 * (POST https://api.anthropic.com/v1/messages), and return the text of the
 * response. Because the interface is still "text in, text out", you're
 * asking Claude to itself produce the Thought/Action/Action Input/Final
 * Answer text format -- NOT using Anthropic's native structured tool-use
 * ("tools" parameter). That's a simplification worth being upfront about if
 * asked in an interview: native tool-use is more robust (the API guarantees
 * valid, schema-matching JSON instead of you regex-parsing free text), and
 * it's the way you'd actually do this in production. Doing it the manual
 * way once is what makes the tradeoff concrete.
 *
 * You'll need:
 *   - an API key (env var ANTHROPIC_API_KEY is the usual convention --
 *     never hardcode it)
 *   - headers: "x-api-key: <key>", "anthropic-version: 2023-06-01",
 *     "content-type: application/json"
 *   - a JSON request body roughly:
 *     {"model": "claude-...", "max_tokens": 1024,
 *      "system": "<systemPrompt>",
 *      "messages": [{"role": "user", "content": "<transcript>"}]}
 *   - the response's content[0].text is what you return
 *   - check console.anthropic.com for current model names and to grab a key
 *     (new accounts sometimes get a small free credit grant, but that
 *     varies -- verify current pricing/models there rather than assuming)
 *
 * TODO(you): implement once the mock brain proves the loop works.
 */
public final class AnthropicBrain implements LlmBrain {

    public AnthropicBrain(String apiKey) {
        // TODO(you)
    }

    @Override
    public String think(String systemPrompt, String transcript) {
        throw new UnsupportedOperationException("TODO: implement AnthropicBrain.think");
    }
}
