package com.onlineshop.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pm_pesanan_item")
public class PesananItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pesanan_item")
    private Long idPesananItem;

    @ManyToOne
    @JoinColumn(name = "id_pesanan", nullable = false)
    private Pesanan pesanan;

    @ManyToOne
    @JoinColumn(name = "id_produk", nullable = false)
    private Produk produk;

    @Column(name = "nama_produk", nullable = false, length = 150)
    private String namaProduk;

    @Column(name = "harga_satuan", nullable = false, precision = 12, scale = 2)
    private BigDecimal hargaSatuan;

    @Column(name = "jumlah", nullable = false)
    private Integer jumlah;

    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    // ===== Constructor =====
    public PesananItem() {
    }

    // ===== Getter & Setter =====
    public Long getIdPesananItem() {
        return idPesananItem;
    }

    public void setIdPesananItem(Long idPesananItem) {
        this.idPesananItem = idPesananItem;
    }

    public Pesanan getPesanan() {
        return pesanan;
    }

    public void setPesanan(Pesanan pesanan) {
        this.pesanan = pesanan;
    }

    public Produk getProduk() {
        return produk;
    }

    public void setProduk(Produk produk) {
        this.produk = produk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public BigDecimal getHargaSatuan() {
        return hargaSatuan;
    }

    public void setHargaSatuan(BigDecimal hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
