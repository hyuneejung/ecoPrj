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
import java.sql.ResultSet;
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

public class ModifyNick extends JFrame {

	private Connection conn = null;

	private ImageIcon modifyButtonBasicImage = new ImageIcon(Main.class.getResource("../images/modifyBasic.png"));
	private ImageIcon modifyButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/modifyEntered.png"));

	private Image nickModifyImage = new ImageIcon(Main.class.getResource("../images/nickModifyImage.png")).getImage();

	private JButton modifyButton = new JButton(modifyButtonBasicImage);

	private JTextField nickInput = new JTextField("닉네임");

	private JTextField[] inputs = { nickInput };

	public boolean nickFlag = false;

	public ModifyNick() {

		setTitle("닉네임 변경");
		JPanel background = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(nickModifyImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};

		background.setLayout(null);

		nickInput.setBounds(155, 120, 150, 30);

		// JTextField에 포커스 리스너 추가
		for (JTextField input : inputs) {
			input.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					JTextField textField = (JTextField) e.getSource();
					if (textField.getText().equals("")) {
						if (textField.equals(nickInput)) {
							textField.setText("닉네임");
						}
					}
				}

				@Override
				public void focusGained(FocusEvent e) {
					JTextField textField = (JTextField) e.getSource();
					if (textField.getText().equals("닉네임")) {
						textField.setText("");
					}
				}
			});
		}

		modifyButton.setBorderPainted(false);
		modifyButton.setFocusPainted(false);
		modifyButton.setContentAreaFilled(false);
		modifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkNick(nickInput.getText());
				if (nickFlag) {
					if (updateNick() == 1) {
						JOptionPane.showMessageDialog(background, "닉네임이 " + nickInput.getText() + "(으)로 변경되었습니다.", "안내",
								JOptionPane.INFORMATION_MESSAGE);
						Main.updateUser();
						dispose();
					} else {
						JOptionPane.showMessageDialog(background, "닉네임이 변경되지 않았습니다! 다시 시도해 주세요.", "오류",
								JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(background, "중복된 닉네임입니다. 다시 입력해 주세요.", "오류",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		modifyButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				JButton button = (JButton) e.getSource();
				if (button.getIcon() != null) {
					if (button.getIcon().equals(modifyButtonBasicImage)) {
						button.setIcon(modifyButtonEnteredImage);
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
					if (button.getIcon().equals(modifyButtonEnteredImage)) {
						button.setIcon(modifyButtonBasicImage);
					}
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}
		});

		modifyButton.setBounds(158, 193, 149, 65);

		background.add(nickInput);
		background.add(modifyButton);

		setContentPane(background);
		setSize(466, 369);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	private void checkNick(String nick) {
		conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM ECO_MEMBER WHERE NICK = ?";
		ResultSet result = null;
		String checkNick = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, nick);
			result = pstmt.executeQuery();
			while (result.next()) {
				checkNick = result.getString("NICK");
				if (checkNick.equals(nick)) {
					return;
				}
			}
		} catch (SQLException e) {
			MiniConn.rollback(conn);
		} finally {
			MiniConn.close(pstmt, result, conn);
		}

		nickFlag = true;
	}

	private int updateNick() {

		String nick = nickInput.getText();
		String id = Main.LoginUser.getId();

		// 연결
		conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;

		// SQL 작성
		String sql = "UPDATE ECO_MEMBER SET NICK = ? WHERE ID = ?";

		// SQL 객체에 담기 (+SQL 완성)
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, nick);
			pstmt.setString(2, id);

			// SQL 실행 (실행 결과 저장)
			int result = pstmt.executeUpdate();

			// 실행 결과에 따른 로직 처리
			if (result == 1) {
				Main.LoginUser.setNick(nickInput.getText());
				conn.commit();
				return 1;
			} else {
				conn.rollback();
				return 2;
			}
		} catch (SQLException e) {
			MiniConn.rollback(conn);
			return 3;
		} finally {
			MiniConn.close(pstmt, conn);
		}
	}

}
