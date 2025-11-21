import Vue from 'vue'
import Router from 'vue-router'
import LoginView from './views/LoginView.vue'
import RegisterView from './views/RegisterView.vue'
import HomeView from './views/HomeView.vue'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
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
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: true },
    },
    {
      path: '*',
      redirect: '/login',
    },
  ],
})

// 🧠 Guard: cek sudah login atau belum
router.beforeEach((to, from, next) => {
  const isLoggedIn = !!localStorage.getItem('user') // user disimpan setelah login

  if (to.matched.some(r => r.meta.requiresAuth) && !isLoggedIn) {
    // butuh login tapi belum login
    next('/login')
  } else if (to.matched.some(r => r.meta.guestOnly) && isLoggedIn) {
    // sudah login tapi ke /login atau /register
    next('/')
  } else {
    next()
  }
})

export default router
