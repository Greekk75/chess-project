package com.chess.model;

public class Bishop extends Piece {
    public Bishop(boolean white) {
        super(white);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        int xDist = Math.abs(start.getX() - end.getX());
        int yDist = Math.abs(start.getY() - end.getY());

        // 1. Must move diagonally (x distance == y distance)
        if (xDist != yDist) {
            return false;
        }

        // 2. Check if the diagonal path is clear
        if (!isPathClear(board, start, end)) {
            return false;
        }

        // 3. Cannot capture teammate
        if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }

        return true;
    }

    private boolean isPathClear(Board board, Spot start, Spot end) {
        int xDir = Integer.compare(end.getX(), start.getX());
        int yDir = Integer.compare(end.getY(), start.getY());

        int currX = start.getX() + xDir;
        int currY = start.getY() + yDir;

        while (currX != end.getX()) {
            if (board.getBox(currX, currY).getPiece() != null) {
                return false;
            }
            currX += xDir;
            currY += yDir;
        }
        return true;
    }
}