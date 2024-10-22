package implementations;
import com.zeroc.Ice.Current;

import Contract.MasterPrx;
import Contract.Worker;


public class WorkerImp implements Worker {

    private int amountOfPointsToThrow;
    private int amountOfPointsInsideTheCircle;
    private int amountOfPointsOutsideTheCircle;
    private boolean isCalculatingPi;
    private boolean isMasterConnected;
    private MasterPrx master;

    public WorkerImp(){
        this.isCalculatingPi = false;
        this.isMasterConnected = false;
    }

    @Override
    public void throwPointToCalculatePi(int amountOfPointsToThrow, Current current) {
        this.amountOfPointsToThrow = amountOfPointsToThrow;
        this.isCalculatingPi = true;
        this.amountOfPointsInsideTheCircle = 0;
        this.amountOfPointsOutsideTheCircle = 0;
    }

}
