package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*Objectif : avoir un domaine comme dans tous SGBDR*/

/*Idée : le sgbdr doit avoir des syntaxe precis comme dans tout sgbdr connu */

/*Approche : pour chaque type de domaine classique (int, double, varchar , date) , on met dans notre Object[] type un valeur par défaut de meme type que le domaine choisi */

/*NB : pour l'enum (cad definir un domaine spécifique) : en cours */
public class Domaine {
    String name;
    Object[] type;
    Domaine[] sousDomaines;

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
            Date a = new Date(1999, 1, 1);
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

    // plusieurs domaines (cree par union) =====
    private Domaine(Domaine[] membres) {
        this.name = "UNION";
        this.sousDomaines = membres;
        this.type = membres;
    }

    public Domaine[] getSousDomaines() {
        return this.sousDomaines;
    }

    // deux domaines simples sont identiques : meme type et memes parametres
    public boolean identique(Domaine autre) {
        return this.name.equalsIgnoreCase(autre.name) && Arrays.equals(this.type, autre.type);
    }

    private static void ajouterMembres(List<Domaine> liste, Domaine d) {
        Domaine[] membres = d.sousDomaines;
        if (membres == null) {
            membres = new Domaine[] { d };
        }
        for (Domaine m : membres) {
            boolean deja = false;
            for (Domaine x : liste) {
                if (x.identique(m)) {
                    deja = true;
                    break;
                }
            }
            if (!deja) {
                liste.add(m);
            }
        }
    }

    // meme domaine si identiques, sinon domaine compose (sans doublon)
    public static Domaine fusion(Domaine d1, Domaine d2) {
        List<Domaine> liste = new ArrayList<>();
        ajouterMembres(liste, d1);
        ajouterMembres(liste, d2);
        if (liste.size() == 1) {
            return liste.get(0);
        }
        return new Domaine(liste.toArray(new Domaine[0]));
    }

    public String toString() {
        if (sousDomaines != null) {
            String s = "";
            for (int i = 0; i < sousDomaines.length; i++) {
                if (i > 0) {
                    s = s + " | ";
                }
                s = s + sousDomaines[i].toString();
            }
            return "[" + s + "]";
        }
        if (name.equalsIgnoreCase("varchar")) {
            return "VARCHAR(" + type[0] + ")";
        }
        if (name.equalsIgnoreCase("enum")) {
            return "ENUM" + Arrays.toString(type);
        }
        if (name.equalsIgnoreCase("int") && type.length == 2) {
            return "INT[" + type[0] + ".." + type[1] + "]";
        }
        return name.toUpperCase();
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