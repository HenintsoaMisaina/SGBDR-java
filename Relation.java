import java.io.IOException;
import java.lang.reflect.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import javax.security.sasl.RealmCallback;

import org.w3c.dom.Attr;

import java.util.Date;

public class Relation {
    String nom;
    Attribut[] attribut;
    List<Object[]> individu;

    public void setNom(String name) {
        this.nom = name;
    }

    public List getIndividu() {
        return this.individu;
    }

    public Attribut[] getAttribut() {
        return this.attribut;
    }

    public Relation(String nom, Attribut[] Atr) {
        this.nom = nom;
        this.attribut = Atr;
        this.individu = new ArrayList<>(100);
    }

    public int getAttributLength() {
        return attribut.length;
    }

    public void add(Object[] nuplet) {

        int tailleAtr = this.getAttributLength();
        Object[] toAdd = new Object[tailleAtr];
        for (int i = 0; i < tailleAtr; i++) {
            /* exemple x1 => domaine : varchar */
            String AtrName = this.attribut[i].getDomaine().getName(); // "pour le nom : VARCHAR"

            String nupletType = nuplet[i].getClass().getSimpleName(); // "pour le nom : String"
            Object nup = nuplet[i];

            if (AtrName.equalsIgnoreCase("enum")) {
                /* on va regarder un par un les choix d'enum et voir si le nup y correspond */
                // verification
                // System.out.println("je suis passer ici : enum");
                //
                Object[] choices = attribut[i].getDomaine().getType();
                for (int j = 0; j < choices.length; j++) {
                    if (nup.equals(choices[j])) {
                        /* INTEGRATION NORMALE DANS LE TABLEAU DOUBLE */
                        // verification
                        // System.out.println("verification reussi : controle enum");
                        //

                        toAdd[i] = nup;

                    }
                }

            } else if (AtrName.equalsIgnoreCase("varchar")) {
                int tailleMax = (int) attribut[i].getDomaine().getReferenceType();
                // verification
                // System.out.println("je suis passer ici : varchar");
                //
                if (nupletType.equalsIgnoreCase("string")) {
                    String stringNuplet = (String) nuplet[i];
                    // verification
                    // System.out.println("verification reussi : controle varchar 1");
                    //

                    if (stringNuplet.length() <= tailleMax) {
                        // verification
                        // System.out.println("verification reussi : controle varchar 2");
                        //
                        toAdd[i] = nup;
                    }
                } else {
                    System.out.println("données entrer n'est pas un varchar");
                }
            } else {
                /* nb : le doute teto de oe , aona mo zany le int sy integer */
                /*
                 * tsy maninona le izy satria a partir du moment oe tsy enum sy varchar de tsy
                 * mijery name tsika fa type amzay
                 */

                String AtrType = this.attribut[i].getDomaine().getReferenceType().getClass().getSimpleName();
                if (AtrType.equalsIgnoreCase(nupletType)) {
                    /* INTEGRATION NORMALE DANS LE TABLEAU DOUBLE */
                    // verification
                    // System.out.println("je suis passer ici : int | date | double");
                    //
                    if (AtrName.equalsIgnoreCase("date")) {
                        Date thisDate = (Date) nup;
                        String yymmdd = thisDate.toString();
                        toAdd[i] = yymmdd;
                    } else if (AtrName.equalsIgnoreCase("int")) {
                        int numbr = (int) nup;
                        int typeLength = this.attribut[i].getDomaine().getTypeLength();
                        Object[] tip = this.attribut[i].getDomaine().getType();

                        if (typeLength == 2) {
                            int min = (int) tip[0];
                            int max = (int) tip[1];
                            if (numbr >= min && numbr <= max) {
                                toAdd[i] = numbr;
                            }
                        } else {
                            toAdd[i] = nup;
                        }
                    } else {
                        toAdd[i] = nup;
                    }

                }
            }

        }

        individu.add(toAdd);

    }

