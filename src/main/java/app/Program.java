package app; // Package cho main app.

// Import tất cả các cipher classes từ entity để mở frame.
import entity.CaesarCipher;
import entity.ChuyenDichDong;
import entity.MaHoaBangChuDon;
import entity.PlayfairCipher;
import entity.VigenereCipher;

/**
 * Lớp Program: Entry point của ứng dụng, tạo menu chọn thuật toán mã hóa. Sử
 * dụng JOptionPane cho menu đơn giản (không cần GUI phức tạp).
 */
public class Program {
	/**
	 * Phương thức main: Hiển thị menu và mở frame tương ứng.
	 *
	 * @param args: Tham số dòng lệnh (không dùng).
	 */
	public static void main(String[] args) {
		// Chạy trên Event Dispatch Thread (EDT) để an toàn Swing.
		javax.swing.SwingUtilities.invokeLater(() -> {
			// Tạo menu chọn cipher bằng JOptionPane.
			String[] options = { "Caesar Cipher", "Bảng chữ đơn", "Playfair Cipher", "Vigenere Cipher",
					"Chuyển dịch dòng", "Thoát" };
			var choice = javax.swing.JOptionPane.showOptionDialog(null, // Parent component (null = dialog ở giữa).
					"Chọn thuật toán mã hóa:", // Message.
					"Mã Hóa Ứng Dụng", // Title.
					javax.swing.JOptionPane.DEFAULT_OPTION, // Option type.
					javax.swing.JOptionPane.QUESTION_MESSAGE, // Message type.
					null, // Icon.
					options, // Options array.
					options[0] // Default selection.
			);

			// Xử lý lựa chọn.
			switch (choice) {
			case 0: // Caesar.
				new CaesarCipher().setVisible(true);
				break;
			case 1: // Bảng chữ đơn.
				new MaHoaBangChuDon().setVisible(true);
				break;
			case 2: // Playfair.
				new PlayfairCipher().setVisible(true);
				break;
			case 3: // Vigenere.
				new VigenereCipher().setVisible(true);
				break;
			case 4: // Chuyển dịch dòng.
				new ChuyenDichDong().setVisible(true);
				break;
			case 5: // Thoát.
			default:
				System.exit(0);
				break;
			}
		});
	}
}