package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import java.util.List;

public class InputHandler extends InputAdapter {
    private OrthographicCamera camera;
    private Map map;
    private List<Unit> units;
    private Unit selectedUnit;
    private MyGdxGame game;
    private GameState gameState;

    public InputHandler(OrthographicCamera camera, Map map, List<Unit> units, MyGdxGame game, GameState gameState) {
        this.camera = camera;
        this.map = map;
        this.units = units;
        this.game = game;
        this.gameState = gameState;
        Gdx.input.setInputProcessor(this);
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos3D = new Vector3(screenX, screenY, 0);
        camera.unproject(touchPos3D);
        Vector2 touchPos = new Vector2(touchPos3D.x, touchPos3D.y);

        if (selectedUnit == null) {
            // Проверка нажатия на юнита текущего игрока
            for (Unit unit : units) {
                if (unit.getPosition().dst(touchPos) < 16 && unit.getOwner() == gameState.getCurrentPlayer()) {
                    selectedUnit = unit;
                    game.setHighlightedTiles(map.getMovableTiles(unit));
                    return true;
                }
            }
            // Проверка нажатия на здание для найма юнита
            for (Building building : map.getBuildings()) {
                if (building.getPosition().dst(touchPos) < 32) {
                    if (!map.isCellOccupied(building.getPosition())) {
                        Unit newUnit = building.hireUnit(gameState.getCurrentPlayer());
                        if (newUnit != null) {
                            units.add(newUnit);
                        }
                    }
                    return true;
                }
            }
        } else {
            // Перемещение выбранного юнита и захват здания
            if (selectedUnit.canMoveTo(touchPos.x, touchPos.y, map) && !map.isCellOccupied(touchPos)) {
                // Перемещение в центр тайла
                int tileSize = map.getTileSize();
                float targetX = (float) Math.floor(touchPos.x / tileSize) * tileSize;
                float targetY = (float) Math.floor(touchPos.y / tileSize) * tileSize;
                selectedUnit.moveTo(targetX, targetY);

                // Проверка на захват здания
                for (Building building : map.getBuildings()) {
                    if (building.getPosition().dst(selectedUnit.getPosition()) < 16) {
                        building.setOwner(selectedUnit.getOwner());
                    }
                }

                selectedUnit = null;
                game.clearHighlightedTiles();
                gameState.endTurn();
            } else {
                selectedUnit = null;
                game.clearHighlightedTiles();
            }
        }
        return false;
    }


    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnit = selectedUnit;
    }
}
