package com.teradata.appcenter.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.teradata.appcenter.entity.Schedule;

@Repository("scheduleDao")
public class ScheduleDaoImpl extends AbstractDaoImpl<Schedule, String> implements ScheduleDao{

	@Override
	public Schedule getScheduleById(String id) {
		return getById(id);
	}

	@Override
	public Schedule saveSchedule(Schedule schedule) {
		return saveOrUpdate(schedule);
	}
	
	@Override
	public Schedule updateSchedule(Schedule schedule) {
		return saveOrUpdate(schedule);
	}

	@Override
	public void deleteSchedule(Schedule schedule) {
		delete(schedule);
	}
	
	@Override
	public void deleteSchedule(String id) {
		delete(id);
	}

	@Override
	public List<Schedule> getAllSchedules() {
		return getAll();
	}
	
}
