package entity; // Package entity cho cipher classes (giữ nguyên cấu trúc project).

// Import AWT cho layout (BorderLayout, GridLayout) và events.
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
 * Lớp CaesarCipher: JFrame cho thuật toán Caesar Cipher với giao diện cân bằng
 * hoàn hảo. Layout: BorderLayout frame + GridBagLayout cho input/output panel
 * con để align thẳng hàng. Thêm nút quay về menu chính.
 */
public class CaesarCipher extends JFrame { // Kế thừa JFrame để tạo cửa sổ chính.

	private JTextArea inputTextArea;
	private JTextField inputKeyField;
	private JButton encryptButton;
	private JTextArea outputTextArea;
	private JTextField outputKeyField;
	private JButton decryptButton;
	private JScrollPane inputScrollPane;
	private JScrollPane outputScrollPane;
	private static final Color BG_COLOR = new Color(240, 248, 255); // Xanh nhạt
	private static final Color BUTTON_COLOR = new Color(173, 216, 230); // Xanh nhạt cho button
	private static final Color HOVER_COLOR = new Color(135, 206, 235); // Xanh sáng hơn khi hover
	private static final Color BORDER_COLOR = new Color(70, 130, 180); // Xanh đậm cho border
	private static final Color BACK_BUTTON_COLOR = new Color(255, 182, 193); // Màu hồng nhạt cho nút quay về

	/**
	 * Constructor: Khởi tạo UI, layout, và listeners.
	 */
	public CaesarCipher() {
		initLookAndFeel();
		// Gọi method
		initComponents();
		setupEventHandlers();
		setTitle("Caesar Cipher"); // Tiêu đề
		setSize(750, 600); // Kích thước
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng frame mà không thoát app
		setResizable(false); // Không cho co giãn
		getContentPane().setBackground(BG_COLOR); // Themee
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
		setLayout(new BorderLayout());

		var titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBackground(BG_COLOR);
		titlePanel.setBorder(new EmptyBorder(15, 0, 15, 0));
		var titleLabel = new JLabel("Caesar Cipher Demo", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabel.setForeground(BORDER_COLOR);
		titlePanel.add(titleLabel, BorderLayout.CENTER);
		add(titlePanel, BorderLayout.NORTH);

		var centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBackground(BG_COLOR);
		centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding ngoài.

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

		// Hover ( như kiểu hiện màu khi di chuột vào đó)
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

		// logic chạy (Action: Hiển thị menu chính và đóng frame nàyƯ)
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

		var keyPanel = new JPanel();
		keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.Y_AXIS));
		keyPanel.setOpaque(false);
		keyPanel.setPreferredSize(new Dimension(180, 0));

		var keyLabel = createStyledLabel("Key:");
		keyPanel.add(keyLabel);

		var keyField = (sectionType.equals("Input")) ? createStyledTextField("5") : createStyledTextField("0");
		keyField.setMaximumSize(new Dimension(120, 30));
		keyField.setAlignmentX(0.5f);
		if (sectionType.equals("Input")) {
			inputKeyField = keyField;
			inputKeyField.setToolTipText("Nhập số shift (0-25)");
		} else {
			outputKeyField = keyField;
			outputKeyField.setToolTipText("Nhập số shift để giải mã");
		}
		keyPanel.add(keyField);

		// Button
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
		field.setColumns(5);
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
		button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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
	 * Method thiết lập event cho buttons. Clear placeholder khi dùng key. Xóa kiểm
	 * tra input
	 */
	private void setupEventHandlers() {

		encryptButton.addActionListener(e -> {
			var input = inputTextArea.getText().trim();
			if (input.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng nhập văn bản!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			var keyStr = inputKeyField.getText().trim();
			if (keyStr.isEmpty() || keyStr.equals("5")) {
				keyStr = "5";
			}
			inputKeyField.setForeground(Color.BLACK);
			int shift;
			try {
				shift = Integer.parseInt(keyStr);
				shift = Math.floorMod(shift, 26);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi: Key phải là số nguyên!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			var encrypted = encryptCaesar(input, shift);
			outputTextArea.setText(encrypted);
			outputKeyField.setText(String.valueOf(shift));
			outputKeyField.setForeground(Color.BLACK);
			inputTextArea.requestFocus();
		});

		decryptButton.addActionListener(e -> {
			var input = outputTextArea.getText().trim();
			if (input.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng mã hóa trước!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			var keyStr = outputKeyField.getText().trim();
			if (keyStr.isEmpty() || keyStr.equals("0")) {
				keyStr = "0";
			}
			outputKeyField.setForeground(Color.BLACK);
			int shift;
			try {
				shift = Integer.parseInt(keyStr);
				shift = Math.floorMod(shift, 26);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi: Key phải là số nguyên!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			var decrypted = decryptCaesar(input, shift);
			inputTextArea.setText(decrypted);
		});
	}

	/**
	 * Method mã hóa Caesar
	 */
	private String encryptCaesar(String plaintext, int shift) {
		var result = new StringBuilder();
		for (char c : plaintext.toCharArray()) {
			if (Character.isLetter(c)) {
				var base = Character.isUpperCase(c) ? 'A' : 'a';
				result.append((char) ((c - base + shift) % 26 + base));
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}

	/**
	 * Method giải mã Caesar
	 */
	private String decryptCaesar(String ciphertext, int shift) {
		return encryptCaesar(ciphertext, 26 - shift);
	}
}