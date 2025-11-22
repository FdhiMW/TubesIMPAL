<template>
  <div class="product-page">
    <!-- KONTEN DETAIL PRODUK -->
    <main class="product-content">
      <!-- ERROR STATE -->
      <div v-if="errorMessage" class="error-state">
        {{ errorMessage }}
      </div>

      <!-- DETAIL PRODUK -->
      <div v-else-if="produk" class="product-layout">
        <!-- KOLOM KIRI: GAMBAR -->
        <section class="product-left">
          <div class="product-main-image">
            <div v-if="!produk.imageUrl" class="image-placeholder"></div>
            <img
              v-else
              :src="produk.imageUrl"
              :alt="produk.namaProduk"
            />
          </div>

          <div class="product-thumbs">
            <div class="thumb" v-for="n in 3" :key="n"></div>
          </div>
        </section>

        <!-- KOLOM KANAN: INFO PRODUK -->
        <section class="product-right">
          <!-- Judul -->
          <h1 class="product-title">{{ produk.namaProduk }}</h1>

          <!-- Rating -->
          <div class="rating-row">
            <span class="rating-score">5.0</span>
            <span class="rating-stars">
              ★★★★★
            </span>
            <span class="rating-count">5RB Penilaian</span>
          </div>

          <!-- Panel Harga -->
          <div class="price-panel">
            <span class="price-value">
              Rp{{ formatHarga(produk.harga) }}
            </span>
          </div>

          <!-- Jaminan -->
          <div class="info-row">
            <span class="info-label">Jaminan</span>
            <span class="info-value">
              Bebas pengembalian + Proteksi kerusakan
            </span>
          </div>

          <!-- Ukuran -->
          <div class="info-row">
            <span class="info-label">Ukuran</span>
            <div class="chip-row">
              <button
                v-for="size in sizeOptions"
                :key="size"
                class="chip-option"
                :class="{ 'chip-option--active': selectedSize === size }"
                @click="selectSize(size)"
              >
                {{ size }}
              </button>
            </div>
          </div>

          <!-- Warna -->
          <div class="info-row">
            <span class="info-label">Warna</span>
            <div class="chip-row">
              <button
                v-for="color in colorOptions"
                :key="color"
                class="chip-option chip-option--pill"
                :class="{ 'chip-option--active-pill': selectedColor === color }"
                @click="selectColor(color)"
              >
                {{ color }}
              </button>
            </div>
          </div>

          <!-- Kuantitas -->
          <div class="info-row">
            <span class="info-label">Kuantitas</span>
            <div class="qty-control">
              <button class="qty-btn" @click="decreaseQty">-</button>
              <input
                type="text"
                class="qty-input"
                :value="qty"
                readonly
              />
              <button class="qty-btn" @click="increaseQty">+</button>
            </div>
          </div>

          <!-- Tombol Aksi -->
          <div class="action-row">
            <button class="btn-outline" @click="addToCart">
              <span class="cart-icon">🛒</span>
              Masukkan Keranjang
            </button>
            <button class="btn-primary" @click="buyNow">
              Beli Sekarang
            </button>
          </div>
        </section>
      </div>

      <!-- LOADING -->
      <div v-else class="loading-state">
        Memuat produk...
      </div>
    </main>
  </div>
</template>

<script>
import http from '@/api/httpClient'

export default {
  name: 'ProdukDetailView',
  data() {
    return {
      produk: null,
      errorMessage: '',
      qty: 1,
      sizeOptions: ['S', 'M', 'L', 'XL'],
      colorOptions: ['Merah', 'Hitam', 'Kuning'],
      selectedSize: '',
      selectedColor: '',
    }
  },
  created() {
    this.loadProduk()
  },
  methods: {
    async loadProduk() {
      try {
        const id = this.$route.params.id
        const res = await http.get(`/produk/${id}`)
        this.produk = res.data

        // set default size & color kalau sesuai data produk
        if (this.produk.ukuran && this.sizeOptions.includes(this.produk.ukuran)) {
          this.selectedSize = this.produk.ukuran
        }
        if (this.produk.warna) {
          // kapitalisasi sederhana biar mirip daftar colorOptions
          const w = this.produk.warna.charAt(0).toUpperCase() + this.produk.warna.slice(1).toLowerCase()
          if (this.colorOptions.includes(w)) {
            this.selectedColor = w
          }
        }
      } catch (err) {
        console.error('Gagal load produk', err)
        if (err.response && err.response.status === 404) {
          this.errorMessage = 'Produk tidak ditemukan.'
        } else {
          this.errorMessage = 'Terjadi kesalahan saat memuat produk.'
        }
      }
    },
    formatHarga(v) {
      if (v === null || v === undefined) return ''
      return new Intl.NumberFormat('id-ID').format(v)
    },
    selectSize(size) {
      this.selectedSize = size
    },
    selectColor(color) {
      this.selectedColor = color
    },
    decreaseQty() {
      if (this.qty > 1) {
        this.qty -= 1
      }
    },
    increaseQty() {
      const max = this.produk && this.produk.stok ? this.produk.stok : 99
      if (this.qty < max) {
        this.qty += 1
      }
    },
    addToCart() {
      // nanti bisa diganti panggil API keranjang
      alert('Masukkan keranjang (belum diimplementasi)')
    },
    buyNow() {
      // nanti bisa diarahkan ke halaman checkout
      alert('Beli sekarang (belum diimplementasi)')
    },
  },
}
</script>

