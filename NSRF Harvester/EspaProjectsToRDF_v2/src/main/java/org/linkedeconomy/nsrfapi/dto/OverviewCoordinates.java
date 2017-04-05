package org.linkedeconomy.nsrfapi.dto;

/**
 *
 * @author G. Vafeiadis
 */
public class OverviewCoordinates {
    
    private String kodikos;
    private Integer coordinatesId;
    private String coordinates;

    public String getKodikos() {
        return kodikos;
    }

    public Integer getCooedinatesId() {
        return coordinatesId;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setKodikos(String kodikos) {
        this.kodikos = kodikos;
    }

    public void setCoordinatesId(Integer coordinatesId) {
        this.coordinatesId = coordinatesId;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
    
    
}
