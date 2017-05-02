/**
 * 
 */
package junyi.ddz.gamer;

import java.util.Collections;
import java.util.List;

import junyi.ddz.core.Card;
import junyi.ddz.core.CardGroup;
import junyi.ddz.core.CardType;
import junyi.ddz.core.Game;
import junyi.ddz.strategy.Strategy;




/**
 * @author junyi
 *
 */
public abstract class Gamer{
	public static final int LAND_OWNER = 1;
	public static final int FARMER = 0;
	protected Gamer last , next;
	protected Game game;
	protected int role;//1 地主 0 农民
	protected List<Card> list = null;//手里的牌
	private String allCards;
	protected Strategy strategy;
	
	public boolean hasAction = false;//针对抢地主或者是出牌，是否已经操作过了？
	
	public Gamer(){
		
	}
	
	private void readList(){
		allCards = "";
		if(list != null){
			for(Card card : list){
				allCards += card.toString() + " ";
			}
		}
		
	}
	
	public void addLord(List<Card> lord){
		list.addAll(lord);
		Collections.sort(list);
		strategy.scan();
		readList();
	}
	
	public Gamer(List<Card> list){
		this.list = list;
	}
	
	
	public boolean canOut(CardGroup group){
		if(group.getType() == CardType.T0){
			return false;
		}
		boolean allHas = true;
		for(int i = 0; i < group.getCards().size(); i++){
			Card c = group.getCards().get(i);
			boolean curHas = false;
			for(int j = 0; j < this.list.size(); j++){
				if(this.list.get(j) == c){
					curHas = true;
					break;
				}
			}
			allHas = allHas && curHas;
		}
		return allHas;
	}
	
	public boolean out(CardGroup group){
		for(int i = 0; i < group.getCards().size(); i++){
			for(int j = 0; j < list.size(); j++){
				if(list.get(j) == group.getCards().get(i)){
					list.remove(j);
					break;
				}
			}
		}
		Collections.sort(this.list);
		strategy.scan();
		return true;
	}
	
	public static int getHandEvaluate(List<Card> handList){//对于自己手牌好坏的评估
		int score = 0;
		for(Card card : handList){
			int value = card.getCValue();
			if(value <= 15){
				score += value;
			} else if(value == Card.SKING){
				score += 60;
			} else if(value == Card.BKING){
				score += 100;
			}
		}
		return score;
	}
	
	public void setGame(Game g){
		this.game = g;
	}
	
	public Gamer(int role , Gamer last , Gamer next , List<Card> list){
		this.role = role;
		this.last = last;
		this.next = next;
		this.list = list;
		this.allCards = new CardGroup(list).toString();
	}
	@Override
	public String toString(){
		return allCards;
	}
	
	
	public Gamer getLast() {
		return last;
	}

	public void setLast(Gamer last) {
		this.last = last;
	}

	public Gamer getNext() {
		return next;
	}

	public void setNext(Gamer next) {
		this.next = next;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public List<Card> getList() {
		return list;
	}

	public void setList(List<Card> list) {
		this.list = list;
		readList();
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	
	
}
