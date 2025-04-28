package com.example.fonksiyonel.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Page Title
            Box(
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Accent1, Accent2)
                        )
                    )
                    .padding(horizontal = 32.dp, vertical = 16.dp),
            ) {
                Text(
                    text = "Hesap Oluştur",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            
            // Full Name Field
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Ad Soyad") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Full Name")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
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
                    Icon(Icons.Default.Email, contentDescription = "Email")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
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
                    Icon(Icons.Default.Lock, contentDescription = "Password")
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
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
                    Icon(Icons.Default.Lock, contentDescription = "Confirm Password")
                },
                trailingIcon = {
                    IconButton(onClick = { confirmVisible = !confirmVisible }) {
                        Icon(
                            if (confirmVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (confirmVisible) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                isError = password != passwordConfirm && passwordConfirm.isNotEmpty(),
                supportingText = {
                    if (password != passwordConfirm && passwordConfirm.isNotEmpty()) {
                        Text("Şifreler eşleşmiyor")
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
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
                    Icon(Icons.Default.DateRange, contentDescription = "Age")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )
            )
            
            // Gender Field
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Cinsiyet") },
                    leadingIcon = {
                        Icon(Icons.Default.Face, contentDescription = "Gender")
                    },
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Show options")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                )
                
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .width(IntrinsicSize.Min)
                        .padding(top = 8.dp)
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            // Register Error Message
            AnimatedVisibility(visible = registerState is RegisterState.Error) {
                Text(
                    text = (registerState as? RegisterState.Error)?.message ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
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
                shape = RoundedCornerShape(12.dp),
                enabled = email.isNotEmpty() && password.isNotEmpty() && password == passwordConfirm && 
                        fullName.isNotEmpty() && age.isNotEmpty() && gender.isNotEmpty() && 
                        registerState !is RegisterState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (registerState is RegisterState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Kayıt Ol", fontSize = 16.sp)
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
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
} 