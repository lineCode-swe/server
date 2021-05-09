package org.linecode.server.persistence;

import org.linecode.server.Position;

import java.util.List;

public interface ObstacleRepository {
    public List<String> getObstaclesKey();
    public void setObstacle(Position obstacle);
    public Position getPosition();

}
