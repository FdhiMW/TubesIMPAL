package com.onlineshop.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pm_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "nama_lengkap", nullable = false, length = 100)
    private String namaLengkap;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "nomor_telepon", length = 20)
    private String nomorTelepon;

    @Column(name = "tanggal_daftar", nullable = false)
    private LocalDateTime tanggalDaftar;

    @Column(name = "peran_pengguna", nullable = false, length = 20)
    private String peranPengguna;

    @Column(name = "aktif", nullable = false)
    private Boolean aktif;

    // GETTER & SETTER

    public Long getIdUser() { return idUser; }
    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getNomorTelepon() { return nomorTelepon; }
    public void setNomorTelepon(String nomorTelepon) { this.nomorTelepon = nomorTelepon; }

    public LocalDateTime getTanggalDaftar() { return tanggalDaftar; }
    public void setTanggalDaftar(LocalDateTime tanggalDaftar) { this.tanggalDaftar = tanggalDaftar; }

    public String getPeranPengguna() { return peranPengguna; }
    public void setPeranPengguna(String peranPengguna) { this.peranPengguna = peranPengguna; }

    public Boolean getAktif() { return aktif; }
    public void setAktif(Boolean aktif) { this.aktif = aktif; }
}
