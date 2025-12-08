<template>
  <div class="cart-page">
    <main class="cart-content">
      <h1 class="cart-title">Keranjang Belanja</h1>

      <div v-if="loading" class="cart-loading">
        Memuat keranjang...
      </div>

      <div v-else-if="errorMessage" class="cart-error">
        {{ errorMessage }}
      </div>

      <div v-else>
        <!-- Jika keranjang kosong -->
        <div v-if="items.length === 0" class="cart-empty">
          <p>Keranjang kamu masih kosong.</p>
          <button class="btn-primary" @click="goToHome">
            Belanja Sekarang
          </button>
        </div>

        <!-- Jika ada isi keranjang -->
        <div v-else class="cart-layout">
          <!-- KIRI: DAFTAR ITEM -->
          <section class="cart-list">
            <div
              v-for="item in items"
              :key="item.idKeranjang"
              class="cart-item"
            >
              <div class="cart-item-left">
                <div class="cart-item-image">
                  <div v-if="!item.imageUrl" class="image-placeholder"></div>
                  <img
                    v-else
                    :src="item.imageUrl"
                    :alt="item.namaProduk"
                  />
                </div>
                <div class="cart-item-info">
                  <h2 class="cart-item-name">{{ item.namaProduk }}</h2>
                  <p class="cart-item-price">
                    Harga: Rp{{ formatRupiah(item.hargaSatuan) }}
                  </p>
                  <p class="cart-item-subtotal">
                    Subtotal: Rp{{ formatRupiah(item.subtotal) }}
                  </p>
                </div>
              </div>

              <div class="cart-item-right">
                <!-- Kontrol jumlah -->
                <div class="qty-control">
                  <button
                    class="qty-btn"
                    @click="changeQty(item, -1)"
                    :disabled="updatingId === item.idKeranjang"
                  >
                    -
                  </button>
                  <input
                    type="number"
                    class="qty-input"
                    :value="item.jumlah"
                    readonly
                  />
                  <button
                    class="qty-btn"
                    @click="changeQty(item, 1)"
                    :disabled="updatingId === item.idKeranjang"
                  >
                    +
                  </button>
                </div>

                <!-- Tombol aksi -->
                <div class="cart-item-actions">
                  <button
                    class="btn-outline small"
                    @click="removeItem(item)"
                    :disabled="updatingId === item.idKeranjang"
                  >
                    Hapus
                  </button>
                  <button
                    class="btn-primary small"
                    @click="goToCheckout(item)"
                  >
                    Checkout
                  </button>
                </div>
              </div>
            </div>
          </section>

          <!-- KANAN: RINGKASAN -->
          <aside class="cart-summary">
            <h2 class="summary-title">Ringkasan Belanja</h2>
            <div class="summary-row">
              <span>Total sebelum ongkir</span>
              <span class="summary-value">
                Rp{{ formatRupiah(cartTotal) }}
              </span>
            </div>
            <p class="summary-note">
              Total di atas belum termasuk ongkos kirim dan biaya lainnya.
              Ongkir akan dihitung di halaman pembayaran.
            </p>

            <button class="btn-primary full" @click="goToHome">
              Belanja Lagi
            </button>
          </aside>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import keranjangApi from '@/api/keranjangApi'

export default {
  name: 'CartView',
  data() {
    return {
      items: [],
      loading: false,
      errorMessage: '',
      updatingId: null,
    }
  },
  computed: {
    cartTotal() {
      return this.items.reduce((sum, item) => {
        const sub = Number(item.subtotal || 0)
        return sum + sub
      }, 0)
    },
  },
  created() {
    this.loadCart()
  },
  methods: {
    async loadCart() {
      const rawUser = localStorage.getItem('user')
      if (!rawUser) {
        alert('Silakan login terlebih dahulu.')
        this.$router.push('/login')
        return
      }

      const user = JSON.parse(rawUser)

      this.loading = true
      this.errorMessage = ''

      try {
        const data = await keranjangApi.getCart(user.idUser)
        this.items = data || []
      } catch (err) {
        console.error('Gagal memuat keranjang', err)
        this.errorMessage = 'Gagal memuat keranjang. Silakan coba lagi.'
      } finally {
        this.loading = false
      }
    },
    formatRupiah(value) {
      if (value === null || value === undefined) return '0'
      return new Intl.NumberFormat('id-ID').format(Number(value))
    },
    async changeQty(item, delta) {
      const newQty = item.jumlah + delta
      if (newQty <= 0) {
        const ok = confirm('Jumlah 0 akan menghapus item dari keranjang. Lanjutkan?')
        if (!ok) return
        await this.removeItem(item)
        return
      }

      this.updatingId = item.idKeranjang
      try {
        const updated = await keranjangApi.updateQty(item.idKeranjang, newQty)
        // Jika service melempar 204 (NO_CONTENT) ketika qty <= 0, handle-nya di catch
        item.jumlah = updated.jumlah
        item.subtotal = updated.subtotal
      } catch (err) {
        console.error('Gagal mengubah jumlah keranjang', err)
        alert('Gagal mengubah jumlah. Silakan coba lagi.')
        // reload biar sinkron dengan server
        this.loadCart()
      } finally {
        this.updatingId = null
      }
    },
    async removeItem(item) {
      const ok = confirm('Hapus item ini dari keranjang?')
      if (!ok) return

      this.updatingId = item.idKeranjang
      try {
        await keranjangApi.removeItem(item.idKeranjang)
        this.items = this.items.filter(i => i.idKeranjang !== item.idKeranjang)
      } catch (err) {
        console.error('Gagal menghapus item keranjang', err)
        alert('Gagal menghapus item. Silakan coba lagi.')
      } finally {
        this.updatingId = null
      }
    },
    goToCheckout(item) {
      // Checkout item ini saja, dengan jumlah saat ini
      this.$router.push({
        name: 'Checkout',
        query: {
          id: item.idProduk,
          qty: item.jumlah,
        },
      })
    },
    goToHome() {
      this.$router.push('/')
    },
  },
}
</script>

