package de.jmoeller.turn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class TurnableEntity implements GameObject {

	/** The bitmaps that are visible to the user and main part of the game */
	private Bitmap mFaceUp;
	private Bitmap mFaceDown;
	private int mX;
	/** x and y position of the Entity. */
	private int mY;
	/** Size of the Entity */
	private int mSize;
	/** which face is currently displayed? */
	private boolean mIsFaceUp;
	/** Is the entity selcted? */
	private boolean mIsSelected;

	/**
	 * Loading the Images..
	 * 
	 * @param context
	 *            Context from the surface view (needed for resources).
	 */
	public TurnableEntity(Context context, int x, int y, int size,
			boolean isFaceUp) {
		mFaceUp = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.faceup);
		mFaceDown = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.facedown);
		this.mX = x;
		this.mY = y;
		this.mSize = size;
		this.mIsFaceUp = isFaceUp;
		this.mIsSelected = false;
	}

	@Override
	public void draw(Paint p, Canvas canvas) {
		Rect dest = new Rect(mX, mY, mX + mSize, mY + mSize);

		p.setFilterBitmap(true);
		if (this.mIsSelected) {
			p.setColor(Color.rgb(0, 153, 204));
			canvas.drawRect(dest, p);
		}
		if (this.mIsFaceUp) {
			canvas.drawBitmap(mFaceUp, null, dest, p);
		} else {
			canvas.drawBitmap(mFaceDown, null, dest, p);
		}

	}

	public void turn() {
		this.mIsFaceUp = !mIsFaceUp;
	}

	// GETTERS AND SETTERS...
	public int getX() {
		return mX;
	}

	public void setX(int x) {
		this.mX = x;
	}

	public int getY() {
		return mY;
	}

	public void setY(int y) {
		this.mY = y;
	}

	public Bitmap getFaceUp() {
		return mFaceUp;
	}

	public void setFaceUp(Bitmap faceUp) {
		this.mFaceUp = faceUp;
	}

	public Bitmap getFaceDown() {
		return mFaceDown;
	}

	public void setFaceDown(Bitmap faceDown) {
		this.mFaceDown = faceDown;
	}

	public boolean isSelected() {
		return mIsSelected;
	}

	public void setSelected(boolean isSelected) {
		this.mIsSelected = isSelected;
	}

	public boolean isFaceUp() {
		return mIsFaceUp;
	}

	public void setFaceUp(boolean isFaceUp) {
		this.mIsFaceUp = isFaceUp;
	}
	// END OF GETTERS AND SETTERS.

}
