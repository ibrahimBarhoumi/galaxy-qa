package fr.lovotech.galaxy.qa.backoffice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {

    private int totalPages;
    private long totalItems;
    private int currentPage;
    private boolean first;
    private boolean last;
    private int itemsPerPage;
    private int pageSize;
    private List<T> content;

    public PageResponse(Page<T> page) {
        first = page.isFirst();
        last = page.isLast();
        currentPage = page.getNumber() + 1;
        pageSize = page.getSize();
        totalPages = page.getTotalPages();
        totalItems = page.getTotalElements();
        itemsPerPage = page.getPageable().getPageSize();
        content = page.getContent();
    }


}