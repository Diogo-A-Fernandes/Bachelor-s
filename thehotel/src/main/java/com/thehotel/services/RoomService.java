package com.thehotel.services;

import com.thehotel.model.Manager;
import com.thehotel.model.Room;
import com.thehotel.model.RoomRequest;
import com.thehotel.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RoomService {
    private int roomIdCounter = 0;
    private final LinkedList<Room> roomList = new LinkedList<>();
    private Manager admin = null;

    public RoomService(Manager admin) {
        this.admin = admin;
    }

    /*
     * --------------------------------------------------------------------------------------------
     * ROOM MANAGEMENT
     * --------------------------------------------------------------------------------------------
     */

    public synchronized int generateRoomId() {
        return roomIdCounter++;
    }

    // Get all rooms
    public LinkedList<Room> getAllRooms(User requester) throws IllegalAccessException {
        if (!requester.getRole().equals("manager") && !requester.getRole().equals("employee")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores ou funcionários podem visualizar a lista de quartos.");
        }
        return roomList;
    }

    // Get a room by ID
    public Room getRoomById(User user, int id) throws IllegalAccessException {
        if (!user.getRole().equals("manager")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores podem obter um quarto por ID.");
        }
        for (Room room : roomList) {
            if (room.getId() == id) {
                return room;
            }
        }
        throw new IllegalArgumentException("Quarto com ID " + id + " não encontrado.");
    }

    // Register a new room
    public String registerRoom(User user, int maxGuests, int numBeds, String viewType, boolean hasKitchen,
                               boolean hasBalcony, int numWC, double pricePerNight) throws IllegalAccessException {
        if (!user.getRole().equals("manager")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores podem editar quartos.");
        }

        if (!validateRoomData(maxGuests, numBeds, viewType, numWC, pricePerNight)) {
            throw new IllegalArgumentException("Dados fornecidos para registo do quarto são inválidos.");
        }

        int roomId = generateRoomId();
        Room newRoom = new Room(
                roomId,
                maxGuests,
                numBeds,
                viewType,
                hasKitchen,
                hasBalcony,
                numWC,
                pricePerNight,
                "available",
                LocalDate.now(),
                user.getId(),
                false
        );

        roomList.add(newRoom);
        return "Quarto criado com sucesso com o ID " + roomId;
    }

    // Edit a room
    public String editRoom(User user, int roomId, Integer maxGuests, Integer numBeds, String viewType,
                           Boolean hasKitchen, Boolean hasBalcony, Integer numWC, Double pricePerNight) throws IllegalAccessException {

        if (!user.getRole().equals("manager")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores podem editar quartos.");
        }

        Room room = getRoomById(user, roomId);

        if (maxGuests != null){
            if(maxGuests > 0) room.setMaxGuests(maxGuests);
            else throw new IllegalArgumentException("Número máximo de hóspedes inválido. Insira um número superior a 0 (zero).");
        }
        if (numBeds != null){
            if(numBeds > 0) room.setNumBeds(numBeds);
            else throw new IllegalArgumentException("Número de camas inválido. Insira um número superior a 0 (zero).");
        }
        if (viewType != null) {
            if(viewType.equalsIgnoreCase("serra") || viewType.equalsIgnoreCase("mar")) room.setViewType(viewType);
            else throw new IllegalArgumentException("Tipo de vista inválido. Insira 'mar' ou 'serra'");
        }
        if (hasKitchen != null) room.setHasKitchen(hasKitchen);
        if (hasBalcony != null) room.setHasBalcony(hasBalcony);
        if (numWC != null) {
            if(numWC > 0) room.setNumWC(numWC);
            else throw new IllegalArgumentException("Número de WCs inválido. Insira um número superior a 0 (zero).");
        }
        if (pricePerNight != null){
            if(pricePerNight > 0) room.setPricePerNight(pricePerNight);
            else throw new IllegalArgumentException("Preço por noite inválido. Insira um valor superior a 0.0 EUR.");
        }

        return "Quarto com o ID " + roomId + " editado com sucesso.";
    }

    // Remove a room
    public String removeRoom(User user, int roomId) throws IllegalAccessException {
        if (!user.getRole().equals("manager")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores podem remover quartos.");
        }

        Room room = getRoomById(user, roomId);

        if (!"available".equalsIgnoreCase(room.getStatus())) {
            throw new IllegalArgumentException("Erro: O quarto não pode ser removido porque está ocupado ou inativo.");
        }

        room.setStatus("inactive");
        return "Quarto com ID " + roomId + " removido com sucesso.";
    }


    public void updateRoomMaintenanceStatus(User user, int roomId, boolean status) throws IllegalAccessException {
        if (!user.getRole().equalsIgnoreCase("manager") && !user.getRole().equalsIgnoreCase("employee")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores ou funcionários podem atualizar o estado de manutenção do quarto.");
        }
        Room room = getRoomById(admin, roomId);
        room.setNeedsMaintenance(status);
    }

    /*
     * --------------------------------------------------------------------------------------------
     * VALIDATION METHODS
     * --------------------------------------------------------------------------------------------
     */

    public boolean validateRoomData(int maxGuests, int numBeds, String viewType, int numWC, double pricePerNight) {
        return maxGuests > 0 &&
                numBeds > 0 &&
                viewType != null &&
                (viewType.equalsIgnoreCase("serra") || viewType.equalsIgnoreCase("mar")) &&
                numWC > 0 &&
                pricePerNight > 0.0;
    }


}
