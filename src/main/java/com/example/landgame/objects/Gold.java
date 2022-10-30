package com.example.landgame.objects;

public class Gold extends Resource {
    public Gold(int x, int y, int health) {
        super(x, y, health);
    }

    @Override
    public boolean takeDamage(Player player) {
        this.health -= 1;
        if (this.health == 0) {
            player.getTeam().addGold(1);
            return true;
        }
        return false;
    }
}
