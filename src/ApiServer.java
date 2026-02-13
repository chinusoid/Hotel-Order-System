import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ApiServer {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                    new JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                    LocalDate.parse(json.getAsString()))
            .create();

    private static final GuestDAO guestDAO = new GuestDAO();
    private static final RoomDAO roomDAO = new RoomDAO();
    private static final BookingDAO bookingDAO = new BookingDAO();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        System.out.println("=== ЗАПУСК REST API СЕРВЕРА ===");
        System.out.println("Сервер запущен на http://localhost:8080");
        System.out.println("\nДоступные endpoints:");
        System.out.println("GET    /api/guests - получить всех гостей");
        System.out.println("POST   /api/guests - добавить гостя");
        System.out.println("GET    /api/rooms - получить все номера");
        System.out.println("POST   /api/rooms - добавить номер");
        System.out.println("GET    /api/bookings - получить все бронирования");
        System.out.println("POST   /api/checkin - заселить гостя");
        System.out.println("POST   /api/checkout - выселить гостя");
        System.out.println("\nДля остановки нажмите Ctrl+C\n");

        server.createContext("/api/guests", new GuestHandler());
        server.createContext("/api/rooms", new RoomHandler());
        server.createContext("/api/bookings", new BookingHandler());
        server.createContext("/api/checkin", new CheckInHandler());
        server.createContext("/api/checkout", new CheckOutHandler());

        server.setExecutor(null);
        server.start();
    }

    static class GuestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String method = exchange.getRequestMethod();

            if (method.equals("GET")) {
                handleGetGuests(exchange);
            } else if (method.equals("POST")) {
                handlePostGuest(exchange);
            } else {
                sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            }
        }

        private void handleGetGuests(HttpExchange exchange) throws IOException {
            List<Guest> guests = guestDAO.getAllGuests();
            String json = gson.toJson(guests);
            sendResponse(exchange, 200, json);
        }

        private void handlePostGuest(HttpExchange exchange) throws IOException {
            String body = readRequestBody(exchange);
            Guest guest = gson.fromJson(body, Guest.class);
            guestDAO.addOrUpdateGuest(guest);
            String json = gson.toJson(guest);
            sendResponse(exchange, 201, json);
        }
    }

    static class RoomHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String method = exchange.getRequestMethod();

            if (method.equals("GET")) {
                handleGetRooms(exchange);
            } else if (method.equals("POST")) {
                handlePostRoom(exchange);
            } else {
                sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            }
        }

        private void handleGetRooms(HttpExchange exchange) throws IOException {
            List<Room> rooms = roomDAO.getAllRooms();
            String json = gson.toJson(rooms);
            sendResponse(exchange, 200, json);
        }

        private void handlePostRoom(HttpExchange exchange) throws IOException {
            String body = readRequestBody(exchange);
            Room room = gson.fromJson(body, Room.class);
            roomDAO.addOrUpdateRoom(room);
            String json = gson.toJson(room);
            sendResponse(exchange, 201, json);
        }
    }

    static class BookingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, OPTIONS");
                exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String method = exchange.getRequestMethod();

            if (method.equals("GET")) {
                handleGetBookings(exchange);
            } else {
                sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            }
        }

        private void handleGetBookings(HttpExchange exchange) throws IOException {
            List<Booking> bookings = bookingDAO.getActiveBookings();
            String json = gson.toJson(bookings);
            sendResponse(exchange, 200, json);
        }
    }

    static class CheckInHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
                exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (!exchange.getRequestMethod().equals("POST")) {
                sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
                return;
            }

            String body = readRequestBody(exchange);
            Map<String, Object> data = gson.fromJson(body, Map.class);

            int guestId = ((Double) data.get("guestId")).intValue();
            int roomNumber = ((Double) data.get("roomNumber")).intValue();
            LocalDate checkInDate = LocalDate.parse((String) data.get("checkInDate"));
            LocalDate checkOutDate = LocalDate.parse((String) data.get("checkOutDate"));
            int numberOfGuests = ((Double) data.get("numberOfGuests")).intValue();

            bookingDAO.checkInGuest(guestId, roomNumber, checkInDate, checkOutDate, numberOfGuests);

            sendResponse(exchange, 200, "{\"success\": true, \"message\": \"Гость заселён\"}");
        }
    }

    static class CheckOutHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
                exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (!exchange.getRequestMethod().equals("POST")) {
                sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
                return;
            }

            try {
                String body = readRequestBody(exchange);
                System.out.println("Checkout request body: " + body);

                Map<String, Object> data = gson.fromJson(body, Map.class);

                int roomNumber = ((Double) data.get("roomNumber")).intValue();
                System.out.println("Attempting checkout for room: " + roomNumber);

                bookingDAO.checkOutGuest(roomNumber);

                String response = "{\"success\": true, \"message\": \"Гость выселен, номер освобождён\"}";
                System.out.println("Checkout successful for room: " + roomNumber);
                sendResponse(exchange, 200, response);

            } catch (Exception e) {
                System.err.println("Error during checkout: " + e.getMessage());
                e.printStackTrace();
                sendResponse(exchange, 500, "{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
            }
        }
    }

    private static String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        return body.toString();
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.getResponseHeaders().set("Access-Control-Max-Age", "3600");

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}