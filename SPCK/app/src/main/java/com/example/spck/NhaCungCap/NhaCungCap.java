package com.example.spck.NhaCungCap;

import java.io.Serializable;
import java.util.Date;

public class NhaCungCap implements Serializable {
    int key;
    String maNCC;
    String tenNhaCC;
    String soDT;
    String diaChi;
    String email;
    Date ngayTao;
    String ghiChu;

    public NhaCungCap(){}

    public NhaCungCap(int key, String maNCC, String tenNhaCC, String soDT, String diaChi, String email, Date ngayTao, String ghiChu) {
        this.key = key;
        this.maNCC = maNCC;
        this.tenNhaCC = tenNhaCC;
        this.soDT = soDT;
        this.diaChi = diaChi;
        this.email = email;
        this.ngayTao = ngayTao;
        this.ghiChu = ghiChu;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public String getTenNhaCC() {
        return tenNhaCC;
    }

    public void setTenNhaCC(String tenNhaCC) {
        this.tenNhaCC = tenNhaCC;
    }

    public String getSoDT() {
        return soDT;
    }

    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public String toString() {
        return "NhaCungCap{" +
                "key=" + key +
                ", maNCC='" + maNCC + '\'' +
                ", tenNhaCC='" + tenNhaCC + '\'' +
                ", soDT='" + soDT + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", email=" + email +
                ", ngayTao=" + ngayTao +
                ", ghiChu='" + ghiChu + '\'' +
                '}';
    }
}
