package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HabrCareerParse implements Parse {

    private static final int PAGES = 5;

    public static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);

    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static void main(String[] args) {
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        List<Post> listPost = habrCareerParse.list(PAGE_LINK);
        Properties cfg = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("app.properties")) {
            cfg.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (PsqlStore psqlStore = new PsqlStore(cfg)) {
            for (Post post : listPost) {
                psqlStore.save(post);
            }
            System.out.println(psqlStore.getAll() + "/n");
            System.out.println(psqlStore.findById(10));
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static String retrieveDescription(String link) {
            Connection connection = Jsoup.connect(link);
        Document document;
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
                try {
                    rsl.add(getPost(row));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return rsl;
    }

    private Post getPost(Element element) throws IOException {
        Element vacancyName = element.select(".vacancy-card__title").first();
        String vacancyLink = String.format("%s%s", SOURCE_LINK, vacancyName.child(0).attr("href"));
        return new Post(vacancyName.text(), vacancyLink, retrieveDescription(vacancyLink),
                this.dateTimeParser.parse(element.select(".vacancy-card__date").
                        first().child(0).attr("datetime")));
    }


}
