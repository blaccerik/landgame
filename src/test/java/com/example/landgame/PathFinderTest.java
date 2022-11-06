package com.example.landgame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathFinderTest {

//    @Test
//    void findPathSimple() { // takes 2.3 sec
//
//        // generate map
//        Random random = new Random();
//        random.setSeed(0);
//        TerrainGeneration terrainGeneration = new TerrainGeneration(random, HEIGHT, WIDTH);
//        Terrain[][] terrain = terrainGeneration.generateTerrain();
//        List<Resource> resources = terrainGeneration.generateResources();
//        List<Player> players = new ArrayList<>();
//
//        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources);
//        PathFinder pathFinder = game.getPathFinder();
//        MoveNode moveNode;
//        moveNode = pathFinder.findPath(80,80,85,85);
//        assertEquals(moveNode.getDistance(), 10);
//        assertEquals(moveNode.getConnectedNodes(), 2);
//        assertTrue(moveNode.getMoveNumber() == 21 || moveNode.getMoveNumber() == 12 );
//    }
//
//    @Test
//    @Timeout(4)
//    void findPathGetSamePath() { // takes 2.5 - 3.5 sec
//
//        // generate map
//        Random random = new Random();
//        random.setSeed(0);
//        TerrainGeneration terrainGeneration = new TerrainGeneration(random, HEIGHT, WIDTH);
//        Terrain[][] terrain = terrainGeneration.generateTerrain();
//        List<Resource> resources = terrainGeneration.generateResources();
//        List<Player> players = new ArrayList<>();
//
//        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources);
//        PathFinder pathFinder = game.getPathFinder();
//
//        for (int i = 0; i < 1_000_000; i++) {
//            MoveNode moveNode;
//            moveNode = pathFinder.findPath(80,80,85,85);
//            assertEquals(moveNode.getDistance(), 10);
//            assertEquals(moveNode.getDistance(), 10);
//            assertEquals(moveNode.getConnectedNodes(), 2);
//            assertTrue(moveNode.getMoveNumber() == 21 || moveNode.getMoveNumber() == 12);
//        }
//    }
//
//    @Test
//    @Timeout(4)
//    void findPathGetAllPointInRadius() { // takes 2.5 - 3.5 sec
//
//        // generate map
//        Random random = new Random();
//        random.setSeed(0);
//        TerrainGeneration terrainGeneration = new TerrainGeneration(random, HEIGHT, WIDTH);
//        Terrain[][] terrain = terrainGeneration.generateTerrain();
//        List<Resource> resources = terrainGeneration.generateResources();
//        List<Player> players = new ArrayList<>();
//
//        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources);
//        PathFinder pathFinder = game.getPathFinder();
//
//        MoveNode moveNode;
//
//        moveNode = pathFinder.findPath(60, 73, 60, 70);
//        assertEquals(moveNode.getConnectedNodes(), 1);
//        moveNode = pathFinder.findPath(61, 72, 60, 70);
//        assertEquals(moveNode.getConnectedNodes(), 2);
//        moveNode = pathFinder.findPath(62, 71, 60, 70);
//        assertEquals(moveNode.getConnectedNodes(), 2);
//        moveNode = pathFinder.findPath(63, 70, 60, 70);
//        assertEquals(moveNode.getConnectedNodes(), 1);
//        moveNode = pathFinder.findPath(62, 69, 60, 70);
//        assertEquals(moveNode.getConnectedNodes(), 2);
//        moveNode = pathFinder.findPath(61, 68, 60, 70);
//        assertEquals(moveNode.getConnectedNodes(), 2);
//        moveNode = pathFinder.findPath(60, 67, 60, 70);
//        assertEquals(moveNode.getConnectedNodes(), 1);
//        moveNode = pathFinder.findPath(59, 68, 60, 70);
//        assertEquals(moveNode.getConnectedNodes(), 2);
//        moveNode = pathFinder.findPath(58, 69, 60, 70);
//        assertEquals(moveNode.getConnectedNodes(), 2);
//        moveNode = pathFinder.findPath(57, 70, 60, 70);
//        assertEquals(moveNode.getConnectedNodes(), 1);
//        moveNode = pathFinder.findPath(58, 71, 60, 70);
//        assertEquals(moveNode.getConnectedNodes(), 2);
//        moveNode = pathFinder.findPath(59, 72, 60, 70);
//        assertEquals(moveNode.getConnectedNodes(), 2);
//    }
//
//    @Test
//    @Timeout(4)
//    void findPathLongDistanceEndStaysSame() { // 2.3
//
//        // generate map
//        Random random = new Random();
//        random.setSeed(0);
//
//        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                Terrain terrain1 = new Terrain(i, j, Type.LAND);
//                terrain[i][j] = terrain1;
//            }
//        }
//
//        List<Player> players = new ArrayList<>();
//        List<Resource> resources = new ArrayList<>();
//        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources);
//        PathFinder pathFinder = game.getPathFinder();
//
//        MoveNode moveNode;
//
//        for (int i = 0; i < HEIGHT; i++) {
//            moveNode = pathFinder.findPath(i, HEIGHT - 1 - i, 149, 0);
//            if (i == HEIGHT - 1 ) {
//                assertEquals(moveNode.getConnectedNodes(), 0);
//            } else {
//                assertEquals(moveNode.getConnectedNodes(), 2);
//            }
//        }
//    }
//
//    @Test
//    @Timeout(8)
//    void findPathLongDistanceEndMovesCloser() { // 6-7s
//
//        // generate map
//        Random random = new Random();
//        random.setSeed(0);
//
//        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                Terrain terrain1 = new Terrain(i, j, Type.LAND);
//                terrain[i][j] = terrain1;
//            }
//        }
//
//        List<Player> players = new ArrayList<>();
//        List<Resource> resources = new ArrayList<>();
//        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources);
//        PathFinder pathFinder = game.getPathFinder();
//
//        MoveNode moveNode;
//
//        for (int i = 0; i < HEIGHT; i++) {
//            moveNode = pathFinder.findPath(0, HEIGHT - 1, HEIGHT - i - 1, i);
//            if (i == HEIGHT - 1 ) {
//                assertEquals(moveNode.getConnectedNodes(), 0);
//            } else {
//                assertEquals(moveNode.getConnectedNodes(), 2);
//            }
//        }
//    }
//
//    @Test
//    @Timeout(4)
//    void findPathNoPathIsStored() { //  2.6s
//
//        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                Terrain terrain1 = new Terrain(i, j, Type.LAND);
//                terrain[i][j] = terrain1;
//            }
//        }
//        terrain[1][0].setType(Type.WATER);
//        terrain[0][1].setType(Type.WATER);
//        terrain[1][1].setType(Type.WATER);
//
//        List<Player> players = new ArrayList<>();
//        List<Resource> resources = new ArrayList<>();
//
//        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources);
//        PathFinder pathFinder = game.getPathFinder();
//        MoveNode moveNode;
//        for (int i = 0; i < 1000; i++) {
//            moveNode = pathFinder.findPath(4, 4, 2, 2);
//            assertEquals(moveNode.getConnectedNodes(), 2);
//            assertEquals(moveNode.getDistance(), 4);
//            assertTrue(moveNode.getMoveNumber() == 43 || moveNode.getMoveNumber() == 34);
//        }
//
//        for (int i = 0; i < 1000; i++) {
//            moveNode = pathFinder.findPath(0, 0, 2, 2);
//            assertEquals(moveNode.getConnectedNodes(), 0);
//            assertEquals(moveNode.getDistance(), -1);
//        }
//    }
//
//    @Test
//    @Timeout(4)
//    void findPathNoPath() { //  1.5s
//
//        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                Terrain terrain1 = new Terrain(i, j, Type.LAND);
//                terrain[i][j] = terrain1;
//            }
//        }
//        terrain[1][0].setType(Type.WATER);
//        terrain[0][1].setType(Type.WATER);
//        terrain[1][1].setType(Type.WATER);
//
//        List<Player> players = new ArrayList<>();
//        List<Resource> resources = new ArrayList<>();
//
//        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources);
//        PathFinder pathFinder = game.getPathFinder();
//        MoveNode moveNode;
//        moveNode = pathFinder.findPath(4, 4, 2, 2);
//        assertEquals(moveNode.getConnectedNodes(), 2);
//        assertEquals(moveNode.getDistance(), 4);
//        assertTrue(moveNode.getMoveNumber() == 34 || moveNode.getMoveNumber() == 43);
//
//        moveNode = pathFinder.findPath(0, 0, 2, 2);
//        assertEquals(moveNode.getConnectedNodes(), 0);
//        assertEquals(moveNode.getDistance(), -1);
//        assertEquals(moveNode.getX(), 0);
//        assertEquals(moveNode.getY(), 0);
//
//        moveNode = pathFinder.findPath(-3, -3, 2, 2);
//        assertEquals(moveNode.getConnectedNodes(), 0);
//        assertEquals(moveNode.getDistance(), -1);
//        assertEquals(moveNode.getX(), -1);
//        assertEquals(moveNode.getY(), -1);
//
//        moveNode = pathFinder.findPath(4, 4, -3, -3);
//        assertEquals(moveNode.getConnectedNodes(), 0);
//        assertEquals(moveNode.getDistance(), -1);
//        assertEquals(moveNode.getX(), -1);
//        assertEquals(moveNode.getY(), -1);
//    }
//
//    @Test
//    @Timeout(4)
//    void findPathSameCoords() { //  1.5s
//
//        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                Terrain terrain1 = new Terrain(i, j, Type.LAND);
//                terrain[i][j] = terrain1;
//            }
//        }
//
//        List<Player> players = new ArrayList<>();
//        List<Resource> resources = new ArrayList<>();
//
//        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources);
//        PathFinder pathFinder = game.getPathFinder();
//        MoveNode moveNode;
//        moveNode = pathFinder.findPath(0, 0, 0, 0);
//        assertEquals(moveNode.getConnectedNodes(), 0);
//        assertEquals(moveNode.getDistance(), 0);
//    }
}