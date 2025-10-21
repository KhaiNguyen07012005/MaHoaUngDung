package entity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import app.Program;

/**
 * Lớp MaHoaBangChuDon: JFrame cho thuật toán Bảng chữ đơn (Monoalphabetic
 * Cipher) với giao diện cân bằng hiện đại. Layout: BorderLayout frame +
 * GridBagLayout cho input/output panel con để align thẳng hàng. Thêm nút quay
 * về menu chính.
 */
public class MaHoaBangChuDon extends JFrame {
	// Components cho input section.
	private JTextArea inputTextArea; // nhập plaintext
	private JTextField inputKeyField; // key cho encrypt
	private JButton randomKeyButton;
	private JButton encryptButton;
	private JTextArea outputTextArea; // TextArea hiển thị ciphertext (dưới input).
	private JTextField outputKeyField;
	private JButton decryptButton;
	private JScrollPane inputScrollPane;
	private JScrollPane outputScrollPane;
	private static final Color BG_COLOR = new Color(240, 248, 255); // Xanh nhạt.
	private static final Color BUTTON_COLOR = new Color(173, 216, 230); // Xanh nhạt
	private static final Color HOVER_COLOR = new Color(135, 206, 235); // Xanh
	private static final Color BORDER_COLOR = new Color(70, 130, 180); // Xanh đậm
	private static final Color BACK_BUTTON_COLOR = new Color(255, 182, 193);// Màu hồng

