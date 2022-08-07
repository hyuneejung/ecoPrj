package myPackage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JTextField;

import dto.HistoryDto;
import member.EcoDto;
import util.MiniConn;

@SuppressWarnings("serial")
public class SendPanel extends JFrame {
	private Connection conn = null;

	private JPanel panel;

	// image
	private Image backgroundImage = new ImageIcon(main.Main.class.getResource("../images/toBankImage.png")).getImage();
	private ImageIcon sendButtonImage = new ImageIcon(main.Main.class.getResource("../images/toBankButton.png"));
	private ImageIcon sendButtonEnteredImage = new ImageIcon(main.Main.class.getResource("../images/toBankButtonEnteredd.png"));
	private ImageIcon backspaceButtonImage = new ImageIcon(
			main.Main.class.getResource("../images/backSpaceButton.png"));

	// component
	private JButton backspaceButton = new JButton(backspaceButtonImage);
	private JLabel bankLabel = new JLabel("bank");
	private JLabel accountLabel = new JLabel("account");
	private JLabel ecoLabel = new JLabel("eco");
	private JTextField ecoInput = new JTextField();
	private JButton sendButton = new JButton(sendButtonImage);

	public SendPanel() {
		setTitle("this.eco = money;");
		
		
		//panel 설정
		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(backgroundImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		panel.setLayout(null);
		
		//뒤로가기 버튼 설정
		backspaceButton.setBounds(119, 115, 39, 28);
		backspaceButton.setBorderPainted(false);
		backspaceButton.setFocusPainted(false);
		backspaceButton.setContentAreaFilled(false);
		backspaceButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel.add(backspaceButton);
		
		// 은행 정보 Label 설정
		bankLabel.setBounds(300, 170, 140, 23);
		updateBank();
		panel.add(bankLabel);
		
		// 계좌 정보 Label 설정
		accountLabel.setBounds(350, 170, 140, 23);
		updateAccount();
		panel.add(accountLabel);
		
		// eco 정보 Label 설정
		ecoLabel.setBounds(300 ,210, 140, 23);
		updateEco();
		panel.add(ecoLabel);
		
		// ecoInput 설정
		ecoInput.setBounds(300, 250, 140, 23);
		ecoInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				
				if(!Character.isDigit(c)) {
					e.consume();
					return;
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		panel.add(ecoInput);
		
		//송금 버튼 설정
		sendButton.setBounds(222, 325, 156, 58);
		sendButton.setBorderPainted(false);
		sendButton.setFocusPainted(false);
		sendButton.setContentAreaFilled(false);
		sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int sendEco = Integer.parseInt(ecoInput.getText());
				
				int answer = JOptionPane.showConfirmDialog(panel, "해당 계좌번호로 " + sendEco + " ECO를 송금합니다.", "알림", JOptionPane.YES_NO_OPTION);
				
				if(answer == JOptionPane.CLOSED_OPTION) {
					//알림창을 닫은 경우
					return;
				} else if(answer == JOptionPane.YES_OPTION) {
					//예 클릭
					if(sendEco < 10000) {
						JOptionPane.showMessageDialog(panel, "이체는 10000ECO 이상 가능합니다.", "오류", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					if(sendEco > main.Main.LoginUser.getPoint()) {
						JOptionPane.showMessageDialog(panel, "ECO가 모자랍니다.", "오류", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					updateMemberPoint(main.Main.LoginUser, sendEco);
					HistoryDto hDto = new HistoryDto();
					hDto.setId(main.Main.LoginUser.getId());
					hDto.setPoint(-sendEco);
					hDto.setReportNo(3);
					hDto.setNo(34);
					addHistory(hDto);
					
					JOptionPane.showMessageDialog(panel, "송금이 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
					main.Main.updateUser();
					updateEco();
					
				} else {
					//아니오
					return;
				}
			}
		});
		sendButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (sendButton.getIcon() != null) {
					if (sendButton.getIcon().equals(sendButtonImage)) {
						sendButton.setIcon(sendButtonEnteredImage);
					}
				}

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (sendButton.getIcon() != null) {
					if (sendButton.getIcon().equals(sendButtonEnteredImage)) {
						sendButton.setIcon(sendButtonImage);
					} 
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}
		});
		
		panel.add(sendButton);
		
		
		setContentPane(panel);
		setSize(616, 524);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void updateBank() {
		bankLabel.setText(main.Main.LoginUser.getBankName());
	}

	private void updateAccount() {
		accountLabel.setText(main.Main.LoginUser.getAccount());
	}

	private void updateEco() {
		ecoLabel.setText(String.valueOf(main.Main.LoginUser.getPoint()));
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
