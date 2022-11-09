package com.example.landgame;

import com.example.landgame.config.TeamConfig;
import com.example.landgame.map.Map;
import com.example.landgame.map.Terrain;
import com.example.landgame.map.TerrainGeneration;
import com.example.landgame.objects.Entity;
import com.example.landgame.objects.Farmer;
import com.example.landgame.visual.Renderer;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.landgame.enums.TeamColor.BLUE;
import static com.example.landgame.enums.TeamColor.GREEN;
import static com.example.landgame.enums.TeamColor.RED;

public class MainApplication extends javafx.application.Application {

    public static final int TILE_SIZE = 5;
    private static Canvas canvas;
    private final Text text = new Text();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // info box
        HBox hBox = new HBox();
        hBox.setMinHeight(60);
        hBox.setMaxHeight(60);
        hBox.getChildren().add(text);
        hBox.setStyle("-fx-background-color: #f8f802; ");

        // generate map
        int size = 150;
        Map map = new Map(size, size, 0);

        canvas = new Canvas(map.getWidth() * TILE_SIZE, map.getHeight() * TILE_SIZE);

        TeamConfig teamConfig = new TeamConfig(
                3,
                10,
                1,
                301,
                2,
                1);
        Team blue = new Team(BLUE, teamConfig, 0);
        Team red = new Team(RED, teamConfig, 0);
        Team green = new Team(GREEN, teamConfig, 0);

        List<Entity> entities = new ArrayList<>();
        entities.add(blue.createPlayer(149, 149, Farmer.class));
        entities.add(blue.createPlayer(139, 139, Farmer.class));
        entities.add(red.createPlayer(39, 4, Farmer.class));
        entities.add(red.createPlayer(55, 1, Farmer.class));
        entities.add(green.createPlayer(149, 0, Farmer.class));
        entities.addAll(map.getTerrainGeneration().generateResources());

//        // generate game
//        List<Object> list = new ArrayList<>();
//        list.add(new Base(70, 70, 10));
//        list.add(new Worker(71, 70, 5));
//        list.add(new Rock(80, 80, 5));



        long start = System.currentTimeMillis();
        Game game = new Game(map, entities);
        long end = System.currentTimeMillis();

        Renderer renderer = new Renderer(canvas, text, map.getHeight(), map.getWidth());

        System.out.println("Diff " + (end - start) + " ms");
        System.out.println("Memory");
        System.out.println(Runtime.getRuntime().totalMemory());
        System.out.println(Runtime.getRuntime().maxMemory());
        System.out.println(Runtime.getRuntime().freeMemory());
        System.out.println("------");

        renderer.render(game.getMap());

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(hBox);
        borderPane.setCenter(canvas);

        Scene scene = new Scene(borderPane);
        primaryStage.setTitle("test");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        AnimationTimer animationTimer = new AnimationTimer() {
            private long lastUpdate = 0 ;
            private static final long min_frame = 16_000_000;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= min_frame) {
//                    long start_time = System.currentTimeMillis();
                    game.tick();
//                    long end_time = System.currentTimeMillis();
//                    double difference = (end_time - start_time);
////                    System.out.println("That took " + difference + " milliseconds");
//                    lastUpdate = now;
                    lastUpdate = 9555_555_555L;

                    renderer.render(game.getMap());
                    renderer.upDateStats(game);
                }
            }
        };
        animationTimer.start();
    }
}
