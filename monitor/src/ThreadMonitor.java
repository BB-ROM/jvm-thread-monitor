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
			System.out.println("|_");
			System.out.println("| Thread");
			System.out.println("| Name: " + t.getName());
			System.out.println("| Id: " + t.getId());
			System.out.println("| State: " + t.getState());
			System.out.println("| Priority: " + t.getPriority());
			System.out.println("| Daemon: " + t.isDaemon());
		}
	}
	
}
