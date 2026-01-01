<template>
  <header class="topbar">
    <!-- ====== [DISESUAIKAN: KLIK LOGO KEMBALI KE DASHBOARD] ====== -->
    <div class="topbar-left brand-click" @click="goDashboard">
      <div class="navbar-brand" @click="goHome">
        <img
          src="@/assets/foto/logo.jpg"
          alt="Thriftly Logo"
          class="navbar-logo"
        />
        <span class="brand-text">Thriftly</span>
      </div>
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
        this.$router.push('/admin').catch((err) => {
          // ✅ cegah error "NavigationDuplicated"
          if (err && err.name === 'NavigationDuplicated') return
          const msg = (err && err.message) ? err.message : ''
          if (msg.includes('Avoided redundant navigation')) return
          throw err
        })
      }
    },

    logout() {
      localStorage.removeItem('user')
      this.$router.push('/login').catch((err) => {
        // ✅ cegah error duplikat navigation
        if (err && err.name === 'NavigationDuplicated') return
        const msg = (err && err.message) ? err.message : ''
        if (msg.includes('Avoided redundant navigation')) return
        throw err
      })
    },

    submitSearch() {
      const q = this.searchQuery && this.searchQuery.trim()
      if (!q) return

      // ✅ target route yang kamu pakai sekarang
      const target = {
        name: 'admin-product-list',
        query: { q },
      }

      // ✅ CEGAH push ke lokasi yang sama persis (penyebab error)
      const currentName = this.$route.name
      const currentQ = (this.$route.query && this.$route.query.q) ? String(this.$route.query.q) : ''
      const nextQ = String(q)

      if (currentName === target.name && currentQ === nextQ) {
        return
      }

      // ✅ push aman: ignore NavigationDuplicated
      this.$router.push(target).catch((err) => {
        if (err && err.name === 'NavigationDuplicated') return
        const msg = (err && err.message) ? err.message : ''
        if (msg.includes('Avoided redundant navigation')) return
        throw err
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
.navbar-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.navbar-logo {
  width: 50px;
  height: 50px;
  object-fit: contain;

  background: #ffffff;
  padding: 6px;
  border-radius: 50%;

  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.18);
}

.brand-text {
  font-size:40px;
  font-weight: 800;
  color: white;       /* karena navbar kamu gradient orange */
  letter-spacing: 0.3px;
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
