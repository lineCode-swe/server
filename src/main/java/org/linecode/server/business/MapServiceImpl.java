/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/
package org.linecode.server.business;

import com.github.msteinbeck.sig4j.signal.Signal1;
import com.github.msteinbeck.sig4j.slot.Slot1;
import org.linecode.server.Position;
import org.linecode.server.api.UnitEndpoint;
import org.linecode.server.persistence.Cell;
import org.linecode.server.persistence.MapRepository;
import org.linecode.server.persistence.ObstacleRepository;
import org.linecode.server.persistence.UnitRepository;
import org.linecode.server.persistence.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MapServiceImpl implements MapService {

    protected Grid map;
    protected final UnitRepository unitRepo;
    protected final ObstacleRepository obsRepo;
    protected final MapRepository mapRepo;
    protected final Signal1<Grid> mapSignal;
    protected final Signal1<List<Position>> obstaclesSignal;
    private final Logger logger = LoggerFactory.getLogger(MapServiceImpl.class);

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

        obstaclesSignal.emit(obsRepo.getObstaclesList());
    }

    @Override
    public Grid getMap() {
        return map;
    }

    @Override
    public List<Position> getObstacles() {
        return obsRepo.getObstaclesList();
    }

    @Override
    public void newMap(String mapSchema) {
        char[] characters = mapSchema.toCharArray();
        List<Cell> lista= new ArrayList<>();
        int x = 0;
        int y = 0;

        for (int j = 0; j < mapSchema.length(); ++j){
            switch(characters[j]) {
                case 'X':
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
                case 'b':
                    lista.add(new Cell(new Position(x, y), false, true, Direction.ALL,false));
                    ++x;
                    break;

                case 'P':
                case 'p':
                    lista.add(new Cell(new Position(x, y), false, false, Direction.ALL,true));
                    ++x;
                    break;

                case '\n':
                    ++y;
                    x = 0;
                    break;

                case '+':
                    lista.add(new Cell(new Position(x, y), false, false, Direction.ALL,false));
                    ++x;
                    break;

                case ' ':
                case '\r':
                default:
                    break;
            }
        }

        map = new Grid(lista,x,y+1);
        mapRepo.setCells(lista, x, y + 1);
        mapSignal.emit(map);
    }

    @Override
    public List<Position> getNextPath(String id,List<Position> premesis) {
        List<Position> path = new ArrayList<Position>();
        Position unitPosition= unitRepo.getPosition(id);
        List<Position> pois = unitRepo.getPoiList(id);
        int distance = Integer.MAX_VALUE;
        boolean sizeOfPois = !pois.isEmpty();
        List<Position> invalidCells = new ArrayList<Position>();
        if(!premesis.isEmpty()) {
            for (Position cellsPrem : premesis) {
                addInvalids(cellsPrem, invalidCells);
            }
        }
        invalidCells.remove(unitPosition);
        logger.info("Le celle invalide per il calcolo del percorso sono : "+ invalidCells);
        if (sizeOfPois) {
            distance = getPath(unitPosition, pois.get(0), path,invalidCells);
        } else {
            distance = getPath(unitPosition, unitRepo.getBase(id), path,invalidCells);
        }

        if (distance != Integer.MAX_VALUE){
            return path;
        } else {
            return new ArrayList<Position>();
        }
    }

     protected int getPath(Position start, Position end, List<Position> path, List<Position> invalidCells) {
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
                        && checkValidity(cell,invalidCells,start)) {
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

    protected boolean checkValidity(Position cell,List<Position> invalidCells,Position start){
        return  !checkObstacle(cell)
                && (!checkUnit(cell) || cell.equals(start))
                && (map.getCell(cell)!=null)
                && !map.getCell(cell).isLocked()
                && !invalidCells.contains(cell);
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

    protected void addInvalids(Position pos, List<Position> list) {
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
                if (isValid(row, col) && !list.contains(new Position(row, col))) {
                    list.add(new Position(row, col));
                }
            } list.add(pos);
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

    public boolean checkUnit(Position cell){
       List<Position> positions = unitRepo.getPositionUnits();
       return positions.contains(cell);

    }
    /*//TODO
    private boolean checkUnitStatus(Position cell){
       List<String> unit = unitRepo.getUnit(cell);
       if(!unit.isEmpty()){
           logger.info("BBBBBBBBBBBB: "+unit.get(0) + " STATUS : " +unitRepo.getStatus(unit.get(0))
           + " |DESIRED STATUS = " + UnitStatus.GOINGTO);
           return unitRepo.getStatus(unit.get(0)).equals(UnitStatus.GOINGTO);
       }
       return true;
    }*/
    
    public boolean checkObstacle(Position cell){

        return obsRepo.getObstaclesList().contains(cell);

    }
    /*//Todo
    private boolean checkSharedCells(Position cell){
        List<Position> units = unitRepo.getPositionUnits();
        return units.size()>1;
    }*/

    @Override
    public void connectMapSignal(Slot1<Grid> slot) {
        mapSignal.connect(slot);
    }

    @Override
    public void connectObstaclesSignal(Slot1<List<Position>> slot) {
        obstaclesSignal.connect(slot);
    }

    @Override
    public List<Position> checkPremises(Position position) {
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
        List<Position> toReturn= new ArrayList<Position>();
        Iterator<Position> iterate = vicinanze.iterator();
        while(iterate.hasNext()){
            Position prossimo = iterate.next();
            if(checkUnit(prossimo) && !prossimo.equals(position)){
                toReturn.add(prossimo);
            }
        }
        return toReturn;

    }
}
