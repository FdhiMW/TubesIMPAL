<!-- frontend/onlineshop-frontend/src/views/HomeView.vue -->
<template>
  <div class="user-home">
    <main class="page-content">
      <!-- HERO + BANNER -->
      <section class="hero-section">
        <div class="hero-main skeleton"></div>
        <div class="hero-side">
          <div class="hero-side-item skeleton"></div>
          <div class="hero-side-item skeleton"></div>
        </div>
      </section>

      <!-- CATEGORY CHIPS -->
      <section class="category-chips">
        <button
          v-for="chip in chips"
          :key="chip"
          class="chip"
          @click="goToCategoryByName(chip)"
        >
          <span class="chip-circle"></span>
          <span class="chip-label">{{ chip }}</span>
        </button>
      </section>

      <!-- KATEGORI PAKAIAN -->
      <section class="section-block">
        <div class="section-header">
          <div>
            <h2>Kategori Pakaian</h2>
            <p class="section-subtitle">
              Pilih kategori pakaian bekas yang kamu inginkan.
            </p>
          </div>
          <!-- kalau mau nanti bisa diarahkan ke page khusus kategori -->
          <button class="link-button" @click="goToAllCategories">Lihat semua ›</button>
        </div>

        <div class="category-grid">
          <article
            v-for="item in categoryItems"
            :key="item.id"
            class="category-card"
            @click="goToCategory(item)"
          >
            <div class="category-thumb skeleton"></div>
            <div class="category-body">
              <h3>{{ item.title }}</h3>
              <p>{{ item.subtitle }}</p>
              <span class="category-hint">{{ item.hint }}</span>
            </div>
          </article>
        </div>
      </section>

      <!-- BARANG TERLARIS -->
      <section class="section-block">
        <h2 class="section-title-center">BARANG TERLARIS</h2>

        <div class="best-seller-grid">
          <article
            v-for="item in bestSellers"
            :key="item.id"
            class="best-card"
            @click="goToProductDetail(item.id)"
          >
            <div class="best-thumb skeleton">
              <span class="discount-badge">-{{ item.discount }}%</span>
            </div>

            <div class="best-body">
              <h3>{{ item.title }}</h3>
              <p class="best-tagline">{{ item.tagline }}</p>

              <div class="label-row">
                <span
                  v-for="label in item.labels"
                  :key="label"
                  class="pill"
                >
                  {{ label }}
                </span>
              </div>

              <div class="price-row">
                <span class="price">Rp{{ formatPrice(item.price) }}</span>
              </div>
            </div>
          </article>
        </div>
      </section>
    </main>
  </div>
</template>

<script>
import http from '@/api/httpClient'

// Konfigurasi copywriting per kategori (opsional)
// Nama harus sama dengan nama_kategori di DB (Kaos, Kemeja, Jaket, Celana, Dress)
const CATEGORY_UI_CONFIG = {
  Kaos: {
    subtitle: 'Oversize & graphic tee',
    hint: 'Cocok buat OOTD',
  },
  Kemeja: {
    subtitle: 'Casual sampai formal',
    hint: 'Look santai & rapi',
  },
  Jaket: {
    subtitle: 'Denim, bomber, varsity',
    hint: 'Outer andalan',
  },
  Celana: {
    subtitle: 'Jeans, chino, cargo',
    hint: 'Mix & match mudah',
  },
  Dress: {
    subtitle: 'Casual sampai kondangan',
    hint: 'Feminine style',
  },
}

