import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public void addRoom(Room room) {
        String sql = "INSERT INTO rooms (room_number, room_type, price_per_night, is_available) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, room.getRoomNumber());
            pstmt.setString(2, room.getRoomType());
            pstmt.setDouble(3, room.getPricePerNight());
            pstmt.setBoolean(4, room.isAvailable());

            pstmt.executeUpdate();
            System.out.println("Номер добавлен в базу данных: " + room.getRoomNumber());

        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении номера: " + e.getMessage());
        }
    }

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int roomNumber = rs.getInt("room_number");
                String roomType = rs.getString("room_type");
                double price = rs.getDouble("price_per_night");

                Room room = new Room(roomNumber, roomType, price);
                rooms.add(room);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при чтении номеров: " + e.getMessage());
        }

        return rooms;
    }

    public void updateRoomPrice(int roomNumber, double newPrice) {
        String sql = "UPDATE rooms SET price_per_night = ? WHERE room_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, roomNumber);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Цена обновлена для номера: " + roomNumber);
            } else {
                System.out.println("Номер " + roomNumber + " не найден");
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении цены: " + e.getMessage());
        }
    }

    public void deleteRoom(int roomNumber) {
        String sql = "DELETE FROM rooms WHERE room_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomNumber);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Номер удалён: " + roomNumber);
            } else {
                System.out.println("Номер " + roomNumber + " не найден");
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при удалении номера: " + e.getMessage());
        }
    }
}