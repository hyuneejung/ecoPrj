package myPackage;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Main;

@SuppressWarnings("serial")

public class ProjectInfo extends JFrame {

	private Image ProjectInfoImage = new ImageIcon(Main.class.getResource("../images/ProjectInfoImage.png")).getImage();

	public ProjectInfo() {

		setTitle("Project Info");
		JPanel background = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(ProjectInfoImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};

		background.setLayout(null);

		setContentPane(background);
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

}