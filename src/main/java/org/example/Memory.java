package org.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Memory {
    private final int MEMORY_SIZE;
    private final int PAGE_SIZE = 32;
    private final int AMOUNT_OF_PAGES;
    private final byte[] RAM;
    private final byte[] HARD_DISK;
    private final HashMap<Integer, Integer> MEMORY_USAGE;
    private final HashMap<Integer, Integer> MEMORY_MANAGER;

    public Memory(int memorySize) {
        MEMORY_SIZE = memorySize;
        AMOUNT_OF_PAGES = MEMORY_SIZE * 2 / PAGE_SIZE;
        RAM = new byte[MEMORY_SIZE];
        HARD_DISK = new byte[MEMORY_SIZE];
        ////////////////////////////////////////////
        MEMORY_USAGE = new LinkedHashMap<>();
        MEMORY_MANAGER = new LinkedHashMap<>();
        IntStream.range(0, AMOUNT_OF_PAGES).forEach(index -> {
            if (index == 3) {
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
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                for (Map.Entry<Integer, Integer> entry : MEMORY_USAGE.entrySet()) {
                    int key = entry.getKey();
                    int value = entry.getValue();
                    MEMORY_USAGE.put(key, value + 1);
                }
//                System.out.println(MEMORY_USAGE);
            }
        });
        incrementThread.start();
    }

    public void readAll() {
        MEMORY_MANAGER.forEach((index, place_index) -> {
            if (place_index < AMOUNT_OF_PAGES / 2) {
                System.out.printf("Страница %d расположена в RAM под индексом %d \n", index, place_index);
                System.out.println(Arrays.toString(
                        Arrays.copyOfRange(RAM, place_index * PAGE_SIZE, place_index * PAGE_SIZE + PAGE_SIZE)
                ));

            } else {
//                if (place_index == 32)
//                    System.out.println("-----------------------------------------------------------------------------------------");
                System.out.printf("Страница %d расположена на HD под индексом %d \n", index, place_index - AMOUNT_OF_PAGES / 2);
                System.out.println(Arrays.toString(
                        Arrays.copyOfRange(HARD_DISK, (place_index - AMOUNT_OF_PAGES / 2) * PAGE_SIZE, (place_index - AMOUNT_OF_PAGES / 2) * PAGE_SIZE + PAGE_SIZE)
                ));
            }

        });
        System.out.println(MEMORY_USAGE);
        System.out.println(MEMORY_MANAGER);
    }

    public void write(int index, byte value) {
        int rightIndex = index / PAGE_SIZE;
        System.out.println("Значение записано на страницу " + rightIndex);
        if (MEMORY_MANAGER.get(rightIndex) < MEMORY_SIZE / PAGE_SIZE) {
            if (index >= MEMORY_SIZE) {
                RAM[index - MEMORY_SIZE] = value;
            } else {
                RAM[index] = value;
            }

        } else {
            var ind = findNFU().getKey();
//            System.out.println("Индекс страницы после нахождения " + ind);
            changeLocationOfPage(ind, rightIndex);

            RAM[MEMORY_MANAGER.get(rightIndex) * PAGE_SIZE + index % PAGE_SIZE] = value;
        }
    }

    public void read(int indexOfPage) {
        var placeIndex = MEMORY_MANAGER.get(indexOfPage);
        if (placeIndex < AMOUNT_OF_PAGES / 2) {
            System.out.println(Arrays.toString(
                    Arrays.copyOfRange(RAM, placeIndex * PAGE_SIZE, placeIndex * PAGE_SIZE + PAGE_SIZE)
            ));

        } else {
            var ind = findNFU().getKey();
            changeLocationOfPage(ind, indexOfPage);
            System.out.println(Arrays.toString(
                    Arrays.copyOfRange(HARD_DISK, (placeIndex - AMOUNT_OF_PAGES / 2) * PAGE_SIZE, (placeIndex - AMOUNT_OF_PAGES / 2) * PAGE_SIZE + PAGE_SIZE)
            ));
        }
    }

    private Map.Entry<Integer, Integer> findNFU() {
        Map.Entry<Integer, Integer> maxEntry = null;

        for (Map.Entry<Integer, Integer> entry : MEMORY_USAGE.entrySet()) {
            if (MEMORY_MANAGER.get(entry.getKey()) < AMOUNT_OF_PAGES / 2 && (maxEntry == null || entry.getValue() > maxEntry.getValue())) {
                maxEntry = entry;
            }
        }
        assert maxEntry != null;
//        System.out.println("Индекс страницы: " + maxEntry.getKey());
        return maxEntry;
    }

    private void changeLocationOfPage(int indexOfPageOnRAM, int indexOfPageOnHardDisk) {
//        System.out.println("Индекс страницы 1 " + indexOfPageOnRAM);
//        System.out.println("Индекс страницы 2 " + indexOfPageOnHardDisk);
        // Получаем индексы страниц
        int ramPageIndex = MEMORY_MANAGER.get(indexOfPageOnRAM);//3
        int hdPageIndex = MEMORY_MANAGER.get(indexOfPageOnHardDisk);//5
//        System.out.println("Индекс по расположению " + ramPageIndex);
//        System.out.println("Индекс по расположению 2 стр " + hdPageIndex);
        // Создаем временные буферы для хранения данных
        byte[] tempRamBuffer = Arrays.copyOfRange(
                RAM,
                ramPageIndex * PAGE_SIZE,
                ramPageIndex * PAGE_SIZE + PAGE_SIZE
        );

        byte[] tempHdBuffer = Arrays.copyOfRange(
                HARD_DISK,
                (hdPageIndex - AMOUNT_OF_PAGES / 2) * PAGE_SIZE,
                (hdPageIndex - AMOUNT_OF_PAGES / 2) * PAGE_SIZE + PAGE_SIZE
        );

        // Меняем местами данные
        System.arraycopy(tempHdBuffer, 0, RAM, ramPageIndex * PAGE_SIZE, PAGE_SIZE);
        System.arraycopy(tempRamBuffer, 0, HARD_DISK, (hdPageIndex - AMOUNT_OF_PAGES / 2) * PAGE_SIZE, PAGE_SIZE);

        // Обновляем MEMORY_MANAGER
        MEMORY_MANAGER.put(indexOfPageOnRAM, hdPageIndex);
        MEMORY_MANAGER.put(indexOfPageOnHardDisk, ramPageIndex);

//        MEMORY_USAGE.put(indexOfPageOnRAM, 0);
        MEMORY_USAGE.put(indexOfPageOnHardDisk, 0);

        // Выводим результат
        System.out.println("Страница " + indexOfPageOnRAM + " перемещена на HD под индекс " + (hdPageIndex - AMOUNT_OF_PAGES / 2));
        System.out.println("Страница " + indexOfPageOnHardDisk + " перемещена в RAM под индекс " + ramPageIndex);
    }


}
