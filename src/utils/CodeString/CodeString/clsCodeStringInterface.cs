using System;
using System.Collections.Generic;
using System.Text;

namespace CodeString
{
    public interface clsCodeStringInterface
    {
        string authenticate(string serverIp, string secretCode, string userName, string password, int port);
        string codeString(string str, string strKey);
        string EncryptDes(string str, string myKey);
        string EncryptPin(string pin, string myKey);
        string CodeString2(string arr,string strKey);
        byte[] Encrypt(byte[] inputByteArray, string myKey);
    }
}