export default {
  name: 'HomeView',
  data() {
    return {
      chips: [],
      categoryItems: [],
      bestSellers: [],
    }
  },
  methods: {
    formatPrice(value) {
      if (!value && value !== 0) return ''
      return Number(value).toLocaleString('id-ID')
    },
    goToProductDetail(id) {
      this.$router.push(`/produk/${id}`)
    },

    // ==== KATEGORI ====
    async loadCategories() {
      try {
        const res = await http.get('/kategori')
        const list = Array.isArray(res.data) ? res.data : []

        // bentuk data untuk kartu kategori
        this.categoryItems = list.map((k, idx) => {
          const ui = CATEGORY_UI_CONFIG[k.namaKategori] || {}
          return {
            id: k.idKategori || idx + 1,
            key: k.namaKategori,
            title: k.namaKategori,
            subtitle: ui.subtitle || k.deskripsi || '',
            hint: ui.hint || 'Lihat koleksi',
          }
        })

        // chips diambil dari kategori yang sama
        this.chips = this.categoryItems.map(c => c.title)
      } catch (err) {
        console.error('Gagal memuat kategori, pakai dummy:', err)

        // fallback statis kalau API error
        const fallback = [
          {
            id: 1,
            key: 'Kemeja',
            title: 'Kemeja',
            subtitle: 'Casual sampai formal',
            hint: 'Look santai & rapi',
          },
          {
            id: 2,
            key: 'Kaos',
            title: 'Kaos',
            subtitle: 'Oversize & graphic tee',
            hint: 'Cocok buat OOTD',
          },
          {
            id: 3,
            key: 'Jaket',
            title: 'Jaket',
            subtitle: 'Denim, bomber, varsity',
            hint: 'Outer andalan',
          },
          {
            id: 4,
            key: 'Celana',
            title: 'Celana',
            subtitle: 'Jeans, chino, cargo',
            hint: 'Mix & match mudah',
          },
          {
            id: 5,
            key: 'Dress',
            title: 'Dress',
            subtitle: 'Casual sampai kondangan',
            hint: 'Feminine style',
          },
        ]

        this.categoryItems = fallback
        this.chips = fallback.map(f => f.title)
      }
    },

    goToCategory(item) {
      // item.key berisi nama kategori (Kaos, Kemeja, dsb.)
      this.$router.push({
        name: 'product-search',
        query: {
          kategori: item.key,
        },
      })
    },

    goToCategoryByName(name) {
      // dipanggil dari chip (string)
      this.$router.push({
        name: 'product-search',
        query: {
          kategori: name,
        },
      })
    },

    goToAllCategories() {
      // sementara diarahkan ke search tanpa filter
      this.$router.push({ name: 'product-search' })
    },

    // ==== BARANG TERLARIS ====
    async loadBestSellers() {
      try {
        const res = await http.get('/produk/terlaris?limit=6')
        this.bestSellers = res.data.map(p => ({
          id: p.idProduk,
          title: p.namaProduk,
          tagline: `${p.totalTerjual} terjual`,
          labels: ['Best Seller'],
          price: p.harga,
          discount: 30,
        }))
      } catch (err) {
        console.error('Gagal memuat barang terlaris, pakai dummy dulu:', err)
        this.bestSellers = [
          {
            id: 1,
            title: 'Kemeja Flanel Pria Vintage',
            tagline: 'Stok Terbatas',
            labels: ['Stok Terbatas'],
            price: 35000,
            discount: 40,
          },
          {
            id: 2,
            title: 'Kaos Band Oversize Hitam',
            tagline: 'Favorit Anak Kost',
            labels: ['Favorit Anak Kost'],
            price: 28000,
            discount: 30,
          },
          {
            id: 3,
            title: 'Hoodie Basic Cream Unisex',
            tagline: 'Best Seller',
            labels: ['Best Seller'],
            price: 45000,
            discount: 55,
          },
          {
            id: 4,
            title: 'Jaket Denim Wash Light',
            tagline: 'Look Vintage',
            labels: ['Look Vintage'],
            price: 60000,
            discount: 35,
          },
          {
            id: 5,
            title: 'Jeans Momfit High Waist',
            tagline: 'Ukuran Terbatas',
            labels: ['Ukuran Terbatas'],
            price: 55000,
            discount: 25,
          },
          {
            id: 6,
            title: 'Dress Floral Summer',
            tagline: 'Cocok Liburan',
            labels: ['Cocok Liburan'],
            price: 42000,
            discount: 50,
          },
        ]
      }
    },
  },
  mounted() {
    this.loadCategories()
    this.loadBestSellers()
  },
}
</script>

