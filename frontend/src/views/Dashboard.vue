<template>
  <div class="dashboard">
    <div class="container-fluid">
      <!-- Header Section -->
      <div class="row mb-4">
        <div class="col">
          <div class="d-flex justify-content-between align-items-center">
            <div>
              <h1 class="h3 mb-0">Dashboard</h1>
              <p class="text-muted">Welcome back, {{ user?.username }}!</p>
            </div>
            <div class="text-end">
              <small class="text-muted">{{ currentDate }}</small>
            </div>
          </div>
        </div>
      </div>

      <!-- Stats Cards -->
      <div class="row mb-4">
        <div class="col-md-3 mb-3">
          <div class="card stats-card bg-primary text-white">
            <div class="card-body">
              <div class="d-flex justify-content-between align-items-center">
                <div>
                  <h6 class="card-title mb-1">Total Patients</h6>
                  <h3 class="mb-0">{{ stats.totalPatients }}</h3>
                </div>
                <i class="fas fa-users fa-2x opacity-75"></i>
              </div>
            </div>
          </div>
        </div>

        <div class="col-md-3 mb-3">
          <div class="card stats-card bg-success text-white">
            <div class="card-body">
              <div class="d-flex justify-content-between align-items-center">
                <div>
                  <h6 class="card-title mb-1">Appointments Today</h6>
                  <h3 class="mb-0">{{ stats.appointmentsToday }}</h3>
                </div>
                <i class="fas fa-calendar-check fa-2x opacity-75"></i>
              </div>
            </div>
          </div>
        </div>

        <div class="col-md-3 mb-3">
          <div class="card stats-card bg-warning text-white">
            <div class="card-body">
              <div class="d-flex justify-content-between align-items-center">
                <div>
                  <h6 class="card-title mb-1">Pending Reviews</h6>
                  <h3 class="mb-0">{{ stats.pendingReviews }}</h3>
                </div>
                <i class="fas fa-clock fa-2x opacity-75"></i>
              </div>
            </div>
          </div>
        </div>

        <div class="col-md-3 mb-3">
          <div class="card stats-card bg-info text-white">
            <div class="card-body">
              <div class="d-flex justify-content-between align-items-center">
                <div>
                  <h6 class="card-title mb-1">Active Doctors</h6>
                  <h3 class="mb-0">{{ stats.activeDoctors }}</h3>
                </div>
                <i class="fas fa-user-md fa-2x opacity-75"></i>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Main Content -->
      <div class="row">
        <!-- Recent Activities -->
        <div class="col-lg-6 mb-4">
          <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
              <h5 class="mb-0">
                <i class="fas fa-history me-2"></i>
                Recent Activities
              </h5>
              <button class="btn btn-sm btn-outline-primary">View All</button>
            </div>
            <div class="card-body">
              <div v-if="loading.activities" class="text-center py-3">
                <div class="spinner-border text-primary" role="status"></div>
              </div>

              <div v-else-if="activities.length === 0" class="text-center py-3 text-muted">
                <i class="fas fa-inbox fa-2x mb-2"></i>
                <p>No recent activities</p>
              </div>

              <div v-else class="activity-list">
                <div
                    v-for="activity in activities"
                    :key="activity.id"
                    class="activity-item d-flex align-items-center py-2 border-bottom"
                >
                  <div class="activity-icon me-3">
                    <i :class="getActivityIcon(activity.type)" class="text-primary"></i>
                  </div>
                  <div class="flex-grow-1">
                    <p class="mb-1">{{ activity.description }}</p>
                    <small class="text-muted">{{ formatDate(activity.timestamp) }}</small>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Quick Actions -->
        <div class="col-lg-6 mb-4">
          <div class="card">
            <div class="card-header">
              <h5 class="mb-0">
                <i class="fas fa-bolt me-2"></i>
                Quick Actions
              </h5>
            </div>
            <div class="card-body">
              <div class="row g-3">
                <div class="col-6">
                  <button class="btn btn-outline-primary w-100 d-flex flex-column align-items-center py-3">
                    <i class="fas fa-user-plus fa-2x mb-2"></i>
                    <span>Add Patient</span>
                  </button>
                </div>
                <div class="col-6">
                  <button class="btn btn-outline-success w-100 d-flex flex-column align-items-center py-3">
                    <i class="fas fa-calendar-plus fa-2x mb-2"></i>
                    <span>Schedule</span>
                  </button>
                </div>
                <div class="col-6">
                  <router-link to="/patients" class="btn btn-outline-info w-100 d-flex flex-column align-items-center py-3 text-decoration-none">
                    <i class="fas fa-list fa-2x mb-2"></i>
                    <span>View Patients</span>
                  </router-link>
                </div>
                <div class="col-6">
                  <button class="btn btn-outline-warning w-100 d-flex flex-column align-items-center py-3">
                    <i class="fas fa-chart-bar fa-2x mb-2"></i>
                    <span>Reports</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Dashboard Content -->
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h5 class="mb-0">
                <i class="fas fa-tachometer-alt me-2"></i>
                Dashboard Data
              </h5>
            </div>
            <div class="card-body">
              <div v-if="loading.dashboard" class="text-center py-4">
                <div class="spinner-border text-primary" role="status"></div>
                <p class="mt-2 text-muted">Loading dashboard data...</p>
              </div>

              <div v-else-if="error" class="alert alert-danger" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>
                {{ error }}
              </div>

              <div v-else class="dashboard-content">
                <div class="alert alert-info" role="alert">
                  <i class="fas fa-info-circle me-2"></i>
                  {{ dashboardData }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Chat Assistant Component -->
    <ChatAssistant />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { healthcareApi } from '../services/api'
import ChatAssistant from '../components/ChatAssistant.vue'

export default {
  name: 'Dashboard',
  components: {
    ChatAssistant
  },
  data() {
    return {
      dashboardData: '',
      activities: [
        {
          id: 1,
          type: 'appointment',
          description: 'New appointment scheduled with Dr. Smith',
          timestamp: new Date(Date.now() - 1000 * 60 * 30) // 30 minutes ago
        },
        {
          id: 2,
          type: 'patient',
          description: 'Patient John Doe updated medical records',
          timestamp: new Date(Date.now() - 1000 * 60 * 60 * 2) // 2 hours ago
        },
        {
          id: 3,
          type: 'system',
          description: 'System backup completed successfully',
          timestamp: new Date(Date.now() - 1000 * 60 * 60 * 4) // 4 hours ago
        }
      ],
      stats: {
        totalPatients: 1247,
        appointmentsToday: 23,
        pendingReviews: 8,
        activeDoctors: 12
      },
      loading: {
        dashboard: false,
        activities: false
      },
      error: null
    }
  },
  computed: {
    ...mapGetters(['user']),
    currentDate() {
      return new Date().toLocaleDateString('en-US', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      })
    }
  },
  methods: {
    async loadDashboardData() {
      try {
        this.loading.dashboard = true
        this.error = null

        const response = await healthcareApi.getDashboard()
        this.dashboardData = response.data
      } catch (error) {
        this.error = error.response?.data || 'Failed to load dashboard data'
      } finally {
        this.loading.dashboard = false
      }
    },

    getActivityIcon(type) {
      const icons = {
        appointment: 'fas fa-calendar-check',
        patient: 'fas fa-user',
        system: 'fas fa-cog',
        default: 'fas fa-bell'
      }
      return icons[type] || icons.default
    },

    formatDate(date) {
      const now = new Date()
      const diff = now - new Date(date)
      const minutes = Math.floor(diff / (1000 * 60))
      const hours = Math.floor(diff / (1000 * 60 * 60))
      const days = Math.floor(diff / (1000 * 60 * 60 * 24))

      if (minutes < 60) {
        return `${minutes} minutes ago`
      } else if (hours < 24) {
        return `${hours} hours ago`
      } else {
        return `${days} days ago`
      }
    }
  },

  async mounted() {
    await this.loadDashboardData()
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px 0;
}

.stats-card {
  border: none;
  border-radius: 12px;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.stats-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.card {
  border: none;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  transition: box-shadow 0.2s ease;
}

.card:hover {
  box-shadow: 0 4px 16px rgba(0,0,0,0.12);
}

.card-header {
  background-color: #f8f9fa;
  border-bottom: 1px solid #dee2e6;
  border-radius: 12px 12px 0 0 !important;
}

.activity-item:last-child {
  border-bottom: none !important;
}

.activity-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8f9fa;
  border-radius: 50%;
}

.btn {
  border-radius: 8px;
  transition: all 0.2s ease;
}

.btn:hover {
  transform: translateY(-1px);
}

.opacity-75 {
  opacity: 0.75;
}
</style>