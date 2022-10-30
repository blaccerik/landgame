package com.example.landgame.objects;

import com.example.landgame.Team;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Queue;

@Getter
@Setter
public class Player extends Entity {

    protected final Team team;
    protected final int damageToPlayers;
    protected int lastMove;
//    protected final Queue<Integer> lastMoves;
//    protected final int lastMovesSize = 3;

    public Player(int x, int y, int health, Team team, int damageToPlayers) {
        super(x, y, health);
        this.team = team;
        this.damageToPlayers = damageToPlayers;
        this.lastMove = 0;
//        this.lastMoves = new ArrayDeque<>();
//        for (int i = 0; i < lastMovesSize; i++) {
//            this.lastMoves.add(0);
//        }
    }

    public void addMove(int move) {
        this.lastMove = move;
//        this.lastMoves.add(move);
//        this.lastMoves.poll();
    }

    @Override
    public boolean sameTeam(Player player) {
        return player.getTeam() == this.team;
    }

    public String toString() {
        return "Player: " + this.x + " " + this.y + " " + this.team;
    }
}
