package com.example.landgame.visual;

import com.example.landgame.Team;
import com.example.landgame.map.Map;
import com.example.landgame.map.Terrain;
import com.example.landgame.objects.Building;
import com.example.landgame.objects.Entity;
import com.example.landgame.objects.Gold;
import com.example.landgame.objects.Player;
import com.example.landgame.objects.Stone;
import com.example.landgame.objects.Tree;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static com.example.landgame.MainApplication.TILE_SIZE;

public class Renderer {

    private final Canvas canvas;
    private final Text text;
    private final int height;
    private final int width;

    public Renderer(Canvas canvas, Text text, int height, int width) {
        this.canvas = canvas;
        this.text = text;
        this.height = height;
        this.width = width;
    }

    public void render(Map map) {
        GraphicsContext graphicsContext = this.canvas.getGraphicsContext2D();
        for (int i=0; i<this.width; i++) {
            for (int j=0; j<this.height; j++) {
                Terrain terrain = map.getTile(i,j);
                drawTile(graphicsContext, terrain);
            }
        }

    }

    public void upDateStats(Team[] teams) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < teams.length; i++) {
            s.append(teams[i].getScore());
            s.append("\n");
        }
        this.text.setText(s.toString());
    }

    /**
     * Player:
     * xx = team color
     * RR = role color
     * [][][][][]
     * []xxxxxx[]
     * []xxRRxx[]
     * []xxxxxx[]
     * [][][][][]
     *
     * Building:
     * xx = team color
     * RR = role color
     * []xxxxxx[]
     * xxxxRRxxxx
     * xxRRRRRRxx
     * xxxxRRxxxx
     * []xxxxxx[]
     *
     */
    private void drawTile(GraphicsContext graphicsContext, Terrain terrain) {
        Color color = Color.WHITE;
        switch (terrain.getTerrainType()) {
            case LAND:
                color = Color.FORESTGREEN;
                break;
            case SAND:
                color = Color.BEIGE;
                break;
            case WATER:
                color = Color.AQUA;
                break;
            case DEEP_WATER:
                color = Color.BLUE;
                break;
        }
        graphicsContext.setFill(color);
        graphicsContext.fillRect(terrain.getX() * TILE_SIZE, terrain.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);


        Entity entity = terrain.getEntity();
        Building building = terrain.getBuilding();
        if (building == null) {
            if (entity != null) {
                if (entity instanceof Player) {
                    switch (((Player) entity).getTeam().getTeamColor()) {
                        case BLUE:
                            color = Color.BLUEVIOLET;
                            break;
                        case RED:
                            color = Color.RED;
                            break;
                        case GREEN:
                            color = Color.LIME;
                    }
                    graphicsContext.setFill(color);
                    graphicsContext.fillRect(terrain.getX() * TILE_SIZE + 1, terrain.getY() * TILE_SIZE + 1, TILE_SIZE - 2, TILE_SIZE - 2);
                } else {
                    if (entity instanceof Stone) {
                        graphicsContext.setFill(Color.LIGHTGRAY);
                        graphicsContext.fillPolygon(
                                new double[]{terrain.getX() * TILE_SIZE, terrain.getX() * TILE_SIZE + 2.5, terrain.getX() * TILE_SIZE + 5},
                                new double[]{terrain.getY() * TILE_SIZE + 5, terrain.getY() * TILE_SIZE, terrain.getY() * TILE_SIZE + 5},
                                3);
                    } else if (entity instanceof Tree) {
                        graphicsContext.setFill(Color.GREENYELLOW);
                        graphicsContext.fillOval(terrain.getX() * TILE_SIZE, terrain.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                        graphicsContext.setFill(Color.BROWN);
                        graphicsContext.fillRect(terrain.getX() * TILE_SIZE + 1, terrain.getY() * TILE_SIZE + 3, 3, 2);

                    } else if (entity instanceof Gold) {
                        graphicsContext.setFill(Color.GOLD);
                        graphicsContext.fillPolygon(
                                new double[]{terrain.getX() * TILE_SIZE, terrain.getX() * TILE_SIZE + 2.5, terrain.getX() * TILE_SIZE + 5},
                                new double[]{terrain.getY() * TILE_SIZE + 5, terrain.getY() * TILE_SIZE, terrain.getY() * TILE_SIZE + 5},
                                3);
                    }
                }
            }
        } else {
            switch (building.getTeam().getTeamColor()) {
                case BLUE:
                    color = Color.BLUEVIOLET;
                    break;
                case RED:
                    color = Color.RED;
                    break;
                case GREEN:
                    color = Color.LIME;
            }
            graphicsContext.setFill(color);
            graphicsContext.setFill(Color.DARKGRAY);
            graphicsContext.fillOval(terrain.getX() * TILE_SIZE, terrain.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            graphicsContext.setFill(color);
            graphicsContext.fillRect(terrain.getX() * TILE_SIZE + 1, terrain.getY() * TILE_SIZE + 1, 3, 3);
        }

    }
}
