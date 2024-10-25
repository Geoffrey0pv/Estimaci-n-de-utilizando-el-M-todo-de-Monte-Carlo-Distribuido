package implementations;

import com.zeroc.Ice.Current;
import Contract.Client;

public class ClientImp implements Client {

    private String clientName;

    public ClientImp(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public void masterNotifiedItsDone(double piValue, Current current) {
        System.out.println("Client [" + clientName + "] has been notified with Pi value: " + piValue);
    }
}
