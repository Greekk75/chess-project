package com.chess.model;

public class Move {
    private Spot start;
    private Spot end;
    private Piece pieceMoved;
    private Piece pieceKilled;
    private boolean isFirstMove; // NEW: Track if piece hadn't moved before

    public Move() {
    }

    // Constructor for backward compatibility (default to false)
    public Move(Spot start, Spot end, Piece pieceMoved, Piece pieceKilled) {
        this(start, end, pieceMoved, pieceKilled, false);
    }

    // New constructor with isFirstMove tracking
    public Move(Spot start, Spot end, Piece pieceMoved, Piece pieceKilled, boolean isFirstMove) {
        this.start = start;
        this.end = end;
        this.pieceMoved = pieceMoved;
        this.pieceKilled = pieceKilled;
        this.isFirstMove = isFirstMove;
    }

    public Spot getStart() {
        return start;
    }

    public void setStart(Spot start) {
        this.start = start;
    }

    public Spot getEnd() {
        return end;
    }

    public void setEnd(Spot end) {
        this.end = end;
    }

    public Piece getPieceMoved() {
        return pieceMoved;
    }

    public void setPieceMoved(Piece pieceMoved) {
        this.pieceMoved = pieceMoved;
    }

    public Piece getPieceKilled() {
        return pieceKilled;
    }

    public void setPieceKilled(Piece pieceKilled) {
        this.pieceKilled = pieceKilled;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }
}