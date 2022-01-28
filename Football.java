
import com.golden.gamedev.Game;
import com.golden.gamedev.GameLoader;
import com.golden.gamedev.object.AnimatedSprite;
import com.golden.gamedev.object.Background;
import com.golden.gamedev.object.GameFont;
import com.golden.gamedev.object.Sprite;
import com.golden.gamedev.object.SpriteGroup;
import com.golden.gamedev.object.Timer;
import com.golden.gamedev.object.background.ImageBackground;
import com.golden.gamedev.object.collision.BasicCollisionGroup;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

public class Football extends Game{

    Background background;
    Sprite gawang_kiri,gawang_kanan;
    AnimatedSprite asPlayer;
    boolean isRun = false;
    GameFont font;
    int score = 0;
    Timer waktu;
    long startTimer = System.nanoTime();
    SpriteGroup group_bola;
    SpriteGroup group_player;
    SpriteGroup group_gawang;
    CollidePlayerWithBall collide1;
    CollideBallWithGawang collide2;
    
    @Override
    public void initResources() {
        background = new ImageBackground(getImage("Resources/background.jpg"),800,600);
        gawang_kiri = new Sprite(getImage("Resources/Gawang_kiri.png"),0,235);
        gawang_kanan = new Sprite(getImage("Resources/Gawang_kanan.png"),755,235);
        
        BufferedImage[] images_player = getImages("Resources/Player.png",2,1);
        asPlayer = new AnimatedSprite(images_player,390,270);
        
        font = fontManager.getFont(getImages("Resources/font.png",20,3),
             " !            ,.0123456789:   -? ABCDEFGHIJKLMNOPQRSTUVWXYZ ");
        
        group_bola = new SpriteGroup("BOLA");
        group_player = new SpriteGroup("PLAYER");
        group_gawang = new SpriteGroup("GAWANG");
        
        waktu = new Timer(3000); 
        group_player.add(asPlayer);
        group_gawang.add(gawang_kiri);
        group_gawang.add(gawang_kanan);
        
        collide1 = new CollidePlayerWithBall();
        collide1.setCollisionGroup(group_player,group_bola);
        
        collide2 = new CollideBallWithGawang();
        collide2.setCollisionGroup(group_bola,group_gawang);
         
        acak();
    }
    
    public void acak(){
        Sprite ball = new Sprite(getImage("Resources/red.png"),getRandom(200,600),10);
        ball.setVerticalSpeed(0.2);
        group_bola.add(ball);
        
        Sprite ball2 = new Sprite(getImage("Resources/yellow.png"),getRandom(200,600),10);
        ball2.setVerticalSpeed(0.2);
        group_bola.add(ball2);
        
        Sprite ball3 = new Sprite(getImage("Resources/blue.png"),getRandom(200,600),10);
        ball3.setVerticalSpeed(0.2);
        group_bola.add(ball3);
    }

    @Override
    public void update(long elapsedTime) {
        
        collide1.checkCollision();
        collide2.checkCollision();
        
        if(waktu.action(elapsedTime)){
            acak();
        }
        
        group_bola.update(elapsedTime);
        if (score >= 200) {

        long finishTimer = System.nanoTime();
        long timeSpent = TimeUnit.SECONDS.convert(finishTimer - startTimer, TimeUnit.NANOSECONDS);

        String timeSpentMessage;

        if (timeSpent > 60) {
        long minute = timeSpent / 60;
        long seconds = timeSpent - (minute * 60);
        timeSpentMessage = minute + " menit " + seconds + " detik";
        } else {
        timeSpentMessage = timeSpent + " detik";
        }

        JOptionPane.showMessageDialog(null, "Selamat anda menyelesaikan game dalam waktu " + timeSpentMessage, "Pesan", JOptionPane.PLAIN_MESSAGE);

        score = 0;
        startTimer = System.nanoTime();
    }
        
        if(keyDown(KeyEvent.VK_LEFT)&& keyDown(KeyEvent.VK_UP)){
            if(isRun==false){
                asPlayer.setAnimationFrame(0,0);
                asPlayer.setSpeed(-0.2, -0.2);
                asPlayer.update(elapsedTime);
                isRun = true;
            }
        }
        
        if(keyDown(KeyEvent.VK_LEFT)&& keyDown(KeyEvent.VK_DOWN)){
            if(isRun==false){
                asPlayer.setAnimationFrame(0,0);
                asPlayer.setSpeed(-0.2, -0.2);
                asPlayer.update(elapsedTime);
                isRun = true;
            }
        }
        
        if(keyDown(KeyEvent.VK_LEFT)){
            if(isRun==false){
                asPlayer.setAnimationFrame(0,0);
                asPlayer.setSpeed(-0.2, 0);
                asPlayer.update(elapsedTime);
                isRun = true;
            }
        }
        if(keyDown(KeyEvent.VK_RIGHT)){
            if(!isRun){
                asPlayer.setAnimationFrame(1,1);
                asPlayer.setSpeed(0.2,0);
                asPlayer.update(elapsedTime);
                isRun = true;
            }
        }
        if(keyDown(KeyEvent.VK_UP)){
            if(!isRun){
                //asPlayer.setAnimationFrame(1,1);
                asPlayer.setSpeed(0,-0.2);
                asPlayer.update(elapsedTime);
                isRun = true;
        }
    }
        if(keyDown(KeyEvent.VK_DOWN)){
            if(!isRun){
                //asPlayer.setAnimationFrame(1,1);
                asPlayer.setSpeed(0,0.2);
                asPlayer.update(elapsedTime);
                isRun = true;
        }
    }
    
    isRun = false;
    
    }

    @Override
    public void render(Graphics2D gd) {
       background.render(gd);
       gawang_kiri.render(gd);
       gawang_kanan.render(gd);
       asPlayer.render(gd);
       font.drawString(gd,"SCORE : " + score, 5, 5);
       group_bola.render(gd);
    }
    
    public static void main(String[] args) {
        GameLoader game = new GameLoader();
        game.setup(new Football(), new Dimension(800,600), false);
        game.start();
    }
    
    class CollidePlayerWithBall extends BasicCollisionGroup{
        
        public CollidePlayerWithBall(){
            
        }
        
        @Override
        public void collided(Sprite player, Sprite ball) {
            ball.setSpeed(-0.2, 0);
        }
        
    }
    
    class CollideBallWithGawang extends BasicCollisionGroup{
        
        public CollideBallWithGawang(){
            
        }

        @Override
        public void collided(Sprite ball, Sprite gawang) {
            ball.setActive(false);
            score+=20;
        }
    }
    
}
