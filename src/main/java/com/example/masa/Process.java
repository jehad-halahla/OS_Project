package com.example.masa;

import java.util.ArrayList;

//this class implements comparable so that we can sort the processes by arrival time
public class Process implements Comparable<Process> {
    private int PID;
    private boolean isFinished;
    private int numOfCpuBursts;
    private int arrivalTime;
    // Cpu bursts and IO bursts are arrays because each process has more than one CPU burst and IO burst
    private ArrayList<Integer> cpuBursts;
    private ArrayList<Integer> ioBursts;

    //create a constructor with all fields without taking parameters
    public Process() {
        PID = 0;
        arrivalTime = 0;
        numOfCpuBursts = 1;//so we don't get array out of bounds exception
        isFinished = false;
        cpuBursts = new ArrayList<>();
        ioBursts = new ArrayList<>();
    }
    // create getters and setters for all fields
    public int getPID() {
        return PID;
    }
    public void setPID(int PID) {
        this.PID = PID;
    }
    public int getNumOfCpuBursts() {
        return numOfCpuBursts;
    }
    public void setNumOfCpuBursts(int numOfCpuBursts) {
        this.numOfCpuBursts = numOfCpuBursts;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public ArrayList<Integer> getCpuBursts() {
        return cpuBursts;
    }
    public void setCpuBursts(ArrayList<Integer> cpuBursts) {
        this.cpuBursts = cpuBursts;
    }
    public ArrayList<Integer> getIoBursts() {
        return ioBursts;
    }
    public void setIoBursts(ArrayList<Integer> ioBursts) {
        this.ioBursts = ioBursts;
    }
    //we wil create a method to set a specific cpu burst
    public void setCpuBurst(int index, int burst) {
        cpuBursts.set(index, burst);
    }
//we wil create a method to set a specific io burst
    public void setIoBurst(int index, int burst) {
        ioBursts.set(index, burst);
    }
//we wil create a method to get a specific cpu burst
    public int getCpuBurst(int index) {
        if(cpuBursts.size() != 0)
        return cpuBursts.get(index);
        else return 0;}
    //now we set the total arraylist from an array
    public void setCpuBursts(int[] cpuBursts) {
        for (int i = 0; i < cpuBursts.length; i++) {
            this.cpuBursts.add(cpuBursts[i]);
        }
    }
    // a method to cut the first element from cpu bursts
    public void cutFirstCpuBurst() {
        if(cpuBursts.size() != 0)
        cpuBursts.remove(0);
    }
    // a method to cut the first element from io bursts
    public void cutFirstIoBurst() {
        if(ioBursts.size() != 0)
        ioBursts.remove(0);
    }
public void setIoBursts(int[] ioBursts) {
        for (int i = 0; i < ioBursts.length; i++) {
            this.ioBursts.add(ioBursts[i]);
        }
    }
//we wil create a method to get a specific io burst
    public int getIoBurst(int index) {
        if(ioBursts.size() != 0)
        return ioBursts.get(index);
        else return 0;
    }

//we wil create a method to check if the process is finished
    public boolean isFinished() {
        return isFinished;
    }
//we wil create a method to set the process as finished
    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    //override the compareTo method to compare the arrival time of the processes
    @Override
    public int compareTo(Process o) {
        if (this.arrivalTime < o.arrivalTime) {
            return -1;
        } else if (this.arrivalTime > o.arrivalTime) {
            return 1;
        } else {
            return 0;
        }
    }

}
