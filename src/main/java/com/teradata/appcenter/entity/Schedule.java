package com.teradata.appcenter.entity;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "schedule")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Schedule {
	private double CPUs;
	private double memory;
	private String image;	
	private String id = UUID.randomUUID().toString().replace("-", "");
	private String stagedTime = Instant.now().toString();
	private long   nextExecutionTime;
	private String cronSchedule;
	
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

	public String getEndTime() {
		return cronSchedule;
	}

	public void setEndTime(String endTime) {
		this.cronSchedule = endTime;
	}

	public String getStagedTime(){
		return stagedTime;
	}
	
	public void setStagedTime(String stagedTime) {
		this.stagedTime = stagedTime;
	}


	@Id
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof Schedule){
			return getId().equals(((Schedule) o).getId());
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return getId().hashCode();
	}

	public long getNextExecutionTime() {
		return nextExecutionTime;
	}

	public void setNextExecutionTime(long nextExecutionTime) {
		this.nextExecutionTime = nextExecutionTime;
	}

	public String getCronSchedule() {
		return cronSchedule;
	}

	public void setCronSchedule(String cronSchedule) {
		this.cronSchedule = cronSchedule;
	}

}
