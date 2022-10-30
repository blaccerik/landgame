package com.example.landgame;

import com.example.landgame.map.Terrain;
import com.example.landgame.objects.Building;
import com.example.landgame.objects.Entity;
import com.example.landgame.objects.Player;
import com.example.landgame.objects.Resource;
import com.example.landgame.pathfinding.PathMatrix;
import com.example.landgame.pathfinding.Vector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.example.landgame.MainApplication.HEIGHT;
import static com.example.landgame.MainApplication.WIDTH;
import static org.junit.jupiter.api.Assertions.*;
import static com.example.landgame.enums.TerrainType.*;

class ExploreNodeTest {

    @Test
    void swapPath() {
        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Terrain terrain1 = new Terrain(i, j, LAND);
                terrain[i][j] = terrain1;
            }
        }
        List<Entity> players = new ArrayList<>();
        Game game = new Game(HEIGHT, WIDTH, terrain, players);
        PathMatrix pathMatrix = game.getPathMatrix();

        int move = pathMatrix.findMoveNumber(0,0, 1, 1);
        Vector vector = PathMatrix.decode(move);
        assertEquals(vector.getDistance(), 2);
        assertEquals(vector.getMove1(), 2);
        assertEquals(vector.getMove2(), 1);

        pathMatrix.swap(0,0,1,1);

        move = pathMatrix.findMoveNumber(0,0, 1, 1);
        vector = PathMatrix.decode(move);
        assertEquals(vector.getDistance(), 2);
        assertEquals(vector.getMove1(), 1);
        assertEquals(vector.getMove2(), 2);

        pathMatrix.swap(0,0,1,1);

        move = pathMatrix.findMoveNumber(0,0, 1, 1);
        vector = PathMatrix.decode(move);
        assertEquals(vector.getDistance(), 2);
        assertEquals(vector.getMove1(), 2);
        assertEquals(vector.getMove2(), 1);

        move = pathMatrix.findMoveNumber(1,0, 1, 1);
        vector = PathMatrix.decode(move);
        assertEquals(vector.getDistance(), 1);
        assertEquals(vector.getMove1(), 1);
        assertEquals(vector.getMove2(), 0);

        pathMatrix.swap(1,0,1,1);

        move = pathMatrix.findMoveNumber(1,0, 1, 1);
        vector = PathMatrix.decode(move);
        assertEquals(vector.getDistance(), 1);
        assertEquals(vector.getMove1(), 1);
        assertEquals(vector.getMove2(), 0);
    }

    @Test
    void encodeDecode() {
        int number = PathMatrix.encode(137,4,3);
        assertEquals(number, 8803);

        Vector vector = PathMatrix.decode(8803);
        assertEquals(vector.getDistance(), 137);
        assertEquals(vector.getMove1(), 4);
        assertEquals(vector.getMove2(), 3);

        number = PathMatrix.encode(1023,4,3);
        assertEquals(number, 65507);

        vector = PathMatrix.decode(65507);
        assertEquals(vector.getDistance(), 1023);
        assertEquals(vector.getMove1(), 4);
        assertEquals(vector.getMove2(), 3);

//        vector = PathMatrix.decode(-1);
//        assertNull(vector);
    }
}