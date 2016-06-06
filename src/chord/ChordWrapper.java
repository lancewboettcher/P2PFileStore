package chord;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Set;

import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.Key;
import de.uniba.wiai.lspi.chord.service.ServiceException;

public class ChordWrapper {
	
	public Chord chord;
	
	public void joinChordNetwork(String thisHost, Integer thisPort, String bootstrapHost, Integer bootstrapPort) {
		de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL); URL localURL = null;
		try {
		localURL = new URL(protocol + "://" + thisHost + ":" + thisPort + "/"); } catch (MalformedURLException e){

		throw new RuntimeException(e); }
		URL bootstrapURL = null;
		try {
		bootstrapURL = new URL(protocol + "://" + bootstrapHost + ":"+ bootstrapPort + "/");
		} catch (MalformedURLException e){ throw new RuntimeException(e);
		}
		chord = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl
		(); try {
			chord.join(localURL , bootstrapURL);
			System.out.println("Joined Chord Network");
			
		} catch (ServiceException e) {
			throw new RuntimeException("Could not join DHT!", e);
		}
		
	}
	
	public void createChordNetwork(String host, Integer port) {
		de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
		
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL); 
		URL localURL;
		
		try {
			localURL = new URL(protocol + "://" + host + ":" + port + "/"); } catch (MalformedURLException e1){
			throw new RuntimeException(e1); }
			chord = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl ();
		
		try { 
			chord.create(localURL);
			System.out.println("Created Chord Network");
		} catch (ServiceException e) {
			throw new RuntimeException("Could not create DHT!", e);
		}
	}
	
	public void insert(Key key, Serializable value) {
		System.out.println("Inserting: " + value);
		
		try{
			chord.insert(key, value);
			System.out.println("Inserted: " + value);			
		} catch(ServiceException e){ //handle exception
			System.out.println("Error inserting");
		}
	}
	
	public Set<Serializable> retrieve(Key key) {
		Set<Serializable> res = null;
		
		try{
			res = chord.retrieve(key);
		
			System.out.println("Retrieved: " + res);
		} catch(ServiceException e){ //handle exception
			System.out.println("Error inserting");
		}
		
		return res;
	}
	
	public void remove(Key key, Serializable value) {
		Set<Serializable> res = null;
		
		try{
			chord.remove(key, value);
		
			System.out.println("Retrieved: " + res);
		} catch(ServiceException e){ //handle exception
			System.out.println("Error inserting");
		}
		
	}
}
