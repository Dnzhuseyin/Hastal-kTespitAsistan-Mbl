package com.example.fonksiyonel.ui.diagnose

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fonksiyonel.data.models.AnalysisState
import com.example.fonksiyonel.data.models.DiagnoseResult
import com.example.fonksiyonel.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnoseResultScreen(
    viewModel: DiagnoseViewModel,
    onBackToImageSelection: () -> Unit,
    onNewAnalysis: () -> Unit
) {
    val analysisState by viewModel.analysisState.collectAsState()
    val imageState by viewModel.imageState.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        if (analysisState !is AnalysisState.Success) {
            onBackToImageSelection()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Analiz Sonuçları",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackToImageSelection) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.shadow(8.dp)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (analysisState is AnalysisState.Success) {
                val result = (analysisState as AnalysisState.Success).result
                
                // Determine if healthy or not
                val isHealthy = result.disease.contains("Sağlıklı", ignoreCase = true)
                val statusColor = if (isHealthy) Healthy else Warning
                val statusIcon = if (isHealthy) Icons.Rounded.CheckCircle else Icons.Rounded.Warning
                val statusText = if (isHealthy) "Sağlıklı" else "Dikkat Gerekiyor"
                
                // Animate progress
                val progressAnimation = remember { Animatable(0f) }
                LaunchedEffect(result) {
                    progressAnimation.animateTo(
                        targetValue = result.confidence,
                        animationSpec = tween(1500, easing = EaseOutQuart)
                    )
                }
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Status header
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = statusColor
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = statusIcon,
                                contentDescription = statusText,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Text(
                                text = statusText,
                                color = Color.White,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Analyzed image
                    Card(
                        modifier = Modifier
                            .size(250.dp),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (imageState.uri != null) {
                                AsyncImage(
                                    model = imageState.uri,
                                    contentDescription = "Analyzed Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Image,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(64.dp)
                                    )
                                }
                            }
                            
                            // Status overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .background(statusColor.copy(alpha = 0.8f))
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = result.disease,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Diagnosis details
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Analytics,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Text(
                                    text = "Analiz Sonuçları",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            // Confidence progress
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Güven Oranı",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(statusColor),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "%",
                                                color = Color.White,
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        
                                        Spacer(modifier = Modifier.width(8.dp))
                                        
                                        Text(
                                            text = "${(progressAnimation.value * 100).toInt()}",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = statusColor
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                LinearProgressIndicator(
                                    progress = { progressAnimation.value },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(12.dp)
                                        .clip(RoundedCornerShape(6.dp)),
                                    color = statusColor,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }
                            
                            Divider(
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            
                            // Description
                            Column {
                                Text(
                                    text = "Tespit Edilen Durum",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    )
                                ) {
                                    Text(
                                        text = result.description,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                            
                            // Date
                            val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("tr"))
                            val formattedDate = dateFormat.format(Date(result.timestamp))
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.CalendarToday,
                                    contentDescription = "Date",
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.size(16.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Text(
                                    text = formattedDate,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Recommendations
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isHealthy) 
                                Healthy.copy(alpha = 0.1f) 
                            else 
                                Warning.copy(alpha = 0.1f)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Lightbulb,
                                    contentDescription = "Recommendations",
                                    tint = statusColor,
                                    modifier = Modifier.size(24.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Text(
                                    text = "Öneriler",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = statusColor
                                )
                            }
                            
                            result.recommendations.forEachIndexed { index, recommendation ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    )
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.Top,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(statusColor),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${index + 1}",
                                                color = Color.White,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        
                                        Spacer(modifier = Modifier.width(12.dp))
                                        
                                        Text(
                                            text = recommendation,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Disclaimer
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Text(
                                text = "Bu analiz sonuçları sadece bilgilendirme amaçlıdır. Kesin teşhis ve tedavi için lütfen bir sağlık kuruluşuna başvurun.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // New Analysis Button
                    Button(
                        onClick = {
                            viewModel.resetAnalysis()
                            onNewAnalysis()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AddPhotoAlternate,
                                contentDescription = "New Analysis",
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = "Yeni Analiz", 
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else {
                // Fallback in case we somehow end up here without results
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(56.dp),
                        strokeWidth = 4.dp
                    )
                }
            }
        }
    }
} 