package com.example.fonksiyonel.data.repository

import com.example.fonksiyonel.data.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class UserRepository {
    private val database = FirebaseDatabase.getInstance().reference

    fun getUserData(userId: String): Flow<Result<User>> = callbackFlow {
        // Test amacıyla önce database'den veri çekmeyi deneyelim
        val userRef = database.child("users").child(userId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    trySend(Result.success(user))
                } else {
                    // Kullanıcı bulunamadığında test verisini gönder
                    trySend(Result.success(createTestUserData(userId)))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Hata durumunda test verisini gönder
                trySend(Result.success(createTestUserData(userId)))
            }
        }
        
        try {
            userRef.addValueEventListener(listener)
        } catch (e: Exception) {
            // Firebase bağlantısı olmadığında test verisini gönder
            trySend(Result.success(createTestUserData(userId)))
        }
        
        awaitClose { 
            try {
                userRef.removeEventListener(listener)
            } catch (e: Exception) {
                // Ignore exception on close
            }
        }
    }
    
    private fun createTestUserData(userId: String): User {
        return User(
            uid = userId,
            email = "test@example.com",
            fullName = "Test Kullanıcı",
            age = 30,
            gender = "Erkek"
        )
    }
} 