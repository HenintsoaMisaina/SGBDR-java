package utils;  

public class Attribut {
    String name;
    Domaine domaine;
    String secondName;

    public Attribut(String anarana, Domaine dom) {
        this.name = anarana;
        this.domaine = dom;
    }

    public Attribut avecDomaine(Domaine nouveau) {
        Attribut c = new Attribut(this.name, nouveau);
        c.secondName = this.secondName;
        return c;
    }

    public Attribut qualifie(String relation) {
        Attribut c = new Attribut(this.name, this.domaine);
        c.secondName = relation + "." + this.name;
        return c;
    }

    public String getName() {
        return this.name;
    }

    public Domaine getDomaine() {
        return this.domaine;
    }


    public String getSecondName() {
        return this.secondName;
    }

}