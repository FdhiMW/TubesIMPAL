package com.onlineshop.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "pm_kategori")
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_kategori")
    private Long idKategori;

    @Column(name = "nama_kategori", nullable = false, length = 50)
    private String namaKategori;

    @Column(name = "deskripsi", length = 255)
    private String deskripsi;

    // Relasi 1..* ke Produk (optional, tapi berguna)
    @OneToMany(mappedBy = "kategori")
    private List<Produk> produkList;

    // ===== Constructor kosong (wajib untuk JPA) =====
    public Kategori() {
    }

    // ===== Getter & Setter =====
    public Long getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(Long idKategori) {
        this.idKategori = idKategori;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public List<Produk> getProdukList() {
        return produkList;
    }

    public void setProdukList(List<Produk> produkList) {
        this.produkList = produkList;
    }
}
