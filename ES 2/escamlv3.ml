(* Ambiente *)

type ide = string;;

type 'a env = (ide * 'a) list;; (*ambiente*)

let emptyenv ev = [ ("" , ev) ] ;;

let bind (a,x,y) = (x,y)::a ;;

let rec lookup (amb ,id) = (*cerca binding nell'ambiente*)
    match amb with 
    | [(_ , x)] -> x
    
    | (id2, e)::tail -> if id=id2 then e else lookup (tail, id)

    | _ -> failwith "Ambiente Sbagliato"

type exp =
	 | Ide of ide
	 | Eint of int
	 | Ebool of bool
	 | And of exp*exp
	 | Or of exp*exp
	 | Not of exp
	 | Add of exp*exp
	 | Sub of exp*exp
	 | Mul of exp*exp
	 | Eq of exp*exp
     | Mineq of exp*exp
	 | IfThenElse of exp*exp*exp
	 | Let of ide*exp*exp
     | Fun of ide*exp
	 | Call of exp * exp 
	 | Tuple of exp list
	 | Applyfun of exp*exp (* e' la map*)
	 | EleTupla of exp*exp (* accesso elemento tupla*)
	 | FirstKappa of exp*exp (*selezione elementi della tupla*)


type eval = Int of int
	  | Bool of bool
	  | Funval of ef
	  | Unbound
	  | Tup of eval list
and ef = exp * eval env;;


(* funzioni di supporto *)

let rec primik (lista,k) = match ( lista , k ) with
	|(_,0) -> []
	|([],_) -> []
	|(_,k1) when k1=0 -> []
	|(x::l,k1) -> x::primik (l, (k1-1))

let rec elk (lista,k) = match (lista , k) with 
	| (x::l,0) -> x
	| ( [], _ ) -> failwith "lista vuota"
	| ( _ :: l , i) -> if i > 0 
			then elk (l,(i-1))
			else failwith "indice sbagliato"

let rec equiall ((lista1: eval list) , (lista2: eval list) ) = match (lista1,lista2) with
	| ([],[]) -> true
	| ([],_) -> false
	| (_,[]) -> false
	| (x::xs, y::ys) -> (match (x,y) with
		| (Int a ,Int b) -> if a = b
				then true && equiall (xs,ys)
				else false
		| (Bool a ,Bool b) -> if a = b
				then true && equiall ( xs , ys )
				else false
		| (Tup a , Tup b) -> equiall (a ,b) && equiall (xs,ys)
		| _ -> false ) ;;



let rec sem ((e:exp), (amb:eval env)) = 
	match e with
	| Ide id -> lookup (amb, id)
	
	| Eint i -> Int i
	
 	| Ebool b -> Bool b
	
	| And (e1,e2) -> (match (sem (e1, amb), sem (e2, amb)) with   (*regola interna valuto tutte le espressioni interne e poi applico loperatore*)
			 | (Bool true, Bool true) -> Bool true
			 | (Bool true, Bool false) -> Bool false
			 | (Bool false, Bool true) -> Bool false
			 | (Bool false, Bool false) -> Bool false
			 | _ -> failwith "And: tipo sbagliato")
			 
	| Or (e1,e2) -> (match (sem (e1, amb), sem (e2, amb)) with
			| (Bool true, Bool true) -> Bool true
			| (Bool true, Bool false) -> Bool true
			| (Bool false, Bool true) -> Bool true
			| (Bool false, Bool false) -> Bool false
			| _ -> failwith "Or: tipo sbagliato")
			
	| Not e1 -> (match (sem (e1, amb)) with
		    | Bool true -> Bool false
		    | Bool false -> Bool true
		    | _ -> failwith "Not: tipo sbagliato")
	
	
	| Add (e1,e2) -> (match (sem (e1,amb),sem( e2,amb)) with
			| (Int v1, Int v2) -> Int(v1+v2)    
			| _ -> failwith "Add: tipo sbagliato")

	
	| Sub (e1,e2) -> (match (sem (e1,amb),sem( e2,amb)) with
			| (Int v1, Int v2) -> Int(v1-v2)    
			| _ -> failwith "Sub: tipo sbagliato")
	
	
	| Mul (e1,e2) -> (match (sem (e1,amb),sem( e2,amb)) with
			| (Int v1, Int v2) -> Int(v1*v2)    
			| _ -> failwith "Mul: tipo sbagliato")
	
	
	| Eq (e1,e2) -> (match (sem (e1,amb),sem( e2,amb)) with
			| (Int v1, Int v2) -> Bool(v1=v2)
			| (Bool v1, Bool v2) -> Bool(v1=v2)
			| (Tup v1, Tup v2) -> Bool (equiall(v1,v2))    
			| _ -> failwith "tipo sbagliato")

	| Mineq (e1,e2) -> (match (sem (e1,amb),sem( e2,amb)) with
			| (Int v1, Int v2) -> Bool(v1<=v2)    
			| _ -> failwith "Mineq: tipo sbagliato")

	| IfThenElse (e1,e2,e3) -> (match sem (e1, amb) with (*regola esterna, valuto  e1 e in base a quello valuto gli altri*)
			   | Bool true -> sem (e2, amb) 
			   | Bool false -> sem (e3, amb)
			   | _ -> failwith "If: guardia non booleana")

	| Let (id, e1, e2) -> sem (e2, bind (amb, id , sem(e1, amb)))

	| Fun ( id, e1 ) -> Funval ( e , amb )

	| Call ( f , att ) -> ( match sem ( f , amb ) with
		| Funval ( Fun (id , e1 ) , amb1 ) ->
			sem ( e1 , bind ( amb1 , id , sem ( att , amb ) ) )
		| _ -> failwith "Wrong id")

	| Tuple (e1) -> Tup (elts (e1, amb) )

	| Applyfun (e1,f) -> (match (sem (e1, amb), sem(f,amb) ) with
			      | (Tup l, Funval (Fun (id,e3), amb1) ) -> Tup (applyfun (l,id,e3,amb1))	
			      | _ -> failwith "Applyfun: non corretta")

	| EleTupla (tupl,ind) -> (match (sem(tupl,amb), sem (ind,amb)) with
				 | (Tup t, Int i) -> elk (t,i)
				 | _ -> failwith "Argomenti sbagliati")

	| FirstKappa (tupl, ind) ->(match (sem(tupl, amb),sem(ind,amb)) with
			         | (Tup(l),Int k) -> Tup( primik (l,k))
			         | _ -> failwith "Argomenti sbagliati")



	and  applyfun ((l:eval list) ,(id:ide) ,(e: exp) ,(amb1: eval env)) = match l with
							| []->[]
							| t::ts -> sem(e, bind(amb1,id,t)) :: applyfun (ts,id,e,amb1)

	and elts ((l: exp list),(a: eval env)) = match l with
		| []->[]
		| x::xs -> sem (x,a) :: elts (xs,a);;




(* TEST *)


(* test 1 somma:  x + 3 ;; con x = 1 => int 4 *)

let amb : eval env = [ ( "x" , Int 1 ) ] @ emptyenv Unbound ;;
let espr : exp = Add ( Eint 3 , Ide "x" ) ;;
sem ( espr , amb ) ;;

(*----------------------------------------------------------*)

(* test 2: let n = if ( a = true ) && ( x <= y )
	then  1 + x
	else  y - 5
	in let m = 3
		in m * n ;;
con y = 2 , x = 1 , a = true => int 6 *)

let amb : eval env = [ ( "y" , Int 2 ) ; ( "x" , Int 1 ) ; ( "a" , Bool true ) ] @ emptyenv Unbound ;;
let e : exp = 
	Let ( "n" , IfThenElse ( And ( Eq ( Ide "a" , Ebool true ) , Mineq ( Ide "x" , Ide "y" ) ) ,
			Add ( Ide "x" , Eint 1) ,
			Sub ( Ide "y", Eint 5 ) ) ,
				Let ( "m" , Eint 3 ,
					Mul ( Ide "n" , Ide "m" ) ) ) ;;
sem ( e , amb ) ;;

(*----------------------------------------------------------*)

(* test 3 di fun: let a = fun arg -> arg + 7
	in let t = x - 2
		in a t ;;
 *)
let amb : eval env = [ ( "x" , Int 5 ) ] @ emptyenv Unbound ;;
let e : exp = Let ( "a" , Fun ( "arg" , Add ( Ide "arg" , Eint 7 ) ) ,
				Let ( "n" , Sub ( Ide "x" , Eint 2 ) ,
					Call ( Ide "a" , Ide "t" ) ) ) ;;
sem ( e , amb ) ;;

(*----------------------------------------------------------*)

(* test 4 : let t = [ x , x + 2 , y <= 3 , [ 5 , not a ] , at ( [ 5 , not a ] , 0 ) = 3 ]
	in ( t = t ) || ( t = [ 5 , not a ] ) ;;
con y = 2 , x = 1 , a = true => bool true *)

let amb : eval env = [ ( "y" , Int 2 ) ; ( "x" , Int 1 ) ; ( "a" , Bool true ) ] @ emptyenv Unbound ;;
let tup2 : exp = Tuple [ Eint 5 ; Not ( Ide "a" ) ] ;;
let tup1 : exp = Tuple [ Ide "x" ; Add ( Ide "x" , Eint 2 ) ; Mineq ( Ide "y" , Eint 3 ) ; tup2 ; Eq ( EleTupla ( tup2 , Eint 0 ) , Eint 3 ) ];;
let e : exp = Let ( "t" , tup1 , Or ( Eq ( Ide "t" , Ide "t" ) , Eq ( tup2 , Ide "t" ) ) ) ;;
sem ( tup1 , amb ) ;;
sem ( tup2 , amb ) ;;
sem ( e , amb ) ;;

(*----------------------------------------------------------*)

(* test chiesto: let add5 = fun x -> x + 5 in
	let tup = [ 5 , 6 , true , 7 ] in
		applyfun ( primik t 2 ) add5 ;;
=> tupla fatta da [ 10 , 11 ] *)
let amb = emptyenv Unbound ;;
let e : exp = Let ( "add5" , Fun ( "x" , Add ( Ide "x" , Eint 5 ) ) ,
				Let ( "t" , Tuple [ Eint 5 ; Eint 6 ; Ebool true ; Eint 7 ] ,
					Applyfun ( FirstKappa ( Ide "t" , Eint 2 ) , Ide "add5") ) ) ;;
sem ( e , amb ) ;;








