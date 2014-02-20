package org.ioarmband.client.desktop.demo.app_powerpoint;

import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.ioarmband.client.desktop.demo.app_powerpoint.connection.BluetoothConnectionManager;
import org.ioarmband.client.desktop.demo.app_powerpoint.connection.ImageEncoder;
import org.ioarmband.client.desktop.demo.app_powerpoint.connection.ImageTools;
import org.ioarmband.client.desktop.demo.app_powerpoint.ui.ImageViewer;
import org.ioarmband.net.connection.manager.ServiceOutConnection;
import org.ioarmband.net.message.Command;
import org.ioarmband.net.message.impl.CastingImageMessage;
import org.ioarmband.net.message.impl.CastingRequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CastingioArmband implements ServiceOutConnection{

	 static  BluetoothConnectionManager bluetoothConnectionManager;
	 static ImageViewer imageViewer;
	 static CastingioArmband castingioArmband;
		private static final Logger logger = LoggerFactory.getLogger(AppImage.class);
		
		static JFrame jf;
		static ImageViewer jp;
		
		
    public static void main( String[] args )
    {
    	bluetoothConnectionManager = BluetoothConnectionManager.getInstance();

		jf = new JFrame("ioArmband");
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize( 1600/2, 900/2);
		jf.setAlwaysOnTop(true);
		jf.setVisible(true);
		jp = new ImageViewer();
		jp.setSize(jf.getSize());
		jf.getContentPane().add(jp);
		

          
          // shutdown the program on windows close event
		jf.addWindowListener(new WindowAdapter() {
              public void windowClosing(WindowEvent ev) {
            	  bluetoothConnectionManager.removeUseConnection(castingioArmband);
                  System.exit(0);        
              }
          });
          
                   
          castingioArmband = new CastingioArmband();
          bluetoothConnectionManager.useConnection(castingioArmband);
    }
    
	@Override
	public void onCommandReiceved(Command command) {
		logger.info("Command received : " + command);
		CastingImageMessage msg = (CastingImageMessage) command.getMessage();
		Image im = ImageEncoder.decodeBase64(msg.getEncodedImage());
		im = ImageTools.resize(im, 1600/2, 900/2);
		jp.removeAll();
		jp.add(new JLabel(new ImageIcon(im)));
		jp.updateUI();
	
}

	@Override
	public void onConnectionClose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWinControl() {
		bluetoothConnectionManager.sendMessage(new CastingRequestMessage(1000,200,150));

	}

	@Override
	public void onLoseControl() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionStarted() {
		// TODO Auto-generated method stub
		
	}
	

}
