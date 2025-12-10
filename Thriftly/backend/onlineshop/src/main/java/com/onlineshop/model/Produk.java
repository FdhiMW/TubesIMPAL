package com.onlineshop.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pm_produk")
public class Produk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produk")
    private Long idProduk;

    @ManyToOne
    @JoinColumn(name = "id_kategori")
    private Kategori kategori;

    @ManyToOne
    @JoinColumn(name = "id_penjual")
    private User penjual;

    @Column(name = "nama_produk", nullable = false, length = 150)
    private String namaProduk;

    @Column(name = "deskripsi", columnDefinition = "TEXT")
    private String deskripsi;

    @Column(name = "harga", nullable = false, precision = 12, scale = 2)
    private BigDecimal harga;

    @Column(name = "stok", nullable = false)
    private Integer stok;

    @Column(name = "barang_terjual")
    private Integer barangTerjual = 0;

    @Column(name = "kondisi", length = 20)
    private String kondisi; // misal: bekas, like new

    @Column(name = "ukuran", length = 10)
    private String ukuran;

    @Column(name = "warna", length = 30)
    private String warna;

    @Column(name = "merek", length = 50)
    private String merek;

    @Column(name = "jenis_kelamin", length = 20)
    private String jenisKelamin; // Unisex / Pria / Wanita

    @Column(name = "status_produk", nullable = false, length = 20)
    private String statusProduk; // aktif, nonaktif, terjual, dll.

    @Column(name = "tanggal_ditambahkan", nullable = false)
    private LocalDateTime tanggalDitambahkan;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    // ===== Constructor =====
    public Produk() {
    }

    // ===== Getter & Setter =====
    public Long getIdProduk() {
        return idProduk;
    }

    public void setIdProduk(Long idProduk) {
        this.idProduk = idProduk;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public User getPenjual() {
        return penjual;
    }

    public void setPenjual(User penjual) {
        this.penjual = penjual;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }

    public Integer getStok() {
        return stok;
    }

    public void setStok(Integer stok) {
        this.stok = stok;
    }

    public Integer getBarangTerjual() {
        return barangTerjual;
    }
    
    public void setBarangTerjual(Integer barangTerjual) {
        this.barangTerjual = barangTerjual;
    }    

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public String getUkuran() {
        return ukuran;
    }

    public void setUkuran(String ukuran) {
        this.ukuran = ukuran;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public String getMerek() {
        return merek;
    }

    public void setMerek(String merek) {
        this.merek = merek;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getStatusProduk() {
        return statusProduk;
    }

    public void setStatusProduk(String statusProduk) {
        this.statusProduk = statusProduk;
    }

    public LocalDateTime getTanggalDitambahkan() {
        return tanggalDitambahkan;
    }

    public void setTanggalDitambahkan(LocalDateTime tanggalDitambahkan) {
        this.tanggalDitambahkan = tanggalDitambahkan;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
