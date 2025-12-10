<template>
  <div class="admin-page">
    <main class="page-content">
      <header class="page-header">
        <h1>Katalog Produk (Admin)</h1>
        <p v-if="keyword" class="subtitle">
          Hasil pencarian untuk: "<strong>{{ keyword }}</strong>"
        </p>
        <p v-else class="subtitle">
          Ketik kata kunci pada search bar di atas untuk mencari produk.
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
          >
            <div class="thumb"></div>
            <div class="body">
              <h3 class="title">{{ p.namaProduk }}</h3>
              <p class="meta">
                {{ p.merek || 'Tanpa merek' }} • Stok: {{ p.stok || 0 }}
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
  name: 'AdminProductListView',
  data() {
    return {
      products: [],
      loading: false,
      keyword: '',
    }
  },
  created() {
    this.syncFromRoute()
  },
  watch: {
    '$route.query.q'() {
      this.syncFromRoute()
    },
  },
  methods: {
    syncFromRoute() {
      this.keyword = this.$route.query.q || ''
      this.loadProducts()
    },

    async loadProducts() {
      const q = this.keyword ? this.keyword.trim() : ''

      this.loading = true
      try {
        // jika q kosong, backend akan mengembalikan semua produk
        const res = await searchProduk(q, null)
        this.products = Array.isArray(res.data) ? res.data : []
      } catch (err) {
        console.error('Gagal memuat produk pencarian admin', err)
        this.products = []
      } finally {
        this.loading = false
      }
    },

    formatPrice(value) {
      if (value == null) return ''
      return Number(value).toLocaleString('id-ID')
    },
  },
}
</script>

<style scoped>
.admin-page {
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
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 16px;
}

.product-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(15, 23, 42, 0.06);
  overflow: hidden;
  cursor: default; /* TIDAK bisa diklik ke detail */
}

.thumb {
  height: 120px;
  background: linear-gradient(135deg, #e0f2fe, #f1f5f9);
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
  color: #0ea5e9;
}

.state {
  margin-top: 24px;
  font-size: 14px;
  color: #6b7280;
}
</style>
