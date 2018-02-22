package com.teradata.appcenter.entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.mesos.Protos.TaskState;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.netflix.fenzo.ConstraintEvaluator;
import com.netflix.fenzo.TaskRequest;
import com.netflix.fenzo.VMTaskFitnessCalculator;

@Entity
@Table(name = "task")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class MyTaskRequest implements TaskRequest{
	private double CPUs;
	private double memory;
	private String image;	
	private String id = UUID.randomUUID().toString().replace("-", "");
	private String stagedTime = Instant.now().toString();
	private String startTime;
	private String endTime;
	private TaskState state;
	
	public TaskState getState() {
		return state;
	}

	public double getCPUs() {
		return CPUs;
	}

	public void setCPUs(double cPUs) {
		CPUs = cPUs;
	}

	public double getMemory() {
		return memory;
	}

	public void setMemory(double memory) {
		this.memory = memory;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setState(TaskState state) {
		this.state = state;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStagedTime(){
		return stagedTime;
	}
	
	public void setStagedTime(String stagedTime) {
		this.stagedTime = stagedTime;
	}

	@Override
	@Transient
	public double getDisk() {
		return 10;
	}
	
	@Override
	@Transient
	@JsonIgnore
	public List<? extends ConstraintEvaluator> getHardConstraints() {
		return null;
	}

	@Id
	@Override
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@Override
	@Transient
	@JsonIgnore
	public double getNetworkMbps() {
		return 0;
	}

	@Override
	@Transient
	@JsonIgnore
	public int getPorts() {
		return 0;
	}

	@Override
	@Transient
	@JsonIgnore
	public List<? extends VMTaskFitnessCalculator> getSoftConstraints() {
		return null;
	}

	@Override
	public String taskGroupName() {
		return null;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof MyTaskRequest){
			return getId().equals(((MyTaskRequest) o).getId());
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return getId().hashCode();
	}

}
