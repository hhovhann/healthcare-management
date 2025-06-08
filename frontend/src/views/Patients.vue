<template>
  <div class="patients">
    <div class="container-fluid">
      <!-- Header Section -->
      <div class="row mb-4">
        <div class="col">
          <div class="d-flex justify-content-between align-items-center">
            <div>
              <h1 class="h3 mb-0">Patient Management</h1>
              <p class="text-muted">Manage patient records and information</p>
            </div>
            <button class="btn btn-primary">
              <i class="fas fa-plus me-2"></i>
              Add New Patient
            </button>
          </div>
        </div>
      </div>

      <!-- Search and Filters -->
      <div class="row mb-4">
        <div class="col-md-6">
          <div class="input-group">
            <span class="input-group-text">
              <i class="fas fa-search"></i>
            </span>
            <input
                v-model="searchQuery"
                type="text"
                class="form-control"
                placeholder="Search patients by name, ID, or phone..."
            >
          </div>
        </div>
        <div class="col-md-3">
          <select v-model="statusFilter" class="form-select">
            <option value="">All Status</option>
            <option value="active">Active</option>
            <option value="inactive">Inactive</option>
            <option value="critical">Critical</option>
          </select>
        </div>
        <div class="col-md-3">
          <select v-model="sortBy" class="form-select">
            <option value="name">Sort by Name</option>
            <option value="date">Sort by Date</option>
            <option value="id">Sort by ID</option>
          </select>
        </div>
      </div>

      <!-- Patients Data -->
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
              <h5 class="mb-0">
                <i class="fas fa-users me-2"></i>
                Patient Records
              </h5>
              <span class="badge bg-primary">{{ filteredPatients.length }} patients</span>
            </div>
            <div class="card-body">
              <div v-if="loading.patients" class="text-center py-4">
                <div class="spinner-border text-primary" role="status"></div>
                <p class="mt-2 text-muted">Loading patient data...</p>
              </div>

              <div v-else-if="error" class="alert alert-danger" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>
                {{ error }}
              </div>

              <div v-else-if="filteredPatients.length === 0" class="text-center py-4 text-muted">
                <i class="fas fa-user-slash fa-3x mb-3"></i>
                <h5>No patients found</h5>
                <p>{{ searchQuery ? 'Try adjusting your search criteria' : 'No patient records available' }}</p>
              </div>

              <div v-else>
                <!-- API Response Display -->
                <div class="alert alert-info mb-4" role="alert">
                  <i class="fas fa-info-circle me-2"></i>
                  <strong>API Response:</strong> {{ patientsData }}
                </div>

                <!-- Sample Patient Table -->
                <div class="table-responsive">
                  <table class="table table-hover">
                    <thead class="table-light">
                    <tr>
                      <th>Patient ID</th>
                      <th>Name</th>
                      <th>Age</th>
                      <th>Phone</th>
                      <th>Last Visit</th>
                      <th>Status</th>
                      <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="patient in filteredPatients" :key="patient.id">
                      <td>
                        <span class="fw-bold text-primary">#{{ patient.id }}</span>
                      </td>
                      <td>
                        <div class="d-flex align-items-center">
                          <div class="avatar me-3">
                            <i class="fas fa-user-circle fa-2x text-muted"></i>
                          </div>
                          <div>
                            <div class="fw-bold">{{ patient.name }}</div>
                            <small class="text-muted">{{ patient.email }}</small>
                          </div>
                        </div>
                      </td>
                      <td>{{ patient.age }}</td>
                      <td>{{ patient.phone }}</td>
                      <td>{{ formatDate(patient.lastVisit) }}</td>
                      <td>
                          <span :class="getStatusBadgeClass(patient.status)" class="badge">
                            {{ patient.status }}
                          </span>
                      </td>
                      <td>
                        <div class="btn-group" role="group">
                          <button class="btn btn-sm btn-outline-primary" title="View">
                            <i class="fas fa-eye"></i>
                          </button>
                          <button class="btn btn-sm btn-outline-secondary" title="Edit">
                            <i class="fas fa-edit"></i>
                          </button>
                          <button class="btn btn-sm btn-outline-danger" title="Delete">
                            <i class="fas fa-trash"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                    </tbody>
                  </table>
                </div>

                <!-- Pagination -->
                <nav aria-label="Patient pagination" class="mt-4">
                  <ul class="pagination justify-content-center">
                    <li class="page-item disabled">
                      <span class="page-link">Previous</span>
                    </li>
                    <li class="page-item active">
                      <span class="page-link">1</span>
                    </li>
                    <li class="page-item">
                      <a class="page-link" href="#">2</a>
                    </li>
                    <li class="page-item">
                      <a class="page-link" href="#">3</a>
                    </li>
                    <li class="page-item">
                      <a class="page-link" href="#">Next</a>
                    </li>
                  </ul>
                </nav>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {healthcareApi} from '../services/api'

