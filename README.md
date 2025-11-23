# 🎵 Sonora Music Player

<div align="center">

**A Modern, Feature-Rich Java Music Player with AI-Powered Recommendations**

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![FlatLaf](https://img.shields.io/badge/UI-FlatLaf-brightgreen.svg)](https://www.formdev.com/flatlaf/)

*Smart music playback with audio fingerprinting, intelligent recommendations, and real-time visualization*

</div>

---

## ✨ Features

### 🎧 **Core Playback**
- **Multi-format Support**: MP3, WAV, FLAC, and more via VLC integration
- **High-Quality Audio**: Powered by VLCJ for professional-grade playback
- **Queue Management**: Create and manage playback queues effortlessly
- **Gapless Playback**: Seamless transitions between tracks

### 🔍 **Audio Fingerprinting**
- **Song Identification**: Identify unknown tracks using audio fingerprinting technology
- **Metadata Enrichment**: Automatically fetch song details, album art, and lyrics
- **AudD.io Integration**: Powered by industry-leading audio recognition API
- **Spotify & Apple Music**: Direct links to streaming platforms

### 🤖 **Smart Recommendations**
- **AI-Powered Suggestions**: Intelligent track recommendations based on listening history
- **Daily Mix**: Personalized playlists generated from your preferences
- **Genre & Artist Analysis**: Discover new music similar to your favorites
- **Collaborative Filtering**: Apache Mahout-based recommendation engine

### 📊 **Audio Visualization**
- **Real-time Spectrum Analyzer**: Visualize audio frequencies as you listen
- **Waveform Display**: See the audio waveform of your current track
- **Equalizer**: 10-band graphic equalizer with presets
- **Audio Analytics**: Track listening statistics and patterns

### 📚 **Library Management**
- **Smart Library**: Organize your music collection efficiently
- **Playlist Creation**: Create, edit, and manage custom playlists
- **Search & Filter**: Quickly find tracks by title, artist, album, or genre
- **Metadata Editor**: Edit track information directly in the app

### 📈 **Analytics Dashboard**
- **Listening History**: Track your music listening patterns
- **Play Statistics**: View most-played tracks and artists
- **Visual Charts**: Beautiful charts powered by JFreeChart
- **Export Data**: Export your listening history for analysis

### 🎨 **Modern UI**
- **Dark Theme**: Sleek, modern interface with FlatLaf Dark theme
- **Responsive Design**: Smooth animations and transitions
- **Intuitive Navigation**: Easy-to-use sidebar navigation
- **Album Art Display**: Beautiful album artwork integration

---

## 🛠️ Technology Stack

### **Core Technologies**
- **Java 17**: Modern Java features and performance
- **Maven**: Dependency management and build automation
- **VLCJ 4.8.2**: VLC-based media framework

### **UI Framework**
- **Swing**: Native Java GUI toolkit
- **FlatLaf 3.2.5**: Modern look and feel
- **MigLayout**: Flexible layout manager
- **JFreeChart**: Advanced charting library

### **Audio Processing**
- **TarsosDSP**: Audio analysis and feature extraction
- **VLC Media Player**: High-quality audio playback
- **JLayer**: MP3 decoding support
- **MP3SPI**: Java Sound MP3 support

### **Machine Learning & Recommendations**
- **Apache Mahout**: Collaborative filtering algorithms
- **AudD.io API**: Audio fingerprinting service

### **Data Management**
- **H2 Database**: Embedded SQL database for metadata
- **SQLite**: Lightweight database for preferences
- **Jackson**: JSON processing

### **Network & APIs**
- **OkHttp**: HTTP client for API requests
- **YouTube Music Integration**: Fetch metadata and lyrics

---

## 🚀 Getting Started

### Prerequisites

- **Java Development Kit (JDK) 17** or higher
- **Maven 3.8+**
- **VLC Media Player** (for development, not required for standalone builds)

### Installation

#### **Option 1: Download Pre-built Installer** (Recommended)

1. Download the latest `Sonora-1.0.msi` installer from the releases
2. Run the installer
3. Launch Sonora Music Player from your Start Menu

> ✅ **No Java or VLC installation required** - Everything is bundled!

#### **Option 2: Build from Source**

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/sonora-music-player.git
   cd sonora-music-player
   ```

2. **Build with Maven**
   ```bash
   mvn clean package
   ```

3. **Run the application**
   ```bash
   java -jar target/sonora-music-player-1.0.0.jar
   ```

#### **Option 3: Run in Development Mode**

```bash
mvn clean compile exec:java
```

---

## 📦 Building Standalone Executable

### Using jpackage (Windows)

To create a standalone installer that includes both JRE and VLC:

```bash
# Package the application
mvn clean package

# Create Windows installer with jpackage
jpackage --input target \
  --name Sonora \
  --main-jar sonora-music-player-1.0.0.jar \
  --main-class com.musicplayer.MusicPlayerApp \
  --type msi \
  --app-version 1.0 \
  --vendor "Your Name" \
  --win-dir-chooser \
  --win-menu \
  --win-shortcut \
  --resource-dir installer
```

The installer will bundle:
- Java Runtime Environment (JRE)
- VLC native libraries
- All application dependencies

---

## 📁 Project Structure

```
sonora-music-player/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/musicplayer/
│       │       ├── MusicPlayerApp.java          # Main entry point
│       │       ├── controller/                  # Application controllers
│       │       │   └── MusicPlayerController.java
│       │       ├── model/                       # Data models
│       │       │   ├── Track.java
│       │       │   ├── Playlist.java
│       │       │   ├── Recommendation.java
│       │       │   └── PlayHistory.java
│       │       ├── service/                     # Business logic services
│       │       │   ├── AudioPlayerService.java  # Audio playback
│       │       │   ├── AudioFingerprintService.java
│       │       │   ├── RecommendationService.java
│       │       │   ├── LyricsService.java
│       │       │   └── MetadataEnrichmentService.java
│       │       ├── repository/                  # Data access layer
│       │       │   ├── DatabaseManager.java
│       │       │   └── AnalyticsDatabaseManager.java
│       │       ├── view/                        # UI components
│       │       │   ├── MainFrame.java
│       │       │   ├── LibraryPanel.java
│       │       │   ├── NowPlayingPanel.java
│       │       │   ├── RecommendationPanel.java
│       │       │   ├── FingerprintPanel.java
│       │       │   ├── EqualizerPanel.java
│       │       │   └── AnalyticsPanel.java
│       │       └── util/                        # Utility classes
│       │           ├── IconLoader.java
│       │           ├── ImageUtils.java
│       │           └── TimeFormatter.java
│       └── resources/
│           ├── vlc/                            # VLC native libraries
│           └── icons/                          # Application icons
```

---

## 🎯 Usage Guide

### Identifying Unknown Songs
1. Navigate to **"Identify Song"** panel
2. Click **"Record"** to capture audio from your microphone
3. Or select an audio file to identify
4. View matched results with metadata and album art

### Getting Recommendations
1. Go to **"Recommendations"** panel
2. Click **"Generate Daily Mix"**
3. Explore personalized suggestions based on your taste
4. Play recommended tracks directly

### Creating Playlists
1. Open **"Playlists"** panel
2. Click **"Create New Playlist"**
3. Name your playlist and add tracks
4. Manage and edit playlists anytime

### Viewing Analytics
1. Navigate to **"Analytics"** panel
2. View listening history, top tracks, and statistics
3. Analyze your music preferences over time

---

## 🔧 Configuration

### Database
- **H2 Database**: Stores track metadata and playlists
- **SQLite**: Stores user preferences and analytics
- Location: `data/` folder in application directory

### VLC Integration
- VLC libraries are bundled in `src/main/resources/vlc/`
- Automatically detected and loaded at runtime
- No separate VLC installation needed for production builds

### API Keys
Edit `AudioFingerprintService.java` to add your own AudD.io API key:
```java
private static final String API_TOKEN = "your-api-token-here";
```

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 🙏 Acknowledgments

- **FlatLaf** - Modern Swing look and feel
- **VLCJ** - VLC-based Java media framework
- **TarsosDSP** - Audio processing library
- **Apache Mahout** - Machine learning library
- **AudD.io** - Audio recognition API
- **JFreeChart** - Charting library
- **OkHttp** - HTTP client

---

## 📧 Contact

For questions, suggestions, or issues, please open an issue on GitHub.

---

<div align="center">

**Made with ❤️ using Java**

⭐ If you like this project, please give it a star!

</div>