package com.onlineshop.repository;

import com.onlineshop.model.Produk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdukRepository extends JpaRepository<Produk, Long> {

    // Dashboard admin: hitung produk aktif (status_produk = 'aktif')
    long countByStatusProdukIgnoreCase(String statusProduk);

    // Barang terlaris (pakai pageable)
    Page<Produk> findByOrderByBarangTerjualDesc(Pageable pageable);

    // Search by kategori name (relasi kategori)
    List<Produk> findByKategori_NamaKategoriIgnoreCase(String namaKategori);

    // Search by keyword (nama atau merek)
    List<Produk> findByNamaProdukContainingIgnoreCaseOrMerekContainingIgnoreCase(String nama, String merek);

    // ====== [SISIPKAN] DISTINCT DROPDOWN DARI DB ======
    @Query("select distinct p.kondisi from Produk p where p.kondisi is not null and p.kondisi <> '' order by p.kondisi")
    List<String> findDistinctKondisi();

    @Query("select distinct p.ukuran from Produk p where p.ukuran is not null and p.ukuran <> '' order by p.ukuran")
    List<String> findDistinctUkuran();

    @Query("select distinct p.jenisKelamin from Produk p where p.jenisKelamin is not null and p.jenisKelamin <> '' order by p.jenisKelamin")
    List<String> findDistinctJenisKelamin();

    // ✅ TAMBAHAN: WARNA & MEREK
    @Query("select distinct p.warna from Produk p where p.warna is not null and p.warna <> '' order by p.warna")
    List<String> findDistinctWarna();

    @Query("select distinct p.merek from Produk p where p.merek is not null and p.merek <> '' order by p.merek")
    List<String> findDistinctMerek();
    // ====== [AKHIR SISIPAN] ======
}
