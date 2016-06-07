package filestore;

import chord.ChordWrapper;
import db.DBWrapper;
import de.uniba.wiai.lspi.chord.console.command.entry.Key;
import domain.P2PFile;

import java.util.*;
import java.util.stream.Collectors;

public class FileStore {
	
	ChordWrapper chord = new ChordWrapper();
	DBWrapper db = new DBWrapper();
    Map<String, String> savedFiles;
	
	public FileStore(String thisHost, Integer thisPort, String bootstrapHost, Integer bootstrapPort) {
		if (bootstrapPort == null) {
			chord.createChordNetwork(thisHost, thisPort);
		}
		else {
			chord.joinChordNetwork(thisHost, thisPort, bootstrapHost, bootstrapPort);
		}
        savedFiles = new HashMap<>();
	}

	/* 
	 * List Files
	 * Return: <Filename, # Nodes Containing File>
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
        String totalFilepath = chord.getHostAddress() + "/" + filepath;
		List<String> hosts = new ArrayList<>();

        savedFiles.put(filename, totalFilepath);
		hosts.add(totalFilepath);
		P2PFile file = new P2PFile(filename, 2, 3, hosts);

//        System.out.println(">>> Add to DB");
        db.addFile(file);
//        System.out.println(">>> Add to DHT");
        chord.insert(new Key(filename), totalFilepath);
	}

    /*
     * Leave Chordring
     */
    public void leaveChordRing() {
        if(savedFiles.size() > 0){
            System.out.printf("Node on %s is leaving. Saved following files:\n", chord.getHostAddress());
            String hostAdd = chord.getHostAddress();
            for (String filename : savedFiles.keySet()) {
//            db.removeFile(filename, hostAdd);
                chord.remove(new Key(filename), savedFiles.get(filename));
                System.out.println("   " + filename);
            }
        }
        else
        {
            System.out.printf("Node on %s is leaving. Saved no files.\n", chord.getHostAddress());
        }

        chord.leave();
    }
}
