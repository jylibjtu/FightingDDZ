package junyi.ddz.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import junyi.ddz.adapter.Log4jAdapter;
import junyi.ddz.gamer.AIGamer;
import junyi.ddz.gamer.Gamer;
import junyi.ddz.iterator.Container;
import junyi.ddz.iterator.Iterator;
import junyi.ddz.observer.Observer;
import junyi.ddz.observer.ScoreRecord;
import junyi.ddz.state.NormalState;
import junyi.ddz.state.OutingState;
import junyi.ddz.strategy.KillerStrategy;
import junyi.ddz.util.GameTimer;
import junyi.ddz.util.SoundPlayer;
import junyi.ddz.util.SystemHandler;
import junyi.ddz.view.MainWindow;

public class Game implements Container , Runnable{
	
	private MainWindow view;
	public Gamer[] gamers = new Gamer[3];//对局者
	
	/**
	 * 观察者队列，包含计分器、日志记录器、UI界面响应器
	 */
	private List<Observer> observers = new ArrayList<Observer>();
	public static int bgpIndex = 1;
	public static int bgmIndex = 3;
	public List<Card> pairs;//一整副牌的每局游戏乱序
	public List<Card> list0 = new ArrayList<Card>(17);
	public List<Card> list1 = new ArrayList<Card>(17);
	public List<Card> list2 = new ArrayList<Card>(17);
	public List<Card> listLord = new ArrayList<Card>(3);
	public List<Card> lastCards;//上一套出到场上的牌
	public List<Card> lastCards0;//玩家0出到场上的牌
	public List<Card> lastCards1;//玩家1出到场上的牌
	public List<Card> lastCards2;//玩家2出到场上的牌
	public int winnerRole;//胜利阵营  0 农民 1地主
	public int lord;//地主下表 0~2
	public boolean initOver = false;//是否初始化结束（逻辑上的发牌等）
	public boolean isLordOver = false;//抢地主是否结束?
	public boolean isGameOver = false;//游戏是否结束？
	private Gamer onFocusGamer = null;//此时的轮转对象
	private Thread t_clock;//时钟线程
	private Thread t_back_sound;//背景音乐播放线程
	public final static Card []Pair = new Card[54];//一整副牌
	static{//初始化一副牌
		for(int i = 3; i <= 15; i++){//3 ~ A 2 所有牌
			int j = i - 3;
			for(int k = 0; k < 4; k++){//四种花色
				Pair[j * 4 + k] = new Card(i, ColorType.getTypeByInt(k));
			}
		}
		Pair[52] = new Card(Card.SKING, null);
		Pair[53] = new Card(Card.BKING, null);
	}
	
	/**
	 * 停止bgm线程播放音乐
	 */
	public void musicStop(){
		((SoundPlayer)t_back_sound).shut();
	}
	
	/**
	 * 停止时钟计时
	 */
	public void clockStop(){
		if(t_clock != null){
			((GameTimer)t_clock).shut();
		}
	}
	
	/**
	 * 切换背景音乐
	 */
	public void changeBGM(){
		musicStop();
		bgmIndex = (++bgmIndex) % 4;
		t_back_sound = new SoundPlayer(SoundPlayer.SOUND_ROOT_PATH + "bg" + bgmIndex + ".wav" , true);
		t_back_sound.start();
	}
	
