package com.example.fonksiyonel.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fonksiyonel.ui.theme.Accent1
import com.example.fonksiyonel.ui.theme.Accent2
import com.example.fonksiyonel.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }
    
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Erkek", "Kadın", "Diğer")
    
    val registerState by viewModel.registerState.collectAsState()
    
    LaunchedEffect(registerState) {
        if (registerState is RegisterState.Success) {
            onNavigateToProfile()
            viewModel.resetStates()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Header
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Primary, Accent1, Accent2)
                        )
                    )
                    .padding(3.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PersonAdd,
                        contentDescription = "Register",
                        tint = Primary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Yeni Hesap Oluştur",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "Sağlık asistanını kullanmak için kayıt ol",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Register Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Full Name Field
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Ad Soyad") },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.Person,
                                contentDescription = "Full Name",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                        )
                    )
                    
                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.Email,
                                contentDescription = "Email",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                        )
                    )
                    
                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Şifre") },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.Lock,
                                contentDescription = "Password",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                        )
                    )
                    
                    // Password Confirm Field
                    OutlinedTextField(
                        value = passwordConfirm,
                        onValueChange = { passwordConfirm = it },
                        label = { Text("Şifre Tekrar") },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.Lock,
                                contentDescription = "Confirm Password",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { confirmVisible = !confirmVisible }) {
                                Icon(
                                    if (confirmVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                    contentDescription = if (confirmVisible) "Hide password" else "Show password",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        isError = password != passwordConfirm && passwordConfirm.isNotEmpty(),
                        supportingText = {
                            if (password != passwordConfirm && passwordConfirm.isNotEmpty()) {
                                Text("Şifreler eşleşmiyor", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                            errorBorderColor = MaterialTheme.colorScheme.error
                        )
                    )
                    
                    // Age Field
                    OutlinedTextField(
                        value = age,
                        onValueChange = { if (it.all { char -> char.isDigit() } || it.isEmpty()) age = it },
                        label = { Text("Yaş") },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.DateRange,
                                contentDescription = "Age",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                        )
                    )
                    
                    // Gender Field
                    Column {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Cinsiyet") },
                            leadingIcon = {
                                Icon(
                                    Icons.Rounded.Face,
                                    contentDescription = "Gender",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(
                                        Icons.Rounded.ArrowDropDown,
                                        contentDescription = "Show gender options",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                            )
                        )
                        
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .width(IntrinsicSize.Min)
                        ) {
                            genderOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        gender = option
                                        expanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = when (option) {
                                                "Erkek" -> Icons.Rounded.Male
                                                "Kadın" -> Icons.Rounded.Female
                                                else -> Icons.Rounded.Face
                                            },
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Register Error Message
            AnimatedVisibility(
                visible = registerState is RegisterState.Error,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Error,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = (registerState as? RegisterState.Error)?.message ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Register Button
            Button(
                onClick = {
                    if (password == passwordConfirm && age.isNotEmpty() && fullName.isNotEmpty() && email.isNotEmpty() && gender.isNotEmpty()) {
                        viewModel.register(email, password, fullName, age.toInt(), gender)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = email.isNotEmpty() && password.isNotEmpty() && password == passwordConfirm && 
                        fullName.isNotEmpty() && age.isNotEmpty() && gender.isNotEmpty() && 
                        registerState !is RegisterState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary
                )
            ) {
                if (registerState is RegisterState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PersonAdd,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = "Kayıt Ol", 
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Login Link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Zaten bir hesabınız var mı?",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        text = "Giriş Yapın",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
} 