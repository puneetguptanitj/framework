package com.teradata.appcenter.entity;

import javax.annotation.PostConstruct;

import org.apache.mesos.MesosSchedulerDriver;
import org.apache.mesos.Protos.FrameworkID;
import org.apache.mesos.Protos.FrameworkInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MyFramework {
	@Autowired
	MySchduler schduler;
	
	@PostConstruct
	public void started(){
		System.out.println("Framewoek started");
	}
	
	public void init() {
		System.out.println("Checking if scheduler is initialized on framework = " + schduler.toString());
		FrameworkInfo info = FrameworkInfo.newBuilder().setName("My Awesome Framework")
				.setUser("root").setId(FrameworkID.newBuilder().setValue("myframeworkid"))
		.setFailoverTimeout(100000).build();
		//.setCheckpoint(true)
		//.setPrincipal("test").build();
		MesosSchedulerDriver driver = new MesosSchedulerDriver(schduler, info, "10.0.2.15:5050");
		driver.run();
		driver.stop();
	}

}
