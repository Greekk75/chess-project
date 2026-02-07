# ♟️ Complete Chess Application

A full-stack, aesthetically pleasing **Chess Application** built with **Spring Boot** (Backend) and **React + Vite** (Frontend). This project implements standard FIDE chess rules, including complex moves like Castling, En Passant, and Pawn Promotion, wrapped in a modern, responsive UI.

## ✨ Features

### Core Gameplay
- **Full Chess Logic**: Complete implementation of piece movement rules.
- **Special Moves**:
  - **Castling**: King-side and Queen-side castling logic (King moves 2 squares).
  - **En Passant**: Capture opponent pawns that move two squares forward.
  - **Pawn Promotion**: Auto-promotion to Queen upon reaching the opposite rank.
- **Game State**:
  - **Check Detection**: Visual highlighting when the King is in check.
  - **Checkmate**: Stops the game and declares the winner with a modal popup.
  - **Stalemate**: Detects draws when no legal moves are available.
  - **Turn Management**: Enforces turn-based play (White/Black).

### UI/UX Design
- **Modern Dark Theme**: Deep blue/purple aesthetics (`#1a1a2e`) with vibrant accents (`#e94560`).
- **Glassmorphism**: Translucent UI elements for a premium feel.
- **Responsive Board**: CSS Grid-based layout that adapts to screen size.
- **Refined Assets**: Clean chess piece characters with hover effects.

---

## 🛠️ Technology Stack

- **Backend**: Java 17+, Spring Boot
- **Frontend**: React 19, Vite, CSS (Vanilla with Variables)
- **Communication**: REST API (State polling/updating)

---

## 🚀 Getting Started

### Prerequisites
- **Java 17** or higher
- **Maven**
- **Node.js** & **npm**

### 1. Start the Backend Server
The backend handles all game logic and state.

```bash
cd chess-project
mvn spring-boot:run
```
*The server will start on `http://localhost:8080`.*

### 2. Start the Frontend Application
The frontend provides the interactive board.

```bash
cd chess-frontend
npm install
npm run dev
```
*Open your browser to `http://localhost:5173` to play!*

---

## 🎮 How to Play

1.  **Move**: Click a piece to select it (highlighted), then click a valid destination square.
2.  **Castling**: 
    - Ensure the King and Rook have not moved.
    - Path must be clear and not under attack.
    - Click the King, then click the destination square (g1/c1 for White).
3.  **Reset**: Click "New Game" at any time to reset the board.

---

## 📡 API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/chess/show-board` | Retrieve current game state (board, turn, status). |
| `POST` | `/api/chess/move` | Execute a move. Body: `{startX, startY, endX, endY}`. |
| `POST` | `/api/chess/reset` | Reset the game to the initial state. |

---

## 📂 Project Structure

```
chess/
├── chess-project/       # Spring Boot Backend
│   ├── src/main/java/com/chess/
│   │   ├── controller/  # REST API Controllers
│   │   ├── service/     # Game Logic (Move validation, Checkmate)
│   │   ├── model/       # Piece definitions (King, Pawn, etc.)
│   │   └── dto/         # Data Transfer Objects
│   └── ...
├── chess-frontend/      # React Frontend
│   ├── src/
│   │   ├── components/  # Board, Square, Piece components
│   │   ├── hooks/       # Custom hooks (useChess)
│   │   ├── api/         # API integration
│   │   └── styles/      # CSS files
│   └── ...
└── README.md            # This file
```

## 🔮 Future Improvements
- **Multiplayer**: Websockets for real-time PvP.
- **AI Opponent**: Minimize algorithm integration.
- **Move History**: Visual log of moves (PGN format).
