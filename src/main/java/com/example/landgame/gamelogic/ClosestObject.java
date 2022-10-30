package com.example.landgame.gamelogic;

import com.example.landgame.objects.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClosestObject {
//    private final Entity entity;
    private final int distance;
    private final int moveNumber1;
    private final int moveNumber2;
}
