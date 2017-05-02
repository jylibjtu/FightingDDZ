package junyi.ddz.identify;

import java.util.List;

import junyi.ddz.core.Card;
import junyi.ddz.core.CardGroup;
import junyi.ddz.core.CardType;

public class ThreeCarryTwoIdentifier extends TypeIdentifier {

	@Override
	public void identify(CardGroup group) {
		List<Card> list = group.getCards();
		if(list.size() == 5){
			int i1 = list.get(0).getCValue();
			int i2 = list.get(1).getCValue();
			int i3 = list.get(2).getCValue();
			int i4 = list.get(3).getCValue();
			int i5 = list.get(4).getCValue();
			if(i1 == i2 && i4 == i5){
				//三带一对，一定有首位各两张同牌
				if(i2 == i3 || i3 == i4){
					//后三同还是前三同
					group.setType(CardType.T32);
					group.setMainValue(i3);
					return ;
				}
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
