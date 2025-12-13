import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breastieproject.ui.theme.BackupTheme
import com.example.breastieproject.R  // ✅ ADD INI!


@Composable
fun OnboardingPage(
    imageRes: Int,
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Logo di atas
            // Logo + Text "BREASTIE"
            Row(
                modifier = Modifier.padding(bottom = 40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo icon
                Image(
                    painter = painterResource(id = R.drawable.logo_breastie),
                    contentDescription = "Logo Breastie",
                    modifier = Modifier.height(50.dp) // Adjust size
                )

                Spacer(modifier = Modifier.width(12.dp)) // Jarak antara logo & text

                // Text "BREASTIE"
                Text(
                    text = "BREASTIE",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFFEC7FA9), // Pink tua
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }

            // Ilustrasi besar di tengah
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Onboarding illustration",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(1f)  // 80% lebar screen
                    .height(280.dp)
                    .padding(bottom = 32.dp)
            )

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.displayMedium,
                color = Color(0xFFBE5985),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPagePreview() {
    BackupTheme {
        OnboardingPage(
            imageRes = R.drawable.onboarding_1,
            title = "Welcome to Your\nSupportive Community"
        )
    }
}


/**
 * ============================================================================
 * FILE: OnboardingPage.kt
 * LOCATION: ui/screens/onboarding/components/OnboardingPage.kt
 * ============================================================================
 *
 * 🎯 DESKRIPSI SIMPLE:
 * Component untuk SATU SLIDE onboarding.
 * Reusable - dipakai 3x untuk 3 slides.
 *
 * ============================================================================
 * 🤔 UNTUK APA?
 * ============================================================================
 *
 * Tampilkan konten slide:
 *   ✅ Logo BREASTIE di atas
 *   ✅ Gambar ilustrasi tengah
 *   ✅ Title text di bawah
 *
 * ============================================================================
 * 📥 PARAMETER
 * ============================================================================
 *
 * imageRes: Int
 *   - Resource ID gambar PNG
 *   - Contoh: R.drawable.onboarding_1
 *
 * title: String
 *   - Text judul slide
 *   - Support multi-line (\n)
 *   - Contoh: "Welcome to Your\nSupportive Community"
 *
 * modifier: Modifier (optional)
 *   - Styling tambahan dari parent
 *
 * ============================================================================
 * 🎨 TAMPILAN
 * ============================================================================
 *
 * Layout (centered):
 * ┌─────────────────────────────────┐
 * │                                 │
 * │    [●] BREASTIE                 │  ← Logo + text
 * │                                 │
 * │                                 │
 * │    [Gambar Ilustrasi]           │  ← imageRes
 * │                                 │
 * │                                 │
 * │    Welcome to Your              │  ← title
 * │    Supportive Community         │
 * │                                 │
 * └─────────────────────────────────┘
 *
 * ============================================================================
 * 💡 CARA PAKAI
 * ============================================================================
 *
 * // Single slide
 * OnboardingPage(
 *     imageRes = R.drawable.onboarding_1,
 *     title = "Welcome!"
 * )
 *
 * // Di HorizontalPager (OnboardingScreen)
 * HorizontalPager(...) { page ->
 *     OnboardingPage(
 *         imageRes = pages[page].imageRes,
 *         title = pages[page].title
 *     )
 * }
 *
 * ============================================================================
 * 🔗 FILE YANG DIBUTUHKAN
 * ============================================================================
 *
 * Images:
 *   - res/drawable/logo_breastie.png
 *   - res/drawable/onboarding_1.png
 *   - res/drawable/onboarding_2.png
 *   - res/drawable/onboarding_3.png
 *
 * Called by:
 *   - OnboardingScreen.kt
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * ============================================================================
 */