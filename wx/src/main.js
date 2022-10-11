import Vue from 'vue'
import App from './App.vue'
import axios from "axios"
Vue.config.productionTip = false
import VueQriously from 'vue-qriously'
Vue.use(VueQriously)
Vue.prototype.$ajax=axios
new Vue({
  render: h => h(App),
}).$mount('#app')
