/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.business;

import com.github.msteinbeck.sig4j.signal.Signal1;
import com.github.msteinbeck.sig4j.slot.Slot1;
import org.linecode.server.Position;
import org.linecode.server.persistence.Cell;
import org.linecode.server.persistence.MapRepository;
import org.linecode.server.persistence.ObstacleRepository;
import org.linecode.server.persistence.UnitRepository;
import org.linecode.server.persistence.Direction;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MapServiceImpl implements MapService {

    protected Grid map;
    protected final UnitRepository unitRepo;
    protected final ObstacleRepository obsRepo;
    protected final MapRepository mapRepo;
    protected final Signal1<Grid> mapSignal;
    protected final Signal1<List<Position>> obstaclesSignal;

    @Inject
    public MapServiceImpl(UnitRepository unitRepo, ObstacleRepository obsRepo,
                          MapRepository mapRepo,
                          Signal1<Grid> mapSignal, Signal1<List<Position>> obstaclesSignal) {

        this.unitRepo = unitRepo;
        this.obsRepo = obsRepo;
        this.mapRepo = mapRepo;
        this.mapSignal = mapSignal;
        this.obstaclesSignal = obstaclesSignal;
        this.map = new Grid(mapRepo.getCells(), mapRepo.getLength(), mapRepo.getHeight());
    }




    @Override
    public void newObstacleList(List<Position> obstacles, Position p) {
        int[][] premises = new int[][]{{-1, -1},{-1, 0}, {1, 0}, {0, -1}, {0, 1},{1 , 1},{-1, 1},{1, -1}};
        int x= p.getX();
        int y= p.getY();
        List<Position> vicinanze = new ArrayList<Position>();

        for (int[] d : premises) {
            int row = x + d[0];
            int col = y + d[1];
            if (isValid(row, col)) {
                vicinanze.add(new Position(row, col));
            }
        }

        for(Position cella : vicinanze){
            obsRepo.delObstacle(cella);
        }


        for(Position obstacle : obstacles){
            obsRepo.setObstacle(obstacle);
        }

        obstaclesSignal.emit(obstacles);
    }

    @Override
    public Grid getMap() {
        return map;
    }

    @Override
    public void newMap(String mapSchema) {
        char[] characters = mapSchema.toCharArray();
        List<Cell> lista= new ArrayList<>();
        int x = 0;
        int y = 0;

        for (int j=0; j<mapSchema.length();++j){
            switch(characters[j]) {
                case 'x':
                    lista.add(new Cell(new Position(x, y), true, false, Direction.NONE,false));
                    ++x;
                    break;
                case '^':
                    lista.add(new Cell(new Position(x, y), false, false, Direction.UP,false));
                    ++x;
                    break;
                case '_':
                    lista.add(new Cell(new Position(x, y), false, false, Direction.DOWN,false));
                    ++x;
                    break;
                case '>':
                    lista.add(new Cell(new Position(x, y), false, false, Direction.RIGHT,false));
                    ++x;
                    break;
                case '<':
                    lista.add(new Cell(new Position(x, y), false, false, Direction.LEFT,false));
                    ++x;
                    break;
                case 'B':
                    lista.add(new Cell(new Position(x, y), false, true, Direction.ALL,false));
                    ++x;
                    break;
                case 'P':
                    lista.add(new Cell(new Position(x, y), false, false, Direction.ALL,true));
                    ++x;
                    break;
                case '\n':
                    ++y;
                    x = 0;
                    break;
                default:
                case '+':
                    lista.add(new Cell(new Position(x, y), false, false, Direction.ALL,false));
                    ++x;
                    break;
            }
        }

        map = new Grid(lista,x,y+1);
        mapRepo.setCells(lista, x, y + 1);
        mapSignal.emit(map);
    }

    @Override
    public List<Position> getNextPath(String id) {
        List<Position> path = new ArrayList<>();
        List<Position> pois = unitRepo.getPoiList(id);
        int distance = Integer.MAX_VALUE;
        boolean sizeOfPois = !pois.isEmpty();

        if (sizeOfPois) {
            distance = getPath(unitRepo.getPosition(id), pois.get(0), path);
        } else {
            distance = getPath(unitRepo.getPosition(id), unitRepo.getBase(id), path);
        }

        if (distance != Integer.MAX_VALUE){
            return path;
        } else {
            return new ArrayList<>();
        }
    }

     protected int getPath(Position start, Position end, List<Position> path) {
        int[][] distances = new int[map.getLength()][map.getHeight()];
        for (int i = 0; i < map.getLength(); ++i) {
            for (int j = 0; j < map.getHeight(); ++j){
                distances[i][j] = Integer.MAX_VALUE;
            }
        }

        int distance = 0;
        List<Position> currentCells = Arrays.asList(start);

        while (distances[end.getX()][end.getY()] == Integer.MAX_VALUE
                && !currentCells.isEmpty()) {
            List<Position> nextCells = new ArrayList<>();

            for (Position cell : currentCells) {
                if (distances[cell.getX()][cell.getY()] == Integer.MAX_VALUE
                        && !checkObstacle(cell)
                        && (!checkUnit(cell) || cell.equals(start))
                        && map.getCell(cell) != null
                        && !map.getCell(cell).isLocked()) {
                    distances[cell.getX()][cell.getY()] = distance;
                    addNeighbors(cell, nextCells);
                }
            }

            currentCells = nextCells;
            ++distance;
        }

        if (distances[end.getX()][end.getY()] < Integer.MAX_VALUE) {
            Position cell = end;
            path.add(0,end);
            for (int d = distances[end.getX()][end.getY()]-1; d >= 0; d--) {
                cell = getNeighbor(cell, d, distances);
                path.add(0,cell);

            }
        }

        return distances[end.getX()][end.getY()];
    }

    protected boolean isValid(int x, int y) {
        return (x >= 0) && (x < map.getLength()) &&
                (y >= 0) && (y < map.getHeight());
    }

    protected void addNeighbors(Position pos, List<Position> list) {
        int[][] ds;

        if (map.getCell(pos) != null) {
            switch (map.getCell(pos).getDirection()) {
                case UP:
                    ds = new int[][]{{-1, 0}, {1, 0}, {0, -1}};
                    break;
                case DOWN:
                    ds = new int[][]{{-1, 0}, {1, 0}, {0, 1}};
                    break;
                case LEFT:
                    ds = new int[][]{{-1, 0}, {0, -1}, {0, 1}};
                    break;
                case RIGHT:
                    ds = new int[][]{{1, 0}, {0, -1}, {0, 1}};
                    break;
                case NONE:
                    ds = new int[][]{};
                    break;
                default:
                case ALL:
                    ds = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                    break;
            }

            for (int[] d : ds) {
                int row = pos.getX() + d[0];
                int col = pos.getY() + d[1];
                if (isValid(row, col)) {
                    list.add(new Position(row, col));
                }
            }
        }
    }

    protected Position getNeighbor(Position cell, int distance, int[][] distances) {

        int[][] ds = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        if(isValid(cell.getX() - 1, cell.getY()) &&
                map.getCell(new Position(cell.getX() - 1, cell.getY())).getDirection().equals(Direction.LEFT)) {
            ds[0][0] = 0;
            ds[0][1] = 0;
        }

        if(isValid(cell.getX() + 1, cell.getY()) &&
                map.getCell(new Position(cell.getX() + 1, cell.getY())).getDirection().equals(Direction.RIGHT)) {
            ds[1][0] = 0;
            ds[1][1] = 0;
        }

        if(isValid(cell.getX(), cell.getY()+1) &&
                map.getCell(new Position(cell.getX(), cell.getY() + 1)).getDirection().equals(Direction.DOWN)) {
            ds[3][0] = 0;
            ds[3][1] = 0;
        }

        if(isValid(cell.getX(), cell.getY() - 1) &&
                map.getCell(new Position(cell.getX(), cell.getY() - 1)).getDirection().equals(Direction.UP)) {
            ds[2][0] = 0;
            ds[2][1] = 0;
        }

        for (int[] d : ds) {
            int row = cell.getX() + d[0];
            int col = cell.getY() + d[1];
            if (isValid(row, col)
                    && distances[row][col] == distance && !cell.equals(new Position(row,col))) {
                return new Position(row, col);
                }
            }
        return null;
    }

    private boolean checkUnit(Position cell){

        boolean unitPlaced = false;
        Set<String> temporalListUnit = unitRepo.getUnits();
        Iterator<String> iterate = temporalListUnit.iterator();
        while(iterate.hasNext() && !unitPlaced){
            unitPlaced= unitRepo.getPosition(iterate.next()).equals(cell);
        }
        return unitPlaced;

    }
    
    private boolean checkObstacle(Position cell){

        return obsRepo.getObstaclesList().contains(cell);

    }

    @Override
    public void connectMapSignal(Slot1<Grid> slot) {
        mapSignal.connect(slot);
    }

    @Override
    public void connectObstaclesSignal(Slot1<List<Position>> slot) {
        obstaclesSignal.connect(slot);
    }

    @Override
    public boolean checkPremises(Position position) {
        int[][] premises = new int[][]{{-1, -1},{-1, 0}, {1, 0}, {0, -1}, {0, 1},{1 , 1},{-1, 1},{1, -1}};
        int x= position.getX();
        int y= position.getY();
        List<Position> vicinanze = new ArrayList<Position>();

        for (int[] d : premises) {
            int row = x + d[0];
            int col = y + d[1];
            if (isValid(row, col)) {
                vicinanze.add(new Position(row, col));
            }
        }
        boolean toReturn= false;
        Iterator<Position> iterate = vicinanze.iterator();
        while(iterate.hasNext() && !toReturn){
            toReturn = checkUnit(iterate.next());
        }
        return toReturn;
    }
}
