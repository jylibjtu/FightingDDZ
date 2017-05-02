package junyi.ddz.identify;

import java.util.List;

import junyi.ddz.core.Card;
import junyi.ddz.core.CardGroup;
import junyi.ddz.core.CardType;

public class ThreeCardIdentifier extends TypeIdentifier {

	@Override
	public void identify(CardGroup group) {
		List<Card> list = group.getCards();
		if(list.size() == 3){
			if(list.get(0).getCValue() == list.get(1).getCValue() && list.get(1).getCValue() == list.get(2).getCValue()){
				group.setType(CardType.T3);
				group.setMainValue(list.get(0).getCValue());
				return ;
			} else {
				group.setType(CardType.T0);
				return ;
			}
		}
		if(this.next != null){
			next.identify(group);
			return ;
		}
		
		group.setType(CardType.T0);
		return ;
	}

}
