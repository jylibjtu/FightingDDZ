package junyi.ddz.state;


import junyi.ddz.core.CardGroup;
import junyi.ddz.gamer.Gamer;

public abstract class State {
	/**针对当前游戏对局状态确定 
	 * 	是否去打一套牌
	 * @param host 状态的拥有者
	 * @param personFrom 上一套牌是谁出的
	 * @param lastGroup 上一套牌的属性
	 * @return
	 */
	public abstract boolean whetherAttack(Gamer host , Gamer personFrom , CardGroup lastGroup);
}
