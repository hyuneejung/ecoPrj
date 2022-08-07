package myPackage;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dto.DonationDto;

@SuppressWarnings("serial")
public class DonationInfoPanel extends JFrame {
	private JPanel panel;
	private JTextArea orgArea = new JTextArea(30, 50);

	public DonationInfoPanel(DonationDto[] orgs) {
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

		for (DonationDto org : orgs) {
			int no = org.getNo();
			String name = org.getName();
			String object = org.getObject();
			int addPoint = org.getAddedPoint();

			String description = String.format("번호: %2d\n이름: %s\n설명: %s\n기부포인트: %10d\n", no, name, object, addPoint);
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
