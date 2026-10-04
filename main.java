import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.List.*;
import java.util.concurrent.ArrayBlockingQueue;

public class main {
    public static void main(String[] args) {
        System.out.println("SGBDR de Misaina , enga anie mba andeha :");
        Attribut nom = new Attribut("nom", new Domaine("varchar", 50));
        Attribut prenom = new Attribut("prenom", new Domaine("varchar", 50));
        Attribut date_naissance = new Attribut("date_naissance", new Domaine("date"));
        Attribut sexe = new Attribut("sexe", new Domaine("enum", new Object[] { "m", "f" }));

        Relation emp = new Relation("emp", new Attribut[] { nom, prenom, date_naissance, sexe });

        emp.add(new Object[] { "RAZAFIMANDIMBY", "Ny Fitiavana", new Date(2007, 06, 30), "m" });
        emp.add(new Object[] { "RAKOTONIRINA", "Vahatra Ny Aina", new Date(2008, 11, 3), "m" });
        emp.add(new Object[] { "RAKOTO", "Son", new Date(1989, 12, 25), 1 });
        emp.select();

        Date test = new Date(2026, 9, 12);
        System.out.println(test);

        Attribut choix = new Attribut("choix", new Domaine("enum", new Object[] { "Rakoto", "Rasoa", 0 }));
        Attribut id = new Attribut("id", new Domaine("int"));

        Relation testChoix = new Relation("testChoix", new Attribut[] { id, choix });
        testChoix.add(new Object[] { 0, "Rakoto" });
        testChoix.add(new Object[] { 1, "Rasoa" });
        testChoix.add(new Object[] { 2, 0 });
        testChoix.add(new Object[] { "a", 0 });
        testChoix.add(new Object[] { 3, 12 });
        testChoix.add(new Object[] { "tsy mety", 13 });

        testChoix.select();

        Attribut h1 = new Attribut("h1", new Domaine("int"));
        Attribut h2 = new Attribut("h2", new Domaine("int"));
        Attribut h3 = new Attribut("h3", new Domaine("int"));
        Relation a = new Relation("a", new Attribut[] { h1, h2,h3 });

        a.add(new Object[] { 1, 2, 5 });
        a.add(new Object[] { 3, 4, 3 });
        a.add(new Object[] { 1, 2, 2 });
        a.add(new Object[] { 5, 4, 2 });
        a.add(new Object[] { 1, 2, 4 });
        a.add(new Object[] { 3, 4, 1 });
        a.select();


        Relation a1 = a.project(new Attribut[]{h1, h2});
        a1.select();

    }
}