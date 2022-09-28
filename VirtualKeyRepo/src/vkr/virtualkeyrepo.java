package vkr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

class LockersRepo{
	String GlobalPath;
	int lineWidth = 100;

	public String getGlobalPath() {
		return GlobalPath;
	}

	public void setGlobalPath(String globalPath) {
		GlobalPath = globalPath;
	}
	
	public void lineCr(String text, String design, String brackets) {
		String gl = "";
		int tlen = text.length();
		
		int padding = (lineWidth - tlen - ((design.length() + brackets.length())*2)) / 2;
		for(int i=0; i < padding; i++) {
			gl += design;
		}
		
		gl = gl + brackets + text + brackets + gl;
		System.out.println(gl);
	}

	public void welcome() {
		lineCr("=","=", "=");
		lineCr("Developer Name: Mohini Shinde"," ", " ");
		lineCr("Project Name: Virtual Key Repo"," ", " ");
		lineCr("=","=", "=");
	}

	public void dirPath() {
		System.out.println("\t Enter the absolute path of directory in which you have to perform the tasks.");
		System.out.print("\t Directory path => ");
		Scanner sc = new Scanner(System.in);
		if (sc.hasNext()) {
			String tempPath = sc.next();
			if (!(Pattern.matches("((\\/)+|(\\\\)+)", tempPath))) {
				if (new File(tempPath).exists() && new File(tempPath).isDirectory()) {
					setGlobalPath(tempPath);
					mainmenu();
				} else {
					System.out.println("Invalid path provided, try again..");
					dirPath();
				}
			} else {
				System.out.println("Invalid path provided, try again..");
				dirPath();
			}
		} else {
			System.out.println("Invalid path provided, try again..");
			dirPath();
		}
	}

	public void exit() {
		lineCr("Thank you for using the app.","=", "|");
	}
	
	public void commonOption(int caseC) {
		lineCr("=","=", "=");
		System.out.println("\t[1] Go to back menu\n\t[2] Exit");
		if(caseC > 2) System.out.print("\t[3] Different Search\n\t[4] Go to actions menu ");System.out.println("");
		
		switch(userInput(1, caseC)) {
		case 1:
			mainmenu();
			break;
		case 2:
			exit();
			break;
		case 3:
			lineCr("Search Folder","=", " | ");
			searchFolder();
			break;
		case 4:
			actionmennu();
			break;
		}
	}
	
