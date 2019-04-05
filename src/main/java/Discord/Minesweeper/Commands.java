package discord.minesweeper;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
	private static int countAdjacentMines(String[][] grid, int x, int y) {
		int ret = 0;
		
		if (isMine(grid, x - 1, y - 1)) ret++;
		if (isMine(grid, x - 1, y)) ret++;
		if (isMine(grid, x - 1, y + 1)) ret++;
		if (isMine(grid, x, y - 1)) ret++;
		if (isMine(grid, x, y + 1)) ret++;
		if (isMine(grid, x + 1, y - 1)) ret++;
		if (isMine(grid, x + 1, y)) ret++;
		if (isMine(grid, x + 1, y + 1)) ret++;
		
		return ret;
	}
	
	private static boolean isMine(String[][] grid, int x, int y) {
		if (x < 0) return false;
		if (x >= grid.length) return false;
		if (y < 0) return false;
		if (y >= grid[0].length) return false;
		
		if (grid[x][y] == null) return false;
		if (grid[x][y].equals(":bomb:")) return true;
		return false;
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent e) {
    	// Object definition
    	User user = e.getAuthor();
    	MessageChannel channel = e.getChannel();
    	Message msg = e.getMessage();
		String[] cmd = msg.getContentRaw().split(" ");
		
    	// Commands
    	if (user.isBot()) return;
    	
    	// Generates a basic game of Minesweeper.
	    if (cmd[0].equalsIgnoreCase(Ref.prefix + "mines")) {
	    	String[][] grid;
	    	int mines;
	    	
	    	if (cmd.length == 1) {
		    	grid = new String[10][10];
		    	mines = 25;
	    	} else if (cmd.length == 4) {
		    	try {
		    		grid = new String[Integer.parseInt(cmd[1])][Integer.parseInt(cmd[2])];
		    		mines = Integer.parseInt(cmd[3]);
		    	} catch (NumberFormatException er) {
		    		channel.sendMessage("**Error:** Invalid format.").queue();
		    		return;
		    	}
	    	} else {
	    		channel.sendMessage("**Error:** Invalid format.").queue();
	    		return;
	    	}
	    	
	    	final int size = grid.length * grid[0].length;
	    	
	    	// Error checking.
	    	if (mines > size) {
	    		channel.sendMessage("**Error:** Can't have more mines than spaces.").queue();
	    		return;
	    	}
	    	
	    	if (grid.length > 15 || grid[0].length > 15) {
	    		channel.sendMessage("**Error:** Grid too big.").queue();
	    		return;
	    	}
	    	
	    	if (grid.length <= 0 || grid[0].length <= 0) {
	    		channel.sendMessage("**Error:** Grid too small.").queue();
	    		return;	
	    	}
	    	
	    	// Sets the location of mines.
	    	for (int i = 0; i < mines; i++) {
	    		int loc = (int) (Math.random() * (size));
	    		int x = loc % grid.length;
	    		int y = loc / grid.length;
	    		
	    		if (grid[x][y] == null) grid[x][y] = ":bomb:";
	    		else i--;
	    	}
	    	
	    	// Sets the correct number for every remaining grid space.
	    	for (int x = 0; x < grid.length; x++) {
	    		for (int y = 0; y < grid[0].length; y++) {
	    			if (grid[x][y] != null) continue;
	    			int adj = countAdjacentMines(grid, x, y);
	    			
	    			switch (adj) {
    				case 0: grid[x][y] = ":zero:";
							break;
    				case 1: grid[x][y] = ":one:";
							break;
    				case 2: grid[x][y] = ":two:";
							break;
    				case 3: grid[x][y] = ":three:";
							break;
    				case 4: grid[x][y] = ":four:";
							break;
    				case 5: grid[x][y] = ":five:";
							break;
    				case 6: grid[x][y] = ":six:";
							break;
    				case 7: grid[x][y] = ":seven:";
							break;
    				case 8: grid[x][y] = ":eight:";
							break;
					default: 
							channel.sendMessage("**Error:** Grid generated incorrectly.").queue();
							return;
	    			}
	    		}
	    	}
	    	
	    	// Sends the board as a message.
	    	StringBuilder board = new StringBuilder(String.format("Board size: %dx%d. Mines: %d\n", grid.length, grid[0].length, mines));
	    	for (int x = 0; x < grid.length; x++) {
	    		for (int y = 0; y < grid[0].length; y++) {
	    			board.append(String.format("||%s||", grid[x][y]));
	    		}
	    		board.append("\n");
	    	}
	    	channel.sendMessage(board).queue();
    	}
	}
}