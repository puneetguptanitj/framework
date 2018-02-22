package com.teradata.appcenter.dao;

import java.util.List;

import org.apache.mesos.Protos.TaskState;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.teradata.appcenter.entity.MyTaskRequest;

@Repository("myTaskRequestDao")
public class MyTaskRequestDaoImpl extends AbstractDaoImpl<MyTaskRequest, String> implements MyTaskRequestDao {

	@Override
	public MyTaskRequest getJobById(String id) {
		return getById(id);
	}

	@Override
	public MyTaskRequest saveJob(MyTaskRequest Job) {
		return saveOrUpdate(Job);
	}
	
	@Override
	public MyTaskRequest updateJob(MyTaskRequest Job) {
		return saveOrUpdate(Job);
	}

	@Override
	public void deleteJob(MyTaskRequest Job) {
		delete(Job);
	}
	
	@Override
	public void deleteJob(String id) {
		delete(id);
	}

	@Override
	public List<MyTaskRequest> getAllJobs() {
		return getAll();
	}
	
	private Criteria getCriteriaForState(TaskState state){
		Criteria criteria = getCurrentSession().createCriteria(MyTaskRequest.class);
		if(state == null){
			System.out.println("Setting restriction as empty or null");
			criteria.add(Restrictions.isNull("state"));
		}else{
			criteria.add(Restrictions.eq("state", state));
		}
		return criteria;
	}
	@Override
	public List<MyTaskRequest> getJobs(TaskState state) {
		Criteria criteria = getCriteriaForState(state);
		return criteria.list();
	}
	
	@Override
	public int getCountOfJobs(TaskState state) {
		Criteria criteria = getCriteriaForState(state);
		System.out.println("criteria output as list " + criteria.list());
		criteria.setProjection(Projections.rowCount());
		Object result =  criteria.uniqueResult();
		return (null == result) ? 0 : ((Number) result).intValue();
	}

	
}
