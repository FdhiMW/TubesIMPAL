package com.onlineshop.repository;

import com.onlineshop.model.Produk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdukRepository extends JpaRepository<Produk, Long> {
}
