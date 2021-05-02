package IPI.NoSQLProjet;

import IPI.NoSQLProjet.entity.*;
import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.conventions.DocumentConventions;
import net.ravendb.client.documents.operations.attachments.AttachmentDetails;
import net.ravendb.client.documents.operations.attachments.CloseableAttachmentResult;
import net.ravendb.client.documents.queries.Query;
import net.ravendb.client.documents.session.IDocumentQuery;
import net.ravendb.client.documents.session.IDocumentSession;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

public class runner {

    static final String URL = "http://localhost:8080";
    static final String DATABASE = "TpBDD";

    // les stores correspondant a une "table" de document le store correspond a la collection album et la session execute le store
    static IDocumentStore store;
    static IDocumentSession session;
    static IDocumentStore storeArtist;
    static IDocumentSession sessionArtist;
    static IDocumentStore storeGenres;
    static IDocumentSession sessionGenres;
    static IDocumentStore storePays;
    static IDocumentSession sessionPays;
    
    static Games ObjetGame;
    static Albums ObjetAlbum;
    static Artist ObjectArtist;

    public static void main(String[] args) throws IOException {
        /// C'est ici que le code s'execute et appele les fonctions correspondant aux exos
        CreateStoreAlbum();
        CreateStoreArtist();
        exo1();
        exo2();
        exo3Price();
        exo3Name();

    }
    public static  void exo1() throws IOException {
        System.out.println("/////////// EXO 1 : RECHERCHE ALBUM");
        ////// Initialisation et ouverture de la session de la collection Album
        store.initialize();
        session = store.openSession();
        //////  Requete RQL : SELECT * FROM Album ORDER BY année
        IDocumentQuery<Albums> AlbumsQuery = session.query(Albums.class, Query.collection("Albums")).orderBy("alb_annee");
        //////  Recupere tous les resultats de la requete Album dans une liste
        List<Albums> listSearch = AlbumsQuery.toList();
        ////// Initialisation et ouverture de la session de la collection Artiste
        storeArtist.initialize();
        sessionArtist = storeArtist.openSession();
        //////  Requete RQL : SELECT * FROM artiste
        IDocumentQuery<Artist> ArtistQuery = sessionArtist.query(Artist.class, Query.collection("Artists"));
        //////  Recupere tous les resultats de la requete Artiste dans une liste
        List<Artist> listArtist = ArtistQuery.toList();
        //////  Boucle sur les resultats de la requete d'album
        for (Albums content : listSearch) {
            //////  Recupere les donnée du document sur lequel on boucle
            Integer IdArtist = content.alb_art;
            String AlbumName = content.alb_nom;
            String AlbumDate = content.alb_annee.toString();

            String ArtistName = null;
            //////  Boucle sur les resultats de la requete d'artiste
            for (Artist content2 : listArtist) {
                ////// Si l'id de alb_art correspond a art_id alors récupere le nom de l'artiste
                if(content2.art_id == IdArtist){
                    ArtistName = content2.art_nom;
                }
            }
            System.out.print("Name : " + ArtistName +" || ");
            System.out.print("Album : " + AlbumName+" || ");
            System.out.print("Année : " + AlbumDate);
            System.out.println();
        }
    }

