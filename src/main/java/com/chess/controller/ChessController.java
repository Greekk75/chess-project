package com.chess.controller;

import com.chess.dto.GameState;
import com.chess.dto.MoveRequest;
import com.chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chess")
public class ChessController {

    @Autowired
    private ChessService chessService;

    @GetMapping("/show-board")
    public GameState getBoard() {
        return chessService.getGameState();
    }

    @PostMapping("/move")
    public GameState makeMove(@RequestBody MoveRequest move) {
        System.out.println("Move Request: " + move.getStartX() + "," + move.getStartY() + " -> " + move.getEndX() + ","
                + move.getEndY() + " Prom: " + move.getPromotionPiece());
        return chessService.movePiece(move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY(),
                move.getPromotionPiece());
    }

    @PostMapping("/reset")
    public GameState resetGame() {
        chessService.resetBoard();
        return chessService.getGameState();
    }

    @PostMapping("/undo")
    public GameState undoLastMove() {
        return chessService.undoLastMove();
    }
}
