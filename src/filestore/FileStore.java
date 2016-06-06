package filestore;

import java.util.List;
import java.util.Map;

import chord.ChordWrapper;

public class FileStore {
	
	ChordWrapper chord = new ChordWrapper();
	
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
	 */
	public Map<String, Integer> listFiles() {
		
		return null;
	}
	
	/* 
	 * Request File
	 * Params: Filename
	 * Return: <Filename, Hosts>
	 */
	public Map<String, List<String>> requestFile(String filename) {
		
		return null;
	}
	
	/*
	 * Add File 
	 * Params: Filename, Filepath
	 */
	public void addFile(String filename, String filepath) {
		
	}
}
