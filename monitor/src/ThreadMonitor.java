import java.lang.ThreadGroup;
import java.lang.Thread;

public class ThreadMonitor {
	
	public ThreadMonitor() {
		ThreadGroup root = getRoot();
		System.out.println("Root: " + root);
	}
	
	ThreadGroup getRoot() {
		Thread thread = Thread.currentThread();
		ThreadGroup root = thread.getThreadGroup();
		while(root.getParent() != null) {
			root = root.getParent();
		}
		return root;
	}
	
}
