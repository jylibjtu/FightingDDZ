package junyi.ddz.strategy;

import java.util.ArrayList;
import java.util.List;

import junyi.ddz.core.Card;
import junyi.ddz.core.CardGroup;
import junyi.ddz.core.CardType;
import junyi.ddz.gamer.Gamer;

public class KillerStrategy extends Strategy {
	/**
	 *策略：能打就打，最小成本打 
	 * 
	 */
	
	
	public boolean hasBoom(){
		return booms.size() > 0;
	}
	/**
	 * 从手牌中获取n张核心值为m的牌
	 * @param n
	 * @param m
	 * @return 返回这n张牌
	 */
	
	/**
	 * 在单牌对牌的队列中取 共 n 单/对
	 * @param n 取数
	 * @param m 1单 2对
	 * @return
	 */
	public List<Card> findN_MLenCardsInNormal(int n , int m){
		List<Card> card = null;
		int all = 0;
		for(int i = 0; i < N3to16_S_B.length; i++){
			if(N3to16_S_B[i] < 3){
				all += N3to16_S_B[i];
			}
		}
		if(all < n * m){//根本没那么多单双的牌
			return null;
		} else {
			if(m == 2){//找对
				if(this.doubles.size() >= n){
					card = new ArrayList<Card>(n * m);
					for(int i = 0; i < n; i++){
						card.addAll(doubles.get(i).getCards());
					}
				}
			} else {//找单
				card = new ArrayList<Card>(n * m);
				if(this.singles.size() >= n){//单就够
					for(int i = 0; i < n; i++){
						card.addAll(singles.get(i).getCards());
					}
				} else {//需要拆双
					int need = n - singles.size();
					for(int i = 0; i < singles.size(); i++){
						card.addAll(singles.get(i).getCards());
					}
					int left = need % 2;
					need /= 2;
					int j = 0;
					for(int i = 0; i < need; i++ , j++){
						card.addAll(doubles.get(i).getCards());
					}
					for(int i = 0; i < left; i++){
						card.add(doubles.get(j).getCards().get(i));
					}
					
				}
			}
		}
		return card;
	}
	
