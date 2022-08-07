package myPackage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Main;

@SuppressWarnings("serial")
public class EcoMoney extends JFrame {
	private ImageIcon infoButtonBasicImage = new ImageIcon(Main.class.getResource("../images/infoButtonBasic.png"));
	private ImageIcon infoButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/infoButtonEntered.png"));
	private ImageIcon joinButtonBasicImage = new ImageIcon(Main.class.getResource("../images/joinButtonBasic.png"));
	private ImageIcon joinButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/joinButtonEntered.png"));
	private ImageIcon loginButtonBasicImage = new ImageIcon(Main.class.getResource("../images/loginButtonBasic.png"));
	private ImageIcon loginButtonEnteredImage = new ImageIcon(
			Main.class.getResource("../images/loginButtonEntered.png"));

	private Image mainImage = new ImageIcon(Main.class.getResource("../images/mainImage.jpg")).getImage();

	private JButton infoButton = new JButton(infoButtonBasicImage);
	private JButton joinButton = new JButton(joinButtonBasicImage);
	private JButton loginButton = new JButton(loginButtonBasicImage);

	private JButton[] buttons = { joinButton, loginButton, infoButton };

	public EcoMoney() {
		
		setTitle("this.eco = money;");

		// 배경 설정
		JPanel background = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(mainImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		background.setLayout(null);
		int y = 361;

		for (JButton button : buttons) {
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setContentAreaFilled(false);
			button.setBounds(185, y, 240, 85);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton) e.getSource();
					if (button.equals(joinButton)) {
						new Join();
					} else if (button.equals(loginButton)) {
						new Login();
						dispose();
					} else if (button.equals(infoButton)) {
						new ProjectInfo();
					}
				}
			});
			button.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					JButton button = (JButton) e.getSource();
					if (button.getIcon() != null) {
						if (button.getIcon().equals(infoButtonBasicImage)) {
							button.setIcon(infoButtonEnteredImage);
						} else if (button.getIcon().equals(joinButtonBasicImage)) {
							button.setIcon(joinButtonEnteredImage);
						} else if (button.getIcon().equals(loginButtonBasicImage)) {
							button.setIcon(loginButtonEnteredImage);
						}
					}

				}

				@Override
				public void mouseReleased(MouseEvent e) {

				}

				@Override
				public void mouseExited(MouseEvent e) {
					JButton button = (JButton) e.getSource();
					if (button.getIcon() != null) {
						if (button.getIcon().equals(infoButtonEnteredImage)) {
							button.setIcon(infoButtonBasicImage);
						} else if (button.getIcon().equals(joinButtonEnteredImage)) {
							button.setIcon(joinButtonBasicImage);
						} else if (button.getIcon().equals(loginButtonEnteredImage)) {
							button.setIcon(loginButtonBasicImage);
						}
					}

				}

				@Override
				public void mousePressed(MouseEvent e) {

				}
			});
			background.add(button);
			y += 80;
		}

		setContentPane(background);
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

}
