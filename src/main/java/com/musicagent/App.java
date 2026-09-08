package com.musicagent;

/**
 * Entry point. This is the "wiring" -- construct the real objects and hand
 * them to Agent, no agent logic of its own.
 *
 * TODO(you), roughly in this order (see BUILD_GUIDE.md for the fuller
 * milestone breakdown):
 *   1. Read config/artists.json into a List<String> of artist names.
 *   2. Construct a MusicBrainzClient.
 *   3. Construct a ReleaseMemory and load() it from data/state.json.
 *   4. Construct a ToolRegistry and register ListTrackedArtistsTool +
 *      CheckNewReleasesTool.
 *   5. Pick a brain: MockReasoningBrain to start; later, if
 *      System.getenv("ANTHROPIC_API_KEY") is set, use AnthropicBrain
 *      instead -- same Agent code either way.
 *   6. Construct an Agent and call run(...) with a goal string describing
 *      the task, e.g. "Check my tracked artists for new releases and
 *      summarize anything new."
 *   7. Print the result.
 *   8. Save memory back to data/state.json so the next run remembers what
 *      it already told you about.
 *
 * Consider a --self-test or --verbose flag while you're building this, so
 * you can see each Thought/Action/Observation as it happens rather than
 * only the final answer -- that visibility is what makes debugging an agent
 * loop tractable.
 */
public final class App {

    public static void main(String[] args) {
        // TODO(you)
    }
}
