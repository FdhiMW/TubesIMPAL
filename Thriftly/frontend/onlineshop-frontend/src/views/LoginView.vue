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
          <h2>Login</h2>
          <form @submit.prevent="onSubmit">
            <div class="field">
              <input
                v-model="email"
                type="text"
                placeholder="Username/Email"
              />
            </div>
            <div class="field">
              <input
                v-model="password"
                type="password"
                placeholder="Password"
              />
            </div>

            <button type="submit" class="btn-login" :disabled="loading">
              {{ loading ? 'Memproses...' : 'Login' }}
            </button>

            <p class="link-small">
              <a href="#">Lupa Password?</a>
            </p>

            <p class="bottom-text">
              Belum punya akun?
              <router-link to="/register">Daftar</router-link>
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
import { login } from '../api/authApi'

export default {
  name: 'LoginView',
  components: { Navbar },
  data() {
    return {
      email: '',
      password: '',
      loading: false,
      error: '',
    }
  },
  methods: {
    async onSubmit() {
      this.error = ''
      this.loading = true
      try {
        const res = await login(this.email, this.password)
        const user = res.data
        // Simpan data user di localStorage (sementara, tanpa JWT)
        localStorage.setItem('user', JSON.stringify(user))

        if (user.peranPengguna === 'admin') {
          this.$router.push('/admin')     // admin → dashboard admin
        } else {
          this.$router.push('/')          // customer → beranda user
        }
      } catch (e) {
        this.error =
          e.response?.data?.message || 'Email atau password salah'
      } finally {
        this.loading = false
      }
    },
  },
}

</script>

<style scoped>
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

.btn-login {
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

.link-small {
  margin-top: 10px;
  font-size: 13px;
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
