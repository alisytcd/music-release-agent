package com.musicagent.memory;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    private HashMap<String,ArtistState> artistStates;

    public ReleaseMemory() {

        this.artistStates = new HashMap<>();

    }


    /* Stored as JSON at data/state.json, shape roughly:
     *   { "Radiohead": { "lastSeenDate": "2016-05-08", "lastSeenTitle": "A Moon Shaped Pool" },
     *     "Fred again..": { "lastSeenDate": "2024-01-01", "lastSeenTitle": "Actual Life 3" } }
     */
    public void load(String path) throws IOException {

        Path filePath = Path.of(path);

        if(Files.exists(filePath)){
            JSONObject releaseContents = new JSONObject(Files.readString(filePath));
            Set<String> artistNames = releaseContents.keySet();
            for(String artistName : artistNames){
                JSONObject artist = releaseContents.getJSONObject(artistName);
                ArtistState artistState = new ArtistState(artist.getString("lastSeenDate"),artist.getString("lastSeenTitle"));
                artistStates.put(artistName,artistState);

            }

        }

    }

    /* Stored as JSON at data/state.json, shape roughly:
     *   { "Radiohead": { "lastSeenDate": "2016-05-08", "lastSeenTitle": "A Moon Shaped Pool" },
     *     "Fred again..": { "lastSeenDate": "2024-01-01", "lastSeenTitle": "Actual Life 3" } }
     */
    public void save(String path) throws IOException {

        Path filePath = Path.of(path);

        JSONObject artistObject = new JSONObject();

        for(Map.Entry<String,ArtistState> entry : artistStates.entrySet()){

            String artistName = entry.getKey();

            String lastSeenDate = entry.getValue().getLastSeenDate();

            String lastSeenTitle = entry.getValue().getLastSeenTitle();

            JSONObject releaseDetailsObject = new JSONObject();
            releaseDetailsObject.put("lastSeenDate",lastSeenDate);
            releaseDetailsObject.put("lastSeenTitle",lastSeenTitle);


            artistObject.put(artistName,releaseDetailsObject);

        }

        Files.writeString(filePath,artistObject.toString());
    }

    public String getLastSeenDate(String artist) {

        if(artistStates.containsKey(artist)) {
            return artistStates.get(artist).getLastSeenDate();
        }
        else return null;

    }

    public void recordLatest(String artist, String date, String title) {

        ArtistState latestArtistState = new ArtistState(date,title);

        artistStates.put(artist,latestArtistState);

    }
}
