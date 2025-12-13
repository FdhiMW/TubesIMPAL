package com.onlineshop.service;

import com.onlineshop.dto.BarangTerlarisDto;
import com.onlineshop.dto.ProdukDetailDto;
import com.onlineshop.model.Produk;
import com.onlineshop.repository.PesananItemRepository;
import com.onlineshop.repository.ProdukRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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

    // ====== [SISIPKAN DI SINI] ======
    // Dashboard admin: total produk aktif (status_produk = 'aktif')
    public long count_produk_aktif() {
        return produkRepository.countByStatusProdukIgnoreCase("aktif");
    }
    // ====== [AKHIR SISIPAN] ======

    // --------------------------------------------------------------------
    // BARANG TERLARIS
    // --------------------------------------------------------------------
    public List<BarangTerlarisDto> getBarangTerlaris(int limit) {
        if (limit <= 0) {
            limit = 5;
        }

        Pageable pageable = PageRequest.of(0, limit);
        var pageProduk = produkRepository.findByOrderByBarangTerjualDesc(pageable);

        if (!pageProduk.isEmpty()) {
            return pageProduk.getContent().stream()
                    .map(p -> new BarangTerlarisDto(
                            p.getIdProduk(),
                            p.getNamaProduk(),
                            p.getHarga(),
                            p.getBarangTerjual() == null ? 0L : p.getBarangTerjual().longValue()
                    ))
                    .toList();
        }

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

    // --------------------------------------------------------------------
    // DETAIL PRODUK
    // --------------------------------------------------------------------
    public ProdukDetailDto getProdukDetail(Long idProduk) {
        Produk p = produkRepository.findById(idProduk)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produk tidak ditemukan"));

        ProdukDetailDto dto = new ProdukDetailDto();
        dto.setIdProduk(p.getIdProduk());
        dto.setNamaProduk(p.getNamaProduk());
        dto.setDeskripsi(p.getDeskripsi());
        dto.setHarga(p.getHarga());
        dto.setStok(p.getStok());
        dto.setKondisi(p.getKondisi());
        dto.setUkuran(p.getUkuran());
        dto.setWarna(p.getWarna());
        dto.setMerek(p.getMerek());
        dto.setJenisKelamin(p.getJenisKelamin());
        dto.setImageUrl(p.getImageUrl());

        if (p.getBarangTerjual() != null) {
            dto.setBarangTerjual(p.getBarangTerjual().longValue());
        }

        return dto;
    }

    // --------------------------------------------------------------------
    // SEARCH PRODUK
    // --------------------------------------------------------------------
    public List<ProdukDetailDto> searchProduk(String keyword, String kategoriName) {
        String key = (keyword == null) ? "" : keyword.trim();
        String cat = (kategoriName == null) ? "" : kategoriName.trim();

        List<Produk> hasil;

        if (!cat.isEmpty()) {
            hasil = produkRepository.findByKategori_NamaKategoriIgnoreCase(cat);
        } else if (!key.isEmpty()) {
            hasil = produkRepository
                    .findByNamaProdukContainingIgnoreCaseOrMerekContainingIgnoreCase(key, key);
        } else {
            hasil = produkRepository.findAll();
        }

        return hasil.stream()
                .map(p -> {
                    ProdukDetailDto dto = new ProdukDetailDto();
                    dto.setIdProduk(p.getIdProduk());
                    dto.setNamaProduk(p.getNamaProduk());
                    dto.setDeskripsi(p.getDeskripsi());
                    dto.setHarga(p.getHarga());
                    dto.setStok(p.getStok());
                    dto.setKondisi(p.getKondisi());
                    dto.setUkuran(p.getUkuran());
                    dto.setWarna(p.getWarna());
                    dto.setMerek(p.getMerek());
                    dto.setJenisKelamin(p.getJenisKelamin());
                    dto.setImageUrl(p.getImageUrl());
                    if (p.getBarangTerjual() != null) {
                        dto.setBarangTerjual(p.getBarangTerjual().longValue());
                    }
                    return dto;
                })
                .toList();
    }

    // --------------------------------------------------------------------
    // TAMBAH PRODUK
    // --------------------------------------------------------------------
    @Transactional
    public ProdukDetailDto createProduk(ProdukDetailDto dto) {
        if (dto.getHarga() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Harga wajib diisi");
        }
        if (dto.getStok() == null) {
            dto.setStok(0);
        }

        Produk p = new Produk();
        p.setNamaProduk(dto.getNamaProduk());
        p.setDeskripsi(dto.getDeskripsi());
        p.setHarga(dto.getHarga());
        p.setStok(dto.getStok());
        p.setKondisi(dto.getKondisi());
        p.setUkuran(dto.getUkuran());
        p.setWarna(dto.getWarna());
        p.setMerek(dto.getMerek());
        p.setJenisKelamin(dto.getJenisKelamin());
        p.setImageUrl(dto.getImageUrl());

        p.setStatusProduk("aktif");
        p.setTanggalDitambahkan(LocalDateTime.now());

        if (dto.getBarangTerjual() != null) {
            p.setBarangTerjual(dto.getBarangTerjual().intValue());
        } else {
            p.setBarangTerjual(0);
        }

        Produk saved = produkRepository.save(p);

        ProdukDetailDto result = new ProdukDetailDto();
        result.setIdProduk(saved.getIdProduk());
        result.setNamaProduk(saved.getNamaProduk());
        result.setDeskripsi(saved.getDeskripsi());
        result.setHarga(saved.getHarga());
        result.setStok(saved.getStok());
        result.setKondisi(saved.getKondisi());
        result.setUkuran(saved.getUkuran());
        result.setWarna(saved.getWarna());
        result.setMerek(saved.getMerek());
        result.setJenisKelamin(saved.getJenisKelamin());
        result.setImageUrl(saved.getImageUrl());
        result.setBarangTerjual(
                saved.getBarangTerjual() == null ? 0L : saved.getBarangTerjual().longValue()
        );

        return result;
    }

    // --------------------------------------------------------------------
    // HAPUS PRODUK
    // --------------------------------------------------------------------
    @Transactional
    public void deleteProduk(Long idProduk) {
        if (!produkRepository.existsById(idProduk)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Produk tidak ditemukan");
        }

        pesananItemRepository.deleteByProduk_IdProduk(idProduk);
        produkRepository.deleteById(idProduk);
    }
}
