package com.example.landgame.objects;

import com.example.landgame.Team;

import static com.example.landgame.config.Config.farmerPlayerDamage;

public class Farmer extends Player {
    public Farmer(int x, int y, int health, Team team) {
        super(x, y, health, team, farmerPlayerDamage);
    }

    @Override
    public boolean takeDamage(Player player) {
        if (this.health > 0) {
            this.health -= player.damageToPlayers;
            if (this.health <= 0) {
                this.team.addFarmer(-1);
                return true;
            }
        }
        return false;
    }
}
