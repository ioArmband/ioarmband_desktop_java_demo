package org.ioarmband.client.desktop.demo.app_powerpoint.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.ioarmband.client.desktop.demo.app_powerpoint.util.ImagesDirectory;

public class ImagesDirectoryViewer extends JPanel {
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(ImagesDirectoryViewer.class);

	private ImagesDirectory images;

	Set<ImageChangeListener> imageChangeListeners = new HashSet<ImageChangeListener>();
	
	public ImagesDirectory getImagesDirectory() {
		return images;
	}

	public void setImagesDirectory(ImagesDirectory images) {
		this.images = images;
		numImageCurrent = 0;
		//updateUI();
		refreshImage();
	}

	private int numImageCurrent;
	private JButton nextButton;
	private JButton beforeButton;
	private ImageViewer imageViewer;

	public ImagesDirectoryViewer() {
		this.images = new ImagesDirectory();
		this.numImageCurrent = 0;
		init();
	}

	public ImagesDirectoryViewer(ImagesDirectory images) {
		this.images = images;
		this.numImageCurrent = 0;
		init();
	}

	public void init() {
		logger.info("init");
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel gridButtonPanel = new JPanel();

		gridButtonPanel.setLayout(new GridLayout(2, 2));
		gridButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		gridButtonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		//gridButtonPanel.setMaximumSize(new Dimension(512, 200));
		imageViewer = new ImageViewer();
		add(imageViewer);
		
		//BUTTON next and before
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		beforeButton = new JButton("before");
		beforeButton.addActionListener(beforeButtonListener);
		panel.add(beforeButton);

		nextButton = new JButton("next");
		nextButton.addActionListener(nextButtonListener);
		panel.add(nextButton);

		add(panel);

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		  .addKeyEventDispatcher(new KeyEventDispatcher() {
		      @Override
		      public boolean dispatchKeyEvent(KeyEvent e) {
		        System.out.println("Got key event!");
		        if (e.getID() == KeyEvent.KEY_PRESSED) {
		        switch (e.getKeyCode()) {
				case KeyEvent.VK_RIGHT:
					nextImage();
					break;
				case KeyEvent.VK_LEFT:
					beforeImage();
					break;
				case KeyEvent.VK_UP:

					break;
				case KeyEvent.VK_DOWN:

					break;
				}
		      }
		        return false;
		      }
		});
		
		setFocusable(true);
		 refreshImage();
	}

	private ActionListener nextButtonListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			nextImage();

		}
	};
	
	public void getCurrentImage() {
		
	}

	public void nextImage() {
		if (numImageCurrent >= (images.size() - 1)){
			numImageCurrent = images.size() - 1;}
		else
		{
			numImageCurrent++;
		refreshImage();
		}
		//updateUI();
	}

	public void beforeImage() {
		if (numImageCurrent <= 0)
		{
			numImageCurrent = 0;
		}
		else{
			numImageCurrent--;
		refreshImage();
		}
		//updateUI();
	}

	private ActionListener beforeButtonListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			beforeImage();

		}
	};

	public void refreshImage()
	{

		if (images != null && numImageCurrent < images.size()
				&& numImageCurrent >= 0) {
			BufferedImage image = images.get(numImageCurrent);
			
			if (image != null) {
				imageViewer.setImage(image);
				dispatchImageChange(image);
				logger.info("image print");
			}
		}
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

	}
	
	public void requestDirectoryChange() {

		JFileChooser dialogue = new JFileChooser();
		dialogue.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// affichage
		dialogue.showOpenDialog(null);

		String pathDirectory = dialogue.getSelectedFile().toPath().toString();

		ImagesDirectory imagesDirectory = new ImagesDirectory(pathDirectory);
		setImagesDirectory(imagesDirectory);	
	}
	
	private void dispatchImageChange(BufferedImage image){
		for (ImageChangeListener listener : imageChangeListeners) {
			listener.onChangeImage(image);
		}
	}
	public void addImageChangeListener(ImageChangeListener imageChangeListener)
	{
		this.imageChangeListeners.add(imageChangeListener);		
		
	} 
}
