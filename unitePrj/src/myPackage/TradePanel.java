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

import dto.HistoryDto;
import main.Main;
import member.EcoDto;
import util.MiniConn;

public class TradePanel extends JFrame {

	private JPanel panel;
	
	private ImageIcon cuButtonBasicImage = new ImageIcon(Main.class.getResource("../images/cuButtonBasic.png"));
	private ImageIcon cuButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/cuButtonEntered.png"));
	private ImageIcon gs25ButtonBasicImage = new ImageIcon(Main.class.getResource("../images/gs25ButtonBasic.png"));
	private ImageIcon gs25ButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/gs25ButtonEntered.png"));
	private ImageIcon hollysButtonBasicImage = new ImageIcon(Main.class.getResource("../images/hollysButtonBasic.png"));
	private ImageIcon hollysButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/hollysButtonEntered.png"));
	private ImageIcon ediyaButtonBasicImage = new ImageIcon(Main.class.getResource("../images/ediyaButtonBasic.png"));
	private ImageIcon ediyaButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/ediyaButtonEntered.png"));
	private ImageIcon cultureButtonBasicImage = new ImageIcon(Main.class.getResource("../images/cultureButtonBasic.png"));
	private ImageIcon cultureButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/cultureButtonEntered.png"));
	private ImageIcon culture2ButtonBasicImage = new ImageIcon(Main.class.getResource("../images/culture2ButtonBasic.png"));
	private ImageIcon culture2ButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/culture2ButtonEntered.png"));
	private ImageIcon backSpaceButtonImage = new ImageIcon(main.Main.class.getResource("../images/backSpaceButton.png"));

	private Image tradeImage = new ImageIcon(Main.class.getResource("../images/tradeImage.png")).getImage();

	private JButton cuButton = new JButton(cuButtonBasicImage);
	private JButton gs25Button = new JButton(gs25ButtonBasicImage);
	private JButton hollysButton = new JButton(hollysButtonBasicImage);
	private JButton ediyaButton = new JButton(ediyaButtonBasicImage);
	private JButton cultureButton = new JButton(cultureButtonBasicImage);
	private JButton culture2Button = new JButton(culture2ButtonBasicImage);
	private JButton backSpaceButton = new JButton(backSpaceButtonImage);

	private JButton[] buttons = { cuButton, gs25Button, hollysButton, ediyaButton, cultureButton, culture2Button, backSpaceButton };

	private Connection conn = null;

	public TradePanel() {

		setTitle("교환하기");
		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(tradeImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		panel.setLayout(null);

		for (JButton button : buttons) {
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setContentAreaFilled(false);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton) e.getSource();
					if (button.equals(cuButton)) {
						minus(5000, 30);
					} else if (button.equals(gs25Button)) {
						minus(10000, 31);
					} else if (button.equals(hollysButton)) {
						minus(5000, 32);
					} else if (button.equals(ediyaButton)) {
						minus(10000, 33);
					} else if (button.equals(cultureButton)) {
						minus(5000, 28);
					} else if (button.equals(culture2Button)) {
						minus(10000, 29);
					} else if (button.equals(backSpaceButton)) {
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
						if (button.getIcon().equals(cuButtonBasicImage)) {
							button.setIcon(cuButtonEnteredImage);
						} else if (button.getIcon().equals(gs25ButtonBasicImage)) {
							button.setIcon(gs25ButtonEnteredImage);
						} else if (button.getIcon().equals(hollysButtonBasicImage)) {
							button.setIcon(hollysButtonEnteredImage);
						} else if (button.getIcon().equals(ediyaButtonBasicImage)) {
							button.setIcon(ediyaButtonEnteredImage);
						} else if (button.getIcon().equals(cultureButtonBasicImage)) {
							button.setIcon(cultureButtonEnteredImage);
						} else if (button.getIcon().equals(culture2ButtonBasicImage)) {
							button.setIcon(culture2ButtonEnteredImage);
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
						if (button.getIcon().equals(cuButtonEnteredImage)) {
							button.setIcon(cuButtonBasicImage);
						} else if (button.getIcon().equals(gs25ButtonEnteredImage)) {
							button.setIcon(gs25ButtonBasicImage);
						} else if (button.getIcon().equals(hollysButtonEnteredImage)) {
							button.setIcon(hollysButtonBasicImage);
						} else if (button.getIcon().equals(ediyaButtonEnteredImage)) {
							button.setIcon(ediyaButtonBasicImage);
						} else if (button.getIcon().equals(cultureButtonEnteredImage)) {
							button.setIcon(cultureButtonBasicImage);
						} else if (button.getIcon().equals(culture2ButtonEnteredImage)) {
							button.setIcon(culture2ButtonBasicImage);
						}
					}

				}

				@Override
				public void mousePressed(MouseEvent e) {

				}
			});
			
			panel.add(button);
			
		}
		
		backSpaceButton.setBounds(119, 118, 39, 28);
		cuButton.setBounds(124, 403, 168, 108);
		gs25Button.setBounds(319, 403, 168, 108);
		hollysButton.setBounds(124, 247, 168, 108);
		ediyaButton.setBounds(319, 247, 168, 108);
		cultureButton.setBounds(124, 560, 168, 108);
		culture2Button.setBounds(319, 560, 168, 108);

		setContentPane(panel);
		setSize(616, 839);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void minus(int tradeEco, int product) {
		
		int answer = JOptionPane.showConfirmDialog(panel, "현재 있는 포인트 : " + Main.LoginUser.getPoint() + "\n" + tradeEco + "ECO를 사용합니다.", "알림", JOptionPane.YES_NO_OPTION);
		
		if(answer == JOptionPane.CLOSED_OPTION) {
			//알림창을 닫은 경우
			return;
		} else if(answer == JOptionPane.YES_OPTION) {
			//예 클릭
			if(tradeEco > main.Main.LoginUser.getPoint()) {
				JOptionPane.showMessageDialog(panel, "ECO가 모자랍니다.", "오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			updateMemberPoint(main.Main.LoginUser, tradeEco);
			HistoryDto hDto = new HistoryDto();
			hDto.setId(main.Main.LoginUser.getId());
			hDto.setPoint(-tradeEco);
			hDto.setReportNo(5);
			hDto.setNo(product);
			addHistory(hDto);
			
			JOptionPane.showMessageDialog(panel, "교환이 완료되었습니다. \n메시지함을 확인해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			main.Main.updateUser();
			
		} else {
			//아니오
			return;
		}
		
	}
	
	private void updateMemberPoint(EcoDto dto, int eco) {
		
		conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;

		String sql1 = "UPDATE ECO_MEMBER SET POINT = ? WHERE ID = ?";
		try {
			// update 실시 후 커밋, 실패면 롤백
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, dto.getPoint());
			pstmt.setInt(1, dto.getPoint() - eco);
			pstmt.setString(2, dto.getId());
			int result = pstmt.executeUpdate();
			if (result == 1)
				MiniConn.commit(conn);
			else {
				MiniConn.rollback(conn);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void addHistory(HistoryDto hDto) {
		conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "INSERT INTO HISTORY VALUES(SEQ_HISTORY_NO.NEXTVAL, ?, SYSDATE, ?, ?, ?)";
		try {
			// update 실시 후 커밋, 실패면 롤백
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, hDto.getId());
			pstmt.setInt(2, hDto.getPoint());
			pstmt.setInt(3, hDto.getReportNo());
			pstmt.setInt(4, hDto.getNo());
			int result = pstmt.executeUpdate();
			if (result == 1)
				MiniConn.commit(conn);
			else {
				MiniConn.rollback(conn);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}
	}

}