    public void select() {
        int nbrIndividu = this.individu.size();
        int nbrAttribut = this.attribut.length;
        String tete = "| ";
        for (int j = 0; j < nbrAttribut; j++) {
            tete = tete + this.attribut[j].getName() + " | ";
        }
        System.out.println(tete);
        for (int i = 0; i < nbrIndividu; i++) {
            Object[] thisOne = this.individu.get(i);
            String ligne = "| ";
            for (int j = 0; j < nbrAttribut; j++) {
                ligne = ligne + thisOne[j] + " | ";
            }
            System.out.println(ligne);
        }
        System.out.println("");
    }

    public void NoDoublon() {
        int nbrIndividu = this.individu.size();

        for (int i = 0; i < nbrIndividu; i++) {

            Object[] toCompare = this.individu.get(i);
            for (int j = i + 1; j < nbrIndividu; j++) {
                Object[] toMe = this.individu.get(j);
                if (Arrays.equals(toCompare, toMe)) {
                    this.individu.remove(j);
                    nbrIndividu--;
                    j--;
                }
            }
        }

    }

    public int[] getIndexOfAttribut(Attribut[] attributs) {
        int[] index = new int[attributs.length];
        for (int j = 0; j < attributs.length; j++) {
            Attribut projection = attributs[j];
            for (int i = 0; i < this.attribut.length; i++) {
                Attribut attribut = this.attribut[i];
                if (attribut.equals(projection)) {
                    index[j] = i;
                    break;
                }
            }
        }
        return index;
    }

    public Relation project(Attribut[] toproject) {
        Relation toreturn = new Relation("default", toproject);

        int nbrIndividu = this.individu.size();
        int nbrAttribut = this.attribut.length;
        int[] index = this.getIndexOfAttribut(toproject);

        for (int k = 0; k < nbrIndividu; k++) {
            Object[] concern = this.individu.get(k);
            Object[] ajout = new Object[toproject.length];
            int b = 0;
            for (int o = 0; o < toproject.length; o++) {
                ajout[b] = concern[index[o]];
                b++;
            }
            toreturn.add(ajout);
        }
        toreturn.NoDoublon();
        return toreturn;
    }

    public Relation produit(Relation relation2) {
        Attribut[] Atr1 = this.getAttribut();
        Attribut[] Atr2 = relation2.getAttribut();

        List Ind1 = this.getIndividu();
        List Ind2 = relation2.getIndividu();

        int newAtrLength = relation2.getAttributLength() + this.getAttributLength();
        Attribut[] newAtr = new Attribut[newAtrLength];
        for (int i = 0; i < this.getAttributLength(); i++) {
            newAtr[i] = Atr1[i];
        }
        for (int i = 0; i < relation2.getAttributLength(); i++) {
            newAtr[Atr1.length + i] = Atr2[i];
        }

        Relation toreturn = new Relation("default", newAtr);

        for (int i = 0; i < Ind1.size(); i++) {
            Object[] I = (Object[]) Ind1.get(i);

            for (int j = 0; j < Ind2.size(); j++) {
                List individuel = new ArrayList<>();

                for (int i1 = 0; i1 < I.length; i1++) {
                    individuel.add(I[i1]);
                }

                Object[] J = (Object[]) Ind2.get(j);
                for (int j1 = 0; j1 < J.length; j1++) {
                    individuel.add(J[j1]);
                }

                toreturn.add(individuel.toArray());
            }
        }

        return toreturn;
    }

    public int indexOf(Attribut atr) {
        for (int i = 0; i < this.attribut.length; i++) {
            if (this.attribut[i].equals(atr)) {
                return i;
            }
        }
        return 0;
    }

    public Relation selection_egal(Attribut atr, Object val) {
        Relation toreturn = new Relation("default", getAttribut());
        int x = this.indexOf(atr);
        for (int i = 0; i < this.individu.size(); i++) {
            Object[] one = (Object[]) this.individu.get(i);
            if (one[x].equals(val)) {
                toreturn.add(one);
            }
        }
        return toreturn;
    }