	public CardGroup recognize(CardGroup group){
		CardGroup out = null;
		switch(group.getType()){
		case T0:
			break;
		case T1:
			for(CardGroup g : this.singles){//单牌扫描
				if(g.biggerthan(group)){
					out = g;
					break;
				}
			}
			if(out == null){
				for(CardGroup g : this.doubles){//拆对
					if(g.getCards().get(0).getCValue() > group.getMainValue()){
						out = new CardGroup(new ArrayList<Card>());
						out.add(g.getCards().get(0));
						break;
					}
				}
				if(out == null){
					for(CardGroup g : this.tripples){//拆三
						if(g.getCards().get(0).getCValue() > group.getMainValue()){
							out = new CardGroup(new ArrayList<Card>());
							out.add(g.getCards().get(0));
							break;
						}
					}
				}
			}
			break;
		case T2:
			for(CardGroup g : this.doubles){//对扫描
				if(g.biggerthan(group)){
					out = g;
					break;
				}
			}
			if(out == null){
				for(CardGroup g : this.tripples){//拆三
					if(g.getCards().get(0).getCValue() > group.getMainValue()){
						List<Card> list = new ArrayList<Card>();
						list.add(g.getCards().get(0));
						list.add(g.getCards().get(1));
						out = new CardGroup(list);
						break;
					}
				}
			}
			break;
		case T3:
			for(CardGroup g : this.tripples){//扫描三
				if(g.biggerthan(group)){
					out = g;
					break;
				}
			}
			break;
		case T4:
			for(CardGroup g : this.booms){//对扫描
				if(g.biggerthan(group)){
					out = g;
					break;
				}
			}
			break;
		case T31:
			for(CardGroup g : this.tripples){//扫描三
				if(g.getMainValue() > group.getMainValue()){
					out = g;
					List<Card> add = null;
					if((add = findN_MLenCardsInNormal(1, 1)) != null){//能随便找一个单
						out.add(add);
					} else {
						out = null;
					}
					break;
				}
			}

			break;
		case T32:
			for(CardGroup g : this.tripples){//扫描三
				if(g.getMainValue() > group.getMainValue()){
					out = g;
					List<Card> add = null;
					if((add = findN_MLenCardsInNormal(1, 2)) != null){//能随便找一个单
						out.add(add);
					} else {
						out = null;
					}
					break;
				}
			}
			break;
		case T411:
			for(CardGroup g : this.booms){//对扫描
				if(g.biggerthan(group)){
					out = g;
					break;
				}
			}
			break;
		case T422:
			for(CardGroup g : this.booms){//对扫描
				if(g.biggerthan(group)){
					out = g;
					break;
				}
			}
			break;
		case T123:
			for(CardGroup g : this.straights){
				if(g.getLength() >= group.getLength() 
						&& g.getMainValue() + g.getLength() 
						> group.getMainValue() + group.getLength()){//能打
					List<Card> cards = new ArrayList<Card>();
					if(g.getMainValue() > group.getMainValue()){
						for(int i = 0; i < group.getLength(); i++){
							cards.add(g.getCards().get(i));
						}
					} else {
						int mainLess = group.getMainValue() - g.getMainValue();
						for(int i = 0; i < group.getLength(); i++){
							cards.add(g.getCards().get(i + mainLess + 1));
						}
					}
					out = new CardGroup(cards);
					break;
				}
			}
			break;
		case T1122:
			for(CardGroup g : this.doubles){
				if(g.getLength() >= group.getLength() 
						&& g.getMainValue() + g.getLength()
						> group.getMainValue() + group.getLength()){//能打
					List<Card> cards = new ArrayList<Card>();
					if(g.getMainValue() > group.getMainValue()){
						for(int i = 0; i < group.getLength(); i++){
							cards.add(g.getCards().get(2 * i));
							cards.add(g.getCards().get(2 * i + 1));
						}
					} else {
						int mainLess = group.getMainValue() - g.getMainValue();
						for(int i = 0; i < group.getLength(); i++){
							cards.add(g.getCards().get(2 * (i + mainLess + 1)));
							cards.add(g.getCards().get(2 * (i + mainLess + 1) + 1));
						}
					}
					out = new CardGroup(cards);
					break;
				}
			}
			break;
		case T111222:
			for(CardGroup g : this.tripples){
				if(g.getLength() >= group.getLength() 
						&& g.getMainValue() + g.getLength()
						> group.getMainValue() + group.getLength()){//能打
					List<Card> cards = new ArrayList<Card>();
					if(g.getMainValue() > group.getMainValue()){
						for(int i = 0; i < group.getLength(); i++){
							cards.add(g.getCards().get(3 * i));
							cards.add(g.getCards().get(3 * i + 1));
							cards.add(g.getCards().get(3 * i + 2));
						}
					} else {
						int mainLess = group.getMainValue() - g.getMainValue();
						for(int i = 0; i < group.getLength(); i++){
							cards.add(g.getCards().get(3 * (i + mainLess + 1)));
							cards.add(g.getCards().get(3 * (i + mainLess + 1) + 1));
							cards.add(g.getCards().get(3 * (i + mainLess + 1) + 2));
						}
					}
					out = new CardGroup(cards);
					break;
				}
			}
			break;
		case T11122234:
			for(CardGroup g : this.tripples){
				if(g.getLength() >= group.getLength() 
						&& g.getMainValue() + g.getLength()
						> group.getMainValue() + group.getLength()
						&& this.findN_MLenCardsInNormal(g.getLength(), 1) != null){//能打
					List<Card> cards = new ArrayList<Card>();
					if(g.getMainValue() > group.getMainValue()){
						for(int i = 0; i < group.getLength(); i++){
							cards.add(g.getCards().get(3 * i));
							cards.add(g.getCards().get(3 * i + 1));
							cards.add(g.getCards().get(3 * i + 2));
						}
					} else {
						int mainLess = group.getMainValue() - g.getMainValue();
						for(int i = 0; i < group.getLength(); i++){
							cards.add(g.getCards().get(3 * (i + mainLess + 1)));
							cards.add(g.getCards().get(3 * (i + mainLess + 1) + 1));
							cards.add(g.getCards().get(3 * (i + mainLess + 1) + 2));
						}
					}
					cards.addAll(findN_MLenCardsInNormal(g.getLength(), 1));
					out = new CardGroup(cards);
					break;
				}
			}
			break;
		case T1112223344:
			for(CardGroup g : this.tripples){
				if(g.getLength() >= group.getLength() 
						&& g.getMainValue() + g.getLength()
						> group.getMainValue() + group.getLength()
						&& this.findN_MLenCardsInNormal(g.getLength(), 2) != null){//能打
					List<Card> cards = new ArrayList<Card>();
					if(g.getMainValue() > group.getMainValue()){
						for(int i = 0; i < group.getLength(); i++){
							cards.add(g.getCards().get(3 * i));
							cards.add(g.getCards().get(3 * i + 1));
							cards.add(g.getCards().get(3 * i + 2));
						}
					} else {
						int mainLess = group.getMainValue() - g.getMainValue();
						for(int i = 0; i < group.getLength(); i++){
							cards.add(g.getCards().get(3 * (i + mainLess + 1)));
							cards.add(g.getCards().get(3 * (i + mainLess + 1) + 1));
							cards.add(g.getCards().get(3 * (i + mainLess + 1) + 2));
						}
					}
					cards.addAll(findN_MLenCardsInNormal(g.getLength(), 2));
					out = new CardGroup(cards);
					break;
				}
			}
			break;
		}
		if(out == null && group.getType() != CardType.T4 && this.booms.size() != 0){
			if(group.getCoreValueSum() >= 40){
				out = booms.get(0);
			}
		}
		return out;
	}
	public CardGroup ownRecognize(){
		CardGroup out = null;
		if(this.straights.size() != 0){
			out = straights.get(0);
		} else if(this.dStraights.size() != 0){
			out = dStraights.get(0);
		} else if(this.Flycores.size() != 0){
			out = Flycores.get(0);
			int len = out.getLength();
			List<Card> sub = new ArrayList<Card>();
			if(singles.size() >= len){
				for(int i = 0; i < len; i++){
					sub.addAll(singles.get(i).getCards());
				}
			} else if((len - singles.size()) / 2 <= doubles.size()){
				for(CardGroup group : singles){
					sub.addAll(group.getCards());
				}
				int dLen = len - singles.size();
				for(int i = 0; i < dLen / 2; i++){
					sub.addAll(doubles.get(i).getCards());
				}
				for(int i = 0; i < dLen % 2; i++){
					sub.add(doubles.get(dLen / 2).getCards().get(0));
				}
			} else {
				
			}
			out.add(sub);
		}  else if(this.singles.size() != 0){
			out = singles.get(0);
		} else if(this.tripples.size() != 0){
			out = tripples.get(0);
			if(doubles.size() > 0){
				List<Card> sub = doubles.get(0).getCards();
				out.add(sub);
			}
		} else if(this.doubles.size() != 0){
			out = doubles.get(0);
		} else if(this.booms.size() != 0){
			out = booms.get(0);
		}
		return out;
	}
	
	public KillerStrategy(Gamer g){
		super(g);
	}
	
	public CardGroup attack(CardGroup group) {
		CardGroup out = recognize(group);
		if(out != null && gamer.canOut(out)){
			gamer.out(out);
		}
		this.scan();
		return out;
	}
	
	public CardGroup ownOut() {
		CardGroup out = ownRecognize();
		if(out != null && gamer.canOut(out)){
			gamer.out(out);
		}
		this.scan();
		return out;
	}

}
