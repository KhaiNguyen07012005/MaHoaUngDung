package entity; // Package entity cho cipher classes (giữ nguyên cấu trúc project).

// Import AWT cho layout (BorderLayout, GridBagLayout) và events.
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
// Import Util cho ArrayList và Arrays (xử lý ma trận, pairs).
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 * Lớp PlayfairCipher: JFrame cho thuật toán Playfair Cipher với layout giống
 * demo series. Layout: Input textarea trên cùng với text demo "hide the gold",
 * key field + "Encryption" bên phải. Dưới: Output textarea với kết quả, key
 * field (giống input) + "Decryption" bên phải. Logic: Tạo ma trận 5x5 từ key
 * (loại duplicate, I/J chung), chuẩn hóa text (cặp chữ, thêm X), mã hóa/giải mã
 * theo quy tắc (hàng/cột/chéo). Demo: Input "hide the gold" key "playfair" →
 * Output "BMODZBXDNAB" (uppercase, không space).
 */
public class PlayfairCipher extends JFrame { // Kế thừa JFrame để tạo cửa sổ chính.

	// Components cho input section.
	private JTextArea inputTextArea; // TextArea nhập plaintext (trên cùng).
	private JTextField inputKeyField; // TextField key cho encrypt (string để tạo ma trận).
	private JButton encryptButton; // Button "Encryption" bên dưới key input.

	// Components cho output section.
	private JTextArea outputTextArea; // TextArea hiển thị ciphertext (dưới input).
	private JTextField outputKeyField; // TextField key cho decrypt (giống input key).
	private JButton decryptButton; // Button "Decryption" bên dưới key output.

	// Scroll panes cho textarea.
	private JScrollPane inputScrollPane; // Scroll cho input textarea.
	private JScrollPane outputScrollPane; // Scroll cho output textarea.

	// Ma trận Playfair 5x5 (instance variable để dùng chung encrypt/decrypt).
	private char[][] matrix = new char[5][5]; // Ma trận toàn cục cho key.

	/**
	 * Constructor: Khởi tạo UI, layout, và listeners.
	 */
	public PlayfairCipher() {
		initComponents(); // Gọi method tạo và layout components (tương tự NetBeans generated).
		setupEventHandlers(); // Gọi method thiết lập listeners cho buttons.
		setTitle("Playfair cipher demo"); // Title frame (theo pattern demo).
		setSize(600, 500); // Kích thước frame (rộng hơn để giống demo).
		setLocationRelativeTo(null); // Đặt frame ở giữa màn hình.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Đóng frame = thoát app (demo đơn lẻ).
		setResizable(false); // Không cho resize để giữ layout.
	}