    public static  void exo2() throws IOException {
        System.out.println("/////////// EXO 2 : RECHERCHE ARTISTE");
        ////// Initialisation et ouverture de la session de la collection Artiste
        storeArtist.initialize();
        sessionArtist = storeArtist.openSession();
        //////  Requete RQL : SELECT * FROM Artist ORDER BY nom
        IDocumentQuery<Artist> ArtistQuery = sessionArtist.query(Artist.class, Query.collection("Artists")).orderBy("art_nom");
        //////  Recupere tous les resultats de la requete Artiste dans une liste
        List<Artist> listArtist = ArtistQuery.toList();
        ////// Initialisation et ouverture de la session de la collection Genre
        CreateStoreGenre();
        storeGenres.initialize();
        //////  Requete RQL : SELECT * FROM Genres
        sessionGenres = storeGenres.openSession();IDocumentQuery<Genres> GenresQuery = sessionGenres.query(Genres.class, Query.collection("Genres"));
        //////  Recupere tous les resultats de la requete Genres dans une liste
        List<Genres> listGenre = GenresQuery.toList();
        ////// Initialisation et ouverture de la session de la collection Pays
        CreateStorePays();
        storePays.initialize();
        //////  Requete RQL : SELECT * FROM Pays
        sessionPays = storePays.openSession();IDocumentQuery<Pays> PaysQuery = sessionPays.query(Pays.class, Query.collection("Pays"));
        //////  Recupere tous les resultats de la requete Pays dans une liste
        List<Pays> listPays = PaysQuery.toList();
        //////  Boucle sur les resultats de la requete d'artiste
        for (Artist Acontent : listArtist) {
            //////  Recupere les donnée du document sur lequel on boucle
            String GenreArtist = null;
            String PaysArtist = null;
            String ArtistName = Acontent.art_nom;
            //////  Boucle sur les resultats de la requete Genres
                for (Genres Gcontent : listGenre) {
                    ////// Si le genre de gen_genre correspond a art_genre alors récupere le libelle du genre
                    if(Gcontent.gen_genre.equals(Acontent.art_genre)){
                        GenreArtist = Gcontent.gen_libelle;
                    }
                }
            //////  Boucle sur les resultats de la requete Pays
            for (Pays Pcontent : listPays) {
                ////// Si le pays de pay_pays correspond a art_pays alors récupere le libelle du pays
                if(Pcontent.pay_pays.equals(Acontent.art_pays)){
                    PaysArtist = Pcontent.pay_libelle;
                }
            }
            System.out.print("Name : " + ArtistName+" || ");
            System.out.print("Genre : " + GenreArtist+" || ");
            System.out.print("Pays : " + PaysArtist);
            System.out.println();
        }
    }

    public static  void exo3Price() throws IOException {
        System.out.println("/////////// EXO 3 : RECHERCHE ALBUM BY PRICE");
        CreateStoreAlbum();
        store.initialize();
        session = store.openSession();
        IDocumentQuery<Albums> AlbumsQuery = session.query(Albums.class, Query.collection("Albums")).orderBy("alb_prix");
        List<Albums> listSearch = AlbumsQuery.toList();

        CreateStoreArtist();
        storeArtist.initialize();
        sessionArtist = storeArtist.openSession();
        IDocumentQuery<Artist> ArtistQuery = sessionArtist.query(Artist.class, Query.collection("Artists"));
        List<Artist> listArtist = ArtistQuery.toList();
        ////// Création de fenetre car une fenetre ne peut pas avoir + 11 résultats
        JFrame frame = CreateJFrame("triByPrice");
        JFrame frame2 = CreateJFrame("triByPrice");
        JFrame frame3 = CreateJFrame("triByPrice");
        JFrame frame4 = CreateJFrame("triByPrice");
        Integer i = 0;
        for (Albums content : listSearch) {
            String AlbumId = session.advanced().getDocumentId(content);
            Integer IdArtist = content.alb_art;
            String AlbumName = content.alb_nom;
            String AlbumDate = content.alb_annee.toString();
            String AlbumPrice = content.alb_prix.toString();

            String ArtistName = null;
            for (Artist content2 : listArtist) {
                if(content2.art_id == IdArtist){
                    ArtistName = content2.art_nom;
                }
            }
            System.out.print("Name : " + ArtistName +" || ");
            System.out.print("Album : " + AlbumName+" || ");
            System.out.print("Prix : " + AlbumPrice+" || ");
            System.out.print("Année : " + AlbumDate);
            System.out.println();
            Image img = null;
            InputStream inputStream = null;
            ////// Récupere le fichier enregistrer dans le document
            try (CloseableAttachmentResult file2 = session.advanced().attachments().get(AlbumId, "ImageAlbum" + content.alb_art + ".jpg")) {

                inputStream = file2.getData();
                img = ImageIO.read(inputStream);

            }
            ////// Ecrit les résultats dans le JFrame java
            if(i < 11){
                MakeJFrame(frame,img,ArtistName,AlbumName,AlbumDate,AlbumPrice);
            }
            if(i < 22 && i > 11){
                MakeJFrame(frame2,img,ArtistName,AlbumName,AlbumDate,AlbumPrice);
            }
            if(i < 33 && i > 22){
                MakeJFrame(frame3,img,ArtistName,AlbumName,AlbumDate,AlbumPrice);
            }
            if((i < 44 && i > 33)){
                MakeJFrame(frame4,img,ArtistName,AlbumName,AlbumDate,AlbumPrice);
            }

            i++;
        }
    }

