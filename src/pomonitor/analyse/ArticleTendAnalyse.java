package pomonitor.analyse;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import pomonitor.analyse.articletend.ArticleTendAnalyseRealize;
import pomonitor.analyse.entity.TendAnalyseArticle;
import pomonitor.entity.EntityManagerHelper;
import pomonitor.entity.News;
import pomonitor.entity.NewsDAO;
import pomonitor.entity.NewsTend;
import pomonitor.entity.NewsTendDAO;

/**
 * 
 * @author xiaoyulun 2016��1��5�� ����11:44:52
 */
public class ArticleTendAnalyse {

    public static void tendAnalyse(String start_time, String end_time,
	    String UserId) {
	NewsDAO newsDAO = new NewsDAO();
	List<News> newsList = newsDAO.findBetweenDate(start_time, end_time);
	HashMap<String, Float> hashMap = new HashMap<>();
	ArticleTendAnalyseRealize analyseRealize = new ArticleTendAnalyseRealize();
	for (News news : newsList) {
	    if (news.getIsFinsh() == 0) {
		continue;
	    }
	    TendAnalyseArticle tendArticle = new TendAnalyseArticle();

	    try {
		tendArticle = analyseRealize.analyseArticleTend(news);
	    } catch (Exception e) {
		// TODO: handle exception
		System.out.println("�ı�����");
		continue;
	    }
	    String webName = tendArticle.getWeb();
	    Float score = tendArticle.getTendScore();
	    InsertNewsTend(news, newsDAO, score);
	}
    }

    /**
     * ��newstend���з������� showWebTend
     * 
     * @param start_time
     * @param end_time
     * @param UserId
     * @return
     */
    public static HashMap<String, Float> showWebTend(String start_time,
	    String end_time, String UserId) {
	NewsTendDAO newsTendDAO = new NewsTendDAO();
	List<NewsTend> newsList = newsTendDAO.findBetweenDate(start_time,
		end_time);
	HashMap<String, Float> hashMap = new HashMap<>();
	for (NewsTend news : newsList) {
	    String webName = news.getWeb();
	    Float score = news.getTendscore();

	    if (hashMap.get(webName) == null) {
		hashMap.put(webName, score);
	    } else {
		hashMap.put(webName, hashMap.get(webName) + score);
	    }
	}
	return hashMap;
    }

    /**
     * �������������ţ�����newstend����,������news��IsFinished�ֶ� InsertNewsTend
     * 
     * @param news
     * @param newsDAO
     * @param tendScore
     */
    private static void InsertNewsTend(News news, NewsDAO newsDAO,
	    Float tendScore) {
	// news.setIsFinsh(0);
	NewsTend newsTend = new NewsTend();
	newsTend.setDate(news.getTime());
	try {
	    newsTend.setWeb(new String(gbk2utf8(news.getWeb()), "utf-8"));
	    System.out.println(newsTend.getWeb()
		    + "*****************************88888");
	} catch (UnsupportedEncodingException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	newsTend.setTendscore(tendScore);
	newsTend.setNewsId(news.getRelId());
	newsTend.setTendclass(0);
	NewsTendDAO newsTendDAO = new NewsTendDAO();
	try {
	    EntityManagerHelper.beginTransaction();
	    newsTendDAO.save(newsTend);
	    EntityManagerHelper.commit();

	    // EntityManagerHelper.beginTransaction();
	    // newsDAO.update(news);
	    // EntityManagerHelper.commit();

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public static byte[] gbk2utf8(String chenese) {

	// Step 1: �õ�GBK�����µ��ַ����飬һ�������ַ���Ӧ�����һ��c[i]
	char c[] = chenese.toCharArray();

	// Step 2: UTF-8ʹ��3���ֽڴ��һ�������ַ������Գ��ȱ���Ϊ�ַ���3��
	byte[] fullByte = new byte[3 * c.length];

	// Step 3: ѭ�����ַ���GBK����ת����UTF-8����
	for (int i = 0; i < c.length; i++) {

	    // Step 3-1�����ַ���ASCII����ת����2����ֵ
	    int m = (int) c[i];
	    String word = Integer.toBinaryString(m);
	    // System.out.println(word);

	    // Step 3-2����2����ֵ����16λ(2���ֽڵĳ���)
	    StringBuffer sb = new StringBuffer();
	    int len = 16 - word.length();
	    for (int j = 0; j < len; j++) {
		sb.append("0");
	    }
	    // Step 3-3���õ����ַ����յ�2����GBK����
	    // ���ƣ�1000 0010 0111 1010
	    sb.append(word);

	    // Step 3-4����ؼ��Ĳ��裬����UTF-8�ĺ��ֱ���������ֽ�
	    // ��1110��ͷ�����ֽ���10��ͷ����3�ֽ���10��ͷ����ԭʼ��2����
	    // �ַ����в����־λ�����յĳ��ȴ�16--->16+3+2+2=24��
	    sb.insert(0, "1110");
	    sb.insert(8, "10");
	    sb.insert(16, "10");
	    // System.out.println(sb.toString());

	    // Step 3-5�����µ��ַ������зֶν�ȡ����Ϊ3���ֽ�
	    String s1 = sb.substring(0, 8);
	    String s2 = sb.substring(8, 16);
	    String s3 = sb.substring(16);

	    // Step 3-6�����Ĳ��裬�Ѵ���3���ֽڵ��ַ�����2���Ƶķ�ʽ
	    // ����ת�������2���Ƶ���������ת����16����ֵ
	    byte b0 = Integer.valueOf(s1, 2).byteValue();
	    byte b1 = Integer.valueOf(s2, 2).byteValue();
	    byte b2 = Integer.valueOf(s3, 2).byteValue();

	    // Step 3-7����ת�����3���ֽڰ�˳���ŵ��ֽ�����Ķ�Ӧλ��
	    byte[] bf = new byte[3];
	    bf[0] = b0;
	    bf[1] = b1;
	    bf[2] = b2;

	    fullByte[i * 3] = bf[0];
	    fullByte[i * 3 + 1] = bf[1];
	    fullByte[i * 3 + 2] = bf[2];

	    // Step 3-8�����ؼ���������һ�������ַ�
	}
	return fullByte;
    }
}
