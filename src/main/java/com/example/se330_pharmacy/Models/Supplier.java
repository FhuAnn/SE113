package com.example.se330_pharmacy.Models;

public class Supplier {
    int partner_id; // doi tac
    String partnername;
    String address;
    String phonenumber;
    String email;

    public Supplier() {
    }

    public Supplier(int partner_id, String partnername, String address, String phonenumber, String email) {
        this.partner_id = partner_id;
        this.partnername = partnername;
        this.address = address;
        this.phonenumber = phonenumber;
        this.email = email;
    }

    public Supplier( String partnername, String address, String phonenumber, String email) {
        this.partnername = partnername;
        this.address = address;
        this.phonenumber = phonenumber;
        this.email = email;
    }

    public int getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(int partner_id) {
        this.partner_id = partner_id;
    }

    public String getPartnername() {
        return partnername;
    }

    public void setPartnername(String partnername) {
        this.partnername = partnername;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
