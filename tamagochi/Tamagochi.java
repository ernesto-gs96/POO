import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.image.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Tamagochi extends JFrame  implements LeeRed {
static String preguntas []= { "COMO TE LLAMAS","DONDE VIVES"};
static String respuestas []= {"MI NOMBRE ES TAMAGOCHI","EN LA COMPU"};
private Canvas3D canvas3D;
private Appearance ap;
private static Texture texture;
private JPanel jp1, jp2;
private JButton bcambia,bEstado[];//ARREGLO DE JBUTTON AGREGADO
private EventHandler eh;//proporciona soporte para generar dinámicamente oyentes de eventos cuyos métodos ejecutan una declaración simple que involucra un objeto de evento entrante y un objeto de destino.
private String nombres[]={"cara1.jpg","cara2.jpg","cara3.jpg","cara4.jpg","cara5.jpg"};//NOMBRE DE IMAGENES
private String caras[]={"FELIZ","TRISTE","PENSANDO","NORMAL","ASUSTADO"};
private int turno;
private MicroChat microChat;
private Body body;
private Red r;

public Tamagochi(){
   super("Tamagochi 3D");
   turno=0;
   //setResizable(false);
   setSize(400, 500);
   GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
   canvas3D = new Canvas3D(config);
   canvas3D.setSize(300, 400);
   eh = new EventHandler();
   bcambia=new JButton("Cambiar");
   bcambia.addActionListener(eh);
   bEstado= new JButton[5];//Se crea el arreglo de botones

   for(int i = 0; i < 5; i++) {
        bEstado[i] = new JButton(String.valueOf(caras[i]));//Se impre el numero en el boton
        bEstado[i].addActionListener(eh);//Se inica el oyente de eventos
    }

   jp1=new JPanel();

	for(int i = 0; i < 5; i++) {
		jp1.add(bEstado[i]);//Se añade el arreglo al panel
	}

   add("North", jp1);
   add("Center",canvas3D);
   setup3DGraphics();
   setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
   setVisible(true);
   r=new Red(this);//Se establece conexcion con el servidor
   microChat = new MicroChat(r);
   add("South", microChat);
}
void setup3DGraphics(){
   DirectionalLight light1;
   SimpleUniverse universe = new SimpleUniverse(canvas3D);
   universe.getViewingPlatform().setNominalViewingTransform();
   body=new Body(-0.4f,0.0f,0.0f,"",true, this, "Avatar1");
   body.changeTextureCab(texture, "cara1.jpg");
   BranchGroup group= body.mybody();
   Color3f light1Color = new Color3f(1.0f, 1.0f, 0.0f);
   BoundingSphere bounds =new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
   Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
   light1 = new DirectionalLight(light1Color, light1Direction);
   light1.setInfluencingBounds(bounds);
   group.addChild(light1);
   universe.addBranchGraph(group);
}
public static void main(String[] args) { new Tamagochi(); }


class EventHandler implements ActionListener {
  public void actionPerformed(ActionEvent e) {
     Object obj=e.getSource();//Objeto para saber el origen del evento
     
     if(obj instanceof JButton){//El objetivo del operador instanceof es conocer si un objeto es de un tipo determinado.
     	JButton btn=(JButton)e.getSource();
      for(int i=0;i<=4;i++){
     	  if(btn==bEstado[i]){ turno=i; }
      }

     	body.changeTextureCab(texture, nombres[turno]);//Se realiza el cambio de acuero al numero del boton seleccionado
     	r.escribeRed(new Icono("Tamagochi", turno));
     }
  }
}
public void leeRed(Object obj){
	if(obj instanceof Icono){
        	Icono i=(Icono)obj;
                System.out.println("Recibi"+nombres[i.getTurno()]);
                body.changeTextureCab(texture, nombres[i.getTurno()]);
	}
	if(obj instanceof Mensaje){
		System.out.println("Recibi "+(Mensaje)obj);
                microChat.recibir((Mensaje)obj);
	}
}
static String findMatch(String str) {
		String result = "";
		for(int i = 0; i < preguntas.length; ++i) {
			if(preguntas[i].equalsIgnoreCase(str)) {
				result = respuestas[i];
				break;
			}
		}
		return result;
}
}
