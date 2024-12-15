package com.example.spck.HangHoa;

import java.io.Serializable;

public class HangHoa implements Serializable {

        int key;
        String hinhAnh;
        String tenSP;
        int giaSP;
        int daBan;
        int tonKho;
        String noiDungSP;
        String loaiSP;


        public HangHoa() {}


        public HangHoa(int key, String hinhAnh, String tenSP, int giaSP, int daBan, int tonKho, String noiDungSP, String loaiSP) {
            this.key = key;
            this.hinhAnh = hinhAnh;
            this.tenSP = tenSP;
            this.giaSP = giaSP;
            this.daBan = daBan;
            this.tonKho = tonKho;
            this.noiDungSP = noiDungSP;
            this.loaiSP = loaiSP;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getHinhAnh() {
            return hinhAnh;
        }

        public void setHinhAnh(String hinhAnh) {
            this.hinhAnh = hinhAnh;
        }

        public String getTenSP() {
            return tenSP;
        }

        public void setTenSP(String tenSP) {
            this.tenSP = tenSP;
        }

        public int getGiaSP() {
            return giaSP;
        }

        public void setGiaSP(int giaSP) {
            this.giaSP = giaSP;
        }

        public int getDaBan() {
            return daBan;
        }

        public void setDaBan(int daBan) {
            this.daBan = daBan;
        }

        public int getTonKho() {
            return tonKho;
        }

        public void setTonKho(int tonKho) {
            this.tonKho = tonKho;
        }

        public String getNoiDungSP() {
            return noiDungSP;
        }

        public void setNoiDungSP(String noiDungSP) {
            this.noiDungSP = noiDungSP;
        }

        public String getLoaiSP() {
            return loaiSP;
        }

        public void setLoaiSP(String loaiSP) {
            this.loaiSP = loaiSP;
        }
    }


