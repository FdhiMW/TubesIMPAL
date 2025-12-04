<template>
  <div class="admin-page">
    <!-- TOP NAV (SAMA DENGAN DASHBOARD) -->
    <header class="topbar">
      <div class="topbar-left">
        <div class="logo-badge"></div>
        <span class="logo-text">KlikMall Admin</span>
      </div>

      <div class="search-wrapper">
        <input
          type="text"
          class="search-input"
          placeholder="Cari barang di katalog admin..."
        />
      </div>

      <nav class="topbar-right">
        <!-- tombol Dashboard & Pesanan pakai router -->
        <a href="#" class="topbar-link" @click.prevent="goDashboard">
          Dashboard
        </a>
        <a href="#" class="topbar-link" @click.prevent="goOrders">
          Pesanan
        </a>

        <div class="profile-wrapper" @click.stop="toggleProfileMenu">
          <span class="topbar-link">
            Profil ▾
          </span>
          <div v-if="showProfileMenu" class="profile-dropdown">
            <button class="dropdown-item" @click.stop="logout">
              Logout
            </button>
          </div>
        </div>
      </nav>
    </header>

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
                <th>Total Barang</th>
                <th>Total Pembayaran</th>
                <th>Status</th>
                <th>Aksi</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="!loading && orders.length === 0">
                <td colspan="5" class="empty-state">
                  Belum ada pesanan yang ditemukan.
                </td>
              </tr>

              <tr v-for="order in orders" :key="order.idPesanan">
                <td>{{ order.kodePesanan }}</td>
                <td>{{ order.totalBarang }}</td>
                <td>{{ formatRupiah(order.totalPembayaran) }}</td>
                <td>
                  <span
                    class="status-pill"
                    :class="mapStatusToClass(order.statusPesanan)"
                  >
                    {{ mapStatusToLabel(order.statusPesanan) }}
                  </span>
                </td>
                <td class="action-cell">
                  <button
                    class="ghost-btn small"
                    @click="changeStatus(order, 'DIKEMAS')"
                    :disabled="loading || order.statusPesanan === 'DIKEMAS'"
                  >
                    Jadikan Dikemas
                  </button>
                  <button
                    class="primary-btn small"
                    @click="changeStatus(order, 'DALAM_PERJALANAN')"
                    :disabled="loading || order.statusPesanan === 'DALAM_PERJALANAN'"
                  >
                    Dalam Perjalanan
                  </button>
                </td>
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
      showProfileMenu: false,
      orders: [],
      searchKode: '',
      loading: false,
      errorMessage: '',
    }
  },
  methods: {
    // NAVBAR
    goDashboard() {
      this.$router.push('/admin')
    },
    goOrders() {
      this.$router.push('/admin/pesanan')
    },
    toggleProfileMenu() {
      this.showProfileMenu = !this.showProfileMenu
    },
    logout() {
      localStorage.removeItem('user')
      this.showProfileMenu = false
      this.$router.push('/login')
    },
    handleClickOutside() {
      this.showProfileMenu = false
    },

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
  },
  mounted() {
    document.addEventListener('click', this.handleClickOutside)
    this.loadOrders()
  },
  beforeDestroy() {
    document.removeEventListener('click', this.handleClickOutside)
  },
}
</script>

<style scoped>
/* layout sama dengan dashboard */
.admin-page {
  min-height: 100vh;
  background: #f5f7fb;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI',
    sans-serif;
  color: #222;
}

/* TOPBAR */

.topbar {
  display: flex;
  align-items: center;
  padding: 10px 32px;
  background: linear-gradient(90deg, #ff5a3c, #ff9f1c);
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.1);
  gap: 24px;
}

.topbar-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-badge {
  width: 32px;
  height: 32px;
  border-radius: 999px;
  background: #ffd46c;
}

.logo-text {
  font-weight: 700;
  font-size: 18px;
  color: #fff;
}

.search-wrapper {
  flex: 1;
}

.search-input {
  width: 100%;
  border-radius: 999px;
  border: none;
  padding: 10px 18px;
  font-size: 14px;
  outline: none;
  box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.4);
}

.topbar-right {
  display: flex;
  gap: 16px;
}

.profile-wrapper {
  position: relative;
  cursor: pointer;
}

.profile-dropdown {
  position: absolute;
  right: 0;
  top: 32px;
  background: #ffffff;
  border-radius: 10px;
  box-shadow: 0 8px 20px rgba(15, 23, 42, 0.15);
  padding: 6px 0;
  min-width: 120px;
  z-index: 20;
}

.dropdown-item {
  width: 100%;
  padding: 6px 14px;
  border: none;
  background: transparent;
  font-size: 13px;
  text-align: left;
  cursor: pointer;
  color: #e11d48;
}

.dropdown-item:hover {
  background: #fee2e2;
}

.topbar-link {
  font-size: 13px;
  color: #fff;
  text-decoration: none;
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
