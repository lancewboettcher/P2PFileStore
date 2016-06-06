import java.net.MalformedURLException;

import chord.StringKey;
import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;


public class ChordClient {
	public static void main(String[] args) { de.uniba.wiai.lspi.chord.service.PropertiesLoader.
		loadPropertyFile();
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL); URL localURL = null;
		try {
		localURL = new URL(protocol + "://localhost:8189/"); } catch (MalformedURLException e){

		throw new RuntimeException(e); }
		URL bootstrapURL = null;
		try {
		bootstrapURL = new URL(protocol + "://localhost:8080/");
		} catch (MalformedURLException e){ throw new RuntimeException(e);
		}
		Chord chord = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl
		(); try {
			chord.join(localURL , bootstrapURL);
			System.out.println("Here");
			
			
		} catch (ServiceException e) {
		throw new RuntimeException("Could not join DHT!", e);
		}
		
		
		String data = "Just an example."; 
		StringKey myKey = new StringKey(data);
		try{
			chord.insert(myKey , data);
			System.out.println("Inserted");
			
			System.out.println(chord.retrieve(myKey));
		} catch(ServiceException e){ //handle exception
			System.out.println("Error inserting");
		}
	}
}
