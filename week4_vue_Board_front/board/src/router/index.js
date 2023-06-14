import { createRouter, createWebHistory } from 'vue-router'
import BoardGetList from '../views/BoardGetList.vue'
import BoardWriteInfo from '../views/BoardWriteInfo.vue'
import BoardGetInfo from '../views/BoardGetInfo.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: BoardGetList
  },
  {
    path: '/board/list',
    name: 'BoardGetList',
    component: BoardGetList
  },
    {
    path: '/board/write',
    name: 'BoardWriteInfo',
    component: BoardWriteInfo
  },
  {
    path: '/board/view',
    name: 'BoardGetInfo',
    component: BoardGetInfo
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
