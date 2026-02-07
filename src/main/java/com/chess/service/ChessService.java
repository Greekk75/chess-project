package com.chess.service;

import com.chess.dto.GameState;
import com.chess.model.*;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    // This keeps the game state alive in memory while the server is running
    private Board board = new Board();
    private boolean isWhiteTurn = true;

    // Returns the current state of the game including board and turn
    public GameState getGameState() {
        boolean inCheck = isKingInCheck(isWhiteTurn);
        boolean hasMoves = hasValidMoves(isWhiteTurn);
        boolean checkmate = inCheck && !hasMoves;
        String status = "Current State";
        if (checkmate)
            status = "Checkmate!";
        else if (inCheck)
            status = "Check!";

        return new GameState(board.getBoxes(), isWhiteTurn, status, true, board.getHistory(), inCheck, checkmate);
    }

    /**
     * Resets the board and the turn state.
     */
    public void resetBoard() {
        board.resetBoard();
        this.isWhiteTurn = true;
    }

    public GameState movePiece(int sX, int sY, int eX, int eY) {
        Spot[][] boxes = board.getBoxes();

        // Validation: Bounds check
        if (sX < 0 || sX > 7 || sY < 0 || sY > 7 || eX < 0 || eX > 7 || eY < 0 || eY > 7) {
            return new GameState(boxes, isWhiteTurn, "Error: Move out of bounds", false, board.getHistory());
        }

        Spot start = boxes[sX][sY];
        Spot end = boxes[eX][eY];
        Piece piece = start.getPiece();

        // 1. Validation: Is there a piece?
        if (piece == null) {
            return new GameState(boxes, isWhiteTurn, "Error: No piece selected", false, board.getHistory());
        }

        // 2. Validation: Does the piece color match the current turn?
        if (piece.isWhite() != isWhiteTurn) {
            return new GameState(boxes, isWhiteTurn, "Error: It is " + (isWhiteTurn ? "White's" : "Black's") + " turn!",
                    false, board.getHistory());
        }

        // 3. Validation: specific piece rules
        if (!piece.canMove(board, start, end)) {
            return new GameState(boxes, isWhiteTurn, "Error: Invalid Move for " + piece.getType(), false,
                    board.getHistory());
        }

        // DETECT MOVE TYPE (Normal vs En Passant vs Castling)
        Spot pieceToKillSpot = end;
        Piece pieceToKill = end.getPiece();
        boolean isEnPassant = false;
        boolean isCastling = false;
        Spot rookStart = null, rookEnd = null;
        Piece castlingRook = null;

        if (piece instanceof Pawn && Math.abs(start.getY() - end.getY()) == 1 && end.getPiece() == null) {
            // Diagonal move with no capture at destination = En Passant
            isEnPassant = true;
            pieceToKillSpot = board.getBox(start.getX(), end.getY());
            pieceToKill = pieceToKillSpot.getPiece();
        } else if (piece instanceof King && Math.abs(start.getX() - end.getX()) == 2) {
            isCastling = true;
            // Identify Rook moves
            int direction = end.getX() - start.getX(); // +2 Right, -2 Left
            int rookX = direction > 0 ? 7 : 0;
            rookStart = board.getBox(rookX, start.getY());
            rookEnd = board.getBox(start.getX() + (direction > 0 ? 1 : -1), start.getY());
            castlingRook = rookStart.getPiece();
        }

        // 4. Simulation: Apply move temporarily
        pieceToKillSpot.setPiece(null);
        end.setPiece(piece);
        start.setPiece(null);

        if (isCastling) {
            rookStart.setPiece(null);
            rookEnd.setPiece(castlingRook);
        }

        // 5. Validation: Does this move leave the King in check?
        boolean kingInCheck = isKingInCheck(isWhiteTurn);
        if (kingInCheck || (isCastling && isSquareAttacked(
                board.getBox(start.getX() + (end.getX() - start.getX()) / 2, start.getY()), !isWhiteTurn))) {
            // Castling specific rule: Cannot castle OUT of check, THROUGH check, or INTO
            // check.
            // isKingInCheck covers "Into Check".
            // We need to check if we are currently in check (Before move) ???
            // Actually, if we are in check currently, can we castle? NO.
            // We need to revert and return error.
        }

        // Re-verify Castling rules that require board analysis (like "not through
        // check")
        if (isCastling) {
            // Revert temp move to check "start" state
            if (isCastling) {
                rookEnd.setPiece(null);
                rookStart.setPiece(castlingRook);
            }
            start.setPiece(piece);
            end.setPiece(null); // Castling dest is always empty
            pieceToKillSpot.setPiece(pieceToKill); // Should be null/empty usually

            if (isKingInCheck(isWhiteTurn)) {
                return new GameState(boxes, isWhiteTurn, "Error: Cannot castle while in check!", false,
                        board.getHistory());
            }

            // Check "Pass Through" square
            int dir = (end.getX() - start.getX()) > 0 ? 1 : -1;
            Spot passThrough = board.getBox(start.getX() + dir, start.getY());
            // How to check if passThrough is attacked?
            // We can reuse isKingInCheck logic but for a specific square?
            // Let's extract `isSquareAttacked` helper.
            if (isSquareAttacked(passThrough, !isWhiteTurn)) {
                return new GameState(boxes, isWhiteTurn, "Error: Cannot castle through check!", false,
                        board.getHistory());
            }

            // Re-Apply Move
            pieceToKillSpot.setPiece(null);
            end.setPiece(piece);
            start.setPiece(null);
            rookStart.setPiece(null);
            rookEnd.setPiece(castlingRook);

            // Final check for "Into Check" (already covered by generic check below)
        }

        if (isKingInCheck(isWhiteTurn)) {
            // Revert Move
            start.setPiece(piece);
            end.setPiece(null);

            if (isEnPassant) {
                pieceToKillSpot.setPiece(pieceToKill);
            } else if (isCastling) {
                rookEnd.setPiece(null);
                rookStart.setPiece(castlingRook);
            } else {
                end.setPiece(pieceToKill);
            }

            return new GameState(boxes, isWhiteTurn, "Error: Move places King in check!", false, board.getHistory(),
                    isKingInCheck(isWhiteTurn), false);
        }

        // 6. Execution: Move is valid and committed
        piece.setMoved(true); // MARK AS MOVED
        if (isCastling)
            castlingRook.setMoved(true);

        // PAWN PROMOTION
        if (piece instanceof Pawn) {
            if ((piece.isWhite() && end.getY() == 7) || (!piece.isWhite() && end.getY() == 0)) {
                end.setPiece(new Queen(piece.isWhite())); // Auto-promote to Queen
            }
        }

        board.addMove(new Move(start, end, piece, pieceToKill));

        // 7. State Update: Flip the turn
        isWhiteTurn = !isWhiteTurn;

        // 8. Game Status: Check, Checkmate, Stalemate
        String status = "Move Successful";
        boolean inCheck = isKingInCheck(isWhiteTurn);
        boolean hasMoves = hasValidMoves(isWhiteTurn);
        boolean checkmate = false;

        if (inCheck) {
            if (!hasMoves) {
                checkmate = true;
                status = "Checkmate! " + (isWhiteTurn ? "Black" : "White") + " Wins!";
            } else {
                status = "Check!";
            }
        } else {
            if (!hasMoves) {
                status = "Stalemate! Game Draw.";
            }
        }

        // SPECIAL: "King Dies" Win Condition (User Request)
        // If checking for actual king capture is desired, strictly speaking checkmate
        // covers it.
        // But if we want to be explicit:
        if (checkmate) {
            // Game Over.
        }

        return new GameState(boxes, isWhiteTurn, status, true, board.getHistory(), inCheck, checkmate);
    }

    // Helper to check if a specific square is under attack by opponent
    private boolean isSquareAttacked(Spot targetError, boolean byWhite) {
        Spot[][] boxes = board.getBoxes();
        for (Spot[] row : boxes) {
            for (Spot spot : row) {
                Piece p = spot.getPiece();
                if (p != null && p.isWhite() == byWhite) {
                    if (p.canMove(board, spot, targetError)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isKingInCheck(boolean isWhite) {
        Spot kingSpot = null;
        Spot[][] boxes = board.getBoxes();

        // Find King
        for (Spot[] row : boxes) {
            for (Spot spot : row) {
                Piece p = spot.getPiece();
                if (p instanceof King && p.isWhite() == isWhite) {
                    kingSpot = spot;
                    break;
                }
            }
        }

        if (kingSpot == null)
            return false; // Should never happen

        // Check if any opponent piece attacks the King
        for (Spot[] row : boxes) {
            for (Spot spot : row) {
                Piece p = spot.getPiece();
                if (p != null && p.isWhite() != isWhite) {
                    if (p.canMove(board, spot, kingSpot)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean hasValidMoves(boolean isWhite) {
        Spot[][] boxes = board.getBoxes();

        for (Spot[] row : boxes) {
            for (Spot start : row) {
                Piece p = start.getPiece();
                if (p != null && p.isWhite() == isWhite) {
                    // Try all possible moves
                    for (Spot[] r : boxes) {
                        for (Spot end : r) {
                            if (p.canMove(board, start, end)) {
                                // Simulate
                                Piece captured = end.getPiece();
                                // Handle basic move simulation (ignoring EP complexity for existence check
                                // usually fine,
                                // but for correctness we should probably just do basic check or reuse move
                                // logic)
                                // To be 100% accurate we duplicate logic:

                                Spot pieceToKillSpot = end;
                                Piece pieceToKill = captured;
                                boolean isEnPassant = false;

                                if (p instanceof Pawn && Math.abs(start.getY() - end.getY()) == 1
                                        && end.getPiece() == null) {
                                    // En Passant suspect
                                    isEnPassant = true;
                                    pieceToKillSpot = board.getBox(start.getX(), end.getY());
                                    pieceToKill = pieceToKillSpot.getPiece();
                                }

                                pieceToKillSpot.setPiece(null);
                                end.setPiece(p);
                                start.setPiece(null);

                                boolean stillInCheck = isKingInCheck(isWhite);

                                // Revert
                                start.setPiece(p);
                                end.setPiece(null);
                                if (isEnPassant) {
                                    pieceToKillSpot.setPiece(pieceToKill);
                                } else {
                                    end.setPiece(pieceToKill);
                                }

                                if (!stillInCheck)
                                    return true; // Found at least one valid move
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}