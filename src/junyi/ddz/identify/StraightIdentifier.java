package junyi.ddz.identify;

import java.util.List;

import junyi.ddz.core.Card;
import junyi.ddz.core.CardGroup;
import junyi.ddz.core.CardType;

public class StraightIdentifier extends TypeIdentifier {

	@Override
	public void identify(CardGroup group) {
		List<Card> list = group.getCards();
		if(list.size() >= 5){
			int continueLength = 1;
			for(int i = 1; i < list.size(); i++){
				if(list.get(i).getCValue() != 15 && list.get(i).getCValue() == list.get(i - 1).getCValue() + 1){
					continueLength++;
				}
			}
			if(continueLength == list.size()){
				group.setType(CardType.T123);
				group.setMainValue(list.get(0).getCValue());
				group.setLength(continueLength);
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
