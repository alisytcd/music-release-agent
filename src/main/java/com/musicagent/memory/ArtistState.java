package com.musicagent.memory;

public final class ArtistState {

    private String lastSeenDate;

    private String lastSeenTitle;

    public ArtistState(String lastSeenDate, String lastSeenTitle){

        this.lastSeenDate = lastSeenDate;

        this.lastSeenTitle = lastSeenTitle;

    }

    public String getLastSeenDate(){
        return this.lastSeenDate;
    }

    public String getLastSeenTitle(){
        return this.lastSeenTitle;
    }
}
