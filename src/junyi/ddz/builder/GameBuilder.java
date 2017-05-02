package junyi.ddz.builder;

import junyi.ddz.core.Game;

public abstract class GameBuilder {
	public static Game buildGame(){
		return new Game();
	}
}
