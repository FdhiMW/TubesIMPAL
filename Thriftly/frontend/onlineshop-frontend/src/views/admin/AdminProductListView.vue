<template>
  <div class="admin-page">
    <main class="page-content">
      <div class="header-row">
        <div class="header-left">
          <h1 class="page-title">Katalog Produk</h1>
        </div>

        <!-- ====== [SISIPKAN: BUTTON TAMBAH BARANG DI POJOK KANAN ATAS] ====== -->
        <button class="primary-btn" @click="goToCreateProduct">
          + Tambah Barang
        </button>
        <!-- ====== [AKHIR SISIPAN] ====== -->
      </div>

      <section class="grid">
        <div v-for="p in products" :key="p.idProduk" class="card">
          <!-- ====== [SISIPKAN: BUTTON MINUS UNTUK HAPUS PRODUK DI TIAP KOTAK] ====== -->
          <button
            class="minus-btn"
            :disabled="deletingId === p.idProduk"
            @click.stop="confirmDeleteCard(p.idProduk, p.namaProduk)"
            title="Hapus produk"
          >
            {{ deletingId === p.idProduk ? '…' : '−' }}
          </button>
          <!-- ====== [AKHIR SISIPAN] ====== -->

          <div class="thumb">
            <img
              v-if="p.imageUrl"
              :src="p.imageUrl"
              alt="Foto produk"
              class="thumb-img"
            />
          </div>

          <div class="card-body">
            <p class="name">{{ p.namaProduk }}</p>
            <p class="meta">{{ (p.merek || '-') }} · Stok: {{ p.stok || 0 }}</p>
            <p class="price">Rp{{ formatPrice(p.harga) }}</p>
          </div>
        </div>

        <div v-if="!loading && products.length === 0" class="empty">
          Tidak ada produk.
        </div>
      </section>
    </main>
  </div>
</template>

<script>
import { searchProduk, deleteProduk } from '@/api/produkApi'

export default {
  name: 'AdminProductListView',
  data() {
    return {
      products: [],
      loading: false,
      deletingId: null,
    }
  },
  methods: {
    formatPrice(value) {
      return Number(value || 0).toLocaleString('id-ID')
    },

    // ====== [SISIPKAN: NAVIGASI KE FORM TAMBAH BARANG] ======
    goToCreateProduct() {
      this.$router.push({ name: 'admin-product-create' })
    },
    // ====== [AKHIR SISIPAN] ======

    async loadProducts() {
      this.loading = true
      try {
        // katalog admin: ambil semua (tanpa keyword)
        const res = await searchProduk(null, null)
        this.products = Array.isArray(res.data) ? res.data : []
      } catch (e) {
        console.error('Gagal memuat katalog admin', e)
        this.products = []
      } finally {
        this.loading = false
      }
    },

    // ====== [SISIPKAN: HAPUS DARI CARD (BUTTON MINUS)] ======
    async confirmDeleteCard(idProduk, namaProduk) {
      if (!window.confirm(`Yakin ingin menghapus produk: ${namaProduk || idProduk}?`)) return
      this.deletingId = idProduk
      try {
        await deleteProduk(idProduk)
        this.products = this.products.filter((x) => x.idProduk !== idProduk)
      } catch (e) {
        console.error('Gagal menghapus produk (card)', e)
        alert('Gagal menghapus produk, cek console.')
      } finally {
        this.deletingId = null
      }
    },
    // ====== [AKHIR SISIPAN] ======
  },
  mounted() {
    this.loadProducts()
  },
}
</script>

<style scoped>
.admin-page {
  min-height: 100vh;
  background: #f5f7fb;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  color: #222;
}

.page-content {
  padding: 20px 32px 32px;
}

.header-row {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 14px;
}

.page-title {
  margin: 0;
  font-size: 40px;
  font-weight: 700;
}

.page-subtitle {
  margin: 4px 0 0;
  font-size: 12px;
  color: #6b7280;
}

/* button style samakan dengan dashboard */
.primary-btn {
  border-radius: 999px;
  border: none;
  background: linear-gradient(135deg, #ff5a3c, #ff9f1c);
  padding: 8px 16px;
  color: #fff;
  font-size: 12px;
  cursor: pointer;
  white-space: nowrap;
}

.grid {
  display: grid;
  grid-template-columns: repeat(9, minmax(0, 1fr));
  gap: 14px;
  align-items: start;
}

.card {
  position: relative;
  background: #fff;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 10px 22px rgba(15, 23, 42, 0.06);
}

.thumb {
  height: 86px;
  background: linear-gradient(135deg, #eef3ff, #f6f8ff);
}

.thumb-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-body {
  padding: 10px 10px 12px;
}

.name {
  margin: 0 0 4px;
  font-size: 11px;
  font-weight: 700;
  line-height: 1.2;
}

.meta {
  margin: 0 0 6px;
  font-size: 10px;
  color: #6b7280;
}

.price {
  margin: 0;
  font-size: 11px;
  font-weight: 700;
  color: #2563eb;
}

/* ====== [PINDAH POSISI BUTTON MINUS KE LINGKARAN MERAH] ====== */
.minus-btn {
  position: absolute;

  /* sebelumnya: top/right. sekarang: kanan bawah (area putih) */
  top: auto;
  right: 10px;
  bottom: 12px;

  width: 22px;
  height: 22px;
  border-radius: 999px;
  border: none;
  background: rgba(230, 57, 57, 0.95);
  color: #fff;
  font-size: 16px;
  line-height: 22px;
  cursor: pointer;
  display: grid;
  place-items: center;
  z-index: 3;
}

.minus-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
/* ====== [AKHIR PINDAH] ====== */

.empty {
  grid-column: 1 / -1;
  color: #9ca3af;
  font-size: 12px;
  text-align: center;
  padding: 18px 0;
}

/* responsif biar tidak kepotong */
@media (max-width: 1400px) {
  .grid { grid-template-columns: repeat(6, minmax(0, 1fr)); }
}
@media (max-width: 1024px) {
  .grid { grid-template-columns: repeat(3, minmax(0, 1fr)); }
  .header-row { flex-direction: column; align-items: stretch; }
}
</style>
