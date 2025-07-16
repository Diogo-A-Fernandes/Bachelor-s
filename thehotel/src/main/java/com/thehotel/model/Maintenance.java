package com.thehotel.model;

import java.time.LocalDate;

public class Maintenance {
    private int id;
    private int roomId;
    private String description;
    private LocalDate dateRegister;
    private int idUserRegister;
    private int idUserConclusion;
    private int idUserCancellation;
    private LocalDate dateConclusion;
    private LocalDate dateCancellation;
    private String status;

    public Maintenance(int id, int roomId, String description, int idUserRegister) {
        this.id = id;
        this.roomId = roomId;
        this.description = description;
        this.dateRegister = LocalDate.now();
        this.idUserRegister = idUserRegister;
        this.idUserConclusion = -1;
        this.idUserCancellation = -1;
        this.status = "pendent";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(LocalDate dateRegister) {
        this.dateRegister = dateRegister;
    }

    public int getIdUserRegister() {
        return idUserRegister;
    }

    public void setIdUserRegister(int idUserRegister) {
        this.idUserRegister = idUserRegister;
    }

    public int getIdUserConclusion() {
        return idUserConclusion;
    }

    public void setIdUserConclusion(int idUserConclusion) {
        this.idUserConclusion = idUserConclusion;
    }

    public int getIdUserCancellation() {
        return idUserCancellation;
    }

    public void setIdUserCancellation(int idUserCancellation) {
        this.idUserCancellation = idUserCancellation;
    }

    public LocalDate getDateConclusion() {
        return dateConclusion;
    }

    public void setDateConclusion(LocalDate dateConclusion) {
        this.dateConclusion = dateConclusion;
    }

    public LocalDate getDateCancellation() {
        return dateCancellation;
    }

    public void setDateCancellation(LocalDate dateCancellation) {
        this.dateCancellation = dateCancellation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String idUserConclusionStr = (idUserConclusion == -1) ? "undefined" : String.valueOf(idUserConclusion);
        String idUserCancellationStr = (idUserCancellation == -1) ? "undefined" : String.valueOf(idUserCancellation);

        return String.format(
                "%nManutenção - ID: %d, " +
                        "ID quarto: %d, " +
                        "Descrição: %s, " +
                        "Data Registo: %s, " +
                        "ID Utilizador Registo: %d, " +
                        "Data Conclusão: %s, " +
                        "ID Utilizador Conclusão: %s, " +
                        "Data Cancelamento: %s, " +
                        "ID Utilizador Cancelamento: %s, " +
                        "Estado: %s",
                id,
                roomId,
                description,
                dateRegister != null ? dateRegister.toString() : "N/A",
                idUserRegister,
                dateConclusion != null ? dateConclusion.toString() : "N/A",
                idUserConclusionStr,
                dateCancellation != null ? dateCancellation.toString() : "N/A",
                idUserCancellationStr,
                status
        );
    }


}
