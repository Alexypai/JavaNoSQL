package IPI.NoSQLProjet.entity;

import java.util.List;

public class Editeur
{
    public List<String> Nom ;

    public String Localisation ;

    public List<String> getNom() {
        return Nom;
    }

    public void setNom(List<String> nom) {
        Nom = nom;
    }

    public String getLocalisation() {
        return Localisation;
    }

    public void setLocalisation(String localisation) {
        Localisation = localisation;
    }
}