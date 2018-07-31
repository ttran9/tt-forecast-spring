package tran.example.weatherforecast.domain.pagination;

import lombok.Getter;
import lombok.Setter;

/**
 * Used this repository as reference: https://github.com/BranislavLazic/spring-thymeleaf-pagination
 * This holds the range of the start and end pages for the buttons to allow the user to move from
 * page to page.
 */
@Getter
@Setter
public class Pager {
    /**
     * This is how many different pages the user can select from.
     */
    private int pageButtonsToDisplay = 5;
    /**
     * The minimum page number the user can select.
     */
    private int startPage;
    /**
     * The last page number the user can select.
     */
    private int endPage;

    /**
     * Sets the start and end pages by looking at the current page passed in (this is actually
     * current page - 1). If the user selects page 1, this value will be 0.
     * @param totalPages The total number of pages in the underlying data set.
     * @param currentPage The page number the user is currently selecting minus 1.
     */
    public Pager(int totalPages, int currentPage) {
        /**
         * The logic is if you have 5 page buttons to display and the user is currently on page 4
         * (current page is 3) then the minimum (start page) would be 3 - 2 where 2 is obtained
         * from pageButtonsToDisplay / 2, and the end page would be 3 + 2.
         */
        int pagesToShowOnEachHalf = pageButtonsToDisplay / 2;

        if(totalPages <= pageButtonsToDisplay) {
            // if you have less than the total pages you can only show up to totalPages.
            setStartPage(1);
            setEndPage(totalPages);
        } else if(currentPage - pagesToShowOnEachHalf <= 0) {
            /**
             * if the current page you are at is not at least 1 more than pagesToShowOnEach half
             * the smallest page is still 1.
             */
            setStartPage(1);
            setEndPage(pageButtonsToDisplay);
        } else if(currentPage + pagesToShowOnEachHalf == totalPages) {
            /**
             * you still want to show five pages but you have hit the minimum page where if you
             * start at will give the user an option to select a page greater than the totalPages.
             */
            setStartPage(currentPage - pagesToShowOnEachHalf);
            setEndPage(totalPages);
        } else if(currentPage + pagesToShowOnEachHalf > totalPages) {
            /**
             * the current page + a half exceeds the totalPages but you still want to show five
             * pages.
             */
            setStartPage(totalPages - pageButtonsToDisplay + 1);
            setEndPage(totalPages);
        } else {
            /**
             * the currentPages value is not within +/- pageButtonsToDisplay to totalPages or
             * where the start page would be < 1.
             */
            setStartPage(currentPage - pagesToShowOnEachHalf);
            setEndPage(currentPage + pagesToShowOnEachHalf);
        }
    }
}
