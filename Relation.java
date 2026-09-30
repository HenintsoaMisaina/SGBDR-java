import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import  java.util.Date;

import org.w3c.dom.Attr;

public class Relation {
    String nom;
    Attribut[] attribut;
    List<Object[]> individu;

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
            String AtrType = this.attribut[i].getDomaine().getReferenceType().getClass().getSimpleName(); // "pour
                                                                                                          // le
                                                                                                          // nom :
                                                                                                          // INT
                                                                                                          // ** car
                                                                                                          // c'est
                                                                                                          // la
                                                                                                          // taille
                                                                                                          // qui
                                                                                                          // est
                                                                                                          // dans
                                                                                                          // type "

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
            } else if (AtrType.equalsIgnoreCase(nupletType)) {
                /* INTEGRATION NORMALE DANS LE TABLEAU DOUBLE */
                // verification
                // System.out.println("je suis passer ici : int | date | double");
                //
                if (AtrName.equalsIgnoreCase("date")) {
                    Date thisDate = (Date) nup;
                    String yymmdd = thisDate.toString();
                    toAdd[i] = yymmdd;
                }else {
                    toAdd[i] = nup;
                }

            }

        }

        individu.add(toAdd);

    }

    public void select() {
        int nbrIndividu = this.individu.size();
        int nbrAttribut = this.attribut.length;
        for (int i = 0; i < nbrIndividu; i++) {
            Object[] thisOne = this.individu.get(i);
            String ligne = "| ";
            for (int j = 0; j < nbrAttribut; j++) {
                ligne = ligne + thisOne[j] + " | ";
            }
            System.out.println(ligne);
        }
    }

}
