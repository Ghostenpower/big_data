import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import StockListView from '../views/StockListView.vue'
import AboutView from '../views/AboutView.vue'
import StockDetail from '@/views/StockDetailView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/stocklist',
      name: 'stocklist',
      component: StockListView
    },
    {
      path: '/about',
      name: 'about',
      component: AboutView
    },
    {
      path: '/stockdetail',
      name: 'stockdetail',
      component: StockDetail
    }
  ]
})

export default router
