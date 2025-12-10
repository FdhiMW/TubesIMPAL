package com.onlineshop.controller;

import com.onlineshop.dto.ProdukDetailDto;
import com.onlineshop.dto.BarangTerlarisDto;
import com.onlineshop.service.ProdukService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produk")
@CrossOrigin(origins = "*")
public class ProdukController {

    private final ProdukService produkService;

    public ProdukController(ProdukService produkService) {
        this.produkService = produkService;
    }

    @GetMapping("/terlaris")
    public List<BarangTerlarisDto> getBarangTerlaris(
            @RequestParam(name = "limit", defaultValue = "5") int limit) {
        return produkService.getBarangTerlaris(limit);
    }

    @GetMapping("/{id}")
    public ProdukDetailDto getProdukDetail(@PathVariable("id") Long id) {
        return produkService.getProdukDetail(id);
    }

    @GetMapping("/search")
    public List<ProdukDetailDto> searchProduk(
            @RequestParam(name = "q", required = false) String keyword,
            @RequestParam(name = "kategori", required = false) String kategori
    ) {
        return produkService.searchProduk(keyword, kategori);
    }
}

