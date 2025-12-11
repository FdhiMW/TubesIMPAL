<template>
  <div class="admin-page">
    <main class="page-content">
      <!-- QUICK ACTIONS -->
      <section class="quick-actions">
        <div class="qa-card qa-search" @click="goToAdminProductList">
          <h3>Cari Barang</h3>
          <p>Telusuri dan filter stok produk.</p>
        </div>

        <div class="qa-card qa-manage" @click="goToCreateProduct">
          <h3>Tambah / Hapus Barang</h3>
          <p>Kelola katalog dengan cepat.</p>
        </div>

        <div class="qa-card qa-orders" @click="goToOrders">
          <h3>Lihat Pesanan</h3>
          <p>Pantau status pesanan terbaru.</p>
        </div>
      </section>

      <!-- SUMMARY -->
      <section class="summary-row">
        <div class="summary-card">
          <p class="summary-label">Total Barang Aktif</p>
          <p class="summary-value">1.245</p>
          <p class="summary-note">Produk yang tampil di beranda pengguna</p>
        </div>
        <div class="summary-card">
          <p class="summary-label">Pesanan Baru</p>
          <p class="summary-value">37</p>
          <p class="summary-note">Belum diproses admin</p>
        </div>
        <div class="summary-card">
          <p class="summary-label">Stok Hampir Habis</p>
          <p class="summary-value">18</p>
          <p class="summary-note">Perlu dicek ulang</p>
        </div>
      </section>

      <!-- MAIN GRID -->
      <section class="main-grid">
        <!-- KELOLA BARANG -->
        <div class="inventory-card">
          <header class="inventory-header">
            <h2>Kelola Barang</h2>
            <div class="inventory-actions">
              <button class="ghost-btn">Import</button>
              <button class="primary-btn" @click="goToCreateProduct">
                Tambah Barang
              </button>
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
                    <button
                      class="link-small"
                      @click="editProduct(item.id)"
                    >
                      Edit
                    </button>
                    <span class="divider">|</span>
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
                  <td
                    colspan="6"
                    style="text-align:center; font-size:12px; color:#9ca3af;"
                  >
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
              <p class="section-subtitle">
                Ringkasan 5 pesanan terakhir
              </p>
            </div>
            <button class="ghost-btn small" @click="goToOrders">
              Lihat Semua
            </button>
          </header>

          <ul class="orders-list">
            <li
              v-for="order in recentOrders"
              :key="order.id"
              class="order-item"
            >
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
  </div>
</template>

<script>
import { fetchAdminOrders } from '@/api/pesananApi'
import { searchProduk, deleteProduk } from '@/api/produkApi'
import NavbarAdmin from '@/components/layout/NavbarAdmin.vue'

export default {
  name: 'AdminDashboardView',
  components: { NavbarAdmin },
  data() {
    return {
      // kontrol pencarian & status
      searchKeyword: '',
      loading: false,
      deletingId: null,

      // inventory akan diisi dari backend
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
        {
          id: 2,
          name: 'Smartphone X Lite',
          category: 'Elektronik',
          stock: 0,
          price: 2999000,
          statusLabel: 'Stok Habis',
          statusClass: 'status-out',
        },
        {
          id: 3,
          name: 'Keyboard Mekanik RGB',
          category: 'Elektronik',
          stock: 11,
          price: 725000,
          statusLabel: 'Aktif',
          statusClass: 'status-active',
        },
      ],
      recentOrders: [],
    }
  },
  methods: {
    formatPrice(value) {
      return Number(value || 0).toLocaleString('id-ID')
    },

    goToOrders() {
      if (this.$route.path !== '/admin/pesanan') {
        this.$router.push('/admin/pesanan')
      }
    },

    goToAdminProductList() {
      this.$router.push({ name: 'admin-product-list' })
    },

    goToCreateProduct() {
      this.$router.push({ name: 'admin-product-create' })
    },

    // ============= KELOLA BARANG (DINAMIS) =============
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
            category: p.kategoriName || 'Elektronik', // sesuaikan jika DTO punya nama kategori
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

    editProduct(id) {
      // buka form tambah produk dalam mode edit
      this.$router.push({
        name: 'admin-product-create',
        query: { id },
      })
    },

    async confirmDelete(id) {
      if (!window.confirm('Yakin ingin menghapus produk ini?')) return

      this.deletingId = id
      try {
        await deleteProduk(id)
        this.inventory = this.inventory.filter((p) => p.id !== id)
      } catch (err) {
        console.error('Gagal menghapus produk', err)
        alert('Gagal menghapus produk, cek console.')
      } finally {
        this.deletingId = null
      }
    },
    // ===================================================

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
  },
  mounted() {
    this.loadRecentOrders()
    this.loadInventory() // load produk dari backend saat dashboard dibuka
  },
}
</script>

