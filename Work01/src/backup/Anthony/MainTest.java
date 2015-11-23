package backup.Anthony;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class MainTest {

	Main sut = new Main();
	
	@Test
	public void testRecursiveScanDirFileTreeMapOfStringListOfString() {
		
		// Prepare
		String source1 = "F:\\Users\\Bla\\workspace\\Work01\\Source1";
		TreeMap<String, List<String>> tree = new TreeMap<String,List<String>>();
		
		// Perform
		sut.recursiveScanDir(new File(source1),tree);
		List<String> keyBE = tree.get("e0184adedf913b076626646d3f52c3b49c39ad6d");
		List<String> keyA= tree.get("6dcd4ce23d88e2ee9568ba546c007c63d9131c1b");
		List<String> keyC = tree.get("32096c2e0eff33d844ee6d675407ace18289357d");
		
		// Post-check
		assertEquals(3, tree.size());
		assertEquals("F:\\Users\\Bla\\workspace\\Work01\\Source1\\SousDossier\\B.txt", keyBE.get(0));
		assertEquals("F:\\Users\\Bla\\workspace\\Work01\\Source1\\SousDossier\\E.txt", keyBE.get(1));
		
		assertEquals("F:\\Users\\Bla\\workspace\\Work01\\Source1\\SousDossier\\A.txt", keyA.get(0));
		assertEquals("F:\\Users\\Bla\\workspace\\Work01\\Source1\\C.txt", keyC.get(0));
		
		
		
	}
	
	@Test
	public void testCompareTree()
	{
		// Prepare
		String source1 = "F:\\Users\\Bla\\workspace\\Work01\\Source1";
		String source2 = "F:\\Users\\Bla\\workspace\\Work01\\Source2";
		TreeMap<String, List<String>> treeMapS1 = new TreeMap<String,List<String>>();
		TreeMap<String, List<String>> treeMapS2 = new TreeMap<String,List<String>>();
		List<String> lsH= new ArrayList<String>();
		// Perform
		sut.recursiveScanDir(new File(source1),treeMapS1);
		sut.recursiveScanDir(new File(source2),treeMapS2);
		
		// Post-check
		sut.compareTree(treeMapS1, treeMapS2, source1, source2,lsH);
		assertEquals(3,lsH.size());
		assertEquals("cp F:\\Users\\Bla\\workspace\\Work01\\Source1\\C.txt F:\\Users\\Bla\\workspace\\Work01\\Source2\\C.txt",lsH.get(0));
		assertEquals("rm F:\\Users\\Bla\\workspace\\Work01\\Source2\\D.txt",lsH.get(1));
		assertEquals("rm F:\\Users\\Bla\\workspace\\Work01\\Source2\\B.txt",lsH.get(2));
		
		
		
	}
	

}
