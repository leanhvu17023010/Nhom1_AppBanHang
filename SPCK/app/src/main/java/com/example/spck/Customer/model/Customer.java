package com.example.spck.Customer.model;

import java.io.Serializable;

public class Customer implements Serializable {

    private int id;
    private String name;
    private String phone;
    private String address;
    private String email;
    private String birthDate;
    private String notes;
    private int totalPaid;
    private int remainingBalance;
    private String productType; // Thêm trường productType
    private byte[] photo;  // Thêm trường lưu trữ ảnh

    public Customer() {}

    public Customer(int id, String name, String phone, String address, String email, String birthDate, String notes, int totalPaid, int remainingBalance, String productType, byte[] photo) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.birthDate = birthDate;
        this.notes = notes;
        this.totalPaid = totalPaid;
        this.remainingBalance = remainingBalance;
        this.productType = productType;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(int totalPaid) {
        this.totalPaid = totalPaid;
    }

    public int getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(int remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    // Getter và Setter cho trường photo
    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
