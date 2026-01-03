<template>
  <div class="admin-page">
    <main class="page-content">
      <!-- QUICK ACTIONS -->
      <section class="quick-actions">
        <div class="qa-card qa-search" @click="goToAdminProductList">
          <h3>Cari Barang</h3>
          <p>Telusuri dan filter stok produk.</p>
        </div>

        <!-- ====== [SISIPKAN: HIDE CARD TAMBAH/HAPUS DI DASHBOARD (STRUKTUR TETAP ADA)] ====== -->
        <div class="qa-card qa-manage" @click="goToCreateProduct" v-if="false">
          <h3>Tambah / Hapus Barang</h3>
          <p>Kelola katalog dengan cepat.</p>
        </div>
        <!-- ====== [AKHIR SISIPAN] ====== -->

        <div class="qa-card qa-orders" @click="goToOrders">
          <h3>Lihat Pesanan</h3>
          <p>Pantau status pesanan terbaru.</p>
        </div>
      </section>

      <!-- SUMMARY -->
      <section class="summary-row">
        <div class="summary-card">
          <p class="summary-label">Total Barang Aktif</p>
          <p class="summary-value">{{ formatPrice(totalBarangAktif) }}</p>
          <p class="summary-note">Produk yang tampil di beranda pengguna</p>
        </div>

        <div class="summary-card">
          <p class="summary-label">Pesanan Aktif</p>
          <p class="summary-value">{{ formatPrice(totalPesananAktif) }}</p>
          <p class="summary-note">Pesanan berstatus Dikemas / Dalam Perjalanan</p>
        </div>

        <div class="summary-card">
          <p class="summary-label">Pesanan Selesai</p>
          <p class="summary-value">{{ formatPrice(totalPesananSelesai) }}</p>
          <p class="summary-note">Pesanan berstatus Selesai</p>
        </div>
      </section>

      <!-- MAIN GRID -->
      <section class="main-grid">
        <!-- KELOLA BARANG -->
        <div class="inventory-card">
          <header class="inventory-header">
            <h2>Kelola Barang</h2>
            <div class="inventory-actions">
              <!-- ====== [SISIPKAN: HIDE BUTTON TAMBAH BARANG DI DASHBOARD] ====== -->
              <button class="primary-btn" @click="goToCreateProduct" v-if="false">
                Tambah Barang
              </button>
              <!-- ====== [AKHIR SISIPAN] ====== -->
            </div>
          </header>

          <div class="inventory-search">
            <input
              v-model="searchKeyword"
              @keyup.enter="loadInventory"
              type="text"
              class="search-input"
              placeholder="Nama barang, SKU, atau kategori..."
            />
            <button class="round-btn" @click="loadInventory">
              Cari
            </button>
          </div>

          <div class="inventory-table-wrapper">
            <table class="inventory-table">
              <thead>
                <tr>
                  <th>BARANG</th>
                  <th>KATEGORI</th>
                  <th>STOK</th>
                  <th>HARGA</th>
                  <th>STATUS</th>
                  <th>AKSI</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in inventory" :key="item.id">
                  <td>{{ item.name }}</td>
                  <td>{{ item.category }}</td>
                  <td>{{ item.stock }}</td>
                  <td>Rp{{ formatPrice(item.price) }}</td>
                  <td>
                    <span :class="['status-pill', item.statusClass]">
                      {{ item.statusLabel }}
                    </span>
                  </td>
                  <td>
                    <!-- ✅ EDIT DISEMBUNYIKAN (STRUKTUR TETAP ADA) -->
                    <button class="link-small" @click="editProduct(item.id)" v-if="false">
                      Edit
                    </button>

                    <!-- ✅ divider juga disembunyikan supaya tidak ada sisa garis -->
                    <span class="divider" v-if="false">|</span>

                    <!-- ✅ hanya Hapus yang tampil -->
                    <button
                      class="link-small danger"
                      :disabled="deletingId === item.id"
                      @click="confirmDelete(item.id)"
                    >
                      {{ deletingId === item.id ? 'Menghapus...' : 'Hapus' }}
                    </button>
                  </td>
                </tr>
                <tr v-if="!loading && inventory.length === 0">
                  <td colspan="6" style="text-align:center; font-size:12px; color:#9ca3af;">
                    Tidak ada produk yang ditemukan.
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- PESANAN TERBARU -->
        <aside class="orders-card">
          <header class="orders-header">
            <div>
              <h2>Pesanan Terbaru</h2>
              <p class="section-subtitle">Ringkasan 5 pesanan terakhir</p>
            </div>
            <button class="ghost-btn small" @click="goToOrders">
              Lihat Semua
            </button>
          </header>

          <ul class="orders-list">
            <li v-for="order in recentOrders" :key="order.id" class="order-item">
              <div class="order-main">
                <p class="order-code">{{ order.code }}</p>
                <p class="order-info">
                  {{ order.items }} barang · Rp{{ formatPrice(order.total) }}
                </p>
                <p class="order-meta">
                  oleh {{ order.customer }} · {{ order.time }}
                </p>
              </div>
              <span :class="['order-status', order.statusClass]">
                {{ order.statusLabel }}
              </span>
            </li>
          </ul>
        </aside>
      </section>
    </main>

    <!-- =========================
         [DITAMBAHKAN] POPUP KONFIRMASI HAPUS
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
      :title="deleteSuccessTitle"
      buttonText="OK"
      @close="showDeleteSuccess = false"
    />
  </div>
