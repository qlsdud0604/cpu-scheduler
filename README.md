# CPU 스케쥴러
## 1. 프로젝트의 목적
* 다양한 스케쥴링 알고리즘을 직접 구현해 봄으로써 각 스케쥴링 기법의 동작과정을 이해한다.
* 각 스케쥴링 알고리즘의 간트차트를 분석하여 특성을 파악한다.
* 구현한 프로그램을 통해 모의실험을 함으로써 각 스케쥴링 알고리즘을 평가해 본다.

-----
## 2. 프로젝트의 내용
* 여러 가지 스케쥴링 기법에 대응하는 벤치마킹 프로그램을 구현한다.
* 구현한 벤치마킹 프로그램을 이용해 모의실험을 진행한다.
* 모의실험을 통해 각각의 스케쥴링 기법을 평가한다.

-----
## 3. 프로세스 정보
```
class process {
	public int processNumber;
	public int arriveTime;
	public int burstTime;

	public int startTime;
	public int finishTime; 

	public int waitingTime;
	public int turnAroundTime;

	process(int processNumber, int arriveTime, int burstTime) {
		this.processNumber = processNumber;
		this.arriveTime = arriveTime;
		this.burstTime = burstTime;
	}
}
```
ㆍ CPU에게 할당을 할 프로세스를 정의한 클래스이다.   
ㆍ 프로세스가 가진 정보를 클래스를 통해 구현하였다.   
ㆍ 콘솔을 통해 사용자로부터 프로세스에 대한 정보를 입력받는다.    
ㆍ 각 스케쥴링 알고리즘은 위와 같은 프로세스의 정보를 가지고 동작을 한다.   

-----
## 4. FCFS 스케쥴링
**1) 스케쥴링 알고리즘의 구현**
```
public void sortByArriveTime(process[] processes) {
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
}
```
ㆍ 프로세스들을 도착시간 기준으로 오름차순 정렬을 해주는 메소드이다.   
```
public void findStartTime(process[] processes) {
	processes[0].startTime = processes[0].arriveTime;
	for (int i = 1; i < numOfProcesses; i++) {
		processes[i].startTime = processes[i - 1].startTime + processes[i - 1].burstTime;
	}
}
```
ㆍ 도착시간을 기준으로 오름차순 정렬을 한 프로세스의 배열을 이용해 간트 차트 내에서 시작시간을 계산한다.   
```
public void findFinishTime(process[] processes) {
	processes[0].finishTime = processes[0].startTime + processes[0].burstTime;
	for (int i = 1; i < numOfProcesses; i++) {
		processes[i].finishTime = processes[i - 1].finishTime + processes[i].burstTime;
	}
}
```
ㆍ 도착시간을 기준으로 오름차순 정렬을 한 프로세스의 배열을 이용해 간트 차트 내에서 종료시간을 계산한다.   

**2) 간트 차트의 표현**
```
public void paint(Graphics g) {
	super.paint(g);
	this.getContentPane().setBackground(Color.white);

	sortByArriveTime(this.processes);
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
```
ㆍ 각 스케쥴링 알고리즘은 JFrame 클래스를 상속받아 paint() 메소드를 오버라이딩한 후 간트 차트를 출력하는 형식을 취하고 있다.   
ㆍ 사용자로부터 입력받은 프로세스의 배열을 sortByArriveTime() 메소드를 이용해 정렬을 한 후, findStartTime(), findFinishTime() 메소드를 통해 간트 차트내에서 시작시간과 종료시간을 구한다.   
ㆍ 위 메소드를 통해서 구한 각 프로세스의 정보를 가지고 drawRect() 메소드를 이용하여 각 프로세스의 버스트시간에 따른 사각형의 크기를 조정하고, 그에 따른 프로세스별 시작시간과 종료시간을 drawString() 메소드를 통해 표시한다.   

**3) 실행 결과**

<img src="https://user-images.githubusercontent.com/61148914/89640263-2ef55b80-d8ea-11ea-9fd1-bc9044ff7b4b.JPG" width="45%">

