package com.thehotel;

import com.thehotel.model.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class HotelServerTest {
    private Manager admin;

    @BeforeEach
    void setUp () {
        admin = new Manager(0, "Admin", "123456789", "admin@example.com", LocalDate.of(1980, 1, 1), "Rua A",
                "1234-567", "Portugal", "912345678", "PT50001234567890123456789", "12345678", null);
    }

    //------------
    //HotelServer-
    //------------

    //Verify if the admin acc has been created
    @Test
    void HotelServerTestAdminAccCreate() {
        HotelServer server = HotelServer.getInstance();
        Manager admin = (Manager) server.getAccountService().getUserById(0);

        assertNotNull(admin);
        assertEquals("Admin Manager", admin.getFullName());
        assertEquals("111111111", admin.getNif());
        assertEquals("Rua das Rosas, 23", admin.getAddress());
        assertEquals("910000000", admin.getPhone());
        assertEquals("PT50000000000000000000000", admin.getIban());
        assertEquals("123456789", admin.getCitizenCard());
        assertEquals(LocalDate.of(2025, 1, 1), admin.getBirthDate());
        assertEquals("Admin!1234", admin.getPassword());
    }

    //------------
    //getInstance-
    //------------

    //Test for getInstance Initialization
    @Test
    void testGetInstanceInitialization() {
        HotelServer instance1 = HotelServer.getInstance();
        HotelServer instance2 = HotelServer.getInstance();

        assertSame(instance1, instance2);
        assertNotNull(instance1);
    }

    //------------
    //AllServices-
    //------------

    //Test for services initialization
    @Test
    void TestServicesInitialization() {
        HotelServer server = HotelServer.getInstance();

        assertNotNull(server.getAccountService());
        assertNotNull(server.getRoomService());
        assertNotNull(server.getMaintenanceService());
        assertNotNull(server.getReservationService());
        assertNotNull(server.getHotelInfo());
    }

    //------------------
    //getAccountService-
    //------------------

    //Test accountService
    @Test
    void testAccountService() throws IllegalAccessException {
        HotelServer server = HotelServer.getInstance();

        assertNotNull(server.getAccountService());
        assertFalse(server.getAccountService().getAllUsers(admin).isEmpty());
    }

    //---------------
    //getRoomService-
    //---------------

    //Test roomService
    @Test
    void testRoomServiceInitialization() throws IllegalAccessException {
        HotelServer server = HotelServer.getInstance();

        assertNotNull(server.getRoomService());
        assertTrue(server.getRoomService().getAllRooms(admin).isEmpty());
    }

    //----------------------
    //getMaintenanceService-
    //----------------------

    //Test MaintenanceService
    @Test
    void testMaintenanceInitialization() throws IllegalAccessException {
        HotelServer server = HotelServer.getInstance();

        assertNotNull(server.getMaintenanceService());
        assertTrue(server.getMaintenanceService().getAllMaintenances(admin).isEmpty());
    }

    //----------------------
    //getReservationService-
    //----------------------

    //Test ReservationService
    @Test
    void testReservationsInitialization() throws IllegalAccessException {
        HotelServer server = HotelServer.getInstance();

        assertNotNull(server.getReservationService());
        assertTrue(server.getReservationService().getAllReservations(admin).isEmpty());
    }

    //-------------
    //getHotelInfo-
    //-------------

    //Test HotelInfo
    @Test
    void testGetHotelInfo() {
        HotelServer server = HotelServer.getInstance();

        String hotelInfo = server.getHotelInfo();
        assertNotNull(hotelInfo);

        String expectedOutput = String.format(
                "Nome: %s%n" +
                        "Endereço: %s%n" +
                        "Classificação: %s%n" +
                        "Descrição: %n%s%n%n" +
                        "Comodidades: %n" +
                        "-> %s%n" +
                        "-> %s%n" +
                        "-> %s%n" +
                        "-> %s%n" +
                        "-> %s%n" +
                        "-> %s%n" +
                        "-> %s%n" +
                        "-> %s%n%n" +
                        "Horário de Check-in e Check-out: %n" +
                        "-> Check-in : %s%n" +
                        "-> Check-out : %s%n%n" +
                        "Política de Cancelamento: %n%s%n%n" +
                        "Contatos: %n" +
                        "-> Telefone: %s%n" +
                        "-> Email: %s%n",
                "Hotel InfoUÉvora",
                "R. Romão Ramalho 59, 7002-554 Évora",
                "***** Estrelas",
                """
                        Situado no coração da histórica cidade de Évora, o hotel InfoUÉvora é o destino ideal para quem procura conforto e sofisticação.
                        Com uma localização privilegiada, este hotel combina a autenticidade do património cultural alentejano com o luxo e modernidade das instalações.\s
                        Desfrute de quartos elegantemente decorados, um serviço de excelência e uma ampla gama de comodidades que garantem uma estadia memorável, \
                        seja para negócios ou lazer.
                        Relaxe no nosso spa exclusivo e piscina exterior aquecida, saboreie pratos típicos no restaurante gourmet e contemple a vista deslumbrante sobre \
                        a cidade a partir do nosso rooftop.""",
                "Piscina exterior aquecida",
                "Rooftop",
                "Spa e centro de bem-estar",
                "Ginásio totalmente equipado",
                "Restaurante gourmet com pratos regionais",
                "Serviço de quarto 24 horas",
                "Wi-fi gratuito em todas as áreas",
                "Estacionamento gratuito",
                "a partir das 15h00",
                "até às 12h00",
                "Cancelamento gratuito até 24 horas antes do check-in",
                "+351 266 123 456",
                "reservas@hotel-infouevora.pt"
        );
        assertEquals(expectedOutput, server.getHotelInfo());
    }
}