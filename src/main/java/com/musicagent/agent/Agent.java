package com.musicagent.agent;

import com.musicagent.llm.LlmBrain;
import com.musicagent.tools.Tool;
import com.musicagent.tools.ToolRegistry;
import com.musicagent.tools.ToolResult;

/**
 * THE CORE OF THE WHOLE PROJECT. Everything else (tools, memory, the LLM
 * brain) exists to be used by this loop. If you only deeply understand one
 * class by Monday, make it this one -- it's the part an interviewer is most
 * likely to ask you to whiteboard.
 *
 * The loop (this is the "agentic loop" / ReAct pattern in its simplest
 * form):
 *
 *   1. Build a system prompt once: the goal, the list of available tools
 *      (name + description + inputSpec, pulled from the ToolRegistry) and
 *      the exact Thought/Action/Action Input/Final Answer text format the
 *      brain must reply in.
 *   2. Maintain a running transcript (plain text) of everything that's
 *      happened: starts with the goal, and grows by one
 *      Thought/Action/Action Input block and one Observation block per
 *      iteration.
 *   3. Loop, up to some max number of steps (protect against infinite
 *      loops -- a real LLM WILL occasionally get stuck without a cap):
 *        a. raw = brain.think(systemPrompt, transcript)
 *        b. append raw to the transcript
 *        c. parse raw into an AgentStep
 *        d. if it's a Final Answer -> return that text, you're done
 *        e. if it's an Action -> look the tool up in the ToolRegistry,
 *           call tool.execute(input), append
 *           "Observation: " + result.text() to the transcript, and loop
 *           again
 *   4. If you exhaust max steps without a Final Answer, return something
 *      sensible rather than silently returning null (that's a real failure
 *      mode of agentic systems worth having an opinion on).
 *
 * PARSING RAW TEXT INTO AN ACTION: expect lines like
 *   Action: check_new_releases
 *   Action Input: {"artist": "Radiohead"}
 * A couple of regexes (or even line-by-line scanning + String methods) are
 * enough -- this doesn't need to be bulletproof for a mock brain that always
 * produces well-formed output, but think about what SHOULD happen if a real
 * LLM produces slightly malformed output (extra whitespace, wrong tool name,
 * invalid JSON) -- that's a legitimate interview question about building
 * reliable agents.
 *
 * TODO(you): implement the constructor and run(goal). Constructor should
 * take the LlmBrain, the ToolRegistry, and a max-steps limit.
 */
public final class Agent {

    public Agent(LlmBrain brain, ToolRegistry tools, int maxSteps) {
        // TODO(you)
    }

    /**
     * Runs the loop to completion (or until maxSteps is hit) and returns the
     * final human-readable answer.
     */
    public String run(String goal) {
        throw new UnsupportedOperationException("TODO: implement Agent.run");
    }

    // TODO(you): private helpers, e.g. buildSystemPrompt(ToolRegistry) and
    // parseStep(String rawText) -> AgentStep
}
