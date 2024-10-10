package com.example.se330_pharmacy.Models;

import java.util.Date;

public class Report {
    private int idBillStatus;
    private Date dateStatus;
    private String employnameStatus;
    private int valueStatus;

    private int idEmployTop;
    private String employnameTop;
    private int numberTop;
    private int totalTop;

    public Report() {
    }

    public Report(int idBillStatus, Date dateStatus, String employnameStatus, int valueStatus, int idEmployTop, String employnameTop, int numberTop, int totalTop) {
        this.idBillStatus = idBillStatus;
        this.dateStatus = dateStatus;
        this.employnameStatus = employnameStatus;
        this.valueStatus = valueStatus;
        this.idEmployTop = idEmployTop;
        this.employnameTop = employnameTop;
        this.numberTop = numberTop;
        this.totalTop = totalTop;
    }

    public int getIdBillStatus() {
        return idBillStatus;
    }

    public void setIdBillStatus(int idBillStatus) {
        this.idBillStatus = idBillStatus;
    }

    public Date getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(Date dateStatus) {
        this.dateStatus = dateStatus;
    }

    public String getEmploynameStatus() {
        return employnameStatus;
    }

    public void setEmploynameStatus(String employnameStatus) {
        this.employnameStatus = employnameStatus;
    }

    public int getValueStatus() {
        return valueStatus;
    }

    public void setValueStatus(int valueStatus) {
        this.valueStatus = valueStatus;
    }

    public int getIdEmployTop() {
        return idEmployTop;
    }

    public void setIdEmployTop(int idEmployTop) {
        this.idEmployTop = idEmployTop;
    }

    public String getEmploynameTop() {
        return employnameTop;
    }

    public void setEmploynameTop(String employnameTop) {
        this.employnameTop = employnameTop;
    }

    public int getNumberTop() {
        return numberTop;
    }

    public void setNumberTop(int numberTop) {
        this.numberTop = numberTop;
    }

    public int getTotalTop() {
        return totalTop;
    }

    public void setTotalTop(int totalTop) {
        this.totalTop = totalTop;
    }
}
