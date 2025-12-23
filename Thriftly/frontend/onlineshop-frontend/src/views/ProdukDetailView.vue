<template>
  <div class="detail-page">
    <main class="detail-container" v-if="produk">
      <!-- KIRI: gambar produk -->
      <section class="image-section">
        <div class="main-image">
          <!-- kalau nanti ada imageUrl, tinggal ganti div ini jadi <img> -->
        </div>
        <div class="thumb-row">
          <div class="thumb-box"></div>
          <div class="thumb-box"></div>
          <div class="thumb-box"></div>
        </div>
      </section>

      <!-- KANAN: info produk -->
      <section class="info-section">
        <h1 class="product-title">{{ produk.namaProduk }}</h1>

        <!-- [DISESUAIKAN] Rating DIHILANGKAN tanpa menghapus struktur -->
        <div class="rating-row" v-if="false">
          <span class="rating-stars">★ ★ ★ ★ ★</span>
          <span class="rating-text">5.0 • 5RB Penilaian</span>
        </div>

        <div class="price-box">
          Rp{{ formatRupiah(produk.harga) }}
        </div>

        <!-- [DISESUAIKAN] Jaminan tidak dimunculkan tapi struktur tetap ada -->
        <div class="info-row" v-if="false">
          <span class="label">Jaminan</span>
          <span class="value">Bebas pengembalian + Proteksi kerusakan</span>
        </div>

        <!-- =========================
             [DISESUAIKAN] Deskripsi dipindah ke paling atas (di atas Jenis Kelamin)
             ========================= -->
        <div class="info-row info-row-desc">
          <span class="label">Deskripsi</span>
          <span class="value value-wrap">{{ produk.deskripsi || '-' }}</span>
        </div>

        <!-- =========================
             [FIELD DB]
             ========================= -->
        <div class="info-row">
          <span class="label">Jenis Kelamin</span>
          <span class="value">{{ produk.jenisKelamin || '-' }}</span>
        </div>

        <div class="info-row">
          <span class="label">Kondisi</span>
          <span class="value">{{ produk.kondisi || '-' }}</span>
        </div>

        <div class="info-row">
          <span class="label">Merek</span>
          <span class="value">{{ produk.merek || '-' }}</span>
        </div>

        <!-- [DISESUAIKAN] Terjual tidak dimunculkan tapi struktur tetap ada -->
        <div class="info-row" v-if="false">
          <span class="label">Terjual</span>
          <span class="value">{{ produk.barangTerjual ?? 0 }}</span>
        </div>

        <!-- =========================
             [DISESUAIKAN] Ukuran & Warna jadi field biasa (seperti yang lain)
             ========================= -->
        <div class="info-row">
          <span class="label">Ukuran</span>
          <span class="value">{{ produk.ukuran || '-' }}</span>
        </div>

        <!-- Struktur chip lama tetap ada tapi disembunyikan (tidak dihapus) -->
        <div class="info-row" v-if="false">
          <span class="label">Ukuran</span>
          <div class="chip-row">
            <button class="chip">S</button>
            <button class="chip">M</button>
            <button class="chip chip-active">L</button>
            <button class="chip">XL</button>
          </div>
        </div>

        <div class="info-row">
          <span class="label">Warna</span>
          <span class="value">{{ produk.warna || '-' }}</span>
        </div>

        <!-- Struktur chip lama tetap ada tapi disembunyikan (tidak dihapus) -->
        <div class="info-row" v-if="false">
          <span class="label">Warna</span>
          <div class="chip-row">
            <button class="chip">Merah</button>
            <button class="chip chip-active">Hitam</button>
            <button class="chip">Kuning</button>
          </div>
        </div>

        <!-- ✅ [DISESUAIKAN] STOK DIPINDAHKAN KE ATAS FIELD KUANTITAS (STRUKTUR TETAP) -->
        <div class="info-row">
          <span class="label">Stok</span>
          <span class="value">{{ produk.stok ?? 0 }}</span>
        </div>

        <div class="info-row">
          <span class="label">Kuantitas</span>
          <div class="qty-control">
            <button class="qty-btn" @click="decreaseQty">-</button>
            <input
              type="number"
              class="qty-input"
              :value="qty"
              readonly
            />
            <button class="qty-btn" @click="increaseQty">+</button>
          </div>
        </div>

        <!-- Tombol Aksi -->
        <div class="action-row">
          <button class="btn-outline" @click="addToCart" :disabled="(produk.stok ?? 0) <= 0">
            <span class="cart-icon">🛒</span>
            Masukkan Keranjang
          </button>
          <button class="btn-primary" @click="buyNow" :disabled="(produk.stok ?? 0) <= 0">
            Beli Sekarang
          </button>
        </div>

        <!-- [DITAMBAHKAN] Info stok habis (tidak merusak struktur) -->
        <p v-if="(produk.stok ?? 0) <= 0" class="stock-warning">
          Stok habis. Produk tidak bisa dibeli saat ini.
        </p>
      </section>
    </main>

    <div v-else class="loading-state">
      Memuat detail produk...
    </div>

    <!-- MODAL SUKSES -->
    <SuccessModal
      :visible="showSuccess"
      title="Berhasil ditambahkan ke keranjang"
      @close="showSuccess = false"
    />
  </div>
</template>

<script>
import http from '@/api/httpClient'
import keranjangApi from '@/api/keranjangApi'
import SuccessModal from '@/components/common/SuccessModal.vue'

