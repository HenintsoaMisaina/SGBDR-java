import java.util.Vector;

public class Attribut {
    String name;
    Domaine domaine;

    public Attribut(String anarana, Domaine dom) {
        this.name = anarana;
        this.domaine = dom;
    }

    public String getName(){
        return this.name;
    }

    public Domaine getDomaine(){
        return this.domaine;
    }

}