ㆍ 사용자로부터 5개의 프로세스에 대해 도착시간과 버스트시간을 입력받은 후의 FCFS 스케쥴링 알고리즘을 실행한 결과이다.   
ㆍ 프로세스별 대기시간과 반환시간 등의 정보가 잘 출력되고 있음을 알 수 있다.   

<img src="https://user-images.githubusercontent.com/61148914/89640341-551afb80-d8ea-11ea-841b-5cf6577aba0c.JPG" width="45%">

ㆍ 간트 차트 또한 정확하게 출력되고 있다.   

-----
## 5. Non-Preemptive SJF 스케쥴링
**1) 스케쥴링 알고리즘의 구현**
```
public void sortByBurstTime(process[] processes) {

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
```
ㆍ 프로세스들을 버스트 시간 기준으로 오름차순 정렬을 하는 알고리즘이다.   
ㆍ 도착시간이 제일 빠른 프로세스가 배열의 0번째 자리에 위치한다.   
ㆍ 나머지 프로세스들을 버스트시간 기준으로 오름차순 정렬을 하고, 전체 버스트시간을 늘려나가면서 전체 버스트시간 내에 도착하지 않은 프로세스의 위치를 변경하는 형식의 알고리즘이다.   
```
public void findStartTime(process[] processes) {
	processes[0].startTime = processes[0].arriveTime;
	for (int i = 1; i < numOfProcesses; i++) {
		processes[i].startTime = processes[i - 1].startTime + processes[i - 1].burstTime;
	}
}
```
ㆍ sortBurstTime() 메소드를 이용해 프로세스 배열을 정렬한 후 간트 차트 내에서 프로세스별 시작시간을 구하는 알고리즘이다.   
```
public void findFinishTime(process[] processes) {
	processes[0].finishTime = processes[0].startTime + processes[0].burstTime;
	for (int i = 1; i < numOfProcesses; i++) {
		processes[i].finishTime = processes[i - 1].finishTime + processes[i].burstTime;
	}
}
```
ㆍsetStartTime() 메소드와 마찬가지로 sortBurstTime() 메소드를 이용해 프로세스 배열을 정렬한 후 간트 차트 내에서 프로세스별 시작시간을 구하는 알고리즘이다.   

**2) 간트 차트의 표현**
```
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
```
ㆍ 간트 차트 알고리즘의 구현 방식은 FCFS Scheduling의 방식과 유사하다.   
ㆍ sortByBurstTime(), findStartTime(), findFinishTime() 메소드를 이용해 간트 차트에서 그려질 각 프로세스의 정보를 구한다.   
ㆍ drawRect() 메소드를 이용하여 각 프로세스의 버스트시간에 따른 사각형의 크기를 조정하고, 그에 따른 프로세스별 시작시간과 종료시간을 drawString() 메소드를 통해 표시한다.   

**3) 실행 결과**

<img src="https://user-images.githubusercontent.com/61148914/89640457-94e1e300-d8ea-11ea-9f59-a5f0188c6fc3.JPG" width="45%">

ㆍ 사용자로부터 5개의 프로세스에 대해 도착시간과 버스트시간을 입력받은 후의 Non-Preemptive SJF 스케쥴링 알고리즘을 실행한 결과이다.   
ㆍ 프로세스별 대기시간과 반환시간 등의 정보가 잘 출력되고 있음을 알 수 있다.   

<img src="https://user-images.githubusercontent.com/61148914/89640458-957a7980-d8ea-11ea-8341-4336924e24b3.JPG" width="45%">

ㆍ 간트 차트 또한 정확하게 출력되고 있다.

