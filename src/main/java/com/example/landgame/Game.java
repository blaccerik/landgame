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
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.landgame.enums.TerrainType.*;
import static com.example.landgame.enums.MoveType.*;
import static com.example.landgame.config.Config.*;

@Getter
public class Game {

    public static final boolean globalCheck = true;
    public static final boolean globalTime = true;
    long[] funcTimes = new long[8];
    static long check = 0;
    long[] time = new long[6];

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
    private final int[][][] cache;
    private final int[][][] cacheFast;
    private final int[][][] cacheSingle;
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

        int numberOfValues = 2;
        int directionNumber = 4;
        int size = 400;
        this.cache = new int[this.map.getWidth()][this.map.getHeight()][directionNumber * (numberOfValues + size)];
        // too slow
        this.cacheFast = new int[this.map.getWidth()][this.map.getHeight()][directionNumber * numberOfValues];

        this.cacheSingle = new int[this.map.getWidth()][this.map.getHeight()][];

        for (int i = 0; i < this.map.getHeight(); i++) {
            for (int j = 0; j < this.map.getWidth(); j++) {
                this.cache[i][j][0] = 1000;
                this.cache[i][j][402] = 1000;
                this.cache[i][j][402 * 2] = 1000;
                this.cache[i][j][402 * 3] = 1000;
                this.cacheFast[i][j][0] = 1000;
                this.cacheFast[i][j][2] = 1000;
                this.cacheFast[i][j][4] = 1000;
                this.cacheFast[i][j][6] = 1000;
            }
        }

        this.staticObjectCache = new AllMoveStats[this.map.getHeight()][this.map.getWidth()];
        for (int i = 0; i < this.map.getHeight(); i++) {
            for (int j = 0; j < this.map.getWidth(); j++) {
                this.staticObjectCache[i][j] = new AllMoveStats();
            }
        }

        List<Team> teams = putEntities(entities);
        this.teams = new Team[teams.size()];
        teams.toArray(this.teams);

