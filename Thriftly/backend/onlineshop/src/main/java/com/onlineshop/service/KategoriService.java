package com.onlineshop.service;

import com.onlineshop.dto.KategoriDto;
import com.onlineshop.model.Kategori;
import com.onlineshop.repository.KategoriRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KategoriService {

    private final KategoriRepository kategoriRepository;

    public KategoriService(KategoriRepository kategoriRepository) {
        this.kategoriRepository = kategoriRepository;
    }

    public List<KategoriDto> getAllKategori() {
        return kategoriRepository.findAll()
                .stream()
                .map(k -> new KategoriDto(
                        k.getIdKategori(),
                        k.getNamaKategori(),
                        k.getDeskripsi()
                ))
                .collect(Collectors.toList());
    }
}
