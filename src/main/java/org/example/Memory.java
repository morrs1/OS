package org.example;

public class Memory {
    private final byte[] RAM = new byte[1024];
    private final byte[] HARD_DISK = new byte[1024];
    private final int PAGE_SIZE = 32;
    private int myVariable;

    private void startTimer() {
        Thread incrementThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000); // Задержка 5 секунд
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Восстановление статуса прерывания
                    break; // Выход из цикла при прерывании
                }
                myVariable++; // Увеличение переменной
                System.out.println("myVariable: " + myVariable); // Вывод текущего значения
            }
        });
        incrementThread.start(); // Запуск потока
    }
    public Memory(){
        startTimer();
    }
}