<style scoped>
.cart-page {
  min-height: 100vh;
  background: #f5f7fb;
  display: flex;
  justify-content: center;
  padding: 32px 0;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI',
    sans-serif;
}

.cart-content {
  width: 100%;
  max-width: 1100px;
  background: #ffffff;
  border-radius: 18px;
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.08);
  padding: 28px 32px 32px;
}

.cart-title {
  font-size: 24px;
  font-weight: 700;
  color: #111827;
  margin-bottom: 20px;
}

.cart-loading,
.cart-error {
  padding: 20px;
  text-align: center;
}

.cart-error {
  color: #b91c1c;
}

.cart-empty {
  text-align: center;
  padding: 40px 0;
}

.cart-layout {
  display: grid;
  grid-template-columns: 2.2fr 1fr;
  gap: 24px;
}

/* Item */
.cart-item {
  display: flex;
  justify-content: space-between;
  align-items: stretch;
  padding: 16px 0;
  border-bottom: 1px solid #e5e7eb;
}

.cart-item:last-child {
  border-bottom: none;
}

.cart-item-left {
  display: flex;
  gap: 16px;
}

.cart-item-image {
  width: 80px;
  height: 80px;
  border-radius: 12px;
  overflow: hidden;
  background: #e5e7eb;
}

.cart-item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #e5e7eb, #d1d5db);
}

.cart-item-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.cart-item-name {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 4px;
  color: #111827;
}

.cart-item-price,
.cart-item-subtotal {
  font-size: 14px;
  color: #4b5563;
}

/* Kanan item */
.cart-item-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: space-between;
  gap: 8px;
}

/* Qty control */
.qty-control {
  display: flex;
  align-items: center;
  gap: 8px;
}

.qty-btn {
  width: 32px;
  height: 32px;
  border-radius: 999px;
  border: 1px solid #d1d5db;
  background: #ffffff;
  cursor: pointer;
}

.qty-input {
  width: 48px;
  text-align: center;
  border-radius: 999px;
  border: 1px solid #d1d5db;
  padding: 4px 0;
}

/* Buttons */
.cart-item-actions {
  display: flex;
  gap: 8px;
}

.btn-primary {
  border-radius: 999px;
  padding: 8px 18px;
  border: none;
  background: linear-gradient(135deg, #f97316, #ef4444);
  color: #ffffff;
  font-weight: 600;
  cursor: pointer;
}

.btn-outline {
  border-radius: 999px;
  padding: 8px 18px;
  border: 1px solid #d1d5db;
  background: #ffffff;
  cursor: pointer;
  font-weight: 500;
}

.btn-primary.small,
.btn-outline.small {
  padding: 6px 14px;
  font-size: 13px;
}

.btn-primary.full {
  width: 100%;
  justify-content: center;
}

/* Summary */
.cart-summary {
  border-radius: 16px;
  border: 1px solid #e5e7eb;
  padding: 16px 18px;
  background: #f9fafb;
  align-self: flex-start;
}

.summary-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #111827;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 14px;
}

.summary-value {
  font-weight: 600;
}

.summary-note {
  font-size: 12px;
  color: #6b7280;
  margin-bottom: 12px;
}

/* Responsive */
@media (max-width: 900px) {
  .cart-layout {
    grid-template-columns: 1fr;
  }

  .cart-content {
    padding: 20px;
  }

  .cart-item {
    flex-direction: column;
    gap: 12px;
  }

  .cart-item-right {
    align-items: flex-start;
  }
}
</style>
