package load_balance;

import java.util.*;

public class LoadBalanceImpl implements LoadBalance{

    public List<String> servers = new ArrayList<>();
    public Map<String, Integer> servers2Weight = new HashMap<>();
    public static int pos = 0;

    public String random(){
        Random random = new Random(System.currentTimeMillis());
        int index = random.nextInt(servers.size());
        return servers.get(index);
    }

    public String RoundRobin(){
        if (pos >= servers.size())
            pos = 0;
        return servers.get(pos++);
    }

    /**             总数 = 7
     * a    b   c           a   b   c
     * 4    2   1    a      -3  2   1
     * 1    4   2    b       1  -3  2
     * 5    -1  3    a      -2  -1  3
     * 2    1   4    c       2   1  -3
     * 6    3   -2   a       -1  3  -2
     * 3    5   -1  b       3   -2  -1
     * 7    0   0   a       0   0   0
     *
     * 首先初始化初始的权重，计算所有权重的和
     * 得到一个当前的权重列表，初始化为初始权重
     * 从当前的权重列表里选择一个权重最大的服务器，更新选择之后的当前权重。被选定的服务器：current - total + initial； ，没被选定的：current + initial
     * 更新完了之后，进行下一轮，直到当前权重全部为0
     *
     * 然后重新进行上面所有的操作
     */
    public String RoundRobinWithWeight(){
        return "";
    }
}
