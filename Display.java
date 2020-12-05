/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;


public class Display1 
{
private static JPanel pSpace = new JPanel(new GridLayout(1,4));
private static JLabel gen;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run() 
            {
                JFrame frame = new JFrame("Game Of Life");
                frame.setSize(900, 800);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                
                GridView v;
                v = new GridView();
                ButtonView bv = new ButtonView();
                ComboBoxView cv = new ComboBoxView();
                //GenerationCount gc = new GenerationCount();
                
                Model m = new Model();
                Controller c = new Controller(m,v, bv, cv);
                
                frame.add(v.addGridPanel(), BorderLayout.CENTER);
                JPanel pSpace = new JPanel(new GridLayout(2,4));
                //gen = new JLabel("Generation: " + m.getGeneration());
                pSpace.add(new JLabel(" "));
                pSpace.add(cv);
                pSpace.add(bv);
                pSpace.add(m.getGeneration());
                pSpace.add(new JLabel(" "));
                pSpace.add(new JLabel(" "));
                pSpace.add(new JLabel(" "));
                pSpace.add(new JLabel(" "));
           
                frame.add(pSpace, BorderLayout.SOUTH);
                frame.setVisible(true);
        }
    });
    
   
        
        
    
}

static private class ButtonView extends JPanel
{
    private JButton next;
    private JButton start;
    private JButton edit;
    private boolean editEnabled;
    
    ButtonView()
    {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        next = new JButton("Next");
        next.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        start = new JButton("Start");
        start.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        edit = new JButton("Edit");
        edit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        editEnabled = false;
        
        this.add(new JLabel("  "));
        this.add(next);
        this.add(new JLabel("  "));
        this.add(start);
        this.add(new JLabel("  "));
        this.add(edit);
    }
    
       public void addActionListenerNext(ActionListener Listener)
   {
       next.addActionListener(Listener); 
   }
   
   public void addActionListenerStart(ActionListener Listener)
   {
       start.addActionListener(Listener);
   }
   
   public void addActionListenerEdit(ActionListener Listener)
   {
       edit.addActionListener(Listener);
   }
    
    public void toggleEditEnabled() 
  {
        if(editEnabled)
            editEnabled = false;
        else
            editEnabled = true;
  }
    
     public boolean isEditEnabled() 
  {
        return editEnabled;
  }
    
}
    static private class ComboBoxView extends JPanel
{
    private JComboBox patternList;
    private JComboBox speed;
    private JComboBox zoom;
    
    private String [] patterns = {"New Game", "Clear","Block","Tub","Boat","Snake","Ship"};
    private String [] pace = {"Slow","Normal","Fast"};
    private String [] scale = {"Small","Medium","Big"};
    
    ComboBoxView()
    {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        patternList = new JComboBox(patterns);
        patternList.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        speed = new JComboBox(pace);
        speed.setCursor(new Cursor(Cursor.HAND_CURSOR));

        zoom = new JComboBox(scale);
        zoom.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        add(patternList);
        this.add(new JLabel("  "));
        add(speed);
        this.add(new JLabel("  "));
        add(zoom);
    }

    public String[] getPatterns() {
        return patterns;
    }

    public JComboBox getPatternList() {
        return patternList;
    }
    
    
    
   public void addItemListenerPatternList(ItemListener Listener)
   {
       patternList.addItemListener(Listener);
   }

   
   public void addActionListenerSpeed(ActionListener Listener)
   {
       speed.addActionListener(Listener);
   }
   
