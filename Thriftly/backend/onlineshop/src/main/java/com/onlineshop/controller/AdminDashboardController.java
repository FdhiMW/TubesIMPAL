package com.onlineshop.controller;

import com.onlineshop.service.AdminDashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin(origins = "*")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    // GET /api/admin/dashboard/pesanan-aktif
    @GetMapping("/pesanan-aktif")
    public long countPesananAktif() {
        return adminDashboardService.count_pesanan_aktif();
    }

    // ====== [SISIPKAN: ENDPOINT PESANAN AKTIF NATIVE] ======
    // GET /api/admin/dashboard/pesanan-aktif-native
    @GetMapping("/pesanan-aktif-native")
    public long countPesananAktifNative() {
        return adminDashboardService.count_pesanan_aktif_native();
    }
    // ====== [AKHIR SISIPAN] ======

    // ====== [SISIPKAN: ENDPOINT PESANAN SELESAI] ======
    // GET /api/admin/dashboard/pesanan-selesai
    @GetMapping("/pesanan-selesai")
    public long countPesananSelesai() {
        return adminDashboardService.count_pesanan_selesai_native();
    }
    // ====== [AKHIR SISIPAN] ======
}
