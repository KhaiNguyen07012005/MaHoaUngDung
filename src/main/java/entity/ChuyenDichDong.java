package entity; // Package entity cho cipher classes (giữ nguyên cấu trúc project).

// Import AWT cho layout (BorderLayout, GridBagLayout) và events.
import java.awt.BorderLayout;
import java.awt.EventQueue;
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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Lớp ChuyenDichDong: JFrame cho thuật toán Chuyển dịch dòng (Row Transposition
 * Cipher) với layout giống demo series. Layout: Input textarea trên cùng với
 * text , key field (số cột) + "Encryption" bên phải. Dưới: Output textarea với
 * kết quả, key field (giống input) + "Decryption" bên phải. Logic: Viết text
 * theo hàng (rows = ceil(len/cols)), padding X nếu cần, đọc theo cột để mã hóa;
 * ngược lại để giải mã, loại padding.
 */
public class ChuyenDichDong extends JFrame { // Kế thừa JFrame để tạo cửa sổ chính.

	// Components cho input section.
	private JTextArea inputTextArea; // TextArea nhập plaintext (trên cùng).
	private JTextField inputKeyField; // TextField key cho encrypt (số cột).
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
	public ChuyenDichDong() {
		initComponents(); // Gọi method tạo và layout components (tương tự NetBeans generated).
		setupEventHandlers(); // Gọi method thiết lập listeners cho buttons.
		setTitle("Chuyển dịch dòng demo"); // Title frame (theo pattern demo, tiếng Việt).
		setSize(600, 500); // Kích thước frame (rộng hơn để giống demo).
		setLocationRelativeTo(null); // Đặt frame ở giữa màn hình.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Đóng frame = thoát app (demo đơn lẻ).
		setResizable(false); // Không cho resize để giữ layout.
	}

