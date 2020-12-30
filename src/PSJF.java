import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

/** Preemptive SJF Scheduling */
class PSJF extends JFrame {
	Main object;
	int numOfProcesses;

	process[] processes;
	process[] processesCopy;

	PSJF(Main object) {
		super("Preemptive Scheduling");
		this.setVisible(true);
		this.setSize(800, 300);

		this.object = object;
		this.numOfProcesses = object.numOfProcesses;

		processes = new process[this.numOfProcesses];

		for (int i = 0; i < numOfProcesses; i++) {
			this.processes[i] = new process(i + 1, object.processes[i].arriveTime, object.processes[i].burstTime);
		}
	}

	/** method to get total execution time */
	public int getTotalExecutionTime(process[] processes) {
		int sum = 0;
		for (int i = 0; i < numOfProcesses; i++) {
			sum += processes[i].burstTime;
		}
		return sum;
	}

	/** calculate waiting time of each processes */
	public void findWaitingTime(process[] processes, int numOfProcesses) {

		int burstTimeArr[] = new int[numOfProcesses];

		for (int i = 0; i < numOfProcesses; i++) {
			burstTimeArr[i] = processes[i].burstTime;
		}

		int complete = 0;
		int currentTime = 0;
		int shortestBurstProcess = Integer.MAX_VALUE;
		int shortestBurstIndex = 0;
		int finishTime;
		boolean check = false;

		/** process until all processes are completed */
		while (complete != numOfProcesses) {

			/** find process with minimum remaining time */
			for (int i = 0; i < numOfProcesses; i++) {
				if ((processes[i].arriveTime <= currentTime) && (burstTimeArr[i] < shortestBurstProcess)
						&& burstTimeArr[i] > 0) {
					shortestBurstProcess = burstTimeArr[i];
					shortestBurstIndex = i;
					check = true;
				}
			}

			if (check == false) {
				currentTime++;
				continue;
			}

			/** reduce remaining time */
			burstTimeArr[shortestBurstIndex]--;

			/** update process with minimum remaining time */
			shortestBurstProcess = burstTimeArr[shortestBurstIndex];
			if (shortestBurstProcess == 0) {
				shortestBurstProcess = Integer.MAX_VALUE;
			}

			/** when the process execution is complete */
			if (burstTimeArr[shortestBurstIndex] == 0) {

				complete++;
				check = false;

				finishTime = currentTime + 1;

				/** calculate waiting time */
				processes[shortestBurstIndex].waitingTime = finishTime - processes[shortestBurstIndex].burstTime
						- processes[shortestBurstIndex].arriveTime;

				if (processes[shortestBurstIndex].waitingTime < 0)
					processes[shortestBurstIndex].waitingTime = 0;
			}

			currentTime++;
		}
	}

	/** calculate turn around time of each processes */
	public void findTurnAroundTime(process processes[], int numOfprocesses) {

		for (int i = 0; i < numOfprocesses; i++)
			processes[i].turnAroundTime = processes[i].burstTime + processes[i].waitingTime;
	}

	/** display gantt chart */
	public void paint(Graphics g) {
		super.paint(g);
		this.getContentPane().setBackground(Color.white);

		processesCopy = new process[object.numOfProcesses];

		for (int i = 0; i < numOfProcesses; i++) {
			this.processesCopy[i] = new process(i + 1, object.processes[i].arriveTime, object.processes[i].burstTime);
		}

		int currentTime = getShortestArriveTime(processes);
		int min;
		int minIndex = 0;
		int preMinIndex = 0;
		int start = 50;
		int firstProcess = getShortestArriveTimeProcess(processes);

		g.drawString("" + currentTime, 48, 170);
		g.drawString("P" + firstProcess, 52, 140);
		processes[0].startTime = currentTime;

		for (int i = 0; i < getAllBurstTime(object.processes); i++) {

			min = Integer.MAX_VALUE;

			for (int j = 0; j < numOfProcesses; j++) {
				if (processesCopy[j].burstTime < min && processesCopy[j].arriveTime <= currentTime
						&& processesCopy[j].burstTime != 0) {
					min = processesCopy[j].burstTime;
					minIndex = j;
				}
			}

			if (i == 0)

				preMinIndex = minIndex;

			if (preMinIndex != minIndex || i == getAllBurstTime(object.processes) - 1) {

				if (i == getAllBurstTime(object.processes) - 1) {
					g.drawRect(50, 110, 25 * currentTime, 50);
				} else {
					g.drawRect(50, 110, 25 * currentTime, 50);
				}

				if (i == getAllBurstTime(object.processes) - 1) {
					g.drawString("" + (currentTime + 1), (25 * currentTime) + 45, 170);

				} else {
					g.drawString("" + (currentTime), (25 * currentTime) + 45, 170);
					g.drawString("P" + (minIndex + 1), (25 * currentTime) + 52, 140);
				}
			}
			currentTime++;
			processesCopy[minIndex].burstTime--;
			preMinIndex = minIndex;
		}
	}

	/** get the shortest arrival time among the processes */
	public int getShortestArriveTime(process[] processes) {
		for (int i = 0; i < numOfProcesses - 1; i++) {
			int min = i;
			for (int j = i + 1; j < numOfProcesses; j++) {
				if (processes[j].arriveTime < processes[min].arriveTime)
					min = j;
			}
			process temp = processes[i];
			processes[i] = processes[min];
			processes[min] = temp;
		}

		return processes[0].arriveTime;
	}

	/** get the sum of all burst time */
	public int getAllBurstTime(process[] processes) {
		int allBurstTime = 0;

		for (int i = 0; i < numOfProcesses; i++) {
			allBurstTime += processes[i].burstTime;
		}

		return allBurstTime;
	}

	/** get the process with the shortest arrive time */
	public int getShortestArriveTimeProcess(process[] processes) {
		for (int i = 0; i < numOfProcesses - 1; i++) {
			int min = i;
			for (int j = i + 1; j < numOfProcesses; j++) {
				if (processes[j].arriveTime < processes[min].arriveTime)
					min = j;
			}
			process temp = processes[i];
			processes[i] = processes[min];
			processes[min] = temp;
		}
		
		return processes[0].processNumber;
	}
}
