<template>
  <div class="detail-page">
    <main class="detail-container" v-if="produk">
      <!-- KIRI: gambar produk -->
      <section class="image-section">
        <div class="main-image">
          <img
            v-if="produk && produk.imageUrl"
            :src="resolveImageUrl(produk.imageUrl)"
            alt="Foto produk"
            class="main-image"
          />
        </div>
        <div class="thumb-row">
          <div class="thumb-box"></div>
          <div class="thumb-box"></div>
          <div class="thumb-box"></div>
        </div>
      </section>

      <!-- KANAN: info produk -->
      <section class="info-section">
        <div class="info-top">
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

          <div class="info-row info-row-desc">
            <span class="label">Deskripsi</span>
            <span class="value value-wrap">{{ produk.deskripsi || '-' }}</span>
          </div>

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

          <div class="info-row" v-if="false">
            <span class="label">Terjual</span>
            <span class="value">{{ produk.barangTerjual ?? 0 }}</span>
          </div>

          <div class="info-row">
            <span class="label">Ukuran</span>
            <span class="value">{{ produk.ukuran || '-' }}</span>
          </div>

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

          <div class="info-row" v-if="false">
            <span class="label">Warna</span>
            <div class="chip-row">
              <button class="chip">Merah</button>
              <button class="chip chip-active">Hitam</button>
              <button class="chip">Kuning</button>
            </div>
          </div>

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

          <p v-if="(produk.stok ?? 0) <= 0" class="stock-warning">
            Stok habis. Produk tidak bisa dibeli saat ini.
          </p>
        </div>

        <!-- ✅ BUTTON SEJAJAR THUMBNAIL -->
        <div class="action-bottom">
          <div class="action-row">
            <button class="btn-outline" @click="addToCart" :disabled="(produk.stok ?? 0) <= 0">
              <span class="cart-icon">🛒</span>
              Masukkan Keranjang
            </button>
            <button class="btn-primary" @click="buyNow" :disabled="(produk.stok ?? 0) <= 0">
              Beli Sekarang
            </button>
          </div>
        </div>
      </section>
    </main>

    <div v-else class="loading-state">
      Memuat detail produk...
    </div>

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
  components: { SuccessModal },
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
      if (this.qty < stok) this.qty += 1
    },

    decreaseQty () {
      if (this.qty > 1) this.qty -= 1
    },

    async addToCart () {
      if (!this.produk) return
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
        this.showSuccess = true
      } catch (err) {
        console.error('Gagal menambah ke keranjang', err)
        alert('Gagal menambah ke keranjang, silakan coba lagi.')
      }
    },

    buyNow () {
      if (!this.produk) return
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
    },
    resolveImageUrl(url) {
      const API = "http://localhost:8080";

      if (!url) return "";
      if (url.startsWith("http")) return url;
      if (url.startsWith("/uploads/")) return API + url;
      if (url.startsWith("/foto-barang/")) return url;

      return API + "/uploads/" + url;
    },
  }
}
</script>

<style scoped>
.detail-page {
  width: 100%;
  min-height: 100vh;
  background: #f5f7fb;
  display: flex;
  justify-content: stretch;
  align-items: stretch;
  padding: 0;
  margin: 0;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
}

/* ✅ FIX: hilangkan ruang kosong besar di tengah */
.detail-container {
  --bottomRowH: 170px; /* 🔥 tinggi area bawah (thumb + action) */

  width: 100%;
  max-width: 100%;
  min-height: 100vh;
  background: #ffffff;
  border-radius: 0;
  box-shadow: none;

  padding: 28px 32px 32px;

  display: grid;
  grid-template-columns: 1.1fr 1.5fr;
  grid-template-rows: auto auto;
  grid-template-areas:
    "main  info"
    "thumb action";
  gap: 18px 32px;

  align-content: start;
  align-items: start;
}

/* wrapper tetap ada tapi anak ikut grid parent */
.image-section { display: contents; }
.info-section { display: contents; }

