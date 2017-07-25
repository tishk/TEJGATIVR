//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.sender;

public class SenderConfig {
    public static String LOCAL_TRANSPORT = "local";
    public static String REMOTE_TRANSPORT = "remote";
    public static int LOCAL_TRANSPORT_ID = 0;
    public static int REMOTE_TRANSPORT_ID = 1;
    private String hostName;
    private String queue_manager_name;
    private String client_ID;
    private String reciveQueueName;
    private int numberOfConnections;
    private int numberOfSessionsPerConnection;
    private long timeout;
    private String sendQueueName;
    private int hostPort;
    private String channel_name;
    private String MQusername;
    private String MQuserPass;
    private String transportType;
    private int reconnectionInterval;

    public SenderConfig() {
    }

    public int getReconnectionInterval() {
        return this.reconnectionInterval;
    }

    public void setReconnectionInterval(int reconnectionInterval) {
        this.reconnectionInterval = reconnectionInterval;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getNumberOfSessionsPerConnection() {
        return this.numberOfSessionsPerConnection;
    }

    public void setNumberOfSessionsPerConnection(int numberOfSessionsPerConnection) {
        this.numberOfSessionsPerConnection = numberOfSessionsPerConnection;
    }

    public int getNumberOfConnections() {
        return this.numberOfConnections;
    }

    public void setNumberOfConnections(int numberOfConnections) {
        this.numberOfConnections = numberOfConnections;
    }

    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getQueue_manager_name() {
        return this.queue_manager_name;
    }

    public void setQueue_manager_name(String queue_manager_name) {
        this.queue_manager_name = queue_manager_name;
    }

    public String getClient_ID() {
        return this.client_ID;
    }

    public void setClient_ID(String client_ID) {
        this.client_ID = client_ID;
    }

    public String getReciveQueueName() {
        return this.reciveQueueName;
    }

    public void setReciveQueueName(String reciveQueueName) {
        this.reciveQueueName = reciveQueueName;
    }

    public String getSendQueueName() {
        return this.sendQueueName;
    }

    public void setSendQueueName(String sendQueueName) {
        this.sendQueueName = sendQueueName;
    }

    public int getHostPort() {
        return this.hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public String getChannel_name() {
        return this.channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getMQusername() {
        return this.MQusername;
    }

    public void setMQusername(String MQusername) {
        this.MQusername = MQusername;
    }

    public String getMQuserPass() {
        return this.MQuserPass;
    }

    public void setMQuserPass(String MQuserPass) {
        this.MQuserPass = MQuserPass;
    }

    public String getTransportType() {
        return this.transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }
}
