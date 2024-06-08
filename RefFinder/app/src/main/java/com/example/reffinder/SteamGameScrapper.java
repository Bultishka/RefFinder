package com.example.reffinder;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SteamGameScrapper {
    private static final String TAG = "myLog";
    private String url;
    private String title;
    private String releaseDate;
    private int genre;

    private String logoUrl;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());
    private GameInfoCallback callback;


    private Context context;

    public SteamGameScrapper(String url, GameInfoCallback callback, int genre) {
        this.url = url;
        this.callback = callback;
        this.genre = genre;
   }

    public void extractGameInfo() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "Попытка подключения к URL: " + url);
                    Document doc = Jsoup.connect(url).get();
                    Log.d(TAG, "Успешное подключение к URL: " + url);

                    Element titleElement = doc.select("div.apphub_AppName#appHubAppName").first();
                    if (titleElement != null) {
                        title = titleElement.text();
                        Log.d(TAG, "Название игры: " + title);
                    } else {
                        Log.e(TAG, "Не удалось найти элемент с названием игры");
                        callback.onGameNotFound("Игра недоступна в вашем регионе");
                    }

                    Element releaseDateElement = doc.selectFirst(".release_date .date");
                    if (releaseDateElement != null) {
                        releaseDate = releaseDateElement.text();
                        Log.d(TAG, "Год выпуска: " + releaseDate);

                        // Get only the last four characters of the releaseDate string
                        if (releaseDate.length() >= 4) {
                            releaseDate = releaseDate.substring(releaseDate.length() - 4);
                        } else {
                            Log.e(TAG, "Дата выпуска содержит менее чем 4 символа");
                        }
                    } else {
                        Log.e(TAG, "Не удалось найти элемент с датой выпуска");
                    }

                    // Извлекаем логотип игры
                    Element logoElement = doc.selectFirst(".game_header_image_full");
                    if (logoElement != null) {
                        logoUrl = logoElement.attr("src");
                        Log.d(TAG, "Логотип игры: " + logoUrl);
                    } else {
                        Log.e(TAG, "Не удалось найти элемент с логотипом игры");
                    }

                } catch (IOException e) {
                    Log.e(TAG, "Ошибка при подключении к URL: " + url, e);
                    callback.onGameNotFound("Не получилось достать игру по ссылке");
                }
                if(title != null)
                {
                    callback.onGameFound(title, releaseDate, logoUrl, genre, url);
                }
            }
        });
    }
}
