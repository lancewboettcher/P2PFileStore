package filestore;

import chord.ChordWrapper;
import db.DBWrapper;
import de.uniba.wiai.lspi.chord.console.command.entry.Key;
import domain.P2PFile;

import java.io.Serializable;
import java.util.*;

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
        // Do not change to stream, because lab machines are runing on Java7
        List<String> files = new LinkedList<>();
        for (Serializable ser : chord.retrieve(new Key(filename))) {
            files.add(ser.toString());
        }
        if(files.size() == 0)
        {
            System.out.println("\tFile does not exist in DHT.");
            files = db.getFile(filename).getHosts();
        }
        if(files.size() == 0)
        {
            System.out.println("\tFile does not exist in DB.");
        }
        for (String h : files) {
            if(h.contains(chord.getHostAddress())){
                // If host already saves file, do not add it again
                return files;
            }
        }
        // Add this host to saving nodes for this file
        System.out.println("\tNode now also saves this file");
        addFile(filename, "tmp");
        return files;
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
            db.removeFile(filename, hostAdd);
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
