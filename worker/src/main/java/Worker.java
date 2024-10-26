import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import Contract.MasterPrx;
import Contract.WorkerPrx;
import implementations.WorkerController;
import implementations.WorkerImp;

public class Worker {

    public static void main(String[] args) {

        try (Communicator communicator = Util.initialize(args, "properties.cfg")) {
            // con esto creo el adaptador y obtengo el proxy del Master
            ObjectAdapter adapter = communicator.createObjectAdapter("Worker");
            MasterPrx master = MasterPrx.checkedCast(communicator.propertyToProxy("Master.Proxy"));

            WorkerController controller = new WorkerController(master);
            WorkerImp workerImp = new WorkerImp(controller);

            // aquí inicializo mi WorkerImp y le paso el MasterPrx y el workerID
            ObjectPrx objectPrx = adapter.add(workerImp, Util.stringToIdentity("WorkerEndpoint"));
            WorkerPrx worker = WorkerPrx.checkedCast(objectPrx);

            adapter.activate();

            if (master == null || worker == null) {
                throw new Error("Proxies no válidos para Worker o Master");
            }

            // Aquí me suscribo al Master y obtengo un ID
            controller.subToMaster(worker);
            master.test("estamos probando desde el pi worker desde el main");

            // espero a que el Master me asigne una tarea de cálculo
            communicator.waitForShutdown();
        }
    }
}
