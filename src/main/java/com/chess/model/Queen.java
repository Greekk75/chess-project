package com.chess.model;

public class Queen extends Piece {
    public Queen(boolean white) {
        super(white);
    }

    public Queen() {
        super();
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        // Reuse the logic: Is it a valid Rook move OR a valid Bishop move?
        Rook tempRook = new Rook(this.isWhite());
        Bishop tempBishop = new Bishop(this.isWhite());

        return tempRook.canMove(board, start, end) || tempBishop.canMove(board, start, end);
    }
}

//