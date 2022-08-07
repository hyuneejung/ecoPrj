package myPackage;

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
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.Main;
import util.MiniConn;

public class ModifyQuit extends JFrame {

	private Connection conn = null;

	private ImageIcon modifyButtonBasicImage = new ImageIcon(Main.class.getResource("../images/modifyBasic.png"));
	private ImageIcon modifyButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/modifyEntered.png"));
	private ImageIcon quitYnButtonBasicImage = new ImageIcon(Main.class.getResource("../images/quitYnButtonBasic.png"));
	private ImageIcon quitYnButtonEnteredImage = new ImageIcon(
			Main.class.getResource("../images/quitYnButtonEntered.png"));

	private Image nickModifyImage = new ImageIcon(Main.class.getResource("../images/nickModifyImage.png")).getImage();
	private Image changePwdImage = new ImageIcon(Main.class.getResource("../images/changePwdImage.png")).getImage();
	private Image quitImage = new ImageIcon(Main.class.getResource("../images/quitImage.png")).getImage();

	private JButton modifyButton = new JButton(modifyButtonBasicImage);
	private JButton quitYnButton = new JButton(quitYnButtonBasicImage);

	private JTextField nickInput = new JTextField("닉네임");
	private JPasswordField pwdInput = new JPasswordField("비밀번호");
	private JPasswordField pwd2Input = new JPasswordField("비밀번호");

	private JTextField[] inputs = { nickInput };
	private JTextField[] inputs2 = { pwdInput, pwd2Input };

	public boolean nickFlag = false;
	public boolean pwdUpdate = false;
	public boolean quitCheck = false;

	public ModifyQuit() {

		setTitle("회원 탈퇴");
		JPanel background = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(quitImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};

		background.setLayout(null);

		pwdInput.setBounds(270, 132, 127, 23);
		pwd2Input.setBounds(270, 172, 127, 23);

		// JTextField에 포커스 리스너 추가
		for (JTextField input : inputs2) {
			input.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					JTextField textField = (JTextField) e.getSource();
					if (textField.getText().equals("")) {
						if (textField.equals(pwdInput)) {
							textField.setText("비밀번호");
						} else if (textField.equals(pwd2Input)) {
							textField.setText("비밀번호");
						}
					}
				}

				@Override
				public void focusGained(FocusEvent e) {
					JTextField textField = (JTextField) e.getSource();
					if (textField.getText().equals("비밀번호")) {
						textField.setText("");
					} else if (textField.getText().equals("비밀번호")) {
						textField.setText("");
					}
				}
			});
		}

		quitYnButton.setBorderPainted(false);
		quitYnButton.setFocusPainted(false);
		quitYnButton.setContentAreaFilled(false);
		quitYnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Main.LoginUser.getPwd().equals(pwdInput.getText())) {
					if (pwdInput.getText().equals(pwd2Input.getText())) {
						updateQuit();
						if (quitCheck = true) {
							JOptionPane.showMessageDialog(background, "성공적으로 탈퇴되었습니다. 안녕히 가세요.", "안내",
									JOptionPane.INFORMATION_MESSAGE);
							System.exit(0);
						} else {
							JOptionPane.showMessageDialog(background, "탈퇴 과정에서 오류가 발생했습니다.", "오류",
									JOptionPane.WARNING_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(background, "두 비밀번호가 일치하지 않습니다.", "오류",
								JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(background, "비밀번호가 틀립니다.", "오류", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		quitYnButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				JButton button = (JButton) e.getSource();
				if (button.getIcon() != null) {
					if (button.getIcon().equals(quitYnButtonBasicImage)) {
						button.setIcon(quitYnButtonEnteredImage);
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
					if (button.getIcon().equals(quitYnButtonEnteredImage)) {
						button.setIcon(quitYnButtonBasicImage);
					}
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}
		});

		quitYnButton.setBounds(195, 220, 149, 65);

		background.add(pwdInput);
		background.add(pwd2Input);
		background.add(quitYnButton);

		setContentPane(background);
		setSize(545, 395);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	private boolean updateQuit() {

		String id = Main.LoginUser.getId();

		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;

		String sql = "UPDATE ECO_MEMBER SET QUIT_YN = 'Y' WHERE ID = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			int result = pstmt.executeUpdate();

			if (result == 1) {
				Main.LoginUser.setQuit_yn('Y');
				conn.commit();
				quitCheck = true;
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			MiniConn.rollback(conn);
		} finally {
			MiniConn.close(pstmt, conn);
		}

		return quitCheck;

	}

}
