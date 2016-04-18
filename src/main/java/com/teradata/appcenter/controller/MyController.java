package com.teradata.appcenter.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.teradata.appcenter.entity.MySchduler;
import com.teradata.appcenter.entity.MyTaskRequest;
import com.teradata.appcenter.entity.Task;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
public class MyController {
	@Autowired
	MySchduler schduler;
	@RequestMapping(value ={ "/tasks"}, method = RequestMethod.POST)
	@ApiOperation(value = "Submit a task to scheduler", notes = "Submit a task to scheduler")
	@ApiResponses(value = {@ApiResponse(code = 201, message = "Successfully created an app")})
	public @ResponseBody MyTaskRequest submit(@RequestBody Task task ,
			HttpServletResponse response) throws Exception{
		response.setStatus(HttpServletResponse.SC_CREATED);
		return schduler.addTaskRequest(task);
	}
	
	@RequestMapping(value ={ "/tasks/{task_id}"}, method = RequestMethod.GET)
	@ApiOperation(value = "Get status of a task", notes = "Get status of a task")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully obtained task status")})
	public @ResponseBody MyTaskRequest status(@PathVariable(value="task_id") String taskId ,
			HttpServletResponse response) throws Exception{
		return schduler.getTaskStatus(taskId);
	}
	
	@RequestMapping(value ={ "/tasks/{task_id}"}, method = RequestMethod.DELETE)
	@ApiOperation(value = "Deletes a task", notes = "Deletes a task")
	@ApiResponses(value = {@ApiResponse(code = 204, message = "Successfully deleted a task")})
	public @ResponseBody void delete(@PathVariable(value="task_id") String taskId ,
			HttpServletResponse response) throws Exception{
		schduler.deleteTask(taskId);
	}
	
}
