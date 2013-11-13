package de.jmoeller.turn;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ViewConstructor")
public class GameSurface extends SurfaceView implements Callback {
	private SurfaceHolder mHolder;
	private Canvas mCanvas;
	private Paint mPaint;
	private boolean mIsRunning;
	private ArrayList<TurnableEntity> mEntities;
	private ArrayList<GameObject> mGameObjects;
	private int mWidth = 0;
	private int mHeight = 0;
	private TurnLevel mCurrentGame;
	private TextView mTextViewLevel;
	private TextView mTextViewMoves;
	private TextView mTextViewSelectable;

	public GameSurface(Context context, TextView level, TextView moves,
			TextView selectable) {
		super(context);
		this.mTextViewLevel = level;
		this.mTextViewMoves = moves;
		this.mTextViewSelectable = selectable;
		init();
	}

	/**
	 * Initialization of the SurfaceView thing.
	 */
	public void init() {
		// SurfaceView Stuff
		mEntities = new ArrayList<TurnableEntity>();
		mGameObjects = new ArrayList<GameObject>();
		mHolder = this.getHolder();
		mHolder.addCallback(this);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
		mPaint.setColor(Color.WHITE);
		mIsRunning = true;
		// Main Gamecircle
		new Thread(new Runnable() {
			public void run() {
				while (mIsRunning) {
					draw(mCanvas);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
					}
				}

			}
		}).start();

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void surfaceCreated(SurfaceHolder holder) {

	}

	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	private boolean mFirstDraw = true;

	/**
	 * Here, the final magic happens.
	 */
	public void draw(Canvas c) {
		c = mHolder.lockCanvas();
		if (c != null) {
			if (mFirstDraw) {
				mHeight = c.getHeight();
				mWidth = c.getWidth();
				mCurrentGame = new TurnLevel(this.getContext(), this.mHeight);
				mCurrentGame.setScreenWidth(mWidth);
				// For the entities
				int y = (int) Math.round((double) (this.mHeight - mCurrentGame
						.getEntitySize()) / (double) 2);
				// Don't ask why it works only this way (not mEntities =
				// mCurrentGame.getInitialState(); )
				boolean[] randomShitArray = this.mCurrentGame
						.getInitialStateBoolean();
				for (int i = 0; i < mCurrentGame.getEntityCount(); i++) {
					mEntities.add(new TurnableEntity(this.getContext(), i
							* this.mCurrentGame.getEntitySize(), y,
							mCurrentGame.getEntitySize(), randomShitArray[i]));
				}
				mFirstDraw = false;
			}
			c.drawColor(Color.WHITE);
			for (TurnableEntity t : mEntities) {
				t.draw(mPaint, c);
			}
			for (GameObject g : mGameObjects) {
				g.draw(mPaint, c);
			}
			mHolder.unlockCanvasAndPost(c);
		}

	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			int markedCount = 0;
			for (TurnableEntity t : this.mEntities) {
				if (t.isSelected())
					markedCount++;
			}
			for (TurnableEntity t : this.mEntities) {
				if (event.getX() >= t.getX()
						&& event.getX() <= t.getX()
								+ this.mCurrentGame.getEntitySize()
						&& event.getY() >= t.getY()
						&& event.getY() <= t.getY()
								+ this.mCurrentGame.getEntitySize()) {
					if (markedCount < this.mCurrentGame.getEntitySelectCount())
						t.setSelected(!t.isSelected());
					else if (markedCount == this.mCurrentGame
							.getEntitySelectCount() && t.isSelected()) {
						t.setSelected(!t.isSelected());
					}
					markedCount = 0;
					for (TurnableEntity te : this.mEntities) {
						if (te.isSelected())
							markedCount++;
					}
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < this.mCurrentGame
							.getEntitySelectCount() - markedCount; i++) {
						sb.append("I");
					}
					this.mTextViewSelectable.setText(sb.toString());
				}

			}
		}
		return true;
	}
	
	/** Called when 'turn' is pressed. */
	public void onTurn() {
		int markedCount = 0;
		for (TurnableEntity t : this.mEntities) {
			if (t.isSelected())
				markedCount++;
		}
		if (markedCount == this.mCurrentGame.getEntitySelectCount()) {
			this.mTextViewMoves.setText(this.getResources().getString(
					R.string.label_moves)
					+ this.mCurrentGame.increaseMoveCount());
			for (TurnableEntity t : this.mEntities) {
				if (t.isSelected()) {
					t.turn();
					t.setSelected(false);
				}
			}
			boolean levelCleared = true;
			for (TurnableEntity t : this.mEntities) {
				if (!t.isFaceUp())
					levelCleared = false;
			}
			if (levelCleared) {
				this.mEntities = this.mCurrentGame.nextLevel();
				this.mTextViewLevel.setText(this.getResources().getString(
						R.string.label_level)
						+ this.mCurrentGame.getLevel());
				this.mTextViewMoves.setText(this.getResources().getString(
						R.string.label_moves)
						+ this.mCurrentGame.increaseMoveCount());
				Toast.makeText(getContext(), "You did it!", Toast.LENGTH_LONG)
						.show();
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < this.mCurrentGame.getEntitySelectCount(); i++) {
				sb.append("I");
			}
			this.mTextViewSelectable.setText(sb.toString());
		}

	}
}