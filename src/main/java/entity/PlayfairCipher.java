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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Import Swing cho UI components (JFrame, JTextArea, etc.).
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

// Import Program để quay về menu chính.
import app.Program;

/**
 * Lớp PlayfairCipher: JFrame cho thuật toán Playfair Cipher với giao diện cân
 * bằng hiện đại. Layout: BorderLayout frame + GridBagLayout cho input/output
 * panel con để align thẳng hàng. Thêm nút quay về menu chính.
 */
public class PlayfairCipher extends JFrame {
	private JTextArea inputTextArea;
	private JTextField inputKeyField;
	private JButton encryptButton;

	// Components cho output section.
	private JTextArea outputTextArea; // TextArea hiển thị ciphertext (dưới input).
	private JTextField outputKeyField; // TextField key cho decrypt (giống input key).
	private JButton decryptButton; // Button "Decryption" bên dưới key output.

	// Scroll panes cho textarea.
	private JScrollPane inputScrollPane; // Scroll cho input textarea.
	private JScrollPane outputScrollPane; // Scroll cho output textarea.

	// Ma trận Playfair 5x5 (instance variable để dùng chung encrypt/decrypt).
	private char[][] matrix = new char[5][5]; // Ma trận toàn cục cho key.

	// Colors cho theme hiện đại.
	private static final Color BG_COLOR = new Color(240, 248, 255); // Xanh nhạt.
	private static final Color BUTTON_COLOR = new Color(173, 216, 230); // Xanh nhạt cho button.
	private static final Color HOVER_COLOR = new Color(135, 206, 235); // Xanh sáng hơn khi hover.
	private static final Color BORDER_COLOR = new Color(70, 130, 180); // Xanh đậm cho border.
	private static final Color BACK_BUTTON_COLOR = new Color(255, 182, 193); // Màu hồng nhạt cho nút quay về.

	/**
	 * Constructor: Khởi tạo UI, layout, và listeners.
	 */
	public PlayfairCipher() {
		initLookAndFeel();
		initComponents(); //
		setupEventHandlers();
		setTitle("Playfair Cipher");
		setSize(750, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
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
			// Bỏ qua, dùng default.
		}
	}

