package com.musicagent.musicbrainz;

/**
 * Result of searching MusicBrainz for an artist by name.
 *
 * Plain data holder.
 *   - fields: String mbid (MusicBrainz ID, a UUID string), String name,
 *     double score (MusicBrainz's own match confidence, 0-100)
 *   - constructor + getters (or just make the fields public final -- your
 *     call, this is a pure data class)
 */
public final class ArtistMatch {
    private String mbid;
    private String name;
    private double score;

    public ArtistMatch(String mbid, String name, double score) {
        // TODO(you)
        this.mbid = mbid;
        this.name = name;
        this.score = score;
    }

    public String getMbid(){
        return mbid;
    }

    public String getName(){
        return name;
    }

    public double getScore(){
        return score;
    }

}
