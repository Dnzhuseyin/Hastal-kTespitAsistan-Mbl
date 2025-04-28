package com.example.fonksiyonel.ui.diagnose

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.fonksiyonel.data.models.AnalysisState
import com.example.fonksiyonel.data.models.DiagnoseResult
import com.example.fonksiyonel.data.models.ImageState

class DiagnoseViewModel : ViewModel() {
    
    private val _imageState = MutableStateFlow(ImageState())
    val imageState: StateFlow<ImageState> = _imageState
    
    private val _analysisState = MutableStateFlow<AnalysisState>(AnalysisState.Idle)
    val analysisState: StateFlow<AnalysisState> = _analysisState
    
    fun setSelectedImage(uri: Uri?) {
        _imageState.value = _imageState.value.copy(uri = uri)
    }
    
    fun analyzeImage() {
        if (_imageState.value.uri == null) {
            _analysisState.value = AnalysisState.Error("Lütfen önce bir görüntü seçin.")
            return
        }
        
        _analysisState.value = AnalysisState.Loading
        
        // This would be replaced with actual model inference in a real app
        // For now, we'll simulate a response after a delay
        val mockResult = DiagnoseResult(
            id = "diag123",
            userId = "user123",
            imageUrl = _imageState.value.uri.toString(),
            disease = "Sağlıklı Cilt",
            confidence = 0.94f,
            description = "Analiz sonucuna göre cildiniz sağlıklı görünüyor ve herhangi bir hastalık belirtisi tespit edilmedi.",
            recommendations = listOf(
                "Günlük cilt bakım rutininizi sürdürün",
                "Güneş koruyucu kullanmaya devam edin",
                "Bol su için ve sağlıklı beslenin"
            )
        )
        
        // Simulating a response with a random result (healthy or not)
        val random = Math.random()
        val finalResult = if (random > 0.5) {
            mockResult
        } else {
            mockResult.copy(
                disease = "Sedef Hastalığı",
                confidence = 0.87f,
                description = "Analiz sonucuna göre görüntüde sedef hastalığı belirtileri tespit edildi. Kesin tanı için mutlaka bir dermatoloğa başvurmanız önerilir.",
                recommendations = listOf(
                    "En kısa sürede bir dermatoloğa başvurun",
                    "Cildinizi nemli tutun",
                    "Alkol ve sigara kullanımından kaçının",
                    "Stresten uzak durmaya çalışın"
                )
            )
        }
        
        // Simulate delay for analysis
        android.os.Handler().postDelayed({
            _analysisState.value = AnalysisState.Success(finalResult)
        }, 2000)
    }
    
    fun resetAnalysis() {
        _analysisState.value = AnalysisState.Idle
        _imageState.value = ImageState()
    }
} 