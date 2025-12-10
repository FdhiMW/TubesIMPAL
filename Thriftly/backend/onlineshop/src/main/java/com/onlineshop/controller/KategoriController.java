package com.onlineshop.controller;

import com.onlineshop.dto.KategoriDto;
import com.onlineshop.service.KategoriService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kategori")
@CrossOrigin(origins = "*")
public class KategoriController {

    private final KategoriService kategoriService;

    public KategoriController(KategoriService kategoriService) {
        this.kategoriService = kategoriService;
    }

    @GetMapping
    public List<KategoriDto> getAllKategori() {
        return kategoriService.getAllKategori();
    }
}