    public static  void exo3Name() throws IOException {
        System.out.println("/////////// EXO 3 : RECHERCHE ALBUM BY NAME");

        CreateStoreAlbum();
        store.initialize();
        session = store.openSession();
        IDocumentQuery<Albums> AlbumsQuery = session.query(Albums.class, Query.collection("Albums")).orderBy("alb_nom");
        List<Albums> listSearch = AlbumsQuery.toList();

        CreateStoreArtist();
        storeArtist.initialize();
        sessionArtist = storeArtist.openSession();
        IDocumentQuery<Artist> ArtistQuery = sessionArtist.query(Artist.class, Query.collection("Artists"));
        List<Artist> listArtist = ArtistQuery.toList();

        JFrame frame = CreateJFrame("triByName");
        JFrame frame2 = CreateJFrame("triByName");
        JFrame frame3 = CreateJFrame("triByName");
        JFrame frame4 = CreateJFrame("triByName");

        Integer i = 0;
        for (Albums content : listSearch) {
            String AlbumId = session.advanced().getDocumentId(content);
            Integer IdArtist = content.alb_art;
            String AlbumName = content.alb_nom;
            String AlbumDate = content.alb_annee.toString();
            String AlbumPrice = content.alb_prix.toString();

            String ArtistName = null;
            for (Artist content2 : listArtist) {
                if(content2.art_id == IdArtist){
                    ArtistName = content2.art_nom;
                }
            }
            System.out.print("Name : " + ArtistName +" || ");
            System.out.print("Album : " + AlbumName+" || ");
            System.out.print("Prix : " + AlbumPrice+" || ");
            System.out.print("Année : " + AlbumDate);
            System.out.println();
            Image img = null;
            InputStream inputStream = null;
            try (CloseableAttachmentResult file2 = session.advanced().attachments().get(AlbumId, "ImageAlbum" + content.alb_art + ".jpg")) {

                inputStream = file2.getData();
                img = ImageIO.read(inputStream);

            }
            if(i < 11){
                MakeJFrame(frame,img,ArtistName,AlbumName,AlbumDate,AlbumPrice);
            }
            if(i < 22 && i > 11){
                MakeJFrame(frame2,img,ArtistName,AlbumName,AlbumDate,AlbumPrice);
            }
            if(i < 33 && i > 22){
                MakeJFrame(frame3,img,ArtistName,AlbumName,AlbumDate,AlbumPrice);
            }
            if((i < 44 && i > 33)){
                MakeJFrame(frame4,img,ArtistName,AlbumName,AlbumDate,AlbumPrice);
            }

            i++;
        }
    }

    public static String randomName() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    public static IDocumentStore CreateStore() {
        store = new DocumentStore(new String[]{URL}, DATABASE);
        DocumentConventions conventions = store.getConventions();
        store.initialize();
        return store;
    }

    public static void CreateStoreAlbum() {
        store = new DocumentStore(new String[]{URL}, DATABASE);
        DocumentConventions conventions = store.getConventions();
        conventions.setFindCollectionName(clazz -> {
            if (Albums.class.isAssignableFrom(clazz))
                return "Album";
            return DocumentConventions.defaultGetCollectionName(clazz);
        });
    }

