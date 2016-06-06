package chord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;


public class Runner {

	public static void main(String[] args) {
		
		ChordWrapper chord = new ChordWrapper();
		
		chord.joinChordNetwork("localhost", 8206, "localhost", 3000);
		
		System.out.println("Joined");
		
		String value = "This is a test";
		StringKey key = new StringKey(value);
		
		chord.insert(key, value);
		
		System.out.println("Inserted");
		
		Set<Serializable> res = chord.retrieve(key);
		
		System.out.println(res);
		
		System.out.println("Removing");
		chord.remove(key, value);
		
		Set<Serializable> res2 = chord.retrieve(key);
		
		System.out.println(res2);
		
		
		ArrayList<String> list = new ArrayList<String>();
		list.add("test");
		list.add("test2");
		StringKey listKey = new StringKey("FILEID");
		
		chord.insert(listKey, list);
		
		Set<Serializable> res3 = chord.retrieve(listKey);
		
		System.out.println("Retrieving: " + res3);
		
		System.out.println("Done");
	}

}
