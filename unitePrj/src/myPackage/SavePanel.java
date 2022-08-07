package myPackage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cafe.CafeDto;
import dto.DonationDto;
import main.Main;
import member.EcoDto;
import util.InputUtil;
import util.MiniConn;

@SuppressWarnings("serial")
public class SavePanel extends JFrame {
	private Connection conn = null;

	private JPanel selectPanel;
	private ImageIcon backSpaceButtonImage = new ImageIcon(Main.class.getResource("../images/backSpaceButton.png"));
	private ImageIcon cupButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/cupButtonEntered.png"));
	private ImageIcon cupButtonBasicImage = new ImageIcon(Main.class.getResource("../images/cupButtonBasic.png"));
	private ImageIcon tumblrButtonEnteredImage = new ImageIcon(
			Main.class.getResource("../images/tumblrButtonEntered.png"));
	private ImageIcon tumblrButtonBasicImage = new ImageIcon(Main.class.getResource("../images/tumblrButtonBasic.png"));
	private ImageIcon coffeeBeanButtonImage = new ImageIcon(Main.class.getResource("../images/coffeeBeanButton.png"));

	private JButton backSpaceButton = new JButton(backSpaceButtonImage);
	private JButton cupButton = new JButton(cupButtonBasicImage);
	private JButton tumblrButton = new JButton(tumblrButtonBasicImage);
	private JButton coffeeBeanButton = new JButton(coffeeBeanButtonImage);

	private JButton[] buttons = { backSpaceButton, cupButton, tumblrButton, coffeeBeanButton };

	private Image saveImage = new ImageIcon(Main.class.getResource("../images/saveImage.png")).getImage();

	private JPanel saveUpPanel;

	private Image authselectPanelImage = new ImageIcon(Main.class.getResource("../images/save2Image.png")).getImage();

	private ImageIcon replayButtonImage = new ImageIcon(Main.class.getResource("../images/replayButton.png"));
	private ImageIcon replayButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/replayButtonEntered.png"));
	private ImageIcon exitButtonImage = new ImageIcon(Main.class.getResource("../images/nagaButton.png"));
	private ImageIcon exitButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/nagaButtonEntered.png"));
	private ImageIcon saveUpButtonImage = new ImageIcon(Main.class.getResource("../images/saveUpButton.png"));
	private ImageIcon saveUpButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/saveUpButtonEntered.png"));

	private ImageIcon barcodeImage = new ImageIcon(Main.class.getResource("../images/barcodeImage.png"));
	private JLabel barcodeLabel = new JLabel(barcodeImage);

	private JLabel authNumLabel = new JLabel();
	private JLabel timerLabel = new JLabel("TEST");

	private JTextField cafeNumInput = new JTextField();
	private JTextField authNumInput = new JTextField();

	private JButton saveUpButton = new JButton(saveUpButtonImage);
	private JButton exitButton = new JButton(exitButtonImage);
	private JButton replayButton = new JButton(replayButtonImage);

	private JButton[] authButtons = { saveUpButton, exitButton, replayButton };

	private boolean cup_flag = false;
	private boolean tumb_flag = false;
	
	private CafeDto[] orgs = null;

	private String authNum;

	private Timer timer = null;

	public SavePanel() {
		setTitle("this.eco = money;");

		selectPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(saveImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		
		orgs = getCafedto();

		selectPanel.setLayout(null);

		for (JButton button : buttons) {
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setContentAreaFilled(false);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton) e.getSource();
					if (button.equals(backSpaceButton)) {
						dispose();
					} else if (button.equals(cupButton)) {
						cup_flag = true;
						tumb_flag = false;
						switchToAuthPanel();
					} else if (button.equals(tumblrButton)) {
						tumb_flag = true;
						cup_flag = false;
						switchToAuthPanel();
					} else if (button.equals(coffeeBeanButton)) {
						new CafeInfoPanel(orgs);
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
						if (button.getIcon().equals(cupButtonBasicImage)) {
							button.setIcon(cupButtonEnteredImage);
						} else if (button.getIcon().equals(tumblrButtonBasicImage)) {
							button.setIcon(tumblrButtonEnteredImage);
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
						if (button.getIcon().equals(cupButtonEnteredImage)) {
							button.setIcon(cupButtonBasicImage);
						} else if (button.getIcon().equals(tumblrButtonEnteredImage)) {
							button.setIcon(tumblrButtonBasicImage);
						}
					}

				}

				@Override
				public void mousePressed(MouseEvent e) {

				}
			});

