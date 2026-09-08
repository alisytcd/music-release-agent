package com.musicagent.tools;

/**
 * The outcome of a tool call.
 *
 * DESIGN NOTE: a tool "failing" (artist not found, HTTP error, etc.) should
 * NOT be a Java exception that crashes the run. It should be a normal
 * ToolResult with ok=false, whose text() gets fed back to the LLM as an
 * "Observation:" so the agent can reason about the failure and decide what
 * to do next -- same as a real LLM-driven agent has to handle a failed API
 * call inline, without a human developer there to catch the exception.
 *
 *
 *   - two fields: a boolean ok, and a String text
 *   - a private constructor
 *   - static factory methods ok(String) and error(String)
 *   - getters: isOk() and text()
 *
 * This one's deliberately tiny -- a good first file to knock out before the
 * harder classes.
 */
public final class ToolResult {
    private boolean ok;
    private String text;

    private ToolResult(boolean ok, String text){
        this.ok = ok;
        this.text = text;
    }

    public static ToolResult ok(String text) {
        return new ToolResult(true,text);
    }

    public static ToolResult error(String text) {
        return new ToolResult(false,text);
    }

    public boolean isOk() {
        return ok;
    }

    /** Text that gets fed back to the LLM as the "Observation:" for this step. */
    public String text() {
        return text;
    }
}
