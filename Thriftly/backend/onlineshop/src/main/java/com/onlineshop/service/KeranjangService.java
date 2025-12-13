package com.onlineshop.service;

import com.onlineshop.dto.KeranjangDtos;
import com.onlineshop.model.KeranjangItem;
import com.onlineshop.model.Produk;
import com.onlineshop.model.User;
import com.onlineshop.repository.KeranjangRepository;
import com.onlineshop.repository.ProdukRepository;
import com.onlineshop.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeranjangService {

    private final KeranjangRepository keranjangRepository;
    private final UserRepository userRepository;
    private final ProdukRepository produkRepository;

    public KeranjangService(
            KeranjangRepository keranjangRepository,
            UserRepository userRepository,
            ProdukRepository produkRepository
    ) {
        this.keranjangRepository = keranjangRepository;
        this.userRepository = userRepository;
        this.produkRepository = produkRepository;
    }

    // List keranjang per user
    public List<KeranjangDtos.CartItemResponse> getKeranjangByUser(Long idUser) {
        List<KeranjangItem> items = keranjangRepository.findByUserIdUser(idUser);
        return items.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Tambah ke keranjang (kalau sudah ada produk yang sama -> jumlahnya ditambah)
    public KeranjangDtos.CartItemResponse addToKeranjang(KeranjangDtos.AddCartRequest req) {
        if (req.getIdUser() == null || req.getIdProduk() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idUser dan idProduk wajib diisi");
        }
        if (req.getJumlah() == null || req.getJumlah() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Jumlah harus lebih dari 0");
        }

        User user = userRepository.findById(req.getIdUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User tidak ditemukan"));

        Produk produk = produkRepository.findById(req.getIdProduk())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produk tidak ditemukan"));

        KeranjangItem item = keranjangRepository
                .findByUserIdUserAndProdukIdProduk(user.getIdUser(), produk.getIdProduk())
                .orElse(null);

        if (item == null) {
            item = new KeranjangItem();
            item.setUser(user);
            item.setProduk(produk);
            item.setJumlah(req.getJumlah());
        } else {
            item.setJumlah(item.getJumlah() + req.getJumlah());
        }

        KeranjangItem saved = keranjangRepository.save(item);
        return toResponse(saved);
    }

    // Update jumlah item
    public KeranjangDtos.CartItemResponse updateJumlah(Long idKeranjang, Integer jumlah) {
        KeranjangItem item = keranjangRepository.findById(idKeranjang)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item keranjang tidak ditemukan"));

        if (jumlah == null || jumlah <= 0) {
            keranjangRepository.delete(item);
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Item dihapus karena jumlah <= 0");
        }

        item.setJumlah(jumlah);
        KeranjangItem saved = keranjangRepository.save(item);
        return toResponse(saved);
    }

    public void deleteItem(Long idKeranjang) {
        if (!keranjangRepository.existsById(idKeranjang)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item keranjang tidak ditemukan");
        }
        keranjangRepository.deleteById(idKeranjang);
    }

    public void clearKeranjangByUser(Long idUser) {
        keranjangRepository.deleteByUserIdUser(idUser);
    }

    private KeranjangDtos.CartItemResponse toResponse(KeranjangItem item) {
        KeranjangDtos.CartItemResponse dto = new KeranjangDtos.CartItemResponse();
        dto.setIdKeranjang(item.getIdKeranjang());

        if (item.getProduk() != null) {
            dto.setIdProduk(item.getProduk().getIdProduk());
            dto.setNamaProduk(item.getProduk().getNamaProduk());
            dto.setImageUrl(item.getProduk().getImageUrl());
            dto.setHargaSatuan(item.getProduk().getHarga());
        }

        dto.setJumlah(item.getJumlah());

        BigDecimal harga = (item.getProduk() != null && item.getProduk().getHarga() != null)
                ? item.getProduk().getHarga()
                : BigDecimal.ZERO;

        BigDecimal subtotal = harga.multiply(BigDecimal.valueOf(item.getJumlah() == null ? 0 : item.getJumlah()));
        dto.setSubtotal(subtotal);

        return dto;
    }
}
