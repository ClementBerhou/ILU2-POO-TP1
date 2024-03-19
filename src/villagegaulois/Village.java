package villagegaulois;

import personnages.Chef;	
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int nbEtalsMarche) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		marche = new Marche(nbEtalsMarche);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}
	
	
	public class VillageSansChefException extends RuntimeException {
		  public VillageSansChefException(String s) {
		    super(s);
		  }
	}
	

	public String afficherVillageois() throws VillageSansChefException {
		try {
			chef.getNom();
		} catch (Exception e) {
			throw new VillageSansChefException("Il n'y aucun chef dans ce village !");
		}
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		StringBuilder chaine = new StringBuilder();
		chaine.append(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + " " + produit + ".\n");
		
		int indEtalLibre = marche.trouverEtalLibre();
		
		if (indEtalLibre != -1) {
			marche.utiliserEtal(indEtalLibre, vendeur, produit, nbProduit);
			chaine.append("Le vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n° " + (indEtalLibre+1) + "\n");
		} else {
			chaine.append("Il n'y pas d'étal disponible\n");
		}
		
		return chaine.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {
		StringBuilder chaine = new StringBuilder();
		Etal[] vendeurs = marche.trouverEtals(produit);
		
		if (vendeurs.length > 1) {
			chaine.append("Les vendeurs qui proposent des " + produit + " sont : \n");
			for (int i = 0; i < vendeurs.length; i++) {
				if (vendeurs[i] != null && vendeurs[i].getVendeur() != null && vendeurs[i].getVendeur().getNom() != null ) {
	                chaine.append(" - " + vendeurs[i].getVendeur().getNom() + "\n");	                
	            }
			}
		} else if (vendeurs.length == 1) {
			if (vendeurs[0] != null && vendeurs[0].getVendeur() != null) {
	            chaine.append("Seul le vendeur " + vendeurs[0].getVendeur().getNom() + " propose des " + produit + " au marché.\n");
	        }
		} else if (vendeurs.length == 0) {
			chaine.append("Il n'y a pas de vendeur qui propose des " + produit + " au marché.\n");
		}
		
		return chaine.toString();
	}
	
	public String afficherMarche() {
		int nbEtalVide = 0;
		int indiceEtal = 0;
		StringBuilder chaine = new StringBuilder();
		
		chaine.append("Le marché du village \"" + this.getNom() + "\" possède ");
		if (marche.etals.length > 1) {
			chaine.append("plusieurs étals : \n");
		} else if (marche.etals.length == 1) {
			chaine.append("un étal : \n");
		} else {
			chaine.append("aucun étal.\n");
		}
		
		
		while(indiceEtal < marche.etals.length) {
			Etal etalCour = marche.etals[indiceEtal];
			
			if ("L'étal est libre".equals(etalCour.afficherEtal())) {
				nbEtalVide += 1;
			} else {
				chaine.append(etalCour.afficherEtal());
			}
			indiceEtal += 1;
		}
		
		if (nbEtalVide > 0) {
			chaine.append("Il reste " + nbEtalVide + " étals non utilisés dans le marché.\n");
		}
		
		return chaine.toString();
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
		for (Etal etal : marche.etals) {
			if (etal.isEtalOccupe() && etal.getVendeur().getNom() == vendeur.getNom()) {
				return etal;
			}
		}
		
		return null;
	}
	
	public String partirVendeur(Gaulois vendeur) {
		
		for (Etal etal : marche.etals) {
			if (etal.isEtalOccupe() && etal.getVendeur().getNom() == vendeur.getNom()) {
				return etal.libererEtal();
			}
		}
		
		return null;
	}
	
	public static class Marche {
		private Etal[] etals;
		
		public Marche(int nbEtals) {
			etals = new Etal[nbEtals];
			for (int i = 0; i < nbEtals; i++) {
		        etals[i] = new Etal();
		    }
		}
		
		public void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			
			if (!etals[indiceEtal].isEtalOccupe()) {
					
				etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
				 
			}
		}
		
		public int trouverEtalLibre() {
			int indiceEtal = 0;
			while(indiceEtal < etals.length) {
				if (etals[indiceEtal].isEtalOccupe() ) {
					indiceEtal += 1;
				} else {
					return indiceEtal;
				}	
			}
			return -1;
		}
		
		public Etal[] trouverEtals(String produit) {
			int nbEtal = 0;
			int indiceEtal = 0;
			
			while(indiceEtal < etals.length) {
				if (etals[indiceEtal].isEtalOccupe() && etals[indiceEtal].contientProduit(produit)) {
					nbEtal += 1;
				}
			
				indiceEtal += 1;
				
			}
			
			Etal[] etalsVendProduit = new Etal[nbEtal];
			
			
			int indiceEtalVend = 0;
			indiceEtal = 0;
			
			while(indiceEtal < etals.length) {
				if (etals[indiceEtal].isEtalOccupe() && etals[indiceEtal].contientProduit(produit)) {
					etalsVendProduit[indiceEtalVend] = etals[indiceEtal];
					indiceEtalVend += 1;
				}
				indiceEtal += 1;
			}	
			
			return etalsVendProduit;
		}
		
		public Etal trouverVendeur(Gaulois gaulois) {
			int indiceEtal = 0;
			
			while(indiceEtal < etals.length) {
				if (gaulois == etals[indiceEtal].getVendeur()) {
					return etals[indiceEtal];
				}
				indiceEtal ++;
			}
			
			return null;
		}
		
		
	
	
	
	
	}
	
	
	
	
	
}
