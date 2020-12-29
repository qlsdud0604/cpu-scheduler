/** information of process */
class process {
	public int processNumber; // number of process
	public int arriveTime; // arrive time of process
	public int burstTime; // burst time of process

	public int startTime; // start time of process in gantt chart
	public int finishTime; // finish time of process in gantt chart

	public int waitingTime; // waiting time of process
	public int turnAroundTime; // turn around time of process

	process(int processNumber, int arriveTime, int burstTime) {
		this.processNumber = processNumber;
		this.arriveTime = arriveTime;
		this.burstTime = burstTime;
	}
}