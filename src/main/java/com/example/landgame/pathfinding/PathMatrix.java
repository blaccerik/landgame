package com.example.landgame.pathfinding;

import com.example.landgame.Game;
import com.example.landgame.gamelogic.Coords;
import com.example.landgame.map.Map;
import com.example.landgame.map.Terrain;
import com.example.landgame.objects.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import static com.example.landgame.enums.TerrainType.*;
import static com.example.landgame.pathfinding.PathMatrix.chunkSize;


@AllArgsConstructor
@Getter
@ToString
class ChunkNode {
    private final int x;
    private final int y;
    private final int score;
}

@Getter
@AllArgsConstructor
class MoveDirection {
    private final int x;
    private final int y;
}

@Setter
@Getter
public class PathMatrix {

    private final static MoveDirection[] moves = new MoveDirection[]{
            new MoveDirection(1,0),
            new MoveDirection(-1,0),
            new MoveDirection(0,1),
            new MoveDirection(0,-1)
    };

    private final static Comparator<ChunkNode> comparator = new Comparator<ChunkNode>() {
        @Override
        public int compare(ChunkNode o1, ChunkNode o2) {
            if (o1.getScore() > o2.getScore()) {
                return 1;
            } else if (o1.getScore() < o2.getScore()) {
                return -1;
            }
            return 0;
        }
    };
    public final static int chunkSize = 32;
    public final static int chunkBits = (int) (Math.log10(chunkSize) / Math.log10(2));
    private final Map map;
//    private final Chunk[][] chunks;
//    private final int[][] chunkPortalInfo;

    public PathMatrix(Map map) {
        this.map = map;
//        this.chunks = new Chunk[this.chunkNumber][this.chunkNumber];
//        this.chunkPortalInfo = new int[this.chunkNumber * this.chunkNumber][this.chunkNumber * this.chunkNumber];
//        for (int i = 0; i < this.chunkNumber; i++) {
//            for (int j = 0; j < this.chunkNumber; j++) {
//                this.chunks[i][j] = new Chunk(i,j,map);
//            }
//        }
        // todo update portals
    }


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

    public static int encode(int distance, int move1, int move2) {
        return distance << 6 | move1 << 3 | move2;
    }

    public static int encode(Vector vector) {
        return encode(vector.getDistance(), vector.getMove1(), vector.getMove2());
    }

    public static Vector decode(int number) {
        if (number == -1) {
            throw new RuntimeException("VECTOR232");
        }
        return new Vector(number >> 6, (number >> 3) & 7, number & 7);
    }

//    public void swap(int xs, int ys, int xe, int ye) {
//        int oldNr = getMoveNumber(xs, ys, xe, ye);
//        Vector vector = decode(oldNr);
//        if (vector.getMove2() != 0) {
//            int newNr = encode(vector.getDistance(), vector.getMove2(), vector.getMove1());
//            setMoveNumber(xs,ys,xe,ye,newNr);
//        }
//    }
//

    public static ChunkInfo getChunkInfo(int x, int y) {
        return new ChunkInfo(x >> chunkBits, y >> chunkBits, x & (chunkSize - 1), y & (chunkSize - 1));
    }

//    private boolean isLegalChunk(int x, int y) {
//        return 0 <= x && x < chunkNumber && 0 <= y && y < chunkNumber;
//    }

