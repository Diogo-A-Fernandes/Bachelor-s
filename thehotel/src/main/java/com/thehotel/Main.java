package com.thehotel;
import com.thehotel.model.*;
import com.thehotel.services.ReservationService;
import com.thehotel.services.RoomService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        HotelServer hotelServer = HotelServer.getInstance();
        try {
            // Obtains admin account
            Manager admin = (Manager) hotelServer.getAccountService().getUserById(0);

            // ------------------------------------U1----------------------------------------
            //----------------------------Consultar informações gerais-----------------------
            //System.out.println("------------------------U1: Consultar informações gerais-----------------------");
            System.out.println(hotelServer.getHotelInfo());
            System.out.println();


            //--------------------------------------U2--------------------------------------
            //----------------------------Criar conta de Hóspede ---------------------------

            System.out.println("----------------------------U2: Criar conta de Hóspede ---------------------------");
            String createGuestResp = hotelServer.getAccountService().createGuestAccount(
                    "Rita Casa-Velha",
                    "123456789",
                    "l47345@alunos.uevora.pt",
                    LocalDate.of(1994, 6, 23),
                    "Rua Luís de Camões, 5",
                    "7005-540",
                    "Portugal",
                    "926548970",
                    hotelServer
            );
            System.out.println(createGuestResp);
            System.out.println();
//            System.out.println("Lista de hóspedes: " + admin.listAllUsers());
//            System.out.println();

            Guest guest = (Guest) hotelServer.getAccountService().getUserById(1);

            //--------------------------------------U3--------------------------------------
            //----------------------------Criar conta de Staff -----------------------------

            System.out.println("----------------------------U3: Criar conta de Staff -----------------------------");
            //Manager creating another Manager Account
            String createManagerResp = admin.createStaffAccount("manager", "Rui Dias Barata", "456123789", "ruidiasbarata@hotel-infouevora.pt", LocalDate.of(1995, 1, 2), "Rua da Tulipas, 13", "7006-310", "portugal", "965965965", "PT50003545213657552541232", "258369147");
            System.out.println(createManagerResp);
            System.out.println();
//            System.out.println("Lista de utilizadores = " + admin.listAllUsers());
//            System.out.println();
//
//            //Manager creating another Employee Account
            String createEmployeeResp = admin.createStaffAccount("employee", "Ricardo Dias Pinheiro", "456523799", "ricardopinheiro@hotel-infouevora.pt", LocalDate.of(1966, 10, 25), "Rua D. Afonso Henriques, 13", "5421-001", "portugal", "915478963", "PT50003545213657500001232", "147854987");
            System.out.println(createEmployeeResp);
            System.out.println();
            System.out.println("Lista de utilizadores = " + admin.listAllUsers());
            System.out.println();

            // Obtains employee and manager account
            Employee employee = (Employee) hotelServer.getAccountService().getUserById(3);
            Manager manager = (Manager) hotelServer.getAccountService().getUserById(2);

            //--------------------------------------U6--------------------------------------
            //----------------------------Consultar dados pessoais--------------------------

            System.out.println("----------------------------U6: Consultar dados pessoais--------------------------");
            System.out.println(admin.personalInfo());
            System.out.println();
//            System.out.println(employee.personalInfo());
//            System.out.println();

            //--------------------------------------U7--------------------------------------
            //---------------------Consultar dados pessoais de um utilizador ---------------

            System.out.println("---------------------U7: Consultar dados pessoais de um utilizador ---------------");
            System.out.println(admin.getUserPersonalInfo(3));
            System.out.println();
//            System.out.println(employee.getUserPersonalInfo(1));
//            System.out.println();
//            System.out.println(manager.getUserPersonalInfo(6));
//            System.out.println();

            //--------------------------------------U8--------------------------------------
            //------------------------------Alterar dados pessoais--------------------------
            System.out.println("------------------------------U8: Alterar dados pessoais--------------------------");
            System.out.println(admin.updatePersonalData("858558585",null,null, LocalDate.of(1993,6,23),null,null,null,null));
            System.out.println(admin.personalInfo());
            System.out.println();

            //--------------------------------------U9--------------------------------------
            //----------------------Alterar dados de outro utilizador ----------------------

            System.out.println("----------------------U9: Alterar dados de outro utilizador ----------------------");
            System.out.println(admin.changeUserPersonalData(1,"252525252",null,null,null,null,null,null,"946985852"));
            System.out.println(admin.getUserPersonalInfo(1));
            System.out.println();

            //--------------------------------------U10--------------------------------------
            //--------------------------------Registar quarto--------------------------------
            System.out.println("--------------------------------U10: Registar quarto--------------------------------");
            String roomRegResp= admin.registerRoom(
                    4,
                    2,
                    "mar",
                    true,
                    true,
                    1,
                    120.00
            );

            admin.registerRoom(
                    6,
                    3,
                    "serra",
                    true,
                    true,
                    2,
                    180.00
            );

            System.out.println(roomRegResp);
            System.out.println();
            System.out.println("Lista de quartos: " + admin.listAllRooms());
            System.out.println();

            //--------------------------------------U11--------------------------------------
            //----------------------------------Editar quarto--------------------------------
            System.out.println("---------------------------------U11: Editar quarto--------------------------------");
            String roomEditionResp= admin.editRoom(0, 10, 5, "serra", true, true, 2, 250.0);
            System.out.println(roomEditionResp);
            System.out.println();
            System.out.println("Lista de quartos: " + admin.listAllRooms());
            System.out.println();

            //--------------------------------------U12--------------------------------------
            //----------------------------------Remover quarto-------------------------------
            System.out.println("----------------------------------U12: Remover quarto-------------------------------");
            String roomRemoveResp = admin.removeRoom(0);
            System.out.println("Lista de quartos: " + admin.listAllRooms());
            System.out.println();

            //--------------------------------------U13--------------------------------------
            //--------------------------U13: Registar manutenção a realizar------------------

            System.out.println("--------------------------U13: Registar manutenção a realizar------------------");
            String maintenanceRegResp = admin.registerMaintenance(1, "Limpeza");
            System.out.println(maintenanceRegResp);
            System.out.println();
            System.out.println("Lista de manutenções: " + admin.listAllMaintenances());
            System.out.println();

            String maintenanceRegResp2 = employee.registerMaintenance(1, "Trocar lâmpadas");
            System.out.println(maintenanceRegResp2);
            System.out.println();
            System.out.println("Lista de manutenções: " + employee.listAllMaintenances());
            System.out.println();
            System.out.println("Lista de quartos: " + admin.listAllRooms());
            System.out.println();
            //--------------------------------------U14--------------------------------------
            //--------------------------U14: Registar manutenção finalizada------------------

            System.out.println("--------------------------U14: Registar manutenção finalizada------------------");
            //System.out.println(employee.registerMaintenanceCompletion(0, LocalDate.now()));
            System.out.println(admin.registerMaintenanceCompletion(1, LocalDate.now()));
            System.out.println();
            //System.out.println("Lista de quartos: " + admin.listAllRooms());
            //System.out.println();
            //--------------------------------------U15--------------------------------------
            //---------------------------U15: Cancelar manutenção----------------------------

            System.out.println("---------------------------U15: Cancelar manutenção----------------------------");
            System.out.println(admin.cancelMaintenance(0));
            System.out.println();
            //System.out.println(employee.cancelMaintenance(1));
            System.out.println("Lista de quartos: " + admin.listAllRooms());
            System.out.println();

            //--------------------------------------U16--------------------------------------
            //------------------------U16: Consultar lista de manutenções--------------------

            System.out.println("------------------------U16: Consultar lista de manutenções--------------------");
            System.out.println("Lista de manutenções pendentes: " + employee.listMaintenanceByStatus("pendent"));
            System.out.println();
            System.out.println("Lista de manutenções canceladas: " + admin.listMaintenanceByStatus("cancelled"));
            System.out.println();
            System.out.println("Lista de manutenções concluídas: " + employee.listMaintenanceByStatus("completed"));
            System.out.println();

            //-------------------------------------U17----------------------------------------
            //------------------------U17: Solicitar sugestão de reserva----------------------

            System.out.println("------------------------U17: Solicitar sugestão de reserva----------------------");
            // rooms examples
            admin.registerRoom(2, 1, "mar", true, true, 1, 100.0);
            admin.registerRoom(2, 2, "serra", false, false, 1, 80.0);
            admin.registerRoom(1, 1, "mar", false, true, 1, 50.0);
            admin.registerRoom(2, 1, "mar", true, true, 1, 120.0);

            // RoomRequests list creation
            List<RoomRequest> roomRequests = new ArrayList<>();
            roomRequests.add(new RoomRequest(2, 1, "mar", true, true, 1)); // Quarto duplo com vista mar e cozinha
            roomRequests.add(new RoomRequest(1, 1, "serra", false, false, 1)); // Quarto individual com vista serra

            String suggestion = employee.requestReservationSuggestion(
                    3, // totalGuests
                    2, // totalRooms
                    roomRequests, // rooms criteria
                    LocalDate.of(2025, 3, 10), // Check-in date
                    LocalDate.of(2025, 3, 15) // Check-out date
            );
            System.out.println("Lista de quartos: " + admin.listAllRooms());
            System.out.println(suggestion);
            System.out.println();

            //-------------------------------------U18----------------------------------------
            //---------------------------U18: Efetuar reserva---------------------------------

            System.out.println("---------------------------U18: Efetuar reserva---------------------------------");
            String reservation = admin.makeReservation(0, guest.getId());
            System.out.println(reservation);

            System.out.println("Lista de reservas atualizada: " + employee.listAllReservations());
            System.out.println();

            //-------------------------------------U20----------------------------------------
            //---------------------------U20: Cancelar reserva--------------------------------

            //System.out.println("---------------------------U20: Cancelar reserva--------------------------------");
            /*String cancelReservation = employee.cancelReservation(0);
            //String cancelReservation = employee.cancelReservation(1);
            System.out.println(cancelReservation);
            System.out.println("Lista de reservas após cancelamento: " + employee.listAllReservations());
            System.out.println();*/

            //-------------------------------------U21----------------------------------------
            //---------------------------U21: Check-in de reserva-----------------------------

            System.out.println("---------------------------U21: Check-in de reserva-----------------------------");
            String checkIn = admin.doCheckIn(0);
            System.out.println(checkIn);
            System.out.println("Lista de reservas após check-in: " + employee.listAllReservations());
            System.out.println("Lista de quartos após check-in:  " + admin.listAllRooms());
            System.out.println();

            //-------------------------------------U22----------------------------------------
            //---------------------------U22: Check-out de reserva----------------------------

            System.out.println("---------------------------U22: Check-out de reserva----------------------------");
            String checkOut = employee.doCheckOut(0);
            System.out.println("Lista de reservas após check-out: " + employee.listAllReservations());
            System.out.println("Lista de quartos após check-out:  " + employee.listAllRooms());
            System.out.println("Lista de manutenções: " + employee.listAllMaintenances());
            System.out.println();
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }

    }
}