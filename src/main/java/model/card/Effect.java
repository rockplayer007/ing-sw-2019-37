package model.card;

import java.util.List;

public class Effect {

    private List<AmmoColor> extraCost;
    private List<Operation> operations;

    public Effect(List<AmmoColor> extraCost, List<Operation> operations){
        this.extraCost = extraCost;
        this.operations = operations;
    }

}
