import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class main {
    public static void main(String[] args) {
        System.out.println("SGBDR de Misaina , enga anie mba andeha :");

        Attribut id = new Attribut("id", new Domaine("int", 0, 100));
        Attribut nom = new Attribut("nom", new Domaine("varchar", 50));
        Attribut prenom = new Attribut("prenom", new Domaine("varchar", 50));
        Attribut age = new Attribut("age", new Domaine("int", 0, 100));
        Attribut sexe = new Attribut("sexe", new Domaine("enum", new Object[]{"m", "f"}));

        Attribut id_emp = new Attribut("id_emp", new Domaine("int", 0, 100));
        Attribut montant = new Attribut("montant", new Domaine("double"));

        Relation emp = new Relation("emp", new Attribut[]{id, nom, prenom, age, sexe});
        emp.add(new Object[]{1, "JEAN", "jean", 18, "m"});
        emp.add(new Object[]{2, "RAMILY", "kalo", 25, "f"});
        emp.add(new Object[]{3, "KALI", "Linux", 50, "m"});
        System.out.println("=== emp ===");
        emp.select();

        Relation salaire = new Relation("salaire", new Attribut[]{id, id_emp, montant});
        salaire.add(new Object[]{1, 1, 2500.0});
        salaire.add(new Object[]{2, 2, 3000.0});
        salaire.add(new Object[]{3, 3, 4000.0});
        System.out.println("=== salaire ===");
        salaire.select();

        System.out.println("=== selection_superieur(age > 20) ===");
        emp.selection_superieur(age, 20).select();

        System.out.println("=== selection_egal(nom = JEAN) ===");
        emp.selection_egal(nom, "JEAN").select();

        System.out.println("=== selection_different(sexe != m) ===");
        emp.selection_different(sexe, "m").select();

        System.out.println("=== project(nom, age) ===");
        emp.project(new Attribut[]{nom, age}).select();

        System.out.println("=== project(sexe) : doublons retires ===");
        emp.project(new Attribut[]{sexe}).select();

        System.out.println("=== produit emp x salaire (9 lignes) ===");
        emp.produit(salaire).select();

        System.out.println("=== jointure emp.id = salaire.id_emp (3 lignes) ===");
        emp.jointure(id, "=", salaire, id_emp).select();

        System.out.println("=== jointure emp.id < salaire.id_emp (3 lignes : 1-2, 1-3, 2-3) ===");
        emp.jointure(id, "<", salaire, id_emp).select();

        System.out.println("=== jointure emp.age >= 25 avec salaire (2 x 3 = 6 lignes) ===");
        emp.jointure(age, ">=", salaire, 25).select();

        System.out.println("=== jointure emp.nom = JEAN avec salaire (1 x 3 = 3 lignes) ===");
        emp.jointure(nom, "=", salaire, "JEAN").select();

        System.out.println("=== auto-jointure gauche : emp1.id + 1 = emp2.id ===");
        Relation testAutoJointure = emp.jointureGauche(id, "=", emp, id, 1);
        testAutoJointure.select();
    }
}