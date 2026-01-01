<template>
  <div class="product-list-page">
    <main class="page-content">
      <header class="page-header">
        <h1>Hasil Pencarian</h1>

        <p v-if="keyword" class="subtitle">
          Menampilkan produk untuk: "<strong>{{ keyword }}</strong>"
        </p>
        <p v-else-if="kategori" class="subtitle">
          Menampilkan produk kategori: "<strong>{{ kategori }}</strong>"
        </p>
        <p v-else class="subtitle">
          Gunakan kolom pencarian atau pilih kategori di beranda.
        </p>
      </header>

      <div v-if="loading" class="state">
        Memuat produk...
      </div>

      <div v-else>
        <div v-if="products.length === 0" class="state">
          Tidak ada produk yang cocok.
        </div>

        <div v-else class="product-grid">
          <article
            v-for="p in products"
            :key="p.idProduk"
            class="product-card"
            @click="goToDetail(p.idProduk)"
          >
            <div class="thumb">
              <img
                v-if="p && p.imageUrl"
                :src="resolveImageUrl(p.imageUrl)"
                class="thumb-img"
                alt="Foto produk"
              />
            </div>
            <div class="body">
              <h3 class="title">{{ p.namaProduk }}</h3>
              <p class="meta">
                {{ p.merek || 'Tanpa merek' }} • {{ p.ukuran || '-' }}
              </p>
              <p class="price">Rp{{ formatPrice(p.harga) }}</p>
            </div>
          </article>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { searchProduk } from '@/api/produkApi'

export default {
  name: 'ProductListView',
  data() {
    return {
      products: [],
      loading: false,
      keyword: '',
      kategori: '',
    }
  },
  created() {
    this.syncFromRoute()
  },
  watch: {
    '$route.query.q'() {
      this.syncFromRoute()
    },
    '$route.query.kategori'() {
      this.syncFromRoute()
    },
  },
  methods: {
    syncFromRoute() {
      this.keyword = this.$route.query.q || ''
      this.kategori = this.$route.query.kategori || ''
      this.loadProducts()
    },
    async loadProducts() {
      const q = this.keyword ? this.keyword.trim() : ''
      const cat = this.kategori ? this.kategori.trim() : ''

      if (!q && !cat) {
        this.products = []
        return
      }

      this.loading = true
      try {
        const res = await searchProduk(q, cat)
        this.products = Array.isArray(res.data) ? res.data : []
      } catch (err) {
        console.error('Gagal memuat produk pencarian', err)
        this.products = []
      } finally {
        this.loading = false
      }
    },
    formatPrice(value) {
      if (value == null) return ''
      return Number(value).toLocaleString('id-ID')
    },
    goToDetail(id) {
      this.$router.push(`/produk/${id}`)
    },
    resolveImageUrl(url) {
      const API = "http://localhost:8080";

      if (!url) return "";
      if (url.startsWith("http")) return url;
      if (url.startsWith("/uploads/")) return API + url;
      if (url.startsWith("/foto-barang/")) return url;

      return API + "/uploads/" + url;
    },
  },
}
</script>

<style scoped>
.product-list-page {
  padding: 24px 32px;
}

.page-header h1 {
  margin: 0 0 4px;
  font-size: 20px;
}

.subtitle {
  margin: 0 0 16px;
  color: #6b7280;
  font-size: 13px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 16px;
}

.product-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(15, 23, 42, 0.06);
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.12s ease, box-shadow 0.12s ease;
}

.product-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 18px rgba(15, 23, 42, 0.12);
}

.thumb {
  width: 100%;
  height: 180px;
  background: #f3f6ff;
  overflow: hidden;
  border-top-left-radius: 18px;
  border-top-right-radius: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.thumb-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
  display: block;
}

.thumb-placeholder {
  width: 100%;
  height: 100%;
  display: grid;
  place-items: center;
  font-weight: 700;
  color: #6b7280;
  font-size: 13px;
}

.body {
  padding: 10px 12px 12px;
}

.title {
  margin: 0 0 4px;
  font-size: 14px;
  font-weight: 600;
}

.meta {
  margin: 0 0 6px;
  font-size: 12px;
  color: #6b7280;
}

.price {
  margin: 0;
  font-weight: 700;
  color: #ef4444;
}

.state {
  margin-top: 24px;
  font-size: 14px;
  color: #6b7280;
}
</style>
