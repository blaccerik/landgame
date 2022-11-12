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
