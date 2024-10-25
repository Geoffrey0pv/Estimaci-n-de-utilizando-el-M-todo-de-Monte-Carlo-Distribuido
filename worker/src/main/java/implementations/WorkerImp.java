package implementations;

import java.util.Random;

import com.zeroc.Ice.Current;

import Contract.MasterPrx;
import Contract.Worker;

public class WorkerImp implements Worker {
    
    private MasterPrx master;
    private String workerID;

    public void setWorkerID(String workerID){
        this.workerID = workerID;
    }

    public void setMasterPrx(MasterPrx masterPrx){
        this.master = masterPrx;
    }

    @Override
    public void throwPointToCalculatePi(int amountOfPointsToThrow, Current current) {
        System.out.println("THROWING POINT TO MAKE THE CALCULATION");
        int pointsInsideCircle = 0;
        Random random = new Random();

        for (int i = 0; i < amountOfPointsToThrow; i++) {
            double x = random.nextDouble() * 2 - 1; 
            double y = random.nextDouble() * 2 - 1; 
            if (x * x + y * y <= 1) {  
                pointsInsideCircle++;
            }
        }
        System.out.println("bucle for terminado, enviando los datos: ");

        master.reportFromWorkerPiWasCalculated(pointsInsideCircle, workerID);
        System.out.println("Worker " + workerID + " completó su cálculo con " + pointsInsideCircle + " puntos dentro del círculo.");
    }
}
