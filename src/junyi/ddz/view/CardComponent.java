package junyi.ddz.view;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import junyi.ddz.core.Card;

public class CardComponent extends JLabel implements MouseListener , Comparable<CardComponent> {
	public static final long serialVersionUID = -1;
	private static String back_image = "back3.jpg";
	private Card card;
	public boolean isClicked;//是否被点击了
	private boolean canClicked;//是否可以被点击
	private boolean isUp;//true 正面朝上 false 反面朝上
	
	public static final int width = 71 , height = 96;
	
	public static List<Point> position0 = new ArrayList<Point>(20);
	public static List<Point> position1 = new ArrayList<Point>(20);
	public static List<Point> position2 = new ArrayList<Point>(20);
	public static List<Point> lords = new ArrayList<Point>(3);
	public static List<Point> outing0 = new ArrayList<Point>(20);
	public static List<Point> outing1 = new ArrayList<Point>(20);
	public static List<Point> outing2 = new ArrayList<Point>(20);
	public static List<Point> positionPairs = new ArrayList<Point>(54);
	static{
		for(int i = 0; i < 20; i++){
			Point p0 = new Point(40 , 360 - i * 15);
			Point p1 = new Point(560 - i * 20 , 400);
			Point p2 = new Point(690 , 360 - i * 15);
			Point o0 = new Point(150 , 360 - i * 15);
			Point o1 = new Point(500 - i * 20 , 250);
			Point o2 = new Point(580 , 360 - i * 15);
			position0.add(p0);
			position1.add(p1);
			position2.add(p2);
			outing0.add(o0);
			outing1.add(o1);
			outing2.add(o2);
		}
		
		
		for(int i = 0; i < 54; i ++){
			Point p = new Point(360 , 50 + i * 2);
			positionPairs.add(p);
		}
		
		for(int i = 0; i < 3; i++){
			Point p = new Point(280 + i * 80 , 10);
			lords.add(p);
		}
	}
	
	@Override
	public int compareTo(CardComponent c){
		return card.compareTo(c.getCard());
	}
	
	
	/**
	 * 
	 * @param backIndex 1~5 卡背选择
	 * @return 是否修改成功
	 */
	public static boolean ChangeBack(int backIndex){
		if(backIndex >= 1 && backIndex <= 6){
			back_image = "back" + backIndex + ".jpg";
			return true;
		}
		return false;
	}
	
	public CardComponent(Card card , boolean up){
		this.card = card;
		isClicked = false;
		canClicked = true;
		isUp = up;
		this.setSize(width, height);
		if(isUp){
			this.turnUp();
		} else {
			this.turnRear();
		}
		this.addMouseListener(this);
	}
	
	public CardComponent(Card card , boolean up , boolean canClick){
		this.card = card;
		isClicked = false;
		canClicked = canClick;
		isUp = up;
		this.setSize(width, height);
		if(isUp){
			this.turnUp();
		} else {
			this.turnRear();
		}
		this.addMouseListener(this);
	}
	
	public void draw(){
		if(isUp){
			this.turnUp();
		} else {
			this.turnRear();
		}
		this.setVisible(true);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			click();
		}
	}
	
	public void click(){
		if(this.canClicked)
		{
			Point from=this.getLocation();
			int step; //移动的距离
			if(isClicked)
				step=20;
			else {
				step=-20;
			}
			isClicked=!isClicked; //反向
			//当被选中的时候，向前移动一步/后退一步
			this.setLocation(new Point(from.x , from.y + step));
		}
	}
	
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	
	public void turnRear(){//扣上
		ImageIcon icon = new ImageIcon(Card.IMAGE_ROOT_PATH + back_image);
		icon = new ImageIcon(icon.getImage().getScaledInstance(71, 96, Image.SCALE_DEFAULT));
		this.setIcon(icon);
		this.isUp = false;
	}
	public void turnUp(){//翻开
		ImageIcon icon = new ImageIcon(card.getImagePath());
		icon = new ImageIcon(icon.getImage().getScaledInstance(71, 96, Image.SCALE_DEFAULT));
		this.setIcon(icon);
		this.isUp = true;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public boolean isCanClicked() {
		return canClicked;
	}

	public void setCanClicked(boolean canClicked) {
		this.canClicked = canClicked;
	}

	public boolean isUp() {
		return isUp;
	}

	public void setUp(boolean isUp) {
		this.isUp = isUp;
	}
	@Override
	public CardComponent clone(){
		CardComponent cc = new CardComponent(this.card, true , false);
		cc.setLocation(this.getLocation());
		return cc;
	}
	
}
