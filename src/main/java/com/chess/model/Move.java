package com.chess.model;

public class Move {
    private final Spot start;
    private final Spot end;
    private final Piece pieceMoved;
    private final Piece pieceKilled;
    private final boolean isFirstMove; // NEW: Track if piece hadn't moved before

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

    public Spot getEnd() {
        return end;
    }

    public Piece getPieceMoved() {
        return pieceMoved;
    }

    public Piece getPieceKilled() {
        return pieceKilled;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }
}