package com.example.landgame;

import com.example.landgame.config.TeamConfig;
import com.example.landgame.enums.TeamColor;
import com.example.landgame.objects.Farmer;
import com.example.landgame.objects.Player;
import com.example.landgame.pathfinding.Direction;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.landgame.enums.MoveType.*;
import static com.example.landgame.enums.TeamColor.BLUE;
import static com.example.landgame.enums.TeamColor.RED;
import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    private static final TeamConfig teamConfig = new TeamConfig(
            1,
            1,
            1,
            1,
            1,
            1
    );

    @Test
    void filterMovesChoose1Best() {
        Team team = new Team(TeamColor.RED, teamConfig);
        Player player = team.createPlayer(70, 70, Farmer.class);

        Direction direction1 = new Direction(70, 71, 1, null, MOVE, 12);
        Direction direction2 = new Direction(71, 70, 2, null, MOVE, 10);
        Direction direction3 = new Direction(70, 69, 3, null, MOVE, 10);
        Direction direction4 = new Direction(69, 70, 4, null, MOVE, 10);

        List<Direction> list = new ArrayList<>();
        list.add(direction1);
        list.add(direction2);
        list.add(direction3);
        list.add(direction4);
        Direction direction = team.filterMoves(player, list);
        assertEquals(direction, direction1);
    }

    @Test
    void filterMovesChoose1BestFromMultible() {
        Team team = new Team(TeamColor.RED, teamConfig);
        Player player = team.createPlayer(70, 70, Farmer.class);

        Direction direction1 = new Direction(70, 71, 1, null, MOVE, 12);
        Direction direction2 = new Direction(71, 70, 2, null, MOVE, 12);
        Direction direction3 = new Direction(70, 69, 3, null, MOVE, 10);
        Direction direction4 = new Direction(69, 70, 4, null, MOVE, 10);

        List<Direction> list = new ArrayList<>();
        list.add(direction1);
        list.add(direction2);
        list.add(direction3);
        list.add(direction4);
        Direction direction = team.filterMoves(player, list);
        assertEquals(direction == direction1 || direction == direction2, true);
    }

    @Test
    void filterMovesChooseOtherIsIllegal() {
        Team team = new Team(TeamColor.RED, teamConfig);Player player = team.createPlayer(70, 70, Farmer.class);
        player.setLastMove(1);
        Direction direction1 = new Direction(70, 71, 1, null, ILLEGAL, 12);
        Direction direction2 = new Direction(71, 70, 2, null, MOVE, 12);
        Direction direction3 = new Direction(70, 69, 3, null, MOVE, 10);
        Direction direction4 = new Direction(69, 70, 4, null, MOVE, 10);

        List<Direction> list = new ArrayList<>();
        list.add(direction1);
        list.add(direction2);
        list.add(direction3);
        list.add(direction4);
        Direction direction = team.filterMoves(player, list);
        assertEquals(direction, direction2);
    }

    @Test
    void filterMovesSameScore() {
        Team team = new Team(TeamColor.RED, teamConfig);Player player = team.createPlayer(70, 70, Farmer.class);
        player.setLastMove(2);
        Direction direction1 = new Direction(70, 71, 1, null, MOVE, 10);
        Direction direction2 = new Direction(71, 70, 2, null, MOVE, 10);
        Direction direction3 = new Direction(70, 69, 3, null, MOVE, 10);
        Direction direction4 = new Direction(69, 70, 4, null, MOVE, 10);

        List<Direction> list = new ArrayList<>();
        list.add(direction1);
        list.add(direction2);
        list.add(direction3);
        list.add(direction4);
        int[] test = new int[4];
        for (int i = 0; i < 100; i++) {
            Direction direction = team.filterMoves(player, list);
            test[direction.getMoveNumber() - 1] = 1;
        }
        assertEquals(Arrays.stream(test).sum(), 3);
    }

    @Test
    void filterMoves1IllegalRandomMove() {
        Team team = new Team(TeamColor.RED, teamConfig);Player player = team.createPlayer(70, 70, Farmer.class);
        player.setLastMove(1);
        Direction direction1 = new Direction(70, 71, 1, null, ILLEGAL, 12);
        Direction direction2 = new Direction(71, 70, 2, null, MOVE, 10);
        Direction direction3 = new Direction(70, 69, 3, null, MOVE, 10);
        Direction direction4 = new Direction(69, 70, 4, null, MOVE, 10);

        List<Direction> list = new ArrayList<>();
        list.add(direction1);
        list.add(direction2);
        list.add(direction3);
        list.add(direction4);

        int[] test = new int[3];
        for (int i = 0; i < 100; i++) {
            Direction direction = team.filterMoves(player, list);
            test[direction.getMoveNumber() - 2] = 1;
        }
        assertEquals(Arrays.stream(test).sum(), 2);
    }

    @Test
    void filterMovesBestMoveLastMove() {
        Team team = new Team(TeamColor.RED, teamConfig);Player player = team.createPlayer(70, 70, Farmer.class);
        player.setLastMove(3);
        Direction direction1 = new Direction(70, 71, 1, null, MOVE, 12);
        Direction direction2 = new Direction(71, 70, 2, null, MOVE, 10);
        Direction direction3 = new Direction(70, 69, 3, null, MOVE, 14);
        Direction direction4 = new Direction(69, 70, 4, null, MOVE, 10);

        List<Direction> list = new ArrayList<>();
        list.add(direction1);
        list.add(direction2);
        list.add(direction3);
        list.add(direction4);
        Direction direction = team.filterMoves(player, list);
        assertEquals(direction, direction3);
    }

    @Test
    void filterMovesBestMoveAgainstLastMove() {
        Team team = new Team(TeamColor.RED, teamConfig);Player player = team.createPlayer(70, 70, Farmer.class);
        player.setLastMove(1);
        Direction direction1 = new Direction(70, 71, 1, null, MOVE, 12);
        Direction direction2 = new Direction(71, 70, 2, null, MOVE, 10);
        Direction direction3 = new Direction(70, 69, 3, null, MOVE, 14);
        Direction direction4 = new Direction(69, 70, 4, null, MOVE, 10);

        List<Direction> list = new ArrayList<>();
        list.add(direction1);
        list.add(direction2);
        list.add(direction3);
        list.add(direction4);
        Direction direction = team.filterMoves(player, list);
        assertEquals(direction, direction1);
    }

    @Test
    void filterMovesLastMoveHelps() {
        Team team = new Team(TeamColor.RED, teamConfig);Player player = team.createPlayer(70, 70, Farmer.class);
        player.setLastMove(2);
        Direction direction1 = new Direction(70, 71, 1, null, MOVE, 12);
        Direction direction2 = new Direction(71, 70, 2, null, MOVE, 12);
        Direction direction3 = new Direction(70, 69, 3, null, ILLEGAL, 14);
        Direction direction4 = new Direction(69, 70, 4, null, MOVE, 10);

        List<Direction> list = new ArrayList<>();
        list.add(direction1);
        list.add(direction2);
        list.add(direction3);
        list.add(direction4);
        Direction direction = team.filterMoves(player, list);
        assertEquals(direction, direction2);
    }

    @Test
    void filterMoves2BestMoves1IsAgainstLastMove() {
        Team team = new Team(TeamColor.RED, teamConfig);Player player = team.createPlayer(70, 70, Farmer.class);
        player.setLastMove(1);
        Direction direction1 = new Direction(70, 71, 1, null, MOVE, 12);
        Direction direction2 = new Direction(71, 70, 2, null, MOVE, 10);
        Direction direction3 = new Direction(70, 69, 3, null, MOVE, 14);
        Direction direction4 = new Direction(69, 70, 4, null, MOVE, 14);

        List<Direction> list = new ArrayList<>();
        list.add(direction1);
        list.add(direction2);
        list.add(direction3);
        list.add(direction4);
        Direction direction = team.filterMoves(player, list);
        assertEquals(direction, direction4);
    }

    @Test
    void filterMovesIfIsBlockedMoveDontMoveBack() {
        Team team = new Team(TeamColor.RED, teamConfig);Player player = team.createPlayer(70, 70, Farmer.class);
        player.setLastMove(3);
        Direction direction1 = new Direction(70, 71, 1, null, MOVE, 12);
        Direction direction2 = new Direction(71, 70, 2, null, MOVE, 10);
        Direction direction3 = new Direction(70, 69, 3, null, ILLEGAL, 14);
        Direction direction4 = new Direction(69, 70, 4, null, MOVE, 11);

        List<Direction> list = new ArrayList<>();
        list.add(direction1);
        list.add(direction2);
        list.add(direction3);
        list.add(direction4);
        Direction direction = team.filterMoves(player, list);
        assertEquals(direction, direction4);
    }

    @Test
    void filterMoves2BlockedWays() {
        Team team = new Team(TeamColor.RED, teamConfig);Player player = team.createPlayer(70, 70, Farmer.class);
        player.setLastMove(3);
        Direction direction1 = new Direction(70, 71, 1, null, ILLEGAL, 12);
        Direction direction2 = new Direction(71, 70, 2, null, ILLEGAL, 10);
        Direction direction3 = new Direction(70, 69, 3, null, MOVE, 8);
        Direction direction4 = new Direction(69, 70, 4, null, MOVE, 6);

        List<Direction> list = new ArrayList<>();
        list.add(direction1);
        list.add(direction2);
        list.add(direction3);
        list.add(direction4);
        Direction direction = team.filterMoves(player, list);
        assertEquals(direction, direction3);
    }

    @Test
    void filterMoves2BlockedWaysLastMove() {
        Team team = new Team(TeamColor.RED, teamConfig);Player player = team.createPlayer(70, 70, Farmer.class);
        player.setLastMove(1);
        Direction direction1 = new Direction(70, 71, 1, null, ILLEGAL, 12);
        Direction direction2 = new Direction(71, 70, 2, null, ILLEGAL, 10);
        Direction direction3 = new Direction(70, 69, 3, null, MOVE, 8);
        Direction direction4 = new Direction(69, 70, 4, null, MOVE, 6);

        List<Direction> list = new ArrayList<>();
        list.add(direction1);
        list.add(direction2);
        list.add(direction3);
        list.add(direction4);
        Direction direction = team.filterMoves(player, list);
        assertEquals(direction, direction4);
    }

    @Test
    void filterMovesDontGoBack() {
        Team team = new Team(TeamColor.RED, teamConfig);Player player = team.createPlayer(70, 70, Farmer.class);
        player.setLastMove(2);
        Direction direction1 = new Direction(70, 71, 1, null, MOVE, 8);
        Direction direction2 = new Direction(71, 70, 2, null, ILLEGAL, 12);
        Direction direction3 = new Direction(70, 69, 3, null, MOVE, 6);
        Direction direction4 = new Direction(69, 70, 4, null, MOVE, 10);

        List<Direction> list = new ArrayList<>();
        list.add(direction1);
        list.add(direction2);
        list.add(direction3);
        list.add(direction4);
        Direction direction = team.filterMoves(player, list);
        assertEquals(direction, direction1);
    }
}