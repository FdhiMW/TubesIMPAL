package com.onlineshop.repository;

import com.onlineshop.model.Pesanan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PesananRepository extends JpaRepository<Pesanan, Long> {

    // untuk nanti kalau mau ambil riwayat pesanan per user
    List<Pesanan> findByUser_IdUserOrderByTanggalPesananDesc(Long idUser);
}
