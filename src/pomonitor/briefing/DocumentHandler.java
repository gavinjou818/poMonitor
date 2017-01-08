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
 * 文档操作类,例如创建word,加载freemarket加载器, 转word到pdf等,创建word目录等
 * 
 * 注意 范用到jacob 方法的文件调用都会有平台限制,同时必须在平台安装word，
 * 有局限性,当需要移植到其他平台时,必须考虑到这个问题。或者当有新方法必须更换.
 * 
 * @author zhouzhifeng
 * 
 */
public class DocumentHandler {
	// freemarket配置加载器
	private Configuration configuration = null;

	// 初始化参数,并设置默认编码
	public DocumentHandler() {
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
	public void wordToPDF(String sfileName, String toFileName) {

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

}
