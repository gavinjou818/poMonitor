package pomonitor.briefing;

import sun.misc.BASE64Encoder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import pomonitor.entity.Briefing;
import pomonitor.entity.BriefingDAO;
import pomonitor.util.ConsoleLog;
import pomonitor.util.PropertiesReader;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.sun.xml.internal.ws.client.SenderException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author zhouzhifeng
 * 使用前提概要:
 * 
 1.开启baes64权限
 右键poMonitor->Build Path->JRE system Library(这个根据原来自己的JRE包）->
 Access rules->右边一排有个Edit点击->Add..->Resolution:Accessible,Rule Pattern:**—>保存
 2.配置Jacob包
 将jacob包里面的dll文件存入(你正在使用的)jre的bin目录下
 如果查看jre目录在哪:
 打开Window->Preferences->输入jre->Installed JREs
 3.配置最大内存(这不是本报表的问题,是整个系统需要这样,否则各种问题)
 Window->Preferences->输入jre->Installed JREs->单击(你正在使用的)jre
 ->Edit->Default VM arguments填写-Xms128M -Xmx512M;
 * 
 */

/**
 * 文档操作类,例如创建word,加载freemarket加载器, 转word到pdf等,创建word目录,发送邮件等
 * 
 * 注意 范用到jacob 方法的文件调用都会有平台限制,同时必须在平台安装word，
 * 有局限性,当需要移植到其他平台时,必须考虑到这个问题。或者当有新方法必须更换.
 * 
 * @author zhouzhifeng
 * 
 */
public class DocumentHandler 
{
	// freemarket配置加载器
	private Configuration configuration = null;
	
	//官方邮箱账号
	private  static String officialEmailAccount;
	//官方邮箱密码
	private  static String officialEmailPassword;
    //官方邮箱端口
	private  static String officialEmailPort;
	//官方邮箱服务器
	private  static String officialEmailServer;

	static
	{    
		PropertiesReader propertiesReader = new PropertiesReader();
		
		officialEmailAccount = propertiesReader.getPropertyByName("officialEmailAccount");
		officialEmailPassword = propertiesReader.getPropertyByName("officialEmailPassword");
		officialEmailPort =propertiesReader.getPropertyByName("officialEmailPort");
		officialEmailServer=propertiesReader.getPropertyByName("officialEmailServer");
	}
	
	
	// 初始化参数,并设置默认编码
	public DocumentHandler() 
	{
		// 创建配置实例
		configuration = new Configuration();
		// 设置编码
		configuration.setDefaultEncoding("utf-8");
	}

	/**
	 * @Desc:生成word文件
	 * @author zhouzhifeng
	 * @Date 2016/12/16 模板都存在TEMPLATE包里 先打开/TEMPLATE读取其中的一个模板.ftl 然后进行操作进行映射
	 * @param dataMap
	 *            key->value 对应模板的EA表达中一一对应
	 * @param templateName
	 *            模板的名称.ftl
	 * @param filePath
	 *            输出文件路径
	 * @param fileName
	 *            输出文件名称
	 */
	public void createWord(BriefingData briefing,String userPath) {
		// 加载模板根目录
		configuration.setClassForTemplateLoading(this.getClass(), "/TEMPLATE");
		// 获取指定模板
		Template template = null;
		try {
			template = configuration.getTemplate(briefing.getTemplateName());

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 基本模板输出流.
		File outFile = new File(briefing.getFilePath()+userPath+ briefing.getFileName());

		if (!outFile.getParentFile().exists()) {
			outFile.getParentFile().mkdirs();
		}

		Writer out = null;
		FileOutputStream fos = null;
		try {

			fos = new FileOutputStream(outFile);
			OutputStreamWriter oWriter = new OutputStreamWriter(fos, "UTF-8");
			out = new BufferedWriter(oWriter);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 映射模板并输出到指定位置
		try {
			template.process(briefing.getDataMap(), out);
			out.close();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 输入路径,用于导入图片函数
	public String getImageStr(String filePath) throws Exception {

		InputStream in = null;
		byte[] data = null;
		in = new FileInputStream(filePath);

		data = new byte[in.available()];
		in.read(data);
		in.close();
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}

	/**
	 * 该方法用于更新word文档目录,调用Jacob方法 前提:要将jacob包里面的dll文件存入jre的bin目录下 如果查看jre目录在哪:
	 * 打开Window->Preferences->输入jre->Installed JREs 即可查看
	 * 相关dll已经存在WebRoot->dll文件夹中
	 * 
	 * @author zhouzhifeng
	 * 
	 *         注意操作文档最好用相对路径
	 * @param filePath
	 *            文档路径
	 * @param fileName
	 *            文档名称
	 * 
	 *            2016/12/19
	 */
	public void UpdateCatalogandSave(String filePath, String fileName) {
		// 启动word进程
		ActiveXComponent app = new ActiveXComponent("Word.Application");

		// 设置不可见
		app.setProperty("Visible", new Variant(false));

		// 个人理解获取文件集的属性
		Dispatch docs = app.getProperty("Documents").toDispatch();

		// 打开相应的word文档
		Dispatch doc = Dispatch.invoke(
				docs,
				"Open",
				Dispatch.Method,
				new Object[] { filePath + fileName, new Variant(false),
						new Variant(false) }, new int[1]).toDispatch();

		// 暂时不太理解这个属性,和上面一个感觉是获取内置属性
		Dispatch activeDocument = app.getProperty("ActiveDocument")
				.toDispatch();

		// 获取目录
		Dispatch tablesOfContents = Dispatch.get(activeDocument,
				"TablesOfContents").toDispatch();

		// 获取第一个目录,若有多个目录,则传递对应的参数
		Variant tablesOfContent = Dispatch.call(tablesOfContents, "Item",
				new Variant(1));

		// 更新目录，有两个方法：Update　更新域，UpdatePageNumbers　只更新页码
		Dispatch toc = tablesOfContent.toDispatch();
		toc.call(toc, "Update");

		// 关闭word文档并保存
		Dispatch.call(doc, "Save");
		Dispatch.call(doc, "Close", new Variant(-1));

		// 退出word进程
		app.invoke("Quit", new Variant[] {});

	}
    /**
     * 该方法有缺陷的,需要本机安装了word,里面具有word插件
     * 而且对于本机要求有限制,但就目前多种方法来看,该方法转的pdf是最完美的。
     * 下面的代码是模板代码,无需特别理解。
     * 同样需要把jacob的dll文件放入jdk里面
     * 
     * @author zhouzhifeng
     * @param sfileName  要转换的doc的filepath+filename;
     * @param toFileName 转换后的pdf的filepath+filename;
     */
	public void wordToPDF(String sfileName, String toFileName)
	{

		System.out.println("启动Word...");
		long start = System.currentTimeMillis();
		ActiveXComponent app = null;
		Dispatch doc = null;
		try {
			app = new ActiveXComponent("Word.Application");
			app.setProperty("Visible", new Variant(false));
			Dispatch docs = app.getProperty("Documents").toDispatch();
			// doc = Dispatch.call(docs, "Open" , sourceFile).toDispatch();
			doc = Dispatch.invoke(
					docs,
					"Open",
					Dispatch.Method,
					new Object[] { sfileName, new Variant(false),
							new Variant(true) }, new int[1]).toDispatch();
			System.out.println("打开文档..." + sfileName);
			System.out.println("转换文档到PDF..." + toFileName);
			File tofile = new File(toFileName);
			if (tofile.exists()) {
				tofile.delete();
			}
			// Dispatch.call(doc, "SaveAs", destFile, 17);
			Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {
					toFileName, new Variant(17) }, new int[1]);
			long end = System.currentTimeMillis();
			System.out.println("转换完成..用时：" + (end - start) + "ms.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("========Error:文档转换失败：" + e.getMessage());
		} finally {
			Dispatch.call(doc, "Close", false);
			System.out.println("关闭文档");
			if (app != null)
				app.invoke("Quit", new Variant[] {});
		}
		// 如果没有这句话,winword.exe进程将不会关闭
		ComThread.Release();
	}
     
	/**
	 * @author zhouzhifeng
	 * 邮件发送:
	 * 邮件发送前提是必须建立一个官方邮箱,
	 * 同时开启POP3/SMTP服务.
	 * 开启方法以QQ邮箱为例:打开QQ邮箱->设置->账户->POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV服务->POP3/SMTP服务开启
	 * 即可获得密码.必须利用此密码
	 * 
	 * 暂无利用安全机制
	 * 
	 * 若以后需要拓展,可以开始修改
	 * @throws MessagingException 
	 * 
	 * @param recipientString  所有要发的接收人,其中请以分号;隔开例如“xxx@qq.com;xxx@qq.com...”
	 * 
	 * @param requestString 所要发的文件,其中请以逗号,隔开对应相应的邮件例如“id1,id2,id3.....”,对应各个
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	
	public String sendEmail(String recipientString,String requestString) throws MessagingException, UnsupportedEncodingException
	{
        
		//------->设置邮件属性		
		
		// 创建Properties 类用于记录邮箱的一些属性
        final Properties props = new Properties();
        // 表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.auth", "true");
        //此处填写SMTP服务器
        props.put("mail.smtp.host", officialEmailServer);
        //端口号
        props.put("mail.smtp.port", officialEmailPort);
        // 此处填写你的账号
        props.put("mail.user", officialEmailAccount);
        // 此处的密码就是前面说的16位STMP口令
        props.put("mail.password", officialEmailPassword);
        
        
        
        
        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() 
        {

            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        //------>
        
        
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        
        // 设置发件人
        InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
        message.setFrom(form);
        
        //Subject: 邮件主题
        message.setSubject("南华大学舆情系统中心邮件", "UTF-8");
        
        
        //创建节点
        BodyPart MimeBodyPart=new MimeBodyPart();
        //创建信息
        MimeBodyPart.setText("官方推送邮件,请勿回复.");
        
        //切分出邮箱
        String[] recipients=recipientString.split(";");
        
        //匹配邮箱正则
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)"
        		+ "@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        
        //先判断传过来的字符串的邮箱个数
        if(recipients.length>0)
        {
        	//存放合法的字符串
        	List<String> addressList=new ArrayList<>();
        	//检测接收人邮箱是否合法
        	for(int i=0;i<recipients.length;i++)
        	{
        		Matcher matcher = pattern.matcher(recipients[i]);
        		if(matcher.matches())
        		{
        			addressList.add(recipients[i]);
        		}
        	}
        	//存在合法的邮箱才继续
        	if(addressList.size()>0)
        	{
        		InternetAddress[] address = new InternetAddress[addressList.size()]; 
        		for(int i=0;i<addressList.size();i++)
        		{
        			address[i]=new InternetAddress(addressList.get(i));
        		}
        		//添加所有接收人.
        		message.addRecipients(Message.RecipientType.TO, address);
        		
        		String [] requests=requestString.split(",");
        		if(requests.length>0)
        		{
        		    BriefingDAO briefingDAO=new BriefingDAO();
        		    //创建节点
        		    MimeMultipart mimemultipart=new MimeMultipart();
        		    //添加节点
        		    mimemultipart.addBodyPart(MimeBodyPart);
        		    
        		    for(int i=0;i<requests.length;i++)
        		    {
        		    	
        		    	//通过id找到对应的报表
        		    	Briefing briefing=briefingDAO.findById(Integer.parseInt(requests[i]));
        		    	//附件文档
        		    	MimeBodyPart attachment= new MimeBodyPart();
        		    	//创建数据档
        		    	DataHandler dh2=new DataHandler(new FileDataSource(briefing.getEntityurl()));
        		    	//增加文件句柄
        		    	attachment.setDataHandler(dh2);  
        		        //解码
        		    	attachment.setFileName(MimeUtility.encodeText(dh2.getName()));   
        		        //增加节点
        		    	mimemultipart.addBodyPart(attachment);
        		    }
        		    //设置内容邮件节点
        		    message.setContent(mimemultipart);
        		    //开启日期
        		    Calendar cal=Calendar.getInstance();
        		    //设置时间
        		    message.setSentDate(cal.getTime());
        			//保存所有设置
        		    message.saveChanges();
        		    //最后当然发送邮件啦
        		    Transport.send(message);
        		    
        		    return "{\"message\":\"发送邮件成功\"}";
        		}	
        		
        	}
        	
        }
        	
        return "{\"message\":\"发送邮件失败\"}";
	}
}
