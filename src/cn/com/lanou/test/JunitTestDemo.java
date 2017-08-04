package cn.com.lanou.test;

import java.io.File;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.Who;
import org.junit.Before;
import org.junit.Test;


public class JunitTestDemo {
	private Sigar sigar = null;
	// 磁盘读写初始数据 用于计算读写速率
	private Map<String, String> diskWritesAndReadsOnInit = new HashMap<String, String>();
	private long initTime;
	
	
	@Before
	public void test1(){
		initTime = System.currentTimeMillis();
		String libPath = System.getProperty("java.library.path");
		String classpath = JunitTestDemo.class.getResource("/").getPath();
		System.setProperty("java.library.path", classpath + File.separator + "sigar" + File.pathSeparator + libPath);
		try {

			sigar = new Sigar();
			FileSystem fslist[] = sigar.getFileSystemList();
			FileSystemUsage usage = null;
			for (int i = 0; i < fslist.length; i++) {
				FileSystem fs = fslist[i];
				if (fs.getType() != 2)
					continue;
				usage = sigar.getFileSystemUsage(fs.getDirName());
				diskWritesAndReadsOnInit.put(fs.getDevName(), usage.getDiskReadBytes() + "|" + usage.getDiskWriteBytes());
			}
		} catch (Exception e) {
		} 
	}
	
	/**
	 * 获取cpu信息代码
	 * @throws SigarException
	 */
	@Test
	public void test2() throws SigarException{
		// CPU数量（单位：个）
		int cpuLength = sigar.getCpuInfoList().length;
		System.out.println("CPU数量（单位：个）="+cpuLength);

		// CPU的总量（单位：HZ）及CPU的相关信息
		CpuInfo infos[] = sigar.getCpuInfoList();
		for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
			CpuInfo info = infos[i];
			System.out.println("第"+(i+1)+"个CPU");
			System.out.println("CPU的总量mhz=" + info.getMhz());// CPU的总量MHz
			System.out.println("CPU的卖主vendor=" + info.getVendor());// 获得CPU的卖主，如：Intel
			System.out.println("CPU的类别model=" + info.getModel());// 获得CPU的类别，如：Celeron
			System.out.println("缓冲存储器数量cache size=" + info.getCacheSize());// 缓冲存储器数量
		}

		/** CPU的用户使用量、系统使用剩余量、总的剩余量、总的使用占用量等（单位：100%） **/
		// 方式一，主要是针对一块CPU的情况
		CpuPerc cpu;
		try {
			cpu = sigar.getCpuPerc();
			//System.out.println(cpu);
		} catch (SigarException e) {
			e.printStackTrace();
		}

