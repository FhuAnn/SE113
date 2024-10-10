package com.example.se330_pharmacy.Models;

public class Unit {
    private int id;
    private int value;
    private String bigUnit;
    private String smallUnit;
    @Override
    public String toString() {
        if(bigUnit.equals(smallUnit))
            return  smallUnit;
        return value +" "+smallUnit + "/" + bigUnit;
    }

    public Unit() {
    }

    public Unit(String bigUnit, String smallUnit) {
        this.bigUnit = bigUnit;
        this.smallUnit = smallUnit;
    }
    public Unit(int _id, int _value, String bigUnit, String smallUnit) {
        this.id =_id;
        this.value =_value;
        this.bigUnit = bigUnit;
        this.smallUnit = smallUnit;
    }

    public String getBigUnit() {
        return bigUnit;
    }

    public void setBigUnit(String bigUnit) {
        this.bigUnit = bigUnit;
    }

    public String getSmallUnit() {
        return smallUnit;
    }

    public void setSmallUnit(String smallUnit) {
        this.smallUnit = smallUnit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
