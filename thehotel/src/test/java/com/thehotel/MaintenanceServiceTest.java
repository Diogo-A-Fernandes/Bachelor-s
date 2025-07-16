package com.thehotel;

import com.thehotel.model.*;
import com.thehotel.services.MaintenanceService;
import com.thehotel.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;


public class MaintenanceServiceTest {
    private MaintenanceService maintenanceService;
    private RoomService roomService;
    private Manager manager;
    private Guest guest;
    private Employee employee;
    private int roomId;

    @BeforeEach
    void setUp() throws Exception {
        //manager
        manager = new Manager(0, "Admin", "123456789", "admin@example.com", LocalDate.of(1980, 1, 1), "Rua A",
                "1234- 567", "Portugal", "912345678", "PT50001234567890123456789", "12345678", null);

        //employee
        employee = new Employee(1, "Employee", "987654321", "employee@example.com", LocalDate.of(1990, 2, 1), "Rua B",
                "2345- 123", "Portugal", "917654321", "PT50001234567890123456788", "87654321", null);

        //guest
        guest = new Guest(2, "Guest", "123456781", "guest@example.com", LocalDate.of(1992, 4, 5), "Rua C",
                "2330-123", "Portugal", "9123456781", null);

        roomService = new RoomService(manager);
        maintenanceService = new MaintenanceService(manager);

        roomService.registerRoom(manager, 2, 2, "Mar", true, true, 2, 100.0);

        Room registeredRoom = roomService.getAllRooms(manager).getLast();
        roomId = registeredRoom.getId();

        maintenanceService.registerMaintenance(manager, roomId, "Trocar a lâmpada", roomService);

    }

    //-----------------------------
    //generateMaintenanceId Method-
    //-----------------------------

    //Success case, return the IDS sequence correctly
    @Test
    void generateMaintenanceIdTest() {
        int id1 = maintenanceService.generateMaintenanceId();
        int id2 = maintenanceService.generateMaintenanceId();

        assertEquals(1, id1);
        assertEquals(2, id2);
    }

    //Test to large numbers IDs
    @Test
    void generateMaintenanceIdTestLargeIds(){
        for (int i = 1; i < 100; i++){
            maintenanceService.generateMaintenanceId();
        }

        assertEquals(100, maintenanceService.generateMaintenanceId());
    }

    //--------------------------
    //getAllMaintenances Method-
    //--------------------------

    //Success case, manager accessing the MaintenanceList
    @Test
    void getAllMaintenancesTest() throws IllegalAccessException {
        LinkedList<Maintenance> result = maintenanceService.getAllMaintenances(manager);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    //Success case, employee accessing the Maintenance List
    @Test
    void getAllMaintenancesTestAsEmployee() throws IllegalAccessException {
        LinkedList<Maintenance> result = maintenanceService.getAllMaintenances(employee);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
    //Permissions denied, user without permissions -> Guest
    @Test
    void getAllMaintenancesTestAsGuest() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () ->{
            maintenanceService.getAllMaintenances(guest);
        });

        assertEquals("Acesso não autorizado: Apenas gestores ou funcionários podem visualizar a lista de manutenções.", exception.getMessage());
    }

    //--------------------------
    //getMaintenanceById Method-
    //--------------------------

    //Success case, manager getting the maintenance ID
    @Test
    void getMaintenanceByIdTest() throws IllegalAccessException {
        Maintenance result = maintenanceService.getMaintenanceById(manager, 0);

        assertNotNull(result);
        assertEquals(0, result.getId());
        assertEquals("Trocar a lâmpada", result.getDescription());
        assertEquals(0, result.getRoomId());
    }

    //Success case, employee getting the maintenance ID
    @Test
    void getMaintenanceByIdTestAsEmployee() throws IllegalAccessException {
        Maintenance result = maintenanceService.getMaintenanceById(employee, 0);

        assertNotNull(result);
        assertEquals(0, result.getId());
        assertEquals("Trocar a lâmpada", result.getDescription());
        assertEquals(0, result.getRoomId());
    }

