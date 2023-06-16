import { createRouter, createWebHistory } from 'vue-router'
import BoardGetList from '../views/BoardGetList.vue'
import BoardWriteInfo from '../views/BoardWriteInfo.vue'
import BoardGetInfo from '../views/BoardGetInfo.vue'
import BoardUpdateInfo from "../views/BoardUpdateInfo.vue";

const routes = [
  {
    path: "/",
    name: "home",
    component: BoardGetList,
  },
  {
    path: "/board/list",
    name: "BoardGetList",
    component: BoardGetList,
  },
  {
    path: "/board/write",
    name: "BoardWriteInfo",
    component: BoardWriteInfo,
  },
  {
    path: "/board/view",
    name: "BoardGetInfo",
    component: BoardGetInfo,
  },
  {
    path: "/board/update",
    name: "BoardUpdateInfo",
    component: BoardUpdateInfo,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
