package myPackage;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.Main;
import util.InputUtil;
import util.MiniConn;

public class MyPagePanel extends JFrame {

	private ImageIcon nickModifyButtonBasicImage = new ImageIcon(
			Main.class.getResource("../images/nickModifyBasic.png"));
	private ImageIcon nickModifyButtonEnteredImage = new ImageIcon(
			Main.class.getResource("../images/nickModifyEntered.png"));
	private ImageIcon pwdModifyButtonBasicImage = new ImageIcon(Main.class.getResource("../images/pwdModifyBasic.png"));
	private ImageIcon pwdModifyButtonEnteredImage = new ImageIcon(
			Main.class.getResource("../images/pwdModifyEntered.png"));
	private ImageIcon quitButtonBasicImage = new ImageIcon(Main.class.getResource("../images/quitButtonBasic.png"));
	private ImageIcon quitButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/quitButtonEntered.png"));
	private ImageIcon returnButtonBasicImage = new ImageIcon(Main.class.getResource("../images/returnButtonBasic.png"));
	private ImageIcon returnButtonEnteredImage = new ImageIcon(
			Main.class.getResource("../images/returnButtonEntered.png"));
	private ImageIcon revaliButtonBasicImage = new ImageIcon(Main.class.getResource("../images/revaliButton.png"));
	private ImageIcon pointButtonImage = new ImageIcon(Main.class.getResource("../images/pointButton.png"));
	
	private ImageIcon seedImage = new ImageIcon(Main.class.getResource("../images/seedIcon.png"));
	private ImageIcon sproutImage = new ImageIcon(Main.class.getResource("../images/sproutIcon.png"));
	private ImageIcon branchImage = new ImageIcon(Main.class.getResource("../images/branchIcon.png"));
	private ImageIcon appleImage = new ImageIcon(Main.class.getResource("../images/appleIcon.png"));
	private ImageIcon treeImage = new ImageIcon(Main.class.getResource("../images/treeIcon.png"));

	private JButton nickModifyButton = new JButton(nickModifyButtonBasicImage);
	private JButton pwdModifyButton = new JButton(pwdModifyButtonBasicImage);
	private JButton quitButton = new JButton(quitButtonBasicImage);
	private JButton pointButton = new JButton(pointButtonImage);
	private JButton returnButton = new JButton(returnButtonBasicImage);
	private JButton revaliButton = new JButton(revaliButtonBasicImage);

	private JLabel labelName = new JLabel(Main.LoginUser.getName());
	private JLabel labelNick = new JLabel(Main.LoginUser.getNick());
	private JLabel labelEnroll_Date = new JLabel(String.valueOf(Main.LoginUser.getEnroll_date()));
	private JLabel labelId = new JLabel(Main.LoginUser.getId());
	private JLabel labelPhone = new JLabel(Main.LoginUser.getPhone());
	private JLabel labelPoint = new JLabel(String.valueOf(Main.LoginUser.getPoint()));
	private JLabel labelAddedPoint = new JLabel(String.valueOf(Main.LoginUser.getAddedPoint()));
	private JLabel labelBank = new JLabel(Main.LoginUser.getBankName());
	private JLabel labelAccount = new JLabel(Main.LoginUser.getAccount());
	
	private JLabel labelRank = null;

	private JLabel[] labels = { labelName, labelNick, labelEnroll_Date, labelId, labelPhone, labelPoint,
			labelAddedPoint, labelBank, labelAccount };

	private Image MyPageImage = new ImageIcon(Main.class.getResource("../images/myPageImage.png")).getImage();

	private JButton[] buttons = { nickModifyButton, pwdModifyButton, quitButton, returnButton, revaliButton, pointButton };
	
	private Connection conn = null;

