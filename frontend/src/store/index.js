import { createStore } from 'vuex'
import api from '../services/api'

export default createStore({
    state: {
        user: null,
        token: null,
        isAuthenticated: false,
        loading: false,
        error: null
    },
    mutations: {
        SET_USER(state, user) {
            state.user = user
        },
        SET_TOKEN(state, token) {
            state.token = token
            state.isAuthenticated = !!token
        },
        SET_LOADING(state, status) {
            state.loading = status
        },
        SET_ERROR(state, error) {
            state.error = error
        },
        CLEAR_ERROR(state) {
            state.error = null
        },
        LOGOUT(state) {
            state.user = null
            state.token = null
            state.isAuthenticated = false
        }
    },
    actions: {
        async login({ commit }, credentials) {
            try {
                commit('SET_LOADING', true)
                commit('CLEAR_ERROR')

                const response = await api.post('/auth/login', credentials)
                const { jwt, username } = response.data

                // Store token and user info
                commit('SET_TOKEN', jwt)
                commit('SET_USER', { username })

                // Store in localStorage
                localStorage.setItem('jwt_token', jwt)
                localStorage.setItem('username', username)

                return response.data
            } catch (error) {
                const errorMessage = error.response?.data || 'Login failed'
                commit('SET_ERROR', errorMessage)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        async register({ commit }, userData) {
            try {
                commit('SET_LOADING', true)
                commit('CLEAR_ERROR')

                const response = await api.post('/auth/register', userData)
                const { jwt, username } = response.data

                // Store token and user info
                commit('SET_TOKEN', jwt)
                commit('SET_USER', { username })

                // Store in localStorage
                localStorage.setItem('jwt_token', jwt)
                localStorage.setItem('username', username)

                return response.data
            } catch (error) {
                const errorMessage = error.response?.data || 'Registration failed'
                commit('SET_ERROR', errorMessage)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        initializeAuth({ commit }, token) {
            const username = localStorage.getItem('username')
            if (token && username) {
                commit('SET_TOKEN', token)
                commit('SET_USER', { username })
            }
        },

        logout({ commit }) {
            commit('LOGOUT')
            localStorage.removeItem('jwt_token')
            localStorage.removeItem('username')
        },

        clearError({ commit }) {
            commit('CLEAR_ERROR')
        }
    },
    getters: {
        isAuthenticated: state => state.isAuthenticated,
        user: state => state.user,
        token: state => state.token,
        loading: state => state.loading,
        error: state => state.error,
        userRole: state => {
            // This would typically come from the JWT token or user object
            // For now, returning a default role
            return state.user ? 'ROLE_USER' : null
        }
    }
})