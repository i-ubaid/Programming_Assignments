import java.util.Scanner;

public class Main
{
    public static Scanner scan = new Scanner(System.in);
    public static void main(String[] args)
    {
        int n,m;

        System.out.print("\nEnter no. of Processes : ");
        n = scan.nextInt();
        System.out.print("\nEnter no. of Resources : ");
        m = scan.nextInt();

        int Available[] = new int[m];
        int Max[][] = new int[n][m];
        int Allocation[][] = new int[n][m];
        int Need[][] = new int[n][m];
        int a[] = new int[m];

        // Initializing Allocation and Max here
        for(int i=0;i<n;i++)
        {
        System.out.println(" Process "+ (i+1));
            for(int j=0;j<m;j++)
            {
                System.out.println(" Allocation for resource  "+ (j+1));
                Allocation[i][j] = scan.nextInt();
                System.out.println(" Maximum for resource: "+ (j+1));
                Max[i][j] = scan.nextInt();
            }
        }
        //Initializing Available resources
        System.out.println(" \nAvailable resources : ");
        for(int i=0;i<m;i++)
        {
            System.out.println(" Resource : "+ (i+1));
            Available[i] = scan.nextInt();
        }
        //Initializing Need here
        calculateNeed(Need, Max, Allocation, n, m);

        System.out.println("S\nystem Snapshot at time T = 0  ");
        System.out.print("\nAllocation");
        print(Allocation, n, m);
        System.out.print("\nMax");
        print(Max, n, m);
        System.out.print("\nNeed");
        print(Need, n, m);
        System.out.println("\nAvailable");
        display(Available, m);
        bankingAlgorithm(Available, Allocation, Max, Need, m, n);
    }

    //Safety Algorithm
    public static int safety(int m, int n, int Avail[], int Need[][], int Allocation[][])
    {
        int x=0;
        Boolean finish[] = new Boolean[n];
        int pflag = 0;
        boolean flag = false;// if need > Work
        for (int i =0; i< n; i++)
        {
            finish[i] = false;
        }
        int Work[] = new int[m];
        for (int i =0; i< m; i++)
        {
            Work[i] = Avail[i];
        }

        for(int k=0;k<n;k++)
        {
            for(int i=0;i<n;i++){
                if(finish[i] == false)
                {
                    flag=false;
                    for(int j=0;j<m;j++)
                    {
                        if(Need[i][j] > Work[j])
                            flag=true;
                    }
                    if(flag == false && finish[i] == false)
                    {
                        for(int j=0;j<m;j++) {
                            Work[j] += Allocation[i][j];
                        }
                        finish[i]=true;
                        System.out.println("\nProcess "+(i+1)+" finished, Now Resources are freed.");
                        pflag++;

                    }
                }
            }
            if(pflag == n)//if all processes are finished return 0 for safe state
            {
                return 0;
            }
        }



        return -1;
    }

    //to print 2d Array
    public static void print(int x[][],int n,int m)
    {
        int i,j;
        for(i=0;i<n;i++){
            System.out.println();
            for(j=0;j<m;j++){
                System.out.print(" "+x[i][j]);
            }
        }
    }

    //to print array
    public static void display(int x[], int n)
    {
        int i;
        for(i=0;i<n;i++)
        {
            System.out.print(" "+x[i]);
        }

    }

    //to calculate need
    public static void calculateNeed(int Need[][], int Max[][], int Allocation[][], int n, int m)
    {
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<m;j++)
            {
                Need[i][j] = Max[i][j] - Allocation[i][j];

            }
        }
    }

    //Resource request Algorithm
    public static int res_request(int Allocation[][],int Need[][],int Available[],int pid,int m, int n)
    {
        int req[] = new int[m];

        System.out.println("\n Enter additional request :- ");
        for(int i=0;i<m;i++){
            System.out.println(" Request for resource "+ (i+1));
            req[i] = scan.nextInt();

        }

        for(int i=0;i<m;i++)
            if(req[i] > Need[pid][i]){
                System.out.println("\n Maximum Claim has Exceeded.");
                return -1;
            }

        for(int i=0;i<m;i++)
            if(req[i] > Available[i]){
                System.out.println("\n Resources are unavailable.");
                return -1;
            }

        for(int i=0;i<m;i++){
            Available[i]-=req[i];
            Allocation[pid][i]+=req[i];
            Need[pid][i]-=req[i];
        }
        return 0;
    }

    //Method runs safety and resource request mwthod
    public static void bankingAlgorithm(int Available[], int Allocation[][], int Max[][], int Need[][], int m, int n)
    {

        int ret = safety(m, n, Available, Need, Allocation);
        if (ret == 0) {

            System.out.println("\nSystem is in Safe State.");
            calculateNeed(Need, Max, Allocation, n, m);
            System.out.println("System Snapshot after processessing.");
            System.out.print("\nAllocation");
            print(Allocation, n, m);
            System.out.print("\nMax");
            print(Max, n, m);
            System.out.print("\nNeed");
            print(Need, n, m);
            System.out.println("\nAvailable");
            display(Available, m);

            System.out.println("\n Do you want make an additional request ? (1=Yes|0=No)");
            int ch = scan.nextInt();
            while (ch != 0) {
                if (ch == 1) {
                    System.out.println("\n Enter process no. : ");
                    int pid = scan.nextInt();
                    int output = res_request(Allocation, Need, Available, (pid-1), m, n);
                    if(output != 0)
                    {
                        System.exit(-1);

                    }
                    int res = safety(m, n, Available, Need, Allocation);
                    if (res == 0) {
                        System.out.println("\nSystem is in Safe State.");
                        calculateNeed(Need, Max, Allocation, n, m);
                        System.out.println("System Snapshot after processessing.");
                        System.out.print("\nAllocation");
                        print(Allocation, n, m);
                        System.out.print("\nMax");
                        print(Max, n, m);
                        System.out.print("\nNeed");
                        print(Need, n, m);
                        System.out.println("\nAvailable");
                        display(Available, m);

                        System.out.println("\n Do you want make an additional request ? (1=Yes|0=No)");
                        ch = scan.nextInt();
                    }
                    else
                    {
                        System.out.println("\nDeadlock has occured.");
                        System.exit(-1);
                    }
                }
            }
        }
        else{
            System.out.println("\nDeadlock has occured.");
            System.exit(-1);
        }
    }
}
