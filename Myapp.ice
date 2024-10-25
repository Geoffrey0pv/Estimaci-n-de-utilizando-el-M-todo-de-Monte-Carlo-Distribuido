module Contract
{
    interface Client
    {
        void masterNotifiedItsDone(double piValue);
    }

    interface Worker
    {
        void throwPointToCalculatePi(int amountOfPointsToThrow);
    }

    interface Master
    {
        void calculatePi(int amountOfPoints, Client* clientPrxCaller); //I need the client prx since you need to report back to someone.
        void reportFromWorkerPiWasCalculated(double amountOfpointsInsideTheCircle, string workerIdentifier);
        string subToMaster(Worker* workerPrxCaller); //This method returns a string that will be identifier of the worker
        void test(string s);
    }
}