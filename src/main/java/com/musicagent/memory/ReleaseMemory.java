package com.musicagent.memory;

/**
 * Persistent state: "the last release we already told the user about, per
 * artist". Without this, every run would report an artist's ENTIRE back
 * catalog as "new" -- memory is what turns a one-shot script into something
 * that behaves like it's monitoring something over time.
 *
 * Stored as JSON at data/state.json, shape roughly:
 *   { "Radiohead": { "lastSeenDate": "2016-05-08", "lastSeenTitle": "A Moon Shaped Pool" },
 *     "Fred again..": { "lastSeenDate": "2024-01-01", "lastSeenTitle": "Actual Life 3" } }
 *
 * DESIGN QUESTION worth thinking through before you code this (good
 * interview talking point too): what should happen the VERY FIRST time an
 * artist is tracked, when there's no prior state at all? If you naively
 * treat "no memory" as "everything is new", the first run will dump an
 * artist's whole discography at the user. Most real notification agents
 * instead treat first-contact as "record current state as the baseline,
 * report nothing" -- decide which behavior you want and make sure
 * CheckNewReleasesTool (in the tools package) implements that choice
 * explicitly rather than by accident.
 *
 * TODO(you):
 *   - load(String path): read the JSON file if it exists (empty map if not
 *     -- first-ever run) into an in-memory structure, e.g.
 *     Map<String, ArtistState> where ArtistState holds lastSeenDate/title
 *   - save(String path): write the current in-memory state back out as JSON
 *   - getLastSeenDate(String artist): String or null
 *   - recordLatest(String artist, String date, String title): update
 *     in-memory state (caller decides when to call save())
 */
public final class ReleaseMemory {

    public ReleaseMemory() {
        // TODO(you)
    }

    public void load(String path) {
        throw new UnsupportedOperationException("TODO: implement ReleaseMemory.load");
    }

    public void save(String path) {
        throw new UnsupportedOperationException("TODO: implement ReleaseMemory.save");
    }

    public String getLastSeenDate(String artist) {
        throw new UnsupportedOperationException("TODO: implement ReleaseMemory.getLastSeenDate");
    }

    public void recordLatest(String artist, String date, String title) {
        throw new UnsupportedOperationException("TODO: implement ReleaseMemory.recordLatest");
    }
}
