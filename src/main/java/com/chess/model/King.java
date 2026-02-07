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
        if (!this.hasMoved() && y == 0 && x == 2) {
            // Identify Rook direction
            int direction = end.getX() - start.getX(); // +2 for Kingside (usually), -2 unlikely but depends on board
                                                       // setup
            // Standard board: Kings are at col 4.
            // Queenside Rook at 0, Kingside Rook at 7.
            // Target X for Castling:

            // Check path is clear
            // We need to check if the squares between start and rook are empty.
            // Not strictly checking the Rook here, leaving strict validation to
            // ChessService or further check?
            // Actually, verify path here.

            int step = direction > 0 ? 1 : -1;
            // Check the 1st square over
            if (board.getBox(start.getX() + step, start.getY()).getPiece() != null)
                return false;
            // Check the 2nd square over (destination) - already checked "teammate" above
            // but end piece must be null for castling usually?
            // Actually in Castling, the king moves to an empty square.
            if (end.getPiece() != null)
                return false;

            // Check for Rook existence
            // If moving right (+2), rook should be at 7. If moving left (-2), rook should
            // be at 0.
            int rookX = direction > 0 ? 7 : 0;
            Spot rookSpot = board.getBox(rookX, start.getY());
            Piece rook = rookSpot.getPiece();

            if (rook instanceof Rook && !rook.hasMoved() && rook.isWhite() == this.isWhite()) {
                // Check path for Queenside (3 squares: 1, 2, 3) if moving left
                if (direction < 0) {
                    // Check x=1, x=2, x=3. King is at 4. Moving to 2.
                    // Checked x=3 (start-1). Checking x=1?
                    // Standard: King e1(4,0). O-O-O (Queenside) -> King c1(2,0). Rook a1(0,0) ->
                    // d1(3,0).
                    // Path b1(1,0), c1(2,0), d1(3,0) must be empty?
                    // Actually King moves 2 squares to 2. Rook moves to 3.
                    // The squares King passes through (3) and lands on (2) must be empty.
                    // Also b1 (1) must be empty for Rook to move? Yes.

                    if (board.getBox(1, start.getY()).getPiece() != null)
                        return false;
                    if (board.getBox(2, start.getY()).getPiece() != null)
                        return false; // Destination
                    if (board.getBox(3, start.getY()).getPiece() != null)
                        return false;
                } else {
                    // King-side
                    if (board.getBox(5, start.getY()).getPiece() != null)
                        return false;
                    if (board.getBox(6, start.getY()).getPiece() != null)
                        return false; // Destination
                }
                return true;
            }
        }

        return false;
    }
}