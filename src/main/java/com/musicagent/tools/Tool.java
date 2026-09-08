package com.musicagent.tools;

import java.util.Map;

/**
 * Everything the agent can DO lives behind this interface.
 *
 * This is the core idea of "tool use" / "function calling": the LLM never
 * touches the network, the filesystem, or any API directly. It only ever
 * produces text describing which tool it wants to call and with what
 * arguments. Your code (see agent.Agent) parses that text, calls the real
 * Java method here, and feeds the result back to the LLM as an "Observation".
 *
 * That boundary is what makes agents safe-ish to build: you decide exactly
 * what actions exist and what they're allowed to touch. The model can only
 * pick from this menu.
 */
public interface Tool {

    /** Short, stable identifier the LLM will refer to, e.g. "check_new_releases". */
    String name();

    /**
     * Human (and LLM) readable description of what this tool does and when to
     * use it. This text is injected into the system prompt, so it's the only
     * thing the model has to decide when this tool is the right one to call.
     */
    String description();

    /** Describes the expected JSON input, e.g. {"artist": "string, required"}. */
    String inputSpec();

    /**
     * Execute the tool with the parsed JSON input (may be an empty map if the
     * tool takes no arguments). Should never throw for "expected" failure
     * modes (e.g. artist not found) -- return a ToolResult describing the
     * problem instead, so the agent can reason about it and try something
     * else, just like a real LLM-driven agent has to.
     */
    ToolResult execute(Map<String, Object> input);
}