export default {
  name: 'ProdukDetailView',
  components: {
    SuccessModal
  },
  data () {
    return {
      produk: null,
      qty: 1,
      loading: false,
      showSuccess: false
    }
  },
  created () {
    this.fetchProduct()
  },
  methods: {
    async fetchProduct () {
      const id = this.$route.params.id
      this.loading = true
      try {
        const res = await http.get(`/produk/${id}`)
        this.produk = res.data

        // [DITAMBAHKAN] jika stok 0, qty tetap 1 (aman)
        if ((this.produk?.stok ?? 0) <= 0) {
          this.qty = 1
        }
      } catch (err) {
        console.error('Gagal memuat produk', err)
      } finally {
        this.loading = false
      }
    },

    formatRupiah (value) {
      if (!value && value !== 0) return '0'
      return new Intl.NumberFormat('id-ID').format(Number(value))
    },

    increaseQty () {
      const stok = this.produk?.stok ?? 0
      if (stok <= 0) return

      // [DITAMBAHKAN] batasi qty tidak melebihi stok
      if (this.qty < stok) {
        this.qty += 1
      }
    },

    decreaseQty () {
      if (this.qty > 1) {
        this.qty -= 1
      }
    },

    async addToCart () {
      if (!this.produk) return

      // [DITAMBAHKAN] cegah jika stok habis
      if ((this.produk.stok ?? 0) <= 0) {
        alert('Stok habis. Produk tidak bisa ditambahkan ke keranjang.')
        return
      }

      const rawUser = localStorage.getItem('user')
      if (!rawUser) {
        alert('Silakan login terlebih dahulu sebelum menambahkan ke keranjang.')
        this.$router.push('/login')
        return
      }

      const user = JSON.parse(rawUser)

      try {
        await keranjangApi.addToCart({
          idUser: user.idUser,
          idProduk: this.produk.idProduk,
          jumlah: this.qty
        })

        // tampilkan modal sukses
        this.showSuccess = true
      } catch (err) {
        console.error('Gagal menambah ke keranjang', err)
        alert('Gagal menambah ke keranjang, silakan coba lagi.')
      }
    },

    buyNow () {
      if (!this.produk) return

      // [DITAMBAHKAN] cegah jika stok habis
      if ((this.produk.stok ?? 0) <= 0) {
        alert('Stok habis. Produk tidak bisa dibeli saat ini.')
        return
      }

      this.$router.push({
        name: 'Checkout',
        query: {
          id: this.produk.idProduk,
          qty: this.qty
        }
      })
    }
  }
}
</script>

<style scoped>
.detail-page {
  min-height: 100vh;
  background: #f5f7fb;
  display: flex;
  justify-content: center;
  padding: 32px 0;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
}

.detail-container {
  width: 100%;
  max-width: 1100px;
  background: #ffffff;
  border-radius: 24px;
  box-shadow: 0 18px 40px rgba(15, 23, 42, 0.12);
  padding: 28px 32px 32px;
  display: grid;
  grid-template-columns: 1.1fr 1.5fr;
  gap: 32px;
}

/* KIRI: GAMBAR */
.image-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 🔥 KOTAK MERAH DIPERKECIL DI SINI */
.main-image {
  width: 100%;
  max-height: 420px;                  /* batas maksimal tinggi */
  aspect-ratio: 4 / 5;                /* proporsi foto pakaian */
  border-radius: 24px;
  background: linear-gradient(135deg, #fecaca, #fed7aa);
}

.thumb-row {
  display: flex;
  gap: 12px;
}

.thumb-box {
  flex: 1;
  height: 60px;                       /* tidak terlalu tinggi */
  border-radius: 18px;
  background: linear-gradient(135deg, #fecaca, #fed7aa);
}

/* KANAN: INFO */
.info-section {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.product-title {
  font-size: 28px;
  font-weight: 700;
  color: #111827;
}

.rating-row {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #6b7280;
}

.rating-stars {
  color: #f59e0b;
}

.price-box {
  display: inline-block;
  padding: 10px 16px;
  border-radius: 16px;
  background: #f3f4f6;
  font-size: 22px;
  font-weight: 700;
  color: #f97316;
}

.info-row {
  display: flex;
  align-items: center;
  margin-top: 4px;
}

.label {
  width: 90px;
  font-size: 14px;
  color: #6b7280;
}

.value {
  font-size: 14px;
  color: #111827;
}

.chip-row {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.chip {
  min-width: 40px;
  padding: 6px 10px;
  border-radius: 999px;
  border: 1px solid #e5e7eb;
  background: #ffffff;
  cursor: pointer;
  font-size: 13px;
}

.chip-active {
  border-color: #f97316;
  background: #fff7ed;
  color: #f97316;
}

/* [DITAMBAHKAN] chip display-only */
.chip-disabled {
  cursor: default;
  opacity: 0.95;
}

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

/* BUTTONS */
.action-row {
  margin-top: 12px;
  display: flex;
  gap: 12px;
}

.btn-outline {
  flex: 1;
  border-radius: 999px;
  padding: 10px 18px;
  border: 1px solid #f97316;
  background: #fff7ed;
  color: #f97316;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.cart-icon {
  font-size: 18px;
}

.btn-primary {
  flex: 1;
  border-radius: 999px;
  padding: 10px 18px;
  border: none;
  background: linear-gradient(135deg, #f97316, #ef4444);
  color: #ffffff;
  font-weight: 600;
  cursor: pointer;
}

.btn-outline:disabled,
.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.loading-state {
  padding: 40px;
  text-align: center;
}

/* [DITAMBAHKAN] deskripsi supaya bisa multiline */
.info-row-desc {
  align-items: flex-start;
}

.value-wrap {
  white-space: pre-wrap;
  line-height: 1.45;
}

/* [DITAMBAHKAN] stok habis text */
.stock-warning {
  margin: 6px 0 0;
  font-size: 12px;
  color: #ef4444;
}

/* RESPONSIVE */
@media (max-width: 900px) {
  .detail-container {
    grid-template-columns: 1fr;
    padding: 20px;
  }
}
</style>
