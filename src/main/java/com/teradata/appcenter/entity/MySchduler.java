package com.teradata.appcenter.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mesos.Protos.CommandInfo;
import org.apache.mesos.Protos.ContainerInfo;
import org.apache.mesos.Protos.ContainerInfo.DockerInfo;
import org.apache.mesos.Protos.ContainerInfo.Type;
import org.apache.mesos.Protos.ExecutorID;
import org.apache.mesos.Protos.FrameworkID;
import org.apache.mesos.Protos.MasterInfo;
import org.apache.mesos.Protos.Offer;
import org.apache.mesos.Protos.OfferID;
import org.apache.mesos.Protos.Resource;
import org.apache.mesos.Protos.SlaveID;
import org.apache.mesos.Protos.TaskID;
import org.apache.mesos.Protos.TaskInfo;
import org.apache.mesos.Protos.TaskState;
import org.apache.mesos.Protos.TaskStatus;
import org.apache.mesos.Protos.Value;
import org.apache.mesos.Scheduler;
import org.apache.mesos.SchedulerDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.netflix.fenzo.SchedulingResult;
import com.netflix.fenzo.TaskAssignmentResult;
import com.netflix.fenzo.TaskScheduler;
import com.netflix.fenzo.VMAssignmentResult;
import com.netflix.fenzo.VirtualMachineLease;
import com.netflix.fenzo.functions.Action1;
import com.netflix.fenzo.plugins.VMLeaseObject;
import com.teradata.appcenter.dao.MyTaskRequestDao;

@Component
public class MySchduler implements Scheduler{
	Gson gson  = new Gson();
	private SchedulerDriver driver;
	private TaskScheduler scheduler;
	
	@Autowired
	private MyTaskRequestDao dao;
	
	private MyTaskRequest createTaskRequest(final Task task){
		//persist task sort of WAL
		MyTaskRequest request = new MyTaskRequest();
		request.setCPUs(task.getCpu());
		request.setImage(task.getImage());
		request.setMemory(task.getCpu());
		return request;	
	}
	
	public MyTaskRequest  addTaskRequest(Task task){
		System.out.println("Controller receives a task request = " +  task.getImage());
		MyTaskRequest taskRequest = createTaskRequest(task);
		dao.saveJob(taskRequest);
		System.out.println( "Adding task to task list") ;
		return taskRequest;
	}

