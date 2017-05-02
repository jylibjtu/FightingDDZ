package junyi.ddz.identify;

import java.util.List;

import junyi.ddz.core.Card;
import junyi.ddz.core.CardGroup;
import junyi.ddz.core.CardType;

public class FourCardIdentifier extends TypeIdentifier {

	@Override
	public void identify(CardGroup group) {
		List<Card> list = group.getCards();
		if(list.size() == 4){//4张牌
			int v1 = list.get(0).getCValue();
			int v2 = list.get(1).getCValue();
			int v3 = list.get(2).getCValue();
			int v4 = list.get(3).getCValue();
			if(v1 == v2 && v2 == v3 && v3 == v4){
				group.setType(CardType.T4);
				group.setMainValue(v1);
				return ;
			} else if((v1 == v2 && v2 == v3 && v3 != v4) || 
					(v1 != v2 && v2 == v3 && v3 == v4)){
				group.setType(CardType.T31);
				group.setMainValue(v2);
				return ;
			} else {
				group.setType(CardType.T0);
				return ;
			}
		}
		if(this.next != null){//还有下一个链节
			next.identify(group);
			return ;
		}
		/*不是四张牌，也没有下一个*/
		group.setType(CardType.T0);
		return ;
	}

}
