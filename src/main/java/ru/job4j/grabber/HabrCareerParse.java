package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);

    public static void main(String[] args) throws IOException {
        for (int i = 1; i <= 5; i++) {
            Connection connection = Jsoup.connect(PAGE_LINK + "?page=" + i);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                Element dateElement = row.select(".vacancy-card__date").first();
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                try {
                    System.out.printf("%s %s %s %s%n", vacancyName, link,
                            new HabrCareerDateTimeParser().parse(dateElement.child(0).attr("datetime")),
                            retrieveDescription(link));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        }
    }

    private static String retrieveDescription(String link) throws IOException {
            Connection connection = Jsoup.connect(link);
            Document document = connection.get();
            Elements rows = document.select(".collapsible-description__content");
            return rows.select(".style-ugc").text();

    }
}
