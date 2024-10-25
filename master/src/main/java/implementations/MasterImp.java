package implementations;

import com.zeroc.Ice.Current;

import Contract.ClientPrx;
import Contract.Master;
import Contract.WorkerPrx;
import controller.MasterController;

public class MasterImp implements Master {

    private MasterController masterController;

    public MasterImp(MasterController masterController) {
        this.masterController = masterController;
    }

    @Override
    /**
     * This can throw illegal acces error if you try to connect another client when
     * a client is alredy connected.
     */
    public void calculatePi(int amountOfPoints, ClientPrx clientPrxCaller, Current current) throws IllegalAccessError {
        System.out.println("ORDER TO CALCULATE PI RECEIVED BY CLIENT");
        masterController.addClientWithCalculationInProgress(clientPrxCaller);
        masterController.commandWorkersToCalculatePi(amountOfPoints);
    }

    @Override
    public void reportFromWorkerPiWasCalculated(double amountOfpointsInsideTheCircle, String workerIdentifier,
            Current current) {
        System.out.println("Master was notified that a worker is done");
        masterController.notifyThatAWorkerIsDone(amountOfpointsInsideTheCircle, workerIdentifier);
    }

    @Override
    public String subToMaster(WorkerPrx workerPrxCaller, Current current) {
        if (workerPrxCaller == null) {
            System.err.println("Worker proxy is null.");
            return "";
        }
        String workerSubeddNewID = masterController.subscribeNewWorker(workerPrxCaller);
        System.out.println("NEW WORKER SUBSCRIBED, " + workerSubeddNewID);
        return workerSubeddNewID;
    }

    @Override
    public void test(String s, Current current) {
        System.out.println(s);
    }


}
