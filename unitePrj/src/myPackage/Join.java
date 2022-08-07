package myPackage;

import java.awt.Color;
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
import java.sql.Timestamp;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DocumentFilter;

import main.Main;
import member.EcoDto;
import util.MiniConn;

@SuppressWarnings("serial")
public class Join extends JFrame {
	private String[] bank_names = {"국민", "농협", "신한",  "신협", "카카오뱅크"};
	
	private JTextField idInput = new JTextField("아이디");
	private JPasswordField pwdInput = new JPasswordField("비밀번호");
	private JPasswordField pwdCheckInput = new JPasswordField("비밀번호");
	private JTextField nameInput = new JTextField("이름");
	private JTextField nickInput = new JTextField("닉네임");
	private JTextField phoneInput = new JTextField("휴대폰 번호");
	private JTextField bankInput = new JTextField("계좌번호");
	private JComboBox<String> bank_name_combobox = new JComboBox<>(bank_names);
	// bank_no 만들기 (선택지 주고 선택할 수 있게)

	private ImageIcon RedCheckButtonImage = new ImageIcon(Main.class.getResource("../images/redCheckButton.png"));
	private ImageIcon JoinButtonBasicImage = new ImageIcon(Main.class.getResource("../images/joinButtonBasic.png"));
	private ImageIcon JoinButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/joinButtonEntered.png"));

	private JTextField[] inputs = { idInput, pwdInput, pwdCheckInput, nameInput, nickInput, phoneInput, bankInput };

	private Image joinImage = new ImageIcon(Main.class.getResource("../images/joinImage.png")).getImage();

	private JButton idRedCheckButton = new JButton(RedCheckButtonImage);
	private JButton nickRedCheckButton = new JButton(RedCheckButtonImage);
	private JButton joinButton = new JButton(JoinButtonBasicImage);

	private JButton[] buttons = { idRedCheckButton, nickRedCheckButton, joinButton };
	
	private JRadioButton maleRadioButton = new JRadioButton("M");
	private JRadioButton femaleRadioButton = new JRadioButton("F");
	private JRadioButton[] radioButtons = { maleRadioButton, femaleRadioButton };
	
	ButtonGroup genderGroup = new ButtonGroup();
	
	private boolean idFlag = false;
	private boolean nickFlag = false;

	private Connection conn = null;

	public Join() {

		setTitle("Join");
		JPanel background = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(joinImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		background.setLayout(null);
		
		for (JRadioButton JRadioButton : radioButtons) {
			JRadioButton.setFocusPainted(false);
			JRadioButton.setContentAreaFilled(false);
			JRadioButton.setFont(Main.font);
			genderGroup.add(JRadioButton);
		}
		
		maleRadioButton.setSelected(true);
		
		idInput.setBounds(218, 112, 163, 29);
		pwdInput.setBounds(218, 158, 163, 29);
		pwdCheckInput.setBounds(218, 203, 163, 29);
		nameInput.setBounds(218, 297, 163, 29);
		maleRadioButton.setBounds(400, 297, 40, 29);
		femaleRadioButton.setBounds(450, 297, 40, 29);
		nickInput.setBounds(218, 343, 163, 29);
		phoneInput.setBounds(218, 388, 163, 29);
		bankInput.setBounds(218, 435, 163, 29);
		bank_name_combobox.setBounds(400, 435, 80, 29);

		// JTextField에 포커스 리스너 추가
		for (JTextField input : inputs) {
			input.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					JTextField textField = (JTextField) e.getSource();
					if (textField.getText().equals("")) {
						if (textField.equals(idInput)) {
							textField.setText("아이디");
						} else if (textField.equals(pwdInput)) {
							textField.setText("비밀번호");
						} else if (textField.equals(pwdCheckInput)) {
							textField.setText("비밀번호");
						} else if (textField.equals(nameInput)) {
							textField.setText("이름");
						} else if (textField.equals(nickInput)) {
							textField.setText("닉네임");
						} else if (textField.equals(phoneInput)) {
							textField.setText("휴대폰 번호");
						} else if (textField.equals(bankInput)) {
							textField.setText("계좌번호");
						}
					}
				}

				@Override
				public void focusGained(FocusEvent e) {
					JTextField textField = (JTextField) e.getSource();
					if (textField.getText().equals("아이디")) {
						textField.setText("");
					} else if (textField.getText().equals("비밀번호")) {
						textField.setText("");
					} else if (textField.getText().equals("비밀번호")) {
						textField.setText("");
					} else if (textField.getText().equals("이름")) {
						textField.setText("");
					} else if (textField.getText().equals("닉네임")) {
						textField.setText("");
					} else if (textField.getText().equals("휴대폰 번호")) {
						textField.setText("");
					} else if (textField.getText().equals("계좌번호")) {
						textField.setText("");
					}
				}
				
				
			});
			
