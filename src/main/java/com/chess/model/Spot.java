package com.chess.model;

public class Spot {
    private Piece piece;
    private int x;
    private int y;

    public Spot(int x, int y, Piece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }

    // These 3 methods are what Rook.java is looking for:
    public Piece getPiece() { return piece; }
    public int getX() { return x; }
    public int getY() { return y; }

    public void setPiece(Piece piece) { this.piece = piece; }
}