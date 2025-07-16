package com.thehotel;

import com.thehotel.model.*;
import com.thehotel.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.LinkedList;

public class RoomServiceTest {
    private RoomService roomService;
    private Manager admin;
    private Guest guest;
    private Employee employee;
    private HotelServer hotelServer;
    
    //Data that we need before start tests
    @BeforeEach
    void setUp() throws IllegalAccessException {
        admin = new Manager(0, "Admin", "123456789", "admin@example.com", LocalDate.of(1980, 1, 1), "Rua A",
                "1234-567", "Portugal", "912345678", "PT50001234567890123456789", "12345678", null);
        roomService = new RoomService(admin);

        employee = new Employee(1, "Employee", "987654321", "employee@example.com", LocalDate.of(1990, 2, 1), "Rua B",
                "2345-123", "Portugal", "917654321", "PT50001234567890123456788", "87654321", null);

        guest = new Guest(2, "Guest", "123456781", "guest@example.com", LocalDate.of(1992, 4, 5), "Rua C",
                "2330-123", "Portugal", "9123456781", null);

        roomService.registerRoom(admin, 4, 2, "Mar", true, false, 1, 100.0);
        roomService.registerRoom(admin, 2, 1, "Serra", false, true, 1, 80.0);

    }

    //------------------------
    //GenerateRoomById Method-
    //------------------------

    @Test
    void GenerateRoomIdTest(){
        int id1 = roomService.generateRoomId();
        int id2 = roomService.generateRoomId();
        assertEquals(2, id1);
        assertEquals(3, id2);
    }

    //-------------------
    //getAllRooms Method-
    //-------------------

    //Success, manager can access the rooms list
    @Test
    void getAllRoomsTestAsManager() throws IllegalAccessException {
        LinkedList<Room> rooms = roomService.getAllRooms(admin);
        assertNotNull(rooms);
        assertEquals(2, rooms.size());
    }

    //Success, employee can access the rooms list
    @Test
    void getAllRoomsTestAsEmployee() throws IllegalAccessException {
        LinkedList<Room> rooms = roomService.getAllRooms(employee);
        assertNotNull(rooms);
        assertEquals(2, rooms.size());
    }

