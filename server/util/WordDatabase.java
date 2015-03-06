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
		
		//TODO: mehr Wörter?
		
		String[] temp = {
				"Sonnenblume", "Pferdestall", "Wasserhahn",
				"Bücherwurm", "Schnecke", "Zaun", "Ohrring", "Rakete",
				"Kartenspiel", "Rapunzel", "Handschuh", "Bommelmütze",
				"Schneemann", "Höhle", "Computer", "Fahrrad", "Elefant",
				"Ameisenhaufen", "Eiszapfen", "Schreibtisch", "Schaf", "Zwerg",
				"Riese", "Hochhaus", "Kirchturm", "Armbanduhr", "Gespenst",
				"Seestern", "Mauseloch", "Teekanne", "Mantel", "Garage",
				"Fußballfeld", "Hundehütte", "Pirat", "Schatzkiste", "Knochen",
				"Brille", "Toilette", "Strand", "Muschel", "Haustür",
				"Fensterbank", "Blumentopf", "Löffel", "Schlitten", "Schrank",
				"Kamel", "Salzstange", "Friseur", "Turnschuh", "Schubkarre",
				"Balkon", "Kaffeetasse", "Sternenhimmel", "Regenwolke",
				"Engel", "Bratwurst", "Schiff", "Vogelnest", "Gitarre",
				"Trommel", "Apfelbaum", "Briefumschlag", "Briefmarke",
				"Fernseher", "Pommes", "Riesenrad", "Vampir", "Puppe",
				"Schmetterling", "Tausendfüßler", "Clown", "Stehlampe",
				"Bügeleisen", "Glühbirne", "Erdbeere", "Bonbon", "Advent",
				"Antimaterie", "Antischuppenshampoo", "Antivirensoftware",
				"Apfelschorle", "Aschenbecher", "Ausverkauf", "Baumschule",
				"Beach-Volleyball", "Bibliothek", "Bienenkönigin",
				"Brieftaube", "Brillenputztuch", "Brotzeit", "Buchbinder",
				"Bushaltestelle", "Caipirinha", "CD-Ständer",
				"China-Restaurant", "Chinesische Mauer", "Echo",
				"Eintrittskarte", "Eisblumen", "Erdnussflips",
				"Fahrgemeinschaft", "Fahrzeugschein", "Fegefeuer", "Firewall",
				"FKK-Strand", "Freiheitsstatue", "Friedensnobelpreis",
				"Frühjahrsmüdigkeit", "Frühlingsrolle", "Gasblase",
				"Gladiator", "Gletscher", "Gurkensalat", "Hausmeister",
				"Hawaiitoast", "Heckenschere", "Heißluftballon", "Hello Kitty",
				"Hitlerattentat", "Hobbitfüsse", "Hofhund", "Kettenraucher",
				"Kindergarten", "Kläranlage", "Klammeraffe", "Kletterpflanze",
				"König der Löwen", "Kopfschmerztablette", "Körbchengröße",
				"Krähenfüße", "Krankenpfleger", "Krankheitserreger",
				"Kugelschreiber", "Lachgas", "Laserdrucker",
				"Lebensmittelvergiftung", "Lexikon", "Mikrowelle", "Monsun",
				"Nudelholz", "Nutellabrot", "Obdachlosenheim", "Paradies",
				"Pfeffersteak", "Pizzabäcker", "Programmiersprache",
				"Radiomoderator", "Rattenschwanz", "Rechtsanwaltsgehilfe",
				"Rührei", "Sandmännchen", "Schluckauf", "Schwarzarbeiter",
				"Schwulenbar", "Seiltänzerin", "Selbstmordattentäter",
				"Shisha", "Skiunfall", "Sonnenfinsternis", "Spurrillen",
				"Süsswarenladen", "Tiefseeungeheuer", "Tütensuppe",
				"Uhrzeiger", "Valentinstag", "Verkehrskontrolle", "Viagra",
				"Wählscheibe", "Wasserwerfer", "Wettervorhersage",
				"Zeitumstellung" };
		
		for(String s : temp) {
			allwords.add(s);
		}
		
		return allwords;
	}
}