    public void addItemListenerZoom(ItemListener Listener)
   {
       zoom.addItemListener(Listener);
   }
}

   static private class Controller 
    {
    
   //insert the view class here, @Amir 
   private Model model;
   
   private GridView gridView;
   private ButtonView bView;
   private ComboBoxView cView;
   //private GenerationCount gCount;
   private Shape shapeList[][];
   private ShapeHelper shapeHelper[][];

   
   
   public void clear()
   {
       int rows= gridView.getRow();
       int cols = gridView.getCol();
       for(int i =0;i<rows;i++)
                    {
                        for(int j=0;j<cols;j++)
                        {
                           shapeList[i][j].kill();
                    
                        }
                    }
   }
   
   public Controller( Model model, GridView view, ButtonView bView, ComboBoxView cView) 
   {
        this.model = model;
        this.gridView = view;
        this.bView = bView;
        this.cView = cView;
        //this.gCount = gc;
       
        shapeList = gridView.getShape2DArray();
        shapeHelper = gridView.get2D();

        for(int i = 0; i < shapeList.length; i++)
        {
            for(int j = 0; j < shapeList[0].length; j++)
            {
               shapeList[i][j].addMouseListenerToShape(new BackgroundChanger(shapeList[i][j]));
            }
        }
        
        bView.addActionListenerEdit(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                bView.toggleEditEnabled();;
                gridView.updateEditEnabled(bView.isEditEnabled());
                
                for(int i=0; i < shapeList.length; i++)
                {
                    for(int j = 0; j < shapeList[0].length; j++)
                    {
                        shapeList[i][j].updateEditEnabled(bView.isEditEnabled());
                    }
                }
            }
            
        });
        
        bView.addActionListenerEdit(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                //bView.toggleEditEnabled();;
                //gridView.updateEditEnabled(bView.isEditEnabled());
                
                for(int i=0; i < shapeList.length; i++)
                {
                    for(int j = 0; j < shapeList[0].length; j++)
                    {
                        shapeList[i][j].updateEditEnabled(bView.isEditEnabled());
                    }
                }
              
            }
            
        });
        
        bView.addActionListenerStart(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                bView.toggleEditEnabled();;
                gridView.updateEditEnabled(bView.isEditEnabled());
                
                for(int i=0; i < shapeList.length; i++)
                {
                    model.updateGeneration();
                    for(int j = 0; j < shapeList[0].length; j++)
                    {
                        
                        shapeList[i][j].updateEditEnabled(bView.isEditEnabled());
                    }
                }
            }
            
        });
        
        bView.addActionListenerNext(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
             //shapeHelper[2][2].checkNeighbours();
             model.updateGeneration();
                for(int i=0; i < shapeHelper.length; i++)
                {
                    for(int j = 0; j < shapeHelper[0].length; j++)
                    {
                        shapeHelper[i][j].forNext();//checkNeighbours();
                        
                       // gridView.getPanelGrid().repaint();
                    }
                }
            }
            
        });
        
        bView.addActionListenerStart(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {

                   for(int i=0; i < shapeHelper.length; i++)
                {
                    for(int j = 0; j < shapeHelper[0].length; j++)
                    {
                        shapeHelper[i][j].run();//checkNeighbours();
                    }
                } 
                
            }
            
        });
        
        cView.addItemListenerPatternList(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                int rows= gridView.getRow();
                int cols = gridView.getCol();
              
                if(e.getItem().toString() == "New Game") clear();
                
                if(e.getItem().toString() == "Clear")
                {
                    clear();
                }
                else if(e.getItem().toString() == "Block")
                {
                    clear();
                    shapeList[rows/2][cols/2].toggleStatus();
                    shapeList[rows/2 +1][cols/2].toggleStatus();
                    shapeList[rows/2 +1][cols/2+1].toggleStatus();
                    shapeList[rows/2][cols/2+1].toggleStatus();
                }
             else if(e.getItem().toString() == "Tub")
             {
                clear();
                 shapeList[rows/2+1][cols/2].toggleStatus();
                    shapeList[rows/2][cols/2-1].toggleStatus();
                    shapeList[rows/2][cols/2+1].toggleStatus();
                    shapeList[rows/2-1][cols/2].toggleStatus();
             }
           else if(e.getItem().toString() == "Boat")
             {
                clear();
                 shapeList[rows/2+1][cols/2].toggleStatus();
                    shapeList[rows/2][cols/2-1].toggleStatus();
                    shapeList[rows/2][cols/2+1].toggleStatus();
                    shapeList[rows/2-1][cols/2].toggleStatus();
                    shapeList[rows/2+1][cols/2+1].toggleStatus();
             }
                 else if(e.getItem().toString() == "Snake")
             {
               clear();
                 shapeList[rows/2][cols/2].toggleStatus();
                    shapeList[rows/2][cols/2+1].toggleStatus();
                    shapeList[rows/2+1][cols/2+1].toggleStatus();
                    shapeList[rows/2][cols/2-2].toggleStatus();
                    shapeList[rows/2+1][cols/2-2].toggleStatus();;
                    shapeList[rows/2+1][cols/2-1].toggleStatus();
             }
                 else if(e.getItem().toString() == "Ship")
             {
                clear();
                 shapeList[rows/2+1][cols/2].toggleStatus();
                 shapeList[rows/2][cols/2-1].toggleStatus();
                 shapeList[rows/2][cols/2+1].toggleStatus();
                 shapeList[rows/2-1][cols/2].toggleStatus();
                 shapeList[rows/2+1][cols/2+1].toggleStatus();
                 shapeList[rows/2-1][cols/2-1].toggleStatus();
                    
             }
         
            }
            
        });
        
        for(int i=0; i < shapeList.length; i++)
        {
            for(int j=0; j < shapeList[0].length; j++)
            {
                shapeList[i][j].addMenuKeyListenerSave(new SaveShape(shapeList[i][j], model));
                
            }
        }
        
        cView.addItemListenerZoom(new ItemListener()
        {
             @Override
            public void itemStateChanged(ItemEvent e) 
            {
                if("Small".equals(e.getItem().toString()))
                {
                   gridView.setCol(75);
                   gridView.setRow(75);
                   gridView = new GridView(); 
                   gridView.getPanelGrid().repaint();
                  
                }
                
                else if ("Medium".equals(e.getItem().toString()))
                {
                   gridView.setCol(50);
                   gridView.setRow(50);
                   gridView = new GridView();  
                   gridView.getPanelGrid().repaint();
                }
                
                else if ("Big".equals(e.getItem().toString()))
                {
                  gridView.setCol(25);
                  gridView.setRow(25);
                  gridView = new GridView();  
                  gridView.getPanelGrid().repaint();
                  
                  
                }
            }
            
        });
    
}

   public class BackgroundChanger extends MouseAdapter
   {
       Shape shape;
       
       BackgroundChanger(Shape shape)
       {
           this.shape = shape;
       }
       
       @Override public void mouseClicked(MouseEvent e)
       {
           if(shape.isEditEnabled())
            shape.toggleStatus();
       }
   }
   
   public class SaveShape implements MenuKeyListener
   {
       Shape sh;
       Model md;
       
       SaveShape(Shape s, Model m)
       {
           sh = s;
           md = m;
       }

        @Override
        public void menuKeyTyped(MenuKeyEvent e) {}

        @Override
        public void menuKeyPressed(MenuKeyEvent e)
        {
            try
                       {
                           File file = new File("C:\\Users\\ASUS\\Desktop\\save.txt");
                           BufferedWriter bf = new BufferedWriter(new FileWriter(file));
                           
                         //FileOutputStream saveFile = new FileOutputStream(file);  
                         //ObjectOutputStream save = new ObjectOutputStream(saveFile);
                         bf.write(sh.getrPos());
                         bf.write(sh.getcPos());
                         if(sh.isStatus())
                             bf.write(1);
                         else 
                             bf.write(0);
                         bf.write(sh.getNeighbours());
                         bf.write(sh.getBackgroundColor().toString());
                         bf.write(md.getGeneration().getGeneration());
                         bf.close();
                         
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }
        }

        @Override
        public void menuKeyReleased(MenuKeyEvent e) {}

        private int JOptionPane(Object object, String sAve) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
       
   }
    
}
  
