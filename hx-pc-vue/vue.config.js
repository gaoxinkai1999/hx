const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  // 开发服务器配置
  devServer: {
    port: 8080, // 前端服务端口
    open: true, // 自动打开浏览器
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
