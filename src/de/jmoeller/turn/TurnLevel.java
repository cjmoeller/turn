package de.jmoeller.turn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.util.Log;

public class TurnLevel {
	/** How many faces are in the Level? */
	private int mEntityCount = 0;
	/** How big is the screen? */
	private int mScreenWidth;
	/** How many Entities have to be selected? */
	private int mEntitySelectCount = 0;
	/** Level number. */
	private int mLevel = 1;
	/** Move count. */
	private int mMoveCount = 1;
	/** initial game state. */
	private boolean[] mInitialState;
	/** All the levels. */
	private ArrayList<String> mLevels;
	/** The context. */
	private Context mContext;
	/** The screen height. */
	private int mScreenHeight;


	public TurnLevel(Context context, int screenHeight) {
		this.mContext = context;
		this.mScreenHeight = screenHeight;
		mLevels = new ArrayList<String>();
		InputStream inputStream = null;
		try {
			inputStream = mContext.getAssets().open("levels.turn");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (inputStream != null) {
			InputStreamReader streamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(streamReader);

			String l;

			try {
				while ((l = bufferedReader.readLine()) != null) {
					mLevels.add(l);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadLevel(this.mLevel);
		
	}

	private void loadLevel(int number) {
		String level = mLevels.get(number - 1);
		char[] state = level.split(" ")[0].toCharArray();
		Log.i("Logging er ist beste",
				"Und hier dings, er ist:" + Arrays.toString(state));
		this.mEntityCount = state.length;
		this.mEntitySelectCount = Integer.valueOf(level.split(" ")[1]);
		this.mInitialState = new boolean[state.length];
		for (int i = 0; i < state.length; i++) {
			if (state[i] == '1')
				this.mInitialState[i] = true;
			else
				this.mInitialState[i] = false;
		}
	}

	public void setInitialState(boolean[] initialState) {
		this.mInitialState = initialState;
	}

	public int getMoveCount() {
		return mMoveCount;
	}

	public int increaseMoveCount() {
		return this.mMoveCount++;
	}

	public int getLevel() {
		return mLevel;
	}

	public void setLevel(int level) {
		this.mLevel = level;
	}

	public int getEntitySelectCount() {
		return mEntitySelectCount;
	}

	public void setEntitySelectCount(int entitySelectCount) {
		this.mEntitySelectCount = entitySelectCount;
	}

	public int getEntitySize() {
		return (int) Math.round((double) mScreenWidth / (double) mEntityCount);
	}

	public int getEntityCount() {
		return mEntityCount;
	}

	public void setEntityCount(int entityCount) {
		this.mEntityCount = entityCount;
	}

	public int getScreenWidth() {
		return mScreenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.mScreenWidth = screenWidth;
	}

	public boolean[] getInitialStateBoolean() {
		return mInitialState;
	}


	public ArrayList<TurnableEntity> nextLevel() {
		this.mLevel++;
		this.mMoveCount = 0;
		loadLevel(this.mLevel);
		ArrayList<TurnableEntity> newLevel = new ArrayList<TurnableEntity>();
		int y = (int) Math
				.round((double) (mScreenHeight - this.getEntitySize())
						/ (double) 2);

		for (int i = 0; i < this.getEntityCount(); i++) {
			newLevel.add(new TurnableEntity(mContext, i * this.getEntitySize(),
					y, this.getEntitySize(), this.mInitialState[i]));
		}
		return newLevel;
	}

}
