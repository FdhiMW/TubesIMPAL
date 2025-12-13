package com.onlineshop.repository;

import com.onlineshop.model.Pesanan;
import org.springframework.data.jpa.repository.JpaRepository;

// ====== [SISIPKAN: IMPORT UNTUK NATIVE QUERY] ======
import org.springframework.data.jpa.repository.Query;
// ====== [AKHIR SISIPAN] ======

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

    // Hitung jumlah pesanan yang statusnya DIKEMAS atau DALAM_PERJALANAN
    long countByStatusPesananIgnoreCaseOrStatusPesananIgnoreCase(String status1, String status2);

    // ====== [SISIPKAN: NATIVE COUNT SESUAI DB pm_pesanan] ======
    @Query(
            value = "SELECT COUNT(*) FROM pm_pesanan WHERE status_pesanan IN ('DIKEMAS','DALAM_PERJALANAN')",
            nativeQuery = true
    )
    long countPesananAktifNative();
    // ====== [AKHIR SISIPAN] ======

    // ====== [SISIPKAN: NATIVE COUNT (LEBIH AMAN: TRIM + UPPER)] ======
    @Query(
            value = "SELECT COUNT(*) FROM pm_pesanan WHERE UPPER(TRIM(status_pesanan)) IN ('DIKEMAS','DALAM_PERJALANAN')",
            nativeQuery = true
    )
    long countPesananAktifNativeStrict();
    // ====== [AKHIR SISIPAN] ======

    // ====== [SISIPKAN: COUNT PESANAN SELESAI (NATIVE, AMAN)] ======
    @Query(
            value = "SELECT COUNT(*) FROM pm_pesanan WHERE UPPER(TRIM(status_pesanan)) = 'SELESAI'",
            nativeQuery = true
    )
    long countPesananSelesaiNative();
    // ====== [AKHIR SISIPAN] ======
}
