package pomonitor.test;

import pomonitor.statistics.BriefingSummarize;

public class TestBriefingSummarize 
{

	public static void main(String[] args) {
		
          BriefingSummarize briefingSummarize=new BriefingSummarize();
          System.out.println(briefingSummarize.getPreviewChart("568,999,1071,1563,2041"));
	}

}
