package com.thehotel.services;

import com.thehotel.HotelServer;
import com.thehotel.model.Guest;
import com.thehotel.model.User;
import com.thehotel.model.Manager;
import com.thehotel.model.Employee;

import java.time.LocalDate;
import java.util.LinkedList;

public class AccountService {
    private int userIdCounter = 0;
    private final LinkedList<User> userList = new LinkedList<>();

    public AccountService() {
    }
    /*
     * --------------------------------------------------------------------------------------------
     * USER MANAGEMENT
     * --------------------------------------------------------------------------------------------
     */

    public synchronized int generateUserId() {
        return userIdCounter++;
    }

    //Creates a Guest Account
    public String createGuestAccount(String fullName, String nif, String email, LocalDate birthDate, String address, String zipCode, String country, String phone, HotelServer hotelServer) throws Exception{

        if (!validateFieldsUser(fullName, nif, email, birthDate, address, zipCode, country, phone)) {
            throw new IllegalArgumentException("Um ou mais dados fornecidos são inválidos.");
        }

        if (!isUniqueEmail(email) || !isUniqueNif(nif)) {
            throw new IllegalArgumentException("Email ou NIF já estão associados a uma conta existente.");
        }

        int id = generateUserId();
        Guest newGuest = new Guest(id, fullName, nif, email, birthDate, address, zipCode, country, phone, hotelServer);
        userList.add(newGuest);

        return "Conta de Hóspede criada com sucesso com o ID " + newGuest.getId();
    }

    //Creates a Staff Account (Manager / Employee)
    public String createStaffAccount(User user, String role, String fullName, String nif, String email, LocalDate birthDate,
                                     String address, String zipCode, String country, String phone, String iban,
                                     String citizenCard, HotelServer hotelServer) throws IllegalAccessException {
        if (!user.getRole().equals("manager")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores podem criar contas de staff.");
        }

        if (!validateFieldsUser(fullName, nif, email, birthDate, address, zipCode, country, phone)) {
            throw new IllegalArgumentException("Um ou mais dados fornecidos são inválidos.");
        }

        if (!isUniqueEmail(email) || !isUniqueNif(nif)) {
            throw new IllegalArgumentException("Email ou NIF já estão associados a uma conta existente.");
        }

        if (role.equalsIgnoreCase("manager")) {
            Manager newManager = new Manager(
                    generateUserId(),
                    fullName,
                    nif,
                    email,
                    birthDate,
                    address,
                    zipCode,
                    country,
                    phone,
                    iban,
                    citizenCard,
                    hotelServer
            );
            userList.add(newManager);
            return "Conta de Gerente criada com sucesso com o ID " + newManager.getId();
        } else if (role.equalsIgnoreCase("employee")) {
            Employee newEmployee = new Employee(
                    generateUserId(),
                    fullName,
                    nif,
                    email,
                    birthDate,
                    address,
                    zipCode,
                    country,
                    phone,
                    iban,
                    citizenCard,
                    hotelServer
            );
            userList.add(newEmployee);
            return "Conta de Funcionário criada com sucesso com o ID " + newEmployee.getId();
        } else {
            throw new IllegalArgumentException("Tipo de perfil inválido! Apenas 'manager' ou 'employee' são permitidos.");
        }
    }

    public LinkedList<User> getAllUsers(User requester) throws IllegalAccessException {
        if (!requester.getRole().equals("manager")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores podem visualizar a lista de utilizadores.");
        }
        return userList;
    }

    public User getUserById(int id) {
        for (User user : userList) {
            if (user.getId() == id) {
                return user;
            }
        }
        throw new IllegalArgumentException("Utilizador com ID " + id + " não encontrado.");
    }
    // adds user
    public void addUser(User user) {
        if (user != null) {
            userList.add(user);
        } else {
            throw new IllegalArgumentException("O utilizador não pode ser nulo.");
        }
    }
    /*
     * --------------------------------------------------------------------------------------------
     * VALIDATION METHODS
     * --------------------------------------------------------------------------------------------
     */

    // Validate user fields
    public boolean validateFieldsUser(String fullName, String nif, String email, LocalDate birthDate, String address, String zipCode, String country, String phone){
        return !(fullName == null || fullName.isEmpty() ||
                nif == null || !nif.matches("\\d{9}") ||
                email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$") ||
                birthDate == null ||
                address == null || address.isEmpty() ||
                zipCode == null || !zipCode.matches("\\d{4}-\\d{3}") ||
                country == null || country.isEmpty() ||
                phone == null || !phone.matches("\\d{9}"));
    }

    // Checks if email is unique
    public boolean isUniqueEmail(String newEmail){
        for(User user : userList){
            if(user.getEmail().equals(newEmail)) return false;
        }
        return true;
    }

    // Checks if NIF is unique
    public boolean isUniqueNif(String newNif){
        for(User user : userList){
            if(user.getNif().equals(newNif)) return false;
        }
        return true;
    }

    /*
     * --------------------------------------------------------------------------------------------
     * UTILITY METHODS
     * --------------------------------------------------------------------------------------------
     */
    // Lists all guests
    public LinkedList<User> listGuests(User userReq) throws IllegalAccessException {
        if (!userReq.getRole().equals("manager")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores podem visualizar hóspedes.");
        }
        LinkedList<User> guestList = new LinkedList<>();
        for (User user : userList) {
            if (user.getRole().equals("guest")) {
                guestList.add(user);
            }
        }
        return guestList;
    }

    // List all employees
    public LinkedList<User> listEmployees(User requester) throws IllegalAccessException {
        if (!requester.getRole().equals("manager")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores podem visualizar funcionários.");
        }
        LinkedList<User> employeeList = new LinkedList<>();
        for (User user : userList) {
            if (user.getRole().equals("employee")) {
                employeeList.add(user);
            }
        }
        return employeeList;
    }

