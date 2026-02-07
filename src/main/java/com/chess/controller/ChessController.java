package com.chess.controller;

import com.chess.dto.GameState;
import com.chess.dto.MoveRequest;
import com.chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
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
        return chessService.movePiece(move.getStartX(), move.getStartY(), move.getEndX(), move.getEndY());
    }

    @PostMapping("/reset")
    public GameState resetGame() {
        chessService.resetBoard();
        return chessService.getGameState();
    }
}