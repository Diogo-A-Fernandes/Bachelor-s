package com.thehotel.model;

import com.thehotel.HotelServer;

import java.time.LocalDate;

public class Guest extends User {


    public Guest(int id, String fullName, String nif, String email, LocalDate birthDate, String address, String zipCode, String country, String phone, HotelServer hotelServer) {
        super(id, fullName, nif, email, birthDate, address, zipCode, country, phone,hotelServer);
    }
}
