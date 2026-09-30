import java.util.Date;
import java.util.List.*;

public class main {
    public static void main(String[] args) {
        System.out.println("SGBDR de Misaina , enga anie mba andeha :");
        Attribut nom = new Attribut("nom", new Domaine("varchar", 50));
        Attribut prenom = new Attribut("prenom",new Domaine("varchar", 50) );
        Attribut date_naissance = new Attribut("date_naissance", new Domaine("date"));
        Attribut sexe = new Attribut("sexe", new Domaine("enum", new Object[]{"m","f"}));

        Relation emp = new Relation("emp", new Attribut[]{nom, prenom, date_naissance, sexe});
        
        emp.add(new Object[]{"RAZAFIMANDIMBY", "Ny Fitiavana", new Date(2007, 06, 30), "m"});
        emp.add(new Object[]{"RAKOTONIRINA", "Vahatra Ny Aina", new Date(2008, 11, 3), "m"});
        emp.add(new Object[]{"RAKOTO", "Son", new Date(1989, 12, 25), 1});
        emp.select();

        Date test = new Date(2026, 9,12);
        System.out.println(test);
    }
}