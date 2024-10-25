import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;
import Contract.MasterPrx;
import Contract.WorkerPrx;
import implementations.WorkerImp;

public class Worker {
    public static void main(String[] args) {

        try (Communicator communicator = Util.initialize(args, "properties.cfg")) {

            // con esto creo   el adaptador y obtener el proxy del Master
            ObjectAdapter adapter = communicator.createObjectAdapter("Worker");

            MasterPrx master = MasterPrx.checkedCast(communicator.propertyToProxy("Master.Proxy"));
            // aquí inicializo mi WorkerImp y le paso el MasterPrx y el workerID
            WorkerImp workerImp = new WorkerImp();

            ObjectPrx objectPrx = adapter.add(workerImp, Util.stringToIdentity("WorkerEndpoint"));
            
            WorkerPrx worker = WorkerPrx.checkedCast(objectPrx);

            adapter.activate();

            if (master == null || worker == null) {
                throw new Error("Proxies no válidos para Worker o Master");
            }

            workerImp.setMasterPrx(master);

            // Aquí me suscribo al Master y obtengo un ID
            String workerSubscribedID = master.subToMaster(worker);
            System.out.println("Worker suscrito con ID: " + workerSubscribedID);
            workerImp.setWorkerID(workerSubscribedID);

            // espero a que el Master me asigne una tarea de cálculo
            communicator.waitForShutdown();
        }
    }
}
