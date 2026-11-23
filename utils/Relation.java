package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Relation {
    private String nom;
    private Attribut[] attribut;
    private List<Object[]> individu;

    public Relation() {
    }

    public void setNom(String name) {
        this.nom = name;
    }

    public String getNom() {
        return this.nom;
    }

    public List<Object[]> getIndividu() {
        return this.individu;
    }

    public Attribut[] getAttribut() {
        return this.attribut;
    }

    public Relation as(String alias) {
        Attribut[] a = new Attribut[attribut.length];
        for (int i = 0; i < a.length; i++) {
            a[i] = attribut[i].qualifie(alias);
        }
        Relation r = new Relation(alias, a);
        r.individu.addAll(this.individu);
        return r;
    }

    public Relation(String nom, Attribut[] Atr) {
        this.nom = nom;
        this.attribut = new Attribut[Atr.length];
        for (int i = 0; i < Atr.length; i++) {
            if (Atr[i].getSecondName() == null) {
                this.attribut[i] = Atr[i].qualifie(nom);
            } else {
                this.attribut[i] = Atr[i];
            }
        }
        this.individu = new ArrayList<>(100);
    }

    public int getAttributLength() {
        return attribut.length;
    }

    private boolean accepte(Domaine d, Object valeur) {
        try {
            Relation essai = new Relation("essai", new Attribut[] { new Attribut("c", d) });
            essai.add(new Object[] { valeur });
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void add(Object[] nuplet) {

        // on verifie que le nuplet a autant de valeurs que d'attributs
        if (nuplet.length != this.getAttributLength()) {
            throw new IllegalArgumentException(
                    "Nombre de valeurs incorrect : attendu " + this.getAttributLength() + ", recu " + nuplet.length);
        }

        int tailleAtr = this.getAttributLength();
        Object[] toAdd = new Object[tailleAtr];
        for (int i = 0; i < tailleAtr; i++) {
            /* exemple x1 => domaine : varchar */
            Object nup = nuplet[i];
            if (nup == null) {
                toAdd[i] = null;
                continue;
            }
            String AtrName = this.attribut[i].getDomaine().getName(); // "pour le nom : VARCHAR"

            String nupletType = nuplet[i].getClass().getSimpleName(); // "pour le nom : String"

            if (AtrName.equalsIgnoreCase("union")) {
                Domaine compose = this.attribut[i].getDomaine();
                boolean trouve = false;
                for (Domaine d : compose.getSousDomaines()) {
                    if (accepte(d, nup)) {
                        trouve = true;
                        break;
                    }
                }
                if (!trouve) {
                    throw new IllegalArgumentException("Colonne " + this.attribut[i].getSecondName() + " : " + nup + " (" + nupletType + ") n'est dans aucun des domaines " + compose);
                }
                toAdd[i] = nup;
                continue;
            }

            if (AtrName.equalsIgnoreCase("enum")) {
                /* on va regarder un par un les choix d'enum et voir si le nup y correspond */
                // verification
                // System.out.println("je suis passer ici : enum");
                //
                Object[] choices = attribut[i].getDomaine().getType();
                boolean trouve = false;
                for (int j = 0; j < choices.length; j++) {
                    if (nup.equals(choices[j])) {
                        /* INTEGRATION NORMALE DANS LE TABLEAU DOUBLE */
                        // verification
                        // System.out.println("verification reussi : controle enum");
                        //

                        toAdd[i] = nup;
                        trouve = true;
                        break;
                    }
                }
                if (!trouve) {
                    throw new IllegalArgumentException("Colonne " + this.attribut[i].getSecondName() + " : valeur '"
                            + nup + "' hors enum " + Arrays.toString(choices));
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
                    } else {
                        throw new IllegalArgumentException("Colonne " + this.attribut[i].getSecondName() + " : '" + nup
                                + "' depasse " + tailleMax + " caracteres");
                    }
                } else {
                    throw new IllegalArgumentException("Colonne " + this.attribut[i].getSecondName()
                            + " : attendu String (varchar), recu " + nupletType);
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
                    if (AtrName.equalsIgnoreCase("date")) {
                        toAdd[i] = nup;
                    } else if (AtrName.equalsIgnoreCase("int")) {
                        int numbr = (int) nup;
                        int typeLength = this.attribut[i].getDomaine().getTypeLength();
                        Object[] tip = this.attribut[i].getDomaine().getType();

                        if (typeLength == 2) {
                            int min = (int) tip[0];
                            int max = (int) tip[1];
                            if (numbr >= min && numbr <= max) {
                                toAdd[i] = numbr;
                            } else {
                                throw new IllegalArgumentException("Colonne " + this.attribut[i].getSecondName() + " : "
                                        + numbr + " hors de l'intervalle [" + min + ", " + max + "]");
                            }
                        } else {
                            toAdd[i] = nup;
                        }
                    } else {
                        toAdd[i] = nup;
                    }

                } else {
                    throw new IllegalArgumentException("Colonne " + this.attribut[i].getSecondName() + " : attendu "
                            + AtrType + ", recu " + nupletType);
                }
            }

        }
        individu.add(toAdd);
    }

    public Object valeur(String texte, Domaine d) {
        Domaine[] membres = d.getSousDomaines();
        if (membres == null) {
            return valeur(texte, d.getName());
        }
        boolean texteAccepte = false;
        for (Domaine m : membres) {
            String nom = m.getName();
            if (nom.equalsIgnoreCase("varchar") || nom.equalsIgnoreCase("enum")) {
                texteAccepte = true;
            } else {
                try {
                    return valeur(texte, nom);
                } catch (IllegalArgumentException e) {
                    // pas de ce type, on essaie le suivant
                }
            }
        }
        if (texteAccepte) {
            return texte;
        }
        throw new IllegalArgumentException("Colonne inconnue ou valeur invalide : " + texte);
    }

    public void select() {
        if (this.individu == null) {
            System.out.println("tableau vide");
            return;
        }

        int nbrIndividu = this.individu.size();
        int nbrAttribut = this.attribut.length;
        String tete = "| ";
        for (int j = 0; j < nbrAttribut; j++) {
            tete = tete + this.attribut[j].getSecondName() + " | ";
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

    public Relation projection(String[] noms, String newName) {
        int[] index = new int[noms.length];
        Attribut[] attrs = new Attribut[noms.length];
        for (int j = 0; j < noms.length; j++) {
            index[j] = indexParNom(noms[j]);
            if (index[j] == -1) {
                throw new IllegalArgumentException("Colonne inconnue" + noms[j]);
            }
            attrs[j] = this.attribut[index[j]];
        }
        int nbrIndividu = this.individu.size();
        Relation toreturn = new Relation(newName, attrs);

        for (int i = 0; i < nbrIndividu; i++) {
            Object[] concerne = this.individu.get(i);
            Object[] ajout = new Object[attrs.length];
            int b = 0;
            for (int o = 0; o < attrs.length; o++) {
                ajout[b] = concerne[index[o]];
                b++;
            }
            toreturn.add(ajout);
        }
        toreturn.NoDoublon();
        return toreturn;
    }

    public Relation produit(Relation relation2, String newName) {
        Attribut[] Atr1 = this.getAttribut();
        Attribut[] Atr2 = relation2.getAttribut();

        List<Object[]> Ind1 = this.getIndividu();
        List<Object[]> Ind2 = relation2.getIndividu();

        int newAtrLength = relation2.getAttributLength() + this.getAttributLength();
        Attribut[] newAtr = new Attribut[newAtrLength];
        for (int i = 0; i < this.getAttributLength(); i++) {
            newAtr[i] = Atr1[i];
        }
        for (int i = 0; i < relation2.getAttributLength(); i++) {
            newAtr[Atr1.length + i] = Atr2[i];
        }

        for (int i = 0; i < newAtr.length; i++) {
            for (int j = i + 1; j < newAtr.length; j++) {
                if (newAtr[i].getSecondName().equals(newAtr[j].getSecondName())) {
                    throw new IllegalArgumentException("Colonne ambigue : " + newAtr[i].getSecondName()
                            + " (utilise as(...) sur l'une des relations)");
                }
            }
        }

        Relation toreturn = new Relation(newName, newAtr);

        for (int i = 0; i < Ind1.size(); i++) {
            Object[] I = (Object[]) Ind1.get(i);

            for (int j = 0; j < Ind2.size(); j++) {
                List<Object> individuel = new ArrayList<>();

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

    public int indexParNom(String nom) {
        for (int i = 0; i < getAttributLength(); i++) {
            if (getAttribut()[i].getSecondName().equals(nom)) {
                return i;
            }
        }
        return -1;
    }

    public Object valeur(String texte, String domaine) {
        if (domaine.equalsIgnoreCase("varchar") || domaine.equalsIgnoreCase("enum")) {
            return texte;
        }
        if (domaine.equalsIgnoreCase("date")) {
            try {
                return Date.parse(texte);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Colonne inconnue ou date invalide (format aaaa/mm/jj) : " + texte);
            }
        }
        try {
            return Double.parseDouble(texte);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Colonne inconnue ou valeur invalide : " + texte);
        }
    }

    public int comparer(Object a, Object b) {
        if (a instanceof Number && b instanceof Number) {
            double da = ((Number) a).doubleValue();
            double db = ((Number) b).doubleValue();
            if (da < db) {
                return -1;
            } else if (da > db) {
                return 1;
            } else {
                return 0;
            }
        } else if (a instanceof Date && b instanceof Date) {
            return Date.comparer((Date) a, (Date) b);
        } else {
            return a.toString().compareTo(b.toString());
        }
    }

    private boolean tester(String operateur, Object a, Object b) {
        int c = comparer(a, b);

        if (operateur.equals("=")) {
            return c == 0;
        } else if (operateur.equals("!=")) {
            return c != 0;
        } else if (operateur.equals("<")) {
            return c < 0;
        } else if (operateur.equals(">")) {
            return c > 0;
        } else if (operateur.equals("<=")) {
            return c <= 0;
        } else if (operateur.equals(">=")) {
            return c >= 0;
        } else {
            throw new IllegalArgumentException("Operateur inconnu : " + operateur);
        }
    }

    private Object operande(String nom, Object[] ligne) {
        int k = indexParNom(nom);
        if (k != -1) {
            return ligne[k];
        }
        try {
            return Double.parseDouble(nom);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Colonne inconnue ou nombre invalide : " + nom);
        }
    }

    private Object calculer(Object a, String op, Object b) {
        if (a == null || b == null) {
            return null;
        }
        if (!(a instanceof Number) || !(b instanceof Number)) {
            throw new IllegalArgumentException("Calcul sur une valeur non numérique");
        }
        double x = ((Number) a).doubleValue();
        double y = ((Number) b).doubleValue();
        if (op.equals("+")) {
            return x + y;
        } else if (op.equals("-")) {
            return x - y;
        } else if (op.equals("*")) {
            return x * y;
        } else {
            return x / y;
        }

    }

    public Relation selection(String condition, String newName) {
        condition = condition.trim().replaceAll("\\s+", " ");
        String[] morceaux = condition.split(" ", 3);
        if (morceaux.length < 3) {
            throw new IllegalArgumentException("Condition invalide (format : colonne operateur valeur) : " + condition);
        }
        String nomGauche = morceaux[0];
        String operateur = morceaux[1];
        String droite = morceaux[2];
        String[] expr = morceaux[2].split(" ");
        boolean calcul = expr.length == 3 && expr[1].matches("[+\\-*/]");

        int x = indexParNom(nomGauche);
        int y = calcul ? -1 : indexParNom(morceaux[2]);

        if (x == -1) {
            throw new IllegalArgumentException("Colonne inconnue : " + nomGauche);
        }

        Object constante = null;
        if (y == -1 && !calcul) {
            constante = valeur(droite, this.attribut[x].getDomaine());
        }

        Relation resultat = new Relation(newName, getAttribut());

        for (int i = 0; i < this.individu.size(); i++) {
            Object[] ligne = this.individu.get(i);

            Object valeurGauche = ligne[x];
            Object valeurDroite;
            if (calcul) {
                valeurDroite = calculer(operande(expr[0], ligne), expr[1], operande(expr[2], ligne));
            } else if (y == -1) {
                valeurDroite = constante;
            } else {
                valeurDroite = ligne[y];
            }
            if (valeurGauche == null || valeurDroite == null) {
                continue;
            }

            if (tester(operateur, valeurGauche, valeurDroite)) {
                resultat.add(ligne);
            }
        }
        return resultat;
    }

    public Relation jointure(Relation autre, String condition, String newName) {
        Relation produit = this.produit(autre, newName);
        return produit.selection(condition, newName);
    }

    public Relation jointureGauche(Relation autre, String condition, String newName) {
        Relation resultat = this.jointure(autre, condition, newName);
        int n = this.getAttributLength();
        int m = autre.getAttributLength();

        for (Object o : this.individu) {
            Object[] gauche = (Object[]) o;
            boolean apparie = false;
            for (Object p : resultat.individu) {
                Object[] ligne = (Object[]) p;
                if (Arrays.equals(Arrays.copyOfRange(ligne, 0, n), gauche)) {
                    apparie = true;
                    break;
                }
            }
            if (!apparie) {
                Object[] ligne = new Object[n + m];
                System.arraycopy(gauche, 0, ligne, 0, n);
                resultat.individu.add(ligne);
            }
        }
        return resultat;
    }

    public void estCompatible(Relation autre){

        if (autre.getAttribut().length != this.getAttribut().length){
            throw new IllegalArgumentException("Relation incompatible : nombre de colonne ");
        }

        for (int i=0; i<this.getAttribut().length ;i++){
            Attribut a1 = this.getAttribut()[i];
            Attribut a2 = autre.getAttribut()[i];
            /*if (!a1.getName().equalsIgnoreCase(a2.getName())){
                throw new IllegalArgumentException("Relation incompatible  : nom colonne different " );
            }*/
            if (!a1.getDomaine().getName().equalsIgnoreCase(a2.getDomaine().getName())){
                throw new IllegalArgumentException("Relation incompatible  : domaine different ");
            }
        }
    }

    public boolean contient(Object[] individu){
        for (Object[] one : this.getIndividu()){
            if (Arrays.equals(one , individu)){
                return true;
            }
        }
        return false;
    }

    public Relation intersection(Relation autre, String newName){

        this.estCompatible(autre);
        
        Relation toReturn = new Relation(newName, this.getAttribut());

        for(Object[] individu : autre.getIndividu()){
            if (this.contient(individu)){
                toReturn.add(individu);
            }
        }
        toReturn.NoDoublon();
        return toReturn;
    }

    public Relation difference(Relation autre, String newName){

        this.estCompatible(autre);
        
        Relation toReturn = new Relation(newName, this.getAttribut());

        for(Object[] individu : this.getIndividu()){
            if (!autre.contient(individu)){
                toReturn.add(individu);
            }
        }
        toReturn.NoDoublon();
        return toReturn;
    }

    public Relation union(Relation autre, String newName){

        int n = this.getAttributLength();
        if (autre.getAttributLength() != n){
            throw new IllegalArgumentException("Union impossible : nombre de colonnes different (" + n + " et " + autre.getAttributLength() + ")");
        }

        Attribut[] colonnes = new Attribut[n];
        for (int i = 0; i < n; i++){
            Attribut a1 = this.getAttribut()[i];
            Attribut a2 = autre.getAttribut()[i];
            colonnes[i] = a1.avecDomaine(Domaine.fusion(a1.getDomaine(), a2.getDomaine()));
        }

        Relation toReturn = new Relation(newName, colonnes);

        for (Object[] individu : this.getIndividu()){
            toReturn.add(individu);
        }
        for (Object[] individu : autre.getIndividu()){
            toReturn.add(individu);
        }
        toReturn.NoDoublon();
        return toReturn;
    }

}