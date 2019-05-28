package com.example.appfilmnew;

import java.util.Date;

public class Film {
    private  String ma;
    private  String ten;
    private  String idTheLoai;
    private String idQuocGia;
    private  String daoDien;
    private  String dienVien;
    private String noiDung;
    private Date ngayUp;
    private  int soTap;
    private  String urlHinh;
    private  String NameHinh;

    private String Stt;

    public String getStt() {
        return Stt;
    }

    public void setStt(String stt) {
        Stt = stt;
    }

    public String getNameHinh() {
        return NameHinh;
    }

    public void setNameHinh(String nameHinh) {
        NameHinh = nameHinh;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getIdTheLoai() {
        return idTheLoai;
    }

    public void setIdTheLoai(String idTheLoai) {
        this.idTheLoai = idTheLoai;
    }

    public String getIdQuocGia() {
        return idQuocGia;
    }

    public void setIdQuocGia(String idQuocGia) {
        this.idQuocGia = idQuocGia;
    }

    public String getDaoDien() {
        return daoDien;
    }

    public void setDaoDien(String daoDien) {
        this.daoDien = daoDien;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Date getNgayUp() {
        return ngayUp;
    }

    public void setNgayUp(Date ngayUp) {
        this.ngayUp = ngayUp;
    }

    public int getSoTap() {
        return soTap;
    }

    public void setSoTap(int soTap) {
        this.soTap = soTap;
    }

    public String getUrlHinh() {
        return urlHinh;
    }

    public void setUrlHinh(String urlHinh) {
        this.urlHinh = urlHinh;
    }
    public String getDienVien() {
        return dienVien;
    }

    public void setDienVien(String dienVien) {
        this.dienVien = dienVien;
    }
}
