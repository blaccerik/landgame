package com.example.landgame;

import com.example.landgame.better.entities.Object;
import com.example.landgame.better.entities.Rock;
import com.example.landgame.better.entities.Worker;
import com.example.landgame.enums.MoveType;
import com.example.landgame.gamelogic.ActuallMove;
import com.example.landgame.gamelogic.Coords;
import com.example.landgame.map.Map;
import com.example.landgame.map.Terrain;
import com.example.landgame.objects.Entity;
import com.example.landgame.objects.Building;
import com.example.landgame.objects.Gold;
import com.example.landgame.objects.House;
import com.example.landgame.objects.Player;
import com.example.landgame.objects.Resource;
import com.example.landgame.objects.Stone;
import com.example.landgame.objects.Tree;
import com.example.landgame.pathfinding.Direction;
import com.example.landgame.pathfinding.PathMatrix;
import com.example.landgame.pathfinding.Vector;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static com.example.landgame.enums.TerrainType.*;
import static com.example.landgame.enums.MoveType.*;
import static com.example.landgame.config.Config.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
class MoveStats {
//    private int score;
    private int lowest;
    private int count;
    private int total;

    public void addSum(int sum) {
        if (sum < lowest) {
            lowest = sum;
        }
        this.count++;
        this.total += sum;
//        this.score += (float) 1 / (sum * sum);
    }

    public float getScore() {
        int a = this.lowest * this.total;
        if (a == 0) {
            return 0;
        }
//        System.out.println(this);
        return (float) 1 / a;
    }
}

@Getter
@ToString
class AllMoveStats {
    private final MoveStats moveStats1;
    private final MoveStats moveStats2;
    private final MoveStats moveStats3;
    private final MoveStats moveStats4;

    public AllMoveStats() {
        this.moveStats1 = new MoveStats(1000, 0, 0);
        this.moveStats2 = new MoveStats(1000, 0, 0);
        this.moveStats3 = new MoveStats(1000, 0, 0);
        this.moveStats4 = new MoveStats(1000, 0, 0);
    }

    public void addStat(int t, int distance) {
        get(t).addSum(distance);
    }

    public MoveStats get(int i) {
        switch (i) {
            case 1:
                return this.moveStats1;
            case 2:
                return this.moveStats2;
            case 3:
                return this.moveStats3;
            case 4:
                return this.moveStats4;
        }
        return null;
    }
}

@Getter
public class Game {

    public static final boolean globalCheck = true;

    // for testing only

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

    public static final boolean debug = false;

    public static final Direction[] directions = new Direction[]{

            // store reverse moveNumber as we move away from end point
            new Direction(0,1, 1, null, null, 0),
            new Direction(1,0, 2, null, null, 0),
            new Direction(0,-1, 3, null, null, 0),
            new Direction(-1,0, 4, null, null, 0)

    };

    private final List<Player> players;
    private final List<Resource> resources;
    private final List<Building> buildings;
    private final Team[] teams;
    private final Queue<Entity> removeEntities = new ArrayDeque<>();
//    private final PathMatrix pathMatrix;
    private static long tick = 0;
    private static long lastStoneSpawn = 0;
    private static long lastTreeSpawn = 0;
    private static long lastGoldSpawn = 0;
    private static long lastWeightChange = 0;

    public final static float goodScore = 1000000;



    private final Map map;

    /**
     * Game deals with
     */
    public Game(Map map, List<Entity> entities) {
        this.map = map;

        this.players = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.buildings = new ArrayList<>();
        List<Team> teams = putEntities(entities);
        this.teams = new Team[teams.size()];
        teams.toArray(this.teams);
    }

    private List<Team> putEntities(List<Entity> list) {
        List<Team> teams = new ArrayList<>();
        for (Entity t : list) {
            int x = t.getX();
            int y = t.getY();
            if (t instanceof Player) {
                if (this.map.isBlocked(x,y)) {
                    Team team = ((Player) t).getTeam();
                    if (!teams.contains(team)) {
                        teams.add(team);
                    }
                    this.players.add((Player) t);
                    this.map.getTile(x,y).setEntity(t);
                }
            } else if (t instanceof Building) {
                if (this.map.isBuildable(x,y)) {

                    Team team = ((Building) t).getTeam();
                    if (!teams.contains(team)) {
                        teams.add(team);
                    }
                    Building building = (Building) t;
                    this.buildings.add(building);
                    this.map.getTile(x,y).setBuilding(building);
                }
            } else if (t instanceof Resource) {
                if (this.map.isBlocked(x,y) && this.map.getTile(x,y).getTerrainType().equals(LAND)) {
                    this.resources.add((Resource) t);
                    this.map.getTile(x,y).setEntity(t);
                }
            } else {
                throw new RuntimeException("typrrrr");
            }
        }
        cleanup();
        return teams;
    }