	@Override
	public void disconnected(SchedulerDriver arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(SchedulerDriver arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void executorLost(SchedulerDriver arg0, ExecutorID arg1, SlaveID arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void frameworkMessage(SchedulerDriver arg0, ExecutorID arg1, SlaveID arg2, byte[] arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void offerRescinded(SchedulerDriver arg0, OfferID arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registered(SchedulerDriver drv, FrameworkID arg1, MasterInfo masterInfo) {
		System.out.println("Register to master " + masterInfo.toString()) ;
		scheduler = new TaskScheduler.Builder()
				.withLeaseOfferExpirySecs(1000000000)
				.withLeaseRejectAction(new Action1<VirtualMachineLease>() {
					@Override
					public void call(VirtualMachineLease lease) {
						drv.declineOffer(lease.getOffer().getId());

					}
				})
				.build();
		driver=drv;
	}

	@Override
	public void reregistered(SchedulerDriver arg0, MasterInfo masterInfo) {
		System.out.println("Re-registered to master " + masterInfo.toString()) ;

	}

	@Override
	public void resourceOffers(SchedulerDriver driver, List<Offer> offers) {
		System.out.println("Received offers from master") ;
		int not = dao.getCountOfJobs(null);
		System.out.println("Number of tasks in staged task list = " + not);
		if(not == 0){
			offers.forEach((offer) -> driver.declineOffer(offer.getId()));
		}else{
			processOffers(driver, offers);
		}
	}

	private void processOffers(SchedulerDriver driver, List<Offer> offers) {
		List<VirtualMachineLease> leasesQueue =  new ArrayList<>();;
		for(Offer offer: offers) {
			System.out.println("Adding offer " + offer.getId() + " from host " + offer.getHostname());
			leasesQueue.add(new VMLeaseObject(offer));
		}

		SchedulingResult scheduledResult = scheduler.scheduleOnce(dao.getJobs(null), leasesQueue);

		Map<String,VMAssignmentResult> resultMap = scheduledResult.getResultMap();
		if(!resultMap.isEmpty()) {
			for(VMAssignmentResult result: resultMap.values()) {
				List<VirtualMachineLease> leasesUsed = result.getLeasesUsed();
				List<TaskInfo> taskInfos = new ArrayList<>();
				StringBuilder stringBuilder = new StringBuilder("Launching on VM " + leasesUsed.get(0).hostname() + " tasks ");
				final SlaveID slaveId = leasesUsed.get(0).getOffer().getSlaveId();
				for(TaskAssignmentResult t: result.getTasksAssigned()) {
					processTaskAssignmentResult(taskInfos, stringBuilder, slaveId, t);
				}
				List<OfferID> offerIDs = new ArrayList<>();
				for(VirtualMachineLease l: leasesUsed)
					offerIDs.add(l.getOffer().getId());
				System.out.println(stringBuilder.toString());
				driver.launchTasks(offerIDs, taskInfos);
			}
		}
	}

	private void processTaskAssignmentResult(List<TaskInfo> taskInfos, StringBuilder stringBuilder,
			final SlaveID slaveId, TaskAssignmentResult t) {
		stringBuilder.append(t.getTaskId()).append(", ");
		MyTaskRequest task = dao.getJobById(t.getTaskId());
		TaskInfo taskInfo = getTaskInfo(slaveId, task);
		taskInfos.add(taskInfo);
	}

	private TaskInfo getTaskInfo(final SlaveID slaveId, MyTaskRequest task) {
		DockerInfo.Builder dockerInfoBuilder = DockerInfo.newBuilder();
		dockerInfoBuilder.setImage(task.getImage());
		dockerInfoBuilder.setNetwork(DockerInfo.Network.BRIDGE);
		// container info
		ContainerInfo.Builder containerInfoBuilder = ContainerInfo.newBuilder();
		containerInfoBuilder.setType(Type.DOCKER);
		containerInfoBuilder.setDocker(dockerInfoBuilder.build());
		TaskID taskId = TaskID.newBuilder()
				.setValue(task.getId()).build();
		TaskInfo taskInfo = TaskInfo.newBuilder()
				.setName("task " + task.getId())
				.setTaskId(taskId)
				.setSlaveId(slaveId)
				.addResources(Resource.newBuilder()
						.setName("cpus")
						.setType(Value.Type.SCALAR)
						.setScalar(Value.Scalar.newBuilder().setValue(task.getCPUs())))
				.addResources(Resource.newBuilder()
						.setName("mem")
						.setType(Value.Type.SCALAR)
						.setScalar(Value.Scalar.newBuilder().setValue(task.getMemory())))
				.setContainer(containerInfoBuilder)
				.setCommand(CommandInfo.newBuilder().setShell(false))
				.build();
		return taskInfo;
	}


	@Override
	public void slaveLost(SchedulerDriver arg0, SlaveID arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void statusUpdate(SchedulerDriver arg0, TaskStatus status) {
		String timestamp = status.hasTimestamp() ? Instant.ofEpochMilli(new Double(status.getTimestamp() * 1000).longValue()).toString(): Instant.now().toString();
		String taskId = status.getTaskId().getValue();
		System.out.println("Got status update for " + taskId + " it has timestamp = " + status.hasTimestamp());
		TaskState state = status.getState();
		MyTaskRequest myTaskRequest = dao.getJobById(taskId);
		switch (state) {
		case TASK_ERROR:
		case TASK_FAILED:
		case TASK_FINISHED:
		case TASK_KILLED:
			myTaskRequest.setEndTime(timestamp);
			break;
		case TASK_LOST:
			break;
		case TASK_STAGING:
			break;
		case TASK_RUNNING:
		case TASK_STARTING: //starting is for custom executors
			myTaskRequest.setStartTime(timestamp);
			break;
		}
		myTaskRequest.setState(state);
		dao.saveJob(myTaskRequest);
	}

	public void deleteTask(String taskIdStr) {
		TaskID taskId = TaskID.newBuilder().setValue(taskIdStr).build();
		MyTaskRequest myTaskRequest = dao.getJobById(taskIdStr);
		if(myTaskRequest != null){
			if(myTaskRequest.getState() == null){
				dao.deleteJob(taskIdStr);
			}else if (myTaskRequest.getState().equals(TaskState.TASK_STAGING) || myTaskRequest.getState().equals(TaskState.TASK_RUNNING)){
				driver.killTask(taskId);
			}
		}
	}

	public MyTaskRequest getTaskStatus(String taskId) {
		return dao.getJobById(taskId);
	}

}
