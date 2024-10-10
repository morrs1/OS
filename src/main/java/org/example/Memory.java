package org.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Memory {
    private final byte[] RAM = new byte[1024];
    private final byte[] HARD_DISK = new byte[1024];
    private final int PAGE_SIZE = 32;
    private final HashMap<Integer, Integer> MEMORY_USAGE;
    private final HashMap<Integer, Integer> MEMORY_MANAGER;

    public Memory() {
        MEMORY_USAGE = new LinkedHashMap<>();
        MEMORY_MANAGER = new LinkedHashMap<>();
        IntStream.range(0, 64).forEach(index -> {
            if (index == 13) {
                MEMORY_USAGE.put(index, 130);
            } else {
                MEMORY_USAGE.put(index, 0);
            }
            MEMORY_MANAGER.put(index, index);
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
//                System.out.println(MEMORY_USAGE); // Вывод текущего значения
            }
        });
        incrementThread.start(); // Запуск потока
    }

    public void readAll() {
        MEMORY_MANAGER.forEach((index, place_index) -> {
            if (place_index < 32) {
                System.out.printf("Страница %d расположена в RAM под индексом %d \n", index, place_index);
                System.out.println(Arrays.toString(
                        Arrays.copyOfRange(RAM, place_index * PAGE_SIZE, place_index * PAGE_SIZE + PAGE_SIZE)
                ));

            } else {
                if (place_index == 32)
                    System.out.println("-----------------------------------------------------------------------------------------");
                System.out.printf("Страница %d расположена на HD под индексом %d \n", index, place_index - 32);
                System.out.println(Arrays.toString(
                        Arrays.copyOfRange(HARD_DISK, (place_index - 32) * PAGE_SIZE, (place_index - 32) * PAGE_SIZE + PAGE_SIZE)
                ));
            }

        });
        System.out.println(MEMORY_USAGE);
    }

    public void write(int index, byte value) {
        int rightIndex = index / PAGE_SIZE;
        System.out.println(rightIndex);
        if (MEMORY_MANAGER.get(rightIndex) < 32) {
            RAM[index] = value;
        } else {
            var ind = findNFU().getKey();
            changeLocationOfPage(ind, rightIndex);
            RAM[ind * 32 + index % 32] = value;
        }
    }

    public Map.Entry<Integer, Integer> findNFU() {
        Map.Entry<Integer, Integer> maxEntry = null;

        for (Map.Entry<Integer, Integer> entry : MEMORY_USAGE.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        assert maxEntry != null;
        System.out.println("Индекс страницы: " + maxEntry.getKey());
        return maxEntry;
    }

    public void changeLocationOfPage(int indexOfPageOnRAM, int indexOfPageOnHardDisk) {
        // Получаем индексы страниц
        int ramPageIndex = MEMORY_MANAGER.get(indexOfPageOnRAM);
        int hdPageIndex = MEMORY_MANAGER.get(indexOfPageOnHardDisk);

        // Создаем временные буферы для хранения данных
        byte[] tempRamBuffer = Arrays.copyOfRange(
                RAM,
                ramPageIndex * PAGE_SIZE,
                ramPageIndex * PAGE_SIZE + PAGE_SIZE
        );

        byte[] tempHdBuffer = Arrays.copyOfRange(
                HARD_DISK,
                (hdPageIndex - 32) * PAGE_SIZE,
                (hdPageIndex - 32) * PAGE_SIZE + PAGE_SIZE
        );

        // Меняем местами данные
        System.arraycopy(tempHdBuffer, 0, RAM, ramPageIndex * PAGE_SIZE, PAGE_SIZE);
        System.arraycopy(tempRamBuffer, 0, HARD_DISK, (hdPageIndex - 32) * PAGE_SIZE, PAGE_SIZE);

        // Обновляем MEMORY_MANAGER
        MEMORY_MANAGER.put(indexOfPageOnRAM, hdPageIndex);
        MEMORY_MANAGER.put(indexOfPageOnHardDisk, ramPageIndex);

        // Выводим результат
        System.out.println("Страница " + indexOfPageOnRAM + " перемещена на HD под индекс " + (hdPageIndex - 32));
        System.out.println("Страница " + indexOfPageOnHardDisk + " перемещена в RAM под индекс " + ramPageIndex);
    }


}
