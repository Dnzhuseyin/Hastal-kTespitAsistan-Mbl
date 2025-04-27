package com.example.fonksiyonel.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fonksiyonel.data.models.User
import com.example.fonksiyonel.data.repository.AuthRepository
import com.example.fonksiyonel.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val currentUser = authRepository.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                userRepository.getUserData(currentUser.uid).collect { result ->
                    result.fold(
                        onSuccess = { _profileState.value = ProfileState.Success(it) },
                        onFailure = { _profileState.value = ProfileState.Error(it.message ?: "Unknown error") }
                    )
                }
            }
        } else {
            _profileState.value = ProfileState.Error("User not logged in")
        }
    }

    fun logout() {
        authRepository.logout()
    }
}

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
} 