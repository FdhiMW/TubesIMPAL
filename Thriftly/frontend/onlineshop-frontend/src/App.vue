<template>
  <div id="app">
    <!-- Navbar USER untuk semua route yang BUKAN /admin -->
    <Navbar v-if="!isAdminRoute" />

    <!-- Navbar ADMIN untuk semua route yang diawali /admin -->
    <NavbarAdmin v-if="isAdminRoute" />

    <!-- Halaman utama -->
    <router-view />
  </div>
</template>

<script>
import Navbar from './components/layout/Navbar.vue'
import NavbarAdmin from './components/layout/NavbarAdmin.vue'

export default {
  name: 'App',
  components: {
    Navbar,
    NavbarAdmin,
  },
  computed: {
    // true kalau path sekarang mulai dengan '/admin'
    isAdminRoute() {
      return this.$route.path.startsWith('/admin')
    },
  },
}
</script>

<style>
html,
body {
  margin: 0;
  padding: 0;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI',
    sans-serif;
}

#app {
  min-height: 100vh;
}
</style>