static private class GenerationCount extends JComponent
{
    private int generation;
    GenerationCount()
    {
        generation = 0;
        //add(new JLabel("Generation: " + generation));
    }
    
    @Override public void paintComponent(Graphics g)
    {
        super.paintComponents(g);
        //Graphics2D g2 = (Graphics2D) g;
        //Font f = new Font();
        JLabel gen = new JLabel("Generation: " + generation);
        g.drawString(gen.getText(), 0, 20);
        
    }
    
    public int getGeneration() 
   {
       return generation;
   }
   
   public void updateGeneration() 
   {
       generation+=1;
       repaint();
   }
}
    
static private class GridView
{
    private JPanel panelGrid;
    GridLayout grid;
    
    //private GenerationCount gnCount;
    private Shape[][] shapeList; //notice this
    private ShapeHelper[][] x;
    
    private boolean editEnabled;
    private int row = 50;
    private int col = 50;
    
    GridView()
    {
        shapeList = new Shape[row][col];
        x = new ShapeHelper[row][col];
        for(int i=0; i<row; i++)
        {
            for(int j=0; j<col; j++)
            {
                shapeList[i][j] = new Shape(shapeList, i, j);
                x[i][j] = new ShapeHelper(shapeList[i][j], shapeList, row,col);
            }
        }

        grid = new GridLayout(row,col);//won't change, 
        panelGrid = new JPanel(grid);
        //gnCount = new GenerationCount();
        
    }
    