	/**
	 * Constructor: Khởi tạo UI, layout, và listeners.
	 */
	public MaHoaBangChuDon() {
		initLookAndFeel();
		initComponents();
		setupEventHandlers(); // Gọi method thiết lập listeners cho buttons.
		setTitle("Bảng Chữ Đơn"); // Title frame cập nhật.
		setSize(800, 650); // Kích thước lớn hơn cho key field 26 chars.
		setLocationRelativeTo(null); // Đặt frame ở giữa màn hình.
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false); // Không cho resize để giữ layout.
		getContentPane().setBackground(BG_COLOR);
		inputTextArea.requestFocus();
	}

	/**
	 * Áp dụng Nimbus Look and Feel cho giao diện hiện đại.
	 */
	private void initLookAndFeel() {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ex) {
		}
	}

	/**
	 * Method initComponents: Layout cân bằng với panel con cho input/output
	 */
	private void initComponents() {
		setLayout(new BorderLayout()); // BorderLayout cho frame.

		// Title panel (NORTH).
		var titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBackground(BG_COLOR);
		titlePanel.setBorder(new EmptyBorder(15, 0, 15, 0));
		var titleLabel = new JLabel("Bảng Chữ Đơn Demo", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabel.setForeground(BORDER_COLOR);
		titlePanel.add(titleLabel, BorderLayout.CENTER);
		add(titlePanel, BorderLayout.NORTH);

		var centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBackground(BG_COLOR);
		centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
		var inputPanel = createSectionPanel("Input");
		centerPanel.add(inputPanel);

		var gapPanel = new JPanel();
		gapPanel.setPreferredSize(new Dimension(0, 20));
		gapPanel.setOpaque(false);
		centerPanel.add(gapPanel);

		var outputPanel = createSectionPanel("Output");
		centerPanel.add(outputPanel);

		add(centerPanel, BorderLayout.CENTER);

		// Nút quay về menu
		var backPanel = new JPanel(new BorderLayout());
		backPanel.setBackground(BG_COLOR);
		backPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
		var backButton = createBackButton();
		backPanel.add(backButton, BorderLayout.CENTER);
		add(backPanel, BorderLayout.SOUTH);
	}

	/**
	 * Tạo nút quay về menu với style riêng.
	 */
	private JButton createBackButton() {
		var button = new JButton("Quay về Menu");
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setBackground(BACK_BUTTON_COLOR);
		button.setForeground(Color.BLACK);
		button.setFocusPainted(false);
		button.setRolloverEnabled(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		button.setBorderPainted(false);

		// Hover effect.
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(new Color(255, 105, 180)); // Hồng đậm hơn khi hover.
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(BACK_BUTTON_COLOR);
			}
		});

		// Action: Hiển thị menu chính và đóng frame này.
		button.addActionListener(e -> {
			if (Program.getMainFrame() != null) {
				Program.getMainFrame().setVisible(true);
			}
			dispose(); // Đóng frame
		});

		return button;
	}

	/**
	 * Tạo panel con cho section (input hoặc output): GridBagLayout với textarea
	 * trái, key dọc phải.
	 *
	 * @param sectionType: "Input" hoặc "Output" để phân biệt.
	 */
	private JPanel createSectionPanel(String sectionType) {
		var sectionPanel = new JPanel(new GridBagLayout());
		sectionPanel.setBackground(BG_COLOR);
		sectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		var gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.BOTH;

		var textArea = createStyledTextArea();
		if (sectionType.equals("Input")) {
			inputTextArea = textArea;
			inputScrollPane = new JScrollPane(inputTextArea);
		} else {
			textArea.setEditable(false);
			outputTextArea = textArea;
			outputScrollPane = new JScrollPane(outputTextArea);
		}
		var scrollPane = (sectionType.equals("Input")) ? inputScrollPane : outputScrollPane;
		scrollPane.setPreferredSize(new Dimension(500, 150));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		sectionPanel.add(scrollPane, gbc);

		// Key section phải: JPanel với BoxLayout dọc (label + field + button(s), thẳng
		// hàng).
		var keyPanel = new JPanel();
		keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.Y_AXIS));
		keyPanel.setOpaque(false);
		keyPanel.setPreferredSize(new Dimension(200, 0)); // Rộng hơn cho key 26 chars + buttons.

		// Label "Key:".
		var keyLabel = createStyledLabel("Key:");
		keyPanel.add(keyLabel);

		// Key field.
		var keyField = (sectionType.equals("Input")) ? createStyledTextField("mwgdkvzelbnhsxpriquajfcyto")
				: createStyledTextField("mwgdkvzelbnhsxpriquajfcyto");
		keyField.setMaximumSize(new Dimension(200, 30)); // Rộng cho 26 chars.
		keyField.setAlignmentX(0.5f);
		if (sectionType.equals("Input")) {
			inputKeyField = keyField;
			inputKeyField.setToolTipText("Nhập 26 chữ cái hoa duy nhất (A-Z)");
		} else {
			outputKeyField = keyField;
			outputKeyField.setToolTipText("Nhập key để giải mã");
		}
		keyPanel.add(keyField);

		// Buttons.
		if (sectionType.equals("Input")) {
			// Random key button.
			randomKeyButton = createStyledButton("Random Key", "Tạo key ngẫu nhiên");
			randomKeyButton.setMaximumSize(new Dimension(160, 40));
			randomKeyButton.setAlignmentX(0.5f);
			keyPanel.add(randomKeyButton);

			// Encrypt button.
			encryptButton = createStyledButton("Encryption", "Mã hóa văn bản");
			encryptButton.setMaximumSize(new Dimension(160, 50));
			encryptButton.setAlignmentX(0.5f);
			keyPanel.add(encryptButton);
		} else {
			// Decrypt button.
			decryptButton = createStyledButton("Decryption", "Giải mã văn bản");
			decryptButton.setMaximumSize(new Dimension(160, 50));
			decryptButton.setAlignmentX(0.5f);
			keyPanel.add(decryptButton);
		}

		// Thêm keyPanel vào cột 1 (phải).
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		gbc.weighty = 1.0;
		sectionPanel.add(keyPanel, gbc);

		return sectionPanel;
	}

	/**
	 * Tạo label styled: Font bold, color.
	 */
	private JLabel createStyledLabel(String text) {
		var label = new JLabel(text);
		label.setFont(new Font("Arial", Font.BOLD, 12));
		label.setForeground(BORDER_COLOR);
		label.setAlignmentX(0.5f); // Center trong box.
		return label;
	}

	/**
	 * Tạo TextArea styled: Monospace font, border, background.
	 */
	private JTextArea createStyledTextArea() {
		var textArea = new JTextArea();
		textArea.setFont(new Font("Courier New", Font.PLAIN, 13));
		textArea.setBackground(Color.WHITE);
		textArea.setBorder(new LineBorder(BORDER_COLOR, 1));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		return textArea;
	}

	/**
	 * Tạo TextField styled: Border, columns, placeholder effect.
	 */
	private JTextField createStyledTextField(String defaultText) {
		var field = new JTextField(defaultText);
		field.setColumns(26); // 26 cho key.
		field.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1), new EmptyBorder(5, 5, 5, 5)));
		field.setHorizontalAlignment(JTextField.CENTER);
		var placeholder = defaultText;
		field.setForeground(Color.GRAY);
		field.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				checkAndClear();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				checkAndClear();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				checkAndClear();
			}

			private void checkAndClear() {
				var text = field.getText();
				if (text.trim().isEmpty()) {
					field.setText(placeholder);
					field.setForeground(Color.GRAY);
				} else if (text.equals(placeholder)) {
					field.setText("");
					field.setForeground(Color.BLACK);
				}
			}
		});
		return field;
	}

	/**
	 * Tạo button styled: Không icon, không emoji, chữ đầy đủ, hover.
	 */
	private JButton createStyledButton(String text, String tooltip) {
		var button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 12));
		button.setBackground(BUTTON_COLOR);
		button.setForeground(Color.BLACK);
		button.setFocusPainted(false);
		button.setRolloverEnabled(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding rộng hơn cho chữ.
		button.setBorderPainted(false);

		// Hover
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(HOVER_COLOR);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(BUTTON_COLOR);
			}
		});

		button.setToolTipText(tooltip);
		return button;
	}

	/**
	 * Method thiết lập event handlers (listeners) cho buttons. Clear placeholder
	 * khi dùng key. Chỉ kiểm tra rỗng cho input.
	 */
	private void setupEventHandlers() {
		// Listener cho random key button: Tạo key random 26 chữ duy nhất.
		randomKeyButton.addActionListener(e -> {
			var randomKey = generateRandomKey(); // Gọi method tạo key random.
			inputKeyField.setText(randomKey); // Set vào input key field.
			inputKeyField.setForeground(Color.BLACK); // Clear placeholder color.
			outputKeyField.setText(randomKey); // Set vào output key field (đồng bộ).
			outputKeyField.setForeground(Color.BLACK);
		});

		// Listener cho encrypt button.
		encryptButton.addActionListener(e -> {
			var input = inputTextArea.getText().trim();
			if (input.isEmpty()) { // Chỉ kiểm tra rỗng.
				JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng nhập văn bản!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			var keyStr = inputKeyField.getText().trim().toUpperCase(); // Lấy key và uppercase.
			if (keyStr.length() != 26 || !keyStr.matches("[A-Z]+") || !isUniqueChars(keyStr)) {
				JOptionPane.showMessageDialog(this, "Lỗi: Key phải 26 chữ cái hoa DUY NHẤT!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			inputKeyField.setForeground(Color.BLACK); // Clear placeholder color.
			var encrypted = encryptMonoalphabetic(input, keyStr);
			outputTextArea.setText(encrypted);
			outputKeyField.setText(keyStr);
			outputKeyField.setForeground(Color.BLACK); // Clear placeholder.
			inputTextArea.requestFocus();
		});

		// Listener cho decrypt button.
		decryptButton.addActionListener(e -> {
			var input = outputTextArea.getText().trim();
			if (input.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng mã hóa trước!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			var keyStr = outputKeyField.getText().trim().toUpperCase(); // Lấy key và uppercase.
			if (keyStr.length() != 26 || !keyStr.matches("[A-Z]+") || !isUniqueChars(keyStr)) {
				JOptionPane.showMessageDialog(this, "Lỗi: Key phải 26 chữ cái hoa DUY NHẤT!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			outputKeyField.setForeground(Color.BLACK);
			var decrypted = decryptMonoalphabetic(input, keyStr);
			inputTextArea.setText(decrypted);
		});
	}

	/**
	 * Helper method: Tạo key random 26 chữ cái duy nhất (shuffle alphabet A-Z).
	 *
	 * @return String key 26 chữ uppercase.
	 */
	private String generateRandomKey() {
		var alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		var chars = alphabet.toCharArray(); // Chuyển thành mảng char.
		var random = new Random();
		for (var i = chars.length - 1; i > 0; i--) {
			var j = random.nextInt(i + 1);
			var temp = chars[i]; // Swap.
			chars[i] = chars[j];
			chars[j] = temp;
		}
		return new String(chars); // Trả về string mới.
	}

	/**
	 * Helper method: Kiểm tra key có 26 ký tự duy nhất (không lặp).
	 *
	 * @param key: Key string.
	 * @return true nếu duy nhất.
	 */
	private boolean isUniqueChars(String key) {
		Set<Character> set = new HashSet<>();
		for (char c : key.toCharArray()) {
			if (!set.add(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method mã hóa Monoalphabetic
	 */
	private String encryptMonoalphabetic(String plaintext, String key) {
		Map<Character, Character> table = new HashMap<>(); // HashMap lưu bảng thay thế.
		for (var i = 0; i < 26; i++) { // Duyệt 0-25.
			table.put((char) ('A' + i), key.charAt(i));
		}
		var result = new StringBuilder();
		for (char c : plaintext.toCharArray()) {
			var upperC = Character.toUpperCase(c);
			if (Character.isLetter(c)) {
				char replacement = table.get(upperC);
				result.append(Character.isUpperCase(c) ? replacement : Character.toLowerCase(replacement));
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}

	/**
	 * Method giải mã Monoalphabetic
	 */
	private String decryptMonoalphabetic(String ciphertext, String key) {
		Map<Character, Character> reverseTable = new HashMap<>();
		for (var i = 0; i < 26; i++) { // Duyệt 0-25.
			reverseTable.put(key.charAt(i), (char) ('A' + i));
		}
		var result = new StringBuilder();
		for (char c : ciphertext.toCharArray()) {
			var upperC = Character.toUpperCase(c);
			if (Character.isLetter(c)) {
				char original = reverseTable.get(upperC);
				result.append(Character.isUpperCase(c) ? original : Character.toLowerCase(original));
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}
}