package util;

import java.util.ArrayList;
import java.util.List;

public class WordDatabase {
	
	private List<String> database;
	private List<String> alreadyused;
	
	public WordDatabase () {
		database = new ArrayList<String>(getWords());
		alreadyused = new ArrayList<String>();
	}
	
	public String getRandomWord() {
		int random = 0;
		do {
			random = Math.toIntExact(Math.round(database.size() * Math.random()));
		} while (alreadyused.contains(database.get(random)));
		alreadyused.add(database.get(random));
		return database.get(random);
	}
	
	public void emptyAlreadyUsed() {
		alreadyused.clear();
	}
	
	public int forEachNineLetters(String lukasStinkt){
		int temp = lukasStinkt.length();
		if(temp<=9){
			return 2;
		}else if(temp<=18){
			return 3;
		}else if(temp<=27){
			return 4;
		}else{
			return 5;
		}
	}
	
	/** 
	 * @param anzahl 0 für anzahl der Buchstaben des Wortes, >1 für random hint mit anzahl = Buchstaben
	 * @param answer die korrekte Lösung für den String
	 * @return ganz viele _ mit ein paar richtigen Buchstaben
	 */
	public String giveHint(int anzahl, String answer){
		char[] word = new char[answer.length()];
		int random = 0;
		int benutzt = -1;
		String hint = "";
		for(int i = 0; i < anzahl; i++){
			do{
				random = Math.toIntExact(Math.round(answer.length() * Math.random()));
			}while(random != benutzt && !(answer.charAt(random) == 'a') && !(answer.charAt(random) == 'e') && !(answer.charAt(random) == 'i') && !(answer.charAt(random) == 'o') && !(answer.charAt(random) == 'u'));
			for(int j = 0; j < answer.length(); j++){
				if(!(j == random)){
					word[j] = '_';
				}else if(j == random){
					word[j] = answer.charAt(j);
					benutzt = random;
				}
			}
		}
		for(int h = 0; h < word.length; h++){
			hint = hint + word[h];
		}
		return hint;
	}
	
	public boolean isGuessCorrect(String guess, String answer) {
		
		//TODO: evtl Rechtschreibfehler ignorieren etc
		
		guess.toLowerCase();
		guess.trim();
		answer.toLowerCase();
		answer.trim();
		
		/*
		if(answer.equals(guess)) {
			return true;
		}
		else {
			
			int fehler = 0;
			//fehlertoleranz
			for(int i = 0; i < Math.max(answer.length(), guess.length()); i++) {
				if(guess.charAt(i) == answer.charAt(i)) {
					fehler++;
				}
			}
			if(fehler<forEachNineLetters(answer)) {
				return true;
			}
			//abfangen feminisierung, -in
			if(answer.substring(answer.length()-1) == "e"){
				if(guess.substring(0, guess.length()-2) == answer.substring(0, answer.length()-1) && guess.substring(guess.length()-2) == "in"){
					return true;
				}
			}
			if(answer.concat("in") == guess){
				return true;
			}
		}
		*/
		return answer.equals(guess);
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
