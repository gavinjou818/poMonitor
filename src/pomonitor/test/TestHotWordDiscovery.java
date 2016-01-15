package pomonitor.test;

import java.util.List;

import pomonitor.analyse.HotWordDiscoveryAnalyse;
import pomonitor.analyse.entity.TDArticle;
import pomonitor.analyse.entity.TDCentroid;
import pomonitor.analyse.hotworddiscovery.HotWordDiscovery;
import pomonitor.analyse.hotworddiscovery.KmeansCluster;
import pomonitor.analyse.hotworddiscovery.TextVectorBuilder;
import pomonitor.util.ConsoleLog;

/**
 * �����ȴʷ��ֵ���ع���
 * 
 * @author hengyi
 * 
 */
public class TestHotWordDiscovery {
	public static void main(String[] args) {
		HotWordDiscoveryAnalyse tda = new HotWordDiscoveryAnalyse();
		List<TDArticle> lists = tda.getArticlesBetweenDate("2008-09-10",
				"2009-10-10");
		TextVectorBuilder tvb = new TextVectorBuilder();

		long start = System.currentTimeMillis();
		lists = tvb.buildVectors(lists);
		long end = System.currentTimeMillis();
		ConsoleLog.PrintInfo(TestHotWordDiscovery.class, "�����ı������Ļ���ʱ��Ϊ"
				+ (end - start) + "����");
		List<String> baseStr = tvb.globalFeatureCollections;
		long startClus = System.currentTimeMillis();
		List<TDCentroid> resCluster = KmeansCluster.ArticleCluster(15, lists);
		long endClus = System.currentTimeMillis();
		ConsoleLog.PrintInfo(TestHotWordDiscovery.class, "���໨�ѵ�ʱ��Ϊ"
				+ (endClus - startClus) + "����");
		// TopicDiscovery td = new TopicDiscovery();
		System.out
				.println("ȫ�ֵ�������ϴ�С��:" + tvb.globalFeatureCollections.size());

		HotWordDiscovery hotWordDiscovery = new HotWordDiscovery();
	}

	/**
	 * ���һ����������ֵ
	 * 
	 * @param arr
	 * @return
	 */
	private static double getMax(double[] arr) {
		double maxVar = arr[0];
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > maxVar)
				maxVar = arr[i];
		}
		return maxVar;
	}

	/**
	 * ���������ַ�������ʽ��ʾ
	 * 
	 * @param arr
	 * @return
	 */
	public static String vectorToString(double[] arr) {
		String str = "";
		for (double d : arr) {
			str += d + "->";
		}
		return str;
	}
}
