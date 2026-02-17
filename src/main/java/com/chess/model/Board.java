package com.chess.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Spot[][] boxes = new Spot[8][8];
    private List<Move> history = new ArrayList<>();

    public Board() {
        this.resetBoard();
    }

    public void resetBoard() {
        // 1. Initialize empty spots
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boxes[i][j] = new Spot(i, j, null);
            }
        }
        history.clear();

        // 2. Initialize White Pieces
        boxes[0][0].setPiece(new Rook(true));
        boxes[0][1].setPiece(new Knight(true));
        boxes[0][2].setPiece(new Bishop(true));
        boxes[0][3].setPiece(new Queen(true));
        boxes[0][4].setPiece(new King(true));
        boxes[0][5].setPiece(new Bishop(true));
        boxes[0][6].setPiece(new Knight(true));
        boxes[0][7].setPiece(new Rook(true));
        for (int i = 0; i < 8; i++)
            boxes[1][i].setPiece(new Pawn(true));

        // 3. Initialize Black Pieces
        boxes[7][0].setPiece(new Rook(false));
        boxes[7][1].setPiece(new Knight(false));
        boxes[7][2].setPiece(new Bishop(false));
        boxes[7][3].setPiece(new Queen(false));
        boxes[7][4].setPiece(new King(false));
        boxes[7][5].setPiece(new Bishop(false));
        boxes[7][6].setPiece(new Knight(false));
        boxes[7][7].setPiece(new Rook(false));
        for (int i = 0; i < 8; i++)
            boxes[6][i].setPiece(new Pawn(false));
    }

    public Spot getBox(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7)
            return null;
        return boxes[x][y];
    }

    public Spot[][] getBoxes() {
        return boxes;
    }

    public void setBoxes(Spot[][] boxes) {
        this.boxes = boxes;
    }

    public void addMove(Move move) {
        history.add(move);
    }

    public Move getLastMove() {
        if (history.isEmpty())
            return null;
        return history.get(history.size() - 1);
    }

    public Move removeLastMove() {
        if (history.isEmpty())
            return null;
        return history.remove(history.size() - 1);
    }

    public List<Move> getHistory() {
        return history;
    }

}