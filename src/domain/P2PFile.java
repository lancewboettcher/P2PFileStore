package domain;

import java.util.List;

public class P2PFile {
	private String filename;
	private Integer size;
	private Integer blocks;
	private List<String> hosts;
	
	public P2PFile() {
		
	}
	
	public P2PFile(String filename, Integer size, Integer blocks,
			List<String> hosts) {
		this.filename = filename;
		this.size = size;
		this.blocks = blocks;
		this.hosts = hosts;
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getBlocks() {
		return blocks;
	}
	public void setBlocks(Integer blocks) {
		this.blocks = blocks;
	}
	public List<String> getHosts() {
		return hosts;
	}
	public void setHosts(List<String> hosts) {
		this.hosts = hosts;
	}
	
	public String toString() {
		return "Filename: " + filename + "\nSize: " + size + "\nBlocks: " + blocks + "\nHosts: " + hosts;
	}
}
