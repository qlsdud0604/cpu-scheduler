import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

/** Round Robin Scheduling */
class RR extends JFrame {
	Main object;
	int numOfProcesses;
	int timeQuantum;

	process[] processes;

	RR(Main object) {
		super("Round Robin Scheduling");
		this.setVisible(true);
		this.setSize(800, 300);

		this.object = object;
		this.numOfProcesses = object.numOfProcesses;
		this.timeQuantum = object.timeQuantum;

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
	public void findWaitingTime(process[] processes, int numOfProcesses, int timeQuantum) {
		int burstTimeCopy[] = new int[numOfProcesses];

		for (int i = 0; i < numOfProcesses; i++) {
			burstTimeCopy[i] = processes[i].burstTime;
		}

		int currentTime = 0;

		while (true) {
			boolean check = true;

			for (int i = 0; i < numOfProcesses; i++) {
				if (burstTimeCopy[i] > 0) {
					check = false;

					if (burstTimeCopy[i] > timeQuantum) {
						currentTime += timeQuantum;
						burstTimeCopy[i] -= timeQuantum;
					} else {
						currentTime += burstTimeCopy[i];
						processes[i].waitingTime = currentTime - processes[i].burstTime;
						burstTimeCopy[i] = 0;
					}

				}

			}
			if (check == true)
				break;
		}
	}

	/** calculate turn around time of each processes */
	public void findTurnAroundTime(process[] processes, int nunOfProcesses) {
		for (int i = 0; i < nunOfProcesses; i++)
			processes[i].turnAroundTime = processes[i].burstTime + processes[i].waitingTime;
	}

	/** display gantt chart */
	public void paint(Graphics g) {
		super.paint(g);
		this.getContentPane().setBackground(Color.white);

		int start = 50;
		int currentTime = 0;
		int time = 0;

		int burstTimeCopy[] = new int[numOfProcesses];

		for (int i = 0; i < numOfProcesses; i++) {
			burstTimeCopy[i] = processes[i].burstTime;
		}

		g.drawRect(start, 110, 20 * timeQuantum, 50);
		g.drawString("" + processes[0].arriveTime, 48, 170);

		while (true) {
			boolean check = true;

			for (int i = 0; i < numOfProcesses; i++) {
				if (burstTimeCopy[i] > 0) {
					check = false;

					if (burstTimeCopy[i] > timeQuantum) {
						g.drawString("P" + processes[i].processNumber, 20 * time + 52, 140);
						time += timeQuantum;
						burstTimeCopy[i] -= timeQuantum;

						g.drawRect(start, 110, 20 * time, 50);
						g.drawString("" + time, 45 + 20 * time, 170);

					} else {
						g.drawString("P" + processes[i].processNumber, 20 * time + 52, 140);
						time += burstTimeCopy[i];
						processes[i].waitingTime = time - processes[i].burstTime;
						burstTimeCopy[i] = 0;

						g.drawRect(start, 110, 20 * time, 50);
						g.drawString("" + time, 45 + 20 * time, 170);
					}

				}

			}
			if (check == true)
				break;
		}
	}
}
