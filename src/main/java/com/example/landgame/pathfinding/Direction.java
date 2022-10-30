package com.example.landgame.pathfinding;

import com.example.landgame.enums.MoveType;
import com.example.landgame.objects.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Direction {

    private final int x;
    private final int y;
    private final int moveNumber;
    private final Entity entity;
    private final MoveType moveType;
    private float score;

    public Direction(int x, int y, int moveNumber, Entity entity, MoveType moveType, float score) {
        this.x = x;
        this.y = y;
        this.moveNumber = moveNumber;
        this.entity = entity;
        this.moveType = moveType;
        this.score = score;
    }
}
