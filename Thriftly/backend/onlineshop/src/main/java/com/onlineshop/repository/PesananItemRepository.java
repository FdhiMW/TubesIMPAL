package com.onlineshop.repository;

import com.onlineshop.dto.BarangTerlarisDto;
import com.onlineshop.model.PesananItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PesananItemRepository extends JpaRepository<PesananItem, Long> {

    @Query("""
        SELECT new com.onlineshop.dto.BarangTerlarisDto(
            pi.produk.idProduk,
            pi.produk.namaProduk,
            pi.produk.harga,
            SUM(pi.jumlah),
            pi.produk.imageUrl
        )
        FROM PesananItem pi
        JOIN pi.pesanan p
        WHERE p.statusPesanan = :status
        GROUP BY
            pi.produk.idProduk,
            pi.produk.namaProduk,
            pi.produk.harga,
            pi.produk.imageUrl
        ORDER BY SUM(pi.jumlah) DESC
    """)
    List<BarangTerlarisDto> findBestSellersByStatus(
            @Param("status") String status,
            Pageable pageable
    );

    // dipakai saat HAPUS PRODUK supaya tidak kena error foreign key
    void deleteByProduk_IdProduk(Long idProduk);
}
