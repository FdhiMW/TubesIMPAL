package com.onlineshop.repository;

import com.onlineshop.model.Produk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
