import java.util.ArrayList;
import java.util.Scanner;

public class Ggomuri {
	static char[][] board = new char[10][10];	
	static int horseNum;
	static ArrayList<Horse> horseList = new ArrayList();

	public static void main(String args[]){
		char name = 'A';
		Scanner scanner = new Scanner(System.in);
		System.out.print("말의 수 : ");
		System.out.println();
		horseNum = scanner.nextInt();

		initBoard();
		startGame();


	}

	static void startGame() {
		// TODO Auto-generated method stub
		for(Horse horse : horseList){
			horse.start();
		}

	}
	static Horse checkCrash(int x,int y, char me){
		if(board[x][y]=='_') return null;
		else{
			for(Horse horse : horseList){
				if(!horse.isDead && horse.x==x && horse.y==y && horse.name != me) return horse;
			}
		}
		return null;

	}

	static void initBoard(){//board initialize
		int x,y,name=0;
		for(int i = 0;i<10;i++){
			for(int j=0;j<10;j++){
				board[i][j] = '_';
			}
		}
		for(int i=0;i<horseNum;i++){
			while(true){
				x = (int)(Math.random()*10);
				y = (int)(Math.random()*10);
				if(board[x][y]=='_'){
					Horse newHorse = new Horse(x,y,(char)('A'+name));
					name++;
					board[x][y] = newHorse.name;
					horseList.add(newHorse);
					break;
				}
			}
		}

	}

	static void printBoard() {//board print
		// TODO Auto-generated method stub
		for(int i = 0;i<10;i++){
			for(int j=0;j<10;j++){
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	static void updateBoard() {
		// TODO Auto-generated method stub
		for(int i = 0;i<10;i++){
			for(int j=0;j<10;j++){
				board[i][j] = '_';
			}
		}
		for(Horse horse : horseList){
			if(!horse.isDead) board[horse.x][horse.y] = horse.name;
		}
		printBoard();
		finishCheck();


	}

	public static void finishCheck() {
		// TODO Auto-generated method stub
		int deadNum=0;
		for(Horse horse: horseList){
			if(horse.isDead) deadNum++;
		}
		if(deadNum>=(horseNum-1)){
			for(Horse horse : horseList){
				System.out.println(horse.name + "(" + horse.level + ")'s Kill Score : " + horse.KillScore);
			}
			System.exit(0);}

	}
}

class Horse extends Thread{
	public static final int UP = 0,DOWN = 1,LEFT = 2,RIGHT = 3;
	int level,KillScore=0;
	long speed;
	int x,y;
	char name;
	boolean isDead  =false;


	public Horse(int x, int y, char name) {
		super();
		this.x = x;
		this.y = y;
		this.name = name;
		level = (int) (Math.random()*500);
		speed = (long) (Math.random()*200);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(!isDead){
			synchronized(Ggomuri.class){
				if(isDead) break;
				int direction = (int) (Math.random()*4);
				if(direction == UP){
					if(y!=0) y--;
					else continue;
				}
				else if(direction ==DOWN){
					if(y!=9) y++;
					else continue;
				}
				else if(direction ==LEFT){
					if(x!=0) x--;
					else continue;
				}
				else if(direction ==RIGHT){
					if(x!=9) x++;
					else continue;
				}

				Horse crashHorse = Ggomuri.checkCrash(x, y, this.name);
				if(crashHorse !=null){
					if(crashHorse.level>this.level){
						crashHorse.KillScore++;
						this.isDead = true;
						System.out.println(crashHorse.name + " win, killscore : " + crashHorse.KillScore);
					}
					else if(crashHorse.level<this.level){
						this.KillScore++;
						crashHorse.isDead = true;
						System.out.println(this.name + " win, killscore : " + this.KillScore);
					}
					else{
						System.out.println("Draw");
						//this.KillScore++;
						//crashHorse.KillScore++;
						this.isDead = true;
						crashHorse.isDead = true;
					}
				}
				Ggomuri.updateBoard();
			}
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		Ggomuri.updateBoard();

	}


}


