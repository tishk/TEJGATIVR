USE [master]
GO
/****** Object:  Database [Monitoring]    Script Date: 5/2/2016 9:44:34 AM ******/
CREATE DATABASE [Monitoring]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'Monitoring', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL11.MSSQLSERVER\MSSQL\DATA\Monitoring.mdf' , SIZE = 3072KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'Monitoring_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL11.MSSQLSERVER\MSSQL\DATA\Monitoring_log.ldf' , SIZE = 1024KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [Monitoring] SET COMPATIBILITY_LEVEL = 110
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [Monitoring].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [Monitoring] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [Monitoring] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [Monitoring] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [Monitoring] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [Monitoring] SET ARITHABORT OFF 
GO
ALTER DATABASE [Monitoring] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [Monitoring] SET AUTO_CREATE_STATISTICS ON 
GO
ALTER DATABASE [Monitoring] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [Monitoring] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [Monitoring] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [Monitoring] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [Monitoring] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [Monitoring] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [Monitoring] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [Monitoring] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [Monitoring] SET  DISABLE_BROKER 
GO
ALTER DATABASE [Monitoring] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [Monitoring] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [Monitoring] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [Monitoring] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [Monitoring] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [Monitoring] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [Monitoring] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [Monitoring] SET RECOVERY FULL 
GO
ALTER DATABASE [Monitoring] SET  MULTI_USER 
GO
ALTER DATABASE [Monitoring] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [Monitoring] SET DB_CHAINING OFF 
GO
ALTER DATABASE [Monitoring] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [Monitoring] SET TARGET_RECOVERY_TIME = 0 SECONDS 
GO
EXEC sys.sp_db_vardecimal_storage_format N'Monitoring', N'ON'
GO
USE [Monitoring]
GO
/****** Object:  Table [dbo].[tbl_Status_Database]    Script Date: 5/2/2016 9:44:35 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbl_Status_Database](

) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbl_Status_Gateway]    Script Date: 5/2/2016 9:44:35 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbl_Status_Gateway](




) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbl_Status_TelBank]    Script Date: 5/2/2016 9:44:35 AM ******/
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
USE [master]
GO
ALTER DATABASE [Monitoring] SET  READ_WRITE 
GO
