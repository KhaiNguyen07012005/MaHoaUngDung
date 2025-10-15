package entity; // Package entity cho cipher classes (giữ nguyên cấu trúc project).

// Import AWT cho layout (BorderLayout, GridBagLayout) và events.
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
// Import Util cho Random và HashMap (tạo key random, bảng thay thế).
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
 * Lớp MaHoaBangChuDon: JFrame cho thuật toán Bảng chữ đơn (Monoalphabetic
 * Cipher) với layout giống demo. Layout: Input textarea trên cùng với text
 * "chao cac ban", key field (26 chữ) + button "Random key" + "Encryption" bên
 * phải. Dưới: Output textarea với kết quả, key field (giống input) +
 * "Decryption" bên phải. Logic: Thay thế mỗi chữ cái theo bảng key (A-Z →
 * key[0-25]), giữ case và ký tự khác (space → space). Button "Random key":
 * Shuffle alphabet để tạo key mới 26 chữ duy nhất. Demo: Input "chao cac ban"
 * key "mwgdkvzelbnhsxpriquajfcyto" → Output "gym n gm wb".
 */
public class MaHoaBangChuDon extends JFrame { // Kế thừa JFrame để tạo cửa sổ chính.

	// Components cho input section.
	private JTextArea inputTextArea; // TextArea nhập plaintext (trên cùng).
	private JTextField inputKeyField; // TextField key cho encrypt (26 chữ cái).
	private JButton randomKeyButton; // Button "Random key" bên cạnh key input.
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
	public MaHoaBangChuDon() {
		initComponents(); // Gọi method tạo và layout components (tương tự NetBeans generated).
		setupEventHandlers(); // Gọi method thiết lập listeners cho buttons.
		setTitle("Bảng chữ đơn demo"); // Title frame giống demo.
		setSize(600, 500); // Kích thước frame (rộng hơn để giống demo).
		setLocationRelativeTo(null); // Đặt frame ở giữa màn hình.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Đóng frame = thoát app (demo đơn lẻ).
		setResizable(false); // Không cho resize để giữ layout.
	}

	/**
	 * Method initComponents: Tạo components và layout giống screenshot demo. Sử
	 * dụng BorderLayout cho frame: NORTH cho input section, CENTER cho output
	 * section. Mỗi section dùng GridBagLayout để đặt textarea trái,
	 * key/random/encrypt phải.
	 */
	private void initComponents() {
		// Tạo panel cho input section (NORTH của frame).
		var inputPanel = new JPanel(new GridBagLayout()); // GridBagLayout để linh hoạt vị trí.
		var gbc = new GridBagConstraints(); // Constraints cho GridBag.
		gbc.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa components 5px.

		// Tạo input textarea (trái input panel).
		inputTextArea = new JTextArea(3, 25); // 3 dòng, 25 cột (giống demo).
		inputTextArea.setText("chao cac ban"); // Text demo mặc định.
		inputTextArea.setLineWrap(true); // Tự động wrap dòng.
		inputTextArea.setWrapStyleWord(true); // Wrap theo từ.
		inputScrollPane = new JScrollPane(inputTextArea); // Bọc scroll cho textarea.
		gbc.gridx = 0; // Cột 0 (trái).
		gbc.gridy = 0; // Hàng 0 (trên).
		gbc.gridwidth = 1; // Chiếm 1 cột.
		gbc.weightx = 1.0; // Mở rộng theo x.
		gbc.fill = GridBagConstraints.BOTH; // Fill cả chiều rộng/cao.
		inputPanel.add(inputScrollPane, gbc); // Thêm scroll vào panel.

		// Tạo label "key" cho input key (phải input textarea, hàng 0 cột 1).
		var inputKeyLabel = new JLabel("key"); // Label "key".
		gbc.gridx = 1; // Cột 1 (phải).
		gbc.gridy = 0; // Hàng 0.
		gbc.gridwidth = 1; // 1 cột.
		gbc.weightx = 0.0; // Không mở rộng.
		gbc.fill = GridBagConstraints.NONE; // Không fill.
		inputPanel.add(inputKeyLabel, gbc); // Thêm label.

		// Tạo input key field (hàng 1 cột 1, dài cho 26 chữ).
		inputKeyField = new JTextField(26); // Chiều rộng 26 ký tự.
		inputKeyField.setText("mwgdkvzelbnhsxpriquajfcyto"); // Key demo từ screenshot.
		gbc.gridy = 1; // Hàng 1 (dưới label).
		inputPanel.add(inputKeyField, gbc); // Thêm field.

		// Tạo random key button (hàng 2 cột 1, bên dưới key field).
		randomKeyButton = new JButton("Random key"); // Button text giống demo.
		gbc.gridy = 2; // Hàng 2.
		inputPanel.add(randomKeyButton, gbc); // Thêm button.

		// Tạo encrypt button (hàng 3 cột 1, dưới random button).
		encryptButton = new JButton("Encryption"); // Button text giống demo.
		gbc.gridy = 3; // Hàng 3.
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

		// Tạo label "key" cho output key (phải output textarea, hàng 0 cột 1).
		var outputKeyLabel = new JLabel("key"); // Label "key" thứ 2.
		gbcOut.gridx = 1; // Cột 1.
		gbcOut.gridy = 0; // Hàng 0.
		gbcOut.gridwidth = 1;
		gbcOut.weightx = 0.0;
		gbcOut.fill = GridBagConstraints.NONE;
		outputPanel.add(outputKeyLabel, gbcOut);

		// Tạo output key field (hàng 1 cột 1, copy từ input).
		outputKeyField = new JTextField(26); // Tương tự input key.
		gbcOut.gridy = 1; // Hàng 1.
		outputPanel.add(outputKeyField, gbcOut);

		// Tạo decrypt button (hàng 2 cột 1, dưới key field output).
		decryptButton = new JButton("Decryption"); // Button text giống demo.
		gbcOut.gridy = 2; // Hàng 2.
		outputPanel.add(decryptButton, gbcOut);

		// Layout frame: BorderLayout với inputPanel NORTH, outputPanel CENTER.
		setLayout(new BorderLayout()); // BorderLayout cho frame.
		add(inputPanel, BorderLayout.NORTH); // Input section trên.
		add(outputPanel, BorderLayout.CENTER); // Output section giữa (dưới input).
	}

