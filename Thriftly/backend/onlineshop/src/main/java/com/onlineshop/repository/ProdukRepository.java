package com.onlineshop.repository;

import com.onlineshop.model.Produk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProdukRepository extends JpaRepository<Produk, Long> {
    // cari produk berdasarkan nama atau merek (case-insensitive)
    List<Produk> findByNamaProdukContainingIgnoreCaseOrMerekContainingIgnoreCase(
            String namaProduk,
            String merek
    );

    Page<Produk> findByOrderByBarangTerjualDesc(Pageable pageable);

    List<Produk> findByKategori_NamaKategoriIgnoreCase(String namaKategori);
}
