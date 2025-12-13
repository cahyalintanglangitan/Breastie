package com.example.breastieproject.data.repository.dummy

import com.example.breastieproject.data.model.WebinarEvent

object DummyWebinarData {

    val currentWebinar = WebinarEvent(
        id = "webinar_001",
        title = "Innovative Ideas from Women Entrepreneurs",
        subtitle = "Lorem ipsum dolor sit amet, sed do eiusmod tempor incididunt",
        date = "June 5, 2025",
        location = "Virtual Event (Zoom)",
        imageUrl = "", // Placeholder
        description = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do 
            eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim 
            ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut 
            aliquip ex ea commodo consequat.
            
            Duis aute irure dolor in reprehenderit in voluptate velit esse 
            cillum dolore eu fugiat nulla pariatur.
        """.trimIndent(),
        whatToExpect = """
            • Inspiring keynote from successful women entrepreneurs
            • Panel discussion on overcoming challenges
            • Networking opportunities with like-minded professionals
            • Q&A session with industry experts
            • Resource sharing and community building
        """.trimIndent(),
        registrationDetails = """
            Limited spots available! Register now to secure your place.
            
            You will receive:
            • Zoom link via email 24 hours before the event
            • Access to exclusive resources
            • Certificate of attendance
            
            For questions, contact us at events@breastie.com
        """.trimIndent()
    )
}