package com.thehotel.model;

import java.time.LocalDate;

public class Room {
    private int id;
    private int maxGuests;
    private int numBeds;
    private String viewType;
    private boolean hasKitchen;
    private boolean hasBalcony;
    private int numWC;
    private double pricePerNight;
    private String status;
    private LocalDate registrationDate;
    private int idUserRegistration;
    private boolean needsMaintenance;

    public Room(int id, int maxGuests, int numBeds, String viewType, boolean hasKitchen, boolean hasBalcony, int numWC, double pricePerNight, String status, LocalDate registrationDate, int idUserRegistration, boolean needsMaintenance) {
        this.id = id;
        this.maxGuests = maxGuests;
        this.numBeds = numBeds;
        this.viewType = viewType;
        this.hasKitchen = hasKitchen;
        this.hasBalcony = hasBalcony;
        this.numWC = numWC;
        this.pricePerNight = pricePerNight;
        this.status = status;
        this.registrationDate = registrationDate;
        this.idUserRegistration = idUserRegistration;
        this.needsMaintenance = needsMaintenance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
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

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getIdUserRegistration() {
        return idUserRegistration;
    }

    public void setIdUserRegistration(int idUserRegistration) {
        this.idUserRegistration = idUserRegistration;
    }

    public boolean isNeedsMaintenance() {
        return needsMaintenance;
    }

    public void setNeedsMaintenance(boolean needsMaintenance) {
        this.needsMaintenance = needsMaintenance;
    }

    @Override
    public String toString() {
        return String.format(
                        "%nQuarto - ID: %d, " +
                        "Max. hóspedes: %d, " +
                        "Num. camas: %d, " +
                        "Vista: %s, " +
                        "Cozinha: %s, " +
                        "Varanda: %s, " +
                        "Num. WC: %d, " +
                        "Preço/Noite: %.2f, " +
                        "Estado: %s, " +
                        "Data Registo: %s, " +
                        "ID Utilizador Registo: %d, " +
                        "Necessita Manutenção: %s",
                id,
                maxGuests,
                numBeds,
                viewType,
                hasKitchen ? "Sim" : "Não",
                hasBalcony ? "Sim" : "Não",
                numWC,
                pricePerNight,
                status,
                registrationDate != null ? registrationDate.toString() : "N/A",
                idUserRegistration,
                needsMaintenance ? "Sim" : "Não"
        );
    }

}
