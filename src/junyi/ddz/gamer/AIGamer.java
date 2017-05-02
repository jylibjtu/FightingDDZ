package junyi.ddz.gamer;

import junyi.ddz.core.CardGroup;
import junyi.ddz.state.State;

public class AIGamer extends Gamer {
	protected State state;
	public AIGamer(State state){
		this.state = state;
	}
	
	public boolean landLord(){
		return false;
	}
	
	public CardGroup attack(CardGroup group){
		if(state.whetherAttack(this, game.lastOutingGamer(), group)){
			CardGroup out = strategy.attack(group);
			return out;
		}
		return null;
	}
	
	public CardGroup ownOut(){
		return strategy.ownOut();
	}
	
	public CardGroup ownRecognize(){
		return strategy.ownRecognize();
	}
	
	public CardGroup recognize(CardGroup group){
		return strategy.recognize(group);
	}
	
	
	public boolean whetherLord(){
		return getHandEvaluate(list) > 300;
	}
	@Override
	public String toString(){
		StringBuffer sbRet = new StringBuffer();
		sbRet.append(this.role == 1 ? "地主" : "农民");
		sbRet.append(" [" + super.toString() + "]");
		return sbRet.toString();
	}
	
}
