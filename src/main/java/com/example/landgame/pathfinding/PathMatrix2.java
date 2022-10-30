package com.example.landgame.pathfinding;

import com.example.landgame.Game;
import com.example.landgame.map.Terrain;
import com.example.landgame.objects.Entity;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static com.example.landgame.enums.TerrainType.LAND;


@Setter
public class PathMatrix2 {
//    private final static int minChunkSize = 64;
//    private final int[][] matrix;
//    private final int width;
//    private final int height;
    private Game game;

//    public PathMatrix2(Game game) {
//        this.game = game;
//        this.height = game.getHeight();
//        this.width = game.getWidth();
//        this.matrix = new int[this.width * this.height][this.width * this.height];
//
//        for (int i = 0; i < this.width * this.height; i++) {
//            for (int j = 0; j < this.width * this.height; j++) {
//                this.matrix[i][j] = -2;
//            }
//        }
//    }
//
//    private boolean isOut(int x, int y) {
//        return 0 > x || 0 > y || x >= this.width || y >= this.height;
//    }
//
//    /**
//     * -1 = there is no path
//     * -2 = path is not explored yet
//     */
//    public int getMoveNumber(int xs, int ys, int xe, int ye) {
//        if (isOut(xs, ys) || isOut(xe, ye)) {
//            return -1;
//        }
//        int coords = xs + ys * this.height;
//        int coorde = xe + ye * this.height;
//        return this.matrix[coords][coorde];
//    }
//
//    public void setMoveNumber(int xs, int ys, int xe, int ye, int moveNumber) {
//        if (isOut(xs, ys) || isOut(xe, ye)) {
//            return;
//        }
//        int coords = xs + ys * this.height;
//        int coorde = xe + ye * this.height;
//        this.matrix[coords][coorde] = moveNumber;
//    }
//
//    public static int encode(int distance, int move1, int move2) {
//        return distance << 6 | move1 << 3 | move2;
//    }
//
//    public static int encode(Vector vector) {
//        return encode(vector.getDistance(), vector.getMove1(), vector.getMove2());
//    }
//
//    public static Vector decode(int number) {
//        if (number == -1) {
//            throw new RuntimeException("VECTOR232");
//        }
//        return new Vector(number >> 6, (number >> 3) & 7, number & 7);
//    }
//
//    public void swap(int xs, int ys, int xe, int ye) {
//        int oldNr = getMoveNumber(xs, ys, xe, ye);
//        Vector vector = decode(oldNr);
//        if (vector.getMove2() != 0) {
//            int newNr = encode(vector.getDistance(), vector.getMove2(), vector.getMove1());
//            setMoveNumber(xs,ys,xe,ye,newNr);
//        }
//    }
//
//    public int findMoveNumber(int xs, int ys, int xe, int ye) {
//
//        if (!this.game.isSandOrLand(xs, ys) || !this.game.isSandOrLand(xe, ye)) {
//            return -1;
//        }
//
//        int cache = getMoveNumber(xs, ys, xe, ye);
//        if (cache != -2) {
//            return cache;
//        }
//
//        // keep track of places where have been
//        Queue<ExploreNode> queue = new ArrayDeque<>();
//        ExploreNode[][] map = new ExploreNode[this.width][this.height];
//        ExploreNode start = new ExploreNode(xe, ye, 0, 0);
//        map[xe][ye] = start;
//        queue.add(start);
//        while (!queue.isEmpty()) {
//
//            ExploreNode currentNode = queue.poll();
//            int x = currentNode.getX();
//            int y = currentNode.getY();
//            int distance = currentNode.getDistance();
//
////            System.out.println(currentNode);
//            for (Direction direction : Game.directions) {
//                int finalX = x - direction.getX();
//                int finalY = y - direction.getY();
//
//                if (this.game.isSandOrLand(finalX, finalY)) {
//
//                    int newDistance = distance + 1;
//                    int moveNumber = direction.getMoveNumber();
//
////                    System.out.println("coords " + finalX + " " + finalY);
//                    if (map[finalX][finalY] == null) { // haven't explored yet
//                        ExploreNode exploreNode = new ExploreNode(finalX, finalY, newDistance, moveNumber);
//                        map[finalX][finalY] = exploreNode;
//                        queue.add(exploreNode);
//                    } else { // update if distance is same
//
//                        ExploreNode exploreNode = map[finalX][finalY];
//                        if (exploreNode.getDistance() == newDistance) {
//                            exploreNode.setMove2(moveNumber);
//                        }
//                    }
//                }
//            }
//        }
//
//        // update matrix
//        for (int x = 0; x < this.width; x++) {
//            for (int y = 0; y < this.height; y++) {
//                ExploreNode exploreNode = map[x][y];
//                if (exploreNode != null) {
//                    int d = exploreNode.getDistance() << 6;
//                    int m1 = exploreNode.getMove1() << 3;
//                    int m2 = exploreNode.getMove2();
//                    int f = d | m1 | m2;
//                    setMoveNumber(x,y,xe,ye,f); // is path
//                } else {
//                    setMoveNumber(x,y,xe,ye,-1); // no path
//                }
//            }
//        }
//        return getMoveNumber(xs, ys, xe, ye);
//    }
//
//    public static void main(String[] args) {
//        int WIDTH = 128;
//        int HEIGHT = 128;
//
//
//        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                Terrain terrain1 = new Terrain(i, j, LAND);
//                terrain[i][j] = terrain1;
//            }
//        }
////        List<Player> players = new ArrayList<>();
//        List<Entity> entities = new ArrayList<>();
//        Game game = new Game(HEIGHT, WIDTH, terrain, entities);
////        PathMatrix pathMatrix = game.getPathMatrix();
////        long s = System.currentTimeMillis();
////        for (int i = 0; i < HEIGHT; i++) {
////            for (int j = 0; j < WIDTH; j++) {
////                pathMatrix.findMoveNumber(0,0, 0, j);
////            }
////        }
////        long e = System.currentTimeMillis();
////        System.out.println("time " + (e - s));
////        s = System.currentTimeMillis();
////        int a = 0;
////        for (int k = 0; k < HEIGHT; k++) {
////            for (int i = 0; i < HEIGHT; i++) {
////                for (int j = 0; j < WIDTH; j++) {
////                    a += pathMatrix.findMoveNumber(i,j,0,k);
////                }
////            }
////        }
////        System.out.println(a);
////        e = System.currentTimeMillis();
////        System.out.println("time " + (e - s));
//
//
////
//////        dnumber >> 4, mnumber & 15
//////        int nr = pathMatrix.findMoveNumber(0,0, 1,1);
//////        System.out.println("nr " + nr);
//////        int distance = nr >> 6;
//////        int move1 = (nr >> 3) & 7;
//////        int move2 = nr & 7;
//////        System.out.println(distance);
//////        System.out.println(move1);
//////        System.out.println(move2);
////        int m = 0;
////        for (int j = 0; j < 150; j++) {
////            for (int i = 0; i < 150; i++) {
////                int nr = pathMatrix.findMoveNumber(0,0, i,j);
////                int distance = nr >> 6;
////                int move1 = (nr >> 3) & 7;
////                int move2 = nr & 7;
////                if (distance + move1 + move2 > m) {
////                    m = distance + move1 + move2;
////                }
////            }
////        }
////
////        System.out.println(m);
////
//
//    }

}
