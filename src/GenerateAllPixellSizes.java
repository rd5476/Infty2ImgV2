import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.jsoup.Jsoup;

public class GenerateAllPixellSizes extends PDFTextStripper{
   public GenerateAllPixellSizes() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
   static Map<String,ArrayList<Float>> charDim = new HashMap();
   static { init_char_dim();}
   static void init_char_dim() {
		File gst = new File("uni2dim.csv");
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(gst);
		
			BufferedReader br = new BufferedReader(fileReader);
			
			String line = null;
			// if no more lines the readLine() returns null
			
			while ((line = br.readLine()) != null) {
				String [] tokens = line.split(" ");
				ArrayList<Float> temp = new ArrayList<>();
				temp.add(Float.parseFloat(tokens[1]));
				temp.add(Float.parseFloat(tokens[2]));
				charDim.put(tokens[0],temp);
			//	System.out.println(tokens[0]);
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
static PrintWriter writer;
static Map<String,ArrayList<String>> charData = new HashMap<>();
   static Map<String,Float> unitosize = new HashMap<>();
   static int fontsize;
   
   
   
   
   static void init_generic_symbol_table(int fs) {
	   	fontsize = fs;
	//   Map<String,String> generic_symbol_table = new HashMap<>();
	//	File gst = new File("src/generic_symbol_table.csv");
	   	String fontname = "Arial-Unicode-Italic";
	   	File gst = new File("src/ocr2uni.csv");
		FileReader fileReader = null;
		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage( page );
		PDFont font=null;
		PDPageContentStream contentStream = null;
		int counter =0;
		
		
		try {
			fileReader = new FileReader(gst);
		
			BufferedReader br = new BufferedReader(fileReader);
			
			String line = null;
			// if no more lines the readLine() returns null
			//line = br.readLine();
			contentStream = new PDPageContentStream(document, page);
			//contentStream.beginText();
			//contentStream.newLineAtOffset(100,1000);
			int offset =0;
			int offsety =0; 
			
			font = PDType0Font.load(document, new File("src/"+fontname+".ttf"));
		//	font = PDType0Font.load(document, new File("src/TIMESS__.ttf"));
			//font = PDType0Font.load(document, new File("src/times.ttf"));
			while ((line = br.readLine()) != null) {
				
				String [] tokens = line.split(",");
				if(tokens.length<2) continue;
				if(tokens[1].charAt(0)!='#')continue;
				ArrayList<String> temp = new ArrayList();
				temp.add(tokens[1]);
				charData.put(tokens[0],temp);
				//charData.get(tokens)
				contentStream.beginText();
//				if(tokens[3].length()>1) {
//					font = PDType1Font.SYMBOL;
//				}else {
//					font = PDType1Font.COURIER;
//				}
				String result=  Jsoup.parse("&"+tokens[1]).text();
				
				contentStream.setFont(font, fontsize);
		//	System.out.println(tokens[0]+" - - "+tokens[1]+" -- "+result);
				
				try {
						contentStream.newLineAtOffset(40+offset,700+offsety);
						
						contentStream.showText(result);
						counter++;
					}catch(IllegalArgumentException iae) {
						System.out.println(tokens[0]);
						iae.printStackTrace();
						
//						if(font==PDType1Font.SYMBOL) {
//							font = PDType1Font.COURIER;
//						}else if(font == PDType1Font.COURIER) {
//							font = PDType1Font.SYMBOL;
//							}
					/*	contentStream.newLineAtOffset(40+offset,700+offsety);
						contentStream.setFont(font, fontsize);
						try {
						contentStream.showText(result);//tokens[1]);
						
						}catch(Exception e) {
							e.printStackTrace();
							System.out.println("Kyu----------------------------------------------------");
						}*/
						/*try{}catch(IllegalArgumentException iae1) {
							iae1.printStackTrace();
							System.out.println(font+"--"+tokens[3]);
							
						}*/
					}
				
				offset +=15;
				if(offset>500) {
					offset = 0;
					offsety -=15;
				}
				
				contentStream.endText();
			}	
			  
			  contentStream.close();
			  System.out.println(counter);
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			  try {
				contentStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		try {
			
			document.save( fontname+".pdf");
			document.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
   
   
   public static void dispInfo() throws IOException	{
		PDDocument document = null;
		String fileName = "AllCharacterFont12.pdf";
		 writer = new PrintWriter("uni2width"+fontsize+".csv", "UTF-8");
		try {
			document = PDDocument.load( new File(fileName) );
			PDFTextStripper stripper = new GenerateAllPixellSizes();
           stripper.setSortByPosition( true );
           stripper.setStartPage( 0 );
           stripper.setEndPage( document.getNumberOfPages() );

           Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
           stripper.writeText(document, dummy);
		}
		finally {
			if( document != null ) {
				document.close();
			}
		}
		 writer.close();
	}

	/**
	 * Override the default functionality of PDFTextStripper.writeString()
	 */
   @Override
	protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
	  
	   //writer.println("Hello");
	   for (TextPosition text : textPositions) {
			float width = text.getWidthDirAdj();
			
		/*	System.out.println(text.getUnicode()+ " [(X=" + text.getXDirAdj() + ",Y=" +
					text.getYDirAdj() + ") height=" + text.getYDirAdj() + " width=" +
					text.getWidthDirAdj() + "]");
			System.out.println(text.getEndX()+" - "+text.getEndY()+" - "+text.getX()+" - "+text.getY());*/
			String key ="#x"+Integer.toHexString(text.getUnicode().charAt(0) | 0x10000).substring(1) ;
			
			unitosize.put(key, width);
			//System.out.println(key+" - "+width);
			writer.println(key+","+width);
			
		}
	  
	}
   
   static float scaleFontSize(String unicode,float imageWidth,float baseRatio) {
	  // System.out.println(unicode);
	   float stdWidth=1;
	   try {
	    stdWidth = GenerateAllPixellSizes.charDim.get(unicode.toLowerCase()).get(1) ;
	   }catch(Exception e){
		   System.out.println(unicode +" #################################### "+ stdWidth);
		   e.printStackTrace();
	   }
	 //  System.out.println(imageWidth/stdWidth); 
	   
		   return (12 *( (imageWidth/stdWidth)/baseRatio));
		   
   }

}
