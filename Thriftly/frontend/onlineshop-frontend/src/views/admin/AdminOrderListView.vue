<template>
  <div class="admin-page">
    <!-- TIDAK ADA NAVBAR DI SINI, SUDAH DIHANDLE NavbarAdmin lewat App.vue -->

    <!-- KONTEN LIHAT PESANAN -->
    <main class="page-content">
      <div class="order-page">
        <header class="order-page-header">
          <h1 class="page-title">Kelola Pesanan</h1>
        </header>

        <!-- TOOLBAR PENCARIAN -->
        <section class="order-toolbar">
          <input
            v-model="searchKode"
            type="text"
            class="search-input-toolbar"
            placeholder="Cari nomor pesanan (contoh: ORD-XXXX)"
            @keyup.enter="loadOrders"
          />
          <button class="primary-btn" @click="loadOrders" :disabled="loading">
            {{ loading ? 'Memuat...' : 'Cari' }}
          </button>
          <button
            v-if="searchKode"
            class="ghost-btn"
            @click="resetSearch"
            :disabled="loading"
          >
            Reset
          </button>
        </section>

        <!-- TABEL PESANAN -->
        <section class="order-table-wrapper">
          <table class="order-table">
            <thead>
              <tr>
                <th>Nomor Pesanan</th>
                <!-- ====== [DISESUAIKAN: Total Barang -> Jumlah Barang] ====== -->
                <th>Jumlah Barang</th>
                <!-- ====== [DITAMBAHKAN: Detail Alamat di sebelah Jumlah Barang] ====== -->
                <th>Detail Alamat</th>
                <th>Total Pembayaran</th>
                <th>Status</th>
                <th>Aksi</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="!loading && orders.length === 0">
                <td colspan="6" class="empty-state">
                  Belum ada pesanan yang ditemukan.
                </td>
              </tr>

              <tr v-for="order in orders" :key="order.idPesanan">
                <td>{{ order.kodePesanan }}</td>

                <!-- ====== [DISESUAIKAN: Jumlah Barang] ====== -->
                <td>{{ order.totalBarang }}</td>

                <!-- ====== [DITAMBAHKAN: Alamat terhubung dari field DB/API] ====== -->
                <td class="alamat-cell" :title="getAlamat(order)">
                  {{ getAlamat(order) }}
                </td>

                <td>{{ formatRupiah(order.totalPembayaran) }}</td>
                <td>
                  <span
                    class="status-pill"
                    :class="mapStatusToClass(order.statusPesanan)"
                  >
                    {{ mapStatusToLabel(order.statusPesanan) }}
                  </span>
                </td>

                <!-- ====== [DISESUAIKAN: AKSI jadi 1 tombol sesuai status] ====== -->
                <td class="action-cell">
                  <!-- MENUNGGU_PEMBAYARAN -> tombol Jadikan Dikemas -->
                  <button
                    v-if="normalizeStatus(order.statusPesanan) === 'MENUNGGU_PEMBAYARAN'"
                    class="primary-btn small"
                    @click="changeStatus(order, 'DIKEMAS')"
                    :disabled="loading"
                  >
                    Jadikan Dikemas
                  </button>

                  <!-- DIKEMAS -> tombol Dalam Perjalanan -->
                  <button
                    v-else-if="normalizeStatus(order.statusPesanan) === 'DIKEMAS'"
                    class="primary-btn small"
                    @click="changeStatus(order, 'DALAM_PERJALANAN')"
                    :disabled="loading"
                  >
                    Dalam Perjalanan
                  </button>

                  <!-- SELESAI -> tombol hijau Selesai -->
                  <button
                    v-else-if="normalizeStatus(order.statusPesanan) === 'SELESAI'"
                    class="success-btn small"
                    disabled
                  >
                    Selesai
                  </button>

                  <!-- DALAM_PERJALANAN -> tidak ada tombol (kosong) -->
                  <span v-else class="no-action">-</span>
                </td>
                <!-- ====== [AKHIR PENYESUAIAN AKSI] ====== -->
              </tr>
            </tbody>
          </table>
        </section>

        <p v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </p>
      </div>
    </main>
  </div>
</template>

<script>
import { fetchAdminOrders, updateOrderStatus } from '@/api/pesananApi'

