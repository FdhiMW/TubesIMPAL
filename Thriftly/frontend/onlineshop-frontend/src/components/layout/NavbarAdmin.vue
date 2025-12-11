<template>
  <header class="topbar">
    <div class="topbar-left">
      <div class="logo-badge"></div>
      <span class="logo-text">KlikMall Admin</span>
    </div>

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
      <!-- Link ke dashboard admin -->
      <router-link to="/admin" class="topbar-link">
        Dashboard
      </router-link>

      <!-- Link ke halaman pesanan admin -->
      <router-link to="/admin/pesanan" class="topbar-link">
        Pesanan
      </router-link>

      <!-- Tombol logout -->
      <button class="logout-btn" @click="logout">
        Logout
      </button>
    </nav>
  </header>
</template>

<script>
export default {
  name: 'NavbarAdmin',

  // [DITAMBAHKAN] supaya v-model="searchQuery" tidak error
  data() {
    return {
      searchQuery: '',
    }
  },

  methods: {
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
  padding: 10px 32px;
  background: linear-gradient(90deg, #ff5a3c, #ff9f1c);
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.1);
  gap: 24px;
  color: #fff;
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
}

.topbar-link.router-link-active {
  font-weight: 600;
  text-decoration: underline;
}

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
</style>
