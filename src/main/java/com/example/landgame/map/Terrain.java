package com.example.landgame.map;

import com.example.landgame.enums.TerrainType;
import com.example.landgame.objects.Building;
import com.example.landgame.objects.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Terrain {

    private final int x;
    private final int y;
    private final TerrainType terrainType;
    private Entity entity;
    private Building building;
    private int test;


    public Terrain(int x, int y, TerrainType terrainType) {
        this.x = x;
        this.y = y;
        this.terrainType = terrainType;
        this.entity = null;
        this.building = null;
        this.test = 0;
    }

    public boolean isBlocked() {
        return this.entity == null &&
                this.building == null &&
                (this.terrainType.equals(TerrainType.SAND) || this.terrainType.equals(TerrainType.LAND));
    }
}
