package com.example.masa;

import java.util.Scanner;

public class simulator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = 0;
        boolean keepCounting = true;
        while (keepCounting) {
            System.out.println("Current count: " + count);
            count++;
            System.out.print("Enter 'pause' to pause the loop or 'resume' to resume: ");
            String input = scanner.nextLine();
            if (input.equals("pause")) {
                keepCounting = false;
                System.out.println("Paused. Enter 'resume' to continue.");
                while (!input.equals("resume")) {
                    input = scanner.nextLine();
                }
                keepCounting = true;
                System.out.println("Resumed.");
            }
        }
    }
}
