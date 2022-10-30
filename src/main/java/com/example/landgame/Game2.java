package com.example.landgame;

import com.example.landgame.enums.MoveType;
import com.example.landgame.gamelogic.Coords;
import com.example.landgame.map.Terrain;
import com.example.landgame.objects.Building;
import com.example.landgame.objects.Entity;
import com.example.landgame.objects.Gold;
import com.example.landgame.objects.House;
import com.example.landgame.objects.Player;
import com.example.landgame.objects.Resource;
import com.example.landgame.objects.Stone;
import com.example.landgame.objects.Tree;
import com.example.landgame.pathfinding.Direction;
import com.example.landgame.pathfinding.PathMatrix;
import com.example.landgame.pathfinding.Vector;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static com.example.landgame.config.Config.goldHealth;
import static com.example.landgame.config.Config.goldSpawnRate;
import static com.example.landgame.config.Config.houseSpawnRate;
import static com.example.landgame.config.Config.stoneHealth;
import static com.example.landgame.config.Config.stoneSpawnRate;
import static com.example.landgame.config.Config.treeHealth;
import static com.example.landgame.config.Config.treeSpawnRate;
import static com.example.landgame.config.Config.weightChange;
import static com.example.landgame.enums.MoveType.ATTACK;
import static com.example.landgame.enums.MoveType.BUILD;
import static com.example.landgame.enums.MoveType.ILLEGAL;
import static com.example.landgame.enums.MoveType.MOVE;
import static com.example.landgame.enums.TerrainType.LAND;

