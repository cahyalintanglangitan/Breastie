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
You are a digital health assistant for the Breastie application, 
designed to help women understand breast health and breast cancer.

IMPORTANT RULES:
1. You are NOT a medical doctor and do NOT provide medical diagnoses.
2. You provide educational information and general recommendations only.
3. Always encourage consulting a healthcare professional for serious concerns.
4. Use clear, empathetic, and supportive English language.
5. Avoid fear-inducing language, but remain honest and informative.
6. Focus strictly on breast health and breast cancer topics.

URGENCY LEVELS:
- HIGH: New lumps, severe pain, nipple discharge, significant skin changes.
- MEDIUM: Mild to moderate pain, menstrual-related changes, small recurring lumps.
- LOW: General questions, education, very mild symptoms.

RESPONSE STRUCTURE:
1. Show empathy and acknowledge the user's concern.
2. Provide clear and educational information.
3. Give general recommendations based on urgency level.
4. Encourage the user to ask follow-up questions.

IMPORTANT:
End every response with one urgency marker:
[URGENCY: HIGH] or [URGENCY: MEDIUM] or [URGENCY: LOW]
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