# SabiWritersApp

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" alt="App Logo" width="120"/>
</p>

<p align="center">
  <b>A modern Android banking application built with Kotlin</b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green?style=flat-square&logo=android"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-blue?style=flat-square&logo=kotlin"/>
  <img src="https://img.shields.io/badge/Architecture-MVVM-orange?style=flat-square"/>
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-purple?style=flat-square"/>
</p>

---

## Table of Contents

- [About](#about)
- [Features](#features)
- [Screenshots](#screenshots)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

---

## About

**SabiWritersApp** is a feature-rich Android banking application inspired by First Bank's mobile banking experience. Built with modern Android development practices, it demonstrates clean architecture, intuitive UI design, and robust local data management using mock data patterns — perfect for learning, prototyping, or as a foundation for production banking apps.

The app showcases professional banking features including account management, fund transfers, transaction history, and more, all powered by a clean mock data layer that eliminates the need for external APIs during development.

---

## Features

### Core Banking Features
- **Account Dashboard** — View account balances and summary at a glance
- **Fund Transfer** — Send money between accounts with secure validation
- **Transaction History** — Detailed list of all transactions with filtering
- **Bill Payments** — Pay utilities and services directly from the app
- **Airtime & Data Purchase** — Top up mobile airtime and buy data bundles
- **Card Management** — View and manage linked debit/credit cards

### User Experience
- **Biometric Authentication** — Fingerprint and Face ID login support
- **Dark Mode** — Full dark theme support for comfortable viewing
- **Smooth Animations** — Polished transitions and micro-interactions
- **Offline Support** — Core features work without internet connectivity
- **Multi-Language Support** — Localized for diverse user bases

### Security
- **PIN Protection** — Secure PIN-based transaction authorization
- **Session Timeout** — Auto-logout for inactive sessions
- **Encrypted Local Storage** — Sensitive data stored securely

---

## Screenshots

<p align="center">
  <img src="screenshots/login_screen.png" alt="Login" width="200"/>
  <img src="screenshots/dashboard.png" alt="Dashboard" width="200"/>
  <img src="screenshots/transfer.png" alt="Transfer" width="200"/>
  <img src="screenshots/history.png" alt="History" width="200"/>
</p>

> **Note:** Add your actual screenshots to a `screenshots/` folder and update the paths above.

---

## Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **Dependency Injection** | Manual DI (Constructor Injection Pattern) |
| **Local Database** | Room (with mock data layer) |
| **Async Operations** | Kotlin Coroutines & Flow |
| **Navigation** | Jetpack Navigation Component |
| **State Management** | StateFlow & Compose State |
| **Image Loading** | Coil |
| **Build System** | Gradle with Kotlin DSL |

---

## Architecture

This project follows the **MVVM (Model-View-ViewModel)** architecture pattern with a clean separation of concerns:
