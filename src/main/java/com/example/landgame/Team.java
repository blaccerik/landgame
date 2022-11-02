package com.example.landgame;

import com.example.landgame.enums.TeamColor;
import com.example.landgame.config.TeamConfig;
import com.example.landgame.objects.Building;
import com.example.landgame.objects.Farmer;
import com.example.landgame.objects.House;
import com.example.landgame.objects.Player;
import com.example.landgame.objects.Soldier;
import com.example.landgame.objects.Workshop;
import com.example.landgame.pathfinding.Direction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static com.example.landgame.Game.debug;
import static com.example.landgame.Game.directions;
import static com.example.landgame.Game.globalCheck;
import static com.example.landgame.config.Config.*;
import static com.example.landgame.enums.MoveType.BUILD;
import static com.example.landgame.enums.MoveType.ILLEGAL;

@Getter
public class Team {

    private final TeamColor teamColor;
    private int resourceTree = 0;
    private int resourceGold = 0;
    private int resourceStone = 0;
    private int playerSoldier = 0;
    private int playerFarmer = 0;
    private int buildingHouse = 0;
    private int buildingWorkshop = 0;
    private int enemyMoveWeight;
    private int houseMoveWeight;
    private int resourceMoveWeight;
    private final TeamConfig teamConfig;
    private final Random random;

    public final static Comparator<Direction> comparator = new Comparator<Direction>(){
        @Override
        public int compare(Direction o1, Direction o2) {
            if (o1.getScore() > o2.getScore()) {
                return -1;
            } else if (o1.getScore() < o2.getScore()) {
                return 1;
            } else if (o1.getMoveType().equals(ILLEGAL)) {
                return -1;
            } else if (o2.getMoveType().equals(ILLEGAL)) {
                return 1;
            }
            return 0;
        }
    };

    public Team(TeamColor teamColor, TeamConfig teamConfig) {
        this.teamColor = teamColor;
        this.enemyMoveWeight = teamConfig.getEnemyMoveWeight();
        this.houseMoveWeight = teamConfig.getHouseMoveWeight();
        this.resourceMoveWeight = teamConfig.getResourceMoveWeight();
        this.teamConfig = teamConfig;
        this.random = new Random();

    }

    public void changeWeights() {
        if (this.enemyMoveWeight != this.teamConfig.getMaxEnemyMoveWeight()) {
            this.enemyMoveWeight++;
        }
        if (this.houseMoveWeight != this.teamConfig.getMaxHouseMoveWeight()) {
            this.houseMoveWeight++;
        }
        if (this.resourceMoveWeight != this.teamConfig.getMaxResourceMoveWeight()) {
            this.resourceMoveWeight++;
        }
    }

    public String getScore() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.teamColor);
        stringBuilder.append(" F: ");
        stringBuilder.append(this.playerFarmer);
        stringBuilder.append(" S: ");
        stringBuilder.append(this.playerSoldier);
        stringBuilder.append(" T: ");
        stringBuilder.append(this.resourceTree);
        stringBuilder.append(" S: ");
        stringBuilder.append(this.resourceStone);
        stringBuilder.append(" G: ");
        stringBuilder.append(this.resourceGold);
        stringBuilder.append(" H: ");
        stringBuilder.append(this.buildingHouse);
        stringBuilder.append(" W: ");
        stringBuilder.append(this.buildingWorkshop);
        return stringBuilder.toString();
    }

    public Player createPlayer(int x, int y, Class<?> entityClass) {
        if (entityClass == Farmer.class) {
            addFarmer(1);
            if (this.teamColor.equals(TeamColor.BLUE)) {
                return new Farmer(x,y,farmerHealth + 2,this);
            }
            return new Farmer(x,y,farmerHealth,this);
        } else if (entityClass == Soldier.class) {
            addSoldier(1);
            return new Soldier(x,y,soldierHealth,this);
        }
        throw new RuntimeException("plalalla");
    }

    public Player createPlayer(int x, int y, Building building, long tick) {
        building.setLastSpawn(tick);
        return createPlayer(x,y,Farmer.class);
    }

    public Building createBuilding(int x, int y, Class<?> entityClass, long tick) {
        if (entityClass == House.class) {
            addHouse(1);
            addStone(-houseStoneCost);
            addTree(-houseTreeCost);
            return new House(x,y,houseHealth,this,tick);
        } else if (entityClass == Workshop.class) {
            addWorkshop(1);
            return new Workshop(x,y,workshopHealth,this,tick);
        }
        throw new RuntimeException("plalallrra");
    }

    public void addGold(int amount) {
        this.resourceGold += amount;
    }

    public void addStone(int amount) {
        this.resourceStone += amount;
    }

    public void addTree(int amount) {
        this.resourceTree += amount;
    }

    public void addFarmer(int amount) {
        this.playerFarmer += amount;
    }

    public void addSoldier(int amount) {
        this.playerSoldier += amount;
    }

    public void addHouse(int amount) {
        this.buildingHouse += amount;
    }

    public void addWorkshop(int amount) {
        this.buildingWorkshop += amount;
    }

    public float calculateScoreForMove(MoveStats enemy, MoveStats resource, MoveStats house) {
        if (debug) {
            System.out.println("enemy " + enemy.getScore() + " " + enemy);
            System.out.println("resor " + resource.getScore() + " " + resource);

        }

        return enemy.getScore() * this.enemyMoveWeight +
                resource.getScore() * this.resourceMoveWeight +
                house.getScore() * this.houseMoveWeight;
    }

    private boolean hasResourcesToBuildHouse() {
        return this.resourceTree >= houseTreeCost && this.resourceStone >= houseStoneCost;
    }

    private float generateDirectionWithScore(
            Direction direction,
            AllMoveStats enemyStats,
            AllMoveStats resourceStats,
            AllMoveStats houseStats
    ) {
        int moveNr = direction.getMoveNumber();
        return calculateScoreForMove(enemyStats.get(moveNr), resourceStats.get(moveNr), houseStats.get(moveNr));
    }

    public static int oppositeMove(int move) {
        switch (move) {
            case 1:
                return 3;
            case 2:
                return 4;
            case 3:
                return 1;
            case 4:
                return 2;
            default:
                return 0;
        }
    }
    public Direction filterMoves(Player player, List<Direction> moves) {
        moves.sort(comparator);


        // todo remove bad moves before
//        int badMove = oppositeMove(player.getLastMove());
//        moves.removeIf(p -> p.getMoveNumber() == badMove);
//        int size = moves.size();
//        moves.removeIf(p -> p.getMoveType().equals(ILLEGAL));
//        boolean hadIllegal = moves.size() != size;
        float score2 = moves.get(0).getScore();
        moves.removeIf(p -> p.getScore() != score2);
//        if (hadIllegal) {
//            for (Direction direction: moves) {
//                if (direction.getMoveNumber() == player.getLastMove()) {
//                    return direction;
//                }
//            }
//        }
//        Random random = new Random();
        return moves.get(this.random.nextInt(moves.size()));
    }

    public Direction getBestMove(
            Player player,
            boolean canBuild,
            List<Direction> legalMoves,
            AllMoveStats enemyStats,
            AllMoveStats houseStats,
            AllMoveStats resourceStats
    ) {
        int x = player.getX();
        int y = player.getY();


        // todo house should be away from enemies
        //  select best player for that job
        if (canBuild && hasResourcesToBuildHouse()) {
            // todo check if there is no building
            return new Direction(x, y, 0, null, BUILD, 0);
        }

        for (Direction direction: legalMoves) {
            float score = generateDirectionWithScore(
                    direction,
                    enemyStats,
                    resourceStats,
                    houseStats
            );
            direction.setScore(score);
        }
        return filterMoves(player, legalMoves);
    }
}
