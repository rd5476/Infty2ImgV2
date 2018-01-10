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
		System.out.println(sym2glyph.size());
		for(Expression exp : allExpressions) {
			exp.objects.sort((o1, o2) -> ((Integer)(o1.lowX)).compareTo(o2.lowX));
			
			int expWidth,expHeight;
			expWidth = exp.expWidth;
			expHeight = exp.expHeight;
		//	BufferedImage image = new BufferedImage(5000,5000, BufferedImage.TYPE_INT_BGR);
			BufferedImage image = new BufferedImage(expWidth,expHeight, BufferedImage.TYPE_INT_BGR);
			Graphics2D graphic = image.createGraphics();
			RahulDrawGlyph.graphics = graphic;
			ArrayList<DrawSymbol> expressionComponents = new ArrayList<>();
			for(Symbol s : exp.objects){
				String charId =  WriteExpToPdf.generic_symbol_table.get(s.label);
				if(charId==null) {
					 charId =  WriteExpToPdf.generic_symbol_table.get(s.label.toLowerCase());
				}
				charId=Jsoup.parse("&"+charId).text();
		//		System.out.println(s.label+" "+charId);
				characterInfo cInfo = sym2glyph.get(charId);
				if(cInfo!=null) {
				drawGlyph dg = cInfo.glyph;
				//RahulDrawGlyph rdg = new RahulDrawGlyph(dg.glyphPath, dg.ch, dg.unicode, dg.fontSize, dg.EmSqaure);
				System.out.println("startX "+ s.lowX);//*(s.symWidth/cInfo.boundingBox.width));
				System.out.println("startY "+s.lowY);//*(s.symHeigh/cInfo.boundingBox.height));
				//DrawSymbol ds = new DrawSymbol(dg, s.lowX*(s.symWidth/cInfo.boundingBox.width), s.lowY*(s.symHeigh/cInfo.boundingBox.height), 12);
				DrawSymbol ds = new DrawSymbol(dg, s.lowX, s.lowY, 12);
				ds.height = s.symHeigh;///cInfo.boundingBox.height;
				ds.width = s.symWidth;///cInfo.boundingBox.width;
				Rectangle2D r2d = cInfo.glyph.glyphPath.getBounds2D();
				ds.obj.fontFactor = s.symHeigh/r2d.getHeight();//cInfo.boundingBox.width/s.symWidth;
			//	ds.obj.fontFactorX =s.symWidth/r2d.getHeight();//s.symWidth/r2d.getWidth();
			//	ds.obj.fontFactorY = s.symWidth/r2d.getWidth();//s.symWidth/r2d.getHeight();
				expressionComponents.add(ds);
			//	ds.obj.draw(type.Normal, graphic);
				
						//rdg.draw(type.Normal, graphic);
				//GeneralPath gp = cInfo.glyph.glyphPath;
				
				//this.scaleGlyph(cInfo, s);
				}else {System.out.println(s.label);
					}
				
			
			}
	///////////////////////////////////////////////////////////////////////////////////////////////////////		
			expressionComponents.sort((o1, o2) -> ((Float)(o1.startX)).compareTo(o2.startX));
			float base=0;
			float baseY=0;
			 double baseFont = 12;
			// double ff =  
			for(DrawSymbol a :expressionComponents ) {
	        	//System.out.println(a.startX);
				
	        	a.startX -= base;
	        	Rectangle2D r2d = a.obj.glyphPath.getBounds2D();
	        	a.startY -= baseY;
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
			AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
//	        System.out.println(tx.toString());
//	      //  System.out.println("Transform = "+ (int)(maxY.startY+maxY.height));
	        tx.translate(0, -(int)(expHeight));
//	        System.out.println(tx.toString());
	        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	        
	        image = op.filter(image, null);
			File output = new File(dest+"/output"+exp.expId+".png");
	        
	        ImageIO.write(image, "png", output);
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