    public Relation selection_different(Attribut atr, Object val) {
        Relation toreturn = new Relation("default", getAttribut());
        int x = this.indexOf(atr);
        for (int i = 0; i < this.individu.size(); i++) {
            Object[] one = (Object[]) this.individu.get(i);
            if (!one[x].equals(val)) {
                toreturn.add(one);
            }
        }
        return toreturn;
    }

    public Relation selection_superieur(Attribut atr, Object val) {
        Relation toreturn = new Relation("default", getAttribut());
        int x = this.indexOf(atr);
        double valeur = ((Number) val).doubleValue();
        for (int i = 0; i < this.individu.size(); i++) {
            Object[] one = (Object[]) this.individu.get(i);
            double courant = ((Number) one[x]).doubleValue();
            if (courant > valeur) {
                toreturn.add(one);
            }
        }
        return toreturn;
    }

    public Relation selection_inferieur(Attribut atr, Object val) {
        Relation toreturn = new Relation("default", getAttribut());
        int x = this.indexOf(atr);
        double valeur = ((Number) val).doubleValue();
        for (int i = 0; i < this.individu.size(); i++) {
            Object[] one = (Object[]) this.individu.get(i);
            double courant = ((Number) one[x]).doubleValue();
            if (courant < valeur) {
                toreturn.add(one);
            }
        }
        return toreturn;
    }

    public Relation selection_superieur_egal(Attribut atr, Object val) {
        Relation toreturn = new Relation("default", getAttribut());
        int x = this.indexOf(atr);
        double valeur = ((Number) val).doubleValue();
        for (int i = 0; i < this.individu.size(); i++) {
            Object[] one = (Object[]) this.individu.get(i);
            double courant = ((Number) one[x]).doubleValue();
            if (courant >= valeur) {
                toreturn.add(one);
            }
        }
        return toreturn;
    }

    public Relation selection_inferieur_egal(Attribut atr, Object val) {
        Relation toreturn = new Relation("default", getAttribut());
        int x = this.indexOf(atr);
        double valeur = ((Number) val).doubleValue();
        for (int i = 0; i < this.individu.size(); i++) {
            Object[] one = (Object[]) this.individu.get(i);
            double courant = ((Number) one[x]).doubleValue();
            if (courant <= valeur) {
                toreturn.add(one);
            }
        }
        return toreturn;
    }

    public Relation jointure(Attribut atr1, String operation, Relation autre, Attribut atr2) {
        /* Operation : "=" "!=" "<" ">" "<=" ">=" */
        Relation toReturn = this.produit(autre);
        int x = this.indexOf(atr1);
        int y = this.getAttributLength() + autre.indexOf(atr2);
        boolean garder;

        for (int i = toReturn.individu.size() - 1; i >= 0; i--) {
            Object[] a = (Object[]) toReturn.individu.get(i);
            garder = true;
            if (operation.equals("=")) {
                garder = Objects.equals(a[x], a[y]);
            } else if (operation.equals("!=")) {
                garder = !Objects.equals(a[x], a[y]);
            } else {
                double gauche = ((Number) a[x]).doubleValue();
                double droite = ((Number) a[y]).doubleValue();
                if (operation.equals(">")) {
                    garder = gauche > droite;
                } else if (operation.equals("<")) {
                    garder = gauche < droite;
                } else if (operation.equals(">=")) {
                    garder = gauche >= droite;
                } else if (operation.equals("<=")) {
                    garder = gauche <= droite;
                }
            }
            if (!garder) {
                toReturn.individu.remove(i);
            }
        }
        return toReturn;
    }

