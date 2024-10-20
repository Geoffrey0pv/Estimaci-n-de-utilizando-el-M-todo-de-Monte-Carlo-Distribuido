import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import controller.MasterController;
import implementations.MasterImp;

public class Master {
   public static void main(String[] args) {
      try (Communicator communicator = Util.initialize(args, "properties.cfg")) {
         MasterController masterController = new MasterController();

         MasterImp masterImp = new MasterImp(masterController);

         ObjectAdapter adapter = communicator.createObjectAdapter("Master");

         adapter.add(masterImp, Util.stringToIdentity("MasterEndpoint"));
         adapter.activate();

         System.out.println("MASTER INITIATED ON");

         communicator.waitForShutdown();
      }
   }
}
