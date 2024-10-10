package com.example.se330_pharmacy.Models;

public class ExportForm {
    private int exportFormId;
    private String employeeName;
    private String exportReason;
    private String exportDate;
    private long totalMoney;

    public ExportForm() {
    }

    public ExportForm(int exportFormId, String employeeName, String exportReason, String exportDate, long totalMoney) {
        this.exportFormId = exportFormId;
        this.employeeName = employeeName;
        this.exportReason = exportReason;
        this.exportDate = exportDate;
        this.totalMoney = totalMoney;
    }

    public int getExportFormId() {
        return exportFormId;
    }

    public void setExportFormId(int exportFormId) {
        this.exportFormId = exportFormId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getExportReason() {
        return exportReason;
    }

    public void setExportReason(String exportReason) {
        this.exportReason = exportReason;
    }

    public String getExportDate() {
        return exportDate;
    }

    public void setExportDate(String exportDate) {
        this.exportDate = exportDate;
    }

    public long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(long totalMoney) {
        this.totalMoney = totalMoney;
    }
}
