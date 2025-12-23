package com.onlineshop.controller;

import com.onlineshop.model.Kategori;
import com.onlineshop.repository.KategoriRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/kategori")
@CrossOrigin(origins = "*")
public class KategoriController {

    private final KategoriRepository kategoriRepository;

    public KategoriController(KategoriRepository kategoriRepository) {
        this.kategoriRepository = kategoriRepository;
    }

    @GetMapping
    public List<KategoriDto> getAll() {
        return kategoriRepository.findAll()
                .stream()
                .map(k -> new KategoriDto(k.getIdKategori(), k.getNamaKategori(), k.getDeskripsi()))
                .toList();
    }

    // buat kategori baru jika belum ada
    @PostMapping
    public KategoriDto create(@RequestBody CreateKategoriRequest req) {
        String nama = (req.namaKategori() == null) ? "" : req.namaKategori().trim();
        if (nama.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "namaKategori wajib diisi");
        }

        // jika sudah ada, balikin yang existing
        var existing = kategoriRepository.findByNamaKategoriIgnoreCase(nama);
        if (existing.isPresent()) {
            Kategori k = existing.get();
            return new KategoriDto(k.getIdKategori(), k.getNamaKategori(), k.getDeskripsi());
        }

        Kategori k = new Kategori();
        k.setNamaKategori(nama);
        k.setDeskripsi(req.deskripsi() != null ? req.deskripsi().trim() : ("Kategori " + nama));

        Kategori saved = kategoriRepository.save(k);
        return new KategoriDto(saved.getIdKategori(), saved.getNamaKategori(), saved.getDeskripsi());
    }

    public record KategoriDto(Long idKategori, String namaKategori, String deskripsi) {}
    public record CreateKategoriRequest(String namaKategori, String deskripsi) {}
}
