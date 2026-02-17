package com.chess.dto;

import com.chess.model.Spot;

public class GameState {
    private Spot[][] board;
    private boolean isWhiteTurn;
    private String message;
    private boolean success;

    private java.util.List<com.chess.model.Move> history;
    private boolean inCheck;
    private boolean checkmate;

    public GameState() {
    }

    public GameState(Spot[][] board, boolean isWhiteTurn, String message, boolean success) {
        this(board, isWhiteTurn, message, success, new java.util.ArrayList<>(), false, false);
    }

    public GameState(Spot[][] board, boolean isWhiteTurn, String message, boolean success,
            java.util.List<com.chess.model.Move> history) {
        this(board, isWhiteTurn, message, success, history, false, false);
    }

    public GameState(Spot[][] board, boolean isWhiteTurn, String message, boolean success,
            java.util.List<com.chess.model.Move> history, boolean inCheck, boolean checkmate) {
        this.board = board;
        this.isWhiteTurn = isWhiteTurn;
        this.message = message;
        this.success = success;
        this.history = history;
        this.inCheck = inCheck;
        this.checkmate = checkmate;
    }

    public Spot[][] getBoard() {
        return board;
    }

    public void setBoard(Spot[][] board) {
        this.board = board;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public void setWhiteTurn(boolean whiteTurn) {
        isWhiteTurn = whiteTurn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public java.util.List<com.chess.model.Move> getHistory() {
        return history;
    }

    public void setHistory(java.util.List<com.chess.model.Move> history) {
        this.history = history;
    }

    public boolean isInCheck() {
        return inCheck;
    }

    public void setInCheck(boolean inCheck) {
        this.inCheck = inCheck;
    }

    public boolean isCheckmate() {
        return checkmate;
    }

    public void setCheckmate(boolean checkmate) {
        this.checkmate = checkmate;
    }
}
