<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary fixed-top">
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/dashboard">
        <i class="fas fa-heartbeat me-2"></i>
        Healthcare Management
      </router-link>

      <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
      >
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto">
          <li class="nav-item">
            <router-link class="nav-link" to="/dashboard">
              <i class="fas fa-tachometer-alt me-1"></i>
              Dashboard
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/patients">
              <i class="fas fa-users me-1"></i>
              Patients
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/public">
              <i class="fas fa-info-circle me-1"></i>
              Public Info
            </router-link>
          </li>
        </ul>

        <ul class="navbar-nav">
          <li class="nav-item dropdown">
            <a
                class="nav-link dropdown-toggle"
                href="#"
                id="navbarDropdown"
                role="button"
                data-bs-toggle="dropdown"
            >
              <i class="fas fa-user me-1"></i>
              {{ user?.username || 'User' }}
            </a>
            <ul class="dropdown-menu dropdown-menu-end">
              <li><a class="dropdown-item" href="#"><i class="fas fa-cog me-2"></i>Profile</a></li>
              <li><a class="dropdown-item" href="#"><i class="fas fa-bell me-2"></i>Notifications</a></li>
              <li><hr class="dropdown-divider"></li>
              <li>
                <a class="dropdown-item" href="#" @click="handleLogout">
                  <i class="fas fa-sign-out-alt me-2"></i>Logout
                </a>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'NavBar',
  computed: {
    ...mapGetters(['user'])
  },
  methods: {
    handleLogout() {
      this.$store.dispatch('logout')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.navbar-brand {
  font-weight: bold;
  font-size: 1.5rem;
}

.nav-link {
  padding: 0.5rem 1rem;
  border-radius: 0.25rem;
  transition: background-color 0.3s ease;
}

.nav-link:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.nav-link.router-link-active {
  background-color: rgba(255, 255, 255, 0.2);
  font-weight: 500;
}

.dropdown-item {
  padding: 0.5rem 1rem;
  transition: background-color 0.3s ease;
}

.dropdown-item:hover {
  background-color: #f8f9fa;
}
</style>