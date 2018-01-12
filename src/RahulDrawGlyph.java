

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

public class RahulDrawGlyph extends drawGlyph{

	 double offset;
	 double offsetY;
	 double fontFactor;
	 double fontFactorX;
	 double fontFactorY;
	 static Graphics2D graphics;
	RahulDrawGlyph(GeneralPath glyphPath, int ch, String unicode, float fontSize, int EmSqaure) {
		super(glyphPath, ch, unicode, fontSize, EmSqaure);
		//System.out.println(super.unicode);
		// TODO Auto-generated constructor stub
	}
	
	  public void draw(type t, Graphics2D g){
	        coordinates();
	        graphics = g;
	        BoxCoord();
	        if(t == type.Adjusted)
	            adjustCoordResolution();
	        else
	            adjustCoord();

	    }
	
	/*public void adjustCoord(){
	
        GeneralPath newPath = new GeneralPath();
        System.out.println("All MinY "+minX+" MinX "+minY);
        if(minY>0) {
        	minY=0;
        }
        for(int i=0;i<op.size();i++) {
            switch (op.get(i)) {
                case PathIterator.SEG_CLOSE:
                    newPath.closePath();
                    break;
                case PathIterator.SEG_CUBICTO:
                    newPath.curveTo((allx1.get(i)+Math.abs(minX))*fontFactor+offset,
                            (ally1.get(i)+Math.abs(minY))*fontFactor+offsetY,
                            (allx2.get(i)+Math.abs(minX))*fontFactor+offset,
                            (ally2.get(i)+Math.abs(minY))*fontFactor+offsetY,
                            (allx3.get(i)+Math.abs(minX))*fontFactor+offset,
                            (ally3.get(i)+Math.abs(minY))*fontFactor+offsetY);
                    break;

                case PathIterator.SEG_LINETO:
                    newPath.lineTo((allx1.get(i)+Math.abs(minX))*fontFactor+offset,
                            ((ally1.get(i)+Math.abs(minY)))*fontFactor+offsetY);
                    break;

                case PathIterator.SEG_MOVETO:
                    newPath.moveTo((allx1.get(i)+Math.abs(minX))*fontFactor+offset,
                    		((ally1.get(i)+Math.abs(minY)))*fontFactor+offsetY);
                    break;

                case PathIterator.SEG_QUADTO:
                    newPath.quadTo((allx1.get(i)+Math.abs(minX))*fontFactor+offset,
                            ((ally1.get(i)+Math.abs(minY)))*fontFactor+offsetY,
                            (allx2.get(i)+Math.abs(minX))*fontFactor+offset,
                            (ally2.get(i)+Math.abs(minY))*fontFactor+offsetY);
                    break;

              
            }

        }
       drawGlyph temp = new drawGlyph(newPath,ch,unicode,fontSize,EmSqaure);
        temp.paint(graphics);



    }
*/
	  
	  public void adjustCoord(){
			
	        GeneralPath newPath = new GeneralPath();
	      
	      
	        for(int i=0;i<op.size();i++) {
	            switch (op.get(i)) {
	                case PathIterator.SEG_CLOSE:
	                    newPath.closePath();
	                    break;
	                case PathIterator.SEG_CUBICTO:
	                    newPath.curveTo((allx1.get(i)-minX)*fontFactor+offset,
	                            (ally1.get(i)-minY)*fontFactor+offsetY,
	                            (allx2.get(i)-minX)*fontFactor+offset,
	                            (ally2.get(i)-minY)*fontFactor+offsetY,
	                            (allx3.get(i)-minX)*fontFactor+offset,
	                            (ally3.get(i)-minY)*fontFactor+offsetY);
	                    break;

	                case PathIterator.SEG_LINETO:
	                    newPath.lineTo((allx1.get(i)-minX)*fontFactor+offset,
	                            ((ally1.get(i)-minY))*fontFactor+offsetY);
	                    break;

	                case PathIterator.SEG_MOVETO:
	                    newPath.moveTo((allx1.get(i)-minX)*fontFactor+offset,
	                    		((ally1.get(i)-minY))*fontFactor+offsetY);
	                    break;

	                case PathIterator.SEG_QUADTO:
	                    newPath.quadTo((allx1.get(i)-minX)*fontFactor+offset,
	                            ((ally1.get(i)-minY))*fontFactor+offsetY,
	                            (allx2.get(i)-minX)*fontFactor+offset,
	                            (ally2.get(i)-minY)*fontFactor+offsetY);
	                    break;

	              
	            }

	        }
	       drawGlyph temp = new drawGlyph(newPath,ch,unicode,fontSize,EmSqaure);
	        temp.paint(graphics);



	    }


	 /* public void adjustCoord(){
			
	        GeneralPath newPath = new GeneralPath();
	      
	      
	        for(int i=0;i<op.size();i++) {
	            switch (op.get(i)) {
	                case PathIterator.SEG_CLOSE:
	                    newPath.closePath();
	                    break;
	                case PathIterator.SEG_CUBICTO:
	                    newPath.curveTo((allx1.get(i)-minX)*fontFactorX+offset,
	                            (ally1.get(i)-minY)*fontFactorY+offsetY,
	                            (allx2.get(i)-minX)*fontFactorX+offset,
	                            (ally2.get(i)-minY)*fontFactorY+offsetY,
	                            (allx3.get(i)-minX)*fontFactorX+offset,
	                            (ally3.get(i)-minY)*fontFactorY+offsetY);
	                    break;

	                case PathIterator.SEG_LINETO:
	                    newPath.lineTo((allx1.get(i)-minX)*fontFactorX+offset,
	                            ((ally1.get(i)-minY))*fontFactorY+offsetY);
	                    break;

	                case PathIterator.SEG_MOVETO:
	                    newPath.moveTo((allx1.get(i)-minX)*fontFactorX+offset,
	                    		((ally1.get(i)-minY))*fontFactorY+offsetY);
	                    break;

	                case PathIterator.SEG_QUADTO:
	                    newPath.quadTo((allx1.get(i)-minX)*fontFactorX+offset,
	                            ((ally1.get(i)-minY))*fontFactorY+offsetY,
	                            (allx2.get(i)-minX)*fontFactorX+offset,
	                            (ally2.get(i)-minY)*fontFactorY+offsetY);
	                    break;

	              
	            }

	        }
	       drawGlyph temp = new drawGlyph(newPath,ch,unicode,fontSize,EmSqaure);
	        temp.paint(graphics);



	    }
*/
}
