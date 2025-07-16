package com.thehotel.model;

import com.thehotel.HotelServer;
import com.thehotel.services.AccountService;
import com.thehotel.services.RoomService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Manager extends User {
    private String iban;
    private String citizenCard;

    public Manager(int id, String fullName, String nif, String email, LocalDate birthDate,
                   String address, String zipCode, String country, String phone, String iban,
                   String citizenCard, HotelServer hotelServer) {
        super(id, fullName, nif, email, birthDate, address, zipCode, country, phone,hotelServer);
        this.setRole("manager");
        this.iban = iban;
        this.citizenCard = citizenCard;
    }

    // Getters and Setters
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

    // List all users
    public String listAllUsers() throws IllegalAccessException {
        return getHotelServer().getAccountService().getAllUsers(this).toString();
    }

    // Create a staff account (manager or employee)
    public String createStaffAccount(String role, String fullName, String nif, String email, LocalDate birthDate,
                                     String address, String zipCode, String country, String phone, String iban,
                                     String citizenCard) throws IllegalAccessException {
        return getHotelServer().getAccountService().createStaffAccount(this, role, fullName, nif, email, birthDate, address, zipCode, country, phone, iban, citizenCard, getHotelServer());
    }

    // Get Users Personal Information (manager or employee)
    public String getUserPersonalInfo(int userId) throws IllegalAccessException{
        return getHotelServer().getAccountService().getPersonalData(this, userId);
    }


    // Get Users Personal Information (manager)
    public String changeUserPersonalData(int userId,String nif, String email, String fullName,
                                         LocalDate birthDate, String address, String zipCode, String country, String phone) throws IllegalAccessException{

        return getHotelServer().getAccountService().changeUserPersonalData(this, userId, nif,  email,  fullName,
                 birthDate,  address,  zipCode,  country,  phone);
    }

    /*
     *--------------------------------------------------------------------------------------------
     * ROOM MANAGEMENT
     *--------------------------------------------------------------------------------------------
     */

    // List all rooms
    public String listAllRooms() throws IllegalAccessException {
        return getHotelServer().getRoomService().getAllRooms(this).toString();
    }

    // Register a new room
    public String registerRoom(int maxGuests, int numBeds, String viewType, boolean hasKitchen,
                               boolean hasBalcony, int numWC, double pricePerNight) throws IllegalAccessException {
        return getHotelServer().getRoomService().registerRoom(this, maxGuests, numBeds, viewType, hasKitchen, hasBalcony, numWC, pricePerNight);
    }

    // Edit an existing room
    public String editRoom(int roomId, Integer maxGuests, Integer numBeds, String viewType,
                           Boolean hasKitchen, Boolean hasBalcony, Integer numWC, Double pricePerNight) throws IllegalAccessException {
        return getHotelServer().getRoomService().editRoom(this, roomId, maxGuests, numBeds, viewType,
                hasKitchen, hasBalcony, numWC, pricePerNight);
    }

    // Remove a room
    public String removeRoom(int roomId) throws IllegalAccessException {
        return getHotelServer().getRoomService().removeRoom(this, roomId);
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

    //Cancel a maintenance
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