<style scoped>
.product-page {
  min-height: 100vh;
  background: #f5f7fb;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI',
    sans-serif;
  display: flex;
  justify-content: center;
  padding: 32px 0;
}

.product-content {
  width: 100%;
  max-width: 1100px;
  background: #ffffff;
  border-radius: 18px;
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.08);
  padding: 28px 32px 32px;
}

.product-layout {
  display: grid;
  grid-template-columns: 1.3fr 1.7fr;
  gap: 32px;
}

/* Kiri: gambar */
.product-left {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.product-main-image {
  border-radius: 24px;
  overflow: hidden;
  background: linear-gradient(135deg, #fca5a5, #fdba74);
  min-height: 280px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-main-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-placeholder {
  width: 100%;
  height: 100%;
}

.product-thumbs {
  display: flex;
  gap: 12px;
}

.thumb {
  flex: 1;
  border-radius: 18px;
  background: linear-gradient(135deg, #fca5a5, #fdba74);
  height: 72px;
}

/* Kanan: info */
.product-right {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.product-title {
  font-size: 26px;
  margin: 0;
  font-weight: 700;
  color: #111827;
}

.rating-row {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #4b5563;
}

.rating-score {
  font-weight: 600;
}

.rating-stars {
  color: #facc15;
  letter-spacing: 2px;
}

.rating-count {
  color: #6b7280;
}

/* Panel harga */
.price-panel {
  margin-top: 4px;
  background: #e5e7eb;
  border-radius: 14px;
  padding: 16px 18px;
  display: flex;
  align-items: center;
}

.price-value {
  font-size: 22px;
  font-weight: 700;
  color: #f97316;
}

/* Baris info */
.info-row {
  display: flex;
  align-items: center;
  gap: 24px;
  font-size: 13px;
  padding: 8px 0;
  border-bottom: 1px solid #e5e7eb;
}

.info-label {
  width: 80px;
  color: #6b7280;
}

.info-value {
  color: #111827;
}

/* Chips */
.chip-row {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.chip-option {
  min-width: 36px;
  padding: 6px 12px;
  border-radius: 999px;
  border: 1px solid #d1d5db;
  background: #f9fafb;
  font-size: 13px;
  cursor: pointer;
  color: #4b5563;
}

.chip-option--active {
  background: #eff6ff;
  border-color: #2563eb;
  color: #2563eb;
  font-weight: 600;
}

.chip-option--pill {
  border-radius: 999px;
}

.chip-option--active-pill {
  background: #e0f2fe;
  border-color: #0ea5e9;
  color: #0ea5e9;
  font-weight: 600;
}

/* Kuantitas */
.qty-control {
  display: inline-flex;
  align-items: center;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  overflow: hidden;
}

.qty-btn {
  width: 30px;
  height: 30px;
  border: none;
  background: #f3f4f6;
  cursor: pointer;
  font-size: 16px;
}

.qty-input {
  width: 40px;
  height: 30px;
  border: none;
  text-align: center;
  font-size: 13px;
}

/* Tombol aksi */
.action-row {
  display: flex;
  gap: 16px;
  margin-top: 18px;
}

.btn-outline,
.btn-primary {
  flex: 1;
  border-radius: 999px;
  padding: 10px 18px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}

.btn-outline {
  border: 2px solid #f97316;
  background: #fff7ed;
  color: #f97316;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.cart-icon {
  font-size: 16px;
}

.btn-primary {
  border: none;
  background: linear-gradient(90deg, #ff5a3c, #ff9f1c);
  color: #ffffff;
}

/* State */
.error-state {
  color: #dc2626;
  font-size: 14px;
}

.loading-state {
  color: #4b5563;
}

/* Responsive */
@media (max-width: 900px) {
  .product-layout {
    grid-template-columns: 1fr;
  }
}
</style>
