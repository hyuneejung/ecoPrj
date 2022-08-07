package myPackage;

import java.awt.Color;
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
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dto.DonationDto;
import dto.HistoryDto;
import member.EcoDto;
import util.MiniConn;

@SuppressWarnings("serial")
public class DonationPanel extends JFrame {
	private Connection conn = null;
	
	private JPanel panel;
	
	private Image donateImage = new ImageIcon(main.Main.class.getResource("../images/donateImage.png")).getImage();
	
	private ImageIcon backSpaceButtonImage = new ImageIcon(main.Main.class.getResource("../images/backSpaceButton.png"));
	private ImageIcon donateButtonImage = new ImageIcon(main.Main.class.getResource("../images/donateButton.png"));
	private ImageIcon donateButtonEnteredImage = new ImageIcon(main.Main.class.getResource("../images/donateButtonEnteredd.png"));
	private ImageIcon checkOrgButtonImage = new ImageIcon(main.Main.class.getResource("../images/checkOrgButton.png"));
	private ImageIcon checkOrgButtonEnteredImage = new ImageIcon(main.Main.class.getResource("../images/checkOrgButtonEntered.png"));
	
	private JButton backSpaceButton = new JButton(backSpaceButtonImage);
	 
	private JLabel ecoLabel = new JLabel("ECO");
	
	private JComboBox<String> orgComboBox;
	
	private JTextField ecoInput = new JTextField();
	
	private JButton checkOrgButton = new JButton(checkOrgButtonImage);
	private JButton donateButton = new JButton(donateButtonImage);
	
