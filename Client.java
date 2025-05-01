import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
// import java.awt.event.*;
public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
        PrintWriter out;
        private JLabel heading=new JLabel("Client Area");
        private JTextArea message=new JTextArea();
        private JTextField messageInput=new JTextField();
        private Font font=new Font("Roboto",Font.PLAIN,20);

    public Client(){
        try{
            System.out.println("sending request to server");
            socket=new Socket("127.0.0.1",7776);
            System.out.println("connection done");
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
       out=new PrintWriter(socket.getOutputStream());
    createGUI();
    handleEvents();

        startReading();
        // startWriting();
        }
        catch(Exception e){

        }
    }
    private void  createGUI(){
        this.setTitle("Client Message[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        heading.setFont(font);
        message.setFont(font);
        messageInput.setFont(font);
        
        

     
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        message.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        this.setLayout(new BorderLayout());

        this.add(heading,BorderLayout.NORTH);
        
        JScrollPane jScrollPane=new JScrollPane(message);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true);

    }
    private void handleEvents(){
        try{
        messageInput.addKeyListener(new KeyListener(){

            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {}

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String contentToSend = messageInput.getText();
                    message.append("Me: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                }
            }
            

        });
    }
    catch(Exception e){

    }
    }
    public void startReading(){
        Runnable r1=()->{
            System.out.println("Reader started...");
            try{
            while(true){
               
               String msg=br.readLine();
               if(msg.equals("exit")){
                System.out.println("server terminated the chat");
                JOptionPane.showMessageDialog(this,"server Terminated the chat");
                message.setEnabled(false);
                socket.close();
                break;
               }
            //    System.out.println("server : "+msg);
            message.append("server : "+msg+"\n");
            }
        }
            
        
                catch(Exception e){
                    // e.printStackTrace();
                    System.out.println("connection is closed");

                }
            

        };
        new Thread(r1).start();

    }
    public void startWriting(){

        Runnable r2=()->{
            System.out.println("Writer started...");
            try{
            while(true && !socket.isClosed()){
                
                    BufferedReader brl=new BufferedReader(new InputStreamReader(System.in));
                    String content=brl.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }

                }
            }
            
            catch(Exception e){
                // e.printStackTrace();
                System.out.println("connection is closed");
            }
        

        };
        new Thread(r2).start();
    }
    
    public static void main(String[] args) {
        System.out.println("This is client");
        new Client();
    }
}
