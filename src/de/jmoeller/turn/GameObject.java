package de.jmoeller.turn;

import android.graphics.Canvas;
import android.graphics.Paint;

/** Represents an ingame Object, that is visible to the user. */
public interface GameObject {
	/**
	 * The draw Method.
	 * 
	 * @param p
	 *            The paint.
	 * @param canvas
	 *            The canvas to draw on.
	 */
	public void draw(Paint p, Canvas canvas);
}
