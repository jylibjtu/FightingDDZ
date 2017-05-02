package junyi.ddz.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;

import junyi.ddz.identify.AircraftIdentifier;
import junyi.ddz.identify.DoubleStraightIdentifier;
import junyi.ddz.identify.FourCardIdentifier;
import junyi.ddz.identify.FourCarryIdentifier;
import junyi.ddz.identify.OneCardIdentifier;
import junyi.ddz.identify.StraightIdentifier;
import junyi.ddz.identify.ThreeCardIdentifier;
import junyi.ddz.identify.ThreeCarryTwoIdentifier;
import junyi.ddz.identify.TwoCardIdentifier;
import junyi.ddz.identify.TypeIdentifier;
import junyi.ddz.util.SoundPlayer;
import junyi.ddz.util.SystemHandler;

//import org.junit.Test;
public class CardGroup implements Comparable<CardGroup> {
	private List<Card> cards = null;//一套牌的集合
	private int mainValue = 0;//主要值——连牌的最小牌coreValue值/ 单、双、三、炸牌的coreValue牌值
	private int length = 0;//套牌的连续长度，只有连个、连对、飞机有这个属性
	private CardType type;
	
	/**
	 * 计算卡牌核心值的∑，主要用于排序比较
	 * @return 核心值之和
	 */
	public int getCoreValueSum(){
		int n = 0;
		for(Card c : this.cards){
			n += c.getCValue();
		}
		return n;
	}
	
	/**
	 * 评估一套牌的权值
	 * @return 权值
	 */
	public int getValueAssessment(){
		if(this.type == CardType.T0){
			return 0;
		}
		int value = 0;
		int basic = 0;
		if(this.length != 0){
			switch(this.type){
			case T123: 
				basic = 5;
				break;
			case T1122:
				basic = 10;
				break;
			default:
				basic = 20;
				break;
			}
			value = basic * this.length + this.mainValue;
		} else {
			switch(this.type){
			case T1:
				value = this.mainValue;
				break;
			case T2:
				value = this.mainValue * 2;
				break;
			case T3:
			case T31:
			case T32:
				value = this.mainValue * 3;
				break;
			case T4:
				value = 100;
				break;
			case T411:
			case T422:
				value = 90;
				break;
			default:
				break;
			}
			
		}
		
		return value;
	}
	
	/**
	 * 为这一套牌添加几张牌后构成新牌，并检测类型
	 * @param listAdd
	 */
	public void add(List<Card> listAdd){
		this.cards.addAll(listAdd);
		SystemHandler.identify(this);
	}
	public void add(Card c){
		this.cards.add(c);
		SystemHandler.identify(this);
	} 
	
	@Override
	public int compareTo(CardGroup group){
		return this.getCoreValueSum() < group.getCoreValueSum() ? -1 : 1;
	}
	
	public CardGroup(List<Card> cardList){
		if(cardList != null){
			this.cards = new ArrayList<Card>();
			Iterator<Card> iter = cardList.iterator();
			while(iter.hasNext()){
				Card c = iter.next();
				cards.add(c);
			}
			Collections.sort(cards);
			SystemHandler.identify(this);
		} else {
			this.cards = new ArrayList<>();
			this.type = CardType.T0;
		}
		
	}
	
	
	/**
	 * 
	 * @param g1 
	 * @param g2
	 * @return g1 可以 压倒 g2吗？
	 */
	public boolean biggerthan(CardGroup g2){
		if(this.type != CardType.T0){
			if(this.getType() == g2.getType()){
				if(this.getLength() == g2.getLength()){
					return this.getMainValue() > g2.getMainValue();
				} else {
					return false;
				}
				
			} else {
				if(this.getType() == CardType.T4){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public Object clone(){
		List<Card> list = new ArrayList<Card>(20);
		for(Card c : this.cards){
			list.add(c);
		}
		CardGroup cGroup = new CardGroup(list);
		return cGroup;
	}
	
	public static void main(String []args){
		List<Card> cards = new ArrayList<Card>();
		cards.add(Game.Pair[0]);
		cards.add(Game.Pair[1]);
		cards.add(Game.Pair[2]);

		CardGroup group = new CardGroup(cards);
		System.out.println(group.getType() + "--" + group.getMainValue() + "-" + group.getLength());
		SoundPlayer soundPlayer = new SoundPlayer(group.getWavPath());
		//soundPlayer.run();
		soundPlayer.start();
		System.out.println("-----");
	}
	/**
	 * 获取卡组类型对应的音频文件路径
	 * @return 字符串路径
	 */
	public String getWavPath(){
		String s_type = new String();
		switch(type){
		case T1:
			s_type = "" + this.getMainValue();
			break;
		case T2:
			s_type = "dui" + this.getMainValue();
			break;
		case T3:
			s_type = "sange.wav" + " " + SoundPlayer.SOUND_ROOT_PATH + "" + this.getMainValue();
			break;
		case T4:
			if(this.getMainValue() == Card.SKING)
				s_type = "wangzha";
			else {
				s_type = "zhadan";
			}
			break;
		case T31:
			s_type = "sandaiyi";
			break;
		case T32:
			s_type = "sandaiyidui";
			break;
		case T411:
			s_type = "sidaier";
			break;
		case T422:
			s_type = "sidailiangdui";
			break;
		case T123:
			s_type = "shunzi";
			break;
		case T1122:
			s_type = "liandui";
			break;
		case T111222:
		case T11122234:
		case T1112223344:
			s_type = "feiji";
			break;
		case T0:
			s_type = "fail";
			break;
		}
		return SoundPlayer.SOUND_ROOT_PATH + s_type + ".wav";
	}
	
	
	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public int getMainValue() {
		return mainValue;
	}

	public void setMainValue(int mainValue) {
		this.mainValue = mainValue;
	}

	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}

	public CardType getType() {
		return type;
	}

	public void setType(CardType type) {
		this.type = type;
	}
	
	@Override
	public String toString(){
		StringBuffer s = new StringBuffer();
		for(Card c : this.cards){
			s.append(c.toString() + " ");
		}
		return s.toString();
	}
	
	
}
