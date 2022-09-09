package com.werner.util;

public class PageSupport {

	//當前頁碼，來自於用戶輸入
	private int currentPageNo = 1;
	
	//總數量(表)
	private int totalCount = 0;
	
	//頁面容量
	private int pageSize = 0;
	
	//總頁數 totalCount/pageSize(+1)
	private int totalPageCount = 1;

	public int getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(int currentPageNo) {
		if(currentPageNo > 0) {
			this.currentPageNo = currentPageNo;
		}		
	}

	public int getTotalCount() {
		return totalCount;
	}

	//OOP三大特性: 封裝、繼承、多態
	//封裝 -> 屬性私有 get/set，在set中限定一些不安定的情況
	public void setTotalCount(int totalCount) {
		if(totalCount > 0) {
			this.totalCount = totalCount;
			//設置總頁數
			this.setTotalPageCountByRs();
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if(pageSize > 0) {
			this.pageSize = pageSize;
		}	
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	
//	public void setTotalPageCountByRs() {
//		if(this.totalPageCount % this.pageSize == 0) {
//			this.totalPageCount = this.totalPageCount / this.pageSize;
//		}else if(this.totalPageCount % this.pageSize > 0){
//			this.totalPageCount = this.totalPageCount / this.pageSize +1;
//		}else {
//			this.totalPageCount = 0;
//		}
//	}
	public void setTotalPageCountByRs() {
		if(this.totalCount % this.pageSize == 0) {
			this.totalPageCount = this.totalCount / this.pageSize;
		}else if(this.totalCount % this.pageSize > 0){
			this.totalPageCount = this.totalCount / this.pageSize +1;
		}else {
			this.totalPageCount = 0;
		}
	}
}
