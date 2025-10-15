package entity; // Package entity cho cipher classes (giữ nguyên cấu trúc project).

// Import AWT cho layout (BorderLayout, GridLayout) và events.
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
 * Lớp CaesarCipher: JFrame cho thuật toán Caesar Cipher với layout giống demo.
 * Layout: Input textarea trên cùng, key + button encrypt bên phải input. Dưới:
 * Output textarea, key + button decrypt bên phải output. Logic: Dịch shift %26,
 * chỉ chữ cái, giữ case và ký tự khác (space, etc.). Demo: Input "hello moto"
 * key 5 → "mjqt ryt"; Decrypt key 0 (hoặc shift gốc) → gốc.
 */
public class CaesarCipher extends JFrame { // Kế thừa JFrame để tạo cửa sổ chính.

	// Components cho input section.
	private JTextArea inputTextArea; // TextArea nhập plaintext (trên cùng).
	private JTextField inputKeyField; // TextField key cho encrypt (bên phải input).
	private JButton encryptButton; // Button "Encryption" bên dưới key input.

	// Components cho output section.
	private JTextArea outputTextArea; // TextArea hiển thị ciphertext (dưới input).
	private JTextField outputKeyField; // TextField key cho decrypt (bên phải output, mặc định 0 cho demo).
	private JButton decryptButton; // Button "Decryption" bên dưới key output.

	// Scroll panes cho textarea.
	private JScrollPane inputScrollPane; // Scroll cho input textarea.
	private JScrollPane outputScrollPane; // Scroll cho output textarea.

	/**
	 * Constructor: Khởi tạo UI, layout, và listeners.
	 */
	public CaesarCipher() {
		initComponents(); // Gọi method tạo và layout components (tương tự NetBeans generated).
		setupEventHandlers(); // Gọi method thiết lập listeners cho buttons.
		setTitle("Caesar cipher demo"); // Title frame giống demo.
		setSize(600, 500); // Kích thước frame (rộng hơn để giống demo).
		setLocationRelativeTo(null); // Đặt frame ở giữa màn hình.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Đóng frame = thoát app (demo đơn lẻ).
		setResizable(false); // Không cho resize để giữ layout.
	}

