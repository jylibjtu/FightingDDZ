package test;

import junyi.ddz.core.Game;
import junyi.ddz.gamer.Gamer;
import junyi.ddz.memoto.GameStatusMemoto;

public class MemotoTest {
	public static void main(String []args){
		Game g = new Game();
		g.init();
		System.out.println("初始");
		System.out.println();
		g.printCurrentInfo();
		GameStatusMemoto gsmem = new GameStatusMemoto();
		gsmem.storeGameStatus(g);//备忘当前game状态
		g.gamers[0].out(g.gamers[0].getStrategy().ownOut());
		g.gamers[1].out(g.gamers[1].getStrategy().ownOut());
		g.gamers[2].out(g.gamers[2].getStrategy().ownOut());
		g.readGamer();
		//出了各一套牌
		System.out.println("出了牌后：");
		System.out.println();
		g.printCurrentInfo();
		//状态
		gsmem.scrollBack(g);
		//回滚备忘
		g.readGamer();
		System.out.println("回滚：");
		System.out.println();
		g.printCurrentInfo();
	}
}
