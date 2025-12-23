<template>
  <header class="topbar">
    <!-- ====== [DISESUAIKAN: KLIK LOGO KEMBALI KE DASHBOARD] ====== -->
    <div class="topbar-left brand-click" @click="goDashboard">
      <div class="logo-badge"></div>
      <span class="logo-text">Thriftly Admin</span>
    </div>
    <!-- ====== [AKHIR PENYESUAIAN] ====== -->

    <div class="search-wrapper">
      <input
        v-model="searchQuery"
        type="text"
        class="search-input"
        placeholder="Cari barang di katalog admin..."
        @keyup.enter="submitSearch"
      />
    </div>

    <nav class="topbar-right">
      <router-link to="/admin" class="topbar-link">
        Dashboard
      </router-link>

      <router-link to="/admin/pesanan" class="topbar-link">
        Pesanan
      </router-link>

      <button class="logout-btn" @click="logout">
        Logout
      </button>
    </nav>
  </header>
</template>

<script>
export default {
  name: 'NavbarAdmin',

  data() {
    return {
      searchQuery: '',
    }
  },

  methods: {
    goDashboard() {
      if (this.$route.path !== '/admin') {
        this.$router.push('/admin')
      }
    },

    logout() {
      localStorage.removeItem('user')
      this.$router.push('/login')
    },

    submitSearch() {
      const q = this.searchQuery && this.searchQuery.trim()
      if (!q) return

      this.$router.push({
        name: 'admin-product-list',
        query: { q },
      })
    },
  },
}
</script>

<style scoped>
.topbar {
  display: flex;
  align-items: center;

  /* ====== NAVBAR SEDIKIT LEBIH BESAR LAGI ====== */
  padding: 16px 32px;
  min-height: 68px;
  /* =========================================== */

  background: linear-gradient(90deg, #ff5a3c, #ff9f1c);
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.1);
  gap: 26px;
  color: #fff;
  flex-wrap: nowrap;
}

.topbar-left {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 0 0 auto;
}

.brand-click {
  cursor: pointer;
}
.brand-click:hover {
  opacity: 0.9;
}

/* ====== LOGO LEBIH BESAR ====== */
.logo-badge {
  width: 40px;
  height: 40px;
  border-radius: 999px;
  background: #ffd46c;
}

.logo-text {
  font-weight: 700;
  font-size: 20px;
}
/* ============================= */

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

  /* ====== SEARCH IKUT PROPORSIONAL ====== */
  padding: 12px 20px;
  font-size: 15px;
  /* ===================================== */

  outline: none;
  box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.4);
}

.topbar-right {
  display: flex;
  align-items: center;
  gap: 18px;
  flex: 0 0 auto;
  white-space: nowrap;
}

/* ====== MENU LEBIH BESAR ====== */
.topbar-link {
  font-size: 15px;
  color: #fff;
  text-decoration: none;
}

.topbar-link.router-link-active {
  font-weight: 600;
  text-decoration: underline;
}
/* ============================== */

/* ====== LOGOUT BUTTON LEBIH BESAR ====== */
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
/* ====================================== */
</style>
