<template>
  <div class="page">

    <div class="container">
      <h2 class="title">Lacak Paket</h2>

      <div v-if="loading" class="state">Memuat...</div>
      <div v-else-if="error" class="state error">{{ error }}</div>
      <div v-else-if="items.length === 0" class="state">
        Belum ada pesanan.
      </div>

      <div v-else class="list">
        <div v-for="(it, idx) in items" :key="idx" class="card">
          <div class="thumb">
            <img :src="it.imageUrl || placeholder" alt="produk" @error="onImgError" />
          </div>

          <div class="info">
            <div class="name">{{ it.namaProduk || '-' }}</div>

            <div class="meta">
              <span>Ukuran: <b>{{ it.ukuran || '-' }}</b></span>
              <span class="dot">•</span>
              <span>Jenis: <b>{{ it.jenis || '-' }}</b></span>
              <span class="dot">•</span>
              <span>Harga: <b>{{ formatRupiah(it.harga) }}</b></span>
            </div>

            <div class="timeline">
              <div class="step">
                <span class="bullet" :class="bulletClass(it.statusPesanan, 'DIKEMAS')"></span>
                <span class="label">Dikemas</span>
              </div>
              <div class="line"></div>
              <div class="step">
                <span class="bullet" :class="bulletClass(it.statusPesanan, 'DALAM_PERJALANAN')"></span>
                <span class="label">Dalam Perjalanan</span>
              </div>
              <div class="line"></div>
              <div class="step">
                <span class="bullet" :class="bulletClass(it.statusPesanan, 'SELESAI')"></span>
                <span class="label">Selesai</span>
              </div>

              <div class="actions">
                <button
                  class="btn"
                  :disabled="!canConfirm(it.statusPesanan) || confirmingId === it.idPesanan"
                  @click="confirmReceived(it)"
                  title="Hanya bisa jika status pesanan DALAM_PERJALANAN"
                >
                  {{ confirmingId === it.idPesanan ? 'Memproses...' : 'Pesanan Selesai' }}
                </button>
              </div>
            </div>

            <div class="order-code">
              Kode Pesanan: <b>{{ it.kodePesanan }}</b>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from '../components/layout/Navbar.vue'
import { fetchUserTracking, confirmUserOrderReceived } from '../api/pesananApi'

export default {
  name: 'TrackPackageView',
  components: { Navbar },
  data() {
    return {
      loading: true,
      error: '',
      items: [],
      confirmingId: null,
      placeholder:
        'data:image/svg+xml;charset=UTF-8,' +
        encodeURIComponent(
          `<svg xmlns="http://www.w3.org/2000/svg" width="200" height="200"><rect width="100%" height="100%" fill="#f3f4f6"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="#9ca3af" font-family="Arial" font-size="14">No Image</text></svg>`
        ),
    }
  },
  methods: {
    getUser() {
      const raw = localStorage.getItem('user')
      return raw ? JSON.parse(raw) : null
    },
    async load() {
      this.loading = true
      this.error = ''
      try {
        const user = this.getUser()
        if (!user?.idUser) {
          this.$router.push('/login')
          return
        }
        const res = await fetchUserTracking(user.idUser)
        this.items = Array.isArray(res.data) ? res.data : []
      } catch (e) {
        this.error = e.response?.data?.message || 'Gagal memuat data lacak paket'
      } finally {
        this.loading = false
      }
    },
    normalizeStatus(s) {
      return (s || '').trim().toUpperCase()
    },
    canConfirm(status) {
      return this.normalizeStatus(status) === 'DALAM_PERJALANAN'
    },
    bulletClass(currentStatus, step) {
      const cur = this.normalizeStatus(currentStatus)
      const order = ['DIKEMAS', 'DALAM_PERJALANAN', 'SELESAI']
      const curIdx = order.indexOf(cur)
      const stepIdx = order.indexOf(step)
      if (curIdx === -1) return 'off'
      return stepIdx <= curIdx ? 'on' : 'off'
    },
    formatRupiah(val) {
      const n = Number(val)
      if (Number.isNaN(n)) return 'Rp0'
      return new Intl.NumberFormat('id-ID', {
        style: 'currency',
        currency: 'IDR',
        minimumFractionDigits: 0,
      }).format(n)
    },
    onImgError(e) {
      e.target.src = this.placeholder
    },
    async confirmReceived(item) {
      if (!this.canConfirm(item.statusPesanan)) return
      const user = this.getUser()
      if (!user?.idUser) return this.$router.push('/login')

      try {
        this.confirmingId = item.idPesanan
        await confirmUserOrderReceived(user.idUser, item.idPesanan)
        item.statusPesanan = 'SELESAI'
      } catch (e) {
        alert(e.response?.data?.message || 'Gagal konfirmasi pesanan selesai')
      } finally {
        this.confirmingId = null
      }
    },
  },
  mounted() {
    this.load()
  },
}
</script>

<style scoped>
.page { min-height: 100vh; background: #ffffff; }
.container { max-width: 980px; margin: 0 auto; padding: 22px 24px 60px; }
.title { font-size: 22px; font-weight: 800; margin: 6px 0 18px; }
.state { padding: 18px; border-radius: 14px; background: #f8fafc; color: #334155; }
.state.error { background: #fee2e2; color: #991b1b; }
.list { display: flex; flex-direction: column; gap: 18px; }
.card { display: flex; gap: 18px; padding: 18px 0; border-bottom: 1px solid #e5e7eb; }
.thumb { width: 140px; height: 110px; border-radius: 18px; overflow: hidden; background: linear-gradient(90deg, #ff5a3c, #ff9f1c); }
.thumb img { width: 100%; height: 100%; object-fit: cover; display: block; }
.info { flex: 1; min-width: 0; }
.name { font-size: 18px; font-weight: 800; margin-bottom: 6px; }
.meta { font-size: 13px; color: #475569; display: flex; flex-wrap: wrap; gap: 8px; align-items: center; }
.dot { color: #cbd5e1; }
.timeline { margin-top: 10px; display: flex; align-items: center; gap: 12px; flex-wrap: wrap; }
.step { display: flex; align-items: center; gap: 6px; }
.bullet { width: 10px; height: 10px; border-radius: 999px; display: inline-block; }
.bullet.on { background: #22c55e; }
.bullet.off { background: #ef4444; }
.label { font-size: 12px; color: #334155; }
.line { height: 2px; width: 120px; background: #e2e8f0; }
.actions { margin-left: auto; }
.btn { border: none; background: linear-gradient(90deg, #ef4444, #f97316); color: #fff; padding: 10px 16px; border-radius: 12px; font-weight: 700; font-size: 13px; cursor: pointer; }
.btn:disabled { opacity: 0.5; cursor: not-allowed; }
.order-code { margin-top: 8px; font-size: 12px; color: #64748b; }
</style>
