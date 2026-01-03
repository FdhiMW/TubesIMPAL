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
                <td>{{ order.jumlahBarang ?? 0 }}</td>

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
/* =========================================================
   [DITAMBAHKAN] PAKSA BACKGROUND GLOBAL PUTIH
   - hilangkan area abu-abu kiri/kanan/bawah (body / html)
   ========================================================= */
:global(html),
:global(body),
:global(#app) {
  height: 100%;
  min-height: 100%;
  margin: 0;
  background: #ffffff !important;
}

/* =========================================================
   [DITAMBAHKAN] BASE FONT LEBIH BESAR UNTUK SEMUA TEKS
   - cukup atur 1x di container, child ikut membesar
   ========================================================= */
.admin-page {
  min-height: 100vh;
  width: 100%;
  background: #ffffff;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI',
    sans-serif;
  color: #222;

  display: flex;
  flex-direction: column;

  font-size: 16px;      /* ✅ TAMBAHAN (naik dari default) */
  line-height: 1.45;    /* ✅ TAMBAHAN biar lebih lega */
}

/* CONTENT WRAPPER (dibuat fleksibel & full width) */
.page-content {
  padding: 22px 34px 34px;  /* ✅ sedikit dibesarkan biar seimbang */
  width: 100%;
  flex: 1;
  display: flex;
}

/* order-page dibuat full lebar agar tidak ada “margin abu” kiri-kanan */
.order-page {
  width: 100%;
  max-width: 100%;
  margin: 0;
}

/* HEADER & TOOLBAR */
.order-page-header {
  margin-bottom: 16px;
}

.page-title {
  font-size: 30px;   /* ✅ DIBESARKAN (sebelumnya 24px) */
  font-weight: 800;  /* ✅ lebih tegas */
  margin-bottom: 8px;
}

.page-subtitle {
  color: #6b7280;
  font-size: 15px;   /* ✅ DIBESARKAN */
}

.order-toolbar {
  display: flex;
  gap: 10px;         /* ✅ sedikit lega */
  margin: 16px 0;
  width: 100%;
}

.search-input-toolbar {
  flex: 1;
  padding: 10px 14px;    /* ✅ lebih besar */
  border-radius: 10px;   /* ✅ lebih halus */
  border: 1px solid #d1d5db;
  font-size: 15px;       /* ✅ DIBESARKAN (sebelumnya 14px) */
}

/* button style sama dengan dashboard */
.ghost-btn {
  border-radius: 999px;
  border: 1px solid #d3d7e6;
  background: #fff;
  padding: 10px 18px;    /* ✅ DIBESARKAN */
  font-size: 14px;       /* ✅ DIBESARKAN */
  cursor: pointer;
  font-weight: 700;      /* ✅ lebih jelas */
}

.ghost-btn.small {
  padding: 8px 14px;     /* ✅ ikut membesar */
  font-size: 13px;
}

.primary-btn {
  border-radius: 999px;
  border: none;
  background: linear-gradient(135deg, #ff5a3c, #ff9f1c);
  padding: 10px 20px;    /* ✅ DIBESARKAN */
  color: #fff;
  font-size: 14px;       /* ✅ DIBESARKAN */
  cursor: pointer;
  font-weight: 800;      /* ✅ lebih tegas */
}

.primary-btn.small {
  padding: 8px 14px;     /* ✅ ikut membesar */
  font-size: 13px;
}

/* tombol hijau untuk SELESAI */
.success-btn {
  border-radius: 999px;
  border: none;
  background: #22c55e;
  padding: 10px 20px;    /* ✅ DIBESARKAN */
  color: #fff;
  font-size: 14px;       /* ✅ DIBESARKAN */
  cursor: not-allowed;
  opacity: 0.95;
  font-weight: 800;      /* ✅ lebih tegas */
}

.success-btn.small {
  padding: 8px 14px;
  font-size: 13px;
}

/* TABLE */
.order-table-wrapper {
  background: #ffffff;
  border-radius: 14px;   /* ✅ sedikit lebih smooth */
  box-shadow: 0 6px 18px rgba(15, 23, 42, 0.08);
  padding: 18px;         /* ✅ lebih lega */
  width: 100%;

  overflow-x: auto;
}

.order-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 980px;
}

.order-table th,
.order-table td {
  padding: 12px 10px;  /* ✅ lebih besar */
  text-align: left;
  font-size: 15px;     /* ✅ DIBESARKAN (sebelumnya 14px) */
  border-bottom: 1px solid #e5e7eb;
}

.order-table th {
  font-weight: 800;
  color: #4b5563;
  font-size: 14px;     /* ✅ header sedikit lebih kecil tapi tetap jelas */
  letter-spacing: 0.2px;
}

.empty-state {
  text-align: center;
  color: #9ca3af;
  font-style: italic;
  font-size: 15px;     /* ✅ DIBESARKAN */
}

.action-cell {
  white-space: nowrap;
}

.no-action {
  color: #9ca3af;
  font-size: 15px;     /* ✅ DIBESARKAN */
}

/* alamat rapi, tidak melebarkan tabel */
.alamat-cell {
  max-width: 560px;      /* ✅ sedikit dilebarkan */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 15px;       /* ✅ ikut membesar */
}

/* status badge */
.status-pill {
  display: inline-block;
  padding: 5px 12px;     /* ✅ DIBESARKAN */
  border-radius: 999px;
  font-size: 13px;       /* ✅ DIBESARKAN (sebelumnya 11px) */
  font-weight: 800;      /* ✅ lebih tegas */
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
  margin-top: 14px;
  color: #b91c1c;
  font-size: 15px;       /* ✅ DIBESARKAN */
  font-weight: 600;
}

/* responsive */
@media (max-width: 680px) {
  .page-content {
    padding: 16px 14px 22px;
  }

  .page-title {
    font-size: 24px;      /* ✅ tetap proporsional di mobile */
  }

  .search-input-toolbar {
    font-size: 14px;
  }
}
</style>
