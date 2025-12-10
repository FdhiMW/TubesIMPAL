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
import java.util.stream.Collectors;
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
                .map(p -> new ProdukDetailDto(
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
                        p.getImageUrl(),
                        p.getBarangTerjual()
                ))
                .toList();
    }    
}
