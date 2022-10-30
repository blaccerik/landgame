package com.example.landgame.map;

import com.example.landgame.enums.TerrainType;
import com.example.landgame.objects.Building;
import com.example.landgame.objects.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Terrain2 {

    private final int x;
    private final int y;
    private final TerrainType terrainType;
    private Entity entity;
    private Building building;


    public Terrain2(int x, int y, TerrainType terrainType) {
        this.x = x;
        this.y = y;
        this.terrainType = terrainType;
        this.entity = null;
        this.building = null;
    }
}