-----
## 6. Preemptive SJF 스케쥴링
**1) 스케쥴링 알고리즘의 구현**
```
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

	while (complete != numOfProcesses) {

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

		burstTimeArr[shortestBurstIndex]--;

		shortestBurstProcess = burstTimeArr[shortestBurstIndex];
		if (shortestBurstProcess == 0) {
			shortestBurstProcess = Integer.MAX_VALUE;
		}

		if (burstTimeArr[shortestBurstIndex] == 0) {

			complete++;
			check = false;

			finishTime = currentTime + 1;

			processes[shortestBurstIndex].waitingTime = finishTime - processes[shortestBurstIndex].burstTime
					- processes[shortestBurstIndex].arriveTime;

			if (processes[shortestBurstIndex].waitingTime < 0)
				processes[shortestBurstIndex].waitingTime = 0;
		}

		currentTime++;
	}
}
```
ㆍ Preemptive SJF Scheduling의 대기시간을 구하는 알고리즘이다.   
ㆍ  각 프로세스의 대기시간을 구하는 알고리즘은 프로세스들의 총 실행시간만큼 반복된다.   
ㆍ  모든 프로세스들 중에서 남아있는 버스트 시간이 가장 짧은 프로세스를 우선 찾고, 해당 프로세스의 버스트 시간을 1씩 줄여나가며 반복하는 형식이다.   
ㆍ  프로세스의 버스트 시간이 끝나면 종료시간에서 프로세스의 버스트 시간과 프로세스의 도착시간을 빼서 해당 프로세스의 대기시간을 구한다.   
```
public void findTurnAroundTime(process processes[], int numOfprocesses) {
	for (int i = 0; i < numOfprocesses; i++)
		processes[i].turnAroundTime = processes[i].burstTime + processes[i].waitingTime;
}
```
ㆍ 각 프로세스의 반환시간을 구하는 메소드이다.   
ㆍ findWaitingTime()메소드를 이용해서 각 프로세스의 대기시간을 구한 후 각 프로세스의 대기시간에서 각 프로세스의 버스트시간의 차이를 반환시간으로 한다.   

**2) 간트 차트의 표현**
```
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
```
ㆍ 프로세스들의 총 실행시간만큼 알고리즘을 실행하며, 각 프로세스의 버스트시간을 1씩 줄여나가면서 진행되는 점에서 대기시간을 구하는 알고리즘과 유사하다.   
ㆍ preMinIndex 변수 와 minIndex 변수를 비교연산자로 비교하여 각 프로세스의 종료시점을 구하고 drawRect() 메소드를 이용하여 각 프로세스의 버스트시간에 따른 사각형의 크기를 조정하고, 그에 따른 프로세스별 시작시간과 종료시간을 drawString() 메소드를 통해 표시한다.   

**3) 실행 결과**

<img src="https://user-images.githubusercontent.com/61148914/89640569-cc508f80-d8ea-11ea-849f-bc8778a40755.JPG" width="45%">

ㆍ 사용자로부터 5개의 프로세스에 대해 도착시간과 버스트시간을 입력받은 후의 Preemptive SJF 스케쥴링 알고리즘을 실행한 결과이다.   
ㆍ 프로세스별 대기시간과 반환시간 등의 정보가 잘 출력되고 있음을 알 수 있다.   

<img src="https://user-images.githubusercontent.com/61148914/89640571-cd81bc80-d8ea-11ea-9a9e-7c8f423f78c8.JPG" width="45%">

ㆍ 간트 차트 또한 정확하게 출력되고 있다.

-----
## 7. Round Roibin 스케쥴링
**1) 스케쥴링 알고리즘의 구현**
```
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
```
ㆍ Round Robin 스케쥴링의 대기시간을 구하는 알고리즘이다.   
ㆍ 우선, burstTimeCopy[] 배열의 각 프로세스의 버스트시간을 복사한다.   
ㆍ burstTimeCopy[]배열의 값이 timeQuantum보다 클 경우 총 진행시간을 timeQuantum만큼 늘리고 burstTimeCopy[] 배열의 해당 값을 timeQuantum만큼 감소 시킨다.   
ㆍ burstTimeCopy[]배열의 값이 timeQuantum보다 작을 경우 해당 프로세스의 대기시간은 총 진행시간에서 해당 프로세스의 현재 남아있는 버스트시간을 감소시켜 대기시간을 구한다.   
```
public void findTurnAroundTime(process[] processes, int nunOfProcesses) {
	for (int i = 0; i < nunOfProcesses; i++)
		processes[i].turnAroundTime = processes[i].burstTime + processes[i].waitingTime;
}
```
ㆍ 각 프로세스의 반환시간을 구하는 메소드이다.   
ㆍ findWaitingTime()메소드를 이용해서 각 프로세스의 대기시간을 구한 후 각 프로세스의 대기시간에서 각 프로세스의 버스트시간의 차이를 반환시간으로 한다.   