    public static void CreateStoreArtist() {
        storeArtist = new DocumentStore(new String[]{URL}, DATABASE);
        DocumentConventions conventions2 = storeArtist.getConventions();
        conventions2.setFindCollectionName(clazz -> {
            if (Artist.class.isAssignableFrom(clazz))
                return "Artist";
            return DocumentConventions.defaultGetCollectionName(clazz);
        });
    }

    public static void CreateStoreGenre() {
        storeGenres = new DocumentStore(new String[]{URL}, DATABASE);
        DocumentConventions conventions2 = storeGenres.getConventions();
        conventions2.setFindCollectionName(clazz -> {
            if (Artist.class.isAssignableFrom(clazz))
                return "Genres";
            return DocumentConventions.defaultGetCollectionName(clazz);
        });
    }

    public static void CreateStorePays() {
        storePays = new DocumentStore(new String[]{URL}, DATABASE);
        DocumentConventions conventions2 = storePays.getConventions();
        conventions2.setFindCollectionName(clazz -> {
            if (Artist.class.isAssignableFrom(clazz))
                return "Pays";
            return DocumentConventions.defaultGetCollectionName(clazz);
        });
    }

    public static void LoadPicture() throws IOException {
        System.out.println("///////////RECHERCHE NOM//////////");
        try (CloseableAttachmentResult file1 = session
                .advanced().attachments().get(ObjetAlbum, "8d0a9a252b95857096b0b8590fdc8429.jpg");
             CloseableAttachmentResult file2 = session
                     .advanced().attachments().get("Albums/0000000000000000128-A", "test.jpg")) {

            InputStream inputStream = file1.getData();

            AttachmentDetails attachmentDetails = file1.getDetails();
            String name = attachmentDetails.getName();
            String contentType = attachmentDetails.getContentType();
            String hash = attachmentDetails.getHash();
            Image img = ImageIO.read(inputStream);
            ImageIcon icon = new ImageIcon(img);
            JFrame frame = new JFrame();
            frame.setLayout(new FlowLayout());
            frame.setSize(500, 500);
            JLabel lbl = new JLabel();
            JLabel txt = new JLabel();
            txt.setText(contentType);
            lbl.setIcon(icon);
            frame.add(txt);
            frame.add(lbl);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            //DisplayImage(inputStream);

/*
            Stream img = attachmentDetails.Stream;
*/
            long size = attachmentDetails.getSize();
            String documentId = attachmentDetails.getDocumentId();
            String changeVector = attachmentDetails.getChangeVector();
            System.out.println(file1);
            System.out.println(file2);
            System.out.println(inputStream);
            System.out.println(hash);
            System.out.println(contentType);
            System.out.println(documentId);
            System.out.println(changeVector);
            System.out.println(img);

        }
    }

