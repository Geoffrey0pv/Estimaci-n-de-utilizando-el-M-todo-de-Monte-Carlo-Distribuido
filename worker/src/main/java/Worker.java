import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import Contract.MasterPrx;
import Contract.WorkerPrx;
import implementations.WorkerImp;


public class Worker {
    public static void main(String[] args) {

        String workerSubeddNewID = "";

        try (Communicator communicator = Util.initialize(args, "properties.cfg")) {

            WorkerImp workerImp = new WorkerImp();

            ObjectAdapter adapter = communicator.createObjectAdapter("Worker");

            adapter.add(workerImp, Util.stringToIdentity("WorkerEndpoint"));

            adapter.activate();

            System.out.println("WORKER INITIATED ON");

            communicator.waitForShutdown();

            WorkerPrx worker = WorkerPrx.checkedCast(communicator.stringToProxy("WorkerEndpoint"));

            MasterPrx master = MasterPrx.checkedCast(communicator.stringToProxy("MasterEndpoint"));

            workerSubeddNewID = master.subToMaster(worker);


            


        }
    }
}
