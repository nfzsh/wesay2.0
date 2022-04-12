<template>
  <img alt="Vue logo" src="./assets/logo.png">
  <HelloWorld msg="Welcome to Your Vue.js App"/>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import { io } from 'socket.io-client'
import {onMounted} from "vue";
export default {
  name: 'App',
  components: {
    HelloWorld
  },
  data() {
    return {
      msg: 'Welcome to Your Vue.js App',
      ws: null,
      ws_url: 'ws://localhost:8081/api/we_say/zsh',
      ws_connected: false,
      ws_message: '',
      ws_messages: [],
      ws_error: ''
    }
  },
  methods: {
    connect() {
      this.ws = new WebSocket(this.ws_url);
      this.ws.onopen = this.onOpen;
      this.ws.onmessage = this.onMessage;
      this.ws.onerror = this.onError;
      this.ws.onclose = this.onClose;
    },
    onOpen() {
      this.ws_connected = true;
      this.ws_error = '';
    },
    onMessage(event) {
      this.ws_messages.push(event.data);
    },
    onError(event) {
      this.ws_connected = false;
      this.ws_error = event.data;
    },
    onClose(event) {
      this.ws_connected = false;
      this.ws_error = event.data;
    },
    send() {
      this.ws.send(this.ws_message);
      this.ws_message = '';
    }
  },
  setup() {
    let socket = null
    console.log('created')
    onMounted(() => {
      // 创建客户端 websocket 的实例
      socket = io({
        reconnection: true,
        forceNew: true,
        transports: ['websocket'],
      })
      socket.on('connect', () => console.log('connect: websocket 连接成功！'))
    })
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
