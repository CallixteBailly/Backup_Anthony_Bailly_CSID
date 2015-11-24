package backup.Anthony;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFileChooser;

public class Main {
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException 
	{
		File source1 = null;
		File source2 = null;
		TreeMap<String, List<String>> treeMapS1 = new TreeMap<String,List<String>>();
		TreeMap<String, List<String>> treeMapS2 = new TreeMap<String,List<String>>();
		List<String> lsH = new ArrayList<String>();
		String nameDirectory1 = null, nameDirectory2 = null ;
		
		JFileChooser dialogue = new JFileChooser(".");
		dialogue.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		

		
		if (dialogue.showOpenDialog(null)==	JFileChooser.APPROVE_OPTION) {
			source1 = dialogue.getSelectedFile();
	        source1 = new File(source1.getPath());

		}
		JFileChooser dialogue2 = new JFileChooser(".");
		dialogue2.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		
		if (dialogue2.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
			source2 = dialogue2.getSelectedFile();
	        source2 = new File(source2.getPath());
		}
	        nameDirectory1 = source1.getName();
	        nameDirectory2 = source2.getName();
        
		recursiveScanDir(source1, treeMapS1);

		recursiveScanDir(source2, treeMapS2);
		
		compareTree(treeMapS1, treeMapS2,nameDirectory1,nameDirectory2,lsH);
		
		System.out.println("-------HISTORY---------");
		
		for (String string : lsH) {
			System.out.println(" - > " + string);
		}

	}
	public static void recursiveScanDir(File file,TreeMap<String, List<String>> tree)
	{
		System.out.println("-------------FILES---------------");
		for (File f : file.listFiles()){

			if (f.isDirectory())
			{
				System.out.println("DIRECTORY: " + f.getPath());
				recursiveScanDir(f, tree);
			}
			else
			{
				String sha1 = sha1(f);
				List<String> l = tree.get(sha1);
				if(l == null)
				{
					l = new ArrayList<String>();
					tree.put(sha1, l);
				
				}
				l.add(f.getPath());
				tree.put(sha1(f), l);
				System.out.println("-" + f.getPath());
			}
		}
	}
	public static TreeMap<String, List<String>>  recursiveScanDir(File file)
	{
		TreeMap<String, List<String>> res = new TreeMap<String, List<String>>();
		recursiveScanDir(file,res);
		return res;
	}
	public static void compareList(List<String> l1, List<String> l2,String nameDirectory1 , String nameDirectory2)
	{
		System.out.println("-------------IDENTICAL FILES------------------");
		for (String s : l1) {
				System.out.println("IDENTICAL ======> " + s);
				if(l2.contains(s.replace(nameDirectory1, nameDirectory2)))
				{
					System.out.println("Balabnlknsa");
				}
		}
		for (String s2 : l2) {
				System.out.println("IDENTICAL ======> " + s2);
				System.out.println("----------------------------------------------------");
		}
	}
	public static void compareTree(TreeMap<String, List<String>> tree,TreeMap<String, List<String>> tree2, String nameDirectory1, String nameDirectory2, List<String> history)
	{
		FileWriter writer = null;
    	File nomFichier = new File("Copy.txt");
		String newLine = System.getProperty("line.separator");
		try {
			writer = new FileWriter(nomFichier);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Map.Entry<String, List<String>> m : tree.entrySet()) {
			if (tree2.containsKey(m.getKey())) {
				List<String> fileName = m.getValue();
				compareList(m.getValue(),tree2.get(m.getKey()),nameDirectory1,nameDirectory2 );
			}
			else
			{
				System.out.println("-------------PASTE FILES------------------");
				for (String string : m.getValue()) {
					System.out.println("Keys add =>  " + m.getKey());
					try {
						history.add("cp " + string + " " + string.replace(nameDirectory1, nameDirectory2));
						writer.write("cp " + string + " " + string.replace(nameDirectory1, nameDirectory2) + " " + newLine);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}				
			}
		}
		for(Map.Entry<String, List<String>> m2 : tree2.entrySet())
		{
			if(tree.containsKey(m2.getKey()) == false)
			{
				System.out.println("-------------REMOVE FILES------------------");
				for (String string : m2.getValue()) {
					System.out.println("Keys add => " + m2.getKey());
					try {
						history.add("rm " + string.replace(nameDirectory1, nameDirectory2));
						writer.write("rm " + string.replace(nameDirectory1, nameDirectory2) + " " + newLine);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}	
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public static String sha1(File file){
        String localSha1Sum = null;
        if (file.exists() && file.isFile() && file.canRead()){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			DigestInputStream dis = new DigestInputStream(new FileInputStream(file), md);
			dis.on(true);
	
			while (dis.read() != -1){
				;
			}
			byte[] b = md.digest();
			localSha1Sum = getHexString(b);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		} else {
			localSha1Sum = "0";
			System.out.println("Est un dossier : "+ file.getAbsolutePath());
		}
		        return localSha1Sum;
		}
		private static String getHexString(byte[] bytes) {
			StringBuilder sb = new StringBuilder(bytes.length*2);
			for (byte b : bytes) {
	                        if (b <= 0x0F && b >= 0x00) { 
	                                sb.append('0');
	                        }
				sb.append( String.format("%x", b) );
			}
			return sb.toString();
		}
	    
}

