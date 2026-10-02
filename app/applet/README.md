# AniSuggest (アニメサジェスト) — Dual App & Web Edition

**AniSuggest** is a hybrid anime discovery and recommendation platform that runs both as a **native Android app** and as a **responsive desktop web application**.

---

## 🌟 How It Runs in Both Environments

### 1. Running as a Native Android App
* **Mobile & Tablet / Desktop Adaptive**: Built with 100% Jetpack Compose and Material Design 3.
  * On phones: Uses comfortable bottom navigation.
  * On desktop screens / tablets (Chromebooks, Windows WSA, foldables): Automatically expands with a side **NavigationRail** and multi-column grid layout.
* **In-App Desktop Web Toggle**: Tap the **"Web Mode"** button in the top app bar to view the offline-bundled responsive desktop web app directly inside the Android app without leaving.
* **Local Persistence**: Powered by SQLite via Android Room Database 2.7.
* **Build Command**:
  ```bash
  gradle :app:assembleDebug
  ```

---

### 2. Running as a Desktop Web Application
The repository includes a standalone web app in the `/web` directory:
* **Zero Dependencies**: Pure HTML5, modern CSS3 (obsidian cyberpunk theme), and vanilla JavaScript ES6.
* **Full Feature Parity**:
  * 🎭 Discover across 8 emotional moods & multi-genre filters
  * 🌐 Live Jikan Edge REST API integration (`https://jikan.lucashdo.com/v1/`) for online search, top anime, and seasonal releases
  * ⏱️ 30-Second Interactive Quiz
  * 🎰 Canvas-animated Gacha Roulette Wheel
  * ✨ Similarity Matcher
  * 🔖 LocalStorage-backed Watchlist with episode tracking
* **How to Run on Desktop**:
  * **Option A**: Double-click `/web/index.html` to open it in Chrome, Edge, Safari, or Firefox.
  * **Option B (Local Web Server)**:
    ```bash
    cd web
    python3 -m http.server 8080
    # Open http://localhost:8080 in your desktop browser
    ```
  * **Option C (Hosting)**: Deploy the `/web` folder to GitHub Pages, Vercel, Netlify, or Firebase Hosting with a single click.
