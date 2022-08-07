package myPackage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import javax.swing.JTextField;

import main.Main;
import util.InputUtil;
import util.MiniConn;

@SuppressWarnings("serial")
public class SearchPanel extends JFrame {

	private ImageIcon findButtonBasicImage = new ImageIcon(Main.class.getResource("../images/findButtonBasic.png"));
	private ImageIcon findButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/findButtonEntered.png"));

	private Image searchIdImage = new ImageIcon(Main.class.getResource("../images/searchIdImage.png")).getImage();
	private Image searchPwdImage = new ImageIcon(Main.class.getResource("../images/searchPwdImage.png")).getImage();

	private JTextField nameInput = new JTextField("이름");
	private JTextField idInput = new JTextField("아이디");
	private JTextField phoneInput = new JTextField("휴대폰 번호");
	private JTextField numInput = new JTextField("인증번호");

	private JTextField[] inputsId = { nameInput, phoneInput, numInput };
	private JTextField[] inputsPw = { nameInput, idInput, phoneInput, numInput };

	private JButton searchButton = new JButton(findButtonBasicImage);
	private JLabel labelNum = new JLabel(InputUtil.randomNum(6, 2));

	private Connection conn = null;

	public void searchId() {

		setTitle("ID 찾기");
		JPanel background = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(searchIdImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};

		background.setLayout(null);

		// 포커스 리스너 추가
		for (JTextField input : inputsId) {
			input.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					JTextField textField = (JTextField) e.getSource();
					if (textField.getText().equals("")) {
						if (textField.equals(nameInput)) {
							textField.setText("이름");
						} else if (textField.equals(phoneInput)) {
							textField.setText("휴대폰 번호");
						} else if (textField.equals(numInput)) {
							textField.setText("인증번호");
						}
					}

				}

				@Override
				public void focusGained(FocusEvent e) {
					JTextField textField = (JTextField) e.getSource();
					if (textField.getText().equals("이름")) {
						textField.setText("");
					} else if (textField.getText().equals("휴대폰 번호")) {
						textField.setText("");
					} else if (textField.getText().equals("인증번호")) {
						textField.setText("");
					}

				}
			});
		}
		searchButton.setBorderPainted(false);
		searchButton.setFocusPainted(false);
		searchButton.setContentAreaFilled(false);
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameInput.getText();
				String phone = phoneInput.getText();
				String result = searchID(name, phone);

				if (rightNum(numInput.getText()) == true) {
					if (result != null) {
						JOptionPane.showMessageDialog(background, "귀하의 아이디는 " + result + "입니다.", "안내",
								JOptionPane.INFORMATION_MESSAGE);
						dispose();
					} else {
						JOptionPane.showMessageDialog(background, "일치하는 ID가 없습니다.", "오류",
								JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(background, "인증번호가 일치하지 않습니다.", "오류",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		nameInput.setBounds(212, 101, 120, 25);
		phoneInput.setBounds(212, 142, 120, 25);
		numInput.setBounds(212, 228, 120, 25);
		searchButton.setBounds(150, 262, 149, 65);

		labelNum.setBounds(208, 181, 120, 25);
		labelNum.setHorizontalAlignment(JLabel.CENTER);

		background.add(nameInput);
		background.add(phoneInput);
		background.add(numInput);
		background.add(searchButton);
		background.add(labelNum);

		setContentPane(background);
		setSize(466, 440);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	private String searchID(String name, String phone) {
		conn = MiniConn.getConnection();

		PreparedStatement pstmt = null;
		ResultSet result = null;
		String sql = "SELECT ID, PHONE FROM ECO_MEMBER WHERE NAME = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			result = pstmt.executeQuery();

			if (result.next()) {
				if (result.getString("PHONE").equals(phone)) {
					return result.getString("ID");
				} else {
					return null;
				}
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			MiniConn.rollback(conn);
			return null;
		} finally {
			MiniConn.close(pstmt, result, conn);
		}
	}

	private boolean rightNum(String input) {
		String num = labelNum.getText();
		if (input.equals(num)) {
			return true;
		} else {
			return false;
		}
	}

	public void searchPwd() {
		setTitle("PW 찾기");
		JPanel background = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(searchPwdImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		background.setLayout(null);

		searchButton.setBorderPainted(false);
		searchButton.setFocusPainted(false);
		searchButton.setContentAreaFilled(false);
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String id = idInput.getText();
				String name = nameInput.getText();
				String phone = phoneInput.getText();
				boolean result = searchPW(id, name, phone);
				if (rightNum(numInput.getText()) == true) {
					if (result == true) {
						JOptionPane.showMessageDialog(background, "변경할 비밀번호를 입력해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
						new ModifyPwd(id);
						dispose();
					} else {
						JOptionPane.showMessageDialog(background, "정보와 일치하는 계정을 찾을 수 없습니다.", "오류",
								JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(background, "인증번호가 일치하지 않습니다.", "오류",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});

		// 포커스 리스너 추가
		for (JTextField input : inputsPw) {
			input.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					JTextField textField = (JTextField) e.getSource();
					if (textField.getText().equals("")) {
						if (textField.equals(idInput)) {
							textField.setText("아이디");
						} else if (textField.equals(nameInput)) {
							textField.setText("이름");
						} else if (textField.equals(phoneInput)) {
							textField.setText("휴대폰 번호");
						} else if (textField.equals(numInput)) {
							textField.setText("인증번호");
						}
					}

				}

				@Override
				public void focusGained(FocusEvent e) {
					JTextField textField = (JTextField) e.getSource();
					if (textField.getText().equals("아이디")) {
						textField.setText("");
					} else if (textField.getText().equals("이름")) {
						textField.setText("");
					} else if (textField.getText().equals("휴대폰 번호")) {
						textField.setText("");
					} else if (textField.getText().equals("인증번호")) {
						textField.setText("");
					}

				}
			});
		}

		nameInput.setBounds(213, 102, 126, 23);
		idInput.setBounds(213, 141, 126, 23);
		phoneInput.setBounds(213, 180, 126, 23);
		numInput.setBounds(213, 269, 126, 23);
		searchButton.setBounds(158, 308, 149, 65);
		
		labelNum.setBounds(208, 222, 120, 25);
		labelNum.setHorizontalAlignment(JLabel.CENTER);

		background.add(nameInput);
		background.add(idInput);
		background.add(phoneInput);
		background.add(numInput);
		background.add(labelNum);
		background.add(searchButton);

		setContentPane(background);
		setSize(463, 488);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	private boolean searchPW(String id, String name, String phone) {
		conn = MiniConn.getConnection();

		PreparedStatement pstmt = null;
		ResultSet result = null;
		String sql = "SELECT PWD, NAME, PHONE FROM ECO_MEMBER WHERE ID = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			result = pstmt.executeQuery();

			if (result.next()) {
				if (result.getString("NAME").equals(name)) {
					if (result.getString("PHONE").equals(phone)) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			MiniConn.rollback(conn);
			return false;
		} finally {
			MiniConn.close(pstmt, result, conn);
		}
	}

}
