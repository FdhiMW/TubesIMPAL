package com.onlineshop.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pm_pesanan")
public class Pesanan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pesanan")
    private Long idPesanan;

    @Column(name = "kode_pesanan", length = 30, unique = true)
    private String kodePesanan;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(name = "tanggal_pesanan", nullable = false)
    private LocalDateTime tanggalPesanan;

    @Column(name = "status_pesanan", nullable = false, length = 20)
    private String statusPesanan;

    @Column(name = "metode_pembayaran", length = 30)
    private String metodePembayaran;

    @Column(name = "total_barang", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalBarang;

    @Column(name = "ongkos_kirim", nullable = false, precision = 12, scale = 2)
    private BigDecimal ongkosKirim;

    @Column(name = "total_pembayaran", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPembayaran;

    @Column(name = "nama_penerima", nullable = false, length = 100)
    private String namaPenerima;

    @Column(name = "nomor_telepon", nullable = false, length = 20)
    private String nomorTelepon;

    @Column(name = "alamat_lengkap", nullable = false, columnDefinition = "TEXT")
    private String alamatLengkap;

    @Column(name = "kode_pos", length = 10)
    private String kodePos;

    @Column(name = "kurir", length = 30)
    private String kurir;

    @Column(name = "nomor_resi", length = 50)
    private String nomorResi;

    @Column(name = "tanggal_dikirim")
    private LocalDateTime tanggalDikirim;

    @Column(name = "tanggal_diterima")
    private LocalDateTime tanggalDiterima;

    @Column(name = "sudah_dikonfirmasi", nullable = false)
    private Boolean sudahDikonfirmasi = false;

    @OneToMany(mappedBy = "pesanan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PesananItem> items = new ArrayList<>();

    // ===== Constructor =====
    public Pesanan() {
    }

    // ===== Getter & Setter =====
    public Long getIdPesanan() {
        return idPesanan;
    }

    public void setIdPesanan(Long idPesanan) {
        this.idPesanan = idPesanan;
    }

    public String getKodePesanan() {
        return kodePesanan;
    }

    public void setKodePesanan(String kodePesanan) {
        this.kodePesanan = kodePesanan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTanggalPesanan() {
        return tanggalPesanan;
    }

    public void setTanggalPesanan(LocalDateTime tanggalPesanan) {
        this.tanggalPesanan = tanggalPesanan;
    }

    public String getStatusPesanan() {
        return statusPesanan;
    }

    public void setStatusPesanan(String statusPesanan) {
        this.statusPesanan = statusPesanan;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }

    public BigDecimal getTotalBarang() {
        return totalBarang;
    }

    public void setTotalBarang(BigDecimal totalBarang) {
        this.totalBarang = totalBarang;
    }

    public BigDecimal getOngkosKirim() {
        return ongkosKirim;
    }

    public void setOngkosKirim(BigDecimal ongkosKirim) {
        this.ongkosKirim = ongkosKirim;
    }

    public BigDecimal getTotalPembayaran() {
        return totalPembayaran;
    }

    public void setTotalPembayaran(BigDecimal totalPembayaran) {
        this.totalPembayaran = totalPembayaran;
    }

    public String getNamaPenerima() {
        return namaPenerima;
    }

    public void setNamaPenerima(String namaPenerima) {
        this.namaPenerima = namaPenerima;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public String getAlamatLengkap() {
        return alamatLengkap;
    }

    public void setAlamatLengkap(String alamatLengkap) {
        this.alamatLengkap = alamatLengkap;
    }

    public String getKodePos() {
        return kodePos;
    }

    public void setKodePos(String kodePos) {
        this.kodePos = kodePos;
    }

    public String getKurir() {
        return kurir;
    }

    public void setKurir(String kurir) {
        this.kurir = kurir;
    }

    public String getNomorResi() {
        return nomorResi;
    }

    public void setNomorResi(String nomorResi) {
        this.nomorResi = nomorResi;
    }

    public LocalDateTime getTanggalDikirim() {
        return tanggalDikirim;
    }

    public void setTanggalDikirim(LocalDateTime tanggalDikirim) {
        this.tanggalDikirim = tanggalDikirim;
    }

    public LocalDateTime getTanggalDiterima() {
        return tanggalDiterima;
    }

    public void setTanggalDiterima(LocalDateTime tanggalDiterima) {
        this.tanggalDiterima = tanggalDiterima;
    }

    public Boolean getSudahDikonfirmasi() {
        return sudahDikonfirmasi;
    }

    public void setSudahDikonfirmasi(Boolean sudahDikonfirmasi) {
        this.sudahDikonfirmasi = sudahDikonfirmasi;
    }

    public List<PesananItem> getItems() {
        return items;
    }

    public void setItems(List<PesananItem> items) {
        this.items = items;
    }
}
