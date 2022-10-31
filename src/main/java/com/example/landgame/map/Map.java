package com.example.landgame.map;

import com.example.landgame.Game;
import com.example.landgame.PortalInfo;
import com.example.landgame.pathfinding.ChunkInfo;
import com.example.landgame.pathfinding.Direction;
import com.example.landgame.pathfinding.ExploreNode;
import com.example.landgame.pathfinding.PathMatrix;
import com.example.landgame.pathfinding.Vector;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import static com.example.landgame.config.Config.storeFlowMatrix;
import static com.example.landgame.pathfinding.PathMatrix.chunkBits;
import static com.example.landgame.pathfinding.PathMatrix.chunkSize;


@Getter
public class Map {


    private final int height;
    private final int width;
//    private final int totalTiles;
//    private int exploredTiles;
    private final Random random;
    private final Terrain[][] terrain;
    private final int[][] matrix;
    private final TerrainGeneration terrainGeneration;

    public Map(int width, int height, Random random) {
        this.width = width;
        this.height = height;
        this.random = random;
        this.terrainGeneration = new TerrainGeneration(this.random, width, height);
        this.terrain = terrainGeneration.generateTerrain();
        this.matrix = new int[width * height][width * height];
        for (int i = 0; i < width * height; i++) {
            for (int j = 0; j < width * height; j++) {
                this.matrix[i][j] = -2;
            }
        }

        if (storeFlowMatrix) {
            updateFlowField();
        }
    }

    private String mapToFileName() {
        return this.height + "_" + this.width + ".map";
    }

    private void updateFlowField() {
        String fileName = mapToFileName();
        try {
            FileReader reader = new FileReader(fileName);
            BufferedReader buffer = new BufferedReader(reader, 16384);

            try {

                for (int i = 0; i < width * height; i++) {
                    for (int j = 0; j < width * height; j++) {
                        String[] strings = buffer.readLine().split(" ");
                    }
//                    fileWriter.write(stringBuilder.toString());
                }


//                for (int i = 0; i < this.height; i++) {
//                    for (int j = 0; j < this.width; j++) {
//                        for (int k = 0; k < this.height; k++) {
//                            for (int l = 0; l < this.width; l++) {
//                                int n = Integer.parseInt(buffer.readLine());
//                                setMoveNumber(i,j,k,l,n);
//                            }
//                        }
//                    }
//                    System.out.println(i +":" + this.height);
//                }
                buffer.close();
                reader.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }





        } catch (FileNotFoundException e) {
            try {
                System.out.println("Creating file");
                FileWriter myWriter = new FileWriter(fileName);
                mapToFile(myWriter);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException ee) {
                System.out.println("An error occurred.");
                ee.printStackTrace();
            }
        }
    }

    private void mapToFile(FileWriter fileWriter) throws IOException {


        for (int i = 0; i < width * height; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < width * height; j++) {
                int n = this.matrix[i][j];
                stringBuilder.append(n);
                stringBuilder.append(" ");
            }
            fileWriter.write(stringBuilder.toString());
            fileWriter.write("\n");
        }

//        for (int i = 0; i < this.height; i++) {
//            for (int j = 0; j < this.width; j++) {
//                for (int k = 0; k < this.height; k++) {
//                    for (int l = 0; l < this.width; l++) {
//                        int number = findPath(i,j,k,l);
//                    }
//                }
//            }
//            System.out.println(i +":" + this.height);
//        }

    }

    public Terrain getTile(int x, int y) {
        return this.terrain[x][y];
    }

    public int getMoveNumber(int xs, int ys, int xe, int ye) {
        int coords = xs + ys * this.height;
        int coorde = xe + ye * this.height;
        return this.matrix[coords][coorde];
    }

    public void setMoveNumber(int xs, int ys, int xe, int ye, int moveNumber) {
        int coords = xs + ys * this.height;
        int coorde = xe + ye * this.height;
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
        int i = map.findPath(47,47, 80,80);
        System.out.println(decode(i));
//        System.out.println(map.getHeight());
//        map.findPath()
    }
}
