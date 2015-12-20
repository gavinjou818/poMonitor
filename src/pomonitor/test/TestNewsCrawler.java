package pomonitor.test;

import org.junit.Test;

import pomonitor.clawer.NewsCrawler;
import pomonitor.clawer.newsanalyse.*;

public class TestNewsCrawler {
	@Test
	public void testClawlerAll() {
		NewsCrawler clawer = new NewsCrawler("E:/test/");
		SinaAnalyse analyse = new SinaAnalyse("����", true);
		RedNetAnalyse redAnalyse = new RedNetAnalyse("����", true);
		RedNetAnalyseF redAnalysef = new RedNetAnalyseF("������̳", false);
		ZhongXinAnalyse zxAnalyse = new ZhongXinAnalyse("������", true);

		HuaShengAnalyse hsAnalyse = new HuaShengAnalyse("��������", true);
		GuangMing gmAnalyse = new GuangMing("������", true);
		WeiXinAnalyse weixinAnalyse = new WeiXinAnalyse("΢��", true);
		XinHuaAnalyse xhAnalyse = new XinHuaAnalyse("�»���", true);
		WangYi wangyi = new WangYi("����", true);
		TengXunAnalyse tengXun = new TengXunAnalyse("��Ѷ", true);
		FenghuangAnalyse fenghuang = new FenghuangAnalyse("���", true);
		SouHuAnalyse souhu = new SouHuAnalyse("�Ѻ�", true);
		clawer.addAnalyse(analyse);
		// clawer.addAnalyse(redAnalysef);
		// clawer.addAnalyse(redAnalyse);
		// ʧ�� clawer.addAnalyse(zxAnalyse);
		// clawer.addAnalyse(gmAnalyse);
		// clawer.addAnalyse(hsAnalyse);
		// �д��� clawer.addAnalyse(weixinAnalyse);
		// clawer.addAnalyse(xhAnalyse);
		// clawer.addAnalyse(wangyi);
		// clawer.addAnalyse(fenghuang);
		// clawer.addAnalyse(souhu);
		// clawer.addAnalyse(tengXun);

		clawer.clawerAll("�ϻ���ѧ", false);
		clawer.start(10);

	}
}
