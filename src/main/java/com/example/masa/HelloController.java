package com.example.masa;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class HelloController{
    @FXML
    private TextArea output2;

    @FXML
    private ScrollPane sroller;
    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Label label3;

    @FXML
    private Label label4;

    @FXML
    private Label label5;

    @FXML
    private Label label6;

    @FXML
    private Label label7;

    @FXML
    private Button button1;

    @FXML
    private Button button2;
    @FXML
    private TextField output1;

    @FXML
    private TextField text1;

    @FXML
    private TextField text2;

    @FXML
    private TextField text3;

    @FXML
    private TextField text4;

    @FXML
    private TextField text5;

    @FXML
    private TextField text6;

    @FXML
    private TextField text7;
    @FXML
    private TextField text8;

    @FXML
    void button1(MouseEvent event) {
        int numOfProccesses = Integer.parseInt(text1.getText());
        int maxNumOfCpuBursts = Integer.parseInt(text2.getText());
        int maxArrivalTime = Integer.parseInt(text3.getText());
        int maxCpuBurst = Integer.parseInt(text4.getText());
        int maxIoBurst = Integer.parseInt(text5.getText());
        int minCpuBurst = Integer.parseInt(text6.getText());
        int minIoBurst = Integer.parseInt(text7.getText());
        int timeQuantum =Integer.parseInt(text8.getText());

        Process[] processes = new Process[numOfProccesses];

        generator(processes, numOfProccesses, maxArrivalTime, maxCpuBurst, maxIoBurst, minCpuBurst, minIoBurst, maxNumOfCpuBursts);
//sort the processes by arrival time
        Arrays.sort(processes);
        //create a file to write the processes and thier data each in a line
        File file = new File("input1.txt");
        try {
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < processes.length; i++) {
                writer.write(processes[i].getPID() + " " + processes[i].getArrivalTime() + " ");
                for (int j = 0; j < processes[i].getCpuBursts().size(); j++) {
                    if (j == processes[i].getNumOfCpuBursts() - 1)
                        writer.write(processes[i].getCpuBursts().get(j) + "\n");
                    else
                        writer.write(processes[i].getCpuBursts().get(j)+ " ");
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        int currentTimeQuantum = timeQuantum;
        int currentTime = 0;
        Queue<Process> readyQueue = new LinkedList<>();
        Queue<Process> roundRobinQueue1 = new LinkedList<>();
        for(int i  = 0 ;  i < processes.length;i++){
            readyQueue.add(processes[i]);
        }
        while(true){
            //if all processes are finished we break the loop
            if(readyQueue.isEmpty() && roundRobinQueue1.isEmpty()){
                break;
            }
            if(!readyQueue.isEmpty()){
                if(readyQueue.peek().getArrivalTime() == currentTime){
                    roundRobinQueue1.add(readyQueue.poll());
                }
            }
            if(!roundRobinQueue1.isEmpty()){
                if(roundRobinQueue1.peek().getCpuBurst(0) != 0 && currentTimeQuantum != 0){
                    currentTimeQuantum--;
                    roundRobinQueue1.peek().setCpuBurst(0,roundRobinQueue1.peek().getCpuBurst(0) - 1);
                }
                else if(roundRobinQueue1.peek().getCpuBurst(0) != 0 && currentTimeQuantum == 0){
                    currentTimeQuantum = timeQuantum;
                    roundRobinQueue1.add(roundRobinQueue1.poll());
                    currentTime--;
                }
                else if(roundRobinQueue1.peek().getCpuBurst(0) == 0  && !roundRobinQueue1.peek().getCpuBursts().isEmpty()){
                    //roundRobinQueue1.peek().setFinished(true);
                    roundRobinQueue1.peek().setArrivalTime(currentTime + roundRobinQueue1.peek().getIoBurst(0));
                   output2.appendText("Process " + roundRobinQueue1.peek().getPID() + " finished at time " + currentTime + " new Arrival time " + roundRobinQueue1.peek().getArrivalTime()+"\n");
                    //we want to make the next cpu burst the first one if there is one
                    roundRobinQueue1.peek().cutFirstCpuBurst();
                    roundRobinQueue1.peek().cutFirstIoBurst();
                    roundRobinQueue1.add(roundRobinQueue1.poll());
                    currentTimeQuantum = timeQuantum;
                    currentTime--;


                }
                else if(roundRobinQueue1.peek().getCpuBurst(0) == 0 && roundRobinQueue1.peek().getCpuBursts().isEmpty()){
                    roundRobinQueue1.peek().setFinished(true);
                    output2.appendText("Process " + roundRobinQueue1.peek().getPID() + " finished at time " + currentTime+ "\n");
                    roundRobinQueue1.poll();
                    currentTimeQuantum = timeQuantum;
                    currentTime--;
                }

            }
            currentTime++;
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
        }}

    public void button2(MouseEvent mouseEvent) {
    }
}







