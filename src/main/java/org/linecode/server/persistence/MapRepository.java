package org.linecode.server.persistence;

public interface MapRepository {
    public void setNewMap(String mapSchema);
    public int getLength();
    public int getHeight();
    public Cell getCell(int length, int height);
}
