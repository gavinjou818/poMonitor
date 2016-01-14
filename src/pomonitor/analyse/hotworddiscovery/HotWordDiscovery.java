package pomonitor.analyse.hotworddiscovery;

import java.util.ArrayList;
import java.util.List;

import pomonitor.analyse.entity.ArticleDegree;
import pomonitor.analyse.entity.ArticleShow;
import pomonitor.analyse.entity.Attitude;
import pomonitor.analyse.entity.HotWord;
import pomonitor.analyse.entity.TDArticle;
import pomonitor.analyse.entity.TDCentroid;
import pomonitor.entity.Emotionalword;
import pomonitor.entity.EmotionalwordDAO;
import pomonitor.entity.NewsTend;
import pomonitor.entity.NewsTendDAO;
import pomonitor.entity.Sensword;
import pomonitor.util.PropertiesReader;

/**
 * 话题发现
 * 
 * @author caihengyi 2015年12月14日 下午9:33:43
 */
public class HotWordDiscovery {
	private List<String> mBaseStrings;
	private final int k;
	private final double sensWeight;// 敏感词加权系数

	/**
	 * 初始化参数信息
	 */
	public HotWordDiscovery() {
		PropertiesReader propertiesReader = new PropertiesReader();
		k = Integer.parseInt(propertiesReader.getPropertyByName("k"));
		sensWeight = Integer.parseInt(propertiesReader
				.getPropertyByName("sensWeight"));
	}

	/**
	 * 根据新闻文本集合和用户的敏感词库，提取全部热词
	 * 
	 * @param articleLists
	 * @param sensitiveDict
	 * @return
	 */
	public List<HotWord> getHotWords(List<TDArticle> articleLists,
			List<Sensword> sensitiveDict) {
		TextVectorBuilder tvb = new TextVectorBuilder();
		List<TDArticle> tdArticlesWithVector = tvb.buildVectors(articleLists);
		mBaseStrings = tvb.globalFeatureCollections;
		// 对文本向量进行聚类
		List<TDCentroid> resTDCentroid = KmeansCluster.ArticleCluster(k,
				tdArticlesWithVector);
		// 对聚类结果进行处理得到全部的HotWord
		List<HotWord> sumHotWords = new ArrayList<HotWord>();
		for (TDCentroid tdCentroid : resTDCentroid) {
			List<HotWord> t = getHotWordsFromCentroid(tdCentroid, sensitiveDict);
			sumHotWords.addAll(t);
		}
		return sumHotWords;
	}

	/**
	 * 从某一类别中提取该类别的热词
	 * 
	 * @param tdc
	 * @param sensitiveDict
	 * @return
	 */
	public List<HotWord> getHotWordsFromCentroid(TDCentroid tdc,
			List<Sensword> sensitiveDict) {
		NewsTendDAO newsTendDAO = new NewsTendDAO();
		EmotionalwordDAO emotionalwordDAO = new EmotionalwordDAO();
		List<HotWord> hotWordsList = new ArrayList<HotWord>(mBaseStrings.size());
		for (int i = 0; i < mBaseStrings.size(); i++) {
			HotWord t = new HotWord();
			t.articleViews = new ArrayList<>();
			t.weight = 0.0;
			t.setAttitude(Attitude.NEUTRAL);
			t.setContent("");
			t.setSensitiveWords(false);
			hotWordsList.add(t);
		}
		double[] base = tdc.GroupedArticle.get(0).vectorSpace;
		for (int i = 1; i < tdc.GroupedArticle.size(); i++) {
			ArticleShow as = new ArticleShow();
			/****** 每条向量代表一篇文章 *****/
			as.setComeFrom(tdc.GroupedArticle.get(i).getComeFrom());
			as.setDescription(tdc.GroupedArticle.get(i).getDescription());
			as.setTimestamp(tdc.GroupedArticle.get(i).getTimestamp());
			as.setTitle(tdc.GroupedArticle.get(i).getTitle());
			as.setUrl(tdc.GroupedArticle.get(i).getUrl());

			// 查询newstend表，获取新闻的倾向性相关信息
			NewsTend nt;
			try {
				nt = newsTendDAO
						.findByNewsId(tdc.GroupedArticle.get(i).getId()).get(0);
				switch (nt.getTendclass()) {
				case 0:
					as.setAttitude(Attitude.NEUTRAL);
					as.setDegree(ArticleDegree.O);
					break;
				case 1:
					as.setAttitude(Attitude.PRAISE);
					as.setDegree(ArticleDegree.A);
					break;
				case -1:
					as.setAttitude(Attitude.DEROGATORY);
					as.setDegree(ArticleDegree.A);
				case 2:
					as.setAttitude(Attitude.PRAISE);
					as.setDegree(ArticleDegree.B);
					break;
				case -2:
					as.setAttitude(Attitude.DEROGATORY);
					as.setDegree(ArticleDegree.B);
					break;
				case 3:
					as.setAttitude(Attitude.PRAISE);
					as.setDegree(ArticleDegree.C);
					break;
				case -3:
					as.setAttitude(Attitude.DEROGATORY);
					as.setDegree(ArticleDegree.C);
					break;
				default:
					as.setAttitude(Attitude.NEUTRAL);
					as.setDegree(ArticleDegree.O);
					break;
				}
			} catch (Exception e) {
				as.setAttitude(Attitude.NEUTRAL);
				as.setDegree(ArticleDegree.O);
			}

			/*****************************************************************/
			for (int j = 0; j < base.length; j++) {
				if (tdc.GroupedArticle.get(i).vectorSpace[j] > base[j])
					as.heat += tdc.GroupedArticle.get(i).vectorSpace[j];
			}
			for (int j = 0; j < base.length; j++) {
				if (tdc.GroupedArticle.get(i).vectorSpace[j] > base[j]) {
					hotWordsList.get(j).articleViews.add(as);
					hotWordsList.get(j).weight += tdc.GroupedArticle.get(i).vectorSpace[j];
					hotWordsList.get(j).setContent(mBaseStrings.get(j));
				}
			}
		}
		for (HotWord word : hotWordsList) {
			// 设置所属类别编号
			word.setBelongto(tdc.CentroidNumber);
			// 是否是敏感词
			if (sensitiveDict.contains(word)) {
				word.setSensitiveWords(true);
				word.weight = word.weight * sensWeight;
			}
			// 褒贬含义
			List<Emotionalword> emoWords = emotionalwordDAO.findByWord(word);
			if (!(emoWords.size() > 0))
				word.setAttitude(Attitude.NEUTRAL);
			else {
				switch (emoWords.get(0).getPolarity()) {
				case 0:
					word.setAttitude(Attitude.NEUTRAL);
					break;
				case -1:
					word.setAttitude(Attitude.DEROGATORY);
					break;
				case 1:
					word.setAttitude(Attitude.PRAISE);
					break;
				default:
					word.setAttitude(Attitude.NEUTRAL);
					break;
				}
			}
		}
		return hotWordsList;
	}

	public static double getMax(double[] arr) {
		double maxVar = arr[0];
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > maxVar)
				maxVar = arr[i];
		}
		return maxVar;
	}

	public static double getMin(double[] arr) {
		double minVar = arr[0];
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] < minVar)
				minVar = arr[i];
		}
		return minVar;
	}

	public static String vectorToString(double[] arr) {
		String str = "";
		for (double d : arr) {
			str += d + "->";
		}
		return str;
	}
}