	/**
	 * Method initComponents: Tạo components và layout giống screenshot demo. Sử
	 * dụng BorderLayout cho frame: NORTH cho input section, CENTER cho output
	 * section. Mỗi section dùng GridBagLayout để đặt textarea trái, key/button
	 * phải.
	 */
	private void initComponents() {
		// Tạo panel cho input section (NORTH của frame).
		var inputPanel = new JPanel(new GridBagLayout()); // GridBagLayout để linh hoạt vị trí.
		var gbc = new GridBagConstraints(); // Constraints cho GridBag.
		gbc.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa components 5px.

		// Tạo input textarea (trái input panel).
		inputTextArea = new JTextArea(3, 25); // 3 dòng, 25 cột (giống demo).
		inputTextArea.setText("hello moto"); // Text demo mặc định.
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
		inputKeyField = new JTextField("5"); // Key demo "5".
		inputKeyField.setColumns(3); // Chiều rộng 3 ký tự.
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

		// Tạo output key field (dưới label key output, demo "0").
		outputKeyField = new JTextField("0"); // Key demo "0" cho decrypt.
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
	 * từ inputTextArea + inputKeyField → mã hóa → hiển thị outputTextArea. Decrypt:
	 * Lấy input từ outputTextArea + outputKeyField → giải mã → hiển thị
	 * inputTextArea (hoặc output).
	 */
	private void setupEventHandlers() {
		// Listener cho encrypt button: Mã hóa khi nhấn.
		encryptButton.addActionListener(e -> {
			var input = inputTextArea.getText().trim(); // Lấy văn bản từ input textarea và loại khoảng trắng thừa.
			if (input.isEmpty()) { // Kiểm tra input rỗng.
				JOptionPane.showMessageDialog(CaesarCipher.this, "Lỗi: Vui lòng nhập văn bản!"); // Hiển thị dialog
																									// lỗi.
				return; // Thoát nếu rỗng.
			}
			var keyStr = inputKeyField.getText().trim(); // Lấy key từ input key field.
			if (keyStr.isEmpty()) { // Kiểm tra key rỗng.
				JOptionPane.showMessageDialog(CaesarCipher.this, "Lỗi: Vui lòng nhập key (số)!"); // Dialog lỗi.
				return;
			}
			int shift; // Biến lưu shift.
			try {
				shift = Integer.parseInt(keyStr); // Parse key thành int.
				shift = Math.floorMod(shift, 26); // Chuẩn hóa shift: 0-25 (xử lý âm tự động).
			} catch (NumberFormatException ex) { // Bắt lỗi parse không phải số.
				JOptionPane.showMessageDialog(CaesarCipher.this, "Lỗi: Key phải là số nguyên!"); // Dialog lỗi.
				return;
			}
			var encrypted = encryptCaesar(input, shift); // Gọi method mã hóa.
			outputTextArea.setText(encrypted); // Hiển thị kết quả vào output textarea (không prefix để giống demo).
			outputKeyField.setText(String.valueOf(shift)); // Đặt key output = shift (cho decrypt dùng chung).
		});

		// Listener cho decrypt button: Giải mã khi nhấn.
		decryptButton.addActionListener(e -> {
			var input = outputTextArea.getText().trim(); // Lấy ciphertext từ output textarea.
			if (input.isEmpty()) { // Kiểm tra rỗng.
				JOptionPane.showMessageDialog(CaesarCipher.this, "Lỗi: Vui lòng mã hóa trước!"); // Dialog lỗi.
				return;
			}
			var keyStr = outputKeyField.getText().trim(); // Lấy key từ output key field.
			if (keyStr.isEmpty()) {
				JOptionPane.showMessageDialog(CaesarCipher.this, "Lỗi: Vui lòng nhập key (số)!"); // Dialog lỗi.
				return;
			}
			int shift;
			try {
				shift = Integer.parseInt(keyStr); // Parse key.
				shift = Math.floorMod(shift, 26); // Chuẩn hóa.
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(CaesarCipher.this, "Lỗi: Key phải là số nguyên!"); // Lỗi.
				return;
			}
			var decrypted = decryptCaesar(input, shift); // Gọi method giải mã.
			inputTextArea.setText(decrypted); // Hiển thị kết quả vào input textarea (quay về gốc).
		});
	}

	/**
	 * Method mã hóa Caesar: Dịch mỗi chữ cái shift vị trí về phải, wrap around
	 * alphabet. Giữ nguyên ký tự không phải chữ cái (space → space).
	 *
	 * @param plaintext: Văn bản gốc.
	 * @param shift:     Số vị trí dịch (0-25).
	 * @return Văn bản đã mã hóa.
	 */
	private String encryptCaesar(String plaintext, int shift) {
		var result = new StringBuilder(); // StringBuilder để xây dựng chuỗi hiệu quả (không dùng + nhiều lần).
		for (char c : plaintext.toCharArray()) { // Duyệt từng ký tự trong plaintext.
			if (Character.isLetter(c)) { // Kiểm tra nếu là chữ cái (A-Z hoặc a-z).
				var base = Character.isUpperCase(c) ? 'A' : 'a'; // Base: 'A' cho hoa, 'a' cho thường để giữ case.
				// Công thức: (c - base + shift) % 26 + base → dịch và wrap (ví dụ: z +1 = a).
				result.append((char) ((c - base + shift) % 26 + base));
			} else {
				result.append(c); // Giữ nguyên ký tự khác (space, số, dấu).
			}
		}
		return result.toString(); // Chuyển StringBuilder thành String và trả về.
	}

	/**
	 * Method giải mã Caesar: Dịch ngược (shift âm tương đương 26 - shift).
	 *
	 * @param ciphertext: Văn bản mã hóa.
	 * @param shift:      Shift gốc.
	 * @return Văn bản gốc.
	 */
	private String decryptCaesar(String ciphertext, int shift) {
		// Giải mã = mã hóa với shift ngược: 26 - shift (vì modulo 26).
		return encryptCaesar(ciphertext, 26 - shift);
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
//			new CaesarCipher().setVisible(true); // Tạo và hiển thị frame.
//		});
//	}
}