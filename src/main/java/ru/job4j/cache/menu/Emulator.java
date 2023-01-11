package ru.job4j.cache.menu;

import ru.job4j.cache.AbstractCache;
import ru.job4j.cache.DirFileCache;

import java.util.Scanner;

public class Emulator {

    public void start(Scanner scanner) {
        System.out.println("Сначалоа укажите директорию");
        String dir = scanner.nextLine();
        System.out.println("Желаете загрузить файл в дириктории в кэш?(Да/Нет)");
        if (scanner.nextLine().equalsIgnoreCase("Да")) {
            System.out.println("Укажите название файла");
            AbstractCache<String, String> dirFileCache = new DirFileCache(dir + "\\" + scanner.nextLine());
            System.out.println("Файл помещен в кэщ. Желатели увидить содержание файла?(Да/Нет)");
            if (scanner.nextLine().equalsIgnoreCase("Да")) {
                System.out.println("Уажите из какого файла вы хотите получить содержимое");
                System.out.println(dirFileCache.get(scanner.nextLine()));
            }
            System.out.println("Желаете добавить в кэщ файл из другой директории?");
            if (scanner.nextLine().equalsIgnoreCase("Да")) {
                Emulator emulator = new Emulator();
                start(scanner);
            }
        }
    }

    public static void main(String[] args) {
        Emulator emulator = new Emulator();
        Scanner scanner = new Scanner(System.in);
        emulator.start(scanner);
    }


}
