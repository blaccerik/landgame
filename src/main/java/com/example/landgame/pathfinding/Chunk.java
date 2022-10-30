//package com.example.landgame.pathfinding;
//
//import com.example.landgame.Game;
//import com.example.landgame.PortalInfo;
//import com.example.landgame.gamelogic.Coords;
//import com.example.landgame.map.Terrain;
//import lombok.Getter;
//
//import java.util.ArrayDeque;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Queue;
//import java.util.TreeMap;
//
//import static com.example.landgame.pathfinding.PathMatrix.chunkBits;
//import static com.example.landgame.pathfinding.PathMatrix.chunkSize;
//
//@Getter
//public class Chunk {
//    private final int x;
//    private final int y;
//    private final int[][] matrix;
//    private final int[][] nearestPortal;
//    private final Terrain[][] terrain;
//    private final List<PortalInfo> portals1;
//    private final List<PortalInfo> portals2;
//    private final List<PortalInfo> portals3;
//    private final List<PortalInfo> portals4;
////    private final int[] travelPlanner;
//
//    public Chunk(int x, int y, Terrain[][] terrain) {
//        this.x = x;
//        this.y = y;
//        this.terrain = terrain;
//        this.matrix = new int[chunkSize * chunkSize][chunkSize * chunkSize];
//        for (int i = 0; i < chunkSize * chunkSize; i++) {
//            for (int j = 0; j < chunkSize * chunkSize; j++) {
//                this.matrix[i][j] = -2;
//            }
//        }
//        this.nearestPortal = new int[chunkSize][chunkSize];
//        this.portals1 = new ArrayList<>();
//        this.
//        this.portals2 = new ArrayList<>();
//        this.portals3 = new ArrayList<>();
//        this.portals4 = new ArrayList<>();
//    }
//
//    public static boolean isAtPortal(int x, int y) {
//        return x == 0 || x == chunkSize - 1 || y == 0 || y == chunkSize - 1;
//    }
//
////    public List<Coords> getAllPortals(int x, int y) {
////
////    }
//
//    public void removePortal(int x, int y) {
//        for (PortalInfo portal: this.portalInfos) {
//
//        }
//    }
//
//    public void setTerrainTile(int x, int y, Terrain terrain) {
//        this.terrain[x][y] = terrain;
//        terrain.setTest(terrain.getTest() + 1);
//    }
//
//
//    /**
//     * -1 = there is no path
//     * -2 = path is blocked by entity
//     * -3 = path is not explored yet
//     */
//    public int getMoveNumber(int xs, int ys, int xe, int ye) {
//        int coords = xs + ys * chunkSize;
//        int coorde = xe + ye * chunkSize;
//        return this.matrix[coords][coorde];
//    }
//
//    public void setMoveNumber(int xs, int ys, int xe, int ye, int moveNumber) {
//        int coords = xs + ys * chunkSize;
//        int coorde = xe + ye * chunkSize;
//        this.matrix[coords][coorde] = moveNumber;
//    }
//
//    private boolean isOut(int x, int y) {
//        return 0 > x || 0 > y || x >= chunkSize || y >= chunkSize;
//    }
//
//
//    /**
//     * Given start coords return all movable Portal coords
//     */
//    public List<Coords> getPortals(int x, int y) {
//        return new ArrayList<>();
//    }
//
//    private boolean isMovable(int x, int y) {
//        if (!isOut(x,y)) {
//            Terrain terrain = this.terrain[x][y];
//            switch (terrain.getTerrainType()) {
//                case WATER:
//                case DEEP_WATER:
//                    return false;
//                case SAND:
//                case LAND:
//                    return true;
//            }
//        }
//        return false;
//    }
//
//    public int findMoveNumber(int xs, int ys, int xe, int ye) {
//
//        int cache = getMoveNumber(xs, ys, xe, ye);
//        if (cache != -2) {
//            return cache;
//        }
//
//        // keep track of places where have been
//        Queue<ExploreNode> queue = new ArrayDeque<>();
//        ExploreNode[][] map = new ExploreNode[chunkSize][chunkSize];
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
//            for (Direction direction : Game.directions) {
//                int finalX = x - direction.getX();
//                int finalY = y - direction.getY();
//
//                if (isMovable(finalX, finalY)) {
//
//                    int newDistance = distance + 1;
//                    int moveNumber = direction.getMoveNumber();
//                    if (map[finalX][finalY] == null) { // haven't explored yet
//                        ExploreNode exploreNode = new ExploreNode(finalX, finalY, newDistance, moveNumber);
//                        map[finalX][finalY] = exploreNode;
//                        queue.add(exploreNode);
//                    } else { // update if distance is same
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
//        for (int x = 0; x < chunkSize; x++) {
//            for (int y = 0; y < chunkSize; y++) {
//                ExploreNode exploreNode = map[x][y];
//                if (exploreNode != null) {
//                    int f = PathMatrix.encode(exploreNode.getDistance(), exploreNode.getMove1(), exploreNode.getMove2());
//                    setMoveNumber(x,y,xe,ye,f); // is path
//                } else {
//                    setMoveNumber(x,y,xe,ye,-1); // no path
//                }
//            }
//        }
//        return getMoveNumber(xs, ys, xe, ye);
//    }
//}
