package com.onlineshop.dto;

import java.math.BigDecimal;

public class BarangTerlarisDto {

    private Long idProduk;
    private String namaProduk;
    private BigDecimal harga;
    private Long totalTerjual;

    public BarangTerlarisDto(Long idProduk, String namaProduk, BigDecimal harga, Long totalTerjual) {
        this.idProduk = idProduk;
        this.namaProduk = namaProduk;
        this.harga = harga;
        this.totalTerjual = totalTerjual;
    }

    public Long getIdProduk() {
        return idProduk;
    }

    public void setIdProduk(Long idProduk) {
        this.idProduk = idProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }

    public Long getTotalTerjual() {
        return totalTerjual;
    }

    public void setTotalTerjual(Long totalTerjual) {
        this.totalTerjual = totalTerjual;
    }
}