/* KIRI */
.main-image {
  grid-area: main;
  width: 100%;
  min-height: 640px;
  height: calc(100vh - 300px);
  max-height: 860px;
  border-radius: 24px;
  background: linear-gradient(135deg, #fecaca, #fed7aa);
}

.thumb-row {
  grid-area: thumb;
  display: flex;
  gap: 16px;
  height: var(--bottomRowH);
  align-items: stretch;
}

.thumb-box {
  flex: 1;
  height: 100%;
  border-radius: 18px;
  background: linear-gradient(135deg, #fecaca, #fed7aa);
}

/* ===============================
   ✅ FONT KANAN (AREA KOTAK MERAH) DIBESARKAN
================================ */
.info-top {
  grid-area: info;
  display: flex;
  flex-direction: column;
  gap: 18px;
}

/* Judul lebih besar */
.product-title {
  font-size: 48px;         /* 🔥 dari 38px */
  font-weight: 900;
  color: #111827;
  line-height: 1.08;
  margin: 0;
}

/* Harga lebih besar */
.price-box {
  display: inline-block;
  padding: 16px 22px;      /* sedikit lebih tebal */
  border-radius: 16px;
  background: #f3f4f6;
  font-size: 32px;         /* 🔥 dari 26px */
  font-weight: 900;
  color: #f97316;
}

/* Detail label/value lebih besar */
.info-row {
  display: flex;
  align-items: center;
  margin-top: 8px;
}

.label {
  width: 150px;            /* biar rapi saat font besar */
  font-size: 18px;         /* 🔥 dari 16px */
  color: #6b7280;
}

.value {
  font-size: 18px;         /* 🔥 dari 16px */
  color: #111827;
}

.info-row-desc { align-items: flex-start; }

.value-wrap {
  white-space: pre-wrap;
  line-height: 1.7;
  font-size: 18px;         /* 🔥 dari 16px */
}

/* qty ikut besar */
.qty-control {
  display: flex;
  align-items: center;
  gap: 14px;
}

.qty-btn {
  width: 42px;             /* 🔥 lebih besar */
  height: 42px;
  border-radius: 999px;
  border: 1px solid #d1d5db;
  background: #ffffff;
  cursor: pointer;
  font-size: 18px;         /* 🔥 */
}

.qty-input {
  width: 72px;             /* 🔥 */
  text-align: center;
  border-radius: 999px;
  border: 1px solid #d1d5db;
  padding: 10px 0;
  font-size: 16px;         /* 🔥 */
}

.stock-warning {
  margin: 12px 0 0;
  font-size: 14px;         /* 🔥 dari 12px */
  color: #ef4444;
}

/* ACTION */
.action-bottom {
  grid-area: action;
  display: flex;
  align-items: center;
  height: var(--bottomRowH);
}

.action-row {
  width: 100%;
  display: flex;
  gap: 16px;
}

.btn-outline,
.btn-primary {
  flex: 1;
  width: 100%;
  min-height: 64px;
  border-radius: 999px;
  padding: 18px 24px;
  font-size: 16px;
  font-weight: 900;
  cursor: pointer;
}

.btn-outline {
  border: 1px solid #f97316;
  background: #fff7ed;
  color: #f97316;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.btn-primary {
  border: none;
  background: linear-gradient(135deg, #f97316, #ef4444);
  color: #ffffff;
}

.btn-outline:disabled,
.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.cart-icon { font-size: 18px; }

.loading-state {
  padding: 40px;
  text-align: center;
}

@media (max-width: 900px) {
  .detail-container {
    grid-template-columns: 1fr;
    grid-template-rows: auto auto auto;
    grid-template-areas:
      "main"
      "info"
      "action";
    padding: 20px;
    min-height: 100vh;
    gap: 18px;
    align-content: start;
  }

  .main-image {
    min-height: 420px;
    height: 520px;
    max-height: none;
  }

  .thumb-row {
    display: none; /* kalau mau tetap tampil di HP, hapus baris ini */
  }

  .action-bottom {
    height: auto;
    align-items: stretch;
  }

  /* versi mobile tetap besar tapi aman */
  .product-title { font-size: 32px; }
  .price-box { font-size: 24px; }
  .label, .value, .value-wrap { font-size: 15px; }

  .btn-outline, .btn-primary {
    min-height: 56px;
    padding: 16px 18px;
    font-size: 14px;
  }
}
</style>