    /*
     * --------------------------------------------------------------------------------------------
     * ACCOUNTS INFO METHODS
     * --------------------------------------------------------------------------------------------
     */

    //Get personal information of the user
    public String getPersonalData(User user,int userId) throws IllegalAccessException {

        if (!user.getRole().equalsIgnoreCase("manager") && !user.getRole().equalsIgnoreCase("employee")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores ou funcionários podem consultar informações.");
        }

        if(userId < 0){
            throw new IllegalAccessException("Acesso não autorizado: O id indicado não é válido.");
        }

        User getUser = getUserById(userId);

        return getUser.personalInfo();
    }

    /*
     * --------------------------------------------------------------------------------------------
     * ACCOUNTS CHANGES METHODS
     * --------------------------------------------------------------------------------------------
     */

    //Change personal data
    public String changePersonalData(User user, String nif, String email, String fullName,
                                     LocalDate birthDate, String address, String zipCode, String country, String phone) throws IllegalArgumentException {
        // Alterar o NIF
        if (nif != null){
            if(!nif.matches("\\d{9}")) throw new IllegalArgumentException("NIF inválido. Insira um NIF com 9 dígitos, entre 0 e 9.");
            if (!isUniqueNif(nif)) throw new IllegalArgumentException("NIF inválido. O NIF indicado já se encontra associado a outra conta.");
            user.setNif(nif);

        }
        // Alterar o Email
        if (email != null) {
            if(!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) throw new IllegalArgumentException("Email inválido. Insira um email com o seguinte formato: example@domain.com");
            if(!isUniqueEmail(email)) throw new IllegalArgumentException("Email inválido. O email indicado já se encontra associado a outra conta.");
            user.setEmail(email);
        }

        // Alterar o Nome Completo
        if (fullName != null){
            if(fullName.isEmpty()) throw new IllegalArgumentException("Nome completo inválido.");
            user.setFullName(fullName);
        }

        // Alterar a Data de Nascimento
        if (birthDate != null) {
            user.setBirthDate(birthDate);
        }

        // Alterar a Morada
        if (address != null) {
            if(address.isEmpty()) throw new IllegalArgumentException("Morada inválida.");
            user.setAddress(address);
        }

        // Alterar o Código Postal
        if (zipCode != null){
            if(!zipCode.matches("\\d{4}-\\d{3}")) throw new IllegalArgumentException("Código Postal inválido. Insira um código postal com o seguinte formato: ####-###.");
            user.setZipCode(zipCode);
        }

        // Alterar o País
        if (country != null) {
            if(country.isEmpty()) throw new IllegalArgumentException("País inválido.");
            user.setCountry(country);
        }

        // Alterar o Telefone
        if (phone != null){
            if(!phone.matches("\\d{9}")) throw new IllegalArgumentException("Telefone inválido. Introduza um telefone com 9 digitos, entre 0 a 9");
            user.setPhone(phone);
        }
        return "Dados pessoais alterados com sucesso.";
    }

    //Change other user personal data
    public String changeUserPersonalData(User user, int userId, String nif, String email, String fullName,
                                     LocalDate birthDate, String address, String zipCode, String country, String phone) throws IllegalArgumentException, IllegalAccessException {

        if (!user.getRole().equalsIgnoreCase("manager")) {
            throw new IllegalAccessException("Acesso não autorizado: Apenas gestores podem alterar informações.");
        }

        if(userId < 0){
            throw new IllegalAccessException("ID inválido.");
        }

        User userToUpdate = getUserById(userId);

        // Alterar o NIF
        if (nif != null){
            if(!nif.matches("\\d{9}")) throw new IllegalArgumentException("NIF inválido. Insira um NIF com 9 dígitos, entre 0 e 9.");
            if (!isUniqueNif(nif)) throw new IllegalArgumentException("NIF inválido. O NIF indicado já se encontra associado a outra conta.");
            userToUpdate.setNif(nif);

        }
        // Alterar o Email
        if (email != null) {
            if(!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) throw new IllegalArgumentException("Email inválido. Insira um email com o seguinte formato: example@domain.com");
            if(!isUniqueEmail(email)) throw new IllegalArgumentException("Email inválido. O email indicado já se encontra associado a outra conta.");
            userToUpdate.setEmail(email);
        }

        // Alterar o Nome Completo
        if (fullName != null){
            if(fullName.isEmpty()) throw new IllegalArgumentException("Nome completo inválido.");
            userToUpdate.setFullName(fullName);
        }

        // Alterar a Data de Nascimento
        if (birthDate != null) {
            userToUpdate.setBirthDate(birthDate);
        }

        // Alterar a Morada
        if (address != null) {
            if(address.isEmpty()) throw new IllegalArgumentException("Morada inválida.");
            userToUpdate.setAddress(address);
        }

        // Alterar o Código Postal
        if (zipCode != null){
            if(!zipCode.matches("\\d{4}-\\d{3}")) throw new IllegalArgumentException("Código Postal inválido. Insira um código postal com o seguinte formato: ####-###.");
            userToUpdate.setZipCode(zipCode);
        }

        // Alterar o País
        if (country != null) {
            if(country.isEmpty()) throw new IllegalArgumentException("País inválido.");
            userToUpdate.setCountry(country);
        }

        // Alterar o Telefone
        if (phone != null){
            if(!phone.matches("\\d{9}")) throw new IllegalArgumentException("Telefone inválido. Introduza um telefone com 9 digitos, entre 0 a 9");
            userToUpdate.setPhone(phone);
        }
        return "Dados pessoais alterados com sucesso.";
    }
}

