package implementations;

import com.zeroc.Ice.Current;
import Contract.Client;

public class ClientImp implements Client {

    @Override
    public void masterNotifiedItsDone(double piValue, Current current) {
        System.out.println("The PI value calculated is " + piValue);
    }
}
