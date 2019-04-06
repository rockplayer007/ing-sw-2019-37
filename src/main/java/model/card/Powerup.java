package model.card;

public class Powerup extends Card {
    private int key; //Mirino key=1,Raggio 2, Granata 3 , Teletr. 4
    private AmmoColor ammo;

    public Powerup (String name, String description, AmmoColor ammo, int key){
        this.name=name;
        this.description=description;
        this.ammo=ammo;
        this.key=key;
    }
    public AmmoColor getAmmoColor(){
        return ammo;
    }

    public int getKey(){
        return key;
    }

    public void operation(){
        switch (key){
            case 1: //eseguire quando stai attaccando
                    //metodo per pagare cubo munizioni, serve la plancia del giocatore
                    //metodo scegli bersaglio
                    //metodo infliggi 1 danno
                    break;
            case 2: //può essere usato nel proprio turno esclusivamente
                    //metodo scegli bersaglio
                    //metodo muovi bersaglio (1,2 quadrati in una direzione)
                    break;
            case 3: //quando ricevi danno da giocatore che PUOI VEDERE
                    //metodo dai danno
                    break;
            case 4: //può essere usato nel proprio turno esclusivamente
                    //muovi bersaglio
                    break;
            default:;
        }

    }
}
