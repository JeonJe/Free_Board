import { createRouter, createWebHistory } from 'vue-router'
// import HomeView from '../views/HomeView.vue'
import BoardGetList from '../views/BoardGetList.vue'
import BoardWriteInfo from '../views/BoardWriteInfo.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: BoardGetList
  },
  {
    path: '/list',
    name: 'BoardGetList',
    component: BoardGetList
  },
    {
    path: '/write',
    name: 'BoardWriteInfo',
    component: BoardWriteInfo
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
