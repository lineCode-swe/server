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
import org.linecode.server.persistence.*;

import java.util.ArrayList;
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
                    lista.add(new Cell(new Position(x,y),true,false,false, Direction.NONE));
                    break;
                case '^':
                    lista.add(new Cell(new Position(x,y),false,false,false, Direction.UP));
                    break;
                case '_':
                    lista.add(new Cell(new Position(x,y),false,false,false, Direction.DOWN));
                    break;
                case '>':
                    lista.add(new Cell(new Position(x,y),false,false,false, Direction.RIGHT));
                    break;
                case '<':
                    lista.add(new Cell(new Position(x,y),false,false,false, Direction.LEFT));
                    break;
               case 'B':
                    lista.add(new Cell(new Position(x,y),false,false,true, Direction.ALL));
                    break;
                case 'P':
                    lista.add(new Cell(new Position(x,y),false,false,false, Direction.ALL).createPoi(true));
                    break;
                case '\n':
                    ++y;
                    x=0;
                    break;
                default:
                case '+':
                    lista.add(new Cell(new Position(x,y),false,false,false, Direction.ALL));
                    break;
            }
            ++x;
        }
        map = new Grid(lista,lista.get(lista.size()-1).getPosition().getX(),lista.get(lista.size()-1).getPosition().getY());
        mapRepo.setNewMap(mapSchema);

    }

    @Override
    public List<Position> getNextPath(String id) {
        //TODO Estrapolare dal POC UnitEndpoint l'algoritmo di pathfinding
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
