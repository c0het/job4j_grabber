package ru.job4j.gc.leak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserGenerator implements Generate {

    public static final String PATH_NAMES = "src/main/java/ru/job4j/gc/leak/files/names";
    public static final String PATH_SURNAMES = "src/main/java/ru/job4j/gc/leak/files/surnames";
    public static final String PATH_PATRONS = "src/main/java/ru/job4j/gc/leak/files/patr";

    public static final String SEPARATOR = " ";
    public static final int NEW_USERS = 1000;

    public List<String> names;
    public List<String> surnames;
    public List<String> patrons;
    private final List<User> users = new ArrayList<>();
    private final Random random;

    public UserGenerator(Random random) {
        this.random = random;
        readAll();
    }

    @Override
    public void generate() {
        users.clear();

        for (int i = 0; i < NEW_USERS; i++) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(surnames.get(random.nextInt(surnames.size()))
                    + names.get(random.nextInt(names.size()))
                    + patrons.get(random.nextInt(patrons.size())));
            users.add(new User(stringBuffer.toString()));
        }
    }

    private void readAll() {
        try {
            names = read(PATH_NAMES);
            surnames = read(PATH_SURNAMES);
            patrons = read(PATH_PATRONS);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public User randomUser() {
        return users.get(random.nextInt(users.size()));
    }

    public List<User> getUsers() {
        return users;
    }
}