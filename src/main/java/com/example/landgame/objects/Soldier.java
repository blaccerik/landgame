package com.example.landgame.objects;

import com.example.landgame.Team;

import static com.example.landgame.config.Config.soldierPlayerDamage;

public class Soldier extends Player {
    public Soldier(int x, int y, int health, Team team) {
        super(x, y, health, team, soldierPlayerDamage);
    }

    @Override
    public boolean takeDamage(Player player) {
        if (this.health > 0) {
            this.health -= player.damageToPlayers;
            if (this.health < 0) {
                this.team.addSoldier(-1);
                return true;
            }
        }
        return false;
    }
}
