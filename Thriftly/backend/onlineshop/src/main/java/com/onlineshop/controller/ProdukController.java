package com.onlineshop.controller;

import com.onlineshop.dto.BarangTerlarisDto;
import com.onlineshop.dto.ProdukDetailDto;
import com.onlineshop.service.ProdukService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/produk")
@CrossOrigin(origins = "*")
public class ProdukController {

    private final ProdukService produkService;

    public ProdukController(ProdukService produkService) {
        this.produkService = produkService;
    }

    // ================= BARANG TERLARIS =================

    @GetMapping("/terlaris")
    public List<BarangTerlarisDto> getBarangTerlaris(
            @RequestParam(name = "limit", defaultValue = "5") int limit) {
        return produkService.getBarangTerlaris(limit);
    }

    // ================= DETAIL PRODUK ====================

    @GetMapping("/{id}")
    public ProdukDetailDto getProdukDetail(@PathVariable("id") Long id) {
        return produkService.getProdukDetail(id);
    }

    // ================= SEARCH PRODUK ====================

    @GetMapping("/search")
    public List<ProdukDetailDto> searchProduk(
            @RequestParam(name = "q", required = false) String keyword,
            @RequestParam(name = "kategori", required = false) String kategori
    ) {
        return produkService.searchProduk(keyword, kategori);
    }

    // =============== TAMBAH PRODUK (MULTIPART) ==========
    // Dipakai oleh form Tambah Produk admin (multipart/form-data)

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProdukDetailDto createProdukMultipart(
            @RequestParam("namaProduk") String namaProduk,
            @RequestParam(value = "deskripsi", required = false) String deskripsi,
            @RequestParam("harga") BigDecimal harga,
            @RequestParam("stok") Integer stok,
            @RequestParam(value = "kondisi", required = false) String kondisi,
            @RequestParam(value = "ukuran", required = false) String ukuran,
            @RequestParam(value = "warna", required = false) String warna,
            @RequestParam(value = "merek", required = false) String merek,
            @RequestParam(value = "jenisKelamin", required = false) String jenisKelamin,
            @RequestPart(value = "gambar", required = false) MultipartFile gambar
    ) {
        ProdukDetailDto dto = new ProdukDetailDto();
        dto.setNamaProduk(namaProduk);
        dto.setDeskripsi(deskripsi);
        dto.setHarga(harga);
        dto.setStok(stok);
        dto.setKondisi(kondisi);
        dto.setUkuran(ukuran);
        dto.setWarna(warna);
        dto.setMerek(merek);
        dto.setJenisKelamin(jenisKelamin);

        // sementara: simpan nama file sebagai imageUrl (kolom image di DB)
        if (gambar != null && !gambar.isEmpty()) {
            dto.setImageUrl(gambar.getOriginalFilename());
        }

        return produkService.createProduk(dto);
    }

    // OPTIONAL: endpoint JSON (kalau suatu saat masih dipakai)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProdukDetailDto createProdukJson(@RequestBody ProdukDetailDto dto) {
        return produkService.createProduk(dto);
    }

    // ================== HAPUS PRODUK ====================

    @DeleteMapping("/{id}")
    public void deleteProduk(@PathVariable("id") Long id) {
        produkService.deleteProduk(id);
    }
}
