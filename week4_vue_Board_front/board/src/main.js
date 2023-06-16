import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import CryptoJS from 'crypto-js'

const app =createApp(App)

app.config.globalProperties.$CryptoJS = CryptoJS

app.use(router).mount('#app')