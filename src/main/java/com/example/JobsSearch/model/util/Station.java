package com.example.JobsSearch.model.util;

public class Station {
    private String station;

    private String transportation;

    public Station() {
    }

    public Station(String station, String transportation) {
        this.station = station;
        this.transportation = transportation;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }
}
