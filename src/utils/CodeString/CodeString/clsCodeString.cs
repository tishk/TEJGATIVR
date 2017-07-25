using System;
using System.IO;
using System.Net.nRadius;
using System.Runtime.InteropServices;
using System.Security.Cryptography;
using System.Text;
using System.Linq;    

namespace CodeString
{
    [ClassInterface(ClassInterfaceType.None)]
    public class clsCodeString:clsCodeStringInterface
    {
        public string authenticate(string serverIp, string secretCode, string userName, string password, int port)
        {
            nRadius_Client client = new nRadius_Client(serverIp, secretCode, userName, password);
            client.Port = port;
            if (client.Authenticate() == 0)
            {
                return "1";
            }
            return "2";
        }

        public string codeString(string str, string strKey)
        {
            try
            {
                string s = str;
                byte[] inputByteArray = new byte[8];
                byte[] bytes = Encoding.GetEncoding("UTF-8").GetBytes(s);
                for (int i = 0; i < bytes.Length; i += 8)
                {
                    for (int j = 0; j < 8; j++)
                    {
                        if ((i + j) >= bytes.Length)
                        {
                            inputByteArray[j] = inputByteArray[j];
                        }
                        else
                        {
                            inputByteArray[j] = (byte)(inputByteArray[j] ^ bytes[i + j]);
                        }
                    }
                    inputByteArray = this.Encrypt(inputByteArray, strKey);
                }
                StringBuilder builder = new StringBuilder();
                foreach (byte num3 in inputByteArray)
                {
                    builder.AppendFormat("{0:X2}", num3);
                }
                return builder.ToString();
            }
            catch
            {
                return "0000000000000000";
            }
        }

        public byte[] Encrypt(byte[] inputByteArray, string myKey)
        {
            try
            {
                DESCryptoServiceProvider provider = new DESCryptoServiceProvider();
                byte[] buffer = this.StringToByteArray(myKey);
                provider.Key = buffer;
                provider.IV = Encoding.UTF8.GetBytes("00000000");
                MemoryStream stream = new MemoryStream();
                provider.Mode = CipherMode.ECB;
                provider.Padding = PaddingMode.None;
                CryptoStream stream2 = new CryptoStream(stream, provider.CreateEncryptor(), CryptoStreamMode.Write);
                stream2.Write(inputByteArray, 0, inputByteArray.Length);
                stream2.FlushFinalBlock();
                return stream.ToArray();
            }
            catch
            {
                return Encoding.UTF8.GetBytes("00000000");
            }
        }

        public string EncryptDes(string str, string myKey)
        {
            try
            {
                byte[] buffer = this.StringToByteArray(str);
                DESCryptoServiceProvider provider = new DESCryptoServiceProvider();
                byte[] buffer2 = this.StringToByteArray(myKey);
                provider.Key = buffer2;
                provider.IV = Encoding.UTF8.GetBytes("00000000");
                MemoryStream stream = new MemoryStream();
                provider.Mode = CipherMode.ECB;
                provider.Padding = PaddingMode.None;
                CryptoStream stream2 = new CryptoStream(stream, provider.CreateEncryptor(), CryptoStreamMode.Write);
                stream2.Write(buffer, 0, buffer.Length);
                stream2.FlushFinalBlock();
                byte[] buffer3 = stream.ToArray();
                StringBuilder builder = new StringBuilder();
                foreach (byte num in buffer3)
                {
                    builder.AppendFormat("{0:X2}", num);
                }
                return builder.ToString();
            }
            catch
            {
                return "0000000000000000";
            }
        }

        public string EncryptPin(string pin, string myKey)
        {
            try
            {
                byte[] bytes = Encoding.ASCII.GetBytes("0000000000000000");
                byte[] buffer = Encoding.UTF8.GetBytes(pin);
                byte[] rgbKey = Encoding.ASCII.GetBytes(myKey);
                RijndaelManaged managed = new RijndaelManaged();
                managed.Mode = CipherMode.ECB;
                managed.Padding = PaddingMode.PKCS7;
                managed.KeySize = 0x80;
                ICryptoTransform transform = managed.CreateEncryptor(rgbKey, bytes);
                MemoryStream stream = new MemoryStream();
                CryptoStream stream2 = new CryptoStream(stream, transform, CryptoStreamMode.Write);
                stream2.Write(buffer, 0, buffer.Length);
                stream2.FlushFinalBlock();
                byte[] buffer4 = stream.ToArray();
                stream.Close();
                stream2.Close();
                StringBuilder builder = new StringBuilder();
                foreach (byte num in buffer4)
                {
                    builder.AppendFormat("{0:X2}", num);
                }
                return builder.ToString();
            }
            catch
            {
                return "0000000000000000";
            }
        }

        public string CodeString2(string str, string strKey)
        {
            string[] m = str.Split(',');
            byte[] arr = m.Select(s => Convert.ToByte(s)).ToArray();
            try
            {
                byte[] inputByteArray = new byte[8];
                for (int i = 0; i < arr.Length; i += 8)
                {
                    for (int j = 0; j < 8; j++)
                    {
                        if ((i + j) >= arr.Length)
                        {
                            inputByteArray[j] = inputByteArray[j];
                        }
                        else
                        {
                            inputByteArray[j] = (byte)(inputByteArray[j] ^ arr[i + j]);
                        }
                    }
                    inputByteArray = this.Encrypt(inputByteArray, strKey);
                }
                StringBuilder builder = new StringBuilder();
                foreach (byte num3 in inputByteArray)
                {
                    builder.AppendFormat("{0:X2}", num3);
                }
                return builder.ToString();
            }
            catch
            {
                return "0000000000000000";
            }
        }

        [ComVisible(true)]
        public byte[] StringToByteArray(string hex)
        {
            int length = hex.Length;
            byte[] buffer = new byte[length / 2];
            for (int i = 0; i < length; i += 2)
            {
                buffer[i / 2] = Convert.ToByte(hex.Substring(i, 2), 0x10);
            }
            return buffer;
        }




    }
}
