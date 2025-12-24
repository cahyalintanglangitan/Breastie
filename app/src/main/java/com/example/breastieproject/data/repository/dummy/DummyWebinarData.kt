package com.example.breastieproject.data.repository.dummy

import com.example.breastieproject.data.model.WebinarEvent

object DummyWebinarData {

    val currentWebinar = WebinarEvent(
        id = "webinar_001",
        title = "Managing Side Effects of Breast Cancer Treatment",
        subtitle = "Practical guidance to help you cope physically and emotionally during treatment",
        date = "January 5, 2026",
        location = "Virtual Event (Zoom)",
        imageUrl = "", // Placeholder
        description = """
             Breast cancer treatment can bring many challenges, both physically and emotionally.
            Side effects such as fatigue, nausea, pain, hair loss, and emotional distress are 
            common experiences for many patients.
            
            This webinar is designed to help you better understand these side effects and 
            provide practical, compassionate strategies to manage them in your daily life.
            
            You are not alone — this session creates a safe space to learn, share, and feel supported.
        """.trimIndent(),
        whatToExpect = """
            • Understanding common side effects of breast cancer treatment
            • Practical tips to manage fatigue, nausea, and pain
            • Emotional coping strategies for stress, anxiety, and mood changes
            • Self-care routines that support healing and recovery
            • Sharing experiences and insights from the breast cancer community
            • Live Q&A session
        """.trimIndent(),
        registrationDetails = """
            Limited spots available! Register now to secure your place.
            
            You will receive:
            • Zoom link via email 24 hours before the event
            • Access to exclusive resources
            • Certificate of attendance
            
            For questions, contact us at breastie@gmail.com 
        """.trimIndent()
    )
}