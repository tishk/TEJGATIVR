import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.messages.AccountListMessage;
import Mainchannel.sender.SenderException;
import ResponseServices.GatewayServices;
import ServiceObjects.Account.AccountInformation;
import ServiceObjects.Account.BalanceForAccount;
import ServiceObjects.Account.Transaction;
import ServiceObjects.Other.SMSAlarmTransaction;
import ServiceObjects.Pin.AuthenticatePin1;
import ServiceObjects.Pin.AuthenticatePin2;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class ClientTest {
	private static  GatewayServices gatewayServices=new GatewayServices();
	public static void main(String args[])throws Exception{
		Bank b=(Bank)Naming.lookup("rmi://127.0.0.1:1364/Gateway");
		Pin2change();
}

	public static void Pin1authenticate() throws RemoteException, SQLException, InterruptedException, ClassNotFoundException, ResponseParsingException, SenderException, InvalidParameterException {

		AuthenticatePin1 Aut=new AuthenticatePin1();
		Aut.setAccountNumber("3435530507");
		Aut.setPin("1361");
		Aut=gatewayServices.accountPin1authenticate(Aut);
		System.out.println(Aut.getActionCode());

	}
	public static void Pin1change() throws RemoteException, ClassNotFoundException, ResponseParsingException, SenderException, InvalidParameterException {

		try {
			Bank b=(Bank)Naming.lookup("rmi://127.0.0.1:1364/Gateway");
			AuthenticatePin1 Aut=new AuthenticatePin1();
			Aut.setAccountNumber("3435530507");
			Aut.setPin("1360");
			Aut.setPin_New("1363");
			Aut.setDoChangePin(true);
			Aut=gatewayServices.accountPin1Change(Aut);
			System.out.println(Aut.getActionCode());

		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int i=0;


		//BalanceForAccount balanceForAccount=new BalanceForAccount();
		//balanceForAccount.setAccountNumber("3435530507");







	}
	public static void Pin2authenticate() throws RemoteException, ClassNotFoundException, ResponseParsingException, SenderException, InvalidParameterException {

		try {
			Bank b=(Bank)Naming.lookup("rmi://127.0.0.1:1364/Gateway");
			AuthenticatePin2 Aut=new AuthenticatePin2();
			Aut.setAccountNumber("3435530507");
			Aut.setPin("12345");
			Aut=gatewayServices.accountPin2authenticate(Aut);
			System.out.println(Aut.getActionCode());

		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i=0;


		//BalanceForAccount balanceForAccount=new BalanceForAccount();
		//balanceForAccount.setAccountNumber("3435530507");







	}
	public static void Pin2change() throws RemoteException, ClassNotFoundException, ResponseParsingException, SenderException, InvalidParameterException {

		try {
			Bank b=(Bank)Naming.lookup("rmi://127.0.0.1:1364/Gateway");
			AuthenticatePin2 Aut=new AuthenticatePin2();
			Aut.setAccountNumber("3435530507");
			Aut.setPin("12344");
			Aut.setPin_New("12345");
			Aut.setDoChangePin(true);
			Aut=gatewayServices.accountPin2Change(Aut);
			System.out.println(Aut.getActionCode());

		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
            e.printStackTrace();
        }
        int i=0;


		//BalanceForAccount balanceForAccount=new BalanceForAccount();
		//balanceForAccount.setAccountNumber("3435530507");







	}
	public static void GetMobileNOOfAccount() throws RemoteException {

		try {
			Bank b=(Bank)Naming.lookup("rmi://127.0.0.1:1364/Gateway");
			SMSAlarmTransaction SMS=new SMSAlarmTransaction();
			SMS.setAccountNumber("3435531082");

			try {
				SMS=gatewayServices.accountGetMobileOfAccount(SMS);
				System.out.println(SMS.getMobileNumber());
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		int i=0;


		//BalanceForAccount balanceForAccount=new BalanceForAccount();
		//balanceForAccount.setAccountNumber("3435530507");







	}
	public static void SetMobileNOOfAccount() throws RemoteException {

		try {
			Bank b=(Bank)Naming.lookup("rmi://127.0.0.1:1364/Gateway");
			SMSAlarmTransaction SMS=new SMSAlarmTransaction();
			SMS.setAccountNumber("3435531082");
			SMS.setMobileNumber("09125016043");
            SMS.setIsSetMobileNumber(true);
			//SMS.setMobileNumber("09379707473");

			try {
				SMS=gatewayServices.accountSetMobileOfAccount(SMS);
				if (SMS.getMobileChanged()) System.out.println("Yes");
				else System.out.println("NO");
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		int i=0;


		//BalanceForAccount balanceForAccount=new BalanceForAccount();
		//balanceForAccount.setAccountNumber("3435530507");







	}
	public static void GetAccountInformation() throws RemoteException {
		try {
			Bank b=(Bank)Naming.lookup("rmi://127.0.0.1:1364/Gateway");
			AccountInformation ACInfo=new AccountInformation();
			ACInfo.setAccountNumber("3435531082");
			AccountListMessage AcList=null;

			try {
				ACInfo=gatewayServices.accountInformation(ACInfo);
				AcList=ACInfo.getResultFromChannel();
				System.out.println(AcList.getAccountType());
				System.out.println(AcList.getFarsiName());
				System.out.println(AcList.getBirthDate());
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		int i=0;
	}
	public static void GetBalance(){
		try{
			Bank b=(Bank)Naming.lookup("rmi://127.0.0.1:1364/Gateway");
			int i=0;




			BalanceForAccount B=new BalanceForAccount();
			B.setAccountNumber("1234");
			BalanceForAccount Result=gatewayServices.accountBalance(B);
			String ABalance= Result.getResultFromChannel().getActualBalance();

			Transaction T=new Transaction();
			T.setAccountNumber("111111");
			//T.setPin("1234");
			T.setStatementType("1");
			T.setStartDate("13940324");
			T.setEndDate("13940328");
			T.setStartTime("");
			T.setEndTime("");
			T.setTransactionCount("3");
			T.setCreditOrDebit("");
			T.setTransactionMaxAmount("");
			T.setTransactionMinAmount("");
			T.setTransactionDocDescription("");
			T.setTransactionDocNO("");
			T.setTransactionOperationCode("8");
			T.setBranchCode("");
			T=gatewayServices.accountTransaction(T);
         /* StatementListMessage L= T.getResultFromCM();
	   StatementMessage SM= L.getStatementMessage(1);
	   String Date= SM.getTransDate();
       */

		}catch (Exception e){
			System.out.println("here0" + e.toString());
		}


	}

}