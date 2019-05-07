package model.card;

import java.util.*;

//genera tutti weapon e scrivere in file.
public class GenerationWeaponCards {
    public static void main(String[] args){
        List<Weapon> weapons =new ArrayList<>();
        List<Effect> basicEffect ;
        List<Effect> otherEffect ;
        List<Operation> operations;
        Effect basicMode;

        //operation without attribute
        VisiblePlayers visiblePlayers = new VisiblePlayers();
        SameSquare sameSquare=new SameSquare();
        SetTargetToSelected setTargetToSelected = new SetTargetToSelected();
        SelectTargets selectTarget1 = new SelectTargets(1,false);
        SelectTargets selectTargets2 = new SelectTargets(2,false);
        SelectFromSelectedTargets selectFromSelectedTargets1 = new SelectFromSelectedTargets(1);
        SelectAllTarget selectAllTarget=new SelectAllTarget();
        SetTargetPositionAsEffectSquare setTargetPositionAsEffectSquare = new SetTargetPositionAsEffectSquare();
        DirectionTargets directionTargets=new DirectionTargets(0,true);
        TargetOnEffectSquare targetOnEffectSquare = new TargetOnEffectSquare();
        MinOrMaxDistance minDistance0=new MinOrMaxDistance(0,false);
        MinOrMaxDistance maxDistance1=new MinOrMaxDistance(1,true);
        Run run2 = new Run(2);
        MoveTargetToEffevtSquare moveTargetToEffevtSquare = new MoveTargetToEffevtSquare();
        MoveTarget moveTarget1=new MoveTarget(1);
        Damage damage1 =new Damage(1);
        Damage damage2 = new Damage(2);
        Damage damage3 = new Damage(3);
        Mark mark1 = new Mark(1);
        Mark mark2 = new Mark(2);


        String description = "basic effect: Deal 2 damage and 1 mark to 1 target  you can see.\n" +
                " with second lock: Deal 1 mark to a different target  you can see.\n";
        operations=new ArrayList<>(Arrays.asList(visiblePlayers,selectTarget1,damage2,mark1,setTargetToSelected));
        basicEffect = Collections.singletonList((new Effect(Collections.emptyList(), operations)));
        operations=Arrays.asList(selectTarget1,mark1);
        otherEffect = Collections.singletonList(new Effect(Collections.singletonList(AmmoColor.RED), operations));

        weapons.add(new OptionalWeapon("LOCK RIFLE", description, AmmoColor.BLUE,Collections.singletonList(AmmoColor.BLUE),
                basicEffect, otherEffect));

        description ="basic effect: Choose 1 or 2 targets you can see and deal 1 damage to each.\n" +
                "with focus shot: Deal 1 additional damage to one of those targets.\n" +
                "with turret tripod: Deal 1 additional damage to the other of those targets and/or deal 1 damage to a different target you can see.\n" +
                "Notes: If you deal";
        operations = new ArrayList<>(Arrays.asList( visiblePlayers, selectTargets2, damage1, setTargetToSelected));
        basicEffect = Collections.singletonList((new Effect(Collections.emptyList(), operations)));
        operations= new ArrayList<>(Arrays.asList( selectFromSelectedTargets1, damage1, setTargetToSelected));
        otherEffect = new ArrayList<>(Collections.singletonList(new Effect(Collections.singletonList(AmmoColor.YELLOW), operations)));
        operations= Arrays.asList( selectFromSelectedTargets1, selectTarget1, damage1);
        otherEffect.add(new Effect(Collections.singletonList(AmmoColor.BLUE), operations));

        weapons.add(new OptionalWeapon("MACHINE GUN",description,AmmoColor.BLUE,Collections.singletonList(AmmoColor.RED),
                basicEffect,otherEffect));



        description ="basic effect: Deal 2 damage to 1 target you can see.\n" +
                "with chain reaction: Deal 1 damage to a second target that your first target can see.\n" +
                "with high voltage: Deal 2 damage to a third target that your second target can see. " +
                "You cannot use this effect unless you first use the chain reaction.\n" +
                "Notes: This card constrains the order in which you can use its effects. " +
                "(Most cards don't.) Also note that each target must be a different player." ;
        operations = Arrays.asList(visiblePlayers, selectTarget1, damage2,setTargetToSelected);
        basicEffect = new ArrayList<>(Collections.singletonList((new Effect(Collections.emptyList(), operations))));
        operations= Arrays.asList(new ThorTargets(0), selectTarget1,damage1,setTargetToSelected);
        otherEffect = new ArrayList<>(Collections.singletonList((new Effect(Collections.singletonList(AmmoColor.BLUE), operations))));
        operations= Arrays.asList(new ThorTargets(1), selectTarget1,damage2);
        otherEffect.add(new Effect(Collections.singletonList(AmmoColor.BLUE), operations));

        weapons.add(new OptionalWeapon("T.H.O.R",description,AmmoColor.RED,Collections.singletonList(AmmoColor.BLUE),
                basicEffect,otherEffect));

        description ="basic effect: Deal 2 damage to 1 target you can see.\n" +
                "with phase glide: Move 1 or 2 squares. This effect can be used either before or after the basic effect.\n" +
                "with charged shot: Deal 1 additional damage to your target.\n" +
                "Notes: The two moves have no ammo cost. You don't have to be able to see your target when you play the card. " +
                "For example, you can move 2 squares and shoot a target you now see. You cannot use 1 move before shooting and " +
                "1 move after.";
//      in this weapon the basic effect is same as previous just add the run effect.
        operations= Collections.singletonList(run2);
        basicEffect.add(new Effect(Collections.emptyList(), operations));
        operations= Arrays.asList(selectFromSelectedTargets1,damage1);
        otherEffect = Collections.singletonList((new Effect(Collections.singletonList(AmmoColor.BLUE), operations)));

        weapons.add(new OptionalWeapon("PLASMA GUN",description,AmmoColor.YELLOW,Collections.singletonList(AmmoColor.BLUE),
                basicEffect,otherEffect));

        description = "effect: Deal 3 damage and 1 mark to 1 target you can see. " +
                "Your target must be at least 2 moves away from you.\n" +
                "Notes: For example, in the 2-by-2 room, you cannot shoot " +
                "a target on an adjacent square, but you can shoot a target " +
                "on the diagonal. If you are beside a door, you can't shoot " +
                "a target on the other side of the door, but you can shoot " +
                "a target on a different square of that room.";
        operations=Arrays.asList(visiblePlayers,new MinOrMaxDistance(1,false),selectTarget1,damage3);

        weapons.add(new AlternativeWeapon("WHISPER", description, AmmoColor.BLUE, Arrays.asList(AmmoColor.BLUE,AmmoColor.YELLOW),
                new Effect(Collections.emptyList(), operations),null));

        description = "basic mode: Deal 1 damage to every other player " +
                "on your square.\n" +
                "in reaper mode: Deal 2 damage to every other player " +
                "on your square.";
        operations=Arrays.asList(sameSquare,selectAllTarget,damage1);
        basicMode=new Effect(Collections.emptyList(), operations);
        operations=Arrays.asList(sameSquare,selectAllTarget,damage2);
        weapons.add(new AlternativeWeapon("ELECTROSCYTHE", description, AmmoColor.BLUE, Collections.emptyList(),
                basicMode, new Effect(Arrays.asList(AmmoColor.BLUE,AmmoColor.RED), operations)));

        description = "basic mode: Move a target 0, 1, or 2 squares to a square " +
                "you can see, and give it 1 damage.\n" +
                "in punisher mode: Choose a target 0, 1, or 2 moves away " +
                "from you. Move the target to your square " +
                "and deal 3 damage to it.\n" +
                "Notes: You can move a target even if you can't see it. " +
                "The target ends up in a place where you can see and " +
                "damage it. The moves do not have to be in the same " +
                "direction.";
        operations=Arrays.asList(visiblePlayers,new AddPossibleTargetBeforeMove(2,false),
                selectTarget1,new MoveTargetToVisible(2), damage1);
        basicMode=new Effect(Collections.emptyList(), operations);
        operations=Arrays.asList(visiblePlayers,new AddPossibleTargetBeforeMove(2,true),
                selectTarget1,new SetPlayerPositionAsEffectSquare(),moveTargetToEffevtSquare,selectTarget1,damage1);

        weapons.add(new AlternativeWeapon("TRACTOR BEAM", description, AmmoColor.BLUE, Collections.emptyList(),
                basicMode, new Effect(Arrays.asList(AmmoColor.RED,AmmoColor.YELLOW), operations)));

        description ="basic effect: Choose a square you can see, but not your " +
                "square. Call it \"the vortex\". Choose a target on the vortex " +
                "or 1 move away from it. Move it onto the vortex and give it " +
                "2 damage.\n" +
                "with black hole: Choose up to 2 other targets on the " +
                "vortex or 1 move away from it. Move them onto the vortex " +
                "and give them each 1 damage.\n" +
                "Notes: The 3 targets must be different, but some might " +
                "start on the same square. It is legal to choose targets on " +
                "your square, on the vortex, or even on squares you can't " +
                "see. They all end up on the vortex.";
        operations= Arrays.asList(new SelectEffectSquare(1),selectTarget1,moveTargetToEffevtSquare,damage1,setTargetToSelected);
        basicEffect = Collections.singletonList(new Effect(Collections.emptyList(), operations));
        operations= Arrays.asList(selectTargets2,damage1);
        otherEffect = Collections.singletonList(new Effect(Collections.singletonList(AmmoColor.RED), operations));

        weapons.add(new OptionalWeapon("VORTEX CANNON",description,AmmoColor.RED,Collections.singletonList(AmmoColor.BLUE),
                basicEffect,otherEffect));

        description = "basic mode: Choose a room you can see, but not the room " +
                "you are in. Deal 1 damage to everyone in that room. " +
                "in cozy fire mode: Choose a square exactly one move " +
                "away. Deal 1 damage and 1 mark to everyone on that " +
                "square.";
        operations=Arrays.asList(new Furance(false),selectAllTarget,damage1);
        basicMode=new Effect(Collections.emptyList(), operations);
        operations=Arrays.asList(new Furance(true),selectAllTarget,damage1,mark1);
        weapons.add(new AlternativeWeapon("FURNACE", description, AmmoColor.RED, Collections.singletonList(AmmoColor.BLUE),
                basicMode, new Effect(Arrays.asList(AmmoColor.RED,AmmoColor.YELLOW), operations)));

        description = "effect: Choose 1 target you cannot see and deal 3 damage " +
                "to it.\n" +
                "Notes: Yes, this can only hit targets you cannot see.";
        operations=Arrays.asList(visiblePlayers,new Heatseekker(),selectTarget1,damage3);

        weapons.add(new AlternativeWeapon("HEATSEEKER", description, AmmoColor.RED, Arrays.asList(AmmoColor.RED,AmmoColor.YELLOW),
                new Effect(Collections.emptyList(), operations),null));

        description = "basic mode: Deal 1 damage to 1 target you can see at least " +
                "1 move away. Then give 1 mark to that target and everyone " +
                "else on that square.\n" +
                "in nano-tracer mode: Deal 1 damage to 1 target you can " +
                "see at least 1 move away. Then give 2 marks to that target " +
                "and everyone else on that square.";
        operations=Arrays.asList(visiblePlayers,minDistance0,selectTarget1,damage1,mark1,
                setTargetPositionAsEffectSquare,targetOnEffectSquare,selectAllTarget,mark1);
        basicMode=new Effect(Collections.emptyList(), operations);
        operations=Arrays.asList(visiblePlayers,minDistance0,selectTarget1,damage1,mark1,
                setTargetPositionAsEffectSquare,targetOnEffectSquare,selectAllTarget,mark2);
        weapons.add(new AlternativeWeapon("HELLION", description, AmmoColor.RED, Collections.singletonList(AmmoColor.YELLOW),
                basicMode, new Effect( Collections.singletonList(AmmoColor.RED), operations)));

        description = "basic mode: Choose a square 1 move away and possibly a second square " +
                "1 more move away in the same direction. On each square, you may " +
                "choose 1 target and give it 1 damage.\n" +
                "in barbecue mode: Choose 2 squares as above. Deal 2 damage to " +
                "everyone on the first square and 1 damage to everyone on the second " +
                "square.\n" +
                "Notes: This weapon cannot damage anyone in your square. However, " +
                "it can sometimes damage a target you can't see – the flame won't go " +
                "through walls, but it will go through doors. Think of it as a straight-line " +
                "blast of flame that can travel 2 squares in a cardinal direction.";
        operations=Arrays.asList(new DirectionTargets(2,false),minDistance0,
                new SelectTargets(2,true),damage1);
        basicMode=new Effect(Collections.emptyList(), operations);
//        operations=Arrays.asList(new DirectionTargets(2,false),minDistance0,selectTarget1,damage1,mark1,
//                setTargetPositionAsEffectSquare,targetOnEffectSquare,selectAllTarget,mark2); TODO
        weapons.add(new AlternativeWeapon("FLAMETHROWER", description, AmmoColor.RED, Collections.emptyList(),
                basicMode, new Effect( Arrays.asList(AmmoColor.YELLOW,AmmoColor.YELLOW), operations)));


        description ="basic effect: Deal 1 damage to 1 target you can see. Then you may move " +
                "the target 1 square.\n" +
                "with extra grenade: Deal 1 damage to every player on a square you can " +
                "see. You can use this before or after the basic effect's move.\n" +
                "Notes: For example, you can shoot a target, move it onto a square with " +
                "other targets, then damage everyone including the first target. " +
                "Or you can deal 2 to a main target, 1 to everyone else on that square, " +
                "then move the main target. Or you can deal 1 to an isolated target and " +
                "1 to everyone on a different square. If you target your own square, " +
                "you will not be moved or damaged.";
        operations= Arrays.asList(visiblePlayers,selectTarget1,damage1,setTargetPositionAsEffectSquare,moveTarget1,setTargetToSelected);
        basicEffect = new ArrayList<>(Collections.singletonList(new Effect(Collections.emptyList(), operations)));
        operations= Arrays.asList(new SelectEffectSquare(0),selectAllTarget,damage1);
        basicEffect.add(new Effect(Collections.singletonList(AmmoColor.RED), operations));

        weapons.add(new OptionalWeapon("GRENADE LAUNCHER",description,AmmoColor.RED,Collections.singletonList(AmmoColor.RED),
                basicEffect,null));

        description ="basic effect: Deal 2 damage to 1 target you can see that is not on your " +
                "square. Then you may move the target 1 square.\n" +
                "with rocket jump: Move 1 or 2 squares. This effect can be used either " +
                "before or after the basic effect.\n" +
                "with fragmenting warhead: During the basic effect, deal 1 damage to " +
                "every player on your target's original square – including the target, " +
                "even if you move it.\n" +
                "Notes: If you use the rocket jump before the basic effect, you consider " +
                "only your new square when determining if a target is legal. You can " +
                "even move off a square so you can shoot someone on it. If you use the " +
                "fragmenting warhead, you deal damage to everyone on the target's " +
                "square before you move the target – your target will take 3 damage total.";
        operations = Arrays.asList(visiblePlayers,minDistance0, selectTarget1, damage2,moveTarget1,setTargetToSelected);
        basicEffect = new ArrayList<>(Collections.singletonList((new Effect(Collections.emptyList(), operations))));
        operations= Collections.singletonList(run2);
        basicEffect.add(new Effect(Collections.singletonList(AmmoColor.BLUE), operations));
        operations= Arrays.asList(new SelectEffectSquare(0),selectAllTarget,damage1);
        basicEffect.add(new Effect(Collections.singletonList(AmmoColor.YELLOW), operations));

        weapons.add(new OptionalWeapon("ROCKET LAUNCHER",description,AmmoColor.RED,Collections.singletonList(AmmoColor.RED),
                basicEffect,null));

        description = "basic mode: Choose a cardinal direction and 1 target in that direction. " +
                "Deal 3 damage to it.\n" +
                "in piercing mode: Choose a cardinal direction and 1 or 2 targets in that " +
                "direction. Deal 2 damage to each.\n" +
                "Notes: Basically, you're shooting in a straight line and ignoring walls. " +
                "You don't have to pick a target on the other side of a wall – it could even " +
                "be someone on your own square – but shooting through walls sure is " +
                "fun. There are only 4 cardinal directions. You imagine facing one wall or " +
                "door, square-on, and firing in that direction. Anyone on a square in that " +
                "direction (including yours) is a valid target. In piercing mode, " +
                "the 2 targets can be on the same square or on different squares.";
        operations=Arrays.asList(directionTargets,selectTarget1,damage3);
        basicMode=new Effect(Collections.emptyList(), operations);
        operations=Arrays.asList(directionTargets,selectTargets2,damage2);
        weapons.add(new AlternativeWeapon("RAILGUN", description, AmmoColor.YELLOW, Arrays.asList(AmmoColor.YELLOW,AmmoColor.BLUE),
                basicMode, new Effect( Collections.emptyList(), operations)));

        description ="basic effect: Deal 2 damage to 1 target on your square.\n" +
                "with shadowstep: Move 1 square before or after the basic effect.\n" +
                "with slice and dice: Deal 2 damage to a different target on your square. " +
                "The shadowstep may be used before or after this effect.\n" +
                "Notes: Combining all effects allows you to move onto a square and " +
                "whack 2 people; or whack somebody, move, and whack somebody else; " +
                "or whack 2 people and then move.";
        operations = Arrays.asList(sameSquare,selectTarget1,damage2,setTargetToSelected);
        basicEffect = new ArrayList<>(Collections.singletonList((new Effect(Collections.emptyList(), operations))));
        operations= Collections.singletonList(run2);
        basicEffect.add(new Effect(Collections.emptyList(), operations));
        operations= Arrays.asList(selectTarget1,damage2);
        otherEffect = Collections.singletonList(new Effect(Collections.singletonList(AmmoColor.YELLOW), operations));

        weapons.add(new OptionalWeapon("CYBERBLADE",description,AmmoColor.YELLOW,Collections.singletonList(AmmoColor.RED),
                basicEffect,otherEffect));

        description = "basic mode: Deal 1 damage and 2 marks to " +
                "1 target you can see.\n" +
                "in scanner mode: Choose up to 3 targets you " +
                "can see and deal 1 mark to each.\n" +
                "Notes: Remember that the 3 targets can be " +
                "in 3 different rooms.";
        operations=Arrays.asList(visiblePlayers,selectTarget1,damage1,mark2);
        basicMode=new Effect(Collections.emptyList(), operations);
        operations=Arrays.asList(visiblePlayers,new SelectTargets(3,false),mark1);
        weapons.add(new AlternativeWeapon("ZX-2", description, AmmoColor.YELLOW, Collections.singletonList(AmmoColor.RED),
                basicMode, new Effect( Collections.emptyList(), operations)));

        description = "basic mode: Deal 3 damage to 1 target on " +
                "your square. If you want, you may then move " +
                "the target 1 square.\n" +
                "in long barrel mode: Deal 2 damage to " +
                "1 target on any square exactly one move " +
                "away.";
        operations=Arrays.asList(sameSquare,selectTarget1,damage3,moveTarget1);
        basicMode=new Effect(Collections.emptyList(), operations);
        operations=Arrays.asList(maxDistance1,minDistance0,selectTarget1,damage2);
        weapons.add(new AlternativeWeapon("SHOTGUN", description, AmmoColor.YELLOW, Collections.singletonList(AmmoColor.YELLOW),
                basicMode, new Effect( Collections.emptyList(), operations)));

        description = "basic mode: Choose 1 target on any square " +
                "exactly 1 move away. Move onto that square " +
                "and give the target 1 damage and 2 marks. " +
                "in rocket fist mode: Choose a square\n" +
                "exactly 1 move away. Move onto that square. " +
                "You may deal 2 damage to 1 target there. " +
                "If you want, you may move 1 more square in " +
                "that same direction (but only if it is a legal " +
                "move). You may deal 2 damage to 1 target " +
                "there, as well.\n" +
                "Notes: In rocket fist mode, you're flying " +
                "2 squares in a straight line, punching " +
                "1 person per square.";
        operations=Arrays.asList(maxDistance1,minDistance0,selectTarget1,damage1,mark2,new MoveToTarget());
        basicMode=new Effect(Collections.emptyList(), operations);
//        operations=Arrays.asList(maxDistance1,minDistance0,selectTarget1,damage2); TODO
        weapons.add(new AlternativeWeapon("POWER GLOVE", description, AmmoColor.YELLOW, Collections.singletonList(AmmoColor.BLUE),
                basicMode, new Effect( Collections.singletonList(AmmoColor.BLUE), operations)));

        description = "basic mode: Choose up to 3 targets on " +
                "different squares, each exactly 1 move away. " +
                "Deal 1 damage to each target.\n" +
                "in tsunami mode: Deal 1 damage to all " +
                "targets that are exactly 1 move away.";
        operations=Arrays.asList(maxDistance1,minDistance0,new SelectTargets(3,true),damage1);
        basicMode=new Effect(Collections.emptyList(), operations);
        operations=Arrays.asList(maxDistance1,minDistance0,selectAllTarget,damage1);
        weapons.add(new AlternativeWeapon("SHOCKWAVE", description, AmmoColor.YELLOW, Collections.emptyList(),
                basicMode, new Effect( Collections.singletonList(AmmoColor.YELLOW), operations)));

        description = "basic mode: Deal 2 damage to 1 target on " +
                "your square.\n" +
                "in pulverize mode: Deal 3 damage to 1 target " +
                "on your square, then move that target 0, 1, " +
                "or 2 squares in one direction.\n" +
                "Notes: Remember that moves go through " +
                "doors, but not walls.";
        operations=Arrays.asList(sameSquare,selectTarget1,damage2);
        basicMode=new Effect(Collections.emptyList(), operations);
        operations=Arrays.asList(sameSquare,selectTarget1,damage3);//TODO
        weapons.add(new AlternativeWeapon("SLEDGEHAMMER", description, AmmoColor.YELLOW, Collections.emptyList(),
                basicMode, new Effect( Collections.singletonList(AmmoColor.RED), operations)));


    }
}
