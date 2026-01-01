package com.onlineshop.dto;

import java.math.BigDecimal;

public class BarangTerlarisDto {

    private Long idProduk;
    private String namaProduk;
    private BigDecimal harga;
    private Long totalTerjual;
    private String imageUrl;

    public BarangTerlarisDto(Long idProduk, String namaProduk, BigDecimal harga, Long totalTerjual, String imageUrl) {
        this.idProduk = idProduk;
        this.namaProduk = namaProduk;
        this.harga = harga;
        this.totalTerjual = totalTerjual;
        this.imageUrl = imageUrl;
    }

    public Long getIdProduk() { return idProduk; }
    public void setIdProduk(Long idProduk) { this.idProduk = idProduk; }

    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public BigDecimal getHarga() { return harga; }
    public void setHarga(BigDecimal harga) { this.harga = harga; }

    public Long getTotalTerjual() { return totalTerjual; }
    public void setTotalTerjual(Long totalTerjual) { this.totalTerjual = totalTerjual; }

    // ✅ getter setter baru
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
