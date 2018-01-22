import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;




public class RenderImage {
	
	
	ArrayList<Expression> allExpressions = null;
	static HashMap<String, characterInfo> sym2glyph = new HashMap<>();
	static Map<String,String> ocr2unicode= new HashMap<>();
	String dest;
	String source;
	
	void renderImages() throws IOException {
		/*
		 * Run for all the expressions loaded
		 */
		for(Expression exp : allExpressions) {
			
			//Sort on min X coordinate
			exp.objects.sort((o1, o2) -> ((Integer)(o1.lowX)).compareTo(o2.lowX));
			
			int expWidth,expHeight;
			expWidth = exp.expWidth;
			expHeight = exp.expHeight;
			expWidth = Math.max(expWidth,1);
			expHeight = Math.max(expHeight, 1);
			
			//Create Image for each expression
			BufferedImage image = new BufferedImage(expWidth+10,expHeight, BufferedImage.TYPE_INT_BGR);
			Graphics2D graphic = image.createGraphics();
			
			
			RahulDrawGlyph.graphics = graphic;
			
			
			// List of all components
			ArrayList<DrawSymbol> expressionComponents = new ArrayList<>();
			for(Symbol s : exp.objects){
				ArrayList< String> symbols =  WriteExpToPdf.multiple_symbols.get(s.label);
				if(symbols==null)
					symbols =  WriteExpToPdf.multiple_symbols.get(s.label.toLowerCase());
				//Get unicode for the label read from .lg file
				String charId="";
				characterInfo cInfo = null;
				if(symbols==null) {
				//	String key1 ="#x"+Integer.toHexString(charId.charAt(0) | 0x10000).substring(1) ;
					if(s.label.equals("fractionalLine")) {
						symbols = new ArrayList<>();
					}else {
					System.out.println("No symbol matched Exception : "+s.label+" - ");//+charId+" - "+key1);
					continue;
					}
				}
				for(String cId: symbols) {
					charId = cId;
					//charId =  WriteExpToPdf.generic_symbol_table.get(s.label);
	//				if(charId==null) {
	//					 charId =  WriteExpToPdf.generic_symbol_table.get(s.label.toLowerCase());
	//				}
					charId=Jsoup.parse("&"+charId).text();
				//	String key ="#x"+Integer.toHexString(s.label.charAt(0) | 0x10000).substring(1) ;
					// Get character info based 
			//		System.out.println("Working : "+s.label+" "+charId);
					cInfo = sym2glyph.get(charId);
					if(cInfo!=null)break;
				}
				if(cInfo!=null) {
					//for(String cc:symbols) System.out.println("Exception list "+cc);
					drawGlyph dg = cInfo.glyph;
				
				
				
					DrawSymbol ds = new DrawSymbol(dg, s.lowX, s.lowY, 12);
					ds.height = s.symHeigh;///cInfo.boundingBox.height;
					ds.width = s.symWidth;///cInfo.boundingBox.width;
					Rectangle2D r2d = cInfo.glyph.glyphPath.getBounds2D();
					double wd =ds.obj.maxX-ds.obj.minX;
					double ht = ds.obj.maxY-ds.obj.minY;
					//Correct version
					ds.obj.fontFactor = s.symHeigh/r2d.getHeight();//cInfo.boundingBox.width/s.symWidth;
					
//					System.out.println("Scaling factors : "+r2d.getHeight()+" - "+ds.height+" "+s.symHeigh);
//					ds.obj.fontFactor = s.symHeigh/ds.height;
//						ds.obj.fontFactorX =s.symWidth/wd; //r2d.getWidth();//s.symWidth/r2d.getWidth();
//						ds.obj.fontFactorY = s.symHeigh/ht;//r2d.getHeight();//s.symWidth/r2d.getHeight();
					expressionComponents.add(ds);
					//	ds.obj.draw(type.Normal, graphic);
				
					//rdg.draw(type.Normal, graphic);
					//GeneralPath gp = cInfo.glyph.glyphPath;
				
					//this.scaleGlyph(cInfo, s);
				}else {
					if(s.label.equals("fractionalLine")) {
						graphic.setStroke(new BasicStroke(10));
			        	graphic.drawLine((int)(s.lowX),(int)(expHeight-s.highY),(int)(s.highX),(int)(expHeight-s.highY));
					}
				//	for(String cc:symbols) System.out.println("Exception list "+cc);
				//	String key1 ="#x"+Integer.toHexString(charId.charAt(0) | 0x10000).substring(1) ;
					System.out.println("Exception : "+s.label+" - ");
				}
				
			
			}
	///////////////////////////////////////////////////////////////////////////////////////////////////////		
	//  Done with glyph in char info extraction for expression components		
	//  Start rendering 			
	//////////////////////////////////////////////////////////////////////////////////////////////////////		
			// 
			expressionComponents.sort((o1, o2) -> ((Float)(o1.startX)).compareTo(o2.startX));
//			float base=0;
//			float baseY=0;
//			double baseFont = 12;
  ///////////////////////////////////////////////////////////////////////////////////////////////////////
	//    Scale down glyphs
	//    Draw on graphic2d object
  //////////////////////////////////////////////////////////////////////////////////////////////////////			
			for(DrawSymbol a :expressionComponents ) {
				
	       // 	a.startX -= base;
	       // 	Rectangle2D r2d = a.obj.glyphPath.getBounds2D();
	       // 	a.startY -= baseY;
	      //  	System.out.println(a.obj.unicode+"--"+a.startX+"--"+a.startY);
	        	//a.obj.minX-=base;
	        	a.obj.offset = a.startX;
	        	a.obj.offsetY = expHeight -(a.startY+ a.height);
	        	        	//a.obj.fontFactor = a.fontSize/baseFont;
	        	//System.out.println(a.startY);
	        //	a.obj.maxX-=base;
	        	a.obj.draw(type.Normal,graphic);
	        	
	        }
			
			
			
	////////////////////////////////////////////////////////////////////////////////////////////////////////		
    //		Flip the image
	////////////////////////////////////////////////////////////////////////////////////////////////////////      
			AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
	        tx.translate(0, -(int)(expHeight));

	        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	        
////////////////////////////////////////////////////////////////////////////////////////////////////////
//		Write the image to the file
////////////////////////////////////////////////////////////////////////////////////////////////////////	        
	        image = op.filter(image, null);
			File output = new File(dest+"/"+exp.expId+".png");
	        
	        ImageIO.write(image, "png", output);
	        System.out.println("Done :"+exp.expId);
		}
	}
	void scaleGlyph(characterInfo ci,Symbol s) throws IOException {
		GeneralPath gp = ci.glyph.glyphPath;
		float ht = s.symHeigh;
		float wd = s.symWidth;
		Rectangle2D bbx  = gp.getBounds2D();
		double ght = bbx.getHeight();
		double gwd = bbx.getWidth();
		AffineTransform tx =AffineTransform.getTranslateInstance(s.lowX, -bbx.getY()+s.lowY);
	//	tx.translate(0, bbx.getY());
		gp.transform(tx);
		tx= AffineTransform.getScaleInstance(wd/gwd, ht/ght);
		gp.transform(tx);		
			
		//graphic.fill(gp);
		
		
	}
}
