package com.chess.model;

public class King extends Piece {
    public King(boolean white) {
        super(white); //
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        // 1. Check if destination has a teammate
        if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }

        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());

        // 2. Logic: Must move exactly 1 square in any direction
        if (x + y > 0 && x <= 1 && y <= 1) {
            return true;
        }

        // 3. Castling Logic
        if (!this.hasMoved() && x == 0 && y == 2) {
            // Identify Rook direction (Target Y - Start Y)
            int direction = end.getY() - start.getY(); // +2 for Kingside (usually), -2 unlikely but depends on board
                                                       // setup

            int step = direction > 0 ? 1 : -1;
            // Check the 1st square over
            if (board.getBox(start.getX(), start.getY() + step).getPiece() != null)
                return false;
            // Check the 2nd square over (destination)
            if (end.getPiece() != null)
                return false;

            // Check for Rook existence
            // If moving right (+), rook should be at col 7. If moving left (-), rook should
            // be at col 0.
            int rookY = direction > 0 ? 7 : 0;
            Spot rookSpot = board.getBox(start.getX(), rookY);
            Piece rook = rookSpot.getPiece();

            if (rook instanceof Rook && !rook.hasMoved() && rook.isWhite() == this.isWhite()) {
                // Check path for Queenside (3 squares)
                if (direction < 0) {
                    // Queenside: King at 4. Rook at 0.
                    // Squares between: 1, 2, 3.
                    // King moves to 2.
                    // Checked start-1 (3) above? "start.getY() + step" -> 4-1 = 3.
                    // Need to check 1 and 2 (destination is 2).
                    // Destination is checked above.
                    // Need to check col 1.
                    if (board.getBox(start.getX(), 1).getPiece() != null)
                        return false;
                    if (board.getBox(start.getX(), 2).getPiece() != null)
                        return false; // Destination
                    if (board.getBox(start.getX(), 3).getPiece() != null)
                        return false; // Passed through
                } else {
                    // King-side (Right)
                    // King at 4. Rook at 7.
                    // Squares: 5, 6.
                    // King moves to 6.
                    // Checked 5 above (start+step).
                    // Checked 6 above (destination).
                }
                return true;
            }
        }

        return false;
    }
}