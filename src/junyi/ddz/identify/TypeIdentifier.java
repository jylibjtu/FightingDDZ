package junyi.ddz.identify;

import junyi.ddz.core.CardGroup;

public abstract class TypeIdentifier {
	protected TypeIdentifier next;//责任链模式,下一识别器
	public abstract void identify(CardGroup group);//应用方法,如果此识别器不识别，抛给下一个识别器
	public void setNext(TypeIdentifier cti){
		next = cti;//设置下一个识别器，成识别器链
	}
}
