package com.musicagent.tools;

import org.json.JSONArray;
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
 */
public final class ListTrackedArtistsTool implements Tool {

    private List<String> trackedArtists;
    public ListTrackedArtistsTool(List<String> trackedArtists) {
        this.trackedArtists = trackedArtists;
    }

    @Override
    public String name() {
        return "list_tracked_artists";
    }

    @Override
    public String description() {
        return "List the artists that the user cares about tracking";
    }

    @Override
    public String inputSpec() {
        return "{}";
    }

    @Override
    public ToolResult execute(Map<String, Object> input) {

        JSONArray artists = new JSONArray(trackedArtists);

        return ToolResult.ok(artists.toString());
    }
}
