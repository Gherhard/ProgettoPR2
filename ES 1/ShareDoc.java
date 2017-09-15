import java.util.*;
public interface ShareDoc {

public boolean addUser(String name, int password)throws NullPointerException;
// REQUIRES: name != null
// MODIFIES: this
// EFFECTS: Aggiunge l’utente con la relativa password alla repository.
// Restituisce true se l'inserimento ha successo,
// false se fallisce perche' esiste gia' un utente con il medesimo nome.

public void removeUser(String name)throws NullPointerException;
// REQUIRES: name != null
// MODIFIES: this
// EFFECTS: Elimina l’utente e tutti i suoi documenti digitali
// Se il nome utente e' null solleva NullPointerException, unchecked.


public boolean addDoc(String user, String doc, int password) throws NullPointerException, WrongIdException;
// REQUIRES: user != null && doc!= null && user presente in this && password giusta per user
// MODIFIES: this
// EFFECTS: Aggiunge al sistema il documento digitale identificato dal nome.
// Restituisce true se l'inserimento ha successo,
// false se fallisce perche' esiste gia' un documento con quel nome.
// Se user o doc sono null solleva NullPointerException, unchecked.
// se l'utente non è il nome di un utente registrato o la password è sbagliata solleva WrongIdException checked.


public boolean removeDoc(String user, String doc, int password) throws NullPointerException, WrongIdException;
// REQUIRES: user != null && doc!= null && user presente in this && password giusta per user
// MODIFIES: this
// EFFECTS: Rimuove dal sistema il documento digitale identificato dal nome.
// Restituisce true se l’operazione ha successo,
// false se fallisce perche' non esiste un documento con quel nome.
// Se user o doc sono null solleva NullPointerException, unchecked.
// se l'utente non è il nome di un utente registrato o la password è sbagliata solleva WrongIdException checked.

public void readDoc(String user, String doc, int password) throws WrongIdException;
// REQUIRES: user != null && doc!= null && user presente in this && password giusta per user && doc deve essere un documento di user
// EFFECTS: Legge il documento digitale identificato dal nome.
// Se user o doc sono null solleva NullPointerException, unchecked.
// se l'utente non è il nome di un utente registrato, la password è sbagliata oppure non esiste un documento
// di user con il nome doc solleva WrongIdException checked.

public void shareDoc(String fromName, String toName, String doc, int password) throws WrongIdException;
// REQUIRES: user != null && doc!= null && user presente in this && password giusta per user && doc deve essere un documento di user
// MODIFIES: this
// EFFECTS: Notifica una condivisione di documento
// Se user o doc sono null solleva NullPointerException, unchecked.
// se l'utente non è il nome di un utente registrato, la password è sbagliata oppure non esiste un documento
// di user con il nome doc solleva WrongIdException checked.
// Se toName non e' un utente registrato solleva WrongIdException

public String getNext(String user, int password) throws EmptyQueueException,WrongIdException;
// REQUIRES: user != null && password giusta per user
// EFFECTS: Restituisce il nome del documento condiviso cancellandolo dalla coda 
// delle notifiche di condivisione.
// Se user o doc sono null solleva NullPointerException, unchecked.
// se l'utente non è il nome di un utente registrato o la password è sbagliata solleva WrongIdException checked.
// Se la coda di notifiche di user e' vuota solleva EmptyQueueException

}
