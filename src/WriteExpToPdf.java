import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType3CharProc;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.apache.pdfbox.pdmodel.font.encoding.Type1Encoding;
import org.jsoup.Jsoup;
import org.omg.IOP.Encoding;


public class WriteExpToPdf {
	static HashMap<String, characterInfo> sym2glyph = new HashMap<>();
	static Map<String,String> generic_symbol_table =  new HashMap<>();
	static Map<String,String> ocr2unicode = new HashMap<>();
	static Map<String,String> unicode2ocr = new HashMap<>();
	static Map<String,String> english2digit = new HashMap<>();
	ArrayList<Expression> allExpressions = null;
	String destination;
	public WriteExpToPdf() {}
	static {
		init_generic_symbol_table();
		//init_ocr2unicode();
		english2digit.put("zero", "0");
		english2digit.put("one", "1");
		english2digit.put("two", "2");
		english2digit.put("three", "3");
		english2digit.put("four", "4");
		english2digit.put("five", "5");
		english2digit.put("six", "6");
		english2digit.put("seven", "7");
		english2digit.put("eight", "8");
		english2digit.put("nine", "9");
	}
	public WriteExpToPdf(String dest) {
		this.destination = dest;
	}
	
	static void init_ocr2unicode(){
		File gst = new File("src/ocr2uni.csv");
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(gst);
		
			BufferedReader br = new BufferedReader(fileReader);
			
			String line = null;
			// if no more lines the readLine() returns null
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				String [] tokens = line.split(",");
				if(tokens.length>1) {
				generic_symbol_table.put(tokens[0], tokens[1]);
				
				}
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static void init_generic_symbol_table() {
	
		File gst = new File("src/ocr2uni.csv");
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(gst);
		
			BufferedReader br = new BufferedReader(fileReader);
			
			String line = null;
			// if no more lines the readLine() returns null
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				String [] tokens = line.split(",");
				
				if(tokens.length>1) {
					
				generic_symbol_table.put(tokens[0], tokens[1]);
				ocr2unicode.put(tokens[1], tokens[0]);
				}
				
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Load Complete");
	}
	
	@SuppressWarnings("static-access")
	void createPDFs() {
		
		
		for(Expression exp : allExpressions) {
			
			// Create a document and add a page to it
			exp.objects.sort((o1, o2) -> ((Integer)(o1.lowX)).compareTo(o2.lowX));
			PDDocument document = new PDDocument();
			PDPage page = new PDPage();
			document.addPage( page );

			// Create a new font object selecting one of the PDF base fonts
			PDFont font = null;
			try {
				font = PDType0Font.load(document, new File("src/times.ttf"));
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			PDPageContentStream contentStream = null;
			try {
				contentStream = new PDPageContentStream(document, page);
				// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
				
				
				
			//	contentStream.moveTo(10, 10);
				
				
				//contentStream.newLineAtOffset(100, 400.5f);
				
				//contentStream.showText(exp.expId);
				float offsetX = 10;
				float offsetY = 700;
				
				float baseX = exp.objects.get(0).lowX;
				String baseChar = exp.objects.get(0).label;
				
			//	float stdWidth =     unitosize.get(unicode); 
				//baseChar = this.generic_symbol_table.get(baseChar.toLowerCase());
				String baseUnicode = this.generic_symbol_table.get(baseChar.toLowerCase());
				System.out.println(baseChar +"*****************"+baseUnicode);
				//	String baseUnicode = "#x"+Integer.toHexString(baseChar.charAt(0) | 0x10000).substring(1) ;
				float baseImageWidth= exp.objects.get(0).symHeigh;
				float fontSzPDFSpace = GenerateAllPixellSizes.charDim.get(baseUnicode.toLowerCase()).get(1);
				characterInfo cInfo = sym2glyph.get(baseUnicode.toLowerCase());
				float baseRatio = baseImageWidth/fontSzPDFSpace;
				//float factor = 0.1f;//12/baseFont;
				contentStream.newLineAtOffset(offsetX, offsetY);
				for(Symbol sym : exp.objects) {
					String result="";
					
				//	contentStream.moveTo(offsetX+(sym.lowX/baseRatio), offsetY+(exp.expHeight-sym.highY)/baseRatio);
					
					
					//contentStream.
					//result = this.generic_symbol_table.get(sym.label);
					if(sym.label.length()>1)sym.label = sym.label.toLowerCase();
					String symUnicode = this.generic_symbol_table.get(sym.label);
					System.out.println("symbol -> "+sym.label+" - "+ symUnicode);
					//String symUnicode = "#x"+Integer.toHexString(result.charAt(0) | 0x10000).substring(1) ;
					
					float newFontSize = GenerateAllPixellSizes.scaleFontSize(symUnicode, sym.symHeigh, baseRatio);
					
					/*System.out.println(newFontSize +"--"+sym.symHeigh+"--"+ GenerateAllPixellSizes.charDim.get(symUnicode).get(1)
							+" -- ");
					*/
					try {
						float w = sym.highX-sym.lowX;
						float h = sym.highY-sym.lowY;
						
						contentStream.addRect(offsetX+(sym.lowX/baseRatio), offsetY+(exp.expHeight-sym.highY )/baseRatio, w/baseRatio, h/baseRatio);
						contentStream.setLineWidth((float)0.);
		                contentStream.setStrokingColor(Color.PINK);
		                contentStream.stroke();
		                contentStream.moveTo(offsetX+(sym.lowX/baseRatio), offsetY+(exp.expHeight-sym.highY)/baseRatio);
		               // contentStream.lineTo(offsetX+(sym.lowX/baseRatio), offsetY+(exp.expHeight-sym.highY)/baseRatio);
		             
		              
		                contentStream.beginText();
					contentStream.setFont(font, newFontSize);//
				//	contentStream.newLineAtOffset(offsetX+(sym.lowX/baseRatio), offsetY+(exp.expHeight-sym.highY)/baseRatio);
					
					//contentStream.d
					result=  Jsoup.parse("&"+symUnicode).text();
					
					}catch(Exception e) {
						System.out.println("symbol "+sym.label);
						e.printStackTrace();
					}
				//	System.out.println("symbol -> "+sym.label+" - "+result+" - "+sym.symHeigh +" - "+sym.symWidth);
					try {
					
					//contentStream.showText(result);
					}catch(IllegalArgumentException iae) {
						System.out.println("symbol "+sym.label);
						iae.printStackTrace();
					}
					
				
					contentStream.endText();
					
				}
				contentStream.close();
				//contentStream.drawString( "Hello World" );
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(this.generic_symbol_table.get("bigleftpar"));
				e.printStackTrace();
			} 

		

			// Make sure that the content stream is closed:
			

			// Save the results and ensure that the document is properly closed:
			try {
				System.out.println(exp.expId);
				document.save( this.destination+"/"+exp.expId+".pdf");
				document.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Done");
			
		}
	}  
	
}