   public JPanel addGridPanel() //allegdly not for me 
   {
       
       panelGrid.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
       
       for(int i = 0; i < row; i++)
       {
           for(int j = 0; j < col; j++)
           {
               if(i==0)
               {
                   if(j==0)
                       shapeList[i][j].setBorder(BorderFactory.createLineBorder(Color.white.darker()));
                   else 
                       shapeList[i][j].setBorder(BorderFactory.createMatteBorder(1, 0, 1 , 1, Color.white.darker()));
               }
               else
               {
                   if(j==0)
                       shapeList[i][j].setBorder(BorderFactory.createMatteBorder(0,1,1,1,Color.white.darker()));
                   else 
                       shapeList[i][j].setBorder(BorderFactory.createMatteBorder(0,0,1,1,Color.white.darker()));
               }
               
               panelGrid.add(shapeList[i][j]);
           }
       }
       return panelGrid;
   }
   
   public void addCell(int r, int c)
   {
       if(shapeList[r][c].isLive()) {System.out.println("Cell already populated.");}
       if(!shapeList[r][c].isLive()) { Shape shape = new Shape(shapeList,r, c); shapeList[r][c].toggleStatus();}
           
           
   }
   
  public void deleteCell(int r, int c)
  {
      if(shapeList[r][c].isLive())
      {
          shapeList[r][c].toggleStatus();
      }

  }

  public Shape[][] getShape2DArray()
  {
      return this.shapeList;
  }
  
   public ShapeHelper[][] get2D()
           
  {
      return this.x;
  }
  

  public boolean isEditEnabled() 
  {
        return editEnabled;
  }

  public void updateEditEnabled(boolean enable) 
  {
        editEnabled = enable;
  }

    public JPanel getPanelGrid() {
        return panelGrid;
    }

    int getCol() {
        return col;
    }

    int getRow() {
        return row;
    }
    public void setRow(int row)
    {
        this.row = row;
    }

    public void setCol(int col) 
    {
        this.col = col;
    }

}

static private class Model {
    private boolean status;// = false; //for the clear button
    //private  Shape[][] grid = new Shape[90][70]; //This is the grid size filled with threads that the view will make a Jbutton for each
    private GenerationCount gen;
    Model()
    {
        status = false;
        gen = new GenerationCount();
    }
    
    public void changeGrid(String s)
    {
        /*if(s.equals("Small"))  grid = new Shape[120][100];
        if(s.equals("Medium"))  grid = new Shape[90][70];
        if(s.equals("Big"))  grid = new Shape[70][50];*/
        
    }

    public GenerationCount getGeneration() 
    {
        return gen;
    }

    public void updateGeneration() 
    {
        gen.updateGeneration();
    }
    
    
    
}

static private class Shape extends JPanel 
{
    private boolean status;
    private boolean editEnabled;
    private int Neighbours =0;
    private Shape[][] shapeList; 
    private int rPos; 
    private int cPos;
    