        show();
    }

    private void show() {
        for (long l : this.time) {
            System.out.printf("% 5d", l / 1_000_000);
            System.out.print(" ");
        }
        System.out.println();
        Arrays.fill(this.time, 0);
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

        check += this.map.findPath(0,0, resource.getX(), resource.getY());


        long s;
        long e;

        s = System.nanoTime();
        // find direction stats for that res
        for (int i = 0; i < this.map.getWidth(); i++) {
            for (int j = 0; j < this.map.getHeight(); j++) {
                int n = this.map.findPath(i,j, resource.getX(), resource.getY());
                if (n != -1) {
                    Vector vector = Map.decode(n);
                    this.staticObjectCache[i][j].addVector(vector);
                }
            }
        }
        e = System.nanoTime();
        this.time[0] += e - s;

        s = System.nanoTime();
        // find direction stats for that res
        for (int i = 0; i < this.map.getWidth(); i++) {
            for (int j = 0; j < this.map.getHeight(); j++) {
                int n = this.map.findPath(i,j, resource.getX(), resource.getY());
                if (n != -1) {
                    Vector vector = Map.decode(n);
                    addToCache(i,j,vector);
                }
            }
        }
        e = System.nanoTime();
        this.time[2] += e - s;

        s = System.nanoTime();
        // find direction stats for that res
        for (int i = 0; i < this.map.getWidth(); i++) {
            for (int j = 0; j < this.map.getHeight(); j++) {
                int n = this.map.findPath(i,j, resource.getX(), resource.getY());
                if (n != -1) {
                    Vector vector = Map.decode(n);
                    addToCacheFast(i,j,vector);

                }
            }
        }
        e = System.nanoTime();
        this.time[4] += e - s;

        for (int i = 0; i < this.map.getWidth(); i++) {
            for (int j = 0; j < this.map.getHeight(); j++) {
                if (this.staticObjectCache[i][j].get(1).getLowest() != this.cache[i][j][0]) {
                    System.out.println(this.staticObjectCache[i][j].get(1).getLowest());
                    System.out.println(this.cache[i][j][0]);
                    throw new RuntimeException();
                }
                if (this.staticObjectCache[i][j].get(1).getTotal() != this.cache[i][j][1]) {
                    System.out.println(i + " " + j);
                    System.out.println(this.staticObjectCache[i][j].get(1).getTotal());
                    System.out.println(this.cache[i][j][1]);
                    throw new RuntimeException();
                }
//                if (this.staticObjectCache[i][j].get(2).getLowest() != this.cacheFast[i][j][2]) {
//                    System.out.println(i + " " + j);
//                    System.out.println(this.staticObjectCache[i][j].get(2).getLowest());
//                    System.out.println( this.cacheFast[i][j][2]);
//                    throw new RuntimeException();
//                }
//                if (this.staticObjectCache[i][j].get(2).getTotal() != this.cacheFast[i][j][3]) {
//                    System.out.println(i + " " + j);
//                    System.out.println(this.staticObjectCache[i][j].get(2).getTotal());
//                    System.out.println( this.cacheFast[i][j][3]);
//                    throw new RuntimeException();
//                }
            }
        }

        this.resources.add(resource);
    }

    private void addToCache(int x, int y, Vector vector) {
        final int size = 400 + 2;
        int distance = vector.getDistance();
        int move1 = vector.getMove1();
        int move2 = vector.getMove2();
        int[] m = this.cache[x][y];
        for (int i = 0; i < 4; i++) {
            int value;
            if (i + 1 == move1 || i + 1 == move2) {
                value = distance - 1;
            } else {
                value = distance + 1;
            }
            if (value < m[size * i]) {
                m[size * i] = value;
            }
            m[size * i + 1] += value;
            m[size * i + 2 + value]++;
        }
    }

    private void addToCacheFast(int x, int y, Vector vector) {
        int distance = vector.getDistance();
        int move1 = vector.getMove1();
        int move2 = vector.getMove2();
        int[] m = this.cacheFast[x][y];
        for (int i = 0; i < 4; i++) {
            int value;
            if (i + 1 == move1 || i + 1 == move2) {
                value = distance - 1;
            } else {
                value = distance + 1;
            }
            if (value < m[i * 2]) {
                m[i * 2] = value;
            }
            m[(i * 2) + 1] += value;
        }
    }

    private void removeResource(Resource resource) {
        check += this.map.findPath(0,0, resource.getX(), resource.getY());
        System.out.println(this.staticObjectCache[0][0].get(1));

        for (int ind = 0; ind < 4; ind++) {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                list.add(this.cache[0][0][402 * ind + i]);
            }
            System.out.println(list);
        }


        long s;
        long e;

        s = System.nanoTime();
        for (int i = 0; i < this.map.getWidth(); i++) {
            for (int j = 0; j < this.map.getHeight(); j++) {
                int n = this.map.findPath(i,j, resource.getX(), resource.getY());
                if (n != -1) {
                    Vector vector = Map.decode(n);
                    // todo store values in array not objects
                    this.staticObjectCache[i][j].removeVector(vector);
                }
            }
        }
        e = System.nanoTime();
        this.time[1] += e - s;

        s = System.nanoTime();
        // find direction stats for that res
        for (int i = 0; i < this.map.getWidth(); i++) {
            for (int j = 0; j < this.map.getHeight(); j++) {
                int n = this.map.findPath(i,j, resource.getX(), resource.getY());
                if (n != -1) {
                    Vector vector = Map.decode(n);
                    removeFromCache(i,j,vector);

                }
            }
        }
        e = System.nanoTime();
        this.time[3] += e - s;


//        s = System.nanoTime();
//        // find direction stats for that res
//        for (int i = 0; i < this.map.getWidth(); i++) {
//            for (int j = 0; j < this.map.getHeight(); j++) {
//                int n = this.map.findPath(i,j, resource.getX(), resource.getY());
//                if (n != -1) {
//                    Vector vector = Map.decode(n);
//                    if (removeFromFastCache(i,j,vector)) {
//                        int[] m = this.cacheFast[i][j];
//                        m[0] = 1000;
//                        m[1] = 0;
//                        m[2] = 1000;
//                        m[3] = 0;
//                        m[4] = 1000;
//                        m[5] = 0;
//                        m[6] = 1000;
//                        m[7] = 0;
//                        for (Resource resource1 : this.resources) {
//                            int n2 = this.map.findPath(i,j, resource1.getX(), resource1.getY());
//                            if (n2 != -1) {
//                                Vector vector2 = Map.decode(n2);
//                                addToCacheFast(i,j,vector2);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        e = System.nanoTime();
//        this.time[5] += e - s;



        // todo add remove for fast cache

