import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import CryptoJS from 'crypto-js'

const app =createApp(App)

//비밀번호 해쉬를 위해 사용 
app.config.globalProperties.$CryptoJS = CryptoJS

app.use(router).mount('#app')