	/**
	 * Method thiết lập event handlers (listeners) cho buttons. Random key: Shuffle
	 * alphabet → tạo key mới, set vào cả input/output key fields. Encrypt: Lấy
	 * input từ inputTextArea + inputKeyField → mã hóa → hiển thị outputTextArea +
	 * copy key. Decrypt: Lấy input từ outputTextArea + outputKeyField → giải mã →
	 * hiển thị inputTextArea.
	 */
	private void setupEventHandlers() {
		// Listener cho random key button: Tạo key random 26 chữ duy nhất.
		randomKeyButton.addActionListener(e -> {
			var randomKey = generateRandomKey(); // Gọi method tạo key random.
			inputKeyField.setText(randomKey); // Set vào input key field.
			outputKeyField.setText(randomKey); // Set vào output key field (đồng bộ).
		});

		// Listener cho encrypt button: Mã hóa khi nhấn.
		encryptButton.addActionListener(e -> {
			var input = inputTextArea.getText().trim(); // Lấy văn bản từ input textarea và loại khoảng trắng thừa.
			if (input.isEmpty()) { // Kiểm tra input rỗng.
				JOptionPane.showMessageDialog(MaHoaBangChuDon.this, "Lỗi: Vui lòng nhập văn bản!"); // Hiển thị
																									// dialog lỗi.
				return; // Thoát nếu rỗng.
			}
			var keyStr = inputKeyField.getText().trim().toUpperCase(); // Lấy key và uppercase.
			if (keyStr.length() != 26 || !keyStr.matches("[A-Z]+")) { // Kiểm tra key: đúng 26 chữ hoa, không lặp
																		// (kiểm tra duy nhất).
				if (!isUniqueChars(keyStr)) { // Kiểm tra lặp.
					JOptionPane.showMessageDialog(MaHoaBangChuDon.this, "Lỗi: Key phải 26 chữ cái hoa DUY NHẤT!"); // Dialog
																													// lỗi.
					return;
				}
				JOptionPane.showMessageDialog(MaHoaBangChuDon.this, "Lỗi: Key phải đúng 26 chữ cái hoa!"); // Dialog
																											// lỗi.
				return;
			}
			var encrypted = encryptMonoalphabetic(input, keyStr); // Gọi method mã hóa.
			outputTextArea.setText(encrypted); // Hiển thị kết quả vào output textarea (không prefix để giống demo).
			outputKeyField.setText(keyStr); // Copy key từ input sang output cho decrypt.
		});

		// Listener cho decrypt button: Giải mã khi nhấn.
		decryptButton.addActionListener(e -> {
			var input = outputTextArea.getText().trim(); // Lấy ciphertext từ output textarea.
			if (input.isEmpty()) { // Kiểm tra rỗng.
				JOptionPane.showMessageDialog(MaHoaBangChuDon.this, "Lỗi: Vui lòng mã hóa trước!"); // Dialog lỗi.
				return;
			}
			var keyStr = outputKeyField.getText().trim().toUpperCase(); // Lấy key và uppercase.
			if (keyStr.length() != 26 || !keyStr.matches("[A-Z]+") || !isUniqueChars(keyStr)) {
				JOptionPane.showMessageDialog(MaHoaBangChuDon.this, "Lỗi: Key phải 26 chữ cái hoa DUY NHẤT!"); // Lỗi.
				return;
			}
			var decrypted = decryptMonoalphabetic(input, keyStr); // Gọi method giải mã.
			inputTextArea.setText(decrypted); // Hiển thị kết quả vào input textarea (quay về gốc).
		});
	}

