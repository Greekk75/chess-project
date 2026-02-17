package com.chess.dto;

public class AiMoveRequest {
    private GameState gameState;
    private String difficulty;

    public AiMoveRequest() {
    }

    public AiMoveRequest(GameState gameState, String difficulty) {
        this.gameState = gameState;
        this.difficulty = difficulty;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
