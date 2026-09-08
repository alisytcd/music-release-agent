package com.musicagent.tools;

import java.util.List;
import java.util.Map;

/**
 * Tool #1 -- the simplest one. Takes no input, just returns the artists the
 * user cares about (loaded from config/artists.json by App.java and passed
 * in via the constructor).
 *
 * Why this tool exists at all, instead of just hardcoding the artist loop in
 * Java: it keeps the agent's "plan" (which artists to check, in what order,
 * whether to stop early) driven by the LLM's reasoning over text, not by
 * your control flow. That's the whole point of the exercise -- in Milestone
 * 2 you'll make the mock brain call this tool first, read its Observation,
 * and only then decide to call check_new_releases once per artist.
 *
 * TODO(you):
 *   - store the List<String> artist names passed into the constructor
 *   - name() -> "list_tracked_artists"
 *   - description() -> a sentence the LLM (or a human) would use to know
 *     when to call this
 *   - inputSpec() -> something like "{} (no input required)"
 *   - execute(input) -> ignore the input, return a ToolResult.ok(...) whose
 *     text is a JSON array of the artist names (e.g. via org.json's
 *     JSONArray, or even just a manually built string -- your call)
 */
public final class ListTrackedArtistsTool implements Tool {

    public ListTrackedArtistsTool(List<String> trackedArtists) {
        // TODO(you)
    }

    @Override
    public String name() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public String description() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public String inputSpec() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public ToolResult execute(Map<String, Object> input) {
        throw new UnsupportedOperationException("TODO");
    }
}
