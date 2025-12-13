<template>
  <header class="topbar">
    <!-- ====== [DISESUAIKAN: TOPBAR-LEFT JADI BUTTON KE DASHBOARD USER] ====== -->
    <div class="topbar-left" @click="goHome" role="button" tabindex="0" @keyup.enter="goHome">
      <div class="logo-badge"></div>
      <span class="logo-text">Thriftly</span>
    </div>
    <!-- ====== [AKHIR PENYESUAIAN] ====== -->

    <div class="search-wrapper" v-if="showSearch">
      <input
        v-model="searchQuery"
        type="text"
        class="search-input"
        placeholder="Cari pakaian bekas berkualitas..."
        @keyup.enter="submitSearch"
      />
    </div>

    <div class="topbar-right">
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
  methods: {
    // ====== [DITAMBAHKAN: KLIK LOGO/TEXT KEMBALI KE DASHBOARD USER] ======
    goHome() {
      // asumsi dashboard user ada di "/"
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

/* ====== [DITAMBAHKAN: BIAR TERASA BISA DIKLIK] ====== */
.topbar-left {
  cursor: pointer;
  user-select: none;
}
/* ====== [AKHIR PENYESUAIAN] ====== */

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
  align-items: center;
  gap: 16px;
}

.topbar-link {
  font-size: 13px;
  color: #fff;
  text-decoration: none;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.link-icon {
  font-size: 18px;
}

/* ====== [DITAMBAHKAN: STYLE BUTTON LOGOUT] ====== */
.logout-btn {
  border-radius: 999px;
  border: none;
  padding: 6px 16px;
  font-size: 13px;
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
