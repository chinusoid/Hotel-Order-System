public class Guest {
    private int id;  // ← это поле должно быть!
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Integer currentRoomId;

    public Guest() {}

    public Guest(String firstName, String lastName, String phoneNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // ← эти методы должны быть!
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getCurrentRoomId() { return currentRoomId; }
    public void setCurrentRoomId(Integer currentRoomId) { this.currentRoomId = currentRoomId; }

    public void displayInfo() {
        System.out.println("Гость: " + firstName + " " + lastName);
        System.out.println("Телефон: " + phoneNumber);
        System.out.println("Email: " + email);
        if (currentRoomId != null) {
            System.out.println("Комната: " + currentRoomId);
        }
    }
}