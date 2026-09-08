package com.musicagent.tools;

import java.util.Collection;

/**
 * Holds the set of Tools the agent is allowed to call this run, keyed by
 * Tool.name(). Agent.java will use this to (a) build the "here's what you
 * can do" section of the system prompt and (b) dispatch a parsed Action
 * to the right Tool.execute(...).
 *
 * TODO(you):
 *   - a backing Map<String, Tool>
 *   - register(Tool tool): add it, keyed by tool.name()
 *   - get(String name): return the Tool, or null / Optional if not found
 *     (decide how Agent should react to the LLM naming a tool that doesn't
 *     exist -- that WILL happen once you swap in a real LLM, so it's worth
 *     thinking through now even though the mock brain won't trigger it)
 *   - all(): return the registered tools, e.g. for prompt-building
 */
public final class ToolRegistry {

    // TODO(you): backing storage

    public ToolRegistry() {
        // TODO(you)
    }

    public void register(Tool tool) {
        throw new UnsupportedOperationException("TODO: implement ToolRegistry.register");
    }

    public Tool get(String name) {
        throw new UnsupportedOperationException("TODO: implement ToolRegistry.get");
    }

    public Collection<Tool> all() {
        throw new UnsupportedOperationException("TODO: implement ToolRegistry.all");
    }
}
