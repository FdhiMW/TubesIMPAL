import http from './httpClient'

export function searchProduk(keyword, kategori) {
  return http.get('/produk/search', {
    params: {
      q: keyword && keyword.trim() ? keyword.trim() : undefined,
      kategori: kategori && kategori.trim() ? kategori.trim() : undefined,
    },
  })
}
