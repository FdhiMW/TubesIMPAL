import Vue from 'vue'
import Router from 'vue-router'

// AUTH & BERANDA USER
import LoginView from './views/LoginView.vue'
import RegisterView from './views/RegisterView.vue'
import HomeView from './views/HomeView.vue'
import CartView from './views/CartView.vue'

// PRODUK
import ProdukDetailView from './views/ProdukDetailView.vue'
import ProductListView from './views/ProductListView.vue'

// PEMBAYARAN
import CheckoutView from './views/CheckoutView.vue'

// ADMIN
import AdminDashboardView from './views/admin/AdminDashboardView.vue'
import AdminOrderListView from './views/admin/AdminOrderListView.vue'
import AdminProductListView from './views/admin/AdminProductListView.vue'
import AdminProductFormView from './views/admin/AdminProductFormView.vue'

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

    // HASIL PENCARIAN USER
    {
      path: '/produk',
      name: 'product-search',
      component: ProductListView,
      meta: { requiresAuth: true },
    },

    // DETAIL PRODUK
    {
      path: '/produk/:id',
      name: 'ProdukDetail',
      component: ProdukDetailView,
      meta: { requiresAuth: true },
    },

    // KERANJANG
    {
      path: '/keranjang',
      name: 'Cart',
      component: CartView,
      meta: { requiresAuth: true },
    },

    // CHECKOUT
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

    // ADMIN: LIST / SEARCH PRODUK
    {
      path: '/admin/produk',
      name: 'admin-product-list',
      component: AdminProductListView,
      meta: { requiresAuth: true, adminOnly: true },
    },
    
    {
      path: '/admin/produk/tambah',
      name: 'admin-product-create',
      component: AdminProductFormView,
      meta: { requiresAuth: true, adminOnly: true },
    },

    // fallback
    { path: '*', redirect: '/login' },
  ],
})

// ==== NAVIGATION GUARD ====
router.beforeEach((to, from, next) => {
  const rawUser = localStorage.getItem('user')
  const user = rawUser ? JSON.parse(rawUser) : null
  const isLoggedIn = !!user
  const role = user?.peranPengguna

  // butuh login tapi belum login
  if (to.matched.some(r => r.meta.requiresAuth) && !isLoggedIn) {
    return next('/login')
  }

  // hanya guest (login & register)
  if (to.matched.some(r => r.meta.guestOnly) && isLoggedIn) {
    if (role === 'admin') return next('/admin')
    return next('/')
  }

  // khusus admin
  if (to.matched.some(r => r.meta.adminOnly)) {
    if (!isLoggedIn) return next('/login')
    if (role !== 'admin') return next('/')
  }

  // kalau admin buka "/", arahkan ke /admin
  if (to.path === '/' && isLoggedIn && role === 'admin') {
    return next('/admin')
  }

  next()
})

export default router
