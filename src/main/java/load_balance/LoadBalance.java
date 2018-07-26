package load_balance;

public interface LoadBalance {

    String random();
    String RoundRobin();
    String RoundRobinWithWeight();

}
