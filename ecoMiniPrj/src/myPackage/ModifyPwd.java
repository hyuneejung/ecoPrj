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

public class ModifyPwd extends JFrame {

	private Connection conn = null;

	private ImageIcon modifyButtonBasicImage = new ImageIcon(Main.class.getResource("../images/modifyBasic.png"));
	private ImageIcon modifyButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/modifyEntered.png"));

	private Image changePwdImage = new ImageIcon(Main.class.getResource("../images/changePwdImage.png")).getImage();

	private JButton modifyButton = new JButton(modifyButtonBasicImage);

	private JPasswordField pwdInput = new JPasswordField("비밀번호");
	private JPasswordField pwd2Input = new JPasswordField("비밀번호");

	private JTextField[] inputs2 = { pwdInput, pwd2Input };

	public boolean pwdUpdate = false;

	public ModifyPwd(String id) {

		setTitle("비밀번호 변경");
		JPanel background = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(changePwdImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};

		background.setLayout(null);

		pwdInput.setBounds(225, 92, 127, 23);
		pwd2Input.setBounds(225, 132, 127, 23);

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

		modifyButton.setBorderPainted(false);
		modifyButton.setFocusPainted(false);
		modifyButton.setContentAreaFilled(false);
		modifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pwdInput.getText().equals(pwd2Input.getText())) {
					if (pwdUpdate = true) {
						updatePwd(id, pwdInput.getText());
						JOptionPane.showMessageDialog(background, "비밀번호가 성공적으로 변경되었습니다.", "안내",
								JOptionPane.INFORMATION_MESSAGE);
						dispose();
					} else {
						JOptionPane.showMessageDialog(background, "비밀번호가 변경되지 않았습니다! 다시 시도해 주세요.", "오류",
								JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(background, "두 비밀번호가 일치하지 않습니다.", "오류", JOptionPane.WARNING_MESSAGE);
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

		modifyButton.setBounds(158, 165, 149, 65);

		background.add(pwdInput);
		background.add(pwd2Input);
		background.add(modifyButton);

		setContentPane(background);
		setSize(466, 335);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	public void updatePwd(String id, String newPwd) {

		// 연결
		Connection conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;

		// SQL 작성
		String sql = "UPDATE ECO_MEMBER SET PWD = ? WHERE ID = ?";

		// SQL 객체에 담기 (+SQL 완성)
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newPwd);
			pstmt.setString(2, id);

			// SQL 실행 (실행 결과 저장)
			int result = pstmt.executeUpdate();

			// 실행 결과에 따른 로직 처리
			if (result == 1) {
				conn.commit();
				pwdUpdate = true;
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			MiniConn.rollback(conn);
		} finally {
			MiniConn.close(pstmt, conn);
		}
	}

}
