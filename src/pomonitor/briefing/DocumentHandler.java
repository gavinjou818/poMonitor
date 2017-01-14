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
 * ʹ��ǰ���Ҫ:
 * 
 1.����baes64Ȩ��
 �Ҽ�poMonitor->Build Path->JRE system Library(�������ԭ���Լ���JRE����->
 Access rules->�ұ�һ���и�Edit���->Add..->Resolution:Accessible,Rule Pattern:**��>����
 2.����Jacob��
 ��jacob�������dll�ļ�����(������ʹ�õ�)jre��binĿ¼��
 ����鿴jreĿ¼����:
 ��Window->Preferences->����jre->Installed JREs
 3.��������ڴ�(�ⲻ�Ǳ����������,������ϵͳ��Ҫ����,�����������)
 Window->Preferences->����jre->Installed JREs->����(������ʹ�õ�)jre
 ->Edit->Default VM arguments��д-Xms128M -Xmx512M;
 * 
 */

/**
 * �ĵ�������,���紴��word,����freemarket������, תword��pdf��,����wordĿ¼,�����ʼ���
 * 
 * ע�� ���õ�jacob �������ļ����ö�����ƽ̨����,ͬʱ������ƽ̨��װword��
 * �о�����,����Ҫ��ֲ������ƽ̨ʱ,���뿼�ǵ�������⡣���ߵ����·����������.
 * 
 * @author zhouzhifeng
 * 
 */
public class DocumentHandler 
{
	// freemarket���ü�����
	private Configuration configuration = null;
	
	//�ٷ������˺�
	private  static String officialEmailAccount;
	//�ٷ���������
	private  static String officialEmailPassword;
    //�ٷ�����˿�
	private  static String officialEmailPort;
	//�ٷ����������
	private  static String officialEmailServer;

	static
	{    
		PropertiesReader propertiesReader = new PropertiesReader();
		
		officialEmailAccount = propertiesReader.getPropertyByName("officialEmailAccount");
		officialEmailPassword = propertiesReader.getPropertyByName("officialEmailPassword");
		officialEmailPort =propertiesReader.getPropertyByName("officialEmailPort");
		officialEmailServer=propertiesReader.getPropertyByName("officialEmailServer");
	}
	
	
	// ��ʼ������,������Ĭ�ϱ���
	public DocumentHandler() 
	{
		// ��������ʵ��
		configuration = new Configuration();
		// ���ñ���
		configuration.setDefaultEncoding("utf-8");
	}

	/**
	 * @Desc:����word�ļ�
	 * @author zhouzhifeng
	 * @Date 2016/12/16 ģ�嶼����TEMPLATE���� �ȴ�/TEMPLATE��ȡ���е�һ��ģ��.ftl Ȼ����в�������ӳ��
	 * @param dataMap
	 *            key->value ��Ӧģ���EA�����һһ��Ӧ
	 * @param templateName
	 *            ģ�������.ftl
	 * @param filePath
	 *            ����ļ�·��
	 * @param fileName
	 *            ����ļ�����
	 */
	public void createWord(BriefingData briefing,String userPath) {
		// ����ģ���Ŀ¼
		configuration.setClassForTemplateLoading(this.getClass(), "/TEMPLATE");
		// ��ȡָ��ģ��
		Template template = null;
		try {
			template = configuration.getTemplate(briefing.getTemplateName());

		} catch (Exception e) {
			e.printStackTrace();
		}

		// ����ģ�������.
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

		// ӳ��ģ�岢�����ָ��λ��
		try {
			template.process(briefing.getDataMap(), out);
			out.close();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ����·��,���ڵ���ͼƬ����
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
	 * �÷������ڸ���word�ĵ�Ŀ¼,����Jacob���� ǰ��:Ҫ��jacob�������dll�ļ�����jre��binĿ¼�� ����鿴jreĿ¼����:
	 * ��Window->Preferences->����jre->Installed JREs ���ɲ鿴
	 * ���dll�Ѿ�����WebRoot->dll�ļ�����
	 * 
	 * @author zhouzhifeng
	 * 
	 *         ע������ĵ���������·��
	 * @param filePath
	 *            �ĵ�·��
	 * @param fileName
	 *            �ĵ�����
	 * 
	 *            2016/12/19
	 */
	public void UpdateCatalogandSave(String filePath, String fileName) {
		// ����word����
		ActiveXComponent app = new ActiveXComponent("Word.Application");

		// ���ò��ɼ�
		app.setProperty("Visible", new Variant(false));

		// ��������ȡ�ļ���������
		Dispatch docs = app.getProperty("Documents").toDispatch();

		// ����Ӧ��word�ĵ�
		Dispatch doc = Dispatch.invoke(
				docs,
				"Open",
				Dispatch.Method,
				new Object[] { filePath + fileName, new Variant(false),
						new Variant(false) }, new int[1]).toDispatch();

		// ��ʱ��̫����������,������һ���о��ǻ�ȡ��������
		Dispatch activeDocument = app.getProperty("ActiveDocument")
				.toDispatch();

		// ��ȡĿ¼
		Dispatch tablesOfContents = Dispatch.get(activeDocument,
				"TablesOfContents").toDispatch();

		// ��ȡ��һ��Ŀ¼,���ж��Ŀ¼,�򴫵ݶ�Ӧ�Ĳ���
		Variant tablesOfContent = Dispatch.call(tablesOfContents, "Item",
				new Variant(1));

		// ����Ŀ¼��������������Update��������UpdatePageNumbers��ֻ����ҳ��
		Dispatch toc = tablesOfContent.toDispatch();
		toc.call(toc, "Update");

		// �ر�word�ĵ�������
		Dispatch.call(doc, "Save");
		Dispatch.call(doc, "Close", new Variant(-1));

		// �˳�word����
		app.invoke("Quit", new Variant[] {});

	}
    /**
     * �÷�����ȱ�ݵ�,��Ҫ������װ��word,�������word���
     * ���Ҷ��ڱ���Ҫ��������,����Ŀǰ���ַ�������,�÷���ת��pdf���������ġ�
     * ����Ĵ�����ģ�����,�����ر���⡣
     * ͬ����Ҫ��jacob��dll�ļ�����jdk����
     * 
     * @author zhouzhifeng
     * @param sfileName  Ҫת����doc��filepath+filename;
     * @param toFileName ת�����pdf��filepath+filename;
     */
	public void wordToPDF(String sfileName, String toFileName)
	{

		System.out.println("����Word...");
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
			System.out.println("���ĵ�..." + sfileName);
			System.out.println("ת���ĵ���PDF..." + toFileName);
			File tofile = new File(toFileName);
			if (tofile.exists()) {
				tofile.delete();
			}
			// Dispatch.call(doc, "SaveAs", destFile, 17);
			Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {
					toFileName, new Variant(17) }, new int[1]);
			long end = System.currentTimeMillis();
			System.out.println("ת�����..��ʱ��" + (end - start) + "ms.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("========Error:�ĵ�ת��ʧ�ܣ�" + e.getMessage());
		} finally {
			Dispatch.call(doc, "Close", false);
			System.out.println("�ر��ĵ�");
			if (app != null)
				app.invoke("Quit", new Variant[] {});
		}
		// ���û����仰,winword.exe���̽�����ر�
		ComThread.Release();
	}
     
	/**
	 * @author zhouzhifeng
	 * �ʼ�����:
	 * �ʼ�����ǰ���Ǳ��뽨��һ���ٷ�����,
	 * ͬʱ����POP3/SMTP����.
	 * ����������QQ����Ϊ��:��QQ����->����->�˻�->POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV����->POP3/SMTP������
	 * ���ɻ������.�������ô�����
	 * 
	 * �������ð�ȫ����
	 * 
	 * ���Ժ���Ҫ��չ,���Կ�ʼ�޸�
	 * @throws MessagingException 
	 * 
	 * @param recipientString  ����Ҫ���Ľ�����,�������Էֺ�;�������硰xxx@qq.com;xxx@qq.com...��
	 * 
	 * @param requestString ��Ҫ�����ļ�,�������Զ���,������Ӧ��Ӧ���ʼ����硰id1,id2,id3.....��,��Ӧ����
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	
	public String sendEmail(String recipientString,String requestString) throws MessagingException, UnsupportedEncodingException
	{
        
		//------->�����ʼ�����		
		
		// ����Properties �����ڼ�¼�����һЩ����
        final Properties props = new Properties();
        // ��ʾSMTP�����ʼ���������������֤
        props.put("mail.smtp.auth", "true");
        //�˴���дSMTP������
        props.put("mail.smtp.host", officialEmailServer);
        //�˿ں�
        props.put("mail.smtp.port", officialEmailPort);
        // �˴���д����˺�
        props.put("mail.user", officialEmailAccount);
        // �˴����������ǰ��˵��16λSTMP����
        props.put("mail.password", officialEmailPassword);
        
        
        
        
        // ������Ȩ��Ϣ�����ڽ���SMTP���������֤
        Authenticator authenticator = new Authenticator() 
        {

            protected PasswordAuthentication getPasswordAuthentication() {
                // �û���������
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        //------>
        
        
        // ʹ�û������Ժ���Ȩ��Ϣ�������ʼ��Ự
        Session mailSession = Session.getInstance(props, authenticator);
        
        // �����ʼ���Ϣ
        MimeMessage message = new MimeMessage(mailSession);
        
        // ���÷�����
        InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
        message.setFrom(form);
        
        //Subject: �ʼ�����
        message.setSubject("�ϻ���ѧ����ϵͳ�����ʼ�", "UTF-8");
        
        
        //�����ڵ�
        BodyPart MimeBodyPart=new MimeBodyPart();
        //������Ϣ
        MimeBodyPart.setText("�ٷ������ʼ�,����ظ�.");
        
        //�зֳ�����
        String[] recipients=recipientString.split(";");
        
        //ƥ����������
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)"
        		+ "@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        
        //���жϴ��������ַ������������
        if(recipients.length>0)
        {
        	//��źϷ����ַ���
        	List<String> addressList=new ArrayList<>();
        	//�������������Ƿ�Ϸ�
        	for(int i=0;i<recipients.length;i++)
        	{
        		Matcher matcher = pattern.matcher(recipients[i]);
        		if(matcher.matches())
        		{
        			addressList.add(recipients[i]);
        		}
        	}
        	//���ںϷ�������ż���
        	if(addressList.size()>0)
        	{
        		InternetAddress[] address = new InternetAddress[addressList.size()]; 
        		for(int i=0;i<addressList.size();i++)
        		{
        			address[i]=new InternetAddress(addressList.get(i));
        		}
        		//������н�����.
        		message.addRecipients(Message.RecipientType.TO, address);
        		
        		String [] requests=requestString.split(",");
        		if(requests.length>0)
        		{
        		    BriefingDAO briefingDAO=new BriefingDAO();
        		    //�����ڵ�
        		    MimeMultipart mimemultipart=new MimeMultipart();
        		    //��ӽڵ�
        		    mimemultipart.addBodyPart(MimeBodyPart);
        		    
        		    for(int i=0;i<requests.length;i++)
        		    {
        		    	
        		    	//ͨ��id�ҵ���Ӧ�ı���
        		    	Briefing briefing=briefingDAO.findById(Integer.parseInt(requests[i]));
        		    	//�����ĵ�
        		    	MimeBodyPart attachment= new MimeBodyPart();
        		    	//�������ݵ�
        		    	DataHandler dh2=new DataHandler(new FileDataSource(briefing.getEntityurl()));
        		    	//�����ļ����
        		    	attachment.setDataHandler(dh2);  
        		        //����
        		    	attachment.setFileName(MimeUtility.encodeText(dh2.getName()));   
        		        //���ӽڵ�
        		    	mimemultipart.addBodyPart(attachment);
        		    }
        		    //���������ʼ��ڵ�
        		    message.setContent(mimemultipart);
        		    //��������
        		    Calendar cal=Calendar.getInstance();
        		    //����ʱ��
        		    message.setSentDate(cal.getTime());
        			//������������
        		    message.saveChanges();
        		    //���Ȼ�����ʼ���
        		    Transport.send(message);
        		    
        		    return "{\"message\":\"�����ʼ��ɹ�\"}";
        		}	
        		
        	}
        	
        }
        	
        return "{\"message\":\"�����ʼ�ʧ��\"}";
	}
}
