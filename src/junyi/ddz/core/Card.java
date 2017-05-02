package junyi.ddz.core;

import java.io.File;

public class Card implements Comparable<Card> {
	/*
	 * Card中有真实大小coreValue如3—K、A、2 大小王
	 * coreValue 从小到大 3-13  A->14 2->15 小王-99 大王-100
	 * */ 
	public static final int SKING = 99;
	public static final int BKING = 100;
	public static final String IMAGE_ROOT_PATH = "image" + File.separatorChar;
	private Integer coreValue;
	private ColorType color;
	public Card(int cardValue , ColorType colorType){
		coreValue = new Integer(cardValue);
		color = colorType;
	}
	public static boolean canContinue(Card c){//是否允许出连续牌---大于A则不行
		return c.getCValue() < 15;
	}
	
	public int getValue(){//用于获取用来比较的数值
		if(color == null){
			return coreValue;//大小王 100/99
		}
		return coreValue * 4 + color.ordinal();
	}
	
	@Override
	public int compareTo(Card c){
		if(this.getValue() == c.getValue()){
			return 0;
		}
		return this.getValue() < c.getValue() ? -1 : 1;
	}
	public int getCValue(){
		return coreValue;
	}
	@Override
	public String toString(){
		if(this.color != null){
			return this.coreValue + "-" + ColorType.getValue(this.color);
		} else {
			return this.coreValue + "";
		}
	}
	
	public String getImagePath(){
		return Card.IMAGE_ROOT_PATH + this.toString() + ".jpg";
	}
	
}