</template>

<script>
import { fetchAdminOrders } from '@/api/pesananApi'
import { searchProduk, deleteProduk, countProdukAktif } from '@/api/produkApi'
import {
  countPesananAktif,
  countPesananAktifNative,
  countPesananSelesai,
} from '@/api/adminDashboardApi'

import NavbarAdmin from '@/components/layout/NavbarAdmin.vue'

/* ===== [DITAMBAHKAN] modal components ===== */
import ConfirmModal from '@/components/confirmmodal/ConfirmModal.vue'
import SuccessModal from '@/components/confirmmodal/SuccessModal.vue'

/* ===== [DITAMBAHKAN] ambil kategori dari database ===== */
import http from '@/api/httpClient'

export default {
  name: 'AdminDashboardView',
  components: { NavbarAdmin, ConfirmModal, SuccessModal },
  data() {
    return {
      totalBarangAktif: 0,
      totalPesananAktif: 0,
      totalPesananSelesai: 0,

      searchKeyword: '',
      loading: false,
      deletingId: null,

      inventory: [
        {
          id: 1,
          name: 'Headphone Wireless A1',
          category: 'Elektronik',
          stock: 32,
          price: 450000,
          statusLabel: 'Aktif',
          statusClass: 'status-active',
        },
      ],
      recentOrders: [],

      /* ===== [DITAMBAHKAN] state untuk popup hapus ===== */
      showDeleteConfirm: false,
      pendingDeleteId: null,
      pendingDeleteName: '',

      /* ===== [DITAMBAHKAN] popup sukses hapus ===== */
      showDeleteSuccess: false,
      deleteSuccessTitle: 'Produk berhasil dihapus',

      /* ===== [DITAMBAHKAN] MAP kategori dari DB: { [idKategori]: namaKategori } ===== */
      kategoriMap: {},
    }
  },
  methods: {
    formatPrice(value) {
      return Number(value || 0).toLocaleString('id-ID')
    },

    /* ===== [DITAMBAHKAN] load kategori dari DB ===== */
    async loadKategoriMap() {
      try {
        // Endpoint diasumsikan: GET /kategori
        const res = await http.get('/kategori')
        const list = Array.isArray(res.data) ? res.data : []

        const map = {}
        for (const k of list) {
          const id =
            k.idKategori ?? k.id_kategori ?? k.id ?? k.kode ?? k.kodeKategori
          const nama =
            k.namaKategori ?? k.nama_kategori ?? k.nama ?? k.name ?? k.label

          if (id != null && nama) map[String(id)] = String(nama)
        }

        this.kategoriMap = map
      } catch (err) {
        console.error('[DASHBOARD] Gagal memuat kategori dari DB', err)
        this.kategoriMap = {}
      }
    },

    /* ===== [DITAMBAHKAN] ambil nama kategori produk dari data produk + kategoriMap ===== */
    getKategoriName(p) {
      const direct =
        p.kategoriName ??
        p.namaKategori ??
        p.kategori_nama ??
        p.kategoriNama ??
        p.nama_kategori
      if (direct) return String(direct)

      const objName =
        p.kategori?.namaKategori ??
        p.kategori?.nama_kategori ??
        p.kategori?.name ??
        p.kategori?.nama
      if (objName) return String(objName)

      const id =
        p.idKategori ??
        p.id_kategori ??
        p.kategoriId ??
        p.kategori_id ??
        p.kategori?.idKategori ??
        p.kategori?.id_kategori

      if (id != null && this.kategoriMap[String(id)]) {
        return this.kategoriMap[String(id)]
      }

      return '-'
    },

    async loadTotalBarangAktif() {
      try {
        const res = await countProdukAktif()
        this.totalBarangAktif = Number(res.data || 0)
      } catch (err) {
        console.error('Gagal memuat total barang aktif', err)
        this.totalBarangAktif = 0
      }
    },

    async loadTotalPesananAktif() {
      try {
        let val = 0

        try {
          const res = await countPesananAktif()
          val = Number(res.data || 0)
        } catch (e1) {
          console.error('[DASHBOARD] countPesananAktif gagal:', e1)
        }

        if (val === 0) {
          try {
            const res2 = await countPesananAktifNative()
            val = Number(res2.data || 0)
          } catch (e2) {
            console.error('[DASHBOARD] countPesananAktifNative gagal:', e2)
          }
        }

        if (val === 0) {
          try {
            const res3 = await fetchAdminOrders()
            const list = Array.isArray(res3.data) ? res3.data : []
            val = list.filter((p) => {
              const s = (p.statusPesanan || '').toString().trim().toUpperCase()
              return s === 'DIKEMAS' || s === 'DALAM_PERJALANAN'
            }).length
          } catch (e3) {
            console.error('[DASHBOARD] hitung manual pesanan aktif gagal:', e3)
          }
        }

        this.totalPesananAktif = Number(val || 0)
      } catch (err) {
        console.error('Gagal memuat total pesanan aktif', err)
        this.totalPesananAktif = 0
      }
    },

    async loadTotalPesananSelesai() {
      try {
        let val = 0

        try {
          const res = await countPesananSelesai()
          val = Number(res.data || 0)
        } catch (e1) {
          console.error('[DASHBOARD] countPesananSelesai gagal:', e1)
        }

        if (val === 0) {
          try {
            const res2 = await fetchAdminOrders()
            const list = Array.isArray(res2.data) ? res2.data : []
            val = list.filter((p) => {
              const s = (p.statusPesanan || '').toString().trim().toUpperCase()
              return s === 'SELESAI'
            }).length
          } catch (e2) {
            console.error('[DASHBOARD] hitung manual pesanan selesai gagal:', e2)
          }
        }

        this.totalPesananSelesai = Number(val || 0)
      } catch (err) {
        console.error('Gagal memuat total pesanan selesai', err)
        this.totalPesananSelesai = 0
      }
    },

    goToOrders() {
      if (this.$route.path !== '/admin/pesanan') {
        this.$router.push('/admin/pesanan')
      }
    },

    // ✅ FIX NavigationDuplicated TANPA MENGUBAH STRUKTUR
    goToAdminProductList() {
      const currentName = this.$route?.name
      const currentPath = this.$route?.path

      // kalau sudah berada di halaman list, jangan push lagi
      if (currentName === 'admin-product-list' || currentPath === '/admin/produk') {
        return
      }

      // push + catch untuk Vue Router 3
      this.$router.push({ name: 'admin-product-list' }).catch((err) => {
        if (err && err.name === 'NavigationDuplicated') return
        throw err
      })
    },

    goToCreateProduct() {
      this.$router.push({ name: 'admin-product-create' })
    },

    async loadInventory() {
      this.loading = true
      try {
        const keyword = this.searchKeyword && this.searchKeyword.trim()
        const res = await searchProduk(keyword || null, null)
        const list = Array.isArray(res.data) ? res.data : []

        this.inventory = list.map((p) => {
          const stok = p.stok || 0
          const statusAktif = stok > 0

          return {
            id: p.idProduk,
            name: p.namaProduk,
            category: this.getKategoriName(p),
            stock: stok,
            price: p.harga || 0,
            statusLabel: statusAktif ? 'Aktif' : 'Stok Habis',
            statusClass: statusAktif ? 'status-active' : 'status-out',
          }
        })
      } catch (err) {
        console.error('Gagal memuat inventory admin', err)
        this.inventory = []
      } finally {
        this.loading = false
      }
    },

    // ✅ method edit tetap ada (struktur tidak dihilangkan), tapi tombolnya sudah disembunyikan
    editProduct(id) {
      this.$router.push({
        name: 'admin-product-create',
        query: { id },
      })
    },

    async confirmDelete(id) {
      const found = this.inventory.find((p) => p.id === id)
      this.pendingDeleteId = id
      this.pendingDeleteName = found ? found.name : ''
      this.showDeleteConfirm = true
    },

    onDeleteCancel() {
      this.showDeleteConfirm = false
      this.pendingDeleteId = null
      this.pendingDeleteName = ''
    },

    async onDeleteConfirm() {
      const id = this.pendingDeleteId
      if (!id) {
        this.onDeleteCancel()
        return
      }

      this.showDeleteConfirm = false

      this.deletingId = id
      try {
        await deleteProduk(id)
        this.inventory = this.inventory.filter((p) => p.id !== id)

        this.deleteSuccessTitle = 'Produk berhasil dihapus'
        this.showDeleteSuccess = true
      } catch (err) {
        console.error('Gagal menghapus produk', err)
        alert('Gagal menghapus produk, cek console.')
      } finally {
        this.deletingId = null
        this.pendingDeleteId = null
        this.pendingDeleteName = ''
      }
    },

    mapStatusToLabel(status) {
      const map = {
        MENUNGGU_PEMBAYARAN: 'Menunggu Pembayaran',
        DIKEMAS: 'Dikemas',
        DALAM_PERJALANAN: 'Dalam Perjalanan',
        SELESAI: 'Selesai',
      }
      return map[status] || status || '-'
    },

    mapStatusToClass(status) {
      if (!status) return ''
      const s = status.toUpperCase()
      if (s === 'MENUNGGU_PEMBAYARAN') return 'status-pending'
      if (s === 'DIKEMAS') return 'status-processing'
      if (s === 'DALAM_PERJALANAN') return 'status-processing'
      if (s === 'SELESAI') return 'status-done'
      return ''
    },

    async loadRecentOrders() {
      try {
        const res = await fetchAdminOrders()
        const list = Array.isArray(res.data) ? res.data.slice(0, 5) : []

        this.recentOrders = list.map((p, idx) => ({
          id: p.idPesanan || idx,
          code: p.kodePesanan || '-',
          items: p.totalBarang || 0,
          total: p.totalPembayaran || 0,
          customer: 'Pengguna',
          time: '',
          statusLabel: this.mapStatusToLabel(p.statusPesanan),
          statusClass: this.mapStatusToClass(p.statusPesanan),
        }))
      } catch (err) {
        console.error('Gagal memuat pesanan terbaru', err)
      }
    },

    async initDashboard() {
      await this.loadKategoriMap()
      await this.loadInventory()
      await this.loadRecentOrders()
      await this.loadTotalBarangAktif()
      await this.loadTotalPesananAktif()
      await this.loadTotalPesananSelesai()
    },
  },
  mounted() {
    this.initDashboard()
  },
}
</script>

