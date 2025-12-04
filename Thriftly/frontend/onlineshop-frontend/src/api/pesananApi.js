import http from './httpClient'

// ambil daftar pesanan admin (optional filter kode)
export function fetchAdminOrders(kodePesanan) {
  const params = {}
  if (kodePesanan && kodePesanan.trim() !== '') {
    params.kode = kodePesanan.trim()
  }
  return http.get('/pesanan/admin', { params })
}

// update status pesanan
export function updateOrderStatus(idPesanan, statusPesanan) {
  return http.patch(`/pesanan/admin/${idPesanan}/status`, {
    statusPesanan,
  })
}
