package com.example.masa;
//this class implements comparable so that we can sort the processes by arrival time
// hiiiiiiiiiiiiiiiiiiiiiiiii
public class Process implements Comparable<Process> {
    private int PID;

    private int numOfCpuBursts;
    private int arrivalTime;
    // Cpu bursts and IO bursts are arrays because each process has more than one CPU burst and IO burst
    private int[] cpuBursts;
    private int[] ioBursts;

    //create a constructor with all fields without taking parameters
    public Process() {
        PID = 0;
        arrivalTime = 0;
        numOfCpuBursts = 1;//so we dont get array out of bounds exception
        cpuBursts = new int[numOfCpuBursts];
        ioBursts = new int[numOfCpuBursts-1];
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
    public int[] getCpuBursts() {
        return cpuBursts;
    }
    public void setCpuBursts(int[] cpuBursts) {
        this.cpuBursts = cpuBursts;
    }
    public int[] getIoBursts() {
        return ioBursts;
    }
    public void setIoBursts(int[] ioBursts) {
        this.ioBursts = ioBursts;
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
