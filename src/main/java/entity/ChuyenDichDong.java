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

import app.Program;

/**
 * Lớp ChuyenDichDong: JFrame cho thuật toán Chuyển dịch dòng (Row Transposition
 * Cipher) với giao diện cân bằng hiện đại. Layout: BorderLayout frame +
 * GridBagLayout cho input/output panel con để align thẳng hàng. Thêm nút quay
 * về menu chính.
 */
public class ChuyenDichDong extends JFrame {
	private JTextArea inputTextArea;
	private JTextField inputKeyField;
	private JButton encryptButton;

	private JTextArea outputTextArea;
	private JTextField outputKeyField;
	private JButton decryptButton;

	private JScrollPane inputScrollPane;
	private JScrollPane outputScrollPane;

	// Colors cho theme
	private static final Color BG_COLOR = new Color(240, 248, 255); // Xanh
	private static final Color BUTTON_COLOR = new Color(173, 216, 230); // Xanh
	private static final Color HOVER_COLOR = new Color(135, 206, 235); // Xanh
	private static final Color BORDER_COLOR = new Color(70, 130, 180); // Xanh
	private static final Color BACK_BUTTON_COLOR = new Color(255, 182, 193); // Màu hồng

	/**
	 * Constructor: Khởi tạo UI, layout, và listeners.
	 */
	public ChuyenDichDong() {
		initLookAndFeel();
		initComponents();
		setupEventHandlers();
		setTitle("Chuyển Dịch Dòng");
		setSize(750, 600);
		setLocationRelativeTo(null); // Đặt frame ở giữa màn hình.
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng frame mà không thoát app.
		setResizable(false); // Không cho resize để giữ layout.
		getContentPane().setBackground(BG_COLOR); // Nền frame.
		inputTextArea.requestFocus(); // Auto focus input khi mở.
	}

	/**
	 * Áp dụng Nimbus Look and Feel cho giao diện
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
	 * Method initComponents: Layout cân bằng với panel con cho input output
	 */
	private void initComponents() {
		setLayout(new BorderLayout());
		var titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBackground(BG_COLOR);
		titlePanel.setBorder(new EmptyBorder(15, 0, 15, 0));
		var titleLabel = new JLabel("Chuyển Dịch Dòng Demo", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabel.setForeground(BORDER_COLOR);
		titlePanel.add(titleLabel, BorderLayout.CENTER);
		add(titlePanel, BorderLayout.NORTH);
		var centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBackground(BG_COLOR);
		centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding ngoài
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

		// Như bên ceasar
		button.addActionListener(e -> {
			if (Program.getMainFrame() != null) {
				Program.getMainFrame().setVisible(true);
			}
			dispose();
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
		scrollPane.setPreferredSize(new Dimension(500, 150)); //

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
		var keyField = (sectionType.equals("Input")) ? createStyledTextField("5") : createStyledTextField("5");
		keyField.setMaximumSize(new Dimension(120, 30));
		keyField.setAlignmentX(0.5f);
		if (sectionType.equals("Input")) {
			inputKeyField = keyField;
			inputKeyField.setToolTipText("Nhập số cột (>0)");
		} else {
			outputKeyField = keyField;
			outputKeyField.setToolTipText("Nhập số cột để giải mã");
		}
		keyPanel.add(keyField);
		var button = (sectionType.equals("Input")) ? createStyledButton("Encryption", "Mã hóa văn bản")
				: createStyledButton("Decryption", "Giải mã văn bản");
		button.setMaximumSize(new Dimension(160, 50));
		button.setAlignmentX(0.5f);
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
			var keyStr = inputKeyField.getText().trim();
			if (keyStr.isEmpty() || keyStr.equals("5")) {
				keyStr = "5";
			}
			inputKeyField.setForeground(Color.BLACK);
			int cols;
			try {
				cols = Integer.parseInt(keyStr);
				if (cols <= 0) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi: Key phải là số nguyên >0!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			var encrypted = encryptRowTransposition(input.toUpperCase().replaceAll("[^A-Z]", ""), cols);
			outputTextArea.setText(encrypted);
			outputKeyField.setText(keyStr);
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
			if (keyStr.isEmpty() || keyStr.equals("5")) {
				keyStr = "5";
			}
			outputKeyField.setForeground(Color.BLACK);
			int cols;
			try {
				cols = Integer.parseInt(keyStr);
				if (cols <= 0) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi: Key phải là số nguyên >0!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			var decrypted = decryptRowTransposition(input, cols);
			inputTextArea.setText(decrypted);
		});
	}

	/**
	 * Method mã hóa
	 */
	private String encryptRowTransposition(String text, int cols) {
		var len = text.length();
		var rows = (int) Math.ceil((double) len / cols);
		var grid = new char[rows][cols];
		var index = 0;
		for (var i = 0; i < rows; i++) {
			for (var j = 0; j < cols; j++) {
				if (index < len) {
					grid[i][j] = text.charAt(index++);
				} else {
					grid[i][j] = 'X';
				}
			}
		}

		var result = new StringBuilder();
		for (var j = 0; j < cols; j++) {
			for (var i = 0; i < rows; i++) {
				result.append(grid[i][j]);
			}
		}
		return result.toString();
	}

	/**
	 * Method giải mã Row Transposition
	 */
	private String decryptRowTransposition(String ciphertext, int cols) {
		var len = ciphertext.length();
		var rows = (int) Math.ceil((double) len / cols);
		var grid = new char[rows][cols];

		var index = 0;
		for (var j = 0; j < cols; j++) {
			for (var i = 0; i < rows; i++) {
				if (index < len) {
					grid[i][j] = ciphertext.charAt(index++);
				} else {
					grid[i][j] = 'X';
				}
			}
		}

		var result = new StringBuilder();
		for (var i = 0; i < rows; i++) {
			for (var j = 0; j < cols; j++) {
				result.append(grid[i][j]);
			}
		}

		var raw = result.toString();
		var lastNonX = raw.lastIndexOf('X');
		if (lastNonX != -1 && lastNonX < raw.length() - 1) {
			raw = raw.substring(0, lastNonX + 1);

			var spaced = new StringBuilder(raw.toLowerCase());
			for (var i = spaced.length() - 1; i > 0; i -= 3) {
				if (spaced.charAt(i) != ' ') {
					spaced.insert(i, ' ');
				}
			}
			return spaced.toString().trim();
		}
		return raw;
	}
}

/**
 * Method main: Điểm khởi đầu chương trình (demo chạy độc lập). Set Nimbus Look
 * and Feel nếu có (giống NetBeans generated).
 */
//	public static void main(String args[]) {
//		/* Set Nimbus L&F (đã di chuyển vào constructor).
//		EventQueue.invokeLater(() -> {
//			new ChuyenDichDong().setVisible(true);
//		});*/
//	}
//}