package ru.job4j.grabber;

import java.io.IOException;
import java.util.List;

public interface Parse {

    public static final String SOURCE_LINK = "https://career.habr.com";

    public static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);


    List<Post> list(String link) throws IOException;
}
