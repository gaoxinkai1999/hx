const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    host: '0.0.0.0',
    // https:true,
    port: 8081,
    // client: {
    //   webSocketURL: 'ws://0.0.0.0:8081/ws',
    // },
    headers: {
      'Access-Control-Allow-Origin': '*',
    },
    historyApiFallback: true,
    allowedHosts: 'all',
    // 配置代理，解决跨域问题
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8090', // 后端服务地址
        changeOrigin: true, // 开启代理
        pathRewrite: {
          '^/api': '/api' // 不重写路径
        }
      }
    }
  }
})
