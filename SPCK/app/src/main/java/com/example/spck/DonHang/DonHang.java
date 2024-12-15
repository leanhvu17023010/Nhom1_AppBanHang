package com.example.spck.DonHang;

import java.io.Serializable;
import java.util.Date;

public class DonHang implements Serializable {

    int maDonHang;
    int id;
    String tenKH;
    Date ngayVaGio;
    int tongTien;
    String trangThai;

    public DonHang(){}

    public DonHang(int maDonHang, int id, String tenKH, Date ngayVaGio, int tongTien, String trangThai) {
        this.maDonHang = maDonHang;
        this.id = id;
        this.tenKH = tenKH;
        this.ngayVaGio = ngayVaGio;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public Date getNgayVaGio() {
        return ngayVaGio;
    }

    public void setNgayVaGio(Date ngayVaGio) {
        this.ngayVaGio = ngayVaGio;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "DonHang{" +
                "maDonHang=" + maDonHang +
                ", maKhachHang=" + id +
                ", tenKH='" + tenKH + '\'' +
                ", ngayVaGio=" + ngayVaGio +
                ", tongTien=" + tongTien +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}