    //Exception, user without permissions to access rooms list
    @Test
    void getAllRoomsTestAsGuest() throws IllegalAccessException {
        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> {
            roomService.getAllRooms(guest);
        });
        assertEquals("Acesso não autorizado: Apenas gestores ou funcionários podem visualizar a lista de quartos.", exception.getMessage());
    }

    //-------------------
    //getRoomById Method-
    //-------------------

    //Principal case, success case, the id room exist
    @Test
    void getRoomByIdTestAsAdmin() throws IllegalAccessException {
        Room room = roomService.getRoomById(admin, 0);
        assertNotNull(room);
        assertEquals(0, room.getId());
    }

    //Exception, the id room not exist
    @Test
    void getRoomByIdTestIdNotExist() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            roomService.getRoomById(admin, 10);
        });
        assertEquals("Quarto com ID 10 não encontrado.", exception.getMessage());
    }

    //Exceptions, not admin
    @Test
    void getRoomByIdTestAsEmployee() {
        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> {
            roomService.getRoomById(employee, 0);
        });
        assertEquals("Acesso não autorizado: Apenas gestores podem obter um quarto por ID.", exception.getMessage());
    }

    //Exceptions, not admin
    @Test
    void getRoomByIdTestAsGuest(){
        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> {
            roomService.getRoomById(guest, 0);
        });
        assertEquals("Acesso não autorizado: Apenas gestores podem obter um quarto por ID.", exception.getMessage());
    }

    //--------------------
    //registerRoom Method-
    //--------------------

    //Success case, the admin, register a valid room
    @Test
    void registerRoomTest() throws IllegalAccessException {
        String result = roomService.registerRoom(admin, 4, 2, "Serra", true, true, 1, 150.0);
        assertNotNull(result);

        assertTrue(result.matches("Quarto criado com sucesso com o ID 2"));
        assertEquals(3, roomService.getAllRooms(admin).size());

        Room resultRoom = roomService.getRoomById(admin, 2);
        assertEquals(4, resultRoom.getMaxGuests());
        assertEquals(2, resultRoom.getNumBeds());
        assertEquals("Serra", resultRoom.getViewType());
        assertTrue(resultRoom.isHasKitchen());
        assertTrue(resultRoom.isHasBalcony());
        assertEquals(1, resultRoom.getNumWC());
        assertEquals(150.0, resultRoom.getPricePerNight());
    }

    //Permission denied, the user without permissions try to register a room
    @Test
    void registerRoomTestWithNoPermissions() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            roomService.registerRoom(guest, 4, 2, "Mar", true, false, 1, 100.0);
        });
        assertEquals("Acesso não autorizado: Apenas gestores podem editar quartos.", exception.getMessage());
    }

    //Invalid data to register a room
    @Test
    void registerRoomTestInvalidData() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            roomService.registerRoom(admin, 0, 2, "Mar", true, false, 1, -50.0);
        });
        assertEquals("Dados fornecidos para registo do quarto são inválidos.", exception.getMessage());
    }

    //----------------
    //editRoom Method-
    //----------------

    //Success case, admin can edit a room
    @Test
    void editRoomTest() throws IllegalAccessException {
        Room room = roomService.getAllRooms(admin).getFirst();
        int roomId = room.getId();

        System.out.println(room);
        String result = roomService.editRoom(admin, roomId, 6, 3, "Serra", true, true,2, 200.0);
        System.out.println(room);

        assertEquals(6, room.getMaxGuests());
        assertEquals(3, room.getNumBeds());
        assertEquals("Serra", room.getViewType());
        assertTrue(room.isHasKitchen());
        assertTrue(room.isHasBalcony());
        assertEquals(2, room.getNumWC());
        assertEquals(200.0, room.getPricePerNight());

        assertEquals("Quarto com o ID " + roomId + " editado com sucesso.", result);
    }

    //Access denied, user without permissions -> employee
    @Test
    void editRoomTestAsEmployee() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            roomService.editRoom(employee, 0, null, null, null, null, null, null, null);
        });

        assertEquals("Acesso não autorizado: Apenas gestores podem editar quartos.", exception.getMessage());
    }

    //Access denied, user without permissions -> guest
    @Test
    void editRoomTestAsGuest() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            roomService.editRoom(guest, 0, null,null,null,null,null,null,null);
        });

        assertEquals("Acesso não autorizado: Apenas gestores podem editar quartos.", exception.getMessage());
    }

    //Non-existing ID
    @Test
    void editRoomTestNonExistingId() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            roomService.editRoom(admin, 999, null, null, null, null, null, null, null);
        });

        assertEquals("Quarto com ID 999 não encontrado.", exception.getMessage());
    }

    //Invalid data
    @Test
    void editRoomTestInvalidData() throws IllegalArgumentException {
        //Invalid maxGuests
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            roomService.editRoom(admin, 0, -1, null,null,null,null,null,null);
        });
        assertEquals("Número máximo de hóspedes inválido. Insira um número superior a 0 (zero).", exception.getMessage());

        //Invalid numBeds
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            roomService.editRoom(admin, 0, null, -1, null,null,null,null,null);
        });
        assertEquals("Número de camas inválido. Insira um número superior a 0 (zero).", exception1.getMessage());

        //Invalid viewType
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            roomService.editRoom(admin, 0, null, null, "Invalid", null,null,null,null);
        });
        assertEquals("Tipo de vista inválido. Insira 'mar' ou 'serra'", exception2.getMessage());

        //Invalid numWC
        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            roomService.editRoom(admin, 0, null, null, null, null, null,-1,null);
        });
        assertEquals("Número de WCs inválido. Insira um número superior a 0 (zero).", exception3.getMessage());

        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> {
            roomService.editRoom(admin, 0, null, null, null, null, null, null, -1.0);
        });
        assertEquals("Preço por noite inválido. Insira um valor superior a 0.0 EUR.", exception4.getMessage());

    }
    //------------------
    //removeRoom Method-
    //------------------

    //Success case, admin can remove a room
    @Test
    void removeRoomTest() throws IllegalAccessException {
        String result = roomService.removeRoom(admin, 0);

        assertEquals("Quarto com ID 0 removido com sucesso.", result);
        assertEquals("inactive", roomService.getRoomById(admin, 0).getStatus());
    }

    //Access denied, user without permissions -> Employed
    @Test
    void removeRoomTestAsEmployee() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            roomService.removeRoom(employee, 0);
        });

        assertEquals("Acesso não autorizado: Apenas gestores podem remover quartos.", exception.getMessage());
    }

    //Access denied, user without permissions -> guest
    @Test
    void removeRoomTestAsGuest() throws IllegalAccessException {

        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            roomService.removeRoom(guest, 0);
        });

        assertEquals("Acesso não autorizado: Apenas gestores podem remover quartos.", exception.getMessage());

    }

    //Invalid operation to remove room when room is occupied
    @Test
    void removeOccupiedRoomTest() throws IllegalAccessException {
        Room room = roomService.getAllRooms(admin).getFirst();
        room.setStatus("occupied");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            roomService.removeRoom(admin, 0);
        });

        assertEquals("Erro: O quarto não pode ser removido porque está ocupado ou inativo.", exception.getMessage());

    }

    //Invalid operation to remove room when room is occupied
    @Test
    void removeInactiveRoomTest() throws IllegalAccessException {
        Room room = roomService.getAllRooms(admin).getFirst();
        room.setStatus("inactive");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            roomService.removeRoom(admin, 0);
        });

        assertEquals("Erro: O quarto não pode ser removido porque está ocupado ou inativo.", exception.getMessage());

    }

    //-----------------------------------
    //updateRoomMaintenanceStatus Method-
    //-----------------------------------

    //Success case, admin can update the maintenance status
    @Test
    void updateRoomMaintenanceStatusTestAsManager() throws IllegalAccessException {
        roomService.updateRoomMaintenanceStatus(admin, 0, true);

        //verify if the status has been changed
        Room room = roomService.getRoomById(admin, 0);
        assertTrue(room.isNeedsMaintenance());
    }
    //Success case, employee can update the maintenance status
    @Test
    void updateRoomMaintenanceStatusTestAsEmployee() throws IllegalAccessException {
        roomService.updateRoomMaintenanceStatus(employee, 0, true);

        //verify if the status has been changed
        Room room = roomService.getRoomById(admin, 0);
        assertTrue(room.isNeedsMaintenance());
    }

    //Exception, permissions denied, user without permissions
    @Test
    void updateRoomMaintenanceStatusTestAsNonPermissionUser() throws IllegalAccessException {
        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> {
            roomService.updateRoomMaintenanceStatus(guest, 0, true);
        });

        assertEquals("Acesso não autorizado: Apenas gestores ou funcionários podem atualizar o estado de manutenção do quarto.", exception.getMessage());
    }

    //Trying to update a not existent room
    @Test
    void updateRoomMaintenanceStatusTestForNonexistentRoom() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                roomService.updateRoomMaintenanceStatus(admin, 10, true));
        assertEquals("Quarto com ID 10 não encontrado.", exception.getMessage());
    }

    //Verify the status set to false
    @Test
    void updateRoomMaintenanceStatusTestSetToFalse() throws IllegalAccessException {
        Room room = roomService.getRoomById(admin, 0);
        roomService.updateRoomMaintenanceStatus(admin, 0, true);
        assertTrue(room.isNeedsMaintenance());
        roomService.updateRoomMaintenanceStatus(admin, 0, false);
        assertFalse(room.isNeedsMaintenance());
    }

    //------------------------
    //validateRoomData Method-
    //------------------------

    //Success case, all data is valid
    @Test
    void validateRoomDataTest() {
        assertTrue(roomService.validateRoomData(4, 2, "Serra", 1, 150.0));
    }

    //Invalid maxGuests
    @Test
    void validateRoomDataTestInvalidGuest() {
        assertFalse(roomService.validateRoomData(0, 2, "Serra", 1, 150.0));
        assertFalse(roomService.validateRoomData(-1, 2, "Serra", 1, 150.0));
    }

    //Invalid numBeds
    @Test
    void validateRoomDataTestInvalidNumBed() {
        assertFalse(roomService.validateRoomData(1, 0, "Serra", 1, 150.0));
        assertFalse(roomService.validateRoomData(1, -1, "Serra", 1, 150.0));
    }

    //Invalid Type
    @Test
    void validateRoomDataTestInvalidType() {
        assertFalse(roomService.validateRoomData(1,1,"Rio", 1, 150.0));
    }

    //Invalid NumWC
    @Test
    void validateDataTestInvalidNumWC() {
        assertFalse(roomService.validateRoomData(1, 1, "Mar", 0, 150.0));
        assertFalse(roomService.validateRoomData(1, 1, "Mar", -1, 150.0));
    }

    //Invalid Price
    @Test
    void validateDataTestInvalidPrice() {
        assertFalse(roomService.validateRoomData(1, 1, "Mar", 1, 0));
        assertFalse(roomService.validateRoomData(1, 1, "Mar", 1, -150.0));
    }
}