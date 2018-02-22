package com.teradata.appcenter.dao;

import java.util.List;

import com.teradata.appcenter.entity.Schedule;

public interface ScheduleDao{
	
	public Schedule getScheduleById(String id);

	public Schedule saveSchedule(Schedule schedule);
	
	public Schedule updateSchedule(Schedule schedule);

	public void deleteSchedule(Schedule schedule);
	
	public void deleteSchedule(String id);

	public List<Schedule> getAllSchedules();

}
