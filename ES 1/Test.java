/**
 *
 * @author Gherhard
 */
import java.util.*;
public class Test {
	public static void main (String[] args) throws WrongIdException, EmptyQueueException
	{
            ShareDocImpl a = new ShareDocImpl();
            /* test di addUser */
            System.out.println("Test addUser giusto");
            a.addUser("Elliot",123);
            a.addUser("Angela",321);
            System.out.println("addUser: Test User null");
            try{
                a.addUser(null,0);
            }
            catch(NullPointerException e)
            {
                System.out.println(e.getMessage());
            }
            /* test di removeUser */
            System.out.println("removeUser: Test User null");
            try{
                a.removeUser(null);
            }
            catch(NullPointerException e)
            {
                System.out.println(e.getMessage());
            }
            System.out.println("addDoc: Test User Sbagliato");
            /* test di addDoc */
            try{
                a.addDoc("Ellie", "doc", 123);
            }
            catch(WrongIdException e)
            {
                System.out.println(e.getMessage());
            }
            System.out.println("addDoc: Test Password Sbagliato");
            try{
                a.addDoc("Elliot", "doc", 0);
            }
            catch(WrongIdException e)
            {
                System.out.println(e.getMessage());
            }
            /* tre documenti di test */
            a.addDoc("Elliot", "doc", 123);
            a.addDoc("Elliot", "docs", 123);
            a.addDoc("Elliot", "docx", 123);
            
            /* test di removeDoc */
            System.out.println("removeDoc: Nome Documento Sbagliato");
            try{
                a.removeDoc("Elliot", "ddd", 123);
            }
            catch(WrongIdException e)
            {
                System.out.println(e.getMessage());
            }
            System.out.println("removeDoc: Nome User Sbagliato");
            try{
                a.removeDoc("Ellie", "doc", 123);
            }
            catch(WrongIdException e)
            {
                System.out.println(e.getMessage());
            }
            System.out.println("removeDoc: Nome User null");
            try{
                a.removeDoc(null, "doc", 123);
            }
            catch(NullPointerException e)
            {
                System.out.println(e.getMessage());
            }
            System.out.println("removeDoc: Password null");
            try{
                a.removeDoc("Elliot", null, 123);
            }
            catch(NullPointerException e)
            {
                System.out.println(e.getMessage());
            }
            a.removeDoc("Elliot", "doc", 123);
            a.removeDoc("Elliot", "docs", 123);
            
            /* test readDoc */
            System.out.println("readDoc: Doc null");
            try{
                a.readDoc("Elliot", null, 123);
            }
            catch(NullPointerException e)
            {
                System.out.println(e.getMessage());
            }
            System.out.println("readDoc: Doc sbagliato!");
            try{
                a.readDoc("Elliot", "ddd", 123);
            }
            catch(WrongIdException e)
            {
                System.out.println(e.getMessage());
            }
            System.out.println("readDoc: User name Sbagliato");
            try{
                a.readDoc("Ellie", "docs", 123);
            }
            catch(WrongIdException e)
            {
                System.out.println(e.getMessage());
            }
            a.readDoc("Elliot", "docx", 123);
            /* test shareDoc */
            System.out.println("shareDoc: User name sbagliato");
            try{
                a.shareDoc("Ellie", "Angela", "docx", 123);
            }
            catch(WrongIdException e)
            {
                System.out.println(e.getMessage());
            }
            System.out.println("shareDoc: ToName Sbagliati");
            try{
                a.shareDoc("Elliot", "Ange", "docx", 123);
            }
            catch(WrongIdException e)
            {
                System.out.println(e.getMessage());
            }
            
            a.shareDoc("Elliot", "Angela", "docx", 123);
            /* test getNext */
            System.out.println("getNext: Password sbagliata");
            try{
                a.getNext("Elliot", 0);
            }
            catch(WrongIdException e)
            {
                System.out.println(e.getMessage());
            }
            System.out.println("getNext: User name sbagliato");
            try{
                a.getNext("Ellie", 123);
            }
            catch(WrongIdException e)
            {
                System.out.println(e.getMessage());
            }
            System.out.println("getNext: Coda vuota");
            try{
                a.getNext("Elliot", 123);
            }
            catch(EmptyQueueException e)
            {
                System.out.println(e.getMessage());
            }
            
            a.getNext("Angela", 321);
            /* prova in generale */
             System.out.println("*********----Prova----**********");
             a.addUser("Gherhard",123);
             a.addUser("clack",124);
             a.addUser("fhest",125);
             a.addUser("brumbrum",126);
             a.addDoc("Gherhard", "anon", 123);
             a.addDoc("Gherhard", "xede", 123);
             a.removeUser("clack");
             a.removeUser("fhest");
             a.StampaUser();
             a.shareDoc("Gherhard", "brumbrum", "anon", 123);
             a.getNext("brumbrum", 126);
             
             
        }
		
    
    
}