<style scoped>
/* =========================
   BASE
   ========================= */
.admin-page {
  min-height: 100vh;
  background: #ffffff;
  font-family: system-ui,-apple-system,BlinkMacSystemFont,'Segoe UI',sans-serif;
  color:#222;
}

/* ✅ [DISESUAIKAN LAGI] biar area kotak merah makin “penuh kebawah”
   - padding bawah dinaikkan
   - base font naik
*/
.page-content {
  padding: 24px 32px 180px; /* 🔥 bawah lebih besar agar tidak kosong */
  min-height: calc(100vh - 84px);
  font-size: 16px;          /* 🔥 base font naik lagi */
}

/* ✅ [DISESUAIKAN LAGI] grid ikut penuh tinggi layar lebih maksimal */
.main-grid {
  display: grid;
  grid-template-columns: 2.2fr 1.2fr;
  gap: 20px;

  min-height: calc(100vh - 200px); /* 🔥 lebih besar supaya area bawah terpakai */
  align-items: stretch;
}

/* ✅ card full height */
.inventory-card,
.orders-card {
  height: 100%;
}

/* =========================
   QUICK ACTIONS (dibesarkan lagi)
   ========================= */
.quick-actions {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 18px;
  margin-bottom: 18px;
}

.qa-card {
  border-radius: 24px;
  padding: 24px 24px;     /* 🔥 lebih tinggi */
  color: #fff;
  font-size: 16px;        /* 🔥 font naik */
  cursor: pointer;
}

