import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

/** Non-Preemptive SJF Scheduling */
class NPSJF extends JFrame {
	Main object;
	int numOfProcesses;

	process[] processes;

	NPSJF(Main object) {
		super("Non-Preemptive SJF Scheduling");
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

	/** sort by burst time */
	public void sortByBurstTime(process[] processes) {

		/** sort by arrive time */
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

		/** sort by burst time from index 1 */
		for (int i = 1; i < numOfProcesses - 1; i++) {
			int min = i;
			for (int j = i + 1; j < numOfProcesses; j++) {
				if (processes[j].burstTime < processes[min].burstTime)
					min = j;
			}
			process temp = processes[i];
			processes[i] = processes[min];
			processes[min] = temp;
		}

		/** Change the place of arrive time process within total burst time */
		int totalBurstTime = processes[0].arriveTime + processes[0].burstTime;
		int index = 1;
		while (index < numOfProcesses) {
			for (int i = index; i < numOfProcesses; i++) {
				if (processes[index].arriveTime <= totalBurstTime)
					break;
				if (processes[i].arriveTime <= totalBurstTime) {
					process temp = processes[i];
					processes[i] = processes[index];
					processes[index] = temp;
				}
			}

			for (int i = index + 1; i < numOfProcesses - 1; i++) {
				int min = i;
				for (int j = i + 1; j < numOfProcesses; j++) {
					if (processes[j].burstTime < processes[min].burstTime)
						min = j;
				}
				process temp = processes[i];
				processes[i] = processes[min];
				processes[min] = temp;
			}
			totalBurstTime += processes[index].burstTime;
			index++;
		}

		/** process with a faster arrive time is forwarded */
		for (int i = 0; i < numOfProcesses - 1; i++) {
			for (int j = i + 1; j < numOfProcesses; j++) {
				if (processes[i].burstTime == processes[j].burstTime
						&& processes[j].arriveTime < processes[i].arriveTime) {
					process temp = processes[i];
					processes[i] = processes[j];
					processes[j] = temp;
				}
			}
		}
	}

	/** calculate start time */
	public void findStartTime(process[] processes) {
		processes[0].startTime = processes[0].arriveTime;
		for (int i = 1; i < numOfProcesses; i++) {
			processes[i].startTime = processes[i - 1].startTime + processes[i - 1].burstTime;
		}
	}

	/** calculate finish time */
	public void findFinishTime(process[] processes) {
		processes[0].finishTime = processes[0].startTime + processes[0].burstTime;
		for (int i = 1; i < numOfProcesses; i++) {
			processes[i].finishTime = processes[i - 1].finishTime + processes[i].burstTime;
		}
	}

	/** display gantt chart */
	public void paint(Graphics g) {
		super.paint(g);
		this.getContentPane().setBackground(Color.white);

		sortByBurstTime(this.processes);
		findStartTime(this.processes);
		findFinishTime(this.processes);

		int start = 50;
		int currentTime = processes[0].startTime;

		g.drawString("" + processes[0].startTime, 48, 170);

		for (int i = 0; i < numOfProcesses; i++) {
			g.drawRect(start, 110, 20 * processes[i].burstTime, 50);
			g.drawString("P" + processes[i].processNumber, start + 2, 140);

			start += 20 * processes[i].burstTime;

			currentTime += processes[i].burstTime;
			g.drawString("" + currentTime, start - 5, 170);

		}
	}
}