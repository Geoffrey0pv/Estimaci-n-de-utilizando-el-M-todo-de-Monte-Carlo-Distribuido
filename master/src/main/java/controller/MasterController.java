package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.DoubleAdder;

import Contract.ClientPrx;
import Contract.WorkerPrx;

public class MasterController {
    private ClientPrx clientsWithCalculationsInProgress;
    private HashMap<String, WorkerPrx> availableSubWorkers;

    // Variables concurrentes
    //Se vio necesario utilizar estos tipos de datos debido a que varios workers, pueden notificar que ya terminaron a la vez. 
    //Entonces necesitamos un buen control de concurrencia para esto.
    private AtomicInteger amountOfCurrentWorkersInProgress;
    private AtomicInteger amountOfWorkersDone;
    private DoubleAdder amountOfpointsInsideTheCircle;
    private int amountOfPointsToThrow;

    public MasterController() {
        this.availableSubWorkers = new HashMap<>();
        this.amountOfCurrentWorkersInProgress = new AtomicInteger(0);
        this.amountOfWorkersDone = new AtomicInteger(0);
        this.amountOfpointsInsideTheCircle = new DoubleAdder();
    }

    public void addClientWithCalculationInProgress(ClientPrx client) throws IllegalAccessError {
        if (this.clientsWithCalculationsInProgress != null) {
            throw new IllegalAccessError("There is already a client connected");
        }
        System.out.println("CLIENT CONNECTED");
        this.clientsWithCalculationsInProgress = client;
    }

    public String subscribeNewWorker(WorkerPrx newWorker) {
        String newWorkerID = UUID.randomUUID().toString();
        WorkerPrx previousWorkerInHashMap = availableSubWorkers.put(newWorkerID, newWorker);

        if (previousWorkerInHashMap == null) {
            return newWorkerID;
        }
        return "";
    }

    public void commandWorkersToCalculatePi(int amountOfPointsToThrow) {
        new Thread(() -> {

            if (availableSubWorkers.isEmpty()) {
                System.out.println("No workers available.");
                return;
            }

            this.amountOfPointsToThrow = amountOfPointsToThrow;
            int pointsPerWorker = amountOfPointsToThrow / availableSubWorkers.size();
            int remainingPoints = amountOfPointsToThrow % availableSubWorkers.size();

            ArrayList<WorkerPrx> workers = new ArrayList<>(availableSubWorkers.values());

            for (WorkerPrx worker : workers) {
                int pointsToThrow = pointsPerWorker + (remainingPoints > 0 ? 1 : 0);
                remainingPoints--;
                amountOfCurrentWorkersInProgress.incrementAndGet();
                worker.throwPointToCalculatePi(pointsToThrow);
            }
        }).start();
    }

    public void notifyThatAWorkerIsDone(double amountOfpointsInsideTheCircle, String workerIdentifier) {
        amountOfWorkersDone.incrementAndGet();
        this.amountOfpointsInsideTheCircle.add(amountOfpointsInsideTheCircle);
        if (amountOfWorkersDone.get() == amountOfCurrentWorkersInProgress.get()) {
            System.out.println("Finishing, all workers are done.");
            double piValueCalculated = calculatePiValueWithAmountPointsInsideCircle();

            if (clientsWithCalculationsInProgress != null) {
                clientsWithCalculationsInProgress.masterNotifiedItsDone(piValueCalculated);
            } else {
                System.out.println("No client connected to notify.");
            }

            resetMasterProcessToReceiveANewOrder();
        }
    }

    private void resetMasterProcessToReceiveANewOrder() {
        amountOfCurrentWorkersInProgress.set(0);
        amountOfWorkersDone.set(0);
        clientsWithCalculationsInProgress = null;
        amountOfpointsInsideTheCircle.reset();
        amountOfPointsToThrow = 0;
        System.out.println("Master reset and ready for new calculations.");
    }

    private double calculatePiValueWithAmountPointsInsideCircle() {
        return 4 * (amountOfpointsInsideTheCircle.sum() / amountOfPointsToThrow);
    }
}

