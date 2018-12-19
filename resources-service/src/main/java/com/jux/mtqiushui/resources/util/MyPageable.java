package com.jux.mtqiushui.resources.util;

import java.util.List;

public class MyPageable<T> {

    private List<T> content;

    private int size;

    private int page;

    private int totalPages;

    private int totalElements;

    public MyPageable(int size, int page, List<T> content) {
        this.size = size;
        this.page = page;
        this.totalElements = content.size();
        this.totalPages = content.size() % size == 0 ? content.size() / size : (content.size() / size) + 1;
        this.content = content.subList((page - 1)*size, ((page - 1)*size + size) > content.size() ? content.size() : ((page - 1)*size + size));
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "MyPageable{" +
                "content=" + content +
                ", size=" + size +
                ", page=" + page +
                ", totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                '}';
    }
}
