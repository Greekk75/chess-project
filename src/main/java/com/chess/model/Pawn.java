package com.chess.model;

public class Pawn extends Piece {
    public Pawn(boolean white) {
        super(white);
    }

    public Pawn() {
        super();
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        // x is Row index (0-7), y is Column index (0-7)
        // Direction: White is at Row 0/1 originally? Wait.
        // Board.java: White Rooks at 0,0. White Pawns at 1,x.
        // So White moves from 1 -> 2 -> ... -> 7.
        // Black moves from 6 -> 5 -> ... -> 0.

        int startX = start.getX();
        int startY = start.getY();
        int endX = end.getX();
        int endY = end.getY();

        if (this.isWhite()) {
            // White moves increasing X

            // 1. One step forward
            if (endY == startY && endX == startX + 1 && end.getPiece() == null) {
                return true;
            }

            // 2. Two steps forward (from Row 1)
            if (endY == startY && endX == startX + 2 && startX == 1) {
                // Check path clear
                if (board.getBox(startX + 1, startY).getPiece() == null && end.getPiece() == null) {
                    return true;
                }
            }

            // 3. Capture
            if (Math.abs(endY - startY) == 1 && endX == startX + 1) {
                if (end.getPiece() != null && !end.getPiece().isWhite()) {
                    return true;
                }
                // En Passant check (optional/safe to ignore for basic AI if buggy)
            }

        } else {
            // Black moves decreasing X

            // 1. One step forward
            if (endY == startY && endX == startX - 1 && end.getPiece() == null) {
                return true;
            }

            // 2. Two steps forward (from Row 6)
            if (endY == startY && endX == startX - 2 && startX == 6) {
                // Check path clear
                if (board.getBox(startX - 1, startY).getPiece() == null && end.getPiece() == null) {
                    return true;
                }
            }

            // 3. Capture
            if (Math.abs(endY - startY) == 1 && endX == startX - 1) {
                if (end.getPiece() != null && end.getPiece().isWhite()) {
                    return true;
                }
            }
        }

        return false;
    }
}