.qa-card h3 {
  margin: 0 0 8px;
  font-size: 18px;        /* 🔥 judul naik */
}

.qa-card p {
  margin: 0;
  font-size: 14px;        /* 🔥 desc naik */
  line-height: 1.35;
}

.qa-search { background: linear-gradient(135deg,#ff7750,#ffb48b); }
.qa-manage { background: linear-gradient(135deg,#ff5a3c,#ff9f1c); }
.qa-orders { background: linear-gradient(135deg,#ff8553,#ffc16d); }

/* =========================
   SUMMARY (dibesarkan lagi)
   ========================= */
.summary-row {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 18px;
  margin-bottom: 18px;
}

.summary-card {
  background: #fff;
  border-radius: 20px;
  padding: 18px 20px;      /* 🔥 lebih besar */
  border: 1px solid #eef0f7;
}

.summary-label {
  margin: 0 0 10px;
  font-size: 14px;         /* 🔥 naik */
  color: #777;
  font-weight: 600;
}

.summary-value {
  margin: 0;
  font-size: 36px;         /* 🔥 angka lebih besar */
  font-weight: 900;
  line-height: 1;
}

.summary-note {
  margin: 10px 0 0;
  font-size: 13px;         /* 🔥 naik */
  color: #8a8a8a;
}

/* =========================
   INVENTORY (dibesarkan + tombol besar)
   ========================= */
.inventory-card {
  background: #fff;
  border-radius: 22px;
  padding: 20px 22px 22px;
  border: 1px solid #eef0f7;
  display: flex;
  flex-direction: column;
}

.inventory-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.inventory-header h2 {
  margin: 0;
  font-size: 20px;       /* 🔥 naik */
  font-weight: 900;
}

.inventory-actions { display:flex; gap:10px; }

/* Search bar lebih besar */
.inventory-search {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.search-input {
  flex: 1;
  border-radius: 999px;
  border: 1px solid #d3d7e6;
  padding: 14px 18px;     /* 🔥 lebih tinggi */
  font-size: 15px;        /* 🔥 naik */
  outline: none;
}

/* ✅ tombol “Cari” lebih besar */
.round-btn {
  border-radius: 999px;
  border: none;
  background: #ff6b3d;
  color: #fff;
  padding: 0 28px;        /* 🔥 lebih besar */
  height: 48px;           /* 🔥 tinggi fix */
  font-size: 15px;        /* 🔥 naik */
  font-weight: 800;
  cursor: pointer;
}

/* kalau suatu saat dipakai */
.primary-btn {
  border-radius: 999px;
  border: none;
  background: linear-gradient(135deg,#ff5a3c,#ff9f1c);
  padding: 12px 26px;     /* 🔥 lebih besar */
  color: #fff;
  font-size: 15px;        /* 🔥 naik */
  font-weight: 800;
  cursor: pointer;
}

.inventory-table-wrapper {
  overflow-x: auto;
  background: #ffffff;
  flex: 1;
}

/* ✅ table font lebih besar + row lebih tinggi biar “mengisi” area kosong */
.inventory-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;       /* 🔥 naik */
}

.inventory-table th,
.inventory-table td {
  padding: 14px 10px;    /* 🔥 row lebih tinggi (mengurangi kosong bawah) */
  text-align: left;
  border-bottom: 1px solid #f0f2f8;
}

.inventory-table th {
  font-weight: 800;
  color: #777;
  font-size: 13px;
}

.status-pill {
  display: inline-block;
  padding: 6px 14px;     /* 🔥 lebih besar */
  border-radius: 999px;
  font-size: 13px;       /* 🔥 naik */
  font-weight: 800;
}

.status-active { background:#e6f8ec; color:#18a357; }
.status-out { background:#ffe5e5; color:#e63939; }

.link-small {
  border: none;
  background: none;
  font-size: 13px;       /* 🔥 naik */
  color: #ff6b3d;
  cursor: pointer;
  font-weight: 800;
}

.link-small.danger { color:#e63939; }
.divider { margin:0 8px; color:#ccc; }

/* =========================
   ORDERS (dibesarkan + isi tinggi)
   ========================= */
.orders-card {
  background: #fff;
  border-radius: 22px;
  padding: 20px 22px 22px;
  border: 1px solid #eef0f7;

  display: flex;
  flex-direction: column;
}

.orders-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.orders-header h2 {
  margin: 0;
  font-size: 20px; /* 🔥 naik */
  font-weight: 900;
}

.section-subtitle {
  margin: 6px 0 0;
  font-size: 13.5px; /* 🔥 naik */
  color: #777;
}

/* ✅ tombol “Lihat Semua” lebih besar */
.ghost-btn {
  border-radius: 999px;
  border: 1px solid #d3d7e6;
  background: #fff;
  padding: 10px 18px;      /* 🔥 lebih besar */
  font-size: 14px;         /* 🔥 naik */
  font-weight: 800;
  cursor: pointer;
}

.ghost-btn.small { padding: 10px 18px; }

.orders-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 14px;

  flex: 1; /* 🔥 isi tinggi card */
  background: #ffffff;
}

.order-item {
  display: flex;
  justify-content: space-between;
  align-items: center;

  background: #ffffff;
  border: 1px solid #eef0f7;
  border-radius: 18px;
  padding: 16px 16px;     /* 🔥 lebih tinggi */
}

.order-main { font-size: 14px; }
.order-code { margin:0 0 6px; font-weight:900; }
.order-info { margin:0; color:#555; font-weight:700; }
.order-meta { margin:0; font-size:13px; color:#888; }

.order-status {
  font-size: 13px;
  border-radius: 999px;
  padding: 8px 14px;      /* 🔥 lebih besar */
  font-weight: 900;
}

.status-pending { background:#fff3d6; color:#e6a800; }
.status-processing { background:#e9f7ff; color:#1e88e5; }
.status-done { background:#e6f8ec; color:#18a357; }

/* =========================
   RESPONSIVE
   ========================= */
@media (max-width: 1024px) {
  .quick-actions, .summary-row { grid-template-columns: 1fr; }
  .main-grid { grid-template-columns: 1fr; min-height: auto; }
}
</style>