			selectPanel.add(button);
		}

		backSpaceButton.setBounds(119, 118, 39, 28);
		cupButton.setBounds(183, 182, 240, 85);
		tumblrButton.setBounds(183, 271, 240, 85);
		coffeeBeanButton.setBounds(478, 113, 20, 22);

		saveUpPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(authselectPanelImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		saveUpPanel.setLayout(null);

		int x = 100;
		for (JButton button : authButtons) {
			button.setBounds(x, 385, 129, 58);
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setContentAreaFilled(false);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton) e.getSource();
					if (button.equals(saveUpButton)) {
						CafeDto cafeDto = authCafe(Integer.parseInt(cafeNumInput.getText()));
						if (cafeDto == null) {
							JOptionPane.showMessageDialog(saveUpPanel, "유효하지 않은 카페 고유번호입니다.", "오류",
									JOptionPane.WARNING_MESSAGE);
							return;
						} else if (!authNumber(authNumInput.getText())) {
							JOptionPane.showMessageDialog(selectPanel, "인증번호가 올바르지 않습니다.", "오류",
									JOptionPane.WARNING_MESSAGE);
							return;
						}

						int result = 0;
						if (cup_flag) {
							result = updateCP(cafeDto, Main.LoginUser);
						} else if (tumb_flag) {
							result = updateTP(cafeDto, Main.LoginUser);
						}

						if (result >= 2) {
							JOptionPane.showMessageDialog(selectPanel, "적립 완료", "알림", JOptionPane.INFORMATION_MESSAGE);
							main.Main.updateUser();
							dispose();
						} else {
							JOptionPane.showMessageDialog(selectPanel, "적립 실패!", "오류", JOptionPane.WARNING_MESSAGE);
						}

					} else if (button.equals(replayButton)) {
						// 다시하기 버튼 클릭 시
						switchToAuthPanel();
					} else if (button.equals(exitButton)) {
						dispose();
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
						if (button.getIcon().equals(replayButtonImage)) {
							button.setIcon(replayButtonEnteredImage);
						} else if (button.getIcon().equals(exitButtonImage)) {
							button.setIcon(exitButtonEnteredImage);
						} else if (button.getIcon().equals(saveUpButtonImage)) {
							button.setIcon(saveUpButtonEnteredImage);
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
						if (button.getIcon().equals(replayButtonEnteredImage)) {
							button.setIcon(replayButtonImage);
						} else if (button.getIcon().equals(exitButtonEnteredImage)) {
							button.setIcon(exitButtonImage);
						} else if (button.getIcon().equals(saveUpButtonEnteredImage)) {
							button.setIcon(saveUpButtonImage);
						}
					}

				}

				@Override
				public void mousePressed(MouseEvent e) {

				}
			});
			saveUpPanel.add(button);
			x += 146;
		}

		cafeNumInput.setBounds(376, 115, 111, 23);
		saveUpPanel.add(cafeNumInput);

		authNumInput.setBounds(300, 305, 196, 23);
		saveUpPanel.add(authNumInput);

		barcodeLabel.setBounds(127, 40, barcodeImage.getIconWidth(), barcodeImage.getIconWidth());
		saveUpPanel.add(barcodeLabel);

		authNumLabel.setBounds(240, 270, 120, 23);
		authNumLabel.setHorizontalAlignment(JLabel.CENTER);
		saveUpPanel.add(authNumLabel);

		timerLabel.setBounds(255, 342, 120, 23);
		timerLabel.setHorizontalAlignment(JLabel.CENTER);
		saveUpPanel.add(timerLabel);

		saveUpPanel.setVisible(false);

		setContentPane(selectPanel);
		setSize(615, 503);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	public void switchToAuthPanel() {
		if (timer != null) {
			timer.cancel();
		}

		timer = new Timer();

		authNum = InputUtil.randomNum(6, 1);
		authNumLabel.setText(authNum);

		cafeNumInput.setText("");
		authNumInput.setText("");

		selectPanel.setVisible(false);
		saveUpPanel.setVisible(true);
		setContentPane(saveUpPanel);
		setSize(616, 572);

		TimerTask task = new TimerTask() {
			int count = 15;

			@Override
			public void run() {
				timerLabel.setText(String.valueOf(count) + "초 남았습니다.");
				if (count == 0) {
					switchToSelectPanel();
					timer.cancel();
				}
				count--;
			}

		};
		timer.schedule(task, 0, 1000);
	}

	public void switchToSelectPanel() {
		tumb_flag = false;
		cup_flag = false;
		setSize(615, 503);
		saveUpPanel.setVisible(false);
		selectPanel.setVisible(true);
		setContentPane(selectPanel);
	}

	public CafeDto authCafe(int cafeNo) {
		conn = MiniConn.getConnection();
		CafeDto dto = null;

		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM CAFE WHERE SECRETCODE = ?";
		ResultSet result = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cafeNo);
			result = pstmt.executeQuery();
			if (result.next()) {
				dto = new CafeDto();
				dto.setNo(result.getInt("NO"));
				dto.setName(result.getString("NAME"));
				dto.setCupPoint(result.getInt("CUPPOINT"));
				dto.setTumPoint(result.getInt("TUMPOINT"));
				dto.setSecretCode(cafeNo);
			}
			return dto;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, result, conn);
		}
		return dto;
	}

	public boolean authNumber(String num) {
		return authNum.equals(num);
	}

	public int updateCP(CafeDto dto, EcoDto ed) {
		PreparedStatement pstmt = null;
		String sql = "UPDATE ECO_MEMBER SET"
				+ " POINT = POINT + (SELECT CUPPOINT FROM CAFE WHERE NAME = ?) + (SELECT BONUS FROM RANKSYS WHERE NO = ?)"
				+ ", ADDEDPOINT = ADDEDPOINT + (SELECT CUPPOINT FROM CAFE WHERE NAME = ?) + (SELECT BONUS FROM RANKSYS WHERE NO = ?)"
				+ "WHERE ID = ?";
		String sql2 = "INSERT INTO HISTORY(NO, ID, POINT, REPORT_NO, PLACE_NO)" + " VALUES (SEQ_HISTORY_NO.NEXTVAL, ?"
				+ ", (SELECT CUPPOINT FROM CAFE WHERE NAME = ? ) + (SELECT BONUS FROM RANKSYS WHERE NO = ?), 1"
				+ ", (SELECT PLACE_NO FROM PLACE WHERE PLACE_NAME = ?))";
		int result = 0;
		int result2 = 0;
		try {
			conn = MiniConn.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getName());
			pstmt.setInt(2, ed.getRank());
			pstmt.setString(3, dto.getName());
			pstmt.setInt(4, ed.getRank());
			pstmt.setString(5, ed.getId());
			result = pstmt.executeUpdate();
			pstmt.close();

			pstmt = conn.prepareStatement(sql2);
			pstmt.setString(1, ed.getId());
			pstmt.setString(2, dto.getName());
			pstmt.setInt(3, ed.getRank());
			pstmt.setString(4, dto.getName());
			result2 = pstmt.executeUpdate();
			if (result > 0 && result2 > 0) {
				MiniConn.commit(conn);
			} else {
				MiniConn.rollback(conn);
			}
		} catch (SQLException e) {
			MiniConn.rollback(conn);
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, conn);
		}
		return result + result2;
	}

	public int updateTP(CafeDto dto, EcoDto ed) {
		PreparedStatement pstmt = null;
		String sql = "UPDATE ECO_MEMBER SET"
				+ " POINT = POINT + (SELECT TUMPOINT FROM CAFE WHERE NAME = ?) + (SELECT BONUS FROM RANKSYS WHERE NO = ?)"
				+ ", ADDEDPOINT = ADDEDPOINT + (SELECT TUMPOINT FROM CAFE WHERE NAME = ?) + (SELECT BONUS FROM RANKSYS WHERE NO = ?)"
				+ "WHERE ID = ?";
		String sql2 = "INSERT INTO HISTORY(NO, ID, POINT, REPORT_NO, PLACE_NO)" + " VALUES (SEQ_HISTORY_NO.NEXTVAL, ?"
				+ ", (SELECT TUMPOINT FROM CAFE WHERE NAME = ? ) + (SELECT BONUS FROM RANKSYS WHERE NO = ?), 1"
				+ ", (SELECT PLACE_NO FROM PLACE WHERE PLACE_NAME = ?))";
		int result = 0;
		int result2 = 0;
		try {
			conn = MiniConn.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getName());
			pstmt.setInt(2, ed.getRank());
			pstmt.setString(3, dto.getName());
			pstmt.setInt(4, ed.getRank());
			pstmt.setString(5, ed.getId());
			result = pstmt.executeUpdate();
			pstmt.close();

			pstmt = conn.prepareStatement(sql2);
			pstmt.setString(1, ed.getId());
			pstmt.setString(2, dto.getName());
			pstmt.setInt(3, ed.getRank());
			pstmt.setString(4, dto.getName());
			result2 = pstmt.executeUpdate();
			if (result > 0 && result2 > 0) {
				MiniConn.commit(conn);
			} else {
				MiniConn.rollback(conn);
			}
		} catch (SQLException e) {
			MiniConn.rollback(conn);
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, conn);
		}
		return result + result2;
	}
	
	public CafeDto[] getCafedto() {
		conn = MiniConn.getConnection();
		
		ArrayList<CafeDto> cafeList = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		String sql = "SELECT NO, NAME, CUPPOINT, TUMPOINT FROM CAFE";
		ResultSet result = null;
		CafeDto cafe = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeQuery();
			while(result.next()) {
				cafe = new CafeDto();
				cafe.setNo(result.getInt("NO"));
				cafe.setName(result.getString("NAME"));
				cafe.setTumPoint(result.getInt("TUMPOINT"));
				cafeList.add(cafe);
				cafe = null;
			}
		} catch(SQLException e) {
			
		} finally {
			MiniConn.close(pstmt, result, conn);
		}
		
		return cafeList.toArray(new CafeDto[cafeList.size()]);
	}

}
