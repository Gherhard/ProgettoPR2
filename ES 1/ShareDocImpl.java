import java.util.*;
/* Funzione di astrazione: 
Af (c) = c.users[0],.....,c.users[users.size()-1]

Invariante di rappresentazione

I(c) = c.users!=null && for all i. 0 <= i < users.size() --> users[i].GetPassword >=0 &&
			for all i,j. 0 <= i,j < users.size() --> !(i=j) -> users[i].GetIdUtente() != users[j].GetIdUtente() &&
			for all i. 0<=i < user.size() && for all j,k 0 <= j , k < docs.size() --> 
				users.get(i).docs.get(j).name != users.get(i).docs.get(k).name
	
	
*/
class ShareDocImpl implements ShareDoc {
	private List<User> users; //lista dei user
	private int tutente;
        private int tdoc;
        
	public ShareDocImpl(){
            users = new ArrayList<User>();
            
        }
	public boolean addUser(String name, int password)
	{
		//MODIFIES: this
		/*EFFECTS: Aggiunge l’utente con la relativa password alla repository.
		Restituisce true se l'inserimento ha successo,false se fallisce perche' esiste gia' un utente con il medesimo nome.*/
		if(name==null) throw new NullPointerException();
		
		for(int i=0 ; i<users.size();i++) // cerca l'utente
		{
			if(users.get(i).getIdUtente().equals(name))
				return false;
		}
		User e = new User(name,password);
		users.add(e);
		return true;		
	}
	
	public void removeUser(String name)
	{
		//EFFECTS: Elimina l’utente e tutti i suoi documenti digitali
		
		if(name==null) throw new NullPointerException();
		// devo cercare il user prima e dopo devo cancellare tutti i suoi documenti con la remove
		for(int i=0 ; i<users.size();i++)
		{
			if(users.get(i).getIdUtente().equals(name)) // se trpva l'utente scorre tutti i suoi documenti e li cancella
                        {
                            for(int j=0;j<users.get(i).docs.size();j++)
				{
					users.get(i).docs.remove(j);
				}
                            users.remove(i); // rimuovo l'utente
                        }
		}
	}
	public boolean addDoc(String user, String doc, int password) throws NullPointerException, WrongIdException
	{
		//EFFECTS:Aggiunge al sistema il documento digitale identificato dal nome.Restituisce true se l'inserimento ha successo,
		// false se fallisce perche' esiste gia' un documento con quel nome.
		// WrongIdException se l'utente non ce
                int trovatoU=0;
		if(user==null) throw new NullPointerException();
		if(doc==null) throw new NullPointerException();
                DigitalDoc e = new DigitalDoc();
                e.name=doc;
                // qui non ho usato readDoc perche il metodo readDoc mi restituisce wrongIdException perche non riesce a trovare il documento
                for(int i=0 ; i<users.size();i++)
		{
			if( (users.get(i).getIdUtente().equals(user) ) && (users.get(i).GetPassword()==password) )
			{
                                //aggiungo il documento e  uso trovatoU per il caso di verifica di errore di autenticazione
				trovatoU=1;
                                users.get(i).docs.add(e);
                                return true;
			}
		}
                if(trovatoU==0)
                {
                    throw new WrongIdException("Nome utente o password sbagliati!");
                }
		return false;
	}
	
	public boolean removeDoc(String user, String doc, int password) throws NullPointerException, WrongIdException
	{
		//EFFECTS: Rimuove dal sistema il documento digitale identificato dal nome.Restituisce true se l’operazione ha successo,
		// false se fallisce perche' non esiste un documento con quel nome.
		if(user==null) throw new NullPointerException();
		if(doc==null) throw new NullPointerException();
		readDoc(user,doc,password); // uso readDoc per tutte le verifiche,  tdoc e tutente spiegate nella relazione
		if(this.tdoc!=-1 && this.tutente!=-1)
                {
                    users.get(this.tutente).docs.remove(this.tdoc);
                    return true;
                }
		return false;
	}
	public void readDoc(String user, String doc, int password) throws WrongIdException, NullPointerException
	{
		// EFFECTS:Legge il documento digitale identificato dal nome.
		// Lancia WrongIdException se user non e' il nome di un utente registrato
		// o se non esiste un documento con quel nome
		// o se la password non e' corretta
		int trovatoU=0;
                int trovatoD=0;
                // inizializzo le due variabili che uso sempre nella readDoc
                this.tutente=-1;
                this.tdoc=-1;
		if(user==null) throw new NullPointerException();
		if(doc==null) throw new NullPointerException();
                // cerca l'utente user con la sua password e se lo trova cerca il documento nella lista documenti del utente
		for(int i=0 ; i<users.size();i++)
		{
			if( (users.get(i).getIdUtente().equals(user) ) && (users.get(i).GetPassword()==password) )
			{
				trovatoU=1;
                                this.tutente=i; //trovato utente in posizione i
			
				for(int j=0;j<users.get(i).docs.size();j++)
				{
					if(users.get(i).docs.get(j).name.equals(doc))
					{
						trovatoD=1;
                                                this.tdoc=j; // trovato documento in posizione j
					}
				}
			}
		}
                if(trovatoU==0)
                {
                    throw new WrongIdException("Autenticazione fallita");
                }
                if(trovatoD==0)
                {
                    throw new WrongIdException("Documento non e' stato trovato");
                }
		
	}
	
	public void shareDoc(String fromName, String toName, String doc, int password) throws WrongIdException
	{
            // EFFECTS:Notifica una condivisione di documento
            // Lancia un'eccezione WrongIdException se fromName o toName non sono nomi 
            // di utenti registrati o se non esiste un documento con quel nome
            // o se la password e’ corretta
            int pos=-1;
            readDoc(fromName,doc,password);
            if(this.tdoc==-1 && this.tutente==-1) // readDoc fallisce
            {
                throw new WrongIdException();
            }
            for(int i=0 ; i<users.size();i++)
		{
                        if(users.get(i).getIdUtente().equals(toName) )
                        {
                             pos=i;
                             users.get(i).queue.add(doc);
                        }
                }  
            if(pos==-1)
            {
                throw new WrongIdException("toName non e' stato trovato!");
            }
            //altrimenti
            
	}
	public String getNext(String user, int password) throws EmptyQueueException,WrongIdException
	{
            // EFFECTS: Restituisce il nome del documento condiviso cancellandolo dalla coda 
            // delle notifiche di condivisione.
            // Lancia un'eccezione EmptyQueueException se non ci sono notifiche,
            // o WrongIdException se user non e' il nome di un utente registrato
            // o se la password non e’ corretta
            int trf=-1;
            String a;
            for(int i=0 ; i<users.size();i++)
		{
			if( (users.get(i).getIdUtente().equals(user) ) && (users.get(i).GetPassword()==password) )
                        {
                            // ho trovato user con password giusta
                            // controllo che la coda non sia vuota
                            if(users.get(i).queue.isEmpty())
                                throw new EmptyQueueException("Coda di condivisione e' vuota");
                            trf=i;
                        }
                }
            if(trf==-1)
                throw new WrongIdException("Autenticazione fallita");
            
            a=users.get(trf).queue.remove(); // rimuovo il documento dalla coda e metto il suo nome in a
            System.out.println("documento : " + a);
            return a;
	}
        public void StampaUser()
        {
               Iterator<User> it = users.iterator();
               while(it.hasNext())
               {
                        User user = it.next(); //estrae il prossimo elemento
                        if (it.hasNext() == false)
                            System.out.print(user.getIdUtente());
                        else
                            System.out.print(user.getIdUtente() + ";");
                }
               System.out.println(";");
        }
}
