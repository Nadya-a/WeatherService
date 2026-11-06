import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static <JSONObject> void main(String[] args) throws IOException, InterruptedException {
        // 1. Создаём экземпляр HTTP-клиента.
        HttpClient client = HttpClient.newHttpClient();

        // 2. Строим HTTP-запрос. Укажем URL (URI) и метод.
        // В данном случае делаем GET запрос к skillfactory.ru для примера.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.yandex.ru/v2/forecast?lat=55.75&lon=37.62"))
                .GET()  // явно указываем, что хотим GET (по умолчанию для запроса без тела и так будет GET)
                .header("X-Yandex-Weather-Key", "код")
                .build();

        // 3. Отправляем запрос и получаем ответ.
        // Используем BodyHandlers.ofString(), чтобы сразу получить тело ответа как строку.
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 4. Выводим статус и часть содержимого ответа.
        System.out.println("HTTP статус: " + response.statusCode());


        String body = response.body();
        System.out.println("Тело ответа: " + body);

        JSONObject root = new JSONObject(body);
        int temp = root.getJSONObject("fact").getInt("temp");
        System.out.println(temp);

//        if (body.length() > 100) {
//            System.out.println("Первые 100 символов ответа: " + body.substring(0, 100) + "...");
//        } else {
//            System.out.println("Тело ответа: " + body);
//        }
    }
}

