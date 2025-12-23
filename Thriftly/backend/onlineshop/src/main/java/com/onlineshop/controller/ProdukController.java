package com.onlineshop.controller;

import com.onlineshop.dto.BarangTerlarisDto;
import com.onlineshop.dto.ProdukDetailDto;
import com.onlineshop.service.ProdukService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produk")
@CrossOrigin(origins = "*")
public class ProdukController {

    private final ProdukService produkService;

    public ProdukController(ProdukService produkService) {
        this.produkService = produkService;
    }

    // ====== [SISIPKAN DI SINI] ======
    // Dashboard Admin: Total Barang Aktif
    // GET /api/produk/count/aktif
    @GetMapping("/count/aktif")
    public long countProdukAktif() {
        return produkService.count_produk_aktif();
    }
    // ====== [AKHIR SISIPAN] ======

    // ====== [SISIPKAN] MASTER DROPDOWN (DINAMIS) ======
    @GetMapping("/master-dropdown")
    public Map<String, List<String>> getMasterDropdown() {
        Map<String, List<String>> result = new HashMap<>();
        result.put("kondisi", produkService.get_distinct_kondisi());
        result.put("ukuran", produkService.get_distinct_ukuran());
        result.put("jenisKelamin", produkService.get_distinct_jenis_kelamin());

        // ✅ TAMBAHAN
        result.put("warna", produkService.get_distinct_warna());
        result.put("merek", produkService.get_distinct_merek());
        return result;
    }
    // ====== [AKHIR SISIPAN] ======

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

            // ✅ TAMBAHAN: idKategori dikirim dari frontend
            @RequestParam(value = "idKategori", required = false) Long idKategori,

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

        // ✅ TAMBAHAN
        dto.setIdKategori(idKategori);

        if (gambar != null && !gambar.isEmpty()) {
            dto.setImageUrl(gambar.getOriginalFilename());
        }

        return produkService.createProduk(dto);
    }

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
