package com.chess.model;

public abstract class Piece {
    private boolean white; // Identifies the team

    public Piece(boolean white) {
        this.white = white;
    }

    public boolean isWhite() {
        return white;
    }

    // Every piece will implement its own movement rules here
    public abstract boolean canMove(Board board, Spot start, Spot end);

    private boolean hasMoved = false;

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}