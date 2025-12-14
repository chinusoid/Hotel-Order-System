public class Main {
    public static void main(String[] args) {
        System.out.println("=== СИСТЕМА БРОНИРОВАНИЯ ОТЕЛЯ ===\n");

        // Создаём гостей
        Guest guest1 = new Guest("Айдар", "Нурланов", "+7 777 123 4567", "aidar@gmail.com");
        Guest guest2 = new Guest("Асель", "Жумабекова", "+7 777 987 6543", "asel@mail.ru");

        System.out.println("Созданы гости:");
        guest1.displayInfo();
        System.out.println();
        guest2.displayInfo();
        System.out.println("\n");

        // Создаём номера
        Room room1 = new Room(101, "Одноместный", 15000);
        Room room2 = new Room(202, "Двухместный", 25000);
        Room room3 = new Room(305, "Люкс", 50000);

        System.out.println("Доступные номера:");
        room1.displayInfo();
        System.out.println();
        room2.displayInfo();
        System.out.println();
        room3.displayInfo();
        System.out.println("\n");

        // Создаём бронирования
        Booking booking1 = new Booking(guest1, room1, 3);
        Booking booking2 = new Booking(guest2, room3, 2);

        // Бронируем номера
        room1.bookRoom();
        room3.bookRoom();
        System.out.println();

        // Выводим информацию о бронированиях
        booking1.displayBookingInfo();
        System.out.println();
        booking2.displayBookingInfo();
        System.out.println();

        // Сравниваем объекты
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