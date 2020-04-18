import java.util.*;

public class Scheduling
{

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        //Input from user for processes
        int no;
        System.out.println("Please enter the number of processes to schedule");
        no = input.nextInt();

        Process [] processes = new Process[no];

        //To create process objects
        for (int i=0; i<no; i++)
        {
            processes[i]= new Process("p0",0,0,0,0,0);
        }

        //To initialize processes from user input
        for(int i =0; i<no; i++)
        {
            System.out.println("\n");
            System.out.println("Please enter the process id such as p1 or p5 Please make sure to provide unique id for each process");
            String id = input.next();
            processes[i].Pid = id;
            System.out.println("Please enter the Arrival time for "+processes[i].Pid) ;
            int a = input.nextInt();
            processes[i].Atime = a;
            System.out.println("Please enter the Burst time for "+processes[i].Pid) ;
            int b = input.nextInt();
            processes[i].Btime = b;

        }

        int time =checkArrivalTime(processes);
        for (int i=0; i<processes.length; i++)
        {
            time = processes[i].Btime+time;
        }

        System.out.println("Process ID\t Arrival_Time\t Burst_Time\t Priority\t Age");
        for (int i=0; i<processes.length; i++)
        {
            System.out.println(processes[i].Pid+"\t\t\t "+processes[i].Atime+"\t\t\t\t "+processes[i].Btime+"\t\t\t "+processes[i].priority+"\t\t\t "+ processes[i].age);

        }

        int at = checkArrivalTime(processes);
        int atime = processes[at].Atime;
        System.out.println("\nProcess Executing at Time "+atime+" is : "+processes[at].Pid+"\n");
        processes[at].Btime--;

        System.out.println("Process ID\t Arrival_Time\t Burst_Time\t Priority\t Age \t\tCompletion_Time");
        for (int i=0; i<processes.length; i++)
        {
            System.out.println(processes[i].Pid+"\t\t\t "+processes[i].Atime+"\t\t\t\t "+processes[i].Btime+"\t\t\t "+processes[i].priority+"\t\t\t "+ processes[i].age+"\t\t\t"+processes[i].cTime);
        }

        if(processes[at].Btime == 0)
        {
            System.out.println("\n\t"+processes[at].Pid+" is completed at :"+at+1);
            processes[at].age = -1;
            processes[at].cTime = at+1;
            processes[at].priority = -1;
        }
        int t =0;
        for(int k = atime+1; k<time*2; k++)
        {
            if(check(processes) == -1 )
            {
                t=k;
                break;

            }
            setPriorityAndAge(processes,k+1);
            int pr = checkPriority(processes);
            if(processes[pr].age == 0 && processes[pr].priority == 0)
            {
                System.out.println("\nNo Process Executing at Time "+k+" Processor is currently at idle \n");
            }
            else{
            System.out.println("\nProcess Executing at Time "+k+" is : "+processes[pr].Pid+"\n");
            processes[pr].Btime--;
            }
            if(processes[pr].Btime == 0)
            {
                System.out.println("\n\t"+processes[pr].Pid+" is completed at :"+(k+1)+"\n");
                processes[pr].age = -1;
                processes[pr].cTime = k+1;
                processes[pr].priority = -1;
            }
            System.out.println("Process ID\t Arrival_Time\t Burst_Time\t Priority\t Age \t\tCompletion_Time");
            for (int i=0; i<no; i++)
            {
                System.out.println(processes[i].Pid+"\t\t\t "+processes[i].Atime+"\t\t\t\t "+processes[i].Btime+"\t\t\t "+processes[i].priority+"\t\t\t "+ processes[i].age+"\t\t\t"+processes[i].cTime);
            }
        }

        double thr = 0;
        double util =0;
        util = (time*100)/t; //  here t is the total time taken to complete processes and time is the total burst time
        System.out.println("\nCPU utilization is : "+util);
        thr = (double) processes.length/t;
        System.out.println("\nThroughput is : "+thr);
        turaroundTime(processes);



    }

    public static int checkArrivalTime(Process p[])
    {
        int at = p[0].Atime;
        int index = 0;
        for(int i=1; i<p.length; i++)
        {
            if(at > p[i].Atime)
            {
                index=i;
                at=p[i].Atime;
            }
        }
        return index;
    }

    public static int checkPriority(Process p[])
    {
        int index =0;
        Arrays.sort(p);
        ArrayList al = new ArrayList<Integer>();
        int pr = p[0].priority;
        al.add(0);
        for (int i=1; i<p.length; i++)
        {
            if(p[i].priority == pr && p[i].age != 0)
            {
                al.add(i);
            }

        }
        if(al.size() > 1)
        {
            index = checkAge(al,p);
            return (int) al.get(index);

        }
        else
        {
            return (int) al.get(0);
        }
    }

    public static int checkAge(ArrayList<Integer> a,Process p[])
    {
        int index = 0;
        int age = p[a.get(0)].age;
        for(int i=1; i<a.size(); i++)
        {
            if(p[a.get(i)].age > age)
            {
                age = p[a.get(i)].age;
                index = i;
            }
        }
        ArrayList al = new ArrayList<Integer>();
        for(int i=0; i<a.size(); i++)
        {
            if(p[a.get(index)].age == p[a.get(i)].age && index != i)
            {
                al.add(i);
            }
        }
        if(al.size() < 1)
        {
            return index;
        }
        else
        {
            int ind = checkBurstTime(al,a,p);
            return ind;
        }
    }

    public static int checkBurstTime(ArrayList<Integer> al, ArrayList<Integer> a, Process p[])
    {
        int index =0;
        int btime = p[a.get(al.get(index))].Btime;
        for(int i=0; i<al.size(); i++)
        {
            if(p[a.get(al.get(index))].Btime >= p[a.get(al.get(i))].Btime && index != i ) {

                    index = i;
                    btime = p[a.get(al.get(i))].Btime;
                }
        }
        return index;
    }

    public static void setPriorityAndAge(Process p[], int time)
    {
        for(int i=0;i<p.length;i++)
        {
            if(p[i].Atime <= time && p[i].Btime != 0)
            {
                p[i].age = (time-p[i].Atime);
            }


            if(p[i].age > 0)
            {
                p[i].priority = p[i].Btime + p[i].age;
            }

        }
    }

    public static int check(Process p[])
    {
        int ind =-1;
        for (int i=0; i<p.length; i++)
        {
            if(p[i].Btime > 0)
            {
                ind++;
            }
        }
        return ind;


    }

    public static void turaroundTime(Process p[])
    {
        int [] a = new int[p.length];
        for(int i=0; i<p.length; i++)
        {
            a[i] = p[i].cTime - p[i].Atime;
            System.out.println("Turaround time for process "+p[i].Pid +" is :"+a[i]);
        }
        int num = 0;
        for (int j=0; j<a.length; j++)
        {
            num += a[j];
        }
        double avg = (double) num/a.length;
        System.out.println("Average Turaround time is :"+avg);
    }
}