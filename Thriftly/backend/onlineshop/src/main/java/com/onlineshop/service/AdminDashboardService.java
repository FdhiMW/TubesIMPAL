package com.onlineshop.service;

import com.onlineshop.repository.PesananRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminDashboardService {

    private final PesananRepository pesananRepository;

    public AdminDashboardService(PesananRepository pesananRepository) {
        this.pesananRepository = pesananRepository;
    }

    // Pesanan aktif = DIKEMAS + DALAM_PERJALANAN
    public long count_pesanan_aktif() {
        return pesananRepository.countByStatusPesananIgnoreCaseOrStatusPesananIgnoreCase(
                "DIKEMAS",
                "DALAM_PERJALANAN"
        );
    }

    // ====== [SISIPKAN: PESANAN AKTIF NATIVE (LANGSUNG pm_pesanan)] ======
    public long count_pesanan_aktif_native() {
        return pesananRepository.countPesananAktifNativeStrict();
    }
    // ====== [AKHIR SISIPAN] ======

    // ====== [SISIPKAN: PESANAN SELESAI NATIVE] ======
    public long count_pesanan_selesai_native() {
        return pesananRepository.countPesananSelesaiNative();
    }
    // ====== [AKHIR SISIPAN] ======
}
