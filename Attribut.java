import java.util.Vector;

public class Attribut {
    String nom;
    Domaine domaine;

    public Attribut(String anarana, Domaine dom) {
        this.nom = anarana;
        this.domaine = dom;
    }

    public Domaine getDomaine(){
        return this.domaine;
    }

}