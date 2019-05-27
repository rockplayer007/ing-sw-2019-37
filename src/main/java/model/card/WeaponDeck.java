package model.card;

import java.util.*;

public class WeaponDeck extends Deck<Weapon>{


    public WeaponDeck(){
        this.addAll(generationWeapons());
        mixDeck();

    }


    /**
     * @return all weapons .
     */
    private List<Weapon> generationWeapons(){
        List<Weapon> weapons = new ArrayList<>();
        List<Operation> operations;
        Map<Effect,Integer> effects = new HashMap<>();
        String basicEffect = "Basic effect";
        String basicMode = "Basic mode";


        //operation without attribute
        VisiblePlayers visiblePlayers = new VisiblePlayers();
        SameSquare sameSquare = new SameSquare();
        SetTargetToSelected setTargetToSelected = new SetTargetToSelected();
        SelectTargets selectTarget1 = new SelectTargets(1,false);
        SelectTargets selectTargets2 = new SelectTargets(2,false);
        SelectFromSelectedTargets selectFromSelectedTargets1 = new SelectFromSelectedTargets(1);
        SelectAllTarget selectAllTarget = new SelectAllTarget();
        SetTargetPositionAsEffectSquare setTargetPositionAsEffectSquare = new SetTargetPositionAsEffectSquare();
        DirectionTargets directionTargets = new DirectionTargets(0,true);
        TargetOnEffectSquare targetOnEffectSquare = new TargetOnEffectSquare();
        MinOrMaxDistance minDistance0 = new MinOrMaxDistance(0,false);
        MinOrMaxDistance maxDistance1 = new MinOrMaxDistance(1,true);
        Run run2 = new Run(2);
        MoveTargetToEffevtSquare moveTargetToEffevtSquare = new MoveTargetToEffevtSquare();
        MoveToTarget moveToTarget = new MoveToTarget();
        MoveTarget moveTarget1 = new MoveTarget(1);
        Damage damage1 = new Damage(1);
        Damage damage2 = new Damage(2);
        Damage damage3 = new Damage(3);
        Mark mark1 = new Mark(1);
        Mark mark2 = new Mark(2);


        String description = "Deal 2 damage and 1 mark to 1 target  you can see.\n" ;
        operations=new ArrayList<>(Arrays.asList(visiblePlayers, selectTarget1, damage2, mark1, setTargetToSelected));
        effects.put(new Effect(basicEffect,description, Collections.emptyList(), operations),0);
        operations=Arrays.asList(selectTarget1, mark1);
        description="Deal 1 mark to a different target  you can see.\n";
        effects.put(new Effect("Second lock",description, Collections.singletonList(AmmoColor.RED), operations),1);

        weapons.add(new Weapon("LOCK RIFLE", "", AmmoColor.BLUE, Collections.singletonList(AmmoColor.BLUE),
                true, effects));


        effects = new HashMap<> ();
        description ="Choose 1 or 2 targets you can see and deal 1 damage to each.\n";
        operations =Arrays.asList( visiblePlayers, selectTargets2, damage1, setTargetToSelected);
        effects.put(new Effect(basicEffect,description, Collections.emptyList(), operations),0);
        description ="Deal 1 additional damage to one of those targets.\n";
        operations= new ArrayList<>(Arrays.asList(selectFromSelectedTargets1, damage1, setTargetToSelected));
        effects.put(new Effect("Focus shot",description,Collections.singletonList(AmmoColor.YELLOW), operations), 1);
        description ="Deal 1 additional damage to the other of " +
                "those targets and/or deal 1 damage to a different target you can see.\n" ;
        operations= Arrays.asList( selectFromSelectedTargets1, selectTarget1, damage1);
        effects.put(new Effect("Turret tripod",description, Collections.singletonList(AmmoColor.BLUE), operations), 1);
        description="Notes: If you deal both additional points of damage, they must be dealt to 2 different targets. " +
                "If you see only 2 targets, you deal 2 to each if you use both optional effects. If you use the basic " +
                "effect on only 1 target, you can still use the the turret tripod to give it 1 additional damage.\n";

        weapons.add(new Weapon("MACHINE GUN",description, AmmoColor.BLUE , Collections.singletonList(AmmoColor.RED),
                true, effects ));


        effects = new HashMap<>();
        description ="Deal 2 damage to 1 target you can see.\n" ;
        operations = Arrays.asList(visiblePlayers, selectTarget1, damage2,setTargetToSelected);
        effects.put(new Effect(basicEffect,description,Collections.emptyList(), operations),0);
        description = "Deal 1 damage to a second target that your first target can see.\n";
        operations = Arrays.asList(new ThorTargets(0), selectTarget1, damage1, setTargetToSelected);
        effects.put(new Effect("Chain reaction",description, Collections.singletonList(AmmoColor.BLUE), operations), 1);
        description = "Deal 2 damage to a third target that your second target can see. " +
                "You cannot use this effect unless you first use the chain reaction.\n";
        operations= Arrays.asList(new ThorTargets(1), selectTarget1,damage2);
        effects.put(new Effect("High voltage",description, Collections.singletonList(AmmoColor.BLUE), operations), 2);
        description="Notes: This card constrains the order in which you can use its effects. " +
                "(Most cards don't.) Also note that each target must be a different player.\n" ;

        weapons.add(new Weapon("T.H.O.R.", description, AmmoColor.BLUE, Collections.singletonList(AmmoColor.RED),
                true, effects));


        effects = new HashMap<>();
        description ="Deal 2 damage to 1 target you can see.\n" ;
        operations = Arrays.asList(visiblePlayers, selectTarget1, damage2, setTargetToSelected);
        effects.put(new Effect(basicEffect,description, Collections.emptyList(), operations),0);
        description = "Move 1 or 2 squares. This effect can be used either before or after the basic effect.\n";
        operations = Collections.singletonList(run2);
        effects.put(new Effect("Phase glide",description ,Collections.emptyList(), operations), -1);
        description = "Deal 1 additional damage to your target.\n";
        operations = Arrays.asList(selectFromSelectedTargets1, damage1);
        effects.put(new Effect("Charged shot",description, Collections.singletonList(AmmoColor.BLUE), operations), 1);
        description = "Notes: The two moves have no ammo cost. You don't have to be able to see your target " +
                "when you play the card. For example, you can move 2 squares and shoot a target you now see. " +
                "You cannot use 1 move before shooting and 1 move after.\n";

        weapons.add(new Weapon("PLASMA GUN", description, AmmoColor.BLUE, Collections.singletonList(AmmoColor.YELLOW),
                true, effects));


        effects = new HashMap<>();
        description = "Deal 3 damage and 1 mark to 1 target you can see. Your target must be at least 2 moves away from you.\n" ;
        operations = Arrays.asList(visiblePlayers, new MinOrMaxDistance(1,false), selectTarget1, damage3);
        effects.put(new Effect("Effect",description, Collections.emptyList(), operations), 0);
        description = "Notes: For example, in the 2-by-2 room, you cannot shoot a target on an adjacent square, " +
                "but you can shoot a target on the diagonal. If you are beside a door, you can't shoot a target " +
                "on the other side of the door, but you can shoot a target on a different square of that room.\n";

        weapons.add(new Weapon("WHISPER", description, AmmoColor.BLUE, Arrays.asList(AmmoColor.BLUE, AmmoColor.YELLOW),
                false, effects));


        effects = new HashMap<>();
        description = "Deal 1 damage to every other player on your square.\n";
        operations = Arrays.asList(sameSquare, selectAllTarget, damage1);
        effects.put(new Effect(basicMode,description, Collections.emptyList(), operations), 0);
        description = "Deal 2 damage to every other player on your square.\n";
        operations = Arrays.asList(sameSquare, selectAllTarget, damage2);
        effects.put(new Effect("Reaper mode",description, Arrays.asList(AmmoColor.BLUE, AmmoColor.RED), operations), 0);

        weapons.add(new Weapon("ELECTROSCYTHE", "", AmmoColor.BLUE, Collections.emptyList(),
                false, effects));


        effects = new HashMap<>();
        description = "Move a target 0, 1, or 2 squares to a square you can see, and give it 1 damage.\n";
        operations = Arrays.asList(visiblePlayers,new AddPossibleTargetBeforeMove(2,false),
                selectTarget1, new MoveTargetToVisible(2), damage1);
        effects.put(new Effect(basicMode,description, Collections.emptyList(), operations), 0);
        description = "Choose a target 0, 1, or 2 moves away from you. " +
                "Move the target to your square and deal 3 damage to it.\n";
        operations = Arrays.asList(visiblePlayers, new AddPossibleTargetBeforeMove(2,true),
                selectTarget1, new SetPlayerPositionAsEffectSquare(), moveTargetToEffevtSquare, selectTarget1, damage1);
        effects.put(new Effect("Punisher mode",description, Arrays.asList(AmmoColor.RED, AmmoColor.YELLOW), operations), 0);
        description = "Notes: You can move a target even if you can't see it. The target ends up in a place where you can see and " +
                "damage it. The moves do not have to be in the same direction.\n";

        weapons.add(new Weapon("TRACTOR BEAM", description, AmmoColor.BLUE, Collections.emptyList(),
                false, effects));


        effects=new HashMap<>();
        description = "Choose a square you can see, but not your square. Call it \"the vortex\". " +
                "Choose a target on the vortex or 1 move away from it. Move it onto the vortex and give it 2 damage.\n";
        operations= Arrays.asList(new SelectEffectSquare(1),selectTarget1, moveTargetToEffevtSquare, damage1, setTargetToSelected);
        effects.put(new Effect(basicEffect,description, Collections.emptyList(), operations),0);
        description = "Choose up to 2 other targets on the vortex or 1 move away from it. " +
                "Move them onto the vortex and give them each 1 damage.\n";
        operations= Arrays.asList(selectTargets2, damage1);
        effects.put(new Effect("Black hole",description, Collections.singletonList(AmmoColor.RED), operations), 1);
        description = "Notes: The 3 targets must be different, but some might start on the same square. It is legal " +
                "to choose targets on your square, on the vortex, or even on squares you can't see. They all end up on the vortex.\n";

        weapons.add(new Weapon("VORTEX CANNON", description, AmmoColor.RED, Collections.singletonList(AmmoColor.BLUE),
                true, effects));


        effects = new HashMap<>();
        description = "Choose a room you can see, but not the room you are in. Deal 1 damage to everyone in that room.\n ";
        operations=Arrays.asList(new Furance(false), selectAllTarget, damage1);
        effects.put(new Effect("Basic mode: ",description, Collections.emptyList(), operations), 0);
        description = "Choose a square exactly one move away. Deal 1 damage and 1 mark to everyone on that square.\n";
        operations = Arrays.asList(new Furance(true), selectAllTarget, damage1, mark1);
        effects.put(new Effect("Cozy fire mode",description,Collections.emptyList(), operations), 0);

        weapons.add(new Weapon("FURNACE", "", AmmoColor.RED, Collections.singletonList(AmmoColor.BLUE),
                false, effects));


        effects = new HashMap<>();
        description = "Choose 1 target you cannot see and deal 3 damage to it.\n";
        operations = Arrays.asList(visiblePlayers, new Heatseekker(), selectTarget1, damage3);
        effects.put(new Effect("Effect",description, Collections.emptyList(), operations), 0);
        description = "Notes: Yes, this can only hit targets you cannot see.\n";

        weapons.add(new Weapon("HEATSEEKER", description, AmmoColor.RED, Arrays.asList(AmmoColor.RED, AmmoColor.YELLOW),
                false, effects));


        effects = new HashMap<>();
        description = "Deal 1 damage to 1 target you can see at least 1 move away. " +
                "Then give 1 mark to that target and everyone else on that square.\n";
        operations = Arrays.asList(visiblePlayers, minDistance0, selectTarget1, damage1, mark1,
                setTargetPositionAsEffectSquare, targetOnEffectSquare, selectAllTarget, mark1);
        effects.put(new Effect(basicMode,description, Collections.emptyList(), operations), 0);
        description = "Deal 1 damage to 1 target you can see at least 1 move away. " +
                "Then give 2 marks to that target and everyone else on that square.\n";
        operations = Arrays.asList(visiblePlayers, minDistance0, selectTarget1, damage1, mark1,
                setTargetPositionAsEffectSquare, targetOnEffectSquare, selectAllTarget, mark2);
        effects.put(new Effect("Nano-tracer mode",description,Collections.singletonList(AmmoColor.RED), operations), 0);

        weapons.add(new Weapon("HELLION", "", AmmoColor.RED, Collections.singletonList(AmmoColor.YELLOW),
                false, effects));


        effects = new HashMap<>();
        description = "Choose a square 1 move away and possibly a second square " +
                "1 more move away in the same direction. On each square, you may choose 1 target and give it 1 damage.\n";
        operations = Arrays.asList(new DirectionTargets(2,false), minDistance0,
                new SelectTargets(2,true), damage1);
        effects.put(new Effect(basicMode,description, Collections.emptyList(), operations), 0);
        description = "Choose 2 squares as above. Deal 2 damage to " +
                "everyone on the first square and 1 damage to everyone on the second square.\n";
        operations = Collections.singletonList(new Flamethorwer());
        effects.put(new Effect("Barbecue mode: ",description, Arrays.asList(AmmoColor.YELLOW, AmmoColor.YELLOW), operations), 0);
        description = "Notes: This weapon cannot damage anyone in your square. However, it can sometimes damage " +
                "a target you can't see – the flame won't go through walls, but it will go through doors. " +
                "Think of it as a straight-line blast of flame that can travel 2 squares in a cardinal direction.\n";

        weapons.add(new Weapon("FLAMETHROWER", description, AmmoColor.RED, Collections.emptyList(),
                false,effects));


        effects = new HashMap<>();
        description ="Deal 1 damage to 1 target you can see. Then you may move " +
                "the target 1 square.\n" ;
        operations = Arrays.asList(visiblePlayers, selectTarget1, damage1, setTargetPositionAsEffectSquare, moveTarget1, setTargetToSelected);
        effects.put(new Effect(basicEffect,description, Collections.emptyList(), operations), 0);
        description = "Deal 1 damage to every player on a square you can see. " +
                "You can use this before or after the basic effect's move.\n";
        operations = Arrays.asList(new SelectEffectSquare(0), selectAllTarget, damage1);
        effects.put(new Effect("Extra grenade",description, Collections.singletonList(AmmoColor.RED), operations), 1);
        description = "Notes: For example, you can shoot a target, move it onto a square with other targets, " +
                "then damage everyone including the first target. Or you can deal 2 to a main target, " +
                "1 to everyone else on that square, then move the main target. Or you can deal 1 to an isolated target " +
                "and 1 to everyone on a different square. If you target your own square, you will not be moved or damaged.\n";

        weapons.add(new Weapon("GRENADE LAUNCHER", description, AmmoColor.RED, Collections.singletonList(AmmoColor.RED),
                false, effects));


        effects = new HashMap<>();
        description = "Deal 2 damage to 1 target you can see that is not on your square. Then you may move the target 1 square.\n";
        operations = Arrays.asList(visiblePlayers, minDistance0, selectTarget1, damage2, moveTarget1, setTargetToSelected);
        effects.put(new Effect(basicEffect,description, Collections.emptyList(), operations), 0);
        description = "Move 1 or 2 squares. This effect can be used either before or after the basic effect.\n";
        operations = Collections.singletonList(run2);
        effects.put(new Effect("Rocket jump",description, Collections.singletonList(AmmoColor.BLUE), operations), -1);
        description = "During the basic effect, deal 1 damage to " +
                "every player on your target's original square – including the target, even if you move it.\n";
        operations = Arrays.asList(new SelectEffectSquare(0), selectAllTarget,damage1);
        effects.put(new Effect("Fragmenting warhead",description, Collections.singletonList(AmmoColor.YELLOW), operations), 1);
        description = "Notes: If you use the rocket jump before the basic effect, you consider only your new square when " +
                "determining if a target is legal. You can even move off a square so you can shoot someone on it. If you " +
                "use the fragmenting warhead, you deal damage to everyone on the target's square before you move the target " +
                "– your target will take 3 damage total.\n";

        weapons.add(new Weapon("ROCKET LAUNCHER",description, AmmoColor.RED, Collections.singletonList(AmmoColor.RED),
                false, effects));


        effects = new HashMap<>();
        description = "Choose a cardinal direction and 1 target in that direction. Deal 3 damage to it.\n";
        operations = Arrays.asList(directionTargets, selectTarget1, damage3);
        effects.put(new Effect(basicMode,description,Collections.emptyList(), operations), 0);
        description = "Choose a cardinal direction and 1 or 2 targets in that direction. Deal 2 damage to each.\n";
        operations = Arrays.asList(directionTargets, selectTargets2, damage2);
        effects.put(new Effect("Piercing mode",description, Collections.emptyList(), operations), 0);
        description = "Notes: Basically, you're shooting in a straight line and ignoring walls. You don't have to pick a target on " +
                "the other side of a wall – it could even be someone on your own square – but shooting through walls sure is fun. " +
                "There are only 4 cardinal directions. You imagine facing one wall or door, square-on, and firing in that direction. " +
                "Anyone on a square in that direction (including yours) is a valid target. In piercing mode, " +
                "the 2 targets can be on the same square or on different squares.\n";

        weapons.add(new Weapon("RAILGUN", description, AmmoColor.YELLOW, Arrays.asList(AmmoColor.YELLOW, AmmoColor.BLUE),
                false, effects));


        effects = new HashMap<>();
        description = "Deal 2 damage to 1 target on your square.\n";
        operations = Arrays.asList(sameSquare, selectTarget1, damage2, setTargetToSelected);
        effects.put(new Effect(basicEffect,description, Collections.emptyList(), operations), 0);
        description = "Move 1 square before or after the basic effect.\n";
        operations= Collections.singletonList(new Run(1));
        effects.put(new Effect("Shadowstep",description, Collections.emptyList(), operations), -1);
        description = "Deal 2 damage to a different target on your square. " +
                "The shadowstep may be used before or after this effect.\n";
        operations= Arrays.asList(selectTarget1, damage2);
        effects.put(new Effect("Slice and dice",description, Collections.singletonList(AmmoColor.YELLOW), operations), 1);
        description = "Notes: Combining all effects allows you to move onto a square and whack 2 people; " +
                "or whack somebody, move, and whack somebody else; or whack 2 people and then move.\n";

        weapons.add(new Weapon("CYBERBLADE", description, AmmoColor.YELLOW, Collections.singletonList(AmmoColor.RED),
                true, effects));


        effects = new HashMap<>();
        description = "Deal 1 damage and 2 marks to 1 target you can see.\n";
        operations = Arrays.asList(visiblePlayers, selectTarget1, damage1, mark2);
        effects.put(new Effect(basicMode,description, Collections.emptyList(), operations), 0);
        description = "Choose up to 3 targets you can see and deal 1 mark to each.\n";
        operations = Arrays.asList(visiblePlayers, new SelectTargets(3,false), mark1);
        effects.put(new Effect("Scanner mode",description,Collections.emptyList(), operations), 0);
        description = "Notes: Remember that the 3 targets can be in 3 different rooms.\n";

        weapons.add(new Weapon("ZX-2", description, AmmoColor.YELLOW, Collections.singletonList(AmmoColor.RED),
                false, effects));


        effects=new HashMap<>();
        description = "Deal 3 damage to 1 target on your square. If you want, you may then move the target 1 square.\n";
        operations=Arrays.asList(sameSquare,selectTarget1,damage3,moveTarget1);
        effects.put(new Effect(basicMode,description,Collections.emptyList(), operations), 0);
        description = "Deal 2 damage to 1 target on any square exactly one move away.\n";
        operations=Arrays.asList(maxDistance1,minDistance0,selectTarget1,damage2);
        effects.put(new Effect("Long barrel mode",description,Collections.emptyList(), operations), 0);

        weapons.add(new Weapon("SHOTGUN", "", AmmoColor.YELLOW, Collections.singletonList(AmmoColor.YELLOW),
                false,effects));


        effects = new HashMap<>();
        description = "Choose 1 target on any square exactly 1 move away. Move onto that square " +
                "and give the target 1 damage and 2 marks.\n";
        operations = Arrays.asList(maxDistance1, minDistance0, selectTarget1, damage1, mark2, moveToTarget);
        effects.put(new Effect(basicMode,description, Collections.emptyList(), operations), 0);
        description = "Choose a square exactly 1 move away. Move onto that square. You may deal 2 damage to 1 target there. " +
                "If you want, you may move 1 more square in that same direction (but only if it is a legal " +
                "move). You may deal 2 damage to 1 target there, as well.\n";
        operations = Arrays.asList(maxDistance1, minDistance0, selectTarget1, new NextSquareInDirection(),
                moveToTarget, damage2, targetOnEffectSquare, selectTarget1, moveToTarget, damage2);
        effects.put(new Effect("Rocket fist mode",description, Collections.emptyList(), operations), 0);
        description = "Notes: In rocket fist mode, you're flying 2 squares in a straight line, punching 1 person per square.\n";

        weapons.add(new Weapon("POWER GLOVE", description, AmmoColor.YELLOW, Collections.singletonList(AmmoColor.BLUE),
                false, effects));


        effects = new HashMap<>();
        description = "Choose up to 3 targets on different squares, each exactly 1 move away. " +
                "Deal 1 damage to each target.\n";
        operations = Arrays.asList(maxDistance1, minDistance0, new SelectTargets(3,true), damage1);
        effects.put(new Effect(basicMode,description, Collections.emptyList(), operations), 0);
        description = "Deal 1 damage to all targets that are exactly 1 move away.\n";
        operations = Arrays.asList(maxDistance1, minDistance0, selectAllTarget, damage1);
        effects.put(new Effect("Tin tsunami mode",description, Collections.emptyList(), operations), 0);

        weapons.add(new Weapon("SHOCKWAVE", "", AmmoColor.YELLOW, Collections.emptyList(),
                false, effects));


        effects = new HashMap<>();
        description = "Deal 2 damage to 1 target on your square.\n";
        operations = Arrays.asList(sameSquare,selectTarget1,damage2);
        effects.put(new Effect(basicMode,description,Collections.emptyList(), operations),0);
        description = "Deal 3 damage to 1 target on your square, then move that target 0, 1, " +
                "or 2 squares in one direction.\n";
        operations=Arrays.asList(sameSquare,selectTarget1,damage3,new Repel(2));
        effects.put(new Effect("Pulverize mode",description,Collections.emptyList(), operations),0);
        description = "Notes: Remember that moves go through doors, but not walls.\n";
        weapons.add(new Weapon("SLEDGEHAMMER", description, AmmoColor.YELLOW, Collections.emptyList(),
                false,effects));


        return weapons;
    }
}