	/**
	 * Helper method: Tạo key random 26 chữ cái duy nhất (shuffle alphabet A-Z).
	 *
	 * @return String key 26 chữ uppercase.
	 */
	private String generateRandomKey() {
		var alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Chuỗi alphabet đầy đủ.
		var chars = alphabet.toCharArray(); // Chuyển thành mảng char.
		var random = new Random(); // Tạo Random instance.
		for (var i = chars.length - 1; i > 0; i--) { // Shuffle Fisher-Yates.
			var j = random.nextInt(i + 1); // Chọn index ngẫu nhiên.
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
		Set<Character> set = new HashSet<>(); // HashSet để kiểm tra duplicate.
		for (char c : key.toCharArray()) { // Duyệt từng char.
			if (!set.add(c)) { // Nếu add fail → duplicate.
				return false; // Trả false.
			}
		}
		return true; // Duy nhất.
	}

	/**
	 * Method mã hóa Monoalphabetic: Thay thế mỗi chữ cái theo bảng key (A→key[0],
	 * B→key[1],...). Giữ nguyên ký tự không phải chữ cái (space → space).
	 *
	 * @param plaintext: Văn bản gốc.
	 * @param key:       Key 26 chữ uppercase.
	 * @return Văn bản đã mã hóa.
	 */
	private String encryptMonoalphabetic(String plaintext, String key) {
		Map<Character, Character> table = new HashMap<>(); // HashMap lưu bảng thay thế.
		for (var i = 0; i < 26; i++) { // Duyệt 0-25.
			table.put((char) ('A' + i), key.charAt(i)); // Ánh xạ A+i → key[i].
		}
		var result = new StringBuilder(); // StringBuilder cho kết quả.
		for (char c : plaintext.toCharArray()) { // Duyệt plaintext.
			var upperC = Character.toUpperCase(c); // Chuyển uppercase để tra bảng.
			if (Character.isLetter(c)) { // Nếu chữ cái.
				char replacement = table.get(upperC); // Lấy thay thế (uppercase).
				// Giữ case gốc: Nếu input hoa → output hoa, thường → thường.
				result.append(Character.isUpperCase(c) ? replacement : Character.toLowerCase(replacement));
			} else {
				result.append(c); // Giữ nguyên ký tự khác.
			}
		}
		return result.toString(); // Trả về kết quả.
	}

	/**
	 * Method giải mã Monoalphabetic: Sử dụng bảng ngược (key[i] → A+i).
	 *
	 * @param ciphertext: Văn bản mã hóa.
	 * @param key:        Key gốc.
	 * @return Văn bản gốc.
	 */
	private String decryptMonoalphabetic(String ciphertext, String key) {
		Map<Character, Character> reverseTable = new HashMap<>(); // HashMap cho bảng ngược.
		for (var i = 0; i < 26; i++) { // Duyệt 0-25.
			reverseTable.put(key.charAt(i), (char) ('A' + i)); // Ánh xạ key[i] → A+i.
		}
		var result = new StringBuilder(); // Builder kết quả.
		for (char c : ciphertext.toCharArray()) { // Duyệt ciphertext.
			var upperC = Character.toUpperCase(c); // Uppercase để tra.
			if (Character.isLetter(c)) { // Nếu chữ cái.
				char original = reverseTable.get(upperC); // Lấy gốc (uppercase).
				// Giữ case.
				result.append(Character.isUpperCase(c) ? original : Character.toLowerCase(original));
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
	// main này chạy thử demo
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
//			new MaHoaBangChuDon().setVisible(true); // Tạo và hiển thị frame.
//		});
//	}
}