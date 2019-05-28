package com.example.appfilmnew;

public class ThongTinUser {
    private  String id;
    private  String ten;
    private  String email;
    private  boolean gioiTinh;
    private int coin;
    private String filmDaMua;
    private  int   row;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getFilmDaMua() {
        return filmDaMua;
    }

    public void setFilmDaMua(String filmDaMua) {
        this.filmDaMua = filmDaMua;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
