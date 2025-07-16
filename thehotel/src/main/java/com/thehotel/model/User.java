package com.thehotel.model;

import com.thehotel.HotelServer;

import java.time.LocalDate;
import java.util.LinkedList;

public class User {
    private int id;
    private String email;
    private String password;
    private String fullName;
    private String nif;
    private LocalDate birthDate;
    private String phone;
    private String country;
    private String address;
    private String zipCode;
    private String role;
    private String status;
    private HotelServer hotelServer;

    //private LinkedList<Reservations> reservations;


    public User(int id, String fullName, String nif, String email, LocalDate birthDate, String address, String zipCode, String country, String phone, HotelServer hotelServer) {
        this.id = id;
        this.fullName = fullName;
        this.nif = nif;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
        this.zipCode = zipCode;
        this.country = country;
        this.phone = phone;
        this.role = "guest";
        this.status = "active";
        this.hotelServer = hotelServer;
        /*this.reservations = new LinkedList<>();*/}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public HotelServer getHotelServer() {
        return hotelServer;
    }

    public void setHotelServer(HotelServer hotelServer) {
        this.hotelServer = hotelServer;
    }

 /*   public LinkedList<Reservations> getReservations() {
        return reservations;
    }

    public void addReservation(Reservations reservation) {
        this.reservations.add(reservation);
    }
*/
    @Override
    public String toString() {
        return String.format(
                "%nUtilizador - " +
                        "ID: %d, " +
                        "Nome Completo: %s, " +
                        "Email: %s, " +
                        "NIF: %s, " +
                        "Data Nascimento: %s, " +
                        "Telefone: %s, " +
                        "País: %s, " +
                        "Morada: %s, " +
                        "Código Postal: %s, " +
                        "Função: %s, " +
                        "Estado: %s ",
                id, fullName, email, nif, birthDate, phone, country, address, zipCode, role, status
        );
    }

    public String personalInfo() {
        return String.format(
                "Utilizador - " +
                        "Nome Completo: %s, " +
                        "Email: %s, " +
                        "NIF: %s, " +
                        "Data de Nascimento: %s, " +
                        "Telefone: %s, " +
                        "País: %s, " +
                        "Morada: %s, " +
                        "Código Postal: %s%n" ,
                        fullName, email, nif, birthDate, phone, country, address, zipCode
        );
    }


    public String updatePersonalData(String newNif, String newEmail, String newFullName, LocalDate newBirthDate, String newAddress,
    String newZipCode, String newCountry, String newPhone) throws IllegalAccessException {

        return getHotelServer().getAccountService().changePersonalData(this, newNif, newEmail, newFullName, newBirthDate, newAddress, newZipCode, newCountry, newPhone);
    }

}
