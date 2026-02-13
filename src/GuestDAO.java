import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {

    public void addGuest(Guest guest) {
        String sql = "INSERT INTO guests (first_name, last_name, phone_number, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, guest.getFirstName());
            pstmt.setString(2, guest.getLastName());
            pstmt.setString(3, guest.getPhoneNumber());
            pstmt.setString(4, guest.getEmail());

            pstmt.executeUpdate();

            // Получаем сгенерированный ID
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                guest.setId(rs.getInt(1));
            }

            System.out.println("Гость добавлен в базу данных: " + guest.getFirstName() + " " + guest.getLastName());

        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении гостя: " + e.getMessage());
        }
    }

    public boolean guestExists(String email) {
        String sql = "SELECT COUNT(*) FROM guests WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Ошибка проверки существования гостя: " + e.getMessage());
        }

        return false;
    }

    public void addOrUpdateGuest(Guest guest) {
        if (guestExists(guest.getEmail())) {
            // Обновляем существующего
            String sql = "UPDATE guests SET first_name = ?, last_name = ?, phone_number = ? WHERE email = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, guest.getFirstName());
                pstmt.setString(2, guest.getLastName());
                pstmt.setString(3, guest.getPhoneNumber());
                pstmt.setString(4, guest.getEmail());

                pstmt.executeUpdate();

                // Получаем ID обновлённого гостя
                String getIdSql = "SELECT id FROM guests WHERE email = ?";
                PreparedStatement getIdStmt = conn.prepareStatement(getIdSql);
                getIdStmt.setString(1, guest.getEmail());
                ResultSet rs = getIdStmt.executeQuery();
                if (rs.next()) {
                    guest.setId(rs.getInt(1));
                }

                System.out.println("Гость обновлён в БД: " + guest.getFirstName() + " " + guest.getLastName());

            } catch (SQLException e) {
                System.out.println("Ошибка при обновлении гостя: " + e.getMessage());
            }
        } else {
            // Добавляем нового
            addGuest(guest);
        }
    }
    public List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Guest guest = new Guest();
                guest.setId(rs.getInt("id"));
                guest.setFirstName(rs.getString("first_name"));
                guest.setLastName(rs.getString("last_name"));
                guest.setPhoneNumber(rs.getString("phone_number"));
                guest.setEmail(rs.getString("email"));
                guests.add(guest);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при чтении гостей: " + e.getMessage());
        }

        return guests;
    }

    public Guest getGuestById(int id) {
        String sql = "SELECT * FROM guests WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Guest guest = new Guest();
                guest.setId(rs.getInt("id"));
                guest.setFirstName(rs.getString("first_name"));
                guest.setLastName(rs.getString("last_name"));
                guest.setPhoneNumber(rs.getString("phone_number"));
                guest.setEmail(rs.getString("email"));
                return guest;
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при поиске гостя: " + e.getMessage());
        }

        return null;
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

    public void deleteAllGuests() {
        String sql = "DELETE FROM guests";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("Все гости удалены из БД");

        } catch (SQLException e) {
            System.out.println("Ошибка при удалении всех гостей: " + e.getMessage());
        }
    }
}