package junyi.ddz.state;

import junyi.ddz.core.CardGroup;
import junyi.ddz.core.CardType;
import junyi.ddz.gamer.Gamer;

public class NormalState extends State {

	@Override
	public boolean whetherAttack(Gamer host , Gamer personFrom, CardGroup lastGroup) {
		if(host.getRole() == personFrom.getRole()){//出的牌太大，自己的盟友就不打了
			if(lastGroup.getType() == CardType.T1 ||
					lastGroup.getType() == CardType.T2){
				return lastGroup.getMainValue() <= 13;
			} else if(lastGroup.getType() == CardType.T3 || 
					lastGroup.getType() == CardType.T31 || 
					lastGroup.getType() == CardType.T32) {
				return lastGroup.getMainValue() <= 10;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

}
