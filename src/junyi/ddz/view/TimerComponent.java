package junyi.ddz.view;
import javax.swing.JTextField;


/**
 * 计时器倒计时的UI模块
 * @author junyi
 *
 */
public class TimerComponent extends JTextField {
	public static final long serialVersionUID = -1;
	public TimerComponent(){
		
	}
	public void setLastTime(String secondsInfo){
		this.setText(secondsInfo);
	}
}
