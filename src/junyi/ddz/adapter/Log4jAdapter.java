package junyi.ddz.adapter;

import junyi.ddz.core.Game;
import junyi.ddz.gamer.Gamer;
import junyi.ddz.observer.Observer;
import junyi.ddz.observer.ScoreRecord;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Log4jAdapter implements GLogger , Observer {
	private Game game = null;
	private Logger logger;
	public Log4jAdapter(Game g){
		this.game = g;
		logger= Logger.getLogger(g.getClass());
		PropertyConfigurator.configure("config/log4j.properties");
	}
	public void logGameResult(boolean win , Gamer g0 , Gamer g2 , int currentScore){
		StringBuffer sbBuffer = new StringBuffer();
		if(win){
			sbBuffer.append("\n\tWin");
		} else {
			sbBuffer.append("\n\tFalse");
		}
		sbBuffer.append("\n\t游戏对象:" + g0 + " \n\t&\n\t " + g2);
		sbBuffer.append("\n\t玩家:" + game.gamers[1]);
		sbBuffer.append("\n\t当前积分" + currentScore + "\n");
		logger.info(sbBuffer);
	}
	
	
	@Override
	public void reactionToOver(){
		boolean win1 = false;
		if(game.winnerRole != game.gamers[1].getRole()){
			win1 = false;
		} else {
			win1 = true;
		}
		logGameResult(win1 , game.gamers[0] , game.gamers[2] , ScoreRecord.curScore);
	}
	public void setGame(Game g){
		game = g;
	}
}
