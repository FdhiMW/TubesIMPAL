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
                    v-if="item && item.imageUrl"
                    :src="resolveImageUrl(item.imageUrl)"
                    class="thumb-img"
                    alt="Foto produk"
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

    <!-- ====== [DITAMBAHKAN: POPUP KONFIRMASI + INFO MENARIK] ====== -->
    <div v-if="showPopup" class="popup-overlay" @click.self="onPopupCancel">
      <div class="popup-card" role="dialog" aria-modal="true">
        <div
          class="popup-icon"
          :class="{
            'popup-icon--warn': popupType === 'warn',
            'popup-icon--success': popupType === 'success',
            'popup-icon--confirm': popupType === 'confirm',
          }"
        >
          <span v-if="popupType === 'warn'" class="popup-warn">!</span>
          <span v-else class="popup-check">✓</span>
        </div>

        <div class="popup-title">{{ popupTitle }}</div>
        <div class="popup-subtitle">{{ popupMessage }}</div>

        <!-- tombol confirm -->
        <div v-if="popupType === 'confirm'" class="popup-actions">
          <button class="popup-btn ghost" @click="onPopupCancel">Batal</button>
          <button class="popup-btn primary" @click="onPopupConfirm">OK</button>
        </div>

        <!-- tombol info -->
        <button v-else class="popup-ok" @click="closePopup">OK</button>
      </div>
    </div>
    <!-- ====== [AKHIR PENYESUAIAN] ====== -->
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

      // ====== [DITAMBAHKAN: STATE POPUP MENARIK] ======
      showPopup: false,
      popupType: 'confirm', // 'confirm' | 'warn' | 'success'
      popupTitle: '',
      popupMessage: '',
      popupOnConfirm: null,
      popupOnCancel: null,
      // ====== [AKHIR PENYESUAIAN] ======
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
    // ====== [DITAMBAHKAN: HELPER POPUP] ======
    openPopup({ type = 'confirm', title = '', message = '', onConfirm = null, onCancel = null }) {
      this.popupType = type
      this.popupTitle = title
      this.popupMessage = message
      this.popupOnConfirm = onConfirm
      this.popupOnCancel = onCancel
      this.showPopup = true
    },
    closePopup() {
      this.showPopup = false
      this.popupOnConfirm = null
      this.popupOnCancel = null
    },
    onPopupConfirm() {
      const fn = this.popupOnConfirm
      this.closePopup()
      if (typeof fn === 'function') fn()
    },
    onPopupCancel() {
      const fn = this.popupOnCancel
      this.closePopup()
      if (typeof fn === 'function') fn()
    },
    // ====== [AKHIR PENYESUAIAN] ======

    async loadCart() {
      const rawUser = localStorage.getItem('user')
      if (!rawUser) {
        // alert -> popup menarik
        this.openPopup({
          type: 'warn',
          title: 'Login dibutuhkan',
          message: 'Silakan login terlebih dahulu untuk melihat keranjang.',
          onConfirm: null,
        })
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
        this.openPopup({
          type: 'warn',
          title: 'Gagal memuat keranjang',
          message: 'Terjadi kesalahan saat memuat keranjang. Silakan coba lagi.',
        })
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
        // confirm -> popup menarik
        this.openPopup({
          type: 'confirm',
          title: 'Hapus item dari keranjang?',
          message: 'Jumlah 0 akan menghapus item dari keranjang. Lanjutkan?',
          onConfirm: async () => {
            await this.removeItem(item, { skipConfirm: true })
          },
        })
        return
      }

      this.updatingId = item.idKeranjang
      try {
        const updated = await keranjangApi.updateQty(item.idKeranjang, newQty)
        item.jumlah = updated.jumlah
        item.subtotal = updated.subtotal
      } catch (err) {
        console.error('Gagal mengubah jumlah keranjang', err)
        this.openPopup({
          type: 'warn',
          title: 'Gagal mengubah jumlah',
          message: 'Silakan coba lagi. Data akan disinkronkan ulang.',
        })
        this.loadCart()
      } finally {
        this.updatingId = null
      }
    },

    async removeItem(item, opts = {}) {
      const { skipConfirm = false } = opts

      if (!skipConfirm) {
        // confirm -> popup menarik
        this.openPopup({
          type: 'confirm',
          title: 'Hapus item ini?',
          message: 'Item akan dihapus dari keranjang. Lanjutkan?',
          onConfirm: async () => {
            await this.removeItem(item, { skipConfirm: true })
          },
        })
        return
      }

      this.updatingId = item.idKeranjang
      try {
        await keranjangApi.removeItem(item.idKeranjang)
        this.items = this.items.filter(i => i.idKeranjang !== item.idKeranjang)

        // info sukses (opsional tapi enak)
        this.openPopup({
          type: 'success',
          title: 'Item dihapus',
          message: 'Item berhasil dihapus dari keranjang.',
        })
      } catch (err) {
        console.error('Gagal menghapus item keranjang', err)
        this.openPopup({
          type: 'warn',
          title: 'Gagal menghapus item',
          message: 'Silakan coba lagi.',
        })
      } finally {
        this.updatingId = null
      }
    },

    goToCheckout(item) {
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
    resolveImageUrl(url) {
      const API = 'http://localhost:8080'

      if (!url) return ''
      if (url.startsWith('http')) return url
      if (url.startsWith('/uploads/')) return API + url
      if (url.startsWith('/foto-barang/')) return url

      return API + '/uploads/' + url
    },
  },
}
</script>