    public Relation jointure(Attribut atr1, String operation, Relation autre, Object value) {
        /* Operation : "=" "!=" "<" ">" "<=" ">=" */
        Relation toReturn = this.produit(autre);
        int x = this.indexOf(atr1);
        boolean garder;

        for (int i = toReturn.individu.size() - 1; i >= 0; i--) {
            Object[] a = (Object[]) toReturn.individu.get(i);
            garder = true;
            if (operation.equals("=")) {
                garder = Objects.equals(a[x], value);
            } else if (operation.equals("!=")) {
                garder = !Objects.equals(a[x], value);
            } else {
                double gauche = ((Number) a[x]).doubleValue();
                double droite = ((Number) value).doubleValue();
                if (operation.equals(">")) {
                    garder = gauche > droite;
                } else if (operation.equals("<")) {
                    garder = gauche < droite;
                } else if (operation.equals(">=")) {
                    garder = gauche >= droite;
                } else if (operation.equals("<=")) {
                    garder = gauche <= droite;
                }
            }
            if (!garder) {
                toReturn.individu.remove(i);
            }
        }
        return toReturn;
    }

    public Relation jointure(Attribut atr1, String operation, Relation autre, Attribut atr2, Number decalage) {
        Relation toReturn = this.produit(autre);
        int x = this.indexOf(atr1);
        int y = this.getAttributLength() + autre.indexOf(atr2);
        boolean garder;

        for (int i = toReturn.individu.size() - 1; i >= 0; i--) {
            Object[] a = (Object[]) toReturn.individu.get(i);
            garder = true;
            double gauche = ((Number) a[x]).doubleValue();
            double droite = ((Number) a[y]).doubleValue() + decalage.doubleValue();
            if (operation.equals("=")) {
                garder = gauche == droite;
            } else if (operation.equals("!=")) {
                garder = gauche != droite;
            } else if (operation.equals(">")) {
                garder = gauche > droite;
            } else if (operation.equals("<")) {
                garder = gauche < droite;
            } else if (operation.equals(">=")) {
                garder = gauche >= droite;
            } else if (operation.equals("<=")) {
                garder = gauche <= droite;
            }
            if (!garder) {
                toReturn.individu.remove(i);
            }
        }
        return toReturn;
    }

    public Relation jointureGauche(Attribut atr1, String operation, Relation autre, Attribut atr2, Number decalage) {
        int tg = this.getAttributLength();
        int td = autre.getAttributLength();
        Attribut[] newAtr = new Attribut[tg + td];
        for (int i = 0; i < tg; i++) {
            newAtr[i] = this.attribut[i];
        }
        for (int i = 0; i < td; i++) {
            newAtr[tg + i] = autre.attribut[i];
        }

        Relation toReturn = new Relation("default", newAtr);
        int x = this.indexOf(atr1);
        int y = autre.indexOf(atr2); 
        boolean garder;

        for (int i = 0; i < this.individu.size(); i++) {
            Object[] g = this.individu.get(i);
            boolean trouve = false;

            for (int j = 0; j < autre.individu.size(); j++) {
                Object[] d = autre.individu.get(j);
                double gauche = ((Number) g[x]).doubleValue() + decalage.doubleValue();
                double droite = ((Number) d[y]).doubleValue();
                garder = false;
                if (operation.equals("=")) {
                    garder = gauche == droite;
                } else if (operation.equals("!=")) {
                    garder = gauche != droite;
                } else if (operation.equals(">")) {
                    garder = gauche > droite;
                } else if (operation.equals("<")) {
                    garder = gauche < droite;
                } else if (operation.equals(">=")) {
                    garder = gauche >= droite;
                } else if (operation.equals("<=")) {
                    garder = gauche <= droite;
                }

                if (garder) {
                    Object[] ligne = new Object[tg + td];
                    System.arraycopy(g, 0, ligne, 0, tg);
                    System.arraycopy(d, 0, ligne, tg, td);
                    toReturn.individu.add(ligne);
                    trouve = true;
                }
            }

            if (!trouve) {
                Object[] ligne = new Object[tg + td];
                System.arraycopy(g, 0, ligne, 0, tg);
                toReturn.individu.add(ligne);
            }
        }
        return toReturn;
    }
}