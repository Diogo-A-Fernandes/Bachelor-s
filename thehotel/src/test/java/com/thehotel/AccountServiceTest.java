package com.thehotel;

import com.thehotel.model.Employee;
import com.thehotel.model.Guest;
import com.thehotel.model.Manager;
import com.thehotel.model.User;
import com.thehotel.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {
    private AccountService accountService;
    private Manager manager;
    private Guest guest;
    private Employee employee;

    @BeforeEach
    void setUp() throws Exception {
        accountService = new AccountService();

        //manager
        manager = new Manager(2, "Admin", "123456789", "admin@example.com", LocalDate.of(1980, 1, 1), "Rua A",
                "1234- 567", "Portugal", "912345678", "PT50001234567890123456789", "12345678", null);

        //employee
        employee = new Employee(1, "Employee", "987654321", "employee@example.com", LocalDate.of(1990, 2, 1), "Rua B",
                "2345- 123", "Portugal", "917654321", "PT50001234567890123456788", "87654321", null);

        //guest
        guest = new Guest(0, "Test", "123456789", "test@example.com", LocalDate.of(1990, 1, 1),
                "123 Rua A", "1234-567", "Portugal", "987654321", null);

        //Acc create example
        accountService.createGuestAccount("Test", "123456789", "test@example.com", LocalDate.of(1990, 1, 1),
                "123 Rua A", "1234-567", "Portugal", "987654321", null);

        accountService.createStaffAccount(manager, "employee", "Test", "987654321", "testt@example.com", LocalDate.of(1980, 1, 1),
                "124 Rua Z", "2345-978", "Portugal", "987654312", "PT500000000000000000001", "12213456", null);
    }

    //----------------------
    //generateUserId Method-
    //----------------------

    //Success, case return de IDS sequence correctly
    @Test
    void generateUserIdTest() {
        int id1 = accountService.generateUserId();
        int id2 = accountService.generateUserId();
        int id3 = accountService.generateUserId();

        //Start in number 1 because we created 1 user in setUp function
        assertEquals(2, id1);
        assertEquals(3, id2);
        assertEquals(4, id3);
    }

    //Test to Large Numbers IDs
    @Test
    void generateUserIdTestLargeNumbers () {
        for(int i = 2; i < 100; i++) {
            accountService.generateUserId();
        }

        assertEquals(100, accountService.generateUserId());
    }

    //--------------------------
    //createGuestAccount Method-
    //--------------------------

    //Success, creation a valid acc
    @Test
    void createGuestAccountTest() throws Exception {
        String acc1 = accountService.createGuestAccount("Test1", "234567891", "test1@example.com", LocalDate.of(1980, 1, 2),
                "24 Rua B", "2345-567", "Portugal", "912345678", null);

        Guest expectedGuest = new Guest(2, "Test1", "234567891", "test1@example.com",
                LocalDate.of(1980, 1, 2), "24 Rua B", "2345-567", "Portugal", "912345678", null);

        assertNotNull(acc1);
        assertTrue(acc1.contains("Conta de Hóspede criada com sucesso com o ID"));

        User actualGuest = accountService.getUserById(2);
        assertInstanceOf(Guest.class, actualGuest);

        assertEquals(expectedGuest.getId(), ((Guest) actualGuest).getId());
        assertEquals(expectedGuest.getFullName(), ((Guest) actualGuest).getFullName());
        assertEquals(expectedGuest.getNif(), ((Guest) actualGuest).getNif());
        assertEquals(expectedGuest.getEmail(), ((Guest) actualGuest).getEmail());
        assertEquals(expectedGuest.getBirthDate(), ((Guest) actualGuest).getBirthDate());
        assertEquals(expectedGuest.getAddress(), ((Guest) actualGuest).getAddress());
        assertEquals(expectedGuest.getZipCode(), ((Guest) actualGuest).getZipCode());
        assertEquals(expectedGuest.getCountry(), ((Guest) actualGuest).getCountry());
        assertEquals(expectedGuest.getPhone(), ((Guest) actualGuest).getPhone());
    }

    //Create an acc with invalid Name
    @Test
    void createGuestAccountTestInvalidName() throws Exception {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createGuestAccount("", "123456789", "test2@example.com", LocalDate.of(1990, 1, 1),
                    "25 Rua C", "4567-234", "Portugal", "968754321", null);
        });

        assertEquals("Um ou mais dados fornecidos são inválidos.", exception.getMessage());
    }

    //Invalid Nif
    @Test
    void createGuestAccountTestInvalidNif() throws Exception {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createGuestAccount("Test Acc", "12345678", "test2@example.com", LocalDate.of(1990, 1, 1),
                    "25 Rua C", "4567-234", "Portugal", "968754321", null);
        });

        assertEquals("Um ou mais dados fornecidos são inválidos.", exception.getMessage());
    }

    //Invalid email
    @Test
    void createGuestAccountTestInvalidEmail() throws Exception {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createGuestAccount("Test Acc", "123456789", "test2.example.com", LocalDate.of(1990, 1, 1),
                    "25 Rua C", "4567-234", "Portugal", "968754321", null);
        });

        assertEquals("Um ou mais dados fornecidos são inválidos.", exception.getMessage());
    }

    //Invalid date
    @Test
    void createGuestAccountTestInvalidDate() throws Exception {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createGuestAccount("Test Acc", "123456789", "test2@example.com", null,
                    "25 Rua C", "4567-234", "Portugal", "968754321", null);
        });

        assertEquals("Um ou mais dados fornecidos são inválidos.", exception.getMessage());
    }

    //Invalid Address
    @Test
    void createGuestAccountTestInvalidAddress() throws Exception {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createGuestAccount("Test Acc", "123456789", "test2@example.com", LocalDate.of(1990, 1, 1),
                    "", "4567-234", "Portugal", "968754321", null);
        });

        assertEquals("Um ou mais dados fornecidos são inválidos.", exception.getMessage());
    }

    //Invalid zip-code
    @Test
    void createGuestAccountTestInvalidZipCode() throws Exception {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createGuestAccount("Test Acc", "123456789", "test2@example.com", LocalDate.of(1990, 1, 1),
                    "25 Rua C", "456788-234", "Portugal", "968754321", null);
        });

        assertEquals("Um ou mais dados fornecidos são inválidos.", exception.getMessage());
    }

    //Invalid Country
    @Test
    void createGuestAccountTestInvalidCountry() throws Exception {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createGuestAccount("Test Acc", "123456789", "test2@example.com", LocalDate.of(1990, 1, 1),
                    "25 Rua C", "4567-234", "", "968754321", null);
        });

        assertEquals("Um ou mais dados fornecidos são inválidos.", exception.getMessage());
    }

    //Invalid Phone
    @Test
    void createGuestAccountTestInvalidPhone() throws Exception {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createGuestAccount("Test Acc", "123456789", "test2@example.com", LocalDate.of(1990, 1, 1),
                    "25 Rua C", "4567-234", "Portugal", "96875421", null);
        });

        assertEquals("Um ou mais dados fornecidos são inválidos.", exception.getMessage());
    }

    //Create an acc with a duplicate NIF
    @Test
    void createGuestAccountTestDuplicateNif() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createGuestAccount("Test3", "123456789", "test3@example.com", LocalDate.of(1980, 1, 3),
                    "26 Rua D", "2341-543", "Portugal", "912345656", null);
        });

        assertEquals("Email ou NIF já estão associados a uma conta existente.", exception.getMessage());
    }

    //Create an acc with a duplicate email
    @Test
    void createGuestAccountTestDuplicateEmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createGuestAccount("Test4", "123453123", "test@example.com", LocalDate.of(1990, 4, 2),
                    "27 Rua E", "4567-321", "Portugal", "987896532", null);
        });

        assertEquals("Email ou NIF já estão associados a uma conta existente.", exception.getMessage());
    }

    //--------------------------
    //createStaffAccount Method-
    //--------------------------

    //Success, creation a valid acc, for "manager"
    @Test
    void createStaffAccountTestManager() throws IllegalAccessException {
        String acc2 = accountService.createStaffAccount(manager, "manager", "Test Manager", "123456743", "test5@example.com", LocalDate.of(1990, 10, 1),
                "27 Rua E", "4567-321", "Portugal", "919123456", "PT50001123456789012", "65478321", null);

        Manager expectedManager = new Manager(2, "Test Manager", "123456743", "test5@example.com", LocalDate.of(1990, 10, 1),
                "27 Rua E", "4567-321", "Portugal", "919123456", "PT50001123456789012", "65478321", null);

        assertNotNull(acc2);
        assertEquals("Conta de Gerente criada com sucesso com o ID 2", acc2);

        Manager actualManager = (Manager) accountService.getUserById(2);
        assertInstanceOf(Manager.class, actualManager);

        assertEquals(expectedManager.getId(), ((Manager) actualManager).getId());
        assertEquals(expectedManager.getFullName(), ((Manager) actualManager).getFullName());
        assertEquals(expectedManager.getNif(), ((Manager) actualManager).getNif());
        assertEquals(expectedManager.getEmail(), ((Manager) actualManager).getEmail());
        assertEquals(expectedManager.getBirthDate(), ((Manager) actualManager).getBirthDate());
        assertEquals(expectedManager.getAddress(), ((Manager) actualManager).getAddress());
        assertEquals(expectedManager.getZipCode(), ((Manager) actualManager).getZipCode());
        assertEquals(expectedManager.getCountry(), ((Manager) actualManager).getCountry());
        assertEquals(expectedManager.getPhone(), ((Manager) actualManager).getPhone());
    }

    //Success, creation a valid acc, for "employee"
    @Test
    void createStaffAccountTestEmployee() throws IllegalAccessException {
        String acc3 = accountService.createStaffAccount(manager, "employee", "Test Employee", "765456765", "test6@example.com", LocalDate.of(1985, 6, 7),
                "28 Rua F", "4321-245", "Portugal", "965345574", "PT500011221122112209879", "31234561", null);

        Employee expectedEmployee = new Employee(2, "Test Employee", "765456765", "test6@example.com", LocalDate.of(1985, 6, 7),
                "28 Rua F", "4321-245", "Portugal", "965345574", "PT500011221122112209879", "31234561", null);

        assertNotNull(acc3);
        assertEquals("Conta de Funcionário criada com sucesso com o ID 2", acc3);

        Employee actualEmployee = (Employee) accountService.getUserById(2);
        assertInstanceOf(Employee.class, actualEmployee);

        assertEquals(expectedEmployee.getId(), ((Employee) actualEmployee).getId());
        assertEquals(expectedEmployee.getFullName(), ((Employee) actualEmployee).getFullName());
        assertEquals(expectedEmployee.getNif(), ((Employee) actualEmployee).getNif());
        assertEquals(expectedEmployee.getEmail(), ((Employee) actualEmployee).getEmail());
        assertEquals(expectedEmployee.getBirthDate(), ((Employee) actualEmployee).getBirthDate());
        assertEquals(expectedEmployee.getAddress(), ((Employee) actualEmployee).getAddress());
        assertEquals(expectedEmployee.getZipCode(), ((Employee) actualEmployee).getZipCode());
        assertEquals(expectedEmployee.getCountry(), ((Employee) actualEmployee).getCountry());
        assertEquals(expectedEmployee.getPhone(), ((Employee) actualEmployee).getPhone());
    }

    //Permission denied, user without permissions trying to create an acc -> employee
    @Test
    void createStaffAccountTestAsEmployee () throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            accountService.createStaffAccount(employee, "employee", "Test", "78675436", "test7@example.com", LocalDate.of(1980, 1,10),
                    "28 Rua F", "5652-898", "Portugal", "909123478", "PT500012345678987643212", "87965436", null);
        });

        assertEquals("Acesso não autorizado: Apenas gestores podem criar contas de staff.", exception.getMessage());
    }

    //Permission denied, user without permissions trying to create an acc -> guest
    @Test
    void createStaffAccountTestAsGuest() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            accountService.createStaffAccount(guest, "employee", "Test", "12344321", "test8@example.com", LocalDate.of(1985, 10, 1),
                    "29 Rua G", "4321-765", "Portugal", "9021234567", "PT500034566543345665437", "45676547", null);
        });

        assertEquals("Acesso não autorizado: Apenas gestores podem criar contas de staff.", exception.getMessage());

    }

    //Invalid data
    @Test
    void createStaffAccountTestInvalidData() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createStaffAccount(manager, "employee", "", "909000011", "test.teste.com", LocalDate.of(2025, 10, 1),
                    "", "123444-21212", "", "12121212121212", "1111111111111111111111111111", "111111111111111", null);
        });

        assertEquals("Um ou mais dados fornecidos são inválidos.", exception.getMessage());
    }

    @Test
    //Duplicate NIF
    void createStaffAccountTestDuplicateNIF() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createStaffAccount(manager, "manager", "Test", "987654321", "test9@example.com", LocalDate.of(1970, 10, 10),
                    "30 Rua H", "9876-543", "Portugal", "987865463", "PT500000001111000011110", "31283212", null);
        });

        assertEquals("Email ou NIF já estão associados a uma conta existente.", exception.getMessage());

    }

    //Duplicate Email
    @Test
    void createStaffAccountTestDuplicateEmail() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createStaffAccount(manager, "manager", "Test", "908765453", "test@example.com", LocalDate.of(1990, 1, 10),
                    "31 Rua I", "8765-123", "Portugal", "962345765", "PT500000000000111122221", "31245276", null);
        });

        assertEquals("Email ou NIF já estão associados a uma conta existente.", exception.getMessage());
    }

    //Invalid profile type - invalid role
    @Test
    void createStaffAccountTestInvalidProfileType() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createStaffAccount(manager, "hello", "Test", "111111111", "test10@example.com", LocalDate.of(1980, 10, 5),
                    "32 Rua J", "7777-444", "Portugal", "911111111", "PT500011114444333322221", "11111111", null);
        });

        assertEquals("Tipo de perfil inválido! Apenas 'manager' ou 'employee' são permitidos.", exception.getMessage());
    }

    //-------------------
    //getAllUsers Method-
    //-------------------

    //Success case, the user request the users List
    @Test
    void getAllUsersTest() throws IllegalAccessException{
        LinkedList<User> users = accountService.getAllUsers(manager);
        assertNotNull(users);
        List<User> userList = accountService.getAllUsers(manager);
        System.out.println(userList);

        assertInstanceOf(Guest.class, userList.getFirst());
        assertInstanceOf(Employee.class, userList.getLast());
        assertEquals(2, users.size());
    }

    //Permissions denied, the user without permissions request the users List -> Employee
    @Test
    void getAllUsersTestAsEmployee() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            accountService.getAllUsers(employee);
        });

        assertEquals("Acesso não autorizado: Apenas gestores podem visualizar a lista de utilizadores.", exception.getMessage());
    }

    //Permissions denied, the user without permissions request the users List -> Guest
    @Test
    void getAllUsersTestAsGuest() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            accountService.getAllUsers(guest);
        });

        assertEquals("Acesso não autorizado: Apenas gestores podem visualizar a lista de utilizadores.", exception.getMessage());
    }

    //-------------------
    //getUserById Method-
    //-------------------

    //Success case
    @Test
    void getUserByIdTest() {
        User result = accountService.getUserById(guest.getId());

        assertNotNull(result);
        assertEquals(guest.getId(), result.getId());
        assertEquals(guest.getFullName(), result.getFullName());
    }

    //Non-existing ID
    @Test
    void getUserByIdTestNonExistingId() {
        int invalidId= 100;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.getUserById(invalidId);
        });

        assertEquals("Utilizador com ID " + invalidId + " não encontrado.", exception.getMessage());
    }

    //---------------
    //addUser Method-
    //---------------

    //Success case, add a new user
    @Test
    void addUserTest() throws IllegalAccessException {
        User user = new User(3, "New Guest", "911111222", "user@example.com", LocalDate.of(2000, 10, 1),
                "Rua User 23", "1234-321", "Portugal", "919191919", null);

        accountService.addUser(user);

        //Verify if the user has been add correctly
        LinkedList<User> users = accountService.getAllUsers(manager);
        assertTrue(users.contains(user));
    }

    //Test for null user
    @Test
    void addUserTestNull() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.addUser(null);
        });

        assertEquals("O utilizador não pode ser nulo.", exception.getMessage());
    }

    //--------------------------
    //validateFieldsUser Method-
    //--------------------------

    //Success case, all data valid
    @Test
    void validateFieldUserTest() {
        boolean result = accountService.validateFieldsUser("Test", "123454311", "testtest@example.com", LocalDate.of(1984, 1, 10),
                "1234 Rua H", "1234-912", "Portugal", "912312312");
        assertTrue(result);
    }

    //Name empty
    @Test
    void validateFieldUserTestEmptyName() {
        boolean result = accountService.validateFieldsUser("", "111222334", "tests@example.com", LocalDate.of(1970, 10, 10),
                "12345 Rua K", "9990-112", "Portugal", "919222333");
        assertFalse(result);
    }

    //Invalid NIF
    @Test
    void validateFieldUserTestInvalidNif() {
        boolean result = accountService.validateFieldsUser("Test", "12341234", "tests@example.com", LocalDate.of(1970, 10, 1),
                "12 Rua L", "9999-111", "Portugal", "955555555");
        assertFalse(result);
    }

    //Invalid Email
    @Test
    void validateFieldUserInvalidTestEmail() {
        boolean result = accountService.validateFieldsUser("Test", "111222333", "test.com", LocalDate.of(1980, 10, 10),
                "14 Rua J", "9999-111", "Portugal", "901012012");
        assertFalse(result);
    }

    //Invalid date
    @Test
    void validateFieldUserTestInvalidDate() {
        boolean result = accountService.validateFieldsUser("Test", "111222312", "tests@example.com", null,
                "14 Rua J", "9999-111", "Portugal", "919191912");

        assertFalse(result);
    }

    //Invalid address
    @Test
    void validateFieldUserTestInvalidAdress(){
        boolean result = accountService.validateFieldsUser("Test", "111222312", "tests@example.com", LocalDate.of(1990, 1, 1),
                "", "9999-111", "Portugal", "919191912");

        assertFalse(result);
    }

    //Invalid zipCode
    @Test
    void validateFieldUserTestInvalidZipCode(){
        boolean result = accountService.validateFieldsUser("Test", "111222312", "tests@example.com", LocalDate.of(1990, 1, 1),
                "123 Rua J", "9999-1112", "Portugal", "919191912");

        assertFalse(result);
    }

    //Invalid Country
    @Test
    void validateFieldUserTestInvalidCountry(){
        boolean result = accountService.validateFieldsUser("Test", "111222312", "tests@example.com", LocalDate.of(1990, 1, 1),
                "123 Rua J", "999-1112", "", "919191912");

        assertFalse(result);
    }

    //Invalid phone number
    @Test
    void validateFieldUserTestInvalidPhoneNumber(){
        boolean result = accountService.validateFieldsUser("Test", "111222312", "tests@example.com", LocalDate.of(1990, 1, 1),
                "123 Rua J", "999-1112", "Portugal", "9191919121");

        assertFalse(result);
    }

    //---------------------
    //isUniqueEmail Method-
    //---------------------

    //Success case, email is unique
    @Test
    void isUniqueEmailTest(){
        boolean result = accountService.isUniqueEmail("successexample@example.com");
        assertTrue(result);
    }

    //Duplicate Email
    @Test
    void isUniqueEmailTestNonUnique(){
        boolean result = accountService.isUniqueEmail("test@example.com"); //Existing in setUp
        assertFalse(result);
    }

    //-------------------
    //isUniqueNif Method-
    //-------------------

    //Success case, the Nif is unique
    @Test
    void isUniqueNifTest(){
        boolean result = accountService.isUniqueNif("987877622");
        assertTrue(result);
    }

    //Duplicate Nif
    @Test
    void isUniqueNifTestDuplicate(){
        boolean result = accountService.isUniqueNif("123456789");
        assertFalse(result);
    }

    //------------------
    //listGuests Method-
    //------------------

    //Success case, the user can list the guests
    @Test
    void listGuestsTest() throws IllegalAccessException {
        LinkedList<User> guests = accountService.listGuests(manager);
        System.out.println(guests);

        assertNotNull(guests);
        assertEquals(1, guests.size());
        assertInstanceOf(Guest.class, guests.getFirst());
    }

    //Permissions denied, user without permissions -> employee
    @Test
    void listGuestsTestAsEmployee() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
           accountService.listGuests(employee);
        });

        assertEquals("Acesso não autorizado: Apenas gestores podem visualizar hóspedes.", exception.getMessage());
    }

    //Permissions denied, user without permissions -> guest
    @Test
    void listGuestsTestAsGuest() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            accountService.listGuests(guest);
        });

        assertEquals("Acesso não autorizado: Apenas gestores podem visualizar hóspedes.", exception.getMessage());
    }

    //---------------------
    //listEmployees Method-
    //---------------------

    //Success case, the user can list the employees
    @Test
    void listEmployeesTest() throws IllegalAccessException {
        LinkedList<User> employees = accountService.listEmployees(manager);

        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertInstanceOf(Employee.class, employees.getFirst());
    }

    //Permissions denied, user without permissions -> employee
    @Test
    void listEmployeesTestAsEmployee() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            accountService.listEmployees(employee);
        });

        assertEquals("Acesso não autorizado: Apenas gestores podem visualizar funcionários.", exception.getMessage());
    }

    //Permissions denied, user without permissions -> guest
    @Test
    void listEmployeesTestAsGuest() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            accountService.listEmployees(guest);
        });

        assertEquals("Acesso não autorizado: Apenas gestores podem visualizar funcionários.", exception.getMessage());
    }

    //-----------------------
    //getPersonalData Method-
    //-----------------------

    //Success case, manager user can consult other user data
    @Test
    void getPersonalDataTest() throws IllegalAccessException {
        String personalData = accountService.getPersonalData(manager, 0);

        System.out.println(personalData);
        assertNotNull(personalData);
        assertTrue(personalData.contains(guest.getFullName()));
        assertTrue(personalData.contains(guest.getNif()));
        assertTrue(personalData.contains(guest.getEmail()));
    }

    //Permissions denied, user without permissions -> guest
    @Test
    void getPersonalDataTestAsGuest() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            accountService.getPersonalData(guest,2);
        });
        assertEquals("Acesso não autorizado: Apenas gestores ou funcionários podem consultar informações.", exception.getMessage());
    }

    //Invalid ID
    @Test
    void getPersonalDataTestInvalidId() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            accountService.getPersonalData(manager, -1);
        });
        assertEquals("Acesso não autorizado: O id indicado não é válido.", exception.getMessage());
    }

    //Non existent Id
    @Test
    void getPersonalDataTestNonExistingId() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.getPersonalData(manager, 999);
        });
        assertEquals("Utilizador com ID 999 não encontrado.", exception.getMessage());
    }

    //--------------------------
    //changePersonalData Method-
    //--------------------------

    //Success case, Changing valid data
    @Test
    void changePersonalDataTest() {
        System.out.println(guest);
        String result = accountService.changePersonalData(guest, "123456673", "new@example.com", "Change Test", LocalDate.of(2002, 1, 1),
                "Rua Z 24", "1998-123", "Espanha", "999888111");

        assertNotNull(result);
        assertEquals("Dados pessoais alterados com sucesso.", result);
        System.out.println(guest);
        assertEquals("123456673", guest.getNif());
        assertEquals("Change Test", guest.getFullName());
        assertEquals("new@example.com", guest.getEmail());
        assertEquals(LocalDate.of(2002, 1, 1), guest.getBirthDate());
        assertEquals("999888111", guest.getPhone());
        assertEquals("Espanha", guest.getCountry());
        assertEquals("Rua Z 24", guest.getAddress());
        assertEquals("1998-123", guest.getZipCode());
    }

    //Trying to change duplicate NIF
    @Test
    void changePersonalDataTestDuplicateNif(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changePersonalData(guest, "987654321", "new@example.com", "Change Test", LocalDate.of(2002, 1, 1),
                    "Rua Z 24", "1998-123", "Espanha", "999888111");
        });

        assertEquals("NIF inválido. O NIF indicado já se encontra associado a outra conta.", exception.getMessage());

    }

    //Trying to change duplicate Email
    @Test
    void changePersonalTestDuplicateEmail(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changePersonalData(guest, "123456673", "testt@example.com", "Change Test", LocalDate.of(2002, 1, 1),
                    "Rua Z 24", "1998-123", "Espanha", "999888111");
        });

        assertEquals("Email inválido. O email indicado já se encontra associado a outra conta.", exception.getMessage());
    }

    //Trying to change with invalidate data
    @Test
    void changePersonalTestInvalidData(){
        //Invalid Nif
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changePersonalData(guest, "invalidNIF", null, null, null, null, null, null, null);
        });
        assertEquals("NIF inválido. Insira um NIF com 9 dígitos, entre 0 e 9.", exception1.getMessage());

        //Invalid Email
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changePersonalData(guest, null, "invalidEmail", null, null, null, null, null, null);
        });
        assertEquals("Email inválido. Insira um email com o seguinte formato: example@domain.com", exception2.getMessage());

        //Invalid Name
        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changePersonalData(guest, null, null, "", null, null, null, null, null);
        });
        assertEquals("Nome completo inválido.", exception3.getMessage());

        //Invalid address
        Exception exception5 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changePersonalData(guest, null, null, null, null, "", null, null, null);
        });
        assertEquals("Morada inválida.", exception5.getMessage());

        //Invalid zipCode
        Exception exception6 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changePersonalData(guest, null, null, null, null, null, "123", null, null);
        });
        assertEquals("Código Postal inválido. Insira um código postal com o seguinte formato: ####-###.", exception6.getMessage());

        //Invalid country
        Exception exception7 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changePersonalData(guest, null, null, null, null, null, null, "", null);
        });
        assertEquals("País inválido.", exception7.getMessage());

        //Invalid Phone Number
        Exception exception8 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changePersonalData(guest, null, null, null, null, null, null, null, "invalidPhone");
        });
        assertEquals("Telefone inválido. Introduza um telefone com 9 digitos, entre 0 a 9", exception8.getMessage());
    }

    //------------------------------
    //changeUserPersonalData Method-
    //------------------------------

    //Success case, Changing other user data
    @Test
    void changeUserPersonalDataTest() throws IllegalAccessException {
        User user1 = accountService.getUserById(0);
        System.out.println(user1);
        String result = accountService.changeUserPersonalData(manager, 0, "111111111", "new.email@example.com",
                "Change User Test", LocalDate.of(1985, 5, 20), "789 Rua C", "8765-432", "Espanha", "923456789");

        assertNotNull(result);
        assertEquals("Dados pessoais alterados com sucesso.", result);
        System.out.println(user1);
        assertEquals("111111111", user1.getNif());
        assertEquals("Change User Test", user1.getFullName());
        assertEquals("new.email@example.com", user1.getEmail());
        assertEquals(LocalDate.of(1985, 5, 20), user1.getBirthDate());
        assertEquals("923456789", user1.getPhone());
        assertEquals("Espanha", user1.getCountry());
        assertEquals("789 Rua C", user1.getAddress());
        assertEquals("8765-432", user1.getZipCode());
    }

    //Duplicate Nif
    @Test
    void changeUserPersonalDataTestDuplicateNif() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changeUserPersonalData(manager, 0, "987654321", null, null, null, null, null, null
            ,null);
        });

        assertEquals("NIF inválido. O NIF indicado já se encontra associado a outra conta.", exception.getMessage());
    }

    //Duplicate Email
    @Test
    void changeUserPersonalDataTestDuplicateEmail() throws IllegalAccessException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changeUserPersonalData(manager, 0, null, "testt@example.com", null, null, null, null, null
                    ,null);
        });

        assertEquals("Email inválido. O email indicado já se encontra associado a outra conta.", exception.getMessage());
    }

    //Trying to change with invalidate data
    @Test
    void changeUserPersonalDataTestInvalidData(){
        //Invalid Nif
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changeUserPersonalData(manager,0 ,  "invalidNIF", null, null, null, null, null, null, null);
        });
        assertEquals("NIF inválido. Insira um NIF com 9 dígitos, entre 0 e 9.", exception1.getMessage());

        //Invalid Email
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changeUserPersonalData(manager, 0, null, "invalidEmail", null, null, null, null, null, null);
        });
        assertEquals("Email inválido. Insira um email com o seguinte formato: example@domain.com", exception2.getMessage());

        //Invalid Name
        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changeUserPersonalData(manager, 0, null, null, "", null, null, null, null, null);
        });
        assertEquals("Nome completo inválido.", exception3.getMessage());

        //Invalid address
        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changeUserPersonalData(manager, 0, null, null, null, null, "", null, null, null);
        });
        assertEquals("Morada inválida.", exception4.getMessage());

        //Invalid zipCode
        Exception exception5 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changeUserPersonalData(manager, 0, null, null, null, null, null, "123", null, null);
        });
        assertEquals("Código Postal inválido. Insira um código postal com o seguinte formato: ####-###.", exception5.getMessage());

        //Invalid country
        Exception exception6 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changeUserPersonalData(manager, 0,null,  null, null, null, null, null, "", null);
        });
        assertEquals("País inválido.", exception6.getMessage());

        //Invalid Phone Number
        Exception exception7 = assertThrows(IllegalArgumentException.class, () -> {
            accountService.changeUserPersonalData(manager, 0, null, null, null, null, null, null, null, "123323232323");
        });
        assertEquals("Telefone inválido. Introduza um telefone com 9 digitos, entre 0 a 9", exception7.getMessage());
    }
}