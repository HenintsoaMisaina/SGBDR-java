package utils;

/*Objectif : avoir un domaine comme dans tous SGBDR*/

/*Idée : le sgbdr doit avoir des syntaxe precis comme dans tout sgbdr connu */

/*Approche : pour chaque type de domaine classique (int, double, varchar , date) , on met dans notre Object[] type un valeur par défaut de meme type que le domaine choisi */

/*NB : pour l'enum (cad definir un domaine spécifique) : en cours */
public class Domaine {
    String name;
    Object[] type;

    public Domaine() {
    }

    public Domaine(String srt) {
        if (srt.equalsIgnoreCase("INTEGER") || srt.equalsIgnoreCase("INT")) {
            this.name = srt;
            this.type = new Object[] { 1 };
        } else if (srt.equalsIgnoreCase("DOUBLE")) {
            this.name = srt;
            Double a = 0.1;
            this.type = new Object[] { a };
        } else if (srt.equalsIgnoreCase("DATE")) {
            this.name = srt;
            Date a = new Date(1999, 01, 01);
            this.type = new Object[] { a };
        } else {
            throw new IllegalArgumentException("Type inconnu : " + srt);
        }
    }

    public Domaine(String intLimite, int a, int b) {
        if (intLimite.equalsIgnoreCase("int")) {
            if (a < b) {
                this.name = intLimite;
                this.type = new Object[] { a, b };
            } else {
                throw new IllegalArgumentException(
                        "Intervalle invalide : min " + a + " doit etre inferieur a max " + b);
            }

        } else {
            throw new IllegalArgumentException("erreur syntaxe : 'int' attendu, recu " + intLimite);
        }
    }

    public Domaine(String varchar, int taille) {
        if (varchar.equalsIgnoreCase("VARCHAR")) {
            if (taille <= 0) {
                throw new IllegalArgumentException("VARCHAR : la taille doit etre superieure a 0, recu " + taille);
            }
            this.name = "VARCHAR";
            this.type = new Object[] { taille };
        } else {
            throw new IllegalArgumentException("erreur : VARCHAR|varchar est le syntaxe attendu, recu " + varchar);
        }
    }

    public Domaine(String enumeration, Object[] choix) {
        if (enumeration.equalsIgnoreCase("ENUM")) {
            if (choix == null || choix.length == 0) {
                throw new IllegalArgumentException("ENUM : la liste des choix ne peut pas etre vide");
            }
            this.name = "ENUM";
            this.type = choix;
        } else {
            throw new IllegalArgumentException("erreur de syntax : 'ENUM' attendu, recu " + enumeration);
        }
    }

    public String getName() {
        return this.name;
    }

    public Object getReferenceType() {
        return this.type[0];
    }

    public Object[] getType() {
        return this.type;
    }

    public int getTypeLength() {
        return this.type.length;
    }

}