package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.AiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AiViewModel : ViewModel() {
    private val repository = AiRepository()

    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory: StateFlow<List<ChatMessage>> = _chatHistory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(text: String, branch: String? = null, semester: String? = null) {
        val userMessage = ChatMessage(text, isUser = true)
        _chatHistory.value += userMessage
        _isLoading.value = true

        viewModelScope.launch {
            val response = repository.getAiResponse(text, branch, semester)
            _chatHistory.value += ChatMessage(response, isUser = false)
            _isLoading.value = false
        }
    }

    fun clearHistory() {
        _chatHistory.value = emptyList()
    }
}

data class ChatMessage(val text: String, val isUser: Boolean)
