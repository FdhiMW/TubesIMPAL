package com.onlineshop.dto;

import java.math.BigDecimal;
import java.util.List;

public class PesananDtos {

    public static class ItemRequest {
        private Long idProduk;
        private Integer jumlah;

        public Long getIdProduk() { return idProduk; }
        public void setIdProduk(Long idProduk) { this.idProduk = idProduk; }

        public Integer getJumlah() { return jumlah; }
        public void setJumlah(Integer jumlah) { this.jumlah = jumlah; }
    }

    public static class CreatePesananRequest {
        private Long idUser;
        private String metodePembayaran;

        private String namaPenerima;
        private String nomorTelepon;
        private String alamatLengkap;
        private String kota;
        private String provinsi;
        private String kodePos;

        private String layananPengiriman;
        private BigDecimal ongkosKirim;

        private List<ItemRequest> items;

        public Long getIdUser() { return idUser; }
        public void setIdUser(Long idUser) { this.idUser = idUser; }

        public String getMetodePembayaran() { return metodePembayaran; }
        public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }

        public String getNamaPenerima() { return namaPenerima; }
        public void setNamaPenerima(String namaPenerima) { this.namaPenerima = namaPenerima; }

        public String getNomorTelepon() { return nomorTelepon; }
        public void setNomorTelepon(String nomorTelepon) { this.nomorTelepon = nomorTelepon; }

        public String getAlamatLengkap() { return alamatLengkap; }
        public void setAlamatLengkap(String alamatLengkap) { this.alamatLengkap = alamatLengkap; }

        public String getKota() { return kota; }
        public void setKota(String kota) { this.kota = kota; }

        public String getProvinsi() { return provinsi; }
        public void setProvinsi(String provinsi) { this.provinsi = provinsi; }

        public String getKodePos() { return kodePos; }
        public void setKodePos(String kodePos) { this.kodePos = kodePos; }

        public String getLayananPengiriman() { return layananPengiriman; }
        public void setLayananPengiriman(String layananPengiriman) { this.layananPengiriman = layananPengiriman; }

        public BigDecimal getOngkosKirim() { return ongkosKirim; }
        public void setOngkosKirim(BigDecimal ongkosKirim) { this.ongkosKirim = ongkosKirim; }

        public List<ItemRequest> getItems() { return items; }
        public void setItems(List<ItemRequest> items) { this.items = items; }
    }

    public static class PesananResponse {
        private Long idPesanan;
        private String kodePesanan;
        private String statusPesanan;
        private BigDecimal totalBarang;
        private BigDecimal ongkosKirim;
        private BigDecimal totalPembayaran;

        public Long getIdPesanan() { return idPesanan; }
        public void setIdPesanan(Long idPesanan) { this.idPesanan = idPesanan; }

        public String getKodePesanan() { return kodePesanan; }
        public void setKodePesanan(String kodePesanan) { this.kodePesanan = kodePesanan; }

        public String getStatusPesanan() { return statusPesanan; }
        public void setStatusPesanan(String statusPesanan) { this.statusPesanan = statusPesanan; }

        public BigDecimal getTotalBarang() { return totalBarang; }
        public void setTotalBarang(BigDecimal totalBarang) { this.totalBarang = totalBarang; }

        public BigDecimal getOngkosKirim() { return ongkosKirim; }
        public void setOngkosKirim(BigDecimal ongkosKirim) { this.ongkosKirim = ongkosKirim; }

        public BigDecimal getTotalPembayaran() { return totalPembayaran; }
        public void setTotalPembayaran(BigDecimal totalPembayaran) { this.totalPembayaran = totalPembayaran; }
    }

    public static class UpdateStatusRequest {
        private String statusPesanan;
    
        public String getStatusPesanan() {
            return statusPesanan;
        }
    
        public void setStatusPesanan(String statusPesanan) {
            this.statusPesanan = statusPesanan;
        }
    }

    public static class TrackItemResponse {
        private Long idPesanan;
        private String kodePesanan;
        private String statusPesanan;

        private Long idProduk;
        private String namaProduk;
        private String ukuran;
        private String jenis; // nama kategori
        private java.math.BigDecimal harga;
        private String imageUrl;

        public Long getIdPesanan() { return idPesanan; }
        public void setIdPesanan(Long idPesanan) { this.idPesanan = idPesanan; }

        public String getKodePesanan() { return kodePesanan; }
        public void setKodePesanan(String kodePesanan) { this.kodePesanan = kodePesanan; }

        public String getStatusPesanan() { return statusPesanan; }
        public void setStatusPesanan(String statusPesanan) { this.statusPesanan = statusPesanan; }

        public Long getIdProduk() { return idProduk; }
        public void setIdProduk(Long idProduk) { this.idProduk = idProduk; }

        public String getNamaProduk() { return namaProduk; }
        public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

        public String getUkuran() { return ukuran; }
        public void setUkuran(String ukuran) { this.ukuran = ukuran; }

        public String getJenis() { return jenis; }
        public void setJenis(String jenis) { this.jenis = jenis; }

        public java.math.BigDecimal getHarga() { return harga; }
        public void setHarga(java.math.BigDecimal harga) { this.harga = harga; }

        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    }
}
