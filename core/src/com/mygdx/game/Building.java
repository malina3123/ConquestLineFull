package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Building {
    private Texture texture;
    private Texture neutralTexture;
    private Texture player1Texture;
    private Texture player2Texture;
    private Vector2 position;
    private Texture player1UnitTexture;
    private Texture player2UnitTexture;
    private int owner; // 0 - нейтральное, 1 - игрок 1, 2 - игрок 2

    public Building(String neutralTexturePath, String player1TexturePath, String player2TexturePath, String player1UnitTexturePath, String player2UnitTexturePath, float x, float y) {
        neutralTexture = new Texture(neutralTexturePath);
        player1Texture = new Texture(player1TexturePath);
        player2Texture = new Texture(player2TexturePath);
        player1UnitTexture = new Texture(player1UnitTexturePath);
        player2UnitTexture = new Texture(player2UnitTexturePath);
        texture = neutralTexture;
        position = new Vector2(x, y);
        owner = 0; // По умолчанию нейтральное
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Unit hireUnit(int owner) {
        if (this.owner == owner) {
            Texture unitTexture = (owner == 1) ? player1UnitTexture : player2UnitTexture;
            return new Unit(unitTexture, position.x, position.y, 3 * 32, owner);
        }
        return null;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
        switch (owner) {
            case 1:
                texture = player1Texture;
                break;
            case 2:
                texture = player2Texture;
                break;
            default:
                texture = neutralTexture;
                break;
        }
    }

    public void dispose() {
        neutralTexture.dispose();
        player1Texture.dispose();
        player2Texture.dispose();
        player1UnitTexture.dispose();
        player2UnitTexture.dispose();
    }
}