    private void makeMove(Player player, Direction direction) {

        // move player
        if (direction.getX() != player.getX() || direction.getY() != player.getY()) {
            this.map.getTile(player.getX(), player.getY()).setEntity(null);
            this.map.getTile(direction.getX(), direction.getY()).setEntity(player);

//            Building building2 = this.map.getTile(direction.getX(), direction.getY()).getBuilding();
//            if (building2 != null && !building2.sameTeam(player)) {
//                throw new RuntimeException("ERRORORORO");
//            }

            player.setX(direction.getX());
            player.setY(direction.getY());
            player.addMove(direction.getMoveNumber());
        }
        Entity entity = direction.getEntity();
        if (direction.getMoveType().equals(MoveType.ATTACK)) {
            // do damage
            boolean dead = entity.takeDamage(player);
            if (dead) {
                this.removeEntities.add(entity);
            }
        } else if (direction.getMoveType().equals(BUILD)) {
            Building building = player.getTeam().createBuilding(player.getX(), player.getY(), House.class, tick);
            this.buildings.add(building);

            if (globalCheck) {
                if (this.map.getTile(player.getX(), player.getY()).getBuilding() != null) {
                    throw new RuntimeException("eeweweqw");
                }
            }

            this.map.getTile(player.getX(), player.getY()).setBuilding(building);
        }
    }

    private void cleanup() {
        while (!this.removeEntities.isEmpty()) {
            Entity entity = this.removeEntities.poll();
            int x = entity.getX();
            int y = entity.getY();
            boolean a;
            if (entity instanceof Player) {
                a = this.players.remove(entity);
                this.map.getTile(x, y).setEntity(null);
            } else if (entity instanceof Resource) {
                a = this.resources.remove(entity);
                this.map.getTile(x, y).setEntity(null);
            } else if (entity instanceof Building) {
                a = this.buildings.remove(entity);
                this.map.getTile(x, y).setBuilding(null);
            } else {
                throw new RuntimeException("clanene");
            }
            if (globalCheck) {
                if (!a) {
                    throw new RuntimeException("ewew");
                }
            }
        }
    }

    private void spawnPlayer(Building building) {
        if (tick - building.getLastSpawn() > houseSpawnRate) {
            int x = building.getX();
            int y = building.getY();
            Entity entity = this.map.getTile(x, y).getEntity();
            if (entity == null) {
                Player player = building.getTeam().createPlayer(x, y, building, tick);
                this.players.add(player);
                this.map.getTile(x, y).setEntity(player);
            }
        }
    }

    private void spawnResources() {
        if (tick - lastStoneSpawn > stoneSpawnRate) {
            Random random = new Random();
            int x = random.nextInt(this.map.getWidth());
            int y = random.nextInt(this.map.getHeight());
            if (this.map.canResourceSpawn(x,y)) {
                lastStoneSpawn = tick;
                Stone resource = new Stone(x,y,stoneHealth);
                this.map.getTile(x,y).setEntity(resource);
                this.resources.add(resource);
            }
        }
        if (tick - lastTreeSpawn > treeSpawnRate) {
            Random random = new Random();
            int x = random.nextInt(this.map.getWidth());
            int y = random.nextInt(this.map.getHeight());
            if (this.map.canResourceSpawn(x,y)) {
                lastTreeSpawn = tick;
                Tree resource = new Tree(x,y,treeHealth);
                this.map.getTile(x,y).setEntity(resource);
                this.resources.add(resource);
            }
        }

        if (tick - lastGoldSpawn > goldSpawnRate) {
            Random random = new Random();
            int x = random.nextInt(this.map.getWidth());
            int y = random.nextInt(this.map.getHeight());
            if (this.map.canResourceSpawn(x,y)) {
                lastGoldSpawn = tick;
                Gold resource = new Gold(x,y,goldHealth);
                this.map.getTile(x,y).setEntity(resource);
                this.resources.add(resource);
            }
        }
    }