//        System.out.println(this.staticObjectCache[0][0].get(1));
//        list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add(this.cache[0][0][i]);
//        }
//        System.out.println(list);

        for (int i = 0; i < this.map.getWidth(); i++) {
            for (int j = 0; j < this.map.getHeight(); j++) {
                int total = this.staticObjectCache[i][j].get(1).getTotal();
                int low = this.staticObjectCache[i][j].get(1).getLowest();
                int low2 = this.cache[i][j][0];
                int t2 = this.cache[i][j][1];
                if (total != t2) {
                    System.out.println(i + " " + j);
                    throw new RuntimeException(total + " " + t2);
                } else if (low != low2) {
                    System.out.println(i + " " + j);
                    throw new RuntimeException(low + " " + low2);
                }
            }
        }


        this.funcTimes[7] += 1;
        this.resources.remove(resource);
        this.map.getTile(resource.getX(), resource.getY()).setEntity(null);
    }

    private void removeFromCache(int x, int y, Vector vector) {
        final int size = 400 + 2;
        int distance = vector.getDistance();
        int move1 = vector.getMove1();
        int move2 = vector.getMove2();
        int[] m = this.cache[x][y];
        for (int i = 0; i < 4; i++) {
            int value;
            if (i + 1 == move1 || i + 1 == move2) {
                value = distance - 1;
            } else {
                value = distance + 1;
            }
            // total
            m[size * i + 1] -= value;
            // distance
            int distance2 = m[size * i + 2 + value];
            distance2--;
            m[size * i + 2 + value] = distance2;


//            if (x == 0 && y == 0) {
//                System.out.println("------");
//                List<Integer> list = new ArrayList<>();
//                for (int index = 0; index < 10; index++) {
//                    list.add(this.cache[x][y][index]);
//                }
//                System.out.println(list);
//            }

            // lowest
            int lowest = m[size * i];
            if (value == lowest && distance2 == 0) {

                int best = 1000;
                for (int j = size * i + 2 + value; j < size * i + 400; j++) {
                    int n = m[j];
                    if (n < best && n != 0) {
                        best = j - 2;
                        break;
                    }
                }
                m[size * i] = best;
            }

//            if (x == 0 && y == 0) {
//                List<Integer> list = new ArrayList<>();
//                for (int index = 0; index < 10; index++) {
//                    list.add(this.cache[x][y][index]);
//                }
//                System.out.println(list);
//            }
        }
    }

    private boolean removeFromFastCache(int x, int y, Vector vector) {
        int distance = vector.getDistance();
        int move1 = vector.getMove1();
        int move2 = vector.getMove2();
        int[] m = this.cacheFast[x][y];
        for (int i = 0; i < 4; i++) {
            int value;
            if (i + 1 == move1 || i + 1 == move2) {
                value = distance - 1;
            } else {
                value = distance + 1;
            }
            // total
            m[(2 * i) + 1] -= value;
            // lowest
            int lowest = m[2 * i];
            // find new lowest
            if (value == lowest) {
                return true;
            }
        }
        return false;
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
        for (Direction direction: com.example.landgame.Game.directions) {
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

        com.example.landgame.AllMoveStats allMoveStats = new com.example.landgame.AllMoveStats();
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

            List<Direction> legalMoves = getLegalMovesForPlayer(player);

            // if only 1 move then make it
            if (legalMoves.size() == 1) {
                Direction direction = legalMoves.get(0);
                makeMove(player, direction);
            } else if (legalMoves.size() > 1) {


                s = System.nanoTime();

                com.example.landgame.AllMoveStats enemyStats = getAllMoveStats(player, this.players);
                com.example.landgame.AllMoveStats houseStats = getAllMoveStats(player, this.buildings);

                e = System.nanoTime();
                if (globalTime) {
                    this.funcTimes[1] += e - s;
                }

                s = System.nanoTime();

                // get stats from cache
                com.example.landgame.AllMoveStats resourceStats = this.staticObjectCache[player.getX()][player.getY()];
//                com.example.landgame.AllMoveStats resourceStats2 = getAllMoveStats(player, this.resources);
//                for (int i = 1; i <= 4; i++) {
//                    if (resourceStats.get(i).getCount() != resourceStats2.get(i).getCount() ||
//                            resourceStats.get(i).getTotal() != resourceStats2.get(i).getTotal()
//                            || resourceStats.get(i).getLowest() != resourceStats2.get(i).getLowest()
//                    ) {
//                        int n = 10;
//                        ArrayList<Integer> outArray = new ArrayList<>();
//                        for (int in = 0; in < n; in++)
//                            outArray.add(resourceStats.getMoveStats2().getQueue()[in]);
//                        System.out.println(outArray);
//                        System.out.println(resourceStats.getMoveStats2().getLowest());
//                        n = 10;
//                        outArray = new ArrayList<>();
//                        for (int in = 0; in < n; in++)
//                            outArray.add(resourceStats2.getMoveStats2().getQueue()[in]);
//                        System.out.println(outArray);
//                        System.out.println(resourceStats2.getMoveStats2().getLowest());
//                        throw new RuntimeException("\n" +
//                                resourceStats
//                                + "\n" +
//                                resourceStats2
//                        );
//                    }
//                }

                e = System.nanoTime();
                if (globalTime) {
                    this.funcTimes[2] += e - s;
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

        s = System.nanoTime();
        cleanup();
        e = System.nanoTime();
        if (globalTime) {
            this.funcTimes[3] += e - s;
        }

        for (Building building : this.buildings) {
            spawnPlayer(building);
        }

        s = System.nanoTime();

        spawnResources();

        e = System.nanoTime();
        this.funcTimes[4] += e - s;

        changeWeights();

        long end = System.nanoTime();

        this.funcTimes[0] += end - start;

        show();

//        // stats
//        if (tick % 10 == 0) {
//            System.out.printf("% 5d T: % 5d O: % 6d R: % 6d C: % 6d S: % 6d PS| % 6d RS| % 6d EX| % 6d H| %d\n",
//                    tick,
//                    this.funcTimes[0] / 1_000_000,
//                    this.funcTimes[1] / 1_000_000,
//                    this.funcTimes[2] / 1_000_000,
//                    this.funcTimes[3] / 1_000_000,
//                    this.funcTimes[4] / 1_000_000,
//                    this.players.size(),
//                    this.resources.size(),
//                    this.map.getExploredTiles(),
//                    this.map.getHash()
//
//            );
//            System.out.println(
//                    (this.funcTimes[5] / 1_000_000) + " " +
//                    (this.funcTimes[6] / 1_000_000) + " " +
//                    (this.funcTimes[7])
//
//            );
//        }
//        Arrays.fill(this.funcTimes, 0);
        tick++;
    }

    private static void testNormal(int[] m, int distance, int move1, int move2) {
        int size = 12;

        for (int i = 0; i < 4; i++) {
            int value;
            if (i + 1 == move1 || i + 1 == move2) {
                value = distance - 1;
            } else {
                value = distance + 1;
            }
            if (value < m[size * i]) {
                m[size * i] = value;
            }
            m[size * i + 1] += value;
            m[size * i + 2 + value]++;
        }
        // show
        for (int ind = 0; ind < 4; ind++) {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                list.add(m[12 * ind + i]);
            }
            System.out.println(list);
        }
    }

    private static void testSingle(int[] cacheSingle, int distance, int move1, int move2) {
        if (cacheSingle[0] > distance) {
            cacheSingle[0] = distance;
        }
        cacheSingle[2]++;
        cacheSingle[1] += distance;
        cacheSingle[7 + distance]++;
        for (int i = 0; i < 4; i++) {
            if (i + 1 == move1 || i + 1 == move2) {
                cacheSingle[3 + i]--;
            } else {
                cacheSingle[3 + i]++;
            }
        }
        System.out.println(" L  T   C m1 m2 m3 m4  0  1  2  3");
        System.out.println(Arrays.toString(cacheSingle));;
    }

    public static void main(String[] args) {

        int[] m = new int[4 * (2 + 10)];
        int size = 12;
        m[0] = 1000;
        m[12] = 1000;
        m[24] = 1000;
        m[36] = 1000;
        int[] cacheSingle = new int[4 + 3 + 10];
        cacheSingle[0] = 1000;

//        testNormal(m, 2, 1,0);
//        testSingle(cacheSingle, 2,1,0);

        testNormal(m, 3, 3,0);
        testSingle(cacheSingle, 3,3,0);

        testNormal(m, 4, 3,0);
        testSingle(cacheSingle, 4,3,0);

        testNormal(m, 5, 3,0);
        testSingle(cacheSingle, 5,3,0);
    }
}

