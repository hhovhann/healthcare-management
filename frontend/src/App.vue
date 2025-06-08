<template>
  <div id="app">
    <NavBar v-if="isAuthenticated" />
    <main class="main-content" :class="{ 'authenticated': isAuthenticated }">
      <router-view />
    </main>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import NavBar from './components/NavBar.vue'

export default {
  name: 'App',
  components: {
    NavBar
  },
  computed: {
    ...mapGetters(['isAuthenticated'])
  },
  created() {
    // Check if user is already logged in from localStorage
    const token = localStorage.getItem('jwt_token')
    if (token) {
      this.$store.dispatch('initializeAuth', token)
    }
  }
}
</script>

<style>
#app {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  min-height: 100vh;
  background-color: #f8f9fa;
}

.main-content {
  min-height: 100vh;
  padding: 20px;
}

.main-content.authenticated {
  margin-left: 0;
  padding-top: 80px;
}

@media (max-width: 768px) {
  .main-content.authenticated {
    padding-top: 70px;
  }
}
</style>