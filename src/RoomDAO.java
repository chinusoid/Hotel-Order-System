import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public void addRoom(Room room) {
        String sql = "INSERT INTO rooms (room_number, room_type, price_per_night, is_available, capacity) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, room.getRoomNumber());
            pstmt.setString(2, room.getRoomType());
            pstmt.setDouble(3, room.getPricePerNight());
            pstmt.setBoolean(4, room.isAvailable());
            pstmt.setInt(5, room.getCapacity());

            pstmt.executeUpdate();
            System.out.println("Номер добавлен в базу данных: " + room.getRoomNumber());

        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении номера: " + e.getMessage());
        }
    }

    public boolean roomExists(int roomNumber) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE room_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Ошибка проверки существования номера: " + e.getMessage());
        }

        return false;
    }

    public void addOrUpdateRoom(Room room) {
        if (roomExists(room.getRoomNumber())) {
            String sql = "UPDATE rooms SET room_type = ?, price_per_night = ?, is_available = ?, capacity = ? WHERE room_number = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, room.getRoomType());
                pstmt.setDouble(2, room.getPricePerNight());
                pstmt.setBoolean(3, room.isAvailable());
                pstmt.setInt(4, room.getCapacity());
                pstmt.setInt(5, room.getRoomNumber());

                pstmt.executeUpdate();
                System.out.println("Номер обновлён в БД: " + room.getRoomNumber());

            } catch (SQLException e) {
                System.out.println("Ошибка при обновлении номера: " + e.getMessage());
            }
        } else {
            addRoom(room);
        }
    }

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY room_number";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("id"));
                room.setRoomNumber(rs.getInt("room_number"));
                room.setRoomType(rs.getString("room_type"));
                room.setPricePerNight(rs.getDouble("price_per_night"));
                room.setAvailable(rs.getBoolean("is_available"));
                room.setCapacity(rs.getInt("capacity"));

                Object guestId = rs.getObject("current_guest_id");
                if (guestId != null) {
                    room.setCurrentGuestId((Integer) guestId);
                }

                Date checkIn = rs.getDate("check_in_date");
                if (checkIn != null) {
                    room.setCheckInDate(checkIn.toString());
                }

                Date checkOut = rs.getDate("check_out_date");
                if (checkOut != null) {
                    room.setCheckOutDate(checkOut.toString());
                }

                room.setNumberOfGuests(rs.getInt("number_of_guests"));

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

    public void deleteAllRooms() {
        String sql = "DELETE FROM rooms";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("Все номера удалены из БД");

        } catch (SQLException e) {
            System.out.println("Ошибка при удалении всех номеров: " + e.getMessage());
        }
    }
}