    //Access denied, user without permissions -> Guest
    @Test
    void getMaintenanceByIdTestAsGuest() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            maintenanceService.getMaintenanceById(guest, 0);
        });

        assertEquals("Acesso não autorizado: Apenas gestores ou funcionários podem obter uma manutenção por ID.", exception.getMessage());
    }

    //Non-existing ID
    @Test
    void getMaintenanceByIdTestNonExistingId() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            maintenanceService.getMaintenanceById(manager, 10);
        });

        assertEquals("Manutenção com ID 10 não encontrada.", exception.getMessage());
    }

    //---------------------------
    //registerMaintenance Method-
    //---------------------------

    //Success case, manager registers a maintenance
    @Test
    void registerMaintenanceTest() throws  IllegalAccessException {
        String result = maintenanceService.registerMaintenance(manager, roomId, "Trocar o ar condicionado", roomService);

        assertNotNull(result);
        assertEquals("Manutenção a realizar registada com sucesso com o ID 1 para o quarto 0.", result);

        Maintenance maintenance = maintenanceService.getMaintenanceById(manager, 1);
        assertEquals(1, maintenance.getId());
        assertEquals(roomId, maintenance.getRoomId());
        assertEquals("Trocar o ar condicionado", maintenance.getDescription());
        assertEquals(LocalDate.now(), maintenance.getDateRegister());
        assertEquals(manager.getId(), maintenance.getIdUserRegister());
        assertEquals(-1, maintenance.getIdUserCancellation());
        assertEquals(-1, maintenance.getIdUserConclusion());
        assertNull(maintenance.getDateCancellation());
        assertNull(maintenance.getDateConclusion());
        assertEquals("pendent", maintenance.getStatus());

    }

    //Success case, employee registers a maintenance
    @Test
    void registerMaintenanceTestAsEmployee() throws IllegalAccessException {
        String result = maintenanceService.registerMaintenance(employee, roomId, "Limpar o ar condicionado", roomService);

        assertNotNull(result);
        assertEquals("Manutenção a realizar registada com sucesso com o ID 1 para o quarto 0.", result);
        //assertTrue(result.contains("Limpar o ar condicionado"));

        Maintenance maintenance = maintenanceService.getMaintenanceById(employee, 1);
        assertEquals(1, maintenance.getId());
        assertEquals(roomId, maintenance.getRoomId());
        assertEquals("Limpar o ar condicionado", maintenance.getDescription());
        assertEquals(LocalDate.now(), maintenance.getDateRegister());
        assertEquals(employee.getId(), maintenance.getIdUserRegister());
        assertEquals(-1, maintenance.getIdUserCancellation());
        assertEquals(-1, maintenance.getIdUserConclusion());
        assertNull(maintenance.getDateCancellation());
        assertNull(maintenance.getDateConclusion());
        assertEquals("pendent", maintenance.getStatus());
    }

    //Permissions denied, user without permissions -> Guest
    @Test
    void registerMaintenanceTestAsGuest() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            maintenanceService.registerMaintenance(guest, roomId, "Trocar o candeeiro", roomService);
        });

        assertEquals("Acesso não autorizado: Apenas gestores ou funcionários podem registar manutenção.", exception.getMessage());
    }

    //Description empty
    @Test
    void registerMaintenanceTestEmptyDescription() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            maintenanceService.registerMaintenance(manager, roomId, "", roomService);
        });

        assertEquals("Erro: A descrição da manutenção é obrigatória para o registo de nova manutenção.", exception.getMessage());
    }

    //Description null
    @Test
    void reagisterMaintenanceTestNullDescription() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            maintenanceService.registerMaintenance(manager, roomId, null, roomService);
        });

        assertEquals("Erro: A descrição da manutenção é obrigatória para o registo de nova manutenção.", exception.getMessage());
    }

    //Invalid RoomId
    @Test
    void registerMaintenanceTestInvalidRoomId() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            maintenanceService.registerMaintenance(manager, 10, "Trocar AC", roomService);
        });

        assertEquals("Quarto com ID 10 não encontrado.", exception.getMessage());
    }

    //------------------------------
    //registerMaintenanceCompletion-
    //------------------------------

    //Success case, manager conclude the maintenance
    @Test
    void registerMaintenanceCompletionTest() throws IllegalAccessException {
        LocalDate conclusionDate = LocalDate.now();
        String result = maintenanceService.registerMaintenanceCompletion(manager, 0, conclusionDate, roomService);

        assertNotNull(result);
        assertEquals("Manutenção com ID 0 foi concluída com sucesso para o quarto 0", result);
        Maintenance maintenance = maintenanceService.getMaintenanceById(manager, 0);
        assertEquals("completed", maintenance.getStatus());
        assertEquals(conclusionDate, maintenance.getDateConclusion());
        assertEquals(manager.getId(), maintenance.getIdUserConclusion());
    }

    //Success case, employee conclude the maintenance
    @Test
    void registerMaintenanceCompletionTestAsEmployee() throws IllegalAccessException {
        LocalDate conclusionDate = LocalDate.now();
        String result = maintenanceService.registerMaintenanceCompletion(employee, 0, conclusionDate, roomService);

        assertNotNull(result);
        assertEquals("Manutenção com ID 0 foi concluída com sucesso para o quarto 0", result);
        Maintenance maintenance = maintenanceService.getMaintenanceById(employee, 0);
        assertEquals("completed", maintenance.getStatus());
        assertEquals(conclusionDate, maintenance.getDateConclusion());
        assertEquals(employee.getId(), maintenance.getIdUserConclusion());
    }

    //Permissions denied, user without permissions -> Guest
    @Test
    void registerMaintenanceCompletionTestAsGuest() {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            maintenanceService.registerMaintenanceCompletion(guest, 0, LocalDate.now(), roomService);
        });

        assertEquals("Acesso não autorizado: Apenas gestores ou funcionários podem registar uma manutenção finalizada.", exception.getMessage());
    }

    //Null conclusion date
    @Test
    void registerMaintenanceCompletionTestNullDate() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            maintenanceService.registerMaintenanceCompletion(manager, 0, null, roomService);
        });

        assertEquals("Erro: A data de conclusão é inválida ou futura.", exception.getMessage());
    }

    //Future date
    @Test
    void registerMaintenanceCompletionTestFutureDate() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            maintenanceService.registerMaintenanceCompletion(manager, 0, LocalDate.of(2026,1,1), roomService);
        });

        assertEquals("Erro: A data de conclusão é inválida ou futura.", exception.getMessage());
    }

    //Conclude a maintenance that it was not pendent status
    @Test
    void registerMaintenanceCompletionTestNotPendentStatus() throws IllegalArgumentException, IllegalAccessException {
        Maintenance maintenance = maintenanceService.getMaintenanceById(manager, 0);
        maintenance.setStatus("completed");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            maintenanceService.registerMaintenanceCompletion(manager, 0, LocalDate.now(), roomService);
        });

        assertEquals("Erro: A manutenção com o ID 0 não está no estado 'Pendente'. Estado atual: completed", exception.getMessage());
    }

    //------------------
    //cancelMaintenance-
    //------------------

    //Success case, manager cancel a maintenance
    @Test
    void cancelMaintenanceTest() throws IllegalAccessException {
        String result = maintenanceService.cancelMaintenance(manager, 0, roomService);
        assertNotNull(result);
        assertEquals("Manutenção com ID 0 do quarto 0 foi cancelada com sucesso", result);

        Maintenance maintenance = maintenanceService.getMaintenanceById(manager, 0);
        assertEquals("cancelled", maintenance.getStatus());
        assertEquals(LocalDate.now(), maintenance.getDateCancellation());
        assertEquals(manager.getId(), maintenance.getIdUserCancellation());
    }

    //Success case, employee cancel a maintenance
    @Test
    void cancelMaintenanceTestAsEmployee() throws IllegalAccessException {
        String result = maintenanceService.cancelMaintenance(employee, 0, roomService);
        assertNotNull(result);
        assertEquals("Manutenção com ID 0 do quarto 0 foi cancelada com sucesso", result);

        Maintenance maintenance = maintenanceService.getMaintenanceById(employee, 0);
        assertEquals("cancelled", maintenance.getStatus());
        assertEquals(LocalDate.now(), maintenance.getDateCancellation());
        assertEquals(employee.getId(), maintenance.getIdUserCancellation());
    }

    //Permissions denied, user without permissions -> Guest
    @Test
    void cancelMaintenanceTestAsGuest() {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            maintenanceService.cancelMaintenance(guest, 0, roomService);
        });

        assertEquals("Acesso não autorizado: Apenas gestores ou funcionários podem cancelar manutenções.", exception.getMessage());
    }

    //Cancel a maintenance with not pending status
    @Test
    void cancelMaintenanceTestNonPendingStatus() throws IllegalArgumentException, IllegalAccessException {
        Maintenance maintenance = maintenanceService.getMaintenanceById(manager, 0);
        maintenance.setStatus("completed");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            maintenanceService.cancelMaintenance(manager, 0, roomService);
        });

        assertEquals("Erro: Não é possível cancelar manutenções que não estejam no estado Pendente. Estado atual: completed", exception.getMessage());
    }

    //-----------------------
    //getMaintenanceByStatus-
    //-----------------------

    //Success case, return maintenances with pendent status
    @Test
    void getMaintenanceByStatusTestPendentStatus() throws IllegalAccessException {
        LinkedList<Maintenance> result = maintenanceService.getMaintenanceByStatus(manager, "pendent");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        for(Maintenance maintenance : result ) {
            assertEquals("pendent", maintenance.getStatus().toLowerCase());
        }
    }

    //Success case, return maintenances with completed status
    @Test
    void getMaintenancesByStatusTestCompletedStatus() throws IllegalAccessException {
        Maintenance maintenance1 = maintenanceService.getMaintenanceById(manager, 0);
        maintenance1.setStatus("completed");
        LinkedList<Maintenance> result = maintenanceService.getMaintenanceByStatus(manager, "completed");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        for(Maintenance maintenance : result ) {
            assertEquals("completed", maintenance.getStatus().toLowerCase());
        }
    }

    //Success case, return maintenances with cancelled status
    @Test
    void getMaintenancesByStatusTestCancelledStatus() throws IllegalAccessException {
        Maintenance maintenance1 = maintenanceService.getMaintenanceById(manager, 0);
        maintenance1.setStatus("cancelled");
        LinkedList<Maintenance> result = maintenanceService.getMaintenanceByStatus(manager, "cancelled");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        for(Maintenance maintenance : result ) {
            assertEquals("cancelled", maintenance.getStatus().toLowerCase());
        }
    }

    //Permissions denied, user without permissions -> Guest
    @Test
    void getMaintenancesByStatusTestAsGuest() {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            maintenanceService.getMaintenanceByStatus(guest, "pendent");
        });

        assertEquals("Acesso não autorizado: Apenas gestores ou funcionários podem visualizar a lista de manutenções.", exception.getMessage());
    }

    //Status null
    @Test
    void getMaintenancesByStatusTestWithNullStatus() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            maintenanceService.getMaintenanceByStatus(manager, null);
        });

        assertEquals("O parâmetro 'status' não pode ser nulo ou vazio.", exception.getMessage());
    }

    //Status Empty
    @Test
    void getMaintenancesByStatusTestWithEmptyStatus() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            maintenanceService.getMaintenanceByStatus(manager, "");
        });

        assertEquals("O parâmetro 'status' não pode ser nulo ou vazio.", exception.getMessage());
    }

    //Invalid Status
    @Test
    void getMaintenancesByStatusTestWithInvalidStatus() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            maintenanceService.getMaintenanceByStatus(manager, "Invalid");
        });

        assertEquals("status inválido. Insira: 'pendent', 'completed', 'cancelled'", exception.getMessage());
    }

    //---------------------------
    //roomHasPendingMaintenances-
    //---------------------------

    //Success case, returning true, because we have pending maintenances
    @Test
    void roomHasPendingMaintenancesTest() {
        boolean result = maintenanceService.roomHasPendingMaintenances(0);
        assertTrue(result);
    }

    //Returning false, because we don't have pending maintenances
    @Test
    void roomHasPendingMaintenancesTestWithNoPendingMaintenances() throws IllegalAccessException {
        Maintenance maintenance1 = maintenanceService.getMaintenanceById(manager, 0);
        maintenance1.setStatus("cancelled");

        boolean result = maintenanceService.roomHasPendingMaintenances(0);
        assertFalse(result);
    }
}