import java.lang.ThreadGroup;
import java.lang.Thread;
import javax.swing.tree.DefaultMutableTreeNode;

public class ThreadMonitor implements Runnable {

	private Thread selectedThread;

	private volatile boolean alive = true;
	
	public void terminate() {
        	this.alive = false;
    	}
	
	@Override
	public void run() {
		while(alive) {
			refreshListing();
			try {
				Thread.sleep(5000);
			} catch(InterruptedException ie) {
				
			}
		}
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

	//Prints a tree of all ThreadGroups and Threads of the JVM
	public void refreshListing() {
		ThreadGroup root = getRoot();
		System.out.println("Thread group");
		System.out.println("Name: " + root.getName());
		System.out.println("Max. priority: " + root.getMaxPriority());
		printThreads(root, 0);
		printThreadGroups(root, 0);
		System.out.println("\n\n\n");
	}
	
	//Adds all threads belonging to a parent ThreadGroup as DefaultMutableTreeNode children of the parentNode
	private void addThreadNodes(DefaultMutableTreeNode parentNode, ThreadGroup parent) {
		Thread[] threads = new Thread[parent.activeCount()];
		parent.enumerate(threads);
		
		for(Thread t : threads) {		
			parentNode.add(new DefaultMutableTreeNode(t));
		}
	}
	
	//Adds all the sub-ThreadGroup trees belonging to a parent ThreadGroup as DefaultMutableTreeNode children of the parentNode
	private void addThreadGroupNodes(DefaultMutableTreeNode parentNode, ThreadGroup parent) {
		ThreadGroup[] threadGroups = new ThreadGroup[parent.activeGroupCount()];
		parent.enumerate(threadGroups);
		
		for(ThreadGroup tg : threadGroups) {
			DefaultMutableTreeNode threadGroupNode = new DefaultMutableTreeNode(tg);
			parentNode.add(threadGroupNode);
			addThreadNodes(threadGroupNode, tg);
			addThreadGroupNodes(threadGroupNode, tg);
		}
	}
	
	//Returns a DefaultMutableTreeNode representing the parent node of the Thread tree with all its children
	public DefaultMutableTreeNode buildTree() {
		ThreadGroup root = getRoot();
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(root);
		addThreadNodes(rootNode, root);
		addThreadGroupNodes(rootNode, root);
		return rootNode;
	}
	
	public Thread getSelectedThread() {
		return selectedThread;
	}
	
	public void setSelectedThread(Thread t) {
		selectedThread = t;
	}
	
	public void interruptSelectedThread() {
		synchronized (selectedThread) {
			selectedThread.interrupt();
		}
	}

}
