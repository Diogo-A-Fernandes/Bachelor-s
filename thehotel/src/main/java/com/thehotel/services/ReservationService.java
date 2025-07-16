package com.thehotel.services;


import com.thehotel.model.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

public class ReservationService {

    private int suggestionIdCounter = 0;
    private int reservationIdCounter = 0;
    private final LinkedList<ReservationSuggestion> reservationSuggestionsList = new LinkedList<>();
    private final LinkedList<Reservation> reservationsList = new LinkedList<>();

    public ReservationService() {
    }


    /* * --------------------------------------------------------------------------------------------
     * RESERVATION SUGESTIONS MANAGEMENT
     * --------------------------------------------------------------------------------------------
     */
    public String requestReservationSuggestion(User user,
            int totalGuests, int totalRooms, List<RoomRequest> roomRequests,
            LocalDate checkInDate, LocalDate checkOutDate, RoomService roomService) throws IllegalArgumentException, IllegalAccessException {
        //verifies permissions
        if (!user.getRole().equals("manager") && !user.getRole().equals("employee")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores ou funcionários podem solicitar sugestões de reserva.");
        }

        //verifies if dates are valid
        if (checkInDate == null || checkOutDate == null || checkInDate.isAfter(checkOutDate)) {
            throw new IllegalArgumentException("Datas de entrada e saída são inválidas.");
        }

        //verifies if room requests number is equal to total rooms number
        if (roomRequests != null && roomRequests.size() != totalRooms) {
            throw new IllegalArgumentException("O número de quartos especificados não corresponde ao número total de quartos.");
        }

        //verifies if the sum of guests per room is equal to total guests number
        int sumGuestsPerRoom = roomRequests.stream().mapToInt(RoomRequest::getNumGuests).sum();
        if (sumGuestsPerRoom != totalGuests) {
            throw new IllegalArgumentException("O somatório do número de hóspedes por quarto não corresponde ao número total de hóspedes fornecido.");
        }

        StringBuilder response = new StringBuilder();

        List<Room> suggestedRooms = new ArrayList<>();
        List<Integer> roomsIDs = new ArrayList<>(); //rooms suggested IDs
        Long totalNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate); //calculates the number of days
        double totalPrice = 0;

        //gets the list of available rooms for the given dates
        List<Room> roomsList = roomService.getAllRooms(user);
        List<Room> availableRooms = getAvailableRooms(roomsList, checkInDate, checkOutDate);

        if(availableRooms.isEmpty()) throw new IllegalArgumentException("Não existe nenhum quarto disponível para as datas indicadas.");

        for (int i = 0; i < roomRequests.size(); i++) { //for each room requested
            boolean foundRoom = false;
            for (Room room : availableRooms) { //verifies if there is any room compatible with it
                //ignores if room is inactive or is already been chosen
                if ("inactive".equalsIgnoreCase(room.getStatus()) || roomsIDs.contains(room.getId())) {
                    continue;
                }

                //verifies if the room is compatible with the requested room
                if(isCompatibleRoom(room, roomRequests.get(i))){
                    foundRoom = true;
                    suggestedRooms.add(room); //adds suggestion room
                    roomsIDs.add(room.getId()); //adds its ID to the chosen IDs
                    totalPrice += room.getPricePerNight(); //increments the total price
                    break;
                }
            }

            if(!foundRoom){ //couldn't find a match for the requested room, tries an alternative

                //System.out.println("CONTROLO - ALTERNATIVA PARA O QUARTO " + i);
                //find the room with the higher number of equivalences in common
                Room alternativeRoom = findMostEquivalentRoom(roomRequests.get(i), availableRooms, roomsIDs, checkInDate, checkOutDate);

                if(alternativeRoom == null) throw new IllegalArgumentException("Não existe nenhum quarto disponível para satisfazer o pedido do quarto " + (i+1) + ". Experimente verificar quartos com capacidade inferior.");

                suggestedRooms.add(alternativeRoom); //adds alternative room
                roomsIDs.add(alternativeRoom.getId()); //adds its ID to the chosen IDs
                totalPrice += alternativeRoom.getPricePerNight(); //increments the total price

                response.append("\nATENÇÃO: Foi apresentada uma alternativa para o quarto ").append(i + 1).append(", por não existir quarto disponível para atender a todas as características solicitadas.");
            }

        }

