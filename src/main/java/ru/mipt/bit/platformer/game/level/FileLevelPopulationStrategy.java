package ru.mipt.bit.platformer.game.level;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileLevelPopulationStrategy implements ILevelPopulationStrategy {

    private final String resourcePath;

    public FileLevelPopulationStrategy(String resourcePath) {
        this.resourcePath = Objects.requireNonNull(resourcePath);
    }

    @Override
    public LevelPopulation populate(TiledMapTileLayer groundLayer) {
        List<String> rows = readRows();
        if (rows.isEmpty()) {
            throw new IllegalStateException("Level description file is empty: " + resourcePath);
        }

        int mapHeight = groundLayer.getHeight();
        int mapWidth = groundLayer.getWidth();

        if (rows.size() != mapHeight) {
            throw new IllegalStateException(String.format(
                    "Level height mismatch: map=%d, file=%d", mapHeight, rows.size()));
        }

        List<GridPoint2> treeCoordinates = new ArrayList<>();
        GridPoint2 playerSpawn = null;

        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            String row = stripCarriageReturn(rows.get(rowIndex));
            if (row.length() != mapWidth) {
                throw new IllegalStateException(String.format(
                        "Level width mismatch at row %d: expected %d, was %d",
                        rowIndex, mapWidth, row.length()));
            }

            int mapY = rows.size() - 1 - rowIndex;
            for (int column = 0; column < row.length(); column++) {
                char cell = row.charAt(column);
                GridPoint2 tile = new GridPoint2(column, mapY);
                switch (cell) {
                    case 'T':
                        treeCoordinates.add(tile);
                        break;
                    case 'X':
                        if (playerSpawn != null) {
                            throw new IllegalStateException("Multiple player spawns found in " + resourcePath);
                        }
                        playerSpawn = tile;
                        break;
                    case '_':
                    case ' ':
                        break;
                    default:
                        throw new IllegalStateException(String.format(
                                "Unsupported cell '%s' at (%d, %d) in %s",
                                cell, column, rowIndex, resourcePath));
                }
            }
        }

        if (playerSpawn == null) {
            throw new IllegalStateException("Player spawn (X) not found in " + resourcePath);
        }

        return new LevelPopulation(playerSpawn, treeCoordinates);
    }

    private List<String> readRows() {
        InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new IllegalStateException("Failed to load level description: " + resourcePath);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read level description", e);
        }
    }

    private static String stripCarriageReturn(String value) {
        if (value.endsWith("\r")) {
            return value.substring(0, value.length() - 1);
        }
        return value;
    }
}
