package com.onlineshop.dto;

import java.math.BigDecimal;

public class KeranjangDtos {

    // Request untuk tambah ke keranjang
    public static class AddCartRequest {
        private Long idUser;
        private Long idProduk;
        private Integer jumlah;

        public Long getIdUser() {
            return idUser;
        }

        public void setIdUser(Long idUser) {
            this.idUser = idUser;
        }

        public Long getIdProduk() {
            return idProduk;
        }

        public void setIdProduk(Long idProduk) {
            this.idProduk = idProduk;
        }

        public Integer getJumlah() {
            return jumlah;
        }

        public void setJumlah(Integer jumlah) {
            this.jumlah = jumlah;
        }
    }

    // Request untuk update jumlah
    public static class UpdateCartRequest {
        private Integer jumlah;

        public Integer getJumlah() {
            return jumlah;
        }

        public void setJumlah(Integer jumlah) {
            this.jumlah = jumlah;
        }
    }

    // Response item keranjang
    public static class CartItemResponse {
        private Long idKeranjang;
        private Long idProduk;
        private String namaProduk;
        private String imageUrl;
        private BigDecimal hargaSatuan;
        private Integer jumlah;
        private BigDecimal subtotal;

        public Long getIdKeranjang() {
            return idKeranjang;
        }

        public void setIdKeranjang(Long idKeranjang) {
            this.idKeranjang = idKeranjang;
        }

        public Long getIdProduk() {
            return idProduk;
        }

        public void setIdProduk(Long idProduk) {
            this.idProduk = idProduk;
        }

        public String getNamaProduk() {
            return namaProduk;
        }

        public void setNamaProduk(String namaProduk) {
            this.namaProduk = namaProduk;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public BigDecimal getHargaSatuan() {
            return hargaSatuan;
        }

        public void setHargaSatuan(BigDecimal hargaSatuan) {
            this.hargaSatuan = hargaSatuan;
        }

        public Integer getJumlah() {
            return jumlah;
        }

        public void setJumlah(Integer jumlah) {
            this.jumlah = jumlah;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }
    }
}
