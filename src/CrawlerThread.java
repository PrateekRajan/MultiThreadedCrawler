import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerThread implements Runnable {

	private String seed = null;

	public CrawlerThread(String seed) {
		this.seed = seed;
	}

	@SuppressWarnings("static-access")
	private static void crawl(String url) {
		Document doc;
		try {
			// need http protocol
			doc = Jsoup.connect(url).userAgent("Mozilla").get();
			// get all links
			Elements links = doc.select("a[href]");
			for (Element link : links) {
				if (link.toString().contains("www.")
						&& link.toString().contains(".com")
						&& (link.toString().contains("http://") || link
								.toString().contains("https://"))) {
					synchronized (SetAndQueue.queue) {

						if (!SetAndQueue.queue.contains(link.attr("href"))
								&& !SetAndQueue.hs.contains(link.attr("href"))) {

							SetAndQueue.hs.add(link.attr("href"));

							SetAndQueue.queue.add(link.attr("href"));
						} else {
							if (!SetAndQueue.queue.peek().contains("www.")
									|| !SetAndQueue.queue.peek().contains(
											".com")
									|| !SetAndQueue.queue.peek().startsWith(
											"http")) {
								SetAndQueue.queue.poll();
							}
						}
					}
				}
			}
		} catch (IOException e) {
			long heapSize = Runtime.getRuntime().totalMemory();
			System.out.println("Heap Size = " + heapSize);
		}

	}

	@Override
	public void run() {
		crawl(seed);
		System.out.println(Thread.currentThread().getName() + " "
				+ SetAndQueue.queue.peek());
	}
}
