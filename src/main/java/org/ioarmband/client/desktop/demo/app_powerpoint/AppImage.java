package org.ioarmband.client.desktop.demo.app_powerpoint;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.ioarmband.client.desktop.demo.app_powerpoint.connection.BluetoothConnectionManager;
import org.ioarmband.client.desktop.demo.app_powerpoint.ui.ImageChangeListener;
import org.ioarmband.client.desktop.demo.app_powerpoint.ui.ImagesDirectoryViewer;
import org.ioarmband.net.connection.manager.ServiceOutConnection;
import org.ioarmband.net.message.Command;
import org.ioarmband.net.message.enums.GestureType;
import org.ioarmband.net.message.impl.GestureMessage;
import org.ioarmband.net.message.impl.ImageViewerApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AppImage implements ServiceOutConnection
{
	

	 static ImagesDirectoryViewer idv;
	 static  BluetoothConnectionManager bluetoothConnectionManager;
	private static final Logger logger = LoggerFactory.getLogger(AppImage.class);
	static AppImage appImage;
	
    public static void main( String[] args )
    {
    	bluetoothConnectionManager = BluetoothConnectionManager.getInstance();
    	  JFrame fenetre = new JFrame();
          
          fenetre.setTitle("ImageViewer for ioArmband");
          Toolkit outil = fenetre.getToolkit();
          Dimension windowsDimension = outil.getScreenSize();
          windowsDimension.setSize(windowsDimension.width,windowsDimension.height-50);
          
          fenetre.setSize(windowsDimension);
         
          idv = new ImagesDirectoryViewer();
          idv.addImageChangeListener(imageChangeListener);
          idv.requestDirectoryChange();
          
          // shutdown the program on windows close event
      	fenetre.addWindowListener(new WindowAdapter() {
              public void windowClosing(WindowEvent ev) {
            	  bluetoothConnectionManager.removeUseConnection(appImage);
                  System.exit(0);        
              }
          });
      	
      	fenetre.getContentPane().add(idv);
          fenetre.setVisible(true);
          
          appImage = new AppImage();
          bluetoothConnectionManager.useConnection(appImage);
    }

	private static ImageChangeListener imageChangeListener = new ImageChangeListener()
	{
		@Override
		public void onChangeImage(BufferedImage image) {
			logger.info("change image detected");
			Image im = (Image)image;
			ImageViewerApp msg = new ImageViewerApp(im);
			
			bluetoothConnectionManager.sendMessage(msg);
		}
	};


	@Override
	public void onCommandReiceved(Command command) {
		
		if(command.getClazz().equals(GestureMessage.class.getName()))
		{
			GestureMessage gestureMessage = (GestureMessage) command.getMessage();
			if(gestureMessage.getType() == GestureType.SWIPE)
			{
				String msg = gestureMessage.getSourceName();
				if(msg.equals("left"))
				{
					
					idv.beforeImage();
				}else if(msg.equals("right"))
				{
					idv.nextImage();
				}
				else if(msg.equals("top"))
				{
					idv.nextImage();
				}
				else if(msg.equals("bottom"))
				{
					idv.beforeImage();
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
		idv.refreshImage();
		
	}

	@Override
	public void onLoseControl() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionStarted() {
		logger.info("onConnectionStarted");
}
}
