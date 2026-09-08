package com.musicagent.llm;

/**
 * A fake "LLM" that lets you build and test the WHOLE agent loop without an
 * API key. It doesn't reason about anything -- it just follows a hardcoded
 * script that mimics what a real LLM would plausibly output for this task,
 * in the exact text format your system prompt asks for.
 *
 * THE FORMAT (this is the classic "ReAct" prompting pattern -- Reason + Act):
 * each call to think() should return ONE of:
 *
 *   Thought: <why you're doing the next thing>
 *   Action: <tool name, must match a Tool.name() exactly>
 *   Action Input: <JSON object matching that tool's inputSpec()>
 *
 * or, when there's nothing left to do:
 *
 *   Thought: <final reasoning>
 *   Final Answer: <the human-readable summary the user actually sees>
 *
 * HOW TO DECIDE WHAT TO OUTPUT NEXT (since this brain has no real
 * reasoning): look at the `transcript` argument. It contains every
 * Thought/Action/Action Input/Observation emitted so far, in order. You can
 * figure out "what step are we on" by:
 *   - counting how many "Observation:" markers appear so far
 *   - on Observation #0 (nothing yet): emit an Action for
 *     "list_tracked_artists" (no input)
 *   - on Observation #1: parse the artist list out of that first
 *     Observation's text, and emit an Action for "check_new_releases" with
 *     the first artist
 *   - on each subsequent Observation: move to the next artist and repeat,
 *     accumulating what you've learned
 *   - once you've gone through every artist: emit a Final Answer that
 *     summarizes what was found (which artists have new releases, and
 *     what they are; say plainly when nobody has anything new)
 *
 * This is genuinely representative of how you'd smoke-test a real agent
 * before burning API credits on it -- worth mentioning in an interview if it
 * comes up.
 *
 * TODO(you): implement the state machine described above. You'll probably
 * want a small helper to count/parse "Observation:" blocks out of the
 * transcript string.
 */
public final class MockReasoningBrain implements LlmBrain {

    @Override
    public String think(String systemPrompt, String transcript) {
        throw new UnsupportedOperationException("TODO: implement MockReasoningBrain.think");
    }
}
