package com.example.landgame.pathfinding;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExploreNode {
    private final int x;
    private final int y;
    private final int distance;
    private final int move1;
    private int move2;

    public ExploreNode(int x, int y, int distance, int move1) {
        this.x = x;
        this.y = y;
        this.distance = distance;
        this.move1 = move1;
        this.move2 = 0;
    }
}
