import axios from 'axios'

// Create axios instance
const api = axios.create({
    baseURL: process.env.VUE_APP_API_URL || 'http://localhost:8081/api/v1',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// Request interceptor to add JWT token
api.interceptors.request.use(
    config => {
        const token = localStorage.getItem('jwt_token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// Response interceptor to handle errors
api.interceptors.response.use(
    response => response,
    error => {
        if (error.response?.status === 401) {
            // Token expired or invalid
            localStorage.removeItem('jwt_token')
            localStorage.removeItem('username')
            window.location.href = '/login'
        }
        return Promise.reject(error)
    }
)

export default api

// Healthcare API methods
export const healthcareApi = {
    // Existing methods...

    // Authentication
    login: (credentials) => api.post('/auth/login', credentials),
    register: (userData) => api.post('/auth/register', userData),
    logout: () => api.post('/auth/logout'),

    // Dashboard
    getDashboard: () => api.get('/healthcare/dashboard'),

    // Patients
    getPatients: (params) => api.get('/healthcare/patients', { params }),
    getPatient: (id) => api.get(`/healthcare/patients/${id}`),
    createPatient: (patientData) => api.post('/healthcare/patients', patientData),
    updatePatient: (id, patientData) => api.put(`/healthcare/patients/${id}`, patientData),
    deletePatient: (id) => api.delete(`/healthcare/patients/${id}`),

    // Appointments
    getAppointments: (params) => api.get('/healthcare/appointments', { params }),
    getAppointment: (id) => api.get(`/healthcare/appointments/${id}`),
    createAppointment: (appointmentData) => api.post('/healthcare/appointments', appointmentData),
    updateAppointment: (id, appointmentData) => api.put(`/healthcare/appointments/${id}`, appointmentData),
    deleteAppointment: (id) => api.delete(`/healthcare/appointments/${id}`),

    // Reports
    getReports: (params) => api.get('/healthcare/reports', { params }),
    generateReport: (reportData) => api.post('/healthcare/reports/generate', reportData),

    // Chat AI Methods - NEW
    askChatQuestion: (chatRequest) => api.post('/chat/ask', chatRequest),
    getChatHealth: () => api.get('/chat/health'),

    // Analytics
    getAnalytics: (params) => api.get('/analytics', { params }),

    // Settings
    getSettings: () => api.get('/settings'),
    updateSettings: (settings) => api.put('/settings', settings)
}