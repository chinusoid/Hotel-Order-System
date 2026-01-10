public class Main {
    public static void main(String[] args) {
        System.out.println("=== СИСТЕМА БРОНИРОВАНИЯ ОТЕЛЯ ===\n");

        Guest guest1 = new Guest("Айдар", "Нурланов", "+7 777 123 4567", "aidar@gmail.com");
        Guest guest2 = new Guest("Асель", "Жумабекова", "+7 777 987 6543", "asel@mail.ru");

        System.out.println("Созданы гости:");
        guest1.displayInfo();
        System.out.println();
        guest2.displayInfo();
        System.out.println("\n");

        Room room1 = new StandardRoom(101, 15000, 1);
        Room room2 = new StandardRoom(202, 25000, 2);
        Room room3 = new LuxuryRoom(305, 50000, true, true, true);

        System.out.println("=== ДЕМОНСТРАЦИЯ ПОЛИМОРФИЗМА ===");
        System.out.println("Доступные номера:\n");

        room1.displayInfo();
        System.out.println();
        room2.displayInfo();
        System.out.println();
        room3.displayInfo();
        System.out.println("\n");

        Booking booking1 = new Booking(guest1, room1, 3);
        Booking booking2 = new Booking(guest2, room3, 2);

        room1.bookRoom();
        room3.bookRoom();
        System.out.println();

        booking1.displayBookingInfo();
        System.out.println();
        booking2.displayBookingInfo();
        System.out.println();

        System.out.println("=== СРАВНЕНИЕ НОМЕРОВ ===");
        if (room1.getPricePerNight() < room2.getPricePerNight()) {
            System.out.println("Номер " + room1.getRoomNumber() + " дешевле, чем номер " + room2.getRoomNumber());
        } else {
            System.out.println("Номер " + room2.getRoomNumber() + " дешевле, чем номер " + room1.getRoomNumber());
        }

        System.out.println("\n=== СРАВНЕНИЕ БРОНИРОВАНИЙ ===");
        if (booking1.getTotalPrice() > booking2.getTotalPrice()) {
            System.out.println("Бронирование " + guest1.getFirstName() + " дороже");
        } else {
            System.out.println("Бронирование " + guest2.getFirstName() + " дороже");
        }
    }
}