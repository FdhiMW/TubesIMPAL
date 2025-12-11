<template>
  <div class="admin-page">


    <main class="page-content">
      <header class="page-header">
        <h1>Katalog Produk (Admin)</h1>
        <p class="subtitle">
          Ketik kata kunci pada search bar di atas untuk mencari produk.
        </p>
      </header>

      <section class="product-grid">
        <article
          v-for="p in products"
          :key="p.idProduk"
          class="product-card"
        >
          <div class="thumb">
            <!-- TANPA TEKS 'Tidak ada gambar' -->
            <img
              v-if="p.imageUrl"
              :src="p.imageUrl"
              :alt="p.namaProduk"
            />
            <div v-else class="thumb-empty"></div>
          </div>

          <div class="body">
            <p class="name">{{ p.namaProduk }}</p>
            <p class="meta">
              {{ p.merek || '-' }} · Stok: {{ p.stok || 0 }}
            </p>
            <p class="price">Rp{{ formatPrice(p.harga) }}</p>
          </div>
        </article>

        <p v-if="!loading && products.length === 0" class="empty-text">
          Tidak ada produk yang ditemukan.
        </p>
      </section>
    </main>
  </div>
</template>

<script>
import NavbarAdmin from '@/components/layout/NavbarAdmin.vue'
import { searchProduk } from '@/api/produkApi'

export default {
  name: 'AdminProductListView',
  components: { NavbarAdmin },

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
        const res = await searchProduk(q || null, null)
        this.products = Array.isArray(res.data) ? res.data : []
      } catch (err) {
        console.error('Gagal memuat katalog produk admin', err)
        this.products = []
      } finally {
        this.loading = false
      }
    },

    formatPrice(value) {
      if (value == null) return '0'
      return Number(value).toLocaleString('id-ID')
    },
  },
}
</script>

<style scoped>
.admin-page {
  min-height: 100vh;
  background: #f5f7fb;
}

.page-content {
  padding: 24px 32px;
}

.page-header h1 {
  margin: 0;
  font-size: 20px;
}

.subtitle {
  margin: 4px 0 20px;
  font-size: 13px;
  color: #6b7280;
}

/* grid kartu produk */
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(210px, 1fr));
  gap: 16px;
}

.product-card {
  background: #ffffff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 25px rgba(15, 23, 42, 0.06);
  display: flex;
  flex-direction: column;
}

.thumb {
  height: 110px;
  background: linear-gradient(135deg, #e5f0ff, #f3f4ff);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.thumb-empty {
  width: 100%;
  height: 100%;
}

.body {
  padding: 10px 12px 12px;
}

.name {
  margin: 0 0 4px;
  font-size: 13px;
  font-weight: 600;
}

.meta {
  margin: 0 0 6px;
  font-size: 11px;
  color: #6b7280;
}

.price {
  margin: 0;
  font-size: 13px;
  font-weight: 700;
  color: #2563eb;
}

.empty-text {
  grid-column: 1 / -1;
  font-size: 13px;
  color: #9ca3af;
}
</style>
