package Discord.Minesweeper;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class App extends ListenerAdapter {
    public static void main(String[] args) throws Exception {
        JDA jda = new JDABuilder(AccountType.BOT).setToken(Ref.botToken).buildBlocking();
        jda.addEventListener(new Commands());
        jda.getPresence().setGame(Game.playing("Minesweeper."));
        
        // System tray icon
        Image image = Toolkit.getDefaultToolkit().getImage(".\\src\\main\\java\\Resources\\Incardnate_Icon.png");
        SystemTray tray = SystemTray.getSystemTray();
        PopupMenu popup = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        TrayIcon trayIcon = new TrayIcon(image, "Incardnate", popup);
        
        exitItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		System.exit(0);
        	}
        });
        popup.add(exitItem);
        tray.add(trayIcon);
    }
}
