import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

class PaintFrame extends Frame implements ItemListener,ActionListener,MouseListener, MouseMotionListener
{
 CheckboxMenuItem sLin,sRec,sElp,sFrh;
 MenuItem cBlk,cBlu,cRed,cGrn,cYlo;
 MenuItem mNew,mOpn,mSav,mSas,mExit;

 String shape="";
 Color drawColor=Color.black;

 BufferedImage bmg;
 Graphics2D g2d;
 
 int ox,oy,px,py,wd,ht;

 File fname;

 PaintFrame()
 {
  super("Paintbrush - Untitled");

  setSize(700,500);
  setLocation(150,100);

  sLin = new CheckboxMenuItem("Line");
  sRec = new CheckboxMenuItem("Rectangle");
  sElp = new CheckboxMenuItem("Ellipse");
  sFrh = new CheckboxMenuItem("Free Hand");

  mNew  = new MenuItem("New");
  mOpn  = new MenuItem("Open");
  mSav  = new MenuItem("Save");
  mSas  = new MenuItem("Save As");
  mExit = new MenuItem("Exit");

  cBlk = new MenuItem("Black");
  cBlk.setEnabled(false);
  cBlu = new MenuItem("Blue");
  cRed = new MenuItem("Red");
  cGrn = new MenuItem("Green");
  cYlo = new MenuItem("Yellow");

  Menu mFile,mShape,mColor;
  
  mFile=new Menu("File");

  mFile.add(mNew);
  mFile.add(mOpn);
  mFile.add(mSav);
  mFile.add(mSas);
  mFile.addSeparator();
  mFile.add(mExit);


  mShape=new Menu("Shape");

  mShape.add(sLin);
  mShape.add(sRec);
  mShape.add(sElp);
  mShape.add(sFrh);


  mColor=new Menu("Color");

  mColor.add(cBlk);
  mColor.add(cBlu);
  mColor.add(cRed);
  mColor.add(cGrn);
  mColor.add(cYlo);

  
  MenuBar mb = new MenuBar();
  mb.add(mFile);
  mb.add(mShape);
  mb.add(mColor);

  setMenuBar(mb);

  bmg=new BufferedImage(2000,2000,BufferedImage.TYPE_INT_RGB);
  g2d=bmg.createGraphics();
  
  g2d.setColor(Color.white);
  g2d.fillRect(0,0,2000,2000);
  
  
  sLin.addItemListener(this);
  sRec.addItemListener(this);
  sElp.addItemListener(this); 
  sFrh.addItemListener(this);

  mNew.addActionListener(this);
  mOpn.addActionListener(this);
  mSav.addActionListener(this);
  mSas.addActionListener(this);
  mExit.addActionListener(this);  

  cBlk.addActionListener(this);
  cBlu.addActionListener(this);
  cRed.addActionListener(this);
  cGrn.addActionListener(this);
  cYlo.addActionListener(this);


  addMouseListener(this);
  addMouseMotionListener(this);
  addWindowListener(new PaintAdapter());
 }

 public void paint(Graphics g)
 {
  Image img=(Image)bmg;
  g.drawImage(img,0,0,this);
 }

 public void saveAs()
 {
  FileDialog fd = new FileDialog(this,"Save Image As",FileDialog.SAVE);
  fd.setVisible(true);

  String dn,fn;
  
  dn=fd.getDirectory();
  
  if(dn==null) return;

  fn=fd.getFile();

  setTitle("Paintbrush - "+fn);
   
  fname=new File(dn+fn);
  
  try
  {
   ImageIO.write(bmg,"jpg",fname);
  }
  catch(IOException e)
  {
   JOptionPane.showMessageDialog(null,"i/o alert - "+e.getMessage());
  }
 } 

 public void itemStateChanged(ItemEvent ie)
 {
  sLin.setState(false);
  sRec.setState(false);
  sElp.setState(false);
  sFrh.setState(false);

  if(ie.getSource()==sLin)
  {
   sLin.setState(true);
   shape="line";
  }
  else if(ie.getSource()==sRec)
  {
   sRec.setState(true);
   shape="rectangle";
  }
  else if(ie.getSource()==sElp)
  {
   sElp.setState(true);
   shape="ellipse";
  }
  else 
  {
   sFrh.setState(true);
   shape="freehand";
  }
  
 }