**2) 간트 차트의 표현**
```
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
```
ㆍ 간트 차트를 출력하고 있는 알고리즘은 대기시간을 구하는 알고리즘과 매우 유사한 방식으로 구현 하였다.   
ㆍ 특정 프로세스의 남아있는 버스트 시간이 timeQuantum보다 클 경우 timeQuantum만큼 해당 프로세스를 drawRect()메소드를 이용하여 출력하고, 남아있는 버스트 시간이 timeQuantum보다 작을 경우는 해당 프로세스의 남아있는 버스트 시간만 출력하는 방식이다.   

**3) 실행 결과

<img src="https://user-images.githubusercontent.com/61148914/89640635-f6a24d00-d8ea-11ea-8cce-3e81fab338eb.JPG" width="45%">

ㆍ 사용자로부터 5개의 프로세스에 대해 도착시간과 버스트시간을 입력받은 후의 Round Robin 스케쥴링 알고리즘을 실행한 결과이다.   
ㆍ 프로세스별 대기시간과 반환시간 등의 정보가 잘 출력되고 있음을 알 수 있다.   

<img src="https://user-images.githubusercontent.com/61148914/89640637-f7d37a00-d8ea-11ea-9d95-202646a41f53.JPG" width="45%">

ㆍ 간트 차트 또한 정확하게 출력되고 있다.

-----
## 8. 스케쥴링 알고리즘의 테스트 및 결과 분석
**1) 스케쥴링 알고리즘의 테스트**

<img src="https://user-images.githubusercontent.com/61148914/89640031-aa0a4200-d8e9-11ea-9b05-3504a8464d2f.JPG" width="55%">

ㆍ 각 스케쥴링 알고리즘을 공평하게 평가하기 위하여 위 표와 같은 입력 예제를 각 스케쥴링 알고리즘에게 동일하게 주어 테스트를 진행하였다.  
ㆍ 도착시간을 고려하지 않고 구현한 Round Robin 스케쥴링은 형평성을 위해 테스트 과정에서 제외 하였다.   
ㆍ 각 스케쥴링 알고리즘은 평균 대기시간, 평균 반환시간으로 평가를 하였다.    

<img src="https://user-images.githubusercontent.com/61148914/89640746-30735380-d8eb-11ea-994e-cb824e517b23.JPG" width="55%">

ㆍ 4가지 입력 예제에 대해 각 스케쥴링 기법의 벤치마킹 프로그램으로 평균 대기시간을 구하고 표로 작성 하였다.   

<img src="https://user-images.githubusercontent.com/61148914/89640748-310bea00-d8eb-11ea-922c-1906eb67bc1e.JPG" width="55%">

**2) 테스트 결과 분석**

<img src="https://user-images.githubusercontent.com/61148914/89645404-b1831880-d8f4-11ea-950b-8112b775391c.JPG" width="45%">

ㆍ 4가지 입력 예제에 대한 각 스케쥴링 알고리즘의 평균 대기시간을 막대그래프로 표현 해 보았다.   
ㆍ 그래프에서 보이는 것처럼 4가지 입력 예제에 대해 FCFS, Non-Preemptive SJF, Preemptive SJF 순으로 평균 대기시간이 짧아지고 있다는 것을 확인 할 수 있다.   
ㆍ 대기 시간이 스케쥴링 알고리즘에 실질적으로 영향 받는 요소임을 고려한다면, FCFS, Non-Preemptive SJF, Preemptive SJF 순으로 성능이 좋아지고 있다는 것을 확인 할 수가 있다.   

<img src="https://user-images.githubusercontent.com/61148914/89645408-b34cdc00-d8f4-11ea-9d0f-1c861311e652.JPG" width="45%">

