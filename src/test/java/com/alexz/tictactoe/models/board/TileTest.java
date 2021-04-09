package com.alexz.tictactoe.models.board;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TileTest {

    @Test
    void testTileTransformation() {
        int value = Tile.A1.toInteger();
        Assertions.assertEquals(0, value);
        Assertions.assertEquals(Tile.A1, Tile.fromInteger(value));

        value = Tile.B3.toInteger();
        Assertions.assertEquals(7, value);
        Assertions.assertEquals(Tile.B3, Tile.fromInteger(value));

        value = Tile.C2.toInteger();
        Assertions.assertEquals(5, value);
        Assertions.assertEquals(Tile.C2, Tile.fromInteger(value));

        value = Tile.C3.toInteger();
        Assertions.assertEquals(8, value);
        Assertions.assertEquals(Tile.C3, Tile.fromInteger(value));
    }
}
