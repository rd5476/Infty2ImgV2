import java. io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.net.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;
public class ReadLG {
String source;
String dest;
ArrayList<Expression> expressions;

public ReadLG(String source, String dest) {
	super();
	this.source = source;
	this.dest = dest;
	this.expressions = new ArrayList<>();
}

	 void extractLGData() {
		List<File> filesInFolder = null;
		try {
			filesInFolder = Files.walk(Paths.get(this.source))
			        .filter(Files::isRegularFile)
			        .map(Path::toFile)
			        .collect(Collectors.toList());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(filesInFolder== null) {
			System.err.println("No files in source directory");
		}
		//////////////////////////////////////////////////////////////////////////////////
		//Read Each file line by line
		/////////////////////////////////////////////////////////////////////////////////
		for(File file: filesInFolder){
			Expression exp = new Expression("");
			Map<String,Float> map=new HashMap<>();  
			int minExpX=999999;
			int maxExpX=0;
			int minExpY=999999;
			int maxExpY=0;
			FileReader fileReader = null;
			try {
				fileReader = new FileReader(file);
			
				BufferedReader br = new BufferedReader(fileReader);
				
				String line = null;
				// if no more lines the readLine() returns null
				int counter=0;
				int flag=0;
				while ((line = br.readLine()) != null) {
					// reading lines until the end of the file
					
					if(line.contains("PRIMITAVES")) continue; // No need to read the file
					line = line.replace(',', ' ');
					
					String[] elements = line.split("(\\s)+");
					
					
					if(elements[0].equals("#Exp")) {
						exp.expId = elements[1];
					//	System.out.println(exp.expId);
		
					}
					else if(elements[0].equals("#sym")){
						
					
						Symbol temp = new Symbol();
						try {
							temp.id =( elements[1]);
							temp.lowY = Integer.valueOf(elements[2]);
							if(temp.lowY<minExpY) minExpY = temp.lowY;
							temp.lowX =Integer.valueOf( elements[3]);
							if(temp.lowX<minExpX) minExpX = temp.lowX;
							temp.highY = Integer.valueOf(elements[4]);
							if(temp.highY>maxExpY) maxExpY = temp.highY;
							temp.highX = Integer.valueOf(elements[5]);
							if(temp.highX>maxExpX) maxExpX = temp.highX;
							exp.objects.add(temp);
						}catch(Exception e){
							System.out.println("Exception - "+line);
							e.printStackTrace();
						}
						
					}else if (elements[0].equals("O")) {
						assert(elements[1].equals(exp.objects.get(counter).id));
						try {
						exp.objects.get(counter).updateDim();
						exp.objects.get(counter++).label =elements[2];
						}catch(Exception e) {}
					}
					/*else if (elements[0].equals("R")) {
						if(flag==0) {
							flag=1;
							map.put(elements[1], (float) 1);
						}
						
						if(elements[3].equals("HORIZONTAL")) {
							map.put(elements[2],map.get(elements[1]));
						}
						else if(elements[3].equals("RSUB")) {
							map.put(elements[2],map.get(elements[1])*0.7f);
						}
						else if(elements[3].equals("RSUP")) {
							map.put(elements[2],map.get(elements[1])*0.7f);
						}
					}*/
					
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			exp.expHeight = maxExpY - minExpY;
			exp.expWidth =  maxExpX - minExpX;
			this.expressions.add(exp);
		}
		
	}

}

class Expression{
	String expId;
	int expHeight,expWidth;
	
	ArrayList<Symbol> objects ;
	public Expression() {}
	public Expression(String expId) {
		super();
		this.expId = expId;
		this.objects = new ArrayList<>();
	}	
	
}
class Symbol{
	String id;
	int lowX,lowY,highX,highY;
	
	String label;
	float symHeigh,symWidth;
	public Symbol() {}
	public Symbol(String id, int lowX, int lowY, int highX, int highY, String label) {
		super();
		this.id = id;
		this.lowX = lowX;
		this.lowY = lowY;
		this.highX = highX;
		this.highY = highY;
		this.label = label;
		this.symHeigh = highY - lowY;
		this.symWidth = highX - lowX;
	}
	void updateDim() {
		this.symHeigh = highY - lowY;
		this.symWidth = highX - lowX;
	}
}