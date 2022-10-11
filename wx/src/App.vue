<template>
  <div id="app">
    <el-dialog
        :visible.sync="codeDialogVisible"
        :show-close="false"
        width="350px"
        center
    >
      <qriously :value="codeUrl" :size="300" />
      <!-- <img src="../assets/img/code.png" alt="" style="width:100%"><br> -->
      使用微信扫码支付
    </el-dialog>
  </div>
</template>

<script>


export default {
  name: 'App',

  data() {
    return {
      payBtnDisabled: false, //确认支付按钮是否禁用
      codeDialogVisible: true, //微信支付二维码弹窗
      productList: [], //商品列表
      payOrder: {
        //订单信息
        productId: '', //商品id
        payType: 'wxpay', //支付方式
      },
      codeUrl: '', // 二维码
      orderNo: '', //订单号
      timer: null, // 定时器
    }
  },
  created() {
    this.getCodeUrl()
  },
  methods:{
    getCodeUrl() {
      this.$ajax.interceptors.request.use(config=>{
        return config;
      }
      )

      const param={

      }
      const config={
        headers:{'token':'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbiI6ImQ1YTczNWIzLTZkZTgtNDg1NS04YWExLTA2OTYyMDk2M2Q4YSJ9.JGtguq2_oIX1QlApGrCxOy_9a3Piq0sPYIgXBeirnOI'}
      }

      this.$ajax.post(
          "http://localhost:8090/sys/native/pay/1/1",param,config
        )
          .then(res=>{
        this.codeUrl=res.data.codeUrl;
        console.log(res.data);
      })
    }
  },


}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
