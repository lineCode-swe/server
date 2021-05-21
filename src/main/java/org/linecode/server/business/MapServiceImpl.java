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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapServiceImpl implements MapService{

    private Grid map;
    private final UnitRepository unitRepo;
    private final ObstacleRepository obsRepo;
    private final MapRepository mapRepo;
    private final UnitRepository unitRepository;
    private final Signal1<Grid> mapSignal;
    private final Signal1<List<Position>> obstaclesSignal;

    public MapServiceImpl(Grid map, UnitRepository unitRepo, ObstacleRepository obsRepo,
                          MapRepository mapRepo, UnitRepository unitRepository,
                          Signal1<Grid> mapSignal, Signal1<List<Position>> obstaclesSignal) {
        this.map = map;
        this.unitRepo = unitRepo;
        this.obsRepo = obsRepo;
        this.mapRepo = mapRepo;
        this.unitRepository = unitRepository;
        this.mapSignal = mapSignal;
        this.obstaclesSignal = obstaclesSignal;
    }




    @Override
    public void newObstacleList(List<Position> obstacles) {

    }

    @Override
    public Grid getMap() {
        return map;
    }

    @Override
    public void newMap(String mapSchema) {
        char[] characters = mapSchema.toCharArray();
        List<Cell> lista= new ArrayList<Cell>();
        int x=0,y=0;
        for (int j=0; j<mapSchema.length();++j){
            switch(characters[j]) {
                case 'x':
                    lista.add(new Cell(new Position(x,y),true,false, Direction.NONE));
                    break;
                case '^':
                    lista.add(new Cell(new Position(x,y),false,false, Direction.UP));
                    break;
                case '_':
                    lista.add(new Cell(new Position(x,y),false,false, Direction.DOWN));
                    break;
                case '>':
                    lista.add(new Cell(new Position(x,y),false,false, Direction.RIGHT));
                    break;
                case '<':
                    lista.add(new Cell(new Position(x,y),false,false, Direction.LEFT));
                    break;
               case 'B':
                    lista.add(new Cell(new Position(x,y),false,true, Direction.ALL));
                    break;
                case 'P':
                    lista.add(new Cell(new Position(x,y),false,false, Direction.ALL).createPoi(true));
                    break;
                case '\n':
                    ++y;
                    x=0;
                    break;
                default:
                case '+':
                    lista.add(new Cell(new Position(x,y),false,false, Direction.ALL));
                    break;
            }
            ++x;
        }
        map = new Grid(lista,lista.get(lista.size()-1).getPosition().getX(),
                lista.get(lista.size()-1).getPosition().getY());
        mapRepo.setNewMap(mapSchema);

    }

    @Override
    public List<Position> getNextPath(String id) {
        //TODO Estrapolare dal POC UnitEndpoint l'algoritmo di pathfinding
        List<Position> path = new ArrayList<Position>();
        List<Position> pois = unitRepo.getPoiList(id);
        /*for(int i =0; i<pois.size()-1;++i){

        }*/

        int distance= getPath(map,unitRepo.getPosition(id),pois.get(0),path);
        return path;

    }

     private int getPath(Grid map, Position start, Position end, List<Position> path) {
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
                        && !obsRepo.checkObstacle(cell)
                        && (!unitRepo.checkUnit(cell) || cell.equals(start))
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

    private void addNeighbors(Position pos, List<Position> list) {
        int[][] ds = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] d : ds) {
            int row = pos.getX() + d[0];
            int col = pos.getY() + d[1];
            if (isValid(row, col)) {
                list.add(new Position(row, col));
            }
        }
    }

    private Position getNeighbor(Position cell, int distance, int[][] distances) {
        int[][] ds = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] d : ds) {
            int row = cell.getX()+ d[0];
            int col = cell.getY() + d[1];
            if (isValid(row, col)
                    && distances[row][col] == distance) {
                return new Position(row, col);
            }
        }

        return null;
    }



    @Override
    public void connectMapSignal(Slot1<Grid> slot) {
        mapSignal.connect(slot);
    }

    @Override
    public void connectObstaclesSignal(Slot1<List<Position>> slot) {
        obstaclesSignal.connect(slot);
    }
}
