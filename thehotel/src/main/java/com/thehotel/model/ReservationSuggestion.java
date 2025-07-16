package com.thehotel.model;

import java.time.LocalDate;
import java.util.List;

public class ReservationSuggestion {

    private int id;
    private int totalGuest;
    private int totalRooms;
    private List<RoomRequest> requestRooms;
    private List<Room> sugestionRooms;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;
    private boolean used; //boolean that indicates whether the booking suggestion is already associated with a reservation

    public ReservationSuggestion(int id, int totalGuest, int totalRooms, List<RoomRequest> requestRooms, List<Room> sugestionRooms, LocalDate checkInDate, LocalDate checkOutDate, double totalPrice) {
        this.id = id;
        this.totalGuest = totalGuest;
        this.totalRooms = totalRooms;
        this.requestRooms = requestRooms;
        this.sugestionRooms = sugestionRooms;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
        this.used = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalGuest() {
        return totalGuest;
    }

    public void setTotalGuest(int totalGuest) {
        this.totalGuest = totalGuest;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public List<RoomRequest> getRequestRooms() {
        return requestRooms;
    }

    public void setRequestRooms(List<RoomRequest> requestRooms) {
        this.requestRooms = requestRooms;
    }

    public List<Room> getSugestionRooms() {
        return sugestionRooms;
    }

    public void setSugestionRooms(List<Room> sugestionRooms) {
        this.sugestionRooms = sugestionRooms;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public String toString() {
        StringBuilder roomsDetails = new StringBuilder();
        if (sugestionRooms != null) {
            for (Room room : sugestionRooms) {
                roomsDetails.append(room.toString());
            }
        }

        assert sugestionRooms != null;
        return String.format(
                "%nSugestão Reserva%n" +
                        "ID: %d, " +
                        "Total hóspedes: %d, " +
                        "Total de Quartos: %d, " +
                        "Datas Reserva: %s até %s, " +
                        "Preço Total: %.2fEUR, " +
                        "%nQuartos Sugeridos:%s",
                id,
                totalGuest,
                totalRooms,
                checkInDate.toString(),
                checkOutDate.toString(),
                totalPrice,
                roomsDetails.toString().isEmpty() ? "Nenhum quarto disponível de momento.\n" : roomsDetails.toString()
        );
    }


}
