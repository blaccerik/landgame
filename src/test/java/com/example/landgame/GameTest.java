package com.example.landgame;

import com.example.landgame.config.TeamConfig;
import com.example.landgame.map.Terrain;
import com.example.landgame.objects.Building;
import com.example.landgame.objects.Entity;
import com.example.landgame.objects.Farmer;
import com.example.landgame.objects.Gold;
import com.example.landgame.objects.House;
import com.example.landgame.objects.Player;
import com.example.landgame.objects.Stone;
import com.example.landgame.pathfinding.Direction;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.example.landgame.enums.TeamColor.*;
import static com.example.landgame.enums.TerrainType.*;
import static com.example.landgame.enums.MoveType.*;
import static com.example.landgame.config.Config.*;

class GameTest {

//    private static final TeamConfig teamConfig = new TeamConfig(
//            1,
//            1,
//            1,
//            1,
//            1,
//            1
//    );
//
////    @Test
////    void playersStuck() {
////        // generate map
////        Random random = new Random();
////        random.setSeed(0);
////        TerrainGeneration terrainGeneration = new TerrainGeneration(random, HEIGHT, WIDTH);
////        Terrain[][] terrain = terrainGeneration.generateTerrain();
////
////        List<Resource> resources = terrainGeneration.generateResources();
////
////        Player player1 = new Player(77, 60, 3, RED, damageToPlayers);
////        Player player2 = new Player(77, 61, 3, RED, damageToPlayers);
////        Player player3 = new Player(135, 135, 3, RED, damageToPlayers);
////
////        // generate players
////        List<Player> players = Arrays.asList(player1, player2, player3);
////        List<Building> buildings = new ArrayList<>();
////
////        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources, buildings);
////
////        assertEquals(player1.getX(), 77);
////        assertEquals(player1.getY(), 60);
////        assertEquals(player2.getX(), 77);
////        assertEquals(player2.getY(), 61);
////        assertEquals(player3.getX(), 135);
////        assertEquals(player3.getY(), 135);
////        assertEquals(players.size(), 3);
////
////        game.tick();
////
////        assertTrue(player2.getX() != 77 || player2.getY() != 61);
//////        assertTrue(player1.getX() == 77 && player1.getY() == 60);  // sometime moves depening on random
////        assertTrue(player3.getX() != 135 || player3.getY() != 135);
////        assertEquals(players.size(), 3);
////    }
////
////    @Test
////    void playersStuck2() {
////
////        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
////        for (int i = 0; i < WIDTH; i++) {
////            for (int j = 0; j < HEIGHT; j++) {
////                Terrain terrain1 = new Terrain(i, j, LAND);
////                terrain[i][j] = terrain1;
////            }
////        }
////        List<Resource> resources = new ArrayList<>();
////        List<Building> buildings = new ArrayList<>();
////        Player player1 = new Player(75, 70, 10, RED, damageToPlayers);
////        Player player2 = new Player(73, 70, 10, RED, damageToPlayers);
////        Player player3 = new Player(70, 70, 10, BLUE, damageToPlayers);
////        List<Player> players = Arrays.asList(player1, player2, player3);
////        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources, buildings);
////
////        assertEquals(player1.getX(), 75);
////        assertEquals(player1.getY(), 70);
////        assertEquals(player2.getX(), 73);
////        assertEquals(player2.getY(), 70);
////        assertEquals(player3.getX(), 70);
////        assertEquals(player3.getY(), 70);
////        assertEquals(players.size(), 3);
////
////        game.tick();
////
////        assertEquals(player1.getX(), 74);
////        assertEquals(player1.getY(), 70);
////        assertEquals(player2.getX(), 72);
////        assertEquals(player2.getY(), 70);
////        assertEquals(player3.getX(), 71);
////        assertEquals(player3.getY(), 70);
////        assertEquals(players.size(), 3);
////
////        game.tick();
////
////        assertEquals(player1.getX(), 73);
////        assertEquals(player1.getY(), 70);
////        assertEquals(player2.getX(), 72);
////        assertEquals(player2.getY(), 70);
////        assertEquals(player2.getHealth(), 9);
////        assertEquals(player3.getX(), 71);
////        assertEquals(player3.getY(), 70);
////        assertEquals(player3.getHealth(), 9);
////        assertEquals(players.size(), 3);
////
////        game.tick();
////
////        assertFalse(player1.getX() == 73 && player1.getY() == 70);
////        assertEquals(player2.getX(), 72);
////        assertEquals(player2.getY(), 70);
////        assertEquals(player2.getHealth(), 8);
////        assertEquals(player3.getX(), 71);
////        assertEquals(player3.getY(), 70);
////        assertEquals(player3.getHealth(), 8);
////        assertEquals(players.size(), 3);
////    }
////
////    @Test
////    void getBestMoveDiagonal() {
////        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
////        for (int i = 0; i < WIDTH; i++) {
////            for (int j = 0; j < HEIGHT; j++) {
////                Terrain terrain1 = new Terrain(i, j, LAND);
////                terrain[i][j] = terrain1;
////            }
////        }
////
////        List<Resource> resources = new ArrayList<>();
////        resources.add(new Resource(75, 75, 5, STONE));
////        List<Building> buildings = new ArrayList<>();
////        Player player1 = new Player(70, 70, 10, RED, damageToPlayers);
////        List<Player> players = Arrays.asList(player1);
////        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources, buildings);
////        ActuallMove move = game.generateBestMove(player1);
////        assertEquals(move.getMoveType(), MOVE);
////        assertNull(move.getEntity());
////        assertFalse(move.getMoveX() == 70 && move.getMoveY() == 70);
////        assertNotEquals(69, move.getMoveX());
////        assertNotEquals(69, move.getMoveY());
////    }
////
////    @Test
////    void getBestMoveStraight() {
////        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
////        for (int i = 0; i < WIDTH; i++) {
////            for (int j = 0; j < HEIGHT; j++) {
////                Terrain terrain1 = new Terrain(i, j, LAND);
////                terrain[i][j] = terrain1;
////            }
////        }
////
////        List<Resource> resources = new ArrayList<>();
////        resources.add(new Resource(70, 75, 5, STONE));
////        List<Building> buildings = new ArrayList<>();
////        Player player1 = new Player(70, 70, 10, RED, damageToPlayers);
////        List<Player> players = Arrays.asList(player1);
////        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources, buildings);
////        ActuallMove move = game.generateBestMove(player1);
////        assertEquals(move.getMoveType(), MOVE);
////        assertNull(move.getEntity());
////        assertEquals(move.getMoveX(), 70);
////        assertEquals(move.getMoveY(), 71);
////    }
////
////    @Test
////    void getBestMoveStraightOtherWay() {
////        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
////        for (int i = 0; i < WIDTH; i++) {
////            for (int j = 0; j < HEIGHT; j++) {
////                Terrain terrain1 = new Terrain(i, j, LAND);
////                terrain[i][j] = terrain1;
////            }
////        }
////
////        List<Resource> resources = new ArrayList<>();
////        resources.add(new Resource(70, 70, 5, STONE));
////        List<Building> buildings = new ArrayList<>();
////        Player player1 = new Player(70, 75, 10, RED, damageToPlayers);
////        List<Player> players = Arrays.asList(player1);
////        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources, buildings);
////        ActuallMove move = game.generateBestMove(player1);
////        assertEquals(move.getMoveType(), MOVE);
////        assertNull(move.getEntity());
////        assertEquals(move.getMoveX(), 70);
////        assertEquals(move.getMoveY(), 74);
////    }
////
////    @Test
////    void getBestMoveStraight2Players() {
////        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
////        for (int i = 0; i < WIDTH; i++) {
////            for (int j = 0; j < HEIGHT; j++) {
////                Terrain terrain1 = new Terrain(i, j, LAND);
////                terrain[i][j] = terrain1;
////            }
////        }
////
////        List<Resource> resources = new ArrayList<>();
////        List<Building> buildings = new ArrayList<>();
////        Player player1 = new Player(70, 70, 10, RED, damageToPlayers);
////        Player player2 = new Player(70, 75, 10, BLUE, damageToPlayers);
////        List<Player> players = Arrays.asList(player1, player2);
////        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources, buildings);
////        ActuallMove move = game.generateBestMove(player1);
////        assertEquals(move.getMoveType(), MOVE);
////        assertNull(move.getEntity());
////        assertEquals(move.getMoveX(), 70);
////        assertEquals(move.getMoveY(), 71);
////
////        move = game.generateBestMove(player2);
////        assertEquals(move.getMoveType(), MOVE);
////        assertNull(move.getEntity());
////        assertEquals(move.getMoveX(), 70);
////        assertEquals(move.getMoveY(), 74);
////    }
////
////    @Test
////    void getBestMoveAndThenMine() {
////        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
////        for (int i = 0; i < WIDTH; i++) {
////            for (int j = 0; j < HEIGHT; j++) {
////                Terrain terrain1 = new Terrain(i, j, LAND);
////                terrain[i][j] = terrain1;
////            }
////        }
////
////        List<Resource> resources = new ArrayList<>();
////        Resource resource = new Resource(72, 70, 5, STONE);
////        resources.add(resource);
////        List<Building> buildings = new ArrayList<>();
////        Player player1 = new Player(70, 70, 10, RED, damageToPlayers);
////        List<Player> players = Arrays.asList(player1);
////        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources, buildings);
////
////        ActuallMove move = game.generateBestMove(player1);
////        assertEquals(move.getMoveType(), MOVE);
////        assertNull(move.getEntity());
////        assertEquals(move.getMoveX(), 71);
////        assertEquals(move.getMoveY(), 70);
////
////        game.tick();
////        assertEquals(player1.getX(), 71);
////        assertEquals(player1.getY(), 70);
////
////        move = game.generateBestMove(player1);
////        assertEquals(move.getMoveType(), ATTACK);
////        assertEquals(move.getEntity(), resource);
////        assertEquals(move.getMoveX(), 71);
////        assertEquals(move.getMoveY(), 70);
////    }
////
////    @Test
////    void getBestMoveDifferentWeights() {
////        Config.enemyMoveWeight = 100;
////        Config.houseMoveWeight = 100;
////        Config.resourceMoveWeight = 1;
////
////        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
////        for (int i = 0; i < WIDTH; i++) {
////            for (int j = 0; j < HEIGHT; j++) {
////                Terrain terrain1 = new Terrain(i, j, LAND);
////                terrain[i][j] = terrain1;
////            }
////        }
////
////        List<Resource> resources = new ArrayList<>();
////        Resource resource = new Resource(72, 70, 5, STONE);
////        resources.add(resource);
////        List<Building> buildings = new ArrayList<>();
////        Player player1 = new Player(70, 70, 10, RED, damageToPlayers);
////        Player player2 = new Player(68, 70, 10, BLUE, damageToPlayers);
////        List<Player> players = Arrays.asList(player1, player2);
////        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources, buildings);
////
////        ActuallMove move = game.generateBestMove(player1);
////        assertEquals(move.getMoveType(), MOVE);
////        assertNull(move.getEntity());
////        assertEquals(move.getMoveX(), 71);
////        assertEquals(move.getMoveY(), 70);
////    }
////
////    @Test
////    void getBestMoveDifferentWeights2() throws Exception {
////        Config.enemyMoveWeight = 2;
////        Config.houseMoveWeight = 5;
////        Config.resourceMoveWeight = 5;
////
////        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
////        for (int i = 0; i < WIDTH; i++) {
////            for (int j = 0; j < HEIGHT; j++) {
////                Terrain terrain1 = new Terrain(i, j, LAND);
////                terrain[i][j] = terrain1;
////            }
////        }
////
////        List<Resource> resources = new ArrayList<>();
////        Resource resource = new Resource(72, 70, 5, STONE);
////        resources.add(resource);
////        List<Building> buildings = new ArrayList<>();
////        Player player1 = new Player(70, 70, 10, RED, damageToPlayers);
////        Player player2 = new Player(68, 70, 10, BLUE, damageToPlayers);
////        List<Player> players = Arrays.asList(player1, player2);
////        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources, buildings);
////
////        ActuallMove move = game.generateBestMove(player1);
////        assertEquals(move.getMoveType(), MOVE);
////        assertNull(move.getEntity());
////        assertEquals(move.getMoveX(), 69);
////        assertEquals(move.getMoveY(), 70);
////    }
////
////    @Test
////    void getBestMoveBuildHouse() {
////        Config.enemyMoveWeight = 2;
////        Config.houseMoveWeight = 5;
////        Config.resourceMoveWeight = 5;
////
////        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
////        for (int i = 0; i < WIDTH; i++) {
////            for (int j = 0; j < HEIGHT; j++) {
////                Terrain terrain1 = new Terrain(i, j, LAND);
////                terrain[i][j] = terrain1;
////            }
////        }
////
////        List<Resource> resources = new ArrayList<>();
////        Resource resource = new Resource(80, 70, 5, STONE);
////        resources.add(resource);
////        List<Building> buildings = new ArrayList<>();
////        Player player1 = new Player(70, 70, 10, RED, damageToPlayers);
////        List<Player> players = Arrays.asList(player1);
////        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources, buildings);
////        Score score = game.getScores()[game.teamToIndex(RED)];
////        score.setResources(Config.houseCost);
////
////        ActuallMove move = game.generateBestMove(player1);
////        assertEquals(move.getMoveType(), BUILD);
////        assertNotEquals(move.getEntity(), null);
////
////        game.tick();
////
////        score = game.getScores()[game.teamToIndex(RED)];
////        assertEquals(score.getHouses(), 1);
////        assertEquals(score.getResources(), 0);
////
////        move = game.generateBestMove(player1);
////        assertEquals(move.getMoveType(), MOVE);
////        assertNull(move.getEntity());
////
////        game.tick();
////        assertFalse(player1.getX() == 70 && player1.getY() == 70);
////    }
////
////    @Test
////    void getBestMoveBuildHouseOtherWay() {
////        Config.enemyMoveWeight = 2;
////        Config.houseMoveWeight = 5;
////        Config.resourceMoveWeight = 5;
////
////        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
////        for (int i = 0; i < WIDTH; i++) {
////            for (int j = 0; j < HEIGHT; j++) {
////                Terrain terrain1 = new Terrain(i, j, LAND);
////                terrain[i][j] = terrain1;
////            }
////        }
////
////        List<Resource> resources = new ArrayList<>();
////        Resource resource = new Resource(60, 70, 5, STONE);
////        resources.add(resource);
////        List<Building> buildings = new ArrayList<>();
////        Player player1 = new Player(70, 70, 10, RED, damageToPlayers);
////        List<Player> players = Arrays.asList(player1);
////        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources, buildings);
////        Score score = game.getScores()[game.teamToIndex(RED)];
////        score.setResources(Config.houseCost);
////
////        ActuallMove move = game.generateBestMove(player1);
////        assertEquals(move.getMoveType(), BUILD);
////        assertNotEquals(move.getEntity(), null);
////
////        game.tick();
////
////        score = game.getScores()[game.teamToIndex(RED)];
////        assertEquals(score.getHouses(), 1);
////        assertEquals(score.getResources(), 0);
////
////        move = game.generateBestMove(player1);
////        assertEquals(move.getMoveType(), MOVE);
////        assertNull(move.getEntity());
////
////        game.tick();
////        assertFalse(player1.getX() == 70 && player1.getY() == 70);
////    }
////
////    @Test
////    void getBestMoveChoosePathWithMoreResources() {
////        Config.enemyMoveWeight = 1;
////        Config.houseMoveWeight = 1;
////        Config.resourceMoveWeight = 1;
////
////        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
////        for (int i = 0; i < WIDTH; i++) {
////            for (int j = 0; j < HEIGHT; j++) {
////                Terrain terrain1 = new Terrain(i, j, LAND);
////                terrain[i][j] = terrain1;
////            }
////        }
////
////        List<Building> buildings = new ArrayList<>();
////
////        Resource resource1 = new Resource(70, 73, 5, STONE);
////        Resource resource2 = new Resource(70, 67, 5, STONE);
////        Resource resource3 = new Resource(71, 67, 5, STONE);
////        List<Resource> resources = Arrays.asList(resource1, resource2, resource3);
////
////        Player player1 = new Player(70, 70, 10, RED, damageToPlayers);
////        List<Player> players = Arrays.asList(player1);
////
////        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources, buildings);
////
////        ActuallMove move = game.generateBestMove(player1);
////        assertEquals(move.getMoveType(), MOVE);
////        assertNull(move.getEntity());
////        assertEquals(move.getMoveX(), 70);
////        assertEquals(move.getMoveY(), 69);
////    }
////
////    @Test
////    void getBestMoveMiddleOfResources() {
////        Config.enemyMoveWeight = 1;
////        Config.houseMoveWeight = 1;
////        Config.resourceMoveWeight = 1;
////
////        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
////        for (int i = 0; i < WIDTH; i++) {
////            for (int j = 0; j < HEIGHT; j++) {
////                Terrain terrain1 = new Terrain(i, j, LAND);
////                terrain[i][j] = terrain1;
////            }
////        }
////
////        List<Building> buildings = new ArrayList<>();
////
////        Resource resource1 = new Resource(70, 73, 5, STONE);
////        Resource resource2 = new Resource(70, 67, 5, STONE);
////        Resource resource3 = new Resource(73, 70, 5, STONE);
////        Resource resource4 = new Resource(67, 70, 5, STONE);
////        List<Resource> resources = Arrays.asList(resource1, resource2, resource3, resource4);
////
////        Player player1 = new Player(70, 71, 10, RED, damageToPlayers);
////        List<Player> players = Arrays.asList(player1);
////
////        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources, buildings);
////
////        ActuallMove move = game.generateBestMove(player1);
//////        System.out.println(move);
////        assertEquals(move.getMoveType(), MOVE);
////        assertNull(move.getEntity());
////        assertEquals(move.getMoveX(), 70);
////        assertEquals(move.getMoveY(), 72);
////    }
////
////    @Test
////    void getBestMoveMiddleOfResources2() {
////        Config.enemyMoveWeight = 1;
////        Config.houseMoveWeight = 1;
////        Config.resourceMoveWeight = 1;
////
////        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
////        for (int i = 0; i < WIDTH; i++) {
////            for (int j = 0; j < HEIGHT; j++) {
////                Terrain terrain1 = new Terrain(i, j, LAND);
////                terrain[i][j] = terrain1;
////            }
////        }
////
////        List<Building> buildings = new ArrayList<>();
////
////        Resource resource1 = new Resource(70, 72, 5, STONE);
////        Resource resource2 = new Resource(70, 68, 5, STONE);
////        Resource resource3 = new Resource(70, 67, 5, STONE);
////        List<Resource> resources = Arrays.asList(resource1, resource2, resource3);
////
////        Player player1 = new Player(70, 70, 10, RED, damageToPlayers);
////        List<Player> players = Arrays.asList(player1);
////
////        Game game = new Game(HEIGHT, WIDTH, terrain, players, resources, buildings);
////
////        ActuallMove move = game.generateBestMove(player1);
//////        System.out.println(move);
////        assertEquals(move.getMoveType(), MOVE);
////        assertNull(move.getEntity());
////        assertEquals(move.getMoveX(), 70);
////        assertEquals(move.getMoveY(), 69);
////    }
//
//    @Test
//    void putEntities() {
//        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                Terrain terrain1 = new Terrain(i, j, LAND);
//                terrain[i][j] = terrain1;
//            }
//        }
//
//        Team blue = new Team(BLUE, teamConfig);
//        Stone stone = new Stone(60, 70, 5);
//        Player player = blue.createPlayer(70,70, Farmer.class);
//        Building building = blue.createBuilding(71, 70, House.class, 100000);
//
//        List<Entity> entities = new ArrayList<>();
//        entities.add(stone);
//        entities.add(player);
//        entities.add(building);
//        Game game = new Game(HEIGHT, WIDTH, terrain, entities);
//        assertEquals(game.getBuildings().size(), 1);
//        assertEquals(game.getPlayers().size(), 1);
//        assertEquals(game.getResources().size(), 1);
//        assertEquals(game.getTerrain()[71][70].getBuilding(), building);
//        assertEquals(game.getTerrain()[71][70].getEntity(), null);
//        assertEquals(game.getTerrain()[60][70].getBuilding(), null);
//        assertEquals(game.getTerrain()[60][70].getEntity(), stone);
//        assertEquals(game.getTerrain()[70][70].getBuilding(), null);
//        assertEquals(game.getTerrain()[70][70].getEntity(), player);
//    }
//
//
//    @Test
//    void chooseCloserSameWeights() {
//        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                Terrain terrain1 = new Terrain(i, j, LAND);
//                terrain[i][j] = terrain1;
//            }
//        }
//
//        Team blue = new Team(BLUE, teamConfig);
//        Team red = new Team(RED, teamConfig);
//        Stone stone = new Stone(70, 67, 5);
//        Player player1 = blue.createPlayer(70,70, Farmer.class);
//        Player player2 = red.createPlayer(70,72, Farmer.class);
//
//        List<Entity> entities = new ArrayList<>();
//        entities.add(stone);
//        entities.add(player1);
//        entities.add(player2);
//        Game game = new Game(HEIGHT, WIDTH, terrain, entities);
//
//        List<Direction> legalMoves = game.getLegalMovesForPlayer(player1);
//        AllMoveStats enemyStats = game.getEnemyStats(player1);
//        AllMoveStats houseStats = game.getBuildingStats(player1);
//        AllMoveStats resourceStats = game.getResourceStats(player1);
//        Direction move = blue.getBestMove(player1, legalMoves, enemyStats, houseStats, resourceStats);
//        assertEquals(move.getMoveType(), MOVE);
//        assertNull(move.getEntity());
//        assertEquals(move.getX(), 70);
//        assertEquals(move.getY(), 71);
//    }
//
//    @Test
//    void chooseCloserSameWeightsFar() {
//        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                Terrain terrain1 = new Terrain(i, j, LAND);
//                terrain[i][j] = terrain1;
//            }
//        }
//
//        Team blue = new Team(BLUE, teamConfig);
//        Team red = new Team(RED, teamConfig);
//        Stone stone = new Stone(70, 10, 5);
//        Player player1 = blue.createPlayer(70,70, Farmer.class);
//        Player player2 = red.createPlayer(70,131, Farmer.class);
//
//        List<Entity> entities = new ArrayList<>();
//        entities.add(stone);
//        entities.add(player1);
//        entities.add(player2);
//        Game game = new Game(HEIGHT, WIDTH, terrain, entities);
//
//        List<Direction> legalMoves = game.getLegalMovesForPlayer(player1);
//        AllMoveStats enemyStats = game.getEnemyStats(player1);
//        AllMoveStats houseStats = game.getBuildingStats(player1);
//        AllMoveStats resourceStats = game.getResourceStats(player1);
//        Direction move = blue.getBestMove(player1, legalMoves, enemyStats, houseStats, resourceStats);
//        assertEquals(move.getMoveType(), MOVE);
//        assertNull(move.getEntity());
//        assertEquals(move.getX(), 70);
//        assertEquals(move.getY(), 69);
//    }
//
//    @Test
//    void bestPathBuildingInWay() {
//        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                Terrain terrain1 = new Terrain(i, j, LAND);
//                terrain[i][j] = terrain1;
//            }
//        }
//
//        Team blue = new Team(BLUE, teamConfig);
//        Stone stone = new Stone(60, 70, 5);
//        Stone stone2 = new Stone(72, 70, 5);
//        Player player = blue.createPlayer(70,70, Farmer.class);
//        Building building = blue.createBuilding(71, 70, House.class, 100000);
//
//        List<Entity> entities = new ArrayList<>();
//        entities.add(stone2);
//        entities.add(stone);
//        entities.add(player);
//        entities.add(building);
//        Game game = new Game(HEIGHT, WIDTH, terrain, entities);
//
//        List<Direction> legalMoves = game.getLegalMovesForPlayer(player);
//        AllMoveStats enemyStats = game.getEnemyStats(player);
//        AllMoveStats houseStats = game.getBuildingStats(player);
//        AllMoveStats resourceStats = game.getResourceStats(player);
//        Direction move = blue.getBestMove(player, legalMoves, enemyStats, houseStats, resourceStats);
//        assertEquals(move.getMoveType(), MOVE);
//        assertNull(move.getEntity());
//        assertEquals(move.getX(), 71);
//        assertEquals(move.getY(), 70);
//    }
//
//    @Test
//    void bestPathBlocked() {
//        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                Terrain terrain1 = new Terrain(i, j, LAND);
//                terrain[i][j] = terrain1;
//            }
//        }
//
//        Team blue = new Team(BLUE, teamConfig);
//        Team red = new Team(RED, teamConfig);
//        Player player0 = blue.createPlayer(68,70, Farmer.class);
//        Player player1 = blue.createPlayer(70,70, Farmer.class);
//        Player player2 = red.createPlayer(75,70, Farmer.class);
//
//        List<Entity> entities = new ArrayList<>();
//        entities.add(player0);
//        entities.add(player1);
//        entities.add(player2);
//        Game game = new Game(HEIGHT, WIDTH, terrain, entities);
//        game.tick();
//        assertEquals(player0.getX(), 69);
//        assertEquals(player0.getY(), 70);
//        assertEquals(player1.getX(), 71);
//        assertEquals(player1.getY(), 70);
//        assertEquals(player2.getX(), 74);
//        assertEquals(player2.getY(), 70);
//
//        game.tick();
//        assertEquals(player0.getX(), 70);
//        assertEquals(player0.getY(), 70);
//        assertEquals(player1.getX(), 72);
//        assertEquals(player1.getY(), 70);
//        assertEquals(player2.getX(), 73);
//        assertEquals(player2.getY(), 70);
//
//        game.tick();
//        assertEquals(player0.getX(), 71);
//        assertEquals(player0.getY(), 70);
//        assertEquals(player1.getX(), 72);
//        assertEquals(player1.getY(), 70);
//        assertEquals(player2.getX(), 73);
//        assertEquals(player2.getY(), 70);
//        assertEquals(player0.getHealth(), farmerHealth);
//        assertEquals(player2.getHealth(), farmerHealth - farmerPlayerDamage);
//        assertEquals(player1.getHealth(), farmerHealth - farmerPlayerDamage);
//
//        game.tick();
//        assertEquals(player0.getX() != 71 || player0.getY() != 70, true);
//        assertEquals(player1.getX(), 72);
//        assertEquals(player1.getY(), 70);
//        assertEquals(player2.getX(), 73);
//        assertEquals(player2.getY(), 70);
//        assertEquals(player0.getHealth(), farmerHealth);
//        assertEquals(player2.getHealth(), farmerHealth - 2 * farmerPlayerDamage);
//        assertEquals(player1.getHealth(), farmerHealth - 2 * farmerPlayerDamage);
//    }
//
//    @Test
//    void bestPathFightInBuilding() {
//        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                Terrain terrain1 = new Terrain(i, j, LAND);
//                terrain[i][j] = terrain1;
//            }
//        }
//
//        Team blue = new Team(BLUE, teamConfig);
//        Team red = new Team(RED, teamConfig);
//        Player player = blue.createPlayer(69,70, Farmer.class);
//        Player player2 = red.createPlayer(72,70, Farmer.class);
//        Building building = blue.createBuilding(70, 70, House.class, 100000);
//
//        List<Entity> entities = new ArrayList<>();
//        entities.add(player);
//        entities.add(building);
//        entities.add(player2);
//        Game game = new Game(HEIGHT, WIDTH, terrain, entities);
//        game.tick();
//        assertEquals(player.getX(), 70);
//        assertEquals(player.getY(), 70);
//        assertEquals(game.getTerrain()[70][70].getEntity(), player);
//        assertEquals(game.getTerrain()[70][70].getBuilding(), building);
//        assertEquals(player2.getX(), 71);
//        assertEquals(player2.getY(), 70);
//        game.tick();
//        assertEquals(player.getX(), 70);
//        assertEquals(player.getY(), 70);
//        assertEquals(player2.getX(), 71);
//        assertEquals(player2.getY(), 70);
//        assertEquals(player.getHealth(), farmerHealth);
//        assertEquals(player2.getHealth(), farmerHealth - farmerPlayerDamage);
//        assertEquals(building.getHealth(), houseHealth - farmerPlayerDamage);
//    }
//
//    @Test
//    void bestPathBlocked3() {
//        Terrain[][] terrain = new Terrain[150][150];
//        for (int i = 0; i < 150; i++) {
//            for (int j = 0; j < 150; j++) {
//                Terrain terrain1 = new Terrain(i, j, WATER);
//                terrain[i][j] = terrain1;
//            }
//        }
//        for (int i = 60; i < 70; i++) {
//            for (int j = 60; j < 150; j++) {
//                Terrain terrain1 = new Terrain(i, j, LAND);
//                terrain[i][j] = terrain1;
//            }
//        }
//        for (int i = 70; i < 80; i++) {
//            for (int j = 60; j < 63; j++) {
//                Terrain terrain1 = new Terrain(i, j, LAND);
//                terrain[i][j] = terrain1;
//            }
//        }
//
//        Team blue = new Team(BLUE, teamConfig);
//        Player player0 = blue.createPlayer(75,60, Farmer.class);
//        Player player1 = blue.createPlayer(75,61, Farmer.class);
//        Player player2 = blue.createPlayer(75,62, Farmer.class);
//        Gold gold = new Gold(69, 80, 1000);
//
//        List<Entity> entities = new ArrayList<>();
//        entities.add(player0);
//        entities.add(player1);
//        entities.add(player2);
//        entities.add(gold);
//        Game game = new Game(150, 150, terrain, entities);
//        assertEquals(game.getResources().size(), 1);
//        game.tick();
//        assertEquals(player0.getX(), 74);
//        assertEquals(player0.getY(), 60);
//        assertEquals(player1.getX(), 74);
//        assertEquals(player1.getY(), 61);
//        assertEquals(player2.getX(), 74);
//        assertEquals(player2.getY(), 62);
//        game.tick();
//        game.tick();
//        game.tick();
//        assertEquals(player0.getX(), 71);
//        assertEquals(player0.getY(), 60);
//        assertEquals(player1.getX(), 71);
//        assertEquals(player1.getY(), 61);
//        assertEquals(player2.getX(), 71);
//        assertEquals(player2.getY(), 62);
//        game.tick();
//        assertEquals(player0.getX(), 70);
//        assertEquals(player0.getY(), 60);
//        assertEquals(player1.getX(), 70);
//        assertEquals(player1.getY(), 61);
//        assertEquals(player2.getX(), 70);
//        assertEquals(player2.getY(), 62);
//        game.tick();
//        assertEquals(player0.getX(), 69);
//        assertEquals(player0.getY(), 60);
//        assertEquals(player1.getX(), 69);
//        assertEquals(player1.getY(), 61);
//        assertEquals(player2.getX(), 69);
//        assertEquals(player2.getY(), 62);
//        System.out.println("------");
//        game.tick();
//        assertEquals(player0.getX(), 68);
//        assertEquals(player0.getY(), 60);
//        assertEquals(player1.getX(), 68);
//        assertEquals(player1.getY(), 61);
//        assertEquals(player2.getX(), 69);
//        assertEquals(player2.getY(), 63);
////        System.out.println("------");
////        game.tick();
////        assertEquals(player0.getX(), 67);
////        assertEquals(player0.getY(), 60);
////        assertEquals(player1.getX(), 68);
////        assertEquals(player1.getY(), 62);
////        assertEquals(player2.getX(), 69);
////        assertEquals(player2.getY(), 64);
////        System.out.println("------");
////        game.tick();
////        assertEquals(player0.getX(), 67);
////        assertEquals(player0.getY(), 61);
////        assertEquals(player1.getX() == 68 && player1.getY() == 63 || player1.getX() == 67 && player1.getY() == 62, true);
////        assertEquals(player2.getX(), 69);
////        assertEquals(player2.getY(), 65);
//    }
//
//    @Test
//    void bestPathBlocked2() {
//        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                Terrain terrain1 = new Terrain(i, j, LAND);
//                terrain[i][j] = terrain1;
//            }
//        }
//
//        Team blue = new Team(BLUE, teamConfig);
//        Player player0 = blue.createPlayer(68,70, Farmer.class);
//        Player player1 = blue.createPlayer(70,70, Farmer.class);
//        Gold gold = new Gold(72, 70, 1000);
//        Gold gold2 = new Gold(60, 70, 1000);
//
//        List<Entity> entities = new ArrayList<>();
//        entities.add(player0);
//        entities.add(player1);
//        entities.add(gold);
//        entities.add(gold2);
//        Game game = new Game(HEIGHT, WIDTH, terrain, entities);
//        game.tick();
//        assertEquals(player0.getX(), 69);
//        assertEquals(player0.getY(), 70);
//        assertEquals(player1.getX(), 71);
//        assertEquals(player1.getY(), 70);
//
//        game.tick();
//        assertEquals(player0.getX(), 70);
//        assertEquals(player0.getY(), 70);
//        assertEquals(player1.getX(), 71);
//        assertEquals(player1.getY(), 70);
//        assertEquals(gold.getHealth(), 1000 - farmerPlayerDamage);
//
//        game.tick();
//        System.out.println(player0);
//        assertEquals(player0.getX() != 70 || player0.getY() != 70, true);
//        assertEquals(player0.getX() == 69, false);
//        assertEquals(player1.getX(), 71);
//        assertEquals(player1.getY(), 70);
//
//        game.tick();
//        assertEquals(player0.getX() != 70 || player0.getY() != 70, true);
//    }
//
//    @Test
//    void houseDestroyedPlayerPopOut() {
//        // todo
//    }
}