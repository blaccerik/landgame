package com.example.landgame.config;

import lombok.Getter;

@Getter
public class TeamConfig {



    // Weights
    private final int enemyMoveWeight;
    private final int houseMoveWeight;
    private final int resourceMoveWeight;
    private final int maxEnemyMoveWeight;
    private final int maxHouseMoveWeight;
    private final int maxResourceMoveWeight;

    public TeamConfig(int enemyMoveWeight, int houseMoveWeight, int resourceMoveWeight, int maxEnemyMoveWeight, int maxHouseMoveWeight, int maxResourceMoveWeight) {
        this.enemyMoveWeight = enemyMoveWeight;
        this.houseMoveWeight = houseMoveWeight;
        this.resourceMoveWeight = resourceMoveWeight;
        this.maxEnemyMoveWeight = maxEnemyMoveWeight;
        this.maxHouseMoveWeight = maxHouseMoveWeight;
        this.maxResourceMoveWeight = maxResourceMoveWeight;
    }
}
