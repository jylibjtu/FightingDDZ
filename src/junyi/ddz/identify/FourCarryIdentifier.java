package junyi.ddz.identify;

import java.util.List;

import junyi.ddz.core.Card;
import junyi.ddz.core.CardGroup;
import junyi.ddz.core.CardType;

public class FourCarryIdentifier extends TypeIdentifier {

	@Override
	public void identify(CardGroup group) {
		List<Card> list = group.getCards();
		int len = list.size();
		if(len == 6 || len == 8){
			switch(len){
			case 6:
				int i1 = list.get(0).getCValue();
				int i2 = list.get(1).getCValue();
				int i3 = list.get(2).getCValue();
				int i4 = list.get(3).getCValue();
				int i5 = list.get(4).getCValue();
				int i6 = list.get(5).getCValue();
				if(i1 == i2 && i2 == i3 && i3 == i4){
					//list[0] = list[1] = list[2]  说明四牌在前四张，带两张基值更大的
					group.setType(CardType.T411);
					group.setMainValue(i1);
					return ;
				}else if(i2 == i3 && i3 == i4 && i4 == i5){
					//说明四牌在中间，带一大牌一小牌
					group.setType(CardType.T411);
					group.setMainValue(i2);
					return ;
				}else if(i3 == i4 && i4 == i5 && i5 == i6){
					group.setType(CardType.T411);
					group.setMainValue(i3);
					return ;
				}
				break;
			case 8:
				int n1 = list.get(0).getCValue();
				int n2 = list.get(1).getCValue();
				int n3 = list.get(2).getCValue();
				int n4 = list.get(3).getCValue();
				int n5 = list.get(4).getCValue();
				int n6 = list.get(5).getCValue();
				int n7 = list.get(6).getCValue();
				int n8 = list.get(7).getCValue();
				if(n1 == n2 && n3 == n4 && n5 == n6 && n7 == n8){
					//四带二对，一定是四个对子，关键在于哪两个相邻对子在一起
					if(n2 == n3){
						//前四相同1234
						group.setType(CardType.T422);
						group.setMainValue(n1);
						return ;
					} else if(n4 == n5){
						//中四相同3456
						group.setType(CardType.T422);
						group.setMainValue(n3);
						return ;
					} else if(n6 == n7){
						//后四相同5678
						group.setType(CardType.T422);
						group.setMainValue(n5);
						return ;
					}
					
				}
				break;
				
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
