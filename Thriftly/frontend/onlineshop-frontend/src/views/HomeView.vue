<!-- frontend/onlineshop-frontend/src/views/HomeView.vue -->
<template>
  <div class="user-home">
    <main class="page-content">
      <!-- HERO + BANNER -->
      <section class="hero-section">
        <div class="hero-main"></div>
        <div class="hero-side">
          <div class="hero-side-item1"></div>
          <div class="hero-side-item2"></div>
        </div>
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
        </div>

        <div class="category-grid">
          <article
            v-for="item in categoryItems"
            :key="item.id"
            class="category-card"
            @click="goToCategory(item)"
          >
            <!-- Banner stylish tanpa gambar -->
            <div class="category-cover" :class="`cover-${item.keySlug}`">
              <div class="category-badge">{{ item.badge }}</div>
              <div class="category-icon">{{ item.icon }}</div>
              <div class="category-cover-line"></div>
            </div>

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
            <div class="best-thumb">
              <img
                v-if="item && item.imageUrl"
                :src="resolveImageUrl(item.imageUrl)"
                class="thumb-img"
                alt="Foto produk"
              />
              <span class="discount-badge">-{{ item.discount }}%</span>
            </div>

            <div class="best-body">
              <h3>{{ item.title }}</h3>
              <p class="best-tagline">{{ item.tagline }}</p>

              <div class="label-row">
                <span v-for="label in item.labels" :key="label" class="pill">
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

