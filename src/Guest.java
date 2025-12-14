public class Guest {
    // Атрибуты (данные о госте)
    private String firstName;    // имя
    private String lastName;     // фамилия
    private String phoneNumber;  // телефон
    private String email;        // email

    // Конструктор - создаёт нового гостя
    public Guest(String firstName, String lastName, String phoneNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // Геттеры - получить информацию
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    // Сеттеры - изменить информацию
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Метод для вывода информации о госте
    public void displayInfo() {
        System.out.println("Гость: " + firstName + " " + lastName);
        System.out.println("Телефон: " + phoneNumber);
        System.out.println("Email: " + email);
    }
}