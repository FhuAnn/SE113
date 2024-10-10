package com.example.se330_pharmacy.Models;


import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee {
    public int employeeId;
    public String employeeName;
    public String employeeCitizenId;
    public String employeeAddress;
    public String employeePhoneNumber;
    public String employeeEmail;
    public String employeePosition;
    public String employeeUsername;
    public String employeePassword;
    public String defaultpassword;

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", employeeCitizenId='" + employeeCitizenId + '\'' +
                ", employeeAddress='" + employeeAddress + '\'' +
                ", employeePhoneNumber=" + employeePhoneNumber +
                ", employeeEmail='" + employeeEmail + '\'' +
                ", employeePosition=" + employeePosition + '\'' +
                ", employeeUsername=" + employeeUsername +
                '}';
    }


    public Employee(int employeeId, String employeeName, String employeeCitizenId, String employeeAddress, String employeePhoneNumber, String employeeEmail, String employeePosition, String employeeUsername) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeCitizenId = employeeCitizenId;
        this.employeeAddress = employeeAddress;
        this.employeePhoneNumber = employeePhoneNumber;
        this.employeeEmail = employeeEmail;
        this.employeePosition = employeePosition;
        this.employeeUsername = employeeUsername;
    }

    public Employee(String employeeName, String employeeCitizenId, String employeeAddress, String employeePhoneNumber, String employeeEmail, String employeePosition, String employeeUsername) {
        this.employeeName = employeeName;
        this.employeeCitizenId = employeeCitizenId;
        this.employeeAddress = employeeAddress;
        this.employeePhoneNumber = employeePhoneNumber;
        this.employeeEmail = employeeEmail;
        this.employeePosition = employeePosition;
        this.employeeUsername = employeeUsername;
    }

    public Employee() {
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCitizenId() {
        return employeeCitizenId;
    }

    public void setEmployeeCitizenId(String employeeCitizenId) {
        this.employeeCitizenId = employeeCitizenId;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public String getEmployeePhoneNumber() {
        return employeePhoneNumber;
    }

    public void setEmployeePhoneNumber(String employeePhoneNumber) {
        this.employeePhoneNumber = employeePhoneNumber;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeePosition() {
        return employeePosition;
    }

    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public void setEmployeeUsername(String employeeUsername) {
        this.employeeUsername = employeeUsername;
    }

    public String getEmployeePassword() {
        return employeePassword;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }

    public String getDefaultpassword() {
        return defaultpassword;
    }

    public void setDefaultpassword(String defaultpassword) {
        this.defaultpassword = defaultpassword;
    }
}

