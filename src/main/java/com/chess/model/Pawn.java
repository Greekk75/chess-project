package com.chess.model;

public class Pawn extends Piece {
    public Pawn(boolean white) {
        super(white);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        int x = end.getX() - start.getX();
        int y = end.getY() - start.getY();
        int direction = this.isWhite() ? 1 : -1; // White moves up, Black moves down

        // 1. One step forward to an empty square
        if (y == 0 && x == direction && end.getPiece() == null)
            return true;

        // 2. Two steps forward from starting position
        if (y == 0 && x == 2 * direction && start.getX() == (this.isWhite() ? 1 : 6)) {
            return board.getBox(start.getX() + direction, start.getY()).getPiece() == null && end.getPiece() == null;
        }

        // 3. Diagonal capture
        if (Math.abs(y) == 1 && x == direction && end.getPiece() != null
                && end.getPiece().isWhite() != this.isWhite()) {
            return true;
        }

        // 4. En Passant
        if (Math.abs(y) == 1 && x == direction && end.getPiece() == null) {
            Move lastMove = board.getLastMove();
            System.out.println("EP Check: Diagonal move to empty square detected.");
            if (lastMove != null) {
                Piece lastMovedPiece = lastMove.getPieceMoved();
                System.out.println("Last Move: " + lastMovedPiece.getType() + " from " + lastMove.getStart().getX()
                        + " to " + lastMove.getEnd().getX());

                // Check if last moved piece is a pawn and moved 2 squares
                if (lastMovedPiece instanceof Pawn
                        && Math.abs(lastMove.getStart().getX() - lastMove.getEnd().getX()) == 2) {

                    System.out.println("Last move was double pawn push.");
                    // Check if it is adjacent to start and we are moving to its file
                    if (lastMove.getEnd().getX() == start.getX() && lastMove.getEnd().getY() == end.getY()) {
                        System.out.println("EP Validated!");
                        return true;
                    } else {
                        System.out.println("EP Failed: Not adjacent or wrong file. Ours: " + start.getX() + ","
                                + end.getY() + " Theirs: " + lastMove.getEnd().getX() + "," + lastMove.getEnd().getY());
                    }
                } else {
                    System.out.println("EP Failed: Last move not double pawn push.");
                }
            } else {
                System.out.println("EP Failed: No last move.");
            }
        }

        return false;
    }
}
