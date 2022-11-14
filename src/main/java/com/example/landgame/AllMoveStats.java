package com.example.landgame;

import com.example.landgame.pathfinding.Vector;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class AllMoveStats {
    private final MoveStats moveStats1;
    private final MoveStats moveStats2;
    private final MoveStats moveStats3;
    private final MoveStats moveStats4;

    public AllMoveStats() {
        this.moveStats1 = new MoveStats();
        this.moveStats2 = new MoveStats();
        this.moveStats3 = new MoveStats();
        this.moveStats4 = new MoveStats();
    }

    public AllMoveStats(int i) {
        this.moveStats1 = new MoveStats(i);
        this.moveStats2 = new MoveStats(i);
        this.moveStats3 = new MoveStats(i);
        this.moveStats4 = new MoveStats(i);
    }

    public AllMoveStats(
            int low1, int total1,
            int low2, int total2,
            int low3, int total3,
            int low4, int total4
    ) {
        this.moveStats1 = new MoveStats(low1, total1);
        this.moveStats2 = new MoveStats(low2, total2);
        this.moveStats3 = new MoveStats(low3, total3);
        this.moveStats4 = new MoveStats(low4, total4);
    }

    public void addVector(Vector vector) {
        int distance = vector.getDistance();
        int move1 = vector.getMove1();
        int move2 = vector.getMove2();

        for (int i = 1; i <= 4; i++) {
            if (i == move1 || i == move2) {
                get(i).addDistance(distance - 1);
            } else {
                get(i).addDistance(distance + 1);
            }

        }
    }

    public void addVector2(Vector vector) {
        int distance = vector.getDistance();
        int move1 = vector.getMove1();
        int move2 = vector.getMove2();

        for (int i = 1; i <= 4; i++) {
            if (i == move1 || i == move2) {
                get(i).addDistance2(distance - 1);
            } else {
                get(i).addDistance2(distance + 1);
            }

        }
    }

    public void removeVector(Vector vector) {
        int distance = vector.getDistance();
        int move1 = vector.getMove1();
        int move2 = vector.getMove2();

        for (int i = 1; i <= 4; i++) {
            if (i == move1 || i == move2) {
                get(i).removeDistance(distance - 1);
            } else {
                get(i).removeDistance(distance + 1);
            }

        }
    }

    public MoveStats get(int i) {
        switch (i) {
            case 1:
                return this.moveStats1;
            case 2:
                return this.moveStats2;
            case 3:
                return this.moveStats3;
            case 4:
                return this.moveStats4;
        }
        return null;
    }
}
