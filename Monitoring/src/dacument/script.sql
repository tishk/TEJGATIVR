USE [Monitoring]
GO
/****** Object:  Table [dbo].[tbl_Status_Database]    Script Date: 5/18/2016 10:42:21 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbl_Status_Database](
	[DBPingStatus] [bit] NULL,
	[DBPingStatusDateTime] [datetime] NULL,
	[DBPingStatusActionCode] [nvarchar](4) NULL,
	[DBPingStatusDesc] [nvarchar](100) NULL,
	[DBCPUUsingTime] [nvarchar](20) NULL,
	[DBCPUFreeTime] [nvarchar](20) NULL,
	[DBCPUUsingPercent] [nvarchar](2) NULL,
	[DBCPUFreePercent] [nvarchar](2) NULL,
	[DBRAMAllSpace] [nvarchar](20) NULL,
	[DBRAMUsingSpace] [nvarchar](20) NULL,
	[DBRAMFreeSpace] [nvarchar](20) NULL,
	[DBRAMUsingPercent] [nvarchar](2) NULL,
	[DBRAMFreePercent] [nvarchar](2) NULL,
	[DBNetDownloadRate] [nvarchar](20) NULL,
	[DBNetUploadRate] [nvarchar](20) NULL,
	[DBNetAdapterInfo] [nvarchar](500) NULL,
	[DBSwapAllSpace] [nvarchar](20) NULL,
	[DBSwapUsingSpace] [nvarchar](20) NULL,
	[DBSwapFreeSpace] [nvarchar](20) NULL,
	[DBSwapUsingPercent] [nvarchar](20) NULL,
	[DBSwapFreePercent] [nvarchar](20) NULL,
	[DBDiskAllSpace] [nvarchar](50) NULL,
	[DBDiskUsingSpace] [nvarchar](50) NULL,
	[DBDiskFreeSpace] [nvarchar](50) NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbl_Status_Gateway]    Script Date: 5/18/2016 10:42:22 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbl_Status_Gateway](
	[gPingStatus] [bit] NULL,
	[gPingStatusDateTime] [datetime] NULL,
	[gPingStatusActionCode] [nvarchar](4) NULL,
	[gPingStatusDesc] [nvarchar](100) NULL,
	[gAppRunning] [bit] NULL,
	[gAppRunningDateTime] [datetime] NULL,
	[gAppRunningActionCode] [nchar](10) NULL,
	[gAppRunningDesc] [nvarchar](100) NULL,
	[gChannelConneted] [bit] NULL,
	[gChannelConnetedDateTime] [datetime] NULL,
	[gChannelConnetedActionCode] [nvarchar](4) NULL,
	[gChannelConnetedDesc] [nvarchar](100) NULL,
	[gSabaSwitchConnected] [bit] NULL,
	[gSabaSwitchConnectedDateTime] [datetime] NULL,
	[gSabaSwitchConnectedActionCode] [nvarchar](4) NULL,
	[gSabaSwitchConnectedDesc] [nvarchar](100) NULL,
	[gTelSwitchConnected] [bit] NULL,
	[gTelSwitchConnectedDateTime] [datetime] NULL,
	[gTelSwitchConnectedActionCode] [nvarchar](4) NULL,
	[gTelSwitchConnectedDesc] [nvarchar](100) NULL,
	[gPin1isOK] [bit] NULL,
	[gPin1isOKDateTime] [datetime] NULL,
	[gPin1isOKActionCode] [nvarchar](4) NULL,
	[gPin1isOKDesc] [nvarchar](100) NULL,
	[gPin2isOK] [bit] NULL,
	[gPin2isOKDateTime1] [datetime] NULL,
	[gPin2isOKActionCode1] [nvarchar](4) NULL,
	[gPin2isOKDesc1] [nvarchar](100) NULL,
	[gDBConnected] [bit] NULL,
	[gDBConnectedisOKDateTime] [datetime] NULL,
	[gDBConnectedisOKActionCode] [nvarchar](4) NULL,
	[gDBConnectedisOKDesc] [nvarchar](100) NULL,
	[gCPUUsingTime] [nvarchar](20) NULL,
	[gCPUFreeTime] [nvarchar](20) NULL,
	[gCPUUsingPercent] [nvarchar](2) NULL,
	[gCPUFreePercent] [nvarchar](2) NULL,
	[gRAMAllSpace] [nvarchar](20) NULL,
	[gRAMUsingSpace] [nvarchar](20) NULL,
	[gRAMFreeSpace] [nvarchar](20) NULL,
	[gRAMUsingPercent] [nvarchar](2) NULL,
	[gRAMFreePercent] [nvarchar](2) NULL,
	[gNetDownloadRate] [nvarchar](20) NULL,
	[gNetUploadRate] [nvarchar](20) NULL,
	[gNetAdapterInfo] [nvarchar](500) NULL,
	[gSwapAllSpace] [nvarchar](20) NULL,
	[gSwapUsingSpace] [nvarchar](20) NULL,
	[gSwapFreeSpace] [nvarchar](20) NULL,
	[gSwapUsingPercent] [nvarchar](20) NULL,
	[gSwapFreePercent] [nvarchar](20) NULL,
	[gDiskAllSpace] [nvarchar](50) NULL,
	[gDiskUsingSpace] [nvarchar](50) NULL,
	[gDiskFreeSpace] [nvarchar](50) NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbl_Status_TelBank]    Script Date: 5/18/2016 10:42:22 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbl_Status_TelBank](
	[TBClientID] [nvarchar](3) NULL,
	[TBClientMAC] [nvarchar](17) NULL,
	[TBClientIP] [nvarchar](15) NULL,
	[TBPingStatus] [bit] NULL,
	[TBPingStatusDateTime] [datetime] NULL,
	[TBPingStatusActionCode] [nvarchar](4) NULL,
	[TBPingStatusDesc] [nvarchar](100) NULL,
	[TBAppRunning] [bit] NULL,
	[TBAppRunningStatusDateTime] [datetime] NULL,
	[TBAppRunningStatusActionCode] [nvarchar](4) NULL,
	[TBAppRunningStatusDesc] [nvarchar](100) NULL,
	[TBGatewayConneted] [bit] NULL,
	[TBGatewayConnetedStatusDateTime] [datetime] NULL,
	[TBGatewayConnetedStatusActionCode] [nvarchar](4) NULL,
	[TBGatewayConnetedStatusDesc] [nvarchar](100) NULL,
	[TBCPUUsingTime] [nvarchar](20) NULL,
	[TBCPUFreeTime] [nvarchar](20) NULL,
	[TBCPUUsingPercent] [nvarchar](2) NULL,
	[TBCPUFreePercent] [nvarchar](2) NULL,
	[TBRAMAllSpace] [nvarchar](20) NULL,
	[TBRAMUsingSpace] [nvarchar](20) NULL,
	[TBRAMFreeSpace] [nvarchar](20) NULL,
	[TBRAMUsingPercent] [nvarchar](2) NULL,
	[TBRAMFreePercent] [nvarchar](2) NULL,
	[TBNetDownloadRate] [nvarchar](20) NULL,
	[TBNetUploadRate] [nvarchar](20) NULL,
	[TBNetAdapterInfo] [nvarchar](500) NULL,
	[TBSwapAllSpace] [nvarchar](20) NULL,
	[TBSwapUsingSpace] [nvarchar](20) NULL,
	[TBSwapFreeSpace] [nvarchar](20) NULL,
	[TBSwapUsingPercent] [nvarchar](20) NULL,
	[TBSwapFreePercent] [nvarchar](20) NULL,
	[TBDiskAllSpace] [nvarchar](50) NULL,
	[TBDiskUsingSpace] [nvarchar](50) NULL,
	[TBDiskFreeSpace] [nvarchar](50) NULL
) ON [PRIMARY]

GO
