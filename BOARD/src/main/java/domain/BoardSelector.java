package domain;

import java.net.URLEncoder;

import common.BindTarget;

@BindTarget
public class BoardSelector {
	private String keyword = ""; //검색키워드
	private String option = ""; //검색옵션(타이틀, 작성자, 타이틀 + 작성자)
	private int page = 1;  //요청페이지
	private int beginPage; //시작페이지
	private int endPage; //마지막페이지
	
	private int totalCnt = 0; //DB조회 게시글 수
	private int navSize = 10;  //하단 네비게이션의 요소 갯수
	private int pageSize = 10; //한 페이지당 보여지는 게시글 수
	
	private boolean hasNext; //네비게이션 바의 다음페이지
	private boolean hasPrev; //네비게이션 바의 이전페이지
	
	public BoardSelector() {}
		
	public void calcPageInfo() {
		beginPage = (page - 1) / navSize * navSize + 1;
		endPage   = Integer.min(beginPage + navSize - 1, totalCnt / pageSize + (totalCnt % pageSize == 0 ? 0 : 1)); 		
		page      = Integer.min(page, endPage);
		hasNext   = endPage < (totalCnt / pageSize + (totalCnt % pageSize == 0 ? 0 : 1));
		hasPrev   = beginPage != 1;	
	}
	
	@Override
	public String toString() {
		return "BoardSelector [keyword=" + keyword + ", option=" + option + ", page=" + page + ", beginPage="
				+ beginPage + ", endPage=" + endPage + ", hasNext=" + hasNext + ", hasPrev=" + hasPrev + ", navSize="
				+ navSize + ", pageSize=" + pageSize + ", totalSize=" +totalCnt + "]";
	}
	
	public String getQueryString(int pageNum) {
		return 	"page=" + pageNum + "&" +
				"keyword=" + keyword + "&" +
				"option=" + option;
	}
	
	public String queryString() {
		return getQueryString(this.page);
	}
	
	public String queryString(String charset) throws Exception {
		return getQueryString(this.page, charset);
	}
	
	public String getQueryString(int pageNum, String charset) throws Exception{
		return 	"page=" + pageNum + "&" +
				"keyword=" + URLEncoder.encode(keyword, charset) + "&" +
				"option=" + option;
	}
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String options) {
		this.option = options;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getBeginPage() {
		return beginPage;
	}
	public void setBeginPage(int beginPage) {
		this.beginPage = beginPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public boolean isHasNext() {
		return hasNext;
	}
	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
	public boolean isHasPrev() {
		return hasPrev;
	}
	public void setHasPrev(boolean hasPrev) {
		this.hasPrev = hasPrev;
	}
	public int getNavSize() {
		return navSize;
	}
	public void setNavSize(int navSize) {
		this.navSize = navSize;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

}
