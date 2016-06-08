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
    P2PFile[] snapshot = null;
	
	public FileStore(String thisHost, Integer thisPort, String bootstrapHost, Integer bootstrapPort) {
		if (bootstrapPort == null) {
			chord.createChordNetwork(thisHost, thisPort);
		}
		else {
			chord.joinChordNetwork(thisHost, thisPort, bootstrapHost, bootstrapPort);
		}
        savedFiles = new HashMap<>();
        try {
            snapshot = db.listFiles();
        } catch (Exception e) {
            System.out.println("Could not retrieve initial snapshot");
        }
    }

    /*
     * Find file in snapshot
     * Params: Filename
     * Return: List<Hosts>
     */
    List<String> findInSnaphot(String filename) {
        for (P2PFile f : snapshot) {
            if (f.getFilename() == filename) {
                return f.getHosts();
            }
        }
        return new LinkedList<>();
    }

	/* 
	 * List Files
	 * Return: <Filename, # Nodes Containing File>
	 */
	public Map<String, Integer> listFiles() {
		
		Map<String, Integer> listFileOutput = new HashMap<String, Integer>();

        try {
            snapshot = db.listFiles();
        } catch (Exception e) {
            System.out.println("DB is down. Returning latest snapshot.");
        }

        if(snapshot != null) {
            for (P2PFile file : snapshot) {
                listFileOutput.put(file.getFilename(), file.getHosts().size());
            }
        }

		return listFileOutput;
	}
	
	/* 
	 * Request File
	 * Params: Filename
	 * Return: List<Hosts>
	 */
	public List<String> requestFile(String filename) {
        List<String> hosts = new LinkedList<>();
        if (pingDB()) return hosts;

        System.out.println("Looking for file in DHT");
        // Do not change to stream, because lab machines are running on Java7
        for (Serializable ser : chord.retrieve(new Key(filename))) {
            hosts.add(ser.toString());
        }
        if (hosts.size() == 0) {
        	System.out.println("\tFile does not exist in DHT.");
        	
        	hosts = findInSnaphot(filename);
        }
        if(hosts.size() == 0)
        {
            System.out.println("\tFile does not exist in Snapshot.");

            try {
                snapshot = db.listFiles();
            } catch (Exception e) {
                System.out.println("Cannot reach DB");
            }
            hosts = findInSnaphot(filename);
        }
        if(hosts.size() == 0)
        {
            System.out.println("\tFile does not exist anywhere.");
            return hosts;
        }
        for (String h : hosts) {
            if(h.contains(chord.getHostAddress())){
                // If host already saves file, do not add it again
                return hosts;
            }
        }
        // Add this host to saving nodes for this file
        addFile(filename, true);
        System.out.println("\tNode now also saves this file");
        return hosts;
    }
	
	/*
	 * Add File 
	 * Params: Filename, Filepath
	 */
	public void addFile(String filename, boolean calledFromRequest) {
        if (pingDB()) return;
        
        if (!calledFromRequest) {
           try {
              FileChunker.Chunk(filename);
           } catch (Exception e1) {
              System.out.println("Error. File not found");
              return;
           }
        }
        

        String totalFilepath = chord.getHostAddress() + "/chunks/" + filename;
		List<String> hosts = new ArrayList<>();

        savedFiles.put(filename, totalFilepath);
		hosts.add(totalFilepath);
		P2PFile file = new P2PFile(filename, 2, 3, hosts);

        db.addFile(file);
        try {
            snapshot = db.listFiles();
        } catch (Exception e) {
            System.out.println("Cannot retrieve snapshot");
        }
        chord.insert(new Key(filename), totalFilepath);
	}

    /*
     * Sends command to DB, to check if it is available.
     * Return: True if DB is down, false if up.
     */
    private boolean pingDB() {
        try {
            db.getFile("ping");
        } catch(Exception e) {
            System.out.println("DB is down.");
            return true;
        }
        return false;
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
