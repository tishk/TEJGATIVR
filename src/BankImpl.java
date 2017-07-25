
import ResponseServices.resultFromBank;
import ServiceObjects.Account.FundTransfer;
import ServiceObjects.Other.MonitoringStatus;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


class BankImpl extends UnicastRemoteObject implements Bank{

private static final long serialVersionUID = 1L;
int countOfSuccess=0;
int countOfNoSuccess=0;
	int countOfThreadPoolError=0;
	int countOfTimeout=0;
	int countOf1839=0;
	int count=0;
	int secondInteger=0;
	int lastSecond=0;
	int countInSecond=0;
	int TPS=0;
	String client="?";
	String mac="?";

	String acCode="";
	String startTime=getNowDateTimeWithSeparator();

    BankImpl()throws RemoteException{}
	public  String submitRequest(String params[]){
		resultFromBank res=new resultFromBank(params);
		String result= res.getResultString();
		res=null;
		System.gc();
		return result;
	}
	public  Object submitRequest(Object requestObject) throws RemoteException, SQLException {
		try{
			 client=RemoteServer.getClientHost();

		}catch (ServerNotActiveException e){

		}
        if (requestObject instanceof MonitoringStatus){

			System.out.println("test");
		}

        resultFromBank res=new resultFromBank(requestObject);
		Object obj=res.getResultObject();

		if (obj!=null){

			try{
				res.getActionCodeFromObject(obj);
				acCode=res.getActionCode();
				if (acCode!="4444"){
					System.out.println("request from:"+client+" answered,ActionCode is:"+acCode);
					System.out.println("______________________________________________________________");
				}
				// displayStatus(obj);
			}catch (Exception e){
				// System.out.println("Exception in BankImpl"+e.toString());
			}
		}


		res=null;
		System.gc();

		return  obj;
	}

	public  String getNowTimeWithSeparator() {

		Date Time = new Date();
		SimpleDateFormat DateFormat =
				//new SimpleDateFormat ("hh:mm:ss a");
				new SimpleDateFormat("HH:mm:ss.SSS");
		String Now = DateFormat.format(Time);
		return Now;

	}
	public  String getNowDateTimeWithSeparator() {

		Date Time = new Date();
		SimpleDateFormat DateFormat =
				//new SimpleDateFormat ("hh:mm:ss a");
				new SimpleDateFormat("YYYY:MM:dd:HH:mm:ss.SSS");
		String Now = DateFormat.format(Time);
		return Now;

	}
	public  int    getNowSecond() {

		Date Time = new Date();
		SimpleDateFormat DateFormat =
				//new SimpleDateFormat ("hh:mm:ss a");
				new SimpleDateFormat("ss");
		SimpleDateFormat DateFormat2 =
				new SimpleDateFormat ("mm");
				//new SimpleDateFormat("ss");
		SimpleDateFormat DateFormat3 =
				new SimpleDateFormat ("HH");
		SimpleDateFormat DateFormat4 =
				new SimpleDateFormat ("dd");
		SimpleDateFormat DateFormat5 =
				new SimpleDateFormat ("MM");
		String Now = DateFormat.format(Time);
		String Now2 = DateFormat2.format(Time);
		String Now3 = DateFormat3.format(Time);
		String Now4 = DateFormat4.format(Time);
		String Now5 = DateFormat5.format(Time);

		//String Now1 = DateFormat1.format(Time);
       //   System.out.println("time returned is:"+Now5+Now4+Now3+Now2+Now);
		return Integer.valueOf(Now5+Now4+Now3+Now2+Now);

	}
	public  void   displayStatus(Object obj){
        Object objj=obj;
		if (acCode.equals("0000")){
			countOfSuccess++;
		}else{


			if (acCode.equals("6004")) {
				countOfNoSuccess++;
				countOfThreadPoolError++;
			}else if (acCode.equals("9126")) {
				countOfNoSuccess++;
				countOfTimeout++;
			}else if (acCode.equals("1839")) {
				countOfNoSuccess++;
				countOfTimeout++;
			}

		}
		countInSecond++;
		secondInteger=getNowSecond();
		if (secondInteger>lastSecond) {
			TPS=countInSecond;
			lastSecond=secondInteger;
			countInSecond=0;
		}
		int countOfOtherActionCode=count-countOfSuccess-countOfTimeout-countOfThreadPoolError-countOf1839;
		int dangerousError=countOfTimeout+countOf1839+countOfThreadPoolError;
		System.out.println("____________________________________________________________");
		System.out.println("                                                            ");
		System.out.println("Start Time is                      :"+startTime);
		System.out.println("Request no                         :"+String.valueOf(count++));
		System.out.println("TPS is                             :"+String.valueOf(TPS++));
		System.out.println("From IP                            :"+client);
		System.out.println("In Time                            :"+getNowTimeWithSeparator());
		System.out.println("Last Action Code is                :"+acCode);
		System.out.println("Count Of Action Code (0000) is     :"+String.valueOf(countOfSuccess));
		System.out.println("Count Of ThreadPoolError is        :"+String.valueOf(countOfThreadPoolError));
		System.out.println("Count Of Timeout is                :"+String.valueOf(countOfTimeout));
		System.out.println("Count Of InputeOutpot error is     :"+String.valueOf(countOf1839));
		System.out.println("Count Of Other Action Code  is     :"+String.valueOf(countOfOtherActionCode));
		System.out.println("Percent of Dangerous failed message:% "+String.valueOf((dangerousError*100)/count));
		System.out.println("Percent of timeout message         :% "+String.valueOf((countOfTimeout*100)/count));
		System.out.println("Percent of ThreadPoolError message :% "+String.valueOf((countOfThreadPoolError*100)/count));
		System.out.println("                                                            ");
		System.out.println("____________________________________________________________");
	}
}
