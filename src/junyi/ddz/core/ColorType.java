package junyi.ddz.core;


public enum ColorType {
	HONGTAO,//红桃
	HEITAO,//黑桃
	FANGPIAN,//方片
	MEIHUA;//梅花
	public static ColorType getTypeByInt(int i){
		if(i < 0 || i > 3){
			return null;
		} else {
			for(ColorType type : ColorType.values()){
				  if(type.ordinal() == i){
					  return type;
				  }
			  }
		}
		return null;
	}
	public static int getValue(ColorType ct){
		return ct.ordinal();
	}
}
