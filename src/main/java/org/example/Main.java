package org.example;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Memory memory = new Memory();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Записать данные");
            System.out.println("2. Прочитать данные");
            System.out.println("3. Прочитать все данные");
            System.out.println("4. Выход");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Введите индекс для записи (0-2047): ");
                    int index = Integer.parseInt(scanner.nextLine());
                    System.out.print("Введите значение для записи (0-255): ");
                    byte value = Byte.parseByte(scanner.nextLine());
                    memory.write(index, value);
                    break;
                case "2":
                    System.out.print("Введите индекс страницы для чтения (0-63): ");
                    int pageIndex = Integer.parseInt(scanner.nextLine());
                    memory.read(pageIndex);
                    break;
                case "3":
                    memory.readAll();
                    break;
                case "4":
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }
}