package com.chess.model;

public class Knight extends Piece {
    public Knight(boolean white) {
        super(white); // Calls the Piece constructor
    }

    public Knight() {
        super(); // Calls the Piece constructor
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        // Rule 1: Cannot capture your own piece
        if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }

        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());

        // Rule 2: The L-Shape Math
        // A move is valid if (x=2, y=1) or (x=1, y=2)
        return x * y == 2;
    }
}