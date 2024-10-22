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
     * This can throw illegal acces error if you try to connect another client when a client is alredy connected. 
     */
    public void calculatePi(int amountOfPoints, ClientPrx clientPrxCaller, Current current) throws IllegalAccessError{
        masterController.addClientWithCalculationInProgress(clientPrxCaller);

        masterController.commandWorkersToCalculatePi(amountOfPoints);
    }

    @Override
    public void reportFromWorkerPiWasCalculated(double amountOfpointsInsideTheCircle, String workerIdentifier,
            Current current) {
        masterController.notifyThatAWorkerIsDone(amountOfpointsInsideTheCircle, workerIdentifier);
    }

    @Override
    public String subToMaster(WorkerPrx workerPrxCaller, Current current) {

        String workerSubeddNewID = masterController.subscribeNewWorker(workerPrxCaller);

        return workerSubeddNewID;
    }

}
