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
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private static final int PAGES = 5;

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);

    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public DateTimeParser getDateTimeParser() {
        return dateTimeParser;
    }

    public static void main(String[] args) {
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        System.out.println(habrCareerParse.list(PAGE_LINK));
    }

    private static String retrieveDescription(String link) {
            Connection connection = Jsoup.connect(link);
        Document document = null;
        try {
            document = connection.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements description  = document.select(".style-ugc");
            return description.text();

    }

    @Override
    public List<Post> list(String link)  {
        List<Post> rsl = new ArrayList<>();
        for (int i = 1; i <= PAGES; i++) {
            Connection connection = Jsoup.connect(link + i);
            Document document;
            try {
                document = connection.get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                Element dateElement = row.select(".vacancy-card__date").first();
                try {
                    rsl.add(post(titleElement, linkElement, dateElement));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return rsl;
    }

    private Post post(Element title, Element link, Element date) throws IOException {
        String vacancyLink = String.format("%s%s", SOURCE_LINK, link.attr("href"));
        return new Post(title.text(), vacancyLink, retrieveDescription(vacancyLink),
                this.dateTimeParser.parse(date.child(0).attr("datetime")));
    }


}