        ReservationSuggestion reservationSuggestion = new ReservationSuggestion(
                generateSuggestionId(), totalGuests, totalRooms, roomRequests, suggestedRooms,
                checkInDate, checkOutDate, totalPrice*totalNights
        );
        reservationSuggestionsList.add(reservationSuggestion);

        response.append(reservationSuggestion.toString());
        return response.toString();
    }
    
    public Room findMostEquivalentRoom(RoomRequest request, List<Room> availableRooms, List<Integer> roomsChosenIDs, LocalDate checkInDate, LocalDate checkOutDate) throws IllegalAccessException {
        //defines equivalence criteria
        List<BiFunction<Room, RoomRequest, Boolean>> equivalenceCriteria = List.of(
                (room, req) -> room.getMaxGuests() == req.getNumGuests(),
                (room, req) -> room.getNumBeds() == req.getNumBeds(),
                (room, req) -> room.getViewType().equalsIgnoreCase(req.getViewType()),
                (room, req) -> room.isHasKitchen() == req.isHasKitchen(),
                (room, req) -> room.isHasBalcony() == req.isHasBalcony(),
                (room, req) -> room.getNumWC() == req.getNumWC()
        );

        Room bestRoom = null;
        int maxEquivalences = 0;
        int closestGuestsDifference = Integer.MAX_VALUE;

        for (int i = 0; i < availableRooms.size(); i++) {

            Room room = availableRooms.get(i);
            //if isn't a room that already have been chosen before
            if(!"inactive".equalsIgnoreCase(room.getStatus()) && !roomsChosenIDs.contains(room.getId()) && room.getMaxGuests() >= request.getNumGuests()){
                int equivalences = 0;

                for (BiFunction<Room, RoomRequest, Boolean> criteria : equivalenceCriteria) {
                    if (criteria.apply(room, request)) {
                        equivalences++;
                    }
                }

                int guestsDifference = Math.abs(room.getMaxGuests() - request.getNumGuests());

                //System.out.println("Quarto com ID " + room.getId() + " com " + equivalences + "equivalencias");

                if (equivalences > maxEquivalences || (equivalences == maxEquivalences && guestsDifference < closestGuestsDifference)){
                    maxEquivalences = equivalences;
                    closestGuestsDifference = guestsDifference;
                    bestRoom = room;
                }
            }

        }

        return bestRoom; // returns the room with more common criteria
    }

    public boolean isCompatibleRoom(Room room, RoomRequest request){
        return room.getNumBeds() == request.getNumBeds() &&
                (room.getMaxGuests() == request.getNumGuests()) &&
                (request.getViewType() == null || request.getViewType().equalsIgnoreCase(room.getViewType())) &&
                (request.isHasKitchen() == room.isHasKitchen()) &&
                (request.isHasBalcony() == room.isHasBalcony()) &&
                (room.getNumWC() == request.getNumWC());
    }

    public List<Room> getAvailableRooms(List<Room> roomsList, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Room> availableRooms = new ArrayList<>();

        for (Room room : roomsList) {
            boolean isAvailable = true;

            for (Reservation reservation : reservationsList) {
                ReservationSuggestion reservationSuggestion = getReservSuggestionById(reservation.getIdReservSuggestion());
                List<Room> reservationRooms = reservationSuggestion.getSugestionRooms();

                for (Room reservedRoom : reservationRooms) {
                    if (reservedRoom.getId() == room.getId()) {
                        // Verificar conflito de datas
                        if (reservedRoom.getStatus().equalsIgnoreCase("inactive") || !(checkOutDate.isBefore(reservation.getCheckInDate()) || checkInDate.isAfter(reservation.getCheckOutDate()))) {
                            isAvailable = false;
                            break;
                        }
                    }
                }

                if (!isAvailable) {
                    break;
                }
            }

            if (isAvailable) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    public boolean isAvailableRoom(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        if(room.getStatus().equalsIgnoreCase("inactive")) return false;
        for (Reservation reservation : reservationsList) {
            ReservationSuggestion reservationSuggestion = getReservSuggestionById(reservation.getIdReservSuggestion());
            List<Room> reservationRooms = reservationSuggestion.getSugestionRooms();
            for(Room reservedRoom : reservationRooms){
                if(reservedRoom.getId() == room.getId()){
                    // verifies if there is any conflict with the dates
                    if (reservedRoom.getStatus().equalsIgnoreCase("inactive") || !(checkOutDate.isBefore(reservation.getCheckInDate()) || checkInDate.isAfter(reservation.getCheckOutDate()))) {
                        return false; // room not available
                    }
                }
            }
        }
        return true; // room available
    }

    public LinkedList<ReservationSuggestion> getAllReservSuggestions(User user) throws IllegalAccessException{

        if(!user.getRole().equals("manager") && !user.getRole().equals("employee")){
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores podem visualizar sugestões de reservas.");
        }

        return reservationSuggestionsList;
    }

    public ReservationSuggestion getReservSuggestionById(int id) {
        for (ReservationSuggestion reservSuggestion : reservationSuggestionsList) {
            if (reservSuggestion.getId() == id) {
                return reservSuggestion;
            }
        }
        throw new IllegalArgumentException("Sugestão de reserva com o ID " + id + " não existe.");
    }

    /* * --------------------------------------------------------------------------------------------
     * RESERVATION MANAGEMENT
     * --------------------------------------------------------------------------------------------
     */

    public String makeReservation(User user, int suggestionId, int guestId, RoomService roomService, AccountService accountService) throws IllegalAccessException {
        //verifies permissions
        if (!user.getRole().equals("manager") && !user.getRole().equals("employee")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores ou funcionários podem efetuar reservas.");
        }

        //verifies if suggestion ID is valid
        ReservationSuggestion reservationSuggestion = getReservSuggestionById(suggestionId);
        if (reservationSuggestion.isUsed()) {
            throw new IllegalArgumentException("A sugestão de reserva com o ID " + suggestionId + " já foi utilizada para uma reserva.");
        }
        //verifies if guest ID is valid
        try{
            Guest guest = (Guest) accountService.getUserById(guestId);
        }catch(Exception e){
            throw new IllegalArgumentException("Erro: ID de hóspede inválido ou não encontrado no sistema.");
        }

        // checks if rooms are still available
        for (Room room : reservationSuggestion.getSugestionRooms()) {
            if (!isAvailableRoom(room, reservationSuggestion.getCheckInDate(), reservationSuggestion.getCheckOutDate())) {
                throw new IllegalArgumentException("Erro: O hotel já não tem disponibilidade para reservar a sugestão fornecida. Por favor, solicite uma nova sugestão de reserva.");
            }
        }

        // creates reservation
        Reservation reservation = new Reservation(
                generateReservationId(),
                guestId,
                suggestionId,
                reservationSuggestion.getCheckInDate(),
                reservationSuggestion.getCheckOutDate(),
                reservationSuggestion.getTotalPrice(),
                "confirmed"
        );

        reservationSuggestion.setUsed(true);
        reservationsList.add(reservation); //adds reservation to the global list
        return String.format("Reserva efetuada com sucesso com o ID %d.", reservation.getId());
    }

    public String cancelReservation(User user, int reservationId) throws IllegalAccessException {
        if (!user.getRole().equalsIgnoreCase("manager") && !user.getRole().equalsIgnoreCase("employee")) {
            throw new IllegalAccessException("Acesso negado: Apenas gestores ou funcionários podem cancelar reservas.");
        }

        Reservation reservation = getReservationById(user, reservationId);

        if (!reservation.getStatus().equalsIgnoreCase("pendent") &&
                !reservation.getStatus().equalsIgnoreCase("confirmed")) {
            throw new IllegalArgumentException("A reserva só pode ser cancelada se estiver nos estados 'pendent' ou 'confirmed'. Estado atual: " + reservation.getStatus());
        }

        //verifies cancellation policy
        LocalDate now = LocalDate.now();
        long hoursUntilCheckIn = ChronoUnit.HOURS.between(now.atStartOfDay(), reservation.getCheckInDate().atStartOfDay());
        boolean isCancellationFree = hoursUntilCheckIn >= 24;
        String cancellationMessage = isCancellationFree
                ? "Cancelamento efetuado sem custos adicionais."
                : "Cancelamento efetuado. Custo total da reserva será cobrado.";

        // update reservation status
        reservation.setStatus("cancelled");

        return "Reserva com ID " + reservationId + " foi cancelada com sucesso.\n" + cancellationMessage;
    }

    public String doCheckIn(User user, int reservationId) throws IllegalAccessException {
        if (!user.getRole().equalsIgnoreCase("manager") && !user.getRole().equalsIgnoreCase("employee")) {
            throw new IllegalAccessException("Acesso negado: Apenas gestores ou funcionários podem efetuar check-in de reservas.");
        }
        Reservation reservation = getReservationById(user, reservationId);
        String reservationStatus = reservation.getStatus();
        if(!reservationStatus.equalsIgnoreCase("confirmed")){
            throw new IllegalAccessException("Não é possível efetuar check-in de uma reserva não confirmada. Estado atual: " + reservationStatus);
        }
        reservation.setStatus("ongoing");

        ReservationSuggestion reservationSuggestion = getReservSuggestionById(reservation.getIdReservSuggestion());
        for (Room room : reservationSuggestion.getSugestionRooms()) {
            room.setStatus("occupied");
        }
        return "Check-in efetuado com sucesso para a reserva com o ID " + reservationId;
    }

    public String doCheckOut(User user, int reservationId, RoomService roomService, MaintenanceService maintenanceService) throws IllegalAccessException {
        if (!user.getRole().equalsIgnoreCase("manager") && !user.getRole().equalsIgnoreCase("employee")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores ou funcionários podem efetuar check-out de reservas.");
        }
        Reservation reservation = getReservationById(user, reservationId);
        String reservationStatus = reservation.getStatus();
        if(!reservationStatus.equalsIgnoreCase("ongoing")){
            throw new IllegalAccessException("Não é possível efetuar check-out de uma reserva que não se encontra a decorrer. Estado atual: " + reservationStatus);
        }
        reservation.setStatus("completed");

        ReservationSuggestion reservationSuggestion = getReservSuggestionById(reservation.getIdReservSuggestion());
        for (Room room : reservationSuggestion.getSugestionRooms()) {
            room.setStatus("available");
            maintenanceService.registerMaintenance(user, room.getId(), "Limpeza", roomService);
        }
        return "Check-out efetuado com sucesso para a reserva com o ID " + reservationId;
    }

    public LinkedList<Reservation> getAllReservations(User user) throws IllegalAccessException{
        if(!user.getRole().equals("manager") && !user.getRole().equals("employee")){
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores e funcionários podem visualizar as reservas.");
        }
       return reservationsList;
    }

    public Reservation getReservationById(User user, int reservationId) throws IllegalAccessException{

        if(!user.getRole().equals("manager") && !user.getRole().equals("employee")){
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores podem visualizar as reservas.");
        }

        if(reservationId < 0 ){
            throw new IllegalArgumentException("O id de reserva indicado não é válido. Tente novamente.");
        }

        for (Reservation reservation : reservationsList) {
            if (reservation.getId() == reservationId) {
                return reservation;
            }
        }
        throw new IllegalArgumentException("Reserva com o ID " + reservationId + " não existe.");
    }

    /* --------------------------------------------------------------------------------------------
     * GENERAL METHODS
     * --------------------------------------------------------------------------------------------
     */

    private synchronized int generateSuggestionId(){
        return suggestionIdCounter++;
    }

    public synchronized int generateReservationId(){
        return reservationIdCounter++;
    }

  /*   * --------------------------------------------------------------------------------------------
     * VALIDATION METHODS
     * --------------------------------------------------------------------------------------------*/

}
