package filestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import chord.ChordWrapper;
import db.DBWrapper;
import de.uniba.wiai.lspi.chord.console.command.entry.Key;
import domain.P2PFile;

public class FileStore {
	
	ChordWrapper chord = new ChordWrapper();
	DBWrapper db = new DBWrapper();
	
	public FileStore(String thisHost, Integer thisPort, String bootstrapHost, Integer bootstrapPort) {
		if (bootstrapPort == null) {
			chord.createChordNetwork(thisHost, thisPort);
		}
		else {
			chord.joinChordNetwork(thisHost, thisPort, bootstrapHost, bootstrapPort);
		}
	}

	/* 
	 * List Files
	 * Return: <Filename, # Nodes Containing File>
	 *     TODO contact DB for all filenames (chord does not know what files are in it)
	 */
	public Map<String, Integer> listFiles() {
		
		Map<String, Integer> listFileOutput = new HashMap<String, Integer>();
		
		P2PFile[] files = db.listFiles();
		
		for (P2PFile file : files) {
			listFileOutput.put(file.getFilename(), file.getHosts().size());
		}
		
		return listFileOutput;
	}
	
	/* 
	 * Request File
	 * Params: Filename
	 * Return: List<Hosts>
	 */
	public List<String> requestFile(String filename) {
		return chord.retrieve(new Key(filename)).stream().map(Object::toString).collect(Collectors.toList());
	}
	
	/*
	 * Add File 
	 * Params: Filename, Filepath
	 */
	public void addFile(String filename, String filepath) {
		ArrayList<String> hosts = new ArrayList<String>();
		hosts.add("localhost");
		P2PFile file = new P2PFile("test.txt", 2, 3, hosts);
		
		db.addFile(file);
		
		chord.insert(new Key(filename), filepath);
	}

    /*
     * Leave Chordring
     */
    public void leaveChordRing() {
        chord.leave();
    }
}
