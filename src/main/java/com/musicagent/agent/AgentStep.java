package com.musicagent.agent;

import java.util.Map;

/**
 * Result of parsing one chunk of raw LLM text (see Agent.parseStep) into
 * something your control flow can switch on, instead of re-parsing strings
 * all over Agent.run().
 *
 * TODO(you): design this however makes sense to you. One reasonable shape:
 *   - an enum Type { ACTION, FINAL_ANSWER }
 *   - fields for: the tool name + input Map<String,Object> (when ACTION),
 *     or the final answer text (when FINAL_ANSWER)
 *   - static factory methods action(String toolName, Map<String,Object> input)
 *     and finalAnswer(String text) mirroring the ToolResult.ok/error pattern
 *     you already used in the tools package
 *
 * This class is a hint that parsing belongs in its own place, not that you
 * must use exactly this shape -- a simple sealed interface with two record
 * implementations works too if you'd rather use newer Java features.
 */
public final class AgentStep {

    // TODO(you)
}
