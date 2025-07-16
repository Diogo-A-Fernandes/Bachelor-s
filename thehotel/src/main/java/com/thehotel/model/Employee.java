package com.thehotel.model;

import com.thehotel.HotelServer;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Employee extends User {
    private String iban;
    private String citizenCard;


    public Employee(int id, String fullName, String nif, String email,
                    LocalDate dataNascimento, String address, String zipCode,
                    String country, String phone, String iban, String citizenCard,
                    HotelServer hotelServer) {
        super(id, fullName, nif, email, dataNascimento, address, zipCode, country, phone,hotelServer);
        this.setRole("employee");
        this.iban = iban;
        this.citizenCard = citizenCard;

    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCitizenCard() {
        return citizenCard;
    }

    public void setCitizenCard(String citizenCard) {
        this.citizenCard = citizenCard;
    }

    /*
     *--------------------------------------------------------------------------------------------
     * ACCOUNT MANAGEMENT
     *--------------------------------------------------------------------------------------------
     */

    // Get Users Personal Information (manager or employee)
    public String getUserPersonalInfo(int userId) throws IllegalAccessException{
        return getHotelServer().getAccountService().getPersonalData(this, userId);
    }

    public String listAllRooms() throws IllegalAccessException {
        return getHotelServer().getRoomService().getAllRooms(this).toString();
    }

    /*
     * --------------------------------------------------------------------------------------------
     * MAINTENANCE MANAGEMENT
     * --------------------------------------------------------------------------------------------
     */

    // List all maintenances
    public String listAllMaintenances() throws IllegalAccessException {
        return getHotelServer().getMaintenanceService().getAllMaintenances(this).toString();
    }

    // List maintenances by status
    public String listMaintenanceByStatus(String status) throws IllegalAccessException {
        return getHotelServer().getMaintenanceService().getMaintenanceByStatus(this, status).toString();
    }

    // Register a new maintenance
    public String registerMaintenance(int roomId, String description) throws IllegalAccessException {
        return getHotelServer().getMaintenanceService().registerMaintenance(this, roomId, description, getHotelServer().getRoomService());
    }

    // Register a maintenance completion
    public String registerMaintenanceCompletion(int maintenanceId, LocalDate conclusionDate) throws IllegalAccessException {
        return getHotelServer().getMaintenanceService().registerMaintenanceCompletion(this, maintenanceId, conclusionDate, getHotelServer().getRoomService());
    }

    // Cancel a maintenance
    public String cancelMaintenance(int maintenanceId) throws IllegalAccessException {
        return getHotelServer().getMaintenanceService().cancelMaintenance(this, maintenanceId, getHotelServer().getRoomService());
    }

    /*
     * --------------------------------------------------------------------------------------------
     * RESERVATION MANAGEMENT
     * --------------------------------------------------------------------------------------------
     */

    public String requestReservationSuggestion(int totalGuests, int totalRooms, List<RoomRequest> roomRequests,
                                               LocalDate checkInDate, LocalDate checkOutDate) throws IllegalArgumentException, IllegalAccessException {
        return getHotelServer().getReservationService().requestReservationSuggestion(
                this, totalGuests, totalRooms, roomRequests, checkInDate, checkOutDate, getHotelServer().getRoomService());
    }

    public String listAllReservationSuggestion() throws IllegalAccessException {
        return getHotelServer().getReservationService().getAllReservSuggestions(this).toString();
    }

    public String makeReservation(int suggestionId, int guestId) throws IllegalAccessException {
        return getHotelServer().getReservationService().makeReservation(this, suggestionId, guestId, getHotelServer().getRoomService(), getHotelServer().getAccountService());
    }

    public String listAllReservations() throws IllegalAccessException {
        return getHotelServer().getReservationService().getAllReservations(this).toString();
    }

    public String cancelReservation(int reservationId) throws IllegalAccessException {
        return getHotelServer().getReservationService().cancelReservation(this, reservationId);
    }

    public String doCheckIn(int reservationId) throws IllegalAccessException {
        return getHotelServer().getReservationService().doCheckIn(this, reservationId);
    }

    public String doCheckOut(int reservationId) throws IllegalAccessException {
        return getHotelServer().getReservationService().doCheckOut(this, reservationId, getHotelServer().getRoomService(), getHotelServer().getMaintenanceService());
    }

}
