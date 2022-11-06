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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

@AllArgsConstructor
@Getter
@Setter
@ToString
class MoveStats {
//    private int score;
    private int lowest;
    private int count;
    private int total;

    public void addDistance(int distance) {
        if (distance < lowest) {
            lowest = distance;
        }
        this.count++;
        this.total += distance;
//        this.score += (float) 1 / (sum * sum);
    }

    public void removeDistance(int distance) {
        if (distance == lowest) {
            lowest = 1000;
        }
        this.count--;
        this.total -= distance;
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

    public void addVector(Vector vector) {
        int distance = vector.getDistance();
        int move1 = vector.getMove1();
        int move2 = vector.getMove2();

        for (int i = 1; i <= 4; i++) {
            if (i == move1 || i == move2) {
                get(i).addDistance(distance - 1);
            } else {
                get(i).addDistance(distance + 1);
            }

        }
    }

    public void removeVector(Vector vector) {
        int distance = vector.getDistance();
        int move1 = vector.getMove1();
        int move2 = vector.getMove2();

        for (int i = 1; i <= 4; i++) {
            if (i == move1 || i == move2) {
                get(i).removeDistance(distance - 1);
            } else {
                get(i).removeDistance(distance + 1);
            }

        }
    }

//    private void add(int i, int distance) {
//        switch (i) {
//            case 1:
//                get()
//                this.moveStats1.addDistance(distance);
//                break;
//            case 2:
//                this.moveStats2.addDistance(distance);
//                break;
//            case 3:
//                this.moveStats3.addDistance(distance);
//                break;
//            case 4:
//                this.moveStats4.addDistance(distance);
//                break;
//        }
//    }

//    public void addStat(int t, int distance) {
//        get(t).addDistance(distance);
//    }
//
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
    public static final boolean globalTime = true;
    long[] funcTimes = new long[8];

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

    private final AllMoveStats[][] staticObjectCache;
    private int nr = 0;



    private final Map map;

    /**
     * Game deals with
     */
    public Game(Map map, List<Entity> entities) {
        this.map = map;

        this.players = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.buildings = new ArrayList<>();

        this.staticObjectCache = new AllMoveStats[this.map.getHeight()][this.map.getWidth()];
        for (int i = 0; i < this.map.getHeight(); i++) {
            for (int j = 0; j < this.map.getWidth(); j++) {
                this.staticObjectCache[i][j] = new AllMoveStats();
            }
        }

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
        // find direction stats for that res
        for (int i = 0; i < this.map.getWidth(); i++) {
            for (int j = 0; j < this.map.getHeight(); j++) {
                int n = this.map.findPath(i,j, resource.getX(), resource.getY());
                if (n != -1) {
                    Vector vector = Map.decode(n);
                    this.staticObjectCache[i][j].addVector(vector);
                }

//                int distance = vector.getDistance();
//                int move1 = vector.getMove1();
//                int move2 = vector.getMove2();
//
//                for (int i = 1; i <= 4; i++) {
//                    if (i == move1 || i == move2) {
//                        allMoveStats.addStat(i, distance - 1);
//                    } else {
//                        allMoveStats.addStat(i, distance + 1);
//                    }
//
//                }
            }
        }
        nr++;
        this.resources.add(resource);
    }

    private void removeResource(Resource resource) {
        // remove distance
        for (int i = 0; i < this.map.getWidth(); i++) {
            for (int j = 0; j < this.map.getHeight(); j++) {
                int n = this.map.findPath(i,j, resource.getX(), resource.getY());
                if (n != -1) {
                    Vector vector = Map.decode(n);
                    this.staticObjectCache[i][j].removeVector(vector);
                }

            }
        }
        this.resources.remove(resource);
        this.map.getTile(resource.getX(), resource.getY()).setEntity(null);
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
            Random random = new Random();
            int x = random.nextInt(this.map.getWidth());
            int y = random.nextInt(this.map.getHeight());
            if (this.map.canResourceSpawn(x,y)) {
                lastStoneSpawn = tick;
                Stone resource = new Stone(x,y,stoneHealth);
                this.map.getTile(x,y).setEntity(resource);
                addResource(resource);
//                this.resources.add(resource);
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
                addResource(resource);
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

        AllMoveStats allMoveStats = new AllMoveStats();
        for (Entity entity: list) {
            if (!entity.sameTeam(player)) {

                int number = this.map.findPath(player.getX(), player.getY(), entity.getX(), entity.getY());

                if (number != -1) {

                    Vector vector = PathMatrix.decode(number);
                    allMoveStats.addVector(vector);
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

    public int test0(int index, Player player) {

        int[] array = this.map.getEnds(player.getX(), player.getY());
        for (int i = index + 1; i < this.players.size(); i++) {
            Player player2 = this.players.get(i);
            if (!player.sameTeam(player2)) {
                int number = array[player2.getX() + 150 * player2.getY()];
                if (number == -2) {
                    number = this.map.findPath(player.getX(), player.getY(), player2.getX(), player2.getY());
                }
                int d = Map.decode(number).getDistance();
                player.setUpDistance(player.getUpDistance() + d);
                player2.setUpDistance(player2.getUpDistance() + d);
            }
        }
        return player.getUpDistance();
    }

    public int test(int index, Player player) {
        for (int i = index + 1; i < this.players.size(); i++) {
            Player player2 = this.players.get(i);
            if (!player.sameTeam(player2)) {
                int number = this.map.findPath(player.getX(), player.getY(), player2.getX(), player2.getY());
                int d = Map.decode(number).getDistance();
                player.setUpDistance(player.getUpDistance() + d);
                player2.setUpDistance(player2.getUpDistance() + d);
            }
        }
        return player.getUpDistance();
    }

    public int test2(Player player) {

        int a = 0;

        for (int i = 0; i < this.players.size(); i++) {
            Player player2 = this.players.get(i);
            if (!player.sameTeam(player2)) {
                int number = this.map.findPath(player.getX(), player.getY(), player2.getX(), player2.getY());
                int d = Map.decode(number).getDistance();
                a += d;
            }
        }
        return a;
    }

    public int test_res_default(Player player) {
        int a = 0;
        for (Resource resource: this.resources) {
            int number = this.map.findPath(player.getX(), player.getY(), resource.getX(), resource.getY());
            Vector vector = Map.decode(number);
            if (vector.getMove1() == 1 || vector.getMove2() == 1) {
                a += vector.getDistance() - 1;
            } else {
                a += vector.getDistance() + 1;
            }
        }
        return a;
    }

    public int test_res_cache(Player player) {
//        System.out.println(this.staticObjectCache[player.getX()][player.getY()].get(1));
        return this.staticObjectCache[player.getX()][player.getY()].get(1).getTotal();
    }

    public void tick() {

        /* todo fix when multiple players go straight
         *  A1 A2 E1
         *
         * A1 should make another random move
         */

        long s;
        long e;

        int check = 0;
        int pla = this.players.size();
        int res = this.resources.size();

        for (Player player: this.players) {
            for (Player player2: this.players) {
                check += this.map.findPath(player.getX(), player.getY(), player2.getX(), player2.getY());
            }
        }

        int test = 0;
        int test2 = 0;
        int test0 = 0;

        s = System.currentTimeMillis();
        for (int index = 0; index < this.players.size(); index++) {
            Player player = this.players.get(index);
            test += test(index, player);
        }
        for (Player player: this.players) {
            player.setUpDistance(0);
        }
        e = System.currentTimeMillis();
        this.funcTimes[1] += e - s;

        s = System.currentTimeMillis();
        for (int index = 0; index < this.players.size(); index++) {
            Player player = this.players.get(index);
            test2 += test2(player);
        }
        e = System.currentTimeMillis();
        this.funcTimes[2] += e - s;

        s = System.currentTimeMillis();
        for (int index = 0; index < this.players.size(); index++) {
            Player player = this.players.get(index);
            test0 += test0(index, player);
        }
        for (Player player: this.players) {
            player.setUpDistance(0);
        }
        e = System.currentTimeMillis();
        this.funcTimes[3] += e - s;

        //todo
        // add static object val tables
        // int[] > sum(n) > n**2

        int res1 = 0;
        int res2 = 0;

        s = System.nanoTime();

//        System.out.println("start----");

        for (Player player: this.players) {
            int a = test_res_default(player);
//            System.out.println(player + " " + a);
            res1 += a;
        }
        e = System.nanoTime();
        this.funcTimes[4] += e - s;

//        System.out.println("------");

        s = System.nanoTime();
        for (Player player: this.players) {
            int a = test_res_cache(player);
//            System.out.println(player + " " + a);
            res2 += a;
        }

        e = System.nanoTime();
        this.funcTimes[5] += e - s;

//        System.out.println(this.resources.size());
//        System.out.println(nr);
//        System.out.println("-----");
        if (res1 != res2) {
            throw new RuntimeException(res1 + " " + res2);
        }



//        s = System.currentTimeMillis();
//        for (int index = 0; index < this.players.size(); index++) {
//            Player player = this.players.get(index);
//            test += test(index, player);
//        }
//        for (Player player: this.players) {
//            player.setUpDistance(0);
//        }
//        e = System.currentTimeMillis();
//        this.funcTimes[4] += e - s;
//
//        s = System.currentTimeMillis();
//        for (int index = 0; index < this.players.size(); index++) {
//            Player player = this.players.get(index);
//            test2 += test2(player);
//        }
//        e = System.currentTimeMillis();
//        this.funcTimes[5] += e - s;


//        for (Player player: this.players) {
//            for (Player player2: this.players) {
//                if (!player.sameTeam(player2)) {
//
//                    int number = this.map.findPath(
//                            player.getX(),
//                            player.getY(),
//                            player2.getX(),
//                            player2.getY());
//
//                    test += number;
//                }
//            }
//
//            for (Resource player2: this.resources) {
//                if (!player2.sameTeam(player)) {
//
//                    int number = this.map.findPath(
//                            player.getX(),
//                            player.getY(),
//                            player2.getX(),
//                            player2.getY());
//
//                    test += number;
//                }
//            }
//        }

        tick++;
        for (Player player: this.players) {

            List<Direction> legalMoves = getLegalMovesForPlayer(player);

            // if only 1 move then make it
            if (legalMoves.size() == 1) {
                Direction direction = legalMoves.get(0);
                makeMove(player, direction);
            } else if (legalMoves.size() > 1) {


                s = System.currentTimeMillis();

                AllMoveStats enemyStats = getAllMoveStats(player, this.players);
                AllMoveStats houseStats = getAllMoveStats(player, this.buildings);
                AllMoveStats resourceStats = getAllMoveStats(player, this.resources);

                e = System.currentTimeMillis();
                if (globalTime) {
                    this.funcTimes[0] += e - s;
                }

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

        s = System.nanoTime();
        spawnResources();
        e = System.nanoTime();
        this.funcTimes[6] += e - s;


        changeWeights();
        System.out.println(Arrays.toString(this.funcTimes));
        System.out.println(check + " " + pla + " " + res);
        if (!(test == test0 && test == test2)) {
            throw new RuntimeException(test + " " + test0 + " " + test2);
        }
        Arrays.fill(this.funcTimes, 0);
    }

    public static void main(String[] args) {

//        // todo evey round calculate scoreboard for all objects
//        //  then if want to find score for direction check
//        //  check scoreboard if has calculated if has then no need to recalculate
//        //  clear scoreboard after every tick
//
//        Random random = new Random(0);
//        Map map = new Map(150, 150, random);
//
//        TeamConfig teamConfig = new TeamConfig(
//                1,
//                1,
//                1,
//                1,
//                2,
//                1);
//
//        Team blue = new Team(BLUE, teamConfig);
//        Team red = new Team(RED, teamConfig);
//
//        List<Entity> entities = new ArrayList<>();
//        entities.add(blue.createPlayer(149, 149, Farmer.class));
//        entities.add(blue.createPlayer(139, 139, Farmer.class));
//        entities.add(red.createPlayer(39, 4, Farmer.class));
//
//        Player player = red.createPlayer(45, 1, Farmer.class);
//
//        entities.add(player);
//
//        entities.addAll(map.getTerrainGeneration().generateResources());
//
//        Game game = new Game(map, entities);
//        System.out.println(game.getResources().size());
//        AllMoveStats allMoveStats = game.getAllMoveStats(player, game.getResources());
//        System.out.println(allMoveStats);
//
//        int x = player.getX();
//        int y = player.getY();
//        for (Direction direction: Game.directions) {
//            int finalx = x + direction.getX();
//            int finaly = y + direction.getY();
//            int total = 0;
//
//            for (Resource resource: game.getResources()) {
//                int move = game.getMap().findPath(finalx,finaly,resource.getX(), resource.getY());
//                if (move != -1) {
//                    Vector vector = Map.decode(move);
//                    total += vector.getDistance();
//                }
//
//            }
//
//            System.out.println(direction.getMoveNumber());
//            System.out.println(total);
//        }
//
//
//        /**
//         * normal
//         * 1521
//         * AllMoveStats(
//         * moveStats1=MoveStats(lowest=4, count=1521, total=341784),
//         * moveStats2=MoveStats(lowest=4, count=1521, total=341816),
//         * moveStats3=MoveStats(lowest=2, count=1521, total=338810),
//         * moveStats4=MoveStats(lowest=2, count=1521, total=338774)
//         * )
//         */


        for (int i = 0; i < 10; i++) {
            for (int j = i + 1; j < 10; j++) {
                System.out.println(i + " " + j);
            }
        }
    }
}