ㆍ 4가지 입력 예제에 대해 각 스케쥴링 알고리즘의 실제 평균 반환시간을 막대그래프로 표현 해 보았다.   
ㆍ 그래프에서 보이는 것처럼 4가지 입력 예제에 대해 FCFS, Non-Preemptive SJF, Preemptive SJF 순으로 평균 반환시간이 짧아지고 있다는 것을 확인 할 수 있다.   
ㆍ 평균 반환시간을 기준으로 각각의 스케쥴링 알고리즘을 평가 해본다면,  FCFS, Non-Preemptive SJF, Preemptive SJF 순으로 성능이 좋아지고 있다는 것을 확인 할 수가 있다.   

-----
## 9. 스케쥴링 알고리즘의 분석과 평가
**1) FCFS 스케쥴링**   
ㆍ 각 프로세스는 도착시간에 따라 CPU에 할당되며, 비선점형 정책이기 때문에 CPU의 활용도가 떨어진다는 특성이 있다.   
ㆍ 소요시간이 긴 프로세스가 먼저 도달하여 시간을 잡아먹는 부정적인 현상에 주의해야한다.   
ㆍ 가장 단순한 방식이며, 구현하기가 가장 수월했던 스케쥴링 기법이다.   

**2) Non-Preemptive SJF 스케쥴링**   
ㆍ 버스트 시간이 짧은 프로세스에게 CPU를 먼저 할당하는 방식이다.   
ㆍ FCFS 스케쥴링과 마찬가지로 비선점형 정책을 사용하기 때문에 여전히 CPU의 활용도가 떨어진다는 특성을 가지고 있다.   
ㆍ FCFS 스케쥴링과 비교하여 비교적 빠른 대기시간과 반환시간을 가지고 있다.   

**3) Preemptive SJF 스케쥴링**   
ㆍ 현재 수행중인 프로세스의 남은 버스트 시간보다 더 짧은 버스트 시간을 가진 프로세스가 도착하게 된다면, 현재 수행중인 프로세스에게서 CPU를 빼앗아 새로 도착한 프로세스에게 CPU를 할당하는 방식의 기법이다.   
ㆍ 선점형 정책을 사용하기 때문에 CPU 활용도가 좋으며, 앞서 설명한 두 가지 스케쥴링 기법에 비해 최소한의 평균 대기시간을 제공해주고 있다.   
ㆍ 프로세스의 선점과 같은 여러 상황을 고려해야하기 때문에 구현하기가 가장 까다로웠던 스케쥴링 기법이었다.   

**4) Round Robin Scheduling**   
ㆍ 각 프로세스가 Time Quantum이라 불리는 시간의 양 의해 제한되어 실행된다는 특성을 가진 스케쥴링 기법이다.   
ㆍ Time Quantum의 크기가 너무 작으면 실행시간에 차지하는 오버헤드가 많아져서 전체 실행시간이 느려지는 현상이 발생가능하다. 이 같은 상황을 고려해 적절한 Time Quantum이 필요하다.   
ㆍ 도착시간을 고려하지 않고 구현을 하였기 때문에 구현상 큰 어려움이 없었다. 만약 도착시간을 고려하고 프로그램을 작성하였다면, 큰 어려움이 있었을 꺼라 생각한다.   

-----
## 10. 프로젝트 수행 중 어려웠던 점
* Preemptive SJF 스케쥴링을 구현할 때 버스트 시간이 더 짧은 프로세스가 CPU를 선점하는 경우를 구현하기가 가장 어려웠다.
* 각 스케쥴링 기법에 따라 간트 차트를 출력하는 부분이 구현하기가 어려웠다.

-----
## 11. 프로젝트를 마치고 느낀 점
* 각 스케쥴링 알고리즘을 직접 구현함으로써 각 스케쥴링의 특성을 정확하게 파악할 수 있었고 동작방식을 정확히 이해할 수 있었다.
* 벤치마킹 프로그램을 통해 모의실험을 함으로써 각 스케쥴링 알고리즘의 장단점을 및 성능을 파악할 수 있었다.
