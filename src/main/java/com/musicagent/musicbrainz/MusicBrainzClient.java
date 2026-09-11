package com.musicagent.musicbrainz;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
 */
public final class MusicBrainzClient {

    private long lastRequestAtMillis = 0;
    private static final String BASE_URL = "https://musicbrainz.org/ws/2";

    private final HttpClient httpClient;

    public MusicBrainzClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public ArtistMatch searchArtist(String name) throws IOException, InterruptedException {

        String encodedName = URLEncoder.encode(name,StandardCharsets.UTF_8);

        String urlPath = BASE_URL+"/artist?query="+encodedName+"&fmt=json";

        HttpResponse<String> response = makeRequestAndGetResponse(urlPath);

        //if we get some kind of issue with the service or rate limited or something of the sort
        if(response.statusCode()!=200){
            return new ArtistMatch("-1","SERVICE_ERROR",0);
        }

        return getArtistFromResponse(response.body());
    }

    public List<ReleaseGroup> getReleaseGroups(String artistMbid) throws InterruptedException, IOException {

        String encodedMbid = URLEncoder.encode(artistMbid,StandardCharsets.UTF_8);

        String urlPath = BASE_URL+"/release-group?artist="+encodedMbid+"&limit=100&fmt=json";

        HttpResponse<String> response = makeRequestAndGetResponse(urlPath);

        List<ReleaseGroup> resultGroups = new ArrayList<>();

        if(response.statusCode()!=200){
            resultGroups.add(new ReleaseGroup("-1","SERVICE_ERROR","-1","-1"));
            return resultGroups;
        }

        return getReleaseGroupsFromResponse(response.body());
    }

    private void awaitForRateLimit() throws InterruptedException {
        long elapsed = System.currentTimeMillis() - lastRequestAtMillis;
        if (elapsed < 1000) {
            Thread.sleep(1000 - elapsed);
        }
        lastRequestAtMillis = System.currentTimeMillis();
    }

    private ArtistMatch getArtistFromResponse(String response){

        JSONObject responseJson = new JSONObject(response);
        JSONArray artistsArray = responseJson.getJSONArray("artists");

        if(artistsArray.isEmpty()){
            return new ArtistMatch("-1","NOT_FOUND",0);
        }

        double highestScore = 0;
        String artistId=null;
        String artistName=null;

        for(int i = 0 ; i < artistsArray.length(); i++){

            JSONObject currentArtist = artistsArray.getJSONObject(i);
            double currentArtistScore = currentArtist.getDouble("score");

            if(currentArtistScore > highestScore){
                artistId = currentArtist.getString("id");
                artistName = currentArtist.getString("name");
                highestScore = currentArtistScore;
            }
        }
        return new ArtistMatch(artistId,artistName,highestScore);
    }

    private ArrayList<ReleaseGroup> getReleaseGroupsFromResponse(String response){

        JSONObject responseJson = new JSONObject(response);
        JSONArray releaseGroupsArray = responseJson.getJSONArray("release-groups");

        ArrayList<ReleaseGroup> resultGroups = new ArrayList<>();

        for(int i = 0; i < releaseGroupsArray.length() ; i++){

            JSONObject currentReleaseGroup = releaseGroupsArray.getJSONObject(i);

            String id = currentReleaseGroup.getString("id");

            String title = currentReleaseGroup.getString("title");

            String primaryType = currentReleaseGroup.getString("primary-type");

            String firstReleaseDate = currentReleaseGroup.getString("first-release-date");

            resultGroups.add(new ReleaseGroup(id,title,primaryType,firstReleaseDate));
        }

        return resultGroups;
    }

    private HttpResponse<String> makeRequestAndGetResponse(String urlPath) throws InterruptedException, IOException {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(urlPath)).header("User-Agent","MusicReleaseAgent/1.0 ( alisy@tcd.ie )")
                .build();

        awaitForRateLimit();

        HttpResponse <String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }
}
