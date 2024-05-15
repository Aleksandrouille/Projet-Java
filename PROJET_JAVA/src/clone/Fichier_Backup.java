package clone;

import java.time.LocalDateTime;

// Cette classe permet de:
// Obtenir un fichier comprenant le nom et sa date de creation.

public class Fichier_Backup {
	
	
	private String name;
	private LocalDateTime date;
	LocalDateTime currentDateTime = LocalDateTime.now();
	// (Cree les attributs nom et date et definit la date et heure actuelle)

	
	public Fichier_Backup(String n) {
		this.name = n;
		this.date = currentDateTime;
	}  
	// (Attribue le nom et la date selon les commandes)
	
	public String toString() {
			return "("+date+")"+name ;
			
	} 
	// (Retourne la date entre parentheses et le nom)
    
	public static void main(String[] args) {
		
	}



	}
