package model.card;

import model.exceptions.NotExecutedException;
import model.exceptions.TimeFinishedException;
import model.gamehandler.Room;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Effect implements Serializable {
    private String name;
    private int id;
    private String description;
    private List<AmmoColor> extraCost;
    private transient List<Operation> operations;

    Effect(String name,String description, List<AmmoColor> extraCost, List<Operation> operations){
        this.name = name;
        this.description=description;
        this.extraCost = extraCost;
        this.operations = operations;
    }

    public void execute(Room room)throws NotExecutedException, TimeFinishedException {
        for (Operation o:operations){
            o.execute(room);
        }
    }

    public List<Operation> getOperations() {
        return operations;
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
