import CardSwitchSaba.*;
import CardSwitchSaba.CardSwitch;
import CardSwitchSaba.Old.ReceiveFromSaba0;
import CardSwitchSabaVer7.CardSwitchVer7;
import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.MainMQ;
import Mainchannel.StartMainChannel;
import Mainchannel.messages.StatementListMessage;
import Mainchannel.messages.StatementMessage;
import Mainchannel.sender.SenderException;
import Pin1.StartPin1;
import Pin2.StartPin2;
import ResponseServices.GatewayServices;
import ResponseServices.resultFromBank;
import ServiceObjects.Account.*;
import ServiceObjects.ISO.ISO110;
import ServiceObjects.ISO.ISO430;
import ServiceObjects.Other.BillInfoByTelNumber;
import ServiceObjects.Other.BillPaySayByTelNumber;
import ServiceObjects.Other.LoggerToDB;
import ServiceObjects.Other.ObjectCompare;
import ServiceObjects.Pan.BalanceForCard;
import ServiceObjects.Pan.BillPayByBillIDPan;
import ServiceObjects.Pin.AuthenticatePin1;
import ServiceObjects.Pin.AuthenticatePin2;
import ServiceObjects.SchaduledServices.LoggerScheduler;
import TelSwitchPKG.TelSwitch;
import utils.DataBaseLoggerQueue;
import utils.LoggerSettings;
import utils.PersianDateTime;
import utils.PropertiesUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GatewayRMI{
	private static PersianDateTime PDT = new PersianDateTime();
	private static GatewayServices gatewayServices=new GatewayServices();
    public  static String Mess="";
	public static PropertiesUtils propertiesUtils=new PropertiesUtils();


	public  String  getRequestTime(){
        Date Time=new Date();
        SimpleDateFormat DateFormat =
                //new SimpleDateFormat ("hh:mm:ss a");
                new SimpleDateFormat ("HHmmss");
        String Now=DateFormat.format(Time);
        return Now;
    }
	public GatewayRMI(String args[]) throws Exception {

        liveMode(args);
//        testMode(args);
//		Test_BillPayment();
    }

    private static void liveMode(String args[]) throws ClassNotFoundException, SQLException, IOException, ServerNotActiveException, ExecutionException, InterruptedException {
        if  (StartedRMIServer(args)) {
            new PropertiesUtils();
			if (PropertiesUtils.getUseQueueForLog().equals("1")){
				LoggerSettings.setUseQueueForLogToDataBase(true);
			}
			LoggerScheduler.scheduleQueueCheck();
            DBUtils dbUtils=new DBUtils("MasterDatabase");
			StartMainChannel startMainChannel=new StartMainChannel();
			CardSwitch cardSwitch=new CardSwitch();
			TelSwitch telSwitch = new TelSwitch();
            System.out.println("System Started...");

        }else {
            System.out.println("Error On Start RMI Server : "+Mess);
            return;
        }
    }
    private static void testMode(String args[]) throws InterruptedException, UnsupportedEncodingException, SQLException, ExecutionException, RemoteException {
		if  (StartedRMIServer(args)) {
			new PropertiesUtils();
//			DBUtils dbUtils=new DBUtils("MasterDatabase");
//			StartMainChannel startMainChannel=new StartMainChannel();
//			CardSwitch cardSwitch=new CardSwitch();
//			TelSwitch telSwitch = new TelSwitch();
			System.out.println("System Started...");

		}else {
			System.out.println("Error On Start RMI Server : "+Mess);
			return;
		}
	}

    public static String printToScreen(String S) {
		S = PDT.getShamsiDate() + " " + PDT.GetNowTime() + " --> " + S;

		int i;
		for(i = 0; i < S.length(); ++i) {
			System.out.print("_");
		}

		System.out.println("_");

		for(i = 0; i < S.length() + 1; ++i) {
			System.out.print(" ");
		}

		System.out.println("|");
		System.out.println(S + " |");

		for(i = 0; i < S.length(); ++i) {
			System.out.print("_");
		}

		System.out.println("_|");
		return S;
	}
	private static boolean StartedRMIServer(String params[]) throws RemoteException {
		boolean IsErrorHappened=true;
		Remote r=new BankImpl();

		try{
			Runtime.getRuntime().exec("rmiregistry "+params[1]);
			IsErrorHappened=false;
		}catch (Exception e){
			IsErrorHappened=true;
			 Mess=e.getMessage();
		}

		try{

			LocateRegistry.createRegistry(Integer.valueOf(params[1]));
			IsErrorHappened=false;
		}catch(RemoteException e){
			IsErrorHappened=true;
			try{
				LocateRegistry.getRegistry(Integer.valueOf(params[1]));
				IsErrorHappened=false;

			}catch (Exception ee){
				IsErrorHappened=true;
                Mess=e.getMessage();
			}

		}catch (Exception e) {
			IsErrorHappened=true;
            Mess=e.getMessage();
		}
		try{
			Naming.rebind("rmi://"+params[0]+":"+params[1]+"/Gateway",r);
			IsErrorHappened=false;
		}catch (Exception e){
			IsErrorHappened=true;
            Mess=e.getMessage();
		}
		return !IsErrorHappened;

	}
    private static void CreateListener(){
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future future = executorService.submit(new Runnable() {
			public void run() {
				try {
					new ReceiveFromSaba0();
					// SimpleServer c= new SimpleServer();
					//  c.runServer();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
    private static void CreateListenertelswitch(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    new ReceiveFromSaba0();
                    // SimpleServer c= new SimpleServer();
                    //  c.runServer();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
	private static void CreateRequest(){
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future future = executorService.submit(new Runnable() {
			public void run() {
				try {
					SendToSabaSwitch S= new SendToSabaSwitch("6037991467916494","5228040");
					//S.Test();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private static void Test_Encryption() throws Exception {
		//6273533030000026000
		//  CreateRequest();
		//	CardSwitchSaba.SendToSabaSwitch S= new CardSwitchSaba.SendToSabaSwitch(ConnectToSabaSwitch.socket,"5859831100002046","12345","948834604120","3420685");
		//CardSwitchSaba.SendToSabaSwitch S= new CardSwitchSaba.SendToSabaSwitch("5859831100002616","123456","9488346204120","3620501",1);
		//CardSwitchSaba.SendToSabaSwitch S= new CardSwitchSaba.SendToSabaSwitch("Hot","5859831100002616","123456");
		//CardSwitchSaba.SendToSabaSwitch S= new CardSwitchSaba.SendToSabaSwitch("5859831100002616","123456");
		StringToByteArray("6273533030000026000");
	}
	private static void Test_CardServicesTest(String params[]) throws Exception {
     	Thread.sleep(3000);
        BalanceForCard balanceForCard=new BalanceForCard();
        int i=0;
        while (i++<20) {
            Test_CardServicesTestAsync(params,i);
            //  Thread.sleep(1000);
            // break;
        }
        //new SendToSabaSwitch("1234567891234567","123456");
               // Thread.sleep(3000);
                //new SendToSabaSwitch("6273531403004411000","259100");
		    	//Thread.sleep(3000);
			   //new SendToSabaSwitch("5859831000001247","259100");
            // new SendToSabaSwitch("5859831100000156","11111");
			//Thread.sleep(3000);
           // new SendToSabaSwitch("5859831100000156","11111");
           //Thread.sleep(3000);
           // new SendToSabaSwitch("5859831100000156","12345");
           // Thread.sleep(3000);
            //new SendToSabaSwitch("5859831000001247","259100");
            //new SendToSabaSwitch("5859831100000156","12345");
          //  Thread.sleep(3000);
			// }
		//}
	}
    private static void Test_CardServicesTestAsync(String params[],final int i) throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    BalanceForCard balanceForCard=new BalanceForCard();
                     if (i==0) balanceForCard.setPan("5859831000001247");
                     else if (i==1) balanceForCard.setPan("5859830000000000");
                     else if (i==2) balanceForCard.setPan("5859831000001247");
                     else if (i==3) balanceForCard.setPan("5859831888881247");
                     else if (i==4) balanceForCard.setPan("5859831000001247");
                     else if (i==5) balanceForCard.setPan("5859831333331247");
                     else if (i==6) balanceForCard.setPan("5859831000001247");
                     else if (i==7) balanceForCard.setPan("5859831444441247");
                     else if (i==8) balanceForCard.setPan("5859831000001247");
                     else if (i==9) balanceForCard.setPan("5859831222221247");
                     else if (i==10) balanceForCard.setPan("5859830000000000");
                     else if (i==11) balanceForCard.setPan("5859830000000000");
                     else if (i==12) balanceForCard.setPan("5859831000001247");
                     else if (i==13) balanceForCard.setPan("5859831888881247");
                     else if (i==14) balanceForCard.setPan("5859831000001247");
                     else if (i==15) balanceForCard.setPan("5859831333331247");
                     else if (i==16) balanceForCard.setPan("5859831000001247");
                     else if (i==17) balanceForCard.setPan("5859831444441247");
                     else if (i==18) balanceForCard.setPan("5859831000001247");
                     else if (i==19) balanceForCard.setPan("5859831222221247");
                     else if (i==20) balanceForCard.setPan("5859831222221247");
                    balanceForCard.setPin("123456");
                    System.out.println("Test Number :" + String.valueOf(i));
                    balanceForCard = gatewayServices.cardBalance(balanceForCard);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static void Test_TelSwitch(String params[]) throws InterruptedException, UnsupportedEncodingException, SQLException, RemoteException, ExecutionException {

        Thread.sleep(3000);
        System.out.println("in telswitch test ");
        BillInfoByTelNumber billInfoByTelNumber=new BillInfoByTelNumber();
				GatewayServices gatewayServices=new GatewayServices();
				billInfoByTelNumber.setTelNo("02155880205");
				billInfoByTelNumber.setIsMobile(false);
				billInfoByTelNumber.setPan("5859831000001247");
				billInfoByTelNumber=gatewayServices.telSwitchGetBillData(billInfoByTelNumber);
				System.out.println("Bill Id Is:" + billInfoByTelNumber.getResultFromServer().getBillID());
				System.out.println("Amount Is:" + billInfoByTelNumber.getResultFromServer().getAmount());

	}

	private static void Test_TelSwitchMultiThread(final String params[]) throws RemoteException, ExecutionException, InterruptedException {
		if (StartedRMIServer(params)) {
			TelSwitch telSwitch=new TelSwitch();

			Thread.sleep(3000);
			for (int i = 0; i < 10; i++) {
				ExecutorService executorService = Executors.newSingleThreadExecutor();
				Future future = executorService.submit(new Runnable() {
					public void run() {
						try {
							Test_TelSwitch(params);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (RemoteException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

    private static void Test_TelSwitchtestSayPay(String params[]) throws Exception {
		//if  (StartedRMIServer(params)){

			//TelSwitch telSwitch=new TelSwitch();

			Thread.sleep(3000);
			//while (true){
			/*	BillInfoByTelNumber billInfoByTelNumber=new BillInfoByTelNumber();
				GatewayServices gatewayServices=new GatewayServices();
				billInfoByTelNumber.setTelNo("09125671008");
				billInfoByTelNumber.setIsMobile(true);
				billInfoByTelNumber.setPan("3901510465");
				billInfoByTelNumber=gatewayServices.telSwitchGetBillData(billInfoByTelNumber);
				System.out.println("Bill Id Is:" + billInfoByTelNumber.getResultFromServer().getBillID());
				System.out.println("Amount Is:" + billInfoByTelNumber.getResultFromServer().getAmount());
			    System.out.println("action code is:" + billInfoByTelNumber.getActionCode());
                Thread.sleep(3000);
*/
				BillPaySayByTelNumber billPaySayByTelNumber=new BillPaySayByTelNumber();
				 gatewayServices=new GatewayServices();

				billPaySayByTelNumber.setPan("3901510465");
				billPaySayByTelNumber.setTraceCode("837104");
				billPaySayByTelNumber.setReferenceCode("100038892971");
				billPaySayByTelNumber.setIsMobile(true);
				billPaySayByTelNumber.setBillID("2567100809050");
				billPaySayByTelNumber.setPaymentID("14442807");
				billPaySayByTelNumber.setPayDate("151205");
				billPaySayByTelNumber.setTelNo("09125671008");
				billPaySayByTelNumber.setIsMobile(true);
				billPaySayByTelNumber=gatewayServices.telSwitchSetBillPaymentData(billPaySayByTelNumber);
				System.out.println("ActionCode  Is:" + billPaySayByTelNumber.getActionCode());
				Thread.sleep(100);
				// System.out.println("Amount Is:" + billPaySayByTelNumber.getResultFromServer().getAmount());
			//}


			//new TelSwitchPKG.SendToTelSwitch("2565256",false,"02155880205");

	//	}
    }
	private static void Test_430(String param) throws Exception {
		ISO430 ISO430 =new ISO430();
		ISO430.ProcessReceiveString(param);
	}
	private static void Test_110(String param) throws Exception {
		ISO110 iso110 =new ISO110();
		iso110.ProcessReceiveString(param);
	}
	private static void Test_accountCardNoOfAccount(String params[]) throws RemoteException {
		if  (StartedRMIServer(params)){
            StartMainChannel startMainChannel=new StartMainChannel();
            if (startMainChannel.started){
                try {

                    CardNoOfAccount b=new CardNoOfAccount();
                    b.setAccountNumber("4150645501");

                    //b.setCreditOrDebit();
                    //while (true){
                        //gatewayServices.accountInformation(null);
                        b=gatewayServices.accountGetPan(b);
                        System.out.println("Action Code : "+b.getResultFromChannel().getAction_code());
                        System.out.println("Pan is  Code : "+b.getResultFromChannel().getPan());
                        //System.out.println("BirthDate is : "+b.getResultFromCM().getBirthDate());
                        //System.out.println("FarsiName is : "+b.getResultFromCM().getFarsiName());
                        System.out.println("XML Response is : "+b.getResultFromChannel().getPan());
                        Thread.sleep(5000);
                    //}


                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }

		}
	}
	private static void Test_accountGetCardNoOfAccount(String params[]) throws RemoteException {
		//if  (StartedRMIServer(params)){
		//	MainMQ AccountServices=new MainMQ();
			try {
				//AccountServices.Init();
				AccountNoOfCard b=new AccountNoOfCard();
				b.setCardNo("5859831000001247");

				//b.setCreditOrDebit();
				//while (true){
					//gatewayServices.accountInformation(null);
					b=gatewayServices.accountGetAccountOfPan(b);
					System.out.println("Action Code : "+b.getResultFromChannel().getAction_code());
					//System.out.println("BirthDate is : "+b.getResultFromCM().getBirthDate());
					//System.out.println("FarsiName is : "+b.getResultFromCM().getFarsiName());
					System.out.println("XML Response is : "+b.getResultFromChannel().getFarAccNo());
					Thread.sleep(5000);
				//}


			} catch (Exception e) {
				System.out.println(e.toString());
			}
		//}
	}
    private static void Test_AccountBalance(String params[]) throws RemoteException {
		//if  (StartedRMIServer(params)){
			//MainMQ AccountServices=new MainMQ();
			try {
				//AccountServices.Init();
				BalanceForAccount b=new BalanceForAccount();
				b.setAccountNumber("3435531082");
				//while (true){
					//gatewayServices.accountInformation(null);
					b=gatewayServices.accountBalance(b);
					System.out.println("Action Code : "+b.getResultFromChannel().getAction_code());
					System.out.println("AvailableBalance is : "+b.getResultFromChannel().getAvailableBalance());
					System.out.println("ActualBalance is : "+b.getResultFromChannel().getActualBalance());
					//Thread.sleep(3000);
				//}


			} catch (Exception e) {
				System.out.println(e.toString());
			}
	//	}
	}

	private static void Test_AccountInformation() throws RemoteException {
		//if  (StartedRMIServer(params)){
			//MainMQ AccountServices=new MainMQ();
		MainMQ AccountServices=new MainMQ();
			try {
				AccountServices.Init();
				AccountInformation b=new AccountInformation();
				b.setAccountNumber("4150645501");
				//while (true){
					//gatewayServices.accountInformation(null);
					b=gatewayServices.accountInformation(b);
					System.out.println("Action Code : "+b.getResultFromChannel().getAction_code());
					System.out.println("BirthDate is : "+b.getResultFromChannel().getBirthDate());
					System.out.println("FarsiName is : "+b.getResultFromChannel().getFarsiName());
					System.out.println("XML Response is : "+b.getResultFromChannel().getResponseXml());
//					Thread.sleep(5000);
				//}


			} catch (Exception e) {
				System.out.println(e.toString());
			}
		//}
	}
	private static void Test_foundTransferFollowUp(String params[]) throws RemoteException {
		if  (StartedRMIServer(params)){
			MainMQ AccountServices=new MainMQ();
			try {
				AccountServices.Init();
				FollowUpTransaction b=new FollowUpTransaction();
				b.setSourceAccount("155971983");
				b.setDestinationAccount("4150645501");
				b.setFollowUpCode("123456");
				b.setTransactionDateTime("13860122120102");
				while (true){
					//gatewayServices.accountInformation(null);
					b=gatewayServices.accountFollowUpTransaction(b);
					System.out.println("Action Code : "+b.getResultFromChannel().getAction_code());
					//System.out.println("BirthDate is : "+b.getResultFromCM().getBirthDate());
					//System.out.println("FarsiName is : "+b.getResultFromCM().getFarsiName());
					System.out.println("XML Response is : "+b.getResultFromChannel().getResponseXml());
					Thread.sleep(5000);
				}


			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}
	private static void Test_accountRegistration(String params[]) throws RemoteException {
		if  (StartedRMIServer(params)){
			MainMQ AccountServices=new MainMQ();
			try {
				AccountServices.Init();
				RegisterAccount b=new RegisterAccount();
				b.setAccountNumber("155971991");
				b.setServicesType("1111000000");
				//b.setCreditOrDebit();
				while (true){
					//gatewayServices.accountInformation(null);
					b=gatewayServices.accountRegister(b);
					System.out.println("Action Code : "+b.getResultFromChannel().getAction_code());
					//System.out.println("BirthDate is : "+b.getResultFromCM().getBirthDate());
					//System.out.println("FarsiName is : "+b.getResultFromCM().getFarsiName());
					System.out.println("XML Response is : "+b.getResultFromChannel().getResponseXml());
					Thread.sleep(5000);
				}


			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}
	private static void Test_ChequesStatus(String params[]) throws RemoteException {
		if  (StartedRMIServer(params)){
			MainMQ AccountServices=new MainMQ();
			try {
				AccountServices.Init();
				ChequeStatus b=new ChequeStatus();
				b.setAccountNumber("2904053956");
				b.setChequeNO("937541");
				while (true){
					//gatewayServices.accountInformation(null);
					b=gatewayServices.accountChequeStatus(b);
					System.out.println("Action Code : "+b.getResultFromChannel().getAction_code());
					//System.out.println("BirthDate is : "+b.getResultFromCM().getBirthDate());
					//System.out.println("FarsiName is : "+b.getResultFromCM().getFarsiName());
					System.out.println("XML Response is : "+b.getResultFromChannel().getResponseXml());
					Thread.sleep(5000);
				}


			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}
	private static void Test_BillPaymentValidation(String params[]) throws RemoteException {
		if  (StartedRMIServer(params)){
			MainMQ AccountServices=new MainMQ();
			try {
				AccountServices.Init();
				BillPayByIDAccountValid b=new BillPayByIDAccountValid();
				b.setSourceAccount("155971983");
				b.setAmount("10000");
				b.setBillID("6549685465");
				b.setPaymentID("86969546834");
				while (true){
					//gatewayServices.accountInformation(null);
					b=gatewayServices.accountBillPayByIDValidation(b);
					System.out.println("Action Code : "+b.getResultFromChannel().getAction_code());
					//System.out.println("BirthDate is : "+b.getResultFromCM().getBirthDate());
					//System.out.println("FarsiName is : "+b.getResultFromCM().getFarsiName());
					System.out.println("XML Response is : "+b.getResultFromChannel().getResponseXml());
					Thread.sleep(5000);
				}


			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}
	private static void Test_BillPayment() throws RemoteException {

			MainMQ AccountServices=new MainMQ();
			try {

				AccountServices.Init();
				BillPayByIDAccount b=new BillPayByIDAccount();
				b.setSourceAccount("3435531082");
				b.setAmount("155000");
				b.setBillID("9488346204120");
				b.setPaymentID("15540267");
//				while (true){
					//gatewayServices.accountInformation(null);
					resultFromBank  res=new resultFromBank(b);
					b=(BillPayByIDAccount) res.getResultObject();
					System.out.println("Action Code : "+b.getResultFromChannel().getAction_code());
					//System.out.println("BirthDate is : "+b.getResultFromCM().getBirthDate());
					//System.out.println("FarsiName is : "+b.getResultFromCM().getFarsiName());
					System.out.println("XML Response is : "+b.getResultFromChannel().getResponseXml());
//					Thread.sleep(5000);
//				}


			} catch (Exception e) {
				System.out.println(e.toString());
			}

	}
	private static void Test_FollowUP() throws RemoteException {

			MainMQ AccountServices=new MainMQ();
			try {

				AccountServices.Init();
				FollowUpTransaction followUpTransaction=new FollowUpTransaction();
				followUpTransaction.setIsInternalFollowCode(true);
				followUpTransaction.setFollowUpCode("407609");
				followUpTransaction.setSourceAccount("3435531082");
				followUpTransaction.setCallUniqID("111111111111");
//				followUpTransaction.setIsPanPayment(false);

					resultFromBank  res=new resultFromBank(followUpTransaction);
				followUpTransaction=(FollowUpTransaction) res.getResultObject();
					System.out.println("Action Code : "+followUpTransaction.getActionCode());


			} catch (Exception e) {
				System.out.println(e.toString());
			}

	}
	private static void Test_fundTransfer() throws RemoteException {

			MainMQ AccountServices=new MainMQ();
			try {

				AccountServices.Init();
				FundTransfer b=new FundTransfer();
				b.setSourceAccount("3435531082");
				b.setDestinationAccount("3435531082");
				b.setIsFundTransfer(true);
				b.setTransactionAmount("1");


					//gatewayServices.accountInformation(null);
					resultFromBank  res=new resultFromBank(b);
					b=(FundTransfer) res.getResultObject();
					System.out.println("Action Code : "+b.getResultFromChannel().getAction_code());
					//System.out.println("BirthDate is : "+b.getResultFromCM().getBirthDate());
					//System.out.println("FarsiName is : "+b.getResultFromCM().getFarsiName());
//					System.out.println("XML Response is : "+b.getResultFromChannel().getResponseXml());




			} catch (Exception e) {
				System.out.println(e.toString());
			}

	}
	private static void Test_fundTransferIDENT() throws RemoteException {

			MainMQ AccountServices=new MainMQ();
			try {

				AccountServices.Init();
				FundTransfer b=new FundTransfer();
				b.setSourceAccount("3435531082");
				b.setDestinationAccount("3435531082");
				b.setTransferID("13640529");
				b.setIsIdentFundTranfer(true);
				b.setTransactionAmount("1");


					//gatewayServices.accountInformation(null);
					resultFromBank  res=new resultFromBank(b);
					b=(FundTransfer) res.getResultObject();
					System.out.println("Action Code : "+b.getResultFromChannel().getAction_code());
					//System.out.println("BirthDate is : "+b.getResultFromCM().getBirthDate());
					//System.out.println("FarsiName is : "+b.getResultFromCM().getFarsiName());
//					System.out.println("XML Response is : "+b.getResultFromChannel().getResponseXml());




			} catch (Exception e) {
				System.out.println(e.toString());
			}

	}
	private static void Test_fundTransferInstallment() throws RemoteException {

			MainMQ AccountServices=new MainMQ();
			try {

				AccountServices.Init();
				FundTransfer b=new FundTransfer();
				b.setSourceAccount("3435531082");
				b.setDestinationAccount("0231647473");
//				b.setTransferID("13640529");
				b.setIsInstallmentPay(true);
				b.setTransactionAmount("1500000");


					//gatewayServices.accountInformation(null);
					resultFromBank  res=new resultFromBank(b);
					b=(FundTransfer) res.getResultObject();
					System.out.println("Action Code : "+b.getResultFromChannel().getAction_code());
					//System.out.println("BirthDate is : "+b.getResultFromCM().getBirthDate());
					//System.out.println("FarsiName is : "+b.getResultFromCM().getFarsiName());
//					System.out.println("XML Response is : "+b.getResultFromChannel().getResponseXml());




			} catch (Exception e) {
				System.out.println(e.toString());
			}

	}
    private static void Test_accountTransaction(String params[]) throws RemoteException {
		//if  (StartedRMIServer(params)){
			//MainMQ AccountServices=new MainMQ();
			try {
				//AccountServices.Init();
				Transaction b=new Transaction();
               // b.setAccountNumber("4150645501");
				//b.setStatementType("1");
				//b.setStartDate("13900101");
				//b.setEndDate("13941023");
				//b.setStartTime("000000");
				//b.setEndTime("235959");
				//b.setTransactionCount("30");
                b.setAccountNumber("3435010031");
                b.setStatementType("8");
                b.setTransactionCount("3");

                b.setrrn("");

				//while (true){
					//gatewayServices.accountInformation(null);
					b=gatewayServices.accountTransaction(b);
					System.out.println("Action Code : "+b.getResultFromCM().getAction_code());
					System.out.println("20th transaction : "+b.getResultFromCM().getStatementMessage(2).getAmount());
					//System.out.println("FarsiName is : "+b.getResultFromCM().getFarsiName());
					System.out.println("XML Response is : "+b.getResultFromCM().getResponseXml());
				     createHTMLFaxFile(b);
                Thread.sleep(5000);
				//}


			} catch (Exception e) {
				System.out.println(e.toString());
			}
		//}
	}
    public  static String   createHTMLFaxFile(Transaction transaction) throws IOException {
        PersianDateTime persianDateTime=new PersianDateTime();
        StatementListMessage statementListMessage=new StatementListMessage();
         StatementMessage[]     statementMessage;
        ArrayList<String> Temp = new ArrayList<String>();
        String[][] List=new String[30][15];
        statementListMessage=transaction.getResultFromCM();
        int countOfTrans=Integer.valueOf(statementListMessage.getTransCount());
        statementMessage=new StatementMessage[countOfTrans];
        for (int i=0;i<countOfTrans;i++) statementMessage[i]=statementListMessage.getStatementMessage(i);
        String line;
        try {
            //Util.printMessage(Util.FaxFile+call.getCallerID()+"-"+call.getUniQID()+".html",false);
            InputStream FileName = new FileInputStream(PropertiesUtils.getProjectPath()+"\\Fax" + ".html");
            InputStreamReader InFile = new InputStreamReader(FileName, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(InFile);
            String T=null;
            while ((line = br.readLine()) != null) {
                if (line.contains("date")) {
                    T=line.replace("date", "1394/25/12");Temp.add(T);
                   // Util.printMessage("date loaded...",false);
                } else if (line.contains("accountnumber")) {
                    T=line.replace("accountnumber", "343525656");Temp.add(T);
                   // Util.printMessage("accountnumber loaded...",false);
                }else if (line.contains("branch")) {
                    T=line.replace("branch", "125452");Temp.add(T);
                   // Util.printMessage("branch loaded...",false);
                }else if (line.contains("name")) {
                    T=line.replace("name", "test");Temp.add(T);
                   // Util.printMessage("name loaded...",false);
                } else if (line.contains("todate")) {
                    T=line.replace("todate", "13940250");Temp.add(T);
                   // Util.printMessage("todate loaded...",false);
                }else if (line.contains("fromdate")) {
                    T=line.replace("fromdate","13940101");Temp.add(T);
                  //  Util.printMessage("fromdate loaded...",false);
                }else{

                    Temp.add(line);
                }
                //Util.printMessage("Line is"+line,false);
            }
            // Util.printMessage("file loaded...",false);
        } catch (FileNotFoundException e) {
           // Util.printMessage("e3"+e.toString(),false);
        } catch (IOException e) {
           // Util.printMessage("e4"+e.toString(),false);
        } catch (Exception e){
            //Util.printMessage("e5"+e.toString(),false);
        }
        try{
            int j=0;
            String credit="";
            String debit="";
            while (j<countOfTrans)
            {
                credit="";
                debit="";
                if (statementMessage[j].getCreditDebit().equals("C")) credit=statementMessage[j].getAmount();
                else if (statementMessage[j].getCreditDebit().equals("D")) debit=statementMessage[j].getAmount();
                Temp.add("<tr>");
                Temp.add("<td class=\"style2\"><div align=\"center\" NoWrap=\"NoWrap\">&nbsp;"+statementMessage[j].getLastAmount()+"</div></td>");
                Temp.add("<td class=\"style2\"><div align=\"center\" NoWrap=\"NoWrap\">&nbsp;"+debit+"</div></td>");
                Temp.add("<td class=\"style2\"><div align=\"center\" NoWrap=\"NoWrap\">&nbsp;"+credit+"</div></td>");
                Temp.add("<td class=\"style2\"><div align=\"center\" NoWrap=\"NoWrap\">&nbsp;"+statementMessage[j].getBranchCode()+"</div></td>");
                Temp.add("<td class=\"style2\"><div align=\"center\" NoWrap=\"NoWrap\">&nbsp;"+statementMessage[j].getTransDesc()+"</div></td>");
                Temp.add("<td class=\"style2\"><div align=\"center\" NoWrap=\"NoWrap\">&nbsp;"+statementMessage[j].getTransDocNo()+"</div></td>");
                Temp.add("<td class=\"style2\"><div align=\"center\" NoWrap=\"NoWrap\">&nbsp;"+persianDateTime.ConvertDateForFax(statementMessage[j].getTransDate())+"</div></td>");
                Temp.add("<td class=\"style2\"><div align=\"center\" NoWrap=\"NoWrap\">&nbsp;"+String.valueOf(j)+"</div></td>");
                Temp.add("<tr>");
                j++;
            }
           // Util.printMessage("data loaded ...",false);
            Temp.add("<tr>");
            Temp.add(" <td class=\"style6\"><div align=\"right\" >  </div></td>");
            Temp.add("<td class=\"style6\"><div align=\"right\" >" +"12000"+"</div></td>");
            Temp.add("<td class=\"style6\"><div align=\"right\" >"+"موجودی"+"</div></td>");
            Temp.add("<tr>");


            Temp.add("</table>");
            Temp.add("</td>");
            Temp.add("</tr>");
            Temp.add("</table>");
            Temp.add("</font>");
            Temp.add("</body>");
            Temp.add("</html>");
        }catch (Exception e){
           // Util.printMessage("e2"+e.toString(),false);
        }
        try
        {
           // Util.printMessage(Util.FaxFile+call.getCallerID()+"-"+call.getUniQID()+".html",false);
            File fileDir = new File(PropertiesUtils.getProjectPath()+"\\fax"+"-"+"123"+".html");
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));
            int j=0;
            while (j<Temp.size()){
                out.append(Temp.get(j)).append("\r\n");
                j++;
            }
            out.flush();
            out.close();
           //Util.printMessage("file creted...",false);
            return "null";
        }catch (Exception e){
          //  Util.printMessage("e1"+e.toString(),false);
            return null;
        }
    }
    private static void Test_foundTransfer(String params[]) throws RemoteException {
		if  (StartedRMIServer(params)){
			MainMQ AccountServices=new MainMQ();
			try {
				AccountServices.Init();
				FundTransfer b=new FundTransfer();
				//b.setSourceAccount("5101500200");
				b.setSourceAccount("155971991");
				b.setDestinationAccount("4150645501");
				b.setTransactionAmount("1");
				b.setCurrency("364");
				b.setOperationCode("");
				b.setExtraInfo("");



				//b.setCreditOrDebit();
				while (true){
					//gatewayServices.accountInformation(null);
					b=gatewayServices.accountFundTransfer(b);
					System.out.println("Action Code : "+b.getResultFromChannel().getAction_code());
					//System.out.println("BirthDate is : "+b.getResultFromCM().getBirthDate());
					//System.out.println("FarsiName is : "+b.getResultFromCM().getFarsiName());
					System.out.println("XML Response is : "+b.getResultFromChannel().getResponseXml());
					Thread.sleep(5000);
				}


			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}
	private static void Test_DBLog(String params[]) throws RemoteException, SQLException {
		//DataBaseLogger db=new DataBaseLogger(0,"insert");
	}
	private static void Test_Pin1(String params[]) throws RemoteException, SQLException, InterruptedException, ClassNotFoundException, ResponseParsingException, SenderException, InvalidParameterException {
		StartPin1 startPin1=new StartPin1();
		Pin1authenticate();

	}
    public static  void Pin1authenticate() throws RemoteException, SQLException, InterruptedException, ClassNotFoundException, ResponseParsingException, SenderException, InvalidParameterException {

        AuthenticatePin1 Aut=new AuthenticatePin1();
        Aut.setAccountNumber("3435531082");
        Aut.setPin("9221");
        Aut=gatewayServices.accountPin1authenticate(Aut);
        System.out.println(Aut.getActionCode());

    }
    private static void Test_Pin2(String params[]) throws RemoteException, SQLException, InterruptedException, ClassNotFoundException, ResponseParsingException, SenderException, InvalidParameterException {
        StartPin2 startPin2=new StartPin2();
        Pin1authenticate();

    }
    public static  void Pin2authenticate() throws RemoteException, SQLException, InterruptedException, ClassNotFoundException, ResponseParsingException, SenderException, InvalidParameterException {

        AuthenticatePin2 Aut=new AuthenticatePin2();
        Aut.setAccountNumber("3435531082");
        Aut.setPin("9221");
        Aut=gatewayServices.accountPin2authenticate(Aut);
        System.out.println(Aut.getActionCode());

    }

    private static void Test_Compare() throws RemoteException, SQLException {
        ObjectCompare objectCompare=new ObjectCompare();
        if (objectCompare.isBalanceForCard(new BalanceForCard())){
            System.out.print("yes");
        }else{
            System.out.print("no");
        }
    }
    private static void Test_Compare2(Object obj) throws RemoteException, SQLException {
        if (obj instanceof BalanceForCard){
            //System.out.println("yes");
        }else{
           // System.out.println("no");
        }
    }
	public  static boolean ReadPropertiesFileISOK(String params[]){
		try
		{

			return true;
		}catch (Exception e){
			return false;
		}
	}
	private static byte[] StringToByteArray(String hex) {

		int length = hex.length();
		byte[] buffer = new byte[length / 2];
		try{
			for (int i = 0; i < length; i += 2) {
				String S=hex.substring(i, i + 2);
				int j=Integer.valueOf(S, 0x10);
				buffer[i / 2] =(byte)j ;
				//System.out.println(buffer[i / 2]);
			}
		}catch (Exception var1){

		}

		return buffer;
	}
    public  static String logOfListenerException="";

	private static void TestSaba7() throws ExecutionException, InterruptedException, UnsupportedEncodingException, SQLException {
		CardSwitchVer7 cardSwitchVer7 = new CardSwitchVer7();

		BillPayByBillIDPan billPayByBillIDPan = new BillPayByBillIDPan();

		billPayByBillIDPan.setPan("1234567890123451");
		billPayByBillIDPan.setPin("11111");
		billPayByBillIDPan.setBillID("12121212");
		billPayByBillIDPan.setPaymentID("111111111");
		billPayByBillIDPan.setAmount("12121212");

		GatewayServices gatewayServices = new GatewayServices();
		billPayByBillIDPan = gatewayServices.cardBillPayByBillIDPan(billPayByBillIDPan);
		System.out.println(billPayByBillIDPan.getActionCode());
	}
}




















    //http://rox-xmlrpc.sourceforge.net/niotut/









