<template>
  <div class="page">
    <Navbar :showSearch="true" />

    <div class="home-container">
      <section class="hero">
        <div class="hero-main"></div>
        <div class="hero-side">
          <div class="hero-side-top"></div>
          <div class="hero-side-bottom"></div>
        </div>
      </section>

      <section class="chips">
        <div
          v-for="n in 8"
          :key="n"
          class="chip"
        ></div>
      </section>

      <section class="kategori-section">
        <h3>KATEGORI</h3>
        <div class="kategori-grid">
          <div
            v-for="n in 12"
            :key="n"
            class="kategori-card"
          >
            <span>Elektronik</span>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import Navbar from '../components/layout/Navbar.vue'

export default {
  name: 'HomeView',
  components: { Navbar },
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #ffffff;
}

.home-container {
  padding: 24px 32px 40px 32px;
}

/* hero banner */
.hero {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 16px;
  margin-bottom: 20px;
}

.hero-main,
.hero-side-top,
.hero-side-bottom {
  border-radius: 24px;
  background: linear-gradient(90deg, #f97316, #fb7185);
  opacity: 0.4;
}

.hero-main {
  height: 200px;
}

.hero-side-top {
  height: 120px;
  margin-bottom: 10px;
}

.hero-side-bottom {
  height: 70px;
}

/* chips */
.chips {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}

.chip {
  width: 70px;
  height: 70px;
  border-radius: 24px;
  background: linear-gradient(135deg, #f97316, #fb7185);
  opacity: 0.7;
}

/* kategori grid */
.kategori-section h3 {
  font-size: 16px;
  margin-bottom: 12px;
}

.kategori-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 10px;
  background: #f5f5f5;
  padding: 16px;
  border-radius: 18px;
}

.kategori-card {
  background: #e5e5e5;
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 13px;
}
</style>
