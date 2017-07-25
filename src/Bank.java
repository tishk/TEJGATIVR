
import java.rmi.*;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;

import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;

interface Bank extends Remote{
    public  String submitRequest(String params[])throws  RemoteException, SenderException, InvalidParameterException, ResponseParsingException;
    public  Object submitRequest(Object RequestObject) throws RemoteException, SenderException, InvalidParameterException, ResponseParsingException, SQLException, ServerNotActiveException;
}