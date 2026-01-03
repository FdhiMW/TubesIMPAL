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
              v-if="p && p.imageUrl"
              :src="resolveImageUrl(p.imageUrl)"
              class="thumb-img"
              alt="Foto produk"
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

    <!-- =========================
         [DITAMBAHKAN] POPUP KONFIRMASI HAPUS (MENARIK)
         ========================= -->
    <ConfirmModal
      :show="showDeleteConfirm"
      title="Hapus Produk?"
      :message="`Yakin ingin menghapus produk: ${pendingDeleteName || 'produk ini'}?`"
      okText="Ya"
      cancelText="Tidak"
      @confirm="onDeleteConfirm"
      @cancel="onDeleteCancel"
    />

    <!-- =========================
         [DITAMBAHKAN] POPUP SUKSES HAPUS (MIRIP CONTOH)
         ========================= -->
    <SuccessModal
      :show="showDeleteSuccess"
      title="Produk berhasil dihapus"
      buttonText="OK"
      @close="showDeleteSuccess = false"
    />
  </div>
</template>

<script>
import { searchProduk, deleteProduk } from '@/api/produkApi'

/* ===== [DITAMBAHKAN] modal components ===== */
import ConfirmModal from '@/components/confirmmodal/ConfirmModal.vue'
import SuccessModal from '@/components/confirmmodal/SuccessModal.vue'

export default {
  name: 'AdminProductListView',
  components: { ConfirmModal, SuccessModal },

  data() {
    return {
      products: [],
      loading: false,
      deletingId: null,

      // ✅ [DITAMBAHKAN] simpan keyword dari navbar (query ?q=)
      searchKeyword: '',

      /* ✅ [DITAMBAHKAN] state untuk popup hapus */
      showDeleteConfirm: false,
      pendingDeleteId: null,
      pendingDeleteName: '',

      /* ✅ [DITAMBAHKAN] state popup sukses */
      showDeleteSuccess: false,
    }
  },

  created() {
    // ✅ ambil keyword dari URL saat pertama kali masuk/refresh
    const q = this.$route?.query?.q
    this.searchKeyword = q != null ? String(q).trim() : ''
  },

  watch: {
    // ✅ kalau navbar search saat sudah di katalog -> reload data
    '$route.query.q'(val) {
      this.searchKeyword = val != null ? String(val).trim() : ''
      this.loadProducts()
    },
  },

  methods: {
    formatPrice(value) {
      return Number(value || 0).toLocaleString('id-ID')
    },

    // ====== [SISIPKAN: NAVIGASI KE FORM TAMBAH BARANG] ======
    goToCreateProduct() {
      this.$router.push({ name: 'admin-product-create' }).catch((err) => {
        if (err && err.name === 'NavigationDuplicated') return
        const msg = err?.message ? String(err.message) : ''
        if (msg.includes('Avoided redundant navigation')) return
        throw err
      })
    },
    // ====== [AKHIR SISIPAN] ======

    async loadProducts() {
      this.loading = true
      try {
        const keyword = (this.searchKeyword || '').trim()
        const res = await searchProduk(keyword || null, null)
        this.products = Array.isArray(res.data) ? res.data : []
      } catch (e) {
        console.error('Gagal memuat katalog admin', e)
        this.products = []
      } finally {
        this.loading = false
      }
    },

    // ====== [DISESUAIKAN] HAPUS DARI CARD -> BUKA MODAL (bukan window.confirm) ======
    confirmDeleteCard(idProduk, namaProduk) {
      this.pendingDeleteId = idProduk
      this.pendingDeleteName = namaProduk || String(idProduk)
      this.showDeleteConfirm = true
    },

    onDeleteCancel() {
      this.showDeleteConfirm = false
      this.pendingDeleteId = null
      this.pendingDeleteName = ''
    },

    async onDeleteConfirm() {
      const idProduk = this.pendingDeleteId
      if (!idProduk) {
        this.onDeleteCancel()
        return
      }

      this.showDeleteConfirm = false
      this.deletingId = idProduk

      try {
        await deleteProduk(idProduk)
        this.products = this.products.filter((x) => x.idProduk !== idProduk)

        this.showDeleteSuccess = true
      } catch (e) {
        console.error('Gagal menghapus produk (card)', e)
        alert('Gagal menghapus produk, cek console.')
      } finally {
        this.deletingId = null
        this.pendingDeleteId = null
        this.pendingDeleteName = ''
      }
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

/* ====== button tambah barang ====== */
.primary-btn {
  border-radius: 999px;
  border: none;
  background: linear-gradient(135deg, #ff5a3c, #ff9f1c);
  padding: 12px 22px;
  color: #fff;
  font-size: 14px;
  font-weight: 800;
  cursor: pointer;
  white-space: nowrap;
}

/* ====== grid ====== */
.grid {
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 18px;
  align-items: start;
}

.card {
  position: relative;
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 22px rgba(15, 23, 42, 0.06);
}

/* ====== gambar katalog ====== */
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

/* ====== body ====== */
.card-body {
  padding: 14px 14px 18px;
}

.name {
  margin: 0 0 8px;
  font-size: 18px;
  font-weight: 900;
  line-height: 1.25;
}

.meta {
  margin: 0 0 12px;
  font-size: 14px;
  font-weight: 700;
  color: #6b7280;
  line-height: 1.35;
}

.price {
  margin: 0;
  font-size: 18px;
  font-weight: 950;
  color: #2563eb;
}

/* ====== button minus ====== */
.minus-btn {
  position: absolute;
  top: auto;
  right: 12px;
  bottom: 14px;

  width: 30px;
  height: 30px;
  border-radius: 999px;
  border: none;
  background: rgba(230, 57, 57, 0.95);
  color: #fff;
  font-size: 22px;
  line-height: 30px;
  cursor: pointer;
  display: grid;
  place-items: center;
  z-index: 3;
  box-shadow: 0 8px 16px rgba(230, 57, 57, 0.25);
}

.minus-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.empty {
  grid-column: 1 / -1;
  color: #9ca3af;
  font-size: 12px;
  text-align: center;
  padding: 18px 0;
}

/* responsif */
@media (max-width: 1600px) {
  .grid { grid-template-columns: repeat(4, minmax(0, 1fr)); }
}
@media (max-width: 1400px) {
  .grid { grid-template-columns: repeat(3, minmax(0, 1fr)); }
}
@media (max-width: 1024px) {
  .grid { grid-template-columns: repeat(2, minmax(0, 1fr)); }
  .header-row { flex-direction: column; align-items: stretch; }
}
@media (max-width: 560px) {
  .grid { grid-template-columns: 1fr; }
}
</style>
