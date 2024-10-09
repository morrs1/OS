package org.example;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Memory {
    private final byte[] RAM = new byte[1024];
    private final byte[] HARD_DISK = new byte[1024];
    private final int PAGE_SIZE = 32;
    private int myVariable;
    private final HashMap<Integer, Integer> MEMORY_USAGE;
    private final HashMap<Integer, Integer> MEMORY_MANAGER;

    public Memory() {
        MEMORY_USAGE = new LinkedHashMap<>();
        MEMORY_MANAGER = new HashMap<>();
        IntStream.range(0, 64).forEach(index -> {
            MEMORY_MANAGER.put(index, index);
            MEMORY_USAGE.put(index, 0);
        });
        startTimer();
    }

    private void startTimer() {
        Thread incrementThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000); // Задержка 5 секунд
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Восстановление статуса прерывания
                    break; // Выход из цикла при прерывании
                }
                for (Map.Entry<Integer, Integer> entry : MEMORY_USAGE.entrySet()) {
                    int key = entry.getKey();
                    int value = entry.getValue();
                    MEMORY_USAGE.put(key, value + 1); // Увеличиваем значение на 1
                } // Увеличение переменной
                System.out.println(MEMORY_USAGE); // Вывод текущего значения
            }
        });
        incrementThread.start(); // Запуск потока
    }

    public void checkMemory() {
        MEMORY_MANAGER.forEach((index, place_index) -> {
            if (place_index < 32) {
                System.out.printf("Страница %d расположена в RAM под индексом %d \n", index, place_index);
            } else {
                System.out.printf("Страница %d расположена на HD под индексом %d \n", index, place_index - 31);
            }

        });
        System.out.println(MEMORY_USAGE);
    }

}
