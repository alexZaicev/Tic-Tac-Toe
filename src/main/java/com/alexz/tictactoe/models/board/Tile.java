package com.alexz.tictactoe.models.board;

public enum Tile {
    A1,
    A2,
    A3,
    B1,
    B2,
    B3,
    C1,
    C2,
    C3,
    ;

    public static Tile fromInteger(final int value) {
        final int x = (int) Math.floor((double) value / 3) + 1;
        final int y = value - (x - 1) * 3;

        String name;
        if (y == 0) {
            name = "A";
        } else if (y == 1) {
            name = "B";
        } else {
            name = "C";
        }

        name = String.format("%s%d", name, x);
        return Tile.valueOf(name);
    }

    public int toInteger() {
        final String s = this.toString();
        final int x = Integer.parseInt(s.substring(1)) - 1;
        int y = 0;
        final String row = s.substring(0, 1);
        if (row.equalsIgnoreCase("B")) {
            y = 1;
        } else if (row.equalsIgnoreCase("C")) {
            y = 2;
        }
        return x * 3 + y;
    }
}