	/**
	 * Method initComponents: Layout cân bằng với panel con cho input/output. Mỗi
	 * panel: GridBag - textarea trái (weightx=1, weighty=1), key section phải
	 * (BoxLayout dọc). Thêm nút quay về menu ở SOUTH.
	 */
	private void initComponents() {
		setLayout(new BorderLayout()); // BorderLayout cho frame.

		// Title panel (NORTH).
		var titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBackground(BG_COLOR);
		titlePanel.setBorder(new EmptyBorder(15, 0, 15, 0));
		var titleLabel = new JLabel("Playfair Cipher Demo", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabel.setForeground(BORDER_COLOR);
		titlePanel.add(titleLabel, BorderLayout.CENTER);
		add(titlePanel, BorderLayout.NORTH);

		// Center panel với BoxLayout dọc để stack input + gap + output.
		var centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBackground(BG_COLOR);
		centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding ngoài.

		// Input panel con (GridBag: textarea trái, key phải).
		var inputPanel = createSectionPanel("Input");
		centerPanel.add(inputPanel);

		// Gap giữa input/output (20px).
		var gapPanel = new JPanel();
		gapPanel.setPreferredSize(new Dimension(0, 20));
		gapPanel.setOpaque(false);
		centerPanel.add(gapPanel);

		// Output panel con (tương tự).
		var outputPanel = createSectionPanel("Output");
		centerPanel.add(outputPanel);

		add(centerPanel, BorderLayout.CENTER);

		// Nút quay về menu (SOUTH).
		var backPanel = new JPanel(new BorderLayout());
		backPanel.setBackground(BG_COLOR);
		backPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
		var backButton = createBackButton();
		backPanel.add(backButton, BorderLayout.CENTER);
		add(backPanel, BorderLayout.SOUTH);
	}

	/**
	 * Tạo nút quay về mẹnu
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
			dispose(); // Đóng frame con.
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

		// Key section phải: JPanel với BoxLayout dọc (label + field + button, thẳng
		// hàng).
		var keyPanel = new JPanel();
		keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.Y_AXIS));
		keyPanel.setOpaque(false);
		keyPanel.setPreferredSize(new Dimension(180, 0)); // Tăng rộng hơn để chữ đầy đủ.

		// Label "Key:".
		var keyLabel = createStyledLabel("Key:");
		keyPanel.add(keyLabel);

		// Key field.
		var keyField = (sectionType.equals("Input")) ? createStyledTextField("playfair")
				: createStyledTextField("playfair");
		keyField.setMaximumSize(new Dimension(120, 30)); // Tăng rộng.
		keyField.setAlignmentX(0.5f); // Center trong box.
		if (sectionType.equals("Input")) {
			inputKeyField = keyField;
			inputKeyField.setToolTipText("Nhập chuỗi chữ cái làm key");
		} else {
			outputKeyField = keyField;
			outputKeyField.setToolTipText("Nhập key để giải mã");
		}
		keyPanel.add(keyField);

		// Button (không icon, không emoji, chữ đầy đủ).
		var button = (sectionType.equals("Input")) ? createStyledButton("Encryption", "Mã hóa văn bản")
				: createStyledButton("Decryption", "Giải mã văn bản");
		button.setMaximumSize(new Dimension(160, 50)); // Tăng kích thước để chữ không bị cắt.
		button.setAlignmentX(0.5f); // Center.
		if (sectionType.equals("Input")) {
			encryptButton = button;
		} else {
			decryptButton = button;
		}
		keyPanel.add(button);

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
		field.setColumns(10);
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
		encryptButton.addActionListener(e -> {
			var input = inputTextArea.getText().trim();
			if (input.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng nhập văn bản!"); // Hiển thị dialog lỗi.
				return;
			}
			var keyStr = inputKeyField.getText().trim();
			if (keyStr.isEmpty()) { // Kiểm tra key rỗng.
				JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng nhập key (string)!");
				return;
			}
			inputKeyField.setForeground(Color.BLACK); // Clear placeholder color.
			try {
				generateMatrix(keyStr.toUpperCase()); // Tạo ma trận từ key (uppercase).
				var prepared = prepareText(input.toUpperCase().replaceAll("[^A-Z]", "")); // Chuẩn hóa: uppercase, chỉ
																							// chữ, cặp + X.
				var encrypted = encryptPlayfair(prepared); // Mã hóa cặp chữ.
				outputTextArea.setText(encrypted); // Hiển thị kết quả vào output (uppercase, không space).
				outputKeyField.setText(keyStr); // Copy key từ input sang output cho decrypt.
				outputKeyField.setForeground(Color.BLACK);
			} catch (Exception ex) { // Bắt lỗi (key invalid, etc.).
				JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); // Dialog lỗi.
			}
			inputTextArea.requestFocus();
		});

		// Listener cho decrypt button.
		decryptButton.addActionListener(e -> {
			var input = outputTextArea.getText().trim(); // Lấy ciphertext từ output textarea.
			if (input.isEmpty()) { // Kiểm tra rỗng.
				JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng mã hóa trước!"); // Dialog lỗi.
				return;
			}
			var keyStr = outputKeyField.getText().trim(); // Lấy key từ output key field.
			if (keyStr.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng nhập key (string)!"); // Lỗi.
				return;
			}
			outputKeyField.setForeground(Color.BLACK);
			try {
				generateMatrix(keyStr.toUpperCase()); // Tạo ma trận.
				var decrypted = decryptPlayfair(input.toUpperCase()); // Giải mã và cleanup.
				inputTextArea.setText(decrypted.toLowerCase()); // Hiển thị vào input (lowercase, với space nếu cần).
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); // Lỗi.
			}
		});
	}

	/**
	 * Helper: Tạo ma trận 5x5 từ key (loại duplicate, I/J chung, điền alphabet còn
	 * lại). Giữ nguyên.
	 *
	 * @param key: Key string uppercase.
	 */
	private void generateMatrix(String key) {
		var cleanedKey = new StringBuilder(); // Builder cho key sạch (không duplicate).
		var used = new boolean[26]; // Mảng đánh dấu chữ cái đã dùng (A=0, Z=25).
		Arrays.fill(used, false); // Khởi tạo tất cả false.

		for (char c : key.replace('J', 'I').toCharArray()) { // Duyệt key, thay J bằng I.
			var idx = c - 'A'; // Index trong alphabet (A=0).
			if (Character.isLetter(c) && !used[idx]) { // Nếu chữ cái và chưa dùng.
				cleanedKey.append(c); // Thêm vào cleanedKey.
				used[idx] = true; // Đánh dấu dùng.
			}
		}

		for (var i = 0; i < 26; i++) {
			if (!used[i] && i != 9) {
				cleanedKey.append((char) ('A' + i));
			}
		}

		var k = 0;
		for (var i = 0; i < 5; i++) {
			for (var j = 0; j < 5; j++) {
				matrix[i][j] = cleanedKey.charAt(k++);
			}
		}
	}

	/**
	 * Helper: Chuẩn hóa text thành cặp chữ: Uppercase, chỉ chữ cái, thêm X nếu cặp
	 * giống hoặc lẻ. Giữ nguyên.
	 *
	 * @param text: Text gốc.
	 * @return String cặp chữ uppercase.
	 */
	private String prepareText(String text) {
		var sb = new StringBuilder();
		for (char c : text.toCharArray()) {
			if (Character.isLetter(c)) {
				if (c == 'J') {
					c = 'I';
				}
				sb.append(c);
			} // thay I bằng J
		}
		var clean = sb.toString();
		List<Character> pairs = new ArrayList<>();
		for (var i = 0; i < clean.length(); i++) {
			pairs.add(clean.charAt(i));
			if (i + 1 < clean.length()) {
				if (clean.charAt(i) == clean.charAt(i + 1)) { // Nếu hai chữ giống.
					pairs.add('X'); // Thêm X giữa, bỏ chữ thứ 2.
					i++;
				} else {
					pairs.add(clean.charAt(i + 1)); // Thêm chữ thứ 2 bình thường.
					i++;
				}
			}
		}
		if (pairs.size() % 2 != 0) { // Nếu số chữ lẻ.
			pairs.add('X'); // Thêm X cuối.
		}

		// Xây string từ pairs.
		sb = new StringBuilder();
		for (char ch : pairs) {
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * Helper: Mã hóa một cặp chữ theo quy tắc Playfair (cùng hàng ->phải 1, cột ->
	 * xuống 1, chéo --> đổi cột)
	 *
	 * @param pair: Mảng 2 char.
	 * @return Mảng 2 char mã hóa.
	 */
	private char[] encryptPair(char[] pair) {
		var pos1 = findPosition(pair[0]);
		var pos2 = findPosition(pair[1]);
		var result = new char[2];
		if (pos1[0] == pos2[0]) {
			result[0] = matrix[pos1[0]][(pos1[1] + 1) % 5];
			result[1] = matrix[pos2[0]][(pos2[1] + 1) % 5];
		} else if (pos1[1] == pos2[1]) {
			result[0] = matrix[(pos1[0] + 1) % 5][pos1[1]];
			result[1] = matrix[(pos2[0] + 1) % 5][pos2[1]];
		} else {
			result[0] = matrix[pos1[0]][pos2[1]];
			result[1] = matrix[pos2[0]][pos1[1]];
		}
		return result;
	}

	/**
	 * Method mã hóa Playfair: Chuẩn hóa text, mã hóa từng cặp
	 *
	 * @param preparedText: Text đã chuẩn hóa thành cặp.
	 * @return Ciphertext uppercase (không space)
	 */
	private String encryptPlayfair(String preparedText) {
		var result = new StringBuilder();
		for (var i = 0; i < preparedText.length(); i += 2) {
			char[] pair = { preparedText.charAt(i), preparedText.charAt(i + 1) }; // Lấy cặp.
			var encPair = encryptPair(pair);
			result.append(encPair[0]).append(encPair[1]);
		}
		return result.toString();
	}

	/**
	 * Helper: Giải mã một cặp chữ (ngược quy tắc: trái 1, lên 1, đổi cột). Giữ
	 * nguyên.
	 *
	 * @param pair: Mảng 2 char.
	 * @return Mảng 2 char giải mã.
	 */
	private char[] decryptPair(char[] pair) {
		var pos1 = findPosition(pair[0]);
		var pos2 = findPosition(pair[1]);
		var result = new char[2];
		if (pos1[0] == pos2[0]) {
			result[0] = matrix[pos1[0]][(pos1[1] + 4) % 5];
			result[1] = matrix[pos2[0]][(pos2[1] + 4) % 5];
		} else if (pos1[1] == pos2[1]) {
			result[0] = matrix[(pos1[0] + 4) % 5][pos1[1]];
			result[1] = matrix[(pos2[0] + 4) % 5][pos2[1]];
		} else {
			result[0] = matrix[pos1[0]][pos2[1]];
			result[1] = matrix[pos2[0]][pos1[1]];
		}
		return result;
	}

	/**
	 * Method giải mã Playfair: Giải mã từng cặp, cleanup (loại X thừa, thêm space).
	 *
	 *
	 * @param ciphertext: Ciphertext uppercase.
	 * @return Plaintext (lowercase, với space ước lượng).
	 */
	private String decryptPlayfair(String ciphertext) {
		var result = new StringBuilder();
		for (var i = 0; i < ciphertext.length(); i += 2) {
			char[] pair = { ciphertext.charAt(i), ciphertext.charAt(i + 1) };
			var decPair = decryptPair(pair);
			result.append(decPair[0]).append(decPair[1]);
		}
		var cleaned = cleanupText(result.toString());
		return cleaned;
	}

	/**
	 * Helper: Tìm vị trí [row, col] của char trong ma trận thay I bằng J
	 *
	 *
	 * @param c: Char.
	 * @return int[] {row, col}.
	 */
	private int[] findPosition(char c) {
		if (c == 'J') {
			c = 'I';
		}
		for (var i = 0; i < 5; i++) {
			for (var j = 0; j < 5; j++) {
				if (matrix[i][j] == c) {
					return new int[] { i, j }; // Trả về vị trí.
				}
			}
		}
		return new int[] { -1, -1 };
	}

	/**
	 * Helper: Cleanup text giải mã: Loại X thừa (cuối hoặc XX), thêm space ước
	 * lượng (mỗi 5 char)
	 *
	 * @param text: Text thô uppercase.
	 * @return Text sạch lowercase với space.
	 */
	private String cleanupText(String text) {
		var sb = new StringBuilder(text.toLowerCase());
		while (sb.length() > 0 && sb.charAt(sb.length() - 1) == 'x') {
			sb.deleteCharAt(sb.length() - 1); // Xóa X cuối.
		}
		// Loại XX giữa (thay bằng X hoặc loại tùy case, đơn giản loại một X).
		for (var i = 1; i < sb.length(); i++) {
			if (sb.charAt(i - 1) == 'x' && sb.charAt(i) == 'x') {
				sb.deleteCharAt(i); // Xóa X thứ 2.
				i--; // Backtrack để kiểm tra tiếp.
			}
		}
		var spaced = new StringBuilder();
		for (var i = 0; i < sb.length(); i += 5) {
			spaced.append(sb.substring(i, Math.min(i + 5, sb.length())));
			if (i + 5 < sb.length()) {
				spaced.append(" ");
			}
		}
		return spaced.toString().trim();
	}
}