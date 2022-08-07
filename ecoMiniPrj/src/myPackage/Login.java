package myPackage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.ScrollPane;
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
import member.EcoDto;
import util.MiniConn;

@SuppressWarnings("serial")
public class Login extends JFrame {
	private JTextField idInput = new JTextField("아이디");
	private JPasswordField pwdInput = new JPasswordField("비밀번호");

	private ImageIcon LoginButtonBasicImage = new ImageIcon(Main.class.getResource("../images/login2ButtonBasic.png"));
	private ImageIcon LoginButtonEnteredImage = new ImageIcon(
			Main.class.getResource("../images/login2ButtonEntered.png"));
	private ImageIcon FindIdButtonImage = new ImageIcon(Main.class.getResource("../images/findIdButton.png"));
	private ImageIcon FindPwdButtonImage = new ImageIcon(Main.class.getResource("../images/findPwdButton.png"));
	private ImageIcon backSpaceButtonImage = new ImageIcon(main.Main.class.getResource("../images/backSpaceButton.png"));

	private Image loginImage = new ImageIcon(Main.class.getResource("../images/loginImage.png")).getImage();

	private JButton backSpaceButton = new JButton(backSpaceButtonImage);
	private JButton loginButton = new JButton(LoginButtonBasicImage);
	private JButton findIdButton = new JButton(FindIdButtonImage);
	private JButton findPwdButton = new JButton(FindPwdButtonImage);

	private JButton[] buttons = { loginButton, findIdButton, findPwdButton, backSpaceButton };

	private Connection conn = null;

	public Login() {

		setTitle("Login");
		JPanel background = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(loginImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		background.setLayout(null);

		idInput.setBounds(211, 143, 127, 23);
		pwdInput.setBounds(211, 183, 127, 23);

		idInput.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				JTextField textField = (JTextField) e.getSource();
				if (textField.getText().equals("")) {
					textField.setText("아이디");
				}

			}

			@Override
			public void focusGained(FocusEvent e) {
				JTextField textField = (JTextField) e.getSource();
				if (textField.getText().equals("아이디")) {
					textField.setText("");
				}
			}
		});

		pwdInput.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				JPasswordField pwField = (JPasswordField) e.getSource();
				if (pwField.getText().equals("")) {
					pwField.setText("비밀번호");
				}

			}

			@Override
			public void focusGained(FocusEvent e) {
				JPasswordField pwField = (JPasswordField) e.getSource();
				if (pwField.getText().equals("비밀번호")) {
					pwField.setText("");
				}

			}
		});

		for (JButton button : buttons) {
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setContentAreaFilled(false);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton) e.getSource();
					if (button.equals(loginButton)) {
						String id = idInput.getText();
						String pw = pwdInput.getText();
						int result = login(id, pw);

						if (result == 1) {
							JOptionPane.showMessageDialog(background, "로그인 성공", "알림", JOptionPane.INFORMATION_MESSAGE);
							dispose();
							new MenuPanel();
						} else if (result == 0) {
							JOptionPane.showMessageDialog(background, "비밀번호가 올바르지 않습니다!", "오류",
									JOptionPane.WARNING_MESSAGE);
						} else if (result == -1) {
							JOptionPane.showMessageDialog(background, "없는 ID입니다!", "오류",
									JOptionPane.WARNING_MESSAGE);
						}
					} else if (button.equals(findIdButton)) {
						new SearchPanel().searchId();
					} else if (button.equals(findPwdButton)) {
						new SearchPanel().searchPwd();
					} else if (button.equals(backSpaceButton)) {
						dispose();
						new EcoMoney();
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
						if (button.getIcon().equals(LoginButtonBasicImage)) {
							button.setIcon(LoginButtonEnteredImage);
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
						if (button.getIcon().equals(LoginButtonEnteredImage)) {
							button.setIcon(LoginButtonBasicImage);
						}
					}

				}

				@Override
				public void mousePressed(MouseEvent e) {

				}
			});
			background.add(button);
		}
		
		backSpaceButton.setBounds(95, 81, 39, 28);
		loginButton.setBounds(154, 293, 149, 65);
		findIdButton.setBounds(180, 232, 96, 13);
		findPwdButton.setBounds(175, 255, 106, 13);

		background.add(idInput);
		background.add(pwdInput);

		setContentPane(background);
		setSize(465, 468);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private int login(String id, String pw) {

		conn = MiniConn.getConnection();

		PreparedStatement pstmt = null;
		ResultSet result = null;
		String sql = "SELECT E.NO, E.ID, NAME, E.PWD, E.NICK, E.GENDER, E.PHONE, B.BANKNAME , E.ACCOUNT, E.POINT, E.ADDEDPOINT, E.ENROLL_DATE, E.RANKNO, R.RANK\r\n"
				+ "FROM ECO_MEMBER E\r\n" + "JOIN BANKCODE B ON E.BANKNO = B.NO\r\n"
				+ "JOIN RANKSYS R ON E.RANKNO = R.NO\r\n" + "WHERE E.QUIT_YN = 'N' AND ID = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			result = pstmt.executeQuery();

			if (result.next()) {
				if (result.getString("PWD").equals(pw)) {
					EcoDto ecoLogin = new EcoDto();
					ecoLogin.setId(result.getString("ID"));
					ecoLogin.setPwd(result.getString("PWD"));
					ecoLogin.setName(result.getString("NAME"));
					ecoLogin.setNick(result.getString("NICK"));
					ecoLogin.setGender(result.getString("GENDER"));
					ecoLogin.setPhone(result.getString("PHONE"));
					ecoLogin.setBankName(result.getString("BANKNAME"));
					ecoLogin.setAccount(result.getString("ACCOUNT"));
					ecoLogin.setPoint(result.getInt("POINT"));
					ecoLogin.setAddedPoint(result.getInt("ADDEDPOINT"));
					ecoLogin.setEnroll_date(result.getTimestamp("ENROLL_DATE"));
					ecoLogin.setRank(result.getInt("RANKNO"));
					ecoLogin.setRankName(result.getString("RANK"));
					Main.LoginUser = ecoLogin;
					return 1; // 성공
				} else {
					return 0; // 비밀번호 불일치
				}
			}
			return -1; // 없는 아이디
		} catch (SQLException e) {
			e.printStackTrace();
			MiniConn.rollback(conn);
			return -2;
		} finally {
			MiniConn.close(pstmt, result, conn);
		}
	}

}
