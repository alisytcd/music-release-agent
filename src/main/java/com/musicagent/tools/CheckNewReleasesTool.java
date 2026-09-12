package com.musicagent.tools;

import com.musicagent.memory.ReleaseMemory;
import com.musicagent.musicbrainz.ArtistMatch;
import com.musicagent.musicbrainz.MusicBrainzClient;
import com.musicagent.musicbrainz.ReleaseGroup;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
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
 */
public final class CheckNewReleasesTool implements Tool {

    private  MusicBrainzClient client;

    private ReleaseMemory memory;

    private boolean isFirstContact = false;
    private boolean foundNewRelease = false;
    private String latestReleaseTitle = null;
    private String latestReleaseDate = null;

    private String path = "data/state.json";

    public CheckNewReleasesTool(MusicBrainzClient client, ReleaseMemory memory) {
        this.client = client;
        this.memory = memory;
    }

    @Override
    public String name() {
        return "check_new_releases";
    }

    @Override
    public String description() {
        return "Checks whether a specific tracked artist has released anything new since the last check. " +
                "Compares the artist's release history from MusicBrainz against what was previously recorded " +
                "in memory for that artist, and reports any releases newer than what's already known. " +
                "If this is the first time the artist has been checked, no releases are reported as new -- " +
                "the current latest release is simply recorded as the starting point for future checks.";
    }

    @Override
    public String inputSpec() {
        return "{\"artist\": \"<name>\"}";
    }

    @Override
    //Input: {"artist": "<name>"}
    public ToolResult execute(Map<String, Object> input) {

        ArtistMatch artistMatch;

        boolean executionResult = false;

        String resultMessage = null;

        String artist = (String) input.get("artist");

        try {

            artistMatch = client.searchArtist(artist);

            if(artistMatch.getName().contains("NOT_FOUND")){
                return ToolResult.error("ARTIST_NOT_FOUND");
            }
            else if(artistMatch.getName().contains("SERVICE_ERROR")){
                return ToolResult.error("SERVICE_ERROR");
            }

            List<ReleaseGroup> releaseGroups = client.getReleaseGroups(artistMatch.getMbid());

            executionResult = compareReleaseDatesAndHandleFirstContact(artist,releaseGroups);

        } catch (InterruptedException  | IOException e){

            return ToolResult.error("Error while checking for artist: "+ artist + ". \nError Message: "+e.getMessage() +" Execution Result Successful?: "+executionResult);

        }

        if(executionResult == true){

            if(this.isFirstContact){
                resultMessage = "First check for artist -- recorded \"" + latestReleaseTitle + "\" (" + latestReleaseDate + ") as the baseline.";
            } else if (this.foundNewRelease) {
                resultMessage = "New release found: \"" + latestReleaseTitle + "\" (" + latestReleaseDate + ").";
            } else {
                resultMessage = "No new releases since last check.";
            }
        }
        else {
            resultMessage = "Error - ResultGroups was null or empty.";
        }

        return executionResult ? ToolResult.ok(resultMessage) : ToolResult.error(resultMessage);
    }

    private boolean compareReleaseDatesAndHandleFirstContact(String artist, List<ReleaseGroup> releaseGroups) {

        this.isFirstContact = false;
        this.foundNewRelease = false;
        this.latestReleaseTitle = null;
        this.latestReleaseDate = null;

        if (releaseGroups == null || releaseGroups.isEmpty()) {
            return false;
        }
        String lastSeenDate = memory.getLastSeenDate(artist);

        if (lastSeenDate != null) {

            LocalDate newestSoFar = normalizeToLocalDate(lastSeenDate);
            String newestTitle = null;

            for (ReleaseGroup group : releaseGroups) {

                String releaseDate = group.getFirstReleaseDate();

                if (releaseDate != null && !releaseDate.isEmpty()) {

                    LocalDate currentGroupReleaseDate = normalizeToLocalDate(releaseDate);
                    if (currentGroupReleaseDate.isAfter(newestSoFar)) {
                        newestSoFar = currentGroupReleaseDate;
                        newestTitle = group.getTitle();
                    }
                }

            }

            if (newestTitle != null) {
                memory.recordLatest(artist, newestSoFar.toString(), newestTitle);
                this.latestReleaseDate = newestSoFar.toString();
                this.latestReleaseTitle = newestTitle;
                this.foundNewRelease = true;
            }

        } else {
            LocalDate latestReleaseDate = null;
            String releaseTitle = null;

            for (ReleaseGroup group : releaseGroups) {
                String releaseDate = group.getFirstReleaseDate();
                if (releaseDate != null && !releaseDate.isEmpty()) {
                    LocalDate currentReleaseDate = normalizeToLocalDate(releaseDate);
                    if (latestReleaseDate == null || currentReleaseDate.isAfter(latestReleaseDate)) {
                        latestReleaseDate = currentReleaseDate;
                        releaseTitle = group.getTitle();
                    }
                }
            }

            if (latestReleaseDate != null) {
                memory.recordLatest(artist, latestReleaseDate.toString(), releaseTitle);
                this.isFirstContact = true;
                this.latestReleaseDate = latestReleaseDate.toString();
                this.latestReleaseTitle = releaseTitle;
            } else {
                return false; // none of the release groups had a usable date
            }
        }

        return true;

    }
    private LocalDate normalizeToLocalDate(String date){

        String [] splitDate = date.split("-");

        int dateLength = splitDate.length;

        LocalDate normalizedLocalDate;

        switch (dateLength) {
            case 3 -> normalizedLocalDate = LocalDate.parse(date);
            case 2 -> {
                YearMonth currentDateYearAndMonth = YearMonth.parse(date);
                normalizedLocalDate = currentDateYearAndMonth.atDay(1);
            }
            default -> {
                Year currentDateYear = Year.parse(date);
                normalizedLocalDate = currentDateYear.atMonth(1).atDay(1);
            }
        }

        return normalizedLocalDate;

    }
}
