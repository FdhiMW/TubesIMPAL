import api from './index';

// search untuk dashboard & katalog admin
export function searchProduk(keyword, kategori) {
  return api.get('/api/produk/search', {
    params: {
      q: keyword || null,
      kategori: kategori || null,
    },
  });
}

// create produk dengan multipart (dipakai Tambah Produk)
export function createProduk(formData) {
  return api.post('/api/produk/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}

// hapus produk
export function deleteProduk(id) {
  return api.delete(`/api/produk/${id}`);
}

// (opsional) ambil detail
export function getProdukDetail(id) {
  return api.get(`/api/produk/${id}`);
}

// ====== [SISIPKAN DI produkApi.js] ======
export function countProdukAktif() {
  return api.get('/api/produk/count/aktif');
}
// ====== [AKHIR SISIPAN] ======
