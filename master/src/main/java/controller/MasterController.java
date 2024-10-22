package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import Contract.ClientPrx;
import Contract.WorkerPrx;

public class MasterController {
    private ClientPrx clientsWithCalculationsInProgress;
    // This hashmaps holds all the workers that are subscribed to this master
    private HashMap<String, WorkerPrx> availableSubWorkers;

    private int amountOfCurrentWorkersInProgress;
    private int amountOfWorkersDone;
    private double amountOfpointsInsideTheCircle;
    private int amountOfPointsToThrow;

    public MasterController(){
        this.availableSubWorkers = new HashMap<>();
        this.amountOfCurrentWorkersInProgress = 0;
        this.amountOfWorkersDone = 0;
    }

    /**
     * 
     * @param client this is the client that started a process
     */
    public void addClientWithCalculationInProgress(ClientPrx client) throws IllegalAccessError{
        if (this.clientsWithCalculationsInProgress != null){
            throw new IllegalAccessError("There is alredy a client connected");
        }
        this.clientsWithCalculationsInProgress = client;
    }

    /**
     * 
     * @param newWorker New worker to be added
     * @return a string, indicating the id that that worker got assigned by the master
     */
    public String subscribeNewWorker(WorkerPrx newWorker){
        String newWorkerID = UUID.randomUUID().toString(); 

        WorkerPrx previousWorkerInHashMap = availableSubWorkers.put(newWorkerID, newWorker);

        if (previousWorkerInHashMap == null){
            return newWorkerID;
        }
        return "";
    }

    public void commandWorkersToCalculatePi(int amountOfPointsToThrow){

        this.amountOfPointsToThrow = amountOfPointsToThrow;

        for(Map.Entry<String, WorkerPrx> workerSet : availableSubWorkers.entrySet()){
            WorkerPrx currentWorkerInSet = workerSet.getValue();
            currentWorkerInSet.throwPointToCalculatePi(amountOfPointsToThrow); //TODO: CALCULATE HOW MANY WILL BE SENT TO EACH WORKER
            amountOfCurrentWorkersInProgress++;
        }
    }

    public void notifyThatAWorkerIsDone(double amountOfpointsInsideTheCircle, String workerIdentifier){
        amountOfWorkersDone++;
        this.amountOfpointsInsideTheCircle += amountOfpointsInsideTheCircle;
        //This if indicates that all workers have finished
        if(amountOfWorkersDone == amountOfCurrentWorkersInProgress){
            double piValueCalculated = calculatePiValueWithAmountPointsInsideCircle();
            clientsWithCalculationsInProgress.masterNotifiedItsDone(piValueCalculated);
            resetMasterProcessToReceiveANewOrder();
        }
    }
    /**
     * If a task is finished and the client was notified of the result, it should be open to receive another client request.
     */
    private void resetMasterProcessToReceiveANewOrder(){
        amountOfCurrentWorkersInProgress = 0;
        amountOfWorkersDone = 0;
        clientsWithCalculationsInProgress = null;   
        amountOfpointsInsideTheCircle = 0;
        amountOfPointsToThrow = 0;
    }

    private double calculatePiValueWithAmountPointsInsideCircle(){
        return 4 * (amountOfpointsInsideTheCircle / amountOfPointsToThrow);
    }
}
