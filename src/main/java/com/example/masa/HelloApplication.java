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
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        //launch();
        // we took the input for generator from user
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the number of processes: ");
        int numOfProcesses = input.nextInt();
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
        Process[] processes = new Process[numOfProcesses];
        generator(processes, numOfProcesses, maxArrivalTime, maxCpuBurst, maxIoBurst, minCpuBurst, minIoBurst, maxNumOfCpuBursts);
        //sort the processes by arrival time
        Arrays.sort(processes);
        //create a file to write the processes and their data each in a line
        File file = new File("input.txt");
        try {
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < processes.length; i++) {
                writer.write(processes[i].getPID() + " " + processes[i].getArrivalTime() + " ");
                for (int j = 0; j < processes[i].getCpuBursts().size(); j++) {
                    if (j == processes[i].getNumOfCpuBursts() - 1)
                        writer.write(processes[i].getCpuBursts().get(j) + "\n");
                    else
                        writer.write(processes[i].getCpuBursts().get(j) + " " + processes[i].getIoBursts().get(j) + " ");
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("Enter the time quantum 1: ");
        int timeQuantum = input.nextInt();
        int currentTimeQuantum = timeQuantum;
        System.out.println("Enter the time quantum 2: ");
        int timeQuantum2 = input.nextInt();
        int currentTimeQuantum2 = timeQuantum2;
        int currentTime = 0;
        ArrayList<Integer>GanttChart = new ArrayList<>();
        //I will transfer all the processes to the ready arraylist
        ArrayList<Process> readyQueue = new ArrayList<>();
        Queue<Process> roundRobinQueue1 = new LinkedList<>();
        Queue<Process> roundRobinQueue2 = new LinkedList<>();
        //the third queue will be the shortest remaining time first queue
        Queue<Process> shortestRemainingTimeFirstQueue = new LinkedList<>();
        //the fourth queue will be the first come first serve queue
        Queue<Process> firstComeFirstServeQueue = new LinkedList<>();
        //all queues done

        int busyCpuTime = 0;
        int totalCpuTime = 0;
        for(int i  = 0 ;  i < processes.length;i++){
            readyQueue.add(processes[i]);
        }

        while(true){
            //we will make the system sleep
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(Process p : readyQueue){
                //we will check if the process is in the ready queue and if it is not in the round-robin 1 queue
                if(p.getArrivalTime() == currentTime && !p.isInQueue() && p.getNumberOfPreemptions() <= 10){
                    roundRobinQueue1.add(p);
                    p.setInQueue(true);
                }
                else if (p.getArrivalTime() == currentTime && !p.isInQueue() && p.getNumberOfPreemptions() > 10){
                    roundRobinQueue2.add(p);
                    p.setInQueue(true);
                }
            }


            //we will print the round-robin queue with the first cpu burst
            if(!roundRobinQueue1.isEmpty()){
                System.out.println("Round Robin Queue 1: ");
                for(Process p : roundRobinQueue1){
                    System.out.println(p.getPID() + " " +  p.getCpuBurst(0) + " " + currentTime);
                    System.out.println("Number of preemptions: " + p.getNumberOfPreemptions());

                }
            }
            if(!roundRobinQueue2.isEmpty()){
                System.out.println("Round Robin Queue 2: ");
                for(Process p : roundRobinQueue2){
                    System.out.println(p.getPID() + " " +  p.getCpuBurst(0) + " " + currentTime);
                    //print number of preemptions
                    System.out.println("Number of preemptions: " + p.getNumberOfPreemptions());
                }
            }



            //if all processes are finished we break the loop
            boolean allFinished = true;
            //print processes finish state
            for(Process p : readyQueue){
                if(!p.isFinished()){
                    allFinished = false;
                    break;
                }
            }

            if(allFinished){
                break;
            }

            if(!roundRobinQueue1.isEmpty()){
                if(roundRobinQueue1.peek().getCpuBurst(0) != 0 && currentTimeQuantum != 0){
                    currentTimeQuantum--;
                    GanttChart.add(roundRobinQueue1.peek().getPID());
                    roundRobinQueue1.peek().setCpuBurst(0,roundRobinQueue1.peek().getCpuBurst(0) - 1);
                }
                else if(roundRobinQueue1.peek().getCpuBurst(0) != 0 && currentTimeQuantum == 0){
                    currentTimeQuantum = timeQuantum;
                    roundRobinQueue1.peek().incrementNumberOfPreemptions();
                    if(roundRobinQueue1.peek().getNumberOfPreemptions() <= 10){
                        roundRobinQueue1.add(roundRobinQueue1.poll());

                    }
                    else{
                        roundRobinQueue2.add(roundRobinQueue1.poll());
                    }
                    currentTime--;
                    totalCpuTime--;
                    busyCpuTime++;
                }
                else if(roundRobinQueue1.peek().getCpuBurst(0) == 0 && roundRobinQueue1.peek().getCpuBursts().size() == 1){
                    roundRobinQueue1.peek().setFinished(true);
                    //also make it finished at the ready queue
                    for(Process p : readyQueue){
                        if(p.getPID() == roundRobinQueue1.peek().getPID()){
                            p.setFinished(true);
                        }
                    }
                    System.out.println("Process " + roundRobinQueue1.peek().getPID() + " finished completely at time " + currentTime);
                    roundRobinQueue1.peek().getCpuBursts().remove(0);
                    roundRobinQueue1.peek().addFinishTime(currentTime);
                    roundRobinQueue1.peek().setInQueue(false);


                    System.out.println("Finish times: ");
                    for(int i = 0 ; i < roundRobinQueue1.peek().getFinishTimes().size();i++){
                        System.out.print(roundRobinQueue1.peek().getFinishTimes().get(i) + " ");
                    }
                    //we will print out the waiting time for the finished process
                    System.out.println();
                    System.out.println("Waiting time: ");
                    for(Process p : readyQueue){
                        if(p.getPID() == roundRobinQueue1.peek().getPID()){
                            System.out.println(p.getWaitingTime() + " time units");
                            break;
                        }
                    }
                    roundRobinQueue1.poll();
                    currentTime--;
                    totalCpuTime--;
                    busyCpuTime++;
                }
                else if(roundRobinQueue1.peek().getCpuBurst(0) == 0 && roundRobinQueue1.peek().getCpuBursts().size() > 1){
                    roundRobinQueue1.peek().setInQueue(false);
                    roundRobinQueue1.peek().setArrivalTime(currentTime + roundRobinQueue1.peek().getIoBurst(0));
                    roundRobinQueue1.peek().getCpuBursts().remove(0);
                    roundRobinQueue1.peek().getIoBursts().remove(0);
                    roundRobinQueue1.peek().addFinishTime(currentTime);
                    //i want to print the ready queue
                    System.out.println("Ready Queue: ");
                    for(Process p : readyQueue){
                        System.out.println(p.getPID() + " " + p.getArrivalTime());
                    }

                    readyQueue.add(roundRobinQueue1.poll());
                    Collections.sort(readyQueue);

                    currentTime--;
                    totalCpuTime--;
                    busyCpuTime++;
                }
            }
            else if(!roundRobinQueue2.isEmpty() && roundRobinQueue1.isEmpty()){
                //same as above but for the second round-robin-1 queue
                if(roundRobinQueue2.peek().getCpuBurst(0) != 0 && currentTimeQuantum2 != 0){
                    currentTimeQuantum2--;
                    GanttChart.add(roundRobinQueue2.peek().getPID());
                    roundRobinQueue2.peek().setCpuBurst(0,roundRobinQueue2.peek().getCpuBurst(0) - 1);
                }
                else if(roundRobinQueue2.peek().getCpuBurst(0) != 0 && currentTimeQuantum2 == 0){
                    currentTimeQuantum2 = timeQuantum2;
                    roundRobinQueue2.add(roundRobinQueue2.poll());
                    currentTime--;
                    totalCpuTime--;
                    busyCpuTime++;
                }
                else if(roundRobinQueue2.peek().getCpuBurst(0) == 0 && roundRobinQueue2.peek().getCpuBursts().size() == 1){
                    roundRobinQueue2.peek().setFinished(true);
                    //also make it finished at the ready queue
                    for(Process p : readyQueue){
                        if(p.getPID() == roundRobinQueue2.peek().getPID()){
                            p.setFinished(true);
                        }
                    }
                    System.out.println("Process " + roundRobinQueue2.peek().getPID() + " finished completely at time " + currentTime);
                    roundRobinQueue2.peek().getCpuBursts().remove(0);
                    roundRobinQueue2.peek().addFinishTime(currentTime);
                    roundRobinQueue2.peek().setInQueue(false);
                    System.out.println("Finish times: ");
                    for(int i = 0 ; i < roundRobinQueue2.peek().getFinishTimes().size();i++){
                        System.out.print(roundRobinQueue2.peek().getFinishTimes().get(i) + " ");
                    }
                    System.out.println();
                    System.out.println("Waiting time: ");
                    for(Process p : readyQueue){
                        if(p.getPID() == roundRobinQueue2.peek().getPID()){
                            System.out.println(p.getWaitingTime() + " time units");
                            break;
                        }
                    }

                    readyQueue.add(roundRobinQueue2.poll());
                    Collections.sort(readyQueue);
                    currentTime--;
                    totalCpuTime--;
                    busyCpuTime++;
                }
            }
            else
                GanttChart.add(0);
            currentTime++;
            totalCpuTime++;
        }
        System.out.println("total cpu time: " + totalCpuTime);
        System.out.println("busy cpu time: " + busyCpuTime);
        System.out.println("Gantt Chart: ");
        int busyCount = 0;
for(int i = 0 ; i < GanttChart.size(); i++){
            System.out.print(GanttChart.get(i));
            if(GanttChart.get(i) != 0){
                busyCount++;
            }
        }
        System.out.println();
        System.out.println("Throughput: " + busyCount + " processes per time unit");
        //we will filter dupicate processes from the ready queue using hashset
        Set<Process> set = new HashSet<Process>(readyQueue);
        readyQueue.clear();
        readyQueue.addAll(set);
        //now we calculate the average waiting time
        int totalWaitingTime = 0;
        for(Process p : readyQueue){
            totalWaitingTime += p.getWaitingTime();
        }
        //we will print the cpu utilization from the gannt chart
        System.out.println("CPU Utilization: " + ((double)busyCount / GanttChart.size())*100 + "%");

        System.out.println("Average waiting time: " + (double)totalWaitingTime / readyQueue.size());
        System.exit(0);
    }

    public static void generator(Process[] processes, int numProcesses, int maxArrivalTime, int maxCpuBurst, int maxIoBurst, int minCpuBurst, int minIoBurst, int maxNumOfCpuBursts) {
        for(int i = 0; i < processes.length ; i++) {
            processes[i] = new Process();
            processes[i].setPID(i+1);
            processes[i].setArrivalTime((int) (Math.random() * maxArrivalTime));
            processes[i].setStaticArrivalTime(processes[i].getArrivalTime());
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
            processes[i].calcTotalCpuBurst();
        }
    }
}