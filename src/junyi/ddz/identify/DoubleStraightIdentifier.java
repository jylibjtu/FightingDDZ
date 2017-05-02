package junyi.ddz.identify;

import java.util.List;

import junyi.ddz.core.Card;
import junyi.ddz.core.CardGroup;
import junyi.ddz.core.CardType;

public class DoubleStraightIdentifier extends TypeIdentifier {

	@Override
	public void identify(CardGroup group) {
		List<Card> list = group.getCards();
		if(list.size() >= 6 && list.size() % 2 == 0){
			int cValue = list.get(0).getCValue();
			int continueLength = 1;
			for(int i = 1; i < list.size(); i += 2){
				if(list.get(i).getCValue() != 15//2不可连 
						&& 
						list.get(i).getCValue() == list.get(i - 1).getCValue()//一对
						&& 
						list.get(i).getCValue() == cValue + 1){//递增的对
					continueLength++;
					cValue++;
				}
			}
			if(continueLength == list.size() / 2){
				group.setType(CardType.T1122);
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
