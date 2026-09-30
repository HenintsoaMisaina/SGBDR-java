import  java.util.Date;

/*Objectif : avoir un domaine comme dans tous SGBDR*/

/*Idée : le sgbdr doit avoir des syntaxe precis comme dans tout sgbdr connu */

/*Approche : pour chaque type de domaine classique (int, double, varchar , date) , on met dans notre Object[] type un valeur par défaut de meme type que le domaine choisi */

/*NB : pour l'enum (cad definir un domaine spécifique) : en cours */
public class Domaine {
    String name;
    Object[] type;

    public Domaine(){}
    
    public Domaine(String srt){
        if (srt.equalsIgnoreCase("INTEGER")){
            this.name = srt;
            this.type = new Object[]{1};
        }else if (srt.equalsIgnoreCase("DOUBLE")){
            this.name = srt;
            Double a = 0.1;
            this.type = new Object[]{a};
        }else if (srt.equalsIgnoreCase("DATE")){
            this.name = srt;
            Date a = new Date(1999, 01 ,01);
            this.type = new Object[]{a};
        }
    }

    public Domaine(String varchar, int taille){
        if (varchar.equalsIgnoreCase("VARCHAR")){
            this.name = "VARCHAR";
            this.type = new Object[]{taille};
        }else {
            System.out.println("erreur : VARCHAR|varchar is the correct syntax");
        }
    }

    public Domaine(String enumeration, Object[] choix){
        if (enumeration.equalsIgnoreCase("ENUM")){
            this.name = "ENUM";
            this.type = choix;
        }else {
            System.out.println("erreur de syntax");
        }
    }
    
    public String getName(){
        return this.name;
    }

    public Object getReferenceType(){
        return this.type[0];
    }

    public Object[] getType(){
        return this.type;
    }

}