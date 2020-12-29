import java.util.Scanner;

/** Main Class */
public class Main {
	static Main object; // object of class

	static int numOfProcesses; // number of processes
	static process[] processes; // array of processes

	static int timeQuantum; // time quantum of scheduling

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter the number of processes : ");
		numOfProcesses = scanner.nextInt();

		processes = new process[numOfProcesses];

		/** enter process information */
		for (int i = 0; i < numOfProcesses; i++) {
			System.out.println("-----------------------------------");
			System.out.println("Enter the information of process" + (i + 1));
			System.out.println("-----------------------------------");
			System.out.print("Enter the Arrive Time : ");
			int arriveTime = scanner.nextInt(); // put arrive time
			System.out.print("Enter the Burst Time : ");
			int burstTime = scanner.nextInt(); // put burst time

			processes[i] = new process(i + 1, arriveTime, burstTime);

		}
		schedulingAlgorithm();

	}

	public static void schedulingAlgorithm() {
		Scanner scanner = new Scanner(System.in);

		System.out.println();
		System.out.println("There are some Scheduling Algorithm.");
		System.out.println("1. FCFS Scheduling");
		System.out.println("2. Non-Preemptive SJF Scheduling");
		System.out.println("3. Preemptive SJF Scheduling");
		System.out.println("4. Round Robin Scheduing (『Do not consider Arrive Time『)");
		System.out.println("5. Finish the Program");

		int choice;

		/** select scheduling algorithm */
		while (true) {
			System.out.println();
			System.out.print("Select Scheduling Algorithm : ");
			choice = scanner.nextInt();

			switch (choice) {
			case 1:
				showDataFCFS();
				break;
			case 2:
				showDataNonPreemptiveSJF();
				break;
			case 3:
				showDataPreemptiveSJF();
				break;
			case 4:
				System.out.println();
				System.out.print("Enter Time Quantum : ");
				timeQuantum = scanner.nextInt();
				showDataRoundRobin();
				break;
			case 5:
				System.out.println("The program has ended.");
				return;
			default:
				System.out.println("Please enter correctly");

			}

		}

	}

	// display information of a process that has been FCFS scheduled
	public static void showDataFCFS() {
		FCFS fcfs = new FCFS(object);

		fcfs.sortByArriveTime(fcfs.processes);
		fcfs.findStartTime(fcfs.processes);
		fcfs.findFinishTime(fcfs.processes);

		double sumOfWaitingTime = 0;
		double sumOfTurnAroundTime = 0;

		System.out.println();
		System.out.println("---------------------------------------------------------------");
		System.out.println("Process   ArriveTime   BurstTime   WaitingTime   TurnAroundTime");
		for (int i = 1; i <= numOfProcesses; i++) {
			for (int j = 0; j < numOfProcesses; j++) {
				if (i == fcfs.processes[j].processNumber) {
					System.out.print("  " + "P" + processes[j].processNumber);

					System.out.printf("%11d", fcfs.processes[j].arriveTime);

					System.out.printf("%13d", fcfs.processes[j].burstTime);

					System.out.printf("%13d", fcfs.processes[j].startTime - fcfs.processes[j].arriveTime);
					sumOfWaitingTime += fcfs.processes[j].startTime - fcfs.processes[j].arriveTime;

					System.out.printf("%15d\n", fcfs.processes[j].finishTime - fcfs.processes[j].arriveTime);
					sumOfTurnAroundTime += fcfs.processes[j].finishTime - fcfs.processes[j].arriveTime;

				}
			}
		}
		System.out.println("---------------------------------------------------------------");
		System.out.println("Total Execution Time : " + fcfs.getTotalExecutionTime(fcfs.processes));
		System.out.println("---------------------------------------------------------------");
		System.out.println("Average Waiting Time : " + sumOfWaitingTime / numOfProcesses);
		System.out.println("---------------------------------------------------------------");
		System.out.println("Average TurnAround Time : " + sumOfTurnAroundTime / numOfProcesses);
		System.out.println("---------------------------------------------------------------");
		System.out.println();
		System.out.println("『Click the Maximize button to view the Gantt chart.『");
	}

	// display information of a process that has been Non-Preemptive SJF scheduled
	public static void showDataNonPreemptiveSJF() {
		NPSJF npsjf = new NPSJF(object);

		npsjf.sortByBurstTime(npsjf.processes);
		npsjf.findStartTime(npsjf.processes);
		npsjf.findFinishTime(npsjf.processes);

		double sumOfWaitingTime = 0;
		double sumOfTurnAroundTime = 0;

		System.out.println();
		System.out.println("---------------------------------------------------------------");
		System.out.println("Process   ArriveTime   BurstTime   WaitingTime   TurnAroundTime");
		for (int i = 1; i <= numOfProcesses; i++) {
			for (int j = 0; j < numOfProcesses; j++) {
				if (i == npsjf.processes[j].processNumber) {
					System.out.print("  " + "P" + i);

					System.out.printf("%11d", npsjf.processes[j].arriveTime);

					System.out.printf("%13d", npsjf.processes[j].burstTime);

					System.out.printf("%13d", npsjf.processes[j].startTime - npsjf.processes[j].arriveTime);
					sumOfWaitingTime += npsjf.processes[j].startTime - npsjf.processes[j].arriveTime;

					System.out.printf("%15d\n", npsjf.processes[j].finishTime - npsjf.processes[j].arriveTime);
					sumOfTurnAroundTime += npsjf.processes[j].finishTime - npsjf.processes[j].arriveTime;

				}
			}
		}
		System.out.println("---------------------------------------------------------------");
		System.out.println("Total Execution Time : " + npsjf.getTotalExecutionTime(npsjf.processes));
		System.out.println("---------------------------------------------------------------");
		System.out.println("Average Waiting Time : " + sumOfWaitingTime / numOfProcesses);
		System.out.println("---------------------------------------------------------------");
		System.out.println("Average TurnAround Time : " + sumOfTurnAroundTime / numOfProcesses);
		System.out.println("---------------------------------------------------------------");
		System.out.println();
		System.out.println("『Click the Maximize button to view the Gantt chart.『");
	}

	// display information of a process that has been Preemptive SJF scheduled
	public static void showDataPreemptiveSJF() {
		PSJF psjf = new PSJF(object);

		psjf.findWaitingTime(psjf.processes, psjf.numOfProcesses);
		psjf.findTurnAroundTime(psjf.processes, psjf.numOfProcesses);

		double sumOfWaitingTime = 0;
		double sumOfTurnAroundTime = 0;

		System.out.println();
		System.out.println("---------------------------------------------------------------");
		System.out.println("Process   ArriveTime   BurstTime   WaitingTime   TurnAroundTime");
		for (int i = 0; i < numOfProcesses; i++) {
			System.out.print("  " + "P" + (i + 1));
			System.out.printf("%11d", psjf.processes[i].arriveTime);
			System.out.printf("%13d", psjf.processes[i].burstTime);

			System.out.printf("%13d", psjf.processes[i].waitingTime);
			sumOfWaitingTime += psjf.processes[i].waitingTime;

			System.out.printf("%15d\n", psjf.processes[i].turnAroundTime);
			sumOfTurnAroundTime += psjf.processes[i].turnAroundTime;
		}
		System.out.println("---------------------------------------------------------------");
		System.out.println("Total Execution Time : " + psjf.getTotalExecutionTime(psjf.processes));
		System.out.println("---------------------------------------------------------------");
		System.out.println("Average Waiting Time : " + sumOfWaitingTime / numOfProcesses);
		System.out.println("---------------------------------------------------------------");
		System.out.println("Average TurnAround Time : " + sumOfTurnAroundTime / numOfProcesses);
		System.out.println("---------------------------------------------------------------");
		System.out.println();
		System.out.println("『Click the Maximize button to view the Gantt chart.『");

	}

	// display information of a process that has been Round Robin scheduled
	public static void showDataRoundRobin() {
		RR rr = new RR(object);

		rr.findWaitingTime(rr.processes, rr.numOfProcesses, rr.timeQuantum);
		rr.findTurnAroundTime(rr.processes, rr.numOfProcesses);

		double sumOfWaitingTime = 0;
		double sumOfTurnAroundTime = 0;
		double sumOfResponseTime = 0;

		System.out.println();
		System.out.println("--------------------------------------------------");
		System.out.println("Process   BurstTime   WaitingTime   TurnAroundTime");
		for (int i = 0; i < numOfProcesses; i++) {
			System.out.print("  " + "P" + (i + 1));

			System.out.printf("%11d", rr.processes[i].burstTime);

			System.out.printf("%13d", rr.processes[i].waitingTime);
			sumOfWaitingTime += rr.processes[i].waitingTime;

			System.out.printf("%16d\n", rr.processes[i].turnAroundTime);
			sumOfTurnAroundTime += rr.processes[i].turnAroundTime;
		}
		System.out.println("--------------------------------------------------");
		System.out.println("Time Quantum : " + rr.timeQuantum);
		System.out.println("--------------------------------------------------");
		System.out.println("Total Execution Time : " + rr.getTotalExecutionTime(rr.processes));
		System.out.println("--------------------------------------------------");
		System.out.println("Average Waiting Time : " + sumOfWaitingTime / numOfProcesses);
		System.out.println("--------------------------------------------------");
		System.out.println("Average TurnAround Time : " + sumOfTurnAroundTime / numOfProcesses);
		System.out.println("--------------------------------------------------");
		System.out.println();
		System.out.println("『Click the Maximize button to view the Gantt chart.『");
	}
}