    private JPopupMenu popUp;
    
    Shape(Shape[][] shapeList, int rPos , int cPos)
    {
        this.shapeList = shapeList;
        this.rPos = rPos;
        this.cPos = cPos;
        setBackground(Color.gray.darker());

        editEnabled = false;
        status = false;
        
        popUp = new JPopupMenu();
        popUp.add(new JMenuItem("Save", new ImageIcon("C:\\Users\\ASUS\\Desktop\\saveIcon2.png")));
        popUp.add(new JMenuItem("Open", new ImageIcon("C:\\Users\\ASUS\\Desktop\\openFile.png")));
        setComponentPopupMenu(popUp);
       
    }
    
    public void checkNeighbour() 
    {
        Neighbours =0;
        try{
           synchronized(shapeList[this.getrPos()][this.getcPos()-1])
           {   
               //wait(1000);
               if(shapeList[this.getrPos()][this.getcPos()-1].isLive()) Neighbours++;
           }//notify(); 

          synchronized(shapeList[this.getrPos()][this.getcPos()+1])
          {
               //wait(1000);
              if(shapeList[this.getrPos()][this.getcPos()+1].isLive()) Neighbours++;
          }//notify();
           
          }  catch(ArrayIndexOutOfBoundsException exception) {}

       try{
           synchronized(shapeList[this.getrPos()+1][this.getcPos()])
           {
              // wait(1000);
               if(shapeList[this.getrPos()+1][this.getcPos()].isLive()) Neighbours++;
           } //notify();
           synchronized(shapeList[this.getrPos()+1][this.getcPos()-1])
           {
              // wait(1000);
               if(shapeList[this.getrPos()+1][this.getcPos()-1].isLive()) Neighbours++;
           }// notify(); 
           synchronized(shapeList[this.getrPos()+1][this.getcPos()+1])
           {
              // wait(1000);
               if(shapeList[this.getrPos()+1][this.getcPos()+1].isLive()) Neighbours++;
           }// notify(); 
          }  catch(ArrayIndexOutOfBoundsException exception) {}
       
       
       try{
           synchronized(shapeList[this.getrPos()-1][this.getcPos()])
           {
               //wait(1000);
            if(shapeList[this.getrPos()-1][this.getcPos()].isLive()) Neighbours++;
           } //notify();
           synchronized(shapeList[this.getrPos()-1][this.getcPos()-1])
           {
              // wait(1000);
            if(shapeList[this.getrPos()-1][this.getcPos()-1].isLive()) Neighbours++;
           } //notify();
           synchronized(shapeList[this.getrPos()-1][this.getcPos()+1])
           {
              // wait(1000);
             if(shapeList[this.getrPos()-1][this.getcPos()+1].isLive()) Neighbours++;          
           } //notify();
          }  
       catch(ArrayIndexOutOfBoundsException exception) {}
       
       Timer timer = new Timer(3000, new SpeedSet(this, Neighbours));
       /*if(Neighbours == 0 || Neighbours == 1) this.kill(); //Starvation  
       if(Neighbours == 3) this.rise(); //Alive          
       if(Neighbours > 3) this.kill(); //Overpopulation  */
    }
    
    
    public int getrPos() 
    {
        return rPos;
    }
    
     public int getcPos() 
    {
        return cPos;
    }
     
    public void kill(){status = false; setBackground(Color.gray.darker());}
    public void rise(){status = true; setBackground(Color.green.darker());}
    
    public void toggleStatus()//true means alive, false means dead
    {
        if(status) {status = false; setBackground(Color.gray.darker());}
        else {status = true; setBackground(Color.green.darker());}       
    }
    
    public boolean isLive()//true means alive, false means dead
    {
        return status;
    }
    
    public void addMouseListenerToShape(MouseListener listener)
    {
        this.addMouseListener(listener);
    }
    
    public void updateEditEnabled(boolean enable) 
  {
        editEnabled = enable;
  }
    
    public boolean isEditEnabled() 
  {
        return editEnabled;
  }
    
