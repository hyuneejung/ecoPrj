package myPackage;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import main.Main;
import util.MiniConn;

@SuppressWarnings("serial")
public class ShowHistoryPanel extends JFrame {
	private Connection conn = null;
	
	private ImageIcon backSpaceButtonImage = new ImageIcon(Main.class.getResource("../images/backSpaceButton.png"));

	private JPanel panel;

	private JButton backSpaceButton = new JButton(backSpaceButtonImage);
	
	private JLabel labelNick = new JLabel(Main.LoginUser.getNick());
	
	private Image historyImage = new ImageIcon(Main.class.getResource("../images/historyImage.png")).getImage();
	
	private String[] historyHeader = {"TIME", "POINT", "FOR", "FROM"};
	private String[][] historyLable = null;
	
	public ShowHistoryPanel() {
		setTitle("this.eco = money;");

		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				 g.drawImage(historyImage, 0, 0, null);
				 setOpaque(false);
				 super.paintComponent(g);
			}
		};
		
		panel.setLayout(null);
		
		labelNick.setBounds(240, 81, 85, 21);
		labelNick.setFont(new Font("AppleSDGothicNeoR00", Font.PLAIN, 20));
		panel.add(labelNick);
		
		backSpaceButton.setBounds(119, 78, 39, 28);
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
		
		historyLable = getHistory();
		
		JTable hisTable = new JTable(historyLable, historyHeader);
		JScrollPane hisPane = new JScrollPane(hisTable);
		hisPane.setBounds(110, 128, 469, 279);
		panel.add(hisPane);
		
		setContentPane(panel);
		setSize(670, 520);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private String[][] getHistory() {
		conn = MiniConn.getConnection();
		
		String sql = "SELECT H.TIME_REPORT, H.POINT, R.TYPE, P.PLACE_NAME FROM HISTORY H JOIN PLACE P ON P.PLACE_NO = H.PLACE_NO JOIN REPORT R ON R.REPORT_NO = H.REPORT_NO WHERE ID = ? ORDER BY H.TIME_REPORT DESC";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String[][] history = null;
		
		try {
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setString(1, Main.LoginUser.getId());
			rs = pstmt.executeQuery();
			rs.last();
			history = new String[rs.getRow()][4];
			rs.beforeFirst();
			int index = 0;
			while(rs.next()) {
				history[index][0] = String.valueOf(rs.getTimestamp("TIME_REPORT"));
				history[index][1] = String.valueOf(rs.getInt("POINT"));
				history[index][2] = rs.getString("TYPE");
				history[index][3] = rs.getString("PLACE_NAME");
				index++;
			}
		} catch(SQLException e) {
			
		} finally {
			MiniConn.close(pstmt, rs, conn);
		}
		
		return history;
	}
}