<style scoped>
.cart-page {
  min-height: 100vh;
  background: #ffffff;
  display: flex;
  justify-content: stretch;
  padding: 0;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI',
    sans-serif;
}

.cart-content {
  width: 100%;
  max-width: 100%;
  background: #ffffff;
  border-radius: 0;
  box-shadow: none;
  padding: 34px 48px 48px;
}

.cart-title {
  font-size: 34px;
  font-weight: 900;
  color: #111827;
  margin-bottom: 26px;
  letter-spacing: -0.02em;
}

.cart-loading,
.cart-error {
  padding: 20px;
  text-align: center;
  font-size: 16px;
}

.cart-error {
  color: #b91c1c;
}

.cart-empty {
  text-align: center;
  padding: 40px 0;
}

.cart-empty p {
  font-size: 16px;
}

/* ✅ AREA MERAH (KIRI) DIBESARKAN: list dibuat lebih lebar + item dibuat “lebih tinggi” */
.cart-layout {
  display: grid;
  grid-template-columns: 3.6fr 1.4fr;
  gap: 32px;
  align-items: start;
}

.cart-list {
  min-width: 0;
}

.cart-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 28px 24px;
  border: 1px solid #eef2f7;
  border-radius: 18px;
  background: #ffffff;
  min-height: 170px;
  margin-bottom: 18px;
}

.cart-item:last-child {
  margin-bottom: 0;
}

.cart-item-left {
  display: flex;
  gap: 22px;
  align-items: center;
}

.cart-item-image {
  width: 140px;
  height: 140px;
  border-radius: 18px;
  overflow: hidden;
  background: #e5e7eb;
  flex: 0 0 auto;
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
  gap: 6px;
}

.cart-item-name {
  font-size: 22px;
  font-weight: 900;
  margin: 0;
  color: #111827;
  line-height: 1.15;
}

.cart-item-price,
.cart-item-subtotal {
  font-size: 16px;
  color: #4b5563;
  margin: 0;
}

.cart-item-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: center;
  gap: 16px;
  min-width: 360px;
}

.qty-control {
  display: flex;
  align-items: center;
  gap: 12px;
}

.qty-btn {
  width: 44px;
  height: 44px;
  border-radius: 999px;
  border: 1px solid #d1d5db;
  background: #ffffff;
  cursor: pointer;
  font-size: 18px;
}

.qty-input {
  width: 74px;
  text-align: center;
  border-radius: 999px;
  border: 1px solid #d1d5db;
  padding: 10px 0;
  font-size: 15px;
}

.cart-item-actions {
  display: flex;
  gap: 12px;
}

