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

        emp.select();

        Relation salaire = new Relation("salaire", new Attribut[]{id, id_emp, montant});
        salaire.add(new Object[]{1, 1, 2500.0});
        salaire.add(new Object[]{2, 2, 3000.0});
        salaire.add(new Object[]{3, 3, 4000.0});
        salaire.select();

        emp.selection("age >= 20").select();

        Relation testAutoJointure = emp.jointure(salaire, "id = id_emp");
        testAutoJointure.select();

    }
}