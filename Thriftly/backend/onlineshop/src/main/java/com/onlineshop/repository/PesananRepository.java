package com.onlineshop.repository;

import com.onlineshop.model.Pesanan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PesananRepository extends JpaRepository<Pesanan, Long> {

    // untuk nanti kalau mau ambil riwayat pesanan per user
    List<Pesanan> findByUser_IdUserOrderByTanggalPesananDesc(Long idUser);

    // untuk admin cari berdasarkan kode_pesanan (nomor pesanan)
    List<Pesanan> findByKodePesananContainingIgnoreCaseOrderByTanggalPesananDesc(String kodePesanan);

    // ambil semua pesanan diurutkan dari yang terbaru (dipakai dashboard admin)
    List<Pesanan> findByOrderByTanggalPesananDesc();

    // kalau butuh cuma top 5 pesanan terbaru
    List<Pesanan> findTop5ByOrderByTanggalPesananDesc();
}