export default {
  name: 'AdminOrderListView',
  data() {
    return {
      orders: [],
      searchKode: '',
      loading: false,
      errorMessage: '',
    }
  },
  methods: {
    // DATA PESANAN
    async loadOrders() {
      this.loading = true
      this.errorMessage = ''
      try {
        const res = await fetchAdminOrders(this.searchKode)
        this.orders = res.data
      } catch (err) {
        console.error('Gagal memuat pesanan admin', err)
        this.errorMessage = 'Gagal memuat data pesanan.'
      } finally {
        this.loading = false
      }
    },

    async changeStatus(order, status) {
      if (order.statusPesanan === status) return
      this.loading = true
      this.errorMessage = ''
      try {
        const res = await updateOrderStatus(order.idPesanan, status)
        const updated = res.data
        const idx = this.orders.findIndex(
          (o) => o.idPesanan === order.idPesanan
        )
        if (idx !== -1) {
          this.$set(this.orders, idx, updated)
        }
      } catch (err) {
        console.error('Gagal mengubah status pesanan', err)
        this.errorMessage = 'Gagal mengubah status pesanan.'
      } finally {
        this.loading = false
      }
    },

    resetSearch() {
      this.searchKode = ''
      this.loadOrders()
    },

    formatRupiah(value) {
      if (value == null) return '-'
      const number =
        typeof value === 'number' ? value : parseFloat(value.toString())
      if (isNaN(number)) return value
      return new Intl.NumberFormat('id-ID', {
        style: 'currency',
        currency: 'IDR',
      }).format(number)
    },

    mapStatusToLabel(status) {
      if (!status) return '-'
      const map = {
        MENUNGGU_PEMBAYARAN: 'Menunggu Pembayaran',
        DIKEMAS: 'Dikemas',
        DALAM_PERJALANAN: 'Dalam Perjalanan',
        SELESAI: 'Selesai',
      }
      return map[status] || status
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

    // ====== [DITAMBAHKAN: Ambil alamat yang cocok dengan field DB/API] ======
    getAlamat(order) {
      if (!order) return '-'

      const candidates = [
        order.alamatLengkap,
        order.alamat_lengkap,
        order.alamat,
        order.detailAlamat,
        order.detail_alamat,
      ]

      const val = candidates.find(
        (v) => v !== null && v !== undefined && String(v).trim() !== ''
      )
      return val ? String(val) : '-'
    },
    // ====== [AKHIR PENYESUAIAN] ======

    // ====== [DITAMBAHKAN: normalisasi status untuk kondisi tombol] ======
    normalizeStatus(status) {
      if (!status) return ''
      return String(status).trim().toUpperCase()
    },
    // ====== [AKHIR PENYESUAIAN] ======
  },
  mounted() {
    this.loadOrders()
  },
}
</script>

<style scoped>
.admin-page {
  min-height: 100vh;
  background: #f5f7fb;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI',
    sans-serif;
  color: #222;
}

/* CONTENT WRAPPER */
.page-content {
  padding: 20px 32px 32px;
}

.order-page {
  max-width: 1100px;
  margin: 0 auto;
}

/* HEADER & TOOLBAR */
.order-page-header {
  margin-bottom: 16px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 4px;
}

.page-subtitle {
  color: #6b7280;
  font-size: 14px;
}

.order-toolbar {
  display: flex;
  gap: 8px;
  margin: 16px 0;
}

.search-input-toolbar {
  flex: 1;
  padding: 8px 10px;
  border-radius: 8px;
  border: 1px solid #d1d5db;
  font-size: 14px;
}

/* button style sama dengan dashboard */
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

.primary-btn.small {
  padding: 4px 12px;
  font-size: 12px;
}

/* ====== [DITAMBAHKAN: tombol hijau untuk SELESAI] ====== */
.success-btn {
  border-radius: 999px;
  border: none;
  background: #22c55e;
  padding: 7px 18px;
  color: #fff;
  font-size: 12px;
  cursor: not-allowed;
  opacity: 0.95;
}

.success-btn.small {
  padding: 4px 12px;
  font-size: 12px;
}
/* ====== [AKHIR PENYESUAIAN] ====== */

/* TABLE */
.order-table-wrapper {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 6px 18px rgba(15, 23, 42, 0.08);
  padding: 16px;
}

.order-table {
  width: 100%;
  border-collapse: collapse;
}

.order-table th,
.order-table td {
  padding: 10px 8px;
  text-align: left;
  font-size: 14px;
  border-bottom: 1px solid #e5e7eb;
}

.order-table th {
  font-weight: 600;
  color: #4b5563;
}

.empty-state {
  text-align: center;
  color: #9ca3af;
  font-style: italic;
}

.action-cell {
  white-space: nowrap;
}

.no-action {
  color: #9ca3af;
}

/* ====== [DITAMBAHKAN: alamat rapi, tidak melebarkan tabel] ====== */
.alamat-cell {
  max-width: 420px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
/* ====== [AKHIR PENYESUAIAN] ====== */

/* status badge */
.status-pill {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 999px;
  font-size: 11px;
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

.error-message {
  margin-top: 12px;
  color: #b91c1c;
  font-size: 14px;
}
</style>
