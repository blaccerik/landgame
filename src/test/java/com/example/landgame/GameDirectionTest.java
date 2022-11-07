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
        // 2500
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                entities.add(blue.createPlayer(i,j,Farmer.class));
            }
        }
        // 1000
        for (int i = 100; i < 120; i++) {
            for (int j = 100; j < 150; j++) {
                entities.add(new Stone(i,j,100));
            }
        }
        Game game = new Game(map, entities);
        game.tick();
        game.tick();

    }
}
