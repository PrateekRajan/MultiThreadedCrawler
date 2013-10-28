import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrawlerExecutor {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {

		SetAndQueue.queue.add("http://www.google.com/");
		ExecutorService executor = Executors.newFixedThreadPool(15);
		while (true) {
			try {
				if (!SetAndQueue.queue.peek().isEmpty()) {
					Runnable worker = new CrawlerThread(
							SetAndQueue.queue.poll());
					executor.execute(worker);
					Thread.currentThread().sleep(400);
				}
			} catch (Exception e) {
				System.out.println("Empty Set");
			}
		}
	}
}
