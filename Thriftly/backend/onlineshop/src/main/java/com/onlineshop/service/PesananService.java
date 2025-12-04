package com.onlineshop.service;

import com.onlineshop.dto.PesananDtos;
import com.onlineshop.model.Pesanan;
import com.onlineshop.model.PesananItem;
import com.onlineshop.model.Produk;
import com.onlineshop.model.User;
import com.onlineshop.repository.PesananRepository;
import com.onlineshop.repository.PesananItemRepository;
import com.onlineshop.repository.ProdukRepository;
import com.onlineshop.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Sort;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PesananService {

    private final PesananRepository pesananRepository;
    private final UserRepository userRepository;
    private final ProdukRepository produkRepository;

    public PesananService(
            PesananRepository pesananRepository,
            UserRepository userRepository,
            ProdukRepository produkRepository
    ) {
        this.pesananRepository = pesananRepository;
        this.userRepository = userRepository;
        this.produkRepository = produkRepository;
    }

    @Transactional
    public PesananDtos.PesananResponse createPesanan(PesananDtos.CreatePesananRequest req) {
        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item pesanan tidak boleh kosong");
        }

        if (req.getIdUser() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idUser wajib diisi");
        }

        User user = userRepository.findById(req.getIdUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User tidak ditemukan"));

        Pesanan pesanan = new Pesanan();
        pesanan.setUser(user);
        pesanan.setTanggalPesanan(LocalDateTime.now());
        pesanan.setStatusPesanan("DIKEMAS");
        pesanan.setMetodePembayaran(req.getMetodePembayaran());

        pesanan.setNamaPenerima(req.getNamaPenerima());
        pesanan.setNomorTelepon(req.getNomorTelepon());

        String alamatFull = req.getAlamatLengkap();
        if (req.getKota() != null && !req.getKota().isBlank()) {
            alamatFull = alamatFull + ", " + req.getKota();
        }
        if (req.getProvinsi() != null && !req.getProvinsi().isBlank()) {
            alamatFull = alamatFull + ", " + req.getProvinsi();
        }
        pesanan.setAlamatLengkap(alamatFull);
        pesanan.setKodePos(req.getKodePos());
        pesanan.setKurir(req.getLayananPengiriman());

        BigDecimal ongkir = req.getOngkosKirim() != null ? req.getOngkosKirim() : BigDecimal.ZERO;
        pesanan.setOngkosKirim(ongkir);

        BigDecimal totalBarang = BigDecimal.ZERO;
        List<PesananItem> itemList = new ArrayList<>();

        for (PesananDtos.ItemRequest itemReq : req.getItems()) {
            Produk produk = produkRepository.findById(itemReq.getIdProduk())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Produk tidak ditemukan: " + itemReq.getIdProduk()
                    ));

            if (itemReq.getJumlah() == null || itemReq.getJumlah() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Jumlah produk harus lebih dari 0");
            }

            if (produk.getStok() < itemReq.getJumlah()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Stok tidak cukup untuk produk: " + produk.getNamaProduk()
                );
            }

            PesananItem item = new PesananItem();
            item.setPesanan(pesanan);
            item.setProduk(produk);
            item.setNamaProduk(produk.getNamaProduk());
            item.setHargaSatuan(produk.getHarga());
            item.setJumlah(itemReq.getJumlah());

            BigDecimal subtotal = produk.getHarga()
                    .multiply(BigDecimal.valueOf(itemReq.getJumlah()));
            item.setSubtotal(subtotal);

            totalBarang = totalBarang.add(subtotal);
            itemList.add(item);

            // kurangi stok produk
            produk.setStok(produk.getStok() - itemReq.getJumlah());
        }

        pesanan.setTotalBarang(totalBarang);
        pesanan.setTotalPembayaran(totalBarang.add(ongkir));
        pesanan.setItems(itemList);

        pesanan.setKodePesanan(generateKodePesanan());

        Pesanan saved = pesananRepository.save(pesanan);

        PesananDtos.PesananResponse resp = new PesananDtos.PesananResponse();
        resp.setIdPesanan(saved.getIdPesanan());
        resp.setKodePesanan(saved.getKodePesanan());
        resp.setStatusPesanan(saved.getStatusPesanan());
        resp.setTotalBarang(saved.getTotalBarang());
        resp.setOngkosKirim(saved.getOngkosKirim());
        resp.setTotalPembayaran(saved.getTotalPembayaran());

        return resp;
    }

    @Transactional(readOnly = true)
    public List<PesananDtos.PesananResponse> getPesananForAdmin(String kodePesanan) {
        List<Pesanan> pesananList;

        if (kodePesanan != null && !kodePesanan.trim().isEmpty()) {
            pesananList = pesananRepository
                    .findByKodePesananContainingIgnoreCaseOrderByTanggalPesananDesc(kodePesanan.trim());
        } else {
            pesananList = pesananRepository.findAll(
                    Sort.by(Sort.Direction.DESC, "tanggalPesanan")
            );
        }

        List<PesananDtos.PesananResponse> result = new ArrayList<>();
        for (Pesanan p : pesananList) {
            PesananDtos.PesananResponse resp = new PesananDtos.PesananResponse();
            resp.setIdPesanan(p.getIdPesanan());
            resp.setKodePesanan(p.getKodePesanan());
            resp.setStatusPesanan(p.getStatusPesanan());
            resp.setTotalBarang(p.getTotalBarang());
            resp.setOngkosKirim(p.getOngkosKirim());
            resp.setTotalPembayaran(p.getTotalPembayaran());
            result.add(resp);
        }

        return result;
    }

    @Transactional
    public PesananDtos.PesananResponse updateStatusPesanan(Long idPesanan, String statusBaru) {
        if (statusBaru == null || statusBaru.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status pesanan wajib diisi");
        }

        String normalized = statusBaru.trim().toUpperCase();

        // boleh kamu atur sendiri daftar status validnya
        if (!normalized.equals("DIKEMAS") &&
                !normalized.equals("DALAM_PERJALANAN") &&
                !normalized.equals("SELESAI") &&
                !normalized.equals("MENUNGGU_PEMBAYARAN")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status pesanan tidak valid");
        }

        Pesanan pesanan = pesananRepository.findById(idPesanan)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pesanan tidak ditemukan"
                ));

        pesanan.setStatusPesanan(normalized);
        Pesanan saved = pesananRepository.save(pesanan);

        PesananDtos.PesananResponse resp = new PesananDtos.PesananResponse();
        resp.setIdPesanan(saved.getIdPesanan());
        resp.setKodePesanan(saved.getKodePesanan());
        resp.setStatusPesanan(saved.getStatusPesanan());
        resp.setTotalBarang(saved.getTotalBarang());
        resp.setOngkosKirim(saved.getOngkosKirim());
        resp.setTotalPembayaran(saved.getTotalPembayaran());

        return resp;
    }

    private String generateKodePesanan() {
        // contoh sederhana: ORD-<4 karakter random>
        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "ORD-" + random;
    }
}
