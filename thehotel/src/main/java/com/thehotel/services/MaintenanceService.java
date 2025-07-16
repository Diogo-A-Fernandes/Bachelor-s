package com.thehotel.services;

import com.thehotel.model.Maintenance;
import com.thehotel.model.Manager;
import com.thehotel.model.Room;
import com.thehotel.model.User;

import java.time.LocalDate;
import java.util.LinkedList;

public class MaintenanceService {
    private int maintenanceIdCounter = 0;
    private final LinkedList<Maintenance> maintenanceList = new LinkedList<>();
    private Manager admin = null;

    public MaintenanceService(Manager admin) {
       this.admin = admin;
    }

    /*
     * --------------------------------------------------------------------------------------------
     * MAINTENANCE MANAGEMENT
     * --------------------------------------------------------------------------------------------
     */
    public synchronized int generateMaintenanceId() {
        return maintenanceIdCounter++;
    }

    public LinkedList<Maintenance> getAllMaintenances(User user) throws IllegalAccessException {
        if (!user.getRole().equals("manager") && !user.getRole().equals("employee")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores ou funcionários podem visualizar a lista de manutenções.");
        }
        return maintenanceList;
    }

    // get maintenance by ID
    public Maintenance getMaintenanceById(User user, int maintenanceId) throws IllegalAccessException {
        if (!user.getRole().equals("manager") && !user.getRole().equals("employee")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores ou funcionários podem obter uma manutenção por ID.");
        }
        for (Maintenance maintenance : maintenanceList) {
            if (maintenance.getId() == maintenanceId) {
                return maintenance;
            }
        }
        throw new IllegalArgumentException("Manutenção com ID " + maintenanceId + " não encontrada.");
    }

    // Register a new maintenance
    public String registerMaintenance(User user, int roomId, String description, RoomService roomService) throws IllegalAccessException {
        if (!user.getRole().equals("manager") && !user.getRole().equals("employee")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores ou funcionários podem registar manutenção.");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: A descrição da manutenção é obrigatória para o registo de nova manutenção.");
        }

        roomService.updateRoomMaintenanceStatus(user, roomId, true);

        int maintenanceId = generateMaintenanceId();
        Maintenance newMaintenance = new Maintenance(
                maintenanceId,
                roomId,
                description,
                user.getId()
        );

        maintenanceList.add(newMaintenance);

        return "Manutenção a realizar registada com sucesso com o ID " + maintenanceId + " para o quarto " + roomId + ".";
    }

    // Complete a maintenance
    public String registerMaintenanceCompletion(User user, int maintenanceId, LocalDate conclusionDate, RoomService roomService) throws IllegalAccessException {
        if (!user.getRole().equals("manager") && !user.getRole().equals("employee")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores ou funcionários podem registar uma manutenção finalizada.");
        }

        if (conclusionDate == null || conclusionDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Erro: A data de conclusão é inválida ou futura.");
        }

        Maintenance maintenance = getMaintenanceById(user, maintenanceId);

        if (!maintenance.getStatus().equalsIgnoreCase("pendent")) {
            throw new IllegalArgumentException("Erro: A manutenção com o ID " + maintenanceId + " não está no estado 'Pendente'. Estado atual: " + maintenance.getStatus());
        }

        maintenance.setStatus("completed");
        maintenance.setDateConclusion(conclusionDate);
        maintenance.setIdUserConclusion(user.getId());

        //verifies if room has no more maintenances and in that case updates room maintenance status
        if (!roomHasPendingMaintenances(maintenance.getRoomId())) {
            roomService.updateRoomMaintenanceStatus(user, maintenance.getRoomId(), false);
        }

        return "Manutenção com ID " + maintenanceId + " foi concluída com sucesso para o quarto " + maintenance.getRoomId();
    }

    public String cancelMaintenance(User user, int maintenanceId, RoomService roomService) throws IllegalAccessException {
        if (!user.getRole().equals("manager") && !user.getRole().equals("employee")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores ou funcionários podem cancelar manutenções.");
        }

        Maintenance maintenance = getMaintenanceById(user, maintenanceId);

        if (!maintenance.getStatus().equalsIgnoreCase("pendent")) {
            throw new IllegalArgumentException("Erro: Não é possível cancelar manutenções que não estejam no estado Pendente. Estado atual: " + maintenance.getStatus());
        }

        maintenance.setStatus("cancelled");
        maintenance.setDateCancellation(LocalDate.now());
        maintenance.setIdUserCancellation(user.getId());

        //verifies if room has no more maintenances and in that case updates room maintenance status
        if (!roomHasPendingMaintenances(maintenance.getRoomId())) {
            roomService.updateRoomMaintenanceStatus(user, maintenance.getRoomId(), false);
        }

        return "Manutenção com ID " + maintenanceId + " do quarto " + maintenance.getRoomId() + " foi cancelada com sucesso";
    }

    // List maintenances by status
    public LinkedList<Maintenance> getMaintenanceByStatus(User user, String status) throws IllegalAccessException {
        if (!user.getRole().equals("manager") && !user.getRole().equals("employee")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores ou funcionários podem visualizar a lista de manutenções.");
        }

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("O parâmetro 'status' não pode ser nulo ou vazio.");
        }

        if (!status.equalsIgnoreCase("pendent") && !status.equalsIgnoreCase("completed") && !status.equalsIgnoreCase("cancelled")) {
            throw new IllegalArgumentException("status inválido. Insira: 'pendent', 'completed', 'cancelled'");
        }

        LinkedList<Maintenance> newList = new LinkedList<Maintenance>();

        for(Maintenance maintenance : maintenanceList){
            if(maintenance.getStatus().equalsIgnoreCase(status)) newList.add(maintenance);
        }
        return newList;
    }

    public boolean roomHasPendingMaintenances(int roomId){
        for(Maintenance maintenance : maintenanceList){
            if(maintenance.getRoomId() == roomId && maintenance.getStatus().equalsIgnoreCase("pendent"))
                return true;
        }
        return false;
    }
}