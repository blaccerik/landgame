package com.example.landgame;

import com.example.landgame.enums.MoveType;
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
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Value;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static com.example.landgame.enums.TerrainType.*;
import static com.example.landgame.enums.MoveType.*;
import static com.example.landgame.config.Config.*;


@Getter
public class Game {

    public static final boolean globalCheck = false;
//    public static final boolean globalTime = true;
//    long[] funcTimes = new long[8];

    long[] times = new long[12];
    long[] amount = new long[8];
    public static long forcePath = 0;
    public static final boolean countAmount = true;

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

//    private final AllMoveStats[][] staticObjectCache;

    private final Random random;



    private final Map map;

    /**
     * Game deals with
     */
    public Game(Map map, List<Entity> entities) {
        this.map = map;
        this.random = map.getRandom();
        this.players = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.buildings = new ArrayList<>();

//        this.staticObjectCache = new AllMoveStats[this.map.getHeight()][this.map.getWidth()];
//        for (int i = 0; i < this.map.getHeight(); i++) {
//            for (int j = 0; j < this.map.getWidth(); j++) {
//                this.staticObjectCache[i][j] = new AllMoveStats();
//            }
//        }

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
                    addResource((Resource) t);
                    this.map.getTile(x,y).setEntity(t);
                }
            } else {
                throw new RuntimeException("typrrrr");
            }
        }
        cleanup();
        return teams;
    }

    private void addResource(Resource resource) {
        this.resources.add(resource);
        if (countAmount) {
            amount[0] += 1;
        }
    }

    private void removeResource(Resource resource) {
        this.resources.remove(resource);
        this.map.getTile(resource.getX(), resource.getY()).setEntity(null);
        if (countAmount) {
            amount[1] += 1;
        }
    }

    private void makeMove(Player player, Direction direction) {

        // move player
        if (direction.getX() != player.getX() || direction.getY() != player.getY()) {
            this.map.getTile(player.getX(), player.getY()).setEntity(null);
            this.map.getTile(direction.getX(), direction.getY()).setEntity(player);

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
                removeResource((Resource) entity);
                a = true;
//                a = this.resources.remove(entity);
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
            int x = this.random.nextInt(this.map.getWidth());
            int y = this.random.nextInt(this.map.getHeight());
            if (this.map.canResourceSpawn(x,y)) {
                lastStoneSpawn = tick;
                Stone resource = new Stone(x,y,stoneHealth);
                this.map.getTile(x,y).setEntity(resource);
                addResource(resource);
//                this.resources.add(resource);
            }
        }
        if (tick - lastTreeSpawn > treeSpawnRate) {
            int x = this.random.nextInt(this.map.getWidth());
            int y = this.random.nextInt(this.map.getHeight());
            if (this.map.canResourceSpawn(x,y)) {
                lastTreeSpawn = tick;
                Tree resource = new Tree(x,y,treeHealth);
                this.map.getTile(x,y).setEntity(resource);
                addResource(resource);
            }
        }

        if (tick - lastGoldSpawn > goldSpawnRate) {
            int x = this.random.nextInt(this.map.getWidth());
            int y = this.random.nextInt(this.map.getHeight());
            if (this.map.canResourceSpawn(x,y)) {
                lastGoldSpawn = tick;
                Gold resource = new Gold(x,y,goldHealth);
                this.map.getTile(x,y).setEntity(resource);
                addResource(resource);
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

        AllMoveStats allMoveStats = new AllMoveStats(0);
        for (Entity entity: list) {
            if (!entity.sameTeam(player)) {
//                forcePath += this.map.findPath(player.getX(), player.getY(), entity.getX(), entity.getY());
                long s;
                long e;
                s = System.nanoTime();
                int number = this.map.findPath(player.getX(), player.getY(), entity.getX(), entity.getY());
                e = System.nanoTime();
                times[7] += e - s;
                if (number != -1) {

                    s = System.nanoTime();
                    Vector vector = Map.decode(number);
                    e = System.nanoTime();
                    times[8] += e - s;
                    s = System.nanoTime();
                    // todo store stats locally
                    allMoveStats.addVector2(vector);
                    e = System.nanoTime();
                    times[9] += e - s;
//                    int distance = vector.getDistance();
//                    int move1 = vector.getMove1();
//                    int move2 = vector.getMove2();
//
//                    for (int i = 1; i <= 4; i++) {
//                        if (i == move1 || i == move2) {
//                            allMoveStats.addStat(i, distance - 1);
//                        } else {
//                            allMoveStats.addStat(i, distance + 1);
//                        }
//
//                    }
                }

            }
        }

//        AllMoveStats allMoveStats = new AllMoveStats();
//        for (Entity entity: list) {
//            if (!entity.sameTeam(player)) {
//
//
//                long s = System.nanoTime();
//
//                int number = this.map.findPath(
//                        player.getX(),
//                        player.getY(),
//                        entity.getX(),
//                        entity.getY());
//
//                long e = System.nanoTime();
//                this.funcTimes[1] += e - s;
//
//                if (number != -1) {
//
//                    Vector vector = PathMatrix.decode(number);
//                    int distance = vector.getDistance();
//                    int move1 = vector.getMove1();
//                    int move2 = vector.getMove2();
//
//
//                    /*todo fix distance for straight moves
//                     * X.    X.
//                     * .. -> ..
//                     * .o    o.
//                     * /\ 3  2
//                     * <- 3  4
//                     * \/ 4  3
//                     *
//                     */
//                    for (int i = 1; i <= 4; i++) {
//                        if (i == move1 || i == move2) {
//                            allMoveStats.addStat(i, distance - 1);
//                        } else {
//                            allMoveStats.addStat(i, distance + 1);
//                        }
//
//                    }
//                }
//
//            }
//        }
//        long s = System.nanoTime();
//        int[] a = this.map.getEnds(player.getX(), player.getY());
//        int number = 0;
//        for (Entity entity: list) {
//            if (!entity.sameTeam(player)) {
//                number = a[entity.getX() + entity.getY() * 150];
//            }
//        }
//        long e = System.nanoTime();
//        this.funcTimes[2] += e - s;
//        this.funcTimes[3] = number;
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

        long s;
        long e;
        long start = System.nanoTime();

        for (Player player: this.players) {

            s = System.nanoTime();
            List<Direction> legalMoves = getLegalMovesForPlayer(player);
            e = System.nanoTime();
            times[1] += e - s;

            // if only 1 move then make it
            if (legalMoves.size() == 1) {
                Direction direction = legalMoves.get(0);
                makeMove(player, direction);
            } else if (legalMoves.size() > 1) {

                s = System.nanoTime();
                AllMoveStats enemyStats = getAllMoveStats(player, this.players);
                e = System.nanoTime();
                times[2] += e - s;
                s = System.nanoTime();
                AllMoveStats houseStats = getAllMoveStats(player, this.buildings);
                e = System.nanoTime();
                times[3] += e - s;
                s = System.nanoTime();
                AllMoveStats resourceStats = getAllMoveStats(player, this.resources);
                e = System.nanoTime();
                times[4] += e - s;

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

        s = System.nanoTime();
        cleanup();
        e = System.nanoTime();
        times[5] += e - s;



        s = System.nanoTime();
        for (Building building : this.buildings) {
            spawnPlayer(building);
        }
        spawnResources();
        e = System.nanoTime();
        times[6] += e - s;
        changeWeights();

        long end = System.nanoTime();

        times[0] += end - start;

        show();
        tick++;
    }

    private void show() {

        System.out.print(tick);
        System.out.println("-------------------------------------------------");
        this.amount[2] = this.resources.size();
        this.amount[5] = this.players.size();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Total ");
        stringBuilder.append(String.format("% 5d ", this.times[0] / 1_000_000));
        stringBuilder.append(" Legal ");
        stringBuilder.append(String.format("% 5d ", this.times[1] / 1_000_000));
        stringBuilder.append(" Player ");
        stringBuilder.append(String.format("% 5d ", this.times[2] / 1_000_000));
        stringBuilder.append(" Build ");
        stringBuilder.append(String.format("% 5d ", this.times[3] / 1_000_000));
        stringBuilder.append(" Res ");
        stringBuilder.append(String.format("% 5d ", this.times[4] / 1_000_000));
        stringBuilder.append(" CleanUp ");
        stringBuilder.append(String.format("% 5d ", this.times[5] / 1_000_000));
        stringBuilder.append(" spawn ");
        stringBuilder.append(String.format("% 5d ", this.times[6] / 1_000_000));

        stringBuilder.append(" extra ");
        stringBuilder.append(String.format("% 5d ", this.times[7] / 1_000_000));
        stringBuilder.append(" ");
        stringBuilder.append(String.format("% 5d ", this.times[8] / 1_000_000));
        stringBuilder.append(" ");
        stringBuilder.append(String.format("% 5d ", this.times[9] / 1_000_000));
        stringBuilder.append("\n");

        stringBuilder.append("+Res ");
        stringBuilder.append(String.format("% 5d ", this.amount[0]));
        stringBuilder.append(" -Res ");
        stringBuilder.append(String.format("% 5d ", this.amount[1]));
        stringBuilder.append(" ResLen ");
        stringBuilder.append(String.format("% 5d ", this.amount[2]));
        stringBuilder.append(" PlyrLen ");
        stringBuilder.append(String.format("% 5d ", this.amount[5]));

        System.out.println(stringBuilder);
        Arrays.fill(this.times, 0);
        Arrays.fill(this.amount, 0);
    }


    public static void main(String[] args) {
    }
}
