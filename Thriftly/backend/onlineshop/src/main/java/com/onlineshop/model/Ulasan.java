package com.onlineshop.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pm_ulasan")
public class Ulasan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ulasan")
    private Long idUlasan;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_produk", nullable = false)
    private Produk produk;

    @ManyToOne
    @JoinColumn(name = "id_pesanan_item")
    private PesananItem pesananItem; // opsional, untuk pastikan barang ini benar2 dibeli

    @Column(name = "rating", nullable = false)
    private Integer rating; // 1-5

    @Column(name = "komentar", columnDefinition = "TEXT")
    private String komentar;

    @Column(name = "tanggal_ulasan", nullable = false)
    private LocalDateTime tanggalUlasan;

    // ===== Constructor =====
    public Ulasan() {
    }

    // ===== Getter & Setter =====
    public Long getIdUlasan() {
        return idUlasan;
    }

    public void setIdUlasan(Long idUlasan) {
        this.idUlasan = idUlasan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Produk getProduk() {
        return produk;
    }

    public void setProduk(Produk produk) {
        this.produk = produk;
    }

    public PesananItem getPesananItem() {
        return pesananItem;
    }

    public void setPesananItem(PesananItem pesananItem) {
        this.pesananItem = pesananItem;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public LocalDateTime getTanggalUlasan() {
        return tanggalUlasan;
    }

    public void setTanggalUlasan(LocalDateTime tanggalUlasan) {
        this.tanggalUlasan = tanggalUlasan;
    }
}