	/**
	 * Method initComponents: Tạo components và layout giống pattern demo (tương tự
	 * Caesar/Mono). Sử dụng BorderLayout cho frame: NORTH cho input section, CENTER
	 * cho output section. Mỗi section dùng GridBagLayout để đặt textarea trái,
	 * key/encrypt phải.
	 */
	private void initComponents() {
		// Tạo panel cho input section (NORTH của frame).
		var inputPanel = new JPanel(new GridBagLayout()); // GridBagLayout để linh hoạt vị trí.
		var gbc = new GridBagConstraints(); // Constraints cho GridBag.
		gbc.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa components 5px.

		// Tạo input textarea (trái input panel).
		inputTextArea = new JTextArea(3, 25); // 3 dòng, 25 cột (giống demo).
		inputTextArea.setText("hide the gold"); // Text demo chuẩn cho Playfair.
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
		inputKeyField = new JTextField("playfair"); // Key demo chuẩn.
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
		outputKeyField = new JTextField("playfair"); // Copy key demo.
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
	 * từ inputTextArea + inputKeyField → chuẩn hóa → mã hóa → hiển thị
	 * outputTextArea + copy key. Decrypt: Lấy input từ outputTextArea +
	 * outputKeyField → giải mã → hiển thị inputTextArea (thêm cleanup X).
	 */
	private void setupEventHandlers() {
		// Listener cho encrypt button: Mã hóa khi nhấn.
		encryptButton.addActionListener(e -> {
			var input = inputTextArea.getText().trim(); // Lấy văn bản từ input textarea và loại khoảng trắng thừa.
			if (input.isEmpty()) { // Kiểm tra input rỗng.
				JOptionPane.showMessageDialog(PlayfairCipher.this, "Lỗi: Vui lòng nhập văn bản!"); // Hiển thị
																									// dialog lỗi.
				return; // Thoát nếu rỗng.
			}
			var keyStr = inputKeyField.getText().trim(); // Lấy key từ input key field.
			if (keyStr.isEmpty()) { // Kiểm tra key rỗng.
				JOptionPane.showMessageDialog(PlayfairCipher.this, "Lỗi: Vui lòng nhập key (string)!"); // Dialog
																										// lỗi.
				return;
			}
			try {
				generateMatrix(keyStr.toUpperCase()); // Tạo ma trận từ key (uppercase).
				var prepared = prepareText(input.toUpperCase().replaceAll("[^A-Z]", "")); // Chuẩn hóa: uppercase,
																							// chỉ chữ, cặp + X.
				var encrypted = encryptPlayfair(prepared); // Mã hóa cặp chữ.
				outputTextArea.setText(encrypted); // Hiển thị kết quả vào output (uppercase, không space).
				outputKeyField.setText(keyStr); // Copy key từ input sang output cho decrypt.
			} catch (Exception ex) { // Bắt lỗi (key invalid, etc.).
				JOptionPane.showMessageDialog(PlayfairCipher.this, "Lỗi: " + ex.getMessage()); // Dialog lỗi.
			}
		});

		// Listener cho decrypt button: Giải mã khi nhấn.
		decryptButton.addActionListener(e -> {
			var input = outputTextArea.getText().trim(); // Lấy ciphertext từ output textarea.
			if (input.isEmpty()) { // Kiểm tra rỗng.
				JOptionPane.showMessageDialog(PlayfairCipher.this, "Lỗi: Vui lòng mã hóa trước!"); // Dialog lỗi.
				return;
			}
			var keyStr = outputKeyField.getText().trim(); // Lấy key từ output key field.
			if (keyStr.isEmpty()) {
				JOptionPane.showMessageDialog(PlayfairCipher.this, "Lỗi: Vui lòng nhập key (string)!"); // Lỗi.
				return;
			}
			try {
				generateMatrix(keyStr.toUpperCase()); // Tạo ma trận.
				var decrypted = decryptPlayfair(input.toUpperCase()); // Giải mã và cleanup.
				inputTextArea.setText(decrypted.toLowerCase()); // Hiển thị vào input (lowercase, thêm space nếu
																// cần).
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(PlayfairCipher.this, "Lỗi: " + ex.getMessage()); // Lỗi.
			}
		});
	}

	/**
	 * Helper: Tạo ma trận 5x5 từ key (loại duplicate, I/J chung, điền alphabet còn
	 * lại).
	 *
	 * @param key: Key string uppercase.
	 */
	private void generateMatrix(String key) {
		var cleanedKey = new StringBuilder(); // Builder cho key sạch (không duplicate).
		var used = new boolean[26]; // Mảng đánh dấu chữ cái đã dùng (A=0, Z=25).
		Arrays.fill(used, false); // Khởi tạo tất cả false.

		// Xử lý key: Uppercase, thay J=I, loại duplicate.
		for (char c : key.replace('J', 'I').toCharArray()) { // Duyệt key, thay J bằng I.
			var idx = c - 'A'; // Index trong alphabet (A=0).
			if (Character.isLetter(c) && !used[idx]) { // Nếu chữ cái và chưa dùng.
				cleanedKey.append(c); // Thêm vào cleanedKey.
				used[idx] = true; // Đánh dấu dùng.
			}
		}

		// Điền alphabet còn lại (bỏ J).
		for (var i = 0; i < 26; i++) { // Duyệt A-Z.
			if (!used[i] && i != 9) { // Nếu chưa dùng và không phải J (idx 9).
				cleanedKey.append((char) ('A' + i)); // Thêm chữ cái.
			}
		}

		// Đổ cleanedKey vào ma trận 5x5 (row-major).
		var k = 0; // Index trong cleanedKey.
		for (var i = 0; i < 5; i++) { // Duyệt 5 hàng.
			for (var j = 0; j < 5; j++) { // Duyệt 5 cột.
				matrix[i][j] = cleanedKey.charAt(k++); // Đặt char tại [i][j].
			}
		}
	}

	/**
	 * Helper: Chuẩn hóa text thành cặp chữ: Uppercase, chỉ chữ cái, thêm X nếu cặp
	 * giống hoặc lẻ.
	 *
	 * @param text: Text gốc.
	 * @return String cặp chữ uppercase.
	 */
	private String prepareText(String text) {
		var sb = new StringBuilder(); // Builder cho text sạch.
		for (char c : text.toCharArray()) { // Duyệt text.
			if (Character.isLetter(c)) { // Nếu chữ cái.
				if (c == 'J') {
					c = 'I'; // Thay J bằng I.
				}
				sb.append(c); // Thêm uppercase (đã upper trước).
			}
		}
		var clean = sb.toString(); // Text sạch chỉ chữ cái.

		List<Character> pairs = new ArrayList<>(); // List để xây cặp.
		for (var i = 0; i < clean.length(); i++) { // Duyệt clean.
			pairs.add(clean.charAt(i)); // Thêm chữ đầu cặp.
			if (i + 1 < clean.length()) { // Nếu còn chữ tiếp.
				if (clean.charAt(i) == clean.charAt(i + 1)) { // Nếu hai chữ giống.
					pairs.add('X'); // Thêm X giữa, bỏ chữ thứ 2.
					i++; // Skip i+1.
				} else {
					pairs.add(clean.charAt(i + 1)); // Thêm chữ thứ 2 bình thường.
					i++; // Skip i+1.
				}
			}
		}
		if (pairs.size() % 2 != 0) { // Nếu số chữ lẻ.
			pairs.add('X'); // Thêm X cuối.
		}

		// Xây string từ pairs.
		sb = new StringBuilder(); // Reset builder.
		for (char ch : pairs) { // Duyệt list.
			sb.append(ch); // Thêm char.
		}
		return sb.toString(); // Trả về cặp chữ.
	}

	/**
	 * Helper: Mã hóa một cặp chữ theo quy tắc Playfair (cùng hàng → phải 1, cột →
	 * xuống 1, chéo → đổi cột).
	 *
	 * @param pair: Mảng 2 char.
	 * @return Mảng 2 char mã hóa.
	 */
	private char[] encryptPair(char[] pair) {
		var pos1 = findPosition(pair[0]); // Tìm vị trí [row, col] chữ 1.
		var pos2 = findPosition(pair[1]); // Tìm vị trí chữ 2.
		var result = new char[2]; // Mảng kết quả.

		if (pos1[0] == pos2[0]) { // Cùng hàng: Dịch phải 1 (wrap %5).
			result[0] = matrix[pos1[0]][(pos1[1] + 1) % 5];
			result[1] = matrix[pos2[0]][(pos2[1] + 1) % 5];
		} else if (pos1[1] == pos2[1]) { // Cùng cột: Dịch xuống 1 (wrap %5).
			result[0] = matrix[(pos1[0] + 1) % 5][pos1[1]];
			result[1] = matrix[(pos2[0] + 1) % 5][pos2[1]];
		} else { // Hình chữ nhật (chéo): Đổi cột.
			result[0] = matrix[pos1[0]][pos2[1]];
			result[1] = matrix[pos2[0]][pos1[1]];
		}
		return result; // Trả về cặp mã hóa.
	}

	/**
	 * Method mã hóa Playfair: Chuẩn hóa text, mã hóa từng cặp.
	 *
	 * @param preparedText: Text đã chuẩn hóa thành cặp.
	 * @return Ciphertext uppercase (không space).
	 */
	private String encryptPlayfair(String preparedText) {
		var result = new StringBuilder(); // Builder kết quả.
		for (var i = 0; i < preparedText.length(); i += 2) { // Duyệt theo cặp (bước 2).
			char[] pair = { preparedText.charAt(i), preparedText.charAt(i + 1) }; // Lấy cặp.
			var encPair = encryptPair(pair); // Mã hóa cặp.
			result.append(encPair[0]).append(encPair[1]); // Thêm vào kết quả.
		}
		return result.toString(); // Trả về (ví dụ: "BMODZBXDNAB").
	}

	/**
	 * Helper: Giải mã một cặp chữ (ngược quy tắc: trái 1, lên 1, đổi cột).
	 *
	 * @param pair: Mảng 2 char.
	 * @return Mảng 2 char giải mã.
	 */
	private char[] decryptPair(char[] pair) {
		var pos1 = findPosition(pair[0]); // Tìm vị trí chữ 1.
		var pos2 = findPosition(pair[1]); // Tìm vị trí chữ 2.
		var result = new char[2]; // Mảng kết quả.

		if (pos1[0] == pos2[0]) { // Cùng hàng: Dịch trái 1 ( +4 %5 equiv -1).
			result[0] = matrix[pos1[0]][(pos1[1] + 4) % 5];
			result[1] = matrix[pos2[0]][(pos2[1] + 4) % 5];
		} else if (pos1[1] == pos2[1]) { // Cùng cột: Dịch lên 1 (+4 %5).
			result[0] = matrix[(pos1[0] + 4) % 5][pos1[1]];
			result[1] = matrix[(pos2[0] + 4) % 5][pos2[1]];
		} else { // Chéo: Đổi cột (giống encrypt).
			result[0] = matrix[pos1[0]][pos2[1]];
			result[1] = matrix[pos2[0]][pos1[1]];
		}
		return result; // Trả về cặp giải mã.
	}

	/**
	 * Method giải mã Playfair: Giải mã từng cặp, cleanup (loại X thừa, thêm space).
	 *
	 * @param ciphertext: Ciphertext uppercase.
	 * @return Plaintext (lowercase, với space ước lượng).
	 */
	private String decryptPlayfair(String ciphertext) {
		var result = new StringBuilder(); // Builder kết quả.
		for (var i = 0; i < ciphertext.length(); i += 2) { // Duyệt theo cặp.
			char[] pair = { ciphertext.charAt(i), ciphertext.charAt(i + 1) }; // Lấy cặp.
			var decPair = decryptPair(pair); // Giải mã cặp.
			result.append(decPair[0]).append(decPair[1]); // Thêm vào.
		}
		var cleaned = cleanupText(result.toString()); // Cleanup: Loại X thừa, thêm space đơn giản (mỗi 5 char space).
		return cleaned; // Trả về (ví dụ: "hide the gold").
	}

	/**
	 * Helper: Tìm vị trí [row, col] của char trong ma trận (thay J=I nếu cần).
	 *
	 * @param c: Char.
	 * @return int[] {row, col}.
	 */
	private int[] findPosition(char c) {
		if (c == 'J') {
			c = 'I'; // Thay J bằng I.
		}
		for (var i = 0; i < 5; i++) { // Duyệt 5 hàng.
			for (var j = 0; j < 5; j++) { // Duyệt 5 cột.
				if (matrix[i][j] == c) { // Nếu tìm thấy.
					return new int[] { i, j }; // Trả về vị trí.
				}
			}
		}
		return new int[] { -1, -1 }; // Không tìm thấy (lỗi).
	}

	/**
	 * Helper: Cleanup text giải mã: Loại X thừa (cuối hoặc XX), thêm space ước
	 * lượng (mỗi 5 char).
	 *
	 * @param text: Text thô uppercase.
	 * @return Text sạch lowercase với space.
	 */
	private String cleanupText(String text) {
		var sb = new StringBuilder(text.toLowerCase()); // Lowercase.
		// Loại X cuối nếu lẻ.
		while (sb.length() > 0 && sb.charAt(sb.length() - 1) == 'x') {
			sb.deleteCharAt(sb.length() - 1); // Xóa X cuối.
		}
		// Loại XX giữa (thay bằng X hoặc loại tùy case, đơn giản loại một X).
		for (var i = 1; i < sb.length(); i++) { // Duyệt từ i=1.
			if (sb.charAt(i - 1) == 'x' && sb.charAt(i) == 'x') {
				sb.deleteCharAt(i); // Xóa X thứ 2.
				i--; // Backtrack để kiểm tra tiếp.
			}
		}
		// Thêm space ước lượng (mỗi 5 char space, vì Playfair thường 5-letter words).
		var spaced = new StringBuilder();
		for (var i = 0; i < sb.length(); i += 5) { // Bước 5.
			spaced.append(sb.substring(i, Math.min(i + 5, sb.length()))); // Thêm 5 char.
			if (i + 5 < sb.length()) { // Nếu còn.
				spaced.append(" "); // Thêm space.
			}
		}
		return spaced.toString().trim(); // Trim space thừa và trả về.
	}

	/**
	 * Method main: Điểm khởi đầu chương trình (demo chạy độc lập). Set Nimbus Look
	 * and Feel nếu có (giống NetBeans generated).
	 *
	 * @param args: Tham số dòng lệnh (không dùng).
	 */
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
//			new PlayfairCipher().setVisible(true); // Tạo và hiển thị frame.
//		});
//	}
}