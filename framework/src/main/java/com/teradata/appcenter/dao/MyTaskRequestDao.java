package com.teradata.appcenter.dao;

import java.util.List;

import org.apache.mesos.Protos.TaskState;

import com.teradata.appcenter.entity.MyTaskRequest;

public interface MyTaskRequestDao{
	
	public MyTaskRequest getJobById(String id);

	public MyTaskRequest saveJob(MyTaskRequest Job);
	
	public MyTaskRequest updateJob(MyTaskRequest Job);

	public void deleteJob(MyTaskRequest Job);
	
	public void deleteJob(String id);

	public List<MyTaskRequest> getAllJobs();

	List<MyTaskRequest> getJobs(TaskState state);

	int getCountOfJobs(TaskState state);
	
}