.btn-primary {
  border-radius: 999px;
  padding: 12px 26px;
  border: none;
  background: linear-gradient(135deg, #f97316, #ef4444);
  color: #ffffff;
  font-weight: 900;
  cursor: pointer;
  font-size: 14px;
}

.btn-outline {
  border-radius: 999px;
  padding: 12px 26px;
  border: 1px solid #d1d5db;
  background: #ffffff;
  cursor: pointer;
  font-weight: 800;
  font-size: 14px;
}

.btn-primary.small,
.btn-outline.small {
  padding: 10px 22px;
  font-size: 14px;
}

.btn-primary.full {
  width: 100%;
  justify-content: center;
  padding: 12px 22px;
  font-size: 14px;
}

.cart-summary {
  border-radius: 18px;
  border: 1px solid #e5e7eb;
  padding: 20px 22px;
  background: #f9fafb;
  align-self: flex-start;
  position: sticky;
  top: 18px;
}

.summary-title {
  font-size: 20px;
  font-weight: 900;
  margin-bottom: 14px;
  color: #111827;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 15px;
}

.summary-value {
  font-weight: 900;
}

.summary-note {
  font-size: 13px;
  color: #6b7280;
  margin-bottom: 14px;
  line-height: 1.5;
}

@media (max-width: 1100px) {
  .cart-layout {
    grid-template-columns: 1fr;
  }

  .cart-item-right {
    min-width: unset;
    width: 100%;
    align-items: flex-start;
  }

  .cart-item {
    flex-direction: column;
    align-items: stretch;
    gap: 16px;
  }
}

@media (max-width: 900px) {
  .cart-content {
    padding: 20px;
  }

  .cart-title {
    font-size: 26px;
    margin-bottom: 18px;
  }

  .cart-item {
    padding: 18px;
    min-height: unset;
  }

  .cart-item-image {
    width: 110px;
    height: 110px;
    border-radius: 16px;
  }

  .cart-item-name {
    font-size: 18px;
  }

  .cart-item-price,
  .cart-item-subtotal {
    font-size: 14px;
  }

  .cart-summary {
    position: static;
    top: unset;
  }
}

.thumb-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
  display: block;
}

/* ====== [DITAMBAHKAN: STYLE POPUP MENARIK] ====== */
.popup-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.popup-card {
  width: 520px;
  max-width: calc(100vw - 40px);
  background: #ffffff;
  border-radius: 22px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.25);
  padding: 34px 34px 26px;
  text-align: center;
  animation: popupIn 160ms ease-out;
}

@keyframes popupIn {
  from {
    transform: translateY(10px) scale(0.98);
    opacity: 0;
  }
  to {
    transform: translateY(0) scale(1);
    opacity: 1;
  }
}

.popup-icon {
  width: 92px;
  height: 92px;
  border-radius: 999px;
  margin: 0 auto 18px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.popup-icon--confirm {
  background: linear-gradient(90deg, #ef4444, #f97316);
}

.popup-icon--warn {
  background: linear-gradient(90deg, #f97316, #ef4444);
}

.popup-icon--success {
  background: linear-gradient(90deg, #22c55e, #16a34a);
}

.popup-check {
  color: #ffffff;
  font-size: 46px;
  line-height: 1;
  font-weight: 800;
}

.popup-warn {
  color: #ffffff;
  font-size: 52px;
  line-height: 1;
  font-weight: 900;
}

.popup-title {
  font-size: 28px;
  font-weight: 900;
  color: #111827;
  margin-bottom: 8px;
  letter-spacing: -0.01em;
}

.popup-subtitle {
  font-size: 16px;
  color: #374151;
  margin-bottom: 22px;
  line-height: 1.5;
}

/* Mode info (satu tombol) */
.popup-ok {
  width: 100%;
  border: none;
  border-radius: 999px;
  padding: 14px 0;
  font-size: 16px;
  font-weight: 800;
  cursor: pointer;
  background: linear-gradient(90deg, #ef4444, #f97316);
  color: #ffffff;
}

/* Mode confirm (dua tombol) */
.popup-actions {
  display: flex;
  gap: 12px;
}

.popup-btn {
  flex: 1;
  border: none;
  border-radius: 999px;
  padding: 14px 0;
  font-size: 16px;
  font-weight: 800;
  cursor: pointer;
}

.popup-btn.ghost {
  background: #e5e7eb;
  color: #374151;
}

.popup-btn.primary {
  background: linear-gradient(90deg, #ef4444, #f97316);
  color: #ffffff;
}
/* ====== [AKHIR PENYESUAIAN] ====== */
</style>
