package junyi.ddz.util;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import junyi.ddz.view.TimerComponent;
public class GameTimer extends Thread {
	private int seconds;//计时长
	private boolean hasStart = false; //未开始为false
	private boolean isTiming = false; //未开始 、已结束 均为false 正在倒计时过程中为true
	private Timer timer = new Timer();
	private TimerComponent timerComponent;
	
	public GameTimer(int s , TimerComponent comp){
		seconds = s;
		hasStart = false;
		isTiming = false;
		timerComponent = comp;
	}
	
	public void shut(){
		if(isTiming){
			timerComponent = null;
			isTiming = false;
		}
	}
	
	@Override
	public void run(){
		long start = System.currentTimeMillis();
		final long end = start + seconds * 1000 + 10;
		hasStart = true;//开始计时
		isTiming = true;//正在计时
		try{
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					long show = end - System.currentTimeMillis();
					long s = show/1000%60;
					if(timerComponent != null){
						timerComponent.setLastTime(s + "");
					}
				}
			}, 0 , 1000);//不延迟,每1000ms运行一次
			
			timer.schedule(new TimerTask()
	        {
	            public void run()
	            {
	                timer.cancel();
	                isTiming = false;//计时完毕
	            }
	            
	        }, new Date(end));
		} catch (Exception e){
			if(timerComponent == null){
				System.out.println("时钟终止");
			} else {
			}
		}
		
	}
	
	public boolean overtime(){
		return hasStart && (!isTiming);
	}
	
}
