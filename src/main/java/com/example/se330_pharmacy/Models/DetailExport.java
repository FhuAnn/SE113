package com.example.se330_pharmacy.Models;

public class DetailExport {
    private int exportProductId;
    private String exportProductName;
    private int exportProductPrice;
    private int exportProductQuan;
    private String exportProductUnit;
    private long exportTotal;

    public DetailExport() {
    }

    public DetailExport(int exportProductId, String exportProductName, int exportProductPrice, int exportProductQuan, String exportProductUnit, long exportTotal) {
        this.exportProductId = exportProductId;
        this.exportProductName = exportProductName;
        this.exportProductPrice = exportProductPrice;
        this.exportProductQuan = exportProductQuan;
        this.exportProductUnit = exportProductUnit;
        this.exportTotal = exportTotal;
    }

    public int getExportProductId() {
        return exportProductId;
    }

    public void setExportProductId(int exportProductId) {
        this.exportProductId = exportProductId;
    }

    public String getExportProductName() {
        return exportProductName;
    }

    public void setExportProductName(String exportProductName) {
        this.exportProductName = exportProductName;
    }

    public int getExportProductPrice() {
        return exportProductPrice;
    }

    public void setExportProductPrice(int exportProductPrice) {
        this.exportProductPrice = exportProductPrice;
    }

    public int getExportProductQuan() {
        return exportProductQuan;
    }

    public void setExportProductQuan(int exportProductQuan) {
        this.exportProductQuan = exportProductQuan;
    }

    public String getExportProductUnit() {
        return exportProductUnit;
    }

    public void setExportProductUnit(String exportProductUnit) {
        this.exportProductUnit = exportProductUnit;
    }

    public long getExportTotal() {
        return exportTotal;
    }

    public void setExportTotal(long exportTotal) {
        this.exportTotal = exportTotal;
    }
}
