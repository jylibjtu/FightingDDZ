package junyi.ddz.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import junyi.ddz.builder.GameBuilder;
import junyi.ddz.core.Card;
import junyi.ddz.core.CardGroup;
import junyi.ddz.core.CardType;
import junyi.ddz.core.Game;
import junyi.ddz.gamer.AIGamer;
import junyi.ddz.observer.Observer;
import junyi.ddz.observer.ScoreRecord;
import junyi.ddz.util.SoundPlayer;
import junyi.ddz.util.SystemHandler;

public class MainWindow extends JFrame implements ActionListener , MouseListener , Observer{
	public static final long serialVersionUID = -1;
	
	private Game game;
	public TimerComponent []timers = new TimerComponent[3];
	
	private static Integer doneStart;
	
	private JLabel difficulty , score;
	public boolean initOver = false;
	private List<CardComponent> pairComponent = null;
	private List<CardComponent> cards0 = null;
	private List<CardComponent> cards1 = null;
	private List<CardComponent> cards2 = null;
	private List<CardComponent> lastOut = null;
	private List<CardComponent> lastOut0 = null;
	private List<CardComponent> lastOut1 = null;
	private List<CardComponent> lastOut2 = null;
	private List<CardComponent> lord = null;
	
	private JPanel info;//存放难度和分数
	private JMenuItem restart , exit , about , changeMusic;// 定义菜单按钮
	private JMenuItem level1 , level2;//定义困难级别
	public Container container = null;//主界面的内容容器
	private JButton landlord[] = new JButton[2];//抢地主按钮
	private JButton publishCard[] = new JButton[3];//出牌按钮

	public void setGame(Game g){
		return ;
	}
	
	
	public void lordToGamer(int n){
		if(n < 0 || n > 2){
			return ;
		}
		List<CardComponent> cards = null;
		switch(n){
		case 0:
			cards = cards0;
			break;
		case 1:
			cards = cards1;
			
			
			break;
		case 2:
			cards = cards2;
			break;
		}
		cards.addAll(lord);
		this.cardsReposition(n);
	}
	/**
	 * 用于重新开游戏
	 */
	public void clear(){
		if(pairComponent != null){
			for(CardComponent cc : pairComponent){
				cc.setVisible(false);
				container.remove(cc);
			}
			pairComponent.clear();
		}
		if(cards0 != null){
			for(CardComponent cc : cards0){
				cc.setVisible(false);
			}
			cards0.clear();
		}
		
		if(cards1 != null){
			for(CardComponent cc : cards1){
				cc.setVisible(false);
			}
			cards1.clear();
		}
		
		if(cards2 != null){
			for(CardComponent cc : cards2){
				cc.setVisible(false);
			}
			cards2.clear();
		}
		
		if(lastOut != null){
			for(CardComponent cc : lastOut){
				cc.setVisible(false);
			}
			lastOut.clear();
		}
		
		if(lastOut1 != null){
			for(CardComponent cc : lastOut1){
				cc.setVisible(false);
			}
			lastOut1.clear();
		}
		if(lastOut2 != null){
			for(CardComponent cc : lastOut2){
				cc.setVisible(false);
			}
			lastOut2.clear();
		}
		if(lord != null){
			for(CardComponent cc : lord){
				cc.setVisible(false);
			}
			lord.clear();
		}
		
		pairComponent = null;
		cards0 = null;
		cards1 = null;
		cards2 = null;
		lastOut = null;
		lastOut0 = null;
		lastOut1 = null;
		lastOut2 = null;
		lord = null;
	}
	
