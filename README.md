# Breastie - Breast Cancer Support Community App

Aplikasi mobile berbasis Android untuk mendukung perempuan dalam perjalanan menghadapi kanker payudara melalui fitur edukasi, komunitas, reminder kesehatan, dan konsultasi AI.

---

## 📱 Fitur Utama

### 1. **Authentication**
- Sign Up dengan email & password
- Sign In dengan validasi Firebase
- Anonymous username untuk privacy di komunitas
- Profile management dengan foto profil

### 2. **Health Reminder**
- Jadwal checkup & terapi
- Kalender dinamis dengan indikator schedule
- Perhitungan hari H-X yang akurat

### 3. **Community**
- Join komunitas support groups
- Feed posts dari komunitas yang diikuti
- Like & comment di posts
- Real-time chat dalam komunitas
- Anonymous posting untuk privacy

### 4. **AI Check-Up**
- Konsultasi dengan AI tentang gejala
- FAQ breast cancer
- Riwayat chat tersimpan
- Respon real-time

---

## 🛠️ Tech Stack

### **Frontend**
- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Material Design 3** - UI components
- **Coil** - Image loading

### **Backend & Database**
- **Firebase Authentication** - User authentication
- **Cloud Firestore** - NoSQL database
- **Firebase Storage** - File storage (foto profil)

### **Architecture**
- **MVVM** (Model-View-ViewModel)
- **StateFlow** - State management
- **Coroutines** - Asynchronous operations
- **Navigation Component** - Screen navigation

### **AI Integration**
- **GROQ AI API** - AI chatbot untuk konsultasi

---

## 📦 Project Structure
```
breastieproject/
├── data/
│   └── model/          # Data classes (User, Reminder, Community, Post, etc.)
├── ui/
│   ├── screens/        # UI screens
│   │   ├── auth/       # SignIn, SignUp
│   │   ├── home/       # HomeScreen
│   │   ├── reminder/   # ReminderScreen
│   │   ├── community/  # CommunityScreen & tabs
│   │   ├── checkup/    # CheckUpScreen
│   │   └── profile/    # ProfileScreen
│   ├── components/     # Reusable UI components
│   ├── theme/          # App theme & colors
│   └── state/          # UI states
├── viewmodels/         # ViewModels (AuthViewModel, ReminderViewModel, etc.)
└── navigation/         # Navigation graph
```

---

## 🔑 Setup & Installation

### **Prerequisites**
- Android Studio Hedgehog (2023.1.1) atau lebih baru
- JDK 17 atau lebih baru
- Android SDK 24+ (Android 7.0 Nougat)
- Emulator atau device fisik untuk testing

### **1. Clone Repository**
```bash
git https://github.com/cahyalintanglangitan/Breastie.git
cd Breastie
```

### **2. Setup Firebase**

#### **a. Create Firebase Project**
1. Buka [Firebase Console](https://console.firebase.google.com/)
2. Klik "Add project" → Nama: "Breastie"
3. Disable Google Analytics (optional)
4. Klik "Create project"

#### **b. Add Android App**
1. Di Project Overview → Klik icon Android
2. Android package name: `com.example.breastieproject`
3. App nickname: `Breastie`
4. Download `google-services.json`
5. Copy file ke `app/` folder

#### **c. Enable Authentication**
1. Build → Authentication → Get Started
2. Sign-in method → Email/Password → Enable
3. Save

#### **d. Create Firestore Database**
1. Build → Firestore Database → Create database
2. Start in **test mode** (untuk development)
3. Location: `asia-southeast1` (Singapore)
4. Enable

#### **e. Enable Storage**
1. Build → Storage → Get Started
2. Start in **test mode**
3. Enable


#### **b. Add to Project**
1. Buka `local.properties`
2. Tambahkan:
```properties
# Gemini AI API Key
GEMINI_API_KEY=your_api_key_here
```

⚠️ **PENTING:**
- File `local.properties` sudah masuk `.gitignore`
- JANGAN commit API key ke Git!
- Setiap developer harus punya API key sendiri

### **4. Build & Run**
```bash
# Sync Gradle
./gradlew clean build

# Run di emulator/device
Run → Run 'app'
```

---

## 🔐 Security & Privacy

### **Data Isolation**
- Setiap user hanya bisa akses data mereka sendiri
- Firestore rules filter by userId
- AuthStateListener untuk detect user changes

### **Anonymous Community**
- Posting menggunakan anonymous username
- Real name tidak terlihat di komunitas
- Privacy-first approach

### **API Key Protection**
- API keys di `local.properties` (not committed)
- `.gitignore` melindungi file sensitif
- Production: pakai environment variables

---

## 👥 Team

- **Cahya Lintang Ayu Langitan** - Authentication, Profile, Community Features, Backend
- **Kinanda Mardhatilla** - Home Feaatures, AI Integration, and Backend of ChatBot CheckUp
- **Aulianda Arief** - Reminder Features (Frontend)
- **Haida Alfadila** - ChatBot CheckUp Features (Frontend)
---

## 📝 License

This project is for educational purposes only.

---

## 🐛 Known Issues

1. **Network timeout pada sign in** - Koneksi internet lemot bisa bikin sign in gagal. Solusi: retry atau tunggu lebih lama.
2. **Feed tidak muncul saat pertama buka** - FIXED dengan Flow collector di CommunityViewModel.
3. **Date calculation off by one** - FIXED dengan normalize tanggal ke midnight.


---

## 📞 Contact

Untuk pertanyaan atau bug reports, silakan buat issue di GitHub atau contact tim developer.

---

**Made with ❤️ by Team Breastie - Cheekbuddy**