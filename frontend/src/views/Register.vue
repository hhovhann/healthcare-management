<template>
  <div class="register-container">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-6 col-lg-4">
          <div class="card shadow-lg">
            <div class="card-body p-5">
              <div class="text-center mb-4">
                <i class="fas fa-user-plus text-success" style="font-size: 3rem;"></i>
                <h2 class="mt-3 mb-2">Create Account</h2>
                <p class="text-muted">Join our healthcare platform</p>
              </div>

              <form @submit.prevent="handleRegister">
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
                        placeholder="Choose a username"
                        required
                        :disabled="loading"
                        minlength="3"
                    >
                  </div>
                </div>

                <div class="mb-3">
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
                        placeholder="Create a password"
                        required
                        :disabled="loading"
                        minlength="6"
                    >
                  </div>
                </div>

                <div class="mb-4">
                  <label for="confirmPassword" class="form-label">Confirm Password</label>
                  <div class="input-group">
                    <span class="input-group-text">
                      <i class="fas fa-lock text-muted"></i>
                    </span>
                    <input
                        id="confirmPassword"
                        v-model="form.confirmPassword"
                        type="password"
                        class="form-control"
                        placeholder="Confirm your password"
                        required
                        :disabled="loading"
                        minlength="6"
                    >
                  </div>
                </div>

                <div class="alert alert-danger" v-if="error" role="alert">
                  <i class="fas fa-exclamation-triangle me-2"></i>
                  {{ error }}
                </div>

                <div class="alert alert-warning" v-if="passwordMismatch" role="alert">
                  <i class="fas fa-exclamation-triangle me-2"></i>
                  Passwords do not match
                </div>

                <button
                    type="submit"
                    class="btn btn-success w-100 mb-3"
                    :disabled="loading || passwordMismatch"
                >
                  <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                  <i v-else class="fas fa-user-plus me-2"></i>
                  {{ loading ? 'Creating Account...' : 'Create Account' }}
                </button>

                <div class="text-center">
                  <p class="mb-0">
                    Already have an account?
                    <router-link to="/login" class="text-success text-decoration-none">
                      Sign in
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
  name: 'Register',
  data() {
    return {
      form: {
        username: '',
        password: '',
        confirmPassword: ''
      }
    }
  },
  computed: {
    ...mapGetters(['loading', 'error']),
    passwordMismatch() {
      return this.form.password && this.form.confirmPassword &&
          this.form.password !== this.form.confirmPassword
    }
  },
  methods: {
    async handleRegister() {
      if (this.passwordMismatch) {
        return
      }

      try {
        const {confirmPassword, ...registrationData} = this.form
        await this.$store.dispatch('register', registrationData)
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
.register-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
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
  box-shadow: 0 0 0 0.2rem rgba(17, 153, 142, 0.25);
  border-color: #11998e;
}

.btn-success {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  border: none;
  border-radius: 8px;
  padding: 12px;
  font-weight: 500;
}

.btn-success:hover {
  background: linear-gradient(135deg, #0f8a7e 0%, #32d470 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.text-success {
  color: #11998e !important;
}
</style>