	private void init(){
		this.setTitle("Fighting doudizhu");
		this.setSize(800, 600);
		setResizable(false);
		setLocationRelativeTo(getOwner());
		
		this.SetMenu();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addMouseListener(this);
		
		//内容绝对布局
		container = this.getContentPane();
		container.setLayout(null);
		
		((JPanel)container).setOpaque(false);
		
		ImageIcon background = new ImageIcon("image" + File.separator + "background.jpg");
		background = new ImageIcon(background.getImage().getScaledInstance(800, 600, Image.SCALE_DEFAULT));
		JLabel backLabel = new JLabel(background);
		backLabel.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
		
		this.getLayeredPane().add(backLabel , new Integer(Integer.MIN_VALUE));
		info = new JPanel();
		info.setBackground(new Color(0xcc9f66));
		//info.setOpaque(true);
		difficulty = new JLabel();
		score = new JLabel();
		difficulty.setText("当前难度:简单");
		score.setText("当前积分:" + ScoreRecord.curScore);
		info.add(difficulty);
		info.add(score);
		
		info.setBounds(580, 0, 200, 30);
		container.setVisible(true);
		
		container.add(info);
		
		landlord[0] = new JButton("抢地主");
		landlord[1] = new JButton("不抢");
		landlord[0].setBounds(300 , 360 , 80 , 30);
		landlord[1].setBounds(390 , 360 , 80 , 30);
		landlord[0].addActionListener(this);
		landlord[1].addActionListener(this);
		container.add(landlord[0]);
		container.add(landlord[1]);
		publishCard[0] = new JButton("出牌");
		publishCard[1] = new JButton("不出");
		publishCard[2] = new JButton("提示");
		publishCard[0].setBounds(280 , 360 , 80 , 30);
		publishCard[1].setBounds(370 , 360 , 80 ,  30);
		publishCard[2].setBounds(460 , 360 , 80 , 30);
		publishCard[0].addActionListener(this);
		publishCard[1].addActionListener(this);
		publishCard[2].addActionListener(this);
		container.add(publishCard[0]);
		container.add(publishCard[1]);
		container.add(publishCard[2]);
		
		timers[0] = new TimerComponent();
		timers[0].setBounds(40, 40, 80, 30);
		timers[1] = new TimerComponent();
		timers[1].setBounds(30, 460, 80, 30);
		timers[2] = new TimerComponent();
		timers[2].setBounds(690, 40, 80, 30);
		for(int i = 0; i < 3; i++){
			timers[i].setVisible(false);
		}
		container.add(timers[0]);
		container.add(timers[1]);
		container.add(timers[2]);
		
		hideButtonLandlord();
		hideButtonPublishCard();

	}
	
	public void showButtonPublishCard(){
		publishCard[0].setVisible(true);
		publishCard[1].setVisible(true);
		publishCard[2].setVisible(true);
	}
	public void hideButtonPublishCard(){
		publishCard[0].setVisible(false);
		publishCard[1].setVisible(false);
		publishCard[2].setVisible(false);
	}
	public void showButtonLandlord(){
		landlord[0].setVisible(true);
		landlord[1].setVisible(true);
	}
	
	public void hideButtonLandlord(){
		landlord[0].setVisible(false);
		landlord[1].setVisible(false);
	}
	
