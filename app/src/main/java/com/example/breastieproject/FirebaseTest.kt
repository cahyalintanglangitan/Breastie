package com.example.breastieproject

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun testFirebaseConnection() {
    val auth = FirebaseAuth.getInstance()
    println("✅ Firebase Auth: ${auth != null}")

    val firestore = FirebaseFirestore.getInstance()
    println("✅ Firestore: ${firestore != null}")
}