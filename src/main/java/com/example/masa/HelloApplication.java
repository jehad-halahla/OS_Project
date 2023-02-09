package com.example.masa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        launch();
        // we took the input for generator from user
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the number of processes: ");
        int numOfProccesses = input.nextInt();
        //take the max number of cpu bursts from user
        System.out.println("Enter the max number of cpu bursts: ");
        int maxNumOfCpuBursts = input.nextInt();
        // take Max arrival time from user
        System.out.println("Enter the max arrival time: ");
        int maxArrivalTime = input.nextInt();
        // take Max CPU burst time from user
        System.out.println("Enter the max CPU burst time: ");
        int maxCpuBurst = input.nextInt();
        // take Max IO burst time from user
        System.out.println("Enter the max IO burst time: ");
        int maxIoBurst = input.nextInt();
        // take minimum CPU burst time from user
        System.out.println("Enter the minimum CPU burst time: ");
        int minCpuBurst = input.nextInt();
        // take minimum IO burst time from user
        System.out.println("Enter the minimum IO burst time: ");
        int minIoBurst = input.nextInt();
        Process[] processes = new Process[numOfProccesses];
        generator(processes, numOfProccesses, maxArrivalTime, maxCpuBurst, maxIoBurst, minCpuBurst, minIoBurst, maxNumOfCpuBursts);
        //sort the processes by arrival time
        Arrays.sort(processes);
        //create a file to write the processes and thier data each in a line
        File file = new File("input.txt");
       try{
              FileWriter writer = new FileWriter(file);
              for(int i = 0; i < processes.length; i++) {
                writer.write(processes[i].getPID() + " " + processes[i].getArrivalTime() + " ");
                for(int j = 0; j < processes[i].getCpuBursts().length; j++) {
                    if(j == processes[i].getNumOfCpuBursts()-1)
                    writer.write(processes[i].getCpuBursts()[j]+ "\n");
                    else
                        writer.write(processes[i].getCpuBursts()[j] + " " + processes[i].getIoBursts()[j] + " ");
                }
       }
         writer.close();
         }catch(IOException e){
              System.out.println("An error occurred.");
              e.printStackTrace();
        }
    }
    public static void generator(Process[] processes, int numProcesses, int maxArrivalTime, int maxCpuBurst, int maxIoBurst, int minCpuBurst, int minIoBurst, int maxNumOfCpuBursts) {
        for(int i = 0; i < processes.length ; i++) {
            processes[i] = new Process();
            processes[i].setPID(i+1);
            processes[i].setArrivalTime((int) (Math.random() * maxArrivalTime));
            //minNumOfCpuBursts = 1;
            //generate random number of cpu bursts
            processes[i].setNumOfCpuBursts((int) (Math.random() * maxNumOfCpuBursts) + 1);
            //generate random cpu burst times
            int[] cpuBursts = new int[processes[i].getNumOfCpuBursts()];
            for(int j = 0; j < cpuBursts.length; j++) {
                cpuBursts[j] = (int) (Math.random() * (maxCpuBurst - minCpuBurst)) + minCpuBurst;
            }
            processes[i].setCpuBursts(cpuBursts);
            //generate random io burst times
            int[] ioBursts = new int[processes[i].getNumOfCpuBursts() - 1];
            for(int j = 0; j < ioBursts.length; j++) {
                ioBursts[j] = (int) (Math.random() * (maxIoBurst - minIoBurst)) + minIoBurst;
            }
            processes[i].setIoBursts(ioBursts);
        }
    }
}