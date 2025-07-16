package com.thehotel.model;

import java.time.LocalDate;

public class Reservation {

private int id;
private int idGuest;
private int idReservSuggestion;
private LocalDate checkInDate;
private LocalDate checkOutDate;
private double totalPrice;
private String status;

    public Reservation(int id, int idGuest, int idReservSuggestion, LocalDate checkInDate, LocalDate checkOutDate, double totalPrice, String status) {
        this.id = id;
        this.idGuest = idGuest;
        this.idReservSuggestion = idReservSuggestion;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdGuest() {
        return idGuest;
    }

    public void setIdGuest(int idGuest) {
        this.idGuest = idGuest;
    }

    public int getIdReservSuggestion() {
        return idReservSuggestion;
    }

    public void setIdReservSuggestion(int idReservSuggestion) {
        this.idReservSuggestion = idReservSuggestion;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
                "%nReserva - ID: %d, " +
                        "ID hóspede: %s, " +
                        "ID sugestão reserva: %s, " +
                        "Data check-in: %s, " +
                        "Data check-out: %s, " +
                        "Preço total: %s EUR, " +
                        "Estado: %s",
                id,
                idGuest,
                idReservSuggestion,
                checkInDate,
                checkOutDate,
                totalPrice,
                status
        );
    }
}
