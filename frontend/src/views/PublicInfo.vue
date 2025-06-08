<template>
  <div>
    <h1>Public Data</h1>
    <p>This is accessible without authentication.</p>
    <button @click="loadPublic">Load Public Data</button>
    <p>{{ message }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const message = ref('')

const loadPublic = async () => {
  try {
    const response = await axios.get('/api/v1/healthcare/public')
    message.value = response.data
  } catch (err) {
    message.value = 'Error: ' + (err.response?.data || err.message)
  }
}
</script>
