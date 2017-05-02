package junyi.ddz.identify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junyi.ddz.core.Card;
import junyi.ddz.core.CardGroup;
import junyi.ddz.core.CardType;

public class AircraftIdentifier extends TypeIdentifier {

	private static List<Integer> getTripleGroupList(List <Card> list){
		//从一个有序数组中获取所有三张（或以上）的牌值
		List<Integer> triple = new ArrayList<Integer>();
		for(int i = 0; i < list.size() - 2; i++){
			if(list.get(i).getCValue() != 15//2不可连
					&&
					list.get(i).getCValue() == list.get(i + 1).getCValue() 
					&& 
					list.get(i + 1).getCValue() == list.get(i + 2).getCValue()){
				if(!triple.contains(list.get(i).getCValue())){
					triple.add(list.get(i).getCValue());
				}
			}
		}
		return triple;
	}
	
//	private static boolean 
	
	@Override
	public void identify(CardGroup group) {
		List<Card> list = group.getCards();
		List<Integer> triple = getTripleGroupList(list);
		if((!triple.isEmpty()) && triple.size() >= 2){
			Collections.sort(triple);
			/**
			 * 统计333444666777888
			 * 最大连续数 maxContiLength（可能同时里面有两个连续，如上。则最大有效长度仍为3连）
			 * 最大开始数 maxStart 最大连续输的第一张牌值如  如上。则最大开始为6
			 * */
			int maxContiLength = 1;
			int maxStart = 0;
			int i , j;
			for(i = 0; i < triple.size(); ){
				int contiI = 1;
				for(j = i + 1; j < triple.size(); j++){
					if(triple.get(j) == triple.get(j - 1) + 1){
						contiI ++ ;
					} else {
						break;
					}
				}
				if(contiI > maxContiLength){
					maxContiLength = contiI;
					maxStart = triple.get(i);
				}
				i = j;
			}
			/**
			 * contiList
			 * 作用：存放一个牌组中最长的连续飞机的核心值
			 * 如triple中存放 3 5 6 7 9
			 * 则contiList中存放5 6 7
			 * */
			List<Integer> contiList = new ArrayList<Integer>(maxContiLength);
			for(int l = 0; l < maxContiLength; l++){
				contiList.add(l + maxStart);
			}
			Collections.sort(contiList);
			
			
			/**
			 * 三连数 >2 才称飞机
			 * n长飞机最多出 5n张牌
			 * maxContiLength连三长度
			 * list.size()出牌个数
			 * */
			if(maxContiLength >= 2 && //得够连续2个三
					maxContiLength * 5 >= list.size()){//得能带的出去,每个三最多管五张牌
				
				if(maxContiLength * 3 == list.size()){//飞机不带
					/**飞机不带判断模块
					 * 
					 * 有特别情况 333444555666
					 * 被识别为四飞机不带
					 * 实际可以识别为456飞机带3 3 3三个单
					 * */
					group.setType(CardType.T111222);
					group.setMainValue(maxStart);
					group.setLength(maxContiLength);
					return ;
				} else if(maxContiLength * 4 == list.size()){//飞机带单 
					/**飞机带单判断模块
					 * 
					 * 有特别情况： 333 444 555 666 7777 88 99
					 * 总共20张，在此被识别为34567飞机带78989五单
					 * 实际上还可以识别为3456飞机带77889977四双，没有识别成这样
					 * */
					
					group.setType(CardType.T11122234);
					group.setMainValue(maxStart);
					group.setLength(maxContiLength);
					return ;
				} else if(maxContiLength * 5 == list.size()){//飞机带对
					List<Card> oprtList = new ArrayList<Card>(10);//顶多四飞机带4对，20张牌，共8张对牌
					int threeOrderedCheck = 0;//上一个检查的contiList中值
					for(int ii = 0; ii < list.size();)
					{
						int jj = list.get(ii).getCValue();
						if(!contiList.contains(jj))
						{
							oprtList.add(list.get(ii));
							ii ++ ;
						} 
						else 
						{
							if(threeOrderedCheck < contiList.size() && jj == contiList.get(threeOrderedCheck)){
								ii += 3;
								threeOrderedCheck ++ ;
							} else 
							{
								oprtList.add(list.get(ii));
								ii ++ ;
							}
						}
					}//至此，去掉了连续飞机牌，剩下的牌存于oprtList
					
					/**
					 * 判断去掉连续飞机牌后的牌组：是否都是对子
					 */
					Collections.sort(oprtList);
					if((!oprtList.isEmpty()) && oprtList.size() % 2 == 0){
						boolean allDouble = true;
						for(int kk = 1; kk < oprtList.size(); i += 2){
							if(oprtList.get(kk).getCValue() != oprtList.get(kk - 1).getCValue()){
								allDouble = false;
								break;
							}
						}
						if(allDouble){
							group.setType(CardType.T1112223344);
							group.setMainValue(maxStart);
							group.setLength(maxContiLength);
							return ;
						}
					}
					
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