			if (input.equals(idInput)) {
				input.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
					}
				});
				
				input.getDocument().addDocumentListener(new DocumentListener() {
					
					@Override
					public void removeUpdate(DocumentEvent e) {
						idFlag = false;
					}
					
					@Override
					public void insertUpdate(DocumentEvent e) {
						idFlag = false;
						
					}
					
					@Override
					public void changedUpdate(DocumentEvent e) {
						idFlag = false;
						
					}
				});
			}
			else if(input.equals(nickInput)) {
				input.getDocument().addDocumentListener(new DocumentListener() {
					
					@Override
					public void removeUpdate(DocumentEvent e) {
						nickFlag = false;
						
					}
					
					@Override
					public void insertUpdate(DocumentEvent e) {
						nickFlag = false;
						
					}
					
					@Override
					public void changedUpdate(DocumentEvent e) {
						nickFlag = false;
					}
				});
			}
			
			background.add(input);
		}
		
		for (JButton button : buttons) {
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setContentAreaFilled(false);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton) e.getSource();
					if (button.equals(idRedCheckButton)) {
						checkID(idInput.getText());
						if(idFlag) {
							JOptionPane.showMessageDialog(background, "사용가능한 ID 입니다.", "중복확인", JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(background, "이미 사용 중인 ID 입니다.", "중복확인", JOptionPane.WARNING_MESSAGE);
						}
					} else if (button.equals(nickRedCheckButton)) {
						checkNick(nickInput.getText());
						if(nickFlag) {
							JOptionPane.showMessageDialog(background, "사용가능한 닉네임 입니다.", "중복확인", JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(background, "이미 사용 중인 닉네임 입니다.", "중복확인", JOptionPane.WARNING_MESSAGE);
						}
					} else if (button.equals(joinButton)) {
						int result = join();
						if(result > 0) {
							JOptionPane.showMessageDialog(background, "회원가입이 완료되었습니다!", "중복확인", JOptionPane.INFORMATION_MESSAGE);
							dispose();
						} else if(result == -2) {
							JOptionPane.showMessageDialog(background, "아이디 또는 닉네임 중복확인을 해주세요!", "오류", JOptionPane.WARNING_MESSAGE);
						} else if(result == -3) {
							JOptionPane.showMessageDialog(background, "패스워드가 일치하지 않습니다!", "오류", JOptionPane.WARNING_MESSAGE);
						}
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
						if (button.getIcon().equals(JoinButtonBasicImage)) {
							button.setIcon(JoinButtonEnteredImage);
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
						if (button.getIcon().equals(JoinButtonEnteredImage)) {
							button.setIcon(JoinButtonBasicImage);
						}
					}

				}

				@Override
				public void mousePressed(MouseEvent e) {

				}
			});
			background.add(button);
		}
		
		background.add(bank_name_combobox);
		
		bank_name_combobox.setBackground(Color.white);
		bank_name_combobox.setFont(Main.font);
		
		background.add(maleRadioButton);
		background.add(femaleRadioButton);

		idRedCheckButton.setBounds(405, 98, 104, 58);
		nickRedCheckButton.setBounds(405, 330, 104, 58);
		joinButton.setBounds(187, 510, 240, 86);

		setContentPane(background);
		setSize(616, 730);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	
	private int join() {
		String sql = "INSERT INTO ECO_MEMBER (NO, NAME, NICK, GENDER, ID, PWD, PHONE, BANKNO, ACCOUNT) VALUES (SEQ_ECO_MEMBER_NO.NEXTVAL,?,?,?,?,?,?,?,?)";
		int result = 0;
		
		if(!idFlag || !nickFlag) {
			return -2;
		} else if(!pwdCheckInput.getText().equals(pwdInput.getText())) {
			return -3;
		}
		
		EcoDto memberdto = new EcoDto();
		
		char gender = 'M';
		
		if(femaleRadioButton.isSelected()) {
			gender = 'F';
		}
		
		memberdto.setName(nameInput.getText());
		memberdto.setNick(nickInput.getText());
		memberdto.setGender(String.valueOf(gender));
		memberdto.setId(idInput.getText());
		memberdto.setPwd(pwdInput.getText());
		memberdto.setPhone(phoneInput.getText());
		memberdto.setBankNo(bank_name_combobox.getSelectedIndex() + 1);
		memberdto.setAccount(bankInput.getText());
				
		
		System.out.println(memberdto);
		
		conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberdto.getName());
			pstmt.setString(2, memberdto.getNick());
			pstmt.setString(3, String.valueOf(memberdto.getGender()));
			pstmt.setString(4, memberdto.getId());
			pstmt.setString(5, memberdto.getPwd());
			pstmt.setString(6, memberdto.getPhone());
			pstmt.setInt(7, memberdto.getBankNo());
			pstmt.setString(8, memberdto.getAccount());

			result = pstmt.executeUpdate();
			
			System.out.println(result);

			if (result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (SQLException e) {
			MiniConn.rollback(conn);
			System.out.println("회원가입 중 예외 발생 !");
			e.printStackTrace();

		} finally {
			MiniConn.close(conn);
			MiniConn.close(pstmt);
		}
		
		return result;
	}
	
	private void checkID(String id) {
		conn = MiniConn.getConnection();
		
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM ECO_MEMBER WHERE ID = ?";
		ResultSet result = null;
		String checkId = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			result = pstmt.executeQuery();
			while (result.next()) {
				checkId = result.getString("ID");
				if(checkId.equals(id)) {
					return;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			MiniConn.rollback(conn);
		} finally {
			MiniConn.close(pstmt, result, conn);
		}
		
		idFlag = true;
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
				if(checkNick.equals(nick)) {
					return;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			MiniConn.rollback(conn);
		} finally {
			MiniConn.close(pstmt, result, conn);
		}
		
		nickFlag = true;
	}
	

}