<template>
  <header class="navbar">
    <div class="navbar-left">
      <div class="logo-box">
        <!-- pakai tulisan dulu, logo bisa diganti gambar -->
        <span class="logo-icon">🛍️</span>
        <span class="logo-text">KlikMall</span>
      </div>
    </div>
    <div class="navbar-center" v-if="showSearch">
      <input
        type="text"
        class="search-input"
        placeholder="Search ..."
      />
    </div>
    <div class="navbar-right">
      <i class="icon">📦</i>
      <i class="icon">🛒</i>
      <i class="icon">👤</i>
    </div>
  </header>
</template>

<script>
export default {
  name: 'Navbar',
  props: {
    showSearch: {
      type: Boolean,
      default: false,
    },
  },
}
</script>

<style scoped>
.navbar {
  height: 70px;
  padding: 0 32px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(90deg, #ef4444, #f97316);
  color: #fff;
}

.logo-box {
  display: flex;
  align-items: center;
  gap: 8px;
}

.logo-icon {
  font-size: 28px;
}

.logo-text {
  font-weight: 700;
  font-size: 22px;
}

.search-input {
  width: 400px;
  padding: 8px 14px;
  border-radius: 24px;
  border: none;
  outline: none;
  font-size: 14px;
}

.navbar-right {
  display: flex;
  gap: 20px;
}

.icon {
  font-size: 22px;
  cursor: pointer;
}
</style>
