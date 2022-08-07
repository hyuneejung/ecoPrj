package myPackage;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dto.HistoryDto;
import main.Main;

@SuppressWarnings("serial")
public class MenuPanel extends JFrame {
	
	private ImageIcon rankButtonBasicImage = new ImageIcon(Main.class.getResource("../images/rankButtonBasic.png"));
	private ImageIcon rankButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/rankButtonEntered.png"));
	private ImageIcon saveButtonBasicImage = new ImageIcon(Main.class.getResource("../images/saveButtonBasic.png"));
	private ImageIcon saveButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/saveButtonEntered.png"));
	private ImageIcon tradeButtonBasicImage = new ImageIcon(Main.class.getResource("../images/tradeButtonBasic.png"));
	private ImageIcon tradeButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/tradeButtonEntered.png"));
	private ImageIcon toBankButtonBasicImage = new ImageIcon(Main.class.getResource("../images/toBankButtonBasic.png"));
	private ImageIcon toBankButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/toBankButtonEntered.png"));
	private ImageIcon donateButtonBasicImage = new ImageIcon(Main.class.getResource("../images/donateButtonBasic.png"));
	private ImageIcon donateButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/donateButtonEntered.png"));
	private ImageIcon myPageButtonImage = new ImageIcon(Main.class.getResource("../images/myPageButton.png"));
	private ImageIcon revaliButtonBasicImage = new ImageIcon(Main.class.getResource("../images/revaliButton.png"));
	private ImageIcon logoutButtonImage = new ImageIcon(Main.class.getResource("../images/logoutButton.png"));

	private JButton myPageButton = new JButton(myPageButtonImage);
	private JButton saveButton = new JButton(saveButtonBasicImage);
	private JButton toBankButton = new JButton(toBankButtonBasicImage);
	private JButton donateButton = new JButton(donateButtonBasicImage);
	private JButton rankButton = new JButton(rankButtonBasicImage);
	private JButton tradeButton = new JButton(tradeButtonBasicImage);
	private JButton revaliButton = new JButton(revaliButtonBasicImage);
	private JButton logoutButton = new JButton(logoutButtonImage);
	
	private Font font28 = new Font("고양체", Font.PLAIN, 28);
	
	private JLabel labelNick = new JLabel(Main.LoginUser.getNick());

	private JButton[] buttons = { myPageButton, saveButton, toBankButton, donateButton, rankButton, tradeButton, revaliButton, logoutButton };

	private Image menuImage = new ImageIcon(Main.class.getResource("../images/menuImage.png")).getImage();

	public MenuPanel() {

		setTitle("this.eco = money;");

		JPanel background = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(menuImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};

		background.setLayout(null);

		for (JButton button : buttons) {
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setContentAreaFilled(false);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton) e.getSource();
					if (button.equals(myPageButton)) {
						dispose();
						new MyPagePanel();
					} else if (button.equals(saveButton)) {
						new SavePanel();
					} else if (button.equals(toBankButton)) {
						new SendPanel();
					} else if (button.equals(donateButton)) {
						new DonationPanel();
					} else if (button.equals(rankButton)) {
						new ShowRankingPanel();
					} else if (button.equals(tradeButton)) {
						new TradePanel();
					} else if (button.equals(revaliButton)) {
						labelNick.setText(Main.LoginUser.getNick());
					} else if (button.equals(logoutButton)) {
						int answer = JOptionPane.showConfirmDialog(background, "로그아웃하시겠습니까?", "알림", JOptionPane.YES_NO_OPTION);
						if(answer == JOptionPane.CLOSED_OPTION) {
							return;
						} else if(answer == JOptionPane.YES_OPTION) {
							Main.LoginUser = null;
							JOptionPane.showMessageDialog(background, "로그아웃되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
						} else {
							return;
						}
						dispose();
						new EcoMoney();
					}

				}
			});
			button.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					JButton button = (JButton) e.getSource();
					if (button.getIcon() != null) {
						if (button.getIcon().equals(saveButtonBasicImage)) {
							button.setIcon(saveButtonEnteredImage);
						} else if (button.getIcon().equals(toBankButtonBasicImage)) {
							button.setIcon(toBankButtonEnteredImage);
						} else if (button.getIcon().equals(donateButtonBasicImage)) {
							button.setIcon(donateButtonEnteredImage);
						} else if (button.getIcon().equals(tradeButtonBasicImage)) {
							button.setIcon(tradeButtonEnteredImage);
						} else if (button.getIcon().equals(rankButtonBasicImage)) {
							button.setIcon(rankButtonEnteredImage);
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
						if (button.getIcon().equals(saveButtonEnteredImage)) {
							button.setIcon(saveButtonBasicImage);
						} else if (button.getIcon().equals(toBankButtonEnteredImage)) {
							button.setIcon(toBankButtonBasicImage);
						} else if (button.getIcon().equals(donateButtonEnteredImage)) {
							button.setIcon(donateButtonBasicImage);
						} else if (button.getIcon().equals(tradeButtonEnteredImage)) {
							button.setIcon(tradeButtonBasicImage);
						} else if (button.getIcon().equals(rankButtonEnteredImage)) {
							button.setIcon(rankButtonBasicImage);
						}
					}
					
				}

				@Override
				public void mousePressed(MouseEvent e) {

				}
			});

			background.add(button);
		}

		background.add(labelNick);
		
		labelNick.setFont(font28);
		labelNick.setHorizontalAlignment(JLabel.CENTER);
		
		labelNick.setBounds(90, 120, 100, 80);
		myPageButton.setBounds(373, 125, 116, 116);
		saveButton.setBounds(192, 275, 240, 85);
		toBankButton.setBounds(192, 352, 240, 85);
		donateButton.setBounds(192, 429, 240, 85);
		tradeButton.setBounds(192, 506, 240, 85);
		rankButton.setBounds(192, 583, 240, 85);
		revaliButton.setBounds(475, 665, 22, 20);
		logoutButton.setBounds(118, 665, 20, 20);

		setContentPane(background);
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

}
