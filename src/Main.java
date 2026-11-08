import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // 1. Создаём экземпляр HTTP-клиента.
        HttpClient client = HttpClient.newHttpClient();

        int limit = 7;

        // 2. Строим HTTP-запрос. Укажем координаты и за сколько дней отдать продноз.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.yandex.ru/v2/forecast?lat=55.75&lon=37.62&limit=" + limit))
                .GET()  // явно указываем, что хотим GET (по умолчанию для запроса без тела и так будет GET)
                .header("X-Yandex-Weather-Key", "код")
                .build();

        // 3. Отправляем запрос и получаем ответ.
        // Используем BodyHandlers.ofString(), чтобы сразу получить тело ответа как строку.
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 4. Выводим статус и содержимое ответа.
        System.out.println("HTTP статус: " + response.statusCode());
        String body = response.body();
        System.out.println("Тело ответа: " + body);

        // 5. Выводим температуру за сегодня
        JSONObject root = new JSONObject(body);
        int temp = root.getJSONObject("fact").getInt("temp");
        System.out.println("Температура сегодня: " + temp);

        // 6. Считаем и выводим среднюю температуру за limit дней
        JSONArray forecasts = root.getJSONArray("forecasts");
        double temp_sum = 0;

        for (int i = 0; i < limit; i++){
            //forecasts[*].parts.day.temp_avg путь к значению
            JSONObject forecast = forecasts.getJSONObject(i);
            JSONObject day = forecast.getJSONObject("parts").getJSONObject("day");
            int dayly_temp = day.getInt("temp_avg");
            temp_sum += dayly_temp;
        }
        System.out.println("Средняя температура за " + limit + " дней: " + temp_sum / limit);
    }
}

