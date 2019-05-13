package model.card;

import model.exceptions.InterruptOperationException;
import model.exceptions.NullTargetsException;
import model.gamehandler.Room;

import java.io.Serializable;
import java.util.List;

public class Effect implements Serializable {
    private String description;
    private List<AmmoColor> extraCost;
    private transient List<Operation> operations;

    Effect(String description,List<AmmoColor> extraCost, List<Operation> operations){
        this.description=description;
        this.extraCost = extraCost;
        this.operations = operations;
    }

    public void esecute(Room room)throws InterruptOperationException,NullTargetsException {
        for (Operation o:operations){
            o.execute(room);
        }
    }

    public List<AmmoColor> getExtraCost() {
        return extraCost;
    }

    public String getDescription() {
        return description;
    }

}