	/**
	 * Method initComponents: Tạo components và layout giống pattern demo (tương tự
	 * Caesar/Mono/Playfair/Vigenere). Sử dụng BorderLayout cho frame: NORTH cho
	 * input section, CENTER cho output section. Mỗi section dùng GridBagLayout để
	 * đặt textarea trái, key/encrypt phải.
	 */
	private void initComponents() {
		// Tạo panel cho input section (NORTH của frame).
		var inputPanel = new JPanel(new GridBagLayout()); // GridBagLayout để linh hoạt vị trí.
		var gbc = new GridBagConstraints(); // Constraints cho GridBag.
		gbc.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa components 5px.

		// Tạo input textarea (trái input panel).
		inputTextArea = new JTextArea(3, 25); // 3 dòng, 25 cột (giống demo).
		inputTextArea.setText("we are discovered flee at once"); // Text demo chuẩn cho Row Transposition.
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
		var inputKeyLabel = new JLabel("Key"); // Label "Key" (số cột).
		gbc.gridx = 1; // Cột 1 (phải).
		gbc.gridy = 0; // Hàng 0.
		gbc.gridwidth = 1; // 1 cột.
		gbc.weightx = 0.0; // Không mở rộng.
		gbc.fill = GridBagConstraints.NONE; // Không fill.
		inputPanel.add(inputKeyLabel, gbc); // Thêm label.

		// Tạo input key field (dưới label key input).
		inputKeyField = new JTextField("5"); // Key demo "5" cột.
		inputKeyField.setColumns(3); // Chiều rộng nhỏ cho số.
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
		outputKeyField = new JTextField("5"); // Copy key demo.
		outputKeyField.setColumns(3);
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
	 * từ inputTextArea + inputKeyField → chuẩn hóa uppercase chỉ chữ → viết hàng
	 * đọc cột → hiển thị outputTextArea + copy key. Decrypt: Lấy input từ
	 * outputTextArea + outputKeyField → viết cột đọc hàng → loại X padding → hiển
	 * thị inputTextArea.
	 */
	private void setupEventHandlers() {
		// Listener cho encrypt button: Mã hóa khi nhấn.
		encryptButton.addActionListener(e -> {
			var input = inputTextArea.getText().trim(); // Lấy văn bản từ input textarea và loại khoảng trắng thừa.
			if (input.isEmpty()) { // Kiểm tra input rỗng.
				JOptionPane.showMessageDialog(ChuyenDichDong.this, "Lỗi: Vui lòng nhập văn bản!"); // Hiển thị
																									// dialog lỗi.
				return; // Thoát nếu rỗng.
			}
			var keyStr = inputKeyField.getText().trim(); // Lấy key từ input key field.
			if (keyStr.isEmpty()) { // Kiểm tra key rỗng.
				JOptionPane.showMessageDialog(ChuyenDichDong.this, "Lỗi: Vui lòng nhập key (số cột >0)!"); // Dialog
																											// lỗi.
				return;
			}
			int cols; // Biến lưu số cột.
			try {
				cols = Integer.parseInt(keyStr); // Parse key thành int.
				if (cols <= 0) {
					throw new NumberFormatException(); // Kiểm tra >0.
				}
			} catch (NumberFormatException ex) { // Bắt lỗi parse hoặc <=0.
				JOptionPane.showMessageDialog(ChuyenDichDong.this, "Lỗi: Key phải là số nguyên >0!"); // Dialog lỗi.
				return;
			}
			var encrypted = encryptRowTransposition(input.toUpperCase().replaceAll("[^A-Z]", ""), cols); // Mã hóa:
																											// uppercase,
																											// chỉ
																											// chữ.
			outputTextArea.setText(encrypted); // Hiển thị kết quả vào output (uppercase, không space).
			outputKeyField.setText(keyStr); // Copy key từ input sang output cho decrypt.
		});

		// Listener cho decrypt button: Giải mã khi nhấn.
		decryptButton.addActionListener(e -> {
			var input = outputTextArea.getText().trim(); // Lấy ciphertext từ output textarea.
			if (input.isEmpty()) { // Kiểm tra rỗng.
				JOptionPane.showMessageDialog(ChuyenDichDong.this, "Lỗi: Vui lòng mã hóa trước!"); // Dialog lỗi.
				return;
			}
			var keyStr = outputKeyField.getText().trim(); // Lấy key từ output key field.
			if (keyStr.isEmpty()) {
				JOptionPane.showMessageDialog(ChuyenDichDong.this, "Lỗi: Vui lòng nhập key (số cột >0)!"); // Lỗi.
				return;
			}
			int cols;
			try {
				cols = Integer.parseInt(keyStr);
				if (cols <= 0) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(ChuyenDichDong.this, "Lỗi: Key phải là số nguyên >0!"); // Lỗi.
				return;
			}
			var decrypted = decryptRowTransposition(input, cols); // Giải mã và loại padding.
			inputTextArea.setText(decrypted.toLowerCase()); // Hiển thị vào input (lowercase, thêm space ước lượng).
		});
	}

	/**
	 * Method mã hóa Row Transposition: Viết theo hàng (rows = ceil(len/cols)),
	 * padding X, đọc theo cột.
	 *
	 * @param text: Text uppercase chỉ chữ cái.
	 * @param cols: Số cột (key).
	 * @return Ciphertext uppercase.
	 */
	private String encryptRowTransposition(String text, int cols) {
		var len = text.length(); // Độ dài text.
		var rows = (int) Math.ceil((double) len / cols); // Số hàng = ceil(len / cols).
		var grid = new char[rows][cols]; // Tạo grid rows x cols.

		// Đổ text vào grid theo hàng (row-major).
		var index = 0; // Index trong text.
		for (var i = 0; i < rows; i++) { // Duyệt hàng.
			for (var j = 0; j < cols; j++) { // Duyệt cột.
				if (index < len) { // Nếu còn text.
					grid[i][j] = text.charAt(index++); // Đặt char.
				} else {
					grid[i][j] = 'X'; // Padding X.
				}
			}
		}

		// Đọc grid theo cột (column-major) để mã hóa.
		var result = new StringBuilder(); // Builder kết quả.
		for (var j = 0; j < cols; j++) { // Duyệt cột.
			for (var i = 0; i < rows; i++) { // Duyệt hàng trong cột.
				result.append(grid[i][j]); // Thêm char.
			}
		}
		return result.toString(); // Trả về
	}

	/**
	 * Method giải mã Row Transposition: Viết theo cột, đọc theo hàng, loại X
	 * padding cuối.
	 *
	 * @param ciphertext: Ciphertext uppercase.
	 * @param cols:       Số cột.
	 * @return Plaintext (lowercase, với space ước lượng).
	 */
	private String decryptRowTransposition(String ciphertext, int cols) {
		var len = ciphertext.length(); // Độ dài ciphertext.
		var rows = (int) Math.ceil((double) len / cols); // Số hàng.
		var grid = new char[rows][cols]; // Grid rows x cols.

		// Đổ ciphertext vào grid theo cột (column-major).
		var index = 0; // Index trong ciphertext.
		for (var j = 0; j < cols; j++) { // Duyệt cột.
			for (var i = 0; i < rows; i++) { // Duyệt hàng trong cột.
				if (index < len) { // Nếu còn char.
					grid[i][j] = ciphertext.charAt(index++); // Đặt char.
				} else {
					grid[i][j] = 'X'; // Padding nếu cần (hiếm).
				}
			}
		}

		// Đọc grid theo hàng để giải mã.
		var result = new StringBuilder(); // Builder kết quả.
		for (var i = 0; i < rows; i++) { // Duyệt hàng.
			for (var j = 0; j < cols; j++) { // Duyệt cột trong hàng.
				result.append(grid[i][j]); // Thêm char.
			}
		}

		// Loại padding X cuối (tìm vị trí đầu X và cắt).
		var raw = result.toString(); // Raw string.
		var lastNonX = raw.lastIndexOf('X'); // Tìm index cuối không phải X.
		if (lastNonX != -1 && lastNonX < raw.length() - 1) { // Nếu có X cuối.
			raw = raw.substring(0, lastNonX + 1); // Cắt từ đầu đến sau X cuối non-X.
		}

		// Thêm space ước lượng (mỗi 2-3 char space, vì text có space gốc).
		var spaced = new StringBuilder(raw.toLowerCase()); // Lowercase.
		for (var i = spaced.length() - 1; i > 0; i -= 3) { // Thêm space mỗi 3 char từ cuối (ước lượng).
			if (spaced.charAt(i) != ' ') { // Nếu không phải space.
				spaced.insert(i, ' '); // Chèn space trước i.
			}
		}
		return spaced.toString().trim(); // Trim và trả về (ví dụ: "we are discovered flee at once").
	}

	/**
	 * Method main: Điểm khởi đầu chương trình (demo chạy độc lập). Set Nimbus Look
	 * and Feel nếu có (giống NetBeans generated).
	 *
	 * @param args: Tham số dòng lệnh (không dùng).
	 */
	public static void main(String args[]) {
		// Set Nimbus L&F (nếu available, giống demo screenshot).
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) { // Tìm Nimbus.
					UIManager.setLookAndFeel(info.getClassName()); // Áp dụng nếu tìm thấy.
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ex) {
			// Bỏ qua lỗi L&F, dùng default.
		}

		// Chạy trên Event Dispatch Thread (EDT) để Swing an toàn.
		EventQueue.invokeLater(() -> {
			new ChuyenDichDong().setVisible(true); // Tạo và hiển thị frame.
		});
	}
}