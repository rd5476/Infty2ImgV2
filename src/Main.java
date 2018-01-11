
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.jsoup.Jsoup;


public class Main {

	
	public static void main(String [] arg) throws InvalidPasswordException, IOException {
		
		GenerateAllPixellSizes.init_generic_symbol_table(12);
//		
//		
		
		//////////////////////////////////////////////////////////////////////////////////////////
		File file = new File("AllCharacterFont12.pdf");
        FileInputStream inpStream = new FileInputStream(file);
        PDDocument documnet = PDDocument.load(inpStream);


        //Readfile
        read reader = new read(documnet);
        ArrayList<PageStructure> allPages = reader.readPdf();
        PageStructure allSym = allPages.get(0);
        //////////////////////////////////////////////////////////////////////////////////////////
		
		for(Integer k :allSym.pageCharacters.keySet()) {
			characterInfo ci = allSym.pageCharacters.get(k);
			
			String lbl = ci.value;
			//String unicd =
			String key ="#x"+Integer.toHexString(lbl.charAt(0) | 0x10000).substring(1) ;
			System.out.println(lbl+" - "+key);
			//System.out.println(lbl);
			WriteExpToPdf.sym2glyph.put(lbl, ci);
			//WriteExpToPdf.sym2glyph.put(key, ci);
		}
		ReadLG rlg  = new ReadLG(arg[0], arg[1]) ;
		rlg.extractLGData();
		
		//WriteExpToPdf e2p = new WriteExpToPdf("Output");
		//e2p.allExpressions = rlg.expressions;
		//e2p.createPDFs();
		
		RenderImage ri = new RenderImage();
		ri.allExpressions = rlg.expressions;
		ri.dest = arg[1];
		ri.sym2glyph = WriteExpToPdf.sym2glyph;
		System.out.println(WriteExpToPdf.sym2glyph.size());
		ri.ocr2unicode= WriteExpToPdf.ocr2unicode;
		ri.renderImages();
		
	}
}
