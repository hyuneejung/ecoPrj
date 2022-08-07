package myPackage;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import cafe.CafeDto;
import dto.DonationDto;

@SuppressWarnings("serial")
public class CafeInfoPanel extends JFrame {
	
	private JPanel panel;
	private JTextArea orgArea = new JTextArea(30, 50);

	public CafeInfoPanel(CafeDto[] orgs) {
		setTitle("this.eco = money;");

		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				// g.drawImage(backgroundImage, 0, 0, null);
				// setOpaque(false);
				// super.paintComponent(g);
			}
		};
		panel.setLayout(null);

		orgArea.setBounds(10, 10, 600, 500);
		
		String str = "일회용 컵 반환의 경우 모두 300원이 적립됩니다. ∠( ＾▽＾」∠)＿\n";
		orgArea.append(str);
		orgArea.append(
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

		for (CafeDto dto : orgs) {
			int no = dto.getNo();
			String name = dto.getName();
			int tumPoint = dto.getTumPoint();
			String description = String.format("번호: %2d\n이름: %s\n텀블러포인트: %10d\n", no, name, tumPoint);
			orgArea.append(description);
			orgArea.append(
					"---------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		}
		
		JScrollPane sp = new JScrollPane(orgArea);
		sp.setBounds(10, 10, 590, 485);
		panel.add(sp);

		setContentPane(panel);
		setSize(617, 505);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
