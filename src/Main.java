import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== СИСТЕМА БРОНИРОВАНИЯ ОТЕЛЯ С БД ===\n");

        GuestDAO guestDAO = new GuestDAO();
        RoomDAO roomDAO = new RoomDAO();

        System.out.println("=== ДЕМОНСТРАЦИЯ CREATE (INSERT) ===\n");

        Guest guest1 = new Guest("Айдар", "Нурланов", "+7 777 123 4567", "aidar@gmail.com");
        Guest guest2 = new Guest("Асель", "Жумабекова", "+7 777 987 6543", "asel@mail.ru");

        guestDAO.addGuest(guest1);
        guestDAO.addGuest(guest2);
        System.out.println();

        Room room1 = new StandardRoom(101, 15000, 1);
        Room room2 = new StandardRoom(202, 25000, 2);
        Room room3 = new LuxuryRoom(305, 50000, true, true, true);

        roomDAO.addRoom(room1);
        roomDAO.addRoom(room2);
        roomDAO.addRoom(room3);
        System.out.println();

        System.out.println("=== ДЕМОНСТРАЦИЯ READ (SELECT) ===\n");

        System.out.println("Все гости в базе данных:");
        List<Guest> guests = guestDAO.getAllGuests();
        for (Guest g : guests) {
            g.displayInfo();
            System.out.println();
        }

        System.out.println("Все номера в базе данных:");
        List<Room> rooms = roomDAO.getAllRooms();
        for (Room r : rooms) {
            r.displayInfo();
            System.out.println();
        }

        System.out.println("=== ДЕМОНСТРАЦИЯ UPDATE ===\n");

        guestDAO.updateGuestPhone("aidar@gmail.com", "+7 701 999 8888");
        roomDAO.updateRoomPrice(101, 18000);
        System.out.println();

        System.out.println("=== ДЕМОНСТРАЦИЯ DELETE ===\n");

        guestDAO.deleteGuest("asel@mail.ru");
        roomDAO.deleteRoom(202);
        System.out.println();

        System.out.println("=== ФИНАЛЬНОЕ СОСТОЯНИЕ БД ===\n");

        System.out.println("Гости после изменений:");
        guests = guestDAO.getAllGuests();
        for (Guest g : guests) {
            System.out.println(g.getFirstName() + " " + g.getLastName() + " - " + g.getPhoneNumber());
        }

        System.out.println("\nНомера после изменений:");
        rooms = roomDAO.getAllRooms();
        for (Room r : rooms) {
            System.out.println("Номер " + r.getRoomNumber() + " - " + r.getPricePerNight() + " тенге");
        }

        System.out.println("\n=== ДЕМОНСТРАЦИЯ ПОЛИМОРФИЗМА ===\n");

        room1.displayInfo();
        System.out.println();
        room3.displayInfo();
    }
}