		// 方式二，不管是单块CPU还是多CPU都适用
		CpuPerc cpuList[] = null;
		try {
			cpuList = sigar.getCpuPercList();
		} catch (SigarException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < cpuList.length; i++) {
			System.out.println("第"+(i+1)+"个CPU");
			System.out.println(cpuList[i]);
		}
	}
	
	/**
	 * 获取内存信息代码
	 * @throws SigarException
	 */
	@Test
	public void test3() throws SigarException{
		// 物理内存信息
		Mem mem = sigar.getMem();
		// 内存总量
		System.out.println("内存总量Total = " + mem.getTotal() / 1024L / 1024 + "M av");
		// 当前内存使用量
		System.out.println("当前内存使用量Used = " + mem.getUsed() / 1024L / 1024 + "M used");
		// 当前内存剩余量
		System.out.println("当前内存剩余量Free = " + mem.getFree() / 1024L / 1024 + "M free");

		// 系统页面文件交换区信息
		Swap swap = sigar.getSwap();
		// 交换区总量
		System.out.println("交换区总量Total = " + swap.getTotal() / 1024L + "K av");
		// 当前交换区使用量
		System.out.println("当前交换区使用量Used = " + swap.getUsed() / 1024L + "K used");
		// 当前交换区剩余量
		System.out.println("当前交换区剩余量Free = " + swap.getFree() / 1024L + "K free");
	}
	
	/**
	 * 获取操作系统信息代码
	 * @throws SigarException
	 */
	@Test
	public void test4() throws SigarException{
		// 取到当前操作系统的名称
		String hostname = "";
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (Exception exc) {
			try {
				hostname = sigar.getNetInfo().getHostName();
			} catch (SigarException e) {
				hostname = "localhost.unknown";
			} finally {
				sigar.close();
			}
		}
		System.out.println("计算机名 = "+hostname);

		// 取当前操作系统的信息
		OperatingSystem OS = OperatingSystem.getInstance();
		// 操作系统内核类型如： 386、486、586等x86
		System.out.println("操作系统内核类型OS.getArch() = " + OS.getArch());
		System.out.println("操作系统内核类型OS.getCpuEndian() = " + OS.getCpuEndian());//
		System.out.println("操作系统内核类型OS.getDataModel() = " + OS.getDataModel());//
		// 系统描述
		System.out.println("系统描述OS.getDescription() = " + OS.getDescription());
		System.out.println("系统描述OS.getMachine() = " + OS.getMachine());//
		// 操作系统类型
		System.out.println("操作系统类型OS.getName() = " + OS.getName());
		System.out.println("操作系统类型OS.getPatchLevel() = " + OS.getPatchLevel());//
		// 操作系统的卖主
		System.out.println("操作系统的卖主OS.getVendor() = " + OS.getVendor());
		// 卖主名称
		System.out.println("卖主名称OS.getVendorCodeName() = " + OS.getVendorCodeName());
		// 操作系统名称
		System.out.println("操作系统名称OS.getVendorName() = " + OS.getVendorName());
		// 操作系统卖主类型
		System.out.println("操作系统卖主类型OS.getVendorVersion() = " + OS.getVendorVersion());
		// 操作系统的版本号
		System.out.println("操作系统的版本号OS.getVersion() = " + OS.getVersion());

		// 取当前系统进程表中的用户信息
		Who who[] = sigar.getWhoList();
		if (who != null && who.length > 0) {
			for (int i = 0; i < who.length; i++) {
				System.out.println("\n取当前系统进程表中的用户信息~~~~~~~~~" + String.valueOf(i) + "~~~~~~~~~");
				Who _who = who[i];
				System.out.println("getDevice() = " + _who.getDevice());
				System.out.println("getHost() = " + _who.getHost());
				System.out.println("getTime() = " + _who.getTime());
				// 当前系统进程表中的用户名
				System.out.println("当前系统进程表中的用户名getUser() = " + _who.getUser());
			}
		}

	}
	
	/**
	 * 获取磁盘信息代码
	 * @throws SigarException
	 */
	@Test
	public void test5() throws SigarException{
		// 取硬盘已有的分区及其详细信息（通过sigar.getFileSystemList()来获得FileSystem列表对象，然后对其进行编历
		FileSystem fslist[] = sigar.getFileSystemList();
		String dir = System.getProperty("user.home");// 当前用户文件夹路径
		System.out.println(dir + "   " + fslist.length);
		for (int i = 0; i < fslist.length; i++) {
			FileSystem fs = fslist[i];
			System.out.println("\n~~~~~~~~~~" + fs.getDevName() + "~~~~~~~~~~");
			// 分区的盘符名称
			System.out.println("分区的盘符名称fs.getDevName() = " + fs.getDevName());
			// 分区的盘符名称
			System.out.println("分区的盘符名称fs.getDirName() = " + fs.getDirName());
			System.out.println("fs.getFlags() = " + fs.getFlags());//
			// 文件系统类型，比如 FAT32、NTFS
			System.out.println("文件系统类型fs.getSysTypeName() = " + fs.getSysTypeName());
			// 文件系统类型名，比如本地硬盘、光驱、网络文件系统等
			System.out.println("文件系统类型fs.getTypeName() = " + fs.getTypeName());
			// 文件系统类型
			System.out.println("文件系统类型fs.getType() = " + fs.getType());
			FileSystemUsage usage = null;
			try {
				usage = sigar.getFileSystemUsage(fs.getDirName());
			} catch (SigarException e) {
				if (fs.getType() == 2)
					throw e;
				continue;
			}
			switch (fs.getType()) {
			case 0: // TYPE_UNKNOWN ：未知
				break;
			case 1: // TYPE_NONE
				break;
			case 2: // TYPE_LOCAL_DISK : 本地硬盘
				// 文件系统总大小
				System.out.println("文件系统总大小 Total = " + usage.getTotal() + "KB");
				// 文件系统剩余大小
				System.out.println("文件系统剩余大小 Free = " + usage.getFree() + "KB");
				// 文件系统可用大小
				System.out.println("文件系统可用大小 Avail = " + usage.getAvail() + "KB");
				// 文件系统已经使用量
				System.out.println("文件系统已经使用量 Used = " + usage.getUsed() + "KB");
				double usePercent = usage.getUsePercent() * 100D;
				// 文件系统资源的利用率
				System.out.println("文件系统资源的利用率 Usage = " + usePercent + "%");
				break;
			case 3:// TYPE_NETWORK ：网络
				break;
			case 4:// TYPE_RAM_DISK ：闪存
				break;
			case 5:// TYPE_CDROM ：光驱
				break;
			case 6:// TYPE_SWAP ：页面交换
				break;
			}
			System.out.println("磁盘读取 DiskReads = " + usage.getDiskReads());
			System.out.println("磁盘写入 DiskWrites = " + usage.getDiskWrites());
		}
	}
	
	/**
	 * 获取网络信息代码
	 * @throws SigarException
	 */
	@Test
	public void test6() throws SigarException{
		// 当前机器的正式域名
		try {
			System.out.println(InetAddress.getLocalHost().getCanonicalHostName());
		} catch (Exception e) {
			try {
				System.out.println(sigar.getFQDN());
			} catch (SigarException ex) {
			} finally {
				sigar.close();
			}
		}

		// 取到当前机器的IP地址
		String address = null;
		try {
			address = InetAddress.getLocalHost().getHostAddress();
			// 没有出现异常而正常当取到的IP时，如果取到的不是网卡循回地址时就返回
			// 否则再通过Sigar工具包中的方法来获取
			System.out.println(address);
			if (!NetFlags.LOOPBACK_ADDRESS.equals(address)) {
			}
		} catch (Exception e) {
			// hostname not in DNS or /etc/hosts
		}
		try {
			address = sigar.getNetInterfaceConfig().getAddress();
		} catch (SigarException e) {
			address = NetFlags.LOOPBACK_ADDRESS;
		} finally {
		}
		System.out.println(address);

		// 取到当前机器的MAC地址
		String[] ifaces = sigar.getNetInterfaceList();
		String hwaddr = null;
		for (int i = 0; i < ifaces.length; i++) {
			NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
			if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress())
					|| (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0
					|| NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
				continue;
			}
			hwaddr = cfg.getHwaddr();
			System.out.println(hwaddr);
			// break;
		}
		System.out.println(hwaddr != null ? hwaddr : null);

		// 获取网络流量等信息
		String ifNames[] = sigar.getNetInterfaceList();
		for (int i = 0; i < ifNames.length; i++) {
			String name = ifNames[i];
			NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
			System.out.println("\nname = " + name);// 网络设备名
			System.out.println("Address = " + ifconfig.getAddress());// IP地址
			System.out.println("Netmask = " + ifconfig.getNetmask());// 子网掩码
			if ((ifconfig.getFlags() & 1L) <= 0L) {
				System.out.println("!IFF_UP...skipping getNetInterfaceStat");
				continue;
			}
			try {
				NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
				System.out.println("RxPackets = " + ifstat.getRxPackets());// 接收的总包裹数
				System.out.println("TxPackets = " + ifstat.getTxPackets());// 发送的总包裹数
				System.out.println("RxBytes = " + ifstat.getRxBytes());// 接收到的总字节数
				System.out.println("TxBytes = " + ifstat.getTxBytes());// 发送的总字节数
				System.out.println("RxErrors = " + ifstat.getRxErrors());// 接收到的错误包数
				System.out.println("TxErrors = " + ifstat.getTxErrors());// 发送数据包时的错误数
				System.out.println("RxDropped = " + ifstat.getRxDropped());// 接收时丢弃的包数
				System.out.println("TxDropped = " + ifstat.getTxDropped());// 发送时丢弃的包数
			} catch (SigarNotImplementedException e) {
			} catch (SigarException e) {
				System.out.println(e.getMessage());
			}
		}

		// 一些其他的信息
		for (int i = 0; i < ifaces.length; i++) {
			NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
			if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress())
					|| (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0
					|| NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
				continue;
			}
			System.out.println("cfg.getAddress() = " + cfg.getAddress());// IP地址
			System.out.println("cfg.getBroadcast() = " + cfg.getBroadcast());// 网关广播地址
			System.out.println("cfg.getHwaddr() = " + cfg.getHwaddr());// 网卡MAC地址
			System.out.println("cfg.getNetmask() = " + cfg.getNetmask());// 子网掩码
			System.out.println("cfg.getDescription() = " + cfg.getDescription());// 网卡描述信息
			System.out.println("cfg.getType() = " + cfg.getType());//
			System.out.println("cfg.getDestination() = " + cfg.getDestination());
			System.out.println("cfg.getFlags() = " + cfg.getFlags());//
			System.out.println("cfg.getMetric() = " + cfg.getMetric());
			System.out.println("cfg.getMtu() = " + cfg.getMtu());
			System.out.println("cfg.getName() = " + cfg.getName());
		}
	}
	
}