    public void addMenuKeyListenerSave(MenuKeyListener listener)
    {
        popUp.addMenuKeyListener(listener);
    }

    public Shape[][] getShapeList() 
    {
        return shapeList;
    }

    public boolean isStatus() 
    {
        return status;
    }

    public int getNeighbours() 
    {
        return Neighbours;
    }
    
    public Color getBackgroundColor()
    {
        return getBackground();
    }
    
    
    public class SpeedSet implements ActionListener
    {
        Shape sh;
        int neighbours;
        
        SpeedSet(Shape sh, int n)
        {
            this.sh = sh;
            neighbours = n;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(Neighbours == 0 || Neighbours == 1) sh.kill(); //Starvation  
            if(Neighbours == 3) sh.rise(); //Alive          
            if(Neighbours > 3) sh.kill(); //Overpopulation  
        }
        
    }
    
    
    
}

static private class ShapeHelper extends Thread
{
    private Shape shape;
    private Shape[][] shapeList;
    private int rPos; 
    private int cPos;
    private int Neighbours;
    
    ShapeHelper(Shape shape, Shape[][] shapeList, int rPos, int cPos) {
        this.shape = shape; 
        this.shapeList = shapeList;
        this.rPos = rPos;
        this.cPos = cPos;   
    }
    
    public void forNext() //throws InterruptedException
    {
        Neighbours =0;
        try{
           synchronized(shapeList[shape.getrPos()][shape.getcPos()-1])
           {   
               //currentThread().sleep(123);
               if(shapeList[shape.getrPos()][shape.getcPos()-1].isLive()) Neighbours++;
           }//notify(); 

          synchronized(shapeList[shape.getrPos()][shape.getcPos()+1])
          {
               //currentThread().sleep(123);
              if(shapeList[shape.getrPos()][shape.getcPos()+1].isLive()) Neighbours++;
          }//notify();
           
          }  catch(ArrayIndexOutOfBoundsException exception) {}

       try{
           synchronized(shapeList[shape.getrPos()+1][shape.getcPos()])
           {
               if(shapeList[shape.getrPos()+1][shape.getcPos()].isLive()) Neighbours++;
           } //notify();
           synchronized(shapeList[shape.getrPos()+1][shape.getcPos()-1])
           {
               if(shapeList[shape.getrPos()+1][shape.getcPos()-1].isLive()) Neighbours++;
           } //notify(); 
           synchronized(shapeList[shape.getrPos()+1][shape.getcPos()+1])
           {
               if(shapeList[shape.getrPos()+1][shape.getcPos()+1].isLive()) Neighbours++;
           } //notify(); 
          }  catch(ArrayIndexOutOfBoundsException exception) {}
       
       
       try{
           synchronized(shapeList[shape.getrPos()-1][shape.getcPos()])
           {
            if(shapeList[shape.getrPos()-1][shape.getcPos()].isLive()) Neighbours++;
           } //notify();
           synchronized(shapeList[shape.getrPos()-1][shape.getcPos()-1])
           {
            if(shapeList[shape.getrPos()-1][shape.getcPos()-1].isLive()) Neighbours++;
           } //notify();
           synchronized(shapeList[shape.getrPos()-1][shape.getcPos()+1])
           {
             if(shapeList[shape.getrPos()-1][shape.getcPos()+1].isLive()) Neighbours++;          
           } //notify();
          }  
       catch(ArrayIndexOutOfBoundsException exception) {}
       
       if(Neighbours == 0 || Neighbours == 1) shape.kill(); //Starvation  
       if(Neighbours == 3) shape.rise(); //Alive          
       if(Neighbours > 3) shape.kill(); //Overpopulation     
    }
    
    
    @Override
    public void run()
    {
        //start();
        while(true)
        {
            for(int i =0; i<shapeList.length; i++)
            {
                for(int j=0; j<shapeList[0].length; j++)
                {
                    shapeList[i][j].checkNeighbour();
                }
            }
            if(!isAlive()){start();}
            
        }
        
        
          
       
    }

}


}


