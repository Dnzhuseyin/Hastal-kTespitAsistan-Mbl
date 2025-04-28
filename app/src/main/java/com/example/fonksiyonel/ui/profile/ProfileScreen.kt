package com.example.fonksiyonel.ui.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fonksiyonel.ui.theme.Accent1
import com.example.fonksiyonel.ui.theme.Accent2
import com.example.fonksiyonel.ui.theme.Primary
import com.example.fonksiyonel.ui.theme.Secondary

@Composable
fun ProfileInfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToImageSelection: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val profileState by viewModel.profileState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Profil",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        onNavigateToLogin()
                    }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
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
            when (profileState) {
                is ProfileState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(50.dp),
                            strokeWidth = 5.dp
                        )
                    }
                }
                is ProfileState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(64.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = (profileState as ProfileState.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Button(
                                onClick = onNavigateToLogin,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text("Giriş Yap")
                            }
                        }
                    }
                }
                is ProfileState.Success -> {
                    val user = (profileState as ProfileState.Success).user
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Profile header with avatar
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(Primary, Secondary)
                                        )
                                    )
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // Avatar
                                    Box(
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.2f))
                                            .padding(4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape)
                                                .background(Color.White),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = user.fullName.take(1).uppercase(),
                                                style = MaterialTheme.typography.headlineLarge,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    // Name
                                    Text(
                                        text = user.fullName,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    
                                    // Email
                                    Text(
                                        text = user.email,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Personal Info Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Person,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    
                                    Spacer(modifier = Modifier.width(8.dp))
                                    
                                    Text(
                                        text = "Kişisel Bilgiler",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                ProfileInfoItem(
                                    icon = Icons.Rounded.Person,
                                    label = "Ad Soyad",
                                    value = user.fullName
                                )
                                
                                ProfileInfoItem(
                                    icon = Icons.Rounded.Email,
                                    label = "Email",
                                    value = user.email
                                )
                                
                                ProfileInfoItem(
                                    icon = Icons.Rounded.DateRange,
                                    label = "Yaş",
                                    value = user.age.toString()
                                )
                                
                                ProfileInfoItem(
                                    icon = Icons.Rounded.Face,
                                    label = "Cinsiyet",
                                    value = user.gender
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Hastalık Tespit Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToImageSelection() },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Accent1.copy(alpha = 0.05f)
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    brush = Brush.linearGradient(
                                                        colors = listOf(Accent1, Accent2)
                                                    )
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.HealthAndSafety,
                                                contentDescription = "Health Check",
                                                tint = Color.White,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        
                                        Spacer(modifier = Modifier.width(16.dp))
                                        
                                        Text(
                                            text = "Hastalık Tespiti",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    
                                    Icon(
                                        imageVector = Icons.Rounded.ArrowForward,
                                        contentDescription = "Navigate",
                                        tint = Accent1
                                    )
                                }
                                
                                Text(
                                    text = "Görüntü yükleyerek potansiyel hastalıkları tespit edin",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Button(
                                    onClick = { onNavigateToImageSelection() },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Accent1
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.AddAPhoto,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    
                                    Spacer(modifier = Modifier.width(8.dp))
                                    
                                    Text("Görüntü Yükle", fontSize = 16.sp)
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        // Sağlık ipuçları Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Lightbulb,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    
                                    Spacer(modifier = Modifier.width(8.dp))
                                    
                                    Text(
                                        text = "Sağlık İpuçları",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                // Health tip items
                                HealthTipItem(
                                    icon = Icons.Rounded.WaterDrop,
                                    tip = "Günde en az 2 litre su içmeyi unutmayın"
                                )
                                
                                HealthTipItem(
                                    icon = Icons.Rounded.DirectionsWalk,
                                    tip = "Düzenli egzersiz yaparak sağlığınızı koruyun"
                                )
                                
                                HealthTipItem(
                                    icon = Icons.Rounded.Bedtime,
                                    tip = "Yeterli uyku sağlıklı bir yaşam için temeldir"
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun HealthTipItem(
    icon: ImageVector,
    tip: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = tip,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
} 