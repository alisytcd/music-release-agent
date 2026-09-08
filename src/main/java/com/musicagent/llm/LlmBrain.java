package com.musicagent.llm;

/**
 * The "brain" behind the agent, abstracted so the SAME agent loop
 * (agent.Agent) can run against either a fake, deterministic brain (no API
 * key needed -- MockReasoningBrain) or a real LLM (AnthropicBrain), with
 * zero changes to the loop itself.
 *
 * This interface is intentionally just "text in, text out" -- NOT a
 * structured tool-calling API. That's a deliberate simplification: it means
 * you (and the agent loop) have to do the work of asking the model to
 * produce its next step in a specific text format, and of PARSING that text
 * back into a structured action. That's exactly what the original ReAct
 * agent pattern does, and it's worth understanding by hand once, even though
 * production systems today usually lean on a provider's native "tools"
 * parameter (structured function-calling) instead of text parsing -- know
 * both, and be able to explain the tradeoff (native tool-calling is more
 * reliable/less brittle to parse; text-based ReAct is simpler to reason
 * about and provider-agnostic).
 *
 * TODO(you): just the interface signature -- implementations live in
 * MockReasoningBrain and AnthropicBrain.
 */
public interface LlmBrain {

    /**
     * @param systemPrompt static instructions: the agent's goal, the list of
     *                      available tools and how to call them, and the
     *                      exact output format expected (Thought/Action/
     *                      Action Input/Observation/Final Answer).
     * @param transcript    everything that's happened in this run so far --
     *                      prior Thoughts/Actions/Observations, concatenated
     *                      as plain text.
     * @return the model's raw next chunk of text: either another
     *         Thought/Action/Action Input step, or a "Final Answer: ..."
     */
    String think(String systemPrompt, String transcript);
}