export default {
  name: 'Patients',
  data() {
    return {
      patientsData: '',
      searchQuery: '',
      statusFilter: '',
      sortBy: 'name',
      loading: {
        patients: false
      },
      error: null,
      // Sample patient data for demonstration
      patients: [
        {
          id: 'P001',
          name: 'John Doe',
          email: 'john.doe@email.com',
          age: 34,
          phone: '+1-555-0123',
          lastVisit: new Date('2024-01-15'),
          status: 'active'
        },
        {
          id: 'P002',
          name: 'Jane Smith',
          email: 'jane.smith@email.com',
          age: 28,
          phone: '+1-555-0124',
          lastVisit: new Date('2024-01-10'),
          status: 'active'
        },
        {
          id: 'P003',
          name: 'Robert Johnson',
          email: 'robert.j@email.com',
          age: 45,
          phone: '+1-555-0125',
          lastVisit: new Date('2024-01-08'),
          status: 'critical'
        },
        {
          id: 'P004',
          name: 'Emily Davis',
          email: 'emily.davis@email.com',
          age: 31,
          phone: '+1-555-0126',
          lastVisit: new Date('2024-01-05'),
          status: 'inactive'
        },
        {
          id: 'P005',
          name: 'Michael Brown',
          email: 'michael.b@email.com',
          age: 52,
          phone: '+1-555-0127',
          lastVisit: new Date('2024-01-12'),
          status: 'active'
        }
      ]
    }
  },
  computed: {
    filteredPatients() {
      let filtered = this.patients

      // Apply search filter
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase()
        filtered = filtered.filter(patient =>
            patient.name.toLowerCase().includes(query) ||
            patient.id.toLowerCase().includes(query) ||
            patient.phone.includes(query) ||
            patient.email.toLowerCase().includes(query)
        )
      }

      // Apply status filter
      if (this.statusFilter) {
        filtered = filtered.filter(patient => patient.status === this.statusFilter)
      }

      // Apply sorting
      filtered.sort((a, b) => {
        if (this.sortBy === 'name') {
          return a.name.localeCompare(b.name)
        } else if (this.sortBy === 'date') {
          return new Date(b.lastVisit) - new Date(a.lastVisit)
        } else if (this.sortBy === 'id') {
          return a.id.localeCompare(b.id)
        }
        return 0
      })

      return filtered
    }
  },
  methods: {
    async loadPatientsData() {
      try {
        this.loading.patients = true
        this.error = null

        const response = await healthcareApi.getPatients()
        this.patientsData = response.data
      } catch (error) {
        this.error = error.response?.data || 'Failed to load patient data'
      } finally {
        this.loading.patients = false
      }
    },

    getStatusBadgeClass(status) {
      const classes = {
        active: 'bg-success',
        inactive: 'bg-secondary',
        critical: 'bg-danger'
      }
      return classes[status] || 'bg-primary'
    },

    formatDate(date) {
      return new Date(date).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      })
    }
  },

  async mounted() {
    await this.loadPatientsData()
  }
}
</script>

<style scoped>
.patients {
  padding: 20px 0;
}

.card {
  border: none;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.card-header {
  background-color: #f8f9fa;
  border-bottom: 1px solid #dee2e6;
  border-radius: 12px 12px 0 0 !important;
}

.table {
  margin-bottom: 0;
}

.table th {
  border-top: none;
  font-weight: 600;
  color: #495057;
}

.table-hover tbody tr:hover {
  background-color: rgba(0, 0, 0, 0.02);
}

.avatar {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-group .btn {
  margin-right: 2px;
}

.btn-group .btn:last-child {
  margin-right: 0;
}

.input-group-text {
  background-color: #f8f9fa;
  border-right: none;
}

.form-control {
  border-left: none;
}

.form-control:focus {
  box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.25);
  border-color: #86b7fe;
}

.badge {
  font-size: 0.75rem;
  padding: 0.5em 0.75em;
}
</style>