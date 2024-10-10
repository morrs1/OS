package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        var memory = new Memory();

//        while (true) {
//            memory.write(10, (byte) 11);
//            memory.checkMemory();
//            Scanner scanner = new Scanner(System.in);
//            int a = Integer.parseInt(scanner.nextLine());
//
//        }

//        IntStream.range(0,32).forEach(index->memory.write(index*32+10, (byte) index));

//        memory.readAll();
        memory.findNFU();
        memory.write(325, (byte) 111);
        memory.write(1124, (byte) 22);
        memory.readAll();
        memory.changeLocationOfPage(10, 35);
        memory.readAll();
    }
}