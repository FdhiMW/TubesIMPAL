import api from './index'

// GET semua kategori
export function getKategoriList() {
  return api.get('/api/kategori')
}

// POST buat kategori baru (dipakai saat user mengetik kategori baru)
export function createKategori(payload) {
  return api.post('/api/kategori', payload)
}
