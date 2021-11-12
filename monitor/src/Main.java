public class Main {

	public static void main(String[] args) {
		ThreadMonitor monitor = new ThreadMonitor();
        	Thread listingThread = new Thread(monitor);
        
        	listingThread.start();
	}

}
