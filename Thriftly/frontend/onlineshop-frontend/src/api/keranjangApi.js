import http from './httpClient'

const keranjangApi = {
  async getCart(idUser) {
    const res = await http.get('/keranjang', {
      params: { idUser },
    })
    return res.data
  },

  async addToCart(payload) {
    // payload: { idUser, idProduk, jumlah }
    const res = await http.post('/keranjang', payload)
    return res.data
  },

  async updateQty(idKeranjang, jumlah) {
    const res = await http.patch(`/keranjang/${idKeranjang}/qty`, {
      jumlah,
    })
    return res.data
  },

  async removeItem(idKeranjang) {
    await http.delete(`/keranjang/${idKeranjang}`)
  },

  async clearUserCart(idUser) {
    await http.delete(`/keranjang/user/${idUser}`)
  },
}

export default keranjangApi
