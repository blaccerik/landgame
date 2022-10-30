package com.example.landgame.objects;

import com.example.landgame.Team;

public class House extends Building {


    public House(int x, int y, int health, Team team, long lastSpawn) {
        super(x, y, health, team, lastSpawn);
    }

    @Override
    public boolean takeDamage(Player player) {
        this.health -= 1;
        if (this.health == 0) {
            this.team.addHouse(-1);
            return true;
        }
        return false;
    }
}
