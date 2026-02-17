package com.chess.model;

public class Rook extends Piece {
    public Rook(boolean white) {
        super(white);
    }

    public Rook() {
        super();
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        // 1. Must move in a straight line
        if (start.getX() != end.getX() && start.getY() != end.getY()) {
            return false;
        }

        // 2. Check if the path is clear (No pieces in between)
        if (!isPathClear(board, start, end)) {
            return false;
        }

        // 3. Cannot capture your own piece
        if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }

        return true;
    }

    private boolean isPathClear(Board board, Spot start, Spot end) {
        int xDirection = Integer.compare(end.getX(), start.getX());
        int yDirection = Integer.compare(end.getY(), start.getY());

        int currX = start.getX() + xDirection;
        int currY = start.getY() + yDirection;

        // Loop through the squares between start and end
        while (currX != end.getX() || currY != end.getY()) {
            if (board.getBox(currX, currY).getPiece() != null) {
                return false; // Path is blocked!
            }
            currX += xDirection;
            currY += yDirection;
        }
        return true;
    }
}