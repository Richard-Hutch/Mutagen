package com.bpa;

import com.badlogic.gdx.graphics.OrthographicCamera;


public abstract class L1TMap {
	public abstract void render (OrthographicCamera camera);
	public abstract void update (float delta);
	public abstract void dispose();
	
/**
 * Gets a tile by pixel position within the game world at a specified layer.
 * Gets data for a specific tile and tells about it
 * @param layer
 * @param x
 * @param y
 * @return
 */
	public TileType getTileTypeByLocation(int layer, float x, float y) {
		return this.getTileTypeByCoordinate(layer,(int) (x/TileType.TILE_SIZE), (int)(y/TileType.TILE_SIZE));
	}

	
	
/**
 * Gets a tile at its coordinate within the map at a specified layer
 * @param layer
 * @param x
 * @param y
 * @return
 */
	
	public abstract TileType getTileTypeByCoordinate(int layer, int col, int row);
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract int getLayers();
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
