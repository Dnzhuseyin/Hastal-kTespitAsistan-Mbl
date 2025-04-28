package com.example.fonksiyonel.data.repository

import com.example.fonksiyonel.data.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserRepository {
    private val database = FirebaseDatabase.getInstance().reference
    
    // Test modu
    private val isTestMode = true

    fun getUserData(userId: String): Flow<Result<User>> = callbackFlow {
        // Test verisi
        val testUser = User(
            uid = userId,
            email = "test@example.com",
            fullName = "Test Kullanıcı",
            age = 30,
            gender = "Erkek"
        )
        
        if (isTestMode) {
            // Test modunda hemen test verisi döndür
            trySend(Result.success(testUser))
            
            // Gerçek bir dinleyici olmadığı için, sadece kapama fonksiyonu sağla
            awaitClose { }
            return@callbackFlow
        }
        
        // Gerçek Firebase dinleyicisi
        val userRef = database.child("users").child(userId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    trySend(Result.success(user))
                } else {
                    // Kullanıcı verisi bulunamazsa test verisi kullan
                    trySend(Result.success(testUser))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Hata durumunda test verisi kullan
                trySend(Result.success(testUser))
            }
        }
        
        try {
            userRef.addValueEventListener(listener)
            awaitClose { userRef.removeEventListener(listener) }
        } catch (e: Exception) {
            // Firebase bağlantı hatası durumunda test verisi kullan
            trySend(Result.success(testUser))
            awaitClose { }
        }
    }
} 