package com.musicagent.tools;

import com.musicagent.memory.ReleaseMemory;
import com.musicagent.musicbrainz.MusicBrainzClient;

import java.util.Map;

/**
 * Tool #2 -- the interesting one. Input: {"artist": "<name>"}.
 *
 * What it should do, end to end:
 *   1. Read "artist" out of the input map.
 *   2. MusicBrainzClient.searchArtist(name) -> mbid (handle "not found").
 *   3. MusicBrainzClient.getReleaseGroups(mbid) -> all release-groups.
 *   4. ReleaseMemory.getLastSeenDate(name) -> what we already knew about.
 *   5. Compare: which release-groups have a first-release-date AFTER what's
 *      in memory? (String-compare works fine for ISO dates like
 *      "2024-03-15", but watch out for MusicBrainz's partial dates like
 *      "2024" or "2024-03" -- decide how you want to handle those.)
 *   6. If memory had NOTHING for this artist yet, see the design note in
 *      ReleaseMemory's Javadoc -- you probably want "record baseline, report
 *      nothing new" rather than dumping the whole discography.
 *   7. Update memory with the newest release-group found (regardless of
 *      whether you reported it as "new" or as the baseline).
 *   8. Return a ToolResult whose text describes what was found -- this text
 *      is what the LLM (mock or real) will read as the "Observation" for
 *      this step, so make it something a model (or a human skimming logs)
 *      can actually parse/summarize. A small JSON blob or a short bullet
 *      list both work.
 *
 * TODO(you): wire this up. Constructor takes the MusicBrainzClient and
 * ReleaseMemory this tool should use (dependency injection, not `new`'d
 * inside the tool -- makes it much easier to swap in a fake client for
 * testing later).
 */
public final class CheckNewReleasesTool implements Tool {

    public CheckNewReleasesTool(MusicBrainzClient client, ReleaseMemory memory) {
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
