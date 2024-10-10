package com.example.se330_pharmacy.Models;

public class ExportItem {
    private int exportItemId;
    private String exportItemName;
    private long exportItemPrice;
    private int exportItemQuantity;
    private long exportItemTotal;

    public ExportItem() {
    }

    public ExportItem(int exportItemId, String exportItemName, long exportItemPrice, int exportItemQuantity, long exportItemTotal) {
        this.exportItemId = exportItemId;
        this.exportItemName = exportItemName;
        this.exportItemPrice = exportItemPrice;
        this.exportItemQuantity = exportItemQuantity;
        this.exportItemTotal = exportItemTotal;
    }
    public ExportItem(ExportItem other) {
        this.exportItemId = other.exportItemId;
        this.exportItemName = other.exportItemName;
        this.exportItemPrice = other.exportItemPrice;
        this.exportItemQuantity = other.exportItemQuantity;
        this.exportItemTotal = other.exportItemTotal;
    }

    public int getExportItemId() {
        return exportItemId;
    }

    public void setExportItemId(int exportItemId) {
        this.exportItemId = exportItemId;
    }

    public String getExportItemName() {
        return exportItemName;
    }

    public void setExportItemName(String exportItemName) {
        this.exportItemName = exportItemName;
    }

    public long getExportItemPrice() {
        return exportItemPrice;
    }

    public void setExportItemPrice(long exportItemPrice) {
        this.exportItemPrice = exportItemPrice;
    }

    public int getExportItemQuantity() {
        return exportItemQuantity;
    }

    public void setExportItemQuantity(int exportItemQuantity) {
        this.exportItemQuantity = exportItemQuantity;
    }

    public long getExportItemTotal() {
        return exportItemTotal;
    }

    public void setExportItemTotal(long exportItemTotal) {
        this.exportItemTotal = exportItemTotal;
    }
}
