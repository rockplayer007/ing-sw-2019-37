package model.card;

import model.exceptions.InterruptOperationException;
import model.exceptions.NotExecutedException;
import model.exceptions.TimeFinishedException;
import model.gamehandler.Room;

import java.io.Serializable;
import java.util.List;

public class Effect implements Serializable {
    private String name;
    private int id;
    private String description;
    private List<AmmoColor> extraCost;
    private transient List<Operation> operations;

    /**
     * Constructor
     * @param name name of effect
     * @param description description of effect
     * @param extraCost extra cost for use this
     * @param operations implementation of this effect
     */
    Effect(String name,String description, List<AmmoColor> extraCost, List<Operation> operations){
        this.name = name;
        this.description=description;
        this.extraCost = extraCost;
        this.operations = operations;
    }

    /**
     * Use this effect
     * @param room room that player are
     * @throws NotExecutedException when the effect is non execute
     * @throws TimeFinishedException when the timer of players turn is finished during execution of effect
     * @throws InterruptOperationException when the effect is interrupt but that effect worked.
     */
    public void execute(Room room)throws NotExecutedException, TimeFinishedException, InterruptOperationException {
        for (Operation o:operations){
            o.execute(room);
        }
    }

    public String getName() {
        return name;
    }

    public List<AmmoColor> getExtraCost() {
        return extraCost;
    }

    public String getDescription() {
        return name+": "+description;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
