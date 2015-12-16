package pomonitor.analyse.segment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.CRF.CRFSegment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;

/**
 * 
 * @author luoxu 2015��12��15�� ����1:44:49 �ִʣ��õ�term term�� �������ƣ����Թ��ɡ�
 */
public class GenerateTerms {
	/*
	 * ����Term
	 */
	public List<Term> getTerms(String Content) {
		Segment segment = new CRFSegment();
		KeywordExtractor keywordExtractor = new KeywordExtractor();

		segment.enablePartOfSpeechTagging(true);
		segment.enableAllNamedEntityRecognize(true);
		List<Term> termList = segment.seg(Content);

		termList = keywordExtractor.wipeoffWords(termList);
		return termList;
	}
}
