package com.example.fonksiyonel.data.models

import android.net.Uri

data class DiagnoseResult(
    val id: String = "",
    val userId: String = "",
    val imageUrl: String = "",
    val disease: String = "",
    val confidence: Float = 0f,
    val description: String = "",
    val recommendations: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

data class ImageState(
    val uri: Uri? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class AnalysisState {
    object Idle : AnalysisState()
    object Loading : AnalysisState()
    data class Success(val result: DiagnoseResult) : AnalysisState()
    data class Error(val message: String) : AnalysisState()
} 