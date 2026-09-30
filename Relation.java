import java.lang.reflect.*;

import org.w3c.dom.Attr;

public class Relation {
    String nom;
    Attribut[] attribut;
    Object[][] individu;

    public Relation(String nom, Attribut[] Atr) {
        this.nom = nom;
        this.attribut = Atr;
    }

    public int getAttributLength() {
        return attribut.length;
    }

    public void add(Object[] nuplet) {
        int tailleAtr = this.getAttributLength();
        for (int i = 0; i < tailleAtr; i++) {
            /* exemple x1 => domaine : varchar */
            String AtrName = this.attribut[i].getDomaine().getName(); // "pour le nom : VARCHAR"
            String AtrType = this.attribut[i].getDomaine().getReferenceType().getClass().getSimpleName(); // "pour le
                                                                                                          // nom : INT
                                                                                                          // ** car
                                                                                                          // c'est la
                                                                                                          // taille qui
                                                                                                          // est dans
                                                                                                          // type "

            String nupletType = nuplet[i].getClass().getSimpleName(); // "pour le nom : String"

            if (AtrName.equalsIgnoreCase("enum")) {
                /* on va regarder un par un les choix d'enum et voir si le nup y correspond */
                Object nup = nuplet[i];
                Object[] choices = attribut[i].getDomaine().getType();
                for (int j = 0; j < choices.length; j++) {
                    if (nup.equals(choices[j])) {
                        /* INTEGRATION NORMALE DANS LE TABLEAU DOUBLE */

                        break;
                    }
                }

            } else if (AtrName.equalsIgnoreCase("varchar")) {
                int tailleMax = (int) attribut[i].getDomaine().getReferenceType();

                if (nupletType.equalsIgnoreCase("string")) {
                    String stringNuplet = (String) nuplet[i];
                    if (stringNuplet.length() <= tailleMax) {
                        /* INTEGRATION NORMALE DANS LE TABLEAU DOUBLE */

                        break;
                    }
                } else {
                    System.out.println("données entrer n'est pas un varchar");
                }
            } else if (AtrType.equalsIgnoreCase(nupletType)) {
                /* INTEGRATION NORMALE DANS LE TABLEAU DOUBLE */
            }

        }
    }

}