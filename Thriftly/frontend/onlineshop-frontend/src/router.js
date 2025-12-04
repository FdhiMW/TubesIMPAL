import Vue from 'vue'
import Router from 'vue-router'

// AUTH & BERANDA USER
import LoginView from './views/LoginView.vue'
import RegisterView from './views/RegisterView.vue'
import HomeView from './views/HomeView.vue'

// (kalau kamu nanti punya halaman lain bisa ditambah di sini)

// ADMIN
import AdminDashboardView from './views/admin/AdminDashboardView.vue'

// PRODUK DETAIL
import ProdukDetailView from './views/ProdukDetailView.vue'

//PEMBAYARAN
import CheckoutView from './views/CheckoutView.vue'

//LIHAT PESANAN (ADMIN)
import AdminOrderListView from './views/admin/AdminOrderListView.vue'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    // BERANDA USER
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: true },
    },

    {
      path: '/produk/:id',
      name: 'ProdukDetail',
      component: ProdukDetailView,
      meta: { requiresAuth: true },
    },

    {
      path: '/checkout',
      name: 'Checkout',
      component: CheckoutView,
      meta: { requiresAuth: true },
    },
    
    // AUTH
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { guestOnly: true },
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
      meta: { guestOnly: true },
    },

    // ADMIN DASHBOARD
    {
      path: '/admin',
      name: 'admin-dashboard',
      component: AdminDashboardView,
      meta: { requiresAuth: true, adminOnly: true },
    },

    // ADMIN: LIHAT PESANAN
    {
      path: '/admin/pesanan',
      name: 'admin-order-list',
      component: AdminOrderListView,
      meta: { requiresAuth: true, adminOnly: true },
    },

    // fallback
    { path: '*', redirect: '/login' },

    { path: '/produk/:id', name: 'ProdukDetail', component: ProdukDetailView },
  ],
})

// ==== NAVIGATION GUARD ====
router.beforeEach((to, from, next) => {
  const rawUser = localStorage.getItem('user')
  const user = rawUser ? JSON.parse(rawUser) : null
  const isLoggedIn = !!user
  const role = user?.peranPengguna

  // route butuh login tapi belum login
  if (to.matched.some(r => r.meta.requiresAuth) && !isLoggedIn) {
    return next('/login')
  }

  // route hanya untuk guest (login & register)
  if (to.matched.some(r => r.meta.guestOnly) && isLoggedIn) {
    if (role === 'admin') return next('/admin')
    return next('/')
  }

  // route khusus admin
  if (to.matched.some(r => r.meta.adminOnly)) {
    if (!isLoggedIn) return next('/login')
    if (role !== 'admin') return next('/') // user biasa dilarang ke /admin
  }

  // kalau admin buka "/", arahkan ke /admin
  if (to.path === '/' && isLoggedIn && role === 'admin') {
    return next('/admin')
  }

  next()
})

export default router
