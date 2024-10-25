import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import Contract.ClientPrx;
import Contract.MasterPrx;
import implementations.ClientImp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private Communicator communicator;
    private ClientPrx myPrx;

    public static void main(String[] args) {
        Client client = new Client();
        client.start(args);
    }

    public void start(String[] args) {
        try {
            // Iniciar el comunicador con la configuración del archivo .cfg
            communicator = Util.initialize(args, "properties.cfg");

            // Crear el adapter para el Client
            ObjectAdapter adapter = communicator.createObjectAdapter("ClientAdapter");

            // Crear la implementación del cliente
            ClientImp clientImp = new ClientImp("Client1");

            // Agregar la implementación al adapter
            ObjectPrx objectPrx = adapter.add(clientImp, Util.stringToIdentity("client"));
            this.myPrx = ClientPrx.checkedCast(objectPrx);
            adapter.activate();

            // Conectar con el maestro a través del proxy
            MasterPrx master = MasterPrx.checkedCast(
                    communicator.propertyToProxy("Master.Proxy"));

            if (master == null) {
                throw new Error("Invalid proxy for Master");
            }

            //System.out.println("CONECTED TO MASTER");

            // Show menu while still in program
            while (true) {
                displayMenu();
                String option = reader.readLine();

                switch (option) {
                    case "1":
                        // Send number of point to calculate pi
                        System.out.println("Please enter the number of points to use in the calculation:");
                        String input = reader.readLine();
                        int numberOfPoints;
                        try {
                            numberOfPoints = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number. Please enter a valid integer.");
                            continue;
                        }

                        // Send request to master
                        master.calculatePi(numberOfPoints, myPrx);

                        System.out.println(
                                "Request sent to Master to calculate Pi using " + numberOfPoints + " points...");
                        break;

                    case "2":

                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (communicator != null) {
                communicator.destroy();
            }
        }
    }

    // Method to display the menu.
    private static void displayMenu() {
        System.out.println("1. Send number of points to calculate Pi");
        System.out.println("2. Exit");
    }
}
