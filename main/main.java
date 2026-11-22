package main;

import java.util.ArrayList;
import java.util.List;

import utils.*;

public class main {
    public static void main(String[] args) {
        System.out.println("SGBDR de Misaina , enga anie mba andeha :");

        Attribut id1 = new Attribut("id", new Domaine("int", 0, 100));
        Attribut nom = new Attribut("nom", new Domaine("varchar", 50));
        Attribut prenom = new Attribut("prenom", new Domaine("varchar", 50));
        Attribut age = new Attribut("age", new Domaine("int", 0, 100));
        Attribut sexe = new Attribut("sexe", new Domaine("enum", new Object[] { "m", "f" }));
        Attribut naissance = new Attribut("naissance", new Domaine("date"));

        Attribut id2 = new Attribut("id", new Domaine("int", 0, 100));
        Attribut id_emp = new Attribut("id_emp", new Domaine("int", 0, 100));
        Attribut montant = new Attribut("montant", new Domaine("double"));

        Relation emp = new Relation("emp",
                new Attribut[] { id1, nom, prenom, age, sexe, naissance });
        emp.add(new Object[] { 1, "JEAN", "jean", 18, "m", new Date(2002, 10, 01) });
        emp.add(new Object[] { 2, "RAMILY", "kalo", 25, "f", new Date(2002, 10, 01) });
        emp.add(new Object[] { 3, "KALI", "Linux", 50, "m", new Date(2002, 10, 01) });

        emp.select();

        Relation salaire = new Relation("salaire",
                new Attribut[] { id2, id_emp, montant });
        salaire.add(new Object[] { 1, 2, 2500.0 });
        salaire.add(new Object[] { 2, 1, 3000.0 });
        salaire.add(new Object[] { 3, 3, 4000.0 });
        salaire.select();

        emp.jointure(emp.as("e1"), "e1.id = emp.id + 1", null).select();

        emp.jointureGauche(emp.as("e2"), "e2.id = emp.id + 1", "R").select();

    }
}