<template>
  <div class="page">
    <Navbar />

    <div class="content">
      <div class="left">
        <div class="brand">
          <div class="logo-big">🛍️</div>
          <div class="brand-name">KlikMall</div>
        </div>
      </div>

      <div class="right">
        <div class="card">
          <h2>Daftar</h2>
          <form @submit.prevent="onSubmit">
            <div class="field">
              <input
                v-model="namaLengkap"
                type="text"
                placeholder="Nama Lengkap"
              />
            </div>
            <div class="field">
              <input
                v-model="email"
                type="email"
                placeholder="Masukkan Email"
              />
            </div>
            <div class="field">
              <input
                v-model="password"
                type="password"
                placeholder="Masukkan Password"
              />
            </div>
            <div class="field">
              <input
                v-model="confirmPassword"
                type="password"
                placeholder="Konfirmasi Password"
              />
            </div>

            <button type="submit" class="btn-daftar" :disabled="loading">
              {{ loading ? 'Memproses...' : 'Daftar' }}
            </button>

            <p class="bottom-text">
              Sudah punya akun?
              <router-link to="/login">Login</router-link>
            </p>

            <p v-if="error" class="error">{{ error }}</p>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from '../components/layout/Navbar.vue'
import { register } from '../api/authApi'

export default {
  name: 'RegisterView',
  components: { Navbar },
  data() {
    return {
      namaLengkap: '',
      email: '',
      password: '',
      confirmPassword: '',
      loading: false,
      error: '',
    }
  },
  methods: {
    async onSubmit() {
      this.error = ''

      if (this.password !== this.confirmPassword) {
        this.error = 'Password dan konfirmasi tidak sama'
        return
      }

      this.loading = true
      try {
        const res = await register(
          this.namaLengkap,
          this.email,
          this.password
        )
        localStorage.setItem('user', JSON.stringify(res.data))
        this.$router.push('/')
      } catch (e) {
        this.error =
          e.response?.data?.message || 'Gagal mendaftar, coba lagi'
      } finally {
        this.loading = false
      }
    },
  },
}
</script>

<style scoped>
/* sama seperti LoginView, boleh di-refactor ke CSS global */
.page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content {
  flex: 1;
  display: flex;
  padding: 40px 80px;
}

.left,
.right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.brand {
  text-align: center;
}

.logo-big {
  font-size: 120px;
}

.brand-name {
  font-size: 48px;
  font-weight: 800;
  margin-top: 16px;
}

.card {
  width: 360px;
  padding: 32px 28px;
  border-radius: 24px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08);
  background: #ffffff;
}

.card h2 {
  margin-bottom: 20px;
  font-size: 24px;
}

.field {
  margin-bottom: 14px;
}

.field input {
  width: 100%;
  padding: 10px 14px;
  border-radius: 999px;
  border: 1.5px solid #d4d4d4;
  outline: none;
  font-size: 14px;
}

.btn-daftar {
  width: 100%;
  border: none;
  border-radius: 999px;
  padding: 10px 0;
  margin-top: 8px;
  background: linear-gradient(90deg, #ef4444, #f97316);
  color: #fff;
  font-weight: 600;
  cursor: pointer;
}

.bottom-text {
  margin-top: 24px;
  text-align: center;
  font-size: 13px;
}

.error {
  margin-top: 12px;
  color: #ef4444;
  font-size: 13px;
}
</style>