	public void listfiles() {
		lineCr( getGlobalPath(),"=", " | ");
		
		File folder = new File(getGlobalPath());
		if(folder.list().length > 0) {
			try {
				Files.list(Paths.get(getGlobalPath())).sorted().forEach(s -> {
					if(s.toFile().isDirectory()) {
						System.out.print("\t [Folder] ");
					}else {
						System.out.print("\t [File] ");
					}

					System.out.print(s.toFile().getName());
					System.out.println("");
				});;
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("\t Directory is empty !");
		}
		
		//coomon options
		commonOption(2);
	}
	
	public void createFile(String FileName) {
		try {
		if(new File(FileName).createNewFile()) {
			System.out.println( "File succesfully created");
			actionmennu();
		}else {
			System.out.println("Unable to create a file . Please try again.");
			fileName(1);
		}
		}catch(IOException e) {
			System.out.println("Unable to create a file . Please try again.");
			fileName(1);
		}
	}
	
	public void deleteFile(String FileName) {
		if (new File(FileName).delete()) {
			System.out.println( "File deleted successfully");
			actionmennu();
		}
		else {
			System.out.println("Failed to delete file. Try again..");
			fileName(-1);
		}
	}
	
	public void confirmDel(String fileName) {
		System.out.println("Are you sure you want to delete " + fileName + " ?(y/n)");
		
		Scanner sc = new Scanner(System.in);
		if(sc.hasNext()) {
			String sel = sc.next();
			if(sel.equals("y") || sel.equals("n")) {
				if(sel.equals("y")) {
					deleteFile(getGlobalPath() + '/'+ fileName);
				}else {
					System.out.println("Operation aborted by user");
					actionmennu();
				}
			}else {
				System.out.println("Please provide valid opiton as (y)es or (n)o");
				confirmDel(fileName);
			}
		}else {
			System.out.println("Please provide valid option, try again..");
			confirmDel(fileName);
		}
	}
	
	public boolean checkFile(String fileName) {
		if(new File(getGlobalPath()+'/'+fileName).exists())return true;
		return false;
	}
	
	public void fileName(int action) {
		System.out.print("Enter a file name => ");
		Scanner sc = new Scanner(System.in);
		if(sc.hasNext()) {
			String fName = sc.next();
			if(!checkFile(fName)) {
				if(action > 0) {createFile(getGlobalPath()+'/'+fName);}
				else {System.out.println("File does not exist to delete"); actionmennu();}
			}else {
				if(action < 0) {
					confirmDel(fName);
				}
				else{
				System.out.println("File with the same name already exists. try with different name");
					fileName(action);
				}
			}
		}else{
			System.out.println("Invalid input, try again..");
			fileName(action);
		}
	}
	
	public String searchQ() {
		String q;
		System.out.print("What you want to search => ");
		Scanner sc = new Scanner(System.in);
		if(sc.hasNext()) {
			q = sc.next();
			return q;
		}
		return searchQ();
	}
	
	public void searchFolder() {
		String SearchQuery = searchQ();
		lineCr( "Showing search results for : " + SearchQuery,"=", " | ");
		int SearchResults = 0;
		File folder = new File(getGlobalPath());
		for (File fileEntry : folder.listFiles()) {
			if(!fileEntry.isDirectory()) {
				//check if has a match in name
				String TempFileName = fileEntry.getName().toString();
				if(TempFileName.matches("(.*)"+ SearchQuery + "(.*)")) {
					SearchResults++;
					System.out.println("\t [File] " + fileEntry.getName() );
				}				
			}else {
				String TempFileName = fileEntry.getName().toString();
				if(TempFileName.matches("(.*)"+ SearchQuery + "(.*)")) {
					SearchResults++;
					System.out.println("\t [Folder] " + fileEntry.getName() );
				}
			}
		}

		if(SearchResults < 1) {
			System.out.println("\t No files matching " + SearchQuery + " ");
		}
		
		commonOption(4);
	}
	
	
	public void actionmennu() {
		lineCr("Action Menu","=", " | ");
		lineCr("Directory: "+ getGlobalPath(),"=", " | ");
		System.out.println("\t [1] Create file");
		System.out.println("\t [2] Delete file");
		System.out.println("\t [3] Search folder");
		System.out.println("\t [4] Back to main menu");
		System.out.println("\t [5] Exit");
		lineCr("=","=", "=");
		switch(userInput(1, 5)) {
		case 1:

			lineCr("Create File","=", " | ");
			fileName(1);
			break;
		case 2:
			lineCr("Delete File","=", " | ");
			fileName(-1);
			break;
		case 3:
			lineCr("Search Folder","=", " | ");
			searchFolder();
			break;
		case 4:
			mainmenu();			
			break;
		case 5:
			exit();
		}
	}
	
	public void mainmenu() {
		lineCr("Main Menu","=", " | ");
		System.out.println("\t [1] List of files");
		System.out.println("\t [2] Actions for Files");
		System.out.println("\t [3] Update the Folder path");
		System.out.println("\t [4] Exit");
		lineCr("=","=", "=");
		switch (userInput(1, 4)) {
		case 1:
			lineCr("List of Files","=", " | ");
			listfiles();
			break;
		case 2:
			actionmennu();
			break;
		case 3:
			lineCr("Update folder path","=", " | ");
			dirPath();			
			break;
		case 4:
			exit();
		}
	}

	public void init() {
		welcome();
		dirPath();
	}

	public int userInput(int min, int max) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Please select option =>  ");
		if (sc.hasNextInt()) {
			int r = sc.nextInt();
			if (r < min || r > max) {
				System.out.println("Invalid selection, try again.");
				return userInput(min, max);
			} else {
				return r;
			}

		} else {
			System.out.println("Invalid selection, try again.");
			return userInput(min, max);
		}
	}
}

public class virtualkeyrepo{


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LockersRepo r = new LockersRepo();
		r.init();
	}

}

