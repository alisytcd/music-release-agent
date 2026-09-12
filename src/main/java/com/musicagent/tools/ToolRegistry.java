package com.musicagent.tools;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds the set of Tools the agent is allowed to call this run, keyed by
 * Tool.name(). Agent.java will use this to (a) build the "here's what you
 * can do" section of the system prompt and (b) dispatch a parsed Action
 * to the right Tool.execute(...).
 */
public final class ToolRegistry {

    private Map<String,Tool> toolsMap;

    public ToolRegistry() {
        this.toolsMap = new HashMap<>();
    }

    public void register(Tool tool) {
        this.toolsMap.put(tool.name(),tool);
    }

    public Tool get(String name) {

        return toolsMap.get(name);

    }

    public Collection<Tool> all() {
        return toolsMap.values();
    }
}
