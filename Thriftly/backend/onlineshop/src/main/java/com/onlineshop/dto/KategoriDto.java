package com.onlineshop.dto;

public class KategoriDto {
    private Long idKategori;
    private String namaKategori;
    private String deskripsi;

    public KategoriDto(Long idKategori, String namaKategori, String deskripsi) {
        this.idKategori = idKategori;
        this.namaKategori = namaKategori;
        this.deskripsi = deskripsi;
    }

    public Long getIdKategori() { return idKategori; }
    public String getNamaKategori() { return namaKategori; }
    public String getDeskripsi() { return deskripsi; }
}