	private DonationDto[] orgs = null;
	private String[] orgsStrings = null;
	private int userPoint = 0;
	
	
	public DonationPanel() {
		
		setTitle("기부하기");
		
		orgs = getDonationDto();
		orgsStrings = new String[orgs.length];
		userPoint = getUserPoint();
		
		for(int i = 0; i < orgs.length; i++) {
			orgsStrings[i] = orgs[i].getName();
		}
		
		
		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(donateImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		
		
		panel.setLayout(null);
		
		backSpaceButton.setBounds(119, 116, 39, 28);
		backSpaceButton.setBorderPainted(false);
		backSpaceButton.setFocusPainted(false);
		backSpaceButton.setContentAreaFilled(false);
		backSpaceButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				
			}
		});
		panel.add(backSpaceButton);
		
		
		orgComboBox = new JComboBox<String>(orgsStrings);
		orgComboBox.setBounds(300, 170, 140, 23);
		panel.add(orgComboBox);
		orgComboBox.setBackground(Color.white);
		
		ecoLabel.setBounds(300, 210, 140, 23);
		ecoLabel.setText(String.valueOf(userPoint));
		panel.add(ecoLabel);
		
		
		ecoInput.setBounds(300, 250, 100, 23);
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
		
		
		checkOrgButton.setBounds(130, 320, 156, 58);
		checkOrgButton.setBorderPainted(false);
		checkOrgButton.setFocusPainted(false);
		checkOrgButton.setContentAreaFilled(false);
		checkOrgButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new DonationInfoPanel(orgs);
				
			}
		});
		checkOrgButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (checkOrgButton.getIcon() != null) {
					if (checkOrgButton.getIcon().equals(checkOrgButtonImage)) {
						checkOrgButton.setIcon(checkOrgButtonEnteredImage);
					}
				}

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (checkOrgButton.getIcon() != null) {
					if (checkOrgButton.getIcon().equals(checkOrgButtonEnteredImage)) {
						checkOrgButton.setIcon(checkOrgButtonImage);
					} 
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}
		});
		panel.add(checkOrgButton);
		
		donateButton.setBounds(320, 320, 156, 58);
		donateButton.setBorderPainted(false);
		donateButton.setFocusPainted(false);
		donateButton.setContentAreaFilled(false);
		donateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ecoInput.getText().equals("")) {
					JOptionPane.showMessageDialog(panel, "ECO를 입력해주세요", "오류", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				int inputPoint = Integer.parseInt(ecoInput.getText());
				
				if(inputPoint < 1000) {
					JOptionPane.showMessageDialog(panel, "기부는 1000ECO 이상부터 가능합니다!", "오류", JOptionPane.WARNING_MESSAGE);
					return;
				} else if(inputPoint > userPoint) {
					JOptionPane.showMessageDialog(panel, "기부할 ECO가 유저가 가진 포인트보다 많습니다.", "오류", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				int answer = JOptionPane.showConfirmDialog(panel, orgComboBox.getSelectedItem() + "재단으로 " + inputPoint + "ECO를 기부하시겠습니까?", "알림", JOptionPane.YES_NO_OPTION);
				
				if(answer == JOptionPane.CLOSED_OPTION) {
					//알림창을 닫은 경우
				} else if(answer == JOptionPane.YES_OPTION) {
					//예 클릭: 기부 동의
					updateMemberPoint(main.Main.LoginUser, inputPoint);
					DonationDto selectDto = orgs[orgComboBox.getSelectedIndex()];
					updateDonationPoint(selectDto, inputPoint);
					HistoryDto hDto = new HistoryDto();
					hDto.setId(main.Main.LoginUser.getId());
					hDto.setPoint(-inputPoint);
					hDto.setReportNo(4);
					addHistory(hDto, String.valueOf(orgComboBox.getSelectedItem()));
					JOptionPane.showMessageDialog(panel, "기부가 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
					
					main.Main.updateUser();
					
					userPoint = getUserPoint();
					ecoLabel.setText(String.valueOf(userPoint));
					
				} else {
					//아니오
				}
				
			}
		});
		
		donateButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (donateButton.getIcon() != null) {
					if (donateButton.getIcon().equals(donateButtonImage)) {
						donateButton.setIcon(donateButtonEnteredImage);
					}
				}

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (donateButton.getIcon() != null) {
					if (donateButton.getIcon().equals(donateButtonEnteredImage)) {
						donateButton.setIcon(donateButtonImage);
					} 
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}
		});
		
		panel.add(donateButton);
		
		
		setContentPane(panel);
		setSize(616, 524);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public DonationDto[] getDonationDto() {
		conn = MiniConn.getConnection();
		
		ArrayList<DonationDto> orgList = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM DONATION";
		ResultSet result = null;
		DonationDto temp = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeQuery();
			while(result.next()) {
				temp = new DonationDto();
				temp.setNo(result.getInt("NO"));
				temp.setName(result.getString("NAME"));
				temp.setObject(result.getString("OBJECT"));
				temp.setAddedPoint(result.getInt("ADDEDPOINT"));
				orgList.add(temp);
				temp = null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, result, conn);
		}
		
		return orgList.toArray(new DonationDto[orgList.size()]);
	}
	
	public int getUserPoint() {		
		conn = MiniConn.getConnection();
		
		PreparedStatement pstmt = null;
		String sql = "SELECT POINT FROM ECO_MEMBER WHERE ID = ?";
		ResultSet result = null;
		
		int user_point = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, main.Main.LoginUser.getId());
			result = pstmt.executeQuery();
			if(result.next()) {
				user_point = result.getInt("POINT");
			} 
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, result, conn);
		}
		
		return user_point;
	}
	
	public void updateMemberPoint(EcoDto dto, int eco) {
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
	
	public void updateDonationPoint(DonationDto dto, int eco) {
		conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// 임시로 회원정보 dto에 값을 지정하고 dto값으로 db를 update한다.
		dto.setAddedPoint(dto.getAddedPoint() + eco); // 누적 기부금액 = 기존+기부한eco

		String sql = "UPDATE DONATION SET ADDEDPOINT = ? WHERE NO = ?";
		try {
			// update 실시 후 커밋, 실패면 롤백
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getAddedPoint());
			pstmt.setInt(2, dto.getNo());
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
	
	public void addHistory(HistoryDto hDto, String placeName) {
		conn = MiniConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "INSERT INTO HISTORY VALUES(SEQ_HISTORY_NO.NEXTVAL, ?, SYSDATE, ?, ?, (SELECT PLACE_NO FROM PLACE WHERE PLACE_NAME = ?))";
		try {
			// update 실시 후 커밋, 실패면 롤백
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, hDto.getId());
			pstmt.setInt(2, hDto.getPoint());
			pstmt.setInt(3, hDto.getReportNo());
			pstmt.setString(4, placeName);
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
