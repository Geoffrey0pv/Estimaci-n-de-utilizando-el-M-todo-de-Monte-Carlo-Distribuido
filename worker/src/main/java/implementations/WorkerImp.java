package implementations;

import com.zeroc.Ice.Current;

import Contract.Worker;

public class WorkerImp implements Worker {

    WorkerController controller;

    public WorkerImp(WorkerController controller) {
        this.controller = controller;
    }

    @Override
    public void throwPointToCalculatePi(int amountOfPointsToThrow, Current current) {
        controller.calculatePi(amountOfPointsToThrow);
        System.out.println("finished with the calculation");
    }
}
