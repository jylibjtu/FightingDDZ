package junyi.ddz.identify;

import java.util.List;

import junyi.ddz.core.Card;
import junyi.ddz.core.CardGroup;
import junyi.ddz.core.CardType;

public class TwoCardIdentifier extends TypeIdentifier {

	@Override
	public void identify(CardGroup group) {
		List<Card> list = group.getCards();
		if(list.size() == 2){
			if(list.get(0).getCValue() == list.get(1).getCValue()){
				group.setType(CardType.T2);
				group.setMainValue(list.get(0).getCValue());
				return ;
			} else if((list.get(0).getCValue() == Card.BKING && list.get(1).getCValue() == Card.SKING)
					|| (list.get(0).getCValue() == Card.SKING && list.get(1).getCValue() == Card.BKING)){
				group.setType(CardType.T4);
				group.setMainValue(Card.SKING);
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
