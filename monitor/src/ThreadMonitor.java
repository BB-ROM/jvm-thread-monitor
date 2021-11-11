import java.lang.ThreadGroup;
import java.lang.Thread;

public class ThreadMonitor {
	
	public ThreadMonitor() {
		ThreadGroup root = getRoot();
		System.out.println("Thread group");
		System.out.println("Name: " + root.getName());
		System.out.println("Max. priority: " + root.getMaxPriority());
		printThreads(root, 0);
		printThreadGroups(root, 0);
	}
	
	//Returns the root ThreadGroup of the JVM
	ThreadGroup getRoot() {
		Thread thread = Thread.currentThread();
		ThreadGroup root = thread.getThreadGroup();
		while(root.getParent() != null) {
			root = root.getParent();
		}
		return root;
	}

	//Prints all Threads under a parent ThreadGroup
	void printThreads(ThreadGroup parent, int depth) {
		Thread[] threads = new Thread[parent.activeCount()];
		parent.enumerate(threads);
		
		String treeSpacing = "";
		if(depth > 0) {
			treeSpacing = "|" + "  ".repeat(depth);
		}
		else {
			treeSpacing = "   ".repeat(depth);
		}
		
		for(Thread t : threads) {		
			System.out.println(treeSpacing + "|__");
			System.out.println(treeSpacing + "|  Thread");
			System.out.println(treeSpacing + "|  Name: " + t.getName());
			System.out.println(treeSpacing + "|  Id: " + t.getId());
			System.out.println(treeSpacing + "|  State: " + t.getState());
			System.out.println(treeSpacing + "|  Priority: " + t.getPriority());
			System.out.println(treeSpacing + "|  Daemon: " + t.isDaemon());
		}
	}

	//Prints a tree of sub-ThreadGroups of a parent ThreadGroup
	void printThreadGroups(ThreadGroup parent, int depth) {
		ThreadGroup[] threadGroups = new ThreadGroup[parent.activeGroupCount()];
		parent.enumerate(threadGroups);
		
		String treeSpacing = "";
		if(depth > 0) {
			treeSpacing = "|" + "  ".repeat(depth);
		}
		else {
			treeSpacing = "   ".repeat(depth);
		}
		
		for(ThreadGroup tg : threadGroups) {	
			System.out.println(treeSpacing + "|__");
			System.out.println(treeSpacing + "|  Thread group");
			System.out.println(treeSpacing + "|  Name: " + tg.getName());
			System.out.println(treeSpacing + "|  Max. priority: " + tg.getMaxPriority());
			printThreads(tg, depth + 1);
			printThreadGroups(tg, depth + 1);
		}
	}
	
}
