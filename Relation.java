import java.io.IOException;
import java.lang.reflect.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Attr;

import java.util.Date;

public class Relation {
    String nom;
    Attribut[] attribut;
    List<Object[]> individu;

    public void setNom(String name) {
        this.nom = name;
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
            tete = tete + this.attribut[j].getName() + " |";
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
        int nbrIndividu = this.individu.size();
        int nbrAttribut = this.attribut.length;
        int[] index = new int[attributs.length];

        for (int j = 0; j < attributs.length; j++) {
            Attribut projection = attributs[j];
            for (int i = 0; i < nbrAttribut; i++) {
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



}
