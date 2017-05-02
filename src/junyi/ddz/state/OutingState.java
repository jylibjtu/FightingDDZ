package junyi.ddz.state;

import junyi.ddz.core.CardGroup;
import junyi.ddz.gamer.Gamer;

public class OutingState extends State {

	@Override
	public boolean whetherAttack(Gamer host , Gamer personFrom, CardGroup lastGroup) {
		return true;
	}

}
