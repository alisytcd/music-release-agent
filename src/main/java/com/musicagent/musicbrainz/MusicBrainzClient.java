package com.musicagent.musicbrainz;

import java.util.List;

/**
 * Thin wrapper around the public MusicBrainz REST API (no API key needed).
 * This is the ONLY class that talks to the network -- everything above it
 * (tools, agent) just sees Java objects.
 *
 * Reference, from MusicBrainz's own docs (so you don't have to re-dig for it):
 *
 *   1) Search for an artist by name:
 *        GET https://musicbrainz.org/ws/2/artist?query=<QUERY>&fmt=json
 *      Response JSON shape (abridged):
 *        { "artists": [ { "id": "<mbid>", "name": "...", "score": 100 }, ... ] }
 *      Pick the highest-scoring match (or the first result whose name matches
 *      case-insensitively -- score alone can be noisy for common names).
 *
 *   2) Browse an artist's release-groups (albums/singles/EPs):
 *        GET https://musicbrainz.org/ws/2/release-group?artist=<mbid>&limit=100&fmt=json
 *      Response JSON shape (abridged):
 *        { "release-groups": [
 *            { "id": "<mbid>", "title": "...", "primary-type": "Album",
 *              "first-release-date": "2024-03-15" }, ... ] }
 *      (the "lookup" form -- /artist/<mbid>?inc=release-groups -- also works
 *      but caps results at 25; the browse form above with limit=100 avoids
 *      that cap for prolific artists.)
 *
 * REQUIREMENTS MusicBrainz actually enforces:
 *   - You MUST send a descriptive User-Agent header, e.g.
 *     "MusicReleaseAgent/1.0 ( your-email@example.com )" -- they will block
 *     generic/default HTTP client user agents.
 *   - Rate limit: no more than ~1 request/second per client. TODO(you): add
 *     a simple rate limiter (e.g. track the timestamp of your last request
 *     as an instance field and Thread.sleep the remainder of 1000ms before
 *     firing the next one). This is a real production concern, not
 *     busywork -- most public APIs you'll integrate an agent with have some
 *     form of rate limit, and "the agent silently gets rate-limited /
 *     blocked" is a good thing to be able to talk about in an interview.
 *
 * TODO(you):
 *   - a java.net.http.HttpClient field, built once in the constructor
 *   - searchArtist(String name): build the URL (remember to URL-encode the
 *     query), send the GET with the User-Agent header, parse the JSON
 *     response, return the best ArtistMatch (or null / throw / Optional --
 *     decide how you want callers to detect "not found")
 *   - getReleaseGroups(String mbid): same idea, GET the browse endpoint,
 *     parse "release-groups" into a List<ReleaseGroup>
 *   - you'll need a JSON library for parsing -- add org.json:json (or
 *     Jackson, if you're more comfortable with it) as a Maven dependency
 *     rather than hand-rolling a parser; that's not where the interesting
 *     agent work is
 */
public final class MusicBrainzClient {

    private static final String BASE_URL = "https://musicbrainz.org/ws/2";

    public MusicBrainzClient() {
        // TODO(you): build and store an HttpClient
    }

    public ArtistMatch searchArtist(String name) {
        throw new UnsupportedOperationException("TODO: implement MusicBrainzClient.searchArtist");
    }

    public List<ReleaseGroup> getReleaseGroups(String artistMbid) {
        throw new UnsupportedOperationException("TODO: implement MusicBrainzClient.getReleaseGroups");
    }
}
