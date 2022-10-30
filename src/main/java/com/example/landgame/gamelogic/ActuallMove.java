package com.example.landgame.gamelogic;

import com.example.landgame.enums.MoveType;
import com.example.landgame.gamelogic.Coords;
import com.example.landgame.objects.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActuallMove {

    private final int moveX;
    private final int moveY;
    private final float score;
    private final MoveType moveType;
    private final Entity entity;

//    public ActuallMove(Coords coords, int score, MoveType moveType, Entity entity) {
//        this.moveX = coords.getX();
//        this.moveY = coords.getY();
//        this.score = score;
//        this.moveType = moveType;
//        this.entity = entity;
//    }

    @Override
    public String toString() {
        return "Move: " + this.moveX + " " + this.moveY + " " + this.score + " " + this.moveType + " " + this.entity;
    }
}
