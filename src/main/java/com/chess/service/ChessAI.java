package com.chess.service;

import com.chess.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ChessAI {

    private static final int MAX_DEPTH_HARD = 4;
    private static final int MAX_DEPTH_MEDIUM = 2;
    // Piece values: P=10, N=30, B=30, R=50, Q=90, K=900
    private static final int VAL_PAWN = 10;
    private static final int VAL_KNIGHT = 30;
    private static final int VAL_BISHOP = 30;
    private static final int VAL_ROOK = 50;
    private static final int VAL_QUEEN = 90;
    private static final int VAL_KING = 900;

    public Move getBestMove(Board originalBoard, String difficulty, boolean isWhite) {
        // Clone board to avoid mutating the game state
        Board simBoard = cloneBoard(originalBoard);

        List<Move> legalMoves = generateLegalMoves(simBoard, isWhite);
        if (legalMoves.isEmpty())
            return null;

        if ("EASY".equalsIgnoreCase(difficulty)) {
            return legalMoves.get(new Random().nextInt(legalMoves.size()));
        }

        int depth = "MEDIUM".equalsIgnoreCase(difficulty) ? MAX_DEPTH_MEDIUM : MAX_DEPTH_HARD;

        Move bestMove = null;
        int bestValue = isWhite ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        // Iterative deepening or just fixed depth? Fixed is fine for now.
        // Sort moves for better pruning (captured pieces first)
        sortMoves(legalMoves);

        for (Move move : legalMoves) {
            applyMove(simBoard, move);
            int boardValue = minimax(simBoard, depth - 1, alpha, beta, !isWhite);
            undoMove(simBoard, move);

            if (isWhite) {
                if (boardValue > bestValue) {
                    bestValue = boardValue;
                    bestMove = move;
                }
                alpha = Math.max(alpha, bestValue);
            } else {
                if (boardValue < bestValue) {
                    bestValue = boardValue;
                    bestMove = move;
                }
                beta = Math.min(beta, bestValue);
            }
            if (beta <= alpha)
                break;
        }

        return bestMove;
    }

    private int minimax(Board board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0) {
            return evaluateBoard(board);
        }

        List<Move> legalMoves = generateLegalMoves(board, isMaximizingPlayer);

        if (legalMoves.isEmpty()) {
            if (isKingInCheck(board, isMaximizingPlayer)) {
                return isMaximizingPlayer ? -10000 : 10000; // Checkmate
            }
            return 0; // Stalemate
        }

        sortMoves(legalMoves);

        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : legalMoves) {
                applyMove(board, move);
                int eval = minimax(board, depth - 1, alpha, beta, false);
                undoMove(board, move);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha)
                    break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : legalMoves) {
                applyMove(board, move);
                int eval = minimax(board, depth - 1, alpha, beta, true);
                undoMove(board, move);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha)
                    break;
            }
            return minEval;
        }
    }

    private void sortMoves(List<Move> moves) {
        // Simple heuristic: Captures first
        moves.sort((m1, m2) -> {
            int s1 = m1.getPieceKilled() != null ? getPieceValue(m1.getPieceKilled()) : 0;
            int s2 = m2.getPieceKilled() != null ? getPieceValue(m2.getPieceKilled()) : 0;
            return s2 - s1; // Descending
        });
    }

    private int evaluateBoard(Board board) {
        int whiteScore = 0;
        int blackScore = 0;

        Spot[][] boxes = board.getBoxes();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = boxes[i][j].getPiece();
                if (p != null) {
                    int val = getPieceValue(p);
                    // Add positional value (simplified)
                    val += getPositionBonus(p, i, j);

                    if (p.isWhite())
                        whiteScore += val;
                    else
                        blackScore += val;
                }
            }
        }
        return whiteScore - blackScore;
    }

    private int getPieceValue(Piece p) {
        if (p instanceof Pawn)
            return VAL_PAWN;
        if (p instanceof Knight)
            return VAL_KNIGHT;
        if (p instanceof Bishop)
            return VAL_BISHOP;
        if (p instanceof Rook)
            return VAL_ROOK;
        if (p instanceof Queen)
            return VAL_QUEEN;
        if (p instanceof King)
            return VAL_KING;
        return 0;
    }

    private int getPositionBonus(Piece p, int r, int c) {
        // Favors center control (+1 for center squares)
        if ((r == 3 || r == 4) && (c == 3 || c == 4))
            return 2;
        return 0;
    }

    // --- Board Manipulation ---

    // Deep clone
    private Board cloneBoard(Board original) {
        Board newBoard = new Board(); // Resets to start state
        // Clear it
        Spot[][] newBoxes = newBoard.getBoxes();
        Spot[][] oldBoxes = original.getBoxes();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece oldP = oldBoxes[i][j].getPiece();
                Piece newP = null;
                if (oldP != null) {
                    newP = clonePiece(oldP);
                }
                newBoxes[i][j].setPiece(newP);
            }
        }
        // Copy History if needed, or just clear. Since we apply moves, history is not
        // strictly needed
        // unless we validte Castling/FirstMove.
        // We preserve firstMove flag in Pieces, so castling logic should use Piece
        // state.
        return newBoard;
    }

    private Piece clonePiece(Piece p) {
        Piece newP = null;
        if (p instanceof Pawn)
            newP = new Pawn(p.isWhite());
        else if (p instanceof Rook)
            newP = new Rook(p.isWhite());
        else if (p instanceof Knight)
            newP = new Knight(p.isWhite());
        else if (p instanceof Bishop)
            newP = new Bishop(p.isWhite());
        else if (p instanceof Queen)
            newP = new Queen(p.isWhite());
        else if (p instanceof King)
            newP = new King(p.isWhite());

        if (newP != null)
            newP.setMoved(p.hasMoved());
        return newP;
    }

    // --- Move Generation ---

    private List<Move> generateLegalMoves(Board board, boolean isWhite) {
        List<Move> legalMoves = new ArrayList<>();
        Spot[][] boxes = board.getBoxes();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Spot start = boxes[i][j];
                Piece p = start.getPiece();
                if (p != null && p.isWhite() == isWhite) {
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            Spot end = boxes[x][y];
                            if (p.canMove(board, start, end)) {
                                // Simulate to check king safety
                                Move move = new Move(start, end, p, end.getPiece(), !p.hasMoved());
                                applyMove(board, move);
                                if (!isKingInCheck(board, isWhite)) {
                                    legalMoves.add(move);
                                }
                                undoMove(board, move); // Always revert
                            }
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    private void applyMove(Board board, Move move) {
        Spot start = move.getStart(); // These spots belong to 'board' because move was generated from 'board'
        Spot end = move.getEnd();
        Piece p = move.getPieceMoved();

        // Handle En Passant (Custom Logic needed if not in Move)
        // But for simulation, if Move doesn't store EP flag, we might miss capturing
        // the pawn.
        // However, if we just use standard moves for AI, we may ignore EP for now to
        // simplify,
        // OR checks if it's Pawn diagonal move to empty square.

        if (p instanceof Pawn && start.getY() != end.getY() && end.getPiece() == null) {
            // En Passant Capture
            Spot captureSpot = board.getBox(start.getX(), end.getY());
            captureSpot.setPiece(null); // Remove captured pawn
        }

        // Handle Castling
        // If King moves 2 squares
        if (p instanceof King && Math.abs(start.getY() - end.getY()) == 2) {
            int dir = end.getY() - start.getY(); // +2 or -2
            int rookX = start.getX();
            int rookY = dir > 0 ? 7 : 0;
            int rookEndY = start.getY() + (dir > 0 ? 1 : -1);

            Spot rookStart = board.getBox(rookX, rookY);
            Spot rookEnd = board.getBox(rookX, rookEndY);
            Piece rook = rookStart.getPiece();

            rookStart.setPiece(null);
            rookEnd.setPiece(rook);
            if (rook != null)
                rook.setMoved(true);
        }

        // Apply
        end.setPiece(p);
        start.setPiece(null);
        p.setMoved(true);

        // Promotion (Auto-Queen for AI)
        if (p instanceof Pawn) {
            if ((p.isWhite() && end.getX() == 7) || (!p.isWhite() && end.getX() == 0)) {
                end.setPiece(new Queen(p.isWhite()));
            }
        }
    }

    private void undoMove(Board board, Move move) {
        Spot start = move.getStart();
        Spot end = move.getEnd();
        Piece p = move.getPieceMoved();
        Piece killed = move.getPieceKilled();

        // Revert main move
        start.setPiece(p);
        end.setPiece(killed); // Restore captured piece (usually null, unless capture)
        p.setMoved(move.isFirstMove() ? false : true); // Ideally track 'wasMoved' in Move or assume heuristic
        // The isFirstMove flag in Move constructor is helpful!
        if (move.isFirstMove())
            p.setMoved(false);

        // Handle En Passant Undo
        if (p instanceof Pawn && start.getY() != end.getY() && killed == null) {
            // It was EP, so 'killed' pawn is not on 'end', it's on 'start.x, end.y'
            // Wait, generateMoves -> Move constructor -> killed is null (because end was
            // empty)
            // But we know it's EP. We need to restore the victim.
            // THIS IS TRICKY: We don't have the victim piece object in 'move'.
            // For now, AI might glitch on EP undo.
            // FIX: We can ignore EP undo correctness if we don't support EP in AI or if we
            // just restore 'a pawn'.
            Spot epSpot = board.getBox(start.getX(), end.getY());
            epSpot.setPiece(new Pawn(!p.isWhite()));
            end.setPiece(null);
        }

        // Handle Castling Undo
        if (p instanceof King && Math.abs(start.getY() - end.getY()) == 2) {
            int dir = end.getY() - start.getY();
            int rookX = start.getX();
            int rookY = dir > 0 ? 7 : 0;
            int rookEndY = start.getY() + (dir > 0 ? 1 : -1);

            Spot rookStart = board.getBox(rookX, rookY);
            Spot rookEnd = board.getBox(rookX, rookEndY);
            Piece rook = rookEnd.getPiece();

            rookEnd.setPiece(null);
            rookStart.setPiece(rook);
            if (rook != null)
                rook.setMoved(false);
        }
    }

    // Check detection (copied/adapted from ChessService)
    private boolean isKingInCheck(Board board, boolean isWhite) {
        Spot kingSpot = null;
        Spot[][] boxes = board.getBoxes();
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
            return false;

        for (Spot[] row : boxes) {
            for (Spot spot : row) {
                Piece p = spot.getPiece();
                if (p != null && p.isWhite() != isWhite) {
                    // Critical: canMove checks logic. logic depends on board state.
                    if (p.canMove(board, spot, kingSpot)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
