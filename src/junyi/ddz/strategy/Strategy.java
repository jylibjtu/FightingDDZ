package junyi.ddz.strategy;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junyi.ddz.core.Card;
import junyi.ddz.core.CardGroup;
import junyi.ddz.core.Game;
import junyi.ddz.gamer.Gamer;

public abstract class Strategy {
	protected Gamer gamer;
	protected int N3to16_S_B[] = new int[15];
	protected List<Card> hands;
	protected List<CardGroup> booms = new ArrayList<CardGroup>();
	protected List<CardGroup> Flycores = new ArrayList<CardGroup>();
	protected List<CardGroup> dStraights = new ArrayList<CardGroup>();
	protected List<CardGroup> straights = new ArrayList<CardGroup>();
	protected List<CardGroup> tripples = new ArrayList<CardGroup>(); 
	protected List<CardGroup> doubles = new ArrayList<CardGroup>();
	protected List<CardGroup> singles = new ArrayList<CardGroup>();
	
	
	/**
	 * 
	 * @param host 出牌人
	 * @param group 待打的牌组
	 * @return 出的牌，如果打不了则为null
	 */
	public abstract CardGroup attack(CardGroup group);
	public abstract CardGroup ownOut();
	
	public abstract CardGroup recognize(CardGroup group);
	public abstract CardGroup ownRecognize();
	
	private List<Card> getN_M_VAL_Card(int n , int m){
		List<Card> cards = new ArrayList<Card>(n);
		for(int i = 0; i < hands.size(); i++){
			if(hands.get(i).getCValue() == m){
				for(int j = 0; j < n; j++){
					cards.add(hands.get(i + j));
				}
				break;
			}
		}
		return cards;
	}
	
	public Strategy(Gamer g){
		this.gamer = g;
	}
	
	
	public void clear(){
		booms = new ArrayList<CardGroup>();
		Flycores = new ArrayList<CardGroup>();
		dStraights = new ArrayList<CardGroup>();
		straights = new ArrayList<CardGroup>();
		tripples = new ArrayList<CardGroup>();
		doubles = new ArrayList<CardGroup>();
		singles = new ArrayList<CardGroup>();
	}
	
	public void scan(){
		try{
			hands = gamer.getList();
			clear();
			Arrays.fill(N3to16_S_B, 0);
			for(Card c : hands){
				int core = c.getCValue();
				if(core == Card.SKING){
					N3to16_S_B[13] = 1; 
				} else if (core == Card.BKING){
					 N3to16_S_B[14] = 1;
				} else {
					N3to16_S_B[core - 3]++;
				}
			}
			
			if(N3to16_S_B[13] == 1 && N3to16_S_B[14] == 1){
				List<Card> kingBoom = new ArrayList<Card>();
				kingBoom.add(Game.Pair[52]);
				kingBoom.add(Game.Pair[53]);
				booms.add(new CardGroup(kingBoom));
			}
			for(int i = 0; i < N3to16_S_B.length; i++){
				int core = i + 3;
				if(N3to16_S_B[i] == 1){
					if(i == 13){
						core = Card.SKING;
					} else if (i == 14){
						 core = Card.BKING;
					} else {}
					singles.add(new CardGroup(getN_M_VAL_Card(1, core)));
				} else if(N3to16_S_B[i] == 2){
					
					doubles.add(new CardGroup(getN_M_VAL_Card(2, core)));
				} else if(N3to16_S_B[i] == 3){
					tripples.add(new CardGroup(getN_M_VAL_Card(3, core)));
				} else if(N3to16_S_B[i] == 4){
					booms.add(new CardGroup(getN_M_VAL_Card(4, core)));
				}
			}
			/**
			 * 扫描连子，只存最长的
			 */
			for(int i = 0; i < N3to16_S_B.length - 3; ){
				while(N3to16_S_B[i] < 1 && i < N3to16_S_B.length - 3){
					i++;
				}
				if(i >= 8){//J开始，放弃顺子吧
					break;
				}
				int conti = 1;
				int j = i + 1;
				for(; j < N3to16_S_B.length - 3; j++){
					if(N3to16_S_B[j] >= 1){
						conti++;
					} else {
						if(conti >= 5){
							List<Card> straight = new ArrayList<Card>();
							for(int k = 0; k < conti; k++){
								straight.addAll(getN_M_VAL_Card(1, i + 3 + k));
							}
							Collections.sort(straight);
							straights.add(new CardGroup(straight));
						}
						i = j;
						conti = 1;
						break;
					}
				}
				if(conti >= 5){
					List<Card> straight = new ArrayList<Card>();
					for(int k = 0; k < conti; k++){
						straight.addAll(getN_M_VAL_Card(1, i + 3 + k));
					}
					Collections.sort(straight);
					straights.add(new CardGroup(straight));
					i = j;
				}
				
			}
			/**
			 * 扫描连对
			 */
			for(int i = 0; i < N3to16_S_B.length - 3; ){

				while(N3to16_S_B[i] < 2 && i < N3to16_S_B.length - 3){
					i++;
				}
				if(i >= 10){//K开始，放弃连对吧
					break;
				}
				int conti = 1;
				int j = i + 1;
				for(; j < N3to16_S_B.length - 3; j++){
					if(N3to16_S_B[j] >= 2){
						conti++;
					} else {
						if(conti >= 3){
							List<Card> dStraight = new ArrayList<Card>();
							for(int k = 0; k < conti; k++){
								dStraight.addAll(getN_M_VAL_Card(2, i + 3 + k));
							}
							Collections.sort(dStraight);
							dStraights.add(new CardGroup(dStraight));
						} 
						i = j;
						conti = 1;
						break;
					}
				}
				if(conti >= 3){
					List<Card> dStraight = new ArrayList<Card>();
					for(int k = 0; k < conti; k++){
						dStraight.addAll(getN_M_VAL_Card(2, i + 3 + k));
					}
					Collections.sort(dStraight);
					dStraights.add(new CardGroup(dStraight));
					i = j;
				}
			}
			/**
			 * 扫描核心飞机 （不带）
			 */
			
			for(int i = 0; i < N3to16_S_B.length - 3; ){
				while(N3to16_S_B[i] < 3 && i < N3to16_S_B.length - 3){
					i++;
				}
				if(i >= 11){//A开始，放弃飞机吧
					break;
				}
				int conti = 1;
				int j = i + 1;
				for(; j < N3to16_S_B.length - 3; j++){
					if(N3to16_S_B[j] >= 3){
						conti++;
					} else {
						if(conti >= 2){
							List<Card> dFly = new ArrayList<Card>();
							for(int k = 0; k < conti; k++){
								dFly.addAll(getN_M_VAL_Card(3, i + 3 + k));
							}
							Collections.sort(dFly);
							Flycores.add(new CardGroup(dFly));
						}
						i = j;
						conti = 1;
						break;
					}
				}
				if(conti >= 2){
					List<Card> dFly = new ArrayList<Card>();
					for(int k = 0; k < conti; k++){
						dFly.addAll(getN_M_VAL_Card(3, i + 3 + k));
					}
					Collections.sort(dFly);
					Flycores.add(new CardGroup(dFly));
					i = j;
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
}