// Nama harus sama dengan nama_kategori di DB (Kaos, Kemeja, Jaket, Celana, Dress)
const CATEGORY_UI_CONFIG = {
  Kaos: {
    subtitle: 'Oversize & graphic tee',
    hint: 'Cocok buat OOTD',
    icon: '👕',
    badge: 'Trending',
  },
  Kemeja: {
    subtitle: 'Casual sampai formal',
    hint: 'Look santai & rapi',
    icon: '👔',
    badge: 'Smart',
  },
  Jaket: {
    subtitle: 'Denim, bomber, varsity',
    hint: 'Outer andalan',
    icon: '🧥',
    badge: 'Hangat',
  },
  Celana: {
    subtitle: 'Jeans, chino, cargo',
    hint: 'Mix & match mudah',
    icon: '👖',
    badge: 'Basic',
  },
  Dress: {
    subtitle: 'Casual sampai kondangan',
    hint: 'Feminine style',
    icon: '👗',
    badge: 'Favorite',
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
    // dipakai untuk gambar barang terlaris
    resolveImageUrl(url) {
      const API = 'http://localhost:8080'
      if (!url) return ''
      if (url.startsWith('http')) return url
      if (url.startsWith('/uploads/')) return API + url
      return API + '/uploads/' + url
    },

    formatPrice(value) {
      if (!value && value !== 0) return ''
      return Number(value).toLocaleString('id-ID')
    },

    goToProductDetail(id) {
      this.$router.push(`/produk/${id}`)
    },

    // ==== KATEGORI (TANPA GAMBAR) ====
    async loadCategories() {
      try {
        const res = await http.get('/kategori')
        const list = Array.isArray(res.data) ? res.data : []

        this.categoryItems = list.map((k, idx) => {
          const ui = CATEGORY_UI_CONFIG[k.namaKategori] || {}
          const keySlug = String(k.namaKategori || '')
            .trim()
            .toLowerCase()
            .replace(/\s+/g, '-') // misal "Kaos Oversize" -> "kaos-oversize"

          return {
            id: k.idKategori || idx + 1,
            key: k.namaKategori,
            keySlug,
            title: k.namaKategori,
            subtitle: ui.subtitle || k.deskripsi || '',
            hint: ui.hint || 'Lihat koleksi',
            icon: ui.icon || '🧵',
            badge: ui.badge || 'Kategori',
          }
        })

        this.chips = this.categoryItems.map((c) => c.title)
      } catch (err) {
        console.error('Gagal memuat kategori, pakai dummy:', err)

        const fallback = [
          { id: 1, key: 'Kemeja', title: 'Kemeja', subtitle: 'Casual sampai formal', hint: 'Look santai & rapi', icon: '👔', badge: 'Smart' },
          { id: 2, key: 'Kaos', title: 'Kaos', subtitle: 'Oversize & graphic tee', hint: 'Cocok buat OOTD', icon: '👕', badge: 'Trending' },
          { id: 3, key: 'Jaket', title: 'Jaket', subtitle: 'Denim, bomber, varsity', hint: 'Outer andalan', icon: '🧥', badge: 'Hangat' },
          { id: 4, key: 'Celana', title: 'Celana', subtitle: 'Jeans, chino, cargo', hint: 'Mix & match mudah', icon: '👖', badge: 'Basic' },
          { id: 5, key: 'Dress', title: 'Dress', subtitle: 'Casual sampai kondangan', hint: 'Feminine style', icon: '👗', badge: 'Favorite' },
        ]

        this.categoryItems = fallback.map((f) => ({
          ...f,
          keySlug: String(f.key).trim().toLowerCase().replace(/\s+/g, '-'),
        }))
        this.chips = this.categoryItems.map((c) => c.title)
      }
    },

    goToCategory(item) {
      this.$router.push({
        name: 'product-search',
        query: { kategori: item.key },
      })
    },

    // ==== BARANG TERLARIS ====
    async loadBestSellers() {
      try {
        const res = await http.get('/produk/terlaris?limit=6')
        const raw = res.data
        const list = Array.isArray(raw)
          ? raw
          : Array.isArray(raw?.content)
            ? raw.content
            : []

        this.bestSellers = list.map((p) => ({
          id: p.idProduk,
          title: p.namaProduk,
          tagline: `${p.totalTerjual ?? 0} terjual`,
          labels: ['Best Seller'],
          price: p.harga,
          discount: 30,
          imageUrl: p.imageUrl || '',
        }))
      } catch (e) {
        console.error('Gagal load barang terlaris:', e)
        this.bestSellers = []
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
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  color: #222;
}

.page-content {
  padding: 20px 40px 40px;
}

/* Hero */
.hero-section {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 1px;
  margin-bottom: 20px;
}
.hero-main{
  height: 180px;
  border-radius: 18px;
  overflow: hidden;
  background-image: url("~@/assets/foto/Banner1.jpg");
  background-size: cover;
  background-position: center 30%;
  background-repeat: no-repeat;
}
.hero-side { 
  grid-template-rows: 1fr 1fr; 
  gap: -10px;
}
.hero-side-item1 { 
  height: 79px;
  border-radius: 18px; 
  background-image: url("~@/assets/foto/Banner2.png");
  background-size: cover;
  background-position: center 50%;
  background-repeat: no-repeat;
}
.hero-side-item2 { 
  height: 122px;
  border-radius: 18px;
  background-image: url("~@/assets/foto/Banner3.png"); 
  background-size: cover;
  background-position: center 30%;
  background-repeat: no-repeat;
}

/* Skeleton */
.skeleton { background: linear-gradient(135deg, #ffe0cf, #ffd4bf); }

/* Sections */
.section-block {
  background: #fff;
  border-radius: 20px;
  padding: 18px 20px 22px;
  margin-bottom: 20px;
}
.section-header { display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 16px; }
.section-header h2 { margin: 0; font-size: 18px; }
.section-subtitle { margin: 4px 0 0; font-size: 12px; color: #777; }

/* Category cards */
.category-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
}

.category-card {
  background: #f7f8fc;
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.12s ease, box-shadow 0.12s ease;
  border: 1px solid rgba(255, 203, 186, 0.6);
}

.category-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(15, 23, 42, 0.10);
}

/* Stylish cover (no image) */
.category-cover {
  position: relative;
  height: 120px;
  border-radius: 16px 16px 0 0;
  overflow: hidden;
  background: linear-gradient(135deg, #ffe0cf, #ffd4bf);
}

.category-cover-line {
  position: absolute;
  left: -30%;
  top: 55%;
  width: 160%;
  height: 46px;
  background: rgba(255, 255, 255, 0.22);
  transform: rotate(-6deg);
}

.category-icon {
  position: absolute;
  left: 12px;
  bottom: 10px;
  width: 44px;
  height: 44px;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.75);
  display: grid;
  place-items: center;
  font-size: 22px;
  box-shadow: 0 10px 18px rgba(0,0,0,0.07);
}

.category-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 11px;
  padding: 4px 10px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.85);
  color: #ff6b3d;
  font-weight: 700;
}

/* Per-category gradient variants */
.cover-kemeja { background: linear-gradient(135deg, #ffd7c2, #ffe7dc); }
.cover-kaos   { background: linear-gradient(135deg, #ffe3b5, #ffd6c9); }
.cover-jaket  { background: linear-gradient(135deg, #ffd0e0, #ffd9c9); }
.cover-celana { background: linear-gradient(135deg, #ffd7c2, #ffefc9); }
.cover-dress  { background: linear-gradient(135deg, #ffd0c9, #ffe0f3); }

.category-body { padding: 10px 10px 12px; }
.category-body h3 { margin: 0 0 2px; font-size: 13px; }
.category-body p { margin: 0 0 4px; font-size: 11px; color: #777; }
.category-hint { font-size: 11px; color: #ff6b3d; }

/* Best seller */
.section-title-center { text-align: center; font-size: 16px; margin: 0 0 14px; }

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
  height: 140px;
  overflow: hidden;
  border-radius: 18px 18px 0 0;
  background: #f3f4f6;
}

.thumb-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
  display: block;
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

.best-body { padding: 10px 10px 12px; }
.best-body h3 { margin: 0 0 4px; font-size: 13px; }
.best-tagline { margin: 0 0 8px; font-size: 11px; color: #777; }
.label-row { display: flex; flex-wrap: wrap; gap: 4px; margin-bottom: 6px; }

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
  .hero-section { grid-template-columns: 1fr; }
  .category-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); }
  .best-seller-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); }
}
</style>
