package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import entity.CaesarCipher;
import entity.ChuyenDichDong;
import entity.MaHoaBangChuDon;
import entity.PlayfairCipher;
import entity.VigenereCipher;

/**
 * Lớp Program: Entry point của ứng dụng,tạo menu chọn thuật toán mã hóa với Hỗ
 * trợ ẩn/hiện menu để quay về từ frame con
 */
public class Program {
	private static final String APP_TITLE = "Mã Hóa Ứng Dụng";
	private static final int WINDOW_WIDTH = 400;
	private static final int WINDOW_HEIGHT = 300;

	private static JFrame mainFrame;

	/**
	 * Phương thức main: Hiển thị menu giao diện đẹp và mở frame tương ứng.
	 *
	 * @param args: Tham số dòng lệnh (không dùng).
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			createAndShowMainFrame();
		});
	}

	/**
	 * Tạo và hiển thị JFrame chính với các button chọn cipher.
	 */
	private static void createAndShowMainFrame() {
		mainFrame = new JFrame(APP_TITLE);
		mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		mainFrame.setResizable(false);
		mainFrame.setLocationRelativeTo(null);

		var mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

		var titleLabel = new JLabel(APP_TITLE, SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(new Color(50, 50, 150));
		mainPanel.add(titleLabel, BorderLayout.NORTH);

		var buttonPanel = new JPanel(new GridBagLayout());
		var gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;

		String[] options = { "Caesar Cipher", "Bảng chữ đơn", "Playfair Cipher", "Vigenere Cipher",
				"Chuyển dịch dòng" };
		String[] tooltips = { "Mã hóa Caesar - Dịch chuyển chữ cái đơn giản.", "Mã hóa bằng bảng chữ đơn vị.",
				"Mã hóa Playfair - Sử dụng ma trận 5x5.", "Mã hóa Vigenere - Sử dụng khóa từ.",
				"Chuyển dịch dòng - Mã hóa theo dòng." };

		for (var i = 0; i < options.length; i++) {
			var button = createStyledButton(options[i], tooltips[i]);
			var row = i / 2;
			var col = i % 2;
			gbc.gridx = col;
			gbc.gridy = row;
			button.addActionListener(createCipherListener(options[i]));
			buttonPanel.add(button, gbc);
		}

		var exitButton = createStyledButton("Thoát", "Đóng ứng dụng.");
		exitButton.setForeground(Color.RED);
		gbc.gridx = 0;
		gbc.gridy = options.length / 2 + 1;
		gbc.gridwidth = 2;
		exitButton.addActionListener(e -> System.exit(0));
		buttonPanel.add(exitButton, gbc);

		mainPanel.add(buttonPanel, BorderLayout.CENTER);

		mainPanel.setBackground(new Color(240, 248, 255));
		buttonPanel.setBackground(new Color(240, 248, 255));

		mainFrame.add(mainPanel);
		mainFrame.setVisible(true);
	}

	/**
	 * Getter cho mainFrame (dùng trong frame con để quay về).
	 *
	 * @return JFrame menu chính.
	 */
	public static JFrame getMainFrame() {
		return mainFrame;
	}

	/**
	 * Tạo button Icon, màu sắc, font.
	 */
	private static JButton createStyledButton(String text, String tooltip) {
		var button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setPreferredSize(new Dimension(150, 50));
		button.setBackground(new Color(173, 216, 230));
		button.setForeground(Color.BLACK);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createRaisedBevelBorder());

//		try {
//			var icon = (ImageIcon) UIManager.getIcon("OptionPane.informationIcon");
//			if (icon != null) {
//				button.setIcon(icon);
//			}
//		} catch (Exception ignored) {
//
//		}
//

		// icon vớ vẩn bị che mất hết chữ nút đừng để ý nha
		button.setToolTipText(tooltip);
		return button;
	}

	/**
	 * Tạo ActionListener cho button cipher, mở frame tương ứng và ẩn menu chính.
	 */
	private static ActionListener createCipherListener(String option) {
		return e -> {
			// Ẩn menu chính khi mở frame con.
			if (mainFrame != null) {
				mainFrame.setVisible(false);
			}
			switch (option) {
			case "Caesar Cipher":
				new CaesarCipher().setVisible(true);
				break;
			case "Bảng chữ đơn":
				new MaHoaBangChuDon().setVisible(true);
				break;
			case "Playfair Cipher":
				new PlayfairCipher().setVisible(true);
				break;
			case "Vigenere Cipher":
				new VigenereCipher().setVisible(true);
				break;
			case "Chuyển dịch dòng":
				new ChuyenDichDong().setVisible(true);
				break;
			}
		};
	}
}