 public void actionPerformed(ActionEvent ae)
 {

  if(ae.getSource()==mExit)
   System.exit(0);

  if(ae.getSource()==mNew)
  {
   g2d.setColor(Color.white);
   g2d.fillRect(0,0,2000,2000);
   
   setTitle("Paintbrush - Untitled");
   repaint();
  }
  else
  if(ae.getSource()==mOpn)
  {
   FileDialog fd = new FileDialog(this,"Select Image to Open",FileDialog.LOAD);
   fd.setVisible(true);
   
   String dn = fd.getDirectory();
   
   if(dn==null) return;

   String fn = fd.getFile();
   
   fname = new File(dn+fn);
   setTitle("Paintbrush - "+fn);

   Image img = Toolkit.getDefaultToolkit().getImage(dn+fn);

   MediaTracker mt = new MediaTracker(this);

   mt.addImage(img,0);
   
   try
   {
    mt.waitForID(0);
   }
   catch(InterruptedException e){}

   g2d.setColor(Color.white);
   g2d.fillRect(0,0,2000,2000);

   g2d.drawImage(img,0,0,this);
   
   repaint();
  }
  else
  if(ae.getSource()==mSav)
  {
   if(fname==null)
    saveAs();
   else
   {

    try
    {
     ImageIO.write(bmg,"jpg",fname);
    }
    catch(IOException e)
    {
     JOptionPane.showMessageDialog(null,"i/o alert - "+e.getMessage());
    }

   }

  }
  else
  if(ae.getSource()==mSas)
  {
   saveAs();
  }

  else
  {
   cBlk.setEnabled(true);
   cBlu.setEnabled(true);
   cRed.setEnabled(true);
   cGrn.setEnabled(true);
   cYlo.setEnabled(true);

   if(ae.getSource()==cBlk) 
   {
    cBlk.setEnabled(false);
    drawColor = Color.black;
   }
   else
   if(ae.getSource()==cBlu) 
   {
    cBlu.setEnabled(false);
    drawColor = Color.blue;
   }
   else
   if(ae.getSource()==cRed) 
   {
    cRed.setEnabled(false);
    drawColor = Color.red;
   }
   else
   if(ae.getSource()==cGrn) 
   {
    cGrn.setEnabled(false);
    drawColor = Color.green;
   }
   else
   if(ae.getSource()==cYlo) 
   {
    cYlo.setEnabled(false);
    drawColor = Color.yellow;
   }

  } 
  
 }

 public void mouseEntered(MouseEvent me){}
 public void mouseClicked(MouseEvent me){}
 public void mouseExited (MouseEvent me){}
 public void mouseMoved  (MouseEvent me){}

 public void mousePressed(MouseEvent me)
 {
  ox=px=me.getX();
  oy=py=me.getY();
  wd=ht=0;

  setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
 }

 public void mouseDragged(MouseEvent me)
 {
  Graphics g=getGraphics();
  g.setColor(drawColor);

  if(shape=="freehand")
   g.setPaintMode();
  else
   g.setXORMode(Color.white);

  switch(shape)
  {
   case "line"      : g.drawLine(ox,oy,px,py);
                      break;

   case "rectangle" : if(wd!=0||ht!=0)
                       g.drawRect(px,py,wd,ht);
                       break;

   case "ellipse"   : if(wd!=0||ht!=0)
                       g.drawOval(px,py,wd,ht);
                       break;
                     
   
  }

  px=me.getX();
  py=me.getY();

  switch(shape)
  {
   case "line"      : g.drawLine(ox,oy,px,py);
                      break;
  
   case "rectangle" : wd=px-ox;
                      ht=py-oy;

                      if(wd>0)
                       px=ox;
                      else
                       wd=-wd;
 
                      if(ht>0)
                       py=oy;
                      else
                       ht=-ht;
                     
                      g.drawRect(px,py,wd,ht);
                      break;

   case "ellipse"   : wd=px-ox;
                      ht=py-oy;

                      if(wd>0)
                       px=ox;
                      else
                       wd=-wd;
 
                      if(ht>0)
                       py=oy;
                      else
                       ht=-ht;
                     
                      g.drawOval(px,py,wd,ht);
                      break;
 
   case "freehand"  : g.drawLine(ox,oy,px,py);
                      g2d.setColor(drawColor);
                      g2d.drawLine(ox,oy,px,py);
                      ox=px;
                      oy=py;
                      
  }

 }

 public void mouseReleased(MouseEvent me)
 {
  Graphics g=getGraphics();
  g.setColor(drawColor);
  g.setPaintMode();
  g2d.setColor(drawColor);
  
  switch(shape)
  {
   case "line"      : g.drawLine(ox,oy,px,py);
                      g2d.drawLine(ox,oy,px,py);
                      break;

   case "rectangle" : if(wd!=0&&ht!=0)
                      {
                       g.drawRect(px,py,wd,ht);
                       g2d.drawRect(px,py,wd,ht);
                      }
                      break;

   case "ellipse"   : if(wd!=0&&ht!=0)
                      {
                       g.drawOval(px,py,wd,ht);
                       g2d.drawOval(px,py,wd,ht);
                      }
                      break;   
 
  }

  setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
 }
 


}

class PaintAdapter extends WindowAdapter
{
 public void windowClosing(WindowEvent we)
 {
  System.exit(0);
 }
}

public class PaintBrush
{
 public static void main(String arg[])
 {
  PaintFrame pf=new PaintFrame();
  pf.setVisible(true);
 }
}
