package junyi.ddz.observer;

import junyi.ddz.core.Game;
import junyi.ddz.util.PropertiesUtil;

public class ScoreRecord implements Observer{
	private static PropertiesUtil scoreRecord = new PropertiesUtil(PropertiesUtil.SCORE_FILE);
	public static int curScore = Integer.parseInt(scoreRecord.getProperty("score"));
	public static void storeRecord(){
		scoreRecord.setProper("score", curScore + "");
		scoreRecord.store();
	}
	private Game game = null;
	public ScoreRecord(Game g){
		game = g;
	}
	@Override
	public void reactionToOver(){
		if(game.winnerRole == game.gamers[1].getRole()){
			curScore += 200;
		} else {
			curScore -= 200;
		}
		storeRecord();
	}

}
