package com.example.se330_pharmacy.Models;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Payslip {
    int payslip_id;
    int employee_id;
    String content;
    LocalDateTime createDate;
    int totalPay;
    String note;
    String status;

    int receipt_id;
    String tenNhanVien;
    String ViTriLamViec;
    public Payslip() {
    }

    public Payslip(int payslip_id, int receipt_id, int employee_id, String tenNhanVien, String content, LocalDateTime createDate, int totalPay, String note, String status) {
        this.payslip_id =payslip_id;
        this.receipt_id = receipt_id;
        this.employee_id = employee_id;
        this.tenNhanVien = tenNhanVien;
        this.content = content;
        this.createDate = createDate;
        this.totalPay = totalPay;
        this.note = note;
        this.status = status;
    }
    public int getPayslip_id() {
        return payslip_id;
    }

    public void setPayslip_id(int payslip_id) {
        this.payslip_id = payslip_id;
    }
    public int getReceipt_id() {
        return receipt_id;
    }

    public void setReceipt_id(int receipt_id) {
        this.receipt_id = receipt_id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public int getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(int totalPay) {
        this.totalPay = totalPay;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getViTriLamViec() {
        return ViTriLamViec;
    }

    public void setViTriLamViec(String viTriLamViec) {
        ViTriLamViec = viTriLamViec;
    }
}
