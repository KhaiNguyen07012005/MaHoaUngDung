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
 * Lớp VigenereCipher: JFrame cho thuật toán Vigenere Cipher với giao diện cân
 * bằng hiện đại. Layout: BorderLayout frame + GridBagLayout cho input/output
 * panel con để align thẳng hàng. Thêm nút quay về menu chính.
 */
public class VigenereCipher extends JFrame {

	// Components cho input section.
	private JTextArea inputTextArea;
	private JTextField inputKeyField;
	private JButton encryptButton;
	private JTextArea outputTextArea;
	private JTextField outputKeyField;
	private JButton decryptButton;
	private JScrollPane inputScrollPane;
	private JScrollPane outputScrollPane;

	private static final Color BG_COLOR = new Color(240, 248, 255);
	private static final Color BUTTON_COLOR = new Color(173, 216, 230);
	private static final Color HOVER_COLOR = new Color(135, 206, 235);
	private static final Color BORDER_COLOR = new Color(70, 130, 180);
	private static final Color BACK_BUTTON_COLOR = new Color(255, 182, 193);

	/**
	 * Constructor: Khởi tạo UI, layout, và listeners.
	 */
	public VigenereCipher() {
		initLookAndFeel();
		initComponents();
		setupEventHandlers();
		setTitle("Vigenere Cipher");
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
		}
	}

	/**
	 * Method initComponents: Layout cân bằng với panel con cho input/output *
	 * (BoxLayout dọc) , nút quay về menu
	 */
	private void initComponents() {
		setLayout(new BorderLayout());

		// Title panel (NORTH).
		var titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBackground(BG_COLOR);
		titlePanel.setBorder(new EmptyBorder(15, 0, 15, 0));
		var titleLabel = new JLabel("Vigenere Cipher Demo", JLabel.CENTER);
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
		var backPanel = new JPanel(new BorderLayout());
		backPanel.setBackground(BG_COLOR);
		backPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
		var backButton = createBackButton();
		backPanel.add(backButton, BorderLayout.CENTER);
		add(backPanel, BorderLayout.SOUTH);
	}

	/**
	 * Tạo nút quay về menu
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

		// Hover
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(new Color(255, 105, 180));
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
			dispose(); // Đóng frame này
		});

		return button;
	}

	/**
	 * Tạo panel con cho section
	 *
	 * @param sectionType: "Input" hoặc "Output" để phân biệt
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
		scrollPane.setPreferredSize(new Dimension(500, 150)); // Kích thước lớn, cân bằng.

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
		keyPanel.setPreferredSize(new Dimension(180, 0));
		// Label "Key:".
		var keyLabel = createStyledLabel("Key:");
		keyPanel.add(keyLabel);

		// Key field.
		var keyField = (sectionType.equals("Input")) ? createStyledTextField("lemon") : createStyledTextField("lemon");
		keyField.setMaximumSize(new Dimension(120, 30)); // Tăng rộng.
		keyField.setAlignmentX(0.5f);
		if (sectionType.equals("Input")) {
			inputKeyField = keyField;
			inputKeyField.setToolTipText("Nhập chuỗi chữ cái làm key");
		} else {
			outputKeyField = keyField;
			outputKeyField.setToolTipText("Nhập key để giải mã");
		}
		keyPanel.add(keyField);

		var button = (sectionType.equals("Input")) ? createStyledButton("Encryption", "Mã hóa văn bản")
				: createStyledButton("Decryption", "Giải mã văn bản");
		button.setMaximumSize(new Dimension(160, 50));
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
		label.setAlignmentX(0.5f);
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
		field.setColumns(10); // Phù hợp cho key string.
		field.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 1), new EmptyBorder(5, 5, 5, 5)));
		field.setHorizontalAlignment(JTextField.CENTER); // Center text.
		// Placeholder: Clear khi type nếu chỉ có default (cải thiện).
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
		button.setFocusPainted(false); // Ẩn focus indicator (ô vuông).
		button.setRolloverEnabled(false); // Tắt rollover để tránh effect lạ.
		button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding rộng hơn cho chữ.
		button.setBorderPainted(false); // Nimbus sẽ handle.

		// Không set icon hoặc emoji nữa để tránh ô vuông.

		// Hover effect.
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
		// Listener cho encrypt button.
		encryptButton.addActionListener(e -> {
			var input = inputTextArea.getText().trim();
			if (input.isEmpty()) { // Chỉ kiểm tra rỗng.
				JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng nhập văn bản!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			var keyStr = inputKeyField.getText().trim(); // Lấy key từ input key field.
			if (keyStr.isEmpty() || !keyStr.matches("[a-zA-Z]+")) { // Kiểm tra key rỗng hoặc không phải chữ cái.
				JOptionPane.showMessageDialog(this, "Lỗi: Key phải là chuỗi chữ cái!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			inputKeyField.setForeground(Color.BLACK); // Clear placeholder color.
			var encrypted = encryptVigenere(input, keyStr.toUpperCase()); // Gọi method mã hóa (key uppercase).
			outputTextArea.setText(encrypted); // Hiển thị kết quả vào output (giữ case gốc).
			outputKeyField.setText(keyStr); // Copy key từ input sang output cho decrypt.
			outputKeyField.setForeground(Color.BLACK);
			inputTextArea.requestFocus();
		});

		// Listener cho decrypt button.
		decryptButton.addActionListener(e -> {
			var input = outputTextArea.getText().trim(); // Lấy ciphertext từ output textarea.
			if (input.isEmpty()) { // Kiểm tra rỗng.
				JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng mã hóa trước!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			var keyStr = outputKeyField.getText().trim(); // Lấy key từ output key field.
			if (keyStr.isEmpty() || !keyStr.matches("[a-zA-Z]+")) {
				JOptionPane.showMessageDialog(this, "Lỗi: Key phải là chuỗi chữ cái!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			outputKeyField.setForeground(Color.BLACK);
			var decrypted = decryptVigenere(input, keyStr.toUpperCase()); // Gọi method giải mã.
			inputTextArea.setText(decrypted); // Hiển thị kết quả vào input textarea (giữ case).
		});
	}

	/**
	 * Method mã hóa Vigenere: Giữ nguyên.
	 */
	private String encryptVigenere(String plaintext, String key) {
		var result = new StringBuilder(); // StringBuilder cho kết quả.
		var keyIndex = 0; // Index hiện tại trong key (lặp).
		for (char c : plaintext.toCharArray()) { // Duyệt từng ký tự trong plaintext.
			if (Character.isLetter(c)) { // Nếu là chữ cái.
				var base = Character.isUpperCase(c) ? 'A' : 'a'; // Base: 'A' cho hoa, 'a' cho thường.
				var keyChar = key.charAt(keyIndex % key.length()); // Lấy char key lặp (mod độ dài key).
				var shift = keyChar - 'A'; // Shift = vị trí key char trong alphabet (0-25).
				// Công thức: (c - base + shift) % 26 + base → dịch và wrap.
				result.append((char) ((c - base + shift) % 26 + base));
				keyIndex++; // Tăng index key cho chữ cái tiếp theo.
			} else {
				result.append(c); // Giữ nguyên ký tự không phải chữ cái (space, etc.).
			}
		}
		return result.toString(); // Trả về kết quả.
	}

	/**
	 * Method giải mã Vigenere: Giữ nguyên.
	 */
	private String decryptVigenere(String ciphertext, String key) {
		var result = new StringBuilder(); // Builder kết quả.
		var keyIndex = 0; // Index key lặp.
		for (char c : ciphertext.toCharArray()) { // Duyệt ciphertext.
			if (Character.isLetter(c)) { // Nếu chữ cái.
				var base = Character.isUpperCase(c) ? 'A' : 'a'; // Base case.
				var keyChar = key.charAt(keyIndex % key.length()); // Key char lặp.
				var shift = keyChar - 'A'; // Shift từ key.
				// Công thức giải: (c - base - shift + 26) % 26 + base → dịch ngược.
				result.append((char) ((c - base - shift + 26) % 26 + base));
				keyIndex++; // Tăng index.
			} else {
				result.append(c); // Giữ nguyên.
			}
		}
		return result.toString(); // Trả về.
	}
}