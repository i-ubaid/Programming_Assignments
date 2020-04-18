public class Process implements Comparable
{
    int Atime,Btime,age,priority,cTime;
    String Pid;

    public Process(String id, int A, int B, int p, int a,int c)
    {
        Pid =id;
        Atime = A;
        Btime = B;
        age =a;
        priority = p;
        cTime = c;
    }

    @Override
    public int compareTo(Object o) {
        int pr =((Process) o).priority;
        return pr-this.priority;
    }
}
