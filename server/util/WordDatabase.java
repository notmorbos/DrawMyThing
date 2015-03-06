package util;

import java.util.ArrayList;
import java.util.List;

public class WordDatabase {
	
	private List<String> database;
	private List<String> alreadyused;
	
	WordDatabase () {
		database = new ArrayList<String>(getWords());
		alreadyused = new ArrayList<String>();
	}
	
	public String getRandomWord() {
		int random = 0;
		do {
			random = Math.toIntExact(Math.round(database.size() * Math.random()));
		} while (alreadyused.contains(database.get(random)));
		return database.get(random);
	}
	
	public void emptyAlreadyUsed() {
		alreadyused.clear();
	}
	
	public boolean isGuessCorrect(String guess, String answer) {
		
		//TODO: evtl Rechtschreibfehler ignorieren etc
		
		guess.toLowerCase();
		guess.trim();
		answer.toLowerCase();
		answer.trim();
		
		if(answer.equals(guess)) {
			return true;
		}
		else {
			int fehler = 0;
			for(int i = 0; i < Math.max(answer.length(), guess.length()); i++) {
				if(guess.charAt(i) == answer.charAt(i)) {
					fehler++;
				}
			}
			if(fehler<2) {
				return true;
			}
		}
		return false;
	}

	private List<String> getWords() {
		List<String> allwords = new ArrayList<String>();
		
		//TODO: mehr W�rter?
		
		String[] temp = {
				"Sonnenblume", "Pferdestall", "Wasserhahn",
				"B�cherwurm", "Schnecke", "Zaun", "Ohrring", "Rakete",
				"Kartenspiel", "Rapunzel", "Handschuh", "Bommelm�tze",
				"Schneemann", "H�hle", "Computer", "Fahrrad", "Elefant",
				"Ameisenhaufen", "Eiszapfen", "Schreibtisch", "Schaf", "Zwerg",
				"Riese", "Hochhaus", "Kirchturm", "Armbanduhr", "Gespenst",
				"Seestern", "Mauseloch", "Teekanne", "Mantel", "Garage",
				"Fu�ballfeld", "Hundeh�tte", "Pirat", "Schatzkiste", "Knochen",
				"Brille", "Toilette", "Strand", "Muschel", "Haust�r",
				"Fensterbank", "Blumentopf", "L�ffel", "Schlitten", "Schrank",
				"Kamel", "Salzstange", "Friseur", "Turnschuh", "Schubkarre",
				"Balkon", "Kaffeetasse", "Sternenhimmel", "Regenwolke",
				"Engel", "Bratwurst", "Schiff", "Vogelnest", "Gitarre",
				"Trommel", "Apfelbaum", "Briefumschlag", "Briefmarke",
				"Fernseher", "Pommes", "Riesenrad", "Vampir", "Puppe",
				"Schmetterling", "Tausendf��ler", "Clown", "Stehlampe",
				"B�geleisen", "Gl�hbirne", "Erdbeere", "Bonbon", "Advent",
				"Antimaterie", "Antischuppenshampoo", "Antivirensoftware",
				"Apfelschorle", "Aschenbecher", "Ausverkauf", "Baumschule",
				"Beach-Volleyball", "Bibliothek", "Bienenk�nigin",
				"Brieftaube", "Brillenputztuch", "Brotzeit", "Buchbinder",
				"Bushaltestelle", "Caipirinha", "CD-St�nder",
				"China-Restaurant", "Chinesische Mauer", "Echo",
				"Eintrittskarte", "Eisblumen", "Erdnussflips",
				"Fahrgemeinschaft", "Fahrzeugschein", "Fegefeuer", "Firewall",
				"FKK-Strand", "Freiheitsstatue", "Friedensnobelpreis",
				"Fr�hjahrsm�digkeit", "Fr�hlingsrolle", "Gasblase",
				"Gladiator", "Gletscher", "Gurkensalat", "Hausmeister",
				"Hawaiitoast", "Heckenschere", "Hei�luftballon", "Hello Kitty",
				"Hitlerattentat", "Hobbitf�sse", "Hofhund", "Kettenraucher",
				"Kindergarten", "Kl�ranlage", "Klammeraffe", "Kletterpflanze",
				"K�nig der L�wen", "Kopfschmerztablette", "K�rbchengr��e",
				"Kr�henf��e", "Krankenpfleger", "Krankheitserreger",
				"Kugelschreiber", "Lachgas", "Laserdrucker",
				"Lebensmittelvergiftung", "Lexikon", "Mikrowelle", "Monsun",
				"Nudelholz", "Nutellabrot", "Obdachlosenheim", "Paradies",
				"Pfeffersteak", "Pizzab�cker", "Programmiersprache",
				"Radiomoderator", "Rattenschwanz", "Rechtsanwaltsgehilfe",
				"R�hrei", "Sandm�nnchen", "Schluckauf", "Schwarzarbeiter",
				"Schwulenbar", "Seilt�nzerin", "Selbstmordattent�ter",
				"Shisha", "Skiunfall", "Sonnenfinsternis", "Spurrillen",
				"S�sswarenladen", "Tiefseeungeheuer", "T�tensuppe",
				"Uhrzeiger", "Valentinstag", "Verkehrskontrolle", "Viagra",
				"W�hlscheibe", "Wasserwerfer", "Wettervorhersage",
				"Zeitumstellung" };
		
		for(String s : temp) {
			allwords.add(s);
		}
		
		return allwords;
	}
}