	public void showLord(){
		for(int i = 0; i < lord.size(); i++){
			lord.get(i).turnUp();
			lord.get(i).setVisible(true);
		}
		try{
			Thread.sleep(500);
		} catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	private MainWindow(){
		
	}
	
	private static MainWindow instance;
	public static MainWindow getWindow(){//Singleton
		if(instance == null){
			synchronized (MainWindow.class) {
				if(instance == null){
					instance = new MainWindow();
				}
			}
		}
		return instance;
	}
	
	
	
	public void readGame(Game g){
	
		pairComponent = transCardsToComponents(g.pairs);
		cards0 = transCardsToComponents(g.list0);
		cards1 = transCardsToComponents(g.list1);
		cards2 = transCardsToComponents(g.list2);
		lastOut = transCardsToComponents(g.lastCards);
		lastOut0 = transCardsToComponents(g.lastCards0);
		lastOut1 = transCardsToComponents(g.lastCards1);
		lastOut2 = transCardsToComponents(g.lastCards2);
		lord = transCardsToComponents(g.listLord);
	}
	
	public void gameInit(){
		game = GameBuilder.buildGame();
		game.setView(this);
		new Thread(game).start();
		while(!game.initOver){
			try{
				Thread.sleep(50);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		this.readGame(game);
	}
	
	/**
	 * 
	 */
	public void start(){
		init();
		gameInit();
		this.setVisible(true);
		deal();
		initOver = true;
		try{
			Thread.sleep(500);
		} catch (Exception e){
			e.printStackTrace();
		}   
		
	}
	
	
	// 创建菜单 功能按钮
	public void SetMenu() {
		JMenuBar jMenuBar = new JMenuBar();
		JMenu game = new JMenu("游戏");
		JMenu setting = new JMenu("设置");
		JMenu help = new JMenu("帮助");
		
		restart = new JMenuItem("新游戏");
		exit = new JMenuItem("退出");
		changeMusic = new JMenuItem("换音乐");
		level1 = new JMenuItem("简单");
		level2 = new JMenuItem("一般");
		about = new JMenuItem("关于");
		
		changeMusic.addActionListener(this);
		restart.addActionListener(this);
		exit.addActionListener(this);
		about.addActionListener(this);
		level1.addActionListener(this);
		level2.addActionListener(this);
		
		game.add(restart);
		game.add(exit);
		setting.add(changeMusic);
		setting.add(level1);
		setting.add(level2);
		help.add(about);

		jMenuBar.add(game);
		jMenuBar.add(setting);
		jMenuBar.add(help);
		this.setJMenuBar(jMenuBar);
		
	}
	
	/**
	 * 发牌函数，控制动画等
	 */
	
	public void deal(){
		cards0 = new ArrayList<CardComponent>(17);
		cards1 = new ArrayList<CardComponent>(17);
		cards2 = new ArrayList<CardComponent>(17);
		lord = new ArrayList<CardComponent>(3);
		for(int i = 0; i < 51; i++){
			CardComponent cc = pairComponent.get(i);
			cc.setLocation(CardComponent.positionPairs.get(i));
			container.add(cc);
			if(i % 3 == 0){
				cards0.add(cc);//上家
			} else if(i % 3 == 1){
				cards1.add(cc);//我
			} else {
				cards2.add(cc);//下家
			}
		}
		for(int i = 51; i < 54; i++){
			CardComponent cc = pairComponent.get(i);
			lord.add(cc);
			cc.setLocation(CardComponent.positionPairs.get(i));
			container.add(cc);
		}
		for(CardComponent cc : pairComponent){
			container.add(cc);
		}
		
		
		for(int i = 0; i < cards0.size(); i++){
			int shift = (20 - cards0.size()) / 2;
			SystemHandler.move(cards0.get(i), cards0.get(i).getLocation(), CardComponent.position0.get(shift + i));
			cards1.get(i).turnUp();
			SystemHandler.move(cards1.get(i), cards1.get(i).getLocation(), CardComponent.position1.get(shift + i));
			SystemHandler.move(cards2.get(i), cards2.get(i).getLocation(), CardComponent.position2.get(shift + i));
		}
		for(int i = 0; i < 3; i++){
			SystemHandler.move(lord.get(i), lord.get(i).getLocation(), CardComponent.lords.get(i));
		}
		cardsReposition(0);
		cardsReposition(1);
		cardsReposition(2);
	}
	
	public void newGame(){
		clear();
		this.repaint();
		gameInit();
		deal();
		try{
			Thread.sleep(500);
		} catch (Exception e){
			e.printStackTrace();
		}   
		showButtonLandlord();
	}
	
	public void cardsOut(int playerIndex , List<CardComponent> ccList){
		if(ccList.size() != 0){
			List<CardComponent> lastList;
			List<Point> positions;
			int shift = (20 - ccList.size()) / 2;
			
			switch(playerIndex){
			case 0:
				positions = CardComponent.outing0;
				lastList = lastOut0;
				lastOut0 = ccList;
				break;
			case 1:
				positions = CardComponent.outing1;
				lastList = lastOut1;
				lastOut1 = ccList;
				break;
			case 2:
				positions = CardComponent.outing2;
				lastList = lastOut2;
				lastOut2 = ccList;
				break;
				default:return;
			}
			
			if(lastList != null){
				for(CardComponent cc : lastList){
					cc.setVisible(false);
				}
			}
			
			for(int i = ccList.size() - 1; i >= 0 ; i--){
				CardComponent cc = ccList.get(i);
				SystemHandler.move(cc, cc.getLocation(), positions.get(i + shift));
				cc.turnUp();
				cc.setVisible(true);
				container.setComponentZOrder(cc, 0);
				cc.setCanClicked(false);
			}
			readGame(this.game);
			cardsReposition(playerIndex);	
		}		
	}
	
	private void cardsReposition(int n){
		if(n >= 0 && n <= 2){
			List<CardComponent> cards = null;
			List<Point> positions = null;
			switch(n){
			case 0:
				cards = cards0;
				positions = CardComponent.position0;
				break;
			case 1:
				cards = cards1;
				positions = CardComponent.position1;
				break;
			case 2:
				cards = cards2;
				positions = CardComponent.position2;
				break;
			}
			
			Collections.sort(cards);

			int shift = (20 - cards.size()) / 2;
			
			for(int i = 0; i < cards.size(); i++){
				cards.get(i).isClicked = false;
				if(cards == cards1){
					cards.get(i).turnUp();
				} else {
					cards.get(i).turnRear();
				}
				
				SystemHandler.move(cards.get(i), cards.get(i).getLocation(), positions.get(i + shift));
				container.add(cards.get(i));
			}
		}
		
	}
	
	public void outFailed(){
		for(CardComponent cc : cards1){
			if(cc.isClicked){
				cc.isClicked = false;
			}
		}
		cardsReposition(1);
	}
	
	
	
	private static void transport(List<CardComponent> from , List<CardComponent> to , List<Card> cards){
		for(int i = 0; i < cards.size(); i++){
			for(int j = 0; j < from.size(); j++){
				if(from.get(j).getCard() == cards.get(i)){
					to.add(from.get(j));
					from.remove(j);
					break;
				}
			}
		}
		Collections.sort(from);
		Collections.sort(to);
	}
	
	public void g1out(){
		List<Card> cList = new ArrayList<Card>(20);
		for(CardComponent cc : cards1){
			if(cc.isClicked){
				cList.add(cc.getCard());
			}
		}
		Collections.sort(cList);
		CardGroup outing = new CardGroup(cList);
		
		
		if(outing.getType() != CardType.T0 && (game.lastCards == game.lastCards1 || outing.biggerthan(new CardGroup(game.lastCards)))){			
			List<CardComponent> out1 = new ArrayList<CardComponent>(outing.getCards().size());
			transport(cards1, out1, outing.getCards());//UI级别的组件转运
			game.gamers[1].out(outing);
			game.lastCards1 = outing.getCards();
			cardsOut(1, out1);
			game.gamers[1].hasAction = true;
			
		} else {
			outFailed();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == exit){
			System.exit(0);
		} else if(e.getSource() == about){
			JOptionPane.showMessageDialog(this, "github.com/jylibjtu");
		} else if(e.getSource() == restart){
			game.clockStop();
			game.musicStop();
			initOver = false;
			this.clear();
			gameInit();
			init();
			readGame(this.game);
			this.setVisible(true);
			deal();
			initOver = true;

			
		} else if(e.getSource() == level1){
			difficulty.setText("当前难度:简单");
		} else if(e.getSource() == level2){
			difficulty.setText("当前难度:一般");
		} else if(e.getSource() == changeMusic){
			this.game.changeBGM();
		} else if(e.getSource() == landlord[0]){
			showLord();
			lordToGamer(1);
			game.list1.addAll(game.listLord);
			game.gamers[1].hasAction = true;
			game.gamers[1].setRole(1);
			hideButtonLandlord();
		} else if(e.getSource() == landlord[1]){
			game.gamers[1].hasAction = true;
			game.gamers[1].setRole(0);
			hideButtonLandlord();
		} else if(e.getSource() == publishCard[0]){
			g1out();
		} else if(e.getSource() == publishCard[1]){
			if(game.lastCards != null && game.lastCards != game.lastCards1){
				outFailed();
				game.lastCards1 = null;
				game.gamers[1].hasAction = true;
			}
		} else if(e.getSource() == publishCard[2]){
			CardGroup prompt;
			if(lastOut == null || lastOut == lastOut1){
				prompt = ((AIGamer)game.gamers[1]).ownRecognize();
			} else {
				CardGroup lastGroup = new CardGroup(transComponentsToCards(lastOut));
				prompt = ((AIGamer)game.gamers[1]).recognize(lastGroup);
			}
			if(prompt != null){
				for(CardComponent cc : cards1){
					for(int i = 0; i < prompt.getCards().size(); i++){
						if(cc.getCard() == prompt.getCards().get(i)){
							cc.click();
						}
					}
				}
			}
		}
		
	}
	
	public void mouseClicked(MouseEvent e){
		if(e.getButton() == MouseEvent.BUTTON3 && game.gamers[1] == game.getOnFocusGamer()){
			g1out();
		}
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	
	/**
	 * 与mouseReleased函数结合，处理拖动选定（下方或者上方）
	 * 按下点 检测
	 */
	public void mousePressed(MouseEvent e){
		if(e.getButton() == MouseEvent.BUTTON1){
			Point p = e.getPoint();
			if(isInValidArea(p)){
				int xLen = cards1.get(0).getLocation().x + CardComponent.width - p.x;
				if(xLen <= CardComponent.width){
					doneStart = 0;
				} else {
					doneStart = (xLen - CardComponent.width) / 20 + 1;
				}
			}
		}
			
	}
	
	/**
	 * 与mousePressed函数结合，处理拖动选定（卡片的下方或者上方）
	 * 松开点 检测
	 */
	public void mouseReleased(MouseEvent e){
		if(e.getButton() == MouseEvent.BUTTON1 && doneStart != null){
			Point p = e.getPoint();
			if(isInValidArea(p)){
				Integer curPos = null;
				int xLen = cards1.get(0).getLocation().x + CardComponent.width - p.x;		
				if(xLen <= CardComponent.width){
					curPos = 0;
				} else {
					curPos = (xLen - CardComponent.width) / 20 + 1;
				}
				if(doneStart != null && curPos != null){
					if(curPos < cards1.size() && doneStart < cards1.size()){
						int start = doneStart <= curPos ? doneStart : curPos;
						int end = doneStart + curPos - start;
						for(int i = start; i <= end; i++){
							try{
								Thread.sleep(5);
							} catch (Exception ee){
								ee.printStackTrace();
							}
							cards1.get(i).click();
						}
						
					}
				}
			}
			
			
			doneStart = null;
		}	
	}
	
	public void focusOn(int n){
		if(n > 2 || n < 0){
			return ;
		}
		if(n == 1){
			if(!game.isLordOver){
				showButtonLandlord();
			} else {
				hideButtonLandlord();
				showButtonPublishCard();
				for(CardComponent cc : cards1){
					cc.setCanClicked(true);
				}	
			}
					
		} else {
			hideButtonLandlord();
			hideButtonPublishCard();
			for(CardComponent cc : cards1){
				cc.setCanClicked(false);
			}
		}
		for(int i = 0; i < 3; i++){
			if(i == n){
				timers[i].setVisible(true);
			} else {
				timers[i].setVisible(false);
			}
		}
		
	}
	
	private boolean isInValidArea(Point p){
		if(!game.isGameOver){
			int yUp = CardComponent.position1.get(0).y;

			int xRight = cards1.get(0).getLocation().x + CardComponent.width;
			if(p.y >= yUp - 50 && p.y <= 600){
				if(p.x <= xRight && xRight - CardComponent.width < p.x + (cards1.size() - 1) * 20){
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	public List<CardComponent> transCardsToComponents(List<Card> cList){
		if(cList == null){
			return null;
		}
		
		List<CardComponent> ccList = new ArrayList<CardComponent>(cList.size()); 
		
		for(Card c : cList){
			if(pairComponent != null){
				for(int i = 0; i < pairComponent.size(); i++){
					if(pairComponent.get(i).getCard() == c){
						ccList.add(pairComponent.get(i));
						break;
					}
				}
			} else {
				ccList.add(new CardComponent(c, false , false));
			}
			
		}
		return ccList;
	}
	
	public List<Card> transComponentsToCards(List<CardComponent> ccList){
		if(ccList == null){
			return null;
		}
		
		List<Card> cList = new ArrayList<Card>(ccList.size());
		for(CardComponent cc : ccList){
			cList.add(cc.getCard());
		}
		return cList;
	}
	
	@Override
	public void reactionToOver(){
		String text = "";
		if(game.winnerRole == game.gamers[1].getRole()){
			text = "恭喜你，赢了！获得200分！";
			new SoundPlayer(SoundPlayer.SOUND_ROOT_PATH + "victory.wav").start();
		} else {
			text = "回去再好好练练吧，输了200分！";
			new SoundPlayer(SoundPlayer.SOUND_ROOT_PATH + "defeat.wav").start();
		}
		score.setText("当前积分:" + ScoreRecord.curScore);
		JOptionPane.showMessageDialog(this, text);
		hideButtonLandlord();
		hideButtonPublishCard();
	}
	
}
