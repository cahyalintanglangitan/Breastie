package com.example.breastieproject.data.repository

import android.util.Log
import com.example.breastieproject.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class GroqApiRepository {

    private val apiKey = BuildConfig.GROQ_API_KEY
    private val apiUrl = "https://api.groq.com/openai/v1/chat/completions"

    private val systemPrompt = """
        Anda adalah asisten kesehatan digital untuk aplikasi Breastie, 
        yang membantu wanita memahami kesehatan payudara dan kanker payudara.

        ATURAN PENTING:
        1. Anda BUKAN dokter dan TIDAK memberikan diagnosis medis
        2. Anda memberikan informasi edukatif dan rekomendasi umum
        3. Selalu sarankan konsultasi dokter untuk keluhan serius
        4. Gunakan bahasa Indonesia yang ramah dan empatik
        5. Hindari menakut-nakuti, tetapi tetap jujur
        6. Fokus pada kesehatan payudara dan kanker payudara

        TINGKAT URGENSI:
        - URGENT: Benjolan baru, nyeri hebat, perdarahan dari puting, perubahan kulit signifikan
        - MEDIUM: Nyeri ringan-sedang, perubahan siklus menstruasi, benjolan kecil yang hilang-timbul
        - LOW: Pertanyaan umum, edukasi, gejala sangat ringan

        FORMAT RESPONS:
        1. Empati dan validasi perasaan user
        2. Informasi edukatif terkait keluhan
        3. Rekomendasi tindakan (dengan tingkat urgensi)
        4. Ajakan bertanya lebih lanjut

        PENTING: Akhiri setiap respons dengan marker urgency:
        [URGENCY: HIGH] atau [URGENCY: MEDIUM] atau [URGENCY: LOW]
    """.trimIndent()

    suspend fun getChatResponse(
        userMessage: String,
        conversationHistory: List<Pair<String, String>> = emptyList()
    ): Result<Pair<String, String>> = withContext(Dispatchers.IO) {
        try {
            Log.d("GROQ_API", "Sending request...")

            val messages = JSONArray()

            // Add system prompt
            messages.put(JSONObject().apply {
                put("role", "system")
                put("content", systemPrompt)
            })

            // Add conversation history
            conversationHistory.forEach { (role, content) ->
                messages.put(JSONObject().apply {
                    put("role", role)
                    put("content", content)
                })
            }

            // Add current user message
            messages.put(JSONObject().apply {
                put("role", "user")
                put("content", userMessage)
            })

            val requestBody = JSONObject().apply {
                put("model", "llama-3.3-70b-versatile")
                put("messages", messages)
                put("temperature", 0.7)
                put("max_tokens", 800)
            }

            Log.d("GROQ_API", "Using API Key: ${apiKey.take(6)}****")
            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpURLConnection

            connection.apply {
                requestMethod = "POST"
                setRequestProperty("Authorization", "Bearer $apiKey")
                setRequestProperty("Content-Type", "application/json")
                doOutput = true
                connectTimeout = 30000
                readTimeout = 30000
            }

            // Write request
            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(requestBody.toString())
                writer.flush()
            }

            val responseCode = connection.responseCode
            Log.d("GROQ_API", "Response code: $responseCode")

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = BufferedReader(InputStreamReader(connection.inputStream))
                    .use { it.readText() }

                val jsonResponse = JSONObject(response)
                val content = jsonResponse
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")

                // Extract urgency from response
                val urgency = when {
                    content.contains("[URGENCY: HIGH]", ignoreCase = true) -> "high"
                    content.contains("[URGENCY: MEDIUM]", ignoreCase = true) -> "medium"
                    else -> "low"
                }

                // Remove urgency marker from content
                val cleanContent = content
                    .replace(Regex("\\\\[URGENCY: (HIGH|MEDIUM|LOW)\\\\]", RegexOption.IGNORE_CASE), "")
                    .trim()

                Log.d("GROQ_API", "Response received, urgency: $urgency")

                Result.success(Pair(cleanContent, urgency))

            } else {
                val errorStream = connection.errorStream
                val errorResponse = if (errorStream != null) {
                    BufferedReader(InputStreamReader(errorStream)).use { it.readText() }
                } else {
                    "No error details"
                }

                Log.e("GROQ_API", "Error response: $errorResponse")
                Result.failure(Exception("API Error: $responseCode - $errorResponse"))
            }

        } catch (e: Exception) {
            Log.e("GROQ_API", "Exception occurred", e)
            Result.failure(e)
        }
    }
}