    private void changeWeights() {
        if (tick - lastWeightChange > weightChange) {
            lastWeightChange = tick;
//            System.out.println("change");
//            for (Team team: this.teams) {
//
//                team.changeWeights();
//            }
        }
    }

    /**
     * Can only have max 2 Illegal moves
     */
    public List<Direction> getLegalMovesForPlayer(Player player) {
        List<Direction> list = new ArrayList<>();
        int x = player.getX();
        int y = player.getY();
//        int notBlocked = 0;
//        Direction best = null;
        int badMove = Team.oppositeMove(player.getLastMove());
        Direction badDirection = null;
//        int badMove = 100;
        for (Direction direction: Game.directions) {
            int finalX = x + direction.getX();
            int finalY = y + direction.getY();
            int moveNumber = direction.getMoveNumber();
            if (this.map.isMovable(finalX, finalY)) {
                Terrain terrain = this.map.getTile(finalX, finalY);
                Building building = terrain.getBuilding();

                // enemy building
                if (building != null && !building.sameTeam(player)) {
                    return Collections.singletonList(new Direction(x, y, 0, building, ATTACK, 0));
                }

                Entity entity = terrain.getEntity();
                if (entity == null) {
                    Direction legal = new Direction(finalX, finalY, moveNumber, null, MOVE, 0);
                    if (moveNumber == badMove) {
                        badDirection = legal;
                    } else {
//                        notBlocked++;
                        list.add(legal);
                    }

                } else if (!entity.sameTeam(player)) {  // if next object is foreign then attack it
                    return Collections.singletonList(new Direction(x, y, 0, entity, ATTACK, 0));
                }
//                else {
//                    list.add(new Direction(x,y,direction.getMoveNumber(), null, ILLEGAL, 0));
//                }
            }
        }
        if (list.size() == 0 && badDirection != null) {
            list.add(badDirection);
        }
        return list;
    }

    private <T extends Entity> AllMoveStats getAllMoveStats(Player player, List<T> list) {
        AllMoveStats allMoveStats = new AllMoveStats();
        for (Entity entity: list) {
            if (!entity.sameTeam(player)) {
                int number = this.map.findPath(
                        player.getX(),
                        player.getY(),
                        entity.getX(),
                        entity.getY());
                if (number != -1) {
                    Vector vector = PathMatrix.decode(number);
                    int distance = vector.getDistance();
                    int move1 = vector.getMove1();
                    int move2 = vector.getMove2();
                    for (int i = 1; i <= 4; i++) {
                        if (i == move1 || i == move2) {
                            allMoveStats.addStat(i, distance - 1);
                        } else {
                            allMoveStats.addStat(i, distance + 1);
                        }

                    }
                }
            }
        }
        return allMoveStats;
    }

    private boolean canBuild(int x, int y) {
        return this.map.getTile(x,y).getBuilding() == null;
    }

    public void tick() {

        /* todo fix when multiple players go straight
         *  A1 A2 E1
         *
         * A1 should make another random move
         */

        tick++;
        for (Player player: this.players) {
            List<Direction> legalMoves = getLegalMovesForPlayer(player);
            // if only 1 move then make it
            if (legalMoves.size() == 1) {
                Direction direction = legalMoves.get(0);
                makeMove(player, direction);
            } else if (legalMoves.size() > 1) {

                AllMoveStats enemyStats = getAllMoveStats(player, this.players);
                AllMoveStats houseStats = getAllMoveStats(player, this.buildings);
                AllMoveStats resourceStats = getAllMoveStats(player, this.resources);
                boolean canBuild = canBuild(player.getX(), player.getY());

                Direction bestMove = player.getTeam().getBestMove(
                        player,
                        canBuild,
                        legalMoves,
                        enemyStats,
                        houseStats,
                        resourceStats
                );

                makeMove(player, bestMove);
            }
        }
        cleanup();

        for (Building building : this.buildings) {
            spawnPlayer(building);
        }

        spawnResources();
        changeWeights();
    }
}
