// Jeremy Vercillo
// University of Notre Dame
// 7/5/11
// Jewel-matching bot for Starcraft 2 custom map "Starjeweled"

// improvements
	// go for 4/5 combo first
	// work on all resolutions
	// stop key
	// send units

import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.math.*;

public class Starjeweled {			
	public static void printPixelRGB(int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		System.out.println(pixel + " RGB: " + red + ", " + green + ", " + blue);
	}
	
	public static BufferedImage getScreen() throws AWTException, IOException {
//		BufferedImage screen = ImageIO.read(new File("jewels.bmp"));
		BufferedImage screen = new Robot().createScreenCapture(new Rectangle(742, 67, 384, 384));
		return screen;
	}
	public static int[][] convertToArray(BufferedImage screen) {
		int width = screen.getWidth();
		int height = screen.getHeight();
		System.out.println(width + " " + height);
		int[][] pixels = new int[width][height];		
		for(int w = 0; w < width; w++) {
			for(int h = 0; h < height; h++) {
				pixels[w][h] = screen.getRGB(w, h);
			}
		} 
		return pixels;
	}
	public static char[][] readBoard(int[][] pixels) {
		char[][] board = new char[8][8];
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				int pixelvalue = 0;
				for(int xpix = 0; xpix < 16; xpix++) {
					for(int ypix = 0; ypix < 16; ypix++) {
						pixelvalue += pixels[x*48 + xpix + 16][y*48 + ypix + 16];
					}
				}
				pixelvalue /= (16*16);
				if(pixelvalue > 6600000 && pixelvalue < 6700000) board[x][y] = 'Y';
				else if(pixelvalue > 6000000 && pixelvalue < 6150000) board[x][y] = 'G';
				else if(pixelvalue > -7250000 && pixelvalue < -7100000) board[x][y] = 'K';
				else if(pixelvalue > -7800000 && pixelvalue < -7650000) board[x][y] = 'R';
				else if(pixelvalue > 7000000 && pixelvalue < 7200000) board[x][y] = 'P';
				else if(pixelvalue > 4650000 && pixelvalue < 4800000) board[x][y] = 'B';
				// System.out.println(pixelvalue);
				// System.out.print(board[x][y] + " ");
			}
			System.out.println();
		}
		return board;
	}
	public static int[][] determineMove(char[][] board) {
		int[][] tilestoswap = new int[2][2];
		tilestoswap[0][0] = -1;
		tilestoswap[0][1] = -1;
		tilestoswap[1][0] = -1;
		tilestoswap[1][1] = -1;
		for(int y = 7; y >= 0; y--) {
			for(int x = 7; x >= 0; x--) {
				char myColor = board[x][y];
				if(x > 0) {
					char westNeighbor = board[x-1][y]; // west neighbor
					if(westNeighbor == myColor) {
						System.out.println("Match west " + myColor + westNeighbor);
						try {
							if(board[x-2][y-1] == myColor) {
								tilestoswap[0][0] = x-2;
								tilestoswap[0][1] = y;
								tilestoswap[1][0] = x-2;
								tilestoswap[1][1] = y-1;
								return tilestoswap;
							}
							if(board[x-2][y+1] == myColor) {
								tilestoswap[0][0] = x-2;
								tilestoswap[0][1] = y;
								tilestoswap[1][0] = x-2;
								tilestoswap[1][1] = y+1;
								return tilestoswap;
							}
							if(board[x-3][y] == myColor) {
								tilestoswap[0][0] = x-2;
								tilestoswap[0][1] = y;
								tilestoswap[1][0] = x-3;
								tilestoswap[1][1] = y;
								return tilestoswap;
							}
						}
						catch(Exception e) {
							continue;
						} // index out of bounds
					}
					if(x > 1) {
						char westSkipNeighbor = board[x-2][y]; // 2 over to the west
						if(westSkipNeighbor == myColor) {
							System.out.println("Match west skip " + westSkipNeighbor + myColor);
							try {
								if(board[x-1][y-1] == myColor) {
									tilestoswap[0][0] = x-1;
									tilestoswap[0][1] = y;
									tilestoswap[1][0] = x-1;
									tilestoswap[1][1] = y-1;
									return tilestoswap;
								}
								if(board[x-1][y+1] == myColor) {
									tilestoswap[0][0] = x-1;
									tilestoswap[0][1] = y;
									tilestoswap[1][0] = x-1;
									tilestoswap[1][1] = y+1;	
									return tilestoswap;
								}
							}
							catch(Exception e) {
								continue;
							}
						}
						
					}
				}
				if(y > 0) {
					char northNeighbor = board[x][y-1]; // north neighbor
					if(northNeighbor == myColor) {
						System.out.println("Match north " + myColor + northNeighbor);
						try {
							if(board[x-1][y-2] == myColor) {
								tilestoswap[0][0] = x;
								tilestoswap[0][1] = y-2;
								tilestoswap[1][0] = x-1;
								tilestoswap[1][1] = y-2;
								return tilestoswap;
							}
							if(board[x+1][y-2] == myColor) {
								tilestoswap[0][0] = x;
								tilestoswap[0][1] = y-2;
								tilestoswap[1][0] = x+1;
								tilestoswap[1][1] = y-2;
								return tilestoswap;								
							}
							if(board[x][y-3] == myColor) {
								tilestoswap[0][0] = x;
								tilestoswap[0][1] = y-2;
								tilestoswap[1][0] = x;
								tilestoswap[1][1] = y-3;
								return tilestoswap;								
							}
						}
						catch(Exception e) {
							continue;
						} // index out of bounds
					}
					if(y > 1) {
						char northSkipNeighbor = board[x][y-2]; // 2 over to the north
						if(northSkipNeighbor == myColor) {
							System.out.println("Match north skip " + northSkipNeighbor + myColor);
							try {
								if(board[x+1][y-1] == myColor) {
									tilestoswap[0][0] = x;
									tilestoswap[0][1] = y-1;
									tilestoswap[1][0] = x+1;
									tilestoswap[1][1] = y-1;
									return tilestoswap;
								}
								if(board[x-1][y-1] == myColor) {
									tilestoswap[0][0] = x;
									tilestoswap[0][1] = y-1;
									tilestoswap[1][0] = x-1;
									tilestoswap[1][1] = y-1;	
									return tilestoswap;
								}
							}
							catch(Exception e) {
								continue;
							}
						}
					}
				}
				if(x < 7) {
					char eastNeighbor = board[x+1][y]; // east neighbor
					if(eastNeighbor == myColor) {
						System.out.println("Match east " + myColor + eastNeighbor);
						try {
							if(board[x+2][y-1] == myColor) {
								tilestoswap[0][0] = x+2;
								tilestoswap[0][1] = y;
								tilestoswap[1][0] = x+2;
								tilestoswap[1][1] = y-1;
								return tilestoswap;
							}
							if(board[x+2][y+1] == myColor) {
								tilestoswap[0][0] = x+2;
								tilestoswap[0][1] = y;
								tilestoswap[1][0] = x+2;
								tilestoswap[1][1] = y+1;
								return tilestoswap;
							}
							if(board[x+3][y] == myColor) {
								tilestoswap[0][0] = x+2;
								tilestoswap[0][1] = y;
								tilestoswap[1][0] = x+3;
								tilestoswap[1][1] = y;
								return tilestoswap;
							}
						}
						catch(Exception e) {
							continue;
						} // index out of bounds
					}
					if(x < 6) {
						char eastSkipNeighbor = board[x+2][y]; // 2 over to the east
						if(eastSkipNeighbor == myColor) {
							System.out.println("Match east skip " + eastSkipNeighbor + myColor);
							try {
								if(board[x+1][y-1] == myColor) {
									tilestoswap[0][0] = x+1;
									tilestoswap[0][1] = y;
									tilestoswap[1][0] = x+1;
									tilestoswap[1][1] = y-1;
									return tilestoswap;
								}
								if(board[x+1][y+1] == myColor) {
									tilestoswap[0][0] = x+1;
									tilestoswap[0][1] = y;
									tilestoswap[1][0] = x+1;
									tilestoswap[1][1] = y+1;	
									return tilestoswap;
								}
							}
							catch(Exception e) {
								continue;
							}
						}					
					}
				}
				if(y < 7) {
					char southNeighbor = board[x][y+1]; // south neighbor
					if(southNeighbor == myColor) {
						System.out.println("Match south " + myColor + southNeighbor);
						try {
							if(board[x-1][y+2] == myColor) {
								tilestoswap[0][0] = x;
								tilestoswap[0][1] = y+2;
								tilestoswap[1][0] = x-1;
								tilestoswap[1][1] = y+2;
								return tilestoswap;
							}
							if(board[x+1][y+2] == myColor) {
								tilestoswap[0][0] = x;
								tilestoswap[0][1] = y+2;
								tilestoswap[1][0] = x+1;
								tilestoswap[1][1] = y+2;
								return tilestoswap;								
							}
							if(board[x][y+3] == myColor) {
								tilestoswap[0][0] = x;
								tilestoswap[0][1] = y+2;
								tilestoswap[1][0] = x;
								tilestoswap[1][1] = y+3;
								return tilestoswap;								
							}
						}
						catch(Exception e) {
							continue;
						} // index out of bounds
					}
					if(y < 6) {
						char southSkipNeighbor = board[x][y+2]; // 2 over to the south
						if(southSkipNeighbor == myColor) {
							System.out.println("Match south skip " + southSkipNeighbor + myColor);
							try {
								if(board[x+1][y+1] == myColor) {
									tilestoswap[0][0] = x;
									tilestoswap[0][1] = y+1;
									tilestoswap[1][0] = x+1;
									tilestoswap[1][1] = y+1;
									return tilestoswap;
								}
								if(board[x-1][y+1] == myColor) {
									tilestoswap[0][0] = x;
									tilestoswap[0][1] = y+1;
									tilestoswap[1][0] = x-1;
									tilestoswap[1][1] = y+1;	
									return tilestoswap;
								}
							}
							catch(Exception e) {
								continue;
							}
						}					
					}
				}
			}
		}
		return tilestoswap;
	}
	public static void simulateClick(int xpos, int ypos) throws AWTException {
		xpos += 742;
		ypos += 67;
		System.out.println("Click at (" + xpos + ", " + ypos + ")");
		Robot clickbot = new Robot();
		clickbot.mouseMove(xpos, ypos);
		clickbot.mousePress(InputEvent.BUTTON1_MASK);
		clickbot.mouseRelease(InputEvent.BUTTON1_MASK);
		clickbot.mouseMove(0, 400);
	}
	
	public static void main(String[] args) throws AWTException, IOException, InterruptedException {
		Thread.currentThread().sleep(5000); // Delay to get back into SC2
		while(true) {
			long timein = System.currentTimeMillis();
		
			BufferedImage screen = getScreen(); // captures screen of jewels
			int[][] pixels = convertToArray(screen); // converts BufferedImage into pixel array
			char[][] board = readBoard(pixels); // converts pixels into array of chars (jewels)
			int[][] tilestoswap = determineMove(board); // finds first move
			try { 
				System.out.println("(" + (tilestoswap[0][0]+1) + ", " + (tilestoswap[0][1]+1) + ", " + board[tilestoswap[0][0]][tilestoswap[0][1]] + ") with (" + (tilestoswap[1][0]+1) + ", " + (tilestoswap[1][1]+1) + ", " + board[tilestoswap[1][0]][tilestoswap[1][1]] + ")");
			}
			catch(Exception e) {
				Robot keybot = new Robot();
				keybot.keyPress(KeyEvent.VK_Z);
				keybot.keyRelease(KeyEvent.VK_Z);
				System.out.println("Resetting Board.");
				Thread.currentThread().sleep(3000); // wait for board to reset
			}
				
			simulateClick(tilestoswap[0][0]*48 + 24, tilestoswap[0][1]*48 + 24);
			Thread.currentThread().sleep(100);
			simulateClick(tilestoswap[1][0]*48 + 24, tilestoswap[1][1]*48 + 24);
			Thread.currentThread().sleep(100);
			
			long timeout = System.currentTimeMillis();
			System.out.println("Move took " + (timeout-timein) + " milliseconds.");		
		}
	}
}
