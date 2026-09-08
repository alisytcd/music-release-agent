package com.musicagent.musicbrainz;

/**
 * A single release-group from MusicBrainz -- roughly "an album" or "a single"
 * (MusicBrainz groups different editions/remasters of the same release under
 * one release-group, which is exactly the granularity you want for "did this
 * artist put out something new").
 *
 * TODO(you): plain data holder.
 *   - fields: String mbid, String title, String primaryType (e.g. "Album",
 *     "Single", "EP" -- comes from the "primary-type" JSON field),
 *     String firstReleaseDate (comes from "first-release-date", format
 *     "YYYY-MM-DD", "YYYY-MM", or "YYYY" -- MusicBrainz dates are not always
 *     fully specified, worth handling that rather than assuming full dates)
 *   - constructor + getters/public fields, your call
 */
public final class ReleaseGroup {

    // TODO(you)

    public ReleaseGroup(String mbid, String title, String primaryType, String firstReleaseDate) {
        // TODO(you)
    }
}
