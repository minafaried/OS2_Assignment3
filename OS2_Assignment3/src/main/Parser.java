package main;

import java.util.ArrayList;
import java.util.List;

public class Parser {

	public List<String> parseInput(String command) {
		String parse[] = command.split(" ");
		String parseFileName[] = null;
		List<String> commandParts = new ArrayList<>();
		for (int i = 0; i < parse.length; i++) {
			commandParts.add(parse[i]);
		}
		if (commandParts.size() == 3) {
			parseFileName = parse[1].split("/");
			if(parseFileName.length < 2) {
				return null;
			}
			if (commandParts.get(0).equals("CreateFile") && parseFileName[0].equals("root")) {
				if (commandParts.size() < 3) {
					return null;
				} else {
					try {
						Integer.parseInt(commandParts.get(2));
					} catch (NumberFormatException e) {
						return null;
					}
				}
			}
		} else if (commandParts.size() == 2) {
			parseFileName = parse[1].split("/");
			if(parseFileName.length < 2) {
				return null;
			}
			if (commandParts.get(0).equals("CreateFolder") && parseFileName[0].equals("root")) {
				return commandParts;
			}

			else if (commandParts.get(0).equals("DeleteFile") && parseFileName[0].equals("root")) {
				return commandParts;
			} else if (commandParts.get(0).equals("DeleteFolder") && parseFileName[0].equals("root")) {
				return commandParts;
			} else {
				return null;
			}
		} else if (commandParts.size() == 1) {
			if (commandParts.get(0).equals("DisplayDiskStatus")) {
				return commandParts;
			} else if (commandParts.get(0).equals("DisplayDiskStructure")) {
				return commandParts;
			} else {
				return null;
			}
		} else {
			return null;
		}
		return commandParts;
	}
}
