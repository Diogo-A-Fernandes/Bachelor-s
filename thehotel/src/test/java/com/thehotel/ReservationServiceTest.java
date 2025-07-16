package com.thehotel;

import com.thehotel.model.*;
import com.thehotel.services.AccountService;
import com.thehotel.services.MaintenanceService;
import com.thehotel.services.ReservationService;
import com.thehotel.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ReservationServiceTest {
    private ReservationService reservationService;
    private RoomService roomService;
    private MaintenanceService maintenanceService;
    private AccountService accountService;
    private Manager manager;
    private Guest guest;
    private Employee employee;

    @BeforeEach
    void setUp() throws Exception {
        manager = new Manager(0, "Admin", "123456789", "admin@example.com", LocalDate.of(1980, 1, 1), "Rua A",
                "1234- 567", "Portugal", "912345678", "PT50001234567890123456789", "12345678", null);

        //employee
        employee = new Employee(1, "Employee", "987654321", "employee@example.com", LocalDate.of(1990, 2, 1), "Rua B",
                "2345- 123", "Portugal", "917654321", "PT50001234567890123456788", "87654321", null);

        //guest
        guest = new Guest(2, "Guest", "123456781", "guest@example.com", LocalDate.of(1992, 4, 5), "Rua C",
                "2330-123", "Portugal", "9123456781", null);

        roomService = new RoomService(manager);
        reservationService = new ReservationService();
        maintenanceService = new MaintenanceService(manager);
        accountService = new AccountService();

        accountService.createGuestAccount("Guest", "123456781", "guest1@example.com", LocalDate.of(1994, 4, 5), "Rua C",
                "2330-123", "Portugal", "912345678", null);
        accountService.createGuestAccount("Guest2", "125487999", "guest2@example.com", LocalDate.of(1992, 4, 5), "Rua D",
                "2330-111", "Portugal", "910000002", null);

        roomService.registerRoom(manager, 2, 2, "Mar", true, true, 2, 100.0);
        roomService.registerRoom(manager, 2, 1, "Serra", false, true, 1, 150.0);
        roomService.registerRoom(manager, 2, 2, "Mar", true, false, 2, 80.0);

        //----------------------------------------------------------------------------------------

        //creating a room and making one reservation from 5/5/2025 until 10/5/2025
        roomService.registerRoom(manager, 3, 2, "Mar", true, true, 2, 120.0);
        int totalGuests = 3;
        int totalRooms = 1;
        LocalDate checkInDate = LocalDate.of(2025, 5, 5);
        LocalDate checkOutDate = LocalDate.of(2025, 5, 10);

        List<RoomRequest> roomRequests = List.of(
                new RoomRequest(3, 2, "Mar", true, true, 2)
        );
        reservationService.requestReservationSuggestion(manager, totalGuests, totalRooms, roomRequests, checkInDate, checkOutDate, roomService);
        reservationService.makeReservation(manager, 0, 0, roomService, accountService);

        //-----------------------------------------------------------------------------------------------
        //creating a ReservationSuggestion not used
        List<RoomRequest> roomRequests2 = List.of(
                new RoomRequest(2, 1, "Serra", false, true, 1)
        );
        reservationService.requestReservationSuggestion(manager, 2, 1, roomRequests2, checkInDate, checkOutDate, roomService);
    }

    //-----------------------------
    //requestReservationSuggestion-
    //-----------------------------

    //Success case, compatibles rooms
    @Test
    void requestReservationSuggestionTest() throws IllegalAccessException {
        //given
        int totalGuests = 4;
        int totalRooms = 2;
        LocalDate checkInDate = LocalDate.of(2025, 5, 5);
        LocalDate checkOutDate = LocalDate.of(2025, 5, 10);

        List<RoomRequest> roomRequests = List.of(
                new RoomRequest(2, 2, "Mar", true, true, 2),
                new RoomRequest(2, 1, "Serra", false, true, 1)
        );

        assertEquals(4, roomService.getAllRooms(manager).size());

        //when
        String result = reservationService.requestReservationSuggestion(manager, totalGuests, totalRooms, roomRequests, checkInDate, checkOutDate, roomService);

        //then
        System.out.println(result);

        ReservationSuggestion reservSuggestResult = reservationService.getReservSuggestionById(2);

        assertEquals(2, reservSuggestResult.getId());
        assertEquals(4, reservSuggestResult.getTotalGuest());
        assertEquals(2, reservSuggestResult.getTotalRooms());
        assertEquals(checkInDate, reservSuggestResult.getCheckInDate());
        assertEquals(checkOutDate, reservSuggestResult.getCheckOutDate());
        assertEquals(1250.0, reservSuggestResult.getTotalPrice());
        assertFalse(reservSuggestResult.isUsed());
        assertEquals(roomRequests, reservSuggestResult.getRequestRooms());

        //checks suggested rooms
        List<Room> sugestionRooms = reservSuggestResult.getSugestionRooms();
        assertNotNull(sugestionRooms);
        assertEquals(2, sugestionRooms.size());
        //what we request
        RoomRequest roomRequest1 = roomRequests.get(0);
        RoomRequest roomRequest2 = roomRequests.get(1);
        //what we got
        Room roomSuggestion1 = sugestionRooms.get(0);
        Room roomSuggestion2 = sugestionRooms.get(1);


        //checks room suggestion 1
        assertEquals(0, roomSuggestion1.getId());
        assertEquals(roomRequest1.getNumGuests(), roomSuggestion1.getMaxGuests());
        assertEquals(roomRequest1.getNumBeds(), roomSuggestion1.getNumBeds());
        assertEquals(roomRequest1.getViewType(), roomSuggestion1.getViewType());
        assertEquals(roomRequest1.isHasKitchen(), roomSuggestion1.isHasKitchen());
        assertEquals(roomRequest1.isHasBalcony(), roomSuggestion1.isHasBalcony());
        assertEquals(roomRequest1.getNumWC(), roomSuggestion1.getNumWC());

        //checks room suggestion 2
        assertEquals(1, roomSuggestion2.getId());
        assertEquals(roomRequest2.getNumGuests(), roomSuggestion2.getMaxGuests());
        assertEquals(roomRequest2.getNumBeds(), roomSuggestion2.getNumBeds());
        assertEquals(roomRequest2.getViewType(), roomSuggestion2.getViewType());
        assertEquals(roomRequest2.isHasKitchen(), roomSuggestion2.isHasKitchen());
        assertEquals(roomRequest2.isHasBalcony(), roomSuggestion2.isHasBalcony());
        assertEquals(roomRequest2.getNumWC(), roomSuggestion2.getNumWC());

        assertFalse(result.contains("ATENÇÃO: Foi apresentada uma alternativa para o quarto"));
        assertTrue(result.contains("ID: 0"));
        assertTrue(result.contains("ID: 1"));
    }

    //Permissions denied, user without permissions -> Guest
    @Test
    void requestReservationSuggestionTestInvalidPermissions() {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            reservationService.requestReservationSuggestion(guest, 2, 2, null, null, null, roomService);
        });

        assertEquals("Acesso não autorizado: Apenas gestores ou funcionários podem solicitar sugestões de reserva.", exception.getMessage());
    }

    //Invalid Dates
    @Test
    void requestReservationSuggestionTestInvalidDates() {
        LocalDate checkInDate = LocalDate.of(2025, 5, 5);
        LocalDate checkOutDate = LocalDate.of(2025, 5, 1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.requestReservationSuggestion(manager, 2, 1, null, checkInDate, checkOutDate, roomService);
        });

        assertEquals("Datas de entrada e saída são inválidas.", exception.getMessage());
    }

    //Invalid RoomNumber
    @Test
    void requestReservationSuggestionTestInvalidRoomNumber() {
        LocalDate checkInDate = LocalDate.of(2025, 5, 1);
        LocalDate checkOutDate = LocalDate.of(2025, 5, 5);

        List<RoomRequest> roomRequests = List.of(
                new RoomRequest(2, 2, "Mar", true, true, 2)
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.requestReservationSuggestion(manager, 4, 2, roomRequests, checkInDate, checkOutDate, roomService);
        });

        assertEquals("O número de quartos especificados não corresponde ao número total de quartos.", exception.getMessage());
    }

    //Invalid GuestNumber
    @Test
    void requestReservationSuggestionTestInvalidGuestNumber() {
        LocalDate checkInDate = LocalDate.of(2025, 5, 1);
        LocalDate checkOutDate = LocalDate.of(2025, 5, 5);

        List<RoomRequest> roomRequests = List.of(
                new RoomRequest(2, 2, "Mar", true, true, 2),
                new RoomRequest(1, 1, "Serra", false, true, 1)
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.requestReservationSuggestion(manager, 5, 2, roomRequests, checkInDate, checkOutDate, roomService);
        });

        assertEquals("O somatório do número de hóspedes por quarto não corresponde ao número total de hóspedes fornecido.", exception.getMessage());
    }

    @Test
    void requestReservationSuggestionTestWithoutInactiveRooms() throws IllegalAccessException {
        //given
        Room room = roomService.getRoomById(manager, 0);
        room.setStatus("inactive");
        int totalGuests = 2;
        int totalRooms = 1;
        LocalDate checkInDate = LocalDate.of(2025, 5, 5);
        LocalDate checkOutDate = LocalDate.of(2025, 5, 10);

        List<RoomRequest> roomRequests = List.of(
                new RoomRequest(2, 2, "Mar", true, true, 2)
        );

        RoomRequest roomRequest = roomRequests.get(0);
        System.out.println(roomRequest);

        //when
        String result = reservationService.requestReservationSuggestion(manager, 2, 1, roomRequests, checkInDate, checkOutDate, roomService);
        System.out.println(result);

        //then
        ReservationSuggestion reservSuggestResult = reservationService.getReservSuggestionById(2);

        assertEquals(2, reservSuggestResult.getId());
        assertEquals(2, reservSuggestResult.getTotalGuest());
        assertEquals(1, reservSuggestResult.getTotalRooms());
        assertEquals(checkInDate, reservSuggestResult.getCheckInDate());
        assertEquals(checkOutDate, reservSuggestResult.getCheckOutDate());
        assertEquals(400.0, reservSuggestResult.getTotalPrice());
        assertFalse(reservSuggestResult.isUsed());
        assertEquals(roomRequests, reservSuggestResult.getRequestRooms());

        //checks suggested rooms
        List<Room> sugestionRooms = reservSuggestResult.getSugestionRooms();
        assertNotNull(sugestionRooms);
        assertEquals(1, sugestionRooms.size());

        //checks room suggestion 1
        Room roomSuggestion1 = sugestionRooms.get(0);
        assertEquals(2, roomSuggestion1.getId());
        assertEquals(roomRequest.getNumGuests(), roomSuggestion1.getMaxGuests());
        assertEquals(roomRequest.getNumBeds(), roomSuggestion1.getNumBeds());
        assertEquals(roomRequest.getViewType(), roomSuggestion1.getViewType());
        assertEquals(roomRequest.isHasKitchen(), roomSuggestion1.isHasKitchen());
        assertNotEquals(roomRequest.isHasBalcony(), roomSuggestion1.isHasBalcony());
        assertEquals(roomRequest.getNumWC(), roomSuggestion1.getNumWC());


        assertTrue(result.contains("ATENÇÃO: Foi apresentada uma alternativa para o quarto 1"));
        assertFalse(result.contains("Quarto - ID: 0"));
        assertTrue(result.contains("Quarto - ID: 2"));
    }

    @Test
    void requestReservationSuggestionFailsWhenGuestsExceedRoomCapacity() throws IllegalAccessException {
        //given
        int totalGuests = 5;
        int totalRooms = 1;
        LocalDate checkInDate = LocalDate.of(2025, 5, 5);
        LocalDate checkOutDate = LocalDate.of(2025, 5, 10);

        List<RoomRequest> roomRequests = List.of(
                new RoomRequest(5, 2, "Mar", true, true, 2)
        );

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.requestReservationSuggestion(manager, totalGuests, totalRooms, roomRequests, checkInDate, checkOutDate, roomService);
        });

        //then
        assertEquals("Não existe nenhum quarto disponível para satisfazer o pedido do quarto 1. Experimente verificar quartos com capacidade inferior.", exception.getMessage());



    }

    @Test
    void requestReservationSuggestionAlternativeRoomPrioritizingNumGuestsPerRoom() throws IllegalAccessException {
        //given
        roomService.registerRoom(manager, 3, 1, "Serra", false, false, 1, 50.0); //5 equivalences
        roomService.registerRoom(manager, 1, 1, "Serra", false, true, 1, 50.0); //5 equivalences

        int totalGuests = 1;
        int totalRooms = 1;
        LocalDate checkInDate = LocalDate.of(2025, 5, 5);
        LocalDate checkOutDate = LocalDate.of(2025, 5, 10);

        List<RoomRequest> roomRequests = List.of(
                new RoomRequest(1, 1, "Serra", false, false, 1)
        );

        RoomRequest roomRequest = roomRequests.getFirst();
        System.out.println(roomRequest);

        //when
        String result = reservationService.requestReservationSuggestion(manager, totalGuests, totalRooms, roomRequests, checkInDate, checkOutDate, roomService);
        System.out.println(result);

        //then
        ReservationSuggestion reservSuggestResult = reservationService.getReservSuggestionById(2);

        assertEquals(2, reservSuggestResult.getId());
        assertEquals(1, reservSuggestResult.getTotalGuest());
        assertEquals(1, reservSuggestResult.getTotalRooms());
        assertEquals(checkInDate, reservSuggestResult.getCheckInDate());
        assertEquals(checkOutDate, reservSuggestResult.getCheckOutDate());
        assertEquals(250.0, reservSuggestResult.getTotalPrice());
        assertFalse(reservSuggestResult.isUsed());
        assertEquals(roomRequests, reservSuggestResult.getRequestRooms());

        //checks suggested rooms
        List<Room> sugestionRooms = reservSuggestResult.getSugestionRooms();
        assertNotNull(sugestionRooms);
        assertEquals(1, sugestionRooms.size());

        //checks room suggestion 1
        Room roomSuggestion1 = sugestionRooms.get(0);
        assertEquals(5, roomSuggestion1.getId());
        assertEquals(roomRequest.getNumGuests(), roomSuggestion1.getMaxGuests());
        assertEquals(roomRequest.getNumBeds(), roomSuggestion1.getNumBeds());
        assertEquals(roomRequest.getViewType(), roomSuggestion1.getViewType());
        assertEquals(roomRequest.isHasKitchen(), roomSuggestion1.isHasKitchen());
        assertNotEquals(roomRequest.isHasBalcony(), roomSuggestion1.isHasBalcony());
        assertEquals(roomRequest.getNumWC(), roomSuggestion1.getNumWC());


        assertTrue(result.contains("ATENÇÃO: Foi apresentada uma alternativa para o quarto 1"));
        assertFalse(result.contains("Quarto - ID: 4"));
        assertTrue(result.contains("Quarto - ID: 5"));


    }

    //No rooms available
    @Test
    void requestReservationSuggestionTestNoRoomsAvailable() throws IllegalAccessException {
        //given
        LocalDate checkInDate = LocalDate.of(2025, 5, 5);
        LocalDate checkOutDate = LocalDate.of(2025, 5, 10);

        List<RoomRequest> roomRequests = List.of(
                new RoomRequest(3, 2, "Mar", true, true, 2)
        );
        //when
        //tries to request a reservation to the same dates and same criteria room as room 3
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {

            System.out.println(reservationService.requestReservationSuggestion(manager, 3, 1, roomRequests, checkInDate, checkOutDate, roomService));
            System.out.println(reservationService.getAllReservSuggestions(manager));
        });

        assertEquals("Não existe nenhum quarto disponível para satisfazer o pedido do quarto 1. Experimente verificar quartos com capacidade inferior.", exception.getMessage());
    }

    //Alternatives
    @Test
    void testRequestReservationSuggestionTestWithAlternativeRoom() throws IllegalAccessException {
        //given
        LocalDate checkInDate = LocalDate.of(2025, 5, 1);
        LocalDate checkOutDate = LocalDate.of(2025, 5, 5);

        List<RoomRequest> roomRequests = List.of(
                new RoomRequest(4, 2, "Serra", false, true, 2)
        );

        RoomRequest roomRequest = roomRequests.getFirst();

        roomService.registerRoom(manager, 4, 2,"Mar", false, false, 2, 150.0);

        //when
        String result = reservationService.requestReservationSuggestion(manager, 4, 1, roomRequests, checkInDate, checkOutDate, roomService);
        System.out.println(result);

        //then
        ReservationSuggestion reservSuggestResult = reservationService.getReservSuggestionById(2);

        assertEquals(2, reservSuggestResult.getId());
        assertEquals(4, reservSuggestResult.getTotalGuest());
        assertEquals(1, reservSuggestResult.getTotalRooms());
        assertEquals(checkInDate, reservSuggestResult.getCheckInDate());
        assertEquals(checkOutDate, reservSuggestResult.getCheckOutDate());
        assertEquals(600.0, reservSuggestResult.getTotalPrice());
        assertFalse(reservSuggestResult.isUsed());
        assertEquals(roomRequests, reservSuggestResult.getRequestRooms());

        //checks suggested rooms
        List<Room> sugestionRooms = reservSuggestResult.getSugestionRooms();
        assertNotNull(sugestionRooms);
        assertEquals(1, sugestionRooms.size());

        //checks room suggestion 1
        Room roomSuggestion1 = sugestionRooms.getFirst();
        assertEquals(4, roomSuggestion1.getId());
        assertEquals(roomRequest.getNumGuests(), roomSuggestion1.getMaxGuests());
        assertEquals(roomRequest.getNumBeds(), roomSuggestion1.getNumBeds());
        assertNotEquals(roomRequest.getViewType(), roomSuggestion1.getViewType());
        assertEquals("Mar", roomSuggestion1.getViewType());
        assertEquals(roomRequest.isHasKitchen(), roomSuggestion1.isHasKitchen());
        assertNotEquals(roomRequest.isHasBalcony(), roomSuggestion1.isHasBalcony());
        assertEquals(roomRequest.getNumWC(), roomSuggestion1.getNumWC());


        assertTrue(result.contains("ATENÇÃO: Foi apresentada uma alternativa para o quarto 1"));
        assertTrue(result.contains("Quarto - ID: 4"));
    }

    //-----------------------
    //findMostEquivalentRoom-
    //-----------------------

    //Success case, return the most equivalent room
    @Test
    void findMostEquivalentRoomTest() throws IllegalAccessException {
        //given
        RoomRequest request = new RoomRequest(2, 2, "Mar", true, true, 1);

        roomService.registerRoom(manager, 4, 2,"Mar", false, false, 2, 300.0);
        roomService.registerRoom(manager, 2, 1,"Serra", false, true, 1, 250.0);
        roomService.registerRoom(manager, 1, 1,"Mar", true, false, 1, 200.0);

        List<Room> availableRooms = roomService.getAllRooms(manager);

        List<Integer> roomChoseIds = new ArrayList<>();

        //when
        Room bestRoom = reservationService.findMostEquivalentRoom(request, availableRooms, roomChoseIds, LocalDate.now(), LocalDate.now().plusDays(3));

        //then
        assertNotNull(bestRoom);
        assertEquals(0, bestRoom.getId());
        System.out.println(bestRoom);
    }

    //No match when there is no room with a capacity greater than or equal to the request
    @Test
    void findMostEquivalentRoomTestNoMatchingGuestsCapacity() throws IllegalAccessException {
        //given
        RoomRequest request = new RoomRequest(4, 2, "Mar", true, true, 1);

        roomService.registerRoom(manager, 2, 1,"Serra", false, true, 1, 250.0);
        roomService.registerRoom(manager, 1, 1,"Mar", true, false, 1, 200.0);

        List<Room> availableRooms = roomService.getAllRooms(manager);

        List<Integer> roomChoseIds = new ArrayList<>();

        //when
        Room bestRoom = reservationService.findMostEquivalentRoom(request, availableRooms, roomChoseIds, LocalDate.now(), LocalDate.now().plusDays(3));

        //then
        assertNull(bestRoom);
    }

    //-----------------
    //isCompatibleRoom-
    //-----------------

    //Success case, all data compatible
    @Test
    void isCompatibleRoomTest() throws IllegalAccessException {
        roomService.registerRoom(manager, 2,2, "Mar", true, true, 2, 100.0);
        RoomRequest request = new RoomRequest(2, 2, "Mar", true, true, 2);

        Room registeredRoom = roomService.getRoomById(manager, 4);
        boolean isCompatible = reservationService.isCompatibleRoom(registeredRoom, request);

        assertTrue(isCompatible);
    }

    //Different number of Beds
    @Test
    void isCompatibleRoomTestDifferentBedsNumber() throws IllegalAccessException {
        roomService.registerRoom(manager, 2,1, "Mar", true, true, 2, 100.0);
        RoomRequest request = new RoomRequest(2, 2, "Mar", true, true, 2);

        Room registeredRoom = roomService.getRoomById(manager, 4);
        boolean isCompatible = reservationService.isCompatibleRoom(registeredRoom, request);

        assertFalse(isCompatible);
    }

    //Different max guests
    @Test
    void isCompatibleRoomTestDifferentMaxGuests() throws IllegalAccessException {
        roomService.registerRoom(manager, 2,2, "Mar", true, true, 2, 100.0);
        RoomRequest request = new RoomRequest(1, 2, "Mar", true, true, 2);

        Room registeredRoom = roomService.getRoomById(manager, 4);
        boolean isCompatible = reservationService.isCompatibleRoom(registeredRoom, request);

        assertFalse(isCompatible);
    }

    //Null view type
    @Test
    void isCompatibleRoomTestNullViewType() throws IllegalAccessException {
        roomService.registerRoom(manager, 2,2, "Mar", true, true, 2, 100.0);
        RoomRequest request = new RoomRequest(2, 2, null, true, true, 2);

        Room registeredRoom = roomService.getRoomById(manager, 4);
        boolean isCompatible = reservationService.isCompatibleRoom(registeredRoom, request);

        assertTrue(isCompatible);
    }

    //Different view type
    @Test
    void isCompatibleRoomTestDifferentViewType() throws IllegalAccessException {
        roomService.registerRoom(manager, 2,2, "Mar", true, true, 2, 100.0);
        RoomRequest request = new RoomRequest(2, 2, "Serra", true, true, 2);

        Room registeredRoom = roomService.getRoomById(manager, 4);
        boolean isCompatible = reservationService.isCompatibleRoom(registeredRoom, request);

        assertFalse(isCompatible);
    }

    //Different kitchen availability
    @Test
    void isCompatibleRoomTestDifferentKitchenAvailability() throws IllegalAccessException {
        roomService.registerRoom(manager, 2,2, "Mar", true, true, 2, 100.0);
        RoomRequest request = new RoomRequest(2, 2, "Mar", false, true, 2);

        Room registeredRoom = roomService.getRoomById(manager, 4);
        boolean isCompatible = reservationService.isCompatibleRoom(registeredRoom, request);

        assertFalse(isCompatible);
    }

    //Different balcony availability
    @Test
    void isCompatibleRoomTestDifferentBalconyAvailability() throws IllegalAccessException {
        roomService.registerRoom(manager, 2,2, "Mar", true, true, 2, 100.0);
        RoomRequest request = new RoomRequest(2, 2, "Mar", true, false, 2);

        Room registeredRoom = roomService.getRoomById(manager, 4);
        boolean isCompatible = reservationService.isCompatibleRoom(registeredRoom, request);

        assertFalse(isCompatible);
    }

    //Different WC number
    @Test
    void isCompatibleRoomTestDifferentWcNumber() throws IllegalAccessException {
        roomService.registerRoom(manager, 2,2, "Mar", true, true, 2, 100.0);
        RoomRequest request = new RoomRequest(2, 2, "Mar", true, true, 1);

        Room registeredRoom = roomService.getRoomById(manager, 4);
        boolean isCompatible = reservationService.isCompatibleRoom(registeredRoom, request);

        assertFalse(isCompatible);
    }

    //------------------
    //getAvailableRooms-
    //------------------

    //Success case, all rooms available
    @Test
    void getAvailableRoomsTest() throws IllegalAccessException {
        LocalDate checkInDate = LocalDate.of(2025,1, 10);
        LocalDate checkOutDate = LocalDate.of(2025, 1, 15);

        List<Room> rooms = roomService.getAllRooms(manager);
        List<Room> availableRooms = reservationService.getAvailableRooms(rooms, checkInDate, checkOutDate);

        assertEquals(4, availableRooms.size());
    }

    //----------------
    //isAvailableRoom-
    //----------------

    //Getting available rooms, when is available
    @Test
    void isAvailableRoomTest() throws IllegalAccessException {
        Room room = roomService.getRoomById(manager, 2);
        LocalDate checkInDate = LocalDate.of(2025, 5, 6);
        LocalDate checkOutDate = LocalDate.of(2025, 5, 10);

        assertTrue(reservationService.isAvailableRoom(room, checkInDate, checkOutDate));
    }

    //Trying to get available room when is inactive
    @Test
    void isAvailableRoomTestWhenInactive() throws IllegalAccessException {
        roomService.registerRoom(manager, 2, 2, "Mar", true, true, 2, 100.0);

        Room room = roomService.getRoomById(manager, 4);
        room.setStatus("inactive");

        LocalDate checkInDate = LocalDate.of(2025, 5, 3);
        LocalDate checkOutDate = LocalDate.of(2025, 5, 4);

        assertFalse(reservationService.isAvailableRoom(room, checkInDate, checkOutDate));
    }

    //------------------------
    //getAllReservSuggestions-
    //------------------------

    //Success case, return All reserve suggestions
    @Test
    void getAllReservSuggestionsTest() throws IllegalAccessException {

        List<RoomRequest> roomRequests = new ArrayList<>();
        roomRequests.add(new RoomRequest(2, 2, "Mar", true, true, 2));

        LocalDate checkInDate = LocalDate.now().plusDays(1);
        LocalDate checkOutDate = LocalDate.now().plusDays(3);

        String suggestionResponse = reservationService.requestReservationSuggestion(
                manager, 2, 1, roomRequests, checkInDate, checkOutDate, roomService);

        LinkedList<ReservationSuggestion> suggestions = reservationService.getAllReservSuggestions(manager);

        assertFalse(suggestions.isEmpty());
        assertEquals(3, suggestions.size());

        ReservationSuggestion firstSuggestion = suggestions.get(0);
        assertEquals(0, firstSuggestion.getId());
        assertEquals(3, firstSuggestion.getTotalGuest());
        assertEquals(1, firstSuggestion.getTotalRooms());
        assertEquals(600.0, firstSuggestion.getTotalPrice());

        ReservationSuggestion secondSuggestion = suggestions.get(2);
        assertEquals(2, secondSuggestion.getId());
        assertEquals(2, secondSuggestion.getTotalGuest());
        assertEquals(1, secondSuggestion.getTotalRooms());
        assertEquals(200.0, secondSuggestion.getTotalPrice());
    }

    //Success case, return All sugesttions, as Employee
    @Test
    void getAllReservSuggestionsTestAsEmployee() throws IllegalAccessException {

        List<RoomRequest> roomRequests = new ArrayList<>();
        roomRequests.add(new RoomRequest(2, 2, "Mar", true, true, 2));

        LocalDate checkInDate = LocalDate.now().plusDays(1);
        LocalDate checkOutDate = LocalDate.now().plusDays(3);

        String suggestionResponse = reservationService.requestReservationSuggestion(
                manager, 2, 1, roomRequests, checkInDate, checkOutDate, roomService);

        LinkedList<ReservationSuggestion> suggestions = reservationService.getAllReservSuggestions(employee);

        assertFalse(suggestions.isEmpty());
        assertEquals(3, suggestions.size());

        ReservationSuggestion firstSuggestion = suggestions.get(0);
        assertEquals(0, firstSuggestion.getId());
        assertEquals(3, firstSuggestion.getTotalGuest());
        assertEquals(1, firstSuggestion.getTotalRooms());
        assertEquals(600.0, firstSuggestion.getTotalPrice());

        ReservationSuggestion secondSuggestion = suggestions.get(2);
        assertEquals(2, secondSuggestion.getId());
        assertEquals(2, secondSuggestion.getTotalGuest());
        assertEquals(1, secondSuggestion.getTotalRooms());
        assertEquals(200.0, secondSuggestion.getTotalPrice());
    }

    //Permissions denied, user without permissions -> Guest
    @Test
    void getAllReservSugesttionsTestAsGuest() throws IllegalAccessException {
        List<RoomRequest> roomRequests = new ArrayList<>();
        roomRequests.add(new RoomRequest(2, 2, "Mar", true, true, 2));

        LocalDate checkInDate = LocalDate.now().plusDays(1);
        LocalDate checkOutDate = LocalDate.now().plusDays(3);

        String suggestionResponse = reservationService.requestReservationSuggestion(
                manager, 2, 1, roomRequests, checkInDate, checkOutDate, roomService);

        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            reservationService.getAllReservSuggestions(guest);
        });

        assertEquals("Acesso não autorizado: Apenas gestores podem visualizar sugestões de reservas.", exception.getMessage());
    }

    //------------------------
    //getReservSuggestionById-
    //------------------------

    //Success case, getting reserve by ID
    @Test
    void getReservSuggestionByIdTest() throws IllegalAccessException {
        reservationService.requestReservationSuggestion(manager, 2, 1, List.of(new RoomRequest(2, 2, "Mar", true, true, 2)),
                LocalDate.of(2025, 5, 5), LocalDate.of(2025, 5, 10), roomService);

        ReservationSuggestion result = reservationService.getReservSuggestionById(2);

        assertNotNull(result);
        assertEquals(2, result.getId());
        assertEquals(2, result.getTotalGuest());
        assertEquals(1, result.getTotalRooms());
    }

    //Invalid ReservSuggestion ID
    @Test
    void getReservSuggestionByIdTestInvalidId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.getReservSuggestionById(10);
        });

        assertEquals("Sugestão de reserva com o ID 10 não existe.", exception.getMessage());
    }

    //Negative, invalid ID
    @Test
    void getReservSuggestionByIdTestInvalidIdTest() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.getReservSuggestionById(-1);
        });
        assertEquals("Sugestão de reserva com o ID -1 não existe.", exception.getMessage());
    }

    //------------------------
    //makeReservation-
    //------------------------

    @Test
    void makeReservationTestSuccess() throws IllegalAccessException {
        // Given
        int suggestionId = 1;
        int guestId = 1;
        int expectedReservationId = 1;

        ReservationSuggestion reservationSuggestion = reservationService.getReservSuggestionById(suggestionId);

        // When
        String result = reservationService.makeReservation(manager, suggestionId, guestId, roomService, accountService);

        // Then
        assertNotNull(result);
        assertTrue(result.contains("Reserva efetuada com sucesso com o ID 1"));

        Reservation reservation = reservationService.getReservationById(manager, 1);
        assertEquals(expectedReservationId, reservation.getId());
        assertEquals(guestId, reservation.getIdGuest());
        assertEquals(suggestionId, reservation.getIdReservSuggestion());
        assertEquals(reservationSuggestion.getCheckInDate(), reservation.getCheckInDate());
        assertEquals(reservationSuggestion.getCheckOutDate(), reservation.getCheckOutDate());
        assertEquals(reservationSuggestion.getTotalPrice(), reservation.getTotalPrice());
        assertEquals("confirmed", reservation.getStatus());
    }

    @Test
    void makeReservationTestMadeByEmployee() throws IllegalAccessException {
        // Given
        int suggestionId = 1;
        int guestId = 1;
        int expectedReservationId = 1;

        ReservationSuggestion reservationSuggestion = reservationService.getReservSuggestionById(suggestionId);

        // When
        String result = reservationService.makeReservation(employee, suggestionId, guestId, roomService, accountService);

        // Then
        assertNotNull(result);
        assertTrue(result.contains("Reserva efetuada com sucesso com o ID 1"));

        Reservation reservation = reservationService.getReservationById(employee, 1);
        assertEquals(expectedReservationId, reservation.getId());
        assertEquals(guestId, reservation.getIdGuest());
        assertEquals(suggestionId, reservation.getIdReservSuggestion());
        assertEquals(reservationSuggestion.getCheckInDate(), reservation.getCheckInDate());
        assertEquals(reservationSuggestion.getCheckOutDate(), reservation.getCheckOutDate());
        assertEquals(reservationSuggestion.getTotalPrice(), reservation.getTotalPrice());
        assertEquals("confirmed", reservation.getStatus());
    }

    @Test
    void makeReservationTestPermissionDeniedGuest() {
        // Given
        int suggestionId = 1;
        int guestId = 1;

        // When
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            reservationService.makeReservation(guest, suggestionId, guestId, roomService, accountService);
        });

        // Then
        assertEquals("Acesso não autorizado: Apenas gestores ou funcionários podem efetuar reservas.", exception.getMessage());
    }

    @Test
    void makeReservationTestInvalidSuggestionId() {
        // Given
        int invalidSuggestionId = 999;
        int guestId = 1;

        // When
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.makeReservation(manager, invalidSuggestionId, guestId, roomService, accountService);
        });

        // Then
        assertEquals("Sugestão de reserva com o ID 999 não existe.", exception.getMessage());
    }

    @Test
    void makeReservationTestInvalidGuestId() {
        // Given
        int suggestionId = 1;
        int invalidGuestId = 10;

        // When
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.makeReservation(manager, suggestionId, invalidGuestId, roomService, accountService);
        });

        // Then
        assertEquals("Erro: ID de hóspede inválido ou não encontrado no sistema.", exception.getMessage());
    }

    @Test
    void makeReservationTestSuggestionAlreadyUsed() throws IllegalAccessException {
        // Given
        int suggestionId = 0;
        int guestId = 1;

        // When
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.makeReservation(manager, suggestionId, guestId, roomService, accountService);
        });

        // Then
        assertEquals("A sugestão de reserva com o ID 0 já foi utilizada para uma reserva.", exception.getMessage());
    }

    @Test
    void makeReservationTestRoomUnavailable() throws IllegalAccessException {
        // Given
        int suggestionId = 1;
        int guestId = 1;

        ReservationSuggestion reservationSuggestion = reservationService.getReservSuggestionById(suggestionId);
        Room reservedRoom = reservationSuggestion.getSugestionRooms().get(0);
        reservedRoom.setStatus("inactive");

        // When
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.makeReservation(manager, suggestionId, guestId, roomService, accountService);
        });

        // Then
        assertEquals("Erro: O hotel já não tem disponibilidade para reservar a sugestão fornecida. Por favor, solicite uma nova sugestão de reserva.", exception.getMessage());
    }

    //------------------------
    //cancelReservation-
    //------------------------

    //Success case, user cancel reserve as manager
    @Test
    void cancelReservationTestAsManager() throws IllegalAccessException {
        Reservation reservation = reservationService.getReservationById(manager, 0);

        String result = reservationService.cancelReservation(manager, 0);
        assertEquals("Reserva com ID 0 foi cancelada com sucesso.\nCancelamento efetuado sem custos adicionais.", result);
        assertEquals("cancelled", reservation.getStatus());
    }

    //Success case, user cancel reserve as employee
    @Test
    void cancelReservationTestAsEmployee() throws IllegalAccessException {
        Reservation reservation = reservationService.getReservationById(employee, 0);

        String result = reservationService.cancelReservation(employee, 0);
        assertEquals("Reserva com ID 0 foi cancelada com sucesso.\nCancelamento efetuado sem custos adicionais.", result);
        assertEquals("cancelled", reservation.getStatus());
    }

    //Access denied, user without permissions -> Guest
    @Test
    void cancelReservationTestAsGuest() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            reservationService.getReservationById(guest, 0);
        });

        assertEquals("Acesso não autorizado: Apenas gestores podem visualizar as reservas.", exception.getMessage());
    }

    //Invalid Status
    @Test
    void cancelReservationTestInvalidStatus() throws IllegalAccessException {
        Reservation reservation = reservationService.getReservationById(manager, 0);
        reservation.setStatus("completed");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.cancelReservation(manager, 0);
        });

        assertEquals("A reserva só pode ser cancelada se estiver nos estados 'pendent' ou 'confirmed'. Estado atual: completed", exception.getMessage());
    }

    //Less than 24 hours cancel
    @Test
    void testCancelReservationWithLessThan24Hours() throws IllegalAccessException {
        Reservation reservation = reservationService.getReservationById(manager, 0);
        reservation.setStatus("pendent");
        reservation.setCheckInDate(LocalDate.now());

        String result = reservationService.cancelReservation(manager, 0);
        assertEquals("Reserva com ID 0 foi cancelada com sucesso.\nCancelamento efetuado. Custo total da reserva será cobrado.", result);
        assertEquals("cancelled", reservation.getStatus());
    }

    //More than 24 hours cancel
    @Test
    void cancelReservationTestWithMoreThan24Hours() throws IllegalAccessException {
        Reservation reservation = reservationService.getReservationById(manager, 0);
        reservation.setStatus("pendent");
        reservation.setCheckInDate(LocalDate.now().plusDays(2)); // Check-in em 48 horas

        String result = reservationService.cancelReservation(manager, 0);
        assertEquals("Reserva com ID 0 foi cancelada com sucesso.\nCancelamento efetuado sem custos adicionais.", result);
        assertEquals("cancelled", reservation.getStatus());
    }

    //----------
    //doCheckIn-
    //----------

    //Success case, check in as manager
    @Test
    void doCheckInTestAsManager() throws IllegalAccessException {
        Reservation reservation = reservationService.getReservationById(manager, 0);
        reservation.setStatus("confirmed");

        String result = reservationService.doCheckIn(manager, 0);
        assertEquals("Check-in efetuado com sucesso para a reserva com o ID 0", result);
        assertEquals("ongoing", reservation.getStatus());

        ReservationSuggestion suggestion = reservationService.getReservSuggestionById(reservation.getIdReservSuggestion());
        for (Room room : suggestion.getSugestionRooms()) {
            assertEquals("occupied", room.getStatus());
        }
        Reservation updatedReservation = reservationService.getReservationById(manager, 0);
        assertEquals(0, updatedReservation.getId());
    }

    //Success case, check in as employee
    @Test
    void doCheckInTestAsEmployee() throws IllegalAccessException {
        Reservation reservation = reservationService.getReservationById(employee, 0);
        reservation.setStatus("confirmed");

        String result = reservationService.doCheckIn(employee, 0);
        assertEquals("Check-in efetuado com sucesso para a reserva com o ID 0", result);
        assertEquals("ongoing", reservation.getStatus());

        ReservationSuggestion suggestion = reservationService.getReservSuggestionById(reservation.getIdReservSuggestion());
        for (Room room : suggestion.getSugestionRooms()) {
            assertEquals("occupied", room.getStatus());
        }
        Reservation updatedReservation = reservationService.getReservationById(employee, 0);
        assertEquals(0, updatedReservation.getId());
    }

    //Invalid denied, user without permissions -> Guest
    @Test
    void doCheckInTestAsGuest() {

        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            reservationService.doCheckIn(guest, 0);
        });

        assertEquals("Acesso negado: Apenas gestores ou funcionários podem efetuar check-in de reservas.", exception.getMessage());
    }

    //Unconfirmed reservation
    @Test
    void doCheckInTestWithUnconfirmedReservation() throws IllegalAccessException {
        Reservation reservation = reservationService.getReservationById(manager, 0);
        reservation.setStatus("pendent");

        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            reservationService.doCheckIn(manager, 0);
        });
        assertEquals("Não é possível efetuar check-in de uma reserva não confirmada. Estado atual: pendent", exception.getMessage());
    }

    //-----------
    //doCheckOut-
    //-----------

    //Success case as manager
    @Test
    void doCheckOutAsManger() throws IllegalAccessException {
        Reservation reservation = reservationService.getReservationById(manager, 0);
        reservation.setStatus("ongoing");

        String result = reservationService.doCheckOut(manager, reservation.getId(), roomService, maintenanceService);

        assertEquals("Check-out efetuado com sucesso para a reserva com o ID " + reservation.getId(), result);

        assertEquals("completed", reservation.getStatus());

        ReservationSuggestion suggestion = reservationService.getReservSuggestionById(reservation.getIdReservSuggestion());
        for (Room room : suggestion.getSugestionRooms()) {
            assertEquals("available", room.getStatus());
        }
    }

    //Success case as employee
    @Test
    void doCheckOutAsEmployee() throws IllegalAccessException {
        Reservation reservation = reservationService.getReservationById(employee, 0);
        reservation.setStatus("ongoing");

        String result = reservationService.doCheckOut(employee, reservation.getId(), roomService, maintenanceService);

        assertEquals("Check-out efetuado com sucesso para a reserva com o ID " + reservation.getId(), result);

        assertEquals("completed", reservation.getStatus());

        ReservationSuggestion suggestion = reservationService.getReservSuggestionById(reservation.getIdReservSuggestion());
        for (Room room : suggestion.getSugestionRooms()) {
            assertEquals("available", room.getStatus());
        }
    }

    //Permissions denied, user without permissions -> Guest
    @Test
    void doCheckOutAsGuest() {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            reservationService.doCheckOut(guest, 0, null, null);
        });

        assertEquals("Acesso não autorizado: Apenas gestores ou funcionários podem efetuar check-out de reservas.", exception.getMessage());
    }

    //Trying to do checkout in not ongoing reservation
    @Test
    void doCheckOutWithNonOngoingReservation() throws IllegalAccessException {
        Reservation reservation = reservationService.getReservationById(manager, 0);
        reservation.setStatus("completed");

        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            reservationService.doCheckOut(manager, reservation.getId(), roomService, maintenanceService);
        });

        assertEquals("Não é possível efetuar check-out de uma reserva que não se encontra a decorrer. Estado atual: completed", exception.getMessage());
    }

    //-------------------
    //getAllReservations-
    //-------------------

    //Getting all reservations as Manager
    @Test
    void getAllReservationsTestAsManager() throws IllegalAccessException {
        LinkedList<Reservation> reservations = reservationService.getAllReservations(manager);
        System.out.println(reservations);
        assertNotNull(reservations);
        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());
    }

    //Getting all reservations as Employee
    @Test
    void getAllReservationsTestAsEmployee() throws IllegalAccessException {
        LinkedList<Reservation> reservations = reservationService.getAllReservations(employee);
        System.out.println(reservations);
        assertNotNull(reservations);
        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());
    }

    //Access denied, user without permissions
    @Test
    void getAllReservationsTestAsGuest() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            reservationService.getAllReservations(guest);
        });

        assertEquals("Acesso não autorizado: Apenas gestores e funcionários podem visualizar as reservas.", exception.getMessage());
    }

    //-------------------
    //getReservationById-
    //-------------------

    //Success case, getting the reserve by ID as Manager
    @Test
    void getReservationByIdTestAsManager() throws IllegalAccessException {
        Reservation reservation = reservationService.getReservationById(manager, 0);
        System.out.println(reservation);
        assertNotNull(reservation);
        assertEquals(0, reservation.getId());
    }

    //Success case, getting the reserve by ID as Employee
    @Test
    void getReservationByIdTestAsEmployee() throws IllegalAccessException {
        Reservation reservation = reservationService.getReservationById(employee, 0);
        System.out.println(reservation);
        assertNotNull(reservation);
        assertEquals(0, reservation.getId());
    }

    //Permissions denied, user without permissions
    @Test
    void getReservationByIdTestAsGuest() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            reservationService.getReservationById(guest, 0);
        });

        assertEquals("Acesso não autorizado: Apenas gestores podem visualizar as reservas.", exception.getMessage());
    }

    //Invalid ID, ID less than zero
    @Test
    void getReservationByIdTestInvalidId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.getReservationById(manager, -1);
        });

        assertEquals("O id de reserva indicado não é válido. Tente novamente.", exception.getMessage());
    }

    //Invalid ID, non-existing ID
    @Test
    void getReservationByIdTestNonExistingId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.getReservationById(manager, 10);
        });

        assertEquals("Reserva com o ID 10 não existe.", exception.getMessage());
    }
}