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
import java.util.Map;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

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
 * �ĵ�������,���紴��word,����freemarket������, תword��pdf��,����wordĿ¼��
 * 
 * ע�� ���õ�jacob �������ļ����ö�����ƽ̨����,ͬʱ������ƽ̨��װword��
 * �о�����,����Ҫ��ֲ������ƽ̨ʱ,���뿼�ǵ�������⡣���ߵ����·����������.
 * 
 * @author zhouzhifeng
 * 
 */
public class DocumentHandler {
	// freemarket���ü�����
	private Configuration configuration = null;

	// ��ʼ������,������Ĭ�ϱ���
	public DocumentHandler() {
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
	public void wordToPDF(String sfileName, String toFileName) {

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

}
