package utilities;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class ExtentsReportManager implements ITestListener {

	
	public ExtentSparkReporter sparkReporter;
	public ExtentReports report;
	public ExtentTest test;
	String repName;
	
	public void onStart(ITestContext testContext)
	{
		/*
		SimpleDateFormat df=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		Date dt=new Date();
		String timeStamp=df.format(dt);
		*/
		//or
		
		String timeStamp=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		
		repName="Test-Report-"+timeStamp+".html";
		sparkReporter =new ExtentSparkReporter(".\\reports\\"+repName);
		sparkReporter.config().setDocumentTitle("OpenCart Automation Report");
		sparkReporter.config().setReportName("OpenCart Functional Report");
		sparkReporter.config().setTheme(Theme.DARK);
		
		report=new ExtentReports();
		report.attachReporter(sparkReporter);
		report.setSystemInfo("Applicarion", "Open Cart");
		report.setSystemInfo("Module", "Admin");
		report.setSystemInfo("Sub Module", "Customer");
		report.setSystemInfo("Username", System.getProperty("user.name"));
		report.setSystemInfo("Environment", "QA");
		String os=testContext.getCurrentXmlTest().getParameter("OS");
		report.setSystemInfo("OS", os);
		String browser=testContext.getCurrentXmlTest().getParameter("Browser");
		report.setSystemInfo("Browser", browser);
		List<String> Includedgroups=testContext.getCurrentXmlTest().getIncludedGroups();
		if(!Includedgroups.isEmpty()) {
			report.setSystemInfo("Groups", Includedgroups.toString());
		}
		
		
	}
	public void onTestSuccess(ITestResult result)
	{
		test=report.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());	//to display group in report.
		test.log(Status.PASS,result.getName()+" got successfully executed." );
	}
	
	public void onTestFailure(ITestResult result)
	{
		test=report.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.FAIL, result.getName()+" got failed");
		test.log(Status.INFO, result.getThrowable().getMessage());
		
		try {
		String imgpath=new BaseClass().captureScreenshot(result.getName());
		test.addScreenCaptureFromPath(imgpath);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void onTestSkipped(ITestResult result)
	{
		test=report.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.SKIP, result.getName()+" got skipped");
		test.log(Status.INFO,result.getThrowable().getMessage());
	}
	
	public void onFinish(ITestContext context)
	{
		report.flush();
		String pathOfExtentReport= System.getProperty("user.dir")+"\\reports\\"+repName;
		File extentReport=new File(pathOfExtentReport);
		try
		{
			Desktop.getDesktop().browse(extentReport.toURI());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
//		try {
//			URL url=new URL("file://"+System.getProperty("user.dir")+"\\reports\\"+repName);
//			//create the email message
//			ImageHtmlEmail email=new ImageHtmlEmail();
//			email.setDataSourceResolver(new DataSourceUrlResolver(url));
//			email.setHostName("smtp.googlemail.com");
//			email.setSmtpPort(465);
//			email.setAuthenticator(new DefaultAuthenticator("mudit7saxena@gmail.com", "password"));
//			email.setSSLOnConnect(true);
//			email.setFrom("mudit7saxena@gmail.com");	//sender
//			email.setSubject("Test Results");
//			email.setMsg("Please find Attached report");
//			email.addTo("mudit_saxena@softprodigy.com");	//receiver
//			email.attach(url, "extent report", "please check report");
//			email.send();	//send  the email
//		}
//		
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
		
	}
	
	
} 
