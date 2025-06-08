<template>
  <div class="chat-assistant">
    <!-- Chat Toggle Button -->
    <button
        v-if="!isOpen"
        @click="toggleChat"
        class="chat-toggle-btn btn btn-primary rounded-circle"
        title="Chat with AI Assistant"
    >
      <i class="fas fa-comments"></i>
    </button>

    <!-- Chat Window -->
    <div v-if="isOpen" class="chat-window card shadow-lg">
      <!-- Chat Header -->
      <div class="chat-header card-header bg-primary text-white d-flex justify-content-between align-items-center">
        <div class="d-flex align-items-center">
          <i class="fas fa-robot me-2"></i>
          <span class="fw-bold">Healthcare AI Assistant</span>
        </div>
        <div class="d-flex align-items-center">
          <button @click="minimizeChat" class="btn btn-sm btn-outline-light me-2" title="Minimize">
            <i class="fas fa-minus"></i>
          </button>
          <button @click="closeChat" class="btn btn-sm btn-outline-light" title="Close">
            <i class="fas fa-times"></i>
          </button>
        </div>
      </div>

      <!-- Chat Messages -->
      <div class="chat-messages card-body" ref="messagesContainer">
        <div v-if="messages.length === 0" class="welcome-message text-center text-muted py-4">
          <i class="fas fa-robot fa-3x mb-3"></i>
          <h6>Welcome to Healthcare AI Assistant!</h6>
          <p class="small">I can help you with patient management, scheduling, reports, and general healthcare questions.</p>
          <div class="quick-suggestions">
            <button
                v-for="suggestion in quickSuggestions"
                :key="suggestion"
                @click="sendMessage(suggestion)"
                class="btn btn-sm btn-outline-primary me-2 mb-2"
            >
              {{ suggestion }}
            </button>
          </div>
        </div>

        <div
            v-for="message in messages"
            :key="message.id"
            :class="['message', message.isUser ? 'user-message' : 'ai-message']"
        >
          <div class="message-content">
            <div class="message-text">{{ message.text }}</div>
            <div class="message-time">{{ formatTime(message.timestamp) }}</div>
          </div>
          <div v-if="!message.isUser && message.suggestion" class="suggestion mt-2">
            <button
                @click="sendMessage(message.suggestion)"
                class="btn btn-sm btn-outline-secondary"
            >
              {{ message.suggestion }}
            </button>
          </div>
        </div>

        <div v-if="isTyping" class="typing-indicator ai-message">
          <div class="message-content">
            <div class="typing-dots">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>
        </div>
      </div>

      <!-- Chat Input -->
      <div class="chat-input card-footer">
        <div class="input-group">
          <input
              v-model="currentMessage"
              @keypress.enter="sendMessage()"
              type="text"
              class="form-control"
              placeholder="Ask me anything about healthcare management..."
              :disabled="isTyping"
          />
          <button
              @click="sendMessage()"
              :disabled="!currentMessage.trim() || isTyping"
              class="btn btn-primary"
          >
            <i class="fas fa-paper-plane"></i>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { healthcareApi } from '../services/api'

export default {
  name: 'ChatAssistant',
  data() {
    return {
      isOpen: false,
      currentMessage: '',
      messages: [],
      isTyping: false,
      quickSuggestions: [
        'How to add a patient?',
        'Schedule an appointment',
        'Generate reports',
        'View patient records'
      ]
    }
  },
  methods: {
    toggleChat() {
      this.isOpen = !this.isOpen
      if (this.isOpen) {
        this.$nextTick(() => {
          this.scrollToBottom()
        })
      }
    },

    minimizeChat() {
      this.isOpen = false
    },

    closeChat() {
      this.isOpen = false
      // Optional: Clear messages when closing
      // this.messages = []
    },

    async sendMessage(messageText = null) {
      const text = messageText || this.currentMessage.trim()
      if (!text) return

      // Add user message
      const userMessage = {
        id: Date.now(),
        text: text,
        isUser: true,
        timestamp: new Date()
      }
      this.messages.push(userMessage)
      this.currentMessage = ''
      this.isTyping = true

      this.$nextTick(() => {
        this.scrollToBottom()
      })

      try {
        // Get current route for context
        const context = this.getCurrentContext()

        const response = await healthcareApi.askChatQuestion({
          question: text,
          context: context,
          userId: this.$store.getters.user?.id
        })

        // Add AI response
        const aiMessage = {
          id: Date.now() + 1,
          text: response.data.response,
          isUser: false,
          timestamp: new Date(),
          suggestion: response.data.suggestion
        }
        this.messages.push(aiMessage)

      } catch (error) {
        // Add error message
        const errorMessage = {
          id: Date.now() + 1,
          text: 'Sorry, I encountered an error. Please try again.',
          isUser: false,
          timestamp: new Date()
        }
        this.messages.push(errorMessage)
      } finally {
        this.isTyping = false
        this.$nextTick(() => {
          this.scrollToBottom()
        })
      }
    },

    getCurrentContext() {
      const route = this.$route.name
      const contextMap = {
        'Dashboard': 'User is on the main dashboard',
        'Patients': 'User is viewing patient records',
        'Appointments': 'User is managing appointments',
        'Reports': 'User is in the reports section'
      }
      return contextMap[route] || 'General healthcare management'
    },

    formatTime(timestamp) {
      return timestamp.toLocaleTimeString('en-US', {
        hour: '2-digit',
        minute: '2-digit'
      })
    },

    scrollToBottom() {
      const container = this.$refs.messagesContainer
      if (container) {
        container.scrollTop = container.scrollHeight
      }
    }
  }
}
</script>

<style scoped>
.chat-assistant {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
}

.chat-toggle-btn {
  width: 60px;
  height: 60px;
  font-size: 24px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  border: none;
}

.chat-toggle-btn:hover {
  transform: scale(1.05);
}

.chat-window {
  width: 380px;
  height: 500px;
  border: none;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
}

.chat-header {
  border-radius: 12px 12px 0 0;
  padding: 12px 16px;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  max-height: 350px;
}

.message {
  margin-bottom: 16px;
}

.user-message {
  text-align: right;
}

.user-message .message-content {
  background: #007bff;
  color: white;
  border-radius: 18px 18px 4px 18px;
  padding: 10px 14px;
  display: inline-block;
  max-width: 80%;
}

.ai-message .message-content {
  background: #f8f9fa;
  color: #333;
  border-radius: 18px 18px 18px 4px;
  padding: 10px 14px;
  display: inline-block;
  max-width: 80%;
}

.message-text {
  margin-bottom: 4px;
}

.message-time {
  font-size: 11px;
  opacity: 0.7;
}

.typing-indicator .typing-dots {
  display: flex;
  gap: 4px;
  padding: 8px 0;
}

.typing-dots span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: #666;
  animation: typing 1.4s infinite ease-in-out;
}

.typing-dots span:nth-child(1) { animation-delay: -0.32s; }
.typing-dots span:nth-child(2) { animation-delay: -0.16s; }

@keyframes typing {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}

.chat-input {
  border-radius: 0 0 12px 12px;
  padding: 12px;
}

.welcome-message {
  animation: fadeIn 0.5s ease-in;
}

.quick-suggestions button {
  font-size: 12px;
}

.suggestion button {
  font-size: 12px;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .chat-window {
    width: 320px;
    height: 450px;
  }

  .chat-assistant {
    bottom: 10px;
    right: 10px;
  }
}
</style>