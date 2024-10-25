package implementations;

import java.util.Random;

import com.zeroc.Ice.Current;

import Contract.MasterPrx;
import Contract.Worker;

public class WorkerImp implements Worker {
    
    private MasterPrx master;
    private String workerID;

    public WorkerImp(MasterPrx master) {
        this.master = master;
    }

    public void setWorkerID(String workerID){
        this.workerID = workerID;
    }

    @Override
    public void throwPointToCalculatePi(int amountOfPointsToThrow, Current current) {
        int pointsInsideCircle = 0;
        Random random = new Random();

        for (int i = 0; i < amountOfPointsToThrow; i++) {
            double x = random.nextDouble() * 2 - 1; 
            double y = random.nextDouble() * 2 - 1; 
            if (x * x + y * y <= 1) {  
                pointsInsideCircle++;
            }
        }

        master.reportFromWorkerPiWasCalculated(pointsInsideCircle, workerID);
        System.out.println("Worker " + workerID + " completó su cálculo con " + pointsInsideCircle + " puntos dentro del círculo.");
    }
}