//@Getter
//public class Game2 {
//
//    // for testing only
//
//    public AllMoveStats getEnemyStats(Player player) {
//        return getAllMoveStats(player, this.players);
//    }
//
//    public AllMoveStats getBuildingStats(Player player) {
//        return getAllMoveStats(player, this.buildings);
//    }
//
//    public AllMoveStats getResourceStats(Player player) {
//        return getAllMoveStats(player, this.resources);
//    }
//
//    public static final boolean debug = false;
//
//    public static final Direction[] directions = new Direction[]{
//
//        // store reverse moveNumber as we move away from end point
//        new Direction(0,1, 1, null, null, 0),
//        new Direction(1,0, 2, null, null, 0),
//        new Direction(0,-1, 3, null, null, 0),
//        new Direction(-1,0, 4, null, null, 0)
//
//    };
//
//    private final int height;
//    private final int width;
//    private final Terrain[][] terrain;
//    private final List<Player> players;
//    private final List<Resource> resources;
//    private final List<Building> buildings;
//    private final Team[] teams;
//    private final Queue<Entity> removeEntities = new LinkedList<>();
//    private final PathMatrix pathMatrix;
//    private static long tick = 0;
//    private static long lastStoneSpawn = 0;
//    private static long lastTreeSpawn = 0;
//    private static long lastGoldSpawn = 0;
//    private static long lastWeightChange = 0;
//
//    public final static float goodScore = 1000000;
//
//
//
//
//    /**
//     * Game deals with
//     */
//    public Game2(int height, int width, Terrain[][] terrain, List<Entity> entities) {
//        this.height = height;
//        this.width = width;
//        this.terrain = terrain;
//        this.pathMatrix = new PathMatrix(this);
//
//        this.players = new ArrayList<>();
//        this.resources = new ArrayList<>();
//        this.buildings = new ArrayList<>();
//        List<Team> teams = putEntities(entities);
//        this.teams = new Team[teams.size()];
//        teams.toArray(this.teams);
//    }
//
//    private List<Team> putEntities(List<Entity> list) {
//        List<Team> teams = new ArrayList<>();
//        for (Entity t : list) {
//            int x = t.getX();
//            int y = t.getY();
//            if (t instanceof Player) {
//                if (isMovable(x,y)) {
//                    Team team = ((Player) t).getTeam();
//                    if (!teams.contains(team)) {
//                        teams.add(team);
//                    }
//                    this.players.add((Player) t);
//                    this.terrain[x][y].setEntity(t);
//                } else {
//                    this.removeEntities.add(t);
//                }
//            } else if (t instanceof Building) {
//                if (isBuildable(x,y)) {
//
//                    Team team = ((Building) t).getTeam();
//                    if (!teams.contains(team)) {
//                        teams.add(team);
//                    }
//                    Building building = (Building) t;
//                    this.buildings.add(building);
//                    this.terrain[x][y].setBuilding(building);
//                } else {
//                    this.removeEntities.add(t);
//                }
//            } else if (t instanceof Resource) {
//                if (isMovable(x,y) && this.terrain[x][y].getTerrainType().equals(LAND)) {
//                    this.resources.add((Resource) t);
//                    this.terrain[x][y].setEntity(t);
//                } else {
//                    this.removeEntities.add(t);
//                }
//            } else {
//                throw new RuntimeException("typrrrr");
//            }
//        }
//        cleanup();
//        return teams;
//    }
//
//    private boolean isInbounds(int x, int y) {
//        return x >= 0 && y >= 0 && x < this.width && y < this.height;
//    }
//
//    public boolean isMovable(int x, int y) {
//
//        if (isInbounds(x,y)) {
//            Terrain terrain = this.terrain[x][y];
//            switch (terrain.getTerrainType()) {
//                case WATER:
//                case DEEP_WATER:
//                    return false;
//                case SAND:
//                case LAND:
//                    return terrain.getEntity() == null;
//            }
//        }
//        return false;
//    }
//
//    public boolean isBuildable(int x, int y) {
//        if (isInbounds(x,y)) {
//            Terrain terrain = this.terrain[x][y];
//            switch (terrain.getTerrainType()) {
//                case WATER:
//                case DEEP_WATER:
//                    return false;
//                case SAND:
//                case LAND:
//                    return terrain.getBuilding() == null;
//            }
//        }
//        return false;
//    }
//
//    public boolean canResourceSpawn(int x, int y) {
//        if (isInbounds(x,y)) {
//            Terrain terrain = this.terrain[x][y];
//            switch (terrain.getTerrainType()) {
//                case WATER:
//                case DEEP_WATER:
//                case SAND:
//                    return false;
//                case LAND:
//                    return terrain.getBuilding() == null && terrain.getEntity() == null;
//            }
//        }
//        return false;
//    }
//
//    public boolean isMovable(Coords coords) {
//        return isMovable(coords.getX(), coords.getY());
//    }
//
//    public boolean isSandOrLand(int x, int y) {
//        // inbounds
//        if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
//            Terrain terrain = this.terrain[x][y];
//
//            switch (terrain.getTerrainType()) {
//                case WATER:
//                    case DEEP_WATER:
//                        return false;
//                case SAND:
//                    case LAND:
//                        return true;
//            }
//        }
//        return false;
//    }
//
//    private void makeMove(Player player, Direction direction) {
//
//        // move player
//        if (direction.getX() != player.getX() || direction.getY() != player.getY()) {
//            this.terrain[player.getX()][player.getY()].setEntity(null);
//            this.terrain[direction.getX()][direction.getY()].setEntity(player);
//
//            Building building2 = this.terrain[direction.getX()][direction.getY()].getBuilding();
//            if (building2 != null && !building2.sameTeam(player)) {
//                throw new RuntimeException("ERRORORORO");
//            }
//
//            player.setX(direction.getX());
//            player.setY(direction.getY());
//            player.addMove(direction.getMoveNumber());
//        }
//        Entity entity = direction.getEntity();
//        if (direction.getMoveType().equals(MoveType.ATTACK)) {
//            // do damage
//            boolean dead = entity.takeDamage(player);
//            if (dead) {
//                this.removeEntities.add(entity);
//            }
//        } else if (direction.getMoveType().equals(BUILD)) {
//            Building building = player.getTeam().createBuilding(player.getX(), player.getY(), House.class, tick);
//            this.buildings.add(building);
//            this.terrain[player.getX()][player.getY()].setBuilding(building);
//        }
//    }
//
//    private void cleanup() {
//        while (!this.removeEntities.isEmpty()) {
//            Entity entity = this.removeEntities.poll();
//            int x = entity.getX();
//            int y = entity.getY();
//            if (entity instanceof Player) {
//                this.players.remove(entity);
//                this.terrain[x][y].setEntity(null);
//            } else if (entity instanceof Resource) {
//                this.resources.remove(entity);
//                this.terrain[x][y].setEntity(null);
//            } else if (entity instanceof Building) {
//                this.buildings.remove(entity);
//                this.terrain[x][y].setBuilding(null);
//            } else {
//                throw new RuntimeException("clanene");
//            }
//        }
//    }
//
//    private void spawnPlayer(Building building) {
//        if (tick - building.getLastSpawn() > houseSpawnRate) {
//
//            int x = building.getX();
//            int y = building.getY();
//            Entity entity = this.terrain[x][y].getEntity();
//            if (entity == null) {
//                Player player = building.getTeam().createPlayer(x, y, building, tick);
//                this.players.add(player);
//                this.terrain[x][y].setEntity(player);
//            }
//        }
//    }
//
//    private void spawnResources() {
//        if (tick - lastStoneSpawn > stoneSpawnRate) {
//            Random random = new Random();
//            int x = random.nextInt(this.width);
//            int y = random.nextInt(this.height);
//            if (canResourceSpawn(x,y)) {
//                lastStoneSpawn = tick;
//                Stone resource = new Stone(x,y,stoneHealth);
//                this.terrain[x][y].setEntity(resource);
//                this.resources.add(resource);
//            }
//        }
//        if (tick - lastTreeSpawn > treeSpawnRate) {
//            Random random = new Random();
//            int x = random.nextInt(this.width);
//            int y = random.nextInt(this.height);
//            if (canResourceSpawn(x,y)) {
//                lastTreeSpawn = tick;
//                Tree resource = new Tree(x,y,treeHealth);
//                this.terrain[x][y].setEntity(resource);
//                this.resources.add(resource);
//            }
//        }
//
//        if (tick - lastGoldSpawn > goldSpawnRate) {
//            Random random = new Random();
//            int x = random.nextInt(this.width);
//            int y = random.nextInt(this.height);
//            if (canResourceSpawn(x,y)) {
//                lastGoldSpawn = tick;
//                Gold resource = new Gold(x,y,goldHealth);
//                this.terrain[x][y].setEntity(resource);
//                this.resources.add(resource);
//            }
//        }
//    }
//
//
//    private void changeWeights() {
//        if (tick - lastWeightChange > weightChange) {
//            lastWeightChange = tick;
////            System.out.println("change");
////            for (Team team: this.teams) {
////
////                team.changeWeights();
////            }
//        }
//    }
//
//    /**
//     * Can only have max 2 Illegal moves
//     */
//    public List<Direction> getLegalMovesForPlayer(Player player) {
//        List<Direction> list = new ArrayList<>();
//        int x = player.getX();
//        int y = player.getY();
//        int notBlocked = 0;
//        Direction best = null;
//        int badMove = Team.oppositeMove(player.getLastMove());
//        for (Direction direction: Game2.directions) {
//            int finalX = x + direction.getX();
//            int finalY = y + direction.getY();
//            if (isSandOrLand(finalX, finalY) && direction.getMoveNumber() != badMove) {
//                Building building = this.terrain[finalX][finalY].getBuilding();
//
//                // enemy building
//                if (building != null && !building.sameTeam(player)) {
//                    return Collections.singletonList(new Direction(x, y, direction.getMoveNumber(), building, ATTACK, 0));
//                }
//
//                Entity entity = this.terrain[finalX][finalY].getEntity();
//                if (entity == null) {
//                    notBlocked++;
//                    Direction legal = new Direction(finalX, finalY, direction.getMoveNumber(), null, MOVE, 0);
//                    best = legal;
//                    list.add(legal);
//                } else if (!entity.sameTeam(player)) {  // if next object is foreign then attack it
//                    return Collections.singletonList(new Direction(x, y, direction.getMoveNumber(), entity, ATTACK, 0));
//                } else {
//                    list.add(new Direction(x,y,direction.getMoveNumber(), null, ILLEGAL, 0));
//                }
//            }
//        }
//        if (notBlocked == 0) {
//            return Collections.emptyList();
//        } else if (notBlocked == 1) {
//            return Collections.singletonList(best);
//        }
//        return list;
//    }
//
//    private <T extends Entity> AllMoveStats getAllMoveStats(Player player, List<T> list) {
//        AllMoveStats allMoveStats = new AllMoveStats();
//        for (Entity entity: list) {
//            if (!entity.sameTeam(player)) {
//                int number = this.pathMatrix.findMoveNumber(
//                        player.getX(),
//                        player.getY(),
//                        entity.getX(),
//                        entity.getY());
//                if (number != -1) {
//                    Vector vector = PathMatrix.decode(number);
//                    int distance = vector.getDistance();
//                    int move1 = vector.getMove1();
//                    int move2 = vector.getMove2();
//                    for (int i = 1; i <= 4; i++) {
//                        if (i == move1 || i == move2) {
//                            allMoveStats.addStat(i, distance - 1);
//                        } else {
//                            allMoveStats.addStat(i, distance + 1);
//                        }
//
//                    }
//                }
//            }
//        }
//        return allMoveStats;
//    }
//
//    public void tick() {
//
//        /* todo fix when multiple players go straight
//         *  A1 A2 E1
//         *
//         * A1 should make another random move
//         */
//
//        tick++;
//
//        for (Player player: this.players) {
//            List<Direction> legalMoves = getLegalMovesForPlayer(player);
//            // if only 1 move then make it
//            if (legalMoves.size() == 1) {
//                Direction direction = legalMoves.get(0);
//                makeMove(player, direction);
//            } else if (legalMoves.size() > 1) {
//
//                AllMoveStats enemyStats = getAllMoveStats(player, this.players);
//                AllMoveStats houseStats = getAllMoveStats(player, this.buildings);
//                AllMoveStats resourceStats = getAllMoveStats(player, this.resources);
//
//                Direction bestMove = player.getTeam().getBestMove(
//                        player,
//                        legalMoves,
//                        enemyStats,
//                        houseStats,
//                        resourceStats
//                );
//
//                makeMove(player, bestMove);
//            }
//        }
//        cleanup();
//
//        for (Building building : this.buildings) {
//            spawnPlayer(building);
//        }
//
//        spawnResources();
//        changeWeights();
//    }
//}
