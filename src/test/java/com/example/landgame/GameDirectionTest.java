package com.example.landgame;

import com.example.landgame.config.TeamConfig;
import com.example.landgame.map.Map;
import com.example.landgame.objects.Entity;
import com.example.landgame.objects.Farmer;
import com.example.landgame.objects.Stone;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.landgame.enums.TeamColor.BLUE;
import static com.example.landgame.enums.TeamColor.GREEN;
import static com.example.landgame.enums.TeamColor.RED;

public class GameDirectionTest {

    private final TeamConfig teamConfig = new TeamConfig(
            3,
            10,
            1,
            301,
            2,
            1);

    @Test
    void correctAnswer() {

        int size = 150;
        Map map = new Map(size, size, 69);
        Team blue = new Team(BLUE, teamConfig);
        Team red = new Team(RED, teamConfig);
        Team green = new Team(GREEN, teamConfig);

        List<Entity> entities = new ArrayList<>();
        entities.add(blue.createPlayer(142, 142, Farmer.class));
        entities.add(blue.createPlayer(140, 140, Farmer.class));
        entities.add(new Stone(141,141,10));
        entities.add(new Stone(141,142,10));
        Game game = new Game(map, entities);

        game.tick();

    }
}
