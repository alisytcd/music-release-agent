package com.musicagent.musicbrainz;

/**
 * A single release-group from MusicBrainz -- roughly "an album" or "a single"
 * (MusicBrainz groups different editions/remasters of the same release under
 * one release-group, which is exactly the granularity you want for "did this
 * artist put out something new").
 *
 *
 *   - fields: String mbid, String title, String primaryType (e.g. "Album",
 *     "Single", "EP" -- comes from the "primary-type" JSON field),
 *     String firstReleaseDate (comes from "first-release-date", format
 *     "YYYY-MM-DD", "YYYY-MM", or "YYYY" -- MusicBrainz dates are not always
 *     fully specified, worth handling in a different class that rather than assuming full dates)
 *   - constructor + getters/public fields
 */
public final class ReleaseGroup {
    private String mbid;

    private String title;

    private String primaryType;

    private String firstReleaseDate;

    public ReleaseGroup(String mbid, String title, String primaryType, String firstReleaseDate) {
       this.mbid = mbid;
       this.title = title;
       this.primaryType = primaryType;
       this.firstReleaseDate = firstReleaseDate;
    }

    public String getMbid(){
        return mbid;
    }

    public String getTitle(){
        return title;
    }

    public String getPrimaryType(){
        return primaryType;
    }

    public String getFirstReleaseDate(){
        return firstReleaseDate;
    }

}
