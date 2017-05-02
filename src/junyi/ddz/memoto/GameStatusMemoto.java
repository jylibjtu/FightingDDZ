package junyi.ddz.memoto;

import java.util.ArrayList;
import java.util.List;

import junyi.ddz.core.Card;
import junyi.ddz.core.Game;

public class GameStatusMemoto extends Memoto {
	private List<Card> pairs;//一整副牌的每局游戏乱序
	private List<Card> list0 = new ArrayList<Card>(17);
	private List<Card> list1 = new ArrayList<Card>(17);
	private List<Card> list2 = new ArrayList<Card>(17);
	private List<Card> listLord = new ArrayList<Card>(3);
	private List<Card> lastCards;//上一套出到场上的牌
	private List<Card> lastCards0;//玩家0出到场上的牌
	private List<Card> lastCards1;//玩家1出到场上的牌
	private List<Card> lastCards2;//玩家2出到场上的牌
	
	public void scrollBack(Game g){
		g.gamers[0].setList((g.list0 = this.getList0()));
		g.gamers[1].setList((g.list1 = this.getList1()));
		g.gamers[2].setList((g.list2 = this.getList2()));
		g.lastCards = this.getLastCards();
		g.lastCards0 = this.getLastCards0();
		g.lastCards1 = this.getLastCards1();
		g.lastCards2 = this.getLastCards2();
	}
	
	public void storeGameStatus(Game g){
		if(g.pairs == null){
			this.pairs = null;
		} else {
			this.pairs = new ArrayList<Card>();
			pairs.addAll(g.pairs);
		}
		
		if(g.list0 == null){
			this.list0 = null;
		} else {
			this.list0 = new ArrayList<Card>();
			list0.addAll(g.list0);
		}
		
		if(g.list1 == null){
			this.list1 = null;
		} else {
			this.list1 = new ArrayList<Card>();
			list1.addAll(g.list1);
		}
		
		if(g.list2 == null){
			this.list2 = null;
		} else {
			this.list2 = new ArrayList<Card>();
			list2.addAll(g.list2);
		}
		
		if(g.listLord == null){
			this.listLord = null;
		} else {
			this.listLord = new ArrayList<Card>();
			listLord.addAll(g.listLord);
		}
		
		if(g.lastCards == null){
			this.lastCards = null;
		} else {
			this.lastCards = new ArrayList<Card>();
			lastCards.addAll(g.lastCards);
		}
		
		if(g.lastCards0 == null){
			this.lastCards0 = null;
		} else {
			this.lastCards0 = new ArrayList<Card>();
			lastCards0.addAll(g.lastCards0);
		}
		
		if(g.lastCards1 == null){
			this.lastCards1 = null;
		} else {
			this.lastCards1 = new ArrayList<Card>();
			lastCards1.addAll(g.lastCards1);
		}
		
		if(g.lastCards2 == null){
			this.lastCards2 = null;
		} else {
			this.lastCards2 = new ArrayList<Card>();
			lastCards2.addAll(g.lastCards2);
		}
	}
	public List<Card> getPairs() {
		return pairs;
	}
	public void setPairs(List<Card> pairs) {
		this.pairs = pairs;
	}
	public List<Card> getList0() {
		return list0;
	}
	public void setList0(List<Card> list0) {
		this.list0 = list0;
	}
	public List<Card> getList1() {
		return list1;
	}
	public void setList1(List<Card> list1) {
		this.list1 = list1;
	}
	public List<Card> getList2() {
		return list2;
	}
	public void setList2(List<Card> list2) {
		this.list2 = list2;
	}
	public List<Card> getListLord() {
		return listLord;
	}
	public void setListLord(List<Card> listLord) {
		this.listLord = listLord;
	}
	public List<Card> getLastCards() {
		return lastCards;
	}
	public void setLastCards(List<Card> lastCards) {
		this.lastCards = lastCards;
	}
	public List<Card> getLastCards0() {
		return lastCards0;
	}
	public void setLastCards0(List<Card> lastCards0) {
		this.lastCards0 = lastCards0;
	}
	public List<Card> getLastCards1() {
		return lastCards1;
	}
	public void setLastCards1(List<Card> lastCards1) {
		this.lastCards1 = lastCards1;
	}
	public List<Card> getLastCards2() {
		return lastCards2;
	}
	public void setLastCards2(List<Card> lastCards2) {
		this.lastCards2 = lastCards2;
	}
	
	
}
