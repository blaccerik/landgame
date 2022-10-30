package com.example.landgame.objects;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Entity {
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    protected int x;
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    protected int y;
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    protected int health;


    public boolean takeDamage(Player player) {
        throw new RuntimeException("rer343434");
    }

    public boolean sameTeam(Player player) {
        throw new RuntimeException("rer32343434");
    }

}
