package com.example.landgame;


import com.example.landgame.config.TeamConfig;
import com.example.landgame.map.Map;
import com.example.landgame.objects.Entity;
import com.example.landgame.objects.Farmer;
import com.google.gson.Gson;
import javafx.scene.canvas.Canvas;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.landgame.enums.TeamColor.BLUE;
import static com.example.landgame.enums.TeamColor.GREEN;
import static com.example.landgame.enums.TeamColor.RED;


@Getter
@AllArgsConstructor
class Pair {

}

@Getter
public class Test {


    public static void main(String[] args) throws IOException {

        int size = 150;
        Map map = new Map(size, size, 0);

        TeamConfig teamConfig = new TeamConfig(
                3,
                10,
                1,
                301,
                2,
                1);
        Team blue = new Team(BLUE, teamConfig,0);
        Team red = new Team(RED, teamConfig,0);
        Team green = new Team(GREEN, teamConfig,0);

        List<Entity> entities = new ArrayList<>();
        entities.add(blue.createPlayer(149, 149, Farmer.class));
        entities.add(blue.createPlayer(139, 139, Farmer.class));
        entities.add(red.createPlayer(39, 4, Farmer.class));
        entities.add(red.createPlayer(55, 1, Farmer.class));
        entities.add(green.createPlayer(149, 0, Farmer.class));
        entities.addAll(map.getTerrainGeneration().generateResources());

        Game game = new Game(map, entities);
        game.tick();

//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        list.add(4);
//        List<Integer> list2 = new ArrayList<>();
//        list2.add(0);
//        list2.add(1);
//        list2.add(2);
//        list2.add(3);
//        list2.add(4);
//        Random random = new Random();
//        long s = System.currentTimeMillis();
//        for (int i = 0; i < 1_000_000; i++) {
//            int move1 = list.get(random.nextInt(list.size()));
//            int move2 = list2.get(random.nextInt(list2.size()));
//            for (int j = 1; j < 5; j++) {
//                if (j == move1 || j == move2) {
//                    allMoveStats.addStat(j, 100-j);
//                } else {
//                    allMoveStats.addStat(j, 111-j);
//                }
//
//            }
//        }
//
//        long e = System.currentTimeMillis();
//        System.out.println(allMoveStats);
//        System.out.println("diff " + (e - s));
    }
}
