package model;

public class ModelBuffer {
	private CollectionBuffer collection;
	
	public ModelBuffer(String file) {
		this.collection = ReaderBuffer.getCollection(file);
	}
	
	public CollectionBuffer getCollection() {
		return collection;
	}

}
