package clone;

import java.io.File;
import java.util.TimerTask;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Cette classe permet de:
/////////// Si la source est modifiee en premier /////////////
// - Parcourir les fichiers dans Reference.
// - Voir d'abord si un fichier qui n'est pas dans Source est dans Reference et le supprimer.
// - Parcourir les fichiers dans Source.
// - Verifier si ces fichiers existent deja dans Reference.
// - Si ils n'existent pas encore, les creer.
// - Si ils existent, verifier si ils sont a jour.
// - Si ils ne sont pas a jour, enregistrer l'ancienne verson, la supprimer et creer la nouvelle.
// - Si ils sont a jour, ne rien faire.

/////////// Si c'est l'inverse /////////////
// - refait le meme procede en inversant Source et Reference


// Egalement :
// - Le code est lance toutes les minutes.
// - Les fichiers remplaces dans Reference sont enregistres dans Backup.

//!\\ Si on modifie dans Source, il peut y avoir 2 backups.

public class Synchronisation extends TimerTask {
	
	public void run() {
	
		
		
		
		//////////////////////////   PATHS   ////////////////////////////////////////////
		
		String path_source = "/Users/Rosabelle/Documents/Source";
        String path_reference = "/Users/Rosabelle/Documents/Reference";
        String path_backup = "/Users/Rosabelle/Documents/Backup"; 
        // (Emplacements de la Source, de la Reference et du Backup)
    
        //////////////////////////////////////////////////////////////////////////////////
        
        
        
        
        File dossier_source = new File(path_source); 
        // (Designe un dossier Source dans l'endroit indique)
        File dossier_reference = new File(path_reference);
        // (Designe un dossier Reference dans l'endroit indique)
        
        long tps_ecoule_a_modif_Source = dossier_source.lastModified();
		// (Recupere le temps ecoule a la derniere modification du dossier Source (en ms depuis 1970))
        long tps_ecoule_a_modif_Reference = dossier_reference.lastModified();
		// (Recupere le temps ecoule a la derniere modification du dossier Reference (en ms depuis 1970))
        
        long temps = System.currentTimeMillis();
        // (Permet d'eviter des decalages)
        
        if(tps_ecoule_a_modif_Source > tps_ecoule_a_modif_Reference) {
        	
        	System.out.println("///////////////////////////MODIF_SOURCE///////////////////////////////");
        	System.out.println(tps_ecoule_a_modif_Source-tps_ecoule_a_modif_Reference);
        
	        for(String nom_f_reference: dossier_reference.list()) {
	        	// (Cherche les fichiers dans Reference et recupere le nom sous forme string)
	        	
	        	boolean existe_dans_source = false;
	        	for(String nom_f_source: dossier_source.list()) {
	        		if(nom_f_reference.endsWith(nom_f_source)) {
	        			existe_dans_source = true;
	        		}
	        	}
	        	// (Voit si le fichier reference existe toujours dans Source)
	        	
	        	if(!existe_dans_source) {
	        		// (Ce fichier a ete supprime de Source)
	        		
	        		System.out.println("Le fichier "+nom_f_reference+" n'existe plus et sera supprime de Reference");
	        		
	        		File fichier_f_a_supprimer = new File(path_reference+"/"+nom_f_reference);
	        		// (Designe un fichier a supprimer dans l'endroit indique, ici le fichier reference)
	        		
	        		Fichier_Backup nom_f_reference_backup = new Fichier_Backup(nom_f_reference);
	        		//(Cree un fichier avec le nom du fichier reference et sa date de modification (creation))        		
	        		
	 				Path r = Paths.get(path_reference+"/"+nom_f_reference);
	 				Path b = Paths.get(path_backup+"/"+nom_f_reference_backup);
					// (Permet d'obtenir les chemins du fichier reference et du fichier a creer dans le backup sous forme string)
					
					try {
						Files.copy(r, b);
						// (Copie le contenu du fichier reference et le colle sur le fichier a creer dans Backup)
					} catch (IOException e) {
						System.out.println("Erreur: " + e.getMessage());
					}
					
	        		fichier_f_a_supprimer.delete();
	        		// (Supprime l'ancien fichier reference du dossier Referernce)
	        	}
	        }
	        
			for(String nom_f_source: dossier_source.list()) { 
				// (Cherche les fichiers dans Source et recupere le nom sous forme string)
				
				File fichier_f_source = new File(path_source+"/"+nom_f_source);
				// (Designe un fichier dans l'endroit indique, ici le fichier source)
				
				long delai = System.currentTimeMillis() - temps;
				
				long tps_ecoule_a_modif_source = fichier_f_source.lastModified() - delai;
				// (Recupere le temps ecoule a la derniere modification du fichier source (en ms depuis 1970))
				
				System.out.println("Fichier trouve dans Source = "+nom_f_source);
				System.out.println("Date de derniere modification "+nom_f_source+" = "+tps_ecoule_a_modif_source);
				
				String path_f_ref_existant = null;
				// (Declare un string qui contiendra eventuellement l'emplacement du fichier reference s'il existe deja)
				
				String nom_f_ref_existant = null;
				// (Declare un string qui contiendra eventuellement le nom du fichier reference s'il existe deja)
				
				boolean existe = false;
				for(String nom_f_reference: dossier_reference.list()) {
					if(nom_f_reference.endsWith(nom_f_source)) {
						existe = true;
						path_f_ref_existant = path_reference+"/"+nom_f_reference;
						nom_f_ref_existant = nom_f_reference;
						System.out.println("Ce fichier existe deja");
					}
				}
				// (Verifie si un fichier dont le nom comprend le nom du fichier source existe dans reference)
				
				
				if(!existe) {
					// (Si le fichier reference n'existe pas encore)
					
					System.out.println("Ce fichier n'existe pas encore");
					
					System.out.println("Fichier a creer dans Reference = "+nom_f_source);
				
					Path s = Paths.get(path_source+"/"+nom_f_source);
					Path c = Paths.get(path_reference+"/"+nom_f_source);
					// (Permet d'obtenir les chemins du fichier source et du fichier a creer sous forme string)
				
					try {
						Files.copy(s, c);
						// (Copie le contenu du fichier source et le colle sur le fichier a creer dans Reference)
					} catch (IOException e) {
						System.out.println("Erreur: " + e.getMessage());
					}
		        
				}
				if(existe){
					// (Si le fichier reference existe deja)
					
					File fichier_f_reference = new File(path_f_ref_existant); 
					// (Designe un fichier dans l'endroit indique, ici le fichier reference)
					
					delai = System.currentTimeMillis() - temps;
					
					long tps_ecoule_a_modif_reference = fichier_f_reference.lastModified() - delai;
					// (Recupere le temps ecoule a la derniere modification du fichier reference (en ms depuis 1970))
					
					System.out.println("Fichier trouve dans Reference = "+nom_f_ref_existant);
					System.out.println("Date de derniere modification"+nom_f_ref_existant+"="+tps_ecoule_a_modif_reference);
					
					if(tps_ecoule_a_modif_source > tps_ecoule_a_modif_reference + 10000) {
						// (Le fichier source a ete modifie apres la creation du fichier reference)
						
						System.out.println("Le docuement source a ete modifie");
						
						System.out.println(tps_ecoule_a_modif_source-tps_ecoule_a_modif_reference);
						
						Fichier_Backup nom_f_ref_existant_backup = new Fichier_Backup(nom_f_ref_existant);
						//(Cree un fichier avec le nom du fichier reference et sa date de modification (creation))
						
						Path r = Paths.get(path_f_ref_existant);
						Path b = Paths.get(path_backup+"/"+nom_f_ref_existant_backup);
						// (Permet d'obtenir les chemins du fichier reference et du fichier a creer dans le backup sous forme string)
						
						try {
							Files.copy(r, b);
							// (Copie le contenu du fichier reference et le colle sur le fichier a creer dans Backup)
						} catch (IOException e) {
							System.out.println("Erreur: " + e.getMessage());
						}
						
						fichier_f_reference.delete();
						// (Supprime l'ancien fichier reference du dossier Referernce)
						
						System.out.println("L'ancien fichier a ete supprime dans Reference");
						
						System.out.println("Fichier a creer dans Reference = "+nom_f_source);
						
						Path s = Paths.get(path_source+"/"+nom_f_source);
						Path c = Paths.get(path_reference+"/"+nom_f_source);
						// (Permet d'obtenir les chemins du fichier source et du fichier a creer sous forme string)
						
						try {
							Files.copy(s, c);
							// (copie le contenu du fichier source et le colle sur le fichier a creer dans reference)
						} catch (IOException e) {
							System.out.println("Erreur: " + e.getMessage());
						}
						
						System.out.println("Le docuement " +nom_f_source+ " a ete mis a jour dans Reference et enregistre dans Backup");
						
					}
					else {
						System.out.println("Le docuement " +nom_f_source+ " est a jour dans Reference");
						System.out.println(tps_ecoule_a_modif_source-tps_ecoule_a_modif_reference);
					}
					
				}
			
			}
        }
		
        
        
        
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        
        
		
		if(tps_ecoule_a_modif_Source < tps_ecoule_a_modif_Reference) {
			
			System.out.println("///////////////////////////MODIF_REF///////////////////////////////");
			System.out.println(tps_ecoule_a_modif_Reference-tps_ecoule_a_modif_Source);
		
			for(String nom_f_source: dossier_source.list()) {
	        	// (Cherche les fichiers dans Source et recupere le nom sous forme string)
	        	
	        	boolean existe_dans_reference = false;
	        	for(String nom_f_reference: dossier_reference.list()) {
	        		if(nom_f_source.endsWith(nom_f_reference)) {
	        			existe_dans_reference = true;
	        		}
	        	}
	        	// (Voit si le fichier source existe toujours dans Reference)
	        	
	        	if(!existe_dans_reference) {
	        		// (Ce fichier a ete supprime de Reference)
	        		
	        		System.out.println("Le fichier "+nom_f_source+" n'existe plus et sera supprime de Source");
	        		
	        		File fichier_f_a_supprimer = new File(path_source+"/"+nom_f_source);
	        		// (Designe un fichier a supprimer dans l'endroit indique, ici le fichier source)
	        		
	        		Fichier_Backup nom_f_source_backup = new Fichier_Backup(nom_f_source);
	        		//(Cree un fichier avec le nom du fichier reference et sa date de modification (creation))
	        		
	        		Path r = Paths.get(path_source+"/"+nom_f_source);
					Path b = Paths.get(path_backup+"/"+nom_f_source_backup);
					// (Permet d'obtenir les chemins du fichier source et du fichier a creer dans le backup sous forme string)
					
					try {
						Files.copy(r, b);
						// (Copie le contenu du fichier source et le colle sur le fichier a creer dans Backup)
					} catch (IOException e) {
						System.out.println("Erreur: " + e.getMessage());
					}
					
	                System.out.println("BACKUP REF");
					
	        		fichier_f_a_supprimer.delete();
	        		// (Supprime l'ancien fichier source du dossier Referernce)
	        	}
	        }
	        
			for(String nom_f_reference: dossier_reference.list()) { 
				// (Cherche les fichiers dans Reference et recupere le nom sous forme string)
				
				File fichier_f_reference = new File(path_reference+"/"+nom_f_reference);
				// (Designe un fichier dans l'endroit indique, ici le fichier reference)
				
				long delai = System.currentTimeMillis() - temps;
				
				long tps_ecoule_a_modif_reference = fichier_f_reference.lastModified() - delai;
				// (Recupere le temps ecoule a la derniere modification du fichier reference (en ms depuis 1970))
				
				System.out.println("Fichier trouve dans Reference = "+nom_f_reference);
				System.out.println("Date de derniere modification "+nom_f_reference+" = "+tps_ecoule_a_modif_reference);
				
				String path_f_ref_existant = null;
				// (Declare un string qui contiendra eventuellement l'emplacement du fichier source s'il existe deja)
				
				String nom_f_ref_existant = null;
				// (Declare un string qui contiendra eventuellement le nom du fichier source s'il existe deja)
				
				boolean existe = false;
				for(String nom_f_source: dossier_source.list()) {
					if(nom_f_source.endsWith(nom_f_reference)) {
						existe = true;
						path_f_ref_existant = path_source+"/"+nom_f_source;
						nom_f_ref_existant = nom_f_source;
						System.out.println("Ce fichier existe deja");
					}
				}
				// (Verifie si un fichier dont le nom comprend le nom du fichier reference existe dans source)
				
				
				
				if(!existe) {
					// (Si le fichier source n'existe pas encore)
					
					System.out.println("Ce fichier n'existe pas encore");
					
					System.out.println("Fichier a creer dans Source = "+nom_f_reference);
				
					Path s = Paths.get(path_reference+"/"+nom_f_reference);
					Path c = Paths.get(path_source+"/"+nom_f_reference);
					// (Permet d'obtenir les chemins du fichier reference et du fichier a creer sous forme string)
				
					try {
						Files.copy(s, c);
						// (Copie le contenu du fichier reference et le colle sur le fichier a creer dans Source)
					} catch (IOException e) {
						System.out.println("Erreur: " + e.getMessage());
					}
		        
				}
				if(existe){
					// (Si le fichier source existe deja)
					
					File fichier_f_source = new File(path_f_ref_existant); 
					// (Designe un fichier dans l'endroit indique, ici le fichier source)
					
					delai = System.currentTimeMillis() - temps;
					
					long tps_ecoule_a_modif_source = fichier_f_source.lastModified() - delai;
					// (Recupere le temps ecoule a la derniere modification du fichier source (en ms depuis 1970))
					
					System.out.println("Fichier trouve dans Source = "+nom_f_ref_existant);
					System.out.println("Date de derniere modification"+nom_f_ref_existant+"="+tps_ecoule_a_modif_source);
					
					if(tps_ecoule_a_modif_reference > tps_ecoule_a_modif_source) {
						// (Le fichier reference a ete modifie apres la creation du fichier source)
						
						System.out.println(tps_ecoule_a_modif_reference-tps_ecoule_a_modif_source);
						
						System.out.println("Le docuement reference a ete modifie");
						
						Fichier_Backup nom_f_ref_existant_backup = new Fichier_Backup(nom_f_ref_existant);
						//(Cree un fichier avec le nom du fichier reference et sa date de modification (creation))
						
						Path r = Paths.get(path_f_ref_existant);
						Path b = Paths.get(path_backup+"/"+nom_f_ref_existant_backup);
						// (Permet d'obtenir les chemins du fichier source et du fichier a creer dans le backup sous forme string)
						
						try {
							Files.copy(r, b);
							// (Copie le contenu du fichier source et le colle sur le fichier a creer dans Backup)
						} catch (IOException e) {
							System.out.println("Erreur: " + e.getMessage());
						}
						
	                    System.out.println("BACKUP SOURCE");
						
						fichier_f_source.delete();
						// (Supprime l'ancien fichier source du dossier Referernce)
						
						System.out.println("L'ancien fichier a ete supprime dans Source");
						
						System.out.println("Fichier a creer dans Source = "+nom_f_reference);
						
						Path s = Paths.get(path_reference+"/"+nom_f_reference);
						Path c = Paths.get(path_source+"/"+nom_f_reference);
						// (Permet d'obtenir les chemins du fichier reference et du fichier a creer sous forme string)
						
						try {
							Files.copy(s, c);
							// (copie le contenu du fichier reference et le colle sur le fichier a creer dans source)
						} catch (IOException e) {
							System.out.println("Erreur: " + e.getMessage());
						}
						
						System.out.println("Le docuement " +nom_f_reference+ " a ete mis a jour dans Source et enregistre dans Backup");
						
					}
					else {
						System.out.println("Le docuement " +nom_f_reference+ " est a jour dans Source");
						System.out.println(tps_ecoule_a_modif_reference-tps_ecoule_a_modif_source);
					}
					
				}
				
		    }
	    
	
	
		}
	}
}

