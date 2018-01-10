/*import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*//**
 * This is an example on how to get the x/y coordinates and size of each character in PDF
 *//*
public class GetCharLocationAndSize extends PDFTextStripper {
	static Map<String,Float> unitosize = new HashMap<>();
	public GetCharLocationAndSize() throws IOException {
	}

	*//**
	 * @throws IOException If there is an error parsing the document.
	 *//*
	public static void dispInfo() throws IOException	{
		PDDocument document = null;
		String fileName = "AllCharacterFont12.pdf";
		try {
			document = PDDocument.load( new File(fileName) );
			PDFTextStripper stripper = new GetCharLocationAndSize();
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
	}

	*//**
	 * Override the default functionality of PDFTextStripper.writeString()
	 *//*
	@Override
	protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
		for (TextPosition text : textPositions) {
			float width = text.getWidthDirAdj();
			
			System.out.println(text.getUnicode()+ " [(X=" + text.getXDirAdj() + ",Y=" +
					text.getYDirAdj() + ") height=" + text.getYDirAdj() + " width=" +
					text.getWidthDirAdj() + "]");
			System.out.println(text.getEndX()+" - "+text.getEndY()+" - "+text.getX()+" - "+text.getY());
			String key ="#x"+Integer.toHexString(text.getUnicode().charAt(0) | 0x10000).substring(1) ;
			unitosize.put(key, width);
			System.out.println(key+" - "+width);
		}

	}
	
	static void getCharInfo() {
		
		PDDocument document = null;
		String fileName = "AllCharacterFont12.pdf";
		try {
			document = PDDocument.load( new File(fileName) );
			
			}catch(Exception e) {
				e.printStackTrace();
			}
	}
}*/