package entity; // Package entity cho cipher classes (giữ nguyên cấu trúc project).

// Import AWT cho layout (BorderLayout, GridBagLayout) và events.
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

// Import Swing cho UI components (JFrame, JTextArea, etc.).
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Lớp VigenereCipher: JFrame cho thuật toán Vigenere Cipher với layout giống
 * demo series. Layout: Input textarea trên cùng với text demo "attack at dawn",
 * key field + "Encryption" bên phải. Dưới: Output textarea với kết quả, key
 * field (giống input) + "Decryption" bên phải. Logic: Lặp key để khớp độ dài
 * text, shift từng chữ cái theo key char (key.char - 'A'), modulo 26, giữ case
 * và ký tự khác. Demo: Input "attack at dawn" key "lemon" → Output "lxfopve
 * frpsrq".
 */
public class VigenereCipher extends JFrame { // Kế thừa JFrame để tạo cửa sổ chính.

	// Components cho input section.
	private JTextArea inputTextArea; // TextArea nhập plaintext (trên cùng).
	private JTextField inputKeyField; // TextField key cho encrypt (string để lặp).
	private JButton encryptButton; // Button "Encryption" bên dưới key input.

	// Components cho output section.
	private JTextArea outputTextArea; // TextArea hiển thị ciphertext (dưới input).
	private JTextField outputKeyField; // TextField key cho decrypt (giống input key).
	private JButton decryptButton; // Button "Decryption" bên dưới key output.

	// Scroll panes cho textarea.
	private JScrollPane inputScrollPane; // Scroll cho input textarea.
	private JScrollPane outputScrollPane; // Scroll cho output textarea.

	/**
	 * Constructor: Khởi tạo UI, layout, và listeners.
	 */
	public VigenereCipher() {
		initComponents(); // Gọi method tạo và layout components (tương tự NetBeans generated).
		setupEventHandlers(); // Gọi method thiết lập listeners cho buttons.
		setTitle("Vigenere cipher demo"); // Title frame (theo pattern demo).
		setSize(600, 500); // Kích thước frame (rộng hơn để giống demo).
		setLocationRelativeTo(null); // Đặt frame ở giữa màn hình.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Đóng frame = thoát app (demo đơn lẻ).
		setResizable(false); // Không cho resize để giữ layout.
	}

	/**
	 * Method initComponents: Tạo components và layout giống pattern demo (tương tự
	 * Caesar/Mono/Playfair). Sử dụng BorderLayout cho frame: NORTH cho input
	 * section, CENTER cho output section. Mỗi section dùng GridBagLayout để đặt
	 * textarea trái, key/encrypt phải.
	 */
	private void initComponents() {
		// Tạo panel cho input section (NORTH của frame).
		var inputPanel = new JPanel(new GridBagLayout()); // GridBagLayout để linh hoạt vị trí.
		var gbc = new GridBagConstraints(); // Constraints cho GridBag.
		gbc.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa components 5px.

		// Tạo input textarea (trái input panel).
		inputTextArea = new JTextArea(3, 25); // 3 dòng, 25 cột (giống demo).
		inputTextArea.setText("attack at dawn"); // Text demo chuẩn cho Vigenere.
		inputTextArea.setLineWrap(true); // Tự động wrap dòng.
		inputTextArea.setWrapStyleWord(true); // Wrap theo từ.
		inputScrollPane = new JScrollPane(inputTextArea); // Bọc scroll cho textarea.
		gbc.gridx = 0; // Cột 0 (trái).
		gbc.gridy = 0; // Hàng 0 (trên).
		gbc.gridwidth = 1; // Chiếm 1 cột.
		gbc.weightx = 1.0; // Mở rộng theo x.
		gbc.fill = GridBagConstraints.BOTH; // Fill cả chiều rộng/cao.
		inputPanel.add(inputScrollPane, gbc); // Thêm scroll vào panel.

		// Tạo label "Key" cho input key (phải input textarea).
		var inputKeyLabel = new JLabel("Key"); // Label "Key".
		gbc.gridx = 1; // Cột 1 (phải).
		gbc.gridy = 0; // Hàng 0.
		gbc.gridwidth = 1; // 1 cột.
		gbc.weightx = 0.0; // Không mở rộng.
		gbc.fill = GridBagConstraints.NONE; // Không fill.
		inputPanel.add(inputKeyLabel, gbc); // Thêm label.

		// Tạo input key field (dưới label key input).
		inputKeyField = new JTextField("lemon"); // Key demo chuẩn.
		inputKeyField.setColumns(10); // Chiều rộng phù hợp.
		gbc.gridy = 1; // Hàng 1 (dưới).
		inputPanel.add(inputKeyField, gbc); // Thêm field.

		// Tạo encrypt button (dưới key field input).
		encryptButton = new JButton("Encryption"); // Button text giống demo.
		gbc.gridy = 2; // Hàng 2 (dưới).
		inputPanel.add(encryptButton, gbc); // Thêm button.

		// Tạo panel cho output section (CENTER của frame).
		var outputPanel = new JPanel(new GridBagLayout()); // Tương tự input panel.
		var gbcOut = new GridBagConstraints(); // Constraints riêng cho output.
		gbcOut.insets = new Insets(5, 5, 5, 5);

		// Tạo output textarea (trái output panel, dưới input).
		outputTextArea = new JTextArea(3, 25); // Tương tự input.
		outputTextArea.setLineWrap(true);
		outputTextArea.setWrapStyleWord(true);
		outputTextArea.setEditable(false); // Không cho edit output.
		outputScrollPane = new JScrollPane(outputTextArea); // Scroll cho output.
		gbcOut.gridx = 0; // Cột 0.
		gbcOut.gridy = 0; // Hàng 0.
		gbcOut.gridwidth = 1;
		gbcOut.weightx = 1.0;
		gbcOut.fill = GridBagConstraints.BOTH;
		outputPanel.add(outputScrollPane, gbcOut);

		// Tạo label "Key" cho output key (phải output textarea).
		var outputKeyLabel = new JLabel("Key"); // Label "Key" thứ 2.
		gbcOut.gridx = 1; // Cột 1.
		gbcOut.gridy = 0; // Hàng 0.
		gbcOut.gridwidth = 1;
		gbcOut.weightx = 0.0;
		gbcOut.fill = GridBagConstraints.NONE;
		outputPanel.add(outputKeyLabel, gbcOut);

		// Tạo output key field (dưới label key output).
		outputKeyField = new JTextField("lemon"); // Copy key demo.
		outputKeyField.setColumns(10);
		gbcOut.gridy = 1; // Hàng 1.
		outputPanel.add(outputKeyField, gbcOut);

		// Tạo decrypt button (dưới key field output).
		decryptButton = new JButton("Decryption"); // Button text giống demo.
		gbcOut.gridy = 2; // Hàng 2.
		outputPanel.add(decryptButton, gbcOut);

		// Layout frame: BorderLayout với inputPanel NORTH, outputPanel CENTER.
		setLayout(new BorderLayout()); // BorderLayout cho frame.
		add(inputPanel, BorderLayout.NORTH); // Input section trên.
		add(outputPanel, BorderLayout.CENTER); // Output section giữa (dưới input).
	}

