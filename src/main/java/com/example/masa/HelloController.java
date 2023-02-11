package com.example.masa;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class HelloController{
    @FXML
    private TextField output2;
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
    private Button ll;
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
    void masa(MouseEvent event) {
        int numOfProccesses = Integer.parseInt(text1.getText());
        int maxNumOfCpuBursts = Integer.parseInt(text2.getText());
        int maxArrivalTime = Integer.parseInt(text3.getText());
        int maxCpuBurst = Integer.parseInt(text4.getText());
        int maxIoBurst = Integer.parseInt(text5.getText());
        int minCpuBurst = Integer.parseInt(text6.getText());
        int minIoBurst = Integer.parseInt(text7.getText());
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
        for (int i = 0; i < processes.length; i++) {
            output1.setText(processes[i].getPID() + " " + processes[i].getArrivalTime() + " ");
            for (int j = 0; j < processes[i].getCpuBursts().size(); j++) {
                if (j == processes[i].getNumOfCpuBursts() - 1)
                    output1.setText(processes[i].getCpuBursts().get(j)+ "\n");
                else
                    output1.setText(processes[i].getCpuBursts().get(j) + " " + processes[i].getIoBursts().get(j) + " ");
            }


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

    }







