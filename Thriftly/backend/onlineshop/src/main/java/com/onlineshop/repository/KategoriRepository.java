package com.onlineshop.repository;

import com.onlineshop.model.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KategoriRepository extends JpaRepository<Kategori, Long> {
    Optional<Kategori> findByNamaKategoriIgnoreCase(String namaKategori);
}
