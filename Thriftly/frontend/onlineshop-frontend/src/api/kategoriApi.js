import http from './httpClient'

export function getKategoriList() {
  return http.get('/kategori')
}
