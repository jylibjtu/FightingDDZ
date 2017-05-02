package junyi.ddz.state;

import junyi.ddz.core.CardGroup;
import junyi.ddz.gamer.Gamer;

public class EndangeredState extends State {

	@Override
	public boolean whetherAttack(Gamer host , Gamer personFrom, CardGroup lastGroup) {
		if(host.getRole() == personFrom.getRole()){
			return false;
		} else {
			return true;
		}
	}

}
