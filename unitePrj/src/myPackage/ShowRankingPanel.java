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
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import main.Main;
import member.EcoDto;
import util.MiniConn;

@SuppressWarnings("serial")
public class ShowRankingPanel extends JFrame {
	private Connection conn = null;

	private ImageIcon donateKingBasicImage = new ImageIcon(
			Main.class.getResource("../images/donateKingButtonBasic.png"));
	private ImageIcon donateKingEnteredImage = new ImageIcon(
			Main.class.getResource("../images/donateKingButtonEntered.png"));
	private ImageIcon weekDonateBasicImage = new ImageIcon(
			Main.class.getResource("../images/weekdonateButtonBasic.png"));
	private ImageIcon weekDonateEnteredImage = new ImageIcon(
			Main.class.getResource("../images/weekdonateButtonEntered.png"));
	private ImageIcon addedPointBasicImage = new ImageIcon(
			Main.class.getResource("../images/addedPointButtonBasic.png"));
	private ImageIcon addedPointEnteredImage = new ImageIcon(
			Main.class.getResource("../images/addedPointButtonEntered.png"));
	private ImageIcon backSpaceButtonImage = new ImageIcon(Main.class.getResource("../images/backSpaceButton.png"));

	private JPanel panel;

	private JButton backSpaceButton = new JButton(backSpaceButtonImage);
	private JButton donateKingButton = new JButton(donateKingBasicImage);
	private JButton weekDonateButton = new JButton(weekDonateBasicImage);
	private JButton addedPointButton = new JButton(addedPointBasicImage);

	private JButton[] buttons = { addedPointButton, donateKingButton, weekDonateButton };

	private Image rankImage = new ImageIcon(Main.class.getResource("../images/rankImage.png")).getImage();

	private String[] pointHeader = { "순위", "아이디", "누적 포인트", "등급" };
	private String[] donationHeader = { "순위", "아이디", "기부 포인트" };

	private JLabel labelId = new JLabel();
	private JLabel labelNum = new JLabel();

	private String[][] pointRanking = new String[5][4];
	private String[][] donationRanking = new String[5][3];
	private String[][] weeklyDonationRanking = new String[5][3];
	private String[][] myRanking = new String[1][3];

	public ShowRankingPanel() {
		setTitle("this.eco = money;");

		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(rankImage, 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};

		panel.setLayout(null);

		pointRanking = getPointRanking();
		donationRanking = getDonationRanking();
		weeklyDonationRanking = getWeeklyDonationRanking();

		int y = 120;

		JTable pointTable = new JTable(pointRanking, pointHeader);
		JScrollPane pointPane = new JScrollPane(pointTable);
		pointPane.setBounds(115, 135, 463, 107);
		panel.add(pointPane);

		// 뒤로가기 버튼 설정
		backSpaceButton.setBounds(119, 84, 39, 28);
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

		for (JButton button : buttons) {
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setContentAreaFilled(false);
			button.setBounds(y, 265, 129, 58);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton) e.getSource();
					if (button.equals(donateKingButton)) {
						JTable donationTable = new JTable(donationRanking, donationHeader);
						JScrollPane donationPane = new JScrollPane(donationTable);
						donationPane.setBounds(115, 135, 463, 107);
						panel.add(donationPane);
					} else if (button.equals(weekDonateButton)) {
						JTable weeklyDonationTable = new JTable(weeklyDonationRanking, donationHeader);
						JScrollPane weeklyDonationPane = new JScrollPane(weeklyDonationTable);
						weeklyDonationPane.setBounds(115, 135, 463, 107);
						panel.add(weeklyDonationPane);
					} else if (button.equals(addedPointButton)) {
						JTable pointTable = new JTable(pointRanking, pointHeader);
						JScrollPane pointPane = new JScrollPane(pointTable);
						pointPane.setBounds(115, 135, 463, 107);
						panel.add(pointPane);
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
						if (button.getIcon().equals(donateKingBasicImage)) {
							button.setIcon(donateKingEnteredImage);
						} else if (button.getIcon().equals(weekDonateBasicImage)) {
							button.setIcon(weekDonateEnteredImage);
						} else if (button.getIcon().equals(addedPointBasicImage)) {
							button.setIcon(addedPointEnteredImage);
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
						if (button.getIcon().equals(donateKingEnteredImage)) {
							button.setIcon(donateKingBasicImage);
						} else if (button.getIcon().equals(weekDonateEnteredImage)) {
							button.setIcon(weekDonateBasicImage);
						} else if (button.getIcon().equals(addedPointEnteredImage)) {
							button.setIcon(addedPointBasicImage);
						}
					}

				}

				@Override
				public void mousePressed(MouseEvent e) {

				}
			});

			panel.add(button);
			y += 159;
		}

		setContentPane(panel);
		setSize(696, 437);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	private String[][] getPointRanking() {
		conn = MiniConn.getConnection();
		String[][] rank = new String[5][4];

		String sql = "SELECT ID, 누적포인트, 등급\r\n" + "FROM (SELECT E.ID, E.ADDEDPOINT 누적포인트, R.RANK 등급\r\n"
				+ "FROM ECO_MEMBER E\r\n" + "JOIN RANKSYS R ON R.NO = E.RANKNO\r\n" + "ORDER BY E.ADDEDPOINT DESC)\r\n";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int index = 0;
			for (int i = 1; i <= 5; i++) {
				rs.next();
				rank[index][0] = String.valueOf(i);
				rank[index][1] = rs.getString("ID");
				rank[index][2] = rs.getString("누적포인트");
				rank[index][3] = rs.getString("등급");
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}
		return rank;
	}

	private String[][] getDonationRanking() {
		conn = MiniConn.getConnection();
		String[][] rank = new String[5][3];

		String sql = "SELECT ROWNUM, ID, 포인트\r\n"
				+ "FROM (SELECT ROWNUM, ID, 포인트\r\n"
				+ "FROM (SELECT ID, ABS(SUM(POINT)) 포인트\r\n"
				+ "FROM HISTORY\r\n"
				+ "WHERE REPORT_NO = 4\r\n"
				+ "GROUP BY ID)\r\n"
				+ "ORDER BY 포인트 DESC)\r\n"
				+ "WHERE ROWNUM <= 5";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int index = 0;
			while (rs.next()) {
				rank[index][0] = String.valueOf(rs.getString("ROWNUM"));
				rank[index][1] = rs.getString("ID");
				rank[index][2] = rs.getString("포인트");
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}

		return rank;
	}

	private String[][] getWeeklyDonationRanking() {
		conn = MiniConn.getConnection();
		String[][] rank = new String[5][3];

		String sql = "SELECT ROWNUM, ID, 포인트\r\n" + "FROM (SELECT ROWNUM, ID, 포인트\r\n"
				+ "FROM (SELECT ID, ABS(SUM(POINT)) 포인트\r\n" + "FROM HISTORY\r\n"
				+ "WHERE TIME_REPORT > TO_DATE('20220807') AND TIME_REPORT < TO_DATE('20220813')\r\n"
				+ "AND REPORT_NO = 4\r\n" + "GROUP BY ID)\r\n" + "ORDER BY 포인트 DESC) WHERE ROWNUM <= 5";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int index = 0;
			while (rs.next()) {
				rank[index][0] = String.valueOf(rs.getString("ROWNUM"));
				rank[index][1] = rs.getString("ID");
				rank[index][2] = rs.getString("포인트");
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}

		return rank;
	}
}
