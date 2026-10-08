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
        Attribut age = new Attribut("age",new Domaine("int", 0, 100) );
        Attribut sexe = new Attribut("sexe", new Domaine("enum", new Object[]{"m", "f"}));
        
        Relation emp = new Relation("emp", new Attribut[]{nom, prenom, age, sexe});
        emp.add(new Object[]{"RANDRO SELISON", "Mirado", 18, "m"});
        emp.add(new Object[]{"RAZAFIMANDIMBY", "Walker", 100, "m"});
        emp.add(new Object[]{"ROBSON RADO", "Henintsoa Misaina", 18, "m"});
        emp.select();


        Attribut numero = new Attribut("numero", new Domaine("int"));
        Attribut signature = new Attribut("signature", new Domaine("varchar", 20));

        Relation rien = new Relation("rien", new Attribut[]{numero, signature});
        rien.add(new Object[]{1, "ssh"});

        Relation test = emp.produit(rien);
        test.select();

    }
}