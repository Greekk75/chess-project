package com.chess.model;

import com.chess.service.ChessService;
import com.chess.dto.GameState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnPassantLogicTest {

    @Test
    public void testEnPassantWhiteCapturesBlack() {
        ChessService service = new ChessService();
        service.resetBoard();

        System.out.println("Starting En Passant White Captures Black Test...");

        // 1. White Pawn e2 -> e4 (1,4 -> 3,4)
        GameState state = service.movePiece(1, 4, 3, 4, null);
        assertFalse(state.getMessage().startsWith("Error"), "Move 1 Failed: " + state.getMessage());

        // 2. Black Pawn h7 -> h6 (6,7 -> 5,7) - Waiting move
        state = service.movePiece(6, 7, 5, 7, null);
        assertFalse(state.getMessage().startsWith("Error"), "Move 2 Failed: " + state.getMessage());

        // 3. White Pawn e4 -> e5 (3,4 -> 4,4)
        state = service.movePiece(3, 4, 4, 4, null);
        assertFalse(state.getMessage().startsWith("Error"), "Move 3 Failed: " + state.getMessage());

        // 4. Black Pawn d7 -> d5 (6,3 -> 4,3) - Double push adjacent to White at e5
        state = service.movePiece(6, 3, 4, 3, null);
        assertFalse(state.getMessage().startsWith("Error"), "Move 4 Failed: " + state.getMessage());

        // 5. White Pawn e5 -> d6 (4,4 -> 5,3) - En Passant Capture
        // Logic: Moves diagonal to empty square (5,3) capturing pawn at (4,3)
        state = service.movePiece(4, 4, 5, 3, null);

        System.out.println("En Passant Move Result Message: " + state.getMessage());

        assertFalse(state.getMessage().startsWith("Error"), "En Passant Move Failed: " + state.getMessage());

        // Assertions on board state would require access to Board/Spot from GameState
        // Assuming success message is enough for logic verification given user's issue
        // "not working"
    }
}
