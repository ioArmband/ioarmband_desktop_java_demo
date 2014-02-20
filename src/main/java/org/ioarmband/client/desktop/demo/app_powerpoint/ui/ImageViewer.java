package org.ioarmband.client.desktop.demo.app_powerpoint.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImageViewer extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	
	    public ImageViewer() {
	    	image = null;
	    }
	    
	
	    public ImageViewer(BufferedImage image) {
		super();
		this.image = image;
	}


		@Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	       if(image != null)
	       {
	    	   g.drawImage(image, 0, 0, null);
	       }
	    }

		public BufferedImage getImage() {
			return image;
		}

		public void setImage(BufferedImage image) {
			this.image = image;
			updateUI();
		}
	    
	    
}

