package com.example.landgame.pathfinding;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Vector {

    private final int distance;
    private final int move1;
    private final int move2;

    public Vector(int distance, int move1, int move2) {

        this.distance = distance;
        this.move1 = move1;
        this.move2 = move2;
    }
}
