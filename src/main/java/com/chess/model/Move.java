package com.chess.model;

public class Move {
    private final Spot start;
    private final Spot end;
    private final Piece pieceMoved;
    private final Piece pieceKilled;

    public Move(Spot start, Spot end, Piece pieceMoved, Piece pieceKilled) {
        this.start = start;
        this.end = end;
        this.pieceMoved = pieceMoved;
        this.pieceKilled = pieceKilled;
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
}
