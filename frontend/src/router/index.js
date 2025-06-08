import { createRouter, createWebHistory } from 'vue-router'
import store from '../store'

// Views
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Dashboard from '../views/Dashboard.vue'
import Patients from '../views/Patients.vue'
import PublicInfo from '../views/PublicInfo.vue'

const routes = [
    {
        path: '/',
        redirect: '/dashboard'
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: { requiresGuest: true }
    },
    {
        path: '/register',
        name: 'Register',
        component: Register,
        meta: { requiresGuest: true }
    },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: { requiresAuth: true }
    },
    {
        path: '/patients',
        name: 'Patients',
        component: Patients,
        meta: {
            requiresAuth: true,
            requiresRole: ['ROLE_ADMIN', 'ROLE_DOCTOR']
        }
    },
    {
        path: '/public',
        name: 'PublicInfo',
        component: PublicInfo
    },
    {
        path: '/:pathMatch(.*)*',
        redirect: '/'
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// Navigation guards
router.beforeEach((to, from, next) => {
    const isAuthenticated = store.getters.isAuthenticated
    const userRole = store.getters.userRole

    // Check if route requires authentication
    if (to.meta.requiresAuth && !isAuthenticated) {
        next('/login')
        return
    }

    // Check if route requires guest (not authenticated)
    if (to.meta.requiresGuest && isAuthenticated) {
        next('/dashboard')
        return
    }

    // Check role-based access
    if (to.meta.requiresRole && isAuthenticated) {
        const hasRequiredRole = to.meta.requiresRole.includes(userRole)
        if (!hasRequiredRole) {
            next('/dashboard')
            return
        }
    }

    next()
})

export default router