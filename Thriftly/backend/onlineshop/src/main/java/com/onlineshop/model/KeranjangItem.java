package com.onlineshop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pm_keranjang")
public class KeranjangItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_keranjang")
    private Long idKeranjang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_produk", nullable = false)
    private Produk produk;

    @Column(name = "jumlah", nullable = false)
    private Integer jumlah;

    // ===== Constructor =====
    public KeranjangItem() {
    }

    // ===== Getter & Setter =====
    public Long getIdKeranjang() {
        return idKeranjang;
    }

    public void setIdKeranjang(Long idKeranjang) {
        this.idKeranjang = idKeranjang;
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

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }
}
