import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaowutang on 5/1/18.
 */
public class Crawler {

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";

    public static void main(String[] args) throws IOException {
        Crawler.Crawl("https://sfbay.craigslist.org/d/apts-housing-for-rent/search/apa");
        System.out.println("Job Success");
    }

    // title/rent price/detail url/hood/
    public static void Crawl(String url) throws IOException{
        Document doc = Jsoup.connect(url).userAgent(USER_AGENT).timeout(1000).get();
        Elements items = doc.getElementsByClass("result-title hdrlnk");
        System.out.println("num of items: " + items.size());
        List<String> titleList = new ArrayList<>();
        List<String> priceList = new ArrayList<>();
        List<String> urlList = new ArrayList<>();
        List<String> hoodList = new ArrayList<>();
        String title;
        String price;
        String detailURL;
        String hood;

        for (int i = 1; i <= items.size(); i++) {
            Elements titleElements = doc.select("#sortable-results > ul > li:nth-child(" + i + " ) > p > a");
            if (titleElements != null && titleElements.size() > 0) {
                title = titleElements.first().text();
            } else {
                title = "NULL";
            }
            titleList.add(title);
            Elements priceElements = doc.select("#sortable-results > ul > li:nth-child(" + i + ") > p > span.result-meta > span.result-price");
            if (priceElements != null && priceElements.size() > 0) {
                price = priceElements.first().text();
            } else {
                price = "NULL";
            }
            priceList.add(price);
            Elements detailURLElements = doc.select("#sortable-results > ul > li:nth-child(" + i + ") > p > a");
            if (detailURLElements != null && detailURLElements.size() > 0) {
                detailURL = detailURLElements.first().attr("href");
            } else {
                detailURL = "NULL";
            }
            urlList.add(detailURL);
            Elements hoodElements = doc.select("#sortable-results > ul > li:nth-child(" + i + ") > p > span.result-meta > span.result-hood");
            if (hoodElements != null && hoodElements.size() > 0) {
                hood = hoodElements.first().text();
            } else {
                hood = "NULL";
            }
            hoodList.add(hood);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("rentInfo.txt"))) {
            int index = 0;
            while (index < items.size()) {
                title = titleList.get(index);
                price = priceList.get(index);
                detailURL = urlList.get(index);
                hood = hoodList.get(index);
                bw.append(title);
                bw.append(" ");
                bw.append(price);
                bw.newLine();
                bw.append(detailURL);
                bw.append(hood);
                bw.newLine();
                index++;
            }
            System.out.println("Finished Writing!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
