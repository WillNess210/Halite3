package hlt;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Player {
    public final PlayerId id;
    public Shipyard shipyard;
    public int halite;
    public Map<EntityId, Ship> ships = new LinkedHashMap<>();
    public Map<EntityId, Dropoff> dropoffs = new LinkedHashMap<>();
    public int[][] tunnelMap;
    private Player(final PlayerId id, final Shipyard shipyard) {
        this.id = id;
        this.shipyard = shipyard;
    }

    void _update(final int numShips, final int numDropoffs, final int halite) {
        this.halite = halite;
        ArrayList<EntityId> onlyAllowedIDs = new ArrayList<EntityId>();
        for (int i = 0; i < numShips; ++i) {
            Ship generatedShip = Ship._generate(id);
            if(ships.containsKey(generatedShip.id)) {
            	ships.get(generatedShip.id).halite = generatedShip.halite;
            	if(ships.get(generatedShip.id).position.equals(generatedShip.position)) {
            		ships.get(generatedShip.id).turnsStill++;
            	}else {
            		ships.get(generatedShip.id).turnsStill = 0;
            	}
            	ships.get(generatedShip.id).position.x = generatedShip.position.x;
            	ships.get(generatedShip.id).position.y = generatedShip.position.y;
            }else {
                ships.put(generatedShip.id, generatedShip);
            }
            onlyAllowedIDs.add(generatedShip.id);
        }
        ArrayList<EntityId> toRemove = new ArrayList<EntityId>();
        for(EntityId id : ships.keySet()) {
        	if(!onlyAllowedIDs.contains(id)) {
        		toRemove.add(id);
        	}
        }
        for(EntityId id : toRemove) {
        	ships.remove(id);
        }
        dropoffs.clear();
        for (int i = 0; i < numDropoffs; ++i) {
            final Dropoff dropoff = Dropoff._generate(id);
            dropoffs.put(dropoff.id, dropoff);
        }
    }

    static Player _generate() {
        final Input input = Input.readInput();

        final PlayerId playerId = new PlayerId(input.getInt());
        final int shipyard_x = input.getInt();
        final int shipyard_y = input.getInt();

        return new Player(playerId, new Shipyard(playerId, new Position(shipyard_x, shipyard_y)));
    }
}