    public static void DisplayImage(InputStream data) throws IOException {
        BufferedImage img = ImageIO.read(data);
        ImageIcon icon = new ImageIcon(img);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(200, 300);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static JFrame CreateJFrame(String Name){
        JFrame frame = new JFrame(Name);
        frame.setLayout(new FlowLayout());
        frame.setSize(500, 500);
        return frame;
    }

    public static void MakeJFrame(JFrame frameValue,Image img,String ArtistName, String AlbumName,String AlbumDate, String AlbumPrice){

        JLabel txt = new JLabel();
        JLabel txt2 = new JLabel();
        JLabel txt3 = new JLabel();
        JLabel txt4 = new JLabel();
        ImageIcon icon = new ImageIcon(img);
        JLabel lbl = new JLabel();

        lbl.setIcon(icon);
        frameValue.add(lbl);

        txt.setText("Name : " + ArtistName);
        txt2.setText("Album : " + AlbumName);
        txt3.setText("Année : " + AlbumDate);
        txt4.setText("price : " + AlbumPrice +"$");

        frameValue.add(txt);
        frameValue.add(txt2);
        frameValue.add(txt3);
        frameValue.add(txt4);

        frameValue.setVisible(true);
        frameValue.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
//////////////////// METHODE DU PROJET DE GROUPE/////////////////////////////////
   /* //ObjetAlbum = session.load(Albums.class, "Albums/0000000000000000138-A");
    //System.out.println("la liste contient : " + ObjetGame.listeJeux.size() + "jeux");
        SearchByNom();
        SearchByediteur();
        SearchByAnnée();
        SearchBygenre ();
        SearchByPlatform ();
        SearchByVente ();
        CreateNewGame();
        change();
        Delete3();
        CreateNewGameFor100();
        change();
        Delete3();
    //LoadPicture();*/
    //SearchByName
    /*public static void SearchByNom (){
        System.out.println("///////////RECHERCHE NOM///////////");
        long startTimeOneSearch = System.currentTimeMillis();
        long endTimeOneSearch = 0;
        for (Jeux jeu: ObjetGame.listeJeux) {
                if (jeu.nom.equals("Assassin's Creed: Unity")){
                    System.out.println(jeu.nom);
                    System.out.println(jeu.editeur);
                    System.out.println(jeu.genre);
                    System.out.println(jeu.annee);
                    System.out.println(jeu.NbVente);
                    System.out.println(jeu.plateforme);
                    break;
                }
            }
        endTimeOneSearch = System.currentTimeMillis();
        long durationOneSearch = (endTimeOneSearch - startTimeOneSearch);

        System.out.println("La recherche par nom a mis : " + durationOneSearch + "  milliseconde(s) \n");
    }*/
    //SearchByediteur
   /*public static void SearchByediteur (){

        System.out.println("///////////RECHERCHE EDITEUR///////////");
        long startTimeOneSearch = System.currentTimeMillis();
        long endTimeOneSearch = 0;
        Integer count = 0;
        for (Jeux jeu: ObjetGame.listeJeux) {
            for  (String listeediteurs: jeu.editeur){
                if (listeediteurs.equals("Eidos")){
                    count = count + 1;
                }
            }
        }
        endTimeOneSearch = System.currentTimeMillis();
        long durationOneSearch = (endTimeOneSearch - startTimeOneSearch);
        System.out.println("Nombre de résultats : " + count);
        System.out.println("La recherche par editeur a mis : " + durationOneSearch + " milliseconde(s) \n");

    }*/
    //SearchByAnnée
  /*  public static void SearchByAnnée (){
        System.out.println("///////////RECHERCHE ANNEE///////////");
        long startTimeOneSearch = System.currentTimeMillis();
        long endTimeOneSearch = 0;
        Integer count = 0;
        for (Jeux jeu: ObjetGame.listeJeux) {
                if (jeu.annee == null){
                    jeu.annee = 00000L;
                }
                if (jeu.annee.equals(2007L)){
                    count = count + 1;
                }
            }
        endTimeOneSearch = System.currentTimeMillis();
        long durationOneSearch = (endTimeOneSearch - startTimeOneSearch);
        System.out.println("Nombre de résultats : " + count);
        System.out.println("La recherche par Année a mis : " + durationOneSearch + " milliseconde(s) \n");

    }*/
    //SearchBygenre
   /* public static void SearchBygenre (){
        System.out.println("///////////RECHERCHE GENRE///////////");
        long startTimeOneSearch = System.currentTimeMillis();
        long endTimeOneSearch = 0;
        Integer count = 0;
        for (Jeux jeu: ObjetGame.listeJeux) {
            for (String listegenre : jeu.genre) {
                if (listegenre.equals("Action")) {
                    count = count + 1;
                }
            }
        }
        endTimeOneSearch = System.currentTimeMillis();
        long durationOneSearch = (endTimeOneSearch - startTimeOneSearch);
        System.out.println("Nombre de résultats : " + count);
        System.out.println("La recherche par genre a mis : " + durationOneSearch + " milliseconde(s) \n");
    }*/
    //SearchByPlatform
   /* public static void SearchByPlatform (){
        System.out.println("///////////RECHERCHE PLATFORME///////////");
        long startTimeOneSearch = System.currentTimeMillis();
        long endTimeOneSearch = 0;
        Integer count = 0;
        for (Jeux jeu: ObjetGame.listeJeux) {
            for (String listeplateforme : jeu.plateforme) {
                if (listeplateforme.equals("X360")) {
                    count = count + 1;
                }
            }
        }
        endTimeOneSearch = System.currentTimeMillis();
        long durationOneSearch = (endTimeOneSearch - startTimeOneSearch);
        System.out.println("Nombre de résultats : " + count);
        System.out.println("La recherche par Plateforme a mis : " + durationOneSearch + " milliseconde(s) \n");

    }*/
    //SearchByVente
   /* public static void SearchByVente (){
        System.out.println("///////////RECHERCHE VENTES///////////");
        long startTimeOneSearch = System.currentTimeMillis();
        long endTimeOneSearch = 0;
        Integer count = 0;
        for (Jeux jeu: ObjetGame.listeJeux) {
            if (jeu.NbVente == null){
                jeu.NbVente = 0;
            }
            if (jeu.NbVente >= 1000000){
                count = count + 1;
            }
        }
        endTimeOneSearch = System.currentTimeMillis();
        long durationOneSearch = (endTimeOneSearch - startTimeOneSearch);
        System.out.println("Nombre de résultats : " + count);
        System.out.println("La recherche par Vente a mis : " + durationOneSearch + " milliseconde(s) \n");

    }*/
    //CreateNewGame
    /*public static String CreateNewGame(){
        System.out.println("///////////CREATION D UN JEU///////////");
        long startTimeOneSearch = System.nanoTime();
        long endTimeOneSearch = 0;

        Jeux NewJeu = new Jeux();
        NewJeu.nom = randomName();
        String Name = NewJeu.nom;
        NewJeu.plateforme = Arrays.asList("X360","PS2");
        NewJeu.NbVente = 999999;
        NewJeu.annee = 2021L;
        NewJeu.editeur = Arrays.asList("Eric","Fabrice","Alexy");
        NewJeu.genre = Arrays.asList("Action","Dev");

        ObjetGame.listeJeux.add(NewJeu);

        endTimeOneSearch = System.nanoTime();
        long durationOneSearch = (endTimeOneSearch - startTimeOneSearch);
        durationOneSearch = durationOneSearch/1000000;
        System.out.println("La Créations a mis : " + durationOneSearch + " milliseconde(s) \n");
        session.saveChanges();
        return Name;
    }*/
    //CreateNewGameFor100
    /*    public static void CreateNewGameFor100(){
        System.out.println("///////////CREATE X100///////////");
        long startTimeOneSearch = System.currentTimeMillis();
        long endTimeOneSearch = 0;
        for (int i = 0; i < 100; i++) {
            Jeux NewJeu = new Jeux();
            NewJeu.nom = randomName();
            NewJeu.plateforme = Arrays.asList("X360","PS2");
            NewJeu.NbVente = 999999;
            NewJeu.annee = 2021L;
            NewJeu.editeur = Arrays.asList("Eric","Fabrice","Alexy");
            NewJeu.genre = Arrays.asList("Action","Dev");

            ObjetGame.listeJeux.add(NewJeu);
         }
        session.saveChanges();
        endTimeOneSearch = System.currentTimeMillis();
        long durationOneSearch = (endTimeOneSearch - startTimeOneSearch);
        System.out.println("La Créations de 100 jeux a mis : " + durationOneSearch + " milliseconde(s) \n");
        System.out.println("la liste contient : " + ObjetGame.listeJeux.size() + " jeux");
    }*/
    //Le reste
    /*
    public static void Delete2 (){
        System.out.println("///////////DELETE///////////");
        long startTimeOneSearch = System.nanoTime();
        long endTimeOneSearch = 0;
        Iterator<Jeux> listeJeuTmp = ObjetGame.listeJeux.iterator();
        Integer count = 0;
        while (listeJeuTmp.hasNext()) {
            Jeux jeu =  listeJeuTmp.next();

            if (jeu.NbVente == 999999){
                count = count + 1;
                listeJeuTmp.remove();
            }

        }
        ObjetGame.listeJeux = Lists.newArrayList(listeJeuTmp);
        System.out.println(ObjetGame.listeJeux.size());
        session.saveChanges();
        endTimeOneSearch = System.nanoTime();
        long durationOneSearch = (endTimeOneSearch - startTimeOneSearch);
        durationOneSearch = durationOneSearch/1000000;
        System.out.println("La suppresion a mis : " + durationOneSearch + " milliseconde(s) \n");
        System.out.println("Count :" + count);
    }

    public static void Delete (){
        System.out.println("///////////DELETE///////////");
        long startTimeOneSearch = System.nanoTime();
        long endTimeOneSearch = 0;
        List<Jeux> listeJeuTmp = new ArrayList<>();
        Collections.copy(ObjetGame.listeJeux,listeJeuTmp);
        System.out.println(listeJeuTmp.size());  // NE COPIE PAS

        Integer count = 0;
        for (Jeux jeu : ObjetGame.listeJeux) {
            if (jeu.NbVente == 999999){
                count = count + 1;
                listeJeuTmp.remove(jeu);
            }

        }
        ObjetGame.listeJeux = listeJeuTmp;
        endTimeOneSearch = System.nanoTime();
        long durationOneSearch = (endTimeOneSearch - startTimeOneSearch);
        durationOneSearch = durationOneSearch/1000000;
        System.out.println("La suppresion a mis : " + durationOneSearch + " milliseconde(s) \n");
        System.out.println("Count :" + count);
    }

    public static void Delete3 (){
        System.out.println("///////////DELETE///////////");
        long startTimeOneSearch = System.currentTimeMillis();
        long endTimeOneSearch = 0;
        List<Jeux> listeJeuTmp = new ArrayList<>();
        Integer count = ObjetGame.listeJeux.size();
        for (Jeux jeu : ObjetGame.listeJeux) {
            if (jeu.NbVente != 999999){
                count = count - 1;
                listeJeuTmp.add(jeu);
            }

        }
        ObjetGame.listeJeux = listeJeuTmp;
        session.saveChanges();
        endTimeOneSearch = System.currentTimeMillis();
        long durationOneSearch = (endTimeOneSearch - startTimeOneSearch);
        System.out.println("nombre de jeux supprimé :" + count);
        System.out.println("La suppresion a mis : " + durationOneSearch + " milliseconde(s) \n");
        System.out.println("la liste contient : " + ObjetGame.listeJeux.size() + " jeux");
    }

    public static void change (){
        System.out.println("///////////CHANGE///////////");
        long startTimeOneSearch = System.currentTimeMillis();
        long endTimeOneSearch = 0;
        Integer count = 0;
        for (Jeux jeu: ObjetGame.listeJeux) {
            if (jeu.NbVente == 999999){
                count = count + 1;
                jeu.nom = randomName();
                jeu.editeur =  Arrays.asList("E","F","A");
                jeu.annee = 2022L;
            }
        }
        session.saveChanges();
        endTimeOneSearch = System.currentTimeMillis();
        long durationOneSearch = (endTimeOneSearch - startTimeOneSearch);
        System.out.println("nombre de jeux modifier :" + count);
        System.out.println("La modification a mis : " + durationOneSearch + " milliseconde(s) \n");
        System.out.println("la liste contient : " + ObjetGame.listeJeux.size() + " jeux");
    }*/

