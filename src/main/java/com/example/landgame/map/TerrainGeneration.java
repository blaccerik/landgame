package com.example.landgame.map;


import com.example.landgame.enums.TerrainType;
import com.example.landgame.objects.Gold;
import com.example.landgame.objects.Resource;
import com.example.landgame.objects.Stone;
import com.example.landgame.objects.Tree;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.landgame.config.Config.goldHealth;
import static com.example.landgame.config.Config.goldProbability;
import static com.example.landgame.config.Config.stoneHealth;
import static com.example.landgame.config.Config.stoneProbability;
import static com.example.landgame.config.Config.treeHealth;
import static com.example.landgame.config.Config.treeProbability;
import static com.example.landgame.pathfinding.PathMatrix.chunkSize;

@Getter
public class TerrainGeneration {
    private final int[] p;
    private final int height;
    private final int width;
    private final Random random;

    public TerrainGeneration(Random random, int width, int height) {
        this.width = width;
        this.height = height;
        this.random = random;
        List<Integer> range = IntStream.rangeClosed(0, 255)
                .boxed().collect(Collectors.toList());
        range.add(0);
        Collections.shuffle(range, this.random);
        this.p = range.stream().mapToInt(i->i).toArray();
    }


    public Terrain[][] generateTerrain() {


        Terrain[][] terrains = new Terrain[this.width][this.height];
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {

                double dx = (double) i / this.width;
                double dy = (double) j / this.height;
                int frequency = 3;
                double noise = noise((dx * frequency), (dy * frequency));

                TerrainType terrainType;
                if (noise < -0.20) {  // -0.21
                    terrainType = TerrainType.DEEP_WATER;
                } else if (noise < -0.15) {  // -0.08
                    terrainType = TerrainType.WATER;
                } else if (noise < -0.08) {  // 0.05
                    terrainType = TerrainType.SAND;
                } else {
                    terrainType = TerrainType.LAND;
                }

                Terrain terrain = new Terrain(i, j, terrainType);
                terrains[i][j] = terrain;
            }
        }
        return terrains;
    }

    public List<Resource> generateResources() {

        List<Resource> list = new ArrayList<>();
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {

                float f = this.random.nextFloat();

                // spawn resource
                if (f < stoneProbability) {
                    Resource resource = new Stone(i, j, stoneHealth);
                    list.add(resource);
                } else if (f < stoneProbability + goldProbability) {
                    Resource resource = new Gold(i, j, goldHealth);
                    list.add(resource);
                } else if (f < stoneProbability + goldProbability + treeProbability) {
                    Resource resource = new Tree(i, j, treeHealth);
                    list.add(resource);
                }
            }
        }
        return list;
    }

    private double noise(double x, double y){
        int xi = (int) Math.floor(x) & 255;
        int yi = (int) Math.floor(y) & 255;

        int g1 = p[p[xi] + yi];
        int g2 = p[p[xi + 1] + yi];
        int g3 = p[p[xi] + yi + 1];
        int g4 = p[p[xi + 1] + yi + 1];

        double xf = x - Math.floor(x);
        double yf = y - Math.floor(y);

        double d1 = grad(g1, xf, yf);
        double d2 = grad(g2, xf - 1, yf);
        double d3 = grad(g3, xf, yf - 1);
        double d4 = grad(g4, xf - 1, yf - 1);

        double u = fade(xf);
        double v = fade(yf);

        double x1Inter = lerp(u, d1, d2);
        double x2Inter = lerp(u, d3, d4);
        double yInter = lerp(v, x1Inter, x2Inter);

        return yInter;

    }

    private static double lerp(double amount, double left, double right) {
        return ((1 - amount) * left + amount * right);
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static double grad(int hash, double x, double y){
        switch(hash & 3){
            case 0: return x + y;
            case 1: return -x + y;
            case 2: return x - y;
            case 3: return -x - y;
            default: return 0;
        }
    }
}
