package com.thehotel;

import com.thehotel.model.Manager;
import com.thehotel.model.Reservation;
import com.thehotel.services.*;

import java.time.LocalDate;

//singleton HotelServer
public class HotelServer {
    private static HotelServer hotelServerInstance = null;

    private final AccountService accountService;
    private final RoomService roomService;
    private final MaintenanceService maintenanceService;
    private final HotelService hotelService;
    private final ReservationService reservationService;

    private HotelServer() {
        this.hotelService = new HotelService();
        this.accountService = new AccountService();
        this.reservationService = new ReservationService();

        //creates admin account by default
        Manager admin = new Manager(
                accountService.generateUserId(),
                "Admin Manager",
                "111111111",
                "admin@hotel-infouevora.pt",
                LocalDate.of(2025, 1, 1),  // Exemplo de data de nascimento
                "Rua das Rosas, 23",
                "1000-001",
                "Portugal",
                "910000000",
                "PT50000000000000000000000",
                "123456789",
                this
        );
        admin.setPassword("Admin!1234");
        accountService.addUser(admin);

        this.roomService = new RoomService(admin);
        this.maintenanceService = new MaintenanceService(admin);


    }

    public static HotelServer getInstance() {
        if (hotelServerInstance == null) {
            hotelServerInstance = new HotelServer();
        }
        return hotelServerInstance;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public MaintenanceService getMaintenanceService() {
        return maintenanceService;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }

    public String getHotelInfo(){
        return hotelService.getGeneralInfo();
    }

}
