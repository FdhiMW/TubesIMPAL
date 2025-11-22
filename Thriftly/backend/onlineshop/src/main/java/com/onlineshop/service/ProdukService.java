package com.onlineshop.service;

import com.onlineshop.dto.BarangTerlarisDto;
import com.onlineshop.dto.ProdukDetailDto;
import com.onlineshop.model.Produk;
import com.onlineshop.repository.PesananItemRepository;
import com.onlineshop.repository.ProdukRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class ProdukService {

    private final PesananItemRepository pesananItemRepository;
    private final ProdukRepository produkRepository;

    public ProdukService(PesananItemRepository pesananItemRepository,
                         ProdukRepository produkRepository) {
        this.pesananItemRepository = pesananItemRepository;
        this.produkRepository = produkRepository;
    }

    public List<BarangTerlarisDto> getBarangTerlaris(int limit) {
        if (limit <= 0) {
            limit = 5;
        }

        Pageable pageable = PageRequest.of(0, limit);

        // statusPesanan disesuaikan dengan yang kamu pakai di DB,
        // misal "SELESAI" atau "SELESAI_BAYAR"
        String statusSelesai = "SELESAI";

        List<BarangTerlarisDto> hasil =
                pesananItemRepository.findBestSellersByStatus(statusSelesai, pageable);

        // Kalau belum ada pesanan sama sekali, bisa fallback ke produk biasa
        if (hasil.isEmpty()) {
            return produkRepository
                    .findAll(pageable)
                    .map(p -> new BarangTerlarisDto(
                            p.getIdProduk(),
                            p.getNamaProduk(),
                            p.getHarga(),
                            0L
                    ))
                    .getContent();
        }

        return hasil;
    }

    public ProdukDetailDto getProdukDetail(Long idProduk) {
        Produk p = produkRepository.findById(idProduk)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produk tidak ditemukan"));

        return new ProdukDetailDto(
                p.getIdProduk(),
                p.getNamaProduk(),
                p.getDeskripsi(),
                p.getHarga(),
                p.getStok(),
                p.getKondisi(),
                p.getUkuran(),
                p.getWarna(),
                p.getMerek(),
                p.getJenisKelamin(),
                p.getImageUrl()
        );
    }
}
