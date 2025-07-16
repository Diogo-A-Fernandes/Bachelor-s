package com.thehotel.model;

public class RoomRequest {
    private int numGuests;
    private int numBeds;
    private String viewType;
    private boolean hasKitchen;
    private boolean hasBalcony;
    private int numWC;

    public RoomRequest(int numGuests, int numBeds, String viewType, boolean hasKitchen, boolean hasBalcony, int numWC) {
        this.numGuests = numGuests;
        this.numBeds = numBeds;
        this.viewType = viewType;
        this.hasKitchen = hasKitchen;
        this.hasBalcony = hasBalcony;
        this.numWC = numWC;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public int getNumBeds() {
        return numBeds;
    }

    public void setNumBeds(int numBeds) {
        this.numBeds = numBeds;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public boolean isHasKitchen() {
        return hasKitchen;
    }

    public void setHasKitchen(boolean hasKitchen) {
        this.hasKitchen = hasKitchen;
    }

    public boolean isHasBalcony() {
        return hasBalcony;
    }

    public void setHasBalcony(boolean hasBalcony) {
        this.hasBalcony = hasBalcony;
    }

    public int getNumWC() {
        return numWC;
    }

    public void setNumWC(int numWC) {
        this.numWC = numWC;
    }

    @Override
    public String toString() {
        return String.format(
                "%nQuarto solicitado - " +
                        "Num. Máx. hóspedes: %d, " +
                        "Num. Camas: %d, " +
                        "Vista: %s, " +
                        "Cozinha: %s, " +
                        "Varanda: %s, " +
                        "Num. WC: %d",
                numGuests,
                numBeds,
                viewType,
                hasKitchen ? "Sim" : "Não",
                hasBalcony ? "Sim" : "Não",
                numWC
        );
    }
}
