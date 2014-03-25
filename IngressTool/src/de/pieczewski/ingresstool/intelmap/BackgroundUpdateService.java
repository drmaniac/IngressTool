package de.pieczewski.ingresstool.intelmap;

import java.util.TimerTask;

import android.os.Message;
import android.util.Log;
import de.pieczewski.ingresstool.intelmap.backgroundTasks.GameScoreTask;
import de.pieczewski.ingresstool.intelmap.backgroundTasks.PlayerStatTask;

public class BackgroundUpdateService extends TimerTask {
	
	private static final String TAG = BackgroundUpdateService.class.getName();

	private MapManagerEventHandler mapManagerEventHandler;
	private GameScoreTask gameScoreTask;
	private PlayerStatTask playerStatTask;
	
	public BackgroundUpdateService() {
		mapManagerEventHandler = new MapManagerEventHandler();
		gameScoreTask = new GameScoreTask();
		playerStatTask = new PlayerStatTask();
	}
	
	@Override
	public void run() {
		Log.v(TAG, "run update service");
		Message gameScoreMsg = mapManagerEventHandler.obtainMessage(MapManagerEvents.UPDATE_GAME_SCORE);
		gameScoreMsg.obj = gameScoreTask.loadGameScore();
		mapManagerEventHandler.sendMessage(gameScoreMsg);
		
		Message playerStatMsg = mapManagerEventHandler.obtainMessage(MapManagerEvents.UPDATE_PLAYER_STAT);
		playerStatMsg.obj = playerStatTask.loadPlayerStat();
		if(playerStatMsg.obj != null) {
			mapManagerEventHandler.sendMessage(playerStatMsg);
		}
	}

}