    private int calculateScore(int xs, int ys, int xe, int ye) {
        return Math.abs(xs - xe) + Math.abs(ys - ye);
    }

//    private int getPortalInfo(int xs, int ys, int xe, int ye) {
//        int coords = xs + ys * this.chunkNumber;
//        int coorde = xe + ye * this.chunkNumber;
//        return this.chunkPortalInfo[coords][coorde];
//    }

//    public List<Coords> getChunkPath(int xs, int ys, int xe, int ye) {
//        ChunkNode start = new ChunkNode(xs, ys, 0);
//        PriorityQueue<ChunkNode> queue = new PriorityQueue<>(comparator);
//        ChunkNode[][] cache = new ChunkNode[chunkNumber][chunkNumber];
//        cache[xs][ys] = start;
//        queue.add(start);
//
//        // find path
//        while (!queue.isEmpty()) {
//            ChunkNode chunkNode = queue.poll();
//            int x = chunkNode.getX();
//            int y = chunkNode.getY();
//            for (MoveDirection moveDirection: moves) {
//                int finalX = x + moveDirection.getX();
//                int finalY = y + moveDirection.getY();
//
//                if (finalX == xe && finalY == ye) {
//                    queue.clear();
//                    cache[finalX][finalY] = chunkNode;
//                    break;
//                } else if (isLegalChunk(finalX, finalY) && cache[finalX][finalY] == null) {
//                    // todo check if portal is clear
//                    int number = getPortalInfo(finalX, finalY, xe, ye);
//                    System.out.println(number);
//                    cache[finalX][finalY] = chunkNode;
//                    queue.add(new ChunkNode(finalX, finalY, calculateScore(finalX, finalY, xe, ye)));
//                }
//            }
//        }
//        // if no path
//        if (cache[xe][ye] == null) {
//            return new ArrayList<>();
//        }
//
//        // generate chunk coords
//        ArrayList<Coords> list = new ArrayList<>();
//        list.add(new Coords(xe, ye));
//        int x = xe;
//        int y = ye;
//        while (true) {
//            ChunkNode chunkNode = cache[x][y];
//            x = chunkNode.getX();
//            y = chunkNode.getY();
//            list.add(new Coords(x,y));
//            if (chunkNode.getScore() == 0) {
//                break;
//            }
//        }
//        return list;
//    }
//
//
//    private boolean isAtPortal(int xs, int ys, int xe, int ye) {
//
//    }

    /**
     * Find coords within start chunk to get to the nearest portal near end chunk
     */
    private Coords getNearestPortal(ChunkInfo start, ChunkInfo end) {
        int xdiff = start.getChunkX() - end.getChunkX();
        int ydiff = start.getChunkY() - end.getChunkY();
        if (ydiff > 0) {
            if (xdiff > 0) {
                return new Coords(0, 0);
            } else if (xdiff < 0) {
                return new Coords(chunkSize - 1, 0);
            } else {
                return new Coords(end.getX(), 0);
            }
        } else if (ydiff < 0) {
            if (xdiff > 0) {
                return new Coords(0, chunkSize - 1);
            } else if (xdiff < 0) {
                return new Coords(chunkSize - 1, chunkSize - 1);
            } else {
                return new Coords(end.getX(), chunkSize - 1);
            }
        } else {
            if (xdiff > 0) {
                return new Coords(0, end.getY());
            } else if (xdiff < 0) {
                return new Coords(chunkSize - 1, end.getY());
            } else {
                return new Coords(end.getX(), end.getY());
            }
        }
//        return null;
    }

//    public int findMoveNumber(int xs, int ys, int xe, int ye) {
//
//        // coords to chunk and their coords
//        ChunkInfo start = getChunkInfo(xs, ys);
//        ChunkInfo end = getChunkInfo(xe, ye);
//        System.out.println(start);
//        System.out.println(end);
//        // if exit is in same chunk then find path
//        if (start.getChunkX() == end.getChunkX() && start.getChunkY() == end.getChunkY()) {
//
//        } else {
//            Coords coords = getNearestPortal(start, end);
//            int f = this.chunks[start.getChunkX()][start.getChunkY()].findMoveNumber(
//                    start.getX(),
//                    start.getY(),
//                    coords.getX(),
//                    coords.getY()
//            );
//            // check if can walk to nearest coords
//            // if not then update them
//            System.out.println(coords);
//            System.out.println(decode(f));
////            // if not on the same chunk then go to the closest point in that chunk
////            List<Coords> list = getChunkPath(start.getChunkX(), start.getChunkY(), end.getChunkX(), end.getChunkY());
////            System.out.println(list);
//        }
//
//        return -1;
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

    public static void main(String[] args) {
        int WIDTH = 128 * 2;
        int HEIGHT = 128 * 2;


        Terrain[][] terrain = new Terrain[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Terrain terrain1 = new Terrain(i, j, LAND);
                terrain[i][j] = terrain1;
            }
        }
//        List<Player> players = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
//        Game game = new Game(HEIGHT, WIDTH, terrain, entities);
//        PathMatrix pathMatrix = game.getPathMatrix();
//        pathMatrix.findMoveNumber(70,75,131, 132);

//        long s = System.currentTimeMillis();
//        int a = 0;
//        for (int i = 0; i < HEIGHT; i++) {
//            for (int j = 0; j < WIDTH; j++) {
//                a += PathMatrix.getChunkInfo(i,j).getChunkX();
//            }
//        }
//        long e = System.currentTimeMillis();
//        System.out.println("time " + (e - s));
//        System.out.println(a);
    }
}
