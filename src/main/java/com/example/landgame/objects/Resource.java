package com.example.landgame.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Resource extends Entity {

    public Resource(int x, int y, int health) {
        super(x, y, health);
    }

    @Override
    public boolean sameTeam(Player player) {
        return false;
    }
}
