package org.ioarmband.client.desktop.demo.app_powerpoint;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.ioarmband.client.desktop.demo.app_powerpoint.connection.BluetoothConnectionManager;
import org.ioarmband.net.connection.manager.ServiceOutConnection;
import org.ioarmband.net.message.Command;
import org.ioarmband.net.message.enums.GestureType;
import org.ioarmband.net.message.impl.AppMessage;
import org.ioarmband.net.message.impl.GestureMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class AppDiapo implements ServiceOutConnection
{
	


	private static final Logger logger = LoggerFactory.getLogger(AppDiapo.class);
	 static  BluetoothConnectionManager bluetoothConnectionManager;
	 static AppDiapo appDiapo;
	 
    public static void main( String[] args )
    {
    	  bluetoothConnectionManager = BluetoothConnectionManager.getInstance();

       JFrame fenetre = new JFrame();
       
       fenetre.setTitle("PowerPoint for ioArmband");
      
       
       // shutdown the program on windows close event
   	fenetre.addWindowListener(new WindowAdapter() {
           public void windowClosing(WindowEvent ev) {
        	   bluetoothConnectionManager.removeUseConnection(appDiapo);
               System.exit(0);        
              
           }
       });
   	
       fenetre.setVisible(true);
       appDiapo = new AppDiapo();
       bluetoothConnectionManager.useConnection(appDiapo);
    }
      
    
    
    private static void switchDiapo(int nbDiapo)
    {
    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    	File file = new File(classLoader.getResource("gotoSlide.vbs").getPath());
    	
    	String path = file.getAbsolutePath()+" "+nbDiapo;
    	String newChar = "//";
    	String oldChar = "\\";
    	path = path.replace(oldChar, newChar);
    	
   //  String pathTest =  "C://Users//benjamin//Documents//Android//workspace//app_powerpoint//target//classes//gotoSlide.vbs 1";	
   //  logger.info("pathTest : "+pathTest);
    	
    	logger.info("execution du vbs : "+path);
    	try {
			Runtime rt = Runtime.getRuntime();
			rt.exec("wscript "+ path);
			//Process p = rt.exec(new String[] { "cmd.exe", "/c", path });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("error");
			logger.error(e.toString());
		}
    }
   

	@Override
	public void onCommandReiceved(Command command) {
		
		if(command.getClazz().equals(GestureMessage.class.getName()))
		{
			GestureMessage gestureMessage = (GestureMessage) command.getMessage();
			if(gestureMessage.getType() == GestureType.SWIPE || gestureMessage.getType() == GestureType.TOUCH)
			{
				String msg = gestureMessage.getSourceName();
				if(msg.equals("left"))
				{
					logger.info("switch diapo left");
					switchDiapo(-1);

				}else if(msg.equals("right"))
				{
					logger.info("switch diapo right");
					switchDiapo(1);
				}
				else if(msg.equals("top"))
				{
					switchDiapo(1);
				}
				else if(msg.equals("bottom"))
				{
					switchDiapo(-1);
				}
			}
		}
	}



	@Override
	public void onConnectionClose() {
		logger.info("ConnectionClose");
}



	@Override
	public void onWinControl() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onLoseControl() {
		// TODO Auto-generated method stub	
	}



	@Override
	public void onConnectionStarted() {
		logger.info("ConnectionBegin");
		
		AppMessage msg = new AppMessage(AppMessage.AppStd.SLIDE_SWIPER);
		bluetoothConnectionManager.sendMessage(msg);
		
	}
}
