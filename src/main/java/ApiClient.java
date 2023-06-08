import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;


public class ApiClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/api/time")).GET().build();


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String apiResponse = response.body();
        System.out.println(apiResponse);

        HttpRequest requestip = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/api/json")).GET().build();
        HttpResponse<String> ip = httpClient.send(requestip, HttpResponse.BodyHandlers.ofString());
        String apiip = ip.body();
        System.out.println(apiip);

        try {
            URL url = new URL("http://localhost:8080/api/image");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String ipAddress = getIPAddress();
            connection.getOutputStream().write(ipAddress.getBytes());
            connection.getOutputStream().flush();
            connection.getOutputStream().close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                Path outputPath = Path.of("margot robbie.jpg"); // Specify the output file path
                Files.copy(inputStream, outputPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image downloaded successfully");
            } else {
                System.out.println("Error - HTTP response code: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        private static String getIPAddress () {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return "";
            }
        }

    }