<style scoped>
/* semua style persis seperti yang kamu kirim, tidak diubah */
.admin-page {
  min-height: 100vh;
  background: #f5f7fb;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI',
    sans-serif;
  color: #222;
}

/* CONTENT */
.page-content {
  padding: 20px 32px 32px;
}

/* Quick actions */
.quick-actions {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
  margin-bottom: 18px;
}

.qa-card {
  border-radius: 20px;
  padding: 16px 18px;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
}

.qa-card h3 {
  margin: 0 0 4px;
  font-size: 15px;
}

.qa-card p {
  margin: 0;
  font-size: 12px;
}

.qa-search {
  background: linear-gradient(135deg, #ff7750, #ffb48b);
}

.qa-manage {
  background: linear-gradient(135deg, #ff5a3c, #ff9f1c);
}

.qa-orders {
  background: linear-gradient(135deg, #ff8553, #ffc16d);
}

/* Summary */
.summary-row {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
  margin-bottom: 18px;
}

.summary-card {
  background: #fff;
  border-radius: 18px;
  padding: 14px 16px;
}

.summary-label {
  margin: 0 0 6px;
  font-size: 12px;
  color: #777;
}

.summary-value {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
}

.summary-note {
  margin: 4px 0 0;
  font-size: 11px;
  color: #8a8a8a;
}

/* Main grid */
.main-grid {
  display: grid;
  grid-template-columns: 2.2fr 1.2fr;
  gap: 16px;
}

/* Inventory */
.inventory-card {
  background: #fff;
  border-radius: 20px;
  padding: 16px 18px 18px;
}

.inventory-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.inventory-header h2 {
  margin: 0;
  font-size: 16px;
}

.inventory-actions {
  display: flex;
  gap: 8px;
}

.inventory-search {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.search-input {
  flex: 1;
  border-radius: 999px;
  border: 1px solid #d3d7e6;
  padding: 8px 14px;
  font-size: 13px;
  outline: none;
}

.round-btn {
  border-radius: 999px;
  border: none;
  background: #ff6b3d;
  color: #fff;
  padding: 0 16px;
  font-size: 13px;
  cursor: pointer;
}

/* Buttons */
.ghost-btn {
  border-radius: 999px;
  border: 1px solid #d3d7e6;
  background: #fff;
  padding: 6px 16px;
  font-size: 12px;
  cursor: pointer;
}

.ghost-btn.small {
  padding: 4px 12px;
}

.primary-btn {
  border-radius: 999px;
  border: none;
  background: linear-gradient(135deg, #ff5a3c, #ff9f1c);
  padding: 7px 18px;
  color: #fff;
  font-size: 12px;
  cursor: pointer;
}

/* Table */
.inventory-table-wrapper {
  overflow-x: auto;
}

.inventory-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;
}

.inventory-table th,
.inventory-table td {
  padding: 8px 6px;
  text-align: left;
  border-bottom: 1px solid #eef0f7;
}

.inventory-table th {
  font-weight: 600;
  color: #777;
}

.status-pill {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 999px;
  font-size: 11px;
}

.status-active {
  background: #e6f8ec;
  color: #18a357;
}

.status-out {
  background: #ffe5e5;
  color: #e63939;
}

.link-small {
  border: none;
  background: none;
  font-size: 11px;
  color: #ff6b3d;
  cursor: pointer;
}

.link-small.danger {
  color: #e63939;
}

.divider {
  margin: 0 4px;
  color: #ccc;
}

/* Orders */
.orders-card {
  background: #fff;
  border-radius: 20px;
  padding: 16px 18px 18px;
}

.orders-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.orders-header h2 {
  margin: 0;
  font-size: 16px;
}

.section-subtitle {
  margin: 2px 0 0;
  font-size: 11px;
  color: #777;
}

.orders-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.order-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #f7f8fc;
  border-radius: 14px;
  padding: 8px 10px;
}

.order-main {
  font-size: 12px;
}

.order-code {
  margin: 0 0 2px;
  font-weight: 600;
}

.order-info {
  margin: 0;
  color: #555;
}

.order-meta {
  margin: 0;
  font-size: 11px;
  color: #888;
}

.order-status {
  font-size: 11px;
  border-radius: 999px;
  padding: 4px 10px;
}

.status-pending {
  background: #fff3d6;
  color: #e6a800;
}

.status-processing {
  background: #e9f7ff;
  color: #1e88e5;
}

.status-done {
  background: #e6f8ec;
  color: #18a357;
}

/* Responsive */
@media (max-width: 1024px) {
  .quick-actions,
  .summary-row {
    grid-template-columns: 1fr;
  }

  .main-grid {
    grid-template-columns: 1fr;
  }
}
</style>
