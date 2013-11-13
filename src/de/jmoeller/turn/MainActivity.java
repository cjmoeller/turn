package de.jmoeller.turn;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private GameSurface mGame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		RelativeLayout relLayout = (RelativeLayout) findViewById(R.id.relLayout1);

		Button turnBtn = (Button) findViewById(R.id.button1);
		TextView level = (TextView) findViewById(R.id.textViewLevel);
		level.setText(level.getText().toString() + "1");
		TextView moves = (TextView) findViewById(R.id.textView2);
		TextView selectable = (TextView) findViewById(R.id.textViewSelects);
		moves.setText(moves.getText().toString() + "0");
		mGame = new GameSurface(this, level, moves, selectable);
		relLayout.addView(mGame);
		level.bringToFront();
		turnBtn.bringToFront();
		moves.bringToFront();
		selectable.bringToFront();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onTurn(View source) {
		mGame.onTurn();
	}

}
