//
//package api.pdf;
///**
//* ===========================================
//* Java Pdf Extraction Decoding Access Library
//* ===========================================
//*
//* Project Info:  http://www.jpedal.org
//* (C) Copyright 1997-2008, IDRsolutions and Contributors.
//*
//* 	This file is part of JPedal
//*
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//
//*
//* ---------------
//* PdfPanel.java
//* ---------------
//*/
//import java.awt.*;
//// <start-me>
//import java.awt.dnd.DropTarget;
//// <end-me>
//
//import java.awt.font.GlyphVector;
//import java.awt.geom.AffineTransform;
//import java.awt.geom.Area;
//import java.awt.geom.Rectangle2D;
//import java.awt.geom.RoundRectangle2D;
//import java.awt.image.BufferedImage;
//import java.util.ArrayList;
//
//import javax.swing.border.Border;
//
//import org.jpedal.io.ObjectStore;
//
//import org.jpedal.objects.PdfPageData;
//
////<start-adobe><start-ulc><start-thin><start-me>
//import org.jpedal.examples.simpleviewer.gui.SwingGUI;
//import org.jpedal.examples.simpleviewer.gui.swing.SwingMouseSelector;
////<end-me><end-thin><end-ulc><end-adobe>
//
//import org.jpedal.objects.PrinterOptions;
//import org.jpedal.objects.layers.PdfLayerList;
//
//import org.jpedal.objects.acroforms.rendering.AcroRenderer;
//
//import org.jpedal.render.*;
//
//import org.jpedal.text.TextLines;
//import org.jpedal.external.Options;
//import org.jpedal.external.RenderChangeListener;
//
//import javax.swing.*;
//import org.jpedal.Display;
//import org.jpedal.PageOffsets;
//import org.jpedal.PdfDecoder;
//import org.jpedal.SingleDisplay;
//
//
///**
// * Do not create an instance of this class - provides GUI functionality for
// * PdfDecoder class to extend
// */
//public class PdfPanel extends JPanel{
//
//    BufferedImage previewImage=null;
//
//    String previewText;
//
//    //Animation enabled (currently just turnover in facing)
//    public boolean turnoverOn =  true;
//
//    //Display the first page separately in Facing mode
//    public boolean separateCover = true;
//
//    //Darker background, glowing pages
//    public boolean useNewGraphicsMode = true;
//
//    //Show onscreen mouse dragged box
//    public boolean showMouseBox = false;
//
//    //custom class for flagging painting
//    RenderChangeListener customRenderChangeListener=null;
//
//	private static final long serialVersionUID = -5480323101993399978L;
//
//    double indent=0;
//
//    /**allow user to displace display*/
//    protected int userOffsetX=0, userOffsetY=0,userPrintOffsetX=0, userPrintOffsetY=0;
//
//    //store cursor position for facing drag
//    protected int facingCursorX=10000, facingCursorY=10000;
//
//   // debug ULC and printing
//
//    protected PdfLayerList layers;
//
//    /** Holds the x,y,w,h of the current highlighted image, null if none */
//	int[] highlightedImage = null;
//
//	/** Enable / Disable Point and Click image extraction */
//	private boolean ImageExtractionAllowed = true;
//
//	protected Display pages;
//
//	/**default renderer for acroforms*/
//	protected AcroRenderer formRenderer;//=new DefaultAcroRenderer();
//
//	/**
//	 * The colour of the highlighting box around the text
//	 */
//	public static Color highlightColor = new Color(10,100,170);
//
//	/**
//	 * page colour for PDF background
//	 */
//	public Color pageColor=Color.WHITE;
//	public Color nonDrawnPageColor=Color.WHITE;
//
//	/**
//	 * The colour of the text once highlighted
//	 */
//	public static Color backgroundColor = null;
//
//	/**
//	 * The transparency of the highlighting box around the text stored as a float
//	 */
//	public static float highlightComposite = 0.35f;
//
//    String altName;
//
//	/**tracks indent so changing to continuous does not disturb display*/
//	private int lastIndent=-1;
//
//	PageOffsets currentOffset;
//
//	/**copy of flag to tell program whether to create
//	 * (and possibly update) screen display
//	 */
//	protected boolean renderPage = false;
//
//	/**type of printing*/
//	protected boolean isPrintAutoRotateAndCenter=false;
//
//	/**flag to show we use PDF page size*/
//	protected boolean usePDFPaperSize=false;
//
//	/**page scaling mode to use for printing*/
//	protected int pageScalingMode=PrinterOptions.PAGE_SCALING_REDUCE_TO_PRINTER_MARGINS;
//
//
//	/**display mode (continuous, facing, single)*/
//	protected int displayView=Display.SINGLE_PAGE;
//
//	/**amount we scroll screen to make visible*/
//	private int scrollInterval=10;
//
//	/** count of how many pages loaded */
//	protected int pageCount = 0;
//
//	/**
//	 * if true
//	 * show the crop box as a box with cross on it
//	 * and remove the clip.
//	 */
//	private boolean showCrop = false;
//
//	/** when true setPageParameters draws the page rotated for use with scale to window */
//    boolean isNewRotationSet=false;
//
//	/** displays the viewport border */
//	protected boolean displayViewportBorder=false;
//
//	/**flag to stop multiple attempts to decode*/
//	protected boolean isDecoding=false;
//    protected boolean isGeneratingPage=false;
//
//    protected boolean formsDecoding=false;
//
//	protected int alignment=Display.DISPLAY_LEFT_ALIGNED;
//
//	/** used by setPageParameters to draw rotated pages */
//	protected int displayRotation=0;
//
//	/**allows user to create viewport on page and scale to this*/
//	protected Rectangle viewableArea=null;
//
//	/**used to draw demo cross*/
//	AffineTransform demoAf=null;
//
//	// <start-me>
//	/**repaint manager*/
//	private RepaintManager currentManager=RepaintManager.currentManager(this);
//	// <end-me>
//
//	/**current page*/
//	protected int pageNumber=1;
//
//	/**used to reduce or increase image size*/
//	protected AffineTransform displayScaling;
//
//	/**
//	 * used to apply the imageable area to the displayscaling, used instead of
//	 * displayScaling, as to preserve displayScaling
//	 */
//	protected AffineTransform viewScaling=null;
//
//    /**scrollbar for side-scroll mode*/
//    protected JScrollBar scroll = null;
//
//	/** holds page information used in grouping*/
//	protected PdfPageData pageData = new PdfPageData();
//
//	/**used to track highlight*/
//	private Rectangle lastHighlight=null;
//
//	/**rectangle drawn on screen by user*/
//	protected Rectangle cursorBoxOnScreen = null,lastCursorBoxOnScreen=null;
//
//	/** whether the cross-hairs are drawn */
//	private boolean drawCrossHairs = false;
//
//	/** which box the cursor is currently positioned over */
//	private int boxContained = -1;
//
//	/** color to highlight selected handle */
//	private Color selectedHandleColor = Color.red;
//
//	/** the gap around each point of reference for cursorBox */
//	private int handlesGap = 5;
//
//	/**colour of highlighted rectangle*/
//	private Color outlineColor;
//
//	/**rectangle of object currently under cursor*/
//	protected Rectangle currentHighlightedObject = null;
//
//	/**colour of a shape we highlight on the page*/
//	private Color outlineHighlightColor;
//
//	/**preferred colour to highliht page*/
//	private Color[] highlightColors;
//
//	/**gap around object to repaint*/
//	static final private int strip=2;
//
//	/**highlight around selected area*/
//	private Rectangle2D[] outlineZone = null;
//
//	private int[] processedByRegularExpression=null;
//
//	/**allow for inset of display*/
//	protected int insetW=0,insetH=0;
//
//	/**flag to show if area selected*/
//	private boolean[] highlightedZonesSelected = null;
//
//	private boolean[] hasDrownedObjects = null;
//
//	/**user defined viewport*/
//	Rectangle userAnnot=null;
//
//	/** default height width of bufferedimage in pixels */
//	private int defaultSize = 100;
//
//	/**height of the BufferedImage in pixels*/
//    int y_size = defaultSize;
//
//	/**unscaled page height*/
//    int max_y;
//
//	/**unscaled page Width*/
//    int max_x;
//
//	/**width of the BufferedImage in pixels*/
//    int x_size = defaultSize;
//
//	/**used to plot selection*/
//	int[] cx=null,cy=null;
//
//	/**any scaling factor being used to convert co-ords into correct values
//	 * and to alter image size
//	 */
//	protected float scaling=1;
//
//	/**mode for showing outlines*/
//	private int highlightMode = 0;
//
//	/**flag for showing all object outlines in PDFPanel*/
//	public static final int SHOW_OBJECTS = 1;
//
//	/**flag for showing all lines on page used for grouping */
//	public static final int SHOW_LINES = 2;
//
//	/**flag for showing all lines on page used for grouping */
//	public static final int SHOW_BOXES = 4;
//
//	/**size of font for selection order*/
//	protected int size=20;
//
//	/**font used to show selection order*/
//	protected Font highlightFont=null;
//
//	/**border for component*/
//	protected Border myBorder=null;
//
//	// <start-me>
//	protected DropTarget dropTarget = null;
//	// <end-me>
//
//	/** the ObjectStore for this file */
//	public ObjectStore objectStoreRef = new ObjectStore();
//
//	/**the actual display object*/
//	protected DynamicVectorRenderer currentDisplay=new SwingDisplay(1,objectStoreRef,false); //
//
//	/**flag to show if border appears on printed output*/
//	protected boolean useBorder=true;
//
//	private int[] selectionOrder;
//
//	protected boolean useAcceleration=true;
//
//	/**all text blocks as a shape*/
//	private Shape[] fragmentShapes;
//
//	int x_size_cropped;
//
//	int y_size_cropped;
//
//	private AffineTransform cursorAf;
//
//	private Rectangle actualBox;
//
//	private boolean drawInteractively=false;
//
//	protected int lastFormPage=-1,lastStart=-1,lastEnd=-1;
//
//	private int pageUsedForTransform;
//
//	protected int xOffset=0;
//
//	private boolean displayForms = true;
//
//	//private GraphicsDevice currentGraphicsDevice = null;
//	public boolean extractingAsImage = false;
//
//    /**holds lines of text we create*/
//            private org.jpedal.text.TextLines textLines=new TextLines();
//
//    /**
//        *
//        * access textlines object
//        */
//       public TextLines getTextLines() {
//          return textLines;
//          
//       }
//	//############viewStack ########
//
//	/** used to store the IE views so we can go back to previous views and store changes */
//	protected ViewStack viewStack = new ViewStack();
//
//	public void addAView(int page, Rectangle location, Integer scalingType) {
//		//if we want to start storing the zoom all we need to do is add the type to Viewable and replace the type in changeTo call
//		viewStack.add(page,location,scalingType);
//	}
//
//	/**
//	 * the view stack type object that allows us to store views as they change
//	 * and then go back through them as needed.
//	 *
//	 * @author Chris Wade
//	 */
//	protected class ViewStack {
//
//		private ArrayList ourStack = new ArrayList();
//		private int index = -1;
//		private int length = 0;
//
//		protected Viewable back(){
//
//			if(index-1>-1 && index-1<length){
//				index--;
//				return (Viewable)ourStack.get(index);
//			}else
//				return null;
//		}
//
//		protected Viewable forward(){
//
//			if(index+1>-1 && index+1<length){
//				index++;
//				return (Viewable) ourStack.get(index);
//			}else
//				return null;
//		}
//
//		protected synchronized void add(int page, Rectangle location, Integer scalingType){
//			//check capacity will take the new object and location +1 and +1 for the length.
//			ourStack.ensureCapacity(index+2);
//			ourStack.add(index+1, new Viewable(page,location,scalingType));
//
//			//set the index and length after to ensure correct runing if an exception
//			index++;
//			length = index+1;
//		}
//
//		/** a view of a defined page, Rectangle on page, and scalingtype */
//		protected class Viewable {
//			private int page;
//			private Rectangle location;
//			private Integer type;
//
//			protected Viewable(int inPage, Rectangle rectangle,Integer inType) {
//				page = inPage;
//				location = rectangle;
//				type = inType;
//			}
//
//			protected Rectangle getLocation(){
//				return location;
//			}
//
//			protected int getPage(){
//				return page;
//			}
//
//			protected Integer getType(){
//				return type;
//			}
//		}
//	}
//
//    public void setExtractingAsImage(boolean extractingAsImage) {
//		this.extractingAsImage = extractingAsImage;
//
//	}
//
//	//<start-adobe>
//	public void initNonPDF(PdfDecoder pdf){
//
//		pages=new SingleDisplay(pageNumber,pageCount,currentDisplay);
//
//		pages.setup(true,null, pdf);
//	}
//
//	/**workout combined area of shapes are in an area*/
//	public  Rectangle getCombinedAreas(Rectangle targetRectangle,boolean justText){
//		if(this.currentDisplay!=null)
//			return currentDisplay.getCombinedAreas(targetRectangle, justText);
//		return
//		null;
//	}
//
//	/**set zones we want highlighted onscreen
//	 * NOT RECOMMENDED for general use -
//	 * please look at  setFoundTextAreas(Rectangle areas),setHighlightedAreas(Rectangle[] areas)
//	 * <b>This is NOT part of the API</b> (used in Storypad)
//	 */
//	final public void setHighlightedZones(
//			int mode,
//			int[] cx,int[] cy,
//			Shape[] fragmentShapes,
//			Rectangle2D[] outlineZone,
//			boolean[] highlightedZonesSelected,boolean[] hasDrownedObjects,Color[] highlightColors,int[] selectionOrder,int[] processedByRegularExpression) {
//
//		this.cx=cx;
//		this.cy=cy;
//		this.fragmentShapes=fragmentShapes;
//
//		this.outlineZone = outlineZone;
//		this.processedByRegularExpression=processedByRegularExpression;
//
//		this.highlightedZonesSelected = highlightedZonesSelected;
//		this.hasDrownedObjects = hasDrownedObjects;
//		this.highlightMode = mode;
//		this.highlightColors=highlightColors;
//		this.selectionOrder=selectionOrder;
//
//	}
//
//	/**
//	 * set an inset display so that display will not touch edge of panel*/
//	final public void setInset(int width,int height) {
//		this.insetW=width;
//		this.insetH=height;
//	}
//
//	/**
//	 * make screen scroll to ensure point is visible
//	 */
//	public void ensurePointIsVisible(Point p){
//		super.scrollRectToVisible(new Rectangle(p.x,y_size-p.y,scrollInterval,scrollInterval));
//	}
//	//<end-adobe>
//
//    /**
//     * allow user to 'move' display of PDF
//     *
//     * mode is a Constant in org.jpedal.external.OffsetOptions (ie OffsetOptions.SWING_DISPLAY,OffsetOptions.PRINTING)
//     */
//    public void setUserOffsets(int x, int y, int mode){
//
//        switch(mode){
//
//            case org.jpedal.external.OffsetOptions.DISPLAY:
//                userOffsetX=x;
//                userOffsetY=y;
//                break;
//
//            case org.jpedal.external.OffsetOptions.PRINTING:
//                userPrintOffsetX=x;
//                userPrintOffsetY=-y; //make it negative so both work in same direction
//                break;
//
//            // <start-adobe><start-thin><start-ulc>// <start-me>
//            case org.jpedal.external.OffsetOptions.INTERNAL_DRAG_BLANK:
//                facingCursorX = 0;
//                facingCursorY = getHeight();
//                SwingGUI gui1 = (SwingGUI)((PdfDecoder)this).getExternalHandler(Options.SwingContainer);
//                if (gui1 != null)
//                    gui1.setDragCorner(mode);
//                repaint();
//                break;
//
//
//            case org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_BOTTOM_LEFT:
//                facingCursorX=x;
//                facingCursorY=y;
//                SwingGUI gui2 = (SwingGUI)((PdfDecoder)this).getExternalHandler(Options.SwingContainer);
//                if (gui2 != null)
//                    gui2.setDragCorner(mode);
//                repaint();
//                break;
//
//            case org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_BOTTOM_RIGHT:
//                facingCursorX=x;
//                facingCursorY=y;
//                SwingGUI gui3 = (SwingGUI)((PdfDecoder)this).getExternalHandler(Options.SwingContainer);
//                if (gui3 != null)
//                    gui3.setDragCorner(mode);
//                repaint();
//                break;
//
//            case org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_TOP_LEFT:
//                facingCursorX=x;
//                facingCursorY=y;
//                SwingGUI gui4 = (SwingGUI)((PdfDecoder)this).getExternalHandler(Options.SwingContainer);
//                if (gui4 != null)
//                    gui4.setDragCorner(mode);
//                repaint();
//                break;
//
//            case org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_TOP_RIGHT:
//                facingCursorX=x;
//                facingCursorY=y;
//                SwingGUI gui5 = (SwingGUI)((PdfDecoder)this).getExternalHandler(Options.SwingContainer);
//                if (gui5 != null)
//                    gui5.setDragCorner(mode);
//                repaint();
//                break;
//            // <end-ulc><end-thin><end-adobe>// <end-me>
//
//            default:
//                throw new RuntimeException("No such mode - look in org.jpedal.external.OffsetOptions for valid values");
//        }
//
//    }
//
//    public Point getUserOffsets(int mode){
//
//        switch(mode){
//
//            case org.jpedal.external.OffsetOptions.DISPLAY:
//                return new Point(userOffsetX,userOffsetY);
//
//            case org.jpedal.external.OffsetOptions.PRINTING:
//                return new Point(userPrintOffsetX,userPrintOffsetY);
//
//            case org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_BOTTOM_RIGHT:
//                return new Point(facingCursorX,facingCursorY);
//
//            default:
//                throw new RuntimeException("No such mode - look in org.jpedal.external.OffsetOptions for valid values");
//        }
//
//    }
//
//	/**
//	 * get sizes of panel <BR>
//	 * This is the PDF pagesize (as set in the PDF from pagesize) -
//	 * It now includes any scaling factor you have set (ie a PDF size 800 * 600
//	 * with a scaling factor of 2 will return 1600 *1200)
//	 */
//	final public Dimension getMaximumSize() {
//
//		Dimension pageSize=null;
//
//		if(displayView!=Display.SINGLE_PAGE)
//			pageSize = pages.getPageSize(displayView);
//
//		if(pageSize==null){
//			if((displayRotation==90)|(displayRotation==270))
//				pageSize= new Dimension((int)(y_size_cropped+insetW+insetW+(xOffset*scaling)),x_size_cropped+insetH+insetH);
//			else
//				pageSize= new Dimension((int)(x_size_cropped+insetW+insetW+(xOffset*scaling)),y_size_cropped+insetH+insetH);
//
//            }
//
//        if(pageSize==null)
//        pageSize=getMinimumSize();
//
//        return pageSize;
//
//	}
//
//	/**
//	 * get width*/
//	final public Dimension getMinimumSize() {
//
//		return new Dimension(100+insetW,100+insetH);
//	}
//
//	/**
//	 * get sizes of panel <BR>
//	 * This is the PDF pagesize (as set in the PDF from pagesize) -
//	 * It now includes any scaling factor you have set (ie a PDF size 800 * 600
//	 * with a scaling factor of 2 will return 1600 *1200)
//	 */
//	public Dimension getPreferredSize() {
//		return getMaximumSize();
//	}
//
//	//<start-adobe>
//
//	/**
//	 * update rectangle we draw to highlight an area -
//	 * See SimpleViewer example for example code showing current usage.
//	 */
//	final public void updateCursorBoxOnScreen(
//			Rectangle newOutlineRectangle,
//			Color outlineColor) {
//
//		if(this.displayView!=Display.SINGLE_PAGE)
//			return;
//
//		//area to reapint
//		int x_size=this.x_size;
//		int y_size=this.y_size;
//
//		if(newOutlineRectangle!=null){
//
//			int x=newOutlineRectangle.x;
//			int y=newOutlineRectangle.y;
//			int w=newOutlineRectangle.width;
//			int h=newOutlineRectangle.height;
//
//			int cropX=pageData.getCropBoxX(pageNumber);
//			int cropY=pageData.getCropBoxY(pageNumber);
//			int cropW=pageData.getCropBoxWidth(pageNumber);
//			int cropH=pageData.getCropBoxHeight(pageNumber);
//
//            //allow for odd crops and correct
//            if(y>0 && y<(cropY))
//            y=y+cropY;
//
//			if(x<cropX){
//				int diff=cropX-x;
//				w=w-diff;
//				x=cropX;
//			}
//
//			if(y<cropY){
//				int diff=cropY-y;
//				h=h-diff;
//				y=y+diff;
//			}
//			if((x+w)>(cropW+cropX+xOffset))
//				w=cropX+xOffset+cropW-x;
//			if((y+h)>(cropY+cropH))
//				h=cropY+cropH-y;
//
//			cursorBoxOnScreen = new Rectangle(x,y,w,h);
//
//		}else
//			cursorBoxOnScreen=null;
//
//		this.outlineColor = outlineColor;
//
//		int strip=30;
//
//		/**allow offset from page being centered*/
//		int dx=0;
//		//center if required
//		if(alignment==Display.DISPLAY_CENTERED){
//			int width=this.getBounds().width;
//			int pdfWidth=this.getPDFWidth();
//
//			if(displayView!=Display.SINGLE_PAGE)
//				pdfWidth=(int)pages.getPageSize(displayView).getWidth();
//
//			dx=((width-pdfWidth)/2);
//		}
//
//		// <start-me>
//		if(lastCursorBoxOnScreen!=null){
//			if(displayRotation==0 || displayRotation==180)
//				currentManager.addDirtyRegion(this,insetW+dx,insetH,x_size+5+xOffset,y_size);
//			else
//				currentManager.addDirtyRegion(this,insetH+dx,insetW,y_size+5+xOffset,x_size);
//
//			lastCursorBoxOnScreen=null;
//		}
//
//		if(cursorBoxOnScreen!=null){
//			currentManager.addDirtyRegion(this,(int)(cursorBoxOnScreen.x*scaling)-strip,
//					(int)(((max_y)-cursorBoxOnScreen.y-cursorBoxOnScreen.height)*scaling)-strip,
//					(int)(cursorBoxOnScreen.width*scaling)+strip+strip,
//					(int)(cursorBoxOnScreen.height*scaling)+strip+strip);
//		}
//
//		if(this.viewScaling!=null)
//			currentManager.markCompletelyDirty(this);
//		// <end-me>
//
//        //force repaint
//        repaint();
//	}
//
//	/**requests repaint of an area*/
//	public void repaintArea(Rectangle screenBox,int maxY){
//
//		int strip=10;
//
//		if(strip<this.insetH)
//			strip=insetH;
//
//		if(strip<this.insetW)
//			strip=insetW;
//
//		int x = (int)(screenBox.x*scaling)-strip;
//		int y = (int)((maxY-screenBox.y-screenBox.height)*scaling)-strip;
//		int width = (int)((screenBox.x+screenBox.width)*scaling)+strip+strip;
//		int height = (int)((screenBox.y+screenBox.height)*scaling)+strip+strip;
//
//		// <start-me>
//		currentManager.addDirtyRegion(this,x,y,width,height);
//		/* <end-me>
//		repaint(x, y, width, height);
//		/**/
//
//	}
//
//	/**
//	 * update rectangle we draw to show area of object -
//	 * See org.jpedal.examples.contentextractor.contentExtractor
//	 */
//	final public void removeHiglightedObject(){
//
//		getTextLines().clearHighlights();
//		if(lastHighlight!=null){
//			int x = lastHighlight.x-strip;
//			int y = lastHighlight.y-strip;
//			int w = lastHighlight.width+strip+strip;
//			int h = lastHighlight.height+strip+strip;
//			// <start-me>
//			currentManager.addDirtyRegion(this,x,y,w,h);
//			/* <end-me>
//			repaint(x, y, w, h);
//			/**/
//
//			currentHighlightedObject=null;
//
//		}
//	}
//	//<end-adobe>
//
//
//	public void paint(Graphics g){
//
//		try{
//
//			super.paint(g);
//
//			if(!isDecoding){
//
//
//				/**add any highlighted rectangle on screen*/
//				if (cursorBoxOnScreen != null){
//					Graphics2D g2=(Graphics2D) g;
//					AffineTransform defaultAf=g2.getTransform();
//
//					if(cursorAf!=null){
//						g2.setTransform(cursorAf);
//
//						Shape clip=g2.getClip();
//
//						//remove clip for drawing outline
//						if((alignment==Display.DISPLAY_CENTERED)&&(clip!=null))
//							g2.setClip(null);
//
//						//<start-adobe>
//						paintRectangle(g2);
//						//<end-adobe>
//
//						g2.setClip(clip);
//
//						g2.setTransform(defaultAf);
//
//					}
//				}
//			}
//
//		}catch(Exception e){
//
//			pages.flushPageCaches();
//
//		}catch(Error err){  //for tight memory
//
//
//		    pages.flushPageCaches();
//		    pages.stopGeneratingPage();
//
//		    super.paint(g);
//
//		}
//	}
//
//	/**standard method to draw page and any highlights onto JPanel*/
//	public void paintComponent(Graphics g) {
//
//		if(customRenderChangeListener!=null) //call custom class if present
//			customRenderChangeListener.renderingStarted(this.pageNumber);
//
//		if (SwingUtilities.isEventDispatchThread()) {
//
//			threadSafePaint(g);
//
//			if(customRenderChangeListener!=null) //call custom class if present
//				customRenderChangeListener.renderingEnded(this.pageNumber);
//
//		} else {
//			final Graphics g2 = g;
//			final int page=pageNumber;
//			final Runnable doPaintComponent = new Runnable() {
//				public void run() {
//
//					threadSafePaint(g2);
//
//					if(customRenderChangeListener!=null) //call custom class if present
//						customRenderChangeListener.renderingEnded(page);
//				}
//			};
//			SwingUtilities.invokeLater(doPaintComponent);
//		}
//	}
//
//	/**
//	 * update display
//	 */
//	synchronized void threadSafePaint(Graphics g) {
//
//        //System.out.println("threadSafePaint1-------------------------------------------------------- "+displayRotation);
//
//		super.paintComponent(g);
//
//        //<start-me>
//        if (displayView==Display.PAGEFLOW3D)
//            pages.init(scaling,pageCount,displayRotation,pageNumber,currentDisplay,false, pageData,insetW,insetH);
//        //<end-me>
//
//		if(displayScaling==null)
//			return;
//
//		Graphics2D g2 = (Graphics2D) g;
//
//		//remember so we can put it back
//		final AffineTransform rawAf=g2.getTransform();
//
//
//        //include any user trnaslation
//        g2.translate(userOffsetX, userOffsetY);
//
//		if(Display.debugLayout)
//			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>START PAINT");
//
//		/**
//		 * lazy initialisation to add forms to screen
//		 */
//
//		if (renderPage && displayForms ) {
//
//			//track all changes
//			int start=pageNumber;
//			int end=pageNumber;
//
//            //control if we display forms on multiple pages
//			if(displayView!=Display.SINGLE_PAGE
//					// <start-me>
//					&& displayView!=Display.PAGEFLOW
//					// <end-me>
//					){
//				start=pages.getStartPage();
//				end=pages.getEndPage();
//				if(start==0 || end==0 || lastEnd!=end || lastStart!=start)
//					lastFormPage=-1;
//
//				lastEnd=end;
//				lastStart=start;
//
//			}
//
//			if(lastFormPage!=pageNumber && !isDecoding){
//
//				if (formRenderer != null){
//					formRenderer.displayComponentsOnscreen(start,end);
//
//					//switch off if forms for this page found
//					if(formRenderer.getCompData().getStartComponentCountForPage(pageNumber)!=-1)
//					lastFormPage=pageNumber; //ensure not called too early
//				}
//			}
//		}
//
//		if(DynamicVectorRenderer.debugPaint)
//			System.err.println("threadsafePaint called "+this.displayView);
//
//        pages.init(scaling,pageCount,displayRotation,pageNumber,currentDisplay,false, pageData,insetW,insetH);
//
//		//center if required
//		if(alignment==Display.DISPLAY_CENTERED){
//			double width=this.getBounds().getWidth();
//
//			// <start-me>
//            if(displayView==Display.PAGEFLOW)
//                width=getVisibleRect().getWidth();
//            // <end-me>
//
//			int pdfWidth=this.getPDFWidth();
//
//			if(displayView!=Display.SINGLE_PAGE)
//				pdfWidth=(int)pages.getPageSize(displayView).getWidth();
//
//            {//if(width>pdfWidth || displayView==Display.PAGEFLOW)  {
//
//            	// <start-me>
//                //we indent it here so selected page is  in middle of panel and now make rest of co-ords relative which is all much easier....
//                if(displayView==Display.PAGEFLOW){
//
//                    indent=((this.getVisibleRect().width-pages.getWidthForPage(pageNumber))/2)-insetW-this.getBounds().x;
//
//                } else
//                	// <end-me>
//                if (displayView==Display.FACING) {
//                    PdfDecoder pdf = (PdfDecoder)this;
//
//                    int page = pageNumber;
//                    if (pdf.separateCover && (page & 1)==1)
//                            page--;
//                    else if (!pdf.separateCover && (page & 1)==0)
//                            page--;
//
//
//                    //Get widths of pages
//                    int firstW = 0;
//                    int secondW = 0;
//                    if ((displayRotation + pdf.getPdfPageData().getRotation(page))%180==90)
//                        firstW = pdf.getPdfPageData().getCropBoxHeight(page);
//                    else
//                        firstW = pdf.getPdfPageData().getCropBoxWidth(page);
//
//                    if (page+1 > pageCount) {
//                        secondW = firstW;
//                    } else {
//                        if ((displayRotation + pdf.getPdfPageData().getRotation(page+1))%180==90)
//                            secondW = pdf.getPdfPageData().getCropBoxHeight(page+1);
//                        else
//                            secondW = pdf.getPdfPageData().getCropBoxWidth(page+1);
//                    }
//
//                    //get total width
//                    int totalW = firstW + secondW;
//
//                    //set pageGap
//                    int pageGap = 0;
//                    if (!turnoverOn || pdf.getPdfPageData().hasMultipleSizes() || pageCount==2)
//                        pageGap = currentOffset.pageGap/2;
//
//                    //set indent
//                    indent = (((width - (totalW * scaling)) / 2) - pageGap - insetW);
//                } else
//                    indent=((width-pdfWidth)/2);
//
//				if(displayView==Display.SINGLE_PAGE)
//					lastIndent=(int)indent;
//				else if((displayView==Display.CONTINUOUS
//						// <start-me>
//						|| displayView==Display.PAGEFLOW
//						// <end-me>
//						) && lastIndent!=-1){
//					indent=lastIndent;
//					lastIndent = -1;
//				}else
//					lastIndent=-1;
//
//        				g2.translate(indent,0);
//			}
//
//
//			if(formRenderer!=null && currentOffset!=null){ //if all forms flattened, we can get a null value for currentOffset so avoid this case
//
//				// <start-me>
//                if(displayView==Display.PAGEFLOW)
//                    indent= indent-((pageNumber-1)*(PageOffsets.getPageFlowPageWidth((int)(pageData.getCropBoxWidth(pageNumber)*scaling),scaling)));
//                // <end-me>
//
//                formRenderer.getCompData().setPageValues(scaling,displayRotation,(int)indent,userOffsetX, userOffsetY,displayView,currentOffset.widestPageNR,currentOffset.widestPageR);
//			    formRenderer.getCompData().resetScaledLocation(scaling,displayRotation,(int)indent);//indent here does nothing.
//            }
//		}else if(formRenderer!=null && currentOffset!=null){
//            lastIndent=-1;
//            formRenderer.getCompData().setPageValues(scaling,displayRotation,(int)indent,userOffsetX, userOffsetY,displayView,currentOffset.widestPageNR,currentOffset.widestPageR);
//            formRenderer.getCompData().resetScaledLocation(scaling,displayRotation,(int)indent);
//        }
//
//        /**we need to store the Affine and put it back at
//		 * the end otherwise we f**k up all the form components
//		 * on certain pages
//		 */
//		Rectangle dirtyRegion=null;
//
//		if(textLines.areas!=null)
//			pages.initRenderer(textLines.areas,g2,myBorder,(int)indent);
//		else{
//			pages.initRenderer(null,g2,myBorder,(int)indent);
//		}
//
//        if(!isDecoding||drawInteractively){
//
//            //@itunes - draw page
//            //if(this.displayView==Display.PAGEFLOW)
//            //System.out.println(">>>>>>"+this.getVisibleRect()+" "+this.getAlignmentX()+" "+viewScaling+" "+displayScaling);
//
//			actualBox =pages.drawPage(viewScaling,displayScaling,pageUsedForTransform);
//		}else{ //just fill the background
//            currentDisplay.setG2(g2);
//            currentDisplay.paintBackground(dirtyRegion);
//        }
//
//        /**/
//		//disabled if not in Single PAGE
//		if(displayView==Display.SINGLE_PAGE){
//		/**/
//			/**
//			 * draw highlighted text boxes
//			 */
//
//
//
//			//reset after first clear
//			if ((currentHighlightedObject==null)&&(lastHighlight!=null))
//				lastHighlight=null;
//
//			/**
//			 * add any viewport
//			 */
//			if(viewScaling!=null)
//				g2.transform(viewScaling);
//
//			//<start-adobe>
//
//			/**set any highlighted zones*/
//			if (highlightedZonesSelected != null)
//				paintHighlights(g2);
//
//			//<end-adobe>
//
//			/**add any highlighted rectangle on screen*/
//			if (cursorBoxOnScreen != null)
//				this.cursorAf=g2.getTransform();
//			else
//				this.actualBox=null;
//
//			pages.resetToDefaultClip();
//
//			//draw highlight underneath
//			if (currentHighlightedObject != null) {
//				g2.setColor(outlineHighlightColor);
//				g2.draw(currentHighlightedObject);
//			}
//
//
//			if (showCrop) {
//				g2.setColor(Color.orange);
//				pages.completeForm(g2);
//
//			}
//		}
//
//		if(displayView==Display.SINGLE_PAGE){
//			if(highlightedImage!=null){
//				//All image highlight coords scaled here to allow for any scaling value
//
//				//Varibles added to make the code more readable
//				int x= (int)(highlightedImage[0]*scaling);
//				int y= (int)(highlightedImage[1]*scaling);
//				int w= (int)(highlightedImage[2]*scaling);
//				int h= (int)(highlightedImage[3]*scaling);
//
//				//			//Check for negative values
//				if(w<0){
//					w =-w;
//					x =x-w;
//				}
//				if(h<0){
//					h =-h;
//					y =y-h;
//				}
//
//				//Final values to use
//				int finalX ;
//				int finalY ;
//				int finalW= w;
//				int finalH= h;
//
//				//Handle Any Rotation
//				if(displayRotation==90){
//
//					finalH= w;
//					finalW= h;
//
//					finalX=insetW+y;
//					finalY= insetH +x;
//
//				}else
//					if(displayRotation==180){
//
//						finalX= (int)((max_x*scaling)-(x)-w)+insetW;
//						finalY= (insetH +y);
//
//					}else
//						if(displayRotation==270){
//
//							finalH= w;
//							finalW= h;
//
//							finalY= (int)((max_x*scaling)-(x)-w)+insetW;
//							finalX= (int)((max_y*scaling)-(y)-h)+insetH;
//
//						}else{
//							finalX= insetW +x;
//							finalY= (int)((max_y*scaling)-(y)-h)+insetH;
//
//						}
//
//				Color oldColor = g2.getColor();
//				Composite oldComposite = g2.getComposite();
//				Stroke oldStroke = g2.getStroke();
//
//				g2.setStroke(new BasicStroke(2));
//				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, PdfDecoder.highlightComposite));
//
//				//draw border
//				if(SwingDisplay.invertHighlight){
//					g2.setColor(Color.WHITE);
//					g2.setXORMode(Color.BLACK);
//				}else{
//					g2.setColor(PdfDecoder.highlightColor);
//					g2.drawRect(finalX,finalY,finalW,finalH);
//				}
//
//				//fill border
//
//				g2.fillRect(finalX,finalY,finalW,finalH);
//
//				//set back to original setup
//				g2.setColor(oldColor);
//				g2.setComposite(oldComposite);
//				g2.setStroke(oldStroke);
//			}
//		}else{
//			highlightedImage = null;
//		}
//
//
//
//		/**
//		 * draw other pages if not in SINGLE mode
//		 **/
//		pages.drawBorder();
//
//
//        g2.setTransform(rawAf);
//
//        //draw facing mode turnover
//
//        //<start-me>
//        //draw preview on page if set
//        if(previewImage!=null){
//
//            int iw=previewImage.getWidth();
//            int ih=previewImage.getHeight();
//
//            int textWidth = g2.getFontMetrics().stringWidth(previewText);
//
//            int width = iw > textWidth ? iw : textWidth;
//
//            int x=this.getVisibleRect().x+this.getVisibleRect().width-40-width;
//            int y=(this.getVisibleRect().y+this.getVisibleRect().height-20-ih)/2;
//
//            Composite original = g2.getComposite();
//            g2.setPaint(Color.BLACK);
//            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
//            g2.fill(new RoundRectangle2D.Double(x-10, y-10, width+20, ih+35, 10, 10));
//            g2.setComposite(original);
//
//            g2.setPaint(Color.WHITE);
//            x += (width - iw) / 2;
//            g2.drawImage(previewImage,x,y,null);
//
//            xOffset = (iw+20 - textWidth) / 2;
//
//            g2.drawString(previewText,x + xOffset - 10,y+ih+15);
//        }
//      //<end-me>
//
//		if(Display.debugLayout)
//			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<END PAINT ");
//
//	}
//
//	public void scrollRectToHighlight(Rectangle highlight, int page) {
//		int x = 0, y = 0, w = 0, h = 0;
//
//		if(page<1 || page>pageCount || displayView==Display.SINGLE_PAGE){
//			page=pageNumber;
//		}
//
//		int cropW = pageData.getCropBoxWidth(page);
//		int cropH = pageData.getCropBoxHeight(page);
//		int cropX = pageData.getCropBoxX(page);
//		int cropY = pageData.getCropBoxY(page);
//
//		switch (displayRotation) {
//		case 0:
//		    x = (int) ((highlight.x - cropX) * scaling) + insetW;
//			y = (int) ((cropH - (highlight.y - cropY)) * scaling) + insetH;
//			w = (int) (highlight.width * scaling);
//			h = (int) (highlight.height * scaling);
//
//			break;
//		case 90:
//			x = (int) ((highlight.y - cropY) * scaling) + insetH;
//			y = (int) ((highlight.x - cropX) * scaling) + insetW;
//			w = (int) (highlight.height * scaling);
//			h = (int) (highlight.width * scaling);
//
//			break;
//		case 180:
//			x = (int) ((cropW - (highlight.x - cropX)) * scaling) + insetW;
//			y = (int) ((highlight.y - cropY) * scaling) + insetH;
//			w = (int) (highlight.width * scaling);
//			h = (int) (highlight.height * scaling);
//
//			break;
//		case 270:
//			x = (int) ((cropH - (highlight.y - cropY)) * scaling) + insetH;
//			y = (int) ((cropW - (highlight.x - cropX)) * scaling) + insetW;
//			w = (int) (highlight.height * scaling);
//			h = (int) (highlight.width * scaling);
//
//			break;
//		}
//
//		if(displayView!=Display.SINGLE_PAGE
//				// <start-me>
//				&& displayView!=Display.PAGEFLOW3D
//				// <end-me>
//				){
//			x = x+pages.getXCordForPage(page);
//			y = y+pages.getYCordForPage(page);
//		}
//
//		if(x>this.getVisibleRect().x+(this.getVisibleRect().width/2))
//			x = x+((this.getVisibleRect().width/2)-(highlight.width/2));
//		else
//			x = x-((this.getVisibleRect().width/2)-(highlight.width/2));
//
//		if(y>this.getVisibleRect().y+(this.getVisibleRect().height/2))
//			y = y+((this.getVisibleRect().height/2)-(highlight.height/2));
//		else
//			y = y-((this.getVisibleRect().height/2)-(highlight.height/2));
//
//		Rectangle scrollto = new Rectangle(x - scrollInterval, y - scrollInterval, w + scrollInterval * 2, h + scrollInterval * 2);
//
//		scrollRectToVisible(scrollto);
//	}
//
//	//<start-adobe>
//
//	/**
//	 * turn crossHairs on or off -
//	 * highlight <b>newBoxContained</b> handle with specified Color,<br>
//	 * if <b>newBoxContained</b> is -1 no handles are highlighted -
//	 * See org.jpedal.examples.contentextractor.ContentExtractor
//	 * @param newBoxContained
//	 */
//	public void setDrawCrossHairs(boolean newDrawCrossHairs,int newBoxContained,Color newColor){
//		drawCrossHairs = newDrawCrossHairs;
//		boxContained = newBoxContained;
//		selectedHandleColor = newColor;
//	}
//
//	/**
//	 * draw cursorBox on screen with specified color,
//	 */
//	private void paintRectangle(Graphics2D g2){
//
//        Stroke oldStroke = g2.getStroke();//copy before to stop page border from being dotted
//		Stroke lineStroke;
//
//		//allow for negative
//		if(scaling<0)
//			lineStroke = new BasicStroke(1/-scaling);
//		else
//			lineStroke = new BasicStroke(1/scaling);
//
//		g2.setStroke(lineStroke);
//
//		g2.setColor(outlineColor);
//
//		//Draw opaque square around highlight area
//                //@kieran add a showHighlight with default as true.
//                //we make it profile value with default of null
//                if(extractingAsImage || (cursorBoxOnScreen!=null && showMouseBox))
//			g2.draw(cursorBoxOnScreen);
//
//		if(drawCrossHairs){
//
//			int x1 = cursorBoxOnScreen.x;
//			int y1 = cursorBoxOnScreen.y;
//			int x2 = x1+cursorBoxOnScreen.width;
//			int y2 = y1+cursorBoxOnScreen.height;
//
//			int mediaW = pageData.getMediaBoxWidth(pageNumber);
//			int mediaH = pageData.getMediaBoxHeight(pageNumber);
//			int mediaX = pageData.getMediaBoxX(pageNumber);
//			int mediaY = pageData.getMediaBoxY(pageNumber);
//
//			if(scaling>0)
//				g2.setStroke(new BasicStroke(3/scaling, BasicStroke.CAP_ROUND,
//						BasicStroke.JOIN_ROUND, 0, new float[]{0,6/scaling,0,6/scaling}, 0));
//			else
//				g2.setStroke(new BasicStroke(3/-scaling, BasicStroke.CAP_ROUND,
//						BasicStroke.JOIN_ROUND, 0, new float[]{0,6/-scaling,0,6/-scaling}, 0));
//
//			//draw dotted lines to edges
//			g2.drawLine(x1,y1,mediaX,y1);
//			g2.drawLine(x1,y1,x1,mediaY);
//			g2.drawLine(x2,y1,mediaW,y1);
//			g2.drawLine(x2,y1,x2,mediaY);
//
//			g2.drawLine(x1,y2,mediaX,y2);
//			g2.drawLine(x1,y2,x1,mediaH);
//			g2.drawLine(x2,y2,mediaW,y2);
//			g2.drawLine(x2,y2,x2,mediaH);
//
//
//			Rectangle[] cursorBoxHandles = new Rectangle[8];
//			//*centre of line handles
//			//left
//			cursorBoxHandles[0] = new Rectangle(x1-handlesGap,(y1+(Math.abs(y2-y1))/2)-handlesGap,handlesGap*2,handlesGap*2);//0
//			//bottom
//			cursorBoxHandles[1] = new Rectangle((x1+(Math.abs(x2-x1))/2)-handlesGap,y1-handlesGap,handlesGap*2,handlesGap*2);//1
//			//top
//			cursorBoxHandles[2] = new Rectangle((x1+(Math.abs(x2-x1))/2)-handlesGap,y2-handlesGap,handlesGap*2,handlesGap*2);//2
//			//right
//			cursorBoxHandles[3] = new Rectangle(x2-handlesGap,(y1+(Math.abs(y2-y1))/2)-handlesGap,handlesGap*2,handlesGap*2);//3
//			/**/
//
//			//*corner handles
//			//bottom left
//			cursorBoxHandles[4] = new Rectangle(x1-handlesGap,y1-handlesGap,handlesGap*2,handlesGap*2);//4
//			//top left
//			cursorBoxHandles[5] = new Rectangle(x1-handlesGap,y2-handlesGap,handlesGap*2,handlesGap*2);//5
//			//bottom right
//			cursorBoxHandles[6] = new Rectangle(x2-handlesGap,y1-handlesGap,handlesGap*2,handlesGap*2);//6
//			//top right
//			cursorBoxHandles[7] = new Rectangle(x2-handlesGap,y2-handlesGap,handlesGap*2,handlesGap*2);//7
//
//			/**/
//
//			g2.setStroke(lineStroke);
//			//draw handle box containing cursor
//			if(boxContained!=-1 && boxContained<cursorBoxHandles.length){
//				if(selectedHandleColor!=null){
//					Color old = g2.getColor();
//					g2.setColor(selectedHandleColor);
//					g2.fill(cursorBoxHandles[boxContained]);
//					g2.setColor(old);
//				}else
//					g2.fill(cursorBoxHandles[boxContained]);
//			}
//
//			//draw other handles
//			for(int i=0;i<cursorBoxHandles.length;i++){
//				if(i!=boxContained)
//					g2.draw(cursorBoxHandles[i]);
//			}
//		}
//		g2.setStroke(oldStroke);
//
//		if(actualBox==null){
//			lastCursorBoxOnScreen=cursorBoxOnScreen;
//
//		}else{
//
//			Rectangle b1=cursorBoxOnScreen.getBounds();
//			int minX=(int)b1.getMinX();
//			int minY=(int)b1.getMinY();
//			int maxX=(int)b1.getMaxX();
//			int maxY=(int)b1.getMaxY();
//
//			Rectangle bounds=actualBox.getBounds();
//			int tmp=(int)bounds.getMinX();
//			if(tmp<minX)
//				minX=tmp;
//			tmp=(int) bounds.getMinY();
//			if(tmp<minY)
//				minY=tmp;
//			tmp=(int) bounds.getMaxX();
//			if(tmp>maxX)
//				maxX=tmp;
//			tmp=(int)bounds.getMaxY();
//			if(tmp>maxY)
//				maxY=tmp;
//
//			lastCursorBoxOnScreen=new Rectangle(minX-5,minY-5,10+maxX-minX,10+(maxY-minY));
//		}
//	}
//
//    /**
//     * put selected areas onto screen display as highlights
//     */
//    private void paintHighlights(Graphics2D g2) {
//
//        int items=highlightedZonesSelected.length;
//
//        for (int i = 0; i < items; i++) {
//
//            if (highlightedZonesSelected[i]){
//
//                // added check for null due to issues with contentExtractor throwing NullPointerEception
//                if(hasDrownedObjects == null){
//                    highlightStoryOnscreen(g2, i, false);
//                } else {
//                    highlightStoryOnscreen(g2, i, hasDrownedObjects[i]);
//                }
//
//            }else if (((highlightMode & SHOW_OBJECTS) == SHOW_OBJECTS)&&(fragmentShapes[i]!=null)) {
//                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float) 0.1));
//                if(highlightColors[i]==null)
//                    g2.setColor(Color.darkGray);
//                else
//                    g2.setColor(highlightColors[i]);
//
//                g2.fill(fragmentShapes[i]);
//                g2.draw(outlineZone[i]);
//
//                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float) 0.9));
//                g2.draw(outlineZone[i]);
//            }
//        }
//
//        /**add selection order for highlights*/
//        if(selectionOrder!=null){
//            int itemCount= selectionOrder.length;
//
//            for(int ii=0;ii<itemCount;ii++){
//                int i= selectionOrder[ii];
//                if(i==-1){
//                    ii=itemCount;
//                }else{
//                    //see if linked items and highlight
//                    numberItems(false, g2, "", selectionOrder);
//                }
//            }
//        }
//
//    }
//
//	/**
//	 * draw any outline around story and any linked items
//	 */
//	private void highlightStoryOnscreen(Graphics2D g2, int i, boolean containsDownedObjects) {
//
//		Stroke line = g2.getStroke();
//
//		if(containsDownedObjects){
//			Stroke dashed = new BasicStroke(8, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 12, 12 }, 0);
//			g2.setStroke(dashed);
//		}
//
//		if(fragmentShapes[i]!=null){
//			if(highlightColors[i]==null)
//				g2.setColor(highlightColor);
//			else
//				g2.setColor(highlightColors[i]);
//
//			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float) 0.1));
//			g2.fill(fragmentShapes[i]);
//			g2.draw(outlineZone[i]);
//
//			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float) 0.9));
//			g2.draw(outlineZone[i]);
//
//			int xs=outlineZone[i].getBounds().x;
//			int ys=outlineZone[i].getBounds().y;
//			g2.drawLine(xs,ys+(int)outlineZone[i].getBounds().getHeight(),
//					xs+(int)outlineZone[i].getBounds().getWidth(),ys);
//
//			if(processedByRegularExpression[i]>0)
//				g2.drawLine(xs+(int)outlineZone[i].getBounds().getWidth(),ys+(int)outlineZone[i].getBounds().getHeight(),
//						xs,ys);
//		}
//
//		if(containsDownedObjects){
//			int xoffset = outlineZone[i].getBounds().x;
//			int yoffset = outlineZone[i].getBounds().y;
//
//			xoffset = xoffset+((int)outlineZone[i].getBounds().getWidth()/2)-12;
//			yoffset = yoffset+((int)outlineZone[i].getBounds().getHeight()/2);
//
//			//Opps unhappy face
//			g2.setColor(Color.yellow);
//			g2.fillOval(xoffset, yoffset, 50, 50);
//			//eyes
//			g2.setColor(Color.black);
//			g2.fillOval(xoffset+27, yoffset+30, 8, 8);
//			g2.fillOval(xoffset+15, yoffset+30, 8, 8);
//			//frown
//			g2.setStroke(new BasicStroke(3.0f));
//			g2.drawOval(xoffset, yoffset, 50, 50);
//			g2.drawArc(xoffset+12, yoffset+5, 26, 15, 170, 200);
//		}
//
//		g2.setStroke(line);
//	}
//	/**
//	 * add numbers to selected items
//	 */
//	private void numberItems(boolean isSublist,Graphics2D g2, String prefix,int[] selectionOrder) {
//
//		int itemCount=selectionOrder.length;
//
//		if(itemCount==0)
//			return;
//
//		int order=1;
//
//		for(int ii=0;ii<itemCount;ii++){
//			int i=selectionOrder[ii];
//
//			if(i==-1){
//				ii=itemCount;
//			}else{
//
//				String value= prefix +(order);
//
//				//see if linked items and highlight
//				if(fragmentShapes[i]!=null)
//                    numberItem(g2, i, value);
//
//                order++;
//
//			}
//		}
//	}
//
//    /**
//	 * @param g2
//	 * @param i
//	 * @param value
//	 */
//	private void numberItem(Graphics2D g2, int i, String value){
//
//		AffineTransform af=new AffineTransform();
//		GlyphVector gv;
//		gv=highlightFont.createGlyphVector(g2.getFontRenderContext(),value);
//
//		af.scale(1,-1);
//		af.translate(cx[i],-cy[i]);
//
//		Area a=new Area(gv.getOutline());
//		a.transform(af);
//
//		g2.setColor(Color.black);
//		g2.fill(a.getBounds());
//
//		g2.setColor(Color.white);
//		g2.fill(a);
//	}
//
//	//<end-adobe>
//
//	/**
//	 * get sizes of panel <BR>
//	 * This is the PDF pagesize (as set in the PDF from pagesize) -
//	 * It now includes any scaling factor you have set
//	 */
//	final public int getPDFWidth() {
//		if((displayRotation==90)|(displayRotation==270))
//			return y_size+insetW+insetW;
//		else
//			return x_size+insetW+insetW;
//
//	}
//
//	/**
//	 * get raw width for image
//	 */
//	final public int getRawPDFWidth() {
//		if((displayRotation==90)| (displayRotation==270))
//			return y_size;
//		else
//			return x_size;
//	}
//
//	//<start-adobe>
//
//	/**
//	 * allow user to set component for waring message in renderer to appear -
//	 * if unset no message will appear
//	 * @param frame
//	 */
//	public void setMessageFrame(Container frame){
//		currentDisplay.setMessageFrame(frame);
//	}
//
//	//<end-adobe>
//
//
//	/**
//	 * get sizes of panel -
//	 * This is the PDF pagesize
//	 */
//	final public int getPDFHeight() {
//		if((displayRotation==90)|(displayRotation==270))
//			return x_size+insetH+insetH;
//		else
//			return y_size+insetH+insetH;
//
//	}
//
//	/**
//	 * get sizes of page excluding any insets
//	 */
//	final public int getRawPDFHeight() {
//		if((displayRotation==90)|(displayRotation==270))
//			return x_size;
//		else
//			return y_size;
//
//	}
//
//	/**do not display border when screen printed*/
//	public void disableBorderForPrinting(){
//		useBorder=false;
//	}
//
//	/**set border for screen and print which will be displayed<br>
//	 * Setting a new value will enable screen and border painting - disable
//	 * with disableBorderForPrinting() */
//	final public void setPDFBorder(Border newBorder){
//		this.myBorder=newBorder;
//
//		//switch on as default
//		useBorder=true;
//	}
//
//	/**
//	 * workout Transformation to use on image
//	 */
//	protected final AffineTransform getScalingForImage(int pageNumber,int rotation,float scaling) {
//         /**/
//		//poss new code
//		double mediaX = pageData.getMediaBoxX(pageNumber)*scaling;
//		double mediaY = pageData.getMediaBoxY(pageNumber)*scaling;
//		//double mediaW = pageData.getMediaBoxWidth(pageNumber)*scaling;
//		double mediaH = pageData.getMediaBoxHeight(pageNumber)*scaling;
//
//		double crw = pageData.getCropBoxWidth(pageNumber)*scaling;
//		double crh = pageData.getCropBoxHeight(pageNumber)*scaling;
//		double crx = pageData.getCropBoxX(pageNumber)*scaling;
//		double cry = pageData.getCropBoxY(pageNumber)*scaling;
//
//		//create scaling factor to use
//		AffineTransform displayScaling = new AffineTransform();
//
//		//** new x_size y_size declaration *
//		int x_size=(int) (crw+(crx-mediaX));
//		int y_size=(int) (crh+(cry-mediaY));
//
//		if (rotation == 270) {
//
//			displayScaling.rotate(-Math.PI / 2.0, x_size/ 2, y_size / 2);
//
//			double x_change = (displayScaling.getTranslateX());
//			double y_change = (displayScaling.getTranslateY());
//			displayScaling.translate((y_size - y_change), -x_change);
//			displayScaling.translate(0, y_size);
//			displayScaling.scale(1, -1);
//			displayScaling.translate(-(crx+mediaX), -(mediaH-crh-(cry-mediaY)));
//
//		} else if (rotation == 180) {
//
//			displayScaling.rotate(Math.PI, x_size / 2, y_size / 2);
//			displayScaling.translate(-(crx+mediaX),y_size+(cry+mediaY)-(mediaH-crh-(cry-mediaY)));
//			displayScaling.scale(1, -1);
//
//		} else if (rotation == 90) {
//
//			displayScaling.rotate(Math.PI / 2.0);
//			displayScaling.translate(0,(cry+mediaY)-(mediaH-crh-(cry-mediaY)));
//			displayScaling.scale(1, -1);
//
//		}else{
//			displayScaling.translate(0, y_size);
//			displayScaling.scale(1, -1);
//			displayScaling.translate(0, -(mediaH-crh-(cry-mediaY)));
//		}
//
//		displayScaling.scale(scaling,scaling);
//
//		return displayScaling;
//	}
//
//	/**
//	 * initialise panel and set size to display during updates
//	 * and update the AffineTransform to new values<br>
//	 * @param newRotation - sets display rotation to this value
//	 */
//	final public void setPageRotation(int newRotation) {
//
//		//DO NOT DO THIS!!!
//		//This code is also called to alter scaling
//		//if(newRotation==oldRotation)
//		//return;
//
//		displayRotation = newRotation;
//
//		//assume unrotated for multiple views and rotate on a page basis
//		if(displayView!=Display.SINGLE_PAGE)
//			newRotation=0;
//
//		pageUsedForTransform=pageNumber;
//		if(displayView!=Display.SINGLE_PAGE && displayView!=Display.FACING)
//            displayScaling = getScalingForImage(1,0,scaling);//(int)(pageData.getCropBoxWidth(pageNumber)*scaling),(int)(pageData.getCropBoxHeight(pageNumber)*scaling),
//		else
//            displayScaling = getScalingForImage(pageNumber,newRotation,scaling);//(int)(pageData.getCropBoxWidth(pageNumber)*scaling),(int)(pageData.getCropBoxHeight(pageNumber)*scaling),
//
//		if(newRotation == 90){
//			displayScaling.translate(insetH/scaling,insetW/scaling);
//		}else if(newRotation == 270){
//			displayScaling.translate(-insetH/scaling,-insetW/scaling);
//		}else if(newRotation == 180){
//			displayScaling.translate(-insetW/scaling,insetH/(scaling));
//		}else{
//			displayScaling.translate(insetW/scaling,-insetH/scaling);
//		}
//
//		//force redraw if screen being cached
//		pages.refreshDisplay();
//
//		/**
//		 * now apply any viewport scaling
//		 */
//		if(this.viewableArea!=null){
//
//			viewScaling=new AffineTransform();
//
//			/**workout scaling and choose larger*/
//			double dx=(double)viewableArea.width/(double)pageData.getCropBoxWidth(pageNumber);
//			double dy=(double)viewableArea.height/(double)pageData.getCropBoxHeight(pageNumber);
//			double viewScale=dx;
//			if(dy<dx)
//				viewScale=dy;
//
//			/**workout any translation*/
//			double x=viewableArea.x;//left align
//			double y=viewableArea.y+(viewableArea.height-(pageData.getCropBoxHeight(pageNumber)*viewScale));//top align
//
//			viewScaling.translate(x,y);
//			viewScaling.scale(viewScale,viewScale);
//
//		}else
//			viewScaling=null;
//	}
//
//	/**
//	 * Enables/Disables hardware acceleration of screen rendering in 1.4 (default is on)
//	 */
//	public void setHardwareAccelerationforScreen(boolean useAcceleration) {
//		this.useAcceleration = useAcceleration;
//	}
//
//	//<start-adobe>
//
//	/**return amount to scroll window by when scrolling (default is 10)*/
//	public int getScrollInterval() {
//		return scrollInterval;
//	}
//
//	/**set amount to scroll window by when scrolling*/
//	public void setScrollInterval(int scrollInterval) {
//		this.scrollInterval = scrollInterval;
//	}
//
//	/**
//	 * JPedal will now draw the screen only when fully decoded rather than on any paint
//	 * - to restore previous default behaviour (if required), call this
//	 * routine with true
//	 */
//	public void setDrawInteractively(boolean drawInteractively) {
//		this.drawInteractively = drawInteractively;
//	}
//
//	/**
//	 * returns view mode used in panel -
//	 * SINGLE_PAGE,CONTINUOUS,FACING,CONTINUOUS_FACING
//	 * (has no effect in OS versions)
//	 */
//	public int getDisplayView() {
//		return displayView;
//	}
//
//	//<end-adobe>
//
//
//	/**read current Page scaling mode used for printing*/
//	public int getPrintPageScalingMode() {
//		return pageScalingMode;
//	}
//
//	/**
//	 * set page scaling mode to use - default setting is
//	 * PAGE_SCALING_REDUCE_TO_PRINTER_MARGINS
//	 * All values start PAGE_SCALING
//	 */
//	public void setPrintPageScalingMode(int pageScalingMode) {
//		this.pageScalingMode = pageScalingMode;
//	}
//	public void setUsePDFPaperSize(boolean usePDFPaperSize) {
//		this.usePDFPaperSize = usePDFPaperSize;
//	}
//
//	public void setHighlightedImage(int[] highlightedImage) {
//			this.highlightedImage = highlightedImage;
//	}
//
//	public int[] getHighlightImage(){
//		return highlightedImage;
//	}
//
//	public boolean isImageExtractionAllowed(){
//		return ImageExtractionAllowed;
//	}
//
//	public void setImageExtractionAllowed(boolean allow){
//		ImageExtractionAllowed = allow;
//	}
//
//	public float getScaling() {
//		return scaling;
//	}
//
//	public int getInsetH() {
//		return insetH;
//	}
//
//	public int getInsetW() {
//		return insetW;
//	}
//
//	protected void setDisplayForms(boolean displayForms) {
//        this.displayForms = displayForms;
//    }
//
//	public Rectangle getCursorBoxOnScreen() {
//		return cursorBoxOnScreen;
//	}
//
//	public boolean isExtractingAsImage() {
//		return extractingAsImage;
//	}
//
//    public JScrollBar getPageFlowBar() {
//        return scroll;
//    }
//
//	public void scrollRectToVisible(Rectangle area){
//		addAView(-1, area, null);
//		super.scrollRectToVisible(area);
//	}
//
//    /**
//     * internal method used by SimpleViewer to provide preview of PDF in Viewer
//     */
//    public void setPreviewThumbnail(BufferedImage previewImage, String previewText) {
//
//        this.previewImage=previewImage;
//        this.previewText=previewText;
//    }
//}