	/**
	 * 缺省的玩家初始化
	 */
	public void gamerInit(){
		gamers[0] = new AIGamer(new NormalState());
		gamers[1] = new AIGamer(new OutingState());
		gamers[2] = new AIGamer(new NormalState());
		//设置成环
		gamers[0].setLast(gamers[2]);
		gamers[0].setNext(gamers[1]);
		gamers[1].setLast(gamers[0]);
		gamers[1].setNext(gamers[2]);
		gamers[2].setLast(gamers[1]);
		gamers[2].setNext(gamers[0]);
		//排序
		Collections.sort(list0);
		Collections.sort(list1);
		Collections.sort(list2);
		//发给玩家手牌
		gamers[0].setList(list0);
		gamers[1].setList(list1);
		gamers[2].setList(list2);
		for(int i = 0; i < 3; i++){
			gamers[i].setStrategy(new KillerStrategy(gamers[i]));
			gamers[i].setGame(this);
			gamers[i].getStrategy().scan();
		}
	}
	/**
	 * 游戏结束后的处理函数
	 */
	public void gameOver(){
		this.musicStop();
		gameOverNotify();
	}
	/**
	 * 游戏结束后对观察者们的通知函数
	 */
	public void gameOverNotify(){
		for(Observer o : observers){
			o.reactionToOver();
		}
	}
	/**
	 * 一个Game对象的初始化函数	
	 */
	public void init(){
		
		observers.add(new ScoreRecord(this));
		observers.add(new Log4jAdapter(this));
		observers.add(MainWindow.getWindow());
		
		t_back_sound = new SoundPlayer(SoundPlayer.SOUND_ROOT_PATH + "bg" + bgmIndex + ".wav" , true);
		
		
		pairs = SystemHandler.shuffle();
		for(int i = 0; i < 51; i++){
			if(i % 3 == 0){
				list0.add(pairs.get(i));
			} else if (i % 3 == 1){
				list1.add(pairs.get(i));
			} else{
				list2.add(pairs.get(i));
			}
		}
				
		for(int i = 51; i < 54; i++){//地主牌
			listLord.add(pairs.get(i));
		}
		gamerInit();
		
		
		
		
	}
	
	
	@Override
	public void run(){
		init();
		t_back_sound.start();
		initOver = true;
		Iterator iter = this.getIterator();
		int continueNo = 0;
		while(!view.initOver){
			try{
				Thread.sleep(50);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		do {
			onFocusGamer = (Gamer)iter.next();
			if(continueNo == 2){
				isLordOver = true;
				for(int i = 0; i < 3; i++){
					if(gamers[i] == onFocusGamer){
						gamers[i].setRole(1);
						lord = i;
						gamers[i].addLord(listLord);
						view.lordToGamer(i);
					} else {
						gamers[i].setRole(0);
					}
				}
				break;
			}
			if(onFocusGamer == gamers[1]){
				t_clock = new GameTimer(10 , this.view.timers[1]);
			} else if(onFocusGamer == gamers[0]){
				t_clock = new GameTimer(2 , this.view.timers[0]);
			} else if(onFocusGamer == gamers[2]){
				t_clock = new GameTimer(2 , this.view.timers[2]);
			}
			t_clock.start();
			view.focusOn(getGamerIndex(onFocusGamer));
			
			
			while(!((GameTimer)t_clock).overtime() && (!onFocusGamer.hasAction)){
				try{
					Thread.sleep(10);
				} catch (Exception e){
					
				}
				
			}
			if(onFocusGamer == gamers[1]){
				if(onFocusGamer.hasAction){
					if(onFocusGamer.getRole() == 1){
						lord = 1;
						isLordOver = true;
						gamers[0].setRole(0);
						gamers[2].setRole(0);
						new Thread(SoundPlayer.SOUND_ROOT_PATH + "qiangdizhu.wav").start();
					} else {
						continueNo++;
					}
					onFocusGamer.hasAction = false;
				} else {
					continueNo++;
					((GameTimer)t_clock).shut();
				}
			} else {
				if(((AIGamer)onFocusGamer).whetherLord()){
					for(int i = 0; i < 3; i++){
						if(gamers[i] == onFocusGamer){
							lord = i;
							gamers[i].setRole(1);
							gamers[i].addLord(listLord);
							view.lordToGamer(i);
							isLordOver = true;
							new Thread(SoundPlayer.SOUND_ROOT_PATH + "qiangdizhu.wav").start();
						} else {
							gamers[i].setRole(0);
							new Thread(SoundPlayer.SOUND_ROOT_PATH + "buqiang.wav").start();
						}
					}
				} else {
					continueNo++;
				}
			}
		} while(!isLordOver);
		onFocusGamer = null;
		for(int i = 0; i < gamers.length; i++){
			gamers[i].hasAction = false;
		}
		continueNo = 0;
		do{//出牌循环
			if(iter.hasNext()){
				onFocusGamer = (Gamer)iter.next();
				if(onFocusGamer == gamers[1]){
					t_clock = new GameTimer(30 , this.view.timers[1]);
				} else if(onFocusGamer == gamers[0]){
					t_clock = new GameTimer(2 , this.view.timers[0]);
				} else if(onFocusGamer == gamers[2]){
					t_clock = new GameTimer(2 , this.view.timers[2]);
				}
				
				int onFocus = getGamerIndex(onFocusGamer);
				t_clock.start();
				view.focusOn(onFocus);
				
				while(!((GameTimer)t_clock).overtime() && (!onFocusGamer.hasAction)){
					try{
						Thread.sleep(10);
					} catch (Exception e){
						
					}
				}
				
				if(onFocusGamer == gamers[1]){
					if(onFocusGamer.hasAction){
						if(lastCards1 == null){//没打
							continueNo++;
							new SoundPlayer(SoundPlayer.SOUND_ROOT_PATH + "buyao.wav").start();
						} else {//打了
							readGamer();
							lastCards = lastCards1;
							view.readGame(this);
							continueNo = 0;
							new SoundPlayer(new CardGroup(lastCards1).getWavPath()).start();
						}
						onFocusGamer.hasAction = false;
						((GameTimer)t_clock).shut();
					} else {
						if(lastCards == lastCards1 && continueNo == 2){
							CardGroup group = gamers[1].getStrategy().ownOut();
							view.cardsOut(1, view.transCardsToComponents(group.getCards()));
							lastCards1 = group.getCards();
							lastCards = lastCards1;
							this.readGamer();
							view.readGame(this);
							continueNo = 0;
							new SoundPlayer(group.getWavPath()).start();
							
						} else {
							lastCards1 = null;
							new SoundPlayer(SoundPlayer.SOUND_ROOT_PATH + "buyao.wav").start();
							continueNo++;
						}
						
					}
				} else {
					List<Card> cards;
					if(getGamerIndex(onFocusGamer) == 0){
						cards = lastCards0;
					} else {
						cards = lastCards2;
					}
					CardGroup group = null;
					if(lastCards == cards || lastCards == null){//own out
						group = ((AIGamer)onFocusGamer).ownOut();
					} else {
						group = ((AIGamer)onFocusGamer).attack(new CardGroup(lastCards));
					}
					
					if(group != null){//打了
						if(getGamerIndex(onFocusGamer) == 0){
							lastCards = group.getCards();
							lastCards0 = lastCards;
						} else if(getGamerIndex(onFocusGamer) == 2){
							lastCards = group.getCards();
							lastCards2 = lastCards;
						}
						readGamer();
						view.readGame(this);
						view.cardsOut(getGamerIndex(onFocusGamer), view.transCardsToComponents(group.getCards()));
						
						
						
						
						new SoundPlayer(group.getWavPath()).start();
						continueNo = 0;
						
						
					} else {
						if(getGamerIndex(onFocusGamer) == 0){
							lastCards0 = null;
						} else {
							lastCards2 = null;
						}
						new SoundPlayer(SoundPlayer.SOUND_ROOT_PATH + "buyao.wav").start();
						continueNo++;
					}
				}
			} else {
				break;
			}
			for(int i = 0; i < gamers.length; i++){
				if(gamers[i].getList().size() == 0){
					winnerRole = gamers[i].getRole();
					isGameOver = true;
				}
			}
			
		} while (!isGameOver);
		this.gameOver();
	}
	
	
	public Iterator getIterator(){
		return new GamerIterator();
	}
	/**
	 * 
	 * 迭代器内部类
	 * 用于迭代内部成员
	 */
	private class GamerIterator implements Iterator{
		public boolean hasNext(){
			return true;
		}
		public Object next(){
			if(onFocusGamer == null){
				if(!isLordOver){
					int ran = new Random().nextInt(3);
					onFocusGamer = gamers[ran];
					return onFocusGamer;
				} else {
					for(int i = 0; i < gamers.length; i++){
						if(gamers[i].getRole() == 1){
							return gamers[i];
						}
					}
				}
				
			}
			return onFocusGamer.getNext();
		}
	}
	
	public boolean isOver(){
		return isGameOver;
	}
	

	public void printCurrentInfo(){
		System.out.println("cards0 : " + new CardGroup(list0).toString());
		System.out.println("cards1 : " + new CardGroup(list1).toString());
		System.out.println("cards2 : " + new CardGroup(list2).toString());
		System.out.println("lord pairs : " + new CardGroup(listLord).toString());
		System.out.println("lastCards : " + new CardGroup(lastCards).toString());
		System.out.println("lastCards0 : " + new CardGroup(lastCards0).toString());
		System.out.println("lastCards1 : " + new CardGroup(lastCards1).toString());
		System.out.println("lastCards2 : " + new CardGroup(lastCards2).toString());
	} 

	public MainWindow getView() {
		return view;
	}



	public void setView(MainWindow view) {
		this.view = view;
	}

	public int getWinner() {
		return winnerRole;
	}

	public void setWinner(int winnerRole) {
		this.winnerRole = winnerRole;
	}
	
	public int getGamerIndex(Gamer gamer){
		int index = 0;
		for(int i = 0; i < gamers.length; i++){
			if(gamer == gamers[i]){
				index = i;
				break;
			}
		}
		return index;
	}
	
	public Gamer lastOutingGamer(){
		if(lastCards == lastCards0){
			return gamers[0];
		} else if(lastCards == lastCards1){
			return gamers[1];
		} else if(lastCards == lastCards2){
			return gamers[2];
		}
		return null;
	}

	public Gamer getOnFocusGamer() {
		return onFocusGamer;
	}
	
	public void readGamer(){
		this.list0 = gamers[0].getList();
		this.list1 = gamers[1].getList();
		this.list2 = gamers[2].getList();
	}
	public int getLord() {
		return lord;
	}
	
	
}
