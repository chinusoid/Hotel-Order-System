import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public void createBooking(Booking booking) {
        String sql = "INSERT INTO bookings (guest_id, room_id, check_in_date, check_out_date, number_of_guests, number_of_nights, total_price, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, booking.getGuestId());
            pstmt.setInt(2, booking.getRoomId());
            pstmt.setDate(3, Date.valueOf(booking.getCheckInDate()));
            pstmt.setDate(4, Date.valueOf(booking.getCheckOutDate()));
            pstmt.setInt(5, booking.getNumberOfGuests());
            pstmt.setInt(6, booking.getNumberOfNights());
            pstmt.setDouble(7, booking.getTotalPrice());
            pstmt.setString(8, booking.getStatus());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                booking.setId(rs.getInt(1));
            }

            System.out.println("Бронирование создано #" + booking.getId());

        } catch (SQLException e) {
            System.out.println("Ошибка при создании бронирования: " + e.getMessage());
        }
    }

    public void checkInGuest(int guestId, int roomNumber, LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests) {
        System.out.println("=== ЗАСЕЛЕНИЕ ГОСТЯ ===");
        System.out.println("Гость ID: " + guestId);
        System.out.println("Номер: " + roomNumber);
        System.out.println("Дата заезда: " + checkInDate);
        System.out.println("Дата выезда: " + checkOutDate);
        System.out.println("Количество гостей: " + numberOfGuests);

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // 1. Получаем информацию о комнате
                String getRoomSql = "SELECT id, price_per_night FROM rooms WHERE room_number = ?";
                PreparedStatement getRoomStmt = conn.prepareStatement(getRoomSql);
                getRoomStmt.setInt(1, roomNumber);
                ResultSet rs = getRoomStmt.executeQuery();

                if (!rs.next()) {
                    System.err.println("❌ Номер не найден!");
                    return;
                }

                int roomId = rs.getInt("id");
                double pricePerNight = rs.getDouble("price_per_night");

                long nights = checkOutDate.toEpochDay() - checkInDate.toEpochDay();
                double totalPrice = pricePerNight * nights;

                System.out.println("Room ID: " + roomId);
                System.out.println("Ночей: " + nights);
                System.out.println("Общая стоимость: " + totalPrice);

                // 2. Обновляем статус комнаты (ЗАНИМАЕМ)
                String updateRoomSql = "UPDATE rooms SET is_available = ?, current_guest_id = ?, check_in_date = ?, check_out_date = ?, number_of_guests = ? WHERE room_number = ?";
                PreparedStatement updateRoomStmt = conn.prepareStatement(updateRoomSql);
                updateRoomStmt.setBoolean(1, false);  // is_available = false (ЗАНЯТО!)
                updateRoomStmt.setInt(2, guestId);
                updateRoomStmt.setDate(3, Date.valueOf(checkInDate));
                updateRoomStmt.setDate(4, Date.valueOf(checkOutDate));
                updateRoomStmt.setInt(5, numberOfGuests);
                updateRoomStmt.setInt(6, roomNumber);

                int roomsUpdated = updateRoomStmt.executeUpdate();
                System.out.println("Комнат обновлено: " + roomsUpdated);

                // 3. Создаём запись о бронировании
                String insertBookingSql = "INSERT INTO bookings (guest_id, room_id, check_in_date, check_out_date, number_of_guests, number_of_nights, total_price, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertBookingStmt = conn.prepareStatement(insertBookingSql);
                insertBookingStmt.setInt(1, guestId);
                insertBookingStmt.setInt(2, roomId);
                insertBookingStmt.setDate(3, Date.valueOf(checkInDate));
                insertBookingStmt.setDate(4, Date.valueOf(checkOutDate));
                insertBookingStmt.setInt(5, numberOfGuests);
                insertBookingStmt.setInt(6, (int) nights);
                insertBookingStmt.setDouble(7, totalPrice);
                insertBookingStmt.setString(8, "active");

                insertBookingStmt.executeUpdate();
                System.out.println("Бронирование создано");

                conn.commit();
                System.out.println("✅ COMMIT успешен! Гость #" + guestId + " заселён в номер " + roomNumber);

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("❌ ROLLBACK! Ошибка при заселении: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.err.println("❌ Ошибка подключения: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void checkOutGuest(int roomNumber) {
        System.out.println("=== ВЫСЕЛЕНИЕ ГОСТЯ ===");
        System.out.println("Номер комнаты: " + roomNumber);

        String updateRoomSql = "UPDATE rooms SET is_available = ?, current_guest_id = ?, check_in_date = ?, check_out_date = ?, number_of_guests = ? WHERE room_number = ?";
        String updateBookingSql = "UPDATE bookings SET status = ? WHERE room_id = (SELECT id FROM rooms WHERE room_number = ?) AND status = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // 1. Обновляем комнату
                PreparedStatement updateRoomStmt = conn.prepareStatement(updateRoomSql);
                updateRoomStmt.setBoolean(1, true);        // is_available = true
                updateRoomStmt.setNull(2, java.sql.Types.INTEGER);  // current_guest_id = NULL
                updateRoomStmt.setNull(3, java.sql.Types.DATE);     // check_in_date = NULL
                updateRoomStmt.setNull(4, java.sql.Types.DATE);     // check_out_date = NULL
                updateRoomStmt.setInt(5, 0);                         // number_of_guests = 0
                updateRoomStmt.setInt(6, roomNumber);                // WHERE room_number = ?

                int roomsUpdated = updateRoomStmt.executeUpdate();
                System.out.println("Комнат обновлено: " + roomsUpdated);

                // 2. Завершаем бронирование
                PreparedStatement updateBookingStmt = conn.prepareStatement(updateBookingSql);
                updateBookingStmt.setString(1, "completed");  // status = 'completed'
                updateBookingStmt.setInt(2, roomNumber);      // room_id
                updateBookingStmt.setString(3, "active");     // WHERE status = 'active'

                int bookingsUpdated = updateBookingStmt.executeUpdate();
                System.out.println("Бронирований завершено: " + bookingsUpdated);

                // 3. Коммитим изменения
                conn.commit();
                System.out.println("✅ COMMIT успешен! Номер " + roomNumber + " освобождён");

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("❌ ROLLBACK! Ошибка при выселении: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }

        } catch (SQLException e) {
            System.err.println("❌ Ошибка подключения: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setGuestId(rs.getInt("guest_id"));
                booking.setRoomId(rs.getInt("room_id"));
                booking.setCheckInDate(rs.getDate("check_in_date").toLocalDate());
                booking.setCheckOutDate(rs.getDate("check_out_date").toLocalDate());
                booking.setNumberOfGuests(rs.getInt("number_of_guests"));
                booking.setNumberOfNights(rs.getInt("number_of_nights"));
                booking.setTotalPrice(rs.getDouble("total_price"));
                booking.setStatus(rs.getString("status"));
                bookings.add(booking);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при чтении бронирований: " + e.getMessage());
        }

        return bookings;
    }

    public List<Booking> getActiveBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE status = 'active' ORDER BY check_in_date";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setGuestId(rs.getInt("guest_id"));
                booking.setRoomId(rs.getInt("room_id"));
                booking.setCheckInDate(rs.getDate("check_in_date").toLocalDate());
                booking.setCheckOutDate(rs.getDate("check_out_date").toLocalDate());
                booking.setNumberOfGuests(rs.getInt("number_of_guests"));
                booking.setNumberOfNights(rs.getInt("number_of_nights"));
                booking.setTotalPrice(rs.getDouble("total_price"));
                booking.setStatus(rs.getString("status"));
                bookings.add(booking);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при чтении активных бронирований: " + e.getMessage());
        }

        return bookings;
    }
}