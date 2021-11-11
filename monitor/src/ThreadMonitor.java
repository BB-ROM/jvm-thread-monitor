import java.lang.ThreadGroup;
import java.lang.Thread;

public class ThreadMonitor {
	
	public ThreadMonitor() {
		ThreadGroup root = getRoot();
		System.out.println("Root: " + root);
		printThreads(root);
	}
	
	ThreadGroup getRoot() {
		Thread thread = Thread.currentThread();
		ThreadGroup root = thread.getThreadGroup();
		while(root.getParent() != null) {
			root = root.getParent();
		}
		return root;
	}

	void printThreads(ThreadGroup parent) {
		Thread[] threads = new Thread[parent.activeCount()];
		parent.enumerate(threads);
		for(Thread t : threads) {
			System.out.println("* " + t);
		}
	}
	
}
