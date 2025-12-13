import api from './api'

export function countPesananAktif() {
  return api.get('/api/admin/dashboard/pesanan-aktif')
}

// ====== [SISIPKAN: PESANAN AKTIF NATIVE] ======
export function countPesananAktifNative() {
  return api.get('/api/admin/dashboard/pesanan-aktif-native')
}
// ====== [AKHIR SISIPAN] ======

// ====== [SISIPKAN: PESANAN SELESAI] ======
export function countPesananSelesai() {
  return api.get('/api/admin/dashboard/pesanan-selesai')
}
// ====== [AKHIR SISIPAN] ======
