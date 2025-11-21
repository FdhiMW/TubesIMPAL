import Vue from 'vue'
import Router from 'vue-router'
import LoginView from './views/LoginView.vue'
import RegisterView from './views/RegisterView.vue'
import HomeView from './views/HomeView.vue'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    {
      path: '*',
      redirect: '/login',
    },
  ],
})
