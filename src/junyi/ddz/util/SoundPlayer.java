package junyi.ddz.util;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
public class SoundPlayer extends Thread {
	//根路径
	public static final String SOUND_ROOT_PATH = "sound" + File.separatorChar;
	private String path = null;//具体地址
	private String pathNext = null;//是否是组合型  如三个5 = sange.wav + 5.wav
	private boolean isLoop = false;//是否循环播放  背景音乐
	
	//播放所需器件
	private AudioInputStream audioInputStream = null;
	private AudioFormat audioFormat = null;
	private DataLine.Info dataLineInfo = null;
	private SourceDataLine sourceDataLine = null;
	
	/**
	 * 传入一个path（可能是被空格隔开的path队列）
	 * 最多前两个
	 * @param s_path 路径
	 */
	public SoundPlayer(String s_path){
		this.path = s_path;
		if(path.contains(" ")){
			path = s_path.split(" ")[0];
			pathNext = s_path.split(" ")[1];
		}
	}
	/**
	 * 不设置则自动不循环
	 * @param s_path 路径
	 * @param loop 是否循环
	 */
	public SoundPlayer(String s_path , boolean loop){
		this.path = s_path;
		if(path.contains(" ")){
			path = s_path.split(" ")[0];
			pathNext = s_path.split(" ")[1];
		}
		isLoop = loop;
	}
	
	/**
	 * 停止播放
	 */
	public void shut(){
		try{
			audioInputStream = null;
		}catch(Throwable  t){
			System.out.println("Old music stoped");
		}
	}
	
	/**
	 * 播放
	 */
	@Override
	public void run(){
		try{
			int count;
			byte tempBuffer[] = new byte[1024];
			do{
				audioInputStream = AudioSystem.getAudioInputStream(new File(path));
				audioFormat = audioInputStream.getFormat();
				dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat , AudioSystem.NOT_SPECIFIED);
				sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
				sourceDataLine.open(audioFormat);
				sourceDataLine.start();
				while((count = audioInputStream.read(tempBuffer , 0 , tempBuffer.length)) != -1){
					if(count > 0){
						sourceDataLine.write(tempBuffer, 0, count);
					}
				}
				sourceDataLine.drain();
				sourceDataLine.close();
			} while (isLoop);//循环，一直播放  否则一次
			
			
			if(pathNext != null){
				try{
					audioInputStream = AudioSystem.getAudioInputStream(new File(pathNext));
					audioFormat = audioInputStream.getFormat();
					dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat , AudioSystem.NOT_SPECIFIED);
					sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
					
					sourceDataLine.open(audioFormat);
					sourceDataLine.start();
					while((count = audioInputStream.read(tempBuffer , 0 , tempBuffer.length)) != -1){
						if(count > 0){
							sourceDataLine.write(tempBuffer, 0, count);
						}
					}
					sourceDataLine.drain();
					/*System.out.println(System.currentTimeMillis() - start);*/
					sourceDataLine.close();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		} catch (Exception e){
			if(audioInputStream == null){
				System.out.println("音乐终止");
			} else {
				e.printStackTrace();
			}
		}
	}
}
