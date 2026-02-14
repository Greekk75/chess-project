package com.chess.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MoveRequest {
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }

    @JsonProperty("promotionPiece")
    private String promotionPiece;

    public String getPromotionPiece() {
        return promotionPiece;
    }

    public void setPromotionPiece(String promotionPiece) {
        this.promotionPiece = promotionPiece;
    }
}
