package filestore;

import chord.ChordWrapper;
import de.uniba.wiai.lspi.chord.console.command.entry.Key;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	 *     TODO contact DB for all filenames (chord does not know what files are in it)
	 */
	public Map<String, Integer> listFiles() {
		
		return null;
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
		chord.insert(new Key(filename), filepath);
	}
}
