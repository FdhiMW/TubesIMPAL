package com.onlineshop.dto;

import java.math.BigDecimal;

public class ProdukDetailDto {

    private Long idProduk;
    private String namaProduk;
    private String deskripsi;
    private BigDecimal harga;
    private Integer stok;
    private String kondisi;
    private String ukuran;
    private String warna;
    private String merek;
    private String jenisKelamin;
    private String imageUrl;
    private Long barangTerjual;

    // ✅ TAMBAHAN agar frontend bisa kirim idKategori
    private Long idKategori;

    public ProdukDetailDto() {}

    public Long getIdProduk() { return idProduk; }
    public void setIdProduk(Long idProduk) { this.idProduk = idProduk; }

    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public BigDecimal getHarga() { return harga; }
    public void setHarga(BigDecimal harga) { this.harga = harga; }

    public Integer getStok() { return stok; }
    public void setStok(Integer stok) { this.stok = stok; }

    public String getKondisi() { return kondisi; }
    public void setKondisi(String kondisi) { this.kondisi = kondisi; }

    public String getUkuran() { return ukuran; }
    public void setUkuran(String ukuran) { this.ukuran = ukuran; }

    public String getWarna() { return warna; }
    public void setWarna(String warna) { this.warna = warna; }

    public String getMerek() { return merek; }
    public void setMerek(String merek) { this.merek = merek; }

    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Long getBarangTerjual() { return barangTerjual; }
    public void setBarangTerjual(Long barangTerjual) { this.barangTerjual = barangTerjual; }

    // ✅ TAMBAHAN
    public Long getIdKategori() { return idKategori; }
    public void setIdKategori(Long idKategori) { this.idKategori = idKategori; }
}