	public MyPagePanel() {

		main.Main.updateUser();
		
		setTitle("this.eco = money;");

		JPanel background = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(MyPageImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		background.setLayout(null);
		
		if (Main.LoginUser.getRankName().equals("씨앗")) {
			labelRank = new JLabel("", seedImage, JLabel.CENTER);
		} else if (Main.LoginUser.getRankName().equals("새싹")) {
			labelRank = new JLabel("", sproutImage, JLabel.CENTER);
		} else if (Main.LoginUser.getRankName().equals("가지")) {
			labelRank = new JLabel("", branchImage, JLabel.CENTER);
		} else if (Main.LoginUser.getRankName().equals("열매")) {
			labelRank = new JLabel("", appleImage, JLabel.CENTER);
		} else if (Main.LoginUser.getRankName().equals("꽃")) {
			labelRank = new JLabel("", treeImage, JLabel.CENTER);
		}

		for (JButton button : buttons) {
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setContentAreaFilled(false);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton) e.getSource();
					if (button.equals(nickModifyButton)) {
						new ModifyNick();
					} else if (button.equals(pwdModifyButton)) {
						new ModifyPwd(Main.LoginUser.getId());
					} else if (button.equals(quitButton)) {
						new ModifyQuit();
					} else if (button.equals(returnButton)) {
						dispose();
						new MenuPanel();
					} else if (button.equals(revaliButton)) {
						updateLabel();
					} else if (button.equals(pointButton)) {
						new ShowHistoryPanel();
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
						if (button.getIcon().equals(nickModifyButtonBasicImage)) {
							button.setIcon(nickModifyButtonEnteredImage);
						} else if (button.getIcon().equals(pwdModifyButtonBasicImage)) {
							button.setIcon(pwdModifyButtonEnteredImage);
						} else if (button.getIcon().equals(quitButtonBasicImage)) {
							button.setIcon(quitButtonEnteredImage);
						} else if (button.getIcon().equals(returnButtonBasicImage)) {
							button.setIcon(returnButtonEnteredImage);
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
						if (button.getIcon().equals(nickModifyButtonEnteredImage)) {
							button.setIcon(nickModifyButtonBasicImage);
						} else if (button.getIcon().equals(pwdModifyButtonEnteredImage)) {
							button.setIcon(pwdModifyButtonBasicImage);
						} else if (button.getIcon().equals(quitButtonEnteredImage)) {
							button.setIcon(quitButtonBasicImage);
						} else if (button.getIcon().equals(returnButtonEnteredImage)) {
							button.setIcon(returnButtonBasicImage);
						}
					}

				}

				@Override
				public void mousePressed(MouseEvent e) {

				}
			});

			background.add(button);
		}
		
		for (JLabel label : labels) {
			background.add(label);
			label.setFont(new Font("AppleSDGothicNeoUL00", Font.PLAIN, 16));
		}
		
		background.add(labelRank);
		
		labelName.setBounds(313, 129, 80, 21);
		labelNick.setBounds(320, 166, 85, 21);
		labelEnroll_Date.setBounds(335, 204, 100, 21);
		labelId.setBounds(179, 270, 185, 21); 
		labelPoint.setBounds(179, 324, 96, 21);
		labelAddedPoint.setBounds(396, 324, 96, 21);
		labelPhone.setBounds(210, 380, 282, 21);
		labelBank.setBounds(210, 433, 104, 21);
		labelAccount.setBounds(270, 433, 104, 21);
		labelRank.setBounds(395, 130, 25, 20);
		
		nickModifyButton.setBounds(412, 151, 104, 58);
		pwdModifyButton.setBounds(188, 463, 230, 77);
		quitButton.setBounds(188, 536, 230, 77);
		returnButton.setBounds(188, 609, 230, 77);
		revaliButton.setBounds(475, 665, 22, 20);
		pointButton.setBounds(109, 326, 48, 17);

		setContentPane(background);
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

	}
	
	public String getUserNick() {		
		conn = MiniConn.getConnection();
		
		PreparedStatement pstmt = null;
		String sql = "SELECT NICK FROM ECO_MEMBER WHERE ID = ?";
		ResultSet result = null;
		
		String nick = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, main.Main.LoginUser.getId());
			result = pstmt.executeQuery();
			if(result.next()) {
				nick = result.getString("NICK");
			} 
		} catch(SQLException e) {
			
		} finally {
			MiniConn.close(pstmt, result, conn);
		}
		return nick;
	}
	
	public void updateLabel() {
		
		labelName.setText(Main.LoginUser.getName());
		labelNick.setText(Main.LoginUser.getNick());
		labelEnroll_Date.setText(String.valueOf(Main.LoginUser.getEnroll_date()));
		labelId.setText(Main.LoginUser.getId());
		labelPhone.setText(Main.LoginUser.getPhone());
		labelPoint.setText(String.valueOf(Main.LoginUser.getPoint()));
		labelAddedPoint.setText(String.valueOf(Main.LoginUser.getAddedPoint()));
		labelBank.setText(Main.LoginUser.getBankName());
		labelAccount.setText(Main.LoginUser.getAccount());
		labelRank.revalidate();
	}

}
