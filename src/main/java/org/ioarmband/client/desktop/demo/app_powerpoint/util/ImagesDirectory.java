package org.ioarmband.client.desktop.demo.app_powerpoint.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

public class ImagesDirectory extends ArrayList<BufferedImage> {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(ImagesDirectory.class);


	public ImagesDirectory() {
		super();
	}

	public ImagesDirectory(List<BufferedImage> images) {
		super();
		this.addAll(images);
	}
	
	public ImagesDirectory(String pathDirectory) {
		super();		
		load(pathDirectory);
	}

	private void load(String pathDirectory) {
		File repository = new File(pathDirectory);

		int i;
		String[] listImages = repository.list();
		for (i = 0; i < listImages.length; i++) {
			if (isExtensionSupported(listImages[i])) {
				try {
					logger.info(listImages[i]);
					BufferedImage image = ImageIO.read(new File(pathDirectory+"/"+listImages[i]));
					this.add(image);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private String[] SUPPORTE_DEXTENSION = {"jpg","jpeg","png","bmp","GIF"};
	private boolean isExtensionSupported(String path)
	{
		for (String ext : SUPPORTE_DEXTENSION) {
			if (path.toLowerCase().endsWith("."+ext) == true ) {
				return true;
			}
		}
		return false;
		
	}
}
