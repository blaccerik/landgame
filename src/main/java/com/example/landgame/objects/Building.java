package com.example.landgame.objects;

import com.example.landgame.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Building extends Entity {

    private long lastSpawn;
    protected final Team team;

    public Building(int x, int y, int health, Team team, long lastSpawn) {
        super(x, y, health);
        this.team = team;
        this.lastSpawn = lastSpawn;
    }

    @Override
    public boolean sameTeam(Player player) {
        return player.getTeam() == this.team;
    }

    @Override
    public boolean takeDamage(Player player) {
        throw new RuntimeException("rer343434");
    }

}
