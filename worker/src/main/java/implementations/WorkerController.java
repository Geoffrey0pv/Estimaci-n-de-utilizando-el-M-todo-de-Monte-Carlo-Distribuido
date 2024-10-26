package implementations;

import Contract.MasterPrx;
import Contract.WorkerPrx;

import java.util.Random;

public class WorkerController {

    private String workerID;
    private MasterPrx master;

    public WorkerController( MasterPrx masterPrx) {
        this.master = masterPrx;
    }

    public void subToMaster(WorkerPrx worker){
        this.workerID = master.subToMaster(worker);
    }

    public void calculatePi(int amountOfPointsToThrow) {

        System.out.println("THROWING POINT TO MAKE THE CALCULATION");
        master.test("estamos probando desde el pi worker desde el inciio del metodo");

        int pointsInsideCircle = 0;
        Random random = new Random();

        System.out.println("Worker " + workerID + " comenzando cálculo con " + amountOfPointsToThrow + " puntos.");

        for (int i = 0; i < amountOfPointsToThrow; i++) {
            double x = random.nextDouble() * 2 - 1;
            double y = random.nextDouble() * 2 - 1;
            if (x * x + y * y <= 1) {
                pointsInsideCircle++;
            }
        }
        System.out.println("bucle for terminado, enviando los datos: ");
        System.out.println("Worker " + workerID + " completó su cálculo con " + pointsInsideCircle
                + " puntos dentro del círculo.");

        System.out.println("WORKER SUBSCRIBED WITH ID: " + master.toString());
        master.test("estamos probando desde el pi worker");
        master.reportFromWorkerPiWasCalculated(pointsInsideCircle, workerID);
    }
}