<style scoped>
.user-home {
  min-height: 100vh;
  background: #f5f7fb;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI',
    sans-serif;
  color: #222;
}

.page-content {
  padding: 20px 40px 40px;
}

/* Hero */

.hero-section {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 16px;
  margin-bottom: 20px;
}

.hero-main {
  height: 180px;
  border-radius: 18px;
}

.hero-side {
  display: grid;
  grid-template-rows: 1fr 1fr;
  gap: 12px;
}

.hero-side-item {
  border-radius: 18px;
}

/* Skeleton */

.skeleton {
  background: linear-gradient(135deg, #ffe0cf, #ffd4bf);
}

/* Chips */

.category-chips {
  display: flex;
  gap: 14px;
  padding: 14px 10px;
  background: #fff;
  border-radius: 18px;
  margin-bottom: 24px;
  overflow-x: auto;
}

.chip {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  border: none;
  background: transparent;
  cursor: pointer;
}

.chip-circle {
  width: 52px;
  height: 52px;
  border-radius: 18px;
  background: linear-gradient(135deg, #ff7b56, #ffb094);
}

.chip-label {
  font-size: 11px;
  color: #555;
}

/* Sections */

.section-block {
  background: #fff;
  border-radius: 20px;
  padding: 18px 20px 22px;
  margin-bottom: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 16px;
}

.section-header h2 {
  margin: 0;
  font-size: 18px;
}

.section-subtitle {
  margin: 4px 0 0;
  font-size: 12px;
  color: #777;
}

.link-button {
  border: none;
  background: none;
  color: #ff6b3d;
  font-size: 12px;
  cursor: pointer;
}

/* Category cards */

.category-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
}

.category-card {
  background: #f7f8fc;
  border-radius: 14px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  transition: transform 0.12s ease, box-shadow 0.12s ease;
}

.category-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 14px rgba(15, 23, 42, 0.08);
}

.category-thumb {
  height: 90px;
}

.category-body {
  padding: 10px 10px 12px;
}

.category-body h3 {
  margin: 0 0 2px;
  font-size: 13px;
}

.category-body p {
  margin: 0 0 4px;
  font-size: 11px;
  color: #777;
}

.category-hint {
  font-size: 11px;
  color: #ff6b3d;
}

/* Best seller */

.section-title-center {
  text-align: center;
  font-size: 16px;
  margin: 0 0 14px;
}

.best-seller-grid {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 12px;
}

.best-card {
  background: #fff;
  border-radius: 16px;
  border: 1px solid #ffe1d3;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}

.best-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.06);
}

.best-thumb {
  position: relative;
  height: 100px;
}

.discount-badge {
  position: absolute;
  top: 8px;
  right: 8px;
  background: #ff4b4b;
  color: #fff;
  font-size: 11px;
  padding: 3px 8px;
  border-radius: 999px;
}

.best-body {
  padding: 10px 10px 12px;
}

.best-body h3 {
  margin: 0 0 4px;
  font-size: 13px;
}

.best-tagline {
  margin: 0 0 8px;
  font-size: 11px;
  color: #777;
}

.label-row {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  margin-bottom: 6px;
}

.pill {
  font-size: 10px;
  padding: 2px 6px;
  border-radius: 999px;
  background: #ffe9e0;
  color: #ff6b3d;
}

.price-row .price {
  font-weight: 700;
  font-size: 13px;
  color: #ff4b4b;
}

/* Responsive sederhana */

@media (max-width: 1024px) {
  .hero-section {
    grid-template-columns: 1fr;
  }

  .category-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .best-seller-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>
