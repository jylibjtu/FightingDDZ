package junyi.ddz.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junyi.ddz.core.Card;
import junyi.ddz.core.CardGroup;
import junyi.ddz.core.Game;
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
import junyi.ddz.memoto.GameStatusMemoto;
import junyi.ddz.view.CardComponent;
import junyi.ddz.view.MainWindow;

public abstract class SystemHandler {
	private static Random r1 = new Random(System.currentTimeMillis());
	
	private static TypeIdentifier chainLessFive;//牌型识别链 少于5张
	private static TypeIdentifier chainMoreFive;//牌型识别链 大于等于5张
	static {
		//生成两道牌型判断链
		/**
		 * <5判断链
		 * 单牌->双牌->三张牌->四张牌
		 */
		TypeIdentifier t1 = new OneCardIdentifier();
		TypeIdentifier t2 = new TwoCardIdentifier();
		TypeIdentifier t3 = new ThreeCardIdentifier();
		TypeIdentifier t4 = new FourCardIdentifier();
		t1.setNext(t2);
		t2.setNext(t3);
		t3.setNext(t4);
		chainLessFive = t1;
		/**
		 * >=5判断链
		 * 三带二->四带->顺子->连对->飞机
		 */
		//chainMoreFive
		TypeIdentifier t3c2= new ThreeCarryTwoIdentifier();
		TypeIdentifier t4c= new FourCarryIdentifier();
		TypeIdentifier t123= new StraightIdentifier();
		TypeIdentifier t112233= new DoubleStraightIdentifier();
		TypeIdentifier t111222= new AircraftIdentifier();
		t3c2.setNext(t4c);
		t4c.setNext(t123);
		t123.setNext(t112233);
		t112233.setNext(t111222);
		chainMoreFive = t3c2;
	}
	
	/**
	 * 洗牌函数
	 * pairCopy 将享元对象卡组的引用打乱后的排列
	 * 
	 * @param pairCopy
	 */
	public static List<Card> shuffle(){
		List<Card> copyOfPair = new ArrayList<Card>(54);
		List<Card> pair = new ArrayList<Card>(54);
		for(Card card : Game.Pair){
			copyOfPair.add(card);
		}
		Random r2 = new Random(System.currentTimeMillis());
		while(copyOfPair.size() > 0){
			long ran = r1.nextInt() + r2.nextInt();
			int index = (int)Math.abs(ran % copyOfPair.size());
			Card c = copyOfPair.get(index);
			copyOfPair.remove(index);
			pair.add(c);
		}
		return pair;
		
	}
	
	public static void scrollBack(Game g , GameStatusMemoto mem){
		g.pairs = mem.getPairs();
		g.list0 = mem.getList0();
		g.list1 = mem.getList1();
		g.list2 = mem.getList2();
		g.lastCards = mem.getLastCards();
		g.lastCards0 = mem.getLastCards0();
		g.lastCards1 = mem.getLastCards1();
		g.lastCards2 = mem.getLastCards2();
	}
	/**
	 * 调用责任链识别某套牌的类型
	 */
	public static void identify(CardGroup group){
		if(group.getCards().size() < 5){
			chainLessFive.identify(group);
		} else {
			chainMoreFive.identify(group);
		}
	}
	
	/**
	 * 实现UI卡片组件的移动
	 * @param c 卡片组件
	 * @param from 初始位置（通常在调用时使用c.getLocation()）
	 * @param to 目标位置（通常使用静态定义好的位置序列中的某位置）
	 */
	public static void move(CardComponent c , Point from , Point to){
		if(to.x!=from.x){//不是上下移动
			double k=(1.0)*(to.y-from.y)/(to.x-from.x);
			double b=to.y-to.x*k;
			int flag=0;//判断向左还是向右移动步幅
			if(from.x<to.x)
				flag=20;
			else {
				flag=-20;
			}
			for(int i=from.x;Math.abs(i-to.x)>20;i+=flag)
			{
				double y=k*i+b;//这里主要用的数学中的线性函数
			
				c.setLocation(i,(int)y);
				MainWindow.getWindow().repaint();
				try {
					Thread.sleep(3); //延迟，可自己设置
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		//位置校准
		c.setLocation(to);
	}
}
