package ru.job4j.cache.menu;

import ru.job4j.cache.DirFileCache;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Emulator {

    public static final int DIRECTORY = 1;
    public static final int TAKE_CACHE = 2;

    public static final String MENU = """
           
            1. Указать кэшируемую директорию
            2. Получить содержимое файла из кэша
            3. Выйти
            """;

    public void start(Scanner scanner, DirFileCache dirFileCache) {
        boolean run = true;
        while (run) {
            System.out.println(MENU);
            System.out.println("Выберете пункт меню");
            int choice = Integer.parseInt(scanner.nextLine());
            if (DIRECTORY == choice) {
                System.out.println("Укажите путь к файлу");
                String dir = scanner.nextLine();
                dirFileCache.setCachingDir(dir);
                dirFileCache.put(dir, dirFileCache.load(dir));
            } else if (TAKE_CACHE == choice) {
                System.out.println("Содержание какого файла вы хотите увидеть");
                String nameFile = scanner.nextLine();
                System.out.println(dirFileCache.get(nameFile));
            } else {
                run = false;
            }
        }
    }

    public static void main(String[] args) {
        Emulator emulator = new Emulator();
        Scanner scanner = new Scanner(System.in);
        DirFileCache dirFileCache = new DirFileCache(null);
        emulator.start(scanner, dirFileCache);
    }


}
