import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {

    public void addGuest(Guest guest) {
        String sql = "INSERT INTO guests (first_name, last_name, phone_number, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, guest.getFirstName());
            pstmt.setString(2, guest.getLastName());
            pstmt.setString(3, guest.getPhoneNumber());
            pstmt.setString(4, guest.getEmail());

            pstmt.executeUpdate();
            System.out.println("Гость добавлен в базу данных: " + guest.getFirstName() + " " + guest.getLastName());

        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении гостя: " + e.getMessage());
        }
    }

    public List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String phone = rs.getString("phone_number");
                String email = rs.getString("email");

                Guest guest = new Guest(firstName, lastName, phone, email);
                guests.add(guest);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при чтении гостей: " + e.getMessage());
        }

        return guests;
    }

    public void updateGuestPhone(String email, String newPhone) {
        String sql = "UPDATE guests SET phone_number = ? WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPhone);
            pstmt.setString(2, email);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Телефон обновлён для гостя с email: " + email);
            } else {
                System.out.println("Гость с email " + email + " не найден");
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении телефона: " + e.getMessage());
        }
    }

    public void deleteGuest(String email) {
        String sql = "DELETE FROM guests WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Гость удалён: " + email);
            } else {
                System.out.println("Гость с email " + email + " не найден");
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при удалении гостя: " + e.getMessage());
        }
    }
}