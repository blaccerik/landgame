package com.example.landgame.map;

import com.example.landgame.Game;
import com.example.landgame.PortalInfo;
import com.example.landgame.enums.TerrainType;
import com.example.landgame.pathfinding.ChunkInfo;
import com.example.landgame.pathfinding.Direction;
import com.example.landgame.pathfinding.ExploreNode;
import com.example.landgame.pathfinding.PathMatrix;
import com.example.landgame.pathfinding.Vector;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.Random;
import java.util.Set;



@Getter
public class Map {


    private final int height;
    private final int width;
    private final int totalTiles;
    private int exploredTiles;
    private final Random random;
    private final Terrain[][] terrain;
    private final int[][] matrix;
//    private final int[] coords;
    private final TerrainGeneration terrainGeneration;

    public Map(int width, int height, Random random) {
        this.width = width;
        this.height = height;
        this.random = random;
        this.terrainGeneration = new TerrainGeneration(this.random, width, height);
        this.terrain = terrainGeneration.generateTerrain();

        int a = 0;
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (isMovable(i,j)) {
                    a++;
                }
            }
        }
        this.totalTiles = a;
        this.exploredTiles = 0;


//        this.terrain = new Terrain[150][150];
//        for (int i = 0; i < 150; i++) {
//            for (int j = 0; j < 150; j++) {
//                this.terrain[i][j] = new Terrain(i,j, TerrainType.LAND);
//            }
//        }
//        this.coords = new int[width * height * width * height];
//        for (int i = 0; i < this.coords.length; i++) {
//            this.coords[i] = -2;
//        }
//        Arrays.fill(this.coords, -2);

        this.matrix = new int[width * height][width * height];
        for (int i = 0; i < width * height; i++) {
            for (int j = 0; j < width * height; j++) {
                this.matrix[i][j] = -2;
            }
        }
    }

    public Terrain getTile(int x, int y) {
        return this.terrain[x][y];
    }

    public int getMoveNumber(int xs, int ys, int xe, int ye) {
        final int shift = this.height * this.width;
        int coords = xs + ys * this.height;
        int coorde = xe + ye * this.height;
//        return this.coords[coords + coorde * shift];
        return this.matrix[coords][coorde];
    }

    public void setMoveNumber(int xs, int ys, int xe, int ye, int moveNumber) {
        final int shift = this.height * this.width;
        int coords = xs + ys * this.height;
        int coorde = xe + ye * this.height;
//        this.coords[coords + coorde * shift] = moveNumber;
        this.matrix[coords][coorde] = moveNumber;
    }

    private boolean isOut(int x, int y) {
        return 0 > x || 0 > y || x >= this.width || y >= this.height;
    }


    public boolean isBlocked(int x, int y) {
        if (isMovable(x,y)) {
            Terrain terrain = getTile(x,y);
            return terrain.getBuilding() == null && terrain.getEntity() == null;
        }
        return false;
    }
    public boolean isMovable(int x, int y) {
        if (!isOut(x,y)) {
            Terrain terrain = this.terrain[x][y];
            switch (terrain.getTerrainType()) {
                case WATER:
                case DEEP_WATER:
                    return false;
                case SAND:
                case LAND:
                    return true;
            }
        }
        return false;
    }
    public boolean isBuildable(int x, int y) {
        // todo phantom building??
        if (!isOut(x,y)) {
            Terrain terrain = this.terrain[x][y];
            switch (terrain.getTerrainType()) {
                case WATER:
                case DEEP_WATER:
                    return false;
                case SAND:
                case LAND:
                    return terrain.getBuilding() == null;
            }
        }
        return false;
    }

    public boolean canResourceSpawn(int x, int y) {
        if (!isOut(x,y)) {
            Terrain terrain = this.terrain[x][y];
            switch (terrain.getTerrainType()) {
                case WATER:
                case DEEP_WATER:
                case SAND:
                    return false;
                case LAND:
                    return terrain.getBuilding() == null && terrain.getEntity() == null;
            }
        }
        return false;
    }

    public int findPath(int xs, int ys, int xe, int ye) {
        int cache = getMoveNumber(xs, ys, xe, ye);
        if (cache != -2) {
            return cache;
        }

        this.exploredTiles++;

        // keep track of places where have been
        Queue<ExploreNode> queue = new ArrayDeque<>();
        ExploreNode[][] map = new ExploreNode[this.width][this.height];
        ExploreNode start = new ExploreNode(xe, ye, 0, 0);
        map[xe][ye] = start;
        queue.add(start);
        while (!queue.isEmpty()) {

            ExploreNode currentNode = queue.poll();
            int x = currentNode.getX();
            int y = currentNode.getY();
            int distance = currentNode.getDistance();

            for (Direction direction : Game.directions) {
                int finalX = x - direction.getX();
                int finalY = y - direction.getY();

                if (isMovable(finalX, finalY)) {

                    int newDistance = distance + 1;
                    int moveNumber = direction.getMoveNumber();
                    if (map[finalX][finalY] == null) { // haven't explored yet
                        ExploreNode exploreNode = new ExploreNode(finalX, finalY, newDistance, moveNumber);
                        map[finalX][finalY] = exploreNode;
                        queue.add(exploreNode);
                    } else { // update if distance is same
                        ExploreNode exploreNode = map[finalX][finalY];
                        if (exploreNode.getDistance() == newDistance) {
                            exploreNode.setMove2(moveNumber);
                        }
                    }
                }
            }
        }

        // update matrix
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                ExploreNode exploreNode = map[x][y];
                if (exploreNode != null) {
                    int f = PathMatrix.encode(exploreNode.getDistance(), exploreNode.getMove1(), exploreNode.getMove2());
                    setMoveNumber(x,y,xe,ye,f); // is path
                } else {
                    setMoveNumber(x,y,xe,ye,-1); // no path
                }
            }
        }
        return getMoveNumber(xs, ys, xe, ye);
    }

    public static Vector decode(int number) {
        if (number == -1) {
            throw new RuntimeException("VECTOR232");
        }
        return new Vector(number >> 6, (number >> 3) & 7, number & 7);
    }

    public static void main(String[] args) {
        Random random = new Random();
        random.setSeed(0);
        int size = 150;
        Map map = new Map(size, size, random);


        int a = 0;
//        for (int iter = 0; iter < 150; iter++) {
//            for (int i = 0; i < size; i++) {
//                a += map.findPath(47,47, iter,i);
//            }
//        }

        long s = System.currentTimeMillis();
        Random random1 = new Random();
        for (int i = 0; i < 100000; i++) {
            int x = random1.nextInt(150);
            int y = random1.nextInt(150);
            a += map.findPath(47,47, x, y);
        }

        long e = System.currentTimeMillis();
        System.out.println("time: " + (e - s));
        System.out.println(a);
        // 1d: 17.9 17.1 18.5
        // 2d: 50.1

//        System.out.println(map.getHeight());
//        map.findPath()
    }
}
