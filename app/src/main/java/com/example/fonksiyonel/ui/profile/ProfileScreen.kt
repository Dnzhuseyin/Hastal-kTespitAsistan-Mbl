package com.example.fonksiyonel.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fonksiyonel.ui.theme.Accent1
import com.example.fonksiyonel.ui.theme.Accent2

@Composable
fun ProfileInfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        
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
                title = { Text("Profil") },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        onNavigateToLogin()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
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
                        CircularProgressIndicator()
                    }
                }
                is ProfileState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (profileState as ProfileState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                is ProfileState.Success -> {
                    val user = (profileState as ProfileState.Success).user
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Profile header with avatar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(Accent1, Accent2)
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
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = user.fullName.take(1).uppercase(),
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
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
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Personal Info Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "Kişisel Bilgiler",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                ProfileInfoItem(
                                    icon = Icons.Default.Person,
                                    label = "Ad Soyad",
                                    value = user.fullName
                                )
                                
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                                
                                ProfileInfoItem(
                                    icon = Icons.Default.Email,
                                    label = "Email",
                                    value = user.email
                                )
                                
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                                
                                ProfileInfoItem(
                                    icon = Icons.Default.DateRange,
                                    label = "Yaş",
                                    value = user.age.toString()
                                )
                                
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                                
                                ProfileInfoItem(
                                    icon = Icons.Default.Face,
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
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.HealthAndSafety,
                                        contentDescription = "Health Check",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(16.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Hastalık Tespiti",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    
                                    Text(
                                        text = "Görüntü yükleyerek potansiyel hastalıkları tespit edin",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                                
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Navigate",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Sağlık ipuçları Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = "Sağlık İpuçları",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Health tip items
                                HealthTipItem(
                                    icon = Icons.Default.WaterDrop,
                                    tip = "Günde en az 2 litre su içmeyi unutmayın"
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                HealthTipItem(
                                    icon = Icons.Default.DirectionsWalk,
                                    tip = "Düzenli egzersiz yaparak sağlığınızı koruyun"
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                HealthTipItem(
                                    icon = Icons.Default.Bedtime,
                                    tip = "Yeterli uyku sağlıklı bir yaşam için temeldir"
                                )
                            }
                        }
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
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
                modifier = Modifier.size(24.dp)
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