	/**
	 * Method thiết lập event handlers (listeners) cho buttons. Encrypt: Lấy input
	 * từ inputTextArea + inputKeyField → lặp key → mã hóa → hiển thị outputTextArea
	 * + copy key. Decrypt: Lấy input từ outputTextArea + outputKeyField → giải mã →
	 * hiển thị inputTextArea.
	 */
	private void setupEventHandlers() {
		// Listener cho encrypt button: Mã hóa khi nhấn.
		encryptButton.addActionListener(e -> {
			var input = inputTextArea.getText().trim(); // Lấy văn bản từ input textarea và loại khoảng trắng thừa.
			if (input.isEmpty()) { // Kiểm tra input rỗng.
				JOptionPane.showMessageDialog(VigenereCipher.this, "Lỗi: Vui lòng nhập văn bản!"); // Hiển thị
																									// dialog lỗi.
				return; // Thoát nếu rỗng.
			}
			var keyStr = inputKeyField.getText().trim(); // Lấy key từ input key field.
			if (keyStr.isEmpty() || !keyStr.matches("[a-zA-Z]+")) { // Kiểm tra key rỗng hoặc không phải chữ cái.
				JOptionPane.showMessageDialog(VigenereCipher.this, "Lỗi: Key phải là chuỗi chữ cái!"); // Dialog
																										// lỗi.
				return;
			}
			var encrypted = encryptVigenere(input, keyStr.toUpperCase()); // Gọi method mã hóa (key uppercase).
			outputTextArea.setText(encrypted); // Hiển thị kết quả vào output (giữ case gốc).
			outputKeyField.setText(keyStr); // Copy key từ input sang output cho decrypt.
		});

		// Listener cho decrypt button: Giải mã khi nhấn.
		decryptButton.addActionListener(e -> {
			var input = outputTextArea.getText().trim(); // Lấy ciphertext từ output textarea.
			if (input.isEmpty()) { // Kiểm tra rỗng.
				JOptionPane.showMessageDialog(VigenereCipher.this, "Lỗi: Vui lòng mã hóa trước!"); // Dialog lỗi.
				return;
			}
			var keyStr = outputKeyField.getText().trim(); // Lấy key từ output key field.
			if (keyStr.isEmpty() || !keyStr.matches("[a-zA-Z]+")) {
				JOptionPane.showMessageDialog(VigenereCipher.this, "Lỗi: Key phải là chuỗi chữ cái!"); // Lỗi.
				return;
			}
			var decrypted = decryptVigenere(input, keyStr.toUpperCase()); // Gọi method giải mã.
			inputTextArea.setText(decrypted); // Hiển thị kết quả vào input textarea (giữ case).
		});
	}

	/**
	 * Method mã hóa Vigenere: Lặp key để khớp độ dài, dịch từng chữ cái theo shift
	 * từ key char.
	 *
	 * @param plaintext: Văn bản gốc.
	 * @param key:       Key uppercase.
	 * @return Văn bản đã mã hóa (giữ case và ký tự khác).
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
	 * Method giải mã Vigenere: Dịch ngược theo key lặp (shift âm: -shift +26 %26).
	 *
	 * @param ciphertext: Văn bản mã hóa.
	 * @param key:        Key uppercase.
	 * @return Văn bản gốc.
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

	/**
	 * Method main: Điểm khởi đầu chương trình (demo chạy độc lập). Set Nimbus Look
	 * and Feel nếu có (giống NetBeans generated).
	 *
	 * @param args: Tham số dòng lệnh (không dùng).
	 */
	// main test thử
//	public static void main(String args[]) {
//		// Set Nimbus L&F (nếu available, giống demo screenshot).
//		try {
//			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//				if ("Nimbus".equals(info.getName())) { // Tìm Nimbus.
//					UIManager.setLookAndFeel(info.getClassName()); // Áp dụng nếu tìm thấy.
//					break;
//				}
//			}
//		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
//				| UnsupportedLookAndFeelException ex) {
//			// Bỏ qua lỗi L&F, dùng default.
//		}
//
//		// Chạy trên Event Dispatch Thread (EDT) để Swing an toàn.
//		EventQueue.invokeLater(() -> {
//			new VigenereCipher().setVisible(true); // Tạo và hiển thị frame.
//		});
//	}
}