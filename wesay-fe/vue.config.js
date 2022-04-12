module.exports = {
  devServer: {
    port: 8080,
    open: true,
    proxy: {
      '': {
        target: 'http://127.0.0.1:8081',
        changeOrigin: true,
        ws: true,
        // pathRewrite: {
        //   '^/api': '/api'
        // }
      },
      // '/socket.io': {
      //   target: 'ws://127.0.0.1:8081', // target host
      //   ws: true, // enable websocket proxy
      //   changeOrigin: true, // needed for virtual hosted sites
      //   logLevel: 'debug'
      // }
    }
  }
}