<template>
  <header class="topbar">
    <!-- ====== [DISESUAIKAN: LOGO JADI BUTTON KEMBALI KE DASHBOARD USER] ====== -->
    <div
      class="topbar-left brand-click"
      @click="goHome"
      role="button"
      tabindex="0"
      @keyup.enter="goHome"
    >
      <div class="logo-badge"></div>
      <span class="logo-text">Thriftly</span>
    </div>
    <!-- ====== [AKHIR PENYESUAIAN] ====== -->

    <!-- ====== [DISESUAIKAN: DI HALAMAN LOGIN/REGISTER HILANGKAN SEARCH] ====== -->
    <div class="search-wrapper" v-if="showSearch && !isAuthPage">
      <input
        v-model="searchQuery"
        type="text"
        class="search-input"
        placeholder="Cari pakaian bekas berkualitas..."
        @keyup.enter="submitSearch"
      />
    </div>
    <!-- ====== [AKHIR PENYESUAIAN] ====== -->

    <!-- ====== [DISESUAIKAN: DI HALAMAN LOGIN/REGISTER HILANGKAN LACAK PAKET, KERANJANG, LOGOUT] ====== -->
    <div class="topbar-right" v-if="!isAuthPage">
      <!-- Lacak Paket + icon 📦 -->
      <router-link to="/lacak-paket" class="topbar-link">
        <span class="link-icon">📦</span>
        <span>Lacak Paket</span>
      </router-link>

      <!-- Keranjang + icon 🛒 -->
      <router-link to="/keranjang" class="topbar-link nav-cart">
        <span class="link-icon">🛒</span>
        <span>Keranjang</span>
      </router-link>

      <!-- ====== [DISESUAIKAN: PROFIL DIGANTI BUTTON LOGOUT] ====== -->
      <button class="logout-btn" @click="logout">
        Logout
      </button>
      <!-- ====== [AKHIR PENYESUAIAN] ====== -->
    </div>
    <!-- ====== [AKHIR PENYESUAIAN] ====== -->
  </header>
</template>

<script>
export default {
  name: 'Navbar',
  props: {
    showSearch: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      // [DIBIARKAN] supaya tidak merusak struktur data yang sudah ada
      showProfileMenu: false,
      searchQuery: '',
    }
  },
  computed: {
    // ====== [DISESUAIKAN: DETEKSI HALAMAN LOGIN & REGISTER] ======
    isAuthPage() {
      const p = this.$route && this.$route.path ? this.$route.path : ''
      return p === '/login' || p === '/register'
    },
    // ====== [AKHIR PENYESUAIAN] ======
  },
  methods: {
    // ====== [DIBIARKAN: KLIK LOGO KEMBALI KE DASHBOARD USER] ======
    goHome() {
      if (this.$route.path !== '/') {
        this.$router.push('/')
      }
    },
    // ====== [AKHIR PENYESUAIAN] ======

    // [DIBIARKAN] supaya tidak merusak struktur methods yang sudah ada
    toggleProfileMenu() {
      this.showProfileMenu = !this.showProfileMenu
    },

    logout() {
      localStorage.removeItem('user')
      this.showProfileMenu = false
      this.$router.push('/login')
    },

    // [DIBIARKAN] supaya tidak merusak struktur methods yang sudah ada
    handleClickOutside() {
      this.showProfileMenu = false
    },

    submitSearch() {
      const q = this.searchQuery && this.searchQuery.trim()
      if (!q) return

      this.$router.push({
        name: 'product-search',
        query: { q },
      })
    },
  },
  mounted() {
    document.addEventListener('click', this.handleClickOutside)
  },
  beforeDestroy() {
    document.removeEventListener('click', this.handleClickOutside)
  },
}
</script>

<style scoped>
.topbar {
  display: flex;
  align-items: center;

  /* ====== [DISESUAIKAN: SAMA DENGAN NAVBAR ADMIN] ====== */
  padding: 16px 32px;
  min-height: 68px;
  /* ====== [AKHIR PENYESUAIAN] ====== */

  background: linear-gradient(90deg, #ff5a3c, #ff9f1c);
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.1);

  /* ====== [DISESUAIKAN: SAMA DENGAN NAVBAR ADMIN] ====== */
  gap: 26px;
  color: #fff;
  flex-wrap: nowrap;
  /* ====== [AKHIR PENYESUAIAN] ====== */
}

.topbar-left {
  display: flex;
  align-items: center;

  /* ====== [DISESUAIKAN: SAMA DENGAN NAVBAR ADMIN] ====== */
  gap: 12px;
  flex: 0 0 auto;
  /* ====== [AKHIR PENYESUAIAN] ====== */
}

/* ====== [DIBIARKAN: INDIKASI BISA DIKLIK] ====== */
.brand-click {
  cursor: pointer;
  user-select: none;
}
.brand-click:hover {
  opacity: 0.92;
}
/* ====== [AKHIR PENYESUAIAN] ====== */

/* ====== [DISESUAIKAN: LOGO SAMA DENGAN NAVBAR ADMIN] ====== */
.logo-badge {
  width: 40px;
  height: 40px;
  border-radius: 999px;
  background: #ffd46c;
}

.logo-text {
  font-weight: 700;
  font-size: 20px;
  color: #fff;
}
/* ====== [AKHIR PENYESUAIAN] ====== */

/* ====== [DISESUAIKAN: SEARCH SAMA DENGAN NAVBAR ADMIN] ====== */
.search-wrapper {
  flex: 1;
  display: flex;
  justify-content: center;
  min-width: 0;
}

.search-input {
  width: 540px;
  max-width: 100%;
  border-radius: 999px;
  border: none;
  padding: 12px 20px;
  font-size: 15px;
  outline: none;
  box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.4);
}
/* ====== [AKHIR PENYESUAIAN] ====== */

.topbar-right {
  display: flex;
  align-items: center;

  /* ====== [DISESUAIKAN: SAMA DENGAN NAVBAR ADMIN] ====== */
  gap: 18px;
  flex: 0 0 auto;
  white-space: nowrap;
  /* ====== [AKHIR PENYESUAIAN] ====== */
}

/* ====== [DISESUAIKAN: MENU SAMA DENGAN NAVBAR ADMIN] ====== */
.topbar-link {
  font-size: 15px;
  color: #fff;
  text-decoration: none;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}
/* ====== [AKHIR PENYESUAIAN] ====== */

.link-icon {
  /* ikon tetap ada, tapi ukuran dibuat proporsional */
  font-size: 18px;
}

/* ====== [DISESUAIKAN: LOGOUT BUTTON SAMA DENGAN NAVBAR ADMIN] ====== */
.logout-btn {
  border-radius: 999px;
  border: none;
  padding: 8px 20px;
  font-size: 15px;
  cursor: pointer;
  background: #ffffff;
  color: #ff5a3c;
  font-weight: 600;
}
/* ====== [AKHIR PENYESUAIAN] ====== */

/* [DIBIARKAN] agar tidak menghapus struktur style yang sudah ada */
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
</style>
