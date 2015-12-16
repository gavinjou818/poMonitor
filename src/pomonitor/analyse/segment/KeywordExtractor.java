package pomonitor.analyse.segment;

import java.util.Iterator;
import java.util.List;

import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
/**
 * 
 * @author luoxu
 * 2015��12��15�� ����1:44:49
 * ȥ��
 */
public class KeywordExtractor {
	/**
	 * �Ƿ�Ӧ�������term������㣬�����������ʡ����ʡ����ʡ����ݴ�
	 * 
	 * @param term
	 * @return �Ƿ�Ӧ��
	 */
	public boolean shouldInclude(Term term) {
		// ����ͣ�ô�
		if (term.nature == null)
			return false;
		String nature = term.nature.toString();
		char firstChar = nature.charAt(0);
		switch (firstChar) {
		case 'm':
		case 'b':
		case 'c':
		case 'e':
		case 'o':
		case 'p':
		case 'q':
		case 'u':
		case 'y':
		case 'z':
		case 'r':
		case 'w': {
			return false;
		}
		default: {
			if (term.word.trim().length() > 1
					&& !CoreStopWordDictionary.contains(term.word)) {
				return true;
			}
		}
			break;
		}
		return false;
	}

	public List<Term> wipeoffWords(List<Term> termList){
		Iterator<Term> iter = termList.iterator();
	        while(iter.hasNext()){
	        	Term term = iter.next();
	            if(!shouldInclude(term)){
	            	iter.remove();
	            }
	        }
		return termList;
    }
}
