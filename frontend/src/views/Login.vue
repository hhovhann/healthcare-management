<template>
  <div class="login-container">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-6 col-lg-4">
          <div class="card shadow-lg">
            <div class="card-body p-5">
              <div class="text-center mb-4">
                <i class="fas fa-heartbeat text-primary" style="font-size: 3rem;"></i>
                <h2 class="mt-3 mb-2">Healthcare Login</h2>
                <p class="text-muted">Sign in to your account</p>
              </div>

              <form @submit.prevent="handleLogin">
                <div class="mb-3">
                  <label for="username" class="form-label">Username</label>
                  <div class="input-group">
                    <span class="input-group-text">
                      <i class="fas fa-user text-muted"></i>
                    </span>
                    <input
                        id="username"
                        v-model="form.username"
                        type="text"
                        class="form-control"
                        placeholder="Enter your username"
                        required
                        :disabled="loading"
                    >
                  </div>
                </div>

                <div class="mb-4">
                  <label for="password" class="form-label">Password</label>
                  <div class="input-group">
                    <span class="input-group-text">
                      <i class="fas fa-lock text-muted"></i>
                    </span>
                    <input
                        id="password"
                        v-model="form.password"
                        type="password"
                        class="form-control"
                        placeholder="Enter your password"
                        required
                        :disabled="loading"
                    >
                  </div>
                </div>

                <div class="alert alert-danger" v-if="error" role="alert">
                  <i class="fas fa-exclamation-triangle me-2"></i>
                  {{ error }}
                </div>

                <button
                    type="submit"
                    class="btn btn-primary w-100 mb-3"
                    :disabled="loading"
                >
                  <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                  <i v-else class="fas fa-sign-in-alt me-2"></i>
                  {{ loading ? 'Signing In...' : 'Sign In' }}
                </button>

                <div class="text-center">
                  <p class="mb-0">
                    Don't have an account?
                    <router-link to="/register" class="text-primary text-decoration-none">
                      Sign up
                    </router-link>
                  </p>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'Login',
  data() {
    return {
      form: {
        username: '',
        password: ''
      }
    }
  },
  computed: {
    ...mapGetters(['loading', 'error'])
  },
  methods: {
    async handleLogin() {
      try {
        await this.$store.dispatch('login', this.form)
        this.$router.push('/dashboard')
      } catch (error) {
        // Error is handled by the store
      }
    }
  },
  mounted() {
    // Clear any previous errors
    this.$store.dispatch('clearError')
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  padding: 20px 0;
}

.card {
  border: none;
  border-radius: 15px;
}

.input-group-text {
  background-color: #f8f9fa;
  border-right: none;
}

.form-control {
  border-left: none;
}

.form-control:focus {
  box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
  border-color: #667eea;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
  padding: 12px;
  font-weight: 500;
}

.btn-primary:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.text-primary {
  color: #667eea !important;
}
</style>