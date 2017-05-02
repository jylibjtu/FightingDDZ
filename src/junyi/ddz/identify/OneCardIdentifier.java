package junyi.ddz.identify;

import java.util.List;

import junyi.ddz.core.Card;
import junyi.ddz.core.CardGroup;
import junyi.ddz.core.CardType;

public class OneCardIdentifier extends TypeIdentifier {

	@Override
	public void identify(CardGroup group) {
		List<Card> list = group.getCards();
		if(list.size() == 1){
			group.setType(CardType.T1);
			group.setMainValue(list.get(0).getCValue());
			return ;
		}
		
		if(this.next != null){
			next.identify(group);
			return ;
		}
		
		group.setType(CardType.T0);
		return ;
	}

}
