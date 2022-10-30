package com.example.landgame.objects;

public class Tree extends Resource {
    public Tree(int x, int y, int health) {
        super(x, y, health);
    }

    @Override
    public boolean takeDamage(Player player) {
        this.health -= 1;
        if (this.health == 0) {
            player.getTeam().addTree(1);
            return true;
        }
        